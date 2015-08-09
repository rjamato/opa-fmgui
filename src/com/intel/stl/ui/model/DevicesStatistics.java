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
 *  File Name: DeviceGroupStatistics.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/02/23 22:47:30  jijunwan
 *  Archive Log:    added method #toString to help debug
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/04 21:44:16  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/22 16:40:17  jijunwan
 *  Archive Log:    separated other ports viz for the ports not in a subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/15 22:00:21  jijunwan
 *  Archive Log:    display other ports on UI
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/21 13:48:18  jijunwan
 *  Archive Log:    added # internal, external ports on performance page
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/05 17:31:13  jijunwan
 *  Archive Log:    renamed DeviceGroupStatistics to DevicesStatistics since we are using it for vFabric as well
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/15 22:35:43  rjtierne
 *  Archive Log:    Removed method calculateNumPorts()
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/15 18:23:02  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.EnumMap;

import com.intel.stl.api.subnet.NodeType;

public class DevicesStatistics {

    private long numActivePorts;

    private int numNodes;

    private EnumMap<NodeType, Integer> nodeTypesDist;

    private EnumMap<NodeType, Long> portTypesDist;

    private long internalPorts;

    private long externalPorts;

    public long getNumAtivePorts() {
        return numActivePorts;
    }

    public void setNumActivePorts(long numPorts) {
        this.numActivePorts = numPorts;
    }

    public int getNumNodes() {
        return numNodes;
    }

    public void setNumNodes(int numNodes) {
        this.numNodes = numNodes;
    }

    public EnumMap<NodeType, Integer> getNodeTypesDist() {
        return nodeTypesDist;
    }

    public void setNodeTypesDist(EnumMap<NodeType, Integer> nodeTypesDist) {
        this.nodeTypesDist = nodeTypesDist;
    }

    public EnumMap<NodeType, Long> getPortTypesDist() {
        return portTypesDist;
    }

    public void setPortTypesDist(EnumMap<NodeType, Long> portTypesDist) {
        this.portTypesDist = portTypesDist;
    }

    /**
     * @return the internalPorts
     */
    public long getInternalPorts() {
        return internalPorts;
    }

    /**
     * @param internalPorts
     *            the internalPorts to set
     */
    public void setInternalPorts(long internalPorts) {
        this.internalPorts = internalPorts;
    }

    /**
     * @return the externalPorts
     */
    public long getExternalPorts() {
        return externalPorts;
    }

    /**
     * @param externalPorts
     *            the externalPorts to set
     */
    public void setExternalPorts(long externalPorts) {
        this.externalPorts = externalPorts;
    }

    public long getOtherPorts() {
        Long count = portTypesDist.get(NodeType.OTHER);
        return count == null ? 0 : count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DevicesStatistics [numActivePorts=" + numActivePorts
                + ", numNodes=" + numNodes + ", nodeTypesDist=" + nodeTypesDist
                + ", portTypesDist=" + portTypesDist + ", internalPorts="
                + internalPorts + ", externalPorts=" + externalPorts + "]";
    }

}
