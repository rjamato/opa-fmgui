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
 *  File Name: CableCacheImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/04/24 12:27:54  jypak
 *  Archive Log:    Fixed the logic to return only the results matching with given lid and port number.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/04/22 18:50:25  jypak
 *  Archive Log:    Fix to check LID before update cache.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/21 17:44:24  jypak
 *  Archive Log:    Cable Info record for a lid/port number is spread across two records.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/04 21:37:54  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/
package com.intel.stl.api.subnet.impl;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.api.notice.impl.NoticeProcess;
import com.intel.stl.api.subnet.CableRecordBean;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.configuration.MemoryCache;

public class CableCacheImpl extends MemoryCache<List<CableRecordBean>>
        implements CableCache {

    private final SAHelper helper;

    public CableCacheImpl(CacheManager cacheMgr) {
        super(cacheMgr);
        this.helper = cacheMgr.getSAHelper();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.impl.CableCache#getCables()
     */
    @Override
    public List<CableRecordBean> getCables() {
        List<CableRecordBean> res = getCachedObject();
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.impl.CableCache#getCable(int)
     */
    @Override
    public List<CableRecordBean> getCable(int lid) {
        List<CableRecordBean> cables = getCables();
        List<CableRecordBean> res = new ArrayList<CableRecordBean>();
        if (cables != null) {
            for (CableRecordBean cable : cables) {
                if (cable.getLid() == lid) {
                    res.add(cable);
                }
            }
        }
        if (!res.isEmpty()) {
            return res;
        }

        // might be a new node
        try {
            res = helper.getCables(lid);
            if (res != null && !res.isEmpty()) {
                setCacheReady(false); // Force a refresh on next call;
            }
        } catch (Exception e) {
            log.error("Error getting Cable Infos by lid " + lid, e);
            e.printStackTrace();
            throw SubnetApi.getSubnetException(e);
        }
        return res;
    }

    /**
     * Cable info are in two records.
     */
    @Override
    public List<CableRecordBean> getCable(int lid, short portNum) {
        List<CableRecordBean> cables = getCables();
        List<CableRecordBean> res = new ArrayList<CableRecordBean>();
        if (cables != null) {
            for (CableRecordBean cable : cables) {
                if (cable.getLid() == lid && cable.getPort() == portNum) {
                    res.add(cable);
                }
            }
        }

        if (!res.isEmpty()) {
            return res;
        }

        // might be a new node
        try {
            List<CableRecordBean> cablesFromFE = helper.getCables(lid);
            if (cablesFromFE != null && !cablesFromFE.isEmpty()) {
                setCacheReady(false); // Force a refresh on next call;
                for (CableRecordBean cable : cablesFromFE) {
                    if (cable.getLid() == lid && cable.getPort() == portNum) {
                        res.add(cable);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error getting Cable Infos by lid " + lid + ", portNum="
                    + portNum, e);
            e.printStackTrace();
            throw SubnetApi.getSubnetException(e);
        }
        return res;
    }

    @Override
    protected List<CableRecordBean> retrieveObjectForCache() throws Exception {
        List<CableRecordBean> res = helper.getCables();
        log.info("Retrieve " + (res == null ? 0 : res.size())
                + " Cable Infos from FE");
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
