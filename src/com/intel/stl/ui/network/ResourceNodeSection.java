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
 *  File Name: ResourceDetailController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.BaseSectionController;
import com.intel.stl.ui.common.ICardController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.configuration.view.DevicePropertiesPanel;
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.event.PortSelectedEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.model.ConnectivityTableColumns;
import com.intel.stl.ui.model.ConnectivityTableModel;
import com.intel.stl.ui.model.DeviceProperties;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.monitor.IPortSelectionListener;
import com.intel.stl.ui.monitor.TreeNodeType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.monitor.view.ConnectivitySubpageView;
import com.intel.stl.ui.network.view.ResourcePortView;
import com.intel.stl.ui.network.view.ResourceSubpageView;

public class ResourceNodeSection extends
        BaseSectionController<ISectionListener, ResourceSubpageView> implements
        IPortSelectionListener {

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

    /**
     * Description:
     * 
     * @param view
     */
    public ResourceNodeSection(ResourceSubpageView view,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        this.view = view;
        model = new DeviceProperties();
        initSubpages();

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getTopologyNode(), helpAction.getHelpSet());
    }

    public void setContext(Context pContext, IProgressObserver observer) {
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

    protected void showNode(FVResourceNode source, GraphNode node) {
        if (source == null) {
            return;
        }

        if (lastNode != null && lastNode.equals(node)) {
            IResourceNodeSubpageController currentSubpage =
                    getCurrentSubpage(source.getType());
            if (currentSubpage != null) {
                currentSubpage.showNode(source, node);
            }
            return;
        }

        lastNode = node;

        // Set the card title to the name of the node
        view.setTitle(source.getName(), source.getType().getIcon());

        // Deregister tasks on all subpages
        for (IResourceNodeSubpageController page : mSubpages) {
            page.clear();
        }

        List<IResourceNodeSubpageController> subpages =
                getSubpagesByType(source.getType());
        if (subpages != null) {
            String current = view.getCurrentSubpage();
            int curIndex = -1;
            for (int i = 0; i < subpages.size(); i++) {
                IResourceNodeSubpageController subpage = subpages.get(i);
                if (subpage.getName() == current) {
                    curIndex = i;
                }
                subpage.showNode(source, node);
            }
            view.setTabs(subpages, curIndex);
        }
    }

    protected IResourceNodeSubpageController getCurrentSubpage(TreeNodeType type) {
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

    protected ConnectivitySubpageView createPortView(
            ConnectivityTableModel portTableModel) {

        ConnectivitySubpageView portTableView =
                new ConnectivitySubpageView(portTableModel) {

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
                                        // ConnectivityTableColumns.TX_PACKETS,
                                        // ConnectivityTableColumns.RX_PACKETS,
                                        // ConnectivityTableColumns.ACTIVE_LINK_SPEED,
                                        // ConnectivityTableColumns.SUPPORTED_LINK_SPEED,
                                        // ConnectivityTableColumns.LINK_RECOVERIES,
                                        // ConnectivityTableColumns.LINK_DOWN,
                                        // ConnectivityTableColumns.LOCAL_LINK_INTEGRITY_ERRRORS,
                                        // ConnectivityTableColumns.EXCESSIVE_BUFFER_OVERRUNS,

                                        // Hide these columns
                                        ConnectivityTableColumns.NODE_GUID,
                                        ConnectivityTableColumns.PHYSICAL_LINK_STATE,
                                        ConnectivityTableColumns.ACTIVE_LINK_WIDTH,
                                        ConnectivityTableColumns.ENABLED_LINK_WIDTH,
                                        ConnectivityTableColumns.SUPPORTED_LINK_WIDTH,
                                        ConnectivityTableColumns.ENABLED_LINK_SPEED,
                                        ConnectivityTableColumns.RX_ERRORS,
                                        ConnectivityTableColumns.RX_REMOTE_PHYSICAL_ERRRORS,
                                        ConnectivityTableColumns.TX_DISCARDS,
                                        ConnectivityTableColumns.SWITCH_RELAY_ERRRORS,
                                        ConnectivityTableColumns.TX_PORT_CONSTRAINT,
                                        ConnectivityTableColumns.RX_PORT_CONSTRAINT };

                        for (ConnectivityTableColumns col : toHide) {
                            mTable.getColumnExt(col.getTitle()).setVisible(
                                    false);
                        }
                    }
                };
        portTableView.setPortSelectionListener(this);
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
