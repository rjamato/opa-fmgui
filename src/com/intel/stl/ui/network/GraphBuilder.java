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
 *  File Name: GraphBuilder.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.16  2015/04/28 13:55:04  jijunwan
 *  Archive Log:    minor improvement to ignore "missed nodes" that only happen when we turn on random value that will creates unmatched list of nodes and links via simulated notices
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/04/15 21:32:00  jijunwan
 *  Archive Log:    PR 128134 - Support other tree like toplogy
 *  Archive Log:    - minor improvement to handle tree variance that may happen when a user mis-connect devices
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/02/05 19:10:50  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/12/11 18:47:05  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/10/22 02:21:26  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/10/09 21:29:45  jijunwan
 *  Archive Log:    new Topology Viz
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/10/01 19:25:53  rjtierne
 *  Archive Log:    Relocated image directory to src/main/image. Added new images for Topology graph and removed hardcoded static path to images
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/09/02 18:57:21  jijunwan
 *  Archive Log:    improvement on topology graph refresh - clear and then update graph
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/08/05 18:39:05  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/08/05 13:46:23  jijunwan
 *  Archive Log:    new implementation on topology control that uses double models to avoid synchronization issues on model and view
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/03 22:23:47  jijunwan
 *  Archive Log:    1) improved Topology to support multiple edges selection
 *  Archive Log:    2) added Tree and Graph selection synchronization
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/26 15:08:24  jijunwan
 *  Archive Log:    improved GraphBuilder to handle isolated nodes
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/05 18:32:52  jijunwan
 *  Archive Log:    changed Channel Adapter to Fabric Interface
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/27 22:08:10  jijunwan
 *  Archive Log:    added tooltip for topology
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.model.GraphNode;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public class GraphBuilder {
    private static final Logger log = LoggerFactory
            .getLogger(GraphBuilder.class);

    public static final int SWITCH_SIZE = 64;

    public static final int HFI_SIZE = 32;

    private static boolean DEBUG = false;

    public GraphBuilder() {
    }

    public TopologyTreeModel build(TopGraph graph, List<NodeRecordBean> nodes,
            List<LinkRecordBean> links) {
        // new Exception().printStackTrace();
        long t = System.currentTimeMillis();
        log.info("Create graph with " + nodes.size() + " nodes, "
                + links.size() + " links");
        Map<Integer, GraphNode> nodesMap = new HashMap<Integer, GraphNode>();
        Set<GraphNode> endNodes = new HashSet<GraphNode>();
        for (NodeRecordBean node : nodes) {
            GraphNode gn = new GraphNode(node.getLid());
            gn.setName(node.getNodeDesc());
            gn.setType(node.getNodeInfo().getNodeType());
            gn.setNumPorts(node.getNodeInfo().getNumPorts());
            if (gn.isEndNode()) {
                gn.setDepth(0);
                endNodes.add(gn);
            }
            nodesMap.put(node.getLid(), gn);
        }
        fillLinks(nodesMap, links);
        List<GraphNode> roots =
                endNodes.isEmpty() ? getRoots(nodesMap.values()) : getRoots(
                        nodesMap.values(), endNodes);
        if (DEBUG) {
            for (GraphNode node : nodesMap.values()) {
                node.dump(System.out);
            }
        }
        log.info("Found " + roots.size() + " root nodes");
        if (DEBUG) {
            System.out.println("Roots: " + roots);
        }
        graph.clear();
        TopologyTreeModel model = fillGraph(graph, roots);
        log.info("Created graph " + graph + " in "
                + (System.currentTimeMillis() - t) + " ms "
                + Thread.currentThread());
        return model;
    }

    protected void fillLinks(Map<Integer, GraphNode> map,
            List<LinkRecordBean> links) {
        for (LinkRecordBean link : links) {
            int fromLid = link.getFromLID();
            GraphNode node = map.get(fromLid);
            int toLid = link.getToLID();
            GraphNode toNode = map.get(toLid);
            if (node != null && toNode != null) {
                node.addLink(toNode, link.getFromPortIndex(),
                        link.getToPortIndex());
            } else {
                if (node == null) {
                    // this shouldn't happen
                    log.warn("Node " + fromLid + " are not in node list");
                }

                if (toNode == null) {
                    // this shouldn't happen
                    log.warn("Node " + toLid + " are not in node list");
                }
            }

        }
    }

    protected List<GraphNode> getRoots(Collection<GraphNode> nodes,
            Set<GraphNode> endNodes) {
        Set<GraphNode> workingNodes = new HashSet<GraphNode>(nodes);
        workingNodes.removeAll(endNodes);
        Set<GraphNode> nextRef = new HashSet<GraphNode>(endNodes);

        boolean hasChange = true;
        while (!workingNodes.isEmpty() && hasChange) {
            Set<GraphNode> refNodes = nextRef;
            nextRef = new HashSet<GraphNode>();
            hasChange = false;
            for (GraphNode ref : refNodes) {
                for (GraphNode gn : ref.getMiddleNeighbor()) {
                    boolean success = workingNodes.remove(gn);
                    if (success) {
                        nextRef.add(gn);
                        gn.setDepth(ref.getDepth() + 1);
                    }
                    if (!hasChange) {
                        hasChange = true;
                    }
                }
            }
        }
        List<GraphNode> res = new ArrayList<GraphNode>(nextRef);
        if (!workingNodes.isEmpty()) {
            res.addAll(workingNodes);
            log.warn("Found isolated nodes " + workingNodes.size());
        }
        Collections.sort(res);
        return res;
    }

    /**
     * 
     * Description: if a subnet has no end nodes, we try switches one by one to
     * figure out the root(s)
     * 
     * @param nodes
     * @return
     */
    protected List<GraphNode> getRoots(Collection<GraphNode> nodes) {
        // simple heuristic: using the switches with least links
        // TODO: test and revisit the approach
        Set<GraphNode> refNodes = new HashSet<GraphNode>();
        int minNumLinks = Integer.MAX_VALUE;
        for (GraphNode node : nodes) {
            int links = node.getMiddleNeighbor().size();
            if (links < minNumLinks) {
                refNodes.clear();
                refNodes.add(node);
            } else if (links == minNumLinks) {
                refNodes.add(node);
            }
        }
        return getRoots(nodes, refNodes);
    }

    protected TopologyTreeModel fillGraph(TopGraph graph, List<GraphNode> roots) {
        List<List<Integer>> ranks = new ArrayList<List<Integer>>();
        int maxRankSize = 0;
        int numNodes = 0;
        Set<GraphNode> processed = new HashSet<GraphNode>();

        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            for (GraphNode node : roots) {
                insertVertex(graph, parent, node);
            }

            List<GraphNode> nextNodes = new ArrayList<GraphNode>(roots);
            while (!nextNodes.isEmpty()) {
                List<Integer> rank = new ArrayList<Integer>();
                for (GraphNode node : nextNodes) {
                    rank.add(node.getLid());
                }
                ranks.add(Collections.unmodifiableList(rank));
                numNodes += rank.size();
                if (nextNodes.size() > maxRankSize) {
                    maxRankSize = nextNodes.size();
                }
                List<GraphNode> workingNodes = nextNodes;
                nextNodes = new ArrayList<GraphNode>();
                for (GraphNode node : workingNodes) {
                    mxCell vertex = graph.getVertex(node.getLid());
                    Set<GraphNode> neighbor = node.getMiddleNeighbor();
                    for (GraphNode nbr : neighbor) {
                        if (processed.contains(nbr)) {
                            continue;
                        }
                        mxCell nbrVertex = graph.getVertex(nbr.getLid());
                        if (nbrVertex == null) {
                            nbrVertex = insertVertex(graph, parent, nbr);
                        }
                        String edgeId = TopGraph.getEdgeId(vertex, nbrVertex);
                        graph.insertEdge(parent, edgeId, null, vertex,
                                nbrVertex);
                        if (!nextNodes.contains(nbr)
                                && !workingNodes.contains(nbr)) {
                            nextNodes.add(nbr);
                        }
                    }
                    neighbor = node.getEndNeighbor();
                    numNodes += neighbor.size();
                    for (GraphNode nbr : neighbor) {
                        if (processed.contains(nbr)) {
                            continue;
                        }
                        mxCell nbrVertex = graph.getVertex(nbr.getLid());
                        if (nbrVertex == null) {
                            nbrVertex = insertVertex(graph, parent, nbr);
                        }
                        String edgeId = TopGraph.getEdgeId(vertex, nbrVertex);
                        graph.insertEdge(parent, edgeId, null, vertex,
                                nbrVertex);
                    }
                }
                processed.addAll(workingNodes);
            }
            TopologyTreeModel model =
                    new TopologyTreeModel(ranks, maxRankSize,
                            new ArrayList<Integer>(), numNodes);
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            graph.getModel().endUpdate();
        }

        return null;
    }

    protected mxCell insertVertex(mxGraph graph, Object parent, GraphNode nbr) {
        NodeType type = NodeType.getNodeType(nbr.getType());
        int w = type == NodeType.HFI ? HFI_SIZE : SWITCH_SIZE;
        int h = type == NodeType.HFI ? HFI_SIZE : SWITCH_SIZE;
        String style =
                type == NodeType.HFI ? "shape=image;image="
                        + UIImages.HFI_IMG.getFileName() : "shape=image;image="
                        + UIImages.SWITCH_EXPANDED_IMG.getFileName();
        Object vertex =
                graph.insertVertex(parent, TopGraph.getVertexId(nbr.getLid()),
                        nbr, 0, 0, w, h, style);
        return (mxCell) vertex;
    }

}
