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
 *  File Name: PerformancePort.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.34.2.1  2015/05/17 18:30:42  jijunwan
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
 *  Archive Log:    Revision 1.34  2015/04/24 16:59:19  jypak
 *  Archive Log:    Fix for an issue with selecting an inactive port and then selecting an active port.
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/04/10 18:20:52  jypak
 *  Archive Log:    Fall back to previous way of displaying received/transmitted data in performance page(chart section, table section, counter (error) section).
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2015/04/10 11:46:46  jypak
 *  Archive Log:    Updates to make delta data and cumulative data in same unit.
 *  Archive Log:    For Port performance, the DataChartScaleGroupManager is already updating the unit based on upper value among received/transmitted delta data. Introduced UnitDescription as a wrapper class to grab the unit information to be passed to counter (error) section from chart section.
 *  Archive Log:    For Node performance, since we need to convert data for all the ports, the conversion is done in PerformanceTableSection. the units will be decided by the delta data, not the cumulative data because it's smaller. With this update, received delta/cumulative data will be in a same unit and transmitted delta/cumulative data will be in same unit. However, it is possible that received data and transmitted data can be in different units. The charts are in same unit because it goes down to a double precision but table section is all in integer, so, we don't necessarily want to make them always in a same unit for now.
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/04/03 21:06:27  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/02/24 19:20:49  jypak
 *  Archive Log:    Delete unnecessary PerformanceGraphSection.
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/02/24 14:23:20  jypak
 *  Archive Log:    1. Show Border, Alternating Rows control panel added to the PerformanceErrorsSection.
 *  Archive Log:    2. Undo change of Performance Chart Section title to "Performancefor port Performance subpage.
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/02/13 17:48:18  jijunwan
 *  Archive Log:    fixed a mistake in the code
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
 *  Archive Log:    Revision 1.24  2015/02/10 21:25:56  jypak
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
 *  Archive Log:    Revision 1.19  2014/10/27 20:58:13  jijunwan
 *  Archive Log:    adapt to use timestamp on FM side
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/10/21 16:38:29  fernande
 *  Archive Log:    Customization of Properties display (Show Options/Apply Options)
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/10/09 21:24:49  jijunwan
 *  Archive Log:    improvement on TreeNodeType:
 *  Archive Log:    1) Added icon to TreeNodeType
 *  Archive Log:    2) Rename PORT to ACTIVE_PORT
 *  Archive Log:    3) Removed NODE
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/10/09 12:35:09  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/09/15 15:24:32  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/09/02 19:24:29  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/08/26 15:15:27  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/07/11 19:26:54  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/06/27 22:22:22  jijunwan
 *  Archive Log:    added running indicator to Performance Subpages
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/06/26 15:00:15  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/06/06 13:31:06  jypak
 *  Archive Log:    Performance-Performance subpage updates.
 *  Archive Log:    1. Synchronize y-axis(range axis) bound for a group of charts (packet, data).
 *  Archive Log:    2. Auto conversion of y-axis label title and tick label based on the max value of data in the series.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/06/05 19:48:35  jijunwan
 *  Archive Log:    we will have problem to handler delta style port counters. changed code to force us use non-delta style port counters
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/05 17:34:58  jijunwan
 *  Archive Log:    integrate vFabric into performance pages
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/02 21:46:06  jijunwan
 *  Archive Log:    Fixed the issue with port number. portNum in PortInfoRecord is used only for switches. And for HFI and Router, we should use localPortNum instead.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/30 21:59:10  jijunwan
 *  Archive Log:    moved all random generation to API side, and added a menu item to allow a user turn on/off randomization
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/29 22:06:40  jijunwan
 *  Archive Log:    support both delta and cumulative portCounters
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/29 03:57:55  jijunwan
 *  Archive Log:    fixed couple bugs
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/23 19:24:11  rjtierne
 *  Archive Log:    Renamed lastScheduledTask to portCounterTask.
 *  Archive Log:    Renamed lastHandler to portCounterCallback
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/21 14:45:55  rjtierne
 *  Archive Log:    Renamed from PerformanceSubpageController
 *  Archive Log:
 *
 *  Overview: This class is the controller for the Performance "Port" view which
 *  holds the Tx/Rx data and packet performance graphs, and error counters 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

import static com.intel.stl.ui.common.PageWeight.MEDIUM;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.swing.ImageIcon;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.PortCountersBean;
import com.intel.stl.api.performance.VFPortCountersBean;
import com.intel.stl.ui.common.IPerfSubpageController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.ISectionController;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.view.IDataTypeListener;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.monitor.view.PerformanceChartsSectionView;
import com.intel.stl.ui.monitor.view.PerformanceErrorsSectionView;
import com.intel.stl.ui.monitor.view.PerformanceView;
import com.intel.stl.ui.publisher.CallbackAdapter;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.intel.stl.ui.publisher.subscriber.PortCounterSubscriber;
import com.intel.stl.ui.publisher.subscriber.SubscriberType;
import com.intel.stl.ui.publisher.subscriber.VFPortCounterSubscriber;

public class PerformancePortController implements IPerfSubpageController {

    private TaskScheduler taskScheduler;

    @SuppressWarnings("unused")
    private Context context;

    private int lastLid = -1;

    private short lastPortNum = -1;

    private String lastVfName = null;

    private Task<PortCountersBean> portCounterTask;

    private ICallback<PortCountersBean> portCounterCallback;

    private ICallback<PortCountersBean> portCounterHistoryCallback;

    private Task<VFPortCountersBean> vfPortCounterTask;

    private ICallback<VFPortCountersBean> vfPortCounterCallback;

    private ICallback<VFPortCountersBean> vfPortCounterHistoryCallback;

    private final List<ISectionController<?>> sections;

    private PerformanceChartsSection graphSection;

    private PerformanceErrorsSection errorsSection;

    private final PerformanceView performancePortView;

    @SuppressWarnings("unused")
    private PerformanceTreeController parentController;

    private final MBassador<IAppEvent> eventBus;

    private PortCounterSubscriber portCounterSubscriber;

    private VFPortCounterSubscriber vfPortCounterSubscriber;

    private Future<Void> historyTask;

    private HistoryType historyType;

    private int maxDataPoints;

    public PerformancePortController(PerformanceView performancePortView,
            MBassador<IAppEvent> eventBus) {
        this.performancePortView = performancePortView;
        this.eventBus = eventBus;
        sections = getSections();
        List<JSectionView<?>> sectionViews = new ArrayList<JSectionView<?>>();
        for (ISectionController<?> section : sections) {
            sectionViews.add(section.getView());
        }
        performancePortView.installSectionViews(sectionViews);
    }

    protected List<ISectionController<?>> getSections() {
        List<ISectionController<?>> sections =
                new ArrayList<ISectionController<?>>();

        graphSection =
                new PerformanceChartsSection(new PerformanceChartsSectionView(
                        STLConstants.K0200_PERFORMANCE.getValue()), false,
                        eventBus);
        sections.add(graphSection);

        errorsSection =
                new PerformanceErrorsSection(
                        new PerformanceErrorsSectionView(), eventBus);
        sections.add(errorsSection);

        return sections;
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
        clear();
        this.context = context;

        taskScheduler = context.getTaskScheduler();

        // Get the port counter subscriber from the task scheduler
        portCounterSubscriber =
                (PortCounterSubscriber) taskScheduler
                        .getSubscriber(SubscriberType.PORT_COUNTER);

        // Get the virtual fabrics port counter subscriber from the task
        // scheduler
        vfPortCounterSubscriber =
                (VFPortCounterSubscriber) taskScheduler
                        .getSubscriber(SubscriberType.VF_PORT_COUNTER);

        final PerformanceChartsSectionView view = graphSection.getView();
        view.setHistoryTypeListener(new IDataTypeListener<HistoryType>() {
            @Override
            // Each time different port is selected, each chart will be
            // defaulted to show current. Only when different HistoryScope is
            // selected by user, chart will show different history range.
            public void onDataTypeChange(HistoryType type) {
                setHistoryType(type);
                view.setHistoryType(type);
            }
        });

        observer.onFinish();
    }

    private void setHistoryType(HistoryType type) {
        clearHistory();
        historyType = type;

        if (type == HistoryType.CURRENT) {
            maxDataPoints = PerformanceChartsSection.DEFAULT_DATA_POINTS;
            graphSection.setMaxDataPoints(maxDataPoints);
            return;
        }

        // For chart, use sweep interval, for history query, use refresh
        // rate to calculate maxDataPoints.
        maxDataPoints = type.getMaxDataPoints(taskScheduler.getRefreshRate());
        // Only observer can get access to controller to pass data.
        graphSection.setMaxDataPoints(maxDataPoints);

        initHistory();
    }

    protected void initHistory() {
        if (lastVfName != null) {
            createHistoryCallback(lastVfName);
            if (historyType != HistoryType.CURRENT && historyType != null) {
                historyTask =
                        vfPortCounterSubscriber.initVFPortCountersHistory(
                                lastVfName, lastLid, lastPortNum, historyType,
                                vfPortCounterHistoryCallback);
            }
        } else {
            createHistoryCallback();
            if (historyType != HistoryType.CURRENT && historyType != null) {
                historyTask =
                        portCounterSubscriber.initPortCountersHistory(lastLid,
                                lastPortNum, historyType,
                                portCounterHistoryCallback);
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
        int lid = -1;
        short portNum = -1;
        String vfName = null;
        synchronized (PerformancePortController.this) {
            lid = lastLid;
            portNum = lastPortNum;
            vfName = lastVfName;
        }

        try {
            if (lid == -1 || portNum == -1) {
                return;
            }

            IPerformanceApi perfApi = taskScheduler.getPerformanceApi();
            if (vfName != null) {
                VFPortCountersBean res =
                        perfApi.getVFPortCounters(vfName, lid, portNum);
                vfPortCounterCallback.onDone(res);
            } else {
                PortCountersBean res = perfApi.getPortCounters(lid, portNum);
                portCounterCallback.onDone(res);
            }
        } finally {
            if (observer != null) {
                observer.onFinish();
            }
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
    public void showNode(final FVResourceNode node,
            final IProgressObserver observer) {
        // check node type, reject if it's the type we don't support
        if (node.getType() == TreeNodeType.ACTIVE_PORT) {
            taskScheduler.submitToBackground(new Runnable() {
                @Override
                public void run() {
                    try {
                        FVResourceNode parent = node.getParent();
                        int lid = parent.getId();
                        short portNum = (short) node.getId();
                        String vfName = null;
                        FVResourceNode group = parent.getParent();
                        if (group.getType() == TreeNodeType.VIRTUAL_FABRIC) {
                            vfName = group.getTitle();
                        }
                        if (lid != lastLid
                                || portNum != lastPortNum
                                || ((vfName != null) && !vfName
                                        .equals(lastVfName))) {
                            setPort(lid, portNum, vfName);
                        }
                        refresh(null);
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        observer.onFinish();
                    }
                }
            });
        }
    }

    protected synchronized void setPort(int lid, short portNum, String vfName) {
        if (portCounterTask != null || vfPortCounterTask != null) {
            clear();
        }

        // register to query PortCounters periodically
        if (vfName != null) {
            vfPortCounterCallback = createCallback(vfName);
            vfPortCounterTask =
                    vfPortCounterSubscriber.registerVFPortCounters(vfName, lid,
                            portNum, vfPortCounterCallback);
        } else {
            portCounterCallback = createCallback();
            portCounterTask =
                    portCounterSubscriber.registerPortCounters(lid, portNum,
                            portCounterCallback);
        }

        lastLid = lid;
        lastPortNum = portNum;
        lastVfName = vfName;
        initHistory();
    }

    protected ICallback<PortCountersBean> createCallback() {
        // update datasets
        // call PerformanceSubPageView to update view. this is actually
        // unnecessary beause when we update dataset
        // it will call the charts to update.

        portCounterCallback = new CallbackAdapter<PortCountersBean>() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * com.intel.hpc.stl.ui.publisher.CallBackAdapter#onDone(java.lang
             * .Object)
             */
            @Override
            public synchronized void onDone(PortCountersBean result) {
                if (result != null) {

                    graphSection.processPortCounters(result);
                    errorsSection.updateErrors(result);
                }
            }
        };

        return portCounterCallback;

    }

    protected ICallback<PortCountersBean> createHistoryCallback() {
        // update datasets
        // call PerformanceSubPageView to update view. this is actually
        // unnecessary beause when we update dataset
        // it will call the charts to update.

        portCounterHistoryCallback = new CallbackAdapter<PortCountersBean>() {
            @Override
            public synchronized void onDone(PortCountersBean result) {
                if (result != null) {
                    graphSection.processPortCounters(result);
                }
            }
        };

        return portCounterHistoryCallback;

    }

    protected ICallback<VFPortCountersBean> createCallback(String vfName) {
        // update datasets
        // call PerformanceSubPageView to update view. this is actually
        // unnecessary beause when we update dataset
        // it will call the charts to update.

        vfPortCounterCallback = new CallbackAdapter<VFPortCountersBean>() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * com.intel.hpc.stl.ui.publisher.CallBackAdapter#onDone(java.lang
             * .Object)
             */
            @Override
            public synchronized void onDone(VFPortCountersBean result) {
                if (result != null) {
                    graphSection.processVFPortCounters(result);
                    errorsSection.updateErrors(result);
                }
            }
        };

        return vfPortCounterCallback;

    }

    /**
     * 
     * <i>Description: Separate callback for history not to update error
     * section.
     * 
     * @param vfName
     * @return
     */
    protected ICallback<VFPortCountersBean> createHistoryCallback(String vfName) {
        vfPortCounterHistoryCallback =
                new CallbackAdapter<VFPortCountersBean>() {

                    @Override
                    public synchronized void onDone(VFPortCountersBean result) {
                        if (result != null) {
                            graphSection.processVFPortCounters(result);
                        }
                    }
                };

        return vfPortCounterHistoryCallback;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getName()
     */
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getDescription()
     */
    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getView()
     */
    @Override
    public PerformanceView getView() {
        return performancePortView;
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
        for (ISectionController<?> section : sections) {
            section.clear();
        }

        if (taskScheduler != null) {
            if (portCounterTask != null) {
                portCounterSubscriber.deregisterPortCounters(portCounterTask,
                        portCounterCallback);
            }
            if (vfPortCounterTask != null) {
                vfPortCounterSubscriber.deregisterVFPortCounters(
                        vfPortCounterTask, vfPortCounterCallback);
            }
            if (historyTask != null && !historyTask.isDone()) {
                historyTask.cancel(true);
            }
        }

        lastLid = lastPortNum = -1;
    }

    public void clearHistory() {
        graphSection.clear();
        if (taskScheduler != null) {
            if (historyTask != null && !historyTask.isDone()) {
                historyTask.cancel(true);
            }
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
