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
 *  File Name: TopSummaryProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/02/18 19:32:02  jijunwan
 *  Archive Log:    PR 127102 - Overall summary of Switches under Topology page does not report correct number of switch ports
 *  Archive Log:     - improved the calculation to count both internal and external ports
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/11 23:11:15  jijunwan
 *  Archive Log:    renamed PortUtils to Utils
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/11 18:47:05  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/23 16:00:04  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/22 02:21:26  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.Utils;
import com.intel.stl.api.configuration.LinkSpeedMask;
import com.intel.stl.api.configuration.LinkWidthMask;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.model.NodeTypeViz;
import com.intel.stl.ui.model.PropertyItem;
import com.intel.stl.ui.model.PropertySet;
import com.intel.stl.ui.model.SimplePropertyCategory;
import com.intel.stl.ui.model.SimplePropertyGroup;
import com.intel.stl.ui.model.SimplePropertyKey;
import com.intel.stl.ui.network.TopologyTier.Quality;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;

public class TopologySummaryProcessor {
    private final Logger log = LoggerFactory
            .getLogger(TopologySummaryProcessor.class);

    private final String subnetSumName;

    private final String topSumName;

    private final ISubnetApi subnetApi;

    private final TopologyTreeModel topArch;

    /**
     * The full graph holds the whole subnet, so it's possible for us to figure
     * out external ports
     */
    private final TopGraph refGraph;

    /**
     * current graph
     */
    private final TopGraph graph;

    private final ICancelIndicator cancelIndicator;

    /**
     * Description:
     * 
     * @param name
     * @param topArch
     * @param cancelIndicator
     */
    public TopologySummaryProcessor(String subnetSumName, String topSumName,
            TopologyTreeModel topArch, TopGraph graph, TopGraph fullGraph,
            ISubnetApi subnetApi, ICancelIndicator cancelIndicator) {
        super();
        this.subnetSumName = subnetSumName;
        this.topSumName = topSumName;
        this.topArch = topArch;
        this.subnetApi = subnetApi;
        this.graph = graph;
        this.refGraph = fullGraph;
        this.cancelIndicator = cancelIndicator;
    }

    public PropertySet<SimplePropertyGroup> populate() {
        PropertySet<SimplePropertyGroup> model =
                new PropertySet<SimplePropertyGroup>();

        TopologyTier[] tiers = createTiers();
        SimplePropertyGroup topGroup = new SimplePropertyGroup(topSumName);
        for (TopologyTier tier : tiers) {
            if (!cancelIndicator.isCancelled()) {
                SimplePropertyCategory category =
                        new SimplePropertyCategory(tier.getName(), null);
                SimplePropertyKey key = new SimplePropertyKey(tier.getName());
                PropertyItem<SimplePropertyKey> item =
                        new PropertyItem<SimplePropertyKey>(key, tier);
                category.addItem(item);
                topGroup.addPropertyCategory(category);
            }
        }

        int numSwitches = 0;
        int numHFIs = 0;
        int numActiveSwitchPorts = 0;
        int numOtherPorts = 0;
        Object[] allVertices = graph.getVertices();
        for (Object vertex : allVertices) {
            String id = ((mxCell) vertex).getId();
            mxCell refCell =
                    (mxCell) ((mxGraphModel) refGraph.getModel()).getCell(id);
            GraphNode refNode = (GraphNode) refCell.getValue();
            if (refNode.getType() == NodeType.SWITCH.getId()) {
                numSwitches += 1;
                int activePorts =
                        refNode.getEndNeighbor().size()
                                + refNode.getMiddleNeighbor().size();
                // count switch zero port
                numActiveSwitchPorts += activePorts + 1;
                numOtherPorts += refNode.getNumPorts() - activePorts;
            } else if (refNode.getType() == NodeType.HFI.getId()) {
                numHFIs += 1;
            }
        }

        SimplePropertyGroup subnetGroup =
                new SimplePropertyGroup(subnetSumName);
        SimplePropertyCategory category = populateNodes(numSwitches, numHFIs);
        subnetGroup.addPropertyCategory(category);
        category = populateActivePorts(numActiveSwitchPorts, numHFIs);
        subnetGroup.addPropertyCategory(category);
        category = populateOtherPorts(numOtherPorts);
        subnetGroup.addPropertyCategory(category);

        model.addPropertyGroup(subnetGroup);

        if (numSwitches > 0) {
            model.addPropertyGroup(topGroup);
        }
        return model;
    }

    protected TopologyTier[] createTiers() {
        Map<Point, PortRecordBean> portMap = getPortMap();
        List<List<Integer>> ranks = topArch.getRanks();
        TopologyTier[] tiers = new TopologyTier[ranks.size()];
        for (int i = 0; i < tiers.length; i++) {
            tiers[i] = new TopologyTier(i);
            List<Integer> lids = ranks.get(i);
            tiers[i].setNumSwitches(lids.size());
            int numHFIs = 0;
            int totalPorts = 0;
            Quality upQuality = new Quality();
            Quality downQuality = new Quality();
            for (int lid : lids) {
                GraphNode node = (GraphNode) refGraph.getVertex(lid).getValue();
                totalPorts += node.getNumPorts();
                Set<GraphNode> neighbors = node.getMiddleNeighbor();
                for (GraphNode nbr : neighbors) {
                    Set<Integer> ports = node.getLinkPorts(nbr).keySet();

                    Quality quality = null;
                    if (node.getDepth() > nbr.getDepth()) {
                        quality = downQuality;
                    } else if (node.getDepth() < nbr.getDepth()) {
                        quality = upQuality;
                    } else {
                        // this shouldn't happen
                        log.error("Mismatched nodes depth " + node + " " + nbr);
                        continue;
                    }
                    quality.increaseTotalPorts(ports.size());
                    for (Integer portNum : ports) {
                        PortRecordBean port =
                                portMap.get(new Point(node.getLid(), portNum));
                        if (port != null) {
                            if (!Utils.isExpectedSpeed(port.getPortInfo(),
                                    LinkSpeedMask.STL_LINK_SPEED_25G)) {
                                quality.increaseSlowPorts(1);
                            }
                            if (!Utils.isExpectedWidthDowngrade(
                                    port.getPortInfo(),
                                    LinkWidthMask.STL_LINK_WIDTH_4X)) {
                                quality.increaseDegPorts(1);
                            }
                        } else {
                            // this shouldn't happen
                            log.error("Couldn't find port lid=" + node.getLid()
                                    + " portNim=" + portNum);
                        }
                    }
                }
                neighbors = node.getEndNeighbor();
                for (GraphNode nbr : neighbors) {
                    downQuality.increaseTotalPorts(node.getLinkPorts(nbr)
                            .size());
                }
                numHFIs += neighbors.size();
            }
            tiers[i].setNumHFIs(numHFIs);
            tiers[i].setUpQuality(upQuality);
            tiers[i].setDownQuality(downQuality);
            tiers[i].setNumOtherPorts(totalPorts - upQuality.getTotalPorts()
                    - downQuality.getTotalPorts());
            System.out.println(tiers[i]);
        }
        return tiers;
    }

    protected Map<Point, PortRecordBean> getPortMap() {
        Map<Point, PortRecordBean> map = new HashMap<Point, PortRecordBean>();
        try {
            List<PortRecordBean> ports = subnetApi.getPorts();
            for (PortRecordBean port : ports) {
                map.put(new Point(port.getEndPortLID(), port.getPortNum()),
                        port);
            }
        } catch (SubnetDataNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    protected SimplePropertyCategory populateNodes(int numSwitches, int numHFIs) {
        SimplePropertyCategory category =
                new SimplePropertyCategory(
                        STLConstants.K0014_ACTIVE_NODES.getValue(), null);
        category.setShowHeader(true);
        NodeTypeViz type = NodeTypeViz.SWITCH;
        PropertyItem<SimplePropertyKey> item =
                populateCountItem(type, numSwitches);
        category.addItem(item);

        type = NodeTypeViz.HFI;
        item = populateCountItem(type, numHFIs);
        category.addItem(item);
        return category;
    }

    protected SimplePropertyCategory populateActivePorts(int numSwitches,
            int numHFIs) {
        SimplePropertyCategory category =
                new SimplePropertyCategory(
                        STLConstants.K0024_ACTIVE_PORTS.getValue(), null);
        category.setShowHeader(true);
        NodeTypeViz type = NodeTypeViz.SWITCH;
        PropertyItem<SimplePropertyKey> item =
                populateCountItem(type, numSwitches);
        category.addItem(item);

        type = NodeTypeViz.HFI;
        item = populateCountItem(type, numHFIs);
        category.addItem(item);
        return category;
    }

    protected SimplePropertyCategory populateOtherPorts(int others) {
        String countString = UIConstants.INTEGER.format(others);
        SimplePropertyCategory category =
                new SimplePropertyCategory(
                        STLConstants.K2071_OTHER_PORTS.getValue(), countString);
        category.setShowHeader(true);
        return category;
    }

    protected PropertyItem<SimplePropertyKey> populateCountItem(
            NodeTypeViz type, Integer count) {
        SimplePropertyKey key = new SimplePropertyKey(type.getPluralName());
        String countString =
                count == null ? STLConstants.K0039_NOT_AVAILABLE.getValue()
                        : UIConstants.INTEGER.format(count);
        return new PropertyItem<SimplePropertyKey>(key, countString);
    }
}
