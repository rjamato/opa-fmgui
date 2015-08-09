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
 *  File Name: TopologyNode.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/02/23 22:28:01  jijunwan
 *  Archive Log:    added method #toString to help debug
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/30 15:37:20  fernande
 *  Archive Log:    Changed hashCode methods to use generated code by Eclipse
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/11 22:08:02  fernande
 *  Archive Log:    Changes to add more entities to database schema
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/23 19:43:19  fernande
 *  Archive Log:    Saving topology to database
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

import com.intel.stl.api.StringUtils;

@Embeddable
public class TopologyNodeId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "topologyId")
    private long topologyId;

    @Column(name = "nodeGUID")
    private long topologyNode;

    public long getTopologyId() {
        return topologyId;
    }

    public void setTopologyId(long topologyId) {
        this.topologyId = topologyId;
    }

    public long getTopologyNode() {
        return topologyNode;
    }

    public void setTopologyNode(long nodeGUID) {
        this.topologyNode = nodeGUID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (topologyId ^ (topologyId >>> 32));
        result = prime * result + (int) (topologyNode ^ (topologyNode >>> 32));
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
        TopologyNodeId other = (TopologyNodeId) obj;
        if (topologyId != other.topologyId) {
            return false;
        }
        if (topologyNode != other.topologyNode) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TopologyNodeId [topologyId=" + topologyId + ", topologyNode="
                + StringUtils.longHexString(topologyNode) + "]";
    }

}
