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

import static com.intel.stl.ui.common.STLConstants.K0029_CRITICAL;
import static com.intel.stl.ui.common.STLConstants.K0030_ERROR;
import static com.intel.stl.ui.common.STLConstants.K0031_WARNING;
import static com.intel.stl.ui.common.STLConstants.K0032_INFORMATIONAL;
import static com.intel.stl.ui.common.UIConstants.INTEL_GREEN;
import static com.intel.stl.ui.common.UIConstants.INTEL_ORANGE;
import static com.intel.stl.ui.common.UIConstants.INTEL_RED;
import static com.intel.stl.ui.common.UIConstants.INTEL_YELLOW;

import java.awt.Color;
import java.util.EnumMap;

import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.ui.common.UIImages;

/**
 * @author jijunwan
 * 
 */
public enum NoticeSeverityViz {
    CRITICAL(
            NoticeSeverity.CRITICAL,
            K0029_CRITICAL.getValue(),
            INTEL_RED,
            UIImages.CRITICAL_ICON),
    ERROR(NoticeSeverity.ERROR, 
            K0030_ERROR.getValue(), 
            INTEL_ORANGE, 
            UIImages.ERROR_ICON),
    WARNING(
            NoticeSeverity.WARNING,
            K0031_WARNING.getValue(),
            INTEL_YELLOW,
            UIImages.WARNING_ICON),
    INFO(NoticeSeverity.INFO, 
            K0032_INFORMATIONAL.getValue(), 
            INTEL_GREEN, 
            UIImages.INFORMATION_ICON);

    public final static String[] names = new String[NoticeSeverityViz.values().length];
    static {
        for (int i = 0; i < names.length; i++) {
            names[i] = NoticeSeverityViz.values()[i].name;
        }
    };

    public final static Color[] colors = new Color[NoticeSeverityViz.values().length];
    static {
        for (int i = 0; i < colors.length; i++) {
            colors[i] = NoticeSeverityViz.values()[i].color;
        }
    };

    private final NoticeSeverity severity;

    private final String name;

    private final Color color;

    private final UIImages icon;

    private NoticeSeverityViz(NoticeSeverity severity, String name,
            Color color, UIImages icon) {
        this.severity = severity;
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    /**
     * @return the severity
     */
    public NoticeSeverity getSeverity() {
        return severity;
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

    /**
     * @return the icon
     */
    public UIImages getIcon() {
        return icon;
    }

    public static int[] getDistributionValues(
            EnumMap<NoticeSeverity, Integer> counts) {
        NoticeSeverityViz[] all = NoticeSeverityViz.values();
        int[] res = new int[all.length];
        for (int i = 0; i < all.length; i++) {
            Integer val = counts.get(all[i].severity);
            res[i] = val == null ? 0 : val;
        }
        return res;
    }

    public static NoticeSeverity getNoticeSeverityFor(String name) {
        NoticeSeverityViz[] all = NoticeSeverityViz.values();
        for (int i = 0; i < all.length; i++) {
            if (all[i].getName().equals(name)) {
                return all[i].getSeverity();
            }
        }
        return null;
    }

    public static NoticeSeverityViz getNoticeSeverityVizFor(NoticeSeverity type) {
        NoticeSeverityViz[] values = NoticeSeverityViz.values();
        for (int i = 0; i < values.length; i++) {
            if (type == values[i].getSeverity()) {
                return values[i];
            }
        }
        return null;
    }
}
