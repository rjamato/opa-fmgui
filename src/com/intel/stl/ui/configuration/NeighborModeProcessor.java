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
 *  File Name: NeighborModeProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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

import static com.intel.stl.ui.common.STLConstants.K0004_SWITCH;
import static com.intel.stl.ui.common.STLConstants.K0005_HOST_FABRIC_INTERFACE;
import static com.intel.stl.ui.common.STLConstants.K0080_ON;
import static com.intel.stl.ui.common.STLConstants.K0081_YES;
import static com.intel.stl.ui.common.STLConstants.K0082_NO;
import static com.intel.stl.ui.common.STLConstants.K0699_OFF;
import static com.intel.stl.ui.model.DeviceProperty.NEIGHBOR_FW_AUTH_BYPASS;
import static com.intel.stl.ui.model.DeviceProperty.NEIGHBOR_MGMT_ALLOWED;
import static com.intel.stl.ui.model.DeviceProperty.NEIGHBOR_NODE_TYPE;

import com.intel.stl.api.subnet.PortInfoBean;
import com.intel.stl.ui.model.DevicePropertyCategory;

public class NeighborModeProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        PortInfoBean portInfo = context.getPortInfo();
        if (portInfo == null) {
            getEmptyPortNeighborMode(category);
            return;
        }
        String value = K0082_NO.getValue();
        if (portInfo.isMgmtAllowed()) {
            value = K0081_YES.getValue();
        }
        addProperty(category, NEIGHBOR_MGMT_ALLOWED, value);
        value = K0699_OFF.getValue();
        if (portInfo.isNeighborFWAuthenBypass()) {
            value = K0080_ON.getValue();
        }
        addProperty(category, NEIGHBOR_FW_AUTH_BYPASS, value);
        value = K0005_HOST_FABRIC_INTERFACE.getValue();
        if (portInfo.getNeighborNodeType() != 0) {
            value = K0004_SWITCH.getValue();
        }
        addProperty(category, NEIGHBOR_NODE_TYPE, value);
    }

    private void getEmptyPortNeighborMode(DevicePropertyCategory category) {
        addProperty(category, NEIGHBOR_MGMT_ALLOWED, "");
        addProperty(category, NEIGHBOR_FW_AUTH_BYPASS, "");
        addProperty(category, NEIGHBOR_NODE_TYPE, "");
    }
}
