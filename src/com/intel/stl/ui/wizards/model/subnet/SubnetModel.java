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
 *  File Name: SubnetModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/08/17 18:54:55  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
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
