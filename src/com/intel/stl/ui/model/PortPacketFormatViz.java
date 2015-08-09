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
 *  Archive Log:    Revision 1.1  2015/01/11 21:36:23  jijunwan
 *  Archive Log:    adapt to latest data structure changes on FM
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

import java.util.EnumMap;

import com.intel.stl.api.configuration.PacketFormat;
import com.intel.stl.api.configuration.PortLinkMode;

public enum PortPacketFormatViz {
    NOP(PacketFormat.NOP, "NOP"),
    FORMAT_8B(PacketFormat.FORMAT_8B, "8B"),
    FORMAT_9B(PacketFormat.FORMAT_9B, "9B"),
    FORMAT_10B(PacketFormat.FORMAT_10B, "10B"),
    FORMAT_16B(PacketFormat.FORMAT_16B, "16B");

    private final static EnumMap<PacketFormat, String> packetFormatMap =
            new EnumMap<PacketFormat, String>(PacketFormat.class);
    static {
        for (PortPacketFormatViz ppfv : PortPacketFormatViz.values()) {
            packetFormatMap.put(ppfv.packetFormat, ppfv.value);
        }
    };

    private final PacketFormat packetFormat;

    private final String value;

    private PortPacketFormatViz(PacketFormat packetFormat, String value) {
        this.packetFormat = packetFormat;
        this.value = value;
    }

    public static String getPortPacketFormatStr(PortLinkMode mode) {
        return packetFormatMap.get(mode);
    }

    public static String getPortPacketFormatStr(short format) {
        PacketFormat[] modes = PacketFormat.getPacketFormats(format);
        String comma = "";
        StringBuffer linkModeStr = new StringBuffer();
        for (int i = 0; i < modes.length; i++) {
            linkModeStr.append(comma);
            linkModeStr.append(packetFormatMap.get(modes[i]));
            comma = ", ";
        }
        return linkModeStr.toString();
    }
}
