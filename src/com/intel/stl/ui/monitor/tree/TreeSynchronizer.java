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
 *  File Name: TreeUpater.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/10/09 12:36:03  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
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

import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.ObserverAdapter;

/**
 * Synchronize a tree to given elements
 */
public abstract class TreeSynchronizer<E> {
    protected final boolean removeEmptyGroup;

    /**
     * Description:
     * 
     * @param nodeComparator
     */
    public TreeSynchronizer(boolean removeEmptyGroup) {
        super();
        this.removeEmptyGroup = removeEmptyGroup;
    }

    /**
     * 
     * <i>Description:</i> update a tree branch to ensure its members
     * synchronize with the given elements that are described by keys. The
     * algorithm used here is very similar to merged sort.
     * 
     * @param parent
     *            the tree branch to update
     * @param elements
     *            the desired elements in the given tree branch
     * @param monitors
     *            the monitors used to fire tree change events when necessary
     */
    public void updateTree(FVResourceNode parent, E[] elements,
            List<ITreeMonitor> monitors, IProgressObserver observer) {
        if (observer == null) {
            observer = new ObserverAdapter();
        }

        int oldIndex = 0;
        int newIndex = 0;
        FVResourceNode oldNode =
                oldIndex >= parent.getChildCount() ? null : parent
                        .getChildAt(oldIndex);
        E element = elements[newIndex];
        while (oldNode != null || element != null) {
            int comp =
                    oldNode == null ? 1 : (element == null ? -1 : compare(
                            oldNode, element));
            if (comp == 0) {
                updateNode(oldNode, parent, monitors, observer);
                oldIndex += 1;
                oldNode =
                        oldIndex >= parent.getChildCount() ? null : parent
                                .getChildAt(oldIndex);
                newIndex += 1;
                element =
                        newIndex >= elements.length ? null : elements[newIndex];
            } else if (comp > 0) {
                addNode(oldIndex, element, parent, monitors, observer);
                oldIndex += 1;
                newIndex += 1;
                element =
                        newIndex >= elements.length ? null : elements[newIndex];
            } else {
                removeNode(oldIndex, oldNode, parent, monitors, observer);
                oldNode =
                        oldIndex >= parent.getChildCount() ? null : parent
                                .getChildAt(oldIndex);
            }
        }

        if (removeEmptyGroup) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                FVResourceNode node = parent.getChildAt(i);
                if (node.getChildCount() == 0) {
                    parent.removeChild(i);
                    if (monitors != null) {
                        fireNodesRemoved(monitors, parent, i, node);
                    }
                    i -= 1;
                }
            }
        }
    }

    protected void fireNodesRemoved(final List<ITreeMonitor> monitors,
            final FVResourceNode parent, final int childIndex,
            final FVResourceNode child) {
        for (ITreeMonitor monitor : monitors) {
            monitor.fireTreeNodesRemoved(this, parent.getPath().getPath(),
                    new int[] { childIndex }, new FVResourceNode[] { child });
        }
    }

    protected abstract int compare(FVResourceNode node, E element);

    protected FVResourceNode addNode(int index, E key, FVResourceNode parent,
            List<ITreeMonitor> monitors, IProgressObserver observer) {
        FVResourceNode node = createNode(key);
        assert node != null : "Node shoudn't be null";
        parent.addChild(index, node);
        if (monitors != null) {
            fireNodesInserted(monitors, parent, index, node);
        }
        return node;
    }

    protected void fireNodesInserted(final List<ITreeMonitor> monitors,
            final FVResourceNode parent, final int childIndex,
            final FVResourceNode child) {
        for (ITreeMonitor monitor : monitors) {
            monitor.fireTreeNodesInserted(this, parent.getPath().getPath(),
                    new int[] { childIndex }, new FVResourceNode[] { child });
        }
    }

    protected abstract FVResourceNode createNode(E key);

    /**
     * 
     * <i>Description:</i> Remove a device node from the given tree branch
     * 
     * @param bean
     *            the node record to be removed
     * @param parent
     *            the tree branch to update
     * @param monitors
     *            the monitors used to fire tree change event when necessary
     */
    protected void removeNode(int index, FVResourceNode node,
            FVResourceNode parent, List<ITreeMonitor> monitors,
            IProgressObserver observer) {
        parent.removeChild(index);
        if (monitors != null) {
            fireNodesRemoved(monitors, parent, index, node);
        }
    }

    /**
     * 
     * <i>Description:</i> update a node in a given tree branch
     * 
     * @param bean
     *            the node record to be updated on a tree branch
     * @param parent
     *            the tree branch to update
     * @param monitors
     *            the monitors used to fire tree change events when necessary
     */
    protected abstract void updateNode(FVResourceNode node,
            FVResourceNode parent, List<ITreeMonitor> monitors,
            IProgressObserver observer);

    protected void fireNodesUpdated(final List<ITreeMonitor> monitors,
            final FVResourceNode node) {
        for (ITreeMonitor monitor : monitors) {
            monitor.fireTreeStructureChanged(this, node.getPath());
        }
    }
}
