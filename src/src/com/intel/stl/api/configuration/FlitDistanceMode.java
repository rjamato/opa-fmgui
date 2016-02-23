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
 *  File Name: LinkMode.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/08/17 18:48:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
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
