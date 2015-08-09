/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: LinkMode.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/11 20:04:27  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import java.util.Arrays;

import com.intel.stl.api.StringUtils;

/**
 * <pre>
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_sm.h v1.115
 * STL Port link formats, indicated as follows:
 * values are additive for Supported and Enabled fields
 * 
 * #define STL_PORT_PACKET_FORMAT_NOP  0       // No change 
 * #define STL_PORT_PACKET_FORMAT_8B   1       // Format 8B 
 * #define STL_PORT_PACKET_FORMAT_9B   2       // Format 9B 
 * #define STL_PORT_PACKET_FORMAT_10B  4       // Format 10B
 * #define STL_PORT_PACKET_FORMAT_16B  8       // Format 16B
 * 
 * </pre>
 */
public enum PacketFormat {
    NOP((byte) 0),
    FORMAT_8B((byte) 1),
    FORMAT_9B((byte) 2),
    FORMAT_10B((byte) 4),
    FORMAT_16B((byte) 8);

    private final byte value;

    /**
     * Description:
     * 
     * @param value
     */
    private PacketFormat(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static PacketFormat getPacketFormat(short value) {
        byte bValue = (byte) (value & 0xff);
        for (PacketFormat format : PacketFormat.values()) {
            if (format.value == bValue) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unsupported Packet Format "
                + StringUtils.shortHexString(value));
    }

    public static PacketFormat[] getPacketFormats(short value) {
        byte bValue = (byte) (value & 0xff);
        if (isNoneSupported(bValue)) {
            return new PacketFormat[] { NOP };
        }

        PacketFormat[] allPacketFormats = PacketFormat.values();
        PacketFormat[] formats = new PacketFormat[allPacketFormats.length];
        int numFormats = 0;
        for (PacketFormat pfv : allPacketFormats) {
            if (pfv != NOP && ((pfv.value & bValue) == pfv.value)) {
                formats[numFormats] = pfv;
                numFormats++;
            }
        }
        return Arrays.copyOf(formats, numFormats);
    }

    public static boolean isNoneSupported(byte value) {
        return value == NOP.value;
    }
}
