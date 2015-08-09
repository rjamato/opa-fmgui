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
package com.intel.stl.api.subnet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * NOTE: the way we calculate hashcode and equals have two defects:
 * <ol>
 * <li>subnets with host name described in different ways (host/ip etc) will be
 * identified as different subetnets
 * <li>subnets with secureConnect turned off but associated with different certs
 * info will be identified as different subetnets
 * </ol>
 * 
 * @author jijunwan
 * 
 */

public class SubnetDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Status {
        UNKNOWN,
        VALID,
        INVALID
    }

    private long subnetId;

    private String name;

    private int primaryFEIndex;

    private int currentFEIndex;

    private List<HostInfo> feList;

    private String currentUser;

    private boolean autoConnect;

    private boolean topologyUpdated;

    private AtomicBoolean failoverInProgress = new AtomicBoolean(false);

    private List<SMRecordBean> smList;

    private Status lastStatus = Status.UNKNOWN;

    private long statusTimestamp;

    public SubnetDescription() {
    }

    public SubnetDescription(String name) {
        this.name = name;
    }

    public SubnetDescription(String name, String host, int port) {
        this.name = name;
        HostInfo hostInfo = new HostInfo(host, port);
        getFEList().add(hostInfo);
    }

    public long getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(long subnetId) {
        this.subnetId = subnetId;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public int getPrimaryFEIndex() {
        return primaryFEIndex;
    }

    public void setPrimaryFEIndex(int primary) {
        checkIndex(primary);
        this.primaryFEIndex = primary;
    }

    public int getCurrentFEIndex() {
        return currentFEIndex;
    }

    public void setCurrentFEIndex(int current) {
        checkIndex(current);
        this.currentFEIndex = current;
    }

    public void setFEList(List<HostInfo> feList) {
        this.feList = feList;
    }

    /**
     * @return the host
     */
    public List<HostInfo> getFEList() {
        if (feList == null) {
            feList = new ArrayList<HostInfo>();
        }
        return feList;
    }

    public void setSMList(List<SMRecordBean> smList) {
        this.smList = smList;
    }

    public List<SMRecordBean> getSMList() {
        return smList;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * @return the autoConnect
     */
    public boolean isAutoConnect() {
        return autoConnect;
    }

    /**
     * @return the lastStatus
     */
    public Status getLastStatus() {
        return lastStatus;
    }

    /**
     * @param lastStatus
     *            the lastStatus to set
     */
    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
        this.statusTimestamp = System.currentTimeMillis();
    }

    /**
     * @param statusTimestampe
     *            the statusTimestampe to set
     */
    public void setStatusTimestamp(long statusTimestamp) {
        this.statusTimestamp = statusTimestamp;
    }

    /**
     * @return the statusTimestampe
     */
    public long getStatusTimestamp() {
        return statusTimestamp;
    }

    /**
     * @param autoConnect
     *            the autoConnect to set
     */
    public void setAutoConnect(boolean autoConnect) {
        this.autoConnect = autoConnect;
    }

    public boolean isTopologyUpdated() {
        return topologyUpdated;
    }

    public void setTopologyUpdated(boolean topologyUpdated) {
        this.topologyUpdated = topologyUpdated;
    }

    public boolean isFailoverInProgress() {
        return failoverInProgress.get();
    }

    public boolean setFailoverInProgress(boolean expected,
            boolean failoverInProgress) {
        return this.failoverInProgress.compareAndSet(expected,
                failoverInProgress);
    }

    public HostInfo getPrimaryFE() {
        return getHostInfo(primaryFEIndex);
    }

    public HostInfo getCurrentFE() {
        return getHostInfo(currentFEIndex);
    }

    private HostInfo getHostInfo(int index) {
        if (feList == null) {
            throw new IllegalArgumentException("No list of FEs");
        }
        checkIndex(index);
        return feList.get(index);
    }

    private void checkIndex(int index) {
        if (!(index >= 0 && (index < feList.size()))) {
            throw new IllegalArgumentException("Invalid index '" + index
                    + "' for list of FEs (" + feList.size() + ")");
        }
    }

    public SubnetDescription copy() {
        SubnetDescription subnet = new SubnetDescription();
        subnet.subnetId = subnetId;
        subnet.name = name;
        if (feList != null) {
            List<HostInfo> newFeList = new ArrayList<HostInfo>(feList);
            subnet.setFEList(newFeList);
        } else {
            subnet.setFEList(null);
        }
        subnet.primaryFEIndex = primaryFEIndex;
        subnet.currentFEIndex = currentFEIndex;
        subnet.lastStatus = lastStatus;
        subnet.statusTimestamp = statusTimestamp;
        subnet.topologyUpdated = topologyUpdated;
        return subnet;
    }

    /**
     * 
     * <i>Description:</i> set the basic information based on given subnet.
     * 
     * @param subnet
     */
    public void setContent(SubnetDescription subnet) {
        if (!name.equals(subnet.name)) {
            throw new IllegalArgumentException(
                    "Can not set content of another subnet");
        }

        primaryFEIndex = subnet.primaryFEIndex;
        currentFEIndex = subnet.currentFEIndex;
        autoConnect = subnet.autoConnect;
        lastStatus = subnet.lastStatus;
        feList = subnet.feList;
        currentUser = subnet.currentUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SubnetDescription [id=" + subnetId + ", name=" + name
                + ", list FEs=" + feList + ", autoConnect=" + autoConnect
                + ", topologyUpdated=" + topologyUpdated + ", lastStatus="
                + lastStatus + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (subnetId ^ (subnetId >>> 32));
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
        SubnetDescription other = (SubnetDescription) obj;
        if (subnetId != other.subnetId)
            return false;
        return true;
    }

}
