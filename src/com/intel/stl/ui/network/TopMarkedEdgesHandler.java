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
 *  File Name: TopMarkedCellsHandler.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2.2.1  2015/08/12 15:26:50  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
