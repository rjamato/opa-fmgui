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
 *  File Name: PartialDeviceTypeTreeUpdater.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.9  2015/02/23 22:48:44  jijunwan
 *  Archive Log:    fixed insures on tree update upon notices
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/05 21:21:44  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/12/11 18:46:13  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/14 11:32:08  jypak
 *  Archive Log:    UI updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/02 21:26:21  jijunwan
 *  Archive Log:    fixed issued found by FindBugs
 *  Archive Log:    Some auto-reformate
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/05 15:43:36  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/03 18:10:26  jijunwan
 *  Archive Log:    new Tree Updaters
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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.ui.monitor.TreeNodeType;

public class DeviceTypesTreeUpdater implements ITreeUpdater {
    private final static Logger log = LoggerFactory
            .getLogger(DeviceTypesTreeUpdater.class);

    private final ISubnetApi subnetApi;

    protected final Comparator<FVResourceNode> typeNodeComparator;

    protected final Comparator<FVResourceNode> nodeComparator;

    /**
     * Description:
     * 
     * @param subnetApi
     */
    public DeviceTypesTreeUpdater(ISubnetApi subnetApi) {
        super();
        this.subnetApi = subnetApi;

        typeNodeComparator = TreeNodeFactory.getTypeNodeComparator();
        nodeComparator = TreeNodeFactory.getNodeComparator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.monitor.tree.IPartialTreeUpdater#addNode(int,
     * com.intel.stl.ui.monitor.FVResourceNode,
     * com.intel.stl.ui.monitor.FVTreeModel)
     */
    @Override
    public void addNode(int lid, FVResourceNode tree,
            List<ITreeMonitor> monitors) {
        updateNode(lid, tree, true, monitors);
    }

    @Override
    public void updateNode(int lid, FVResourceNode tree,
            List<ITreeMonitor> monitors) {
        updateNode(lid, tree, true, monitors);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.monitor.tree.IPartialTreeUpdater#removeNode(int,
     * com.intel.stl.ui.monitor.FVResourceNode,
     * com.intel.stl.ui.monitor.FVTreeModel)
     */
    @Override
    public void removeNode(int lid, FVResourceNode tree,
            boolean removeEmptyParents, List<ITreeMonitor> monitors) {
        updateNode(lid, tree, true, monitors);
    }

    public void updateNode(int lid, FVResourceNode tree,
            boolean removeEmptyParents, List<ITreeMonitor> monitors) {
        NodeRecordBean bean = null;
        try {
            bean = subnetApi.getNode(lid);
        } catch (SubnetDataNotFoundException e) {
            // This node is not found from fabric.
        }

        if (bean == null || !bean.isActive()) {
            removeDeviceTypesNode(lid, tree, true, monitors);
            return;
        }

        Map<Integer, NodeRecordBean> nodeMap =
                new HashMap<Integer, NodeRecordBean>();
        nodeMap.put(lid, bean);
        CreationBasedNodesSynchronizer nodeUpdater =
                new CreationBasedNodesSynchronizer(subnetApi, nodeMap);

        FVResourceNode typeNode = getTypeNode(bean, tree);
        FVResourceNode node = TreeNodeFactory.createNode(bean);

        Vector<FVResourceNode> children = typeNode.getChildren();
        int index = Collections.binarySearch(children, node, nodeComparator);
        if (index < 0) {
            index = -index - 1;
            nodeUpdater.addNode(index, lid, typeNode, monitors, null);
        } else {
            FVResourceNode updateNode = typeNode.getChildAt(index);
            nodeUpdater.updateNode(updateNode, typeNode, monitors, null);
        }
    }

    protected FVResourceNode getTypeNode(NodeRecordBean bean,
            FVResourceNode parent) {
        TreeNodeType treeNodeType =
                TreeNodeFactory.getTreeNodeType(bean.getNodeType());
        FVResourceNode node = TreeNodeFactory.createTypeNode(treeNodeType);
        Vector<FVResourceNode> children = parent.getChildren();
        int index =
                Collections.binarySearch(children, node, typeNodeComparator);
        if (index < 0) {
            int pos = -index - 1;
            parent.addChild(pos, node);
        } else {
            node = parent.getChildAt(index);
        }

        return node;
    }

    public void removeDeviceTypesNode(int lid, FVResourceNode tree,
            boolean removeEmptyParents, List<ITreeMonitor> monitors) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            FVResourceNode typeNode = tree.getChildAt(i);
            for (int j = 0; j < typeNode.getChildCount(); j++) {
                FVResourceNode node = typeNode.getChildAt(j);
                if (node.getId() == lid) {
                    int index = j;
                    FVResourceNode parent = typeNode;
                    if (removeEmptyParents && typeNode.getChildCount() == 1) {
                        index = tree.getIndex(typeNode);
                        parent = tree;
                    }
                    parent.removeChild(index);
                    if (monitors != null) {
                        for (ITreeMonitor monitor : monitors) {
                            monitor.fireTreeNodesRemoved(this, parent.getPath()
                                    .getPath(), new int[] { index },
                                    new FVResourceNode[] { node });
                        }
                    }
                    return;
                }
            }
        }
        log.warn("Node lid=" + lid + " doesn't exist!");
        // throw new IllegalArgumentException("Node lid=" + lid
        // + " doesn't exist!");
    }
}
