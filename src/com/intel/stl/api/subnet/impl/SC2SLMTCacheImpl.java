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
 *  File Name: SC2SLMTCacheImpl.java
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.api.subnet.impl;

import java.util.List;

import com.intel.stl.api.notice.impl.NoticeProcess;
import com.intel.stl.api.subnet.SC2SLMTRecordBean;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.configuration.MemoryCache;

public class SC2SLMTCacheImpl extends MemoryCache<List<SC2SLMTRecordBean>>
        implements SC2SLMTCache {

    private final SAHelper helper;

    public SC2SLMTCacheImpl(CacheManager cacheMgr) {
        super(cacheMgr);
        this.helper = cacheMgr.getSAHelper();
    }

    @Override
    public List<SC2SLMTRecordBean> getSC2SLMTs() {
        List<SC2SLMTRecordBean> res = getCachedObject();
        return res;
    }

    @Override
    public SC2SLMTRecordBean getSC2SLMT(int lid) {
        List<SC2SLMTRecordBean> sc2slmts = getSC2SLMTs();
        if (sc2slmts != null) {
            for (SC2SLMTRecordBean sc2sl : sc2slmts) {
                if (sc2sl.getLid() == lid) {
                    return sc2sl;
                }
            }
        }

        // might be a new node
        try {
            SC2SLMTRecordBean res = helper.getSC2SLMT(lid);
            if (res != null) {
                setCacheReady(false); // Force a refresh on next call;
            }

            return res;
        } catch (Exception e) {
            log.error("Error getting Cable Infos by lid " + lid, e);
            e.printStackTrace();
            throw SubnetApi.getSubnetException(e);
        }
    }

    @Override
    protected List<SC2SLMTRecordBean> retrieveObjectForCache() throws Exception {
        List<SC2SLMTRecordBean> res = helper.getSC2SLMTs();
        log.info("Retrieve " + (res == null ? 0 : res.size())
                + " SC2SLMT Infos from FE");
        return res;
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
        // No notice applies to this cache
        return true;
    }

    @Override
    protected RuntimeException processRefreshCacheException(Exception e) {
        return SubnetApi.getSubnetException(e);
    }
}
