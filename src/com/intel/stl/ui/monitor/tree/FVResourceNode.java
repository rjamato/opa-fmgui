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
 *  File Name: FVResourceNode.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/10/09 21:29:04  jijunwan
 *  Archive Log:    Added two helper methods: #isNode and #isPort
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/03 20:38:44  jijunwan
 *  Archive Log:    minor improvement on tree synchronizer
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/02 19:24:28  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/09/02 19:01:24  jijunwan
 *  Archive Log:    forced to assign id for a node, use id and name for hash code calculation
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/08/26 14:27:05  jijunwan
 *  Archive Log:    improved to be able to insert or remove a child
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/08/05 13:39:10  jijunwan
 *  Archive Log:    added #isTypeMatched to provide flexibility on node type comparison
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/03 22:16:04  jijunwan
 *  Archive Log:    minor change - added a helper method to return path for a node
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/05 17:34:10  jijunwan
 *  Archive Log:    added vFabric into Tree View
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/09 21:00:43  jijunwan
 *  Archive Log:    added property; fixed remembering last subpage issue; fixed position problem on IntelTabbedPane
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/04/29 16:44:17  jijunwan
 *  Archive Log:    use the new NameSorter in resource tree
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/28 23:23:19  rjtierne
 *  Archive Log:    Added TitleRecord for storing title names with
 *  Archive Log:    prefix, center, and suffix for sorting
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/23 19:54:06  rjtierne
 *  Archive Log:    Added setType()
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/23 13:45:37  jijunwan
 *  Archive Log:    improvement on TreeView
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 20:47:24  rjtierne
 *  Archive Log:    Relocated from common.view to monitor package
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/17 22:15:49  jijunwan
 *  Archive Log:    device type and device group tree build
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/17 14:37:12  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: The class provides an implementation of the TreeNode interface
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor.tree;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.intel.stl.ui.monitor.TreeNodeType;

/**
 * @author tierney
 * 
 */
public class FVResourceNode implements TreeNode {

    /*
     * Type of this node, which is used by a renderer to set appropriate icon
     * for the node
     */
    private TreeNodeType mType;

    /*
     * Title for a node
     */
    private String mTitle;

    /*
     * Vector holding the children of a node
     */
    private Vector<FVResourceNode> mChildren = new Vector<FVResourceNode>();

    /*
     * The parent node
     */
    private FVResourceNode mParent;

    /**
     * Id for the node. For HCA/SW, it will be lid, for a Port, it will be
     * portNumber. For others, it should be type id.
     */
    private final int id;

    public FVResourceNode(String pTitle, TreeNodeType pType, int id) {
        mTitle = pTitle;
        mType = pType;
        this.id = id;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    // /**
    // * @param id
    // * the id to set
    // */
    // public void setId(int id) {
    // this.id = id;
    // }

    public void addChild(FVResourceNode child) {
        mChildren.add(child);
        child.setParent(this);
    }

    public void addChild(int index, FVResourceNode child) {
        mChildren.add(index, child);
        child.setParent(this);
    }

    public FVResourceNode removeChild(int index) {
        FVResourceNode node = mChildren.remove(index);
        if (node != null) {
            node.setParent(null);
        }
        return node;
    }

    public void setParent(FVResourceNode pParent) {
        mParent = pParent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#getChildAt(int)
     */
    @Override
    public FVResourceNode getChildAt(int pChildIndex) {
        return mChildren.elementAt(pChildIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#getChildCount()
     */
    @Override
    public int getChildCount() {
        return mChildren.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#getParent()
     */
    @Override
    public FVResourceNode getParent() {
        return mParent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
     */
    @Override
    public int getIndex(TreeNode pNode) {
        return mChildren.indexOf(pNode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#getAllowsChildren()
     */
    @Override
    public boolean getAllowsChildren() {

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#isLeaf()
     */
    @Override
    public boolean isLeaf() {
        return (mChildren.size() == 0);
    }

    public boolean isTypeMatched(TreeNodeType type) {
        if (mType == type) {
            return true;
        } else if (type == TreeNodeType.NODE
                && (mType == TreeNodeType.HFI || mType == TreeNodeType.SWITCH)) {
            return true;
        }
        return false;
    }

    public boolean isNode() {
        return mType == TreeNodeType.HFI || mType == TreeNodeType.SWITCH;
    }

    public boolean isPort() {
        return mType == TreeNodeType.ACTIVE_PORT
                || mType == TreeNodeType.INACTIVE_PORT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#children()
     */
    @Override
    public Enumeration<FVResourceNode> children() {
        return mChildren.elements();
    }

    /**
     * @return the mChildren
     */
    public Vector<FVResourceNode> getChildren() {
        return mChildren;
    }

    public TreePath getPath() {
        List<FVResourceNode> path = new ArrayList<FVResourceNode>();
        FVResourceNode tmp = this;
        path.add(tmp);
        while ((tmp = tmp.getParent()) != null) {
            path.add(0, tmp);
        }
        return new TreePath(path.toArray(new FVResourceNode[0]));
    }

    /**
     * The node object should override this method to provide a text that will
     * be displayed for the node in the tree.
     */
    @Override
    public String toString() {
        return mTitle;
    }

    public String getName() {
        return mTitle;
    }

    public TreeNodeType getType() {
        return mType;
    }

    public void setType(TreeNodeType pType) {
        mType = pType;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public FVResourceNode copy() {
        FVResourceNode res = new FVResourceNode(mTitle, mType, id);
        res.mParent = mParent;
        res.mChildren = new Vector<FVResourceNode>();
        for (FVResourceNode child : mChildren) {
            res.addChild(child.copy());
        }
        return res;
    }

    public void dump(PrintStream out, String prefix) {
        out.println(prefix + mTitle + " " + mType);
        for (FVResourceNode child : mChildren) {
            child.dump(out, prefix + "  ");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((mTitle == null) ? 0 : mTitle.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FVResourceNode other = (FVResourceNode) obj;
        if (id != other.id) {
            return false;
        }
        if (mTitle == null) {
            if (other.mTitle != null) {
                return false;
            }
        } else if (!mTitle.equals(other.mTitle)) {
            return false;
        }
        return true;
    }

} // class FVResourceNode
