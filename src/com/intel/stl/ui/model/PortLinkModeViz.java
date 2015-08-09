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
 *  File Name: PortLinkModeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/01/11 18:40:02  jijunwan
 *  Archive Log:    PR 126387 - SCMappingTableRecord PortLinkMode ambiguous
 *  Archive Log:    updated to the latest version stl_sa.h v1.92
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/18 21:31:22  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import static com.intel.stl.ui.common.STLConstants.K0384_ALL_SUPP;

import java.util.EnumMap;

import com.intel.stl.api.configuration.PortLinkMode;

public enum PortLinkModeViz {
    NOP(PortLinkMode.NOP, "NOP"),
    ETH(PortLinkMode.ETH, "ETH"),
    STL(PortLinkMode.STL, "STL"),
    ALL_SUPPORTED(PortLinkMode.ALL_SUPPORTED, K0384_ALL_SUPP.getValue());

    private final static EnumMap<PortLinkMode, String> portLinkModeMap =
            new EnumMap<PortLinkMode, String>(PortLinkMode.class);
    static {
        for (PortLinkModeViz plmv : PortLinkModeViz.values()) {
            portLinkModeMap.put(plmv.portLinkMode, plmv.value);
        }
    };

    private final PortLinkMode portLinkMode;

    private final String value;

    private PortLinkModeViz(PortLinkMode portLinkMode, String value) {
        this.portLinkMode = portLinkMode;
        this.value = value;
    }

    public static String getPortLinkModeStr(PortLinkMode mode) {
        return portLinkModeMap.get(mode);
    }

    public static String getPortLinkModeStr(byte mode) {
        PortLinkMode[] modes = PortLinkMode.getPortLinkModes(mode);
        String comma = "";
        StringBuffer linkModeStr = new StringBuffer();
        for (int i = 0; i < modes.length; i++) {
            linkModeStr.append(comma);
            linkModeStr.append(portLinkModeMap.get(modes[i]));
            comma = ", ";
        }
        return linkModeStr.toString();
    }
}
