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
 *  File Name: NormalType.java
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
