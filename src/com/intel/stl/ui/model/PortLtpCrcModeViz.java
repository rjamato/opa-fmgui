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
 *  File Name: PortLtpCrcModeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1.2.1  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
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
