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
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.fecdriver.IChannel;
import com.intel.stl.fecdriver.messages.adapter.NetHeader;
import com.intel.stl.fecdriver.messages.adapter.NetPacket;
import com.intel.stl.fecdriver.messages.adapter.OobPacket;

public abstract class BaseChannel implements IChannel {
    private static Logger log = LoggerFactory.getLogger(BaseChannel.class);

    private static boolean DEBUG = false;

    private NetHeader netHeader = null;

    private ByteBuffer inBuffer = null;

    protected int inRemaining;

    private ByteBuffer[] outBuffers = null;

    protected long outRemaining;

    protected SocketChannel socketChannel;

    private boolean handshakeInProgress;

    protected final STLConnection connection;

    protected BaseChannel(STLConnection connection) {
        this.connection = connection;
        handshakeInProgress = true;
    }

    @Override
    public void initialize(SubnetDescription subnet) throws Exception {
        socketChannel = connection.getSocketChannel();
        initializeChannel(subnet);
    }

    @Override
    public void beginHandshake() throws Exception {
        handshakeInProgress = true;
        processHandshake();
    }

    @Override
    public void completeHandshake() {
        handshakeInProgress = false;
        handshakeComplete();
    }

    @Override
    public boolean isHandshakeInProgress() {
        return handshakeInProgress;
    }

    @Override
    public void read() throws Exception {
        if (handshakeInProgress) {
            processHandshake();
            return;
        }
        connection.check();
        log.debug("Read from " + socketChannel + " inRemaining=" + inRemaining);
        if (inRemaining == 0) {
            if (netHeader == null) {
                netHeader = new NetHeader();
                netHeader.build(true);
            }
            inBuffer = netHeader.getByteBuffer();
            if (inBuffer.remaining() == 0) {
                inBuffer.clear();
            }
            int len = readBuffer(inBuffer);
            log.debug("Read NetHeader: len = " + len + " isValid = "
                    + netHeader.isValid());
            if (inBuffer.remaining() > 0) {
                return; // keep reading until we get the full netHeader
            }
            if (netHeader.isValid()) {
                inRemaining = netHeader.getMsgLength() - netHeader.getLength();
                inBuffer = ByteBuffer.allocate(inRemaining);
                inBuffer.clear();
                len = readBuffer(inBuffer);
                inRemaining -= len;
                log.debug("Read " + len + " remaining " + inRemaining);
            } else {
                netHeader.dump("", System.out);
                throw new IOException("Illegal Data");
            }
        } else {
            int len = readBuffer(inBuffer);
            inRemaining -= len;
            log.debug("Read " + len + " remaining " + inRemaining);
        }
        if (inRemaining == 0) {
            connection.processResponse(inBuffer);
        }
    }

    @Override
    public void write() throws Exception {
        if (handshakeInProgress) {
            processHandshake();
            return;
        }
        connection.check();
        if (outRemaining == 0) {
            if (connection.arePacketsPending()) {
                log.debug("Write to " + socketChannel);
                OobPacket packet = connection.getNextPacket();

                NetPacket netPacket = new NetPacket();
                netPacket.build(true);
                netPacket.setData(packet);
                outBuffers = netPacket.getByteBuffers();
                for (ByteBuffer buffer : outBuffers) {
                    buffer.clear();
                    outRemaining += buffer.capacity();
                }

                if (DEBUG) {
                    netPacket.dump("", System.out);
                }
                long len = writeBuffers(outBuffers);
                outRemaining -= len;
                log.debug("Write " + len + " remaining " + outRemaining);
            }
        } else {
            long len = writeBuffers(outBuffers);
            outRemaining -= len;
            log.debug("Write " + len + " remaining " + outRemaining);
        }
    }

    /**
     * <i>Description:</i> initializes the channel for handshake processing. The
     * STLConnection should be ready for processing at this point, including a
     * valid SocketChannel.
     * 
     * @throws Exception
     */
    protected abstract void initializeChannel(SubnetDescription subnet)
            throws Exception;

    /**
     * <i>Description:</i> process a handshake request following the handshake
     * protocol; when the protocol is completed, this method should call
     * completeHandshake() to mark the end of the process.
     * 
     * @throws Exception
     */
    protected abstract void processHandshake() throws Exception;

    /**
     * <i>Description:</i> completes the handshake process. This method is
     * invoked after completeHandshake().
     * 
     */
    protected abstract void handshakeComplete();

    protected abstract int readBuffer(ByteBuffer appBuffer) throws IOException;

    protected abstract long writeBuffers(ByteBuffer[] appBuffers)
            throws IOException;

}
