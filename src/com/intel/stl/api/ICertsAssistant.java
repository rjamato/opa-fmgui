/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2014 Intel Corporation All Rights Reserved.
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
 *  File Name: ICertDialog.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/04/03 16:16:28  fernande
 *  Archive Log:    Added getSSLEngine to the interface.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/24 15:07:49  fernande
 *  Archive Log:    Changes to cache KeyManagerFactories and TrustManagerFactories to avoid requests for password.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/19 16:52:17  fernande
 *  Archive Log:    Fix for the UI CertsAssistant which needs the SubnetDescription.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/11 20:56:32  jijunwan
 *  Archive Log:    support secure FE:
 *  Archive Log:    1) added secured STL Connection to communicate with FE
 *  Archive Log:    2) added cert assistant interface that supports certs conf, persistence and password prompt
 *  Archive Log:    3) added default cert assistant
 *  Archive Log:    4) improved Subnet conf to support secure FE
 *  Archive Log:
 *  Archive Log:    NOTE: the secured connection requires Java 1.7
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import com.intel.stl.api.subnet.SubnetDescription;

public interface ICertsAssistant {

    /**
     * 
     * <i>Description:</i> returns a SSLEngine for the SubnetDescription using
     * the current HostInfo
     * 
     * @return a SSLEngine
     */
    SSLEngine getSSLEngine(SubnetDescription subnet) throws Exception;

    /**
     * 
     * <i>Description:</i> returns a KeyManagerFactory for the
     * SubnetDescription.
     * 
     * @return a KeyManagerFactory
     */
    KeyManagerFactory getKeyManagerFactory(SubnetDescription subnet);

    /**
     * 
     * <i>Description:</i> returns a TrustManagerFactory for the
     * SubnetDescription.
     * 
     * @return a TrustManagerFactory
     */
    TrustManagerFactory getTrustManagerFactory(SubnetDescription subnet);

    /**
     * 
     * <i>Description:</i> clears all factories associated with this subnet
     * 
     */
    void clearSubnetFactories(SubnetDescription subnet);
}
