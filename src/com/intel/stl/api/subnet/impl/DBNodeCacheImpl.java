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
 *  File Name: DBNodeCache.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.17.2.1  2015/08/12 15:22:01  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/04/29 14:25:17  fernande
 *  Archive Log:    Fixed issue where changing the name of the subnet is not reflected in the caches.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/02/23 22:22:19  jijunwan
 *  Archive Log:    improved to include/exclude inactive nodes/links in query
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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.intel.stl.api.DatabaseException;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.notice.impl.NoticeProcess;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetException;
import com.intel.stl.configuration.BaseCache;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.datamanager.DatabaseManager;

public class DBNodeCacheImpl extends BaseCache implements NodeCache {

    private final DatabaseManager dbMgr;

    private final SAHelper helper;

    // distribution with active nodes only
    private final AtomicReference<EnumMap<NodeType, Integer>> activeNodesTypeDist;

    public DBNodeCacheImpl(CacheManager cacheMgr) {
        super(cacheMgr);
        this.dbMgr = cacheMgr.getDatabaseManager();
        this.helper = cacheMgr.getSAHelper();
        this.activeNodesTypeDist =
                new AtomicReference<EnumMap<NodeType, Integer>>(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.impl.NodeCache#getNodes()
     */
    @Override
    public List<NodeRecordBean> getNodes(boolean includeInactive)
            throws SubnetDataNotFoundException {
        List<NodeRecordBean> res = new ArrayList<NodeRecordBean>();
        try {
            List<NodeRecordBean> nodes = dbMgr.getNodes(getSubnetName());
            if (nodes != null && !nodes.isEmpty()) {
                for (NodeRecordBean node : nodes) {
                    if (includeInactive || node.isActive()) {
                        res.add(node);
                    }
                }
            }
        } catch (DatabaseException e) {
            log.error("Received a database exception while getting all nodes",
                    e);
            throw SubnetApi.getSubnetException(e);
        }
        return res;
    }

    @Override
    public NodeRecordBean getNode(int lid) throws SubnetDataNotFoundException {
        SubnetDataNotFoundException ne = null;
        try {
            return dbMgr.getNode(getSubnetName(), lid);
        } catch (DatabaseException e) {
            log.error(
                    "Received a database exception while getting a node with lid "
                            + lid, e);
            throw SubnetApi.getSubnetException(e);
        } catch (SubnetDataNotFoundException e) {
            ne = e;
        }

        // might be a new node
        log.info("Couldn't find node lid=" + lid + "  from database");
        NodeRecordBean node = null;
        try {
            node = helper.getNode(lid);
        } catch (Exception exception) {
            SubnetException se = SubnetApi.getSubnetException(exception);
            log.error("Error while getting node with lid=" + lid
                    + " from Fabric", exception);
            throw se;
        }

        if (node != null) {
            activeNodesTypeDist.set(null);
            cacheMgr.startTopologyUpdateTask();
            return node;
        } else {
            throw ne;
        }
    }

    @Override
    public NodeRecordBean getNode(long portGuid)
            throws SubnetDataNotFoundException {
        SubnetDataNotFoundException ne = null;
        try {
            return dbMgr.getNodeByPortGUID(getSubnetName(), portGuid);
        } catch (DatabaseException e) {
            log.error(
                    "Received a database exception while getting a node with port Guid "
                            + portGuid, e);
            throw SubnetApi.getSubnetException(e);
        } catch (SubnetDataNotFoundException e) {
            ne = e;
        }

        // might be a new node
        log.info("Couldn't find node guid="
                + StringUtils.longHexString(portGuid) + "  from database");
        NodeRecordBean node = null;
        try {
            node = helper.getNode(portGuid);
        } catch (Exception exception) {
            SubnetException se = SubnetApi.getSubnetException(exception);
            log.error("Error while getting node with portGuid=" + portGuid
                    + " from Fabric", exception);
            throw se;
        }

        if (node != null) {
            activeNodesTypeDist.set(null);
            cacheMgr.startTopologyUpdateTask();
            return node;
        } else {
            throw ne;
        }
    }

    @Override
    public EnumMap<NodeType, Integer> getNodesTypeDist(boolean includeInactive)
            throws SubnetDataNotFoundException {
        if (includeInactive) {
            return getNodesTypeDist();
        } else {
            return getActiveNodesTypeDist();
        }
    }

    protected EnumMap<NodeType, Integer> getNodesTypeDist()
            throws SubnetDataNotFoundException {
        try {
            return dbMgr.getNodeTypeDist(getSubnetName());
        } catch (DatabaseException e) {
            // int error = e.getErrorCode();
            log.error(
                    "Received a database exception while getting a node type distribution",
                    e);
            throw SubnetApi.getSubnetException(e);
        }
    }

    protected EnumMap<NodeType, Integer> getActiveNodesTypeDist()
            throws SubnetDataNotFoundException {
        if (activeNodesTypeDist.get() == null) {
            List<NodeRecordBean> nodes = getNodes(false);
            EnumMap<NodeType, Integer> nodesTypeDistMap =
                    new EnumMap<NodeType, Integer>(NodeType.class);
            for (NodeRecordBean node : nodes) {
                if (node.isActive()) {
                    NodeType type = node.getNodeType();
                    Integer count = nodesTypeDistMap.get(type);
                    nodesTypeDistMap.put(type, count == null ? 1 : (count + 1));
                }
            }
            activeNodesTypeDist.set(nodesTypeDistMap);
        }
        return activeNodesTypeDist.get();
    }

    private String getSubnetName() {
        return helper.getSubnetDescription().getName();
    }

    @Override
    public boolean refreshCache() {
        activeNodesTypeDist.set(null);
        return true;
    }

    @Override
    public boolean refreshCache(NoticeProcess notice) throws Exception {
        activeNodesTypeDist.set(null);
        return true;
    }

}
