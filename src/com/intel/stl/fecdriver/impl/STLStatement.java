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

/*******************************************************************************
 * I N T E L C O R P O R A T I O N
 * 
 * Functional Group: Fabric Viewer Application
 * 
 * File Name: STLStatement.java
 * 
 * Archive Source: $Source$
 * 
 * Archive Log: $Log$
 * Archive Log: Revision 1.17  2015/08/17 18:49:07  jijunwan
 * Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 * Archive Log: - change backend files' headers
 * Archive Log:
 * Archive Log: Revision 1.16  2015/06/10 20:39:37  fernande
 * Archive Log: PR 129034 Support secure FE. Removed println statements and leftovers from debugging.
 * Archive Log:
 * Archive Log: Revision 1.15  2015/06/05 19:10:18  jijunwan
 * Archive Log: PR 129096 - Some old files have no copyright text
 * Archive Log: - added Intel copyright text
 * Archive Log:
 * 
 * Overview:
 * 
 * @author: jijunwan
 * 
 ******************************************************************************/

package com.intel.stl.fecdriver.impl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.fecdriver.ICommand;
import com.intel.stl.fecdriver.IResponse;
import com.intel.stl.fecdriver.IStatement;
import com.intel.stl.fecdriver.MultipleResponseCommand;
import com.intel.stl.fecdriver.SingleResponseCommand;
import com.intel.stl.fecdriver.messages.adapter.OobPacket;
import com.intel.stl.fecdriver.messages.adapter.RmppMad;

public class STLStatement implements IStatement {
    private static Logger log = LoggerFactory.getLogger(STLStatement.class);

    private static boolean DEBUG = false;

    protected static final int DEFAULT_TIMEOUT = 30000; // in milliseconds

    private int timeout = DEFAULT_TIMEOUT;

    private final AtomicBoolean isClosed = new AtomicBoolean(false);

    private final STLConnection conn;

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
    public <F, E extends IResponse<F>> List<F> execute(
            MultipleResponseCommand<F, E> cmd) throws Exception {
        List<F> result = null;
        submit(cmd);
        try {
            result = cmd.getResults(getTimeout(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException toe) {
            log.info("timeout waiting for response for cmd " + cmd);
            // Tell the STLAdapter about this
            conn.fireOnTimeout();
            // This timeout will be reset by FailoverManager. If it times out
            // again, caller will get it
            result = cmd.getResults(getTimeout(), TimeUnit.MILLISECONDS);
        }
        debugResponse(cmd);
        return result;
    }

    @Override
    public <F, E extends IResponse<F>> F execute(SingleResponseCommand<F, E> cmd)
            throws Exception {
        F result = null;
        submit(cmd);
        try {
            result = cmd.getResult(getTimeout(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException toe) {
            log.info("timeout waiting for response for cmd " + cmd);
            // Tell the STLAdapter about this
            conn.fireOnTimeout();
            // This timeout will be reset by FailoverManager. If it times out
            // again, caller will get it
            result = cmd.getResult(getTimeout(), TimeUnit.MILLISECONDS);
        }
        debugResponse(cmd);
        return result;
    }

    @Override
    public <E extends IResponse<F>, F> void submit(ICommand<E, F> cmd)
            throws IOException {
        if (isClosed.get()) {
            RuntimeException rte = new RuntimeException("Statement is closed");
            cmd.getResponse().setError(rte);
            throw rte;
        }
        // log.info("submit cmd " + cmd + " with argument " + cmd.getInput());
        OobPacket sendPacket = createSendPacket(cmd);
        long id = sendPacket.getRmppMad().getCommonMad().getTransactionId();
        cmd.setPacket(sendPacket);
        cmd.setStatement(this);
        conn.submitCmd(id, sendPacket, cmd);
    }

    protected OobPacket createSendPacket(ICommand<?, ?> cmd) {
        OobPacket sendPacket = new OobPacket();
        sendPacket.build(true);
        RmppMad rmppMad = cmd.prepareMad();
        sendPacket.setRmppMad(rmppMad);
        sendPacket.fillPayloadSize();
        sendPacket.setExpireTime(System.currentTimeMillis() + timeout * 1000);
        return sendPacket;
    }

    @Override
    public SubnetDescription getSubnetDescription() {
        return conn.getConnectionDescription();
    }

    private <E extends IResponse<F>, F> void debugResponse(ICommand<E, F> cmd) {
        E response = cmd.getResponse();
        List<F> results;
        try {
            results = response.get();
            if (DEBUG && results != null) {
                for (int i = 0; i < results.size(); i++) {
                    System.out.println(i + " " + results.get(i));
                }
            }
        } catch (Exception e) {
        }
    }

}
