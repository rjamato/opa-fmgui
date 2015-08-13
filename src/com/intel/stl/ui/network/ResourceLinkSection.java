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
 *  Archive Log:    Revision 1.7.2.1  2015/08/12 15:26:50  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.engio.mbassy.bus.MBassador;

import org.jfree.util.Log;

import com.intel.stl.ui.common.BaseSectionController;
import com.intel.stl.ui.common.ICardController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.event.PortSelectedEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.model.ConnectivityTableColumns;
import com.intel.stl.ui.model.ConnectivityTableModel;
import com.intel.stl.ui.model.GraphEdge;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.monitor.IPortSelectionListener;
import com.intel.stl.ui.monitor.view.ConnectivitySubpageView;
import com.intel.stl.ui.network.view.ResourceLinkSubpageView;
import com.intel.stl.ui.network.view.ResourceLinkView;

public class ResourceLinkSection extends
        BaseSectionController<ISectionListener, ResourceLinkSubpageView>
        implements IPortSelectionListener {

    /**
     * Subpages for the Topology page
     */
    private final Map<GraphEdge, ResourceLinkPage> subpages =
            new LinkedHashMap<GraphEdge, ResourceLinkPage>();

    private Context context;

    private List<GraphEdge> currentLinks;

    private Map<GraphEdge, List<GraphEdge>> currentTraces;

    /**
     * Description:
     * 
     * @param view
     */
    public ResourceLinkSection(ResourceLinkSubpageView view,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        this.view = view;

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getLinks(), helpAction.getHelpSet());
    }

    public void setContext(Context context, IProgressObserver observer) {
        this.context = context;
        observer.onFinish();
    }

    protected void showLinks(List<GraphEdge> links) {
        if (links != null && links.equals(currentLinks)) {
            for (GraphEdge link : links) {
                ResourceLinkPage page = subpages.get(link);
                if (page != null) {
                    page.showLink(link);
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
            ResourceLinkView linkView = new ResourceLinkView();
            ResourceLinkPage linkPage =
                    new ResourceLinkPage(linkTableModel, linkTableView,
                            linkView);
            linkPage.setContext(context, null);
            linkPage.showLink(link);

            // Add subpage to list of subpages
            subpages.put(link, linkPage);
        }

        view.setTabs(subpages.values().toArray(new ResourceLinkPage[0]),
                subpages.size() - 1);
        currentLinks = links;
        currentTraces = null;
    } // showLinks

    protected void showPath(final Map<GraphEdge, List<GraphEdge>> traceMap) {
        if (traceMap != null && traceMap.equals(currentTraces)) {
            for (GraphEdge link : traceMap.keySet()) {
                subpages.get(link).showPath(link, traceMap.get(link));
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
            ResourceLinkView pathView = new ResourceLinkView();
            ResourceLinkPage pathPage =
                    new ResourceLinkPage(pathTableModel, pathTableView,
                            pathView);

            // Page needs context before table update
            pathPage.setContext(context, null);

            // Update the table with the new page info
            pathPage.showPath(entry.getKey(), entry.getValue());

            // Add this pathPage to the list of subpages
            subpages.put(entry.getKey(), pathPage);
        } // while

        // Show all the pages on the tabbed pane
        view.setTabs(subpages.values().toArray(new ResourceLinkPage[0]),
                subpages.size() - 1);

        currentTraces = traceMap;
        currentLinks = null;
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

    protected ConnectivitySubpageView createLinkView(
            ConnectivityTableModel linkTableModel) {

        ConnectivitySubpageView linkTableView =
                new ConnectivitySubpageView(linkTableModel) {

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
                                        // ConnectivityTableColumns.LINK_RECOVERIES,
                                        // ConnectivityTableColumns.LINK_DOWN

                                        // Hide these columns
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
                                        ConnectivityTableColumns.SWITCH_RELAY_ERRRORS,
                                        ConnectivityTableColumns.TX_PORT_CONSTRAINT,
                                        ConnectivityTableColumns.RX_PORT_CONSTRAINT,
                                        ConnectivityTableColumns.LOCAL_LINK_INTEGRITY_ERRRORS,
                                        ConnectivityTableColumns.EXCESSIVE_BUFFER_OVERRUNS };

                        for (ConnectivityTableColumns col : toHide) {
                            mTable.getColumnExt(col.getTitle()).setVisible(
                                    false);
                        }
                    }
                };
        linkTableView.setPortSelectionListener(this);
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
                                        ConnectivityTableColumns.LINK_RECOVERIES,
                                        ConnectivityTableColumns.LINK_DOWN,
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
                                        ConnectivityTableColumns.SWITCH_RELAY_ERRRORS,
                                        ConnectivityTableColumns.TX_PORT_CONSTRAINT,
                                        ConnectivityTableColumns.RX_PORT_CONSTRAINT,
                                        ConnectivityTableColumns.LOCAL_LINK_INTEGRITY_ERRRORS,
                                        ConnectivityTableColumns.EXCESSIVE_BUFFER_OVERRUNS };

                        for (ConnectivityTableColumns col : toHide) {
                            mTable.getColumnExt(col.getTitle()).setVisible(
                                    false);
                        }
                    }
                };
        pathTableView.setPortSelectionListener(this);
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
     * short, com.intel.stl.ui.event.JumpDestination)
     */
    @Override
    public void onJumpToPort(int lid, short portNum, JumpDestination destination) {
        if (eventBus != null) {
            PortSelectedEvent pse =
                    new PortSelectedEvent(lid, portNum, this, destination);
            eventBus.publish(pse);
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
