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
 *  File Name: PerformanceTreeController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.43  2015/04/24 16:59:19  jypak
 *  Archive Log:    Fix for an issue with selecting an inactive port and then selecting an active port.
 *  Archive Log:
 *  Archive Log:    Revision 1.42  2015/04/03 21:06:27  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.41  2015/02/24 14:23:20  jypak
 *  Archive Log:    1. Show Border, Alternating Rows control panel added to the PerformanceErrorsSection.
 *  Archive Log:    2. Undo change of Performance Chart Section title to "Performancefor port Performance subpage.
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2015/02/05 21:21:45  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2014/10/22 02:16:55  jijunwan
 *  Archive Log:    adapt to changed on property model, view and controller
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2014/10/21 16:38:29  fernande
 *  Archive Log:    Customization of Properties display (Show Options/Apply Options)
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2014/10/09 21:24:49  jijunwan
 *  Archive Log:    improvement on TreeNodeType:
 *  Archive Log:    1) Added icon to TreeNodeType
 *  Archive Log:    2) Rename PORT to ACTIVE_PORT
 *  Archive Log:    3) Removed NODE
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2014/10/09 12:35:09  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2014/09/18 21:03:28  jijunwan
 *  Archive Log:    Added link (jump to) capability to Connectivity tables and PortSummary table
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2014/09/18 14:57:38  jijunwan
 *  Archive Log:    supported jumpTo events
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2014/09/15 15:24:32  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2014/09/02 19:24:29  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2014/08/26 15:15:27  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2014/08/12 21:06:52  jijunwan
 *  Archive Log:    add null check
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2014/08/05 18:39:02  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2014/08/05 13:36:42  jijunwan
 *  Archive Log:    fixed typo isCanceled->isCanelled, added cancel interface
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2014/07/25 20:37:58  fernande
 *  Archive Log:    Changed to use new Property pages
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2014/07/21 15:41:34  jijunwan
 *  Archive Log:    minor performance improvement on tree building
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2014/07/11 19:26:54  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2014/07/03 22:21:24  jijunwan
 *  Archive Log:    extended TreeController and TreeView to support multi-selection and programmly operate a tree
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2014/06/27 22:22:22  jijunwan
 *  Archive Log:    added running indicator to Performance Subpages
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2014/06/26 15:49:10  jijunwan
 *  Archive Log:    performance improvement - share tree model among pages so we do not build model several times
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/06/26 15:00:15  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/06/24 20:22:01  rjtierne
 *  Archive Log:    Changed HCA to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/06/12 21:38:23  rjtierne
 *  Archive Log:    Replaced all references to StatisticsSubpage with ConnectivitySubpage
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/06/05 17:34:58  jijunwan
 *  Archive Log:    integrate vFabric into performance pages
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/05/29 14:30:46  jijunwan
 *  Archive Log:    fixed a control logic issue. When a main page set context, it will update its content that causes its subpages to query data. So subpages should get context before the main page set its context.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/05/28 22:21:58  jijunwan
 *  Archive Log:    added port preview to performance subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/05/23 19:27:58  rjtierne
 *  Archive Log:    Calling clear() on all subpages to deregister running tasks
 *  Archive Log:    before switching between subpages.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/05/20 21:26:48  jijunwan
 *  Archive Log:    added events chart to performane subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/05/18 22:43:35  rjtierne
 *  Archive Log:    No longer using individual device groups to add summary page.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/05/16 04:30:38  jijunwan
 *  Archive Log:    Added code to deregister from task scheduler; Added Page Listener to listen enter or exit a (sub)page
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/05/15 18:27:50  rjtierne
 *  Archive Log:    Added summary page for new device group nodes.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/05/15 14:33:15  jijunwan
 *  Archive Log:    minor change on tree controller to support generic
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/05/09 21:00:43  jijunwan
 *  Archive Log:    added property; fixed remembering last subpage issue; fixed position problem on IntelTabbedPane
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/05/09 20:23:17  rjtierne
 *  Archive Log:    Redefined the node types that permit the summary subpage
 *  Archive Log:    to be displayed
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/05/09 19:13:29  rjtierne
 *  Archive Log:    Renamed PerfSummarySubpageController to PSSubpageController
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/06 20:45:16  rjtierne
 *  Archive Log:    Renamed performance summary subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/02 16:34:54  rjtierne
 *  Archive Log:    Changed showNode() to call clearPage() if
 *  Archive Log:    subpages is null
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/02 14:27:19  rjtierne
 *  Archive Log:    Added placeholder for Property subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/01 17:19:58  rjtierne
 *  Archive Log:    In method showNode(), now using local variable
 *  Archive Log:    subpages to retrieve them from the map to ensure proper
 *  Archive Log:    context initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/01 16:32:58  rjtierne
 *  Archive Log:    Added Intel and CVS headers
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import javax.swing.tree.TreeSelectionModel;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.FinishObserver;
import com.intel.stl.ui.common.IPerfSubpageController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.configuration.DevicePropertiesController;
import com.intel.stl.ui.configuration.view.DevicePropertiesPanel;
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.view.IPageListener;
import com.intel.stl.ui.model.ConnectivityTableModel;
import com.intel.stl.ui.model.DeviceProperties;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.monitor.tree.FVTreeManager;
import com.intel.stl.ui.monitor.view.ConnectivitySubpageView;
import com.intel.stl.ui.monitor.view.PerformanceSubpageView;
import com.intel.stl.ui.monitor.view.PerformanceTreeView;
import com.intel.stl.ui.monitor.view.SummarySubpageView;

public class PerformanceTreeController extends
        TreeController<PerformanceTreeView> implements IPageListener {

    /**
     * Subpages for the performance page
     */
    private List<IPerfSubpageController> mSubpages;

    /**
     * name of the currently selected subpage
     */
    private String currentSubageName;

    private IPerfSubpageController currentSubpage;

    /**
     * Map of subpages
     */
    private EnumMap<TreeNodeType, List<IPerfSubpageController>> pageMap;

    private FVResourceNode lastNode;

    public PerformanceTreeController(PerformanceTreeView pTreeView,
            MBassador<IAppEvent> eventBus, FVTreeManager treeBuilder) {
        super(pTreeView, eventBus, treeBuilder);
        view.setPageListener(this);
        view.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        initSubpages();
    }

    @Override
    protected synchronized void showNode(final FVResourceNode node) {
        if (node == null) {
            return;
        }

        if (lastNode != null && lastNode.equals(node)) {
            // refresh the subpage
            setRunning(true);
            currentSubpage.showNode(node, new FinishObserver() {
                @Override
                public void onFinish() {
                    setRunning(false);
                }
            });
            view.setNodeName(node);
            return;
        }

        view.setNodeName(node);
        setRunning(true);

        // Deregister tasks on all subpages
        for (IPerfSubpageController page : mSubpages) {
            page.clear();
        }

        List<IPerfSubpageController> subpages =
                getSubpagesByType(node.getType());
        if (subpages == null) {
            view.clearPage(node.getType());
            setRunning(false);
            lastNode = null;
        } else {
            String current = view.getCurrentSubpage();
            if (current != null) {
                currentSubageName = current;
            }
            int curIndex = -1;
            IPerfSubpageController subpage = null;
            for (int i = 0; i < subpages.size(); i++) {
                subpage = subpages.get(i);
                if (subpage.getName() == currentSubageName) {
                    curIndex = i;
                    break;
                }
            }
            if (curIndex == -1) {
                curIndex = 0;
                subpage = subpages.get(0);
                currentSubageName = subpage.getName();
            }
            subpage.showNode(node, new FinishObserver() {
                @Override
                public void onFinish() {
                    setRunning(false);
                }
            });
            lastNode = node;
            currentSubpage = subpage;
            view.setTabs(subpages, curIndex);
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
    protected void showNodes(FVResourceNode[] node) {
        throw new UnsupportedOperationException();
    }

    protected void initSubpages() {
        // Create thew views
        PerformanceSubpageView perfSubpageView = new PerformanceSubpageView();
        SummarySubpageView summarySubpageView = new SummarySubpageView();
        ConnectivityTableModel connectTableModel = new ConnectivityTableModel();
        ConnectivitySubpageView connectSubpageView =
                new ConnectivitySubpageView(connectTableModel);
        DevicePropertiesPanel propSubpageView = new DevicePropertiesPanel();

        // Create subpages
        IPerfSubpageController summarySP =
                new PSSubpageController(summarySubpageView, eventBus);
        summarySP.setParentController(this);
        IPerfSubpageController performanceSP =
                new PerformanceSubpageController(perfSubpageView, eventBus);
        performanceSP.setParentController(this);
        IPerfSubpageController connectSP =
                new ConnectivitySubpageController(connectTableModel,
                        connectSubpageView, eventBus);
        connectSP.setParentController(this);
        DeviceProperties props = new DeviceProperties();
        IPerfSubpageController propertySP =
                new DevicePropertiesController(props, propSubpageView, eventBus);
        propertySP.setParentController(this);
        mSubpages =
                Arrays.asList(summarySP, performanceSP, connectSP, propertySP);

        // Init TreeNodeType and associated sub-pages
        pageMap =
                new EnumMap<TreeNodeType, List<IPerfSubpageController>>(
                        TreeNodeType.class);
        pageMap.put(TreeNodeType.ALL, Arrays.asList(summarySP));
        pageMap.put(TreeNodeType.INACTIVE_PORT, null);
        pageMap.put(TreeNodeType.DEVICE_GROUP, Arrays.asList(summarySP));
        pageMap.put(TreeNodeType.HCA_GROUP, Arrays.asList(summarySP));
        pageMap.put(TreeNodeType.ROUTER_GROUP, Arrays.asList(summarySP));
        pageMap.put(TreeNodeType.SWITCH_GROUP, Arrays.asList(summarySP));
        pageMap.put(TreeNodeType.VIRTUAL_FABRIC, Arrays.asList(summarySP));
        pageMap.put(TreeNodeType.HFI,
                Arrays.asList(performanceSP, connectSP, propertySP));
        pageMap.put(TreeNodeType.SWITCH,
                Arrays.asList(performanceSP, connectSP, propertySP));
        pageMap.put(TreeNodeType.ACTIVE_PORT,
                Arrays.asList(performanceSP, connectSP, propertySP));

        // for demo purpose
        // pageMap.put(TreeNodeType.HCA, Arrays.asList(performanceSP,
        // propertySP));
        // pageMap.put(TreeNodeType.SWITCH, Arrays.asList(performanceSP,
        // propertySP));
        // pageMap.put(TreeNodeType.PORT, Arrays.asList(performanceSP,
        // propertySP));
    }

    @Override
    public void setContext(Context pContext, IProgressObserver observer) {
        IProgressObserver[] subObservers =
                observer.createSubObservers(mSubpages.size() + 1);
        for (int i = 0; i < mSubpages.size(); i++) {
            // subObservers[i].setNote(mSubpages.get(i).getName());
            mSubpages.get(i).setContext(pContext, subObservers[i]);
            subObservers[i].onFinish();
            // observer.setProgress((i+1.0)/(mSubpages.size()+1));
            if (observer.isCancelled()) {
                for (int j = 0; j <= i; j++) {
                    mSubpages.get(j).clear();
                }
                return;
            }
        }
        // subObservers[mSubpages.size()].setNote("TREE");
        super.setContext(pContext, subObservers[mSubpages.size()]);
        // clean subpages if we cancel after they are initialized
        if (observer.isCancelled()) {
            for (int i = 0; i < mSubpages.size(); i++) {
                for (int j = 0; j <= i; j++) {
                    mSubpages.get(j).clear();
                }
            }
        }
    }

    public void setRunning(boolean isRunning) {
        view.setRunning(isRunning);
    }

    protected List<IPerfSubpageController> getSubpagesByType(TreeNodeType type) {
        return pageMap.get(type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IPageListener#canPageChange(int, int)
     */
    @Override
    public boolean canPageChange(int oldPageId, int newPageId) {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IPageListener#onPageChanged(int, int)
     */
    @Override
    public synchronized void onPageChanged(int oldPageId, int newPageId) {
        List<IPerfSubpageController> subpages =
                getSubpagesByType(lastNode.getType());
        if (subpages == null) {
            // shouldn't happen
            throw new RuntimeException("No pages found for last node "
                    + lastNode);
        }

        IPerfSubpageController oldPage = subpages.get(oldPageId);
        if (oldPage != null) {
            oldPage.onExit();
        }

        IPerfSubpageController newPage = subpages.get(newPageId);
        if (newPage != null && currentSubageName != newPage.getName()) {
            setRunning(true);
            newPage.onEnter();
            newPage.showNode(lastNode, new FinishObserver() {
                @Override
                public void onFinish() {
                    setRunning(false);
                }
            });
            currentSubageName = newPage.getName();
            currentSubpage = newPage;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.monitor.TreeController#getDesiredDestination()
     */
    @Override
    protected JumpDestination getDesiredDestination() {
        return JumpDestination.PERFORMANCE;
    }

}
