/**
 * INTEL CONFIDENTIAL
 * Copyright (c) ${year} Intel Corporation All Rights Reserved.
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
 *  File Name: FVTreeModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/02/04 21:44:20  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/09 13:28:29  jijunwan
 *  Archive Log:    fixed a bug on tree search
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/02 19:24:28  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/09/02 19:03:00  jijunwan
 *  Archive Log:    tree update based on merge sort algorithm
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/08/26 14:30:36  jijunwan
 *  Archive Log:    improved to support TreeModelEvents
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/08/05 13:39:10  jijunwan
 *  Archive Log:    added #isTypeMatched to provide flexibility on node type comparison
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/11 20:32:28  fernande
 *  Archive Log:    Fixing getTreePathForPort to ignore PORT leaf since their id is the port number and it could be mistaken for a low value LID.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/11 19:26:54  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/07 18:23:18  jijunwan
 *  Archive Log:    minor adjustment - try next sibling first rather than previous
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/03 22:18:00  jijunwan
 *  Archive Log:    added search method to search a node by lid with a given reference node. Matched node that is closest to the reference node will be returned
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 20:47:24  rjtierne
 *  Archive Log:    Relocated from common.view to monitor package
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/17 14:37:42  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: This class provides an implementation of the TreeModel
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor.tree;

import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.intel.stl.ui.monitor.TreeNodeType;

/**
 * @author tierney
 * 
 */
public class FVTreeModel implements TreeModel, ITreeMonitor {

    private final FVResourceNode mRootNode;

    private final Vector<TreeModelListener> mListeners =
            new Vector<TreeModelListener>();

    public FVTreeModel(FVResourceNode pRootNode) {
        mRootNode = pRootNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    @Override
    public Object getRoot() {
        return mRootNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    @Override
    public Object getChild(Object pParent, int pIndex) {
        TreeNode parent = (TreeNode) pParent;
        return parent.getChildAt(pIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    @Override
    public int getChildCount(Object pParent) {
        TreeNode parent = (TreeNode) pParent;
        return parent.getChildCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
     */
    @Override
    public boolean isLeaf(Object pNode) {
        TreeNode node = (TreeNode) pNode;
        return node.isLeaf();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath,
     * java.lang.Object)
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public int getIndexOfChild(Object pParent, Object pChild) {

        TreeNode parent = (TreeNode) pParent;
        TreeNode child = (TreeNode) pChild;

        return parent.getIndex(child);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.
     * TreeModelListener)
     */
    @Override
    public void addTreeModelListener(TreeModelListener pListener) {
        mListeners.add(pListener);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.
     * TreeModelListener)
     */
    @Override
    public void removeTreeModelListener(TreeModelListener pListener) {
        mListeners.remove(pListener);

    }

    /**
     * 
     * Description: find tree node by lid based on closest first search
     * 
     * @param lid
     *            target lid to find
     * @param type
     *            target type
     * @param hint
     *            the reference node used for search
     * @return
     */
    public TreePath getTreePath(int lid, TreeNodeType type, FVResourceNode hint) {
        FVResourceNode tmp =
                navigateTree(hint == null ? mRootNode : hint, lid, type);
        if (tmp == null && hint != null) {
            tmp = siblingSearch(hint, lid, type);
        }
        if (tmp == null) {
            return null;
        }

        return tmp.getPath();
    }

    protected FVResourceNode siblingSearch(FVResourceNode node, int lid,
            TreeNodeType type) {
        FVResourceNode tmp = null;

        FVResourceNode parent = node.getParent();
        if (parent == null) {
            return null;
        }

        int ref = parent.getIndex(node);
        int step = 1;
        while (ref - step >= 0 || ref + step < parent.getChildCount()) {
            if (ref + step < parent.getChildCount()) {
                node = parent.getChildAt(ref + step);
                tmp = navigateTree(node, lid, type);
                if (tmp != null) {
                    return tmp;
                }
            }

            if (ref - step >= 0) {
                node = parent.getChildAt(ref - step);
                tmp = navigateTree(node, lid, type);
                if (tmp != null) {
                    return tmp;
                }
            }

            step += 1;
        }

        return siblingSearch(parent, lid, type);
    }

    protected FVResourceNode navigateTree(FVResourceNode node, int lid,
            TreeNodeType type) {
        if (node.isTypeMatched(type) && node.getId() == lid) {
            return node;
        }

        if (node.getChildren() != null) {
            for (FVResourceNode child : node.getChildren()) {
                FVResourceNode tmp = navigateTree(child, lid, type);
                if (tmp != null) {
                    return tmp;
                }
            }
        }
        return null;
    }

    public TreePath getTreePathForPort(int lid, short portNum,
            FVResourceNode hint) {
        FVResourceNode tmp =
                navigateTree(hint == null ? mRootNode : hint, lid,
                        TreeNodeType.NODE);
        if (tmp == null) {
            return null;
        }
        TreeNodeType type = tmp.getType();
        if (type == TreeNodeType.HFI || type == TreeNodeType.SWITCH
                || type == TreeNodeType.ROUTER) {
            if (tmp.getChildren() != null) {
                for (FVResourceNode port : tmp.getChildren()) {
                    if (port.getId() == portNum) {
                        return port.getPath();
                    }
                }
            }
        }
        return tmp.getPath();
    }

    @Override
    public void fireTreeNodesRemoved(Object source, Object[] path,
            int[] childIndices, Object[] children) {
        TreeModelEvent e =
                new TreeModelEvent(source, path, childIndices, children);
        // System.out.println("TreeNodesRemoved " + e);
        for (TreeModelListener listener : mListeners) {
            listener.treeNodesRemoved(e);
        }
    }

    @Override
    public void fireTreeNodesInserted(Object source, Object[] path,
            int[] childIndices, Object[] children) {
        TreeModelEvent e =
                new TreeModelEvent(source, path, childIndices, children);
        // System.out.println("TreeNodesInserted " + e);
        for (TreeModelListener listener : mListeners) {
            listener.treeNodesInserted(e);
        }
    }

    @Override
    public void fireTreeStructureChanged(Object source, TreePath path) {
        TreeModelEvent e = new TreeModelEvent(source, path);
        // System.out.println("TreeStructureChanged " + e);
        for (TreeModelListener listener : mListeners) {
            listener.treeStructureChanged(e);
        }
    }
}
