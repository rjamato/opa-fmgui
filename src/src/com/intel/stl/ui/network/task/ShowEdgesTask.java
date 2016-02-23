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
 *  File Name: ShowEdgesTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/11/24 16:49:00  rjtierne
 *  Archive Log:    PR 131720 - Code cleanup for PRQCode cleanup for PRQ
 *  Archive Log:    - In method getVFName(), changed error message for IllegalArgumentException to use the
 *  Archive Log:    new STLConstant K0808.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/11/02 23:56:54  jijunwan
 *  Archive Log:    PR 131396 - Incorrect Connectivity Table for a VF port
 *  Archive Log:    - adapted to the new connectivity table controller to support VF port
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/08/17 18:54:05  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/28 14:00:33  jijunwan
 *  Archive Log:    1) improved topology viz to use TopGraph copy for outline display. This will avoid graph and outline views share internal graph view that may cause sync issues.
 *  Archive Log:    2) added more debug info in log
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/10 20:20:32  fernande
 *  Archive Log:    Changed TopologyView to be passed two background services (graphService and outlineService) which now reside in FabricController and can be properly shutdown when an error occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/11/03 23:06:13  jijunwan
 *  Archive Log:    improvement on topology view - drawing graph on background
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

import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.model.GraphEdge;
import com.intel.stl.ui.model.LayoutType;
import com.intel.stl.ui.monitor.TreeNodeType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.network.IModelChange;
import com.intel.stl.ui.network.LayoutChange;
import com.intel.stl.ui.network.TopGraph;
import com.intel.stl.ui.network.TopologyGraphController;
import com.intel.stl.ui.network.TopologyTreeModel;
import com.mxgraph.model.mxCell;

public class ShowEdgesTask extends TopologyUpdateTask {
    private final List<GraphEdge> edges;

    private final String vfName;

    private TopologyTreeModel tmpTreeMode;

    private final LayoutType defaultLayout = LayoutType.TREE_SLASH;

    /**
     * Description:
     * 
     * @param controller
     * @param source
     * @param selectedResources
     * @param edges
     */
    public ShowEdgesTask(TopologyGraphController controller, Object source,
            FVResourceNode[] selectedResources, List<GraphEdge> edges) {
        super(controller, source, selectedResources);
        this.edges = edges;
        this.vfName = getVFName(selectedResources);
        setIncludeNeighbors(false);
    }

    protected String getVFName(FVResourceNode[] selectedResources) {
        String res = null;
        FVResourceNode refGroup = null;
        String vfName = null;
        for (int i = 0; i < selectedResources.length; i++) {
            vfName = null;
            FVResourceNode parent = selectedResources[i].getParent();
            FVResourceNode group = parent.getParent();
            if (refGroup != null && group != refGroup) {
                throw new IllegalArgumentException(
                        STLConstants.K0808_DIFFERENT_PARENTS_ERROR.getValue());
            }
            if (group.getType() == TreeNodeType.VIRTUAL_FABRIC) {
                vfName = group.getTitle();
            }
            if (i == 0) {
                res = vfName;
                refGroup = group;
            }
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.task.TopologyUpdateTask#preBackgroundTask(com
     * .intel.stl.ui.common.ICancelIndicator)
     */
    @Override
    public void preBackgroundTask(ICancelIndicator indicator, TopGraph oldGraph) {
        resourceController.showLinks(edges, vfName);
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
        Set<Integer> involvedNodes = new HashSet<Integer>();
        for (GraphEdge edge : edges) {
            involvedNodes.add(edge.getFromLid());
            involvedNodes.add(edge.getToLid());
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
        tmpTreeMode = controller.getFullTopTreeModel().filterBy(newGraph);
        IModelChange change = new LayoutChange(defaultLayout, tmpTreeMode);
        change.execute(newGraph, indicator);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.TopologyUpdateTask#onDone(com.intel.
     * stl.ui.common.ICancelIndicator, com.intel.stl.ui.network.TopGraph)
     */
    @Override
    public void onSuccess(final ICancelIndicator indicator, TopGraph newGraph) {
        super.onSuccess(indicator, newGraph);

        controller.setTopTreeModel(tmpTreeMode);

        FVResourceNode[] selectedNodes = selectedResources;
        if (!indicator.isCancelled() && source != controller) {
            selectedNodes = controller.selectTreePorts(edges, indicator);
        }
        final mxCell[] cells = getEdgeCells(edges, newGraph);

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

        guideView.setSelectedResources(selectedNodes);
        setLayout(defaultLayout);
    }

    protected mxCell[] getEdgeCells(List<GraphEdge> edges, TopGraph graph) {
        mxCell[] cells = new mxCell[edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            GraphEdge edge = edges.get(i);
            mxCell cell = graph.getEdge(edge.getFromLid(), edge.getToLid());
            cells[i] = cell;
        }
        return cells;
    }

}
