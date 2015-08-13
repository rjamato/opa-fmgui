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
 *  File Name: NodeProperties.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6.2.1  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/12/11 18:45:45  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/22 02:05:18  jijunwan
 *  Archive Log:    made property model more general
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/12 20:58:01  jijunwan
 *  Archive Log:    1) renamed HexUtils to StringUtils
 *  Archive Log:    2) added a method to StringUtils to get error message for an exception
 *  Archive Log:    3) changed all code to call StringUtils to get error message
 *  Archive Log:    4) some extra ode format change
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/05 18:39:04  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/05 18:32:51  jijunwan
 *  Archive Log:    changed Channel Adapter to Fabric Interface
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 18:32:10  jincoope
 *  Archive Log:    Moved from configuration package to this package
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/16 16:20:50  jijunwan
 *  Archive Log:    minor refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/16 15:15:49  jijunwan
 *  Archive Log:    use HexUtils for hex string display
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/14 17:09:02  fernande
 *  Archive Log:    Fixed incorrect import
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:48:03  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/09 19:51:30  jincoope
 *  Archive Log:    changed the name of this pack to all lower case
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/09 17:59:44  jincoope
 *  Archive Log:    Added for displaying properties
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jincoope
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.NodeInfoBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.STLConstants;

/**
 * @deprecated use {@link com.intel.stl.ui.model.DevicePropertyCategory}
 */
@Deprecated
public class NodeProperties {

    private static Logger log = LoggerFactory.getLogger(NodeProperties.class);

    private NodeRecordBean nodeRec = null;

    private NodeInfoBean nodeInfo = null;

    private List<String> deviceGrp = null;

    boolean hasData = false;

    public NodeProperties() {

    }

    public NodeProperties(NodeRecordBean RecBean, List<String> grp) {
        nodeRec = RecBean;
        deviceGrp = grp;

        if (nodeRec != null) {
            nodeInfo = nodeRec.getNodeInfo();

            if (nodeInfo != null) {
                hasData = true;
            }
        }
    }

    public String getNodeState() {
        // TODO: need to come from API
        return STLConstants.K0322_PORT_LINK_ACTIVE.getValue();

    }

    public String getDeviceID() {
        if (hasData) {
            return StringUtils.shortHexString(nodeInfo.getDeviceID());
        } else {
            return "";
        }
    }

    public String getVendorID() {
        if (hasData) {
            return StringUtils.intHexString(nodeInfo.getVendorID());
        } else {
            return "";
        }
    }

    public String getRevision() {
        if (hasData) {
            return Integer.toString(nodeInfo.getRevision());
        } else {
            return "";
        }
    }

    public String getNumPorts() {
        if (hasData) {
            return Integer.toString(nodeInfo.getNumPorts());
        } else {
            return "";
        }
    }

    public String getNodeType() {
        String retVal = "";
        if (hasData) {
            NodeType nodeType =
                    NodeType.getNodeType(nodeRec.getNodeInfo().getNodeType());
            switch (nodeType) {

                case SWITCH:
                    retVal = STLConstants.K0004_SWITCH.getValue();
                    break;
                case HFI:
                    retVal =
                            STLConstants.K0005_HOST_FABRIC_INTERFACE.getValue();
                    break;
                case ROUTER:
                    retVal = STLConstants.K0006_ROUTER.getValue();
                    break;
                default:
                    break;
            }
        }

        return retVal;

    }

    public String getNodeGUID() {
        if (hasData) {
            return StringUtils.longHexString(nodeInfo.getNodeGUID());
        } else {
            return "";
        }
    }

    public List<String> getDeviceGroup() {

        if (hasData) {
            return deviceGrp;
        } else {
            return null;
        }
    }
}
