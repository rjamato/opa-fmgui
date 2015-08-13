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
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: SecureType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3.2.2  2015/08/12 15:22:10  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.3.2.1  2015/05/06 19:28:53  jijunwan
 *  Archive Log:    fixed initialization issue found by FindBugs
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/05/01 21:44:31  jijunwan
 *  Archive Log:    fixed initialization issue found by FindBug
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/08 15:17:47  fernande
 *  Archive Log:    Changes to allow for failover to work when the current (initial) FE is not available.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/04/06 21:16:51  fernande
 *  Archive Log:    Improving the handling of connection errors.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/04/03 16:14:03  fernande
 *  Archive Log:    Added an intermediate layer between a connection and the FE which represents the type of connection. Now an FEChannel is a regular channel and a FESecureChannel is a channel that uses SSL.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.impl;

import static com.intel.stl.common.STLMessages.STL61008_SSL_HANDSHAKE_COMPLETE;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.ICertsAssistant;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.SubnetDescription;

public class FESecureChannel extends BaseChannel {
    private static Logger log = LoggerFactory.getLogger(FESecureChannel.class);

    public static final String[] CIPHER_LIST =
            { "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256" };

    public static final long TIME_OUT = 30000; // 30 sec

    private SSLEngine sslEngine = null;

    private int netBufferSize;

    private ByteBuffer myNetData;

    private ByteBuffer peerNetData;

    private int appBufferSize = 4;

    private ByteBuffer peerAppData;

    private ByteBuffer handshakeAppData;

    private ByteBuffer myAppData = ByteBuffer.allocate(appBufferSize);

    private long expired;

    private HandshakeStatus handshakeStatus = HandshakeStatus.NOT_HANDSHAKING;

    private final ICertsAssistant certsAssistant;

    protected FESecureChannel(STLConnection conn, ICertsAssistant assistant) {
        super(conn);
        this.certsAssistant = assistant;
    }

    @Override
    protected void initializeChannel(SubnetDescription subnet) throws Exception {
        try {
            setupForHandshake(subnet);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof SSLHandshakeException) {
                SSLHandshakeException sslhe = (SSLHandshakeException) e;
                throw sslhe;
            } else {
                SSLHandshakeException sslhe =
                        new SSLHandshakeException(
                                StringUtils.getErrorMessage(e));
                sslhe.initCause(e);
                throw sslhe;
            }
        }
    }

    @Override
    public void beginHandshake() throws Exception {
        try {
            // Start handshake process
            startHandshakeProcess();
            super.beginHandshake();
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof SSLHandshakeException) {
                SSLHandshakeException sslhe = (SSLHandshakeException) e;
                throw sslhe;
            } else {
                SSLHandshakeException sslhe =
                        new SSLHandshakeException(
                                StringUtils.getErrorMessage(e));
                sslhe.initCause(e);
                throw sslhe;
            }
        }
    }

    @Override
    protected void processHandshake() throws Exception {
        // Process handshaking message
        switch (handshakeStatus) {
            case NEED_UNWRAP:
                // Receive handshaking data from peer
                int ret = socketChannel.read(peerNetData);
                if (ret < 0) {
                    // The channel has reached end-of-stream
                }

                if (handshakeAppData.remaining() < appBufferSize) {
                    handshakeAppData =
                            resizeBuffer(handshakeAppData, appBufferSize);
                }
                // Process incoming handshaking data
                peerNetData.flip();
                SSLEngineResult res =
                        sslEngine.unwrap(peerNetData, handshakeAppData);
                peerNetData.compact();
                handshakeStatus = res.getHandshakeStatus();
                // Check status
                switch (res.getStatus()) {
                    case OK:
                        // Handle OK status
                        break;

                    case BUFFER_UNDERFLOW:
                        // Resize buffer if needed.
                        netBufferSize =
                                sslEngine.getSession().getPacketBufferSize();
                        if (netBufferSize > peerNetData.capacity()) {
                            peerNetData =
                                    resizeBuffer(peerNetData, netBufferSize);
                        }
                        break;

                    case BUFFER_OVERFLOW:
                        // Reset the application buffer size.
                        appBufferSize =
                                sslEngine.getSession()
                                        .getApplicationBufferSize();
                        break;

                    default: // CLOSED:
                        throw new IOException("Received" + res.getStatus()
                                + "during initial handshaking");
                }
                break;

            case NEED_WRAP:
                // Empty the local network packet buffer.
                myNetData.clear();

                // Generate handshaking data
                res = sslEngine.wrap(myAppData, myNetData);
                handshakeStatus = res.getHandshakeStatus();

                switch (res.getStatus()) {
                    case OK:
                        myNetData.flip();

                        // Send the handshaking data to peer
                        while (myNetData.hasRemaining()) {
                            socketChannel.write(myNetData);
                        }
                        break;

                    default: // BUFFER_OVERFLOW/BUFFER_UNDERFLOW/CLOSED:
                        throw new IOException("Received" + res.getStatus()
                                + "during initial handshaking");
                }
                break;

            case NEED_TASK:
                handshakeStatus = doTasks();
                break;

            default:
                break;
        }
        if (System.currentTimeMillis() > expired) {
            SSLHandshakeException sslhe =
                    new SSLHandshakeException("Handshake timed out!");
            throw sslhe;
        }
        if (handshakeStatus == SSLEngineResult.HandshakeStatus.FINISHED
                || handshakeStatus == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            // Handshake complete
            log.error(STL61008_SSL_HANDSHAKE_COMPLETE
                    .getDescription(handshakeStatus));
            completeHandshake();
        }
    }

    @Override
    protected void handshakeComplete() {
        // At this point, the SSL handshake is complete, we should now perform
        // the sign on processing using a FEChannel, which has the sign on
        // protocol implementation. We could extend from FEChannel to have that
        // logic available here.

        connection.setConnected(true);
    }

    @Override
    public int readBuffer(ByteBuffer dst) throws IOException {
        if (handshakeStatus != HandshakeStatus.FINISHED) {
            throw new IllegalStateException();
        }

        int pos = dst.position();

        if (peerAppData == null) {
            appBufferSize = sslEngine.getSession().getApplicationBufferSize();
            peerAppData = ByteBuffer.allocate(appBufferSize);
        } else {
            // read from peerAppData if we have left over data in it
            if (peerAppData.hasRemaining()) {
                while (peerAppData.hasRemaining() && dst.remaining() > 0) {
                    dst.put(peerAppData.get());
                }
            }
            if (!dst.hasRemaining()) {
                log.debug("Read " + (dst.position() - pos)
                        + " from peerAppData");
                return dst.position() - pos;
            }
            if (!peerAppData.hasRemaining()) {
                peerAppData.clear();
            }
        }

        // need to read data from socket
        int len = 0;
        if ((len = socketChannel.read(peerNetData)) == -1) {
            try {
                sslEngine.closeInbound(); // probably throws exception
                return -1;
            } catch (SSLException se) {
                // This condition happens when the remote host unexpectedly
                // closes the socket.
                ClosedChannelException cce = new ClosedChannelException();
                cce.initCause(se);
                throw cce;
            }
        }

        log.debug("Read " + len + " from socket ");
        SSLEngineResult result = null;
        do {
            if (peerAppData.remaining() < appBufferSize) {
                peerAppData =
                        resizeBuffer(peerAppData, peerAppData.capacity() * 2);
            }
            peerNetData.flip();
            log.debug("pre-unwrap " + peerAppData.position() + " "
                    + peerAppData.capacity());
            result = sslEngine.unwrap(peerNetData, peerAppData);
            log.debug("Unwrap " + peerAppData.position() + " into peerAppData "
                    + result.getStatus());
            peerNetData.compact();

            /*
             * Could check here for a renegotation, but we're only doing a
             * simple read/write, and won't have enough state transitions to do
             * a complete handshake, so ignore that possibility.
             */
            switch (result.getStatus()) {
                case BUFFER_OVERFLOW:
                    appBufferSize =
                            sslEngine.getSession().getApplicationBufferSize();
                    break;

                case BUFFER_UNDERFLOW:
                    // Resize buffer if needed.
                    netBufferSize =
                            sslEngine.getSession().getPacketBufferSize();
                    if (netBufferSize > peerNetData.capacity()) {
                        peerNetData = resizeBuffer(peerNetData, netBufferSize);

                        break; // break, next read will support larger buffer.
                    }
                case OK:
                    if (result.getHandshakeStatus() == HandshakeStatus.NEED_TASK) {
                        doTasks();
                    }
                    break;

                default:
                    throw new IOException("sslEngine error during data read: "
                            + result.getStatus());
            }
        } while ((peerNetData.position() != 0)
                && result.getStatus() != Status.BUFFER_UNDERFLOW);

        peerAppData.flip();
        while (peerAppData.hasRemaining() && dst.remaining() > 0) {
            dst.put(peerAppData.get());
        }
        log.debug("Read " + (dst.position() - pos) + " from peerAppData");

        return (dst.position() - pos);
    }

    @Override
    public long writeBuffers(ByteBuffer[] appBuffers) throws IOException {
        int retValue = 0;

        if (myNetData.hasRemaining() && !tryFlush(myNetData)) {
            return retValue;
        }

        /*
         * The data buffer is empty, we can reuse the entire buffer.
         */
        myNetData.clear();

        SSLEngineResult result = sslEngine.wrap(appBuffers, myNetData);
        retValue = result.bytesConsumed();

        myNetData.flip();

        switch (result.getStatus()) {

            case OK:
                if (result.getHandshakeStatus() == HandshakeStatus.NEED_TASK) {
                    doTasks();
                }
                break;

            default:
                throw new IOException("sslEngine error during data write: "
                        + result.getStatus());
        }

        /*
         * Try to flush the data, regardless of whether or not it's been
         * selected. Odds of a write buffer being full is less than a read
         * buffer being empty.
         */
        if (myNetData.hasRemaining()) {
            tryFlush(myNetData);
        }

        return retValue;
    }

    private void setupForHandshake(SubnetDescription subnet) throws Exception {
        // init SSLEngine
        sslEngine = certsAssistant.getSSLEngine(subnet);
        netBufferSize = sslEngine.getSession().getPacketBufferSize();
        myNetData = ByteBuffer.allocate(netBufferSize);
        peerNetData = ByteBuffer.allocate(netBufferSize);
        peerNetData.position(0);
        peerNetData.limit(0);

        // Create byte buffers to use for holding handshake data
        appBufferSize = sslEngine.getSession().getApplicationBufferSize();
        myAppData = ByteBuffer.allocate(appBufferSize);
        handshakeAppData = ByteBuffer.allocate(appBufferSize);
    }

    private void startHandshakeProcess() throws Exception {
        sslEngine.setEnabledCipherSuites(CIPHER_LIST);
        sslEngine.beginHandshake();
        handshakeStatus = sslEngine.getHandshakeStatus();
        expired = System.currentTimeMillis() + TIME_OUT;
    }

    private boolean tryFlush(ByteBuffer bb) throws IOException {
        socketChannel.write(bb);
        return !bb.hasRemaining();
    }

    private ByteBuffer resizeBuffer(ByteBuffer buffer, int newSize) {
        ByteBuffer bb = ByteBuffer.allocate(newSize);
        buffer.flip();
        bb.put(buffer);
        return bb;
    }

    /*
     * Do all the outstanding handshake tasks in the current Thread.
     */
    private HandshakeStatus doTasks() {
        Runnable runnable;

        /*
         * We could run this in a separate thread, but do in the current for
         * now.
         */
        while ((runnable = sslEngine.getDelegatedTask()) != null) {
            runnable.run();
        }

        return sslEngine.getHandshakeStatus();
    }

}
