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
 *  File Name: LinkWidthMaskViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3.2.1  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
