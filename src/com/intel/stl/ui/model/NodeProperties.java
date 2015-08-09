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
 *  File Name: NodeProperties.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
