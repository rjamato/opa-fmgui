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
 *  File Name: VirtualLaneProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/02/04 21:44:14  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/23 20:15:17  jijunwan
 *  Archive Log:    PR 126673 - "Unsupported VL Cap(0X08)" for all Switch ports other than Switch port 0
 *  Archive Log:    STL is using VL Cap as a number rather than an enum. Removed VL Cap related IB enum, and represent Cap as as a byte number
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/11 19:26:45  jijunwan
 *  Archive Log:    PR 126421 - VL Flow Control Disable Mask not implemented on WFR HFI
 *  Archive Log:    display n/a for HFI and Switch port 0
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/22 01:47:47  jijunwan
 *  Archive Log:    renamed
 *  Archive Log:    PropertyPageCategory to DevicePropertyCategory,
 *  Archive Log:    PropertyItem to DevicePropertyItem,
 *  Archive Log:    PropertyPageGroup to DevicePropertyGroup
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/13 21:06:15  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import static com.intel.stl.ui.common.STLConstants.K0387_UNKNOWN;
import static com.intel.stl.ui.model.DeviceProperty.FLOW_CONTROL_DISABLE_MASK;
import static com.intel.stl.ui.model.DeviceProperty.HOQLIFE_LABEL;
import static com.intel.stl.ui.model.DeviceProperty.OPERATIONAL_VLS;
import static com.intel.stl.ui.model.DeviceProperty.VL_ARBITR_HIGH_CAP;
import static com.intel.stl.ui.model.DeviceProperty.VL_ARBITR_LOW_CAP;
import static com.intel.stl.ui.model.DeviceProperty.VL_CAP;
import static com.intel.stl.ui.model.DeviceProperty.VL_HIGH_LIMIT;
import static com.intel.stl.ui.model.DeviceProperty.VL_PREEMPTING_LIMIT;
import static com.intel.stl.ui.model.DeviceProperty.VL_PREEMPT_CAP;
import static com.intel.stl.ui.model.DeviceProperty.VL_STALL_COUNT;

import com.intel.stl.api.subnet.NodeInfoBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.PortInfoBean;
import com.intel.stl.api.subnet.VirtualLaneBean;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.model.DevicePropertyCategory;

public class VirtualLaneProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        NodeInfoBean nodeInfo = context.getNodeInfo();
        PortInfoBean portInfo = context.getPortInfo();

        if (!(nodeInfo != null && portInfo != null)) {
            getEmptyVirtualLane(category);
            return;
        }
        String unknown = K0387_UNKNOWN.getValue();
        String na = STLConstants.K0383_NA.getValue();
        VirtualLaneBean vlBean = portInfo.getVl();
        String value = unknown;
        byte cap = portInfo.getOperationalVL();
        value = hex(cap);
        addProperty(category, OPERATIONAL_VLS, value);
        cap = vlBean.getCap();
        value = hex(cap);
        addProperty(category, VL_CAP, value);
        addProperty(category, VL_HIGH_LIMIT, dec(vlBean.getHighLimit()));
        addProperty(category, VL_PREEMPT_CAP, dec(vlBean.getPreemptCap()));
        addProperty(category, VL_PREEMPTING_LIMIT,
                dec(vlBean.getPreemptingLimit()));
        if (nodeInfo.getNodeTypeEnum() == NodeType.SWITCH
                && context.getPort().getPortNum() > 0) {
            addProperty(category, FLOW_CONTROL_DISABLE_MASK,
                    hex(portInfo.getFlowControlMask()));
        } else {
            addProperty(category, FLOW_CONTROL_DISABLE_MASK, na);
        }
        addProperty(category, VL_ARBITR_HIGH_CAP,
                Short.toString(vlBean.getArbitrationHighCap()));
        addProperty(category, VL_ARBITR_LOW_CAP,
                Short.toString(vlBean.getArbitrationLowCap()));
        value = na;
        if (nodeInfo.getNodeTypeEnum() == NodeType.SWITCH) {
            value = Byte.toString(portInfo.getVlStallCount()[0]);
        }
        addProperty(category, VL_STALL_COUNT, value);
        value = na;
        if (nodeInfo.getNodeTypeEnum() != NodeType.HFI) {
            value = Byte.toString(portInfo.getHoqLife()[0]);
        }
        addProperty(category, HOQLIFE_LABEL, value);
    }

    private void getEmptyVirtualLane(DevicePropertyCategory category) {
        addProperty(category, OPERATIONAL_VLS, "");
        addProperty(category, VL_CAP, "");
        addProperty(category, VL_HIGH_LIMIT, "");
        addProperty(category, VL_PREEMPT_CAP, "");
        addProperty(category, VL_PREEMPTING_LIMIT, "");
        addProperty(category, FLOW_CONTROL_DISABLE_MASK, "");
        addProperty(category, VL_ARBITR_HIGH_CAP, "");
        addProperty(category, VL_ARBITR_LOW_CAP, "");
        addProperty(category, VL_STALL_COUNT, "");
        addProperty(category, HOQLIFE_LABEL, "");
    }
}
