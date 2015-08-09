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
import java.util.EnumMap;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;

/**
 * @author jijunwan
 * 
 */
public enum NodeTypeViz {
    HFI(NodeType.HFI, STLConstants.K0110_HFI.getValue(),
            STLConstants.K0111_HFIS.getValue(), UIConstants.INTEL_BLUE,
            UIImages.HFI_ICON),
    SWITCH(NodeType.SWITCH, STLConstants.K0017_SWITCH.getValue(),
            STLConstants.K0048_SWITCHES.getValue(), UIConstants.INTEL_GREEN,
            UIImages.SW_ICON),
    ROUTER(NodeType.ROUTER, STLConstants.K0019_ROUTER.getValue(),
            STLConstants.K0050_ROUTERS.getValue(), UIConstants.INTEL_ORANGE,
            UIImages.ROUTER_ICON),
    OTHER(NodeType.OTHER, STLConstants.K0109_OTHERS.getValue(),
            STLConstants.K0109_OTHERS.getValue(), UIConstants.INTEL_GRAY, null);

    public final static String[] names =
            new String[NodeTypeViz.values().length];
    static {
        for (int i = 0; i < names.length; i++) {
            names[i] = NodeTypeViz.values()[i].name;
        }
    };

    public final static Color[] colors = new Color[NodeTypeViz.values().length];
    static {
        for (int i = 0; i < colors.length; i++) {
            colors[i] = NodeTypeViz.values()[i].color;
        }
    };

    private final NodeType type;

    private final String name;

    private final String pluralName;

    private final Color color;

    private final UIImages icon;

    private NodeTypeViz(NodeType type, String name, String pluralName,
            Color color, UIImages icon) {
        this.type = type;
        this.name = name;
        this.pluralName = pluralName;
        this.color = color;
        this.icon = icon;
    }

    /**
     * @return the type
     */
    public NodeType getType() {
        return type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the pluralName
     */
    public String getPluralName() {
        return pluralName;
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

    public static NodeTypeViz getNodeTypeViz(byte id) {
        NodeType type = NodeType.getNodeType(id);
        return getNodeTypeViz(type);
    }

    public static NodeTypeViz getNodeTypeViz(NodeType type) {
        for (NodeTypeViz ntv : NodeTypeViz.values()) {
            if (ntv.type == type) {
                return ntv;
            }
        }
        return null;
    }

    public static long[] getDistributionValues2(
            EnumMap<NodeType, Integer> counts) {
        NodeTypeViz[] all = NodeTypeViz.values();
        long[] res = new long[all.length];
        for (int i = 0; i < res.length; i++) {
            Integer val = counts.get(all[i].type);
            res[i] = val == null ? 0 : val;
        }
        return res;
    }

    public static long[] getDistributionValues(EnumMap<NodeType, Long> counts) {
        NodeTypeViz[] all = NodeTypeViz.values();
        long[] res = new long[all.length];
        for (int i = 0; i < res.length; i++) {
            Long val = counts.get(all[i].type);
            res[i] = val == null ? 0 : val;
        }
        return res;
    }
}
