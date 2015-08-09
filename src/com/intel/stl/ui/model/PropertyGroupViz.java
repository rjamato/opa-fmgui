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
 *  File Name: PropertyPageGroupViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/02/05 19:57:09  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/05 19:25:08  jypak
 *  Archive Log:    Link Down Error Log updates
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/12/31 17:49:41  jypak
 *  Archive Log:    1. CableInfo updates (Moved the QSFP interpretation logic to backend etc.)
 *  Archive Log:    2. SC2SL updates.
 *  Archive Log:    3. SC2VLt updates.
 *  Archive Log:    4. SC3VLnt updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/18 16:33:49  jypak
 *  Archive Log:    Cable Info updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/19 07:13:29  jypak
 *  Archive Log:    HoQLife, VL Stall Count : property bar chart panel updates
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/11/13 00:36:49  jypak
 *  Archive Log:    MTU by VL bar chart panel updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/13 21:08:07  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.HashMap;
import java.util.Map;

import com.intel.stl.ui.common.STLConstants;

public enum PropertyGroupViz {
    GENERAL(STLConstants.K0397_GENERAL),
    SWITCH_INFO(STLConstants.K0436_SWITCH_INFO),
    ROUTING_INFO(STLConstants.K0438_ROUTING_INFO),
    DEVICE_GROUP(STLConstants.K0367_DEVICE_GRP),
    MFT(STLConstants.K0399_MULTI_FWD_TABLE),
    LFT(STLConstants.K0398_LINEAR_FWD_TABLE),
    DEVICE_INFO(STLConstants.K0307_DEV_INFO_TITLE),
    PORT_LINK(STLConstants.K0318_PORT_LINK_TITLE),
    LINK_CONNECTION(STLConstants.K0457_LINK_CONNECTION),
    PORT_CAPABILITY(STLConstants.K0328_PORT_CAPABILITY_TITLE),
    VIRTUAL_LANE(STLConstants.K0342_PORT_VL_TITLE),
    PORT_DIAGNOSTICS(STLConstants.K0350_PORT_DIAG_TITLE),
    PORT_PARTITION_ENFORCEMENT(STLConstants.K0302_PORT_PART_ENFORCE_TITLE),
    PORT_MANAGEMENT(STLConstants.K0358_PORT_MANAGEMENT_TITLE),
    FLIT_CONTROL(STLConstants.K0775_FLIT_CTRL),
    PORT_ERROR_ACTIONS(STLConstants.K1041_PORT_ERROR_ACTIONS),
    MISCELLANEOUS(STLConstants.K0822_MISCELLANEOUS),
    MTU(STLConstants.K1066_MTU_SERIES),
    HOQLIFE(STLConstants.K1069_HOQLIFE),
    VL_STALL_COUNT(STLConstants.K1070_VLSTALL_SERIES),
    CABLE_INFO(STLConstants.K1071_QSFP_CABLE_INFO),
    SC2SLMT(STLConstants.K1104_SC2SL_MAPPING_TABLE),
    SC2VLTMT(STLConstants.K1107_SC2VLT_MAPPING_TABLE),
    SC2VLNTMT(STLConstants.K1108_SC2VLNT_MAPPING_TABLE),
    LINK_DOWN_ERROR_REASON(STLConstants.K1111_LINK_DOWN_ERROR_LOG);

    private final static Map<String, PropertyGroupViz> groupNameMap =
            new HashMap<String, PropertyGroupViz>();
    static {
        for (PropertyGroupViz pgv : PropertyGroupViz.values()) {
            groupNameMap.put(pgv.name(), pgv);
        }
    };

    private final STLConstants title;

    private PropertyGroupViz(STLConstants title) {
        this.title = title;
    }

    public String getTitle() {
        return title.getValue();
    }

    public static PropertyGroupViz getPropertyGroupViz(String name) {
        PropertyGroupViz res = groupNameMap.get(name);
        if (res != null) {
            return res;
        } else {
            throw new IllegalArgumentException("Couldn't find PropertyGroup '"
                    + name + "'");
        }
    }

}
