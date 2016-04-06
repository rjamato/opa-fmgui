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
 *  File Name: ResourceDetailController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.13  2016/03/01 17:11:04  jijunwan
 *  Archive Log:    PR 133064 - externally managed switch node description not updating
 *  Archive Log:    - changed to always reset title even if we are showing the same node
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/08/17 18:54:00  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/08/05 04:09:31  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - applied undo mechanism on Topology Page
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/07/17 15:42:40  rjtierne
 *  Archive Log:    PR 129549 - On connectivity table, clicking on cable info for an HFI results in an error
 *  Archive Log:    In showNode(), removed call to setLastNode() in cableInfoPopupController - no longer used
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/07/13 21:58:15  rjtierne
 *  Archive Log:    PR 129355 - Ability to click on cables to get cable info
 *  Archive Log:    Now using the CableInfoPopupController as the port selection listener
 *  Archive Log:    for the ConnectivitySubpageView; enabling the cable info functionality
 *  Archive Log:    to be available when the cable info column of the connectivity table
 *  Archive Log:    is selected for a node.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/06/09 18:37:25  jijunwan
 *  Archive Log:    PR 129069 - Incorrect Help action
 *  Archive Log:    - moved help action from view to controller
 *  Archive Log:    - only enable help button when we have HelpID
 *  Archive Log:    - fixed incorrect HelpIDs
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/06/01 15:01:21  jypak
 *  Archive Log:    PR 128823 - Improve performance tables to include all portcounters fields.
 *  Archive Log:    All port counters fields added to performance table and connectivity table.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/05/12 17:42:31  rjtierne
 *  Archive Log:    PR 128624 - Klocwork and FindBugs fixes for UI
 *  Archive Log:    In showNode(), use equals() to compare strings subpage.getName() and currentSubpageName.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/04/08 19:44:34  rjtierne
 *  Archive Log:    Removed SYMBOL_ERRORS, TX_32BIT_WORDS, RX_32BIT_WORDS, and VL_15_DROPPED
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/07 18:08:19  jypak
 *  Archive Log:    Online Help updates for additional panels.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/04 21:44:19  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/11/05 16:20:21  jijunwan
 *  Archive Log:    only re-initialize when we have changes
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/23 16:00:04  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/09 21:27:30  jijunwan
 *  Archive Log:    minor changes
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/09 12:37:03  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/18 21:36:49  jijunwan
 *  Archive Log:    fixed a issue that incorrectly use portNum for rowIndex
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/18 21:03:25  jijunwan
 *  Archive Log:    Added link (jump to) capability to Connectivity tables and PortSummary table
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/15 15:24:29  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/26 15:15:19  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/08/05 13:36:45  jijunwan
 *  Archive Log:    fixed typo isCanceled->isCanelled, added cancel interface
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/23 18:02:46  rjtierne
 *  Archive Log:    Created separate Node views for Switch and HFI to
 *  Archive Log:    accommodate different panels
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/18 14:00:20  rjtierne
 *  Archive Log:    Removed the Link page when a Switch or FI is selected from the
 *  Archive Log:    topology graph
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/18 13:41:52  rjtierne
 *  Archive Log:    Added content to method showPath() to display a Connectivity
 *  Archive Log:    table listing the node/port information for each node along the path
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/11 19:29:01  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/10 21:24:55  rjtierne
 *  Archive Log:    Calling showPath() on all applicable subpages
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/10 14:38:32  rjtierne
 *  Archive Log:    Added connectivity table to the Link subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/08 20:12:56  rjtierne
 *  Archive Log:    Renamed from ResourceDetailsCard and now controlling the topology subpage card
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/03 14:11:44  rjtierne
 *  Archive Log:    Added observer. Added Connectivity table to topology Port page with column-hiding
 *  Archive Log:    tailored to this view
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/26 20:22:51  rjtierne
 *  Archive Log:    Made subpages a class attribute to make accessible for setting context
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/25 13:44:13  rjtierne
 *  Archive Log:    Changed Route page references to Port
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/24 20:30:31  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Controller for the subpages card on the Topology page
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.network;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.ICardController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.UndoableJumpEvent;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.configuration.view.DevicePropertiesPanel;
import com.intel.stl.ui.event.NodesSelectedEvent;
import com.intel.stl.ui.event.PortsSelectedEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.main.UndoHandler;
import com.intel.stl.ui.main.view.IPageListener;
import com.intel.stl.ui.model.ConnectivityTableColumns;
import com.intel.stl.ui.model.ConnectivityTableModel;
import com.intel.stl.ui.model.DeviceProperties;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.monitor.CableInfoPopupController;
import com.intel.stl.ui.monitor.IPortSelectionListener;
import com.intel.stl.ui.monitor.TreeNodeType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.monitor.view.CableInfoPopupView;
import com.intel.stl.ui.monitor.view.ConnectivitySubpageView;
import com.intel.stl.ui.network.view.ResourcePortView;
import com.intel.stl.ui.network.view.ResourceSubpageView;

import net.engio.mbassy.bus.MBassador;

public class ResourceNodeSection extends ResourceSection<ResourceSubpageView>
        implements IPortSelectionListener, IPageListener {

    /**
     * Subpages for the Topology page
     */
    private List<IResourceNodeSubpageController> mSubpages;

    private GraphNode lastNode;

    /**
     * Map of subpages
     */
    private EnumMap<TreeNodeType, List<IResourceNodeSubpageController>> pageMap;

    private final DeviceProperties model;

    private CableInfoPopupController cableInfoPopupController;

    private String previousSubpageName;

    private String currentSubpageName;

    private UndoHandler undoHandler;

    private final String origin = TopologyPage.NAME;

    /**
     * Description:
     *
     * @param view
     */
    public ResourceNodeSection(ResourceSubpageView view,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        this.view = view;
        view.setPageListener(this);
        model = new DeviceProperties();
        initSubpages();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.common.BaseSectionController#getHelpID()
     */
    @Override
    public String getHelpID() {
        return HelpAction.getInstance().getTopologyNode();
    }

    @Override
    public void setContext(Context pContext, IProgressObserver observer) {
        cableInfoPopupController.setContext(pContext, observer);
        if (pContext != null && pContext.getController() != null) {
            undoHandler = pContext.getController().getUndoHandler();
        }

        IProgressObserver[] subObservers =
                observer.createSubObservers(mSubpages.size());
        for (int i = 0; i < mSubpages.size(); i++) {
            mSubpages.get(i).setContext(pContext, subObservers[i]);
            subObservers[i].onFinish();
            if (observer.isCancelled()) {
                for (int j = 0; j <= i; j++) {
                    mSubpages.get(j).clear();
                }
                return;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.intel.stl.ui.network.ResourceSection#setCurrentSubpage(java.lang.
     * String)
     */
    @Override
    public void setCurrentSubpage(String subpageName) {
        previousSubpageName = currentSubpageName;
        currentSubpageName = subpageName;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.network.ResourceSection#getPreviousSubpage()
     */
    @Override
    public String getPreviousSubpage() {
        return previousSubpageName;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.network.ResourceSection#getCurrentSubpage()
     */
    @Override
    public String getCurrentSubpage() {
        return currentSubpageName;
    }

    protected void showNode(FVResourceNode source, GraphNode node) {
        if (source == null) {
            return;
        }

        // Set the card title to the name of the node
        view.setTitle(source.getName(), source.getType().getIcon());

        if (lastNode != null && lastNode.equals(node)) {
            IResourceNodeSubpageController currentSubpage =
                    getCurrentSubpage(source.getType());
            if (currentSubpage != null) {
                currentSubpage.showNode(source, node);
            }
            return;
        }

        lastNode = node;
        // connectivitySubpageController.setLastNode(source);

        // Deregister tasks on all subpages
        for (IResourceNodeSubpageController page : mSubpages) {
            page.clear();
        }

        List<IResourceNodeSubpageController> subpages =
                getSubpagesByType(source.getType());
        if (subpages != null) {
            if (currentSubpageName == null) {
                previousSubpageName = null;
                currentSubpageName = view.getCurrentSubpage();
            }
            int curIndex = -1;
            for (int i = 0; i < subpages.size(); i++) {
                IResourceNodeSubpageController subpage = subpages.get(i);
                if (subpage.getName().equals(currentSubpageName)) {
                    curIndex = i;
                }
                subpage.showNode(source, node);
            }
            view.setTabs(subpages, curIndex);
        }
    }

    protected IResourceNodeSubpageController getCurrentSubpage(
            TreeNodeType type) {
        List<IResourceNodeSubpageController> subpages = getSubpagesByType(type);
        if (subpages != null) {
            String current = view.getCurrentSubpage();
            if (current != null) {
                for (int i = 0; i < subpages.size(); i++) {
                    IResourceNodeSubpageController subpage = subpages.get(i);
                    if (current.equals(subpage.getName())) {
                        return subpage;
                    }
                }
            } else {
                return subpages.get(0);
            }
        }
        return null;
    }

    protected void initSubpages() {

        // Table Initialization
        ConnectivityTableModel portTableModel = new ConnectivityTableModel();
        ConnectivitySubpageView portTableView = createPortView(portTableModel);
        CableInfoPopupView cableInfoPopupView =
                new CableInfoPopupView(portTableView);
        portTableView.setCableInfoPopupView(cableInfoPopupView);
        cableInfoPopupController =
                new CableInfoPopupController(cableInfoPopupView);
        cableInfoPopupView.setCableInfoListener(cableInfoPopupController);
        // connectivitySubpageController =
        // new ConnectivitySubpageController(portTableModel,
        // portTableView, eventBus);
        // portTableView.setPortSelectionListener(connectivitySubpageController);
        portTableView.setPortSelectionListener(this);

        // Create the views
        DevicePropertiesPanel switchNodeView = new DevicePropertiesPanel();
        DevicePropertiesPanel hfiNodeView = new DevicePropertiesPanel();
        ResourcePortView portView = new ResourcePortView();

        // Create the subpages
        IResourceNodeSubpageController switchNodePage =
                new ResourceNodePage(model, switchNodeView, eventBus);
        IResourceNodeSubpageController hfiNodePage =
                new ResourceNodePage(model, hfiNodeView, eventBus);
        IResourceNodeSubpageController portPage =
                new ResourcePortPage(portTableModel, portTableView, portView);
        mSubpages = Arrays.asList(hfiNodePage, switchNodePage, portPage);

        // Init TopologyComponentType and associated sub-pages
        pageMap =
                new EnumMap<TreeNodeType, List<IResourceNodeSubpageController>>(
                        TreeNodeType.class);
        pageMap.put(TreeNodeType.SWITCH,
                Arrays.asList(switchNodePage, portPage));
        pageMap.put(TreeNodeType.HFI, Arrays.asList(hfiNodePage, portPage));
    }

    protected List<IResourceNodeSubpageController> getSubpagesByType(
            TreeNodeType type) {
        return pageMap.get(type);
    }

    @Override
    public boolean canPageChange(String oldPage, String newPage) {
        return true;
    }

    @Override
    public synchronized void onPageChanged(String oldPage, String newPage) {
        if (undoHandler != null && !undoHandler.isInProgress()) {
            UndoableNodeSubpageSelection undoSel =
                    new UndoableNodeSubpageSelection(view, oldPage, newPage);
            undoHandler.addUndoAction(undoSel);
        }
        previousSubpageName = oldPage;
        currentSubpageName = newPage;
    }

    protected ConnectivitySubpageView createPortView(
            ConnectivityTableModel portTableModel) {

        ConnectivitySubpageView portTableView =
                new ConnectivitySubpageView(portTableModel) {

                    private static final long serialVersionUID =
                            5930204470646720711L;

                    @Override
                    protected void filterColumns() {
                        ConnectivityTableColumns[] toShow =
                                new ConnectivityTableColumns[] {

                                        // Show these columns
                                        ConnectivityTableColumns.NODE_NAME,
                                        ConnectivityTableColumns.PORT_NUMBER,
                                        ConnectivityTableColumns.CABLE_INFO,
                                        ConnectivityTableColumns.LINK_STATE,
                                        ConnectivityTableColumns.PHYSICAL_LINK_STATE,
                                        ConnectivityTableColumns.LINK_QUALITY,
                                        ConnectivityTableColumns.ACTIVE_LINK_WIDTH,
                                        ConnectivityTableColumns.ACTIVE_LINK_WIDTH_DG_TX,
                                        ConnectivityTableColumns.ACTIVE_LINK_WIDTH_DG_RX,
                                        ConnectivityTableColumns.ACTIVE_LINK_SPEED,
                                        ConnectivityTableColumns.RX_DATA,
                                        ConnectivityTableColumns.TX_DATA,
                                        ConnectivityTableColumns.LINK_DOWNED, };

                        ConnectivityTableColumns[] all =
                                ConnectivityTableColumns.values();
                        boolean[] vis = new boolean[all.length];
                        for (ConnectivityTableColumns col : toShow) {
                            vis[col.getId()] = true;
                        }
                        for (int i = 0; i < vis.length; i++) {
                            mTable.getColumnExt(all[i].getTitle())
                                    .setVisible(vis[i]);
                        }
                    }
                };
        return portTableView;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.common.BaseSectionController#getSectionListener()
     */
    @Override
    protected ISectionListener getSectionListener() {
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.monitor.IPortSelectionListener#onPortSelection(int)
     */
    @Override
    public void onPortSelection(int rowIndex) {
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.monitor.IPortSelectionListener#onJumpToPort(int,
     * short, java.lang.String)
     */
    @Override
    public void onJumpToPort(int lid, short portNum, String destination) {
        if (eventBus != null) {
            PortsSelectedEvent pse =
                    new PortsSelectedEvent(lid, portNum, this, destination);
            eventBus.publish(pse);
            if (undoHandler != null && !undoHandler.isInProgress()) {
                NodeType type = NodeType.getNodeType(lastNode.getType());
                UndoableJumpEvent undoSel =
                        new UndoableJumpEvent(eventBus, new NodesSelectedEvent(
                                lastNode.getLid(), type, this, origin), pse);
                undoHandler.addUndoAction(undoSel);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.common.ISectionController#getCards()
     */
    @Override
    public ICardController<?>[] getCards() {
        return null;
    }
}
