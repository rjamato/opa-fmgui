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
 *  File Name: PortLinkMode.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/11 18:40:03  jijunwan
 *  Archive Log:    PR 126387 - SCMappingTableRecord PortLinkMode ambiguous
 *  Archive Log:    updated to the latest version stl_sa.h v1.92
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
 * STL Port link mode, indicated as follows:
 * values are additive for Supported and Enabled fields
 * 
 * #define STL_PORT_LINK_MODE_NOP  0       // No change
 * // reserved 1
 * #define STL_PORT_LINK_MODE_ETH  2       // Port mode is ETH (Gateway)
 * #define STL_PORT_LINK_MODE_STL  4       // Port mode is STL
 * #define STL_PORT_LINK_MODE_ALL_SUPPORTED 7
 * 
 * </pre>
 */
public enum PortLinkMode {
    NOP((byte) 0x00),
    ETH((byte) 0x02),
    STL((byte) 0x04),
    ALL_SUPPORTED((byte) 0x07);

    private final byte mode;

    private PortLinkMode(byte mode) {
        this.mode = mode;
    }

    public byte getMode() {
        return mode;
    }

    public static PortLinkMode getPortLinkMode(byte mode) {
        byte sMode = (byte) (mode & ALL_SUPPORTED.mode);
        for (PortLinkMode plm : PortLinkMode.values()) {
            if (plm.getMode() == sMode) {
                return plm;
            }
        }
        throw new IllegalArgumentException("Unsupported PortLinkMode "
                + StringUtils.byteHexString(mode));
    }

    public static PortLinkMode[] getPortLinkModes(byte mode) {
        if (isNoneSupported(mode)) {
            return new PortLinkMode[] { NOP };
        }
        if (isAllSupported(mode)) {
            return new PortLinkMode[] { ALL_SUPPORTED };
        }
        PortLinkMode[] portLinkModes = PortLinkMode.values();
        PortLinkMode[] modes = new PortLinkMode[portLinkModes.length];
        int numModes = 0;
        for (PortLinkMode plm : portLinkModes) {
            if (plm != NOP && ((plm.mode & mode) == plm.mode)) {
                modes[numModes] = plm;
                numModes++;
            }
        }
        return Arrays.copyOf(modes, numModes);
    }

    public static boolean isNoneSupported(byte mode) {
        return mode == NOP.mode;
    }

    public static boolean isAllSupported(byte mode) {
        return (ALL_SUPPORTED.mode & mode) == ALL_SUPPORTED.mode;
    }
}
