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
 *  File Name: ResourceCard.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.18.2.1  2015/08/12 15:26:50  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/02/18 19:32:02  jijunwan
 *  Archive Log:    PR 127102 - Overall summary of Switches under Topology page does not report correct number of switch ports
 *  Archive Log:     - improved the calculation to count both internal and external ports
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/11/05 16:37:14  jijunwan
 *  Archive Log:    renamed ResoureLinkCard to ResourceLinkSection since it a section now
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/10/23 16:00:04  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/10/22 02:21:26  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/10/09 12:37:03  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/09/18 21:03:25  jijunwan
 *  Archive Log:    Added link (jump to) capability to Connectivity tables and PortSummary table
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/09/15 15:24:29  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/08/26 15:15:19  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/08/05 22:11:06  jijunwan
 *  Archive Log:    changed text Path to Route
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/08/05 17:59:45  jijunwan
 *  Archive Log:    ensure we update UI on EDT, changed to use SingleTaskManager to manager concurrent UI update tasks
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/08/05 13:46:23  jijunwan
 *  Archive Log:    new implementation on topology control that uses double models to avoid synchronization issues on model and view
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/22 20:09:31  rjtierne
 *  Archive Log:    Implemented showPath() interface methods
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/18 13:39:41  rjtierne
 *  Archive Log:    Added a new pathSubpageCard to the list of possible Controllers for
 *  Archive Log:    the network resource views
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/11 19:29:01  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/10 21:22:09  rjtierne
 *  Archive Log:    Implemented method showPath()
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/10 15:42:36  rjtierne
 *  Archive Log:    Added new interface method setDescription()
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/10 14:34:16  rjtierne
 *  Archive Log:    Added new linkSubpageCard to display a new JCardView when links are
 *  Archive Log:    selected from the topology graph
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/08 20:19:44  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Top level controller for providing swappable JCards on the
 *  topology page depending on whether or not a component has been selected
 *  on the graph
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.network;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.GraphEdge;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.network.view.ResourceAllView;
import com.intel.stl.ui.network.view.ResourceLinkSubpageView;
import com.intel.stl.ui.network.view.ResourceSubpageView;
import com.intel.stl.ui.network.view.ResourceView;

public class ResourceController {

    private final ResourceView view;

    private ResourceAllSection allCard;

    private ResourceNodeSection nodeSubpageCard;

    private ResourceLinkSection linkSubpageCard;

    private ResourceLinkSection pathSubpageCard;

    private Map<ResourceScopeType, Object> cards =
            new HashMap<ResourceScopeType, Object>();

    private final MBassador<IAppEvent> eventBus;

    public ResourceController(ResourceView view, MBassador<IAppEvent> eventBus) {
        this.eventBus = eventBus;
        cards = getCards();
        this.view = view;
        this.view.initializeViews(cards);
    }

    protected Map<ResourceScopeType, Object> getCards() {
        allCard =
                new ResourceAllSection(new ResourceAllView(
                        STLConstants.K1033_TOP_OVERVIEW.getValue()), eventBus);
        cards.put(ResourceScopeType.ALL, allCard);

        nodeSubpageCard =
                new ResourceNodeSection(new ResourceSubpageView(
                        STLConstants.K1021_RESOURCE_DETAILS.getValue()),
                        eventBus);
        cards.put(ResourceScopeType.NODE, nodeSubpageCard);

        linkSubpageCard =
                new ResourceLinkSection(new ResourceLinkSubpageView(
                        STLConstants.K0013_LINKS.getValue(),
                        UIImages.LINKS.getImageIcon()), eventBus);
        cards.put(ResourceScopeType.LINK, linkSubpageCard);

        pathSubpageCard =
                new ResourceLinkSection(new ResourceLinkSubpageView(
                        STLConstants.K1028_ROUTE_RESOURCE.getValue(),
                        UIImages.ROUTE.getImageIcon()), eventBus);
        cards.put(ResourceScopeType.PATH, pathSubpageCard);

        return cards;
    }

    public void setContext(Context context, IProgressObserver observer) {
        IProgressObserver[] subObservers = observer.createSubObservers(4);
        // Pass the context to the cards for this view
        ((ResourceAllSection) cards.get(ResourceScopeType.ALL)).setContext(
                context, subObservers[0]);
        subObservers[0].onFinish();
        ((ResourceNodeSection) cards.get(ResourceScopeType.NODE)).setContext(
                context, subObservers[1]);
        subObservers[1].onFinish();
        ((ResourceLinkSection) cards.get(ResourceScopeType.LINK)).setContext(
                context, subObservers[2]);
        subObservers[2].onFinish();
        ((ResourceLinkSection) cards.get(ResourceScopeType.PATH)).setContext(
                context, subObservers[3]);
        subObservers[3].onFinish();
    }

    public void showAll(final String name, final Icon icon,
            final TopologyTreeModel topArch, final TopGraph graph) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                // Clear subpages on all other views
                pathSubpageCard.clearSubpages();
                linkSubpageCard.clearSubpages();

                view.showLayout(ResourceScopeType.ALL);
                ((ResourceAllSection) cards.get(ResourceScopeType.ALL))
                        .showAll(name, icon, topArch, graph, graph);
            }
        });
    }

    public void showGroup(final String name, final Icon icon,
            final TopologyTreeModel topArch, final TopGraph graph,
            final TopGraph fullGraph) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                // Clear subpages on all other views
                pathSubpageCard.clearSubpages();
                linkSubpageCard.clearSubpages();

                view.showLayout(ResourceScopeType.ALL);
                ((ResourceAllSection) cards.get(ResourceScopeType.ALL))
                        .showAll(name, icon, topArch, graph, fullGraph);
            }
        });
    }

    public void showNode(final FVResourceNode source, final GraphNode node) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                // Clear subpages on all other views
                pathSubpageCard.clearSubpages();
                linkSubpageCard.clearSubpages();

                view.showLayout(ResourceScopeType.NODE);
                ((ResourceNodeSection) cards.get(ResourceScopeType.NODE))
                        .showNode(source, node);
            }
        });
    }

    public void showLinks(final List<GraphEdge> links) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                // Clear subpages on all other views
                pathSubpageCard.clearSubpages();

                view.showLayout(ResourceScopeType.LINK);
                ((ResourceLinkSection) cards.get(ResourceScopeType.LINK))
                        .showLinks(links);
            }
        });
    }

    public void showPath(final Map<GraphEdge, List<GraphEdge>> traceMap) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                // Clear subpages on all other views
                linkSubpageCard.clearSubpages();

                view.showLayout(ResourceScopeType.PATH);
                ((ResourceLinkSection) cards.get(ResourceScopeType.PATH))
                        .showPath(traceMap);
            }
        });
    }

    public Component getView() {
        return this.view;
    }

}
