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
 *  File Name: GroupStatistics.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.9  2015/08/31 22:01:43  jijunwan
 *  Archive Log: PR 130197 - Calculated fabric health above 100% when entire fabric is rebooted
 *  Archive Log: - changed to only use information from ImageInfo for calculation
 *  Archive Log:
 *  Archive Log: Revision 1.8  2015/08/17 18:53:46  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - changed frontend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.7  2015/06/10 19:58:50  jijunwan
 *  Archive Log: PR 129120 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
 *  Archive Log:
 * 
 *  Overview:
 * 
 *  @author: jijunwan
 * 
 ******************************************************************************/
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

        nodeTypesDist = new EnumMap<NodeType, Integer>(NodeType.class);
        nodeTypesDist.put(NodeType.SWITCH, imageInfo.getNumSwitchNodes());
        nodeTypesDist.put(NodeType.HFI, imageInfo.getNumHFIPorts());

        portTypesDist = new EnumMap<NodeType, Long>(NodeType.class);
        portTypesDist.put(NodeType.SWITCH, imageInfo.getNumSwitchPorts());
        portTypesDist.put(NodeType.HFI, (long) imageInfo.getNumHFIPorts());
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
