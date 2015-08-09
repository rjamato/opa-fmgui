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

import java.util.Arrays;
import java.util.EnumMap;

import com.intel.stl.api.performance.ImageInfoBean;
import com.intel.stl.api.performance.SMInfoDataBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.SubnetDescription;

/**
 * @author jijunwan
 * 
 */
public class GroupStatistics {
    private final SubnetDescription subnet;

    private long numLinks;

    private EnumMap<NodeType, Integer> nodeTypesDist;

    private EnumMap<NodeType, Long> portTypesDist;

    private int numFailedNodes;

    private long numFailedPorts;

    private int numSkippedNodes;

    private long numSkippedPorts;

    private int numSMs;

    private SMInfoDataBean[] SMInfo;

    private long msmUptimeInSeconds;

    public GroupStatistics(SubnetDescription subnet, ImageInfoBean imageInfo) {
        this.subnet = subnet;
        if (imageInfo == null) {
            throw new IllegalArgumentException("No imageInfo");
        }

        setImageInfo(imageInfo);
    }

    public void setImageInfo(ImageInfoBean imageInfo) {
        numLinks = imageInfo.getNumLinks();
        numFailedNodes = imageInfo.getNumFailedNodes();
        numFailedPorts = imageInfo.getNumFailedPorts();
        numSkippedNodes = imageInfo.getNumSkippedNodes();
        numSkippedPorts = imageInfo.getNumSkippedPorts();
        numSMs = imageInfo.getNumSMs();
        SMInfo = imageInfo.getSMInfo();
    }

    /**
     * @return the msmUptimeInSeconds
     */
    public long getMsmUptimeInSeconds() {
        return msmUptimeInSeconds;
    }

    /**
     * @param msmUptimeInSeconds
     *            the msmUptimeInSeconds to set
     */
    public void setMsmUptimeInSeconds(long msmUptimeInSeconds) {
        this.msmUptimeInSeconds = msmUptimeInSeconds;
    }

    /**
     * @return the numLinks
     */
    public long getNumLinks() {
        return numLinks;
    }

    public int getNumNodes() {
        int sum = 0;
        if (nodeTypesDist != null) {
            for (Integer count : nodeTypesDist.values()) {
                sum += count;
            }
        }
        return sum;
    }

    /**
     * @return the nodesTypeDist
     */
    public EnumMap<NodeType, Integer> getNodeTypesDist() {
        return nodeTypesDist;
    }

    /**
     * @param nodeTypesDist
     *            the nodesTypeDist to set
     */
    public void setNodeTypesDist(EnumMap<NodeType, Integer> nodeTypesDist) {
        this.nodeTypesDist = nodeTypesDist;
    }

    public long getNumActivePorts() {
        long sum = 0;
        if (portTypesDist != null) {
            for (NodeType type : portTypesDist.keySet()) {
                Long count = portTypesDist.get(type);
                if (type != NodeType.OTHER && count != null) {
                    sum += count;
                }
            }
        }
        return sum;
    }

    /**
     * @return the portsTypeDist
     */
    public EnumMap<NodeType, Long> getPortTypesDist() {
        return portTypesDist;
    }

    /**
     * @param portTypesDist
     *            the portsTypeDist to set
     */
    public void setPortTypesDist(EnumMap<NodeType, Long> portTypesDist) {
        this.portTypesDist = portTypesDist;
    }

    /**
     * <i>Description:</i>
     * 
     * @return
     */
    public long getOtherPorts() {
        Long count = portTypesDist.get(NodeType.OTHER);
        return count == null ? 0 : count;
    }

    /**
     * @return the name
     */
    public String getName() {
        return subnet.getName();
    }

    /**
     * @return the numFailedNodes
     */
    public int getNumFailedNodes() {
        return numFailedNodes;
    }

    /**
     * @return the numFailedPorts
     */
    public long getNumFailedPorts() {
        return numFailedPorts;
    }

    /**
     * @return the numSkippedNodes
     */
    public int getNumSkippedNodes() {
        return numSkippedNodes;
    }

    /**
     * @return the numSkippedPorts
     */
    public long getNumSkippedPorts() {
        return numSkippedPorts;
    }

    /**
     * @return the numSMs
     */
    public int getNumSMs() {
        return numSMs;
    }

    /**
     * @return the sMInfo
     */
    public SMInfoDataBean[] getSMInfo() {
        return SMInfo;
    }

    public SMInfoDataBean getMasterSM() {
        if (SMInfo != null && SMInfo.length > 0) {
            return SMInfo[0];
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GroupStatistics [name=" + subnet.getName() + ", numLinks="
                + numLinks + ", nodeTypesDist=" + nodeTypesDist
                + ", portTypesDist=" + portTypesDist + ", numFailedNodes="
                + numFailedNodes + ", numFailedPorts=" + numFailedPorts
                + ", numSkippedNodes=" + numSkippedNodes + ", numSkippedPorts="
                + numSkippedPorts + ", numSMs=" + numSMs + ", SMInfo="
                + Arrays.toString(SMInfo) + ", msmUptimeInSeconds="
                + msmUptimeInSeconds + "]";
    }

}
