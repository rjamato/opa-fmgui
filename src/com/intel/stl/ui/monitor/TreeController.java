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
 *  Archive Log:    Revision 1.23.2.1  2015/08/12 15:26:58  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
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

import java.util.HashMap;
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
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.event.JumpToEvent;
import com.intel.stl.ui.event.NodeSelectedEvent;
import com.intel.stl.ui.event.NodeUpdateEvent;
import com.intel.stl.ui.event.PortSelectedEvent;
import com.intel.stl.ui.event.PortUpdateEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.monitor.tree.FVTreeManager;
import com.intel.stl.ui.monitor.tree.FVTreeModel;
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
    public void setContext(Context pContext, IProgressObserver observer) {
        mContext = pContext;
        buildTrees(observer);
        view.clear();
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
        view.setTreeModel(type, treeModel);
        treeModels.put(type, treeModel);
        mTreeBuilder.addMonitor(type, treeModel);
    }

    public void updateTrees(IProgressObserver observer) {
        IProgressObserver[] subObservers = observer.createSubObservers(4);
        mTreeBuilder
                .updateTree(TreeTypeEnum.DEVICE_TYPES_TREE, subObservers[0]);
        subObservers[0].onFinish();
        mTreeBuilder.updateTree(TreeTypeEnum.DEVICE_GROUPS_TREE,
                subObservers[1]);
        subObservers[1].onFinish();
        mTreeBuilder.updateTree(TreeTypeEnum.VIRTUAL_FABRICS_TREE,
                subObservers[2]);
        subObservers[2].onFinish();
        // mTreeBuilder.updateTree(TreeTypeEnum.TOP_10_CONGESTED_TREE,
        // subObservers[3]);
        subObservers[3].onFinish();
    }

    public void updateTreeNode(int lid) {
        mTreeBuilder.updateTreeNode(lid, TreeTypeEnum.DEVICE_TYPES_TREE);
        mTreeBuilder.updateTreeNode(lid, TreeTypeEnum.DEVICE_GROUPS_TREE);
        mTreeBuilder.updateTreeNode(lid, TreeTypeEnum.VIRTUAL_FABRICS_TREE);
        // mTreeBuilder.updateTreeNode(lid, TreeTypeEnum.TOP_10_CONGESTED_TREE);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        JTree tree = (JTree) e.getSource();
        currentTreeModel = (FVTreeModel) tree.getModel();

        if (tree.getSelectionCount() == 1) {
            Object node = tree.getLastSelectedPathComponent();
            if (node != null && (node instanceof FVResourceNode)) {
                showNode((FVResourceNode) node);
            }
        } else if (tree.getSelectionCount() > 1) {
            TreePath[] paths = tree.getSelectionPaths();
            FVResourceNode[] nodes = new FVResourceNode[paths.length];
            for (int i = 0; i < nodes.length; i++) {
                Object node = paths[i].getLastPathComponent();
                if (node != null && (node instanceof FVResourceNode)) {
                    nodes[i] = (FVResourceNode) node;
                }
            }
            showNodes(nodes);
        }
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

    protected abstract JumpDestination getDesiredDestination();

    @Handler
    protected void onNodeSelected(NodeSelectedEvent event) {
        if (!acceptEvent(event)) {
            return;
        }

        FVTreeModel deviceTypesTreeModel =
                getTreeModel(TreeTypeEnum.DEVICE_TYPES_TREE);
        if (deviceTypesTreeModel == null) {
            return;
        }

        NodeType nodeType = event.getNodeType();
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
            TreePath selectedPath =
                    deviceTypesTreeModel.getTreePath(event.getLid(),
                            treeNodeType, null);
            view.expandAndSelectTreePath(deviceTypesTreeModel, selectedPath);
        }
    }

    @Handler
    protected void onPortSelected(PortSelectedEvent event) {
        if (!acceptEvent(event)) {
            return;
        }

        FVTreeModel deviceTypesTreeModel =
                getTreeModel(TreeTypeEnum.DEVICE_TYPES_TREE);
        if (deviceTypesTreeModel == null) {
            return;
        }

        TreePath selectedPath =
                deviceTypesTreeModel.getTreePathForPort(event.getNodeLid(),
                        event.getPortNumber(), null);
        view.expandAndSelectTreePath(deviceTypesTreeModel, selectedPath);
    }

    protected boolean acceptEvent(JumpToEvent event) {
        return event.getDestination() == getDesiredDestination();
    }

}
