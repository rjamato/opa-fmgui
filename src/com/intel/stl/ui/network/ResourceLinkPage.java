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
 *  File Name: ResourceLinkPage.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.23  2015/04/03 21:06:30  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/02/20 14:53:55  jijunwan
 *  Archive Log:    PR 127177 - "string index out of range" error when selecting more than 4 resources under topology page
 *  Archive Log:     - changed to more sophisticated code
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/02/05 19:10:50  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/02/04 21:44:19  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/11/05 16:25:44  jijunwan
 *  Archive Log:    code cleanup - removed debug printout
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/10/09 12:37:03  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/08/26 15:15:19  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/08/05 18:39:05  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/08/05 17:59:45  jijunwan
 *  Archive Log:    ensure we update UI on EDT, changed to use SingleTaskManager to manager concurrent UI update tasks
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/08/05 13:46:23  jijunwan
 *  Archive Log:    new implementation on topology control that uses double models to avoid synchronization issues on model and view
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/07/29 15:46:06  rjtierne
 *  Archive Log:    Scheduled periodic Connectivity table updates
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/07/22 20:10:11  rjtierne
 *  Archive Log:    Updated showPath() interface method to handle new argument list
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/07/18 19:24:38  rjtierne
 *  Archive Log:    Removed Link page when node selected from topology page.
 *  Archive Log:    Added Link Statistics panel to the Node page.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/07/18 13:40:01  rjtierne
 *  Archive Log:    Added content to method showPath() to display a Connectivity
 *  Archive Log:    table listing the node/port information for each node along the path.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/07/10 21:22:25  rjtierne
 *  Archive Log:    Added newly required interface methods
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/10 15:44:43  rjtierne
 *  Archive Log:    Provided access methods to set the page description
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/10 15:11:34  rjtierne
 *  Archive Log:    Pass null in place of observer since it's not needed
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/10 14:35:33  rjtierne
 *  Archive Log:    Added a connectivity table for node and link selections from the
 *  Archive Log:    topology graph
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/08 20:22:25  rjtierne
 *  Archive Log:    Removed IProgressObserver from showNode()
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/07 19:06:05  jijunwan
 *  Archive Log:    minor improvements:
 *  Archive Log:    1) null check
 *  Archive Log:    2) stop previous context switching when we need to switch to a new one
 *  Archive Log:    3) auto fit when we resize split panes
 *  Archive Log:    4) put layout execution on background
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/03 14:13:18  rjtierne
 *  Archive Log:    Updated data for Link Statistics and Health panels
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/26 15:00:18  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/24 20:30:31  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Controller for the Link subpage on the Topology page
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.network;

import static com.intel.stl.ui.common.PageWeight.MEDIUM;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;

import org.jfree.util.Log;

import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.NodeInfoBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.ConnectivityTableModel;
import com.intel.stl.ui.model.GraphEdge;
import com.intel.stl.ui.monitor.ConnectivityTableController;
import com.intel.stl.ui.monitor.view.ConnectivitySubpageView;
import com.intel.stl.ui.network.view.ResourceLinkView;

public class ResourceLinkPage implements IPageController {

    private final static byte NUM_TAB_CHARS = 12;

    private final ResourceLinkView view;

    private final ConnectivityTableController tableController;

    private ISubnetApi subnetApi;

    private String pageName = new String(STLConstants.K0013_LINKS.getValue());

    private String pageDescription = new String(
            STLConstants.K1023_LINK_RESOURCE_DESCRIPTION.getValue());

    public ResourceLinkPage(ConnectivityTableModel tableModel,
            ConnectivitySubpageView tableView, ResourceLinkView view) {
        this.view = view;
        this.view.addTableView(tableView);
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
        subnetApi = context.getSubnetApi();
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getName()
     */
    @Override
    public String getName() {
        return pageName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getDescription()
     */
    @Override
    public String getDescription() {
        return pageDescription;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getView()
     */
    @Override
    public Component getView() {
        return view;
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

    private String createNodeName(String name) {
        return Util.truncateString(name, 1, NUM_TAB_CHARS);
    }

    private String createToolTip(String fromName, int fromLid, String toName,
            int toLid) {
        String toolTip = new String("");

        NodeInfoBean fromNode;
        try {
            fromNode = subnetApi.getNode(fromLid).getNodeInfo();
            NodeInfoBean toNode = subnetApi.getNode(toLid).getNodeInfo();

            String fromGuid = String.format("%#020x", fromNode.getNodeGUID());
            String toGuid = String.format("%#020x", toNode.getNodeGUID());
            toolTip =
                    "<html>" + fromName + ": GUID=" + fromGuid + "  LID="
                            + fromLid + "<br>" + toName + ": GUID=" + toGuid
                            + "  LID=" + toLid + "</html>";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toolTip;
    }

    protected String getNodeName(int lid) {
        String nodeName = new String("");

        try {
            nodeName = subnetApi.getNode(lid).getNodeDesc();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nodeName;
    }

    public void showLink(GraphEdge link) {
        if (link.getLinks().size() > 0) {

            NodeRecordBean fromNodeBean = null;
            NodeRecordBean toNodeBean = null;
            try {
                fromNodeBean = subnetApi.getNode(link.getFromLid());
                toNodeBean = subnetApi.getNode(link.getToLid());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (fromNodeBean == null || toNodeBean == null) {
                Log.warn("Couldn't fond nodes for link " + link);
                tableController.clear();
                return;
            }

            // Create abbreviations for the path end nodes to put on the page
            // tab
            String fromName = createNodeName(fromNodeBean.getNodeDesc());
            String toName = createNodeName(toNodeBean.getNodeDesc());
            setName(new String(fromName + "," + toName));

            // Create the tool-tip description for the path end nodes with node
            // name, GUID, and LID
            String description =
                    createToolTip(fromName, link.getFromLid(), toName,
                            link.getToLid());
            setDescription(description);

            // Create the port list
            List<Short> portList = new ArrayList<Short>();
            Map<Integer, Integer> links = link.getLinks();
            Iterator<Entry<Integer, Integer>> it = links.entrySet().iterator();
            while (it.hasNext()) {
                Integer portNum = it.next().getKey();
                portList.add(portNum.shortValue());
            }

            // Convert list of Short to array of short
            Short[] pShorts = portList.toArray(new Short[portList.size()]);
            short[] ports = new short[pShorts.length];
            for (int i = 0; i < pShorts.length; i++) {
                ports[i] = pShorts[i].shortValue();
            }

            // Show the data
            tableController.showConnectivity(link.getFromLid(), null, ports);
        }
    } // showLink

    public void showPath(GraphEdge trace, List<GraphEdge> links) {
        LinkedHashMap<GraphEdge, Short> portMap =
                new LinkedHashMap<GraphEdge, Short>();

        // Populate the map with the lids/ports for this path
        for (GraphEdge link : links) {
            Iterator<Entry<Integer, Integer>> it =
                    link.getLinks().entrySet().iterator();
            while (it.hasNext()) {
                Entry<Integer, Integer> entry = it.next();
                short fromPortNum = entry.getKey().shortValue();
                // connectivity table requires local port number for HFIs
                try {
                    NodeRecordBean nrb = subnetApi.getNode(link.getFromLid());
                    if (nrb.getNodeType() == NodeType.HFI) {
                        fromPortNum = nrb.getNodeInfo().getLocalPortNum();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                portMap.put(link, fromPortNum);
            } // while
        } // for

        // Create abbreviations for the path end nodes to put on the page tab
        String fromName = createNodeName(getNodeName(trace.getFromLid()));
        String toName = createNodeName(getNodeName(trace.getToLid()));
        setName(new String(fromName + "," + toName));

        // Create the tool-tip description for the path end nodes with node
        // name, GUID, and LID
        String description =
                createToolTip(fromName, trace.getFromLid(), toName,
                        trace.getToLid());
        setDescription(description);

        // Update the table
        tableController.showPathConnectivity(portMap, null);
    }

    public void setName(String name) {
        pageName = name;
    }

    public void setDescription(String description) {
        pageDescription = description;
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