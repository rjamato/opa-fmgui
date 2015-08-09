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
 *  File Name: mxCollapseChange.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/06/23 04:56:56  jijunwan
 *  Archive Log:    new topology code to support interactions with a topology graph
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/17 16:55:41  jijunwan
 *  Archive Log:    pan and zoom at current mouse point; shift+mouse drag to move nodes on screen; right control to clear selections; undoable node collapse and expand
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import com.intel.stl.ui.model.GraphNode;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

public class CollapseChange extends mxAtomicGraphModelChange {
    protected GraphNode node;
    protected boolean collapsed, previous = true;
    
    public CollapseChange() {
        this(null, null, false);
    }
    
    /**
     * Description: 
     *
     * @param node
     * @param collapsed
     * @param previous 
     */
    public CollapseChange(mxGraphModel model, GraphNode node, boolean collapsed) {
        super(model);
        this.node = node;
        this.collapsed = collapsed;
        this.previous = collapsed;
    }

    /**
     * @return the node
     */
    public GraphNode getNode() {
        return node;
    }

    /**
     * @param node the node to set
     */
    public void setNode(GraphNode node) {
        this.node = node;
    }

    /**
     * @return the collapsed
     */
    public boolean isCollapsed() {
        return collapsed;
    }

    /**
     * @param collapsed the collapsed to set
     */
    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    /**
     * @return the previous
     */
    public boolean isPrevious() {
        return previous;
    }

    /**
     * @param previous the previous to set
     */
    public void setPrevious(boolean previous) {
        this.previous = previous;
    }

    /* (non-Javadoc)
     * @see com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange#execute()
     */
    @Override
    public void execute() {
        collapsed = previous;
        previous = node.isCollapsed();
        node.setCollapsed(collapsed);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "mxCollapseChange [node=" + node + ", collapsed=" + collapsed
                + ", previous=" + previous + "]";
    }

}
