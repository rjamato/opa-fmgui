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
 * STL Port Flit distance mode, indicated as follows:
 * values are additive for Supported and Enabled fields
 * 
 * #define STL_PORT_FLIT_DISTANCE_MODE_NONE    0   // No change
 * #define STL_PORT_FLIT_DISTANCE_MODE_1       1   // STL1 mode
 * #define STL_PORT_FLIT_DISTANCE_MODE_2       2   // STL2 mode
 * 
 * </pre>
 */
public enum FlitDistanceMode {
    NOP((byte) 0),
    MODE_1((byte) 1),
    MODE_2((byte) 2);

    private final byte value;

    /**
     * Description:
     * 
     * @param value
     */
    private FlitDistanceMode(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static FlitDistanceMode getFlitDistanceMode(byte value) {
        for (FlitDistanceMode mode : FlitDistanceMode.values()) {
            if (mode.value == value) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unsupported FlitDistanceMode "
                + StringUtils.byteHexString(value));
    }

    public static FlitDistanceMode[] getFlitDistanceModes(byte value) {
        if (isNoneSupported(value)) {
            return new FlitDistanceMode[] { NOP };
        }

        FlitDistanceMode[] allModes = FlitDistanceMode.values();
        FlitDistanceMode[] modes = new FlitDistanceMode[allModes.length];
        int numFormats = 0;
        for (FlitDistanceMode pdm : allModes) {
            if (pdm != NOP && ((pdm.value & value) == pdm.value)) {
                modes[numFormats] = pdm;
                numFormats++;
            }
        }
        return Arrays.copyOf(modes, numFormats);
    }

    public static boolean isNoneSupported(byte value) {
        return value == NOP.value;
    }
}
