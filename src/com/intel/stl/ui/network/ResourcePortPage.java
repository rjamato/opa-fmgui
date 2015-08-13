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
 *  File Name: ResourcePortPage.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.20.2.1  2015/08/12 15:26:50  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/04/03 21:06:30  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/02/04 21:44:19  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/01/29 21:32:04  jijunwan
 *  Archive Log:    improved the handle the special case - B2B topology
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/11/17 17:15:24  jijunwan
 *  Archive Log:    hanged Port subpage's name to Connectivity
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/10/23 16:00:04  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/10/09 12:37:03  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/08/26 15:15:19  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/08/05 18:39:05  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/08/05 17:59:45  jijunwan
 *  Archive Log:    ensure we update UI on EDT, changed to use SingleTaskManager to manager concurrent UI update tasks
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/07/29 15:46:06  rjtierne
 *  Archive Log:    Scheduled periodic Connectivity table updates
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/07/22 20:09:31  rjtierne
 *  Archive Log:    Implemented showPath() interface methods
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/07/18 13:41:21  rjtierne
 *  Archive Log:    Changed prototype for showPath() to accept a list of nodes to
 *  Archive Log:    match the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/10 21:24:08  rjtierne
 *  Archive Log:    Added newly required interface methods
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/10 15:43:16  rjtierne
 *  Archive Log:    Added new interface method setDescription()
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/10 15:12:06  rjtierne
 *  Archive Log:    Pass null to setContext() in place of observer since it's not needed
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/10 14:36:44  rjtierne
 *  Archive Log:    Added new interface methods. Added end nodes to the connectivity table
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/08 20:26:06  rjtierne
 *  Archive Log:    Removed IProgressObserver from showNode(), added unimplemented
 *  Archive Log:    methods from interface
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/03 14:16:52  rjtierne
 *  Archive Log:    Created connectivity table controller and updated the table for the
 *  Archive Log:    topology port page
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/26 15:00:18  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/25 13:28:14  rjtierne
 *  Archive Log:    Initial Version - renamed from ResourceRoutePage
 *  Archive Log:
 *
 *  Overview: Controller for the Port subpage on the Topology page
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.network;

import static com.intel.stl.ui.common.PageWeight.MEDIUM;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.ImageIcon;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.ConnectivityTableModel;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.monitor.ConnectivityTableController;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.monitor.view.ConnectivitySubpageView;
import com.intel.stl.ui.network.view.ResourcePortView;

public class ResourcePortPage implements IResourceNodeSubpageController {

    private final ResourcePortView pageView;

    private ConnectivityTableController tableController;

    public ResourcePortPage(ResourcePortView view) {
        this.pageView = view;
    }

    public ResourcePortPage(ConnectivityTableModel tableModel,
            ConnectivitySubpageView tableView, ResourcePortView pageView) {

        this.pageView = pageView;
        this.pageView.addTableView(tableView);
        tableController =
                new ConnectivityTableController(tableModel,
                        tableView.getTable());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.IPageController#setContext(com.intel.stl.ui.main
     * .Context, com.intel.stl.ui.common.IProgressObserver)
     */
    @Override
    public void setContext(Context context, IProgressObserver observer) {
        tableController.setContext(context, null);
        if (observer != null) {
            observer.onFinish();
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
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getName()
     */
    @Override
    public String getName() {
        return STLConstants.K0415_CONNECTIVITY.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getDescription()
     */
    @Override
    public String getDescription() {
        return STLConstants.K0416_CONNECTIVITY_DESCRIPTION.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getView()
     */
    @Override
    public Component getView() {
        return pageView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getIcon()
     */
    @Override
    public ImageIcon getIcon() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#cleanup()
     */
    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onEnter()
     */
    @Override
    public void onEnter() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onExit()
     */
    @Override
    public void onExit() {
        // TODO Auto-generated method stub

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
     * @see
     * com.intel.stl.ui.network.IResourceSubpageController#showNode(com.intel
     * .stl.ui.model.GraphNode)
     */
    @Override
    public void showNode(FVResourceNode source, GraphNode node) {
        processNode(node, NodeType.getNodeType(node.getType()));
    }

    protected void processNode(GraphNode node, NodeType nodeType) {
        node.dump(System.out);
        TreeMap<GraphNode, TreeMap<Integer, Integer>> middleNodes =
                node.getMiddleNodes();
        List<Short> portList = new ArrayList<Short>();

        if (middleNodes != null && middleNodes.keySet().size() > 0) {
            // For each middle node, get the port numbers
            for (GraphNode gNode : node.getMiddleNodes().keySet()) {
                TreeMap<Integer, Integer> neighbor =
                        node.getMiddleNodes().get(gNode);

                for (Integer portNum : neighbor.keySet()) {
                    portList.add(portNum.shortValue());
                }
            }
        }

        // For each end node, get the port numbers if applicable
        if (node.getEndNodes() != null) {
            for (GraphNode gNode : node.getEndNodes().keySet()) {
                TreeMap<Integer, Integer> neighbor =
                        node.getEndNodes().get(gNode);

                for (Integer portNum : neighbor.keySet()) {
                    portList.add(portNum.shortValue());
                }
            }
        }

        // Convert list of Short to array of short
        Short[] pShorts = portList.toArray(new Short[portList.size()]);
        short[] ports = new short[pShorts.length];
        for (int i = 0; i < pShorts.length; i++) {
            ports[i] = pShorts[i].shortValue();

            // Increment the port number for HFIs
            if (nodeType == NodeType.HFI) {
                ports[i]++;
            }
        }

        tableController.showConnectivity(node.getLid(), null, ports);
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
