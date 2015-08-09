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
 *  File Name: LinkSpeedMask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/01/22 06:00:36  jijunwan
 *  Archive Log:    update to the new link speed defined in FM 308
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/21 22:55:45  jijunwan
 *  Archive Log:    fixed typo
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/11 20:04:27  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/21 22:15:03  jijunwan
 *  Archive Log:    updated to match the latest STL spec
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/01 19:08:48  jijunwan
 *  Archive Log:    changed LinkSpeedMask to include speed, so we can use it to so some comparison
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
import java.util.List;

import com.intel.stl.api.StringUtils;

/**
 * <pre>
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_sm.h v1.121
 * 
 * STL Link speed, continued from IB_LINK_SPEED and indicated as follows:
 * values are additive for Supported and Enabled fields
 * 
 * #define STL_LINK_SPEED_NOP       0      // no change, valid only for enabled 
 * #define STL_LINK_SPEED_12_5G     0x0001 // 12.5 Gbps 
 * #define STL_LINK_SPEED_25G       0x0002     // 25.78125? Gbps (EDR)
 * 
 * </pre>
 */
public enum LinkSpeedMask {
    STL_LINK_SPEED_RESERVED((short) 0x0080, 5f),
    STL_LINK_SPEED_12_5G((short) 0x0001, 12.5f),
    STL_LINK_SPEED_25G((short) 0x002, 25.78125f); // special case

    private final short val;

    private final float speedInGb;

    private LinkSpeedMask(short inval, float speedInGb) {
        val = inval;
        this.speedInGb = speedInGb;
    }

    public short getSpeedMask() {
        return val;
    }

    public float getSpeedInGb() {
        return speedInGb;
    }

    public static LinkSpeedMask getLinkSpeedMask(short inval) {
        for (LinkSpeedMask spd : LinkSpeedMask.values()) {
            if (spd.getSpeedMask() == inval) {
                return spd;
            }
        }
        throw new IllegalArgumentException("Unsupported Link Speed "
                + StringUtils.shortHexString(inval));
    }

    public static List<LinkSpeedMask> getSpeedMasks(short inval) {
        List<LinkSpeedMask> res = new ArrayList<LinkSpeedMask>();
        for (LinkSpeedMask mask : LinkSpeedMask.values()) {
            if ((inval & mask.getSpeedMask()) == mask.getSpeedMask()) {
                res.add(mask);
            }
        }
        return res;
    }

}
