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
 *  File Name: LinkCacheImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.14.2.1  2015/08/12 15:22:01  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/02/23 22:22:19  jijunwan
 *  Archive Log:    improved to include/exclude inactive nodes/links in query
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/02/04 21:37:54  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/11/04 14:04:41  fernande
 *  Archive Log:    NoticeManager performance improvements. Now notices are processed in batches to the database, resulting in less CopyTopology requests. Increased threading so that database work runs now in parallel with cache updates. Implemented PortLinkStateChange notice event by checking the status of all ports for the LID
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/10/24 18:48:59  fernande
 *  Archive Log:    Initial batch of fixes for notice processing. Notice updates not working yet but need a stable environment for Bob.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/09/17 16:40:08  fernande
 *  Archive Log:    Refactored CacheManager to load caches according to what's defined in enums MemCacheType and DBCacheType, to make it more dynamic
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/09/17 13:17:34  jypak
 *  Archive Log:    Return boolean for each cache process notice operation to let CacheManager know whether it need to start the CopyTopologyTask.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/09/15 15:15:37  jypak
 *  Archive Log:    Notice Manager JUnit tests and relevant fixes.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/09/05 15:35:53  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/08/28 14:56:50  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/08/13 14:59:22  jijunwan
 *  Archive Log:    improved exception handling
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/12 22:35:28  jijunwan
 *  Archive Log:    improved exception handling
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/12 20:27:41  jijunwan
 *  Archive Log:    1) changed specific xxxxNotFoundExceptions to SubnetDataNotFoundException or PerformanceDataNotFoundException
 *  Archive Log:    2) added throws SubnetException to ISubnetApi
 *  Archive Log:    3) added throws PerformanceException to IPerformanceApi
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/11 13:06:59  jypak
 *  Archive Log:    1. Added runtime, non runtime exceptions to be thrown for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    2. Updated exception generating code due to Cache Manager related changes.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.intel.stl.api.configuration.PortState;
import com.intel.stl.api.notice.impl.NoticeProcess;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.configuration.MemoryCache;

public class LinkCacheImpl extends MemoryCache<List<LinkRecordBean>> implements
        LinkCache {

    private final SAHelper helper;

    public LinkCacheImpl(CacheManager cacheMgr) {
        super(cacheMgr);
        this.helper = cacheMgr.getSAHelper();
    }

    @Override
    public List<LinkRecordBean> getLinks(boolean includeInactive)
            throws SubnetDataNotFoundException {
        List<LinkRecordBean> res = new ArrayList<LinkRecordBean>();

        List<LinkRecordBean> links = getCachedObject();
        if (links != null && !links.isEmpty()) {
            for (LinkRecordBean link : links) {
                if (includeInactive || link.isActive()) {
                    res.add(link);
                }
            }
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
        List<LinkRecordBean> links = getLinks(true);
        for (LinkRecordBean link : links) {
            if (link.getFromLID() == lid && link.getFromPortIndex() == portNum) {
                return link;
            }
        }

        // might be a new node
        try {
            links = helper.getLinks(lid);
            if (links != null && !links.isEmpty()) {
                setCacheReady(false); // Force a refresh on next call
                for (LinkRecordBean link : links) {
                    if (link.getFromPortIndex() == portNum) {
                        return link;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error getting link by source lid=" + lid + ", portNum="
                    + portNum + " from Fabric", e);
            e.printStackTrace();
            throw SubnetApi.getSubnetException(e);
        }

        throw new SubnetDataNotFoundException(STL30058_LINK_NOT_FOUND_CACHE_ALL);
    }

    @Override
    public LinkRecordBean getLinkByDestination(int lid, short portNum)
            throws SubnetDataNotFoundException {
        List<LinkRecordBean> links = getLinks(true);
        for (LinkRecordBean link : links) {
            if (link.getToLID() == lid && link.getToPortIndex() == portNum) {
                return link;
            }
        }

        // might be a new node
        try {
            links = helper.getLinks(lid);
            if (links != null && !links.isEmpty()) {
                for (LinkRecordBean link : links) {
                    if (link.getFromPortIndex() == portNum) {
                        setCacheReady(false); // Force a refresh on next call
                        return new LinkRecordBean(link.getToLID(),
                                link.getToPortIndex(), link.getFromLID(),
                                link.getFromPortIndex());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error getting link by destination lid=" + lid
                    + ", portNum=" + portNum + " from Fabric", e);
            e.printStackTrace();
            throw SubnetApi.getSubnetException(e);
        }

        throw new SubnetDataNotFoundException(STL30058_LINK_NOT_FOUND_CACHE_ALL);
    }

    @Override
    protected List<LinkRecordBean> retrieveObjectForCache() throws Exception {
        List<LinkRecordBean> res = helper.getLinks();
        log.info("Retrieve " + (res == null ? 0 : res.size())
                + " links from FE");
        return res;
    }

    @Override
    public boolean refreshCache(NoticeProcess notice) throws Exception {
        // If there was an exception during refreshCache(), this will rethrow
        // the exception
        List<LinkRecordBean> links = getCachedObject();
        // If links is null, most probably DBLinkCache is in use
        if (links == null) {
            log.info("No links from FM");
            return false;
        }
        switch (notice.getTrapType()) {
            case GID_NOW_IN_SERVICE:
                resetLinks(notice, true);
                break;
            case GID_OUT_OF_SERVICE:
                resetLinks(notice, false);
                break;
            case LINK_PORT_CHANGE_STATE:
                Map<Short, PortState> portMap = new HashMap<Short, PortState>();
                for (PortRecordBean port : notice.getPorts()) {
                    PortState portState =
                            port.getPortInfo().getPortStates().getPortState();
                    portMap.put(port.getPortNum(), portState);
                }
                resetLinksUsingPortMap(notice, portMap);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected RuntimeException processRefreshCacheException(Exception e) {
        return SubnetApi.getSubnetException(e);
    }

    private void resetLinks(NoticeProcess notice, boolean status) {
        List<LinkRecordBean> links = getCachedObject();
        int lid = notice.getLid();
        List<LinkRecordBean> newLinks = notice.getLinks();
        if (newLinks == null) {
            // No link records in the FM for this lid. The rule is: if no
            // corresponding record in the FM, then record in cache should be
            // set to inactive, just in case this is a transient condition that
            // would correct itself later. A SaveTopology would delete the link
            // if the condition persists.
            for (LinkRecordBean link : links) {
                if (link.getFromLID() == lid || link.getToLID() == lid) {
                    link.setActive(false);
                }
            }
        } else {
            // newLinks is R/O; copy it for this logic
            List<LinkRecordBean> rwNewLinks =
                    new ArrayList<LinkRecordBean>(newLinks);
            for (LinkRecordBean link : links) {
                if (link.getFromLID() == lid || link.getToLID() == lid) {
                    processLink(link, status, rwNewLinks);
                }
            }
            // At this point, newLinks should have only non-matching links that
            // need to be added to the cache
            if (rwNewLinks.size() > 0) {
                for (LinkRecordBean newLink : rwNewLinks) {
                    newLink.setActive(status);
                    links.add(newLink);
                }
            }
        }
    }

    private void processLink(LinkRecordBean memLink, boolean status,
            List<LinkRecordBean> newLinks) {
        Iterator<LinkRecordBean> it = newLinks.iterator();
        boolean found = false;
        while (it.hasNext()) {
            LinkRecordBean newLink = it.next();
            if (memLink.getFromLID() == newLink.getFromLID()
                    && memLink.getFromPortIndex() == newLink.getFromPortIndex()
                    && memLink.getToLID() == newLink.getToLID()
                    && memLink.getToPortIndex() == newLink.getToPortIndex()) {
                found = true;
                memLink.setActive(status);
                // Do not process this link again
                it.remove();
            }
        }
        if (!found) {
            // See the rule about No corresponding records in the FM above
            memLink.setActive(false);
        }

    }

    private void resetLinksUsingPortMap(NoticeProcess notice,
            Map<Short, PortState> portMap) {
        List<LinkRecordBean> links = getCachedObject();
        int lid = notice.getLid();
        for (LinkRecordBean link : links) {
            PortState portState = null;
            if (link.getFromLID() == lid) {
                portState = portMap.get(link.getFromPortIndex());
            } else if (link.getToLID() == lid) {
                portState = portMap.get(link.getToPortIndex());
            }
            if (portState == null) {
                continue;
            }
            if (portState == PortState.ACTIVE) {
                link.setActive(true);
            } else {
                link.setActive(false);
            }
        }
    }
}
