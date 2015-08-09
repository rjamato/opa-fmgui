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
 *  File Name: RefreshGraphTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/04/28 14:00:33  jijunwan
 *  Archive Log:    1) improved topology viz to use TopGraph copy for outline display. This will avoid graph and outline views share internal graph view that may cause sync issues.
 *  Archive Log:    2) added more debug info in log
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/10 20:20:32  fernande
 *  Archive Log:    Changed TopologyView to be passed two background services (graphService and outlineService) which now reside in FabricController and can be properly shutdown when an error occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/23 22:49:46  jijunwan
 *  Archive Log:    improved to update topology graph properly for notices
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/11/05 23:02:29  jijunwan
 *  Archive Log:    ignore InterruptedExceptions
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/11/05 16:29:27  jijunwan
 *  Archive Log:    synchronized topology update based on notices
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network.task;

import java.util.List;

import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.model.LayoutType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.network.GraphBuilder;
import com.intel.stl.ui.network.IModelChange;
import com.intel.stl.ui.network.LayoutChange;
import com.intel.stl.ui.network.TopGraph;
import com.intel.stl.ui.network.TopologyGraphController;
import com.intel.stl.ui.network.TopologyTreeModel;

public class RefreshGraphTask extends TopologyUpdateTask {
    private final LayoutType type;

    private final IProgressObserver observer;

    private TopologyTreeModel tmpTreeMode;

    /**
     * Description:
     * 
     * @param controller
     * @param source
     * @param selectedResources
     */
    public RefreshGraphTask(TopologyGraphController controller, Object source,
            FVResourceNode[] selectedResources, LayoutType type,
            IProgressObserver observer) {
        super(controller, source, selectedResources);
        this.type = type;
        this.observer = observer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.task.TopologyUpdateTask#createNewGraph(com.intel
     * .stl.ui.common.ICancelIndicator, com.intel.stl.ui.network.TopGraph)
     */
    @Override
    public TopGraph createNewGraph(ICancelIndicator indicator, TopGraph oldGraph) {
        try {
            ISubnetApi subnetApi = controller.getSubnetApi();
            List<NodeRecordBean> nodes = subnetApi.getNodes(false);
            List<LinkRecordBean> links = subnetApi.getLinks(false);
            TopGraph fullGraph = TopGraph.createGraph();
            GraphBuilder builder = new GraphBuilder();
            tmpTreeMode = builder.build(fullGraph, nodes, links);
            fullGraph.expandAll();
            return fullGraph;
        } catch (Exception e) {
            if (!isInterruptedException(e)) {
                e.printStackTrace();
            }
        }
        return oldGraph;
    }

    protected boolean isInterruptedException(Exception e) {
        Throwable tmp = e;
        while (tmp != null) {
            if (tmp instanceof InterruptedException) {
                return true;
            }
            tmp = tmp.getCause();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.task.TopologyUpdateTask#applyChanges(com.intel
     * .stl.ui.common.ICancelIndicator, com.intel.stl.ui.network.TopGraph)
     */
    @Override
    public boolean applyChanges(ICancelIndicator indicator, TopGraph newGraph) {
        IModelChange change = new LayoutChange(type, tmpTreeMode);
        change.execute(newGraph, indicator);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.task.TopologyUpdateTask#onSuccess(com.intel.
     * stl.ui.common.ICancelIndicator, com.intel.stl.ui.network.TopGraph)
     */
    @Override
    public void onSuccess(ICancelIndicator indicator, TopGraph graph) {
        // create a copy before we do any UI related things on it
        final TopGraph outlineGraph = TopGraph.createGraph();
        outlineGraph.setModel(graph.getModel());

        super.onSuccess(indicator, graph);

        controller.setTopTreeModel(tmpTreeMode);

        submitGraphTask(new Runnable() {
            @Override
            public void run() {
                graphView.updateGraph();
            }
        });

        submitOutlineTask(new Runnable() {
            @Override
            public void run() {
                guideView.setGraph(outlineGraph);
                graphView.updateGraph();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.task.TopologyUpdateTask#onFinally(com.intel.
     * stl.ui.common.ICancelIndicator)
     */
    @Override
    public void onFinally(ICancelIndicator indicator) {
        if (observer != null) {
            observer.onFinish();
        }
    }

}
