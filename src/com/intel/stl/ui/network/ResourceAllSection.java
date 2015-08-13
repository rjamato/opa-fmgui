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
 *  File Name: ResourceAllPage.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.1  2015/08/12 15:26:50  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/03/10 18:43:14  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/18 19:32:02  jijunwan
 *  Archive Log:    PR 127102 - Overall summary of Switches under Topology page does not report correct number of switch ports
 *  Archive Log:     - improved the calculation to count both internal and external ports
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/17 17:14:27  jijunwan
 *  Archive Log:    improved to support initializing property display style
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/23 16:00:04  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/22 02:21:26  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/09/15 15:24:29  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/08/26 15:15:19  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/08/12 21:06:53  jijunwan
 *  Archive Log:    add null check
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/08/05 17:59:45  jijunwan
 *  Archive Log:    ensure we update UI on EDT, changed to use SingleTaskManager to manager concurrent UI update tasks
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/22 20:09:31  rjtierne
 *  Archive Log:    Implemented showPath() interface methods
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/18 13:39:16  rjtierne
 *  Archive Log:    Changed prototype for showPath() to accept a list of nodes to match the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/11 19:29:01  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/10 21:21:31  rjtierne
 *  Archive Log:    Added newly required interface methods
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/10 15:42:24  rjtierne
 *  Archive Log:    Added new interface method setDescription()
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/10 14:32:23  rjtierne
 *  Archive Log:    Added new interface methods
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/09 18:48:16  jijunwan
 *  Archive Log:    updated PortInfoRecord, SMInfo to the latest data structure
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/08 20:19:44  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Card controller for the main overview topology JCard displayed
 *  when no components have been selected on the topology graph  
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import java.util.Map;

import javax.swing.Icon;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.ui.common.BaseSectionController;
import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.common.ICardController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.configuration.view.IPropertyListener;
import com.intel.stl.ui.configuration.view.PropertyGroupPanel;
import com.intel.stl.ui.configuration.view.PropertyVizStyle;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.model.PropertySet;
import com.intel.stl.ui.model.SimplePropertyCategory;
import com.intel.stl.ui.model.SimplePropertyGroup;
import com.intel.stl.ui.network.view.ResourceAllView;
import com.intel.stl.ui.network.view.TopSummaryGroupPanel;
import com.intel.stl.ui.publisher.CallbackAdapter;
import com.intel.stl.ui.publisher.CancellableCall;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.SingleTaskManager;

public class ResourceAllSection extends
        BaseSectionController<ISectionListener, ResourceAllView> implements
        IPropertyListener {
    private final String SUBNET_SUMMARY = STLConstants.K2063_OVERALL_SUMMARY
            .getValue();

    private final String TOP_SUMMARY = STLConstants.K2064_TOP_SUMMARY
            .getValue();

    private final PropertyVizStyle style = new PropertyVizStyle(true, false);

    private final ResourceAllView view;

    private PropertySet<SimplePropertyGroup> model;

    private ISubnetApi subnetApi;

    private final SingleTaskManager taskMgr;

    public ResourceAllSection(ResourceAllView view,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        this.view = view;
        view.setInitialStyle(style.isShowBorder(), style.isAlternatRows());
        view.setStyleListener(this);
        taskMgr = new SingleTaskManager();

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getNameOfSubnet(), helpAction.getHelpSet());
    }

    public void setContext(Context context, IProgressObserver observer) {
        subnetApi = context.getSubnetApi();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getView()
     */
    @Override
    public ResourceAllView getView() {
        return view;
    }

    public void showAll(final String name, final Icon icon,
            final TopologyTreeModel topArch, final TopGraph graph,
            final TopGraph fullGraph) {
        CancellableCall<PropertySet<SimplePropertyGroup>> caller =
                new CancellableCall<PropertySet<SimplePropertyGroup>>() {
                    @Override
                    public PropertySet<SimplePropertyGroup> call(
                            ICancelIndicator cancelIndicator) throws Exception {
                        TopologySummaryProcessor topProcessor =
                                new TopologySummaryProcessor(SUBNET_SUMMARY,
                                        TOP_SUMMARY, topArch, graph, fullGraph,
                                        subnetApi, cancelIndicator);
                        PropertySet<SimplePropertyGroup> model =
                                topProcessor.populate();
                        return model;
                    }
                };

        ICallback<PropertySet<SimplePropertyGroup>> callback =
                new CallbackAdapter<PropertySet<SimplePropertyGroup>>() {
                    /*
                     * (non-Javadoc)
                     * 
                     * @see
                     * com.intel.stl.ui.publisher.CallbackAdapter#onDone(java
                     * .lang .Object )
                     */
                    @Override
                    public void onDone(PropertySet<SimplePropertyGroup> result) {
                        view.setTitle(name, icon);
                        updateMode(result);
                    }
                };
        taskMgr.submit(caller, callback);
    }

    protected void updateMode(PropertySet<SimplePropertyGroup> model) {
        this.model = model;
        view.clearPanel();
        for (SimplePropertyGroup group : model.getGroups()) {
            if (group.getGroupName() == SUBNET_SUMMARY) {
                updateSubnetSummaryModel(group);
            } else if (group.getGroupName() == TOP_SUMMARY) {
                updateTopSummaryModel(group);
            }
        }
        view.setModel(model);
    }

    protected void updateSubnetSummaryModel(SimplePropertyGroup model) {
        PropertyGroupPanel<SimplePropertyCategory, SimplePropertyGroup> groupPanel =
                new PropertyGroupPanel<SimplePropertyCategory, SimplePropertyGroup>(
                        style);
        groupPanel.setModel(model);

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(
                groupPanel.getHelpButton(), helpAction.getOverallSummary(),
                helpAction.getHelpSet());

        view.addPropertyGroupPanel(groupPanel);
    }

    protected SimplePropertyGroup populateTopSummaryModel() {
        SimplePropertyGroup model =
                new SimplePropertyGroup(
                        STLConstants.K2064_TOP_SUMMARY.getValue());
        return model;
    }

    protected void updateTopSummaryModel(SimplePropertyGroup model) {
        TopSummaryGroupPanel groupPanel = new TopSummaryGroupPanel(style);
        TopSummaryGroupController groupController =
                new TopSummaryGroupController(groupPanel);
        groupController.setModel(model);
        view.addPropertyGroupPanel(groupPanel);
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
     * @see
     * com.intel.stl.ui.configuration.view.IPropertyStyleListener#onShowBorder
     * (boolean)
     */
    @Override
    public void onShowBorder(boolean b) {
        style.setShowBorder(b);
        updateMode(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.configuration.view.IPropertyStyleListener#showAlternation
     * (boolean)
     */
    @Override
    public void onShowAlternation(boolean b) {
        style.setAlternateRows(b);
        updateMode(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.configuration.view.IPropertyListener#onDisplayChanged
     * (java.util.Map)
     */
    @Override
    public void onDisplayChanged(Map<String, Boolean> newSelections) {
        // TODO Auto-generated method stub

    }
}
