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
 *  File Name: SubnetContext.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/04/08 15:16:19  fernande
 *  Archive Log:    Changes to allow for failover to work when the current (initial) FE is not available.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/03/16 17:33:34  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/10 19:28:15  fernande
 *  Archive Log:    SubnetContext are now created after a successful connection is made to the Fabric Executive, otherwise a SubnetConnectionException is triggered. Also, waitForConnect has been postponed until the UI invokes SubnetContext.initialize (thru Context.initialize). This way the UI shows up faster and the UI progress bar reflects more closely the process.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/06 14:59:03  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/01/30 20:24:42  fernande
 *  Archive Log:    Adding method to retrieve a SubnetContext without going thru the context initialization steps.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/11 20:56:32  jijunwan
 *  Archive Log:    support secure FE:
 *  Archive Log:    1) added secured STL Connection to communicate with FE
 *  Archive Log:    2) added cert assistant interface that supports certs conf, persistence and password prompt
 *  Archive Log:    3) added default cert assistant
 *  Archive Log:    4) improved Subnet conf to support secure FE
 *  Archive Log:
 *  Archive Log:    NOTE: the secured connection requires Java 1.7
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/03 21:36:20  fernande
 *  Archive Log:    Adding the CacheManager in support of APIs
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/19 20:00:09  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/07 19:13:00  fernande
 *  Archive Log:    Changes to save Subnets and EventRules to database
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/17 16:44:41  fernande
 *  Archive Log:    Changed AppContext to provide access to the ConfigurationApi, since it already resides there.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/15 20:27:02  fernande
 *  Archive Log:    Changes to defer creation of APIs until a subnet is chosen
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api;

import java.util.List;

import com.intel.stl.api.configuration.IConfigurationApi;
import com.intel.stl.api.subnet.SubnetDescription;

public interface AppContext {
    void registerCertsAssistant(ICertsAssistant assistant);

    void registerSecurityHandler(ISecurityHandler securityHandler);

    List<SubnetDescription> getSubnets() throws DatabaseException;

    IConfigurationApi getConfigurationApi();

    SubnetContext getSubnetContextFor(String subnetName);

    SubnetContext getSubnetContextFor(String subnetName,
            boolean startBackgroundTasks);

    SubnetContext getSubnetContextFor(SubnetDescription subnet);

    SubnetDescription checkConnectivityFor(SubnetDescription subnet);

    void clearCertsInfoFor(SubnetDescription subnet);

    public void shutdown();
}
