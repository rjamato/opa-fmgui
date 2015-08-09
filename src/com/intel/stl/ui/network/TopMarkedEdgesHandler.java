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
 *  File Name: TopMarkedCellsHandler.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/04/20 14:44:29  rjtierne
 *  Archive Log:    Catch IndexOutOfBoundsExceptions thrown by MxGraph library
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

package com.intel.stl.ui.network;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

public class TopMarkedEdgesHandler {
    protected TopGraphComponent graphComponent;

    protected boolean enabled = true;

    protected Rectangle bounds = null;

    protected transient LinkedHashMap<Object, mxCellHandler> handlers =
            new LinkedHashMap<Object, mxCellHandler>();

    protected transient mxIEventListener refreshHandler =
            new mxIEventListener() {
                @Override
                public void invoke(Object source, mxEventObject evt) {
                    if (isEnabled()) {
                        refresh();
                    }
                }
            };

    /**
     * Description:
     * 
     * @param graphComponent
     */
    public TopMarkedEdgesHandler(TopGraphComponent graphComponent) {
        super();
        this.graphComponent = graphComponent;

        // Installs the graph listeners and keeps them in sync
        addGraphListeners(graphComponent.getGraph());

        graphComponent.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("graph")) {
                    removeGraphListeners((mxGraph) evt.getOldValue());
                    addGraphListeners((mxGraph) evt.getNewValue());
                }
            }
        });
        // Installs the paint handler
        graphComponent.addListener(mxEvent.PAINT, new mxIEventListener() {
            @Override
            public void invoke(Object sender, mxEventObject evt) {
                Graphics g = (Graphics) evt.getProperty("g");
                paintHandlers(g);
            }
        });
    }

    /**
     * Installs the listeners to update the handles after mark selection change.
     */
    protected void addGraphListeners(mxGraph graph) {
        if (graph != null) {
            graph.addListener(mxEvent.MARK, refreshHandler);
            graph.getModel().addListener(mxEvent.CHANGE, refreshHandler);
            graph.getView().addListener(mxEvent.SCALE, refreshHandler);
            graph.getView().addListener(mxEvent.TRANSLATE, refreshHandler);
            graph.getView().addListener(mxEvent.SCALE_AND_TRANSLATE,
                    refreshHandler);
            graph.getView().addListener(mxEvent.DOWN, refreshHandler);
            graph.getView().addListener(mxEvent.UP, refreshHandler);
        }
    }

    /**
     * Removes all installed listeners.
     */
    protected void removeGraphListeners(mxGraph graph) {

        try {
            if (graph != null) {
                graph.removeListener(refreshHandler, mxEvent.MARK);
                graph.getModel().removeListener(refreshHandler, mxEvent.CHANGE);
                graph.getView().removeListener(refreshHandler, mxEvent.SCALE);
                graph.getView().removeListener(refreshHandler,
                        mxEvent.TRANSLATE);
                graph.getView().removeListener(refreshHandler,
                        mxEvent.SCALE_AND_TRANSLATE);
                graph.getView().removeListener(refreshHandler, mxEvent.DOWN);
                graph.getView().removeListener(refreshHandler, mxEvent.UP);
            }
        } catch (IndexOutOfBoundsException e) {
         // MxGraph library may throw this exception when a port goes down
        }

    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled
     *            the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public synchronized void refresh() {
        TopGraph graph = (TopGraph) graphComponent.getGraph();

        LinkedHashMap<Object, mxCellHandler> oldHandlers = handlers;
        handlers = new LinkedHashMap<Object, mxCellHandler>();

        Rectangle handleBounds = null;
        mxIGraphModel model = graph.getModel();
        Object parent = graph.getDefaultParent();
        int count = model.getChildCount(parent);
        for (int i = 0; i < count; i++) {
            mxCell cell = (mxCell) model.getChildAt(parent, i);
            if (cell.isEdge() && graph.isMarked(cell)) {
                mxCellState state = graph.getView().getState(cell);

                if (state != null
                        && state.getCell() != graph.getView().getCurrentRoot()) {
                    mxCellHandler handler = oldHandlers.remove(cell);

                    if (handler != null) {
                        handler.refresh(state);
                    } else {
                        handler = new MarkedEdgeHandler(graphComponent, state);
                    }

                    handlers.put(cell, handler);
                    Rectangle bounds = handler.getBounds();
                    Stroke stroke = handler.getSelectionStroke();

                    if (stroke != null) {
                        bounds = stroke.createStrokedShape(bounds).getBounds();
                    }

                    if (handleBounds == null) {
                        handleBounds = bounds;
                    } else {
                        handleBounds.add(bounds);
                    }
                }
            }
        }

        Rectangle dirty = bounds;

        if (handleBounds != null) {
            if (dirty != null) {
                dirty.add(handleBounds);
            } else {
                dirty = handleBounds;
            }
        }

        if (dirty != null) {
            graphComponent.getGraphControl().repaint(dirty);
        }

        // Stores current bounds for later use
        bounds = handleBounds;
    }

    public synchronized void paintHandlers(Graphics g) {
        mxCellHandler[] tmp = handlers.values().toArray(new mxCellHandler[0]);
        for (mxCellHandler handler : tmp) {
            handler.paint(g);
        }
    }
}
