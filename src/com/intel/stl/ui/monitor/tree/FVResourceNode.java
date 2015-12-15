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
 *  Archive Log:    Revision 1.9  2015/09/30 13:26:45  fisherma
 *  Archive Log:    PR 129357 - ability to hide inactive ports.  Also fixes PR 129689 - Connectivity table exhibits inconsistent behavior on Performance and Topology pages
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/08/17 18:54:19  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/08/11 14:38:22  jijunwan
 *  Archive Log:    PR 129917 - No update on event statistics
 *  Archive Log:    - Apply event subscriber on the event card on Performance page
 *  Archive Log:    - fixed the blink chart issue
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/07/15 19:05:39  fernande
 *  Archive Log:    PR 129199 - Checking copyright test as part of the build step. Fixed year appearing in copyright notice
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/07/13 19:45:08  jijunwan
 *  Archive Log:    PR 128980 - Be able to search devices by name or lid
 *  Archive Log:    - changed to use hex string for GUID match
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/06/22 13:11:52  jypak
 *  Archive Log:    PR 128980 - Be able to search devices by name or lid.
 *  Archive Log:    New feature added to enable search devices by name, lid or node guid. The search results are displayed as a tree and when a result node from the tree is selected, original tree is expanded and the corresponding node is highlighted.
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

import com.intel.stl.api.StringUtils;
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

    private Vector<FVResourceNode> visibleChildren;

    /*
     * The parent node
     */
    private FVResourceNode mParent;

    /**
     * Id for the node. For HCA/SW, it will be lid, for a Port, it will be
     * portNumber. For others, it should be type id.
     */
    private final int id;

    private long guid;

    private String guidStr;

    public FVResourceNode(String pTitle, TreeNodeType pType, int id) {
        mTitle = pTitle;
        mType = pType;
        this.id = id;
    }

    public FVResourceNode(String pTitle, TreeNodeType pType, int id, long guid) {
        this(pTitle, pType, id);
        this.guid = guid;
        guidStr = StringUtils.longHexString(guid);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    protected boolean setChildrenVisibility(INodeVisbilityIndicator indicator) {
        synchronized (mChildren) {
            int oldSize = getVisibleChildren().size();
            visibleChildren = new Vector<FVResourceNode>();
            for (FVResourceNode child : mChildren) {
                child.setChildrenVisibility(indicator);
                if (indicator.isVisible(child)) {
                    visibleChildren.add(child);
                }
            }
            return visibleChildren.size() != oldSize;
        }
    }

    public Vector<FVResourceNode> getVisibleChildren() {
        synchronized (mChildren) {
            if (visibleChildren == null) {
                visibleChildren = mChildren;
            }
            return visibleChildren;
        }
    }

    /**
     * @return the guid
     */
    public long getGuid() {
        return guid;
    }

    /**
     * @return the guidStr
     */
    public String getGuidStr() {
        return guidStr;
    }

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
        return getVisibleChildren().elementAt(pChildIndex);
    }

    public FVResourceNode getModelChildAt(int pChildIndex) {
        return mChildren.elementAt(pChildIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#getChildCount()
     */
    @Override
    public int getChildCount() {
        return getVisibleChildren().size();
    }

    public int getModelChildCount() {
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

    public FVResourceNode getRoot() {
        FVResourceNode res = this;
        while (res.getParent() != null) {
            res = res.getParent();
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
     */
    @Override
    public int getIndex(TreeNode pNode) {
        return getVisibleChildren().indexOf(pNode);
    }

    public int getModelIndex(TreeNode pNode) {
        return mChildren.indexOf(pNode);
    }

    public int getViewIndex(int modelIndex) {
        FVResourceNode node = mChildren.get(modelIndex);
        return getIndex(node);
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
        return getVisibleChildren().elements();
    }

    public Enumeration<FVResourceNode> modelChildren() {
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
        FVResourceNode res = null;
        if (isNode()) {
            res = new FVResourceNode(mTitle, mType, id, guid);
        } else {
            res = new FVResourceNode(mTitle, mType, id);
        }
        res.mParent = mParent;
        res.mChildren = new Vector<FVResourceNode>();
        for (FVResourceNode child : mChildren) {
            res.addChild(child.copy());
        }
        if (visibleChildren != null) {
            res.visibleChildren = new Vector<FVResourceNode>();
            for (FVResourceNode vc : visibleChildren) {
                int index = mChildren.indexOf(vc);
                res.visibleChildren.add(res.mChildren.get(index));
            }
        }
        return res;
    }

    public void dump(PrintStream out, String prefix) {
        out.println(prefix + mTitle + " " + mType);
        for (FVResourceNode child : getVisibleChildren()) {
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
