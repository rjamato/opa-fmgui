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
 *  File Name: Topology.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TOPOLOGIES")
public class TopologyRecord extends DatabaseRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private long id;

    private long numNodes;

    private long numCAs;

    private long numSwitches;

    private int numRouters;

    private int numUnknown;

    @OneToMany(fetch = LAZY, mappedBy = "topology")
    private Set<TopologyNodeRecord> nodes;

    @OneToMany(fetch = LAZY, mappedBy = "topology")
    private Set<TopologyLinkRecord> links;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumNodes() {
        return numNodes;
    }

    public void setNumNodes(long numNodes) {
        this.numNodes = numNodes;
    }

    public long getNumCAs() {
        return numCAs;
    }

    public void setNumCAs(long numCAs) {
        this.numCAs = numCAs;
    }

    public long getNumSwitches() {
        return numSwitches;
    }

    public void setNumSwitches(long numSwitches) {
        this.numSwitches = numSwitches;
    }

    public int getNumRouters() {
        return numRouters;
    }

    public void setNumRouters(int numRouters) {
        this.numRouters = numRouters;
    }

    public int getNumUnknown() {
        return numUnknown;
    }

    public void setNumUnknown(int numUnknown) {
        this.numUnknown = numUnknown;
    }

    public Set<TopologyNodeRecord> getNodes() {
        return nodes;
    }

    public void setNodes(Set<TopologyNodeRecord> nodes) {
        this.nodes = nodes;
    }

    public Set<TopologyLinkRecord> getLinks() {
        return links;
    }

    public void setLinks(Set<TopologyLinkRecord> links) {
        this.links = links;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TopologyRecord other = (TopologyRecord) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
