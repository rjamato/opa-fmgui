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
 *  File Name: SubnetRecord.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/02/11 21:10:21  fernande
 *  Archive Log:    Adding support to remove a subnet (logical delete) from the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/06 15:03:04  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/30 15:37:20  fernande
 *  Archive Log:    Changed hashCode methods to use generated code by Eclipse
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/12 19:58:38  fernande
 *  Archive Log:    We now save ImageInfo and GroupInfo to the database. As they are retrieved by the UI, they are buffered and then saved at certain thresholds.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/27 17:04:40  fernande
 *  Archive Log:    Database changes to add Notice and ImageInfo tables to the schema database
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/20 16:56:12  fernande
 *  Archive Log:    Added basic Entity Manager management to minimize creation of DAOs
 *  Archive Log:    Fixed bugs in database management
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

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.intel.stl.api.subnet.SubnetDescription;

@Entity
@Table(name = "SUBNETS", indexes = { @Index(name = "IDX_SUBNET_NAME",
        columnList = "uniqueName", unique = true) })
@Cacheable(true)
public class SubnetRecord extends DatabaseRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @TableGenerator(name = "TABLE_GEN", pkColumnValue = "SUBNET_SEQ",
            allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    @Column(name = "subnetId")
    private long id;

    @Column(name = "uniqueName", length = 80)
    private String uniqueName;

    private SubnetDescription subnetDescription;

    @ManyToMany(mappedBy = "eventSubnets")
    private Set<EventRuleRecord> eventRules;

    @OneToOne(fetch = LAZY)
    private TopologyRecord topology;

    @OneToMany(fetch = LAZY, mappedBy = "subnet")
    private Set<GroupConfigRecord> groups;

    @OneToMany(fetch = LAZY, mappedBy = "subnet")
    private Set<ImageInfoRecord> images;

    public SubnetRecord() {
    }

    public SubnetRecord(SubnetDescription subnetDescription) {
        this.id = subnetDescription.getSubnetId();
        this.subnetDescription = subnetDescription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    /**
     * 
     * <i>Description:</i> sets the value of the unique name. Column uniqueName
     * is used for a unique index in the database. It's built from the
     * Subnet.name but it adds a character at the beginning to indicate whether
     * the subnet is in use ("1") or logically deleted ("0").
     * 
     * @param uniqueName
     *            the unique name according to the rules described above.
     */
    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public SubnetDescription getSubnetDescription() {
        subnetDescription.setSubnetId(id);
        return subnetDescription;
    }

    public void setSubnetDescription(SubnetDescription subnetDescription) {
        this.subnetDescription = subnetDescription;
    }

    public Set<EventRuleRecord> getEventRules() {
        return eventRules;
    }

    public void setEventRules(Set<EventRuleRecord> eventRules) {
        this.eventRules = eventRules;
    }

    public TopologyRecord getTopology() {
        return topology;
    }

    public void setTopology(TopologyRecord topology) {
        this.topology = topology;
    }

    public Set<GroupConfigRecord> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupConfigRecord> groups) {
        this.groups = groups;
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
        SubnetRecord other = (SubnetRecord) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
