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
 *  File Name: PortCacheImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.32  2015/09/26 06:17:06  jijunwan
 *  Archive Log:    130487 - FM GUI: Topology refresh required after enabling Fabric Simulator
 *  Archive Log:    - added reset to clear all caches and update DB topology
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/09/08 15:58:49  jijunwan
 *  Archive Log:    PR 130326 - Performance enhancement on "large" fabric
 *  Archive Log:    - apply the SimpleCache on port cache
 *  Archive Log:    - split port cache to cache active ports (NodeState) and port info separately
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/08/17 18:48:53  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/08/11 17:37:19  jijunwan
 *  Archive Log:    PR 126645 - Topology Page does not show correct data after port disable/enable event
 *  Archive Log:    - improved to get distribution data with argument "refresh". When it's true, calculate distribution rather than get it from cache
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/05/07 14:08:33  jypak
 *  Archive Log:    PR 128562 - Port Cache synchronization issue:
 *  Archive Log:    Introduced a ConcurrentHashMap to replace the HashMap to avoid the ConcurrentModificationException.
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/02/23 22:22:19  jijunwan
 *  Archive Log:    improved to include/exclude inactive nodes/links in query
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/02/04 21:37:54  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/01/09 21:42:50  jijunwan
 *  Archive Log:    removed debug print
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/01/09 21:34:50  jijunwan
 *  Archive Log:    removed debug print
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2014/11/12 18:23:06  fernande
 *  Archive Log:    Fix for NullPointerException in PortCacheImpl
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2014/11/11 21:49:34  fernande
 *  Archive Log:    Removed test code left from notice process improvements
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/11/04 14:04:41  fernande
 *  Archive Log:    NoticeManager performance improvements. Now notices are processed in batches to the database, resulting in less CopyTopology requests. Increased threading so that database work runs now in parallel with cache updates. Implemented PortLinkStateChange notice event by checking the status of all ports for the LID
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/10/24 18:48:59  fernande
 *  Archive Log:    Initial batch of fixes for notice processing. Notice updates not working yet but need a stable environment for Bob.
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/10/15 21:49:15  jijunwan
 *  Archive Log:    added other ports to port distribution calculation
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/10/02 21:17:38  jijunwan
 *  Archive Log:    fixed issued found by FindBugs
 *  Archive Log:    Some auto-reformate
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/09/26 18:41:44  jijunwan
 *  Archive Log:    clean up debug message
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/09/24 18:04:16  jypak
 *  Archive Log:    1. Unit tests for CopyTopologyTask.
 *  Archive Log:    2. Exceptions thrown are cleaned up.
 *  Archive Log:    3. A fix in CacheManagerImplTest for an issue due to new serial processing service.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/09/18 14:18:39  jypak
 *  Archive Log:    1. When  shutdown notice manager, remove it from the listener list of the STLConnection so that the blocking queue doesn't fill up.
 *  Archive Log:    2. Removed unncessary print out statements.
 *  Archive Log:    3. Port cache now has null check for memory cache.
 *  Archive Log:    4. For FE errors, still process the notice and set the NoticeStatus to FEERROR.
 *  Archive Log:    5. Junit updates for NoticeStatus.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/09/17 16:40:46  fernande
 *  Archive Log:    Refactored CacheManager to load caches according to what's defined in enums MemCacheType and DBCacheType, to make it more dynamic and more extensible
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/09/17 13:17:34  jypak
 *  Archive Log:    Return boolean for each cache process notice operation to let CacheManager know whether it need to start the CopyTopologyTask.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/09/15 15:15:37  jypak
 *  Archive Log:    Notice Manager JUnit tests and relevant fixes.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/09/12 19:56:51  fernande
 *  Archive Log:    We now save ImageInfo and GroupInfo to the database. As they are retrieved by the UI, they are buffered and then saved at certain thresholds.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/09/05 15:35:53  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/08/28 14:56:50  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/08/13 14:59:22  jijunwan
 *  Archive Log:    improved exception handling
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/08/12 22:40:33  jijunwan
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
 *  Archive Log:    Revision 1.4  2014/07/11 13:06:59  jypak
 *  Archive Log:    1. Added runtime, non runtime exceptions to be thrown for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    2. Updated exception generating code due to Cache Manager related changes.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/09 15:09:31  fernande
 *  Archive Log:    Fix for NullPointerException in getPortByLocalPortNum where ports might be null
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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.intel.stl.api.NodeState;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.notice.impl.NoticeProcess;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.impl.PortCacheImpl.PortArray;
import com.intel.stl.common.STLMessages;
import com.intel.stl.common.SimpleCache;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.configuration.MemoryCache;

public class PortCacheImpl extends MemoryCache<SimpleCache<Integer, PortArray>>
        implements PortCache {
    private final SimpleCache<Integer, NodeState> portsStates;

    private final SimpleCache<Integer, PortArray> portsCache;

    // distribution on all ports include the inactive ones
    private final AtomicReference<EnumMap<NodeType, Long>> portsTypeDist;

    private final AtomicReference<Long> subnetPrefix;

    private final SAHelper helper;

    public PortCacheImpl(CacheManager cacheMgr) {
        super(cacheMgr);
        this.portsTypeDist = new AtomicReference<EnumMap<NodeType, Long>>(null);
        this.subnetPrefix = new AtomicReference<Long>(null);
        this.helper = cacheMgr.getSAHelper();

        portsCache = createPortsCache();
        portsStates = createPortsStatesCache();
    }

    protected SimpleCache<Integer, PortArray> createPortsCache() {
        return new SimpleCache<Integer, PortArray>(100, getTickResolution());
    }

    protected SimpleCache<Integer, NodeState> createPortsStatesCache() {
        return new SimpleCache<Integer, NodeState>(5000, 10 * 60 * 1000);
    }

    @Override
    public List<PortRecordBean> getPorts() throws SubnetDataNotFoundException {
        try {
            List<PortRecordBean> ports = helper.getPorts();
            refresh(ports);
            if (ports != null) {
                return ports;
            }
        } catch (Exception e) {
            throw SubnetApi.getSubnetException(e);
        }
        throw new SubnetDataNotFoundException(
                STLMessages.STL30062_PORT_NOT_FOUND_CACHE_ALL);
    }

    @Override
    public PortRecordBean getPortByPortNum(int lid, short portNum)
            throws SubnetDataNotFoundException {
        PortArray portArray = portsCache.get(lid);
        if (portArray != null) {
            PortRecordBean res = portArray.getPort(portNum);
            if (res != null) {
                return res;
            }
        } else {
            try {
                List<PortRecordBean> ports = helper.getPorts(lid);
                if (ports != null && !ports.isEmpty()) {
                    short maxPort = 0;
                    PortRecordBean res = null;
                    for (PortRecordBean port : ports) {
                        if (port.getPortNum() == portNum) {
                            res = port;
                        }
                        if (port.getPortNum() > maxPort) {
                            maxPort = port.getPortNum();
                        }
                    }
                    portsCache.push(lid, new PortArray(ports, maxPort));
                    if (res != null) {
                        return res;
                    }
                }
            } catch (Exception e) {
                throw SubnetApi.getSubnetException(e);
            }
        }

        throw new SubnetDataNotFoundException(
                STLMessages.STL30063_PORT_NOT_FOUND_CACHE,
                StringUtils.intHexString(lid), portNum);
    }

    @Override
    public PortRecordBean getPortByLocalPortNum(int lid, short localPortNum)
            throws SubnetDataNotFoundException {
        PortArray portArray = portsCache.get(lid);
        if (portArray != null) {
            PortRecordBean res = portArray.getPortByLocalPortNum(localPortNum);
            if (res != null) {
                return res;
            }
        } else {
            try {
                List<PortRecordBean> ports = helper.getPorts(lid);
                if (ports != null && !ports.isEmpty()) {
                    short maxPort = 0;
                    PortRecordBean res = null;
                    for (PortRecordBean port : ports) {
                        if (port.getPortInfo().getLocalPortNum() == localPortNum) {
                            res = port;
                        }
                        if (port.getPortNum() > maxPort) {
                            maxPort = port.getPortNum();
                        }
                    }
                    portsCache.push(lid, new PortArray(ports, maxPort));
                    if (res != null) {
                        return res;
                    }
                }
            } catch (Exception e) {
                throw SubnetApi.getSubnetException(e);
            }
        }

        throw new SubnetDataNotFoundException(
                STLMessages.STL30064_PORT_NOT_FOUND_CACHE_LOCAL, lid,
                localPortNum);
    }

    @Override
    public boolean hasPort(int lid, short portNum) {
        boolean res = false;
        NodeState nodeState = portsStates.get(lid);
        if (nodeState != null) {
            res = nodeState.isActivePort(portNum);
        } else {
            try {
                NodeCache nodeCache = cacheMgr.acquireNodeCache();
                NodeRecordBean node = nodeCache.getNode(lid);
                List<PortRecordBean> ports = helper.getPorts(lid);
                if (ports != null && !ports.isEmpty()) {
                    nodeState =
                            new NodeState(node.getNodeType(), node
                                    .getNodeInfo().getNumPorts());
                    portsStates.push(lid, nodeState);
                    for (PortRecordBean port : ports) {
                        nodeState.setActivePort(port.getPortNum());
                        if (port.getPortNum() == portNum) {
                            res = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    @Override
    public boolean hasLocalPort(int lid, short localPortNum) {
        boolean res = false;
        NodeState nodeState = portsStates.get(lid);
        if (nodeState != null) {
            res = nodeState.isActivePort(localPortNum);
        } else {
            try {
                NodeCache nodeCache = cacheMgr.acquireNodeCache();
                NodeRecordBean node = nodeCache.getNode(lid);
                List<PortRecordBean> ports = helper.getPorts(lid);
                if (ports != null && !ports.isEmpty()) {
                    nodeState =
                            new NodeState(node.getNodeType(), node
                                    .getNodeInfo().getNumPorts());
                    portsStates.push(lid, nodeState);
                    for (PortRecordBean port : ports) {
                        short lpc = port.getPortInfo().getLocalPortNum();
                        nodeState.setActivePort(lpc);
                        if (lpc == localPortNum) {
                            res = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    @Override
    public EnumMap<NodeType, Long> getPortsTypeDist(
            boolean countInternalMgrPort, boolean refresh)
            throws SubnetDataNotFoundException {
        if (refresh || portsTypeDist.get() == null) {
            List<PortRecordBean> ports = getPorts();
            refresh(ports);
        }
        return portsTypeDist.get();
    }

    /**
     * 
     * <i>Description:</i> calculate ports type distribution with
     * InternalMgrPort and update ports state cache
     * 
     * @param ports
     * @return
     * @throws SubnetDataNotFoundException
     */
    protected synchronized void refresh(List<PortRecordBean> ports)
            throws SubnetDataNotFoundException {
        if (ports == null) {
            portsStates.clear();
            portsTypeDist.set(null);
            return;
        }

        NodeCache nodeCache = cacheMgr.acquireNodeCache();
        EnumMap<NodeType, Long> portsTypeDistMap =
                new EnumMap<NodeType, Long>(NodeType.class);
        Map<Integer, NodeState> processed = new HashMap<Integer, NodeState>();
        long desiredTotalPorts = 0;
        long realTotalPorts = 0;
        for (PortRecordBean port : ports) {
            int lid = port.getEndPortLID();
            NodeState state = processed.get(lid);
            if (state == null) {
                NodeRecordBean node = nodeCache.getNode(lid);
                NodeType type = node.getNodeType();
                state = new NodeState(type, node.getNodeInfo().getNumPorts());
                processed.put(lid, state);
                portsStates.push(lid, state);
                switch (type) {
                    case SWITCH:
                        desiredTotalPorts +=
                                node.getNodeInfo().getNumPorts() + 1;
                        break;
                    case HFI:
                        desiredTotalPorts += 1;
                        break;
                    default:
                        break;
                }
            }
            NodeType type = state.getType();
            Long count = portsTypeDistMap.get(type);
            portsTypeDistMap.put(type, count == null ? 1 : (count + 1));
            realTotalPorts += 1;
            if (type == NodeType.HFI) {
                state.setActivePort(port.getPortInfo().getLocalPortNum());
            } else {
                state.setActivePort(port.getPortNum());
            }
        }
        portsTypeDistMap
                .put(NodeType.OTHER, desiredTotalPorts - realTotalPorts);
        portsTypeDist.set(portsTypeDistMap);
    }

    @Override
    public long getSubnetPrefix() {
        if (subnetPrefix.get() == null) {
            List<PortRecordBean> ports;
            try {
                ports = getPorts();
                if (!ports.isEmpty()) {
                    long prefix = ports.get(0).getPortInfo().getSubnetPrefix();
                    subnetPrefix.set(new Long(prefix));
                } else {
                    return 0;
                }
            } catch (SubnetDataNotFoundException e) {
                return 0;
            }
        }
        return subnetPrefix.get();
    }

    @Override
    protected SimpleCache<Integer, PortArray> retrieveObjectForCache()
            throws Exception {
        // do not get ports here, we will do it when we really need it
        return portsCache;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.configuration.MemoryCache#reset()
     */
    @Override
    public void reset() {
        super.reset();
        portsStates.clear();
        portsCache.clear();
        portsTypeDist.set(null);
        subnetPrefix.set(null); // should be unnecessary
    }

    /**
     * Since PortCache is a memory only cache, just update the cache with
     * whatever the FM has. NoticeProcess should have the current Port
     * information.
     */
    @Override
    public boolean refreshCache(NoticeProcess notice) throws Exception {
        portsStates.remove(notice.getLid());
        portsCache.remove(notice.getLid());
        portsTypeDist.set(null);
        return true;
    }

    @Override
    protected RuntimeException processRefreshCacheException(Exception e) {
        return SubnetApi.getSubnetException(e);
    }

    protected class PortArray {
        private final PortRecordBean[] ports;

        public PortArray(List<PortRecordBean> beans, Short maxPort) {
            ports = new PortRecordBean[maxPort + 1];
            for (PortRecordBean bean : beans) {
                ports[bean.getPortNum()] = bean;
            }
        }

        public PortRecordBean getPort(short portNum) {
            if (portNum < ports.length) {
                return ports[portNum];
            } else {
                return null;
            }
        }

        public PortRecordBean getPortByLocalPortNum(short localPortNum) {
            for (PortRecordBean port : ports) {
                if (port != null
                        && port.getPortInfo().getLocalPortNum() == localPortNum) {
                    return port;
                }
            }
            return null;
        }

        public PortRecordBean[] getPorts() {
            return ports;
        }

    }

}
