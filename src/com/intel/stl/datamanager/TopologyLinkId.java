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
 *  File Name: TopologyLink.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/02/04 21:37:57  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/30 15:37:20  fernande
 *  Archive Log:    Changed hashCode methods to use generated code by Eclipse
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/26 15:24:31  jijunwan
 *  Archive Log:    changed port index to be byte to be consistent with port number
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/26 15:01:18  jijunwan
 *  Archive Log:    changed port index to be byte to be consistent with port number
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/11 22:08:02  fernande
 *  Archive Log:    Changes to add more entities to database schema
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.datamanager;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TopologyLinkId implements Serializable {

    private static final long serialVersionUID = 1L;

    // The following fields names have been selected so that the ascending
    // alphabetical order yields the desired key for the table: topologyId +
    // nodeGUID + port
    // This is due to a limitation in Hibernate
    @Column(name = "topologyId")
    private long linkTopology;

    @Column(name = "fromNodeGUID")
    private long sourceNode;

    @Column(name = "fromPort")
    private short sourcePort;

    @Column(name = "toNodeGUID")
    private long targetNode;

    @Column(name = "toPort")
    private short targetPort;

    public long getLinkTopology() {
        return linkTopology;
    }

    public void setLinkTopology(long topologyId) {
        this.linkTopology = topologyId;
    }

    public long getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(long sourceNodeGUID) {
        this.sourceNode = sourceNodeGUID;
    }

    public short getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(short sourcePort) {
        this.sourcePort = sourcePort;
    }

    public long getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(long targetNodeGUID) {
        this.targetNode = targetNodeGUID;
    }

    public short getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(short targetPort) {
        this.targetPort = targetPort;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (linkTopology ^ (linkTopology >>> 32));
        result = prime * result + (int) (sourceNode ^ (sourceNode >>> 32));
        result = prime * result + sourcePort;
        result = prime * result + (int) (targetNode ^ (targetNode >>> 32));
        result = prime * result + targetPort;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TopologyLinkId other = (TopologyLinkId) obj;
        if (linkTopology != other.linkTopology) {
            return false;
        }
        if (sourceNode != other.sourceNode) {
            return false;
        }
        if (sourcePort != other.sourcePort) {
            return false;
        }
        if (targetNode != other.targetNode) {
            return false;
        }
        if (targetPort != other.targetPort) {
            return false;
        }
        return true;
    }
}
