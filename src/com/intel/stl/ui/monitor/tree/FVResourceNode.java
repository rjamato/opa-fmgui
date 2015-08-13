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
 *  File Name: FVResourceNode.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3.2.1  2015/08/12 15:27:10  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
