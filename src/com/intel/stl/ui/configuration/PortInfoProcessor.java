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
 *  File Name: PortInfoProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.9  2015/04/10 14:23:04  jijunwan
 *  Archive Log:    PR 127495 - Add LED indicator bit to STL_PORT_STATES
 *  Archive Log:    -- display LED Enabled in port properties
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/05 23:38:02  jijunwan
 *  Archive Log:    updated link down reason to match FM 325
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/02 16:27:19  jijunwan
 *  Archive Log:    matched FM 320 (stl_sm.h v1.26) - added linkInitReason
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/27 19:19:53  jijunwan
 *  Archive Log:    updated PortInfo to match FM 314 (stl_sm.h v1.125)
 *  Archive Log:      - removed downDefaultState
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/15 21:44:38  jijunwan
 *  Archive Log:    updated to FM Build 298 that removed PortUnsleepState from STL_PORT_STATES
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/15 19:21:30  jijunwan
 *  Archive Log:    updated to FM Build 298 that removed PortUnsleepState from STL_PORT_STATES
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/13 18:22:37  jijunwan
 *  Archive Log:    support UniversalDiagCode and VendorDiagCode
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/22 01:47:47  jijunwan
 *  Archive Log:    renamed
 *  Archive Log:    PropertyPageCategory to DevicePropertyCategory,
 *  Archive Log:    PropertyItem to DevicePropertyItem,
 *  Archive Log:    PropertyPageGroup to DevicePropertyGroup
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/13 21:05:30  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import static com.intel.stl.ui.common.STLConstants.K0385_TRUE;
import static com.intel.stl.ui.common.STLConstants.K0386_FALSE;
import static com.intel.stl.ui.common.STLConstants.K0387_UNKNOWN;
import static com.intel.stl.ui.model.DeviceProperty.BASE_LID;
import static com.intel.stl.ui.model.DeviceProperty.GID_PREFIX;
import static com.intel.stl.ui.model.DeviceProperty.LINK_DOWN_REASON;
import static com.intel.stl.ui.model.DeviceProperty.LINK_INIT_REASON;
import static com.intel.stl.ui.model.DeviceProperty.LMC;
import static com.intel.stl.ui.model.DeviceProperty.MAX_MTU_SIZE;
import static com.intel.stl.ui.model.DeviceProperty.MAX_NEIGHBOR_MTU_SIZE;
import static com.intel.stl.ui.model.DeviceProperty.NEIGHBOR_LINK_DOWN_REASON;
import static com.intel.stl.ui.model.DeviceProperty.NODE_GUID;
import static com.intel.stl.ui.model.DeviceProperty.OFFLINE_DISABLED_REASON;
import static com.intel.stl.ui.model.DeviceProperty.PORT_LED_ENABLED;
import static com.intel.stl.ui.model.DeviceProperty.PORT_LID;
import static com.intel.stl.ui.model.DeviceProperty.PORT_LOCAL_NUM;
import static com.intel.stl.ui.model.DeviceProperty.PORT_NEIGHBOR_NORMAL;
import static com.intel.stl.ui.model.DeviceProperty.PORT_NUMBER;
import static com.intel.stl.ui.model.DeviceProperty.PORT_PHYSICAL_STATE;
import static com.intel.stl.ui.model.DeviceProperty.PORT_SM_CONFIG_START;
import static com.intel.stl.ui.model.DeviceProperty.PORT_STATE;
import static com.intel.stl.ui.model.DeviceProperty.PORT_TYPE;
import static com.intel.stl.ui.model.DeviceProperty.UNI_DIAG_CODE;
import static com.intel.stl.ui.model.DeviceProperty.VENDOR_DIAG_CODE;

import com.intel.stl.api.configuration.MTUSize;
import com.intel.stl.api.configuration.PhysicalState;
import com.intel.stl.api.configuration.PortState;
import com.intel.stl.api.subnet.NodeInfoBean;
import com.intel.stl.api.subnet.PortInfoBean;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.PortStatesBean;
import com.intel.stl.ui.model.DevicePropertyCategory;
import com.intel.stl.ui.model.LinkDownReasonViz;
import com.intel.stl.ui.model.LinkInitReasonViz;
import com.intel.stl.ui.model.MTUSizeViz;
import com.intel.stl.ui.model.OfflineDisabledReasonViz;
import com.intel.stl.ui.model.PhysicalStateViz;
import com.intel.stl.ui.model.PortStateViz;
import com.intel.stl.ui.model.PortTypeViz;

public class PortInfoProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        NodeInfoBean nodeInfo = context.getNodeInfo();
        PortRecordBean portBean = context.getPort();
        PortInfoBean portInfo = context.getPortInfo();
        boolean isEndPort = context.isEndPort();

        if (!(nodeInfo != null && portInfo != null)) {
            getEmptyPortDeviceInfo(category);
            return;
        }
        String trueStr = K0385_TRUE.getValue();
        String falseStr = K0386_FALSE.getValue();
        String unknownStr = K0387_UNKNOWN.getValue();
        String value = falseStr;
        addProperty(category, PORT_LID, dec(portBean.getEndPortLID()));
        addProperty(category, PORT_NUMBER, dec(portBean.getPortNum()));
        addProperty(category, PORT_LOCAL_NUM, dec(portInfo.getLocalPortNum()));
        PortStatesBean psBean = portInfo.getPortStates();
        if (psBean != null) {
            PortState portState = psBean.getPortState();
            value = unknownStr;
            if (portState != null) {
                value = PortStateViz.getPortStateStr(portState);
            }
            addProperty(category, PORT_STATE, value);
            addProperty(category, LINK_INIT_REASON,
                    LinkInitReasonViz.getLinkInitReasonStr(portInfo
                            .getLinkInitReason()));
            PhysicalState physState = psBean.getPortPhysicalState();
            value = unknownStr;
            if (physState != null) {
                value = PhysicalStateViz.getPhysicalStateStr(physState);
            }
            addProperty(category, PORT_PHYSICAL_STATE, value);
            value =
                    OfflineDisabledReasonViz.getOfflineDisabledReasonStr(psBean
                            .getOfflineReason());
            if (value == null) {
                value = unknownStr;
            }
            addProperty(category, OFFLINE_DISABLED_REASON, value);
            value = falseStr;
            if (psBean.isLedEnabled()) {
                value = trueStr;
            }
            addProperty(category, PORT_LED_ENABLED, value);
            value = falseStr;
            if (psBean.isSMConfigurationStarted()) {
                value = trueStr;
            }
            addProperty(category, PORT_SM_CONFIG_START, value);
            value = falseStr;
            if (psBean.isNeighborNormal()) {
                value = trueStr;
            }
            addProperty(category, PORT_NEIGHBOR_NORMAL, value);
        } else {
            addProperty(category, PORT_STATE, "");
            addProperty(category, PORT_PHYSICAL_STATE, "");
            addProperty(category, OFFLINE_DISABLED_REASON, "");
            addProperty(category, PORT_SM_CONFIG_START, "");
            addProperty(category, PORT_NEIGHBOR_NORMAL, "");
        }
        if (isEndPort) {
            addProperty(category, BASE_LID, hex(portInfo.getLid()));
            addProperty(category, LMC, dec(portInfo.getLmc()));
        }
        addProperty(category, PORT_TYPE,
                PortTypeViz.getPortTypeStr(portInfo.getPortType()));
        addProperty(category, NODE_GUID, hex(nodeInfo.getNodeGUID()));
        addProperty(category, LINK_DOWN_REASON,
                LinkDownReasonViz.getLinkDownReasonStr(portInfo
                        .getLinkDownReason()));
        addProperty(category, NEIGHBOR_LINK_DOWN_REASON,
                LinkDownReasonViz.getLinkDownReasonStr(portInfo
                        .getNeighborLinkDownReason()));
        if (isEndPort) {
            addProperty(category, GID_PREFIX, hex(portInfo.getSubnetPrefix()));
            addProperty(category, UNI_DIAG_CODE,
                    dec(portInfo.getUniversalDiagCode()));
            addProperty(category, VENDOR_DIAG_CODE,
                    dec(portInfo.getVendorDiagCode()));
        }
        byte neighborMtu = portInfo.getNeighborVL0MTU()[0]; // this is how it is
                                                            // done in
                                                            // fillPortInfo()

        MTUSizeViz mtusize = MTUSizeViz.getMTUSizeViz(neighborMtu);
        if (mtusize != null) {
            addProperty(category, MAX_NEIGHBOR_MTU_SIZE, mtusize.getValue());
        } else {
            addProperty(category, MAX_NEIGHBOR_MTU_SIZE,
                    K0387_UNKNOWN.getValue());
        }
        MTUSize mtuCap = portInfo.getMtuCap();

        if (mtuCap != null) {
            addProperty(category, MAX_MTU_SIZE,
                    MTUSizeViz.getMTUSizeStr(mtuCap));
        } else {
            addProperty(category, MAX_MTU_SIZE, K0387_UNKNOWN.getValue());
        }
    }

    private void getEmptyPortDeviceInfo(DevicePropertyCategory category) {
        addProperty(category, PORT_LID, "");
        addProperty(category, PORT_NUMBER, "");
        addProperty(category, PORT_STATE, "");
        addProperty(category, PORT_PHYSICAL_STATE, "");
        addProperty(category, OFFLINE_DISABLED_REASON, "");
        addProperty(category, PORT_SM_CONFIG_START, "");
        addProperty(category, PORT_NEIGHBOR_NORMAL, "");
        addProperty(category, BASE_LID, "");
        addProperty(category, LMC, "");
        addProperty(category, PORT_TYPE, "");
        addProperty(category, NODE_GUID, "");
        addProperty(category, LINK_DOWN_REASON, "");
        addProperty(category, GID_PREFIX, "");
        addProperty(category, UNI_DIAG_CODE, "");
        addProperty(category, VENDOR_DIAG_CODE, "");
        addProperty(category, MAX_NEIGHBOR_MTU_SIZE, "");
        addProperty(category, MAX_MTU_SIZE, "");
    }
}
