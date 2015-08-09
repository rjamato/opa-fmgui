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
 *  File Name: CacheManager.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.23  2015/03/27 20:42:26  fernande
 *  Archive Log:    Changed to use the new SerialProcessingService interface
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/03/16 17:35:29  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/02/23 22:24:35  jijunwan
 *  Archive Log:    added GroupConfCache
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/02/03 21:26:20  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/12/31 17:41:16  jypak
 *  Archive Log:    1. CableInfo updates (Moved the QSFP interpretation logic to backend etc.)
 *  Archive Log:    2. SC2SL updates.
 *  Archive Log:    3. SC2VLt updates.
 *  Archive Log:    4. SC3VLnt updates.
 *  Archive Log:    Some of the SubnetApi, CachedSubnetApi updates should be undone when the FE supports cable info, SC2SL, SC2VLt, SC2VLnt.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/12/18 16:25:40  jypak
 *  Archive Log:    Cable Info updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/12/11 18:34:55  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/11/05 22:42:04  fernande
 *  Archive Log:    Adding integration testing for the NoticeManager connecting to a simulated fabric. The test is not complete though, we need to connect thru SSH to the fabric simulator and play with the fabric.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/11/04 15:13:14  fernande
 *  Archive Log:    Fixes for unit test
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/11/04 14:14:41  fernande
 *  Archive Log:    NoticeManager performance improvements. Notices are now processed in batches and database update is done in parallel with cache updates. Changes to the management of caches; if a cache is not ready, no updates for a notice are carried out.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/10/24 18:49:56  fernande
 *  Archive Log:    Initial batch of fixes for notice processing. Notice updates not working yet but need a stable environment for Bob.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/10/14 20:44:46  jijunwan
 *  Archive Log:    1) improved to set SubnetContext invalid when we have network connection issues
 *  Archive Log:    2) improved to recreate SubnetContext when we query for it and the current one is invalid. We also clean up (include shutdown) the old context before we replace it with a new one
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/09/24 18:04:18  jypak
 *  Archive Log:    1. Unit tests for CopyTopologyTask.
 *  Archive Log:    2. Exceptions thrown are cleaned up.
 *  Archive Log:    3. A fix in CacheManagerImplTest for an issue due to new serial processing service.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/09/22 11:35:55  jypak
 *  Archive Log:    Added a new CopyTopologyTask and related updates for the subnet DAO and the cache manager.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/09/18 20:48:50  fernande
 *  Archive Log:    Enabling GroupInfo saving after fixing issues in the application
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/09/17 16:44:03  fernande
 *  Archive Log:    Refactored CacheManager to load caches according to what's defined in enums MemCacheType and DBCacheType, to make it more dynamic and more extensible. Changed code so that Exception caught during refreshCache is converted into a RuntimeException and it's rethrown every time getCachedObject is invoked. This to reflect that the caller has not much to do with the original error.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/09/17 13:17:35  jypak
 *  Archive Log:    Return boolean for each cache process notice operation to let CacheManager know whether it need to start the CopyTopologyTask.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/09/15 15:15:40  jypak
 *  Archive Log:    Notice Manager JUnit tests and relevant fixes.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/12 19:58:06  fernande
 *  Archive Log:    We now save ImageInfo and GroupInfo to the database. As they are retrieved by the UI, they are buffered and then saved at certain thresholds.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/28 14:56:57  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/26 18:05:18  jypak
 *  Archive Log:    Synchronization test : two threads trying to write a node to DB at the same time and it's written only once.  To simulate this, two threads need to be processing TopologyUpdateTask at the same time (meaning both threads need to be processing CacheManagerImpl startTopologyUpdate()).  In order to force that, another (3rd) thread created by submitting the TopologyUpdateTask should be sleeping 0.5 sec while saving topology is being processed so that the other thread of two threads started in main thread can start processing the  TopologyUpdateTask(in other words, start processing startTopologyUpdate()).  And also, make sure that only one object of the TopologyUpdateTask is created.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/07 21:19:48  fernande
 *  Archive Log:    Adding method to trigger a topology update from a managed cache
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

import static com.intel.stl.configuration.MemCacheType.CABLE;
import static com.intel.stl.configuration.MemCacheType.GROUP;
import static com.intel.stl.configuration.MemCacheType.GROUP_CONF;
import static com.intel.stl.configuration.MemCacheType.LFT;
import static com.intel.stl.configuration.MemCacheType.LINK;
import static com.intel.stl.configuration.MemCacheType.MFT;
import static com.intel.stl.configuration.MemCacheType.NODE;
import static com.intel.stl.configuration.MemCacheType.PKEYTABLE;
import static com.intel.stl.configuration.MemCacheType.PORT;
import static com.intel.stl.configuration.MemCacheType.SC2SL;
import static com.intel.stl.configuration.MemCacheType.SC2VLNT;
import static com.intel.stl.configuration.MemCacheType.SC2VLT;
import static com.intel.stl.configuration.MemCacheType.SM;
import static com.intel.stl.configuration.MemCacheType.SWITCH;
import static com.intel.stl.configuration.MemCacheType.VLARBTABLE;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.notice.impl.NoticeProcess;
import com.intel.stl.api.performance.impl.GroupCache;
import com.intel.stl.api.performance.impl.GroupConfCache;
import com.intel.stl.api.performance.impl.PAHelper;
import com.intel.stl.api.subnet.impl.CableCache;
import com.intel.stl.api.subnet.impl.LFTCache;
import com.intel.stl.api.subnet.impl.LinkCache;
import com.intel.stl.api.subnet.impl.MFTCache;
import com.intel.stl.api.subnet.impl.NodeCache;
import com.intel.stl.api.subnet.impl.PKeyTableCache;
import com.intel.stl.api.subnet.impl.PortCache;
import com.intel.stl.api.subnet.impl.SAHelper;
import com.intel.stl.api.subnet.impl.SC2SLMTCache;
import com.intel.stl.api.subnet.impl.SC2VLNTMTCache;
import com.intel.stl.api.subnet.impl.SC2VLTMTCache;
import com.intel.stl.api.subnet.impl.SMCache;
import com.intel.stl.api.subnet.impl.SwitchCache;
import com.intel.stl.api.subnet.impl.TopologyUpdateTask;
import com.intel.stl.api.subnet.impl.VLArbTableCache;
import com.intel.stl.common.STLMessages;
import com.intel.stl.datamanager.DatabaseManager;
import com.intel.stl.fecdriver.impl.FEHelper;

public class CacheManagerImpl implements CacheManager {

    // After a topology update task failure, no retries for 5 minutes
    private static final long THROTTLE_TIMEOUT = 300000L;

    private static Logger log = LoggerFactory.getLogger(CacheManagerImpl.class);

    private final Map<String, FEHelper> helpers =
            new HashMap<String, FEHelper>();

    private final DatabaseManager dbMgr;

    private final SerialProcessingService processingService;

    private final Map<String, ManagedCache> allCaches =
            new ConcurrentHashMap<String, ManagedCache>(10, 0.9f, 1);

    private final Map<MemCacheType, ManagedCache> caches =
            new ConcurrentHashMap<MemCacheType, ManagedCache>(8, 0.9f, 1);

    private boolean useDB;

    private final AtomicReference<TopologyUpdateTask> topologyUpdateRef;

    private boolean throttleTask = false;

    private long lastErrorTimestamp = 0;

    public CacheManagerImpl(DatabaseManager dbMgr,
            SerialProcessingService processingService) {
        this.dbMgr = dbMgr;
        this.processingService = processingService;
        this.topologyUpdateRef = new AtomicReference<TopologyUpdateTask>(null);
    }

    @Override
    public FEHelper getHelper(String helperName) {
        FEHelper helper = helpers.get(helperName);
        return helper;
    }

    @Override
    public SAHelper getSAHelper() {
        return (SAHelper) helpers.get(SA_HELPER);
    }

    @Override
    public PAHelper getPAHelper() {
        return (PAHelper) helpers.get(PA_HELPER);
    }

    @Override
    public SerialProcessingService getProcessingService() {
        return processingService;
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return dbMgr;
    }

    @Override
    public NodeCache acquireNodeCache() {
        return (NodeCache) getManagedCache(NODE);
    }

    @Override
    public LinkCache acquireLinkCache() {
        return (LinkCache) getManagedCache(LINK);
    }

    @Override
    public PortCache acquirePortCache() {
        return (PortCache) getManagedCache(PORT);
    }

    @Override
    public SwitchCache acquireSwitchCache() {
        return (SwitchCache) getManagedCache(SWITCH);
    }

    @Override
    public LFTCache acquireLFTCache() {
        return (LFTCache) getManagedCache(LFT);
    }

    @Override
    public MFTCache acquireMFTCache() {
        return (MFTCache) getManagedCache(MFT);
    }

    @Override
    public CableCache acquireCableCache() {
        return (CableCache) getManagedCache(CABLE);
    }

    @Override
    public PKeyTableCache acquirePKeyTableCache() {
        return (PKeyTableCache) getManagedCache(PKEYTABLE);
    }

    @Override
    public VLArbTableCache acquireVLArbTableCache() {
        return (VLArbTableCache) getManagedCache(VLARBTABLE);
    }

    @Override
    public SMCache acquireSMCache() {
        return (SMCache) getManagedCache(SM);
    }

    @Override
    public GroupCache acquireGroupCache() {
        return (GroupCache) getManagedCache(GROUP);
    }

    @Override
    public GroupConfCache acquireGroupConfCache() {
        return (GroupConfCache) getManagedCache(GROUP_CONF);
    }

    @Override
    public SC2SLMTCache acquireSC2SLMTCache() {
        return (SC2SLMTCache) getManagedCache(SC2SL);
    }

    @Override
    public SC2VLTMTCache acquireSC2VLTMTCache() {
        return (SC2VLTMTCache) getManagedCache(SC2VLT);
    }

    @Override
    public SC2VLNTMTCache acquireSC2VLNTMTCache() {
        return (SC2VLNTMTCache) getManagedCache(SC2VLNT);
    }

    @Override
    public void startTopologyUpdateTask() {
        log.info("Starting topology update task");
        startTopologyUpdate();
    }

    /**
     * 
     * Description: getManagedCache update memory cache but not DB. It basically
     * retrieves nodes from FE. the TopologyUpdateTask does update DB. It
     * serializes the cache accesses by different threads. acquireNodeCache() ->
     * (NodeCache) getManagedCache(NODE_CACHE) -> MemoryCache.refreshCache() ->
     * NodeCacheImpl.retrieveObjectForCache()
     * 
     * @param cacheId
     * @return
     */
    private ManagedCache getManagedCache(MemCacheType cacheType) {
        ManagedCache cache = caches.get(cacheType);
        if (cache == null) {
            throw new RuntimeException(
                    STLMessages.STL30073_NO_CACHE_FOUND
                            .getDescription(cacheType));
        }
        boolean cacheReady = cache.isCacheReady();
        if (!cacheReady) {
            cache.updateCache();
        }
        return cache;
    }

    /**
     * 
     * Description: goes through all managed caches to request they update
     * themselves based on the notice information
     * 
     * @throws Exception
     * 
     */
    @Override
    public void updateCaches(NoticeProcess notice) throws Exception {
        for (ManagedCache cache : allCaches.values()) {
            cache.processNotice(notice);
        }
    }

    public synchronized void initialize(boolean useDB,
            boolean startBackgroundTasks) {
        this.useDB = useDB;

        createCaches();
        if (startBackgroundTasks) {
            startTopologyUpdate();
        }

    }

    /**
     * 
     * Description: All caches are supposed to be created except DB caches. DB
     * must be synchronized and populated with FM before the DB cahces are
     * created.
     * 
     */
    private void createCaches() {
        MemCacheType[] memCaches = MemCacheType.values();

        for (int i = 0; i < memCaches.length; i++) {
            MemCacheType cacheType = memCaches[i];
            String cacheName = cacheType.getImplementingClassName();
            if (allCaches.get(cacheName) == null) {
                try {
                    ManagedCache cache = cacheType.getInstance(this);
                    allCaches.put(cacheName, cache);
                    caches.put(cacheType, cache);
                } catch (Exception e) {
                    String emsg =
                            "Error instantiating cache '" + cacheName + "': "
                                    + StringUtils.getErrorMessage(e);
                    log.error(emsg, e);
                    RuntimeException rte = new RuntimeException(emsg, e);
                    throw rte;
                }
            }
        }

        DBCacheType[] dbCaches = DBCacheType.values();
        for (int i = 0; i < dbCaches.length; i++) {
            DBCacheType dbCacheType = dbCaches[i];
            String cacheName = dbCacheType.getImplementingClassName();
            if (allCaches.get(cacheName) == null) {
                try {
                    ManagedCache cache = dbCacheType.getInstance(this);
                    allCaches.put(cacheName, cache);
                } catch (Exception e) {
                    String emsg =
                            "Error instantiating cache '" + cacheName + "': "
                                    + StringUtils.getErrorMessage(e);
                    log.error(emsg, e);
                    RuntimeException rte = new RuntimeException(emsg, e);
                    throw rte;
                }
            }
        }
    }

    public void registerHelper(String helperName, FEHelper helper) {
        FEHelper currHelper = helpers.get(helperName);
        if (currHelper != null) {
            throw new IllegalArgumentException(
                    "A helper with that name already exists");
        }
        helpers.put(helperName, helper);
    }

    private void startTopologyUpdate() {

        if (!useDB) {
            log.info("Topology update task not started because of user setting");
            return;
        }
        TopologyUpdateTask topologyUpdate = topologyUpdateRef.get();
        if (topologyUpdate == null) {

            SAHelper helper = getSAHelper();
            topologyUpdate = new TopologyUpdateTask(helper, dbMgr);
            boolean updated =
                    topologyUpdateRef.compareAndSet(null, topologyUpdate);
            if (!updated) {
                // Somebody else just set it; let him do the submitting
                return;
            }
        }
        if (topologyUpdate.getFuture() != null
                && !topologyUpdate.getFuture().isDone()) {
            log.warn("Attempting to start a topology update task but the previous has not finished yet");
            return;
        }
        if (throttleTask) {
            long now = System.currentTimeMillis();
            if ((now - lastErrorTimestamp) < THROTTLE_TIMEOUT) {
                log.warn("Topology update task was not started due to throttle");
                return;
            }
        }

        processingService.submitSerial(topologyUpdate,
                new ResultHandler<Void>() {

                    @Override
                    public void onTaskCompleted(Future<Void> result) {
                        try {
                            result.get();
                            DBCacheType[] dbCaches = DBCacheType.values();
                            for (int i = 0; i < dbCaches.length; i++) {
                                DBCacheType dbCacheType = dbCaches[i];
                                MemCacheType cacheType =
                                        dbCacheType.getMemCacheType();
                                ManagedCache cache = caches.get(cacheType);
                                if (cache != null
                                        && !cache
                                                .getClass()
                                                .getCanonicalName()
                                                .equals(dbCacheType
                                                        .getImplementingClassName())) {
                                    ManagedCache dbCache =
                                            allCaches.get(dbCacheType
                                                    .getImplementingClassName());
                                    caches.put(cacheType, dbCache);
                                }
                            }
                            topologyUpdateRef.set(null);
                            throttleTask = false;
                        } catch (InterruptedException e) {
                            log.error("Topology update task was interrupted", e);
                            topologyUpdateRef.set(null);
                            lastErrorTimestamp = System.currentTimeMillis();
                            throttleTask = true;
                        } catch (ExecutionException e) {
                            log.error(
                                    "Exception caught during topology update task",
                                    e.getCause());
                            topologyUpdateRef.set(null);
                            lastErrorTimestamp = System.currentTimeMillis();
                            throttleTask = true;
                        }
                    }

                });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.configuration.CacheManager#cleancup()
     */
    @Override
    public void cleanup() {
        for (FEHelper helper : helpers.values()) {
            helper.close();
        }
    }

    // For testing
    protected Map<MemCacheType, ManagedCache> getCaches() {
        return caches;
    }

    protected TopologyUpdateTask getTopologyUpdateTask() {
        return topologyUpdateRef.get();
    }
}
