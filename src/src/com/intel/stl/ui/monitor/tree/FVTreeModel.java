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
 *  File Name: FVTreeModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.13  2015/12/03 17:15:52  jijunwan
 *  Archive Log:    PR 131865 - Klocwork Issue on TreeController
 *  Archive Log:    - fixed null pointer issues
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/10/23 19:19:11  jijunwan
 *  Archive Log:    PR 129357 - Be able to hide inactive ports
 *  Archive Log:    - improved to support both model tree and view tree
 *  Archive Log:    - change to always use view tree for interaction
 *  Archive Log:    - added helper method to transfer a TreePath to current view tree's TreePath
 *  Archive Log:    - improved to fire tree event with checking on whether the event is for the current view tree
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/10/15 21:20:59  jijunwan
 *  Archive Log:    catch exception on treeNodesRemoved
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/09/30 13:26:45  fisherma
 *  Archive Log:    PR 129357 - ability to hide inactive ports.  Also fixes PR 129689 - Connectivity table exhibits inconsistent behavior on Performance and Topology pages
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/09/08 14:59:54  jijunwan
 *  Archive Log:    PR 130277 - FM GUI Locked up due to [AWT-EventQueue-0] ERROR - Unsupported MTUSize 0x0d java.lang.IllegalArgumentException: Unsupported MTUSize 0x0d
 *  Archive Log:    - change to update tree on background thread and update rendering on EDT
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/08/17 18:54:19  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/08/05 03:18:19  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - improved tree navigation to search a group node
 *  Archive Log:    - fixed issue on port navigation that use a port node as device node by mistake
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/07/15 19:05:39  fernande
 *  Archive Log:    PR 129199 - Checking copyright test as part of the build step. Fixed year appearing in copyright notice
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/06/05 16:45:29  jijunwan
 *  Archive Log:    PR 129089 - Link jumping doesn't keep context
 *  Archive Log:    - search in current tree and use current node as hint in node search
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/05/20 17:05:20  jijunwan
 *  Archive Log:    PR 128797 - Notice update failed to update related notes
 *  Archive Log:    - improved to fire tree update event at port level, so if we select a port that is under change, the port will still get selected and updated
 *  Archive Log:
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.monitor.TreeNodeType;

/**
 * @author tierney
 * 
 */
public class FVTreeModel implements TreeModel, ITreeMonitor {
    private final static Logger log = LoggerFactory
            .getLogger(FVTreeModel.class);

    private final FVResourceNode mRootNode;

    private FVResourceNode vizRootNode;

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
        return vizRootNode;
    }

    public Object getOrgRoot() {
        return mRootNode;
    }

    public void filter(INodeVisbilityIndicator indicator) {
        if (mRootNode != null) {
            this.vizRootNode =
                    indicator.isEnabled() ? mRootNode.filter(indicator)
                            : mRootNode;
        }
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
     * <i>Description:</i> update TreePath from one tree model to TreePath in
     * this model
     * 
     * @param old
     * @return
     */
    public TreePath getTreePath(TreePath old) throws IllegalArgumentException {
        Object[] elements = old.getPath();
        FVResourceNode currentRoot = (FVResourceNode) getRoot();
        if (elements[0] == currentRoot) {
            // the same tree model
            return old;
        }

        FVResourceNode[] newElements = new FVResourceNode[elements.length];
        newElements[0] = currentRoot;
        for (int i = 1; i < elements.length; i++) {
            int index =
                    newElements[i - 1].getIndex((FVResourceNode) elements[i]);
            if (index == -1) {
                throw new IllegalArgumentException(
                        "Source TreePath doesn't match this model");
            } else {
                newElements[i] = newElements[i - 1].getChildAt(index);
            }
        }
        return new TreePath(newElements);
    }

    public TreePath[] getTreePaths(TreePath[] old) {
        List<TreePath> res = new ArrayList<TreePath>();
        for (TreePath path : old) {
            try {
                TreePath newPath = getTreePath(path);
                res.add(newPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res.toArray(new TreePath[0]);
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
                navigateTree(hint == null ? vizRootNode : hint, lid, type);
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
        if (node.isPort()) {
            // get node since we are searching node by lid
            parent = parent.getParent();
        }
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
                navigateTree(hint == null ? vizRootNode : hint, lid,
                        TreeNodeType.NODE);
        if (tmp == null && hint != null) {
            tmp = siblingSearch(hint, lid, TreeNodeType.NODE);
        }
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

    /**
     * <i>Description:</i> search by matching name and type from root
     * 
     * @param name
     * @param type
     * @return
     */
    public TreePath getTreePath(String name, TreeNodeType type) {
        FVResourceNode res = navigateTree(name, type);
        if (res != null) {
            return res.getPath();
        } else {
            return null;
        }
    }

    // breadth first search
    protected FVResourceNode navigateTree(String name, TreeNodeType type) {
        if (vizRootNode.getTitle().equals(name)
                && vizRootNode.getType() == type) {
            return vizRootNode;
        }

        Queue<FVResourceNode> toDo = new LinkedList<FVResourceNode>();
        toDo.add(vizRootNode);
        while (!toDo.isEmpty()) {
            FVResourceNode node = toDo.poll();
            if (node.getChildCount() > 0) {
                for (FVResourceNode child : node.getChildren()) {
                    if (child.getTitle().equals(name)
                            && child.getType() == type) {
                        return child;
                    } else {
                        toDo.add(child);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void fireTreeNodesRemoved(Object source, final Object[] path,
            int[] childIndices, Object[] children) {
        if (vizRootNode != path[0]) {
            // if we are using filtered tree, these events doesn't apply
            return;
        }

        final TreeModelEvent e =
                new TreeModelEvent(source, path, childIndices, children);
        // System.out.println("TreeNodesRemoved " + e);
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                for (TreeModelListener listener : mListeners) {
                    try {
                        listener.treeNodesRemoved(e);
                    } catch (Exception e1) {
                        log.warn("Error on remove nodes event " + e, e1);
                        ((FVResourceNode) path[path.length - 1]).dump(
                                System.out, "E ");
                    }
                }
            }
        });
    }

    @Override
    public void fireTreeNodesInserted(Object source, final Object[] path,
            int[] childIndices, Object[] children) {
        if (vizRootNode != path[0]) {
            // if we are using filtered tree, these events doesn't apply
            return;
        }

        final TreeModelEvent e =
                new TreeModelEvent(source, path, childIndices, children);
        // System.out.println("TreeNodesInserted " + e);
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                for (TreeModelListener listener : mListeners) {
                    try {
                        listener.treeNodesInserted(e);
                    } catch (Exception e1) {
                        log.warn("Error on insert nodes event " + e, e1);
                        ((FVResourceNode) path[path.length - 1]).dump(
                                System.out, "E ");
                    }
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.tree.ITreeMonitor#fireTreeNodesChanged(java.
     * lang.Object, java.lang.Object[], int[], java.lang.Object[])
     */
    @Override
    public void fireTreeNodesChanged(Object source, final Object[] path,
            final int[] childIndices, Object[] children) {
        if (vizRootNode != path[0]) {
            // if we are using filtered tree, these events doesn't apply
            return;
        }

        final TreeModelEvent e =
                new TreeModelEvent(source, path, childIndices, children);
        // System.out.println("TreeNodesChaneed " + e);
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                for (TreeModelListener listener : mListeners) {
                    try {
                        listener.treeNodesChanged(e);
                    } catch (Exception e1) {
                        log.warn("Error on nodes change event " + e, e1);
                        ((FVResourceNode) path[path.length - 1]).dump(
                                System.out, "E ");
                    }
                }
            }
        });
    }

    @Override
    public void fireTreeStructureChanged(Object source, final TreePath path) {
        if (vizRootNode != path.getPath()[0]) {
            // if we are using filtered tree, these events doesn't apply
            return;
        }

        final TreeModelEvent e = new TreeModelEvent(source, path);
        // System.out.println("TreeStructureChanged " + e);
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                for (TreeModelListener listener : mListeners) {
                    try {
                        listener.treeStructureChanged(e);
                    } catch (Exception e1) {
                        log.warn("Error on structure change event " + e, e1);
                        ((FVResourceNode) path.getLastPathComponent()).dump(
                                System.out, "E ");
                    }
                }
            }
        });
    }

}
