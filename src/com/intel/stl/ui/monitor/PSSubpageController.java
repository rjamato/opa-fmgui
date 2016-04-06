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
 *  File Name: PSSubpageController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.58  2016/02/09 20:23:08  jijunwan
 *  Archive Log:    PR 132575 - [PSC] Null pointer message in FM GUI
 *  Archive Log:
 *  Archive Log:    - some minor improvements
 *  Archive Log:
 *  Archive Log:    Revision 1.57  2015/10/15 21:16:04  jijunwan
 *  Archive Log:    PR 131044 - Switch/HFI status can go beyond 100%
 *  Archive Log:    - changed to use larger number of nodes
 *  Archive Log:
 *  Archive Log:    Revision 1.56  2015/09/20 23:41:18  jijunwan
 *  Archive Log:    PR 130523 - Performance Event window reports negative when nodes are rebooted
 *  Archive Log:    - changed to use base line data
 *  Archive Log:
 *  Archive Log:    Revision 1.55  2015/08/17 18:53:41  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.54  2015/08/11 14:38:23  jijunwan
 *  Archive Log:    PR 129917 - No update on event statistics
 *  Archive Log:    - Apply event subscriber on the event card on Performance page
 *  Archive Log:    - fixed the blink chart issue
 *  Archive Log:
 *  Archive Log:    Revision 1.53  2015/08/06 17:19:41  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - improved GroupSelectedEvent to GroupsSelectedEvent to support selecting multiple groups
 *  Archive Log:    - fixed couple NPE issues
 *  Archive Log:
 *  Archive Log:    Revision 1.52  2015/08/06 13:18:10  jypak
 *  Archive Log:    PR 129707 - Device Types or Device Groups and All/Internal/External labels.
 *  Archive Log:    When disable irrelevant data types for different device types (All, HFI, SW etc.), set a default data type for the device type.
 *  Archive Log:
 *  Archive Log:    Revision 1.51  2015/08/05 04:04:47  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - applied undo mechanism on Performance Page
 *  Archive Log:
 *  Archive Log:    Revision 1.50  2015/06/25 20:50:02  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - applied pin framework on dynamic cards that can have different data sources
 *  Archive Log:    - change to use port counter performance item
 *  Archive Log:
 *  Archive Log:    Revision 1.49  2015/04/17 13:15:22  robertja
 *  Archive Log:    Resolve Klocwork critical issues around failing to check for null returned from a method.
 *  Archive Log:
 *  Archive Log:    Revision 1.48  2015/04/16 14:32:35  robertja
 *  Archive Log:    PR 126671 Initialize Performance page Events card in the absence of event-driven updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.47  2015/04/03 21:06:27  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.46  2015/03/16 14:41:25  jijunwan
 *  Archive Log:    renamed DevieGroup to DefaultDeviceGroup because it's an enum of default DGs, plus we need to use DeviceGroup for the DG definition used in opafm.xml
 *  Archive Log:
 *  Archive Log:    Revision 1.45  2015/02/13 23:05:38  jijunwan
 *  Archive Log:    PR 126911 - Even though HFI does not represent "Internal" data under opatop, FV still provides drop down for "Internal"
 *  Archive Log:     -- added a feature to be able to disable unsupported types
 *  Archive Log:
 *  Archive Log:    Revision 1.44  2015/02/04 21:44:17  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.43  2015/02/02 15:38:27  rjtierne
 *  Archive Log:    New TaskScheduler architecture; now employs subscribers to submit
 *  Archive Log:    tasks for scheduling.  When update rate is changed on Wizard, TaskScheduler
 *  Archive Log:    uses this new architecture to terminate tasks and service and restart them.
 *  Archive Log:
 *  Archive Log:    Revision 1.42  2014/11/05 23:02:57  jijunwan
 *  Archive Log:    added null point check
 *  Archive Log:
 *  Archive Log:    Revision 1.41  2014/10/28 15:10:23  robertja
 *  Archive Log:    Change Home page and Performance page status panel updates from poll-driven to event-driven.
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2014/10/22 16:40:15  jijunwan
 *  Archive Log:    separated other ports viz for the ports not in a subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2014/10/22 01:32:08  jijunwan
 *  Archive Log:    added other ports to UI
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2014/10/21 16:38:29  fernande
 *  Archive Log:    Customization of Properties display (Show Options/Apply Options)
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2014/10/15 22:00:22  jijunwan
 *  Archive Log:    display other ports on UI
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2014/10/13 15:07:30  jijunwan
 *  Archive Log:    fixed synchronization issues on performance charts
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2014/10/09 21:26:28  jijunwan
 *  Archive Log:    fixed a performance issue
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2014/10/09 12:35:09  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2014/09/15 15:24:32  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2014/09/02 19:24:29  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2014/08/26 15:15:27  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2014/08/15 21:46:38  jijunwan
 *  Archive Log:    adapter to the new GroupConfig and FocusPorts data structures
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2014/08/12 21:06:52  jijunwan
 *  Archive Log:    add null check
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2014/08/05 18:39:02  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2014/07/22 18:48:39  jijunwan
 *  Archive Log:    moved ChartsSectionView/ChartsSectionController to common package
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2014/07/21 16:28:38  jijunwan
 *  Archive Log:    integer format adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2014/07/21 13:48:17  jijunwan
 *  Archive Log:    added # internal, external ports on performance page
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2014/07/16 15:19:30  jijunwan
 *  Archive Log:    applied new performance framework and performance group viz to support bandwidth, packet rate, congestion and integrity data
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2014/07/11 19:27:33  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2014/07/11 13:16:27  jypak
 *  Archive Log:    Added runtime, non runtime exceptions handler for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    As of now, all different exceptions are generally handled as 'Exception' but when we define how to handle differently for different exception, based on the error code, handler (catch block will be different). Also, we are thinking of centralized 'failure recovery' process to handle all exceptions in one place.
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/07/01 14:44:37  jijunwan
 *  Archive Log:    added null check
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/06/27 22:22:22  jijunwan
 *  Archive Log:    added running indicator to Performance Subpages
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/06/27 11:54:38  jypak
 *  Archive Log:    INTEL_STL (Backend) UtilStatsBean bwBuckets is changed to a serializable Integer array to be cast to a Blob. The primitive int array cannot be cast to a Blob.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/06/26 15:00:15  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/06/23 05:00:12  jijunwan
 *  Archive Log:    minor improvement. Will fully fix the problem when we work on supporting different styles data - internal, external and send/transmit data.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/06/19 20:13:58  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/06/05 18:32:49  jijunwan
 *  Archive Log:    changed Channel Adapter to Fabric Interface
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/06/05 17:34:58  jijunwan
 *  Archive Log:    integrate vFabric into performance pages
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/05/30 21:59:10  jijunwan
 *  Archive Log:    moved all random generation to API side, and added a menu item to allow a user turn on/off randomization
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/05/29 03:57:55  jijunwan
 *  Archive Log:    fixed couple bugs
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/05/29 03:08:18  jijunwan
 *  Archive Log:    clear current treenode when we do clean()
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/05/23 19:30:09  rjtierne
 *  Archive Log:    Existing method clear() now overriding method as required by
 *  Archive Log:    the implemented interface.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/05/20 21:26:48  jijunwan
 *  Archive Log:    added events chart to performane subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/05/19 22:08:54  jijunwan
 *  Archive Log:    moved filter from EventCalculator to StateSummary, so we can have better consistent result
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/05/19 15:18:01  rjtierne
 *  Archive Log:    Changed Summary subpage tab name from Summary to Performance
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/18 22:48:16  rjtierne
 *  Archive Log:    Cleaned up showNode(). Added customStatesTask and callback
 *  Archive Log:    to update the event severity pie chart
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/16 04:30:38  jijunwan
 *  Archive Log:    Added code to deregister from task scheduler; Added Page Listener to listen enter or exit a (sub)page
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/15 22:37:37  rjtierne
 *  Archive Log:    Added event notice listener. Added logic to count the internal
 *  Archive Log:    management port for switches
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/15 18:46:54  rjtierne
 *  Archive Log:    Added functionality to show the distribution of nodes and
 *  Archive Log:    ports among switches, channel adapters, and routers.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/13 13:56:51  rjtierne
 *  Archive Log:    Removed unnecessary failed/skipped node/ports from
 *  Archive Log:    processImageInfo()
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/09 19:20:24  rjtierne
 *  Archive Log:    Renamed from PerfSummarySubpageController and completely
 *  Archive Log:    changed after MVC Refactoring
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/08 19:25:37  jijunwan
 *  Archive Log:    MVC refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/07 21:55:39  rjtierne
 *  Archive Log:    Created new callback processImageInfo() and registered
 *  Archive Log:    it with the task scheduler.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 21:23:07  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/02 21:52:29  rjtierne
 *  Archive Log:    Installed section views on the summary subpage view
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/01 16:33:50  rjtierne
 *  Archive Log:    Added Intel and CVS headers
 *  Archive Log:
 *
 *  Overview: Controller for the summary subpage view
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor;

import static com.intel.stl.ui.common.PageWeight.MEDIUM;

import java.awt.Component;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;

import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.api.performance.GroupConfigRspBean;
import com.intel.stl.api.performance.GroupInfoBean;
import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.VFConfigRspBean;
import com.intel.stl.api.subnet.DefaultDeviceGroup;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.ui.common.ChartsSectionController;
import com.intel.stl.ui.common.IPerfSubpageController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.ISectionController;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.event.GroupsSelectedEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.model.DevicesStatistics;
import com.intel.stl.ui.model.StateSummary;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.monitor.view.PSGraphSectionView;
import com.intel.stl.ui.monitor.view.PSInfoSectionView;
import com.intel.stl.ui.monitor.view.SummarySubpageView;
import com.intel.stl.ui.performance.GroupSource;
import com.intel.stl.ui.performance.provider.DataProviderName;
import com.intel.stl.ui.publisher.CallbackAdapter;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.IEventFilter;
import com.intel.stl.ui.publisher.IStateChangeListener;
import com.intel.stl.ui.publisher.Task;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.intel.stl.ui.publisher.subscriber.EventSubscriber;
import com.intel.stl.ui.publisher.subscriber.SubscriberType;

import net.engio.mbassy.bus.MBassador;

public class PSSubpageController
        implements IPerfSubpageController, IStateChangeListener {

    private final SummarySubpageView mSubpageView;

    private ISubnetApi subnetApi;

    private IPerformanceApi perfApi;

    private TaskScheduler mTaskScheduler;

    private final List<ISectionController<?>> mSections;

    private final Map<TreeNodeType, NodeType> deviceTypeMap =
            new HashMap<TreeNodeType, NodeType>();

    private final Map<TreeNodeType, DefaultDeviceGroup> deviceGroupMap =
            new HashMap<TreeNodeType, DefaultDeviceGroup>();

    private PSInfoSection mInfoSectionController;

    private DevicesStatistics dgStats;

    private final Object dgStatsLock = new Object();

    private ChartsSectionController mGraphSectionController;

    private Context mContext;

    private FVResourceNode selectedTreeNode;

    private final MBassador<IAppEvent> eventBus;

    private NodeType mNodeType;

    private Set<Integer> mNodes;

    private boolean isNewContext;

    private EventSubscriber eventSubscriber;

    private ICallback<StateSummary> stateSummaryCallback;

    private Task<StateSummary> stateSummaryTask;

    public PSSubpageController(SummarySubpageView pSubpageView,
            MBassador<IAppEvent> eventBus) {
        mSubpageView = pSubpageView;
        this.eventBus = eventBus;
        mSections = getSections();

        isNewContext = false;

        List<JSectionView<?>> sectionViews = new ArrayList<JSectionView<?>>();
        for (ISectionController<?> section : mSections) {
            sectionViews.add(section.getView());
        }
        mSubpageView.installSectionViews(sectionViews);

        // Initialize the device type and group maps
        deviceTypeMap.put(TreeNodeType.HCA_GROUP, NodeType.HFI);
        deviceTypeMap.put(TreeNodeType.SWITCH_GROUP, NodeType.SWITCH);

        deviceGroupMap.put(TreeNodeType.ALL, DefaultDeviceGroup.ALL);
        deviceGroupMap.put(TreeNodeType.HCA_GROUP, DefaultDeviceGroup.HFI);
        deviceGroupMap.put(TreeNodeType.SWITCH_GROUP, DefaultDeviceGroup.SW);
    }

    protected List<ISectionController<?>> getSections() {
        List<ISectionController<?>> sections =
                new ArrayList<ISectionController<?>>();

        mInfoSectionController =
                new PSInfoSection(new PSInfoSectionView(), eventBus);
        sections.add(mInfoSectionController);

        mGraphSectionController =
                new PSGraphSection(new PSGraphSectionView(), eventBus);
        sections.add(mGraphSectionController);

        return sections;
    }

    @Override
    public void setContext(Context pContext, IProgressObserver observer) {
        clear();
        uninstallEventMonitor();

        isNewContext = mContext == pContext;
        mContext = pContext;
        subnetApi = mContext.getSubnetApi();
        perfApi = mContext.getPerformanceApi();
        mTaskScheduler = mContext.getTaskScheduler();
        installEventMonitor();

        mGraphSectionController.setContext(pContext, observer);
        observer.onFinish();
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
        // page refresh comes from re-select a node. This method only applies
        // when we do a local refresh within this subpage. So far, no
        // requirement on this. So the following untested codes are commented
        // out

        // viewClear();
        //
        // if (selectedTreeNode != null) {
        // if (selectedTreeNode.getType() == TreeNodeType.VIRTUAL_FABRIC) {
        // processVFTreeNode(selectedTreeNode, observer);
        // } else {
        // processTreeNode(selectedTreeNode, observer);
        // }
        // }
        //
        // if (observer != null) {
        // observer.setProgress(1);
        // }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.common.IPageController#clear()
     */
    @Override
    public void clear() {
        for (ISectionController<?> section : mSections) {
            section.clear();
        }

        selectedTreeNode = null;
    }

    protected void installEventMonitor() {
        mContext.getEvtCal().addListener(this);

        eventSubscriber = (EventSubscriber) mTaskScheduler
                .getSubscriber(SubscriberType.EVENT);
        stateSummaryCallback = new CallbackAdapter<StateSummary>() {
            /*
             * (non-Javadoc)
             *
             * @see
             * com.intel.hpc.stl.ui.publisher.CallBackAdapter#onDone(java.lang
             * .Object)
             */
            @Override
            public synchronized void onDone(StateSummary result) {
                if (result != null) {
                    onStateChange(result);
                }
            }
        };
        stateSummaryTask =
                eventSubscriber.registerStateSummary(stateSummaryCallback);

    }

    protected void uninstallEventMonitor() {
        if (mContext != null && mContext.getEvtCal() != null) {
            mContext.getEvtCal().removeListener(this);
        }
        if (eventSubscriber != null && stateSummaryTask != null) {
            eventSubscriber.deregisterStateSummary(stateSummaryTask,
                    stateSummaryCallback);
        }
    }

    @Override
    public void setParentController(
            PerformanceTreeController parentController) {
    }

    /**
     *
     * Description: Populates the device group statistics and calls the
     * controller to update the screen
     *
     * @param groupList
     *            - list of GroupConfigBeans for the groups listed in the
     *            deviceList
     *
     * @param group
     *            - group config bean
     */
    private void processGroupConfig(List<GroupConfigRspBean> portList,
            long internalPorts, long externalPorts) {
        synchronized (dgStatsLock) {
            dgStats = getDevicesStats(portList);
            dgStats.setInternalPorts(internalPorts);
            dgStats.setExternalPorts(externalPorts);
        }

        // Update the view
        mInfoSectionController.updateStatistics(dgStats);
    }

    private void processVfConfig(List<VFConfigRspBean> portList,
            int internalPorts, int externalPorts) {
        synchronized (dgStatsLock) {
            dgStats = getVFDevicesStats(portList);
            dgStats.setInternalPorts(dgStats.getNumAtivePorts());
        }

        // Update the view
        mInfoSectionController.updateStatistics(dgStats);
    }

    /**
     * Description: Generate DeviceGroupStatistics based on the specified
     * GroupConfigBean
     *
     * @param portList
     *            - a list of port config bean
     *
     * @return devices statistics
     */
    private DevicesStatistics getDevicesStats(
            List<GroupConfigRspBean> portList) {
        DevicesStatistics res = new DevicesStatistics();
        Map<Integer, NodeType> nodeTypes = new HashMap<Integer, NodeType>();
        EnumMap<NodeType, Long> portsTypeDist =
                new EnumMap<NodeType, Long>(NodeType.class);
        EnumMap<NodeType, Integer> nodesTypeDist =
                new EnumMap<NodeType, Integer>(NodeType.class);
        long realNumPorts = 0;
        long desiredTotalPorts = 0;
        for (GroupConfigRspBean port : portList) {
            int lid = port.getPort().getNodeLid();
            NodeType nodeType = nodeTypes.get(lid);
            if (nodeType == null) {
                NodeRecordBean nrb = null;
                try {
                    nrb = subnetApi.getNode(lid);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (nrb == null) {
                    continue;
                }
                nodeType =
                        NodeType.getNodeType(nrb.getNodeInfo().getNodeType());
                nodeTypes.put(lid, nodeType);
                switch (nodeType) {
                    case SWITCH:
                        desiredTotalPorts +=
                                nrb.getNodeInfo().getNumPorts() + 1;
                        break;
                    case HFI:
                        desiredTotalPorts += 1;
                        break;
                    default:
                        break;
                }
                Integer nodesCount = nodesTypeDist.get(nodeType);
                nodesTypeDist.put(nodeType,
                        nodesCount == null ? 1 : nodesCount + 1);
            }
            Long portsCount = portsTypeDist.get(nodeType);
            portsTypeDist.put(nodeType,
                    portsCount == null ? 1 : portsCount + 1);
            realNumPorts += 1;
        } // for
        portsTypeDist.put(NodeType.OTHER, desiredTotalPorts - realNumPorts);

        res.setNodeTypesDist(nodesTypeDist);
        res.setNumNodes(nodeTypes.size());
        res.setPortTypesDist(portsTypeDist);
        res.setNumActivePorts(realNumPorts);
        return res;
    }

    private DevicesStatistics getVFDevicesStats(
            List<VFConfigRspBean> portList) {
        DevicesStatistics res = new DevicesStatistics();
        Map<Integer, NodeType> nodeTypes = new HashMap<Integer, NodeType>();
        EnumMap<NodeType, Long> portsTypeDist =
                new EnumMap<NodeType, Long>(NodeType.class);
        EnumMap<NodeType, Integer> nodesTypeDist =
                new EnumMap<NodeType, Integer>(NodeType.class);
        long realNumPorts = 0;
        long desiredTotalPorts = 0;
        for (VFConfigRspBean port : portList) {
            int lid = port.getPort().getNodeLid();
            NodeType nodeType = nodeTypes.get(lid);
            if (nodeType == null) {
                NodeRecordBean nrb = null;
                try {
                    nrb = subnetApi.getNode(lid);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (nrb == null) {
                    continue;
                }
                nodeType =
                        NodeType.getNodeType(nrb.getNodeInfo().getNodeType());
                nodeTypes.put(lid, nodeType);
                switch (nodeType) {
                    case SWITCH:
                        desiredTotalPorts +=
                                nrb.getNodeInfo().getNumPorts() + 1;
                        break;
                    case HFI:
                        desiredTotalPorts += 1;
                        break;
                    default:
                        break;
                }
                Integer nodesCount = nodesTypeDist.get(nodeType);
                nodesTypeDist.put(nodeType,
                        nodesCount == null ? 1 : nodesCount + 1);
            }
            Long portsCount = portsTypeDist.get(nodeType);
            portsTypeDist.put(nodeType,
                    portsCount == null ? 1 : portsCount + 1);
            realNumPorts += 1;
        } // for
        portsTypeDist.put(NodeType.OTHER, desiredTotalPorts - realNumPorts);

        res.setNodeTypesDist(nodesTypeDist);
        res.setNumNodes(nodeTypes.size());
        res.setPortTypesDist(portsTypeDist);
        res.setNumActivePorts(realNumPorts);
        return res;
    }

    private void processNewEvent(StateSummary summary, NodeType type,
            final Set<Integer> nodes) {
        if (summary == null) {
            return;
        }

        int total = 0;
        EnumMap<NoticeSeverity, Integer> severityMap = null;
        synchronized (dgStatsLock) {
            if (dgStats == null || dgStats.getNodeTypesDist() == null) {
                return;
            }

            EnumMap<NodeType, Integer> currenDist = dgStats.getNodeTypesDist();
            if (currenDist == null) {
                return;
            }

            if (type == NodeType.HFI) {
                total = Math.max(summary.getBaseTotalHFIs(),
                        currenDist.get(NodeType.HFI));
                severityMap = summary.getHfiStates();
            } else if (type == NodeType.SWITCH) {
                total = Math.max(summary.getBaseTotalSWs(),
                        currenDist.get(NodeType.SWITCH));
                severityMap = summary.getSwitchStates();
            } else if (selectedTreeNode != null
                    && selectedTreeNode.getType() == TreeNodeType.ALL) {
                total = Math.max(summary.getBaseTotalNodes(),
                        dgStats.getNumNodes());
                severityMap = summary.getStates(null);
            } else if (nodes != null && !nodes.isEmpty()) {
                total = nodes.size();
                severityMap = summary.getStates(new IEventFilter() {
                    @Override
                    public boolean accept(int nodeLid, NodeType nodeType) {
                        return nodes.contains(nodeLid);
                    }
                });
            }
        }
        mInfoSectionController.updateStates(severityMap, total);
    }

    @Override
    public String getName() {
        // Summary subpage tab has been renamed to Performance
        return STLConstants.K0200_PERFORMANCE.getValue();
    }

    @Override
    public String getDescription() {
        return STLConstants.K0412_SUMMARY_DESCRIPTION.getValue();
    }

    @Override
    public Component getView() {
        return mSubpageView;
    }

    @Override
    public ImageIcon getIcon() {
        return null;
    }

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
     * @see
     * com.intel.stl.ui.common.IPerfSubpageController#showNode(com.intel.stl
     * .ui.monitor.FVResourceNode)
     */
    @Override
    public void showNode(final FVResourceNode treeNode,
            final IProgressObserver observer) {

        if (perfApi == null || treeNode == null) {
            return;
        }

        boolean newNode = !treeNode.equals(selectedTreeNode);
        if (newNode) {
            clear();
            // Capture the selected tree node so the custom states callback
            // knows what kind of severity statistics to update
            selectedTreeNode = treeNode;
        }

        if (treeNode.getType() == TreeNodeType.VIRTUAL_FABRIC) {
            processVFTreeNode(treeNode, observer);
        } else {
            processTreeNode(treeNode, observer);
        }

        if (!newNode) {
            return;
        }
        // FIXME: need to handle synchronization
        mNodeType = getNodeType(treeNode);
        mNodes = mNodeType == null ? getNodes(treeNode) : null;

        if (isNewContext == true) {
            StateSummary summary = mContext.getEvtCal().getSummary();
            if (summary != null) {
                isNewContext = false;
                initEvents(summary);
            }
        }

        mGraphSectionController.setOrigin(new GroupsSelectedEvent(this,
                PerformancePage.NAME, treeNode.getTitle(), treeNode.getType()));
    }

    protected void processTreeNode(FVResourceNode treeNode,
            final IProgressObserver observer) {
        // Calculate node and port distribution across switches and channels
        // for the specified group
        DefaultDeviceGroup dg = deviceGroupMap.get(treeNode.getType());
        final String name = dg != null ? dg.getName() : treeNode.getTitle();
        mTaskScheduler.submitToBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    List<GroupConfigRspBean> group =
                            perfApi.getGroupConfig(name);
                    GroupInfoBean bean = perfApi.getGroupInfo(name);
                    if (group != null && bean != null) {
                        processGroupConfig(group, bean.getNumInternalPorts(),
                                bean.getNumExternalPorts());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    observer.onFinish();
                }
            }
        });

        TreeNodeType type = treeNode.getType();
        if (type == TreeNodeType.ALL) {
            mGraphSectionController.setDisabledDataTypes(DataType.ALL,
                    DataType.EXTERNAL, DataType.TRANSMIT, DataType.RECEIVE);
        } else if (type == TreeNodeType.HCA_GROUP) {
            mGraphSectionController.setDisabledDataTypes(DataType.ALL,
                    DataType.INTERNAL);
        } else if (type == TreeNodeType.DEVICE_GROUP) {
            if (treeNode.getTitle().equals(DefaultDeviceGroup.HFI.getName())) {
                mGraphSectionController.setDisabledDataTypes(DataType.ALL,
                        DataType.INTERNAL);
            } else if (treeNode.getTitle()
                    .equals(DefaultDeviceGroup.ALL.getName())) {
                mGraphSectionController.setDisabledDataTypes(DataType.ALL,
                        DataType.EXTERNAL, DataType.TRANSMIT, DataType.RECEIVE);
            } else {
                mGraphSectionController.setDisabledDataTypes(DataType.ALL,
                        (DataType[]) null);
            }
        } else {
            mGraphSectionController.setDisabledDataTypes(null,
                    (DataType[]) null);
        }
        mGraphSectionController.setDataProvider(DataProviderName.PORT_GROUP);
        mGraphSectionController.setSource(new GroupSource(name));
    }

    protected void processVFTreeNode(FVResourceNode treeNode,
            final IProgressObserver observer) {
        // Calculate node and port distribution across switches and channels
        // for the specified group
        final String name = treeNode.getTitle();
        mTaskScheduler.submitToBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    List<VFConfigRspBean> group = perfApi.getVFConfig(name);
                    if (group != null) {
                        processVfConfig(group, 0, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    observer.onFinish();
                }
            }
        });
        mGraphSectionController.setDisabledDataTypes(DataType.ALL,
                DataType.EXTERNAL, DataType.TRANSMIT, DataType.RECEIVE);
        mGraphSectionController
                .setDataProvider(DataProviderName.VIRTUAL_FABRIC);
        mGraphSectionController.setSource(new GroupSource(name));
    }

    protected NodeType getNodeType(FVResourceNode treeNode) {
        switch (treeNode.getType()) {
            case HCA_GROUP:
                return NodeType.HFI;
            case SWITCH_GROUP:
                return NodeType.SWITCH;
            case DEVICE_GROUP:
                DefaultDeviceGroup dg =
                        DefaultDeviceGroup.getType(treeNode.getTitle());
                if (dg != null) {
                    switch (dg) {
                        case SW:
                            return NodeType.SWITCH;
                        case HFI:
                        case TFI:
                            return NodeType.HFI;
                        default:
                            break;
                    }
                }
                break;
            default:
                break;
        }
        return null;
    }

    protected Set<Integer> getNodes(FVResourceNode treeNode) {
        Set<Integer> res = new HashSet<Integer>();
        for (FVResourceNode child : treeNode.getChildren()) {
            res.add(child.getId());
        }
        return res;
    }

    @Override
    public PageWeight getContextSwitchWeight() {
        return MEDIUM;
    }

    @Override
    public PageWeight getRefreshWeight() {
        return MEDIUM;
    }

    @Override
    public void onStateChange(StateSummary summary) {
        if (summary != null) {
            processNewEvent(summary, mNodeType, mNodes);
        }
    }

    @Override
    public String toString() {
        return "PSSubpageController";
    }

    private void initEvents(StateSummary summary) {
        int total = 1;
        try {
            ISubnetApi subnetApi = mContext.getSubnetApi();
            if (subnetApi != null) {
                total = subnetApi.getNodes(false).size();
            }
        } catch (SubnetDataNotFoundException e) {
            e.printStackTrace();
        }
        EnumMap<NoticeSeverity, Integer> severityMap = null;
        severityMap = summary.getStates(null);
        mInfoSectionController.updateStates(severityMap, total);
    }
}
