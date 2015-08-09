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
 *  File Name: DeviceTypeTreeUpdater.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/04/09 16:09:53  jijunwan
 *  Archive Log:    workaround for PR 128038
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/23 22:46:00  jijunwan
 *  Archive Log:    improved to include/exclude inactive nodes/links in query
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/05 21:21:44  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/05 15:43:36  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/02 19:24:28  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/02 19:02:59  jijunwan
 *  Archive Log:    tree update based on merge sort algorithm
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.NameSorter;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.monitor.TreeNodeType;

public class DeviceTypesTreeSynchronizer extends TreeSynchronizer<TreeNodeType> {
    private final ISubnetApi subnetApi;

    private Map<Integer, NodeRecordBean> nodeMap;

    private Map<TreeNodeType, List<Integer>> typeMap;

    private final CreationBasedNodesSynchronizer nodesUpadter;

    public DeviceTypesTreeSynchronizer(ISubnetApi subnetApi) {
        this(subnetApi, true);
    }

    /**
     * Description:
     * 
     * @param nodeComparator
     * @param nodeMap
     */
    public DeviceTypesTreeSynchronizer(ISubnetApi subnetApi,
            boolean removeEmptyGroup) {
        super(removeEmptyGroup);
        this.subnetApi = subnetApi;
        initData();
        nodesUpadter = new CreationBasedNodesSynchronizer(subnetApi, nodeMap);
    }

    protected void initData() {
        // Retrieve a list of nodes from the Subnet API
        List<NodeRecordBean> allNodeBeans;
        try {
            allNodeBeans = subnetApi.getNodes(false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(
                    UILabels.STL90005_UPDATE_TREE_FAILED.getDescription(
                            STLConstants.K0407_DEVICE_TYPES.getValue(),
                            StringUtils.getErrorMessage(e)));
        }

        nodeMap = new HashMap<Integer, NodeRecordBean>();
        Map<NodeType, List<Integer>> nodeTypeMap =
                new HashMap<NodeType, List<Integer>>();
        for (NodeRecordBean node : allNodeBeans) {
            int lid = node.getLid();
            nodeMap.put(lid, node);
            NodeType type = node.getNodeType();
            List<Integer> members = nodeTypeMap.get(type);
            if (members == null) {
                members = new LinkedList<Integer>();
                nodeTypeMap.put(type, members);
            }
            members.add(lid);
        }

        typeMap = new LinkedHashMap<TreeNodeType, List<Integer>>();
        Comparator<Integer> comparator = new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                NodeRecordBean node1 = nodeMap.get(o1);
                NodeRecordBean node2 = nodeMap.get(o2);
                String name1 = node1 == null ? null : node1.getNodeDesc();
                String name2 = node2 == null ? null : node2.getNodeDesc();
                return NameSorter.instance().compare(name1, name2);
            }

        };
        List<Integer> members = nodeTypeMap.get(NodeType.HFI);
        if (members != null) {
            Collections.sort(members, comparator);
            typeMap.put(TreeNodeType.HCA_GROUP, members);
        }
        members = nodeTypeMap.get(NodeType.SWITCH);
        if (members != null) {
            Collections.sort(members, comparator);
            typeMap.put(TreeNodeType.SWITCH_GROUP, members);
        }
        members = nodeTypeMap.get(NodeType.ROUTER);
        if (members != null) {
            Collections.sort(members, comparator);
            typeMap.put(TreeNodeType.ROUTER_GROUP, members);
        }
    }

    public void updateTree(FVResourceNode parent, List<ITreeMonitor> monitors,
            IProgressObserver observer) {
        List<TreeNodeType> types = new ArrayList<TreeNodeType>();
        for (TreeNodeType type : typeMap.keySet()) {
            if (!typeMap.get(type).isEmpty()) {
                types.add(type);
            }
        }
        updateTree(parent, types.toArray(new TreeNodeType[0]), monitors,
                observer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.tree.FastTreeUpater#compare(com.intel.stl.ui
     * .monitor.FVResourceNode, java.lang.Object)
     */
    @Override
    protected int compare(FVResourceNode node, TreeNodeType element) {
        return TreeNodeFactory.comapreTreeNodeType(node.getType(), element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.monitor.tree.TreeUpater#createNode(int)
     */
    @Override
    protected FVResourceNode createNode(TreeNodeType type) {
        return TreeNodeFactory.createTypeNode(type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.monitor.tree.TreeUpater#addNode(int,
     * com.intel.stl.ui.monitor.FVResourceNode,
     * com.intel.stl.ui.monitor.FVTreeModel)
     */
    @Override
    protected FVResourceNode addNode(int index, TreeNodeType key,
            FVResourceNode parent, List<ITreeMonitor> monitors,
            IProgressObserver observer) {
        FVResourceNode node =
                super.addNode(index, key, parent, monitors, observer);
        // we call updateNode to fill children for a device type node
        updateNode(node, parent, monitors, observer);
        return node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.tree.TreeUpater#updateNode(com.intel.stl.ui.
     * monitor.FVResourceNode, com.intel.stl.ui.monitor.FVResourceNode,
     * com.intel.stl.ui.monitor.FVTreeModel)
     */
    @Override
    protected void updateNode(FVResourceNode node, FVResourceNode parent,
            List<ITreeMonitor> monitors, IProgressObserver observer) {
        TreeNodeType type = node.getType();
        List<Integer> elements = typeMap.get(type);
        if (elements != null) {
            nodesUpadter.updateTree(node, elements.toArray(new Integer[0]),
                    monitors, observer);
        }
    }

}
