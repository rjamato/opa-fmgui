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
 *  File Name: MemNodeCache.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.14  2015/02/23 22:22:19  jijunwan
 *  Archive Log:    improved to include/exclude inactive nodes/links in query
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/11/04 14:04:41  fernande
 *  Archive Log:    NoticeManager performance improvements. Now notices are processed in batches to the database, resulting in less CopyTopology requests. Increased threading so that database work runs now in parallel with cache updates. Implemented PortLinkStateChange notice event by checking the status of all ports for the LID
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/10/24 18:48:59  fernande
 *  Archive Log:    Initial batch of fixes for notice processing. Notice updates not working yet but need a stable environment for Bob.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/09/17 16:40:08  fernande
 *  Archive Log:    Refactored CacheManager to load caches according to what's defined in enums MemCacheType and DBCacheType, to make it more dynamic
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/09/17 13:17:34  jypak
 *  Archive Log:    Return boolean for each cache process notice operation to let CacheManager know whether it need to start the CopyTopologyTask.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/09/15 15:15:37  jypak
 *  Archive Log:    Notice Manager JUnit tests and relevant fixes.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/09/05 15:35:53  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/08/28 14:56:50  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/08/13 14:59:22  jijunwan
 *  Archive Log:    improved exception handling
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/08/12 22:35:28  jijunwan
 *  Archive Log:    improved exception handling
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/12 20:27:41  jijunwan
 *  Archive Log:    1) changed specific xxxxNotFoundExceptions to SubnetDataNotFoundException or PerformanceDataNotFoundException
 *  Archive Log:    2) added throws SubnetException to ISubnetApi
 *  Archive Log:    3) added throws PerformanceException to IPerformanceApi
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/11 13:06:59  jypak
 *  Archive Log:    1. Added runtime, non runtime exceptions to be thrown for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    2. Updated exception generating code due to Cache Manager related changes.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/07 16:50:39  fernande
 *  Archive Log:    Changing WeakReferences to SoftReference and List to Map for NodeCache and PortCache
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

import static com.intel.stl.common.STLMessages.STL30055_NODE_NOT_FOUND_IN_CACHE_LID;
import static com.intel.stl.common.STLMessages.STL30056_NODE_NOT_FOUND_IN_CACHE_PORT_GUID;
import static com.intel.stl.common.STLMessages.STL30057_NODE_TYPE_DIST_FOUND_IN_CACHE;
import static com.intel.stl.common.STLMessages.STL30061_NODE_NOT_FOUND_CACHE_ALL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.notice.impl.NoticeProcess;
import com.intel.stl.api.subnet.NodeInfoBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.configuration.MemoryCache;

public class NodeCacheImpl extends MemoryCache<Map<Integer, NodeRecordBean>>
        implements NodeCache {

    // distribution with active nodes only
    private final AtomicReference<EnumMap<NodeType, Integer>> nodesTypeDist;

    // distribution with active and inactive nodes
    private final AtomicReference<EnumMap<NodeType, Integer>> nodesTypeDist2;

    private final SAHelper helper;

    public NodeCacheImpl(CacheManager cacheMgr) {
        super(cacheMgr);
        this.helper = cacheMgr.getSAHelper();
        this.nodesTypeDist =
                new AtomicReference<EnumMap<NodeType, Integer>>(null);
        this.nodesTypeDist2 =
                new AtomicReference<EnumMap<NodeType, Integer>>(null);
    }

    @Override
    public List<NodeRecordBean> getNodes(boolean includeInactive)
            throws SubnetDataNotFoundException {
        Map<Integer, NodeRecordBean> map = getCachedObject();

        List<NodeRecordBean> res = new ArrayList<NodeRecordBean>();
        if (map != null && !map.isEmpty()) {
            for (NodeRecordBean node : map.values()) {
                if (includeInactive || node.isActive()) {
                    res.add(node);
                }
            }
        }
        if (!res.isEmpty()) {
            return res;
        } else {
            throw new SubnetDataNotFoundException(
                    STL30061_NODE_NOT_FOUND_CACHE_ALL);
        }
    }

    @Override
    public NodeRecordBean getNode(int lid) throws SubnetDataNotFoundException {
        Map<Integer, NodeRecordBean> nodeMap = getCachedObject();

        if (nodeMap != null && !nodeMap.isEmpty()) {
            NodeRecordBean node = nodeMap.get(lid);
            if (node != null) {
                return node;
            }
        }

        // might be a new node
        log.info("Couldn't find node lid=" + StringUtils.longHexString(lid)
                + "  from cache");
        NodeRecordBean node = null;
        try {
            node = helper.getNode(lid);
        } catch (Exception e) {
            log.error("Error while getting node with lid=" + lid
                    + " from Fabric", e);
            e.printStackTrace();
            throw SubnetApi.getSubnetException(e);
        }

        if (node != null) {
            setCacheReady(false); // Force a refresh on next call
            return node;
        } else {
            throw new SubnetDataNotFoundException(
                    STL30055_NODE_NOT_FOUND_IN_CACHE_LID, lid);
        }
    }

    @Override
    public NodeRecordBean getNode(long portGuid)
            throws SubnetDataNotFoundException {
        Map<Integer, NodeRecordBean> nodeMap = getCachedObject();

        if (nodeMap != null && !nodeMap.isEmpty()) {
            Collection<NodeRecordBean> nodes = nodeMap.values();
            for (NodeRecordBean node : nodes) {
                if (node.getNodeInfo().getPortGUID() == portGuid) {
                    return node;
                }
            }
        }

        // might be a new node
        log.info("Couldn't find node guid="
                + StringUtils.longHexString(portGuid) + "  from cache");
        NodeRecordBean node = null;
        try {
            node = helper.getNode(portGuid);
        } catch (Exception e) {
            log.error("Error while getting node with portGuid=" + portGuid
                    + " from Fabric", e);
            e.printStackTrace();
            throw SubnetApi.getSubnetException(e);
        }

        if (node != null) {
            setCacheReady(false); // Force a refresh on next call
            return node;
        } else {
            throw new SubnetDataNotFoundException(
                    STL30056_NODE_NOT_FOUND_IN_CACHE_PORT_GUID, portGuid);
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

    protected EnumMap<NodeType, Integer> getActiveNodesTypeDist()
            throws SubnetDataNotFoundException {
        if (nodesTypeDist.get() == null) {
            Map<Integer, NodeRecordBean> nodeMap = getCachedObject();

            if (nodeMap != null && !nodeMap.isEmpty()) {
                EnumMap<NodeType, Integer> nodesTypeDistMap =
                        new EnumMap<NodeType, Integer>(NodeType.class);

                Collection<NodeRecordBean> nodes = nodeMap.values();
                for (NodeRecordBean node : nodes) {
                    if (node.isActive()) {
                        NodeType type = node.getNodeType();
                        Integer count = nodesTypeDistMap.get(type);
                        nodesTypeDistMap.put(type, count == null ? 1
                                : (count + 1));
                    }
                }
                nodesTypeDist.set(nodesTypeDistMap);
            } else {
                throw new SubnetDataNotFoundException(
                        STL30057_NODE_TYPE_DIST_FOUND_IN_CACHE);
            }
        }
        return nodesTypeDist.get();
    }

    protected EnumMap<NodeType, Integer> getNodesTypeDist()
            throws SubnetDataNotFoundException {
        if (nodesTypeDist2.get() == null) {
            Map<Integer, NodeRecordBean> nodeMap = getCachedObject();

            if (nodeMap != null && !nodeMap.isEmpty()) {
                EnumMap<NodeType, Integer> nodesTypeDistMap =
                        new EnumMap<NodeType, Integer>(NodeType.class);

                Collection<NodeRecordBean> nodes = nodeMap.values();
                for (NodeRecordBean node : nodes) {
                    NodeType type = node.getNodeType();
                    Integer count = nodesTypeDistMap.get(type);
                    nodesTypeDistMap.put(type, count == null ? 1 : (count + 1));
                }
                nodesTypeDist2.set(nodesTypeDistMap);
            } else {
                throw new SubnetDataNotFoundException(
                        STL30057_NODE_TYPE_DIST_FOUND_IN_CACHE);
            }
        }
        return nodesTypeDist.get();
    }

    @Override
    protected Map<Integer, NodeRecordBean> retrieveObjectForCache()
            throws Exception {
        List<NodeRecordBean> nodes = helper.getNodes();
        log.info("Retrieve " + (nodes == null ? 0 : nodes.size())
                + " nodes from FE");
        Map<Integer, NodeRecordBean> map = null;
        if (nodes != null) {
            map = new HashMap<Integer, NodeRecordBean>();
            for (NodeRecordBean node : nodes) {
                map.put(node.getLid(), node);
            }
        }
        nodesTypeDist.set(null);
        nodesTypeDist2.set(null);
        return map;
    }

    @Override
    public boolean refreshCache(NoticeProcess notice) throws Exception {
        // If there was an exception during refreshCache(), this will rethrow
        // the exception
        Map<Integer, NodeRecordBean> nodeMap = getCachedObject();
        // If nodeMap is null, most probably DBNodeCache is in use
        if (nodeMap == null) {
            log.info("Node map is null");
            return false;
        }
        switch (notice.getTrapType()) {
            case GID_NOW_IN_SERVICE:
                resetNode(notice, true);
                break;
            case GID_OUT_OF_SERVICE:
                resetNode(notice, false);
            default:
                break;
        }
        nodesTypeDist.set(null);
        nodesTypeDist2.set(null);

        return true;
    }

    @Override
    protected RuntimeException processRefreshCacheException(Exception e) {
        return SubnetApi.getSubnetException(e);
    }

    private void resetNode(NoticeProcess notice, boolean status) {
        Map<Integer, NodeRecordBean> nodeMap = getCachedObject();
        int lid = notice.getLid();
        NodeRecordBean node = nodeMap.get(lid);
        NodeRecordBean newNode = notice.getNode();
        if (node != null && newNode != null) {
            NodeInfoBean nodeInfo = node.getNodeInfo();
            NodeInfoBean newNodeInfo = newNode.getNodeInfo();
            if (nodeInfo.getNodeGUID() != newNodeInfo.getNodeGUID()) {
                // LID has changed
                log.info("Node GUID in cache does not match GUID in FM");
                setCacheReady(false);
            }
            node.setActive(status);
        } else {
            if (node == null) {
                // No node in cache
                if (newNode != null) {
                    newNode.setActive(status);
                    nodeMap.put(lid, newNode);
                } else {
                    log.error("Notice for node with lid " + lid
                            + " to set active status to " + status
                            + " but no node from subnet");
                }
            } else {
                // There is a node in cache but no node in the FM. Apply rule,
                // no FM definition, resource set to inactive
                node.setActive(false);
            }
        }
    }

}
