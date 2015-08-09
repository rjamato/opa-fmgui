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
 *  File Name: PortLtpCrcMode.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/11 20:04:27  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/18 21:28:32  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import java.util.Arrays;

import com.intel.stl.api.StringUtils;

/**
 * <pre>
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_sm.h v1.115
 * STL Port LTP CRC mode, indicated as follows:
 * values are additive for Supported and Enabled fields
 * 
 * #define STL_PORT_LTP_CRC_MODE_NONE  0   // No change
 * #define STL_PORT_LTP_CRC_MODE_14    1   // 14-bit LTP CRC mode (optional) 
 * #define STL_PORT_LTP_CRC_MODE_16    2   // 16-bit LTP CRC mode 
 * #define STL_PORT_LTP_CRC_MODE_48    4   // 48-bit LTP CRC mode (optional) 
 * #define STL_PORT_LTP_CRC_MODE_12_16_PER_LANE 8  // 12/16-bit per lane LTP CRC mode 
 * #define STL_PORT_LTP_CRC_MODE_ALL   15
 * 
 * </pre>
 */
public enum PortLtpCrcMode {
    STL_PORT_LTP_CRC_MODE_NONE((byte) 0x00), /* No change */
    STL_PORT_LTP_CRC_MODE_14((byte) 0x01), /* 14-bit LTP CRC mode (optional) */
    STL_PORT_LTP_CRC_MODE_16((byte) 0x02), /* 16-bit LTP CRC mode */
    STL_PORT_LTP_CRC_MODE_48((byte) 0x04), /* 48-bit LTP CRC mode (optional) */
    STL_PORT_LTP_CRC_MODE_12_16_PER_LANE((byte) 0x08), /*
                                                        * 12/16-bit per lane LTP
                                                        * CRC mode
                                                        */
    STL_PORT_LTP_CRC_MODE_ALL((byte) 0xff);

    private final byte mode;

    private PortLtpCrcMode(byte mode) {
        this.mode = mode;
    }

    public byte getMode() {
        return mode;
    }

    public static PortLtpCrcMode getPortLtpCrcMode(byte mode) {
        for (PortLtpCrcMode plcm : PortLtpCrcMode.values()) {
            if (plcm.getMode() == mode) {
                return plcm;
            }
        }
        throw new IllegalArgumentException("Unsupported PortLtpCrcMode "
                + StringUtils.byteHexString(mode));
    }

    public static PortLtpCrcMode[] getPortLtpCrcModes(byte mode) {
        if (isNoneSupported(mode)) {
            return new PortLtpCrcMode[] { STL_PORT_LTP_CRC_MODE_NONE };
        }
        if (isAllSupported(mode)) {
            return new PortLtpCrcMode[] { STL_PORT_LTP_CRC_MODE_ALL };
        }
        PortLtpCrcMode[] portLtpCrcModes = PortLtpCrcMode.values();
        PortLtpCrcMode[] modes = new PortLtpCrcMode[portLtpCrcModes.length];
        int numModes = 0;
        for (PortLtpCrcMode plcm : portLtpCrcModes) {
            if (plcm != STL_PORT_LTP_CRC_MODE_NONE
                    && ((plcm.mode & mode) == plcm.mode)) {
                modes[numModes] = plcm;
                numModes++;
            }
        }
        return Arrays.copyOf(modes, numModes);
    }

    public static boolean isNoneSupported(byte mode) {
        return mode == STL_PORT_LTP_CRC_MODE_NONE.mode;
    }

    public static boolean isAllSupported(byte mode) {
        return (STL_PORT_LTP_CRC_MODE_ALL.mode & mode) == STL_PORT_LTP_CRC_MODE_ALL.mode;
    }
}
