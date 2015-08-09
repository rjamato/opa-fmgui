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
 *  File Name: LinkSpeedMaskViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/01/22 06:02:28  jijunwan
 *  Archive Log:    update to the new link speed defined in FM 308
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/11 21:36:23  jijunwan
 *  Archive Log:    adapt to latest data structure changes on FM
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/22 01:19:41  jijunwan
 *  Archive Log:    updated to match the latest STL spec
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/29 13:48:52  fernande
 *  Archive Log:    Removed repetitive conversion from FE values to API enums
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/22 21:52:34  fernande
 *  Archive Log:    Refactoring PropertyStrings into individual enums
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.EnumMap;

import com.intel.stl.api.configuration.LinkSpeedMask;

public enum LinkSpeedMaskViz {
    STL_LINK_SPEED_RESERVED(LinkSpeedMask.STL_LINK_SPEED_RESERVED, "1-5 Gbps"),
    STL_LINK_SPEED_12_5G(LinkSpeedMask.STL_LINK_SPEED_12_5G, "12.5 Gbps"),
    STL_LINK_SPEED_25G(LinkSpeedMask.STL_LINK_SPEED_25G, "25 Gbps");

    private final static EnumMap<LinkSpeedMask, String> linkSpeedMap =
            new EnumMap<LinkSpeedMask, String>(LinkSpeedMask.class);
    static {
        for (LinkSpeedMaskViz lsmv : LinkSpeedMaskViz.values()) {
            linkSpeedMap.put(lsmv.linkSpeedMask, lsmv.value);
        }
    };

    private final LinkSpeedMask linkSpeedMask;

    private final String value;

    private LinkSpeedMaskViz(LinkSpeedMask linkSpeedMask, String value) {
        this.linkSpeedMask = linkSpeedMask;
        this.value = value;
    }

    public LinkSpeedMask getLinkSpeedMask() {
        return linkSpeedMask;
    }

    public String getValue() {
        return value;
    }

    public static String getLinkSpeedMaskStr(LinkSpeedMask mask) {
        return linkSpeedMap.get(mask);
    }
}
