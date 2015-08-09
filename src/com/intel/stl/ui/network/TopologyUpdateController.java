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
 *  File Name: TopModelManager.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/04/28 14:00:34  jijunwan
 *  Archive Log:    1) improved topology viz to use TopGraph copy for outline display. This will avoid graph and outline views share internal graph view that may cause sync issues.
 *  Archive Log:    2) added more debug info in log
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/04/20 14:44:29  rjtierne
 *  Archive Log:    Catch IndexOutOfBoundsExceptions thrown by MxGraph library
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/23 22:49:45  jijunwan
 *  Archive Log:    improved to update topology graph properly for notices
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/12/11 18:47:05  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/11/05 23:01:47  jijunwan
 *  Archive Log:    improved to support whether we want to cancel a task while it's running
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/11/03 23:06:12  jijunwan
 *  Archive Log:    improvement on topology view - drawing graph on background
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/23 16:00:04  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/22 02:21:26  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/09 21:29:45  jijunwan
 *  Archive Log:    new Topology Viz
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/02 18:57:21  jijunwan
 *  Archive Log:    improvement on topology graph refresh - clear and then update graph
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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.network.task.ITopologyUpdateTask;
import com.intel.stl.ui.network.view.TopologyGraphView;
import com.intel.stl.ui.publisher.CallbackAdapter;
import com.intel.stl.ui.publisher.CancellableCall;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.SingleTaskManager;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.view.mxGraph;

/**
 * class in charge topology model update.
 */
public class TopologyUpdateController {
    private final static Logger log = LoggerFactory
            .getLogger(TopologyUpdateController.class);

    private TopGraph graph;

    private final TopologyGraphView view;

    private final mxUndoManager undoManager = new mxUndoManager();

    private final mxIEventListener undoHandler = new mxIEventListener() {
        @Override
        public void invoke(Object source, mxEventObject evt) {
            undoManager.undoableEditHappened((mxUndoableEdit) evt
                    .getProperty("edit"));
        }
    };

    private final SingleTaskManager taskMgr;

    private final AtomicInteger taskCounter = new AtomicInteger();

    private final AtomicBoolean isCancelled = new AtomicBoolean();

    private final AtomicInteger modelUpdatingCounter = new AtomicInteger();

    /**
     * Description:
     * 
     * @param topTreeModel
     * @param graphModel
     */
    public TopologyUpdateController(TopGraph graph, TopologyGraphView view) {
        super();
        this.graph = graph;
        this.view = view;
        taskMgr = new SingleTaskManager();
        taskMgr.setMayInterruptIfRunning(false);
    }

    /**
     * @param graph
     *            the graph to set
     */
    public void setGraph(TopGraph graph) {
        this.graph = graph;
    }

    /**
     * @return the graph
     */
    public TopGraph getGraph() {
        return graph;
    }

    public GraphNode getGraphNode(int lid) {
        mxCell cell = graph.getVertex(lid);
        if (cell != null) {
            return (GraphNode) cell.getValue();
        } else {
            return null;
        }
    }

    public mxCell getVertex(int lid) {
        return graph.getVertex(lid);
    }

    public mxCell getEdge(int fromLid, int toLid) {
        return graph.getEdge(fromLid, toLid);
    }

    public boolean undo() {
        undoManager.undo();
        return undoManager.canUndo();
    }

    public boolean redo() {
        undoManager.redo();
        return undoManager.canRedo();
    }

    public void cancel() {
        isCancelled.set(true);
    }

    public void update(final ITopologyUpdateTask task) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                if (modelUpdatingCounter.getAndIncrement() == 0) {
                    view.showLayoutUpdating(true);
                }
            }
        });

        final int taskId = taskCounter.incrementAndGet();
        task.setId(taskId);
        log.info("Current topology update task " + taskId + " " + task);
        final ICancelIndicator indicator = new ICancelIndicator() {
            @Override
            public boolean isCancelled() {
                // if not latest task or the controller is cancelled
                return taskId != taskCounter.get() || isCancelled.get();
            }
        };

        CancellableCall<TopGraph> caller = new CancellableCall<TopGraph>() {

            @Override
            public TopGraph call(ICancelIndicator cancelIndicator)
                    throws Exception {
                if (indicator.isCancelled()) {
                    log.info("topology update task " + taskId + " cancelled");
                    return null;
                }

                log.info("topology update task " + taskId + " started");
                task.preBackgroundTask(cancelIndicator, graph);
                TopGraph newGraph = null;
                try {
                    newGraph = task.createNewGraph(indicator, graph);
                    task.applyChanges(indicator, newGraph);

                    TopGraph oldGraph = view.getGraph();
                    uninstallUndoManager(oldGraph);
                    installUndoManager(newGraph);

                    if (indicator.isCancelled()) {
                        log.info("topology update task " + taskId
                                + " cancelled");
                        return null;
                    }

                    return newGraph;
                } finally {
                    task.postBackgroundTask(cancelIndicator, newGraph);
                }
            }
        };
        caller.setCancelIndicator(indicator);

        ICallback<TopGraph> realCallback = new CallbackAdapter<TopGraph>() {

            /*
             * (non-Javadoc)
             * 
             * @see com.intel.stl.ui.publisher.CallbackAdapter#onDone(java
             * .lang.Object )
             */
            @Override
            public void onDone(final TopGraph result) {
                if (result == null || indicator.isCancelled()) {
                    return;
                }

                task.onSuccess(indicator, result);
            }

            /*
             * (non-Javadoc)
             * 
             * @see com.intel.stl.ui.publisher.CallbackAdapter#onError(java
             * .lang.Throwable[])
             */
            @Override
            public void onError(Throwable... errors) {
                task.onError(indicator, errors);
            }

            /*
             * (non-Javadoc)
             * 
             * @see com.intel.stl.ui.publisher.CallbackAdapter#onFinally()
             */
            @Override
            public void onFinally() {
                try {
                    task.onFinally(indicator);
                } finally {
                    if (modelUpdatingCounter.decrementAndGet() == 0) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                view.showLayoutUpdating(false);
                            }
                        });
                    }
                }
            }

        };

        taskMgr.submit(caller, realCallback);
    }

    protected void uninstallUndoManager(mxGraph graph) {
        try {
            graph.getModel().removeListener(undoHandler);
            graph.getView().removeListener(undoHandler);
        } catch (IndexOutOfBoundsException e) {
            // MxGraph library may throw this exception when a port goes down
        }
    }

    protected void installUndoManager(mxGraph graph) {
        undoManager.clear();
        graph.getModel().addListener(mxEvent.UNDO, undoHandler);
        graph.getView().addListener(mxEvent.UNDO, undoHandler);

        // // Keeps the selection in sync with the command history
        // undoHandler = new mxIEventListener() {
        // public void invoke(Object source, mxEventObject evt) {
        // List<mxUndoableChange> changes = ((mxUndoableEdit) evt
        // .getProperty("edit")).getChanges();
        // graph.setSelectionCells(graph
        // .getSelectionCellsForChanges(changes));
        // }
        // };
        //
        // undoManager.addListener(mxEvent.UNDO, undoHandler);
        // undoManager.addListener(mxEvent.REDO, undoHandler);
    }

}
