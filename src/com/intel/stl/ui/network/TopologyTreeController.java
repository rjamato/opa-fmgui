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
 *  File Name: TopologyTreeController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.47  2015/11/02 20:08:52  jijunwan
 *  Archive Log:    PR 131377 - Port information when selecting between HFI nodes doesn't get updated under Topology tab
 *  Archive Log:    - improved node comparison to compare the whole path and the root need to be the same instance
 *  Archive Log:
 *  Archive Log:    Revision 1.46  2015/08/17 18:54:00  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.45  2015/08/05 04:09:31  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - applied undo mechanism on Topology Page
 *  Archive Log:
 *  Archive Log:    Revision 1.44  2015/06/25 15:27:19  jypak
 *  Archive Log:    PR 128980 - Be able to search devices by name or lid.
 *  Archive Log:    Fixes for the FindBugs issues.
 *  Archive Log:
 *  Archive Log:    Revision 1.43  2015/06/22 13:11:57  jypak
 *  Archive Log:    PR 128980 - Be able to search devices by name or lid.
 *  Archive Log:    New feature added to enable search devices by name, lid or node guid. The search results are displayed as a tree and when a result node from the tree is selected, original tree is expanded and the corresponding node is highlighted.
 *  Archive Log:
 *  Archive Log:    Revision 1.42  2015/06/05 16:45:30  jijunwan
 *  Archive Log:    PR 129089 - Link jumping doesn't keep context
 *  Archive Log:    - search in current tree and use current node as hint in node search
 *  Archive Log:
 *  Archive Log:    Revision 1.41  2015/05/07 14:18:40  jypak
 *  Archive Log:    PR 128564 - Topology Tree synchronization issue:
 *  Archive Log:    Null check the context before update in the TopologyTreeController. Other safe guard code added to avoid potential synchronization issue.
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2015/04/28 13:52:09  jijunwan
 *  Archive Log:    ignored PortUpdateEvent since it's redundant to NodeUpdateEvent
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2015/02/02 15:38:28  rjtierne
 *  Archive Log:    New TaskScheduler architecture; now employs subscribers to submit
 *  Archive Log:    tasks for scheduling.  When update rate is changed on Wizard, TaskScheduler
 *  Archive Log:    uses this new architecture to terminate tasks and service and restart them.
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2014/12/11 18:47:06  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2014/11/05 16:29:26  jijunwan
 *  Archive Log:    synchronized topology update based on notices
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2014/11/03 23:06:12  jijunwan
 *  Archive Log:    improvement on topology view - drawing graph on background
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2014/10/22 13:57:01  jijunwan
 *  Archive Log:    temporarily commented update code for topology. Will check in working code later when we have time to work on it.
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2014/10/22 02:21:26  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2014/10/14 11:32:15  jypak
 *  Archive Log:    UI updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2014/10/09 21:29:45  jijunwan
 *  Archive Log:    new Topology Viz
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2014/10/09 12:37:03  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2014/10/02 21:37:55  jijunwan
 *  Archive Log:    fixed issues found by FindBugs
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2014/09/18 14:57:43  jijunwan
 *  Archive Log:    supported jumpTo events
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2014/09/15 15:24:29  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2014/09/02 19:24:33  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2014/08/26 15:15:19  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2014/08/05 18:00:34  jijunwan
 *  Archive Log:    minor changes to adjust progress display
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2014/08/05 13:46:23  jijunwan
 *  Archive Log:    new implementation on topology control that uses double models to avoid synchronization issues on model and view
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2014/07/22 20:10:47  rjtierne
 *  Archive Log:    Removed nodes from call to showPath()
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2014/07/22 16:14:46  jijunwan
 *  Archive Log:    minor change - added debug info
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/07/22 16:02:11  jijunwan
 *  Archive Log:    ongoing work
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/07/22 15:00:28  jijunwan
 *  Archive Log:    ongoing work
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/07/18 13:42:11  rjtierne
 *  Archive Log:    Changed prototype for showPath() to accept a list of nodes to
 *  Archive Log:    match the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/07/11 19:29:01  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/07/11 13:16:28  jypak
 *  Archive Log:    Added runtime, non runtime exceptions handler for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    As of now, all different exceptions are generally handled as 'Exception' but when we define how to handle differently for different exception, based on the error code, handler (catch block will be different). Also, we are thinking of centralized 'failure recovery' process to handle all exceptions in one place.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/07/10 21:25:26  rjtierne
 *  Archive Log:    Calling showPath() after edges are created
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/07/08 20:29:53  rjtierne
 *  Archive Log:    Removed ITopologyResourceView interface requirement.  Replaced ResourceDetailsCard and View with ResourceController and View to accommodate swappable JCards
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/07/07 19:06:05  jijunwan
 *  Archive Log:    minor improvements:
 *  Archive Log:    1) null check
 *  Archive Log:    2) stop previous context switching when we need to switch to a new one
 *  Archive Log:    3) auto fit when we resize split panes
 *  Archive Log:    4) put layout execution on background
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/07/03 22:23:47  jijunwan
 *  Archive Log:    1) improved Topology to support multiple edges selection
 *  Archive Log:    2) added Tree and Graph selection synchronization
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/06/30 21:08:27  rjtierne
 *  Archive Log:    Removed print statements
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/06/26 20:27:19  rjtierne
 *  Archive Log:    Passed the context to the resourceCard
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/06/26 15:49:11  jijunwan
 *  Archive Log:    performance improvement - share tree model among pages so we do not build model several times
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/06/26 15:00:18  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/06/25 13:44:42  rjtierne
 *  Archive Log:    Changed Route page references to Port
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/24 20:28:25  rjtierne
 *  Archive Log:    Added ResourceDetailsCard and added the show methods to the
 *  Archive Log:    ITopologyResourceView interface
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/23 04:56:56  jijunwan
 *  Archive Log:    new topology code to support interactions with a topology graph
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/19 20:14:24  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/17 16:55:41  jijunwan
 *  Archive Log:    pan and zoom at current mouse point; shift+mouse drag to move nodes on screen; right control to clear selections; undoable node collapse and expand
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/27 22:09:08  jijunwan
 *  Archive Log:    added Tree_Line layout, added collapsable image
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/27 13:48:39  jijunwan
 *  Archive Log:    added connection highlight
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/23 19:47:54  jijunwan
 *  Archive Log:    init version of topology page
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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.tree.TreePath;

import net.engio.mbassy.bus.MBassador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.event.NodeUpdateEvent;
import com.intel.stl.ui.event.PortUpdateEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.UndoableSelection;
import com.intel.stl.ui.model.GraphCells;
import com.intel.stl.ui.model.GraphEdge;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.monitor.SearchController;
import com.intel.stl.ui.monitor.TreeController;
import com.intel.stl.ui.monitor.TreeNodeType;
import com.intel.stl.ui.monitor.TreeSelection;
import com.intel.stl.ui.monitor.TreeSubpageSelection;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.monitor.tree.FVTreeManager;
import com.intel.stl.ui.monitor.tree.FVTreeModel;
import com.intel.stl.ui.network.view.TopologyView;
import com.intel.stl.ui.publisher.CallbackAdapter;

public class TopologyTreeController extends TreeController<TopologyView> {
    private static final Logger log = LoggerFactory
            .getLogger(TopologyTreeController.class);

    private static final boolean DEBUG = true;

    private FVResourceNode[] lastTreeSelection;

    private final TopologyGraphController graphSelectionController;

    private String previousSubpageName;

    private String currentSubpageName;

    /**
     * Description:
     * 
     * @param pTreeView
     */
    public TopologyTreeController(TopologyView pTreeView,
            MBassador<IAppEvent> eventBus, FVTreeManager treeBuilder) {
        super(pTreeView, eventBus, treeBuilder);
        graphSelectionController = new TopologyGraphController(this, eventBus);

        new SearchController(view.getSearchView(), eventBus, treeBuilder, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.TreeController#setContext(com.intel.stl.ui.main
     * .Context)
     */
    @Override
    public synchronized void setContext(final Context pContext,
            IProgressObserver observer) {
        IProgressObserver[] subObservers = observer.createSubObservers(2);
        graphSelectionController.setContext(pContext, subObservers[0]);
        subObservers[0].onFinish();
        if (observer.isCancelled()) {
            return;
        }
        super.setContext(pContext, subObservers[1]);
        subObservers[1].onFinish();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.TreeController#onRefresh(com.intel.stl.ui.common
     * .IProgressObserver)
     */
    @Override
    public void onRefresh(final IProgressObserver observer) {
        isSystemUpdate = true;
        // we do refresh by recovering last tree selections which is done when
        // we refresh our hierarchy trees. Setting lastTreeSelection to null
        // will allow us respond to these selections rather ignore them.
        lastTreeSelection = null;
        final IProgressObserver[] subObservers = observer.createSubObservers(2);
        graphSelectionController.onRefresh(subObservers[0],
                new CallbackAdapter<Void>() {
                    @Override
                    public void onDone(Void result) {
                        if (!observer.isCancelled()) {
                            // refresh tree only after graph model was updated
                            // properly, so we can update tree selection on our
                            // graph correctly
                            refreshTreeOnBackground(subObservers[1]);
                        }
                    }
                });
    }

    protected void refreshTreeOnBackground(final IProgressObserver observer) {
        mContext.getTaskScheduler().submitToBackground(new Runnable() {
            @Override
            public void run() {
                TopologyTreeController.super.onRefresh(observer);
            }
        });
    }

    @Override
    public synchronized void onNodeUpdate(final NodeUpdateEvent evt) {
        // ignore portUpdateEvent
        if (evt instanceof PortUpdateEvent || mContext == null) {
            return;
        }

        isSystemUpdate = true;
        lastTreeSelection = null;
        graphSelectionController.onRefresh(null, new CallbackAdapter<Void>() {
            @Override
            public void onDone(Void result) {
                // update tree only after graph model was updated
                // properly, so we can update tree selection on our
                // graph correctly
                updateTreeOnBackground(evt);
            }
        });
    }

    protected void updateTreeOnBackground(final NodeUpdateEvent evt) {
        mContext.getTaskScheduler().submitToBackground(new Runnable() {
            @Override
            public void run() {
                TopologyTreeController.super.onNodeUpdate(evt);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.TreeController#showNode(com.intel.stl.ui.monitor
     * .FVResourceNode)
     */
    @Override
    protected void showNode(FVResourceNode node) {
        if (node != null) {
            showNodes(new FVResourceNode[] { node });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.TreeController#showNodes(com.intel.stl.ui.monitor
     * .FVResourceNode[])
     */
    @Override
    protected synchronized void showNodes(FVResourceNode[] nodes) {
        if (DEBUG) {
            System.out.println("Current TreeNodes " + Arrays.toString(nodes));
            System.out.println("Last TreeNodes "
                    + Arrays.toString(lastTreeSelection));
        }

        if (nodes == null || nodes.length == 0
                || areSameNodes(lastTreeSelection, nodes)) {
            return;
        }

        previousSubpageName = graphSelectionController.getCurrentSubpage();
        // always set subpage name to null for a new set of nodes. If they come
        // from undo, we shall keep currentSubpageName because it's set during
        // undo
        if (!undoHandler.isInProgress()) {
            currentSubpageName = null;
        }

        graphSelectionController.setCurrentSubpage(currentSubpageName);
        collapseTreeSelections(lastTreeSelection, nodes);
        lastTreeSelection = nodes;
        switch (nodes[0].getType()) {
            case SWITCH:
            case HFI:
                graphSelectionController.processTreeNodes(nodes);
                break;

            case ACTIVE_PORT:
            case INACTIVE_PORT:
                graphSelectionController.processTreePorts(nodes);
                break;

            case HCA_GROUP:
            case SWITCH_GROUP:
            case DEVICE_GROUP:
            case VIRTUAL_FABRIC:
                graphSelectionController.processTreeGroups(nodes);
                break;

            default:
                graphSelectionController.onSelectionChange(new GraphCells(),
                        graphSelectionController, nodes);
                break;
        } // switch
    }

    /**
     * <i>Description:</i> to return <code>true</code>, the nodes in
     * <code>nodes1</code> and <code>nodes2</code> shall have the same path and
     * the root needs to be the same instance
     * 
     * @param nodes1
     * @param nodes2
     * @return
     */
    protected boolean areSameNodes(FVResourceNode[] nodes1,
            FVResourceNode[] nodes2) {
        if (!Arrays.equals(nodes1, nodes2)) {
            return false;
        }

        for (int i = 0; i < nodes1.length; i++) {
            if (!nodes1[i].hasSamePath(nodes2[i])) {
                return false;
            } else if (nodes1[i].getRoot() != nodes2[i].getRoot()) {
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.TreeController#getUndoableSelection(com.intel
     * .stl.ui.monitor.TreeSelection, com.intel.stl.ui.monitor.TreeSelection)
     */
    @Override
    protected UndoableSelection<?> getUndoableSelection(
            TreeSelection oldSelection, TreeSelection newSelection) {
        // In theory, the currentSubpageName shall be the real current subpage
        // name. However, it will take time to know the subpage name because
        // the subpage is set after background tasks finished. This will require
        // some sync and the control logic can be complex. To make things
        // simple, we always set desired subpage name to null, and rely on the
        // same logic to figure out the real subpage, i.e. always pick up the
        // first subpage. In this way, we needn't to wait for the real subpage
        // and can safely generate undo action by using null for
        // currentSubpageName.
        TreeSubpageSelection oldTSSelection =
                new TreeSubpageSelection(oldSelection, previousSubpageName);
        TreeSubpageSelection newTSSelection =
                new TreeSubpageSelection(newSelection, currentSubpageName);
        return new UndoableTopTreeSelection(this, oldTSSelection,
                newTSSelection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.monitor.TreeController#getCurrentNode()
     */
    @Override
    protected FVResourceNode getCurrentNode() {
        if (lastTreeSelection == null || lastTreeSelection.length == 0) {
            return null;
        } else {
            return lastTreeSelection[lastTreeSelection.length - 1];
        }
    }

    protected void collapseTreeSelections(FVResourceNode[] previous,
            FVResourceNode[] current) {
        if (previous == null || previous.length == 0) {
            return;
        }

        Set<TreePath> toCollapse = new HashSet<TreePath>();
        for (FVResourceNode node : previous) {
            TreePath path = null;
            switch (node.getType()) {
                case SWITCH:
                case HFI:
                    if (node.getParent() != null) {
                        path = node.getParent().getPath();
                    }
                    break;

                case ACTIVE_PORT:
                case INACTIVE_PORT:
                    if (node.getParent() != null) {
                        path = node.getParent().getPath();
                    }
                    break;

                default:
                    break;
            }
            if (path != null) {
                toCollapse.add(path);
            }
        }
        for (FVResourceNode node : current) {
            switch (node.getType()) {
                case SWITCH:
                case HFI:
                    if (node.getParent() != null) {
                        toCollapse.remove(node.getParent().getPath());
                    }
                    break;

                case ACTIVE_PORT:
                case INACTIVE_PORT:
                    FVResourceNode parent = node.getParent();
                    if (parent != null) {
                        toCollapse.remove(parent.getPath());
                        parent = parent.getParent();
                        if (parent != null) {
                            toCollapse.remove(parent.getPath());
                        }
                    }
                    break;

                default:
                    break;
            }
        }
        for (TreePath path : toCollapse) {
            view.collapseTreePath(getCurrentTreeModel(), path);
        }
    }

    protected void selectTreeSelections(FVResourceNode[] nodes) {
        if (nodes == null || nodes.length == 0) {
            return;
        }

        TreePath[] paths = new TreePath[nodes.length];
        for (int i = 0; i < paths.length; i++) {
            paths[i] = nodes[i].getPath();
        }
        view.setTreeSelection(getCurrentTreeModel(), paths);
    }

    /**
     * Description:
     * 
     */
    public void clearTreeSelection() {
        view.clearTreeSelection(getCurrentTreeModel());
    }

    protected FVResourceNode[] selectTreeNodes(List<GraphNode> nodes,
            ICancelIndicator indicator) {
        FVTreeModel model = getCurrentTreeModel();
        List<TreePath> paths = new ArrayList<TreePath>();
        List<FVResourceNode> treeNodes = new ArrayList<FVResourceNode>();
        for (GraphNode node : nodes) {
            if (indicator.isCancelled()) {
                return null;
            }
            int lid = node.getLid();
            TreePath path = null;
            path =
                    model.getTreePath(lid, node.isEndNode() ? TreeNodeType.HFI
                            : TreeNodeType.SWITCH, getSearchHint());
            if (path == null) {
                log.warn("Couldn't find tree node for node Lid=" + lid);
            } else {
                paths.add(path);
                treeNodes.add((FVResourceNode) path.getLastPathComponent());
            }
        }
        if (!indicator.isCancelled() && !paths.isEmpty()) {
            lastTreeSelection = treeNodes.toArray(new FVResourceNode[0]);
            view.setTreeSelection(model, paths.toArray(new TreePath[0]));
        }
        return lastTreeSelection;
    }

    protected FVResourceNode getSearchHint() {
        if (lastTreeSelection == null || lastTreeSelection.length == 0) {
            return null;
        }

        FVResourceNode hint = lastTreeSelection[0];
        switch (hint.getType()) {
            case SWITCH:
            case HFI:
                hint = hint.getParent();
                break;

            case ACTIVE_PORT:
            case INACTIVE_PORT:
                hint = hint.getParent().getParent();
                break;

            default:
                break;
        }
        return hint;
    }

    protected FVResourceNode[] selectTreePorts(List<GraphEdge> edges,
            ICancelIndicator indicator) {
        FVTreeModel model = getCurrentTreeModel();
        List<TreePath> paths = new ArrayList<TreePath>();
        for (GraphEdge edge : edges) {
            if (indicator.isCancelled()) {
                return null;
            }
            int lid = edge.getFromLid();
            Collection<Integer> ports = edge.getLinks().keySet();
            populatePaths(lid, ports, paths);

            lid = edge.getToLid();
            ports = edge.getLinks().values();
            populatePaths(lid, ports, paths);
        }

        if (indicator.isCancelled()) {
            return null;
        }
        // by default we will scroll tree to ensure the first path visible.
        // to avoid escaping from the current selected tree node, we put the
        // first matched selection on the top of tree path array. The following
        // code is inefficient. But given we have no a lot selections, it
        // should be fine. Otherwise we need to consider use hash
        int firstPath = -1;
        if (lastTreeSelection != null && paths.size() > 1) {
            for (FVResourceNode node : lastTreeSelection) {
                for (int i = 0; i < paths.size(); i++) {
                    if (paths.get(i).getLastPathComponent() == node) {
                        firstPath = i;
                        break;
                    }
                }
            }
        }
        if (firstPath >= 0) {
            TreePath path = paths.remove(firstPath);
            paths.add(0, path);
        }

        if (!paths.isEmpty()) {
            List<FVResourceNode> treeNodes = new ArrayList<FVResourceNode>();
            TreePath[] pathArray = new TreePath[paths.size()];
            for (int i = 0; i < paths.size(); i++) {
                pathArray[i] = paths.get(i);
                treeNodes.add((FVResourceNode) pathArray[i]
                        .getLastPathComponent());
            }
            lastTreeSelection = treeNodes.toArray(new FVResourceNode[0]);
            view.setTreeSelection(model, pathArray);
        }
        return lastTreeSelection;
    }

    protected void populatePaths(int lid, Collection<Integer> ports,
            List<TreePath> paths) {
        FVTreeModel model = getCurrentTreeModel();
        TreePath path =
                model.getTreePath(lid, TreeNodeType.NODE, getSearchHint());
        if (path == null) {
            log.warn("Couldn't find tree node for node Lid=" + lid);
        } else {
            FVResourceNode node = (FVResourceNode) path.getLastPathComponent();
            if (node.getType() == TreeNodeType.HFI) {
                paths.add(path.pathByAddingChild(node.getChildAt(0)));
            } else {
                for (int port : ports) {
                    paths.add(path.pathByAddingChild(node.getChildAt(port)));
                }
            }
        }
    }

    @Override
    public String getName() {
        return JumpDestination.TOPOLOGY.getName();
    }

    public void cleanup() {
        graphSelectionController.cleanup();
    }

    /**
     * <i>Description:</i>
     * 
     * @param treeModel
     * @param paths
     * @param expanded
     * @param subpageName
     */
    public void showNode(FVTreeModel treeModel, TreePath[] paths,
            boolean[] expanded, String subpageName) {
        // isSystemUpdate = true;
        currentSubpageName = subpageName;
        showNode(treeModel, paths, expanded);
    }
}
