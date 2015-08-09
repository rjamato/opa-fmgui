/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
 * The source code contained or described herein and all documents related to the source code ("Material")
 * are owned by Intel Corporation or its suppliers or licensors. Title to the Material remains with Intel
 * Corporation or its suppliers and licensors. The Material contains trade secrets and proprietary and
 * confidential information of Intel or its suppliers and licensors. The Material is protected by
 * worldwide copyright and trade secret laws and treaty provisions. No part of the Material may be used,
 * copied, reproduced, modified, published, uploaded, posted, transmitted, distributed, or disclosed in
 * any way without Intel's prior express written permission. No license under any patent, copyright,
 * trade secret or other intellectual property right is granted to or conferred upon you by disclosure
 * or delivery of the Materials, either expressly, by implication, inducement, estoppel or otherwise.
 * Any license under such intellectual property rights must be express and approved by Intel in writing.
 */

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: BaseChannel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
    }

    @Override
    public void initialize(SubnetDescription subnet) throws Exception {
        socketChannel = connection.getSocketChannel();
        handshakeInProgress = false;
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
