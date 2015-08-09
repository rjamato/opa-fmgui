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
 *  File Name: TopGraphComponent.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.17  2015/04/28 14:00:34  jijunwan
 *  Archive Log:    1) improved topology viz to use TopGraph copy for outline display. This will avoid graph and outline views share internal graph view that may cause sync issues.
 *  Archive Log:    2) added more debug info in log
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/04/20 14:44:29  rjtierne
 *  Archive Log:    Catch IndexOutOfBoundsExceptions thrown by MxGraph library
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/04/10 20:20:00  fernande
 *  Archive Log:    Changed TopologyView to be passed two background services (graphService and outlineService) which now reside in FabricController and can be properly shutdown when an error occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/12/11 18:47:06  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/11/03 23:06:12  jijunwan
 *  Archive Log:    improvement on topology view - drawing graph on background
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/10/23 16:33:23  jijunwan
 *  Archive Log:    minor change on timers - intend to improve timer behavior so the action will be cancelled event if it's already in EDT queue
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/10/22 02:21:26  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/10/10 16:43:55  jijunwan
 *  Archive Log:    removed debug print
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/10/09 21:29:45  jijunwan
 *  Archive Log:    new Topology Viz
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/02 21:37:55  jijunwan
 *  Archive Log:    fixed issues found by FindBugs
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/08/05 19:36:04  jijunwan
 *  Archive Log:    reformat, added an empty border to topology panel
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/08/05 13:46:23  jijunwan
 *  Archive Log:    new implementation on topology control that uses double models to avoid synchronization issues on model and view
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/07 18:28:11  jijunwan
 *  Archive Log:    added quickPaint for panning where we directly draw a pre-stored image rather than render from scratch
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/03 22:25:36  jijunwan
 *  Archive Log:    minor change. We will do more on it to improve pan performance
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/23 04:56:56  jijunwan
 *  Archive Log:    new topology code to support interactions with a topology graph
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/17 16:55:41  jijunwan
 *  Archive Log:    pan and zoom at current mouse point; shift+mouse drag to move nodes on screen; right control to clear selections; undoable node collapse and expand
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/28 15:54:40  jijunwan
 *  Archive Log:    minor adjustments on topology: 1) scale vertices when they begin to overlap each other, 2) pick vertex first when we find cell under a mouse point
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.ui.common.IBackgroundService;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.model.GraphNode;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.swing.handler.mxPanningHandler;
import com.mxgraph.swing.handler.mxSelectionCellsHandler;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

public class TopGraphComponent extends mxGraphComponent {
    private static final long serialVersionUID = -3168649345200765940L;

    private static final Logger log = LoggerFactory
            .getLogger(TopGraphComponent.class);

    private boolean graphBuffered = true;

    private mxGraphView view;

    private mxPoint viewTranslate;

    private mxIEventListener selectionListener;

    private int margin = 5;

    private Timer repaintTimer;

    private boolean userTranslated;

    private final AtomicInteger imageId = new AtomicInteger();

    private final AtomicInteger cellFindingId = new AtomicInteger();

    private final AtomicInteger transId = new AtomicInteger();

    private final IBackgroundService updateService;

    /**
     * Description:
     * 
     * @param graph
     */
    public TopGraphComponent(IBackgroundService updateService, TopGraph graph) {
        super(graph);
        this.updateService = updateService;

        // showDirtyRectangle = true;
        // override selection colors and strokes
        mxSwingConstants.VERTEX_SELECTION_COLOR = UIConstants.VERTEX_SEL_COLOR;
        mxSwingConstants.VERTEX_SELECTION_STROKE =
                UIConstants.VERTEX_SEL_STROKE;
        mxSwingConstants.EDGE_SELECTION_COLOR = UIConstants.EDGE_SEL_COLOR;
        mxSwingConstants.EDGE_SELECTION_STROKE = UIConstants.EDGE_SEL_STROKE;

        setBackground(UIConstants.INTEL_WHITE);
        setViewportBorder(BorderFactory.createEmptyBorder(margin, margin,
                margin, margin));

        ((SelectionCellsHandler) getSelectionCellsHandler())
                .setUpdateService(updateService);
    }

    /**
     * @return the graphBuffered
     */
    public boolean isGraphBuffered() {
        return graphBuffered;
    }

    /**
     * @param graphBuffered
     *            the graphBuffered to set
     */
    public void setGraphBuffered(boolean graphBuffered) {
        this.graphBuffered = graphBuffered;
    }

    /**
     * @param margin
     *            the margin to set
     */
    public void setMargin(int margin) {
        this.margin = margin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mxgraph.swing.mxGraphComponent#createGraphControl()
     */
    @Override
    protected mxGraphControl createGraphControl() {
        return new GraphControl();
    }

    /**
     * @param selectionListener
     *            the selectionListener to set
     */
    public void setSelectionListener(mxIEventListener listener) {
        if (selectionListener != null && graph != null) {
            graph.getSelectionModel().removeListener(selectionListener);
        }

        selectionListener = listener;
        if (graph != null && listener != null) {
            graph.getSelectionModel().addListener(mxEvent.CHANGE, listener);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mxgraph.swing.mxGraphComponent#createHandlers()
     */
    @Override
    protected void createHandlers() {
        super.createHandlers();

        // // zoom to a user specified area
        // new mxRubberband(this) {
        // /*
        // * (non-Javadoc)
        // *
        // * @see
        // * com.mxgraph.swing.handler.mxRubberband#isRubberbandTrigger(java
        // * .awt.event.MouseEvent)
        // */
        // @Override
        // public boolean isRubberbandTrigger(MouseEvent e) {
        // return e.isControlDown();
        // }
        //
        // /*
        // * (non-Javadoc)
        // *
        // * @see
        // * com.mxgraph.swing.handler.mxRubberband#mouseReleased(java.awt
        // * .event.MouseEvent)
        // */
        // @Override
        // public void mouseReleased(MouseEvent e) {
        // Rectangle rect = bounds;
        // super.mouseReleased(e);
        // if (e.isControlDown() && e.isShiftDown()) {
        // zoomToRectangle(rect);
        // }
        // }
        // };

        new TopMarkedEdgesHandler(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mxgraph.swing.mxGraphComponent#createPanningHandler()
     */
    @Override
    protected mxPanningHandler createPanningHandler() {
        return new PanningHandler(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mxgraph.swing.mxGraphComponent#createGraphHandler()
     */
    @Override
    protected mxGraphHandler createGraphHandler() {
        return new GraphHandler(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mxgraph.swing.mxGraphComponent#createSelectionCellsHandler()
     */
    @Override
    protected mxSelectionCellsHandler createSelectionCellsHandler() {
        return new SelectionCellsHandler(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mxgraph.swing.mxGraphComponent#installDoubleClickHandler()
     */
    @Override
    protected void installDoubleClickHandler() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.mxgraph.swing.mxGraphComponent#isPanningEvent(java.awt.event.MouseEvent
     * )
     */
    @Override
    public boolean isPanningEvent(MouseEvent event) {
        return SwingUtilities.isLeftMouseButton(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.mxgraph.swing.mxGraphComponent#createHandler(com.mxgraph.view.mxCellState
     * )
     */
    @Override
    public mxCellHandler createHandler(mxCellState state) {
        if (((TopGraph) graph).isMarked((mxCell) state.getCell())) {
            return new MarkedEdgeHandler(this, state);
        } else if (graph.getModel().isVertex(state.getCell())) {
            return new VertexHandler(this, state);
        }
        return super.createHandler(state);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mxgraph.swing.mxGraphComponent#selectRegion(java.awt.Rectangle,
     * java.awt.event.MouseEvent)
     */
    @Override
    public Object[] selectRegion(Rectangle rect, MouseEvent e) {
        Object[] cells = getCells(rect);
        if (cells.length > 0) {
            // always select all cells. no any toggle selection.
            graph.setSelectionCells(cells);
        } else if (!graph.isSelectionEmpty() && !e.isConsumed()) {
            graph.clearSelection();
        }

        return cells;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mxgraph.swing.mxGraphComponent#getCellAt(int, int, boolean,
     * java.lang.Object)
     */
    @Override
    public Object getCellAt(int x, int y, boolean hitSwimlaneContent,
            Object parent) {
        Object res = getCellAt(x, y, hitSwimlaneContent, parent, true);
        if (res == null) {
            res = getCellAt(x, y, hitSwimlaneContent, parent, false);
        }
        return res;
    }

    public Object getCellAt(final int x, final int y,
            final boolean hitSwimlaneContent, final Object parent,
            final boolean vertex) {
        final int id = cellFindingId.incrementAndGet();
        Future<Object> task = updateService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return findCellAt(id, x, y, hitSwimlaneContent, parent, vertex);
            }
        });
        Object res = null;
        try {
            res = task.get();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return res;
    }

    protected Object findCellAt(int id, int x, int y,
            boolean hitSwimlaneContent, Object parent, boolean vertex) {
        if (id != cellFindingId.get()) {
            return null;
        }

        if (parent == null) {
            parent = graph.getDefaultParent();
        }

        if (parent != null) {
            Point previousTranslate = canvas.getTranslate();
            double previousScale = canvas.getScale();

            try {
                canvas.setScale(view.getScale());
                canvas.setTranslate(0, 0);

                mxIGraphModel model = graph.getModel();

                Rectangle hit = new Rectangle(x, y, 1, 1);
                int childCount = model.getChildCount(parent);

                for (int i = childCount - 1; i >= 0; i--) {
                    if (id != cellFindingId.get()) {
                        return null;
                    }

                    Object cell = model.getChildAt(parent, i);
                    Object result =
                            findCellAt(id, x, y, hitSwimlaneContent, cell,
                                    vertex);

                    if (result != null) {
                        return result;
                    } else if (graph.isCellVisible(cell)
                            && (vertex == model.isVertex(cell))) {
                        mxCellState state = view.getState(cell);

                        if (state != null
                                && canvas.intersects(this, hit, state)
                                && (!graph.isSwimlane(cell)
                                        || hitSwimlaneContent || (transparentSwimlaneContent && !canvas
                                        .hitSwimlaneContent(this, state, x, y)))) {
                            return cell;
                        }
                    }
                }
            } finally {
                canvas.setScale(previousScale);
                canvas.setTranslate(previousTranslate.x, previousTranslate.y);
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.mxgraph.swing.mxGraphComponent#setGraph(com.mxgraph.view.mxGraph)
     */
    @Override
    public void setGraph(mxGraph value) {
        if (value == null) {
            return;
        }

        log.info("SetGraph " + value + " " + Thread.currentThread());
        if (value instanceof TopGraph) {
            if (selectionListener != null && graph != null) {
                graph.getSelectionModel().removeListener(selectionListener);
            }

            try {
                super.setGraph(value);
            } catch (Exception e) {
                e.printStackTrace();
            }

            view = value.getView();
            Dimension viewSize = getViewport().getSize();
            if (viewSize.width > 0 && viewSize.height > 0) {
                Rectangle rec = view.getGraphBounds().getRectangle();
                zoomToRectangle(rec);
            }
            if (selectionListener != null) {
                value.getSelectionModel().addListener(mxEvent.CHANGE,
                        selectionListener);
            }
        } else {
            throw new IllegalArgumentException("graph "
                    + value.getClass().getName() + " is not "
                    + TopGraph.class.getName());
        }
    }

    // public void resetTranslate() {
    // mxPoint oldTranslate = view.getTranslate();
    // if (oldTranslate == null || oldTranslate.getX() != 0
    // || oldTranslate.getY() != 0) {
    // view.setTranslate(new mxPoint(0, 0));
    // }
    // }

    public void updateGraph() {
        Dimension viewSize = getViewport().getSize();
        ((GraphControl) graphControl).updateBuffer(viewSize.width,
                viewSize.height);
    }

    /**
     * @return the userTranslated
     */
    public boolean isUserTranslated() {
        return userTranslated;
    }

    /**
     * @param userTranslated
     *            the userTranslated to set
     */
    public void setUserTranslated(boolean userTranslated) {
        this.userTranslated = userTranslated;
    }

    /**
     * Description:
     * 
     * @param point
     */
    public void zoomIn(Point point) {
        zoom(zoomFactor, point);
    }

    /**
     * Description:
     * 
     * @param point
     */
    public void zoomOut(Point point) {
        zoom(1 / zoomFactor, point);
    }

    public void zoom(final double factor, final Point point) {
        updateService.submit(new Runnable() {
            @Override
            public void run() {
                double newScale = view.getScale() * factor;
                if (Math.abs(newScale - view.getScale()) > 1e-4
                        && newScale > 1e-3) {
                    mxPoint translate =
                            point == null ? new mxPoint() : getTranslate(point,
                                    view.getScale(), newScale);
                    mxPoint oldTranslate = view.getTranslate();
                    view.scaleAndTranslate(newScale, translate.getX()
                            + oldTranslate.getX(), translate.getY()
                            + oldTranslate.getY());
                    restartTimer();
                    // System.out.println("ScaleAndTranslate " + view.getScale()
                    // + " "
                    // + view.getTranslate());
                }
            }
        });
    }

    protected mxPoint getTranslate(Point point, double oldScale, double newScale) {
        int x = point.x;
        int y = point.y;
        double f = (newScale - oldScale) / (oldScale * newScale);
        return new mxPoint(-x * f, -y * f);
    }

    public void zoomToRectangle(Rectangle rect) {
        if (rect != null) {
            double newScale = 1;
            double gw = rect.width;
            double gh = rect.height;
            if (gw > 0 && gh > 0) {
                // Dimension viewSize = graphControl.getSize();
                Dimension viewSize = getViewport().getSize();
                double w = viewSize.getWidth() - margin * 2;
                double h = viewSize.getHeight() - margin * 2;

                newScale = Math.min(w / gw, h / gh);
            }

            double oldScale = view.getScale();
            mxPoint trans = view.getTranslate();
            // zoom to fit selected area
            view.scaleAndTranslate(oldScale * newScale, -rect.x / oldScale
                    + trans.getX(), -rect.y / oldScale + trans.getY());
            // System.out.println("ScaleAndTranslate " + view.getScale()
            // + " "
            // + view.getTranslate());
        }
    }

    protected void restartTimer() {
        if (repaintTimer == null) {
            repaintTimer =
                    new Timer(UIConstants.UPDATE_TIME / 2,
                            new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (repaintTimer != null) {
                                        updateGraph();
                                    }
                                }
                            });
            repaintTimer.setRepeats(false);
        }
        repaintTimer.restart();
    }

    protected void clearTimer() {
        if (repaintTimer != null) {
            if (repaintTimer.isRunning()) {
                repaintTimer.stop();
            }
            repaintTimer = null;
        }
    }

    class PanningHandler extends mxPanningHandler {

        /**
         * Description:
         * 
         * @param graphComponent
         */
        public PanningHandler(mxGraphComponent graphComponent) {
            super(graphComponent);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.mxgraph.swing.handler.mxPanningHandler#mousePressed(java.awt.
         * event.MouseEvent)
         */
        @Override
        public void mousePressed(final MouseEvent e) {
            if (isEnabled() && !e.isConsumed()
                    && graphComponent.isPanningEvent(e) && !e.isPopupTrigger()) {
                graphControl.setCursor(mxGraphHandler.FOLD_CURSOR);
                updateService.submit(new Runnable() {
                    @Override
                    public void run() {
                        start = e.getPoint();
                        viewTranslate = view.getTranslate();
                    }
                });
            }
        }

        @Override
        public void mouseDragged(final MouseEvent e) {
            if (!e.isConsumed() && start != null) {
                final int id = transId.incrementAndGet();
                updateService.submit(new Runnable() {
                    @Override
                    public void run() {
                        if (id == transId.get()) {
                            int dx = e.getX() - start.x;
                            int dy = e.getY() - start.y;
                            double scale = view.getScale();
                            view.setTranslate(new mxPoint(viewTranslate.getX()
                                    + dx / scale, viewTranslate.getY() + dy
                                    / scale));
                            restartTimer();
                        }
                        e.consume();
                    }
                });
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.mxgraph.swing.handler.mxPanningHandler#mouseReleased(java.awt
         * .event.MouseEvent)
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            graphControl.setCursor(mxGraphHandler.DEFAULT_CURSOR);
            userTranslated = true;
        }

    }

    class GraphHandler extends mxGraphHandler {
        private boolean hasHighlight;

        /**
         * Description:
         * 
         * @param graphComponent
         */
        public GraphHandler(mxGraphComponent graphComponent) {
            super(graphComponent);
            cloneEnabled = false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.mxgraph.swing.handler.mxGraphHandler#mousePressed(java.awt.event
         * .MouseEvent)
         */
        @Override
        public void mousePressed(MouseEvent e) {
            if (isDragTrigger(e)) {
                super.mousePressed(e);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.mxgraph.swing.handler.mxGraphHandler#mouseReleased(java.awt.event
         * .MouseEvent)
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            if (first != null) {
                super.mouseReleased(e);
            } else if (!e.isConsumed()) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // graph.clearSelection();
                } else if (SwingUtilities.isLeftMouseButton(e)
                        && e.getClickCount() > 1) {
                    Object cell = getCellAt(e.getX(), e.getY(), false);
                    if (cell != null) {
                        graphComponent.selectCellForEvent(cell, e);
                    }
                }
            }
        }

        protected boolean isDragTrigger(MouseEvent e) {
            return e.isShiftDown() && SwingUtilities.isLeftMouseButton(e);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.mxgraph.swing.handler.mxGraphHandler#getCursor(java.awt.event
         * .MouseEvent)
         */
        @Override
        protected synchronized Cursor getCursor(MouseEvent e) {
            mxCell cell =
                    (mxCell) graphComponent
                            .getCellAt(e.getX(), e.getY(), false);

            updateHighlight(cellFindingId.get(), cell);
            if (cell != null && cell.isVertex()) {
                GraphNode node = (GraphNode) cell.getValue();
                if (node.hasEndNodes()) {
                    return FOLD_CURSOR;
                }
            }

            if (isMoveEnabled()) {
                if (cell != null) {
                    if (graphComponent.isFoldingEnabled()
                            && graphComponent.hitFoldingIcon(cell, e.getX(),
                                    e.getY())) {
                        return FOLD_CURSOR;
                    } else if (graphComponent.getGraph().isCellMovable(cell)
                            && isDragTrigger(e)) {
                        return MOVE_CURSOR;
                    }
                }
            }

            return null;
        }

        protected void updateHighlight(final int id, final mxCell cell) {
            updateService.submit(new Runnable() {
                @Override
                public void run() {
                    if (id != cellFindingId.get()) {
                        return;
                    }

                    if (cell != null && cell.isVertex()) {
                        if (hasHighlight) {
                            ((TopGraph) graph).clearHighlightedEdges();
                        }
                        ((TopGraph) graph).highlightConnections(cell, true);
                        hasHighlight = true;
                        updateGraph();
                    } else if (hasHighlight) {
                        ((TopGraph) graph).clearHighlightedEdges();
                        hasHighlight = false;
                        updateGraph();
                    }
                }
            });
        }

    }

    class GraphControl extends mxGraphControl {
        private static final long serialVersionUID = 2776035858098303467L;

        private final AtomicReference<GraphBuffer> graphBufferRef =
                new AtomicReference<GraphBuffer>(null);

        public void updateBuffer(final int maxW, final int maxH) {
            final int id = imageId.incrementAndGet();
            updateService.submit(new Runnable() {
                @Override
                public void run() {
                    GraphBuffer graphBuffer = createGraphBuffer(id, maxW, maxH);
                    graphBufferRef.set(graphBuffer);
                    repaint();
                }
            });
        }

        protected GraphBuffer createGraphBuffer(int id, int maxW, int maxH) {
            if (id < imageId.get()) {
                // System.out.println("GraphView: Ignore image " + id + "<"
                // + imageId.get());
                return graphBufferRef.get();
            }

            long t = System.currentTimeMillis();
            BufferedImage img = null;
            mxPoint newTrans = view.getTranslate();
            double newScale = view.getScale();
            try {
                mxRectangle bounds = graph.getGraphBounds();
                int width =
                        (int) Math.ceil(bounds.getX() + bounds.getWidth() + 2);
                int height =
                        (int) Math.ceil(bounds.getY() + bounds.getHeight() + 2);
                if (width > maxW || height > maxH) {
                    width = maxW;
                    height = maxH;
                }
                img = mxUtils.createBufferedImage(width, height, null);
                Graphics2D g2d = img.createGraphics();
                try {
                    super.paint(g2d);
                } finally {
                    g2d.dispose();
                }
            } finally {
                log.info("Create GraphBuffer-"
                        + id
                        + " "
                        + (img == null ? "" : img.getWidth() + "x"
                                + img.getHeight()) + " on " + graph + " in "
                        + (System.currentTimeMillis() - t) + " ms");
            }
            return new GraphBuffer(view, img, newTrans, newScale);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.mxgraph.swing.mxGraphComponent.mxGraphControl#paint(java.awt.
         * Graphics)
         */
        @Override
        public void paint(Graphics g) {
            GraphBuffer graphBuffer = graphBufferRef.get();
            if (graphBuffer != null) {
                AffineTransform transform = new AffineTransform();
                double scale = graphBuffer.graphView.getScale();

                mxPoint graphBufferTrans = graphBuffer.trans;
                double graphBufferScale = graphBuffer.scale;

                mxPoint trans = graphBuffer.graphView.getTranslate();
                double dx = trans.getX() - graphBufferTrans.getX();
                double dy = trans.getY() - graphBufferTrans.getY();
                transform.translate(dx * scale, dy * scale);

                double newScale = scale / graphBufferScale;
                transform.scale(newScale, newScale);

                ((Graphics2D) g).drawImage(graphBuffer.image, transform, this);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.mxgraph.swing.mxGraphComponent.mxGraphControl#updatePreferredSize
         * ()
         */
        @Override
        public void updatePreferredSize() {
            // super.updatePreferredSize();
        }

        private class GraphBuffer {
            mxGraphView graphView;

            BufferedImage image;

            mxPoint trans;

            double scale;

            /**
             * Description:
             * 
             * @param image
             * @param trans
             * @param scale
             */
            public GraphBuffer(mxGraphView graphView, BufferedImage image,
                    mxPoint trans, double scale) {
                super();
                this.graphView = graphView;
                this.image = image;
                this.trans = trans;
                this.scale = scale;
            }

        }
    }
}
