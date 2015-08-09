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
 *  File Name: SMCacheImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2014/09/17 16:40:46  fernande
 *  Archive Log:    Refactored CacheManager to load caches according to what's defined in enums MemCacheType and DBCacheType, to make it more dynamic and more extensible
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/17 13:17:34  jypak
 *  Archive Log:    Return boolean for each cache process notice operation to let CacheManager know whether it need to start the CopyTopologyTask.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/28 14:56:50  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/12 20:27:41  jijunwan
 *  Archive Log:    1) changed specific xxxxNotFoundExceptions to SubnetDataNotFoundException or PerformanceDataNotFoundException
 *  Archive Log:    2) added throws SubnetException to ISubnetApi
 *  Archive Log:    3) added throws PerformanceException to IPerformanceApi
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/03 21:35:54  fernande
 *  Archive Log:    Adding the CacheManager in support of APIs
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.subnet.impl;

import java.util.List;

import com.intel.stl.api.notice.impl.NoticeProcess;
import com.intel.stl.api.subnet.SMRecordBean;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.configuration.MemoryCache;

public class SMCacheImpl extends MemoryCache<List<SMRecordBean>> implements
        SMCache {

    private final SAHelper helper;

    public SMCacheImpl(CacheManager cacheMgr) {
        super(cacheMgr);
        this.helper = cacheMgr.getSAHelper();
    }

    @Override
    public List<SMRecordBean> getSMs() {
        List<SMRecordBean> res = getCachedObject();
        return res;
    }

    @Override
    public SMRecordBean getSM(int lid) {
        List<SMRecordBean> sms = getSMs();
        if (sms != null) {
            for (SMRecordBean sm : sms) {
                if (sm.getLid() == lid) {
                    return sm;
                }
            }
        }

        return null;
    }

    @Override
    protected List<SMRecordBean> retrieveObjectForCache() throws Exception {
        List<SMRecordBean> res = helper.getSMs();
        log.info("Retrieve " + (res == null ? 0 : res.size()) + " SMs from FE");
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
