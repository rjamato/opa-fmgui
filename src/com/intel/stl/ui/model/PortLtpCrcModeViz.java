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
 *  File Name: PortLtpCrcModeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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

import java.util.EnumMap;

import com.intel.stl.api.configuration.PortLtpCrcMode;

public enum PortLtpCrcModeViz {
    STL_PORT_LTP_CRC_MODE_NONE(
            PortLtpCrcMode.STL_PORT_LTP_CRC_MODE_NONE,
            "None"),
    STL_PORT_LTP_CRC_MODE_14(PortLtpCrcMode.STL_PORT_LTP_CRC_MODE_14, "14-bit"),
    STL_PORT_LTP_CRC_MODE_16(PortLtpCrcMode.STL_PORT_LTP_CRC_MODE_16, "16-bit"),
    STL_PORT_LTP_CRC_MODE_48(PortLtpCrcMode.STL_PORT_LTP_CRC_MODE_48, "48-bit"),
    STL_PORT_LTP_CRC_MODE_12_16_PER_LANE(
            PortLtpCrcMode.STL_PORT_LTP_CRC_MODE_12_16_PER_LANE,
            "12/16-bit per lane"),
    STL_PORT_LTP_CRC_MODE_ALL(PortLtpCrcMode.STL_PORT_LTP_CRC_MODE_ALL, "All");

    private final static EnumMap<PortLtpCrcMode, String> portLtpCrcModeMap = new EnumMap<PortLtpCrcMode, String>(
            PortLtpCrcMode.class);
    static {
        for (PortLtpCrcModeViz plcmv : PortLtpCrcModeViz.values()) {
            portLtpCrcModeMap.put(plcmv.portLtpCrcMode, plcmv.value);
        }
    };

    private final PortLtpCrcMode portLtpCrcMode;

    private final String value;

    private PortLtpCrcModeViz(PortLtpCrcMode portLtpCrcMode, String value) {
        this.portLtpCrcMode = portLtpCrcMode;
        this.value = value;
    }

    public static String getPortLtpCrcModeStr(PortLtpCrcMode mode) {
        return portLtpCrcModeMap.get(mode);
    }

    public static String getPortLtpCrcModeStr(byte mode) {
        PortLtpCrcMode[] modes = PortLtpCrcMode.getPortLtpCrcModes(mode);
        String comma = "";
        StringBuffer linkModeStr = new StringBuffer();
        for (int i = 0; i < modes.length; i++) {
            linkModeStr.append(comma);
            linkModeStr.append(portLtpCrcModeMap.get(modes[i]));
            comma = ", ";
        }
        return linkModeStr.toString();
    }
}
