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
 *  File Name: ShowGroupTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7.2.1  2015/08/12 15:26:56  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
