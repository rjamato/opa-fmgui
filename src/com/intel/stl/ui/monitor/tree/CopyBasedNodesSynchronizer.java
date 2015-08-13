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
 *  Archive Log:    Revision 1.4.2.1  2015/08/12 15:27:10  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
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
        boolean hasChanged = false;
        FVResourceNode refNode = nodeMap.get(node.getId());
        if (refNode == null) {
            throw new IllegalArgumentException("Couldn't find FVResourceNode "
                    + node.getId());
        }
        int toUpdate = Math.min(refNode.getChildCount(), node.getChildCount());
        for (int i = 0; i < toUpdate; i++) {
            // update ports
            FVResourceNode port = node.getChildAt(i);
            FVResourceNode refPort = refNode.getChildAt(i);
            if (port.getType() != refPort.getType()) {
                port.setType(refPort.getType());
                if (!hasChanged) {
                    hasChanged = true;
                }
            }
        }
        if (toUpdate < node.getChildCount()) {
            // remove ports
            for (int i = toUpdate; i < node.getChildCount(); i++) {
                node.removeChild(toUpdate);
            }
            if (!hasChanged) {
                hasChanged = true;
            }
        } else if (toUpdate < refNode.getChildCount()) {
            // add ports
            for (int i = toUpdate; i < refNode.getChildCount(); i++) {
                FVResourceNode port = refNode.getChildAt(i).copy();
                node.addChild(port);
            }
            if (!hasChanged) {
                hasChanged = true;
            }
        }
        if (hasChanged && monitors != null) {
            fireNodesUpdated(monitors, node);
        }
    }

}
