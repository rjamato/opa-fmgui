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
 *  File Name: TreeModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/02/18 19:32:02  jijunwan
 *  Archive Log:    PR 127102 - Overall summary of Switches under Topology page does not report correct number of switch ports
 *  Archive Log:     - improved the calculation to count both internal and external ports
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/12/11 18:47:05  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/22 02:21:26  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/09 21:29:45  jijunwan
 *  Archive Log:    new Topology Viz
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/05 13:46:23  jijunwan
 *  Archive Log:    new implementation on topology control that uses double models to avoid synchronization issues on model and view
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/03 22:23:47  jijunwan
 *  Archive Log:    1) improved Topology to support multiple edges selection
 *  Archive Log:    2) added Tree and Graph selection synchronization
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/23 04:56:56  jijunwan
 *  Archive Log:    new topology code to support interactions with a topology graph
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/29 04:05:29  jijunwan
 *  Archive Log:    fixed undo issue happened after we expand or collapse all nodes
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/27 13:48:39  jijunwan
 *  Archive Log:    added connection highlight
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/23 19:47:54  jijunwan
 *  Archive Log:    init version of topology page
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.ui.model.GraphNode;
import com.mxgraph.model.mxCell;

/**
 * model stores tree structure information to help us do tree layout.
 */
public class TopologyTreeModel {
    private final static Logger log = LoggerFactory
            .getLogger(TopologyTreeModel.class);

    private final List<List<Integer>> ranks;

    /**
     * The max width across all ranks
     */
    private final int maxWidth;

    private final int numTotalNodes;

    private final List<Integer> unclassifiedNodes;

    public TopologyTreeModel(List<List<Integer>> ranks, int maxRankSize,
            List<Integer> unclassifiedNodes, int numTotalNodes) {
        this.ranks = Collections.unmodifiableList(ranks);
        this.maxWidth = maxRankSize;
        this.unclassifiedNodes = unclassifiedNodes;
        this.numTotalNodes = numTotalNodes;
    }

    /**
     * @return the nodeLevels
     */
    public List<List<Integer>> getRanks() {
        return ranks;
    }

    /**
     * @return the maxWidth
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    public int getNumRanks() {
        return ranks.size();
    }

    /**
     * @return the numNodes
     */
    public int getNumTotalNodes() {
        return numTotalNodes;
    }

    /**
     * @return the isolatedNodes
     */
    public List<Integer> getUnclassifiedNodes() {
        return unclassifiedNodes;
    }

    public TopologyTreeModel filterBy(TopGraph graph) {
        Object[] allNodes = graph.getVertices();
        Set<Integer> allLids = new HashSet<Integer>();
        for (Object node : allNodes) {
            allLids.add(((GraphNode) ((mxCell) node).getValue()).getLid());
        }
        List<List<Integer>> newRanks = new ArrayList<List<Integer>>();
        int newMaxWidth = 0;
        for (List<Integer> rank : ranks) {
            List<Integer> newRank = new ArrayList<Integer>();
            for (Integer nodeId : rank) {
                mxCell cell = graph.getVertex(nodeId);
                if (cell != null) {
                    newRank.add(nodeId);
                    allLids.remove(nodeId);
                    Set<GraphNode> neighbors =
                            ((GraphNode) cell.getValue()).getEndNeighbor();
                    for (GraphNode nbr : neighbors) {
                        allLids.remove(nbr.getLid());
                    }
                }
            }
            newRanks.add(newRank);
            if (newRank.size() > newMaxWidth) {
                newMaxWidth = newRank.size();
            }
        }
        return new TopologyTreeModel(newRanks, newMaxWidth,
                new ArrayList<Integer>(allLids), allNodes.length);
    }

    public void dump(PrintStream out) {
        out.println(ranks.size() + " tiers");
        for (int i = 0; i < ranks.size(); i++) {
            out.println(i + " " + ranks.get(i));
        }
        out.println(unclassifiedNodes.size() + " unclassifiedNodes "
                + unclassifiedNodes);
    }
}
