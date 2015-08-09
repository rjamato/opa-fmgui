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
 *  File Name: NodeRecord.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/02/23 22:28:01  jijunwan
 *  Archive Log:    added method #toString to help debug
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/11/04 14:16:23  fernande
 *  Archive Log:    NoticeManager performance improvements. Removed notice processing-related methods that are not used anymore because notices are processed in batches
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/30 15:37:20  fernande
 *  Archive Log:    Changed hashCode methods to use generated code by Eclipse
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/20 16:56:12  fernande
 *  Archive Log:    Added basic Entity Manager management to minimize creation of DAOs
 *  Archive Log:    Fixed bugs in database management
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/19 20:06:28  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
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

import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.intel.stl.api.subnet.NodeInfoBean;
import com.intel.stl.api.subnet.NodeRecordBean;

@Entity
@Table(name = "NODES", indexes = { @Index(name = "IDX_NODE_PORTGUID",
        columnList = "portGUID") })
public class NodeRecord extends DatabaseRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private long nodeGUID;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "nodeType")
    private NodeTypeRecord type = new NodeTypeRecord();

    private NodeRecordBean node;

    @OneToMany(fetch = LAZY, mappedBy = "node")
    private Set<TopologyNodeRecord> topologies;

    @OneToMany(fetch = LAZY, mappedBy = "fromNode")
    private Set<TopologyLinkRecord> outboundLinks;

    @OneToMany(fetch = LAZY, mappedBy = "toNode")
    private Set<TopologyLinkRecord> inboundLinks;

    public NodeRecord() {
    }

    public NodeRecord(NodeRecordBean node) {
        setNodeFields(node);
        this.node = node;
    }

    public long getNodeGUID() {
        return nodeGUID;
    }

    public void setNodeGUID(long nodeGUID) {
        this.nodeGUID = nodeGUID;
    }

    public NodeTypeRecord getType() {
        return type;
    }

    public void setType(NodeTypeRecord type) {
        this.type = type;
    }

    public NodeRecordBean getNode() {
        NodeInfoBean info = node.getNodeInfo();
        info.setNodeGUID(nodeGUID);
        info.setNodeTypeEnum(type.getNodeType());
        return node;
    }

    public void setNode(NodeRecordBean node) {
        setNodeFields(node);
        this.node = node;
    }

    public Set<TopologyNodeRecord> getTopologies() {
        return topologies;
    }

    public void setTopologies(Set<TopologyNodeRecord> topologies) {
        this.topologies = topologies;
    }

    public Set<TopologyLinkRecord> getOutboundLinks() {
        return outboundLinks;
    }

    public void setOutboundLinks(Set<TopologyLinkRecord> outboundLinks) {
        this.outboundLinks = outboundLinks;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (nodeGUID ^ (nodeGUID >>> 32));
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
        NodeRecord other = (NodeRecord) obj;
        if (nodeGUID != other.nodeGUID) {
            return false;
        }
        return true;
    }

    private void setNodeFields(NodeRecordBean node) {
        NodeInfoBean info = node.getNodeInfo();
        if (info == null) {
            // TODO Create message for this
            throw new IllegalArgumentException(
                    "No NodeInfoBean attached to NodeRecordBean");
        }
        this.nodeGUID = info.getNodeGUID();
        this.type.setId(info.getNodeType());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NodeRecord [nodeGUID=" + nodeGUID + ", node=" + getNode() + "]";
    }

}
