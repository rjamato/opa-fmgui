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
 *  File Name: LinkWidthMask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/01/27 19:43:42  jijunwan
 *  Archive Log:    updated PortInfo to match FM 314 (stl_sm.h v1.125)
 *  Archive Log:      - removed IsSCtoSCMappingEnabled
 *  Archive Log:      - updated comment for STL_LINK_WIDTH_NOP
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/21 22:59:48  jijunwan
 *  Archive Log:    fixed typo
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/11 20:04:27  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/21 22:15:03  jijunwan
 *  Archive Log:    updated to match the latest STL spec
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:21:11  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.intel.stl.api.StringUtils;

/**
 * <pre>
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_sm.h v1.125
 * STL Link width, continued from IB_LINK_WIDTH and indicated as follows:
 * values are additive for Supported and Enabled fields
 * 
 * #define     STL_LINK_WIDTH_NOP                   0  // LinkWidth.Enabled: no changeon set (nop)
 *                                                     // LinkWidth.Active: link is LinkDown
 *                                                     // LinkWidthDowngrade.Supported: unsupported
 *                                                     // LinkWidthDowngrade.Enable: disabled
 *                                                     // LinkWidthDowngrade.TxActive: link is LinkDown
 *                                                     // LinkWidthDowngrade.RxActive: link is LinkDown
 * #define STL_LINK_WIDTH_1X 0x0001
 * #define STL_LINK_WIDTH_2X 0x0002
 * #define STL_LINK_WIDTH_3X 0x0004
 * #define STL_LINK_WIDTH_4X 0x0008
 * </pre>
 */
public enum LinkWidthMask {
    STL_LINK_WIDTH_NOP((short) 0x0000, -1),
    STL_LINK_WIDTH_1X((short) 0x0001, 1),
    STL_LINK_WIDTH_2X((short) 0x0002, 2),
    STL_LINK_WIDTH_3X((short) 0x0004, 3),
    STL_LINK_WIDTH_4X((short) 0x0008, 4);

    private final short val;

    private final int width;

    private LinkWidthMask(short inval, int width) {
        val = inval;
        this.width = width;
    }

    public short getWidthMask() {
        return val;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    public static LinkWidthMask getLinkWidthMask(short inval) {
        for (LinkWidthMask spd : LinkWidthMask.values()) {
            if (spd.getWidthMask() == inval) {
                return spd;
            }
        }
        throw new IllegalArgumentException("Unsupported Link Width "
                + StringUtils.shortHexString(inval));
    }

    public static List<LinkWidthMask> getWidthMasks(short inval) {
        // special case for IB_LINK_SPEED_NOP
        if (inval == STL_LINK_WIDTH_NOP.getWidthMask()) {
            return Collections.singletonList(STL_LINK_WIDTH_NOP);
        }

        List<LinkWidthMask> res = new ArrayList<LinkWidthMask>();
        for (LinkWidthMask mask : LinkWidthMask.values()) {
            if (mask != STL_LINK_WIDTH_NOP
                    && (inval & mask.getWidthMask()) == mask.getWidthMask()) {
                res.add(mask);
            }
        }
        return res;
    }

}
