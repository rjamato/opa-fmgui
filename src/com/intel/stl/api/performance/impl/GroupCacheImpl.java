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
 *  File Name: GroupCacheImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/04/09 03:29:24  jijunwan
 *  Archive Log:    updated to match FM 390
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/18 20:47:56  fernande
 *  Archive Log:    Enabling GroupInfo saving after fixing issues in the application
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.performance.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intel.stl.api.notice.impl.NoticeProcess;
import com.intel.stl.api.performance.GroupListBean;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.configuration.MemoryCache;
import com.intel.stl.datamanager.DatabaseManager;

public class GroupCacheImpl extends MemoryCache<Map<String, String>> implements
        GroupCache {

    private final DatabaseManager dbMgr;

    private final PAHelper helper;

    private final String subnetName;

    public GroupCacheImpl(CacheManager cacheMgr) {
        super(cacheMgr);
        this.dbMgr = cacheMgr.getDatabaseManager();
        this.helper = cacheMgr.getPAHelper();
        this.subnetName = helper.getSubnetDescription().getName();
    }

    @Override
    public boolean isGroupDefined(String groupName) {
        Map<String, String> groupList = getCachedObject();
        boolean defined = groupList.containsKey(groupName);
        if (!defined) {
            // This will force a refreshCache() which will call
            // retrieveObjectForCache(), below. Method updateCache is
            // synchronized, which will put all others request on hold until
            // refreshCache() is finished.
            setCacheReady(false);
            updateCache();
            defined = groupList.containsKey(groupName);
        }
        return defined;
    }

    @Override
    protected Map<String, String> retrieveObjectForCache() throws Exception {
        List<GroupListBean> groupList = helper.getGroupList();
        if (groupList == null || groupList.isEmpty()) {
            return new HashMap<String, String>();
        }
        dbMgr.saveGroupList(subnetName, groupList);
        Map<String, String> groups =
                new HashMap<String, String>(groupList.size());
        for (GroupListBean group : groupList) {
            groups.put(group.getGroupName(), null);
        }
        return groups;
    }

    @Override
    public boolean refreshCache(NoticeProcess notice) throws Exception {
        // Don't know about any notice that would apply to this cache
        return true;
    }
}
