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
 *  File Name: SubnetDAOImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.38  2015/12/04 22:23:22  fernande
 *  Archive Log:    PR131909 - FM GUI does not update switch names after switch name is changed. Changed saveTopology method in SubnetDAO to check for a change in NodeDesc and update node info in database if needed.
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2015/08/17 18:49:34  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2015/08/11 17:39:14  jijunwan
 *  Archive Log:    PR 126645 - Topology Page does not show correct data after port disable/enable event
 *  Archive Log:    - changed to copy active state as well when we copy a topology
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2015/05/01 21:42:44  jijunwan
 *  Archive Log:    fixed typo found by FindBug.
 *  Archive Log:    removed unused code.
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2015/03/16 17:38:35  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/02/25 20:41:35  fernande
 *  Archive Log:    Fix for autoconnect not being set on the database
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2015/02/23 22:26:26  jijunwan
 *  Archive Log:    added querying links by lid
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/02/11 21:11:29  fernande
 *  Archive Log:    Adding support to remove a subnet (logical delete) from the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/02/06 15:04:47  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/02/04 21:37:58  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/02/03 21:30:48  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/02/03 21:26:35  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2014/12/11 18:36:41  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2014/11/04 22:31:37  fernande
 *  Archive Log:    Fixes for CopyTopology issues
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2014/11/04 14:21:57  fernande
 *  Archive Log:    NoticeManager improvements. Added new methods in support of batch processing of notices and removed methods not used anymore because they were used for individual updates. Improved BaseDAO so that the DatabaseContext is available from within a DAO and therefore other DAOs are available within a DAO.
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2014/09/24 18:04:15  jypak
 *  Archive Log:    1. Unit tests for CopyTopologyTask.
 *  Archive Log:    2. Exceptions thrown are cleaned up.
 *  Archive Log:    3. A fix in CacheManagerImplTest for an issue due to new serial processing service.
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2014/09/22 11:35:52  jypak
 *  Archive Log:    Added a new CopyTopologyTask and related updates for the subnet DAO and the cache manager.
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/09/17 13:17:32  jypak
 *  Archive Log:    Return boolean for each cache process notice operation to let CacheManager know whether it need to start the CopyTopologyTask.
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/09/15 15:15:44  jypak
 *  Archive Log:    Notice Manager JUnit tests and relevant fixes.
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/09/05 15:39:14  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/08/28 14:56:59  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/08/26 15:24:30  jijunwan
 *  Archive Log:    changed port index to be byte to be consistent with port number
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/08/26 15:01:12  jijunwan
 *  Archive Log:    changed port index to be byte to be consistent with port number
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/08/12 21:12:05  jijunwan
 *  Archive Log:    removed testing code
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/08/12 20:07:38  jijunwan
 *  Archive Log:    1) renamed HexUtils to StringUtils
 *  Archive Log:    2) added a method to StringUtils to get error message for an exception
 *  Archive Log:    3) changed all code to call StringUtils to get error message
 *  Archive Log:    4) some extra ode format change
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/08/05 18:35:19  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/07/11 13:07:04  jypak
 *  Archive Log:    1. Added runtime, non runtime exceptions to be thrown for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    2. Updated exception generating code due to Cache Manager related changes.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/06/30 17:04:57  fernande
 *  Archive Log:    Converting guids to displayable strings
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/06/23 21:16:27  fernande
 *  Archive Log:    Fix for TopologyLinkRecord
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/06/23 17:31:21  fernande
 *  Archive Log:    Fix for when topology has not been saved yet
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/06/20 16:58:25  fernande
 *  Archive Log:    Added basic Entity Manager management to minimize creation of DAOs
 *  Archive Log:    Fixed bugs in database management
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/19 20:08:39  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/17 13:10:23  jypak
 *  Archive Log:    Comments added.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/11 22:09:28  fernande
 *  Archive Log:    Changes to add more entities to database schema
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/23 19:47:14  fernande
 *  Archive Log:    Saving topology to database
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/07 19:11:34  fernande
 *  Archive Log:    Changes to save Subnets and EventRules to database
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/30 16:16:04  fernande
 *  Archive Log:    Implementing DAOs to persist EventRule and SubnetDescription
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.dbengine.impl;

import static com.intel.stl.common.STLMessages.STL30017_NODE_WITH_DUPLICATE_LID;
import static com.intel.stl.common.STLMessages.STL30018_NODE_NOT_FOUND_WITH_LID;
import static com.intel.stl.common.STLMessages.STL30022_SUBNET_NOT_FOUND;
import static com.intel.stl.common.STLMessages.STL30023_NODE_NOT_FOUND;
import static com.intel.stl.common.STLMessages.STL30024_LINK_NOT_FOUND;
import static com.intel.stl.common.STLMessages.STL30025_TOPOLOGY_NOT_FOUND;
import static com.intel.stl.common.STLMessages.STL30030_LINK_NOT_FOUND_TNF;
import static com.intel.stl.common.STLMessages.STL30031_NODE_NOT_FOUND_LID;
import static com.intel.stl.common.STLMessages.STL30032_NODE_NOT_FOUND_TNF;
import static com.intel.stl.common.STLMessages.STL30033_NODE_NOT_FOUND_PORT_GUID;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.DatabaseException;
import com.intel.stl.api.FMException;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.common.STLMessages;
import com.intel.stl.datamanager.NodeRecord;
import com.intel.stl.datamanager.NodeTypeRecord;
import com.intel.stl.datamanager.SubnetRecord;
import com.intel.stl.datamanager.TopologyLinkId;
import com.intel.stl.datamanager.TopologyLinkRecord;
import com.intel.stl.datamanager.TopologyNodeId;
import com.intel.stl.datamanager.TopologyNodeRecord;
import com.intel.stl.datamanager.TopologyRecord;
import com.intel.stl.dbengine.DatabaseContext;
import com.intel.stl.dbengine.SubnetDAO;

public class SubnetDAOImpl extends BaseDAO implements SubnetDAO {
    private static Logger log = LoggerFactory.getLogger("org.hibernate.SQL");

    private static final String SQLQUERY_LINKS_WITH_LIDS =
            "SELECT s.lid AS fromLID, l.fromPort, t.lid AS toLID, l.toPort, l.active "
                    + "FROM TOPOLOGIES_LINKS l "
                    + "LEFT JOIN TOPOLOGIES_NODES s ON l.topologyId = s.topologyId AND l.fromNodeGUID = s.nodeGUID "
                    + "LEFT JOIN TOPOLOGIES_NODES t ON l.topologyId = t.topologyId AND l.toNodeGUID = t.nodeGUID "
                    + "WHERE l.topologyId = :topologyId ORDER BY s.lid, l.fromPort";

    protected static int BATCH_SIZE = 1000;

    // private Pattern ipv4Pattern;
    //
    // private Pattern ipv6Pattern;

    public SubnetDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public SubnetDAOImpl(EntityManager entityManager,
            DatabaseContext databaseCtx) {
        super(entityManager, databaseCtx);
    }

    @Override
    public List<SubnetDescription> getSubnets() {
        TypedQuery<SubnetRecord> query =
                em.createNamedQuery("Subnet.All", SubnetRecord.class);
        List<SubnetRecord> records = query.getResultList();

        List<SubnetDescription> subnets =
                new ArrayList<SubnetDescription>(records.size());
        for (SubnetRecord record : records) {
            String uniqueName = record.getUniqueName();
            if (uniqueName.startsWith("1")) {
                subnets.add(record.getSubnetDescription());
            }
        }
        return subnets;
    }

    @Override
    public SubnetRecord getSubnet(String subnetName) {
        return getSubnet(subnetName, "1");
    }

    @Override
    public SubnetRecord getSubnet(long subnetId) {
        return em.find(SubnetRecord.class, subnetId);
    }

    @Override
    public SubnetDescription defineSubnet(SubnetDescription subnet) {
        SubnetRecord newSubnet = new SubnetRecord();
        newSubnet.setSubnetDescription(subnet);
        String uniqueName = "1" + subnet.getName();
        newSubnet.setUniqueName(uniqueName);
        startTransaction();
        em.persist(newSubnet);
        try {
            commitTransaction();
            TypedQuery<SubnetRecord> query =
                    em.createNamedQuery("Subnet.findByName", SubnetRecord.class);
            query.setParameter("subnetName", "1" + subnet.getName());
            SubnetDescription savedSubnet =
                    query.getSingleResult().getSubnetDescription();
            return savedSubnet;
        } catch (Exception e) {
            DatabaseException dbe =
                    createPersistException(e, SubnetDescription.class,
                            subnet.getName());
            throw dbe;
        }
    }

    @Override
    public void updateSubnet(SubnetDescription subnet)
            throws SubnetDataNotFoundException {
        SubnetRecord currSubnet =
                em.find(SubnetRecord.class, subnet.getSubnetId());
        if (currSubnet == null) {
            throw createDataNotFoundException(STL30022_SUBNET_NOT_FOUND,
                    subnet.getName());
        } else {
            List<HostInfo> feList = new ArrayList<HostInfo>(subnet.getFEList());
            subnet.setFEList(feList);
            startTransaction();
            currSubnet.setSubnetDescription(subnet);
            String uniqueName = "1" + subnet.getName();
            currSubnet.setUniqueName(uniqueName);
            em.merge(currSubnet);
        }
        try {
            commitTransaction();
        } catch (Exception e) {
            DatabaseException dbe =
                    createPersistException(e, SubnetDescription.class,
                            subnet.getName());
            throw dbe;
        }
    }

    @Override
    public void removeSubnet(long subnetId) throws SubnetDataNotFoundException {
        SubnetRecord currSubnet = em.find(SubnetRecord.class, subnetId);
        if (currSubnet == null) {
            throw createDataNotFoundException(STL30022_SUBNET_NOT_FOUND,
                    subnetId);
        } else {
            startTransaction();
            String subnetName = currSubnet.getSubnetDescription().getName();
            String uniqueName = "0" + subnetName + currSubnet.getId();
            currSubnet.setUniqueName(uniqueName);
            em.merge(currSubnet);
        }
        try {
            commitTransaction();
        } catch (Exception e) {
            DatabaseException dbe =
                    createPersistException(e, SubnetDescription.class,
                            currSubnet.getSubnetDescription().getName() + "("
                                    + currSubnet.getId() + ")");
            throw dbe;
        }
    }

    @Override
    public NodeRecordBean getNode(long guid) {
        NodeRecord node = em.find(NodeRecord.class, guid);
        return node.getNode();
    }

    @Override
    public NodeRecordBean getNode(String subnetName, int lid)
            throws SubnetDataNotFoundException {
        SubnetRecord subnet = getSubnetWithException(subnetName);
        TopologyRecord topology = subnet.getTopology();

        if (topology == null) {
            // Why use "TopologyRecord"? Because there is no 'Bean' for this in
            // DB level.
            throw createNodeTopologyNotFoundException(subnetName);
        }

        TopologyNodeRecord topoNode =
                getTopologyNodeRecordWithException(topology.getId(), lid);
        NodeRecord node = topoNode.getNode();
        NodeRecordBean nodeBean = node.getNode();
        nodeBean.setLid(topoNode.getLid());
        nodeBean.setActive(topoNode.isActive());
        return nodeBean;
    }

    @Override
    public NodeRecordBean getNode(String subnetName, long guid)
            throws SubnetDataNotFoundException {
        SubnetRecord subnet = getSubnetWithException(subnetName);
        TopologyRecord topology = subnet.getTopology();

        if (topology == null) {
            throw createNodeTopologyNotFoundException(subnetName);
        }

        TopologyNodeRecord topoNode =
                getTopologyNodeRecord(topology.getId(), guid);
        NodeRecord node = topoNode.getNode();
        NodeRecordBean nodeBean = node.getNode();
        nodeBean.setLid(topoNode.getLid());
        nodeBean.setActive(topoNode.isActive());
        return nodeBean;
    }

    @Override
    public NodeRecordBean getNodeByPortGUID(String subnetName, long portGuid)
            throws SubnetDataNotFoundException {
        SubnetRecord subnet = getSubnetWithException(subnetName);
        TopologyRecord topology = subnet.getTopology();

        if (topology == null) {
            throw createNodeTopologyNotFoundException(subnetName);
        }

        TypedQuery<NodeRecord> query =
                em.createNamedQuery("Node.findByPortGuid", NodeRecord.class);
        query.setParameter("portguid", portGuid);
        List<NodeRecord> records = query.getResultList();
        if (records == null || records.size() == 0) {
            throw createPortGuidNotFoundException(StringUtils
                    .longHexString(portGuid));
        }
        NodeRecord node = records.get(0);
        TopologyNodeRecord topoNode =
                getTopologyNodeRecord(topology.getId(), node.getNodeGUID());
        NodeRecordBean nodeBean = node.getNode();
        nodeBean.setLid(topoNode.getLid());
        nodeBean.setActive(topoNode.isActive());
        return nodeBean;
    }

    @Override
    public void insertNode(NodeRecordBean node) {
        NodeType nodeType = node.getNodeType();
        if (nodeType == null) {
            return;
        }

        startTransaction();
        Byte typeValue = nodeType.getId();
        NodeRecord noderec = new NodeRecord(node);
        noderec.setType(em.getReference(NodeTypeRecord.class, typeValue));
        em.persist(noderec);
        try {
            commitTransaction();
        } catch (Exception e) {
            DatabaseException dbe =
                    createPersistException(e, NodeRecordBean.class,
                            StringUtils.longHexString(noderec.getNodeGUID()));
            throw dbe;
        }
    }

    @Override
    public void updateNode(NodeRecordBean node)
            throws SubnetDataNotFoundException {
        long nodeGuid = node.getNodeInfo().getNodeGUID();
        NodeRecord currNode = em.find(NodeRecord.class, nodeGuid);
        if (currNode == null) {
            throw createNodeNotFoundException("NodeRecordBean",
                    StringUtils.longHexString(nodeGuid));
        }
        startTransaction();
        NodeRecordBean nodeBean = currNode.getNode();
        nodeBean.setLid(node.getLid());
        nodeBean.setNodeDesc(node.getNodeDesc());
        nodeBean.setNodeInfo(node.getNodeInfo());
        nodeBean.setActive(node.isActive());
        em.merge(currNode);
        try {
            commitTransaction();
        } catch (Exception e) {
            DatabaseException dbe =
                    createPersistException(e, NodeRecordBean.class,
                            StringUtils.longHexString(currNode.getNodeGUID()));
            throw dbe;
        }
    }

    @Override
    public List<NodeRecordBean> getNodes(String subnetName)
            throws SubnetDataNotFoundException {
        SubnetRecord subnet = getSubnetWithException(subnetName);
        TopologyRecord topology = subnet.getTopology();
        if (topology == null) {
            return new ArrayList<NodeRecordBean>();
        }
        Set<TopologyNodeRecord> topoNodes = topology.getNodes();
        int size = topoNodes.size();
        List<NodeRecordBean> nodes = new ArrayList<NodeRecordBean>(size);
        for (TopologyNodeRecord topoNode : topoNodes) {
            NodeRecordBean node = topoNode.getNode().getNode();
            node.setLid(topoNode.getLid());
            node.setActive(topoNode.isActive());
            nodes.add(node);
        }
        return nodes;
    }

    @Override
    public TopologyRecord saveTopology(String subnetName,
            List<NodeRecordBean> nodes, List<LinkRecordBean> links)
            throws SubnetDataNotFoundException {
        // To build the link set from the list of links, you need an updated
        // node set with updated LIDs, therefore we update first the node set
        // and then deal with the links
        SubnetRecord currSubnet = getSubnetWithException(subnetName);
        TopologyRecord currTopo = currSubnet.getTopology();
        if (currTopo == null) {
            TopologyRecord newTopo = createTopology(currSubnet, nodes, links);
            currTopo = newTopo;
        } else {
            TopologyRecord newTopo =
                    createTopologyIfNeeded(currSubnet, nodes, links);
            currTopo = newTopo;
        }
        return currTopo;
    }

    /**
     * 
     * Description: Copy an existing topology into a new topology. Please note
     * that this version of copyTopology is optimized for notice processing. In
     * that processing, it is determined which nodes and links are new and which
     * ones are already in the database. A more generic copyTopology would
     * accept as parameters the name of the subnet and a LIST of nodes and a
     * LIST of links, determine which ones need to be persisted (new) and which
     * ones need to be merged (updated), but we'll leave that for when there is
     * a need
     * 
     * @param subnetName
     *            the name of the subnet whose topology is being copied
     * @param newNodes
     *            any new TopologyNodeRecords being added to the current
     *            topology
     * @param updNodes
     *            any existing TopologyNodeRecords that already exist in the
     *            database and need update
     * @param newLinks
     *            any new TopologyLinkRecords being added to the current
     *            topology
     * @param updNodes
     *            any existing TopologyLinkRecords that already exist in the
     *            database and need update
     * @return
     * @throws SubnetDataNotFoundException
     */
    @Override
    public TopologyRecord copyTopology(SubnetRecord subnet,
            Set<TopologyNodeRecord> newNodes, Set<TopologyNodeRecord> updNodes,
            Set<TopologyLinkRecord> newLinks, Set<TopologyLinkRecord> updLinks) {

        // SubnetRecord should have been verified previous to calling
        // copyTopology so that a null doesn't happen here
        TopologyRecord topology = subnet.getTopology();

        Set<TopologyNodeRecord> currNodes = topology.getNodes();
        Set<TopologyLinkRecord> currLinks = topology.getLinks();
        // The following two statements force an actual retrieval of nodes and
        // links from the database
        currNodes.size();
        currLinks.size();
        for (TopologyNodeRecord updNode : updNodes) {
            currNodes.remove(updNode);
            currNodes.add(updNode);
            switch (updNode.getNode().getNode().getNodeType()) {
                case HFI: {
                    topology.setNumCAs(topology.getNumCAs() + 1);
                    break;
                }
                case SWITCH: {
                    topology.setNumSwitches(topology.getNumSwitches() + 1);
                    break;
                }
                case ROUTER: {
                    topology.setNumRouters(topology.getNumRouters() + 1);
                    break;
                }
                case UNKNOWN: {
                    topology.setNumUnknown(topology.getNumUnknown() + 1);
                    break;
                }
                default:
                    break;
            }
        }
        for (TopologyLinkRecord updLink : updLinks) {
            currLinks.remove(updLink);
            currLinks.add(updLink);
        }

        startTransaction();

        TopologyRecord newTopo = newTopology(topology);
        em.persist(newTopo);
        updateNodeSetWithTopology(currNodes, newTopo);
        createNodeSet(newNodes, newTopo);
        createLinkSet(currLinks, newTopo);
        createLinkSet(newLinks, newTopo);

        subnet.setTopology(newTopo);
        em.merge(subnet);

        commitTransaction();
        return newTopo;
    }

    @Override
    public List<LinkRecordBean> getLinks(String subnetName)
            throws SubnetDataNotFoundException {
        SubnetRecord subnet = getSubnetWithException(subnetName);
        TopologyRecord topology = subnet.getTopology();

        if (topology == null) {
            // Why use "TopologyRecord"? Because there is no 'Bean' for this in
            // DB level.
            throw createLinkTopologyNotFoundException(subnetName);
        }

        int numNodes = (int) topology.getNumNodes();
        List<LinkRecordBean> links = new ArrayList<LinkRecordBean>(numNodes);
        Query query = em.createNativeQuery(SQLQUERY_LINKS_WITH_LIDS);
        query.setParameter("topologyId", topology.getId());
        @SuppressWarnings("unchecked")
        List<Object> rows = query.getResultList();

        // if (rows == null || rows.size() == 0) {
        // throwFindDatabaseException(STL30024_LINK_NOT_FOUND, "LinkRecordBean",
        // HexUtils.longHexString(topology.getId()));
        // }

        for (Object row : rows) {
            Object[] r = (Object[]) row;
            LinkRecordBean link = new LinkRecordBean();
            link.setFromLID((Integer) r[0]);
            link.setFromPortIndex((Short) r[1]);
            link.setToLID((Integer) r[2]);
            link.setToPortIndex((Short) r[3]);
            link.setActive((Boolean) r[4]);
            links.add(link);
        }
        return links;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.dbengine.SubnetDAO#getLinks(java.lang.String, int)
     */
    @Override
    public List<LinkRecordBean> getLinks(String subnetName, int lid)
            throws SubnetDataNotFoundException {
        SubnetRecord subnet = getSubnetWithException(subnetName);
        TopologyRecord topology = subnet.getTopology();

        if (topology == null) {
            throw createLinkTopologyNotFoundException(subnetName);
        }

        long topologyId = topology.getId();
        TopologyNodeRecord sourceNode =
                getTopologyNodeRecordWithException(topologyId, lid);
        long sourceGuid = sourceNode.getId().getTopologyNode();

        List<TopologyLinkRecord> topoLinks =
                getTopologyLinkRecords(topologyId, sourceGuid);
        if (topoLinks == null) {
            throw createLinkTopologyNotFoundException(subnetName);
        }

        List<LinkRecordBean> res =
                new ArrayList<LinkRecordBean>(topoLinks.size());
        for (TopologyLinkRecord topoLink : topoLinks) {
            long targetGuid = topoLink.getId().getTargetNode();
            TopologyNodeRecord targetNode =
                    getTopologyNodeRecord(topologyId, targetGuid);
            int targetLid = targetNode.getLid();
            LinkRecordBean link = new LinkRecordBean();
            link.setActive(topoLink.isActive());
            link.setFromLID(lid);
            link.setFromPortIndex(topoLink.getId().getSourcePort());
            link.setToLID(targetLid);
            link.setToPortIndex(topoLink.getId().getTargetPort());
            res.add(link);
        }
        return res;
    }

    @Override
    public TopologyRecord getTopology(String subnetName)
            throws SubnetDataNotFoundException {
        SubnetRecord subnet = getSubnetWithException(subnetName);
        TopologyRecord topology = subnet.getTopology();
        if (topology == null) {
            throw createDataNotFoundException(STL30025_TOPOLOGY_NOT_FOUND,
                    subnetName);
        }
        return topology;
    }

    @Override
    public LinkRecordBean getLinkBySource(String subnetName, int lid,
            short portNum) throws FMException {
        SubnetRecord subnet = getSubnetWithException(subnetName);
        TopologyRecord topology = subnet.getTopology();

        if (topology == null) {
            throw createLinkTopologyNotFoundException(subnetName);
        }

        long topologyId = topology.getId();
        TopologyNodeRecord sourceNode =
                getTopologyNodeRecordWithException(topologyId, lid);
        long sourceGuid = sourceNode.getId().getTopologyNode();

        List<TopologyLinkRecord> topoLinks =
                getTopologyLinkRecord(topologyId, sourceGuid, portNum);

        TopologyLinkRecord topoLink = topoLinks.get(0);
        long targetGuid = topoLink.getId().getTargetNode();
        TopologyNodeRecord targetNode =
                getTopologyNodeRecord(topologyId, targetGuid);
        int targetLid = targetNode.getLid();
        LinkRecordBean link = new LinkRecordBean();
        link.setActive(topoLink.isActive());
        link.setFromLID(lid);
        link.setFromPortIndex(portNum);
        link.setToLID(targetLid);
        link.setToPortIndex(topoLink.getId().getTargetPort());
        return link;
    }

    @Override
    public LinkRecordBean getLinkByDestination(String subnetName, int lid,
            short portNum) throws FMException {
        SubnetRecord subnet = getSubnetWithException(subnetName);
        TopologyRecord topology = subnet.getTopology();

        if (topology == null) {
            throw createLinkTopologyNotFoundException(subnetName);
        }

        long topologyId = topology.getId();
        TopologyNodeRecord targetNode =
                getTopologyNodeRecordWithException(topologyId, lid);
        long targetGuid = targetNode.getId().getTopologyNode();

        // getTopologyLinkRecord query uses source GUID, source Port num.
        // look into the 'findByGuidPort' query.
        // So, the following gets the target node GUID as a sourceGuid to get
        // sourceNode for the link to be returned.

        List<TopologyLinkRecord> topoLinks =
                getTopologyLinkRecord(topologyId, targetGuid, portNum);

        TopologyLinkRecord topoLink = topoLinks.get(0);
        long sourceGuid = topoLink.getId().getTargetNode();
        TopologyNodeRecord sourceNode =
                getTopologyNodeRecord(topologyId, sourceGuid);
        int sourceLid = sourceNode.getLid();
        LinkRecordBean link = new LinkRecordBean();
        link.setActive(topoLink.isActive());
        link.setFromLID(sourceLid);
        link.setFromPortIndex(topoLink.getId().getTargetPort());
        link.setToLID(lid);
        link.setToPortIndex(portNum);
        return link;
    }

    @Override
    public TopologyNodeRecord getTopologyNodeRecord(long topologyId, int lid) {
        TypedQuery<TopologyNodeRecord> query =
                em.createNamedQuery("Node.findByLid", TopologyNodeRecord.class);
        query.setParameter("topologyId", topologyId);
        query.setParameter("lid", lid);
        List<TopologyNodeRecord> records = query.getResultList();
        if (records == null || records.size() == 0) {
            return null;
        }
        return records.get(0);
    }

    @Override
    public List<TopologyLinkRecord> getTopologyLinkRecords(long topologyId,
            long guid) {
        TypedQuery<TopologyLinkRecord> query =
                em.createNamedQuery("Link.findByGuid", TopologyLinkRecord.class);
        query.setParameter("topologyId", topologyId);
        query.setParameter("guid", guid);
        List<TopologyLinkRecord> records = query.getResultList();
        if (records == null || records.size() == 0) {
            return null;
        }
        return records;
    }

    private SubnetRecord getSubnetWithException(String subnetName)
            throws SubnetDataNotFoundException {
        SubnetRecord currSubnet = getSubnet(subnetName);
        if (currSubnet == null) {
            throw createDataNotFoundException(STL30022_SUBNET_NOT_FOUND,
                    subnetName);
        }
        return currSubnet;
    }

    private TopologyRecord createTopology(SubnetRecord subnet,
            List<NodeRecordBean> nodes, List<LinkRecordBean> links) {
        TopologyRecord topology = newTopology(null);
        int size = nodes.size();
        Map<Integer, Long> lidMap = new HashMap<Integer, Long>(size);
        startTransaction();
        em.persist(topology);
        persistNodes(nodes, topology, lidMap);
        persistLinks(links, topology, lidMap);
        em.merge(topology);
        subnet.setTopology(topology);
        em.merge(subnet);
        commitTransaction();
        return topology;
    }

    /**
     * 
     * Description: For each node in a topology, create a TopologyNodeRecord and
     * find a matching NodeRecord with the given nodeGUID from DB and if found,
     * set it to the TopologyNodeRecord created. Otherwise, create a NodeRecord
     * and save it to DB and set it to the TopologyNodeRecord created. Also set
     * LID and other fields for this topology.
     * 
     * @param nodes
     * @param topology
     * @param lidMap
     * @throws DatabaseException
     */
    private void persistNodes(List<NodeRecordBean> nodes,
            TopologyRecord topology, Map<Integer, Long> lidMap) {
        long updates = 0;
        long numNodes = 0;
        EnumMap<NodeType, Long> nodesTypeDist =
                new EnumMap<NodeType, Long>(NodeType.class);

        Iterator<NodeRecordBean> it = nodes.iterator();
        while (it.hasNext()) {
            // Create a TopologyNodeRecord (many to one) and find a matching
            // NodeRecord with nodeGUID
            // from DB and set it to the TopologyNodeRecord created.
            TopologyNodeRecord topoNode = new TopologyNodeRecord();
            topoNode.setTopology(topology);
            NodeRecordBean node = it.next();
            long nodeGUID = node.getNodeInfo().getNodeGUID();
            int lid = node.getLid();
            NodeRecord dbNode = em.find(NodeRecord.class, nodeGUID);
            if (dbNode == null) {
                NodeRecord nodeRec = new NodeRecord(node);
                em.persist(nodeRec);
                topoNode.setNode(nodeRec);
                updates++;
            } else {
                topoNode.setNode(dbNode);
            }
            if (lidMap.containsKey(lid)) {
                DatabaseException dbe =
                        new DatabaseException(STL30017_NODE_WITH_DUPLICATE_LID,
                                nodeGUID, lid);
                log.error(dbe.getMessage());
                log.error(node.toString());
                throw dbe;
            }
            lidMap.put(lid, nodeGUID);
            topoNode.setLid(lid);
            em.persist(topoNode);
            updates++;
            if (updates >= BATCH_SIZE) {
                flush();
                clear();
                updates = 0;
            }
            NodeType type = node.getNodeType();
            Long count = nodesTypeDist.get(type);
            nodesTypeDist.put(type, count == null ? 1 : (count + 1));
            numNodes++;
        }
        if (updates >= 0) {
            flush();
            clear();
        }
        topology.setNumNodes(numNodes);
        Long numCAs = nodesTypeDist.get(NodeType.HFI);
        topology.setNumCAs((numCAs == null) ? 0 : numCAs.longValue());
        Long numSwitches = nodesTypeDist.get(NodeType.SWITCH);
        topology.setNumSwitches((numSwitches == null) ? 0 : numSwitches
                .longValue());
        Long numRouters = nodesTypeDist.get(NodeType.ROUTER);
        topology.setNumRouters((numRouters == null) ? 0 : numRouters.intValue());
        Long numUnknown = nodesTypeDist.get(NodeType.UNKNOWN);
        topology.setNumUnknown((numUnknown == null) ? 0 : numUnknown.intValue());
    }

    private void persistLinks(List<LinkRecordBean> links,
            TopologyRecord topology, Map<Integer, Long> lidMap) {
        long updates = 0;
        Iterator<LinkRecordBean> it = links.iterator();
        long topologyId = topology.getId();
        while (it.hasNext()) {
            LinkRecordBean link = it.next();
            int fromLid = link.getFromLID();
            int toLid = link.getToLID();
            TopologyLinkRecord topoLink = new TopologyLinkRecord(link);
            Long fromNode = getNodeGUIDByLid(fromLid, lidMap);
            Long toNode = getNodeGUIDByLid(toLid, lidMap);
            TopologyLinkId topoLinkId = topoLink.getId();
            topoLinkId.setLinkTopology(topologyId);
            topoLinkId.setSourceNode(fromNode.longValue());
            topoLinkId.setTargetNode(toNode.longValue());
            em.persist(topoLink);
            updates++;
            if (updates >= BATCH_SIZE) {
                flush();
                clear();
                updates = 0;
            }
        }
        if (updates > 0) {
            flush();
            clear();
        }
    }

    private TopologyRecord createTopologyIfNeeded(SubnetRecord subnet,
            List<NodeRecordBean> nodes, List<LinkRecordBean> links) {
        int size = nodes.size();
        TopologyRecord currTopology = subnet.getTopology();
        Map<Integer, Long> lidMap = new HashMap<Integer, Long>(size);
        Set<TopologyNodeRecord> newNodes =
                getNodeSet(currTopology, nodes, lidMap);
        Set<TopologyLinkRecord> newLinks =
                getLinkSet(currTopology, links, lidMap);
        Set<TopologyNodeRecord> currNodes = currTopology.getNodes();
        Set<TopologyLinkRecord> currLinks = currTopology.getLinks();
        Set<NodeRecord> updNodes = new HashSet<NodeRecord>();
        boolean nodesNeedDeletion =
                retainExistingNodes(currNodes, newNodes, updNodes);
        boolean someNodesInTopology = newNodes.removeAll(currNodes);
        boolean noNodeChanges =
                !nodesNeedDeletion && someNodesInTopology
                        && (newNodes.size() == 0);
        boolean noLinkChanges;
        if (currLinks.size() == 0 && newLinks.size() == 0) {
            noLinkChanges = true;
        } else {
            boolean linksNeedDeletion = currLinks.retainAll(newLinks);
            boolean someLinksInTopology = newLinks.removeAll(currLinks);
            noLinkChanges =
                    !linksNeedDeletion && someLinksInTopology
                            && (newLinks.size() == 0);
        }
        startTransaction();
        if (updNodes.size() > 0) {
            for (NodeRecord node : updNodes) {
                em.merge(node);
            }
        }
        if (noNodeChanges && noLinkChanges) {
            // Only update nodes whose LID have changed
            updateNodeSet(currNodes);
        } else {
            TopologyRecord newTopo = newTopology(currTopology);
            em.persist(newTopo);
            updateNodeSetWithTopology(currNodes, newTopo);
            createNodeSet(newNodes, newTopo);
            createLinkSet(currLinks, newTopo);
            createLinkSet(newLinks, newTopo);
            subnet.setTopology(newTopo);
            em.merge(subnet);
            currTopology = newTopo;
        }
        commitTransaction();
        return currTopology;
    }

    /**
     * 
     * Description: For a TopologyRecord, create a TopologyNodeRecord and a
     * NodeRecord for each NodeRecordBean.
     * 
     * @param topology
     * @param nodes
     * @param lidMap
     * @return
     */
    private Set<TopologyNodeRecord> getNodeSet(TopologyRecord topology,
            List<NodeRecordBean> nodes, Map<Integer, Long> lidMap) {
        int size = nodes.size();
        Set<TopologyNodeRecord> set = new HashSet<TopologyNodeRecord>(size);
        long numNodes = 0;
        long numCAs = 0;
        long numSwitches = 0;
        int numRouters = 0;
        int numUnknown = 0;
        Iterator<NodeRecordBean> it = nodes.iterator();
        while (it.hasNext()) {
            NodeRecordBean node = it.next();
            int lid = node.getLid();
            long nodeGUID = node.getNodeInfo().getNodeGUID();
            TopologyNodeRecord topoNode = new TopologyNodeRecord();
            NodeRecord nodeRec = new NodeRecord(node);
            topoNode.setTopology(topology);
            topoNode.setNode(nodeRec);
            topoNode.setLid(lid);
            set.add(topoNode);
            lidMap.put(new Integer(lid), new Long(nodeGUID));
            switch (node.getNodeType()) {
                case HFI: {
                    numCAs++;
                    break;
                }
                case SWITCH: {
                    numSwitches++;
                    break;
                }
                case ROUTER: {
                    numRouters++;
                    break;
                }
                case UNKNOWN: {
                    numUnknown++;
                    break;
                }
                default:
                    break;
            }
            numNodes++;
        }
        topology.setNumNodes(numNodes);
        topology.setNumCAs(numCAs);
        topology.setNumSwitches(numSwitches);
        topology.setNumRouters(numRouters);
        topology.setNumUnknown(numUnknown);
        return set;
    }

    private Set<TopologyLinkRecord> getLinkSet(TopologyRecord topology,
            List<LinkRecordBean> links, Map<Integer, Long> lidMap) {
        int size = links.size();
        Set<TopologyLinkRecord> set = new HashSet<TopologyLinkRecord>(size);
        Iterator<LinkRecordBean> it = links.iterator();
        while (it.hasNext()) {
            LinkRecordBean link = it.next();
            int fromLid = link.getFromLID();
            int toLid = link.getToLID();
            TopologyLinkRecord topoLink = new TopologyLinkRecord(link);
            topoLink.setTopology(topology);
            Long fromNode = getNodeGUIDByLid(fromLid, lidMap);
            Long toNode = getNodeGUIDByLid(toLid, lidMap);
            TopologyLinkId id = topoLink.getId();
            id.setSourceNode(fromNode.longValue());
            id.setTargetNode(toNode.longValue());
            set.add(topoLink);
        }
        return set;
    }

    private long getNodeGUIDByLid(int lid, Map<Integer, Long> lidMap) {
        Integer targetLid = new Integer(lid);
        Long nodeGUID = lidMap.get(targetLid);
        if (nodeGUID == null) {
            DatabaseException dbe =
                    new DatabaseException(STL30018_NODE_NOT_FOUND_WITH_LID, lid);
            throw dbe;
        }
        return nodeGUID.longValue();
    }

    private boolean retainExistingNodes(Set<TopologyNodeRecord> currNodes,
            Set<TopologyNodeRecord> newNodes, Set<NodeRecord> updNodes) {
        boolean nodesRemoved = false;
        Iterator<TopologyNodeRecord> currit = currNodes.iterator();
        while (currit.hasNext()) {
            TopologyNodeRecord topoNode = currit.next();
            Iterator<TopologyNodeRecord> newit = newNodes.iterator();
            boolean found = false;
            while (newit.hasNext()) {
                TopologyNodeRecord newNode = newit.next();
                if (topoNode.equals(newNode)) {
                    if (nodeDescNeedsUpdate(topoNode, newNode)) {
                        NodeRecord updNode = topoNode.getNode();
                        updNode.getNode().setNodeDesc(
                                newNode.getNode().getNode().getNodeDesc());
                        updNodes.add(updNode);
                    }
                    if (topoNode.getLid() != newNode.getLid()) {
                        topoNode.setLid(newNode.getLid());
                        topoNode.setLidChanged(true);
                        topoNode.setActive(newNode.isActive());
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                currit.remove();
                nodesRemoved = true;
            }

        }
        return nodesRemoved;
    }

    private boolean nodeDescNeedsUpdate(TopologyNodeRecord currNode,
            TopologyNodeRecord newNode) {
        NodeRecordBean currBean = currNode.getNode().getNode();
        NodeRecordBean newBean = newNode.getNode().getNode();
        return !currBean.getNodeDesc().equals(newBean.getNodeDesc());
    }

    private void updateNodeSet(Set<TopologyNodeRecord> nodes) {
        log.debug("updateNodeSet - num nodes: " + nodes.size());
        long updates = 0;
        Iterator<TopologyNodeRecord> it = nodes.iterator();
        while (it.hasNext()) {
            TopologyNodeRecord topoNode = it.next();
            if (topoNode.isLidChanged()) {
                em.merge(topoNode);
                updates++;
                if (updates >= BATCH_SIZE) {
                    flush();
                    clear();
                    updates = 0;
                }
            }
        }
        if (updates > 0) {
            flush();
            clear();
        }
    }

    /**
     * 
     * Description: For a new topology, create a new set of TopologyNodeRecord
     * using the set of TopologyNodeRecord in current topology and persist them.
     * Note that only 'TopologyId' and 'Id' will be changed for a new
     * ToplogyNodeRecord from current one.
     * 
     * 
     * @param nodes
     * @param topology
     */
    private void updateNodeSetWithTopology(Set<TopologyNodeRecord> nodes,
            TopologyRecord topology) {
        long newTopologyId = topology.getId();
        log.debug("updateNodeSetWithTopology - topologyId: " + topology.getId()
                + "; num nodes: " + nodes.size());
        long updates = 0;
        Iterator<TopologyNodeRecord> it = nodes.iterator();
        while (it.hasNext()) {
            TopologyNodeRecord topoNode = it.next();
            TopologyNodeRecord newTopoNode = new TopologyNodeRecord();
            TopologyNodeId newId = newTopoNode.getId();
            newId.setTopologyId(newTopologyId);
            newId.setTopologyNode(topoNode.getId().getTopologyNode());
            newTopoNode.setId(newId);
            newTopoNode.setLid(topoNode.getLid());
            newTopoNode.setActive(topoNode.isActive());
            em.persist(newTopoNode);
            updates++;
            if (updates >= BATCH_SIZE) {
                flush();
                clear();
                updates = 0;
            }
        }
        if (updates > 0) {
            flush();
            clear();
        }
    }

    protected void createNodeSet(Set<TopologyNodeRecord> nodes,
            TopologyRecord topology) {
        log.debug("Number of new nodes being created: " + nodes.size());
        long updates = 0;
        Iterator<TopologyNodeRecord> it = nodes.iterator();
        while (it.hasNext()) {
            TopologyNodeRecord topoNode = it.next();
            topoNode.setTopology(topology);
            NodeRecord node = topoNode.getNode();
            NodeRecord dbNode = em.find(NodeRecord.class, node.getNodeGUID());
            if (dbNode != null) {
                topoNode.setNode(dbNode);
            } else {
                em.persist(node);
            }
            em.persist(topoNode);
            updates++;
            if (updates >= BATCH_SIZE) {
                flush();
                clear();
                updates = 0;
            }
        }
        if (updates > 0) {
            flush();
            clear();
        }
    }

    protected void createNode(TopologyNodeRecord newNode,
            TopologyRecord topology) {
        log.debug("New node with LID = " + newNode.getLid()
                + " being created: ");

        newNode.setTopology(topology);
        NodeRecord node = newNode.getNode();
        NodeRecord dbNode = em.find(NodeRecord.class, node.getNodeGUID());
        if (dbNode != null) {
            newNode.setNode(dbNode);
        } else {
            em.persist(node);
        }
        em.persist(newNode);

    }

    protected void createLinkSet(Set<TopologyLinkRecord> links,
            TopologyRecord topology) {
        long topologyId = topology.getId();
        log.debug("createLinkSet - topologyId: " + topologyId + "; num nodes: "
                + links.size());
        long updates = 0;
        Iterator<TopologyLinkRecord> it = links.iterator();
        while (it.hasNext()) {
            TopologyLinkRecord topoLink = it.next();
            TopologyLinkId linkId = topoLink.getId();
            NodeRecord fromNode =
                    em.getReference(NodeRecord.class, linkId.getSourceNode());
            NodeRecord toNode =
                    em.getReference(NodeRecord.class, linkId.getTargetNode());
            TopologyLinkRecord tlr = new TopologyLinkRecord();
            tlr.setTopology(topology);
            tlr.setFromNode(fromNode);
            tlr.setFromPort(linkId.getSourcePort());
            tlr.setToNode(toNode);
            tlr.setToPort(topoLink.getToPort());
            em.persist(tlr);
            updates++;
            if (updates >= BATCH_SIZE) {
                flush();
                clear();
                updates = 0;
            }
        }
        if (updates > 0) {
            flush();
            clear();
        }
    }

    private TopologyRecord newTopology(TopologyRecord oldTopology) {
        TopologyRecord newTopology = new TopologyRecord();
        Date now = new Date();
        newTopology.setId(now.getTime());
        if (oldTopology != null) {
            newTopology.setNumNodes(oldTopology.getNumNodes());
            newTopology.setNumCAs(oldTopology.getNumCAs());
            newTopology.setNumRouters(oldTopology.getNumRouters());
            newTopology.setNumSwitches(oldTopology.getNumSwitches());
            newTopology.setNumUnknown(oldTopology.getNumUnknown());
        }
        return newTopology;
    }

    private TopologyNodeRecord getTopologyNodeRecordWithException(
            long topologyId, int lid) throws SubnetDataNotFoundException {
        TopologyNodeRecord topoNode = getTopologyNodeRecord(topologyId, lid);
        if (topoNode == null) {
            throw createLidNotFoundException(lid);
        }
        return topoNode;
    }

    private TopologyNodeRecord getTopologyNodeRecord(long topologyId, long guid)
            throws SubnetDataNotFoundException {
        TypedQuery<TopologyNodeRecord> query =
                em.createNamedQuery("Node.findByGuid", TopologyNodeRecord.class);
        query.setParameter("topologyId", topologyId);
        query.setParameter("guid", guid);
        List<TopologyNodeRecord> records = query.getResultList();
        if (records == null || records.size() == 0) {
            throw createNodeNotFoundException("TopologyNodeRecord", topologyId
                    + "/" + StringUtils.longHexString(guid));
        }
        return records.get(0);
    }

    private List<TopologyLinkRecord> getTopologyLinkRecord(long topologyId,
            long guid, int port) throws SubnetDataNotFoundException {
        TypedQuery<TopologyLinkRecord> query =
                em.createNamedQuery("Link.findByGuidPort",
                        TopologyLinkRecord.class);
        query.setParameter("topologyId", topologyId);
        query.setParameter("guid", guid);
        query.setParameter("port", (short) port);
        List<TopologyLinkRecord> records = query.getResultList();
        if (records == null || records.size() == 0) {
            throw createLinkNotFoundException(topologyId + "/"
                    + StringUtils.longHexString(guid) + "/" + port);
        }
        return records;
    }

    private SubnetRecord getSubnet(String subnetName, String prefix) {
        TypedQuery<SubnetRecord> query =
                em.createNamedQuery("Subnet.findByName", SubnetRecord.class);
        String uniqueName = prefix + subnetName;
        query.setParameter("subnetName", uniqueName);
        List<SubnetRecord> records = query.getResultList();
        if (records == null || records.size() == 0) {
            return null;
        }
        return records.get(0);
    }

    // private boolean isIpAddress(String ipAddress) {
    // if (ipv4Pattern.matcher(ipAddress).matches()) {
    // return true;
    // }
    // return ipv6Pattern.matcher(ipAddress).matches();
    // }

    private DatabaseException createPersistException(Throwable cause,
            Class<?> targetClass, Object entityId) throws DatabaseException {
        DatabaseException dbe =
                DatabaseUtils.createPersistDatabaseException(cause,
                        targetClass, entityId);
        // Run time exception should be logged for traceability.
        log.error(dbe.getMessage(), cause);
        return dbe;
    }

    private SubnetDataNotFoundException createNodeNotFoundException(
            Object... arguments) {
        return createDataNotFoundException(STL30023_NODE_NOT_FOUND, arguments);
    }

    private SubnetDataNotFoundException createLidNotFoundException(
            Object... arguments) {
        return createDataNotFoundException(STL30031_NODE_NOT_FOUND_LID,
                arguments);
    }

    private SubnetDataNotFoundException createPortGuidNotFoundException(
            Object... arguments) {
        return createDataNotFoundException(STL30033_NODE_NOT_FOUND_PORT_GUID,
                arguments);
    }

    private SubnetDataNotFoundException createLinkNotFoundException(
            Object... arguments) {
        return createDataNotFoundException(STL30024_LINK_NOT_FOUND, arguments);
    }

    private SubnetDataNotFoundException createNodeTopologyNotFoundException(
            Object... arguments) {
        return createDataNotFoundException(STL30032_NODE_NOT_FOUND_TNF,
                arguments);
    }

    private SubnetDataNotFoundException createLinkTopologyNotFoundException(
            Object... arguments) {
        return createDataNotFoundException(STL30030_LINK_NOT_FOUND_TNF,
                arguments);
    }

    private SubnetDataNotFoundException createDataNotFoundException(
            STLMessages msg, Object... arguments) {
        SubnetDataNotFoundException dnf =
                new SubnetDataNotFoundException(msg, arguments);
        // FMException don't need to be logged because it will be handled in UI.
        return dnf;
    }

}
