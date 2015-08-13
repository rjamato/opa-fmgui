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
 *  File Name: DBLinkCacheImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.18.2.1  2015/08/12 15:22:00  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/04/29 14:25:17  fernande
 *  Archive Log:    Fixed issue where changing the name of the subnet is not reflected in the caches.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/02/23 22:22:19  jijunwan
 *  Archive Log:    improved to include/exclude inactive nodes/links in query
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/02/04 21:37:54  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/11/04 14:04:41  fernande
 *  Archive Log:    NoticeManager performance improvements. Now notices are processed in batches to the database, resulting in less CopyTopology requests. Increased threading so that database work runs now in parallel with cache updates. Implemented PortLinkStateChange notice event by checking the status of all ports for the LID
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/10/24 18:48:59  fernande
 *  Archive Log:    Initial batch of fixes for notice processing. Notice updates not working yet but need a stable environment for Bob.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/09/24 18:04:16  jypak
 *  Archive Log:    1. Unit tests for CopyTopologyTask.
 *  Archive Log:    2. Exceptions thrown are cleaned up.
 *  Archive Log:    3. A fix in CacheManagerImplTest for an issue due to new serial processing service.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/09/17 16:40:08  fernande
 *  Archive Log:    Refactored CacheManager to load caches according to what's defined in enums MemCacheType and DBCacheType, to make it more dynamic
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/09/17 13:17:34  jypak
 *  Archive Log:    Return boolean for each cache process notice operation to let CacheManager know whether it need to start the CopyTopologyTask.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/09/15 15:15:37  jypak
 *  Archive Log:    Notice Manager JUnit tests and relevant fixes.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/09/05 15:35:53  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/08/28 14:56:50  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/08/13 14:59:22  jijunwan
 *  Archive Log:    improved exception handling
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/08/12 22:35:28  jijunwan
 *  Archive Log:    improved exception handling
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/08/12 20:27:41  jijunwan
 *  Archive Log:    1) changed specific xxxxNotFoundExceptions to SubnetDataNotFoundException or PerformanceDataNotFoundException
 *  Archive Log:    2) added throws SubnetException to ISubnetApi
 *  Archive Log:    3) added throws PerformanceException to IPerformanceApi
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/12 13:10:47  fernande
 *  Archive Log:    Adding support for Notice processing
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/11 13:06:59  jypak
 *  Archive Log:    1. Added runtime, non runtime exceptions to be thrown for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    2. Updated exception generating code due to Cache Manager related changes.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/07 21:18:44  fernande
 *  Archive Log:    Enabling the CacheManager and enabling topology updates on the fly by managed caches
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

import static com.intel.stl.common.STLMessages.STL30058_LINK_NOT_FOUND_CACHE_ALL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.intel.stl.api.DatabaseException;
import com.intel.stl.api.notice.impl.NoticeProcess;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetException;
import com.intel.stl.configuration.BaseCache;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.datamanager.DatabaseManager;

public class DBLinkCacheImpl extends BaseCache implements LinkCache {

    private final DatabaseManager dbMgr;

    private final SAHelper helper;

    public DBLinkCacheImpl(CacheManager cacheMgr) {
        super(cacheMgr);
        this.dbMgr = cacheMgr.getDatabaseManager();
        this.helper = cacheMgr.getSAHelper();
    }

    @Override
    public List<LinkRecordBean> getLinks(boolean includeInactive)
            throws SubnetDataNotFoundException {
        List<LinkRecordBean> res = new ArrayList<LinkRecordBean>();
        try {
            List<LinkRecordBean> links = dbMgr.getLinks(getSubnetName());
            if (links != null && !links.isEmpty()) {
                for (LinkRecordBean link : links) {
                    if (includeInactive || link.isActive()) {
                        res.add(link);
                    }
                }
            }
        } catch (DatabaseException e) {
            SubnetException se = SubnetApi.getSubnetException(e);
            log.error("Error getting links", e);
            throw se;
        }

        if (!res.isEmpty()) {
            return Collections.unmodifiableList(res);
        } else {
            throw new SubnetDataNotFoundException(
                    STL30058_LINK_NOT_FOUND_CACHE_ALL);
        }
    }

    @Override
    public LinkRecordBean getLinkBySource(int lid, short portNum)
            throws SubnetDataNotFoundException {

        SubnetDataNotFoundException le = null;
        try {
            return dbMgr.getLinkBySource(getSubnetName(), lid, portNum);
        } catch (DatabaseException e) {
            SubnetException se = SubnetApi.getSubnetException(e);
            log.error("Error getting link by source lid=" + lid + ", portNum="
                    + portNum, e);
            throw se;
        } catch (SubnetDataNotFoundException e) {
            le = e;
        }

        // might be a new link
        log.info("Couldn't find Link by source Lid=" + lid + ", portNum="
                + portNum + "  from cache");
        List<LinkRecordBean> links = null;
        try {
            links = helper.getLinks(lid);
        } catch (Exception exception) {
            SubnetException se = SubnetApi.getSubnetException(exception);
            log.error("Error getting link by source lid=" + lid + ", portNum="
                    + portNum + " from Fabric", exception);
            throw se;
        }
        if (links != null && !links.isEmpty()) {
            for (LinkRecordBean link : links) {
                if (link.getFromPortIndex() == portNum) {
                    cacheMgr.startTopologyUpdateTask();
                    return link;
                }
            }
        }

        // If not found in fabric, throw link not found exception.
        throw le;
    }

    @Override
    public LinkRecordBean getLinkByDestination(int lid, short portNum)
            throws SubnetDataNotFoundException {

        SubnetDataNotFoundException le = null;
        try {
            return dbMgr.getLinkByDestination(getSubnetName(), lid, portNum);
        } catch (DatabaseException e) {
            SubnetException se = SubnetApi.getSubnetException(e);
            log.error("Error getting link by destination lid=" + lid
                    + ", portNum=" + portNum, e);
            throw se;
        } catch (SubnetDataNotFoundException e) {
            le = e;
        }

        // might be a new node
        log.info("Couldn't find Link by destination Lid=" + lid + ", portNum="
                + portNum + "  from cache");
        List<LinkRecordBean> links = null;
        try {
            links = helper.getLinks(lid);
        } catch (Exception exception) {
            SubnetException se = SubnetApi.getSubnetException(exception);
            log.error("Error getting links for lid " + lid, exception);
            throw se;
        }

        if (links != null && !links.isEmpty()) {
            for (LinkRecordBean link : links) {
                if (link.getFromPortIndex() == portNum) {
                    cacheMgr.startTopologyUpdateTask();
                    return new LinkRecordBean(link.getToLID(),
                            link.getToPortIndex(), link.getFromLID(),
                            link.getFromPortIndex());
                }
            }
        }

        // If not found in fabric, throw link not found exception.
        throw le;
    }

    @Override
    public boolean refreshCache() {
        return true;
    }

    @Override
    public boolean refreshCache(NoticeProcess notice) throws Exception {
        // Database-related notice processing is done in NoticeProcessingTask
        return true;
    }

    private String getSubnetName() {
        return helper.getSubnetDescription().getName();
    }

}
