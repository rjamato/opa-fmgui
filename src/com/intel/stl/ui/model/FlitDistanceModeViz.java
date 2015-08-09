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
 *  File Name: PortLinkModeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/01/11 21:36:23  jijunwan
 *  Archive Log:    adapt to latest data structure changes on FM
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/18 21:31:22  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.EnumMap;

import com.intel.stl.api.configuration.FlitDistanceMode;

public enum FlitDistanceModeViz {
    NOP(FlitDistanceMode.NOP, "NOP"),
    MODE_1(FlitDistanceMode.MODE_1, "MODE 1"),
    MODE_2(FlitDistanceMode.MODE_2, "MODE 2");

    private final static EnumMap<FlitDistanceMode, String> flitDistanceModeMap =
            new EnumMap<FlitDistanceMode, String>(FlitDistanceMode.class);
    static {
        for (FlitDistanceModeViz fdmv : FlitDistanceModeViz.values()) {
            flitDistanceModeMap.put(fdmv.flitDistanceMode, fdmv.value);
        }
    };

    private final FlitDistanceMode flitDistanceMode;

    private final String value;

    private FlitDistanceModeViz(FlitDistanceMode portLinkMode, String value) {
        this.flitDistanceMode = portLinkMode;
        this.value = value;
    }

    public static String getFlitDistanceModeStr(FlitDistanceMode mode) {
        return flitDistanceModeMap.get(mode);
    }

    public static String getFlitDistanceModeStr(byte mode) {
        FlitDistanceMode[] modes = FlitDistanceMode.getFlitDistanceModes(mode);
        String comma = "";
        StringBuffer flitDistanceModeStr = new StringBuffer();
        for (int i = 0; i < modes.length; i++) {
            flitDistanceModeStr.append(comma);
            flitDistanceModeStr.append(flitDistanceModeMap.get(modes[i]));
            comma = ", ";
        }
        return flitDistanceModeStr.toString();
    }
}
