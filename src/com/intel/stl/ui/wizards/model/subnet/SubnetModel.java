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
 *  File Name: SubnetModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/03/16 17:46:33  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/20 21:15:14  rjtierne
 *  Archive Log:    Multinet Wizard: Data models for all wizards for data storage and display
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/13 21:29:14  rjtierne
 *  Archive Log:    Multinet Wizard: Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.model.subnet;

import com.intel.stl.api.CertsDescription;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.STLConstants;

public class SubnetModel {

    public final static int DEFAULT_PORT_NUM = Integer
            .valueOf(STLConstants.K3015_DEFAULT_PORT.getValue());

    private SubnetDescription subnet;

    public SubnetDescription getSubnet() {
        return subnet;
    }

    public void setSubnet(SubnetDescription subnet) {
        this.subnet = subnet;
    }

    public void clear() {
        subnet = new SubnetDescription();

        subnet.setAutoConnect(false);
        subnet.setName("");
        subnet.setSubnetId(0);
        HostInfo hostInfo = new HostInfo();
        hostInfo.setCertsDescription(new CertsDescription());
        hostInfo.setHost("");
        hostInfo.setPort(Integer.valueOf(STLConstants.K3015_DEFAULT_PORT
                .getValue()));
        hostInfo.setSecureConnect(false);
        subnet.getFEList().add(hostInfo);
    }
}
