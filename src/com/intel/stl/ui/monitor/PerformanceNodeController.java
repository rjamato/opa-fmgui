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
 *  File Name: PerformanceNodeController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.33.2.2  2015/08/12 15:26:58  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.33.2.1  2015/05/17 18:30:42  jijunwan
 *  Archive Log:    PR 127700 - Delta data on host performance display is accumulating
 *  Archive Log:    - corrected delta value calculation
 *  Archive Log:    - changed to display data/pkts rate rather than delta on chart and table
 *  Archive Log:    - updated chart unit to show rate
 *  Archive Log:    - renamed the following classes to reflect we are dealing with rate
 *  Archive Log:      DataChartRangeUpdater -> DataRateChartRangeUpdater
 *  Archive Log:      PacketChartRangeUpdater -> PacketRateChartRangeUpdater
 *  Archive Log:      DataChartScaleGroupManager -> DataRateChartScaleGroupManager
 *  Archive Log:      PacketChartScaleGroupManager -> PacketRateChartScaleGroupManager
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/04/14 12:00:54  jypak
 *  Archive Log:    Updates to fix this problem: In the performance subpage for a node, if a history scope of non current is selected for the node and then if user select another node, the history is not updated in charts. The fix is to initialize history query after updating the lid, port num and vf name.
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2015/04/03 21:06:27  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/04/01 19:54:05  jijunwan
 *  Archive Log:    fixed the following bugs
 *  Archive Log:    1) no link quality clear when we change preview port
 *  Archive Log:    2) loss data when we go from node performance view to port performance view and then go back to node performance view
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/03/17 19:08:21  jypak
 *  Archive Log:    1. Introduced a setIcon method in JSectionView to only set icon. Also, this will fix the following problem:
 *  Archive Log:    Whenever link quality icon is set, the port number portion of a node performance sub-page chart section title was removed. By only setting the icon, the issue was resolved.
 *  Archive Log:    2. The table selection row index for a node's performance sub-page table section should be saved before initialize history query.
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/03/17 15:56:19  jypak
 *  Archive Log:    DataType, HistoryType JComboBox have been replaced with button popup to save space.
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/02/24 14:23:20  jypak
 *  Archive Log:    1. Show Border, Alternating Rows control panel added to the PerformanceErrorsSection.
 *  Archive Log:    2. Undo change of Performance Chart Section title to "Performancefor port Performance subpage.
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/02/12 19:40:05  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/02/11 21:14:58  jypak
 *  Archive Log:    1. For 'current' history scope, default max data points need to be set.
 *  Archive Log:    2. History icon fixed.
 *  Archive Log:    3. Home Page performance section trend charts should show history scope selections.
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/02/10 23:25:35  jijunwan
 *  Archive Log:    removed refresh rate on caller side since we should be able to directly get it from task scheduler
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/02/10 21:25:57  jypak
 *  Archive Log:    1. Introduced SwingWorker for history query initialization for progress status updates.
 *  Archive Log:    2. Fixed the list of future for history query in TaskScheduler. Now it can have all the Future entries created.
 *  Archive Log:    3. When selecting history type, just cancel the history query not sheduled query.
 *  Archive Log:    4. The refresh rate is now from user settings not from the config api.
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/02/06 20:49:36  jypak
 *  Archive Log:    1. TaskScheduler changed to handle two threads.
 *  Archive Log:    2. All four(VFInfo, VFPortCounters, GroupInfo, PortCounters) attributes history query related updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/02/04 21:44:17  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/02/02 15:38:27  rjtierne
 *  Archive Log:    New TaskScheduler architecture; now employs subscribers to submit
 *  Archive Log:    tasks for scheduling.  When update rate is changed on Wizard, TaskScheduler
 *  Archive Log:    uses this new architecture to terminate tasks and service and restart them.
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/01/21 21:19:11  rjtierne
 *  Archive Log:    Removed individual refresh rates for task registration. Now using
 *  Archive Log:    refresh rate supplied by user input in preferences wizard.
 *  Archive Log:    Reinitialization of scheduler service not yet implemented.
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/10/21 16:38:29  fernande
 *  Archive Log:    Customization of Properties display (Show Options/Apply Options)
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/10/09 21:24:49  jijunwan
 *  Archive Log:    improvement on TreeNodeType:
 *  Archive Log:    1) Added icon to TreeNodeType
 *  Archive Log:    2) Rename PORT to ACTIVE_PORT
 *  Archive Log:    3) Removed NODE
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/10/09 12:35:09  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/09/18 21:36:50  jijunwan
 *  Archive Log:    fixed a issue that incorrectly use portNum for rowIndex
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/09/18 21:03:28  jijunwan
 *  Archive Log:    Added link (jump to) capability to Connectivity tables and PortSummary table
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/09/15 15:24:32  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/09/02 19:24:29  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/08/26 15:15:27  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/07/11 19:26:54  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/06/27 22:22:22  jijunwan
 *  Archive Log:    added running indicator to Performance Subpages
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/06/26 15:00:15  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/06/05 17:34:58  jijunwan
 *  Archive Log:    integrate vFabric into performance pages
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/02 19:56:46  rjtierne
 *  Archive Log:    Clear the performance table and associated data when
 *  Archive Log:    a new node is selected from the tree
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/29 03:07:06  jijunwan
 *  Archive Log:    minor adjustment on performance subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/28 22:21:58  jijunwan
 *  Archive Log:    added port preview to performance subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/28 17:47:22  rjtierne
 *  Archive Log:    Changed performanceNodeView to use new class PerformanceNodeView
 *  Archive Log:    instead of PerformanceView since the view now requires a splitpane
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/23 19:32:45  rjtierne
 *  Archive Log:    Removed print statements in processPortCounters()
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/23 19:22:12  rjtierne
 *  Archive Log:    Implemented PortCountersBean registration and callback.
 *  Archive Log:    Created performance table model and view.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/21 14:45:05  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: This class is the controller for the Performance "Node" view which
 *  holds the performance table and Tx/Rx packet graphs
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

import static com.intel.stl.ui.common.PageWeight.MEDIUM;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.PortCountersBean;
import com.intel.stl.api.performance.VFPortCountersBean;
import com.intel.stl.ui.common.IPerfSubpageController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.ISectionController;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.event.PortSelectedEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.view.IDataTypeListener;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.model.PerformanceTableModel;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.monitor.view.PerformanceChartsSectionView;
import com.intel.stl.ui.monitor.view.PerformanceView;
import com.intel.stl.ui.monitor.view.PerformanceXTableView;
import com.intel.stl.ui.publisher.CallbackAdapter;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.intel.stl.ui.publisher.subscriber.PortCounterSubscriber;
import com.intel.stl.ui.publisher.subscriber.SubscriberType;
import com.intel.stl.ui.publisher.subscriber.VFPortCounterSubscriber;

public class PerformanceNodeController implements IPerfSubpageController,
        IPortSelectionListener {
    private final List<ISectionController<?>> sections;

    private PerformanceTableSection tableSection;

    private PerformanceChartsSection chartsSection;

    private final PerformanceView performanceView;

    private Context context;

    private TaskScheduler taskScheduler;

    private List<Task<PortCountersBean>> portCounterTask;

    private ICallback<PortCountersBean[]> portCounterCallback;

    private ICallback<PortCountersBean> previewPortcounterHistoryCallback;

    private List<Task<VFPortCountersBean>> vfPortCounterTask;

    private ICallback<VFPortCountersBean[]> vfPortCounterCallback;

    private ICallback<VFPortCountersBean> previewVfPortcounterHistoryCallback;

    private Future<Void> historyTask;

    private int maxDataPoints;

    private HistoryType historyType = HistoryType.CURRENT;

    private String vfName;

    private int lid = -1;

    private List<Short> portNumList;

    private int previewPortIndex = 0;

    @SuppressWarnings("unused")
    private PerformanceTreeController parentController;

    private final MBassador<IAppEvent> eventBus;

    private PortCounterSubscriber portCounterSubscriber;

    private VFPortCounterSubscriber vfPortCounterSubscriber;

    public PerformanceNodeController(PerformanceView performanceView,
            MBassador<IAppEvent> eventBus) {
        this.performanceView = performanceView;
        this.eventBus = eventBus;

        sections = getSections();
        List<JSectionView<?>> sectionViews = new ArrayList<JSectionView<?>>();
        for (ISectionController<?> section : sections) {
            sectionViews.add(section.getView());
        }
        performanceView.installSectionViews(sectionViews);
    }

    protected List<ISectionController<?>> getSections() {
        List<ISectionController<?>> sections =
                new ArrayList<ISectionController<?>>();

        final PerformanceTableModel tableModel = new PerformanceTableModel();
        final PerformanceXTableView tableView =
                new PerformanceXTableView(tableModel);
        tableView.setPortSelectionListener(this);
        JSectionView<ISectionListener> tableSectionView =
                new JSectionView<ISectionListener>(
                        STLConstants.K0208_PORTS_TABLE.getValue()) {
                    private static final long serialVersionUID =
                            6166893610476283350L;

                    @Override
                    protected JComponent getMainComponent() {
                        return tableView;
                    }
                };

        tableSection =
                new PerformanceTableSection(tableModel, tableView,
                        tableSectionView, eventBus);
        sections.add(tableSection);

        chartsSection =
                new PerformanceChartsSection(new PerformanceChartsSectionView(
                        UILabels.STL60100_PORT_PREVIEW.getDescription("")),
                        true, eventBus);
        sections.add(chartsSection);

        return sections;
    }

    public PerformanceView getPerformanceView() {
        return performanceView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.IPageController#setContext(com.intel.stl.ui.main
     * .Context)
     */
    @Override
    public void setContext(Context context, IProgressObserver observer) {

        this.context = context;
        taskScheduler = this.context.getTaskScheduler();

        // Get the port counter subscriber from the task scheduler
        portCounterSubscriber =
                (PortCounterSubscriber) taskScheduler
                        .getSubscriber(SubscriberType.PORT_COUNTER);
        portCounterCallback = new CallbackAdapter<PortCountersBean[]>() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * com.intel.hpc.stl.ui.publisher.CallBackAdapter#onDone(java.lang
             * .Object)
             */
            @Override
            public synchronized void onDone(PortCountersBean[] portCounterBeans) {
                if (portCounterBeans != null) {
                    processPortCounters(portCounterBeans);
                }
            }
        };

        previewPortcounterHistoryCallback =
                new CallbackAdapter<PortCountersBean>() {
                    @Override
                    public synchronized void onDone(
                            PortCountersBean portCounterBean) {
                        if (portCounterBean != null) {
                            chartsSection.processPortCounters(portCounterBean);
                        }
                    }
                };

        // Get the virtual fabrics port counter subscriber from the task
        // scheduler
        vfPortCounterSubscriber =
                (VFPortCounterSubscriber) taskScheduler
                        .getSubscriber(SubscriberType.VF_PORT_COUNTER);
        vfPortCounterCallback = new CallbackAdapter<VFPortCountersBean[]>() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * com.intel.hpc.stl.ui.publisher.CallBackAdapter#onDone(java.lang
             * .Object)
             */
            @Override
            public synchronized void onDone(
                    VFPortCountersBean[] portCounterBeans) {
                if (portCounterBeans != null) {
                    processVFPortCounters(portCounterBeans);
                }
            }
        };

        previewVfPortcounterHistoryCallback =
                new CallbackAdapter<VFPortCountersBean>() {
                    @Override
                    public synchronized void onDone(
                            VFPortCountersBean vfPortcounterBean) {
                        if (vfPortcounterBean != null) {
                            chartsSection
                                    .processVFPortCounters(vfPortcounterBean);
                        }
                    }
                };

        final PerformanceChartsSectionView view = chartsSection.getView();
        view.setHistoryTypeListener(new IDataTypeListener<HistoryType>() {
            @Override
            // Each time different node is selected, each chart will be
            // defaulted to show current. Only when different HistoryScope is
            // selected by user, chart will show different history range.
            public void onDataTypeChange(HistoryType type) {
                setHistoryType(type);
                view.setHistoryType(type);
            }
        });
        observer.onFinish();
    } // setContext

    private void setHistoryType(HistoryType type) {
        clearHistory();
        historyType = type;

        if (type == HistoryType.CURRENT) {
            maxDataPoints = PerformanceChartsSection.DEFAULT_DATA_POINTS;
            chartsSection.setMaxDataPoints(maxDataPoints);
            return;
        }

        maxDataPoints = type.getMaxDataPoints(taskScheduler.getRefreshRate());
        // Only observer can get access to controller to pass data.
        chartsSection.setMaxDataPoints(maxDataPoints);

        initHistory();
    }

    protected void initHistory() {
        if (previewPortIndex < 0 || historyType == HistoryType.CURRENT) {
            return;
        }

        if (portNumList != null) {
            short portNumber = portNumList.get(previewPortIndex);
            if (vfName != null) {
                historyTask =
                        vfPortCounterSubscriber.initVFPortCountersHistory(
                                vfName, lid, portNumber, historyType,
                                previewVfPortcounterHistoryCallback);
            } else {
                historyTask =
                        portCounterSubscriber.initPortCountersHistory(lid,
                                portNumber, historyType,
                                previewPortcounterHistoryCallback);
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
    public void onRefresh(final IProgressObserver observer) {
        taskScheduler.submitToBackground(new Runnable() {
            @Override
            public void run() {
                refresh(observer);
            }
        });
    }

    @Override
    public void setParentController(PerformanceTreeController parentController) {
        this.parentController = parentController;
    }

    protected void refresh(IProgressObserver observer) {
        try {
            String vfName = null;
            int lid = -1;
            Short[] ports = null;
            synchronized (this) {
                vfName = this.vfName;
                lid = this.lid;
                ports = this.portNumList.toArray(new Short[0]);
            }

            tableSection.clear();

            IPerformanceApi perfApi = taskScheduler.getPerformanceApi();
            if (vfName == null) {
                PortCountersBean[] res = new PortCountersBean[ports.length];
                for (int i = 0; i < res.length; i++) {
                    res[i] = perfApi.getPortCounters(lid, ports[i]);
                }
                portCounterCallback.onDone(res);
            } else {
                VFPortCountersBean[] res = new VFPortCountersBean[ports.length];
                for (int i = 0; i < res.length; i++) {
                    res[i] = perfApi.getVFPortCounters(vfName, lid, ports[i]);
                }
                vfPortCounterCallback.onDone(res);
            }
        } finally {
            observer.onFinish();
        }
    }

    protected void processPortCounters(PortCountersBean[] beanList) {
        tableSection.updateTable(beanList, previewPortIndex);
        if (previewPortIndex >= 0) {
            chartsSection.processPortCounters(beanList[previewPortIndex]);
        }
    }

    protected void processVFPortCounters(VFPortCountersBean[] beanList) {
        tableSection.updateTable(beanList, previewPortIndex);
        if (previewPortIndex >= 0) {
            chartsSection.processVFPortCounters(beanList[previewPortIndex]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.IPerfSubpageController#showNode(com.intel.stl
     * .ui.monitor.FVResourceNode)
     */
    @Override
    public void showNode(final FVResourceNode treeNode,
            final IProgressObserver observer) {
        int lastLid = -1;
        synchronized (this) {
            lastLid = lid;
        }
        if (treeNode.getId() == lastLid) {
            onRefresh(observer);
            return;
        }

        previewPortIndex = 0;
        taskScheduler.submitToBackground(new Runnable() {
            @Override
            public void run() {

                if (portCounterTask != null) {
                    portCounterSubscriber.deregisterPortCountersArray(
                            portCounterTask, portCounterCallback);
                    if (historyTask != null && !historyTask.isDone()) {
                        historyTask.cancel(true);
                    }
                    chartsSection.clear();
                }
                if (vfPortCounterTask != null) {
                    vfPortCounterSubscriber.deregisterVFPortCounters(
                            vfPortCounterTask, vfPortCounterCallback);
                    if (historyTask != null && !historyTask.isDone()) {
                        historyTask.cancel(true);
                    }
                    chartsSection.clear();
                }

                // Collect all of the port numbers associated with this node
                int lid = treeNode.getId();
                List<Short> portNumList = new ArrayList<Short>();
                for (FVResourceNode portNode : treeNode.getChildren()) {
                    if (portNode.getType() == TreeNodeType.ACTIVE_PORT) {
                        portNumList.add((short) portNode.getId());
                    }
                } // for

                // Clear the performance table
                tableSection.clear();

                // Register for the list of port counter beans associated
                // with the list
                // of port numbers
                FVResourceNode parent = treeNode.getParent();
                String vfName = null;
                if (parent.getType() == TreeNodeType.VIRTUAL_FABRIC) {
                    vfName = parent.getTitle();
                    vfPortCounterTask =
                            vfPortCounterSubscriber.registerVFPortCounters(
                                    vfName, lid, portNumList,
                                    vfPortCounterCallback);

                } else {
                    portCounterTask =
                            portCounterSubscriber.registerPortCounters(lid,
                                    portNumList, portCounterCallback);
                }

                synchronized (PerformanceNodeController.this) {
                    PerformanceNodeController.this.vfName = vfName;
                    PerformanceNodeController.this.lid = lid;
                    PerformanceNodeController.this.portNumList = portNumList;
                }
                previewPortIndex = 0;
                refresh(observer);
                initHistory();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.monitor.IPortSelectionListener#onSelect(int)
     */
    @Override
    public void onPortSelection(int rowIndex) {
        if (rowIndex != previewPortIndex) {
            if (historyTask != null && !historyTask.isDone()) {
                historyTask.cancel(true);
            }
            chartsSection.clear();
            previewPortIndex = rowIndex;
            initHistory();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.monitor.IPortSelectionListener#onJumpToPort(int)
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
     * @see com.intel.stl.ui.common.IPageController#getName()
     */
    @Override
    public String getName() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getDescription()
     */
    @Override
    public String getDescription() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getView()
     */
    @Override
    public Component getView() {
        return performanceView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getIcon()
     */
    @Override
    public ImageIcon getIcon() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#cleanup()
     */
    @Override
    public void cleanup() {
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
        for (ISectionController<?> section : sections) {
            section.clear();
        }

        if (taskScheduler != null) {
            if (portCounterTask != null) {
                portCounterSubscriber.deregisterPortCountersArray(
                        portCounterTask, portCounterCallback);
            }
            if (vfPortCounterTask != null) {
                vfPortCounterSubscriber.deregisterVFPortCounters(
                        vfPortCounterTask, vfPortCounterCallback);
            }
            if (historyTask != null && !historyTask.isDone()) {
                historyTask.cancel(true);
            }
        }

        lid = -1;
    }

    public void clearHistory() {
        chartsSection.clear();
        if (historyTask != null && !historyTask.isDone()) {
            historyTask.cancel(true);
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
