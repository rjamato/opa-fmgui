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
 *  File Name: PartialTreeUpdater.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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

import java.util.List;

/**
 * Partially update a tree
 */
public interface ITreeUpdater {
    /**
     * 
     * <i>Description:</i> add a node (described by lid) to the given tree
     * 
     * @param lid
     *            the node to be added
     * @param tree
     *            the tree (or tree branch) to update
     * @param monitors
     *            the monitors used to first tree change event, so the
     *            listeners, such as a JTree, can update the visualization
     */
    public void addNode(int lid, FVResourceNode tree,
            List<ITreeMonitor> monitors);

    /**
     * 
     * <i>Description:</i> remove a node from the given tree
     * 
     * @param lid
     *            the node to be removed
     * @param tree
     *            same as {@link #addNode(int, FVResourceNode, List)}
     * @param removeEmptyParents
     *            indicates whether remove its parents when they are empty after
     *            we removed the node
     * @param monitors
     *            same as {@link #addNode(int, FVResourceNode, List)}
     */
    public void removeNode(int lid, FVResourceNode tree,
            boolean removeEmptyParents, List<ITreeMonitor> monitors);

    /**
     * 
     * <i>Description:</i>update a node in the given tree
     * 
     * @param lid
     *            the node to be updated
     * @param tree
     *            same as {@link #addNode(int, FVResourceNode, List)}
     * @param monitors
     *            same as {@link #addNode(int, FVResourceNode, List)}
     */
    public void updateNode(int lid, FVResourceNode tree,
            List<ITreeMonitor> monitors);
}
