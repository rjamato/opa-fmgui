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
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.ISecurityHandler;
import com.intel.stl.api.subnet.SubnetDescription;

public class FEChannel extends BaseChannel {
    private static Logger log = LoggerFactory.getLogger(FEChannel.class);

    private String user;

    private char[] password;

    protected final ISecurityHandler securityHandler;

    protected FEChannel(STLConnection conn, ISecurityHandler securityHandler) {
        super(conn);
        this.securityHandler = securityHandler;
    }

    @Override
    protected void initializeChannel(SubnetDescription subnet) throws Exception {
        user = securityHandler.getUser();
        password = securityHandler.getPassword();
    }

    @Override
    protected void processHandshake() throws Exception {
        // At this point we should login to the FE
        // However, this feature is not yet supported
        log.info("Using user '" + user + "' and password "
                + (password == null ? "''" : "'********'"));
        completeHandshake();
    }

    @Override
    protected void handshakeComplete() {
        connection.setConnected(true);
        password = null;
    }

    @Override
    protected int readBuffer(ByteBuffer appBuffer) throws IOException {
        int len = socketChannel.read(appBuffer);
        if (len == -1) {
            ClosedChannelException cce = new ClosedChannelException();
            throw cce;
        }
        return len;
    }

    @Override
    protected long writeBuffers(ByteBuffer[] appBuffers) throws IOException {
        return socketChannel.write(appBuffers);
    }

}
