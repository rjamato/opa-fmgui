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
 *  File Name: LinkWidthMaskViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/01/11 21:36:23  jijunwan
 *  Archive Log:    adapt to latest data structure changes on FM
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/22 01:19:41  jijunwan
 *  Archive Log:    updated to match the latest STL spec
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

import static com.intel.stl.ui.common.STLConstants.K0368_NO_STATE_CHANGE;

import java.util.EnumMap;

import com.intel.stl.api.configuration.LinkWidthMask;

public enum LinkWidthMaskViz {

    STL_LINK_WIDTH_NOP(LinkWidthMask.STL_LINK_WIDTH_NOP, K0368_NO_STATE_CHANGE
            .getValue()),
    STL_LINK_WIDTH_1X(LinkWidthMask.STL_LINK_WIDTH_1X, "1x"),
    STL_LINK_WIDTH_2X(LinkWidthMask.STL_LINK_WIDTH_2X, "2x"),
    STL_LINK_WIDTH_3X(LinkWidthMask.STL_LINK_WIDTH_3X, "3x"),
    STL_LINK_WIDTH_4X(LinkWidthMask.STL_LINK_WIDTH_4X, "4x");

    private final static EnumMap<LinkWidthMask, String> linkWidthMap =
            new EnumMap<LinkWidthMask, String>(LinkWidthMask.class);
    static {
        for (LinkWidthMaskViz lwmv : LinkWidthMaskViz.values()) {
            linkWidthMap.put(lwmv.linkWidthMask, lwmv.value);
        }
    };

    private final LinkWidthMask linkWidthMask;

    private final String value;

    private LinkWidthMaskViz(LinkWidthMask linkWidthMask, String value) {
        this.linkWidthMask = linkWidthMask;
        this.value = value;
    }

    public LinkWidthMask getLinkWidthMask() {
        return linkWidthMask;
    }

    public String getValue() {
        return value;
    }

    public static LinkWidthMaskViz getLinkWidthMaskViz(LinkWidthMask mask) {
        for (LinkWidthMaskViz lwmv : LinkWidthMaskViz.values()) {
            if (lwmv.linkWidthMask == mask) {
                return lwmv;
            }
        }
        return null;
    }

    public static String getLinkWidthMaskStr(LinkWidthMask mask) {
        return linkWidthMap.get(mask);
    }
}
