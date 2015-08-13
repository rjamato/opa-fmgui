/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.intel.stl.fecdriver.impl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.fecdriver.IConnection;
import com.intel.stl.fecdriver.IStatement;
import com.intel.stl.fecdriver.messages.adapter.OobPacket;
import com.intel.stl.fecdriver.messages.adapter.RmppMad;
import com.intel.stl.fecdriver.messages.command.FVCommand;

public class STLStatement implements IStatement<FVCommand<?, ?>> {
    private static Logger log = LoggerFactory.getLogger(STLStatement.class);

    protected static final int DEFAULT_TIMEOUT = 30000; // in milliseconds

    private int timeout = DEFAULT_TIMEOUT;

    private final AtomicBoolean isClosed = new AtomicBoolean(false);

    private STLConnection conn = null;

    public STLStatement(STLConnection conn) {
        this.conn = conn;
    }

    @Override
    public boolean isClosed() {
        return isClosed.get();
    }

    @Override
    public void close() {
        isClosed.set(true);
        conn.cancelCommandsFor(this);
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(int milliseconds) {
        timeout = milliseconds;
    }

    @Override
    public boolean execute(FVCommand<?, ?> cmd) throws Exception {
        if (isClosed.get()) {
            cmd.getResponse().setError(
                    new RuntimeException("Statement is closed"));
            return false;
        }
        log.info("execute cmd " + cmd + " with argument " + cmd.getInput());
        cmd.setSubmittingStatement(this);
        submit(cmd);
        log.info("waiting for response for cmd " + cmd);
        // When the FE is up but the SM is down, the response might take very
        // long. We set a timeout value that can be extended if necessary.
        try {
            cmd.getResponse().get(getTimeout(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException toe) {
            log.info("timeout waiting for response for cmd " + cmd);
            // Tell the STLAdapter about this
            conn.fireOnTimeout();
            // This timeout will be reset by FailoverManager. If it times out
            // again, caller will get it
            cmd.getResponse().get(getTimeout(), TimeUnit.MILLISECONDS);
        }
        return true;
    }

    @Override
    public boolean submit(FVCommand<?, ?> cmd) throws IOException {
        if (isClosed.get()) {
            cmd.getResponse().setError(
                    new RuntimeException("Statement is closed"));
            return false;
        }
        log.info("submit cmd " + cmd + " with argument " + cmd.getInput());
        OobPacket sendPacket = createSendPacket(cmd);
        long id = sendPacket.getRmppMad().getCommonMad().getTransactionId();
        conn.submitCmd(id, sendPacket, cmd);
        return true;
    }

    protected OobPacket createSendPacket(FVCommand<?, ?> cmd) {
        OobPacket sendPacket = new OobPacket();
        sendPacket.build(true);
        RmppMad rmppMad = cmd.prepareMad();
        sendPacket.setRmppMad(rmppMad);
        sendPacket.fillPayloadSize();
        sendPacket.setExpireTime(System.currentTimeMillis() + timeout * 1000);
        long id = rmppMad.getCommonMad().getTransactionId();
        cmd.setMessageID(id);
        return sendPacket;
    }

    @Override
    public IConnection getConnection() {
        return conn;
    }

}
