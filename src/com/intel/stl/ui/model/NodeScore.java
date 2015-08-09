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

import java.io.Serializable;

import javax.swing.ImageIcon;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;

/**
 * @author jijunwan
 * 
 */
public class NodeScore extends TimedScore implements Comparable<NodeScore>,
        Serializable {
    private static final long serialVersionUID = -375009123447030706L;

    private final String name;

    private final NodeType type;

    private final int lid;

    public NodeScore(String name, NodeType type, int lid, long time,
            double score) {
        super(time, score);
        this.name = name;
        this.type = type;
        this.lid = lid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public NodeType getType() {
        return type;
    }

    /**
     * @return the lid
     */
    public int getLid() {
        return lid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(NodeScore o) {
        double s1 = getScore();
        double s2 = o.getScore();
        return Double.compare(s1, s2);
    }

    public ImageIcon getIcon() {
        NodeTypeViz viz = NodeTypeViz.getNodeTypeViz(type.getId());
        if (viz != null) {
            return viz.getIcon().getImageIcon();
        } else {
            throw new IllegalArgumentException("Couldn't find NodeTypeViz for "
                    + type);
        }
    }

    public String getDescription() {
        double score = getScore();
        return UILabels.STL10211_WORST_NODE.getDescription(name,
                UIConstants.DECIMAL.format(score));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NodeScore [getTime()=" + getTime() + ", getScore()="
                + getScore() + ", lid=" + lid + ", name=" + name + ", type="
                + type + "]";
    }

}
