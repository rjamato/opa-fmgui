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
 *  File Name: AbstractTopologyUpdateTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
 *  Archive Log:    Revision 1.1  2014/10/09 21:29:45  jijunwan
 *  Archive Log:    new Topology Viz
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.intel.stl.ui.common.IBackgroundService;
import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.model.LayoutType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.network.IModelChange;
import com.intel.stl.ui.network.ResourceController;
import com.intel.stl.ui.network.TopGraph;
import com.intel.stl.ui.network.TopologyGraphController;
import com.intel.stl.ui.network.view.TopologyGraphView;
import com.intel.stl.ui.network.view.TopologyGuideView;

public class TopologyUpdateTask implements ITopologyUpdateTask {
    private int id;

    protected TopologyGraphController controller;

    protected final TopologyGraphView graphView;

    protected final TopologyGuideView guideView;

    protected final ResourceController resourceController;

    protected boolean includeNeighbors = true;

    private final List<IModelChange> modelChanges =
            new ArrayList<IModelChange>();

    protected final Object source;

    protected final FVResourceNode[] selectedResources;

    private final IBackgroundService graphService;

    private final IBackgroundService outlineService;

    /**
     * Description:
     * 
     * @param controller
     * @param source
     * @param selectedResources
     */
    public TopologyUpdateTask(TopologyGraphController controller,
            Object source, FVResourceNode[] selectedResources) {
        super();
        this.controller = controller;
        this.graphView = controller.getGraphView();
        this.guideView = controller.getGuideView();
        this.resourceController = controller.getResourceController();
        this.graphService = graphView.getUpdateService();
        this.outlineService = guideView.getUpdateService();

        this.source = source;
        this.selectedResources = selectedResources;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the includeNeighbors
     */
    public boolean isIncludeNeighbors() {
        return includeNeighbors;
    }

    /**
     * @param includeNeighbors
     *            the includeNeighbors to set
     */
    public void setIncludeNeighbors(boolean includeNeighbors) {
        this.includeNeighbors = includeNeighbors;
    }

    public void addModelChange(IModelChange change) {
        modelChanges.add(change);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.ITopologyUpdateTask#preBackgroundTask(com.intel
     * .stl.ui.common.ICancelIndicator)
     */
    @Override
    public void preBackgroundTask(ICancelIndicator indicator, TopGraph oldGraph) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.ITopologyUpdateTask#createNewGraph(com.intel
     * .stl.ui.common.ICancelIndicator, com.intel.stl.ui.network.TopGraph)
     */
    @Override
    public TopGraph createNewGraph(ICancelIndicator indicator, TopGraph oldGraph) {
        Collection<Integer> nodes = getInvolvedNodes(indicator, oldGraph);
        return oldGraph.filterBy(getId(), nodes, isIncludeNeighbors(),
                indicator);
    }

    protected Collection<Integer> getInvolvedNodes(ICancelIndicator indicator,
            TopGraph oldGraph) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.ITopologyUpdateTask#applyChanges(com.intel.stl
     * .ui.common.ICancelIndicator, com.intel.stl.ui.network.TopGraph)
     */
    @Override
    public boolean applyChanges(ICancelIndicator indicator, TopGraph newGraph) {
        boolean modelChanged = false;
        if (!modelChanges.isEmpty()) {
            for (IModelChange change : modelChanges) {
                if (change != null) {
                    boolean ret = change.execute(newGraph, indicator);
                    modelChanged = modelChanged || ret;
                }
            }
        }
        return modelChanged;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.ITopologyUpdateTask#postBackgroundTask(com.intel
     * .stl.ui.common.ICancelIndicator)
     */
    @Override
    public void postBackgroundTask(ICancelIndicator indicator, TopGraph oldGraph) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.ITopologyUpdateTask#onDone(com.intel.stl.ui.
     * common.ICancelIndicator, com.mxgraph.model.mxIGraphModel)
     */
    @Override
    public void onSuccess(ICancelIndicator indicator, final TopGraph graph) {
        if (graphView.getGraph() != graph) {
            submitGraphTask(new Runnable() {
                @Override
                public void run() {
                    graphView.setGraph(graph);
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.ITopologyUpdateTask#onError(com.intel.stl.ui
     * .common.ICancelIndicator, java.lang.Throwable[])
     */
    @Override
    public void onError(ICancelIndicator indicator, Throwable... errors) {
        for (Throwable error : errors) {
            error.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.ITopologyUpdateTask#onFinally(com.intel.stl.
     * ui.common.ICancelIndicator)
     */
    @Override
    public void onFinally(ICancelIndicator indicator) {
    }

    protected void submitGraphTask(Runnable runnable) {
        graphService.submit(runnable);
    }

    protected void submitOutlineTask(Runnable runnable) {
        outlineService.submit(runnable);
    }

    protected void setLayout(LayoutType layout) {
        // set controller first
        controller.setCurrentLayout(layout);
        // then set the graph view, so it will not trigger the controller
        // do layout again
        graphView.setLayoutType(layout);
    }
}
