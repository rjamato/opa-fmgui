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
 *  File Name: GroupInfoRecord.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/02/12 20:20:22  jijunwan
 *  Archive Log:    changed back to use timestamp as part of id
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/12 19:30:01  jijunwan
 *  Archive Log:    introduced interface ITimestamped, and all timimg attributes implemented it, so we can easily know which attribute is associated with timestamp
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/06 15:03:04  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/03 05:36:38  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/30 15:37:20  fernande
 *  Archive Log:    Changed hashCode methods to use generated code by Eclipse
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/12 19:58:38  fernande
 *  Archive Log:    We now save ImageInfo and GroupInfo to the database. As they are retrieved by the UI, they are buffered and then saved at certain thresholds.
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

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.intel.stl.api.performance.GroupInfoBean;

@Entity
@Table(name = "GROUP_INFOS")
public class GroupInfoRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private GroupInfoId id = new GroupInfoId();

    @ManyToOne(fetch = LAZY)
    @JoinColumns({
            @JoinColumn(name = "subnetName", insertable = false,
                    updatable = false),
            @JoinColumn(name = "groupName", insertable = false,
                    updatable = false) })
    private GroupConfigRecord groupConfig;

    private GroupInfoBean groupInfo;

    public GroupInfoRecord() {
    }

    public GroupInfoRecord(long subnetId, GroupInfoBean groupInfo) {
        GroupConfigId groupId = id.getGroupID();
        if (groupId == null) {
            groupId = new GroupConfigId();
            id.setGroupID(groupId);
        }
        groupId.setFabricId(subnetId);
        groupId.setSubnetGroup(groupInfo.getGroupName());
        this.id.setSweepTimestamp(groupInfo.getTimestamp());
        this.groupInfo = groupInfo;
    }

    public GroupInfoId getId() {
        return id;
    }

    public void setId(GroupInfoId id) {
        this.id = id;
    }

    public GroupConfigRecord getGroupConfig() {
        return groupConfig;
    }

    public void setGroupConfig(GroupConfigRecord groupConfig) {
        this.groupConfig = groupConfig;
    }

    public GroupInfoBean getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfoBean groupInfo) {
        this.groupInfo = groupInfo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        GroupInfoRecord other = (GroupInfoRecord) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
