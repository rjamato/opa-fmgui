/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: GroupConfCacheImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1.2.1  2015/05/06 19:22:33  jijunwan
 *  Archive Log:    fixed ref issue found by FindBug
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/05/01 21:39:26  jijunwan
 *  Archive Log:    fixed ref issue found by FindBug
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/23 22:24:36  jijunwan
 *  Archive Log:    added GroupConfCache
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.performance.impl;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.notice.impl.NoticeProcess;
import com.intel.stl.api.performance.GroupConfigRspBean;
import com.intel.stl.api.performance.VFConfigRspBean;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.configuration.MemoryCache;

/**
 * Lazy approach based cache. It maintains cache by itself rather than by
 * MemoryCache
 */
public class GroupConfCacheImpl extends MemoryCache<Void> implements
        GroupConfCache {
    private final static Logger log = LoggerFactory
            .getLogger(GroupConfCacheImpl.class);

    private final PAHelper helper;

    private final Map<String, SoftReference<List<GroupConfigRspBean>>> groupConfigs =
            new HashMap<String, SoftReference<List<GroupConfigRspBean>>>();

    private final Map<String, SoftReference<List<VFConfigRspBean>>> vfConfigs =
            new HashMap<String, SoftReference<List<VFConfigRspBean>>>();

    /**
     * Description:
     * 
     * @param cacheMgr
     */
    public GroupConfCacheImpl(CacheManager cacheMgr) {
        super(cacheMgr);
        this.helper = cacheMgr.getPAHelper();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.performance.impl.GroupConfCache#getGroupConfig(java
     * .lang.String)
     */
    @Override
    public List<GroupConfigRspBean> getGroupConfig(String name)
            throws Exception {
        synchronized (groupConfigs) {
            SoftReference<List<GroupConfigRspBean>> confRef =
                    groupConfigs.get(name);
            if (confRef == null || confRef.get() == null) {
                List<GroupConfigRspBean> conf = helper.getGroupConfig(name);
                confRef = new SoftReference<List<GroupConfigRspBean>>(conf);
                groupConfigs.put(name, confRef);
            } else {
            }
            return confRef == null ? null : confRef.get();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.performance.impl.GroupConfCache#getVFConfig(java.lang
     * .String)
     */
    @Override
    public List<VFConfigRspBean> getVFConfig(String name) throws Exception {
        synchronized (vfConfigs) {
            SoftReference<List<VFConfigRspBean>> confRef = vfConfigs.get(name);
            if (confRef == null || confRef.get() == null) {
                List<VFConfigRspBean> conf = helper.getVFConfig(name);
                confRef = new SoftReference<List<VFConfigRspBean>>(conf);
                vfConfigs.put(name, confRef);
            }
            return confRef == null ? null : confRef.get();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.configuration.MemoryCache#isCacheReady()
     */
    @Override
    public boolean isCacheReady() {
        return cacheReady.get();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.configuration.MemoryCache#retrieveObjectForCache()
     */
    @Override
    protected Void retrieveObjectForCache() throws Exception {
        // clear caches to force recreating cache when query
        synchronized (groupConfigs) {
            groupConfigs.clear();
        }
        synchronized (vfConfigs) {
            vfConfigs.clear();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.configuration.BaseCache#refreshCache(com.intel.stl.api.
     * notice.impl.NoticeProcess)
     */
    @Override
    public boolean refreshCache(NoticeProcess notice) throws Exception {
        int lid = notice.getLid();
        refreshGroupConf(lid);
        refreshVfConf(lid);
        return true;
    }

    protected void refreshGroupConf(int lid) {
        synchronized (groupConfigs) {
            for (String group : groupConfigs.keySet()) {
                SoftReference<List<GroupConfigRspBean>> ref =
                        groupConfigs.get(group);
                List<GroupConfigRspBean> confs = ref.get();
                if (confs != null) {
                    for (GroupConfigRspBean conf : confs) {
                        if (conf.getPort().getNodeLid() == lid) {
                            ref.clear();
                            log.info("Cleared cache for Device Group '" + group
                                    + "'");
                            break;
                        }
                    }
                }
            }
        }
    }

    protected void refreshVfConf(int lid) {
        synchronized (vfConfigs) {
            for (String group : vfConfigs.keySet()) {
                SoftReference<List<VFConfigRspBean>> ref = vfConfigs.get(group);
                List<VFConfigRspBean> confs = ref.get();
                if (confs != null) {
                    for (VFConfigRspBean conf : confs) {
                        if (conf.getPort().getNodeLid() == lid) {
                            ref.clear();
                            log.info("Cleared cache for Virtual Fabric '"
                                    + group + "'");
                            break;
                        }
                    }
                }
            }
        }
    }
}
