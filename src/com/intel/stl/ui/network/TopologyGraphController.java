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
 *  File Name: GraphSelectiobTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.24.2.1  2015/08/12 15:26:50  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/04/28 14:00:34  jijunwan
 *  Archive Log:    1) improved topology viz to use TopGraph copy for outline display. This will avoid graph and outline views share internal graph view that may cause sync issues.
 *  Archive Log:    2) added more debug info in log
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/04/10 20:20:00  fernande
 *  Archive Log:    Changed TopologyView to be passed two background services (graphService and outlineService) which now reside in FabricController and can be properly shutdown when an error occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/03/19 16:30:04  jijunwan
 *  Archive Log:    added null check
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/03/16 17:45:40  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/03/16 15:47:13  jijunwan
 *  Archive Log:    minor improvement
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/02/23 22:45:59  jijunwan
 *  Archive Log:    improved to include/exclude inactive nodes/links in query
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/02/05 22:43:47  jijunwan
 *  Archive Log:    improved to handle graph selection
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/02/05 19:10:50  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/01/30 18:00:07  jijunwan
 *  Archive Log:    PR 126650 - For a Switch Device Set selection, Detailed Information Panel does not display "overall summary" and "topology summary"
 *  Archive Log:     - fixed control logic issues
 *  Archive Log:     - fixed a HFI local port number issue
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/12/11 18:47:06  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/11/05 16:29:26  jijunwan
 *  Archive Log:    synchronized topology update based on notices
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/11/03 23:06:12  jijunwan
 *  Archive Log:    improvement on topology view - drawing graph on background
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/10/23 16:00:04  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/10/22 02:21:26  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/10/14 11:32:15  jypak
 *  Archive Log:    UI updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/10/09 21:29:45  jijunwan
 *  Archive Log:    new Topology Viz
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/09 12:37:03  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/09/15 15:24:29  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/09/02 19:24:33  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/02 18:57:21  jijunwan
 *  Archive Log:    improvement on topology graph refresh - clear and then update graph
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/26 15:15:19  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/12 21:06:53  jijunwan
 *  Archive Log:    add null check
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/05 18:00:34  jijunwan
 *  Archive Log:    minor changes to adjust progress display
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;

import net.engio.mbassy.bus.MBassador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.GraphCells;
import com.intel.stl.ui.model.GraphEdge;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.model.LayoutType;
import com.intel.stl.ui.monitor.TreeNodeType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.network.TreeLayout.Style;
import com.intel.stl.ui.network.task.LayoutTask;
import com.intel.stl.ui.network.task.RefreshGraphTask;
import com.intel.stl.ui.network.task.ShowAllTask;
import com.intel.stl.ui.network.task.ShowEdgesTask;
import com.intel.stl.ui.network.task.ShowGroupTask;
import com.intel.stl.ui.network.task.ShowNodeTask;
import com.intel.stl.ui.network.task.ShowRoutesTask;
import com.intel.stl.ui.network.view.TopologyGraphView;
import com.intel.stl.ui.network.view.TopologyGuideView;
import com.intel.stl.ui.network.view.TopologyView;
import com.intel.stl.ui.publisher.CallbackAdapter;
import com.intel.stl.ui.publisher.CancellableCall;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.SingleTaskManager;

public class TopologyGraphController implements ITopologyListener {
    private static final Logger log = LoggerFactory
            .getLogger(TopologyGraphController.class);

    private static final boolean DEBUG = true;

    private final TopologyTreeController parent;

    private final TopologyGraphView graphView;

    private final TopologyGuideView guideView;

    private ISubnetApi subnetApi;

    private final SingleTaskManager taskMgr;

    private TopologyTreeModel fullTopTreeModel;

    private TopologyTreeModel topTreeModel;

    private TopologyUpdateController updateCtrl;

    private final ResourceController resourceController;

    private final LayoutType[] availableLayouts;

    private final LayoutType defaultLayout = LayoutType.TREE_SLASH;

    private LayoutType currentLayout = defaultLayout;

    private GraphCells lastGraphSelection;

    private FVResourceNode[] lastResourceSelection;

    /**
     * Description:
     * 
     * @param parent
     */
    public TopologyGraphController(TopologyTreeController parent,
            MBassador<IAppEvent> eventBus) {
        super();
        this.parent = parent;
        TopologyView topView = parent.getView();
        graphView = topView.getGraphView();
        graphView.setTopologyListener(this);
        availableLayouts = LayoutType.values();
        graphView.setAvailableLayouts(availableLayouts);

        guideView = topView.getGuideView();

        resourceController =
                new ResourceController(topView.getResourceView(), eventBus);
        taskMgr = new SingleTaskManager();
    }

    public void setContext(final Context pContext, IProgressObserver observer) {
        IProgressObserver[] subObservers = observer.createSubObservers(3);

        if (updateCtrl != null) {
            updateCtrl.cancel();
        }

        resourceController.setContext(pContext, subObservers[0]);
        subObservers[0].onFinish();

        subnetApi = pContext.getSubnetApi();
        List<NodeRecordBean> nodes = null;
        List<LinkRecordBean> links = null;
        try {
            nodes = subnetApi.getNodes(false);
            links = subnetApi.getLinks(false);
        } catch (Exception e) {
            e.printStackTrace();
            RuntimeException rte =
                    new RuntimeException("Could not retrieve nodes and links.",
                            e);
            throw rte;
        }
        if (nodes == null || links == null) {
            observer.onFinish();
            return;
        }

        subObservers[1].onFinish();
        if (observer.isCancelled()) {
            return;
        }

        final TopGraph fullGraph = TopGraph.createGraph();
        if (observer.isCancelled()) {
            return;
        }

        GraphBuilder builder = new GraphBuilder();
        fullTopTreeModel =
                topTreeModel = builder.build(fullGraph, nodes, links);
        if (topTreeModel == null || observer.isCancelled()) {
            return;
        }
        fullGraph.expandAll();
        TreeLayout layout =
                new TreeLayout(fullGraph, fullTopTreeModel, Style.SLASH);
        layout.execute(fullGraph.getDefaultParent());
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                TopGraph outlineGraph = TopGraph.createGraph();
                outlineGraph.setModel(fullGraph.getModel());
                guideView.setGraph(outlineGraph);
                graphView.setGraph(fullGraph);
            }
        });

        updateCtrl = new TopologyUpdateController(fullGraph, graphView);
        subObservers[2].onFinish();
        if (observer.isCancelled()) {
            return;
        }

    }

    public void onRefresh(IProgressObserver observer,
            final ICallback<Void> callback) {
        if (updateCtrl == null) {
            // this happens when #setContext exited because of null nodes or
            // links
            TopGraph fullGraph = TopGraph.createGraph();
            TopGraph outlineGraph = TopGraph.createGraph();
            guideView.setGraph(outlineGraph);

            graphView.setGraph(fullGraph);
            updateCtrl = new TopologyUpdateController(fullGraph, graphView);
        }
        RefreshGraphTask task =
                new RefreshGraphTask(this, null, null, defaultLayout, observer) {
                    @Override
                    public void onSuccess(ICancelIndicator indicator,
                            TopGraph graph) {
                        updateCtrl.setGraph(graph);
                        super.onSuccess(indicator, graph);
                        if (callback != null) {
                            callback.onDone(null);
                        } else {
                            onSelectionChange(lastGraphSelection, null,
                                    lastResourceSelection);
                        }
                    }
                };
        updateCtrl.update(task);
    }

    /**
     * @return the subnetApi
     */
    public ISubnetApi getSubnetApi() {
        return subnetApi;
    }

    /**
     * @return the graphView
     */
    public TopologyGraphView getGraphView() {
        return graphView;
    }

    /**
     * @return the guideView
     */
    public TopologyGuideView getGuideView() {
        return guideView;
    }

    /**
     * @return the resourceController
     */
    public ResourceController getResourceController() {
        return resourceController;
    }

    /**
     * @return the fullTopTreeModel
     */
    public TopologyTreeModel getFullTopTreeModel() {
        return fullTopTreeModel;
    }

    /**
     * @return the topTreeModel
     */
    public TopologyTreeModel getTopTreeModel() {
        return topTreeModel;
    }

    /**
     * @param topTreeModel
     *            the topTreeModel to set
     */
    public void setTopTreeModel(TopologyTreeModel topTreeModel) {
        this.topTreeModel = topTreeModel;
    }

    /**
     * @return the currentLayout
     */
    public LayoutType getCurrentLayout() {
        return currentLayout;
    }

    /**
     * @param currentLayout
     *            the currentLayout to set
     */
    public void setCurrentLayout(LayoutType currentLayout) {
        this.currentLayout = currentLayout;
    }

    protected void processTreeGroups(final FVResourceNode[] groups) {
        CancellableCall<GraphCells> caller = new CancellableCall<GraphCells>() {
            @Override
            public GraphCells call(ICancelIndicator indicator) throws Exception {
                GraphCells current = new GraphCells();
                for (FVResourceNode group : groups) {
                    for (FVResourceNode node : group.getChildren()) {
                        if (indicator.isCancelled()) {
                            return null;
                        }

                        GraphNode gNode = updateCtrl.getGraphNode(node.getId());
                        current.addNode(gNode);
                    }
                }
                return current;
            }
        };

        ICallback<GraphCells> callback = new CallbackAdapter<GraphCells>() {
            @Override
            public void onDone(GraphCells result) {
                onSelectionChange(result, TopologyGraphController.this, groups);
            }
        };

        taskMgr.submit(caller, callback);
    }

    protected void processTreeNodes(final FVResourceNode[] nodes) {
        CancellableCall<GraphCells> caller = new CancellableCall<GraphCells>() {
            @Override
            public GraphCells call(ICancelIndicator indicator) throws Exception {
                GraphCells current = new GraphCells();
                for (FVResourceNode node : nodes) {
                    if (indicator.isCancelled()) {
                        return null;
                    }

                    GraphNode gNode = updateCtrl.getGraphNode(node.getId());
                    current.addNode(gNode);
                }
                return current;
            }
        };

        ICallback<GraphCells> callback = new CallbackAdapter<GraphCells>() {
            @Override
            public void onDone(GraphCells result) {
                onSelectionChange(result, TopologyGraphController.this, nodes);
            }
        };

        taskMgr.submit(caller, callback);
    }

    protected void processTreePorts(final FVResourceNode[] nodes) {
        CancellableCall<GraphCells> caller = new CancellableCall<GraphCells>() {
            @Override
            public GraphCells call(ICancelIndicator indicator) throws Exception {
                GraphCells current = new GraphCells();
                Set<GraphEdge> edges = new HashSet<GraphEdge>();
                for (FVResourceNode node : nodes) {
                    if (indicator.isCancelled()) {
                        return null;
                    }

                    FVResourceNode parent = node.getParent();
                    int lid = parent.getId();
                    TreeNodeType type = parent.getType();
                    if (type == TreeNodeType.SWITCH && node.getId() == 0) {
                        // special case: if we select switch port zero,
                        // then treat as selecting the switch. We needn't to
                        // worry we may have mixed nodes and edges because our
                        // tree selection model already handle it to ensure we
                        // only have nodes or edges
                        GraphNode gNode = updateCtrl.getGraphNode(lid);
                        current.addNode(gNode);
                    } else {
                        int portNum = node.getId();
                        GraphNode gNode = updateCtrl.getGraphNode(lid);
                        if (gNode != null) {
                            GraphNode toNode = gNode.getNeighbor(portNum);
                            if (toNode != null) {
                                GraphEdge edge =
                                        new GraphEdge(lid, toNode.getLid(),
                                                gNode.getLinkPorts(toNode));
                                if (!edges.contains(edge)
                                        && !edges.contains(edge.normalize())) {
                                    current.addEdge(edge);
                                    edges.add(edge);
                                }
                            } else {
                                log.warn("Couldn't find connection for Lid="
                                        + lid + " PortNum=" + portNum);
                            }
                        } else {
                            log.warn("Couldn't find node with Lid=" + lid);
                        }
                    }
                }
                return current;
            }
        };

        ICallback<GraphCells> callback = new CallbackAdapter<GraphCells>() {
            @Override
            public void onDone(GraphCells result) {
                onSelectionChange(result, TopologyGraphController.this, nodes);
            }
        };

        taskMgr.submit(caller, callback);
    }

    protected void setLayout(final LayoutType type, final IModelChange preChange) {
        LayoutTask updateTask = new LayoutTask(this, type, preChange);
        updateCtrl.update(updateTask);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.ITopologyListener#onUndo()
     */
    @Override
    public void onUndo() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                graphView.enableUndo(updateCtrl.undo());
                graphView.enableRedo(true);
                graphView.revalidate();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.ITopologyListener#onRedo()
     */
    @Override
    public void onRedo() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                graphView.enableUndo(true);
                graphView.enableRedo(updateCtrl.redo());
                graphView.revalidate();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.ITopologyListener#onReset()
     */
    @Override
    public void onReset() {
        // onCollapseAll();
        setLayout(currentLayout, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.ITopologyListener#onLayoutTypeChange(int)
     */
    @Override
    public void onLayoutTypeChange(int typeIndex) {
        LayoutType layout = availableLayouts[typeIndex];
        if (currentLayout != layout) {
            setLayout(layout, null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.ITopologyListener#onExpandAll()
     */
    @Override
    public void onExpandAll() {
        if (currentLayout != null) {
            IModelChange expandAllchange = new IModelChange() {
                @Override
                public boolean execute(TopGraph graph,
                        ICancelIndicator indicator) {
                    graph.expandAll();
                    return true;
                }
            };
            setLayout(currentLayout, expandAllchange);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.ITopologyListener#onCollapseAll()
     */
    @Override
    public void onCollapseAll() {
        if (currentLayout != null) {
            IModelChange collapseAllchange = new IModelChange() {
                @Override
                public boolean execute(TopGraph graph,
                        ICancelIndicator indicator) {
                    graph.collapseAll();
                    return true;
                }
            };
            setLayout(currentLayout, collapseAllchange);
        }
    }

    @Override
    public void onSelectionChange(final GraphCells current,
            final Object source, FVResourceNode[] selecedResources) {
        if (DEBUG) {
            System.out.println("Current " + Arrays.toString(selecedResources)
                    + " " + current + " " + Thread.currentThread());
            System.out.println("Last " + lastGraphSelection);
        }

        // we have no valid layouted model yet, reject everything
        if (currentLayout == null) {
            return;
        }
        lastGraphSelection = current;
        lastResourceSelection = selecedResources;
        // special case: selection from graph
        if (selecedResources == null) {
            List<FVResourceNode> resources = new ArrayList<FVResourceNode>();
            if (current.hasNodes()) {
                for (GraphNode node : current.getNodes()) {
                    FVResourceNode resource =
                            new FVResourceNode(Integer.toString(node.getLid()),
                                    node.isEndNode() ? TreeNodeType.HFI
                                            : TreeNodeType.SWITCH,
                                    node.getLid());
                    resources.add(resource);
                }
            } else if (current.hasEdges()) {
                for (GraphEdge edge : current.getEdges()) {
                    Map<Integer, Integer> links = edge.getLinks();
                    int portNum = links.keySet().iterator().next();
                    FVResourceNode resource =
                            new FVResourceNode(edge.getFromLid() + ":"
                                    + portNum, TreeNodeType.ACTIVE_PORT,
                                    portNum);
                    resources.add(resource);
                }
            }
            selecedResources = resources.toArray(new FVResourceNode[0]);
        }

        final FVResourceNode firstResource =
                (selecedResources == null || selecedResources.length == 0) ? null
                        : selecedResources[0];
        final FVResourceNode[] resourceSelection = selecedResources;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // the following #showXXX methods should use SwingWorker when it
                // involves connecting to backend to collect data
                final List<GraphNode> nodes = current.getNodes();
                if (firstResource == null
                        || firstResource.getType() == TreeNodeType.ALL) {
                    onEmptySelection(source, resourceSelection);
                } else if (firstResource.isNode()) {
                    if (resourceSelection.length == 1) {
                        onSingleNode(nodes.get(0), source, resourceSelection);
                    } else {
                        onMultipleNodes(nodes, source, resourceSelection);
                    }
                } else if (firstResource.isPort()) {
                    if (firstResource.getId() > 0) {
                        List<GraphEdge> edges = current.getEdges();
                        onEdges(edges, source, resourceSelection);
                    } else {
                        // special cause: multiple switch zero ports are
                        // selected
                        if (resourceSelection.length == 1) {
                            onSingleNode(nodes.get(0), source,
                                    resourceSelection);
                        } else {
                            onMultipleNodes(nodes, source, resourceSelection);
                        }
                    }
                } else {
                    onNodeSet(nodes, source, resourceSelection);
                }
            }
        });
    }

    // no selection, try to show the whole graph
    protected void onEmptySelection(final Object source,
            final FVResourceNode[] selectedResources) {
        ShowAllTask updateTask =
                new ShowAllTask(this, source, selectedResources);
        updateCtrl.update(updateTask);
    }

    // a group of nodes that are treated as a set. it's different from
    // #onMultipleNodes because it doesn't show routing information among the
    // nodes
    protected void onNodeSet(final List<GraphNode> nodes, final Object source,
            final FVResourceNode[] selectedResources) {
        ShowGroupTask updateTask =
                new ShowGroupTask(this, source, selectedResources, nodes);
        updateCtrl.update(updateTask);
    }

    // single node
    protected void onSingleNode(final GraphNode node, final Object source,
            final FVResourceNode[] selectedResources) {
        ShowNodeTask updateTask =
                new ShowNodeTask(this, source, selectedResources, node);
        updateCtrl.update(updateTask);
    }

    // multiple nodes, try to find routes among the nodes
    protected void onMultipleNodes(final List<GraphNode> nodes,
            final Object source, final FVResourceNode[] selectedResources) {
        ShowRoutesTask updateTask =
                new ShowRoutesTask(this, source, selectedResources, nodes);
        updateCtrl.update(updateTask);
    }

    // multiple edges
    protected void onEdges(final List<GraphEdge> edges, final Object source,
            final FVResourceNode[] selectedResources) {
        ShowEdgesTask updateTask =
                new ShowEdgesTask(this, source, selectedResources, edges);
        updateCtrl.update(updateTask);
    }

    // protected NodesVisibilityChange getNodeFlipChange(List<GraphNode> nodes)
    // {
    // int[] toInspect = new int[nodes.size()];
    // for (int i = 0; i < toInspect.length; i++) {
    // toInspect[i] = nodes.get(i).getLid();
    // }
    // return new NodesVisibilityChange(currentLayout, topTreeModel, toInspect);
    // }

    public void clearTreeSelection() {
        parent.clearTreeSelection();
    }

    public FVResourceNode[] selectTreeNodes(List<GraphNode> nodes,
            ICancelIndicator indicator) {
        return parent.selectTreeNodes(nodes, indicator);
    }

    public FVResourceNode[] selectTreePorts(List<GraphEdge> edges,
            ICancelIndicator indicator) {
        return parent.selectTreePorts(edges, indicator);
    }

    public void cleanup() {
        try {
            if (updateCtrl != null) {
                updateCtrl.cancel();
            }
            graphView.getUpdateService().shutdown();
            log.info("GraphView update service shutdown");
        } finally {
            guideView.getUpdateService().shutdown();
            log.info("GuideView update service shutdown");
        }
    }
}
