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
 *  File Name: MFTCacheImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/10/07 11:39:58  jypak
 *  Archive Log:    PR 130608 - Changes made to SC2VL mapping is not reflected in FM GUI's SC2SL Mapping Table.
 *  Archive Log:    Klocwork issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/10/01 17:37:10  jypak
 *  Archive Log:    PR 130608 - Changes made to SC2VL mapping is not reflected in FM GUI's SC2SL Mapping Table.
 *  Archive Log:    Each cache's refreshCache method is implemented to remove relevant cahce.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/08/17 18:48:53  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/17 16:40:08  fernande
 *  Archive Log:    Refactored CacheManager to load caches according to what's defined in enums MemCacheType and DBCacheType, to make it more dynamic
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intel.stl.api.notice.impl.NoticeProcess;
import com.intel.stl.api.subnet.MFTRecordBean;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.configuration.MemoryCache;

public class MFTCacheImpl extends
        MemoryCache<Map<Integer, List<MFTRecordBean>>> implements MFTCache {

    private final SAHelper helper;

    public MFTCacheImpl(CacheManager cacheMgr) {
        super(cacheMgr);
        this.helper = cacheMgr.getSAHelper();
    }

    @Override
    public List<MFTRecordBean> getMFTs() {
        Map<Integer, List<MFTRecordBean>> map = getCachedObject();

        List<MFTRecordBean> res = new ArrayList<MFTRecordBean>();
        if (map != null && !map.isEmpty()) {
            for (List<MFTRecordBean> mfts : map.values()) {
                for (MFTRecordBean mft : mfts) {
                    res.add(mft);
                }
            }
        }

        if (!res.isEmpty()) {
            return res;
        } else {
            // might be a new
            try {
                res = helper.getMFTs();
                if (res != null) {
                    setCacheReady(false); // Force a refresh on next call;
                }
                return res;
            } catch (Exception e) {
                throw SubnetApi.getSubnetException(e);
            }
        }
    }

    @Override
    public List<MFTRecordBean> getMFT(int lid) {
        Map<Integer, List<MFTRecordBean>> map = getCachedObject();
        List<MFTRecordBean> res = new ArrayList<MFTRecordBean>();
        if (map != null) {
            res = map.get(lid);
        }
        if (res != null && !res.isEmpty()) {
            return res;
        }

        // might be a new node
        try {
            res = helper.getMFTs(lid);
            if (res != null && !res.isEmpty()) {
                setCacheReady(false); // Force a refresh on next call;
            }
        } catch (Exception e) {
            log.error("Error getting MFT by lid " + lid, e);
            e.printStackTrace();
            throw SubnetApi.getSubnetException(e);
        }
        return res;
    }

    @Override
    protected Map<Integer, List<MFTRecordBean>> retrieveObjectForCache()
            throws Exception {
        List<MFTRecordBean> mfts = helper.getMFTs();
        log.info("Retrieve " + (mfts == null ? 0 : mfts.size())
                + " MFTs from FE");
        Map<Integer, List<MFTRecordBean>> map = null;
        if (mfts != null) {
            map = new HashMap<Integer, List<MFTRecordBean>>();
            for (MFTRecordBean mft : mfts) {
                int lid = mft.getLid();
                if (map.containsKey(lid)) {
                    map.get(lid).add(mft);
                } else {
                    List<MFTRecordBean> list = new ArrayList<MFTRecordBean>();
                    list.add(mft);
                    map.put(mft.getLid(), list);
                }
            }
        }
        return map;
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
        Map<Integer, List<MFTRecordBean>> map = getCachedObject();
        if (map != null && !map.isEmpty()) {
            map.remove(notice.getLid());
        }
        return true;
    }

    @Override
    protected RuntimeException processRefreshCacheException(Exception e) {
        return SubnetApi.getSubnetException(e);
    }
}
