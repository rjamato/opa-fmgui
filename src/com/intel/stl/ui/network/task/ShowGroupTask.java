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
 *  File Name: ShowGroupTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/04/28 14:00:33  jijunwan
 *  Archive Log:    1) improved topology viz to use TopGraph copy for outline display. This will avoid graph and outline views share internal graph view that may cause sync issues.
 *  Archive Log:    2) added more debug info in log
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/04/20 14:45:40  rjtierne
 *  Archive Log:    Provide null pointer protection for involvedNodes and node in getInvolvedNodes()
 *  Archive Log:    intermittently causing NullPointerException when a port goes down
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/04/10 20:20:32  fernande
 *  Archive Log:    Changed TopologyView to be passed two background services (graphService and outlineService) which now reside in FabricController and can be properly shutdown when an error occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/18 19:32:03  jijunwan
 *  Archive Log:    PR 127102 - Overall summary of Switches under Topology page does not report correct number of switch ports
 *  Archive Log:     - improved the calculation to count both internal and external ports
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/03 23:06:13  jijunwan
 *  Archive Log:    improvement on topology view - drawing graph on background
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/23 16:00:09  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/22 02:21:25  jijunwan
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

package com.intel.stl.ui.network.task;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;

import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.model.LayoutType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.network.IModelChange;
import com.intel.stl.ui.network.LayoutChange;
import com.intel.stl.ui.network.TopGraph;
import com.intel.stl.ui.network.TopologyGraphController;
import com.intel.stl.ui.network.TopologyTreeModel;
import com.mxgraph.model.mxCell;

public class ShowGroupTask extends TopologyUpdateTask {
    private TopGraph oldGraph;

    private final List<GraphNode> nodes;

    private TopologyTreeModel tmpTreeMode;

    private final TopologyTreeModel fullTopTreeModel;

    private final LayoutType defaultLayout = LayoutType.TREE_SLASH;

    /**
     * Description:
     * 
     * @param controller
     * @param source
     * @param selectedResources
     * @param nodes
     */
    public ShowGroupTask(TopologyGraphController controller, Object source,
            FVResourceNode[] selectedResources, List<GraphNode> nodes) {
        super(controller, source, selectedResources);
        this.nodes = nodes;
        fullTopTreeModel = controller.getFullTopTreeModel();
        setIncludeNeighbors(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.TopologyUpdateTask#getNodes(com.intel
     * .stl.ui.common.ICancelIndicator)
     */
    @Override
    protected Collection<Integer> getInvolvedNodes(ICancelIndicator indicator,
            TopGraph oldGraph) {
        this.oldGraph = oldGraph;
        if (nodes.size() == fullTopTreeModel.getNumTotalNodes()) {
            return null;
        }

        Set<Integer> involvedNodes = new HashSet<Integer>();
        for (GraphNode node : nodes) {
            // Null pointer check for intermittent bug when port goes down
            if ((involvedNodes != null) && (node != null)) {
                involvedNodes.add(node.getLid());
            }
        }
        return involvedNodes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.TopologyUpdateTask#applyChanges(com.
     * intel.stl.ui.common.ICancelIndicator, com.intel.stl.ui.network.TopGraph)
     */
    @Override
    public boolean applyChanges(ICancelIndicator indicator, TopGraph newGraph) {
        tmpTreeMode = fullTopTreeModel.filterBy(newGraph);
        IModelChange change = new LayoutChange(defaultLayout, tmpTreeMode);
        change.execute(newGraph, indicator);

        String name = null;
        Icon icon = null;
        if (selectedResources.length == 1) {
            name = selectedResources[0].getName();
            icon = selectedResources[0].getType().getIcon();
        } else {
            name = STLConstants.K2078_DEVIE_SET.getValue();
            icon = UIImages.DEVICE_SET.getImageIcon();
        }
        resourceController.showGroup(name, icon, tmpTreeMode, newGraph,
                oldGraph);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.TopologyUpdateTask#onDone(com.intel.
     * stl.ui.common.ICancelIndicator, com.mxgraph.view.mxGraph)
     */
    @Override
    public void onSuccess(final ICancelIndicator indicator, TopGraph newGraph) {
        super.onSuccess(indicator, newGraph);

        controller.setTopTreeModel(tmpTreeMode);

        final mxCell[] cells = new mxCell[nodes.size()];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = newGraph.getVertex(nodes.get(i).getLid());
        }

        submitGraphTask(new Runnable() {
            @Override
            public void run() {
                graphView.clearEdges();

                if (!indicator.isCancelled()) {
                    graphView.selectNodes(cells);
                }

                graphView.updateGraph();
            }
        });

        submitOutlineTask(new Runnable() {
            @Override
            public void run() {
                guideView.clearEdges();

                if (!indicator.isCancelled()) {
                    guideView.selectNodes(cells);
                }

                guideView.updateGraph();
            }
        });

        guideView.setSelectedResources(selectedResources);
        setLayout(defaultLayout);
    }

}
