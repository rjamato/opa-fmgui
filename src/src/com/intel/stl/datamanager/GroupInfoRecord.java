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
 *  File Name: GroupInfoRecord.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.12  2015/08/17 18:49:14  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/07/14 18:56:23  fernande
 *  Archive Log:    PR 129447 - Database size increases a lot over a short period of time. Fixes for Klocwork issues
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/07/13 17:11:33  fernande
 *  Archive Log:    PR 129447 - Database size increases a lot over a short period of time. Undoing additional column in database since we can use sweepTimestamp by adjusting time to Linux time
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/07/10 20:45:49  fernande
 *  Archive Log:    PR 129522 - Notice is not written to database due to topology not found. Moved FE Helpers to the session object and changed the order of initialization for the SubnetContext.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/07/02 20:23:47  fernande
 *  Archive Log:    PR 129447 - Database size increases a lot over a short period of time. Moving Blobs to the database; arrays are now being saved to the database as collection tables.
 *  Archive Log:
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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
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

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    @CollectionTable(name = "GROUP_INFOS_INTERNAL_BWBUCKETS", joinColumns = {
            @JoinColumn(name = "subnetId"), @JoinColumn(name = "groupName"),
            @JoinColumn(name = "sweepTimestamp") })
    private List<Integer> internalBwBuckets;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    @CollectionTable(name = "GROUP_INFOS_SEND_BWBUCKETS", joinColumns = {
            @JoinColumn(name = "subnetId"), @JoinColumn(name = "groupName"),
            @JoinColumn(name = "sweepTimestamp") })
    private List<Integer> sendBwBuckets;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    @CollectionTable(name = "GROUP_INFOS_RECEIVE_BWBUCKETS", joinColumns = {
            @JoinColumn(name = "subnetId"), @JoinColumn(name = "groupName"),
            @JoinColumn(name = "sweepTimestamp") })
    private List<Integer> receiveBwBuckets;

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
        setGroupInfo(groupInfo);
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
        if (groupInfo.getInternalUtilStats() != null) {
            groupInfo.getInternalUtilStats().setBwBuckets(internalBwBuckets);
        }
        if (groupInfo.getSendUtilStats() != null) {
            groupInfo.getSendUtilStats().setBwBuckets(sendBwBuckets);
        }
        if (groupInfo.getRecvUtilStats() != null) {
            groupInfo.getRecvUtilStats().setBwBuckets(receiveBwBuckets);
        }
        return groupInfo;
    }

    public void setGroupInfo(GroupInfoBean groupInfo) {
        if (groupInfo != null) {
            if (groupInfo.getInternalUtilStats() != null) {
                this.internalBwBuckets =
                        groupInfo.getInternalUtilStats().getBwBuckets();
            } else {
                this.internalBwBuckets = new ArrayList<Integer>();
            }
            if (groupInfo.getSendUtilStats() != null) {
                this.sendBwBuckets =
                        groupInfo.getSendUtilStats().getBwBuckets();
            } else {
                this.sendBwBuckets = new ArrayList<Integer>();
            }
            if (groupInfo.getRecvUtilStats() != null) {
                this.receiveBwBuckets =
                        groupInfo.getRecvUtilStats().getBwBuckets();
            } else {
                this.receiveBwBuckets = new ArrayList<Integer>();
            }
        } else {
            this.internalBwBuckets = new ArrayList<Integer>();
            this.sendBwBuckets = new ArrayList<Integer>();
            this.receiveBwBuckets = new ArrayList<Integer>();
        }
        this.groupInfo = groupInfo;
    }

    public List<Integer> getInternalBwBuckets() {
        return internalBwBuckets;
    }

    public void setInternalBwBuckets(List<Integer> internalBwBuckets) {
        this.internalBwBuckets = internalBwBuckets;
    }

    public List<Integer> getSendBwBuckets() {
        return sendBwBuckets;
    }

    public void setSendBwBuckets(List<Integer> sendBwBuckets) {
        this.sendBwBuckets = sendBwBuckets;
    }

    public List<Integer> getReceiveBwBuckets() {
        return receiveBwBuckets;
    }

    public void setReceiveBwBuckets(List<Integer> receiveBwBuckets) {
        this.receiveBwBuckets = receiveBwBuckets;
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
