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
 *  File Name: TopologyNodeRecord.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/02/23 22:33:23  jijunwan
 *  Archive Log:    changed to use id and active state in hash code calculation so nodes/links with different active states are distinguished and new topology will be created in DB
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/23 22:30:37  jijunwan
 *  Archive Log:    changed to use lid and active state in hash code calculation so nodes with different active states are distinguished and new topology will be created in DB
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/30 15:37:20  fernande
 *  Archive Log:    Changed hashCode methods to use generated code by Eclipse
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/28 14:56:52  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/19 20:06:28  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/11 22:08:02  fernande
 *  Archive Log:    Changes to add more entities to database schema
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/23 19:43:01  fernande
 *  Archive Log:    Saving topology to database
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.datamanager;

import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "TOPOLOGIES_NODES", indexes = { @Index(name = "IDX_NODE_LID",
        columnList = "lid") })
public class TopologyNodeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TopologyNodeId id = new TopologyNodeId();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "topologyId", insertable = false, updatable = false)
    private TopologyRecord topology;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "nodeGUID", insertable = false, updatable = false)
    private NodeRecord node;

    @Transient
    private boolean lidChanged = false;

    private int lid;

    private boolean active = true;

    public TopologyNodeId getId() {
        return id;
    }

    public void setId(TopologyNodeId id) {
        this.id = id;
    }

    public TopologyRecord getTopology() {
        return topology;
    }

    public void setTopology(TopologyRecord topology) {
        this.topology = topology;
        id.setTopologyId(topology.getId());
    }

    public NodeRecord getNode() {
        node.getNode().setActive(active);
        // we join NodeRecord by guid. A node record can have a different lid.
        // So we set its current lid here
        node.getNode().setLid(lid);
        return node;
    }

    public void setNode(NodeRecord node) {
        this.node = node;
        this.id.setTopologyNode(node.getNodeGUID());
    }

    public boolean isLidChanged() {
        return lidChanged;
    }

    public void setLidChanged(boolean lidChanged) {
        this.lidChanged = lidChanged;
    }

    public int getLid() {
        return this.lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active
     *            the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (active ? 1231 : 1237);
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        TopologyNodeRecord other = (TopologyNodeRecord) obj;
        if (active != other.active) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
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
        return "TopologyNodeRecord [id=" + id + ", lidChanged=" + lidChanged
                + ", lid=" + lid + ", active=" + active + ", node=" + node
                + "]";
    }

}
