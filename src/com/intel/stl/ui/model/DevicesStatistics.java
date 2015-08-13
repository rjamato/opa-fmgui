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
 *  File Name: DeviceGroupStatistics.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6.2.1  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
