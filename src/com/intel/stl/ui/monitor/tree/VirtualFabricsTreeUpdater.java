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
 *  File Name: PartialDeviceGroupTreeUpdater.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10  2015/09/30 13:26:45  fisherma
 *  Archive Log:    PR 129357 - ability to hide inactive ports.  Also fixes PR 129689 - Connectivity table exhibits inconsistent behavior on Performance and Topology pages
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/08/17 18:54:19  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/04/09 03:33:41  jijunwan
 *  Archive Log:    updated to match FM 390
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/12/11 18:46:13  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/14 11:32:08  jypak
 *  Archive Log:    UI updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/12 20:09:34  fernande
 *  Archive Log:    Fixed spelling
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.VFListBean;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;

public class VirtualFabricsTreeUpdater implements ITreeUpdater {
    private final static Logger log = LoggerFactory
            .getLogger(VirtualFabricsTreeUpdater.class);

    private final ISubnetApi subnetApi;

    private final IPerformanceApi perfApi;

    private Map<String, Integer> vfNames;

    protected final Comparator<FVResourceNode> vfNodeComparator;

    protected final Comparator<FVResourceNode> nodeComparator;

    /**
     * Description:
     * 
     * @param subnetApi
     */
    public VirtualFabricsTreeUpdater(ISubnetApi subnetApi,
            IPerformanceApi perfApi) {
        super();
        this.subnetApi = subnetApi;
        this.perfApi = perfApi;

        initData();
        vfNodeComparator = TreeNodeFactory.getVfNodeComparator(vfNames);
        nodeComparator = TreeNodeFactory.getNodeComparator();
    }

    protected void initData() {
        List<VFListBean> vfList = perfApi.getVFList();
        vfNames = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < vfList.size(); i++) {
            vfNames.put(vfList.get(i).getVfName(), i);
        }
    }

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

    @Override
    public void removeNode(int lid, FVResourceNode tree,
            boolean removeEmptyParents, List<ITreeMonitor> monitors) {
        updateNode(lid, tree, removeEmptyParents, monitors);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.monitor.tree.IPartialTreeUpdater#addNode(int,
     * com.intel.stl.ui.monitor.FVResourceNode,
     * com.intel.stl.ui.monitor.FVTreeModel)
     */

    public void updateNode(int lid, FVResourceNode tree,
            boolean removeEmptyParents, List<ITreeMonitor> monitors) {
        NodeRecordBean bean = null;
        try {
            bean = subnetApi.getNode(lid);
        } catch (SubnetDataNotFoundException e) {
        }

        if (bean == null || !bean.isActive()) {
            removeVFNode(lid, tree, removeEmptyParents, monitors);
            return;
        }

        Map<Integer, NodeRecordBean> nodeMap =
                new HashMap<Integer, NodeRecordBean>();
        nodeMap.put(lid, bean);

        List<String> vfNames = perfApi.getVFNames(lid);
        for (String name : vfNames) {
            FVResourceNode vfNode = getVfNode(name, tree, monitors);
            FVResourceNode node = TreeNodeFactory.createNode(bean);

            CreationBasedNodesSynchronizer nodeUpdater =
                    new CreationBasedNodesSynchronizer(subnetApi, nodeMap);
            Vector<FVResourceNode> children = vfNode.getChildren();
            int index =
                    Collections.binarySearch(children, node, nodeComparator);
            if (index < 0) {
                index = -index - 1;
                nodeUpdater.addNode(index, lid, vfNode, monitors, null);
            } else {
                FVResourceNode updateNode = vfNode.getModelChildAt(index);
                nodeUpdater.updateNode(updateNode, vfNode, monitors, null);
            }
        }
    }

    protected FVResourceNode getVfNode(String vfName, FVResourceNode parent,
            List<ITreeMonitor> monitors) {
        if (!vfNames.containsKey(vfName)) {
            // new group, update data
            initData();
        }

        int id = vfNames.get(vfName);
        FVResourceNode node = TreeNodeFactory.createVfNode(vfName, id);
        FVResourceNode oldNode =
                id < parent.getModelChildCount() ? parent.getModelChildAt(id)
                        : null;
        if (!node.equals(oldNode)) {
            parent.addChild(id, node);
            if (monitors != null) {
                int viewId = parent.getViewIndex(id);
                for (ITreeMonitor monitor : monitors) {
                    monitor.fireTreeNodesInserted(this, parent.getPath()
                            .getPath(), new int[] { viewId },
                            new FVResourceNode[] { node });
                }
            }
        } else {
            node = oldNode;
        }

        return node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.monitor.tree.IPartialTreeUpdater#removeNode(int,
     * com.intel.stl.ui.monitor.FVResourceNode,
     * com.intel.stl.ui.monitor.FVTreeModel)
     */
    public void removeVFNode(int lid, FVResourceNode tree,
            boolean removeEmptyParents, List<ITreeMonitor> monitors) {
        for (int i = 0; i < tree.getModelChildCount(); i++) {
            FVResourceNode vfNode = tree.getModelChildAt(i);
            for (int j = 0; j < vfNode.getModelChildCount(); j++) {
                FVResourceNode node = vfNode.getModelChildAt(j);
                if (node.getId() == lid) {
                    int index = j;
                    FVResourceNode parent = vfNode;
                    if (removeEmptyParents && vfNode.getModelChildCount() == 1) {
                        index = tree.getModelIndex(vfNode);
                        parent = tree;
                    }
                    parent.removeChild(index);
                    if (monitors != null) {
                        int viewIndex = parent.getViewIndex(index);
                        for (ITreeMonitor monitor : monitors) {
                            monitor.fireTreeNodesRemoved(this, parent.getPath()
                                    .getPath(), new int[] { viewIndex },
                                    new FVResourceNode[] { node });
                        }
                    }
                    break;
                }
            }
        }
    }
}
