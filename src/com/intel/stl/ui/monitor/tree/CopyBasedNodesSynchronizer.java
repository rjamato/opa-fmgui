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
 *  File Name: DeviceGroupNodesUpdater.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/09/30 13:26:45  fisherma
 *  Archive Log:    PR 129357 - ability to hide inactive ports.  Also fixes PR 129689 - Connectivity table exhibits inconsistent behavior on Performance and Topology pages
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/08/17 18:54:19  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/05/20 17:05:20  jijunwan
 *  Archive Log:    PR 128797 - Notice update failed to update related notes
 *  Archive Log:    - improved to fire tree update event at port level, so if we select a port that is under change, the port will still get selected and updated
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/05 21:21:44  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/09 21:24:52  jijunwan
 *  Archive Log:    improvement on TreeNodeType:
 *  Archive Log:    1) Added icon to TreeNodeType
 *  Archive Log:    2) Rename PORT to ACTIVE_PORT
 *  Archive Log:    3) Removed NODE
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intel.stl.ui.common.IProgressObserver;

public class CopyBasedNodesSynchronizer extends TreeSynchronizer<Integer> {
    private final Map<Integer, FVResourceNode> nodeMap;

    /**
     * Description:
     * 
     * @param perfApi
     * @param nodeMap
     */
    public CopyBasedNodesSynchronizer(Map<Integer, FVResourceNode> nodeMap) {
        super(false);
        this.nodeMap = nodeMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.tree.FastTreeUpater#compare(com.intel.stl.ui
     * .monitor.FVResourceNode, java.lang.Object)
     */
    @Override
    protected int compare(FVResourceNode node, Integer element) {
        String name1 = node.getName();
        FVResourceNode node2 = nodeMap.get(element);
        String name2 = node2 == null ? null : node2.getName();
        return TreeNodeFactory.comapreNodeName(name1, name2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.tree.TreeUpater#createNode(java.lang.Object)
     */
    @Override
    protected FVResourceNode createNode(Integer key) {
        FVResourceNode node = nodeMap.get(key);
        return node == null ? new FVResourceNode("null", null, -1) : node
                .copy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.tree.TreeUpater#updateNode(com.intel.stl.ui.
     * monitor.FVResourceNode, com.intel.stl.ui.monitor.FVResourceNode,
     * com.intel.stl.ui.monitor.FVTreeModel,
     * com.intel.stl.ui.common.IProgressObserver)
     */
    @Override
    protected void updateNode(FVResourceNode node, FVResourceNode parent,
            List<ITreeMonitor> monitors, IProgressObserver observer) {
        FVResourceNode refNode = nodeMap.get(node.getId());
        if (refNode == null) {
            throw new IllegalArgumentException("Couldn't find FVResourceNode "
                    + node.getId());
        }

        Map<Integer, FVResourceNode> updated =
                new HashMap<Integer, FVResourceNode>();
        boolean hasStructureChange = false;
        int toUpdate =
                Math.min(refNode.getModelChildCount(),
                        node.getModelChildCount());
        for (int i = 0; i < toUpdate; i++) {
            // update ports
            FVResourceNode port = node.getModelChildAt(i);
            FVResourceNode refPort = refNode.getModelChildAt(i);
            if (port.getType() != refPort.getType()) {
                boolean hasChanged = port.getType() != refPort.getType();
                if (hasChanged) {
                    port.setType(refPort.getType());
                    updated.put(i, port);
                }
            }
        }
        if (toUpdate < node.getModelChildCount()) {
            // remove ports
            for (int i = toUpdate; i < node.getModelChildCount(); i++) {
                node.removeChild(toUpdate);
                if (!hasStructureChange) {
                    hasStructureChange = true;
                }
            }
        } else if (toUpdate < refNode.getModelChildCount()) {
            // add ports
            for (int i = toUpdate; i < refNode.getModelChildCount(); i++) {
                FVResourceNode port = refNode.getModelChildAt(i).copy();
                node.addChild(port);
                if (!hasStructureChange) {
                    hasStructureChange = true;
                }
            }
        }

        if (monitors == null) {
            return;
        }

        if (hasStructureChange) {
            fireStructureChanged(monitors, node);
        } else if (!updated.isEmpty()) {
            for (Integer childIndex : updated.keySet()) {
                int viewIndex = node.getViewIndex(childIndex);
                fireNodesUpdated(monitors, node, viewIndex,
                        updated.get(childIndex));
            }
        }
    }

}
