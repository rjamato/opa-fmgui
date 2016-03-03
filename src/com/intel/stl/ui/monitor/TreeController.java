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
 *  File Name: TreeController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.32  2015/12/10 17:22:03  jijunwan
 *  Archive Log:    PR 132014 - Tree update doesn't work properly for DeviceGroup and VirtualFabric
 *  Archive Log:    - fixed typo in code
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/12/03 17:15:51  jijunwan
 *  Archive Log:    PR 131865 - Klocwork Issue on TreeController
 *  Archive Log:    - fixed null pointer issues
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/10/23 19:20:07  jijunwan
 *  Archive Log:    PR 129357 - Be able to hide inactive ports
 *  Archive Log:    - improved to apply filter after we created or updated  a tree
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/08/17 18:53:40  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/08/06 17:19:41  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - improved GroupSelectedEvent to GroupsSelectedEvent to support selecting multiple groups
 *  Archive Log:    - fixed couple NPE issues
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/08/05 04:04:47  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - applied undo mechanism on Performance Page
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/07/15 19:05:04  fernande
 *  Archive Log:    PR 129199 - Checking copyright test as part of the build step. Fixed year appearing in copyright notice
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/06/22 13:11:50  jypak
 *  Archive Log:    PR 128980 - Be able to search devices by name or lid.
 *  Archive Log:    New feature added to enable search devices by name, lid or node guid. The search results are displayed as a tree and when a result node from the tree is selected, original tree is expanded and the corresponding node is highlighted.
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/06/05 16:45:27  jijunwan
 *  Archive Log:    PR 129089 - Link jumping doesn't keep context
 *  Archive Log:    - search in current tree and use current node as hint in node search
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/04/28 13:52:08  jijunwan
 *  Archive Log:    ignored PortUpdateEvent since it's redundant to NodeUpdateEvent
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/04/07 22:14:03  jijunwan
 *  Archive Log:    turn off "Marked Node" on device tree
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/11/05 23:00:24  jijunwan
 *  Archive Log:    improved UI update event to batch mode so we can efficiently process multiple notices
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/11/05 16:29:25  jijunwan
 *  Archive Log:    synchronized topology update based on notices
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/10/14 11:32:09  jypak
 *  Archive Log:    UI updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/10/09 12:35:09  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/09/18 14:57:38  jijunwan
 *  Archive Log:    supported jumpTo events
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/09/15 15:24:32  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/09/02 19:24:29  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/09/02 19:03:00  jijunwan
 *  Archive Log:    tree update based on merge sort algorithm
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/08/26 15:15:27  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/08/05 13:36:42  jijunwan
 *  Archive Log:    fixed typo isCanceled->isCanelled, added cancel interface
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/07/16 15:02:15  jijunwan
 *  Archive Log:    fixed a bug happens when we open a tree and then switch to another subnet. It may lead to multiple trees opened.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/07/11 19:27:33  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/07/07 18:18:18  jijunwan
 *  Archive Log:    improved to handle switching subnet when one is still in the process of initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/03 22:21:24  jijunwan
 *  Archive Log:    extended TreeController and TreeView to support multi-selection and programmly operate a tree
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/26 15:49:10  jijunwan
 *  Archive Log:    performance improvement - share tree model among pages so we do not build model several times
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/26 15:00:15  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/29 14:26:33  jijunwan
 *  Archive Log:    added code to select first item on first available tree by default
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/15 14:33:15  jijunwan
 *  Archive Log:    minor change on tree controller to support generic
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/01 16:06:57  rjtierne
 *  Archive Log:    Class is now abstract with abstract method showNode(). Also
 *  Archive Log:    implements valueChanged() for the TreeSelectionListener interface.
 *  Archive Log:    This class is passed as a listener to the view.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/24 18:29:07  rjtierne
 *  Archive Log:    Renamed SUBNET_TREE to DEVICE_TYPES_TREE
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 20:47:24  rjtierne
 *  Archive Log:    Relocated from common.view to monitor package
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/17 14:38:42  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: This class builds trees, and creates and set the tree models in
 *  the tree view
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.event.GroupsSelectedEvent;
import com.intel.stl.ui.event.JumpToEvent;
import com.intel.stl.ui.event.NodeUpdateEvent;
import com.intel.stl.ui.event.NodesSelectedEvent;
import com.intel.stl.ui.event.PortUpdateEvent;
import com.intel.stl.ui.event.PortsSelectedEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.UndoHandler;
import com.intel.stl.ui.main.UndoableSelection;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.monitor.tree.FVTreeManager;
import com.intel.stl.ui.monitor.tree.FVTreeModel;
import com.intel.stl.ui.monitor.tree.InactivePortVizIndicator;
import com.intel.stl.ui.monitor.view.TreeViewInterface;

/**
 * @author tierney
 * 
 */
public abstract class TreeController<E extends TreeViewInterface> implements
        TreeControllerInterface, TreeSelectionListener {

    /**
     * Tree View
     */
    protected E view;

    /**
     * Tree models
     */
    private final Map<TreeTypeEnum, FVTreeModel> treeModels;

    protected FVTreeModel currentTreeModel;

    /**
     * API Context
     */
    protected Context mContext;

    /**
     * Tree builder creates hierarchical trees of various types
     */
    protected FVTreeManager mTreeBuilder;

    protected final MBassador<IAppEvent> eventBus;

    protected UndoHandler undoHandler;

    /**
     * System update, such as initialization, refresh, notice response etc.,
     * will trigger tree selection changes. This attribute tracks when system is
     * updating, so we know when we should ignore tree selection on undo track
     */
    protected boolean isSystemUpdate;

    private TreeSelection currentSelection;

    private final InactivePortVizIndicator treeIndicator =
            new InactivePortVizIndicator();

    public TreeController(E pTreeView, MBassador<IAppEvent> eventBus,
            FVTreeManager treeBuilder) {
        view = pTreeView;
        view.addTreeSelectionListener(this);
        mTreeBuilder = treeBuilder;
        this.eventBus = eventBus;
        treeModels = new HashMap<TreeTypeEnum, FVTreeModel>();
        eventBus.subscribe(this);
    } // TreeController

    /**
     * @return the view
     */
    public E getView() {
        return view;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.hpc.stl.ui.trees.TreeControllerInterface#setContext(com.intel
     * .hpc.stl.ui.Context)
     */
    @Override
    public void setContext(Context context, IProgressObserver observer) {
        mContext = context;
        treeIndicator.setContext(context);
        isSystemUpdate = true;
        buildTrees(observer);
        view.clear();

        if (context != null && context.getController() != null) {
            undoHandler = context.getController().getUndoHandler();
        }
    } // setContext

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.TreeControllerInterface#onRefresh(com.intel.
     * stl.ui.common.IProgressObserver)
     */
    @Override
    public void onRefresh(IProgressObserver observer) {
        isSystemUpdate = true;
        updateTrees(observer);
        // By reselecting current selection, all subpage updates for current
        // selected node will be done.
        // Note: Running icon is running so no observer needed.
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                TreeTypeEnum currentTreeType =
                        getTreeType(getCurrentTreeModel());
                view.setTreeSelection(currentTreeType);
            }
        });
    }

    @Override
    public void onNodeUpdate(NodeUpdateEvent evt) {
        // ignore portUpdateEvent
        if (evt instanceof PortUpdateEvent) {
            return;
        }

        isSystemUpdate = true;
        // Not in SwingWorker, let main thread handle this.
        int[] lids = evt.getNodeLids();
        for (int lid : lids) {
            updateTreeNode(lid);
        }

        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                TreeTypeEnum currentTreeType =
                        getTreeType(getCurrentTreeModel());
                view.setTreeSelection(currentTreeType);
            }
        });
    }

    public void buildTrees(IProgressObserver observer) {
        IProgressObserver[] subObservers = observer.createSubObservers(4);
        buildTree(TreeTypeEnum.DEVICE_TYPES_TREE, subObservers[0]);
        subObservers[0].onFinish();
        buildTree(TreeTypeEnum.DEVICE_GROUPS_TREE, subObservers[1]);
        subObservers[1].onFinish();
        buildTree(TreeTypeEnum.VIRTUAL_FABRICS_TREE, subObservers[2]);
        subObservers[2].onFinish();
        // buildTree(TreeTypeEnum.TOP_10_CONGESTED_TREE, subObservers[3]);
        subObservers[3].onFinish();
    }

    protected void buildTree(TreeTypeEnum type, IProgressObserver observer) {
        // Build the trees and set the corresponding models in the tree view
        FVTreeModel oldModel = treeModels.get(type);
        if (oldModel != null) {
            mTreeBuilder.removeMonitor(type, oldModel);
        }
        FVResourceNode tree = mTreeBuilder.buildTree(type, observer);
        if (observer.isCancelled()) {
            return;
        }
        FVTreeModel treeModel = new FVTreeModel(tree);
        treeModel.filter(treeIndicator);
        view.setTreeModel(type, treeModel);
        treeModels.put(type, treeModel);
        mTreeBuilder.addMonitor(type, treeModel);
    }

    public void updateTrees(IProgressObserver observer) {
        IProgressObserver[] subObservers = observer.createSubObservers(4);
        updateTree(TreeTypeEnum.DEVICE_TYPES_TREE, subObservers[0]);
        updateTree(TreeTypeEnum.DEVICE_GROUPS_TREE, subObservers[1]);
        updateTree(TreeTypeEnum.VIRTUAL_FABRICS_TREE, subObservers[2]);
        // mTreeBuilder.updateTree(TreeTypeEnum.TOP_10_CONGESTED_TREE,
        // subObservers[3]);
        subObservers[3].onFinish();
    }

    protected void updateTree(TreeTypeEnum type, IProgressObserver observer) {
        FVTreeModel treeModel = treeModels.get(type);
        if (treeModel != null) {
            mTreeBuilder.updateTree(type, observer);
            treeModel.filter(treeIndicator);
            view.setTreeModel(type, treeModel);
        }
    }

    public void updateTreeNode(int lid) {
        updateTreeNode(lid, TreeTypeEnum.DEVICE_TYPES_TREE);
        updateTreeNode(lid, TreeTypeEnum.DEVICE_GROUPS_TREE);
        updateTreeNode(lid, TreeTypeEnum.VIRTUAL_FABRICS_TREE);
        // updateTreeNode(lid, TreeTypeEnum.TOP_10_CONGESTED_TREE);
    }

    protected void updateTreeNode(int lid, TreeTypeEnum type) {
        FVTreeModel treeModel = treeModels.get(type);
        if (treeModel != null) {
            mTreeBuilder.updateTreeNode(lid, type);
            treeModel.filter(treeIndicator);
            view.setTreeModel(type, treeModel);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        JTree tree = (JTree) e.getSource();
        if (tree.getSelectionCount() == 0) {
            return;
        }

        TreeSelection oldSelection = currentSelection;
        currentTreeModel = (FVTreeModel) tree.getModel();

        if (tree.getSelectionCount() == 1) {
            Object node = tree.getLastSelectedPathComponent();
            if (node != null && (node instanceof FVResourceNode)) {
                currentSelection = new TreeSelection(currentTreeModel);
                currentSelection.addNode((FVResourceNode) node,
                        tree.isExpanded(tree.getSelectionPath()));
                showNode((FVResourceNode) node);
            }
        } else {
            TreePath[] paths = tree.getSelectionPaths();
            FVResourceNode[] nodes = new FVResourceNode[paths.length];
            currentSelection = new TreeSelection(currentTreeModel);
            for (int i = 0; i < nodes.length; i++) {
                Object node = paths[i].getLastPathComponent();
                if (node != null && (node instanceof FVResourceNode)) {
                    nodes[i] = (FVResourceNode) node;
                    currentSelection.addNode(nodes[i],
                            tree.isExpanded(paths[i]));
                }
            }
            showNodes(nodes);
        }

        // when we refresh or respond to a notice, StackPanel will remove
        // selections first and then add them back. This will trigger two
        // valueChanged calls. Checking whether currentSelection is null or not
        // allows us ignore the case of removing all selections, i.e.
        // currentSelection is null
        if (!isSystemUpdate && undoHandler != null
                && !undoHandler.isInProgress()) {
            UndoableSelection<?> undoSel =
                    getUndoableSelection(oldSelection, currentSelection);
            undoHandler.addUndoAction(undoSel);
        }
        if (isSystemUpdate) {
            isSystemUpdate = false;
        }
    }

    protected abstract UndoableSelection<?> getUndoableSelection(
            TreeSelection oldSelection, TreeSelection newSelection);

    public void showNode(TreeTypeEnum type, FVResourceNode node) {
        showNode(treeModels.get(type), node, false);
    }

    public synchronized void showNode(FVTreeModel treeModel,
            FVResourceNode node, boolean isExpanded) {
        showNode(treeModel, new TreePath[] { node.getPath() },
                new boolean[] { isExpanded });
    }

    public synchronized void showNode(FVTreeModel treeModel, TreePath[] paths,
            boolean[] isExpanded) {
        currentTreeModel = treeModel;
        view.expandAndSelectTreePath(currentTreeModel, paths, isExpanded);
    }

    /**
     * @return the currentTreeModel
     */
    public FVTreeModel getCurrentTreeModel() {
        if (currentTreeModel == null) {
            // this shouldn't happen
            currentTreeModel = treeModels.get(TreeTypeEnum.DEVICE_TYPES_TREE);
        }
        return currentTreeModel;
    }

    public FVTreeModel getTreeModel(TreeTypeEnum type) {
        return treeModels.get(type);
    }

    public TreeTypeEnum getTreeType(FVTreeModel model) {
        if (model == null) {
            return TreeTypeEnum.DEVICE_TYPES_TREE;
        }

        for (TreeTypeEnum type : treeModels.keySet()) {
            if (treeModels.get(type) == model) {
                return type;
            }
        }
        throw new RuntimeException("Unknown FVTreeModel " + model);
    }

    /**
     * 
     * Description: Derived class will show specified node info
     * 
     * @param node
     *            - node to display
     */
    protected abstract void showNode(FVResourceNode node);

    protected abstract void showNodes(FVResourceNode[] nodes);

    protected abstract FVResourceNode getCurrentNode();

    @Handler
    protected void onGroupSelected(GroupsSelectedEvent event) {
        if (!acceptEvent(event)) {
            return;
        }

        FVTreeModel deviceTypesTreeModel = getCurrentTreeModel();
        if (deviceTypesTreeModel == null) {
            return;
        }

        TreePath[] paths = new TreePath[event.getNumGroups()];
        for (int i = 0; i < paths.length; i++) {
            paths[i] =
                    deviceTypesTreeModel.getTreePath(event.getName(i),
                            event.getType(i));
        }
        isSystemUpdate = true;
        view.expandAndSelectTreePath(deviceTypesTreeModel, paths,
                new boolean[paths.length]);
    }

    @Handler
    protected void onNodesSelected(NodesSelectedEvent event) {
        if (!acceptEvent(event)) {
            return;
        }

        FVTreeModel deviceTypesTreeModel = getCurrentTreeModel();
        if (deviceTypesTreeModel == null) {
            return;
        }

        List<TreePath> paths = new ArrayList<TreePath>();
        for (int i = 0; i < event.numberOfNodes(); i++) {
            NodeType nodeType = event.getType(i);
            TreeNodeType treeNodeType = null;
            boolean nodeTypeValid = true;
            switch (nodeType) {
                case HFI: {
                    treeNodeType = TreeNodeType.HFI;
                    break;
                }
                case SWITCH: {
                    treeNodeType = TreeNodeType.SWITCH;
                    break;
                }
                case ROUTER: {
                    treeNodeType = TreeNodeType.ROUTER;
                    break;
                }
                default:
                    nodeTypeValid = false;
                    break;
            }
            if (nodeTypeValid) {
                TreePath path =
                        deviceTypesTreeModel.getTreePath(event.getLid(i),
                                treeNodeType, getCurrentNode());
                paths.add(path);
            }
        }
        if (!paths.isEmpty()) {
            isSystemUpdate = true;
            view.expandAndSelectTreePath(deviceTypesTreeModel,
                    paths.toArray(new TreePath[0]), new boolean[paths.size()]);
        }
    }

    @Handler
    protected void onPortsSelected(PortsSelectedEvent event) {
        if (!acceptEvent(event)) {
            return;
        }

        FVTreeModel deviceTypesTreeModel = getCurrentTreeModel();
        if (deviceTypesTreeModel == null) {
            return;
        }

        TreePath[] paths = new TreePath[event.numberOfPorts()];
        for (int i = 0; i < paths.length; i++) {
            paths[i] =
                    deviceTypesTreeModel.getTreePathForPort(event.getLid(i),
                            event.getPortNum(i), getCurrentNode());
        }

        isSystemUpdate = true;
        view.expandAndSelectTreePath(deviceTypesTreeModel, paths,
                new boolean[paths.length]);
    }

    public abstract String getName();

    protected boolean acceptEvent(JumpToEvent event) {
        return event.getDestination().equals(getName());
    }

}
