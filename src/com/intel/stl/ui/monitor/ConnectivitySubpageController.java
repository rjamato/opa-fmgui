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
 *  File Name: ConnectivitySubpageController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.31  2015/11/02 23:56:53  jijunwan
 *  Archive Log:    PR 131396 - Incorrect Connectivity Table for a VF port
 *  Archive Log:    - adapted to the new connectivity table controller to support VF port
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/10/23 19:07:58  jijunwan
 *  Archive Log:    PR 129357 - Be able to hide inactive ports
 *  Archive Log:    - revert back to the old version without visible node support
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/09/30 13:26:50  fisherma
 *  Archive Log:    PR 129357 - ability to hide inactive ports.  Also fixes PR 129689 - Connectivity table exhibits inconsistent behavior on Performance and Topology pages
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/08/18 14:29:42  jijunwan
 *  Archive Log:    PR 130033 - Fix critical issues found by Klocwork or FindBugs
 *  Archive Log:    - fixed null pointer issue
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/08/17 18:53:41  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/08/05 04:04:47  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - applied undo mechanism on Performance Page
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/07/17 15:42:13  rjtierne
 *  Archive Log:    PR 129549 - On connectivity table, clicking on cable info for an HFI results in an error
 *  Archive Log:    In showNode(), removed call to setLastNode in cableInfoPopupController - no longer used
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/07/13 21:56:37  rjtierne
 *  Archive Log:    PR 129355 - Ability to click on cables to get cable info
 *  Archive Log:    Initialized the CableInfoPopup Controller and View
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/04/03 21:06:27  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/02/04 21:44:17  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/10/21 16:38:29  fernande
 *  Archive Log:    Customization of Properties display (Show Options/Apply Options)
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/10/09 21:24:49  jijunwan
 *  Archive Log:    improvement on TreeNodeType:
 *  Archive Log:    1) Added icon to TreeNodeType
 *  Archive Log:    2) Rename PORT to ACTIVE_PORT
 *  Archive Log:    3) Removed NODE
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/10/09 12:35:09  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/09/18 21:36:50  jijunwan
 *  Archive Log:    fixed a issue that incorrectly use portNum for rowIndex
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/09/18 21:03:28  jijunwan
 *  Archive Log:    Added link (jump to) capability to Connectivity tables and PortSummary table
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/09/02 19:24:29  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/08/26 15:15:27  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/08/05 17:57:05  jijunwan
 *  Archive Log:    fixed issues on ConnectivityTable to update performance data properly
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/07/29 15:46:04  rjtierne
 *  Archive Log:    Scheduled periodic Connectivity table updates
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/07/01 19:11:25  jijunwan
 *  Archive Log:    Had a separate ConnectivityTableControler, so we can reuse it
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/06/27 22:22:22  jijunwan
 *  Archive Log:    added running indicator to Performance Subpages
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/06/26 15:03:43  jijunwan
 *  Archive Log:    added inactive link back
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/06/25 20:31:22  fernande
 *  Archive Log:    Only active ports are now being queried.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/06/24 20:21:11  rjtierne
 *  Archive Log:    Changed HCA to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/24 18:50:27  jijunwan
 *  Archive Log:    improvement on connectivity subpage - applied SwingWorker to do data processing to background thread, table update on EDT
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/23 13:53:34  rjtierne
 *  Archive Log:    Tweaked slow link test to show port and neighbor
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/19 20:13:58  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/17 19:23:17  rjtierne
 *  Archive Log:    Added logic to render the Connectivity table entries with an icon when
 *  Archive Log:    links are running slow
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/13 18:55:45  rjtierne
 *  Archive Log:    Updated to support HFI ports and switch port 0
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/13 18:23:49  rjtierne
 *  Archive Log:    Updated Connectivity table to support HFI nodes and switch ports
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/12 21:35:43  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Controller for the Connectivity subpage
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor;

import static com.intel.stl.ui.common.PageWeight.MEDIUM;

import java.awt.Component;
import java.util.Vector;

import javax.swing.ImageIcon;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.IPerfSubpageController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UndoableJumpEvent;
import com.intel.stl.ui.event.JumpToEvent;
import com.intel.stl.ui.event.NodesSelectedEvent;
import com.intel.stl.ui.event.PortsSelectedEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.UndoHandler;
import com.intel.stl.ui.model.ConnectivityTableModel;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.monitor.view.CableInfoPopupView;
import com.intel.stl.ui.monitor.view.ConnectivitySubpageView;

public class ConnectivitySubpageController implements IPerfSubpageController,
        IPortSelectionListener {
    private UndoHandler undoHandler;

    private final String origin = PerformancePage.NAME;

    private final ConnectivityTableController tableController;

    private final ConnectivitySubpageView view;

    private final MBassador<IAppEvent> eventBus;

    private PerformanceTreeController parentController;

    private final CableInfoPopupController cableInfoPopupController;

    private final CableInfoPopupView cableInfoPopupView;

    public ConnectivitySubpageController(
            ConnectivityTableModel connectTableModel,
            ConnectivitySubpageView pSubpageView, MBassador<IAppEvent> eventBus) {

        tableController =
                new ConnectivityTableController(connectTableModel,
                        pSubpageView.getTable());
        cableInfoPopupView = new CableInfoPopupView(pSubpageView);
        pSubpageView.setCableInfoPopupView(cableInfoPopupView);
        cableInfoPopupController =
                new CableInfoPopupController(cableInfoPopupView);
        cableInfoPopupView.setCableInfoListener(cableInfoPopupController);
        view = pSubpageView;
        view.setPortSelectionListener(this);
        this.eventBus = eventBus;
    }

    @Override
    public void setContext(Context context, IProgressObserver observer) {
        if (context != null) {
            tableController.setContext(context, observer);
            cableInfoPopupController.setContext(context, observer);
            if (context.getController() != null) {
                undoHandler = context.getController().getUndoHandler();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.IPageController#onRefresh(com.intel.stl.ui.common
     * .IProgressObserver)
     */
    @Override
    public void onRefresh(IProgressObserver observer) {
        tableController.refreshConnectivity(observer);
    }

    @Override
    public String getName() {
        return STLConstants.K0415_CONNECTIVITY.getValue();
    }

    @Override
    public String getDescription() {
        return STLConstants.K0416_CONNECTIVITY_DESCRIPTION.getValue();
    }

    @Override
    public Component getView() {
        return view;
    }

    @Override
    public ImageIcon getIcon() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub
    }

    @Override
    public void showNode(FVResourceNode node, IProgressObserver observer) {
        switch (node.getType()) {
            case SWITCH:
                processSwitch(node, observer);
                break;

            case HFI:
                processHFI(node, observer);
                break;

            case ACTIVE_PORT:
                FVResourceNode parent = node.getParent();
                if (parent.getType() == TreeNodeType.SWITCH
                        && Byte.decode(node.getName()) == 0) {
                    processSwitch(parent, observer);
                } else {
                    String vfName = null;
                    FVResourceNode group = parent.getParent();
                    if (group.getType() == TreeNodeType.VIRTUAL_FABRIC) {
                        vfName = group.getTitle();
                    }
                    tableController.showConnectivity(node.getParent().getId(),
                            vfName, observer, (short) node.getId());
                }
                break;

            case INACTIVE_PORT:
                break;

            default:
                break;
        } // switch
    }

    @Override
    public void setParentController(PerformanceTreeController parentController) {
        this.parentController = parentController;
    }

    protected void processSwitch(FVResourceNode node, IProgressObserver observer) {
        Vector<FVResourceNode> children = node.getChildren();
        if (children.size() > 1) {
            String vfName = null;
            FVResourceNode group = node.getParent();
            if (group.getType() == TreeNodeType.VIRTUAL_FABRIC) {
                vfName = group.getTitle();
            }
            short[] ports = new short[children.size() - 1];
            for (int i = 1; i < children.size(); i++) {
                ports[i - 1] = (short) children.get(i).getId();
            }
            tableController.showConnectivity(node.getId(), vfName, observer,
                    ports);
        } else {
            observer.onFinish();
        }
    }

    protected void processHFI(FVResourceNode node, IProgressObserver observer) {
        Vector<FVResourceNode> children = node.getChildren();
        if (children.size() > 0) {
            String vfName = null;
            FVResourceNode group = node.getParent();
            if (group.getType() == TreeNodeType.VIRTUAL_FABRIC) {
                vfName = group.getTitle();
            }
            short[] ports = new short[children.size()];
            for (int i = 0; i < children.size(); i++) {
                // set local port number
                ports[i] = (short) children.get(i).getId();
            }
            tableController.showConnectivity(node.getId(), vfName, observer,
                    ports);
        } else {
            observer.onFinish();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onEnter()
     */
    @Override
    public void onEnter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onExit()
     */
    @Override
    public void onExit() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#canExit()
     */
    @Override
    public boolean canExit() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#clear()
     */
    @Override
    public void clear() {
        tableController.clear();
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
     * java.lang.String)
     */
    @Override
    public void onJumpToPort(int lid, short portNum, String destination) {
        if (eventBus != null) {
            PortsSelectedEvent pse =
                    new PortsSelectedEvent(lid, portNum, this, destination);

            if (undoHandler != null && !undoHandler.isInProgress()) {
                JumpToEvent oldSel = null;
                FVResourceNode node = parentController.getCurrentNode();
                if (node.isNode()) {
                    NodeType type = TreeNodeType.getNodeType(node.getType());
                    oldSel =
                            new NodesSelectedEvent(node.getId(), type, this,
                                    origin);
                } else if (node.isPort()) {
                    oldSel =
                            new PortsSelectedEvent(node.getParent().getId(),
                                    (short) node.getId(), this, origin);
                } else {
                    // shouldn't happen
                    throw new RuntimeException("Unsupported node " + node);
                }
                UndoableJumpEvent undoSel =
                        new UndoableJumpEvent(eventBus, oldSel, pse);
                undoHandler.addUndoAction(undoSel);
            }

            eventBus.publish(pse);
        }
    }

    @Override
    public PageWeight getContextSwitchWeight() {
        return MEDIUM;
    }

    @Override
    public PageWeight getRefreshWeight() {
        return MEDIUM;
    }
}
