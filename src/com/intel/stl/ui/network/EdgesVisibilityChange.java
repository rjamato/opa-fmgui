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
 *  File Name: FlipNodeChange.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/12/11 18:47:05  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/05 13:46:23  jijunwan
 *  Archive Log:    new implementation on topology control that uses double models to avoid synchronization issues on model and view
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.model.GraphEdge;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.model.LayoutType;

/**
 * For a given list of edges, make any necessary change on the model to
 * ensure all given edges are visible on our graph.
 */
public class EdgesVisibilityChange extends LayoutChange {
    private static final Logger log = LoggerFactory.getLogger(NodesVisibilityChange.class);
    
    /**
     * the list of edges to inspect. Each edge is simply described as a Point 
     * where x = source LID, and y = target LID.  
     */
    private GraphEdge[] toInspect;
    
    /**
     * Description: 
     *
     * @param type
     * @param toInspect 
     */
    public EdgesVisibilityChange(LayoutType type,
            TopologyTreeModel topTreeModel, GraphEdge[] toInspect) {
        super(type, topTreeModel);
        this.toInspect = toInspect;
    }

    /* (non-Javadoc)
     * @see com.intel.stl.ui.network.IModelChange#execute(com.intel.stl.ui.network.TopGraph, com.intel.stl.ui.common.ICancelIndicator)
     */
    @Override
    public boolean execute(TopGraph graph, ICancelIndicator indicator) {
        boolean hasChange = false;
        for (GraphEdge edge : toInspect) {
            GraphNode fromNode = (GraphNode)graph.getVertex(edge.getFromLid()).getValue();
            GraphNode toNode = (GraphNode)graph.getVertex(edge.getToLid()).getValue();

            // expand a node when necessary to ensure this link is visible
            if (fromNode.isEndNode() && toNode.isCollapsed()) {
                toNode.setCollapsed(false);
                if (!hasChange) {
                    hasChange = true;
                }
            } else if (toNode.isEndNode() && fromNode.isCollapsed()) {
                fromNode.setCollapsed(false);
                if (!hasChange) {
                    hasChange = true;
                }
            }
        }
        
        if (hasChange) {
            super.execute(graph, indicator);
        }
        
        return hasChange;
    }

}
