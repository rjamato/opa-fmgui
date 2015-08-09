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
package com.intel.stl.ui.model;

import java.awt.Color;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;

/**
 * @author jijunwan
 * 
 */
public enum StateShortTypeViz {
    FAILED(STLConstants.K0020_FAILED.getValue(), UIConstants.INTEL_RED),
    SKIPPED(STLConstants.K0021_SKIPPED.getValue(), UIConstants.INTEL_ORANGE),
    NORMAL(STLConstants.K0022_NORMAL.getValue(), UIConstants.INTEL_GRAY);

    public final static String[] names =
            new String[StateShortTypeViz.values().length];
    static {
        for (int i = 0; i < names.length; i++) {
            names[i] = StateShortTypeViz.values()[i].name;
        }
    };

    public final static Color[] colors =
            new Color[StateShortTypeViz.values().length];
    static {
        for (int i = 0; i < colors.length; i++) {
            colors[i] = StateShortTypeViz.values()[i].color;
        }
    };

    private final String name;

    private final Color color;

    private StateShortTypeViz(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    public static long[] getDistributionValues(long failed, long skipped,
            long total) {
        return new long[] { failed, skipped, total - failed - skipped };
    }

}
