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
 *  File Name: ResourceLinkSubpageCard.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.17  2016/02/16 22:16:07  jijunwan
 *  Archive Log:    PR 132888 - Include Num Lanes Down in port counters display
 *  Archive Log:
 *  Archive Log:    - added Num Lanes Down
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/11/02 23:56:52  jijunwan
 *  Archive Log:    PR 131396 - Incorrect Connectivity Table for a VF port
 *  Archive Log:    - adapted to the new connectivity table controller to support VF port
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/08/18 14:36:09  jijunwan
 *  Archive Log:    PR 130033 - Fix critical issues found by Klocwork or FindBugs
 *  Archive Log:    - clean dead code
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/08/17 18:54:00  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/08/05 04:10:48  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - applied undo mechanism
 *  Archive Log:    - fixed single link multiple ports issue to show each port pair as one link
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/07/22 15:40:41  jijunwan
 *  Archive Log:    PR 129355 - Ability to click on cables to get cable info
 *  Archive Log:    - fixed null pointer issue on route connectivity table
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/07/17 15:42:40  rjtierne
 *  Archive Log:    PR 129549 - On connectivity table, clicking on cable info for an HFI results in an error
 *  Archive Log:    In showNode(), removed call to setLastNode() in cableInfoPopupController - no longer used
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/07/13 21:58:05  rjtierne
 *  Archive Log:    PR 129355 - Ability to click on cables to get cable info
 *  Archive Log:    Methods showLinks() and showPath() now use the CableInfoPopupController,
 *  Archive Log:    therefore enabling the cable info functionality in the connectivity table
 *  Archive Log:    on the Topology page when a port (or link) is selected.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/06/09 18:37:25  jijunwan
 *  Archive Log:    PR 129069 - Incorrect Help action
 *  Archive Log:    - moved help action from view to controller
 *  Archive Log:    - only enable help button when we have HelpID
 *  Archive Log:    - fixed incorrect HelpIDs
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/06/01 15:01:21  jypak
 *  Archive Log:    PR 128823 - Improve performance tables to include all portcounters fields.
 *  Archive Log:    All port counters fields added to performance table and connectivity table.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/04/08 19:44:34  rjtierne
 *  Archive Log:    Removed SYMBOL_ERRORS, TX_32BIT_WORDS, RX_32BIT_WORDS, and VL_15_DROPPED
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/03/10 18:43:14  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/05 19:10:50  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/05 17:10:29  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/04 21:44:19  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/30 18:00:07  jijunwan
 *  Archive Log:    PR 126650 - For a Switch Device Set selection, Detailed Information Panel does not display "overall summary" and "topology summary"
 *  Archive Log:     - fixed control logic issues
 *  Archive Log:     - fixed a HFI local port number issue
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/11/05 16:37:14  jijunwan
 *  Archive Log:    renamed ResoureLinkCard to ResourceLinkSection since it a section now
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/23 16:00:04  jijunwan
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
 *  Archive Log:    Revision 1.13  2014/08/05 22:11:06  jijunwan
 *  Archive Log:    changed text Path to Route
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/08/05 17:59:45  jijunwan
 *  Archive Log:    ensure we update UI on EDT, changed to use SingleTaskManager to manager concurrent UI update tasks
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/08/05 13:46:23  jijunwan
 *  Archive Log:    new implementation on topology control that uses double models to avoid synchronization issues on model and view
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/07/23 18:35:27  rjtierne
 *  Archive Log:    Added addSubpage() method to add a subpage to the
 *  Archive Log:    list in the EDT. Invoked by both showLinks() and showPath()
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/07/23 18:28:02  rjtierne
 *  Archive Log:    Added clearSubpages() method to clear the subpages
 *  Archive Log:    list in the EDT. Invoked by both showLinks() and showPath()
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/23 18:02:22  rjtierne
 *  Archive Log:    Moved clearing of subpages in showPath() into the EDT
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/22 20:10:35  rjtierne
 *  Archive Log:    Rewrote showPath() method to display only one path per page
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/18 13:40:27  rjtierne
 *  Archive Log:    Added content to method showPath() to display a Connectivity
 *  Archive Log:    table listing the node/port information for each node along the path
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/11 19:29:01  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/11 13:16:28  jypak
 *  Archive Log:    Added runtime, non runtime exceptions handler for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    As of now, all different exceptions are generally handled as 'Exception' but when we define how to handle differently for different exception, based on the error code, handler (catch block will be different). Also, we are thinking of centralized 'failure recovery' process to handle all exceptions in one place.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/10 21:23:05  rjtierne
 *  Archive Log:    Stacked abbreviated node names on link subpage tabs
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/10 15:44:04  rjtierne
 *  Archive Log:    In method showLinks(), customized the name/description of the link subpages
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/10 14:29:15  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Controller for the JCardView to display tabbed pages when links
 *  are selected on the topology graph
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.engio.mbassy.bus.MBassador;

import org.jfree.util.Log;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.ICardController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UndoableJumpEvent;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.event.JumpToEvent;
import com.intel.stl.ui.event.NodesSelectedEvent;
import com.intel.stl.ui.event.PortsSelectedEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.main.UndoHandler;
import com.intel.stl.ui.main.view.IPageListener;
import com.intel.stl.ui.model.ConnectivityTableColumns;
import com.intel.stl.ui.model.ConnectivityTableModel;
import com.intel.stl.ui.model.GraphEdge;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.monitor.CableInfoPopupController;
import com.intel.stl.ui.monitor.IPortSelectionListener;
import com.intel.stl.ui.monitor.view.CableInfoPopupView;
import com.intel.stl.ui.monitor.view.ConnectivitySubpageView;
import com.intel.stl.ui.network.view.ResourceLinkSubpageView;
import com.intel.stl.ui.network.view.ResourceLinkView;

public class ResourceLinkSection
        extends ResourceSection<ResourceLinkSubpageView>
        implements IPortSelectionListener, IPageListener {

    /**
     * Subpages for the Topology page
     */
    private final Map<GraphEdge, ResourceLinkPage> subpages =
            new LinkedHashMap<GraphEdge, ResourceLinkPage>();

    private Context context;

    private IProgressObserver observer;

    private List<GraphEdge> currentLinks;

    private Map<GraphEdge, List<GraphEdge>> currentTraces;

    private String previousSubpageName;

    private String currentSubpageName;

    private UndoHandler undoHandler;

    private final String origin = TopologyPage.NAME;

    /**
     * Description:
     *
     * @param view
     */
    public ResourceLinkSection(ResourceLinkSubpageView view,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        this.view = view;
        view.setPageListener(this);
    }

    @Override
    public void setContext(Context context, IProgressObserver observer) {
        this.context = context;
        if (context != null && context.getController() != null) {
            undoHandler = context.getController().getUndoHandler();
        }
        observer.onFinish();
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

    protected void showLinks(List<GraphEdge> links, String vfName) {
        links = toPortLinks(links);
        if (links != null && links.equals(currentLinks)) {
            for (GraphEdge link : links) {
                ResourceLinkPage page = subpages.get(link);
                if (page != null) {
                    page.showLink(link, vfName);
                } else {
                    Log.warn("Cannot find page for " + link);
                }
            }
            return;
        }

        // Set the card title to the name of the node
        view.setTitle(STLConstants.K0013_LINKS.getValue());

        // Clear out the subpage list and page map
        clearSubpages();
        if (links == null || links.isEmpty()) {
            currentLinks = null;
            currentTraces = null;
            return;
        }

        // For each selected link, create a new link page and add
        // it to the subpage list
        for (GraphEdge link : links) {
            ConnectivityTableModel linkTableModel =
                    new ConnectivityTableModel();
            ConnectivitySubpageView linkTableView =
                    createLinkView(linkTableModel);
            CableInfoPopupView cableInfoPopupView =
                    new CableInfoPopupView(linkTableView);
            linkTableView.setCableInfoPopupView(cableInfoPopupView);
            CableInfoPopupController cableInfoPopupController =
                    new CableInfoPopupController(cableInfoPopupView);

            cableInfoPopupController.setContext(context, null);
            cableInfoPopupView.setCableInfoListener(cableInfoPopupController);
            linkTableView.setPortSelectionListener(this);

            ResourceLinkView linkView = new ResourceLinkView();
            ResourceLinkPage linkPage = new ResourceLinkPage(linkTableModel,
                    linkTableView, linkView);
            linkPage.setContext(context, null);
            linkPage.showLink(link, vfName);

            // Add subpage to list of subpages
            subpages.put(link, linkPage);
        }

        previousSubpageName = currentSubpageName;
        view.setTabs(subpages.values().toArray(new ResourceLinkPage[0]),
                currentSubpageName);
        currentSubpageName = view.getCurrentSubpage();
        currentLinks = links;
        currentTraces = null;

        setHelpID(HelpAction.getInstance().getLinks());
    } // showLinks

    protected List<GraphEdge> toPortLinks(List<GraphEdge> edges) {
        List<GraphEdge> res = new ArrayList<GraphEdge>();
        for (GraphEdge edge : edges) {
            Map<Integer, Integer> links = edge.getLinks();
            if (links.size() == 1) {
                res.add(edge);
            } else if (links.size() > 1) {
                for (Entry<Integer, Integer> link : links.entrySet()) {
                    res.add(new GraphEdge(edge.getFromLid(), edge.getFromType(),
                            edge.getToLid(), edge.getToType(),
                            Collections.singletonMap(link.getKey(),
                                    link.getValue())));
                }
            }
        }
        return res;
    }

    protected void showPath(final Map<GraphEdge, List<GraphEdge>> traceMap,
            String vfName) {
        if (traceMap != null && traceMap.equals(currentTraces)) {
            for (GraphEdge link : traceMap.keySet()) {
                subpages.get(link).showPath(link, traceMap.get(link), vfName);
            }
            return;
        }

        // Set the card title to the name of the node
        view.setTitle(STLConstants.K1028_ROUTE_RESOURCE.getValue());

        // Clear the subpage list
        clearSubpages();
        if (traceMap == null || traceMap.isEmpty()) {
            currentTraces = traceMap;
            currentLinks = null;
            return;
        }

        // For each path, create one new path subpage and add it to the list
        Iterator<Entry<GraphEdge, List<GraphEdge>>> it =
                traceMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<GraphEdge, List<GraphEdge>> entry = it.next();

            ConnectivityTableModel pathTableModel =
                    new ConnectivityTableModel();
            ConnectivitySubpageView pathTableView =
                    createPathView(pathTableModel);
            CableInfoPopupView cableInfoPopupView =
                    new CableInfoPopupView(pathTableView);
            pathTableView.setCableInfoPopupView(cableInfoPopupView);
            CableInfoPopupController cableInfoPopupController =
                    new CableInfoPopupController(cableInfoPopupView);

            cableInfoPopupController.setContext(context, null);
            cableInfoPopupView.setCableInfoListener(cableInfoPopupController);
            pathTableView.setPortSelectionListener(this);

            ResourceLinkView pathView = new ResourceLinkView();
            ResourceLinkPage pathPage = new ResourceLinkPage(pathTableModel,
                    pathTableView, pathView);

            // Page needs context before table update
            pathPage.setContext(context, null);

            // Update the table with the new page info
            pathPage.showPath(entry.getKey(), entry.getValue(), vfName);

            // Add this pathPage to the list of subpages
            subpages.put(entry.getKey(), pathPage);
        } // while

        previousSubpageName = currentSubpageName;
        // Show all the pages on the tabbed pane
        view.setTabs(subpages.values().toArray(new ResourceLinkPage[0]),
                currentSubpageName);
        currentSubpageName = view.getCurrentSubpage();

        currentTraces = traceMap;
        currentLinks = null;

        setHelpID(HelpAction.getInstance().getRoutes());
    } // showPath

    protected GraphNode findNode(int lid, GraphNode node) {
        boolean found = false;
        GraphNode targetNode = null;

        Iterator<GraphNode> it = node.getMiddleNeighbor().iterator();
        while ((!found) && (it.hasNext())) {
            GraphNode neighbor = it.next();
            if (neighbor.getLid() == lid) {
                found = true;
                targetNode = neighbor;
            }
        }

        return targetNode;
    }

    @Override
    public ISectionListener getSectionListener() {
        return this;
    }

    @Override
    public boolean canPageChange(String oldPage, String newPage) {
        return true;
    }

    @Override
    public synchronized void onPageChanged(String oldPageId, String newPageId) {
        if (undoHandler != null && !undoHandler.isInProgress()) {
            UndoableLinkSubpageSelection undoSel =
                    new UndoableLinkSubpageSelection(view, oldPageId,
                            newPageId);
            undoHandler.addUndoAction(undoSel);
        }
        previousSubpageName = oldPageId;
        currentSubpageName = newPageId;
    }

    protected ConnectivitySubpageView createLinkView(
            ConnectivityTableModel linkTableModel) {

        ConnectivitySubpageView linkTableView =
                new ConnectivitySubpageView(linkTableModel) {

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
        return linkTableView;
    }

    protected ConnectivitySubpageView createPathView(
            ConnectivityTableModel pathTableModel) {

        ConnectivitySubpageView pathTableView =
                new ConnectivitySubpageView(pathTableModel) {

                    private static final long serialVersionUID =
                            5930204470646720711L;

                    @Override
                    protected void filterColumns() {
                        ConnectivityTableColumns[] toHide =
                                new ConnectivityTableColumns[] {

                                        // Show these columns
                                        // ConnectivityTableColumns.DEVICE_NAME,
                                        // ConnectivityTableColumns.PORT_NUMBER,
                                        // ConnectivityTableColumns.LINK_STATE,
                                        // ConnectivityTableColumns.ACTIVE_LINK_SPEED,
                                        // ConnectivityTableColumns.SUPPORTED_LINK_SPEED,

                                        // Hide these columns
                                        ConnectivityTableColumns.LINK_ERROR_RECOVERIES,
                                        ConnectivityTableColumns.LINK_DOWNED,
                                        ConnectivityTableColumns.NUM_LANES_DOWN,
                                        ConnectivityTableColumns.NODE_GUID,
                                        ConnectivityTableColumns.PHYSICAL_LINK_STATE,
                                        ConnectivityTableColumns.ACTIVE_LINK_WIDTH,
                                        ConnectivityTableColumns.ENABLED_LINK_WIDTH,
                                        ConnectivityTableColumns.SUPPORTED_LINK_WIDTH,
                                        ConnectivityTableColumns.ENABLED_LINK_SPEED,
                                        ConnectivityTableColumns.TX_PACKETS,
                                        ConnectivityTableColumns.RX_PACKETS,
                                        ConnectivityTableColumns.RX_ERRORS,
                                        ConnectivityTableColumns.RX_REMOTE_PHYSICAL_ERRRORS,
                                        ConnectivityTableColumns.TX_DISCARDS,
                                        ConnectivityTableColumns.RX_SWITCH_RELAY_ERRRORS,
                                        ConnectivityTableColumns.TX_CONSTRAINT,
                                        ConnectivityTableColumns.RX_CONSTRAINT,
                                        ConnectivityTableColumns.LOCAL_LINK_INTEGRITY,
                                        ConnectivityTableColumns.EXCESSIVE_BUFFER_OVERRUNS,
                                        ConnectivityTableColumns.RX_MC_PACKETS,
                                        ConnectivityTableColumns.RX_ERRORS,
                                        ConnectivityTableColumns.RX_CONSTRAINT,
                                        ConnectivityTableColumns.RX_FECN,
                                        ConnectivityTableColumns.RX_BECN,
                                        ConnectivityTableColumns.RX_BUBBLE,
                                        ConnectivityTableColumns.TX_MC_PACKETS,
                                        ConnectivityTableColumns.TX_WAIT,
                                        ConnectivityTableColumns.TX_TIME_CONG,
                                        ConnectivityTableColumns.TX_WASTED_BW,
                                        ConnectivityTableColumns.TX_WAIT_DATA,
                                        ConnectivityTableColumns.LOCAL_LINK_INTEGRITY,
                                        ConnectivityTableColumns.MARK_FECN,
                                        ConnectivityTableColumns.LINK_ERROR_RECOVERIES,
                                        ConnectivityTableColumns.UNCORRECTABLE_ERRORS,
                                        ConnectivityTableColumns.SW_PORT_CONGESTION };

                        for (ConnectivityTableColumns col : toHide) {
                            mTable.getColumnExt(col.getTitle())
                                    .setVisible(false);
                        }
                    }
                };
        return pathTableView;
    }

    public void clearSubpages() {
        for (ResourceLinkPage subpage : subpages.values()) {
            subpage.clear();
            subpage.cleanup();
        }
        // Clear the subpages
        subpages.clear();
        currentLinks = null;
        currentTraces = null;
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

            if (undoHandler != null && !undoHandler.isInProgress()) {
                UndoableJumpEvent undoSel = new UndoableJumpEvent(eventBus,
                        getOldSelectionEvent(), pse);
                undoHandler.addUndoAction(undoSel);
            }

            eventBus.publish(pse);
        }
    }

    protected JumpToEvent getOldSelectionEvent() {
        if (currentLinks != null) {
            PortsSelectedEvent event = new PortsSelectedEvent(this, origin);
            for (GraphEdge link : currentLinks) {
                event.addPort(link.getFromLid(), link.getLinks().keySet()
                        .iterator().next().shortValue());
            }
            return event;
        }

        if (currentTraces != null) {
            NodesSelectedEvent event = new NodesSelectedEvent(this, origin);
            Map<Integer, Byte> nodes = new HashMap<Integer, Byte>();
            for (GraphEdge source : currentTraces.keySet()) {
                if (!nodes.containsKey(source.getFromLid())) {
                    nodes.put(source.getFromLid(), source.getFromType());
                }
                if (!nodes.containsKey(source.getToLid())) {
                    nodes.put(source.getToLid(), source.getToType());
                }
            }
            for (Entry<Integer, Byte> node : nodes.entrySet()) {
                event.addNode(node.getKey(),
                        NodeType.getNodeType(node.getValue()));
            }
            return event;
        }

        // shouldn't happen
        throw new RuntimeException(
                "Couldn't create JumpToEvent because no links or traces");
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
