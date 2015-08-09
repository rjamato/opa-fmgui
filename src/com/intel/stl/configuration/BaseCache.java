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
 *  File Name: BaseCache.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.9  2014/12/11 18:34:55  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/11/04 14:14:41  fernande
 *  Archive Log:    NoticeManager performance improvements. Notices are now processed in batches and database update is done in parallel with cache updates. Changes to the management of caches; if a cache is not ready, no updates for a notice are carried out.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/24 18:49:56  fernande
 *  Archive Log:    Initial batch of fixes for notice processing. Notice updates not working yet but need a stable environment for Bob.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/09/17 13:17:35  jypak
 *  Archive Log:    Return boolean for each cache process notice operation to let CacheManager know whether it need to start the CopyTopologyTask.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/15 15:15:40  jypak
 *  Archive Log:    Notice Manager JUnit tests and relevant fixes.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/28 14:56:57  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/12 13:10:06  fernande
 *  Archive Log:    Adding support for Notice processing
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/07 16:50:40  fernande
 *  Archive Log:    Changing WeakReferences to SoftReference and List to Map for NodeCache and PortCache
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/03 21:34:19  fernande
 *  Archive Log:    Adding the CacheManager in support of APIs
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.configuration;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.notice.impl.NoticeProcess;

/**
 * BaseCache is the base class from which all ManagedCache implementations
 * should extend from. It uses a flag (cacheReady) to direct access to a
 * particular cache; when set to false, all threads attempting to acquire the
 * same cache (through CacheManager) will be directed to the updateCache()
 * method, which is synchronized, effectively queuing up all requests until the
 * update of the cache is completed by the first thread that found the flag set
 * to false is finished. Something similar happens when processing a notice: the
 * flag is set to false and all subsequent requests are queued up until the
 * notice is processed.
 * 
 * 
 */
public abstract class BaseCache implements ManagedCache {

    protected static Logger log = LoggerFactory.getLogger(CacheManager.class);

    protected final AtomicBoolean cacheReady = new AtomicBoolean(false);

    protected CacheManager cacheMgr;

    public BaseCache(CacheManager cacheMgr) {
        this.cacheMgr = cacheMgr;
    }

    @Override
    public boolean isCacheReady() {
        return cacheReady.get();
    }

    @Override
    public void setCacheReady(boolean ready) {
        this.cacheReady.set(ready);
    }

    @Override
    public synchronized void updateCache() {
        if (!cacheReady.get()) {
            boolean success = refreshCache();
            cacheReady.set(success);
        }
    }

    @Override
    public synchronized void processNotice(NoticeProcess notice)
            throws Exception {
        if (!cacheReady.get()) {
            // If cache is not ready, do nothing. Next time cache is acquired,
            // it will get fresh data from the FM
            return;
        }
        boolean cacheStatus = cacheReady.get();
        cacheReady.set(false);
        try {
            // Refresh cache according to notice.
            boolean success = refreshCache(notice);
            if (!success) {
                log.error("Error processing notice in cache "
                        + this.getClass().getSimpleName());
            }
        } catch (Exception e) {
            log.error("Exception processing notice in cache "
                    + this.getClass().getSimpleName(), e);
            throw e;
        } finally {
            // reset whatever cacheStatus was so that other threads can do
            // whatever they were trying to do for this cache.
            cacheReady.set(cacheStatus);
        }
    }

    public abstract boolean refreshCache();

    public abstract boolean refreshCache(NoticeProcess notice) throws Exception;
}
