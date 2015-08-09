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
 *  File Name: ResourceCategory.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.9  2015/02/06 00:27:01  jijunwan
 *  Archive Log:    added neighbor link down reason to match FM 325
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/01/11 20:47:24  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015.
 *  Archive Log:    adapt to changes introduced on FM side.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/05 19:25:05  jypak
 *  Archive Log:    Link Down Error Log updates
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/12/31 17:41:17  jypak
 *  Archive Log:    1. CableInfo updates (Moved the QSFP interpretation logic to backend etc.)
 *  Archive Log:    2. SC2SL updates.
 *  Archive Log:    3. SC2VLt updates.
 *  Archive Log:    4. SC3VLnt updates.
 *  Archive Log:    Some of the SubnetApi, CachedSubnetApi updates should be undone when the FE supports cable info, SC2SL, SC2VLt, SC2VLnt.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/18 16:25:41  jypak
 *  Archive Log:    Cable Info updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/19 07:13:41  jypak
 *  Archive Log:    HoQLife, VL Stall Count : property bar chart panel updates
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/11/13 00:36:58  jypak
 *  Archive Log:    MTU by VL bar chart panel updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/29 18:53:56  fernande
 *  Archive Log:    Changing UserSettings to support Properties Display options
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import static com.intel.stl.api.configuration.ResourceType.HFI;
import static com.intel.stl.api.configuration.ResourceType.PORT;
import static com.intel.stl.api.configuration.ResourceType.SWITCH;

import java.util.HashMap;
import java.util.Map;

public enum ResourceCategory {

    NODE_INFO(HFI, SWITCH),
    DEVICE_GROUPS(HFI, SWITCH),
    LINK_WIDTH(PORT),
    LINK_WIDTH_DOWNGRADE(PORT),
    LINK_SPEED(PORT),
    LINK_CONNECTED_TO(PORT),
    NEIGHBOR_MODE(PORT),
    NODE_PORT_INFO(HFI, SWITCH),
    PORT_INFO(PORT),
    PORT_LINK_MODE(PORT),
    PORT_LTP_CRC_MODE(PORT),
    PORT_MODE(PORT),
    PORT_PACKET_FORMAT(PORT),
    PORT_ERROR_ACTIONS(PORT),
    PORT_BUFFER_UNITS(PORT),
    PORT_IPADDR(PORT),
    PORT_SUBNET(PORT),
    PORT_CAPABILITIES(PORT),
    PORT_DIAGNOSTICS(PORT),
    PORT_MANAGEMENT(PORT),
    PORT_PARTITION_ENFORCEMENT(PORT),
    FLIT_CTRL_INTERLEAVE(PORT),
    FLIT_CTRL_PREEMPTION(PORT),
    HOQLIFE_CHART(PORT),
    VIRTUAL_LANE(PORT),
    VL_STALL_CHART(PORT),
    MTU_CHART(PORT),
    // CABLE_INFO is PORT ResourceType but not necessarily from PortInfo data
    // structure.
    CABLE_INFO(PORT),
    SC2SLMT_CHART(HFI, SWITCH),
    SC2VLTMT_CHART(PORT),
    SC2VLNTMT_CHART(PORT),
    LINK_DOWN_ERROR_LOG(PORT),
    NEIGHBOR_LINK_DOWN_ERROR_LOG(PORT),

    SWITCH_INFO(SWITCH),
    SWITCH_FORWARDING(SWITCH),
    SWITCH_ROUTING(SWITCH),
    SWITCH_IPADDR(SWITCH),
    SWITCH_PARTITION_ENFORCEMENT(SWITCH),
    SWITCH_ADAPTIVE_ROUTING(SWITCH),
    MFT_TABLE(SWITCH),
    LFT_HISTOGRAM(SWITCH),
    LFT_TABLE(SWITCH);

    private final static Map<String, ResourceCategory> categoryMap =
            new HashMap<String, ResourceCategory>();
    static {
        for (ResourceCategory rc : ResourceCategory.values()) {
            categoryMap.put(rc.name(), rc);
        }
    };

    private final ResourceType[] resourceTypes;

    private ResourceCategory(ResourceType... resourceTypes) {
        this.resourceTypes = resourceTypes;
    }

    public boolean isApplicableTo(ResourceType resourceType) {
        for (int i = 0; i < resourceTypes.length; i++) {
            if (resourceTypes[i] == resourceType) {
                return true;
            }
        }
        return false;
    }

    public static ResourceCategory getResourceCategoryFor(String categoryName) {
        ResourceCategory res = categoryMap.get(categoryName);
        if (res != null) {
            return res;
        } else {
            throw new IllegalArgumentException("Unsupported ResourceCategory '"
                    + categoryName + "'");
        }
    }
}
