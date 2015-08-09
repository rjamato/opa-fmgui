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
 *  File Name: MTUSizeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/01/20 21:45:09  jijunwan
 *  Archive Log:    Bug 126600 - Null pointer exception when trying to view properties page of Switch and HFI ports
 *  Archive Log:    improved to treat zero as invalid value
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

import com.intel.stl.api.configuration.MTUSize;
import com.intel.stl.ui.common.STLConstants;

public enum MTUSizeViz {
    INVALID(MTUSize.INVALID, STLConstants.K0039_NOT_AVAILABLE.getValue()),
    IB_MTU_256(MTUSize.IB_MTU_256, "256"),
    IB_MTU_512(MTUSize.IB_MTU_512, "512"),
    IB_MTU_1024(MTUSize.IB_MTU_1024, "1024"),
    IB_MTU_2048(MTUSize.IB_MTU_2048, "2048"),
    IB_MTU_4096(MTUSize.IB_MTU_4096, "4096"),
    STL_MTU_8192(MTUSize.STL_MTU_8192, "8192"),
    STL_MTU_10240(MTUSize.STL_MTU_10240, "10240");

    private final static EnumMap<MTUSize, String> mtuSizeMap =
            new EnumMap<MTUSize, String>(MTUSize.class);
    static {
        for (MTUSizeViz msv : MTUSizeViz.values()) {
            mtuSizeMap.put(msv.mtuSize, msv.value);
        }
    };

    private final MTUSize mtuSize;

    private final String value;

    private MTUSizeViz(MTUSize mtuSize, String value) {
        this.mtuSize = mtuSize;
        this.value = value;
    }

    public MTUSize getMTUSize() {
        return mtuSize;
    }

    public String getValue() {
        return value;
    }

    public static MTUSizeViz getMTUSizeViz(byte size) {
        for (MTUSizeViz msv : MTUSizeViz.values()) {
            if (msv.mtuSize.getSize() == size) {
                return msv;
            }
        }
        return null;
    }

    public static String getMTUSizeStr(MTUSize mtuSize) {
        return mtuSizeMap.get(mtuSize);
    }
}
