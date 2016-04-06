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
 *  File Name: ConnectivityTableController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.42  2016/02/16 22:16:04  jijunwan
 *  Archive Log:    PR 132888 - Include Num Lanes Down in port counters display
 *  Archive Log:
 *  Archive Log:    - added Num Lanes Down
 *  Archive Log:
 *  Archive Log:    Revision 1.41  2016/02/09 20:23:08  jijunwan
 *  Archive Log:    PR 132575 - [PSC] Null pointer message in FM GUI
 *  Archive Log:
 *  Archive Log:    - some minor improvements
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2015/11/02 23:53:21  jijunwan
 *  Archive Log:    PR 131396 - Incorrect Connectivity Table for a VF port
 *  Archive Log:    - improved ConnectivityTableController to support VF port
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2015/08/17 18:53:41  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2015/08/04 23:00:34  jijunwan
 *  Archive Log:    PR 129821 - connectivity table has no Link Width Down Grade data
 *  Archive Log:    - added related data to data table
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2015/07/17 15:40:15  rjtierne
 *  Archive Log:    PR 129547 - Need to add Node type and lid to the Connectivity
 *  Archive Log:    In createTable() and createTableEntry(), passed node type to ConnectivityTableData()
 *  Archive Log:    so it will appear in the table
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2015/07/13 21:56:49  rjtierne
 *  Archive Log:    PR 129355 - Ability to click on cables to get cable info
 *  Archive Log:    Updated createTableEntry() to initialize the ConnectivityTableData with
 *  Archive Log:    the cable info initial value
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2015/06/10 21:07:21  jijunwan
 *  Archive Log:    PR 129120 - Some old files have no proper file header. They cannot record change logs
 *  Archive Log:    - manual correction on files that our tool cannot  identify
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2015/06/01 15:01:17  jypak
 *  Archive Log:    PR 128823 - Improve performance tables to include all portcounters fields.
 *  Archive Log:    All port counters fields added to performance table and connectivity table.
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/05/19 19:19:54  jijunwan
 *  Archive Log:    PR 128797 - Notice update failed to update related notes
 *  Archive Log:    - remove old node check. always redraw everything
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2015/05/14 15:02:04  rjtierne
 *  Archive Log:    PR 128682 - Set link quality indicator to "Unknown" on port error
 *  Archive Log:    When port counters bean is null, in createTableEntry(), set the link quality indicator to "Unknown"
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/05/14 13:22:25  rjtierne
 *  Archive Log:    When port counters bean is null, in createTableEntry(), set the link quality indicator to "Unknown"
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/05/13 17:15:20  rjtierne
 *  Archive Log:    In createTableEntry(), added null pointer protection to PortBeanCounters to
 *  Archive Log:    prevent dereferencing when null; as is the case when selecting a switch
 *  Archive Log:    which has no ports.
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/04/08 16:39:07  jijunwan
 *  Archive Log:    changed to valid link speed value
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/02/26 20:07:36  fisherma
 *  Archive Log:    Changes to display Link Quality data to port's Performance tab and switch/port configuration table.
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/02/05 21:21:45  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/02/04 21:44:17  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/02/02 15:38:27  rjtierne
 *  Archive Log:    New TaskScheduler architecture; now employs subscribers to submit
 *  Archive Log:    tasks for scheduling.  When update rate is changed on Wizard, TaskScheduler
 *  Archive Log:    uses this new architecture to terminate tasks and service and restart them.
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/01/21 21:19:11  rjtierne
 *  Archive Log:    Removed individual refresh rates for task registration. Now using
 *  Archive Log:    refresh rate supplied by user input in preferences wizard.
 *  Archive Log:    Reinitialization of scheduler service not yet implemented.
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/01/11 23:11:14  jijunwan
 *  Archive Log:    renamed PortUtils to Utils
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/01/11 21:30:24  jijunwan
 *  Archive Log:    adapt change on FM that uses port number 1 for HFI in link query
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/11/05 17:12:29  jijunwan
 *  Archive Log:    fixed a progress indication problem
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/11/05 16:20:20  jijunwan
 *  Archive Log:    only re-initialize when we have changes
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/10/22 01:28:02  jijunwan
 *  Archive Log:    Changed to use PortUtils
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/10/09 12:35:09  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/10/02 21:26:20  jijunwan
 *  Archive Log:    fixed issued found by FindBugs
 *  Archive Log:    Some auto-reformate
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/09/18 21:03:28  jijunwan
 *  Archive Log:    Added link (jump to) capability to Connectivity tables and PortSummary table
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/09/05 15:43:04  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/09/02 19:02:03  jijunwan
 *  Archive Log:    minor improvement
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/08/26 15:15:27  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/08/12 20:58:00  jijunwan
 *  Archive Log:    1) renamed HexUtils to StringUtils
 *  Archive Log:    2) added a method to StringUtils to get error message for an exception
 *  Archive Log:    3) changed all code to call StringUtils to get error message
 *  Archive Log:    4) some extra ode format change
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/08/05 18:39:02  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/08/05 17:57:05  jijunwan
 *  Archive Log:    fixed issues on ConnectivityTable to update performance data properly
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/07/29 15:46:04  rjtierne
 *  Archive Log:    Scheduled periodic Connectivity table updates
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/18 13:38:21  rjtierne
 *  Archive Log:    Added new methods showPathConnectivity() and createPathTable() to
 *  Archive Log:    process links in support of the topology path view
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/11 13:16:27  jypak
 *  Archive Log:    Added runtime, non runtime exceptions handler for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    As of now, all different exceptions are generally handled as 'Exception' but when we define how to handle differently for different exception, based on the error code, handler (catch block will be different). Also, we are thinking of centralized 'failure recovery' process to handle all exceptions in one place.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/10 15:11:01  rjtierne
 *  Archive Log:    Null-check observer before calling setProgress() - only required for initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/10 14:30:16  rjtierne
 *  Archive Log:    Commented out call to setProgress in setContext()
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/08 20:20:11  rjtierne
 *  Archive Log:    Turned off TEST_SLOW_LINKS
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/03 22:14:38  jijunwan
 *  Archive Log:    minor change for consistency
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/03 13:34:01  rjtierne
 *  Archive Log:    Fixed problem with wrong neighbor port numbers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/01 19:11:25  jijunwan
 *  Archive Log:    Had a separate ConnectivityTableControler, so we can reuse it
 *  Archive Log:
 *
 *  Overview:
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.Utils;
import com.intel.stl.api.configuration.LinkQuality;
import com.intel.stl.api.performance.PortCountersBean;
import com.intel.stl.api.performance.VFPortCountersBean;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetException;
import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.ConnectivityTableModel;
import com.intel.stl.ui.model.GraphEdge;
import com.intel.stl.ui.model.PortProperties;
import com.intel.stl.ui.monitor.ConnectivityTableData.PerformanceData;
import com.intel.stl.ui.publisher.CallbackAdapter;
import com.intel.stl.ui.publisher.CancellableCall;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.SingleTaskManager;
import com.intel.stl.ui.publisher.Task;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.intel.stl.ui.publisher.subscriber.PortCounterSubscriber;
import com.intel.stl.ui.publisher.subscriber.SubscriberType;
import com.intel.stl.ui.publisher.subscriber.VFPortCounterSubscriber;

public class ConnectivityTableController {

    private final static Logger log =
            LoggerFactory.getLogger(ConnectivityTableController.class);

    private static final boolean TEST_SLOW_LINKS = false;

    private ISubnetApi subnetApi;

    private final ConnectivityTableModel model;

    private final JXTable view;

    private TaskScheduler taskScheduler;

    private final Map<Point, PortSchedule<?>> schedules;

    private final SingleTaskManager taskMgr;

    private int currentLid;

    private String currentVFName;

    private short[] currentPorts;

    private LinkedHashMap<GraphEdge, Short> currentPaths;

    private PortCounterSubscriber portCounterSubscriber;

    private VFPortCounterSubscriber vfPortCounterSubscriber;

    /**
     * Description:
     *
     * @param subnetApi
     */
    public ConnectivityTableController(ConnectivityTableModel model,
            JXTable view) {
        super();
        this.model = model;
        this.view = view;
        schedules = new HashMap<Point, PortSchedule<?>>();
        taskMgr = new SingleTaskManager();
    }

    public void setContext(Context context, IProgressObserver observer) {
        subnetApi = context.getSubnetApi();
        taskScheduler = context.getTaskScheduler();

        // Get the port counter subscriber from the task scheduler
        portCounterSubscriber = (PortCounterSubscriber) taskScheduler
                .getSubscriber(SubscriberType.PORT_COUNTER);
        vfPortCounterSubscriber = (VFPortCounterSubscriber) taskScheduler
                .getSubscriber(SubscriberType.VF_PORT_COUNTER);

        clear();

        if (observer != null) {
            observer.onFinish();
        }
    }

    public synchronized void showConnectivity(int nodeLid, String vfName,
            IProgressObserver observer, short... portList) {
        clearScheduledTasks();
        currentLid = nodeLid;
        currentVFName = vfName;
        currentPorts = portList;
        refreshConnectivity(observer);
    }

    public synchronized void refreshConnectivity(
            final IProgressObserver observer) {
        CancellableCall<List<ConnectivityTableData>> caller =
                new CancellableCall<List<ConnectivityTableData>>() {
                    @Override
                    public List<ConnectivityTableData> call(
                            ICancelIndicator cancelIndicator) throws Exception {
                        List<ConnectivityTableData> data =
                                createTable(currentLid, currentVFName,
                                        cancelIndicator, currentPorts);
                        return data;
                    }
                };

        ICallback<List<ConnectivityTableData>> callback =
                new CallbackAdapter<List<ConnectivityTableData>>() {

                    /*
                     * (non-Javadoc)
                     *
                     * @see
                     * com.intel.stl.ui.publisher.CallbackAdapter#onDone(java
                     * .lang.Object)
                     */
                    @Override
                    public void onDone(List<ConnectivityTableData> result) {
                        if (result != null) {
                            updateTable(result);
                        }
                    }

                    /*
                     * (non-Javadoc)
                     *
                     * @see
                     * com.intel.stl.ui.publisher.CallbackAdapter#onError(java.
                     * lang.Throwable[])
                     */
                    @Override
                    public void onError(Throwable... errors) {
                        Util.showErrors(view, Arrays.asList(errors));
                    }

                    /*
                     * (non-Javadoc)
                     *
                     * @see
                     * com.intel.stl.ui.publisher.CallbackAdapter#onFinally()
                     */
                    @Override
                    public void onFinally() {
                        if (observer != null) {
                            observer.onFinish();
                        }
                    }

                };
        taskMgr.submit(caller, callback);
    }

    public synchronized void showPathConnectivity(
            LinkedHashMap<GraphEdge, Short> portMap, String vfName,
            IProgressObserver observer) {
        if (currentPaths != null && currentPaths.equals(portMap)) {
            return;
        }

        clearScheduledTasks();
        currentPaths = portMap;
        currentVFName = vfName;
        refreshPathConnectivity(observer);
    }

    public synchronized void refreshPathConnectivity(
            final IProgressObserver observer) {
        CancellableCall<List<ConnectivityTableData>> caller =
                new CancellableCall<List<ConnectivityTableData>>() {
                    @Override
                    public List<ConnectivityTableData> call(
                            ICancelIndicator cancelIndicator) throws Exception {
                        List<ConnectivityTableData> data = createPathTable(
                                currentPaths, currentVFName, cancelIndicator);
                        return data;
                    }
                };

        ICallback<List<ConnectivityTableData>> callback =
                new CallbackAdapter<List<ConnectivityTableData>>() {

                    /*
                     * (non-Javadoc)
                     *
                     * @see
                     * com.intel.stl.ui.publisher.CallbackAdapter#onDone(java
                     * .lang.Object)
                     */
                    @Override
                    public void onDone(List<ConnectivityTableData> result) {
                        if (result != null) {
                            updateTable(result);
                        }
                    }

                    /*
                     * (non-Javadoc)
                     *
                     * @see
                     * com.intel.stl.ui.publisher.CallbackAdapter#onFinally()
                     */
                    @Override
                    public void onFinally() {
                        if (observer != null) {
                            observer.onFinish();
                        }
                    }
                };
        taskMgr.submit(caller, callback);
    }

    /**
     *
     * Description:
     *
     * @param lid
     *            - node lid
     * @param portList
     *            - port numbers for a Switch, and local port numbers for a HFI
     * @return
     * @throws SubnetException
     * @throws SubnetDataNotFoundException
     */
    protected List<ConnectivityTableData> createTable(int lid, String vfName,
            ICancelIndicator indicator, short... portList)
                    throws SubnetException, SubnetDataNotFoundException {
        // TODO: improve performance by querying all ports once!!
        NodeRecordBean nodeBean = null;
        try {
            nodeBean = subnetApi.getNode(lid);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (nodeBean == null) {
            return null;
        }

        List<ConnectivityTableData> dataList =
                new ArrayList<ConnectivityTableData>();
        LinkRecordBean linkBean = null;
        for (short port : portList) {
            if (indicator != null && indicator.isCancelled()) {
                return null;
            }

            boolean isHFI = nodeBean.getNodeType() == NodeType.HFI;
            short portNum = port;
            boolean isActive;
            if (isHFI) {
                // it's very easy we have messed port number and local port
                // number for a HFI. So we do a check here to ensure we have
                // correct port number and local port number for HFI
                port = nodeBean.getNodeInfo().getLocalPortNum();
                portNum = 1;
                isActive = subnetApi.hasLocalPort(lid, port);
            } else {
                isActive = subnetApi.hasPort(lid, port);
            }
            // Add a partial record if the port is inactive
            if (!isActive) {
                ConnectivityTableData nodeData = new ConnectivityTableData(
                        nodeBean.getLid(), nodeBean.getNodeInfo().getNodeGUID(),
                        nodeBean.getNodeType(), port, false);
                nodeData.clear();
                nodeData.setNodeName(nodeBean.getNodeDesc());
                nodeData.setLinkState(STLConstants.K0524_INACTIVE.getValue());
                dataList.add(nodeData);
            } else {
                try {
                    linkBean = subnetApi.getLinkBySource(lid, portNum);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                // Add the data to the list
                PortRecordBean portBean =
                        subnetApi.getPortByPortNum(lid, portNum);
                ConnectivityTableData nodeData = createTableEntry(
                        dataList.size(), linkBean.getFromLID(), vfName, portNum,
                        portBean, linkBean, nodeBean, false);
                if (nodeData != null) {
                    // Update the slow link state
                    nodeData.setSlowLinkState(
                            Utils.isSlowPort(portBean.getPortInfo()));
                    dataList.add(nodeData);
                }

                NodeRecordBean nbrNodeBean = null;
                // Find all the same information for the neighboring node
                linkBean = subnetApi.getLinkByDestination(lid, portNum);
                nbrNodeBean = subnetApi.getNode(linkBean.getFromLID());
                portNum = linkBean.getFromPortIndex();
                portBean = subnetApi.getPortByPortNum(linkBean.getFromLID(),
                        portNum);
                nodeData = createTableEntry(dataList.size(),
                        linkBean.getFromLID(), vfName, portNum, portBean,
                        linkBean, nbrNodeBean, true);
                if (nodeData != null) {
                    // Update the slow link state
                    nodeData.setSlowLinkState(
                            Utils.isSlowPort(portBean.getPortInfo()));
                    dataList.add(nodeData);
                }
            } // else
        } // for

        return dataList;
    }

    /**
     *
     * Description:
     *
     * @param lid
     *            - node lid
     * @param portList
     *            - port numbers for a Switch, and local port numbers for a HFI
     * @return
     * @throws SubnetException
     * @throws SubnetDataNotFoundException
     */
    protected List<ConnectivityTableData> createPathTable(
            LinkedHashMap<GraphEdge, Short> portMap, String vfName,
            ICancelIndicator indicator)
                    throws SubnetException, SubnetDataNotFoundException {
        List<ConnectivityTableData> res =
                new ArrayList<ConnectivityTableData>();
        for (GraphEdge edge : portMap.keySet()) {
            if (indicator != null && indicator.isCancelled()) {
                return null;
            }

            int lid = edge.getFromLid();
            short port = portMap.get(edge);
            List<ConnectivityTableData> tableData =
                    createTable(lid, vfName, indicator, port);
            if (tableData != null) {
                res.addAll(tableData);
            }
        }
        return res;
    }

    /**
     *
     * Description
     *
     * @param lid
     * @param portNum
     *            - port number for a Switch, and local port number for a HFI
     * @param portBean
     * @param linkBean
     * @param nodeBean
     * @return
     */
    @SuppressWarnings("deprecation")
    private ConnectivityTableData createTableEntry(int index, int lid,
            String vfName, short portNum, PortRecordBean portBean,
            LinkRecordBean linkBean, NodeRecordBean nodeBean,
            boolean isNeighbor) {
        ConnectivityTableData nodeData = null;
        if (nodeBean.getNodeType() == NodeType.HFI) {
            portNum = 1; // we use local port number for display
        }

        // TODO This is just a test - remove it!
        if (TEST_SLOW_LINKS) {
            boolean isSwitch3Port7 =
                    (nodeBean.getNodeDesc().equals("MOOSE_STL_SWITCH3"))
                            && (portNum == 7);
            boolean isSwitch0Port7 =
                    (nodeBean.getNodeDesc().equals("MOOSE_STL_SWITCH0"))
                            && (portNum == 7);
            if (isSwitch3Port7 || isSwitch0Port7) {
                portBean.getPortInfo().setLinkSpeedActive((short) 0x80);
            }
        }

        PortProperties portProperties =
                new PortProperties(portBean, nodeBean, linkBean);

        nodeData = new ConnectivityTableData(nodeBean.getLid(),
                nodeBean.getNodeInfo().getNodeGUID(), nodeBean.getNodeType(),
                portNum, isNeighbor);
        nodeData.clear();

        nodeData.setNodeName(nodeBean.getNodeDesc());
        nodeData.setLinkState(portProperties.getState());
        nodeData.setPhysicalLinkState(portProperties.getPhysicalState());

        nodeData.setActiveLinkWidth(portProperties.getLinkWidthActive());
        nodeData.setEnabledLinkWidth(portProperties.getLinkWidthEnabled());
        nodeData.setSupportedLinkWidth(portProperties.getLinkWidthSupported());

        nodeData.setActiveLinkWidthDnGrdTx(
                portProperties.getLinkWidthDnGrdTx());
        nodeData.setActiveLinkWidthDnGrdRx(
                portProperties.getLinkWidthDnGrdRx());
        nodeData.setEnabledLinkWidthDnGrd(
                portProperties.getLinkWidthDnGrdEnabled());
        nodeData.setSupportedLinkWidthDnGrd(
                portProperties.getLinkWidthDnGrdSupported());

        nodeData.setActiveLinkSpeed(portProperties.getLinkSpeedActive());
        nodeData.setEnabledLinkSpeed(portProperties.getLinkSpeedEnabled());
        nodeData.setSupportedLinkSpeed(portProperties.getLinkSpeedSupported());

        // Give the cableInfo a value so the icon will appear
        nodeData.setCableInfo("");

        PortSchedule<?> schedule = null;
        if (vfName == null) {
            schedule =
                    schedulePortPerformanceTask(index, nodeData, lid, portNum);
        } else {
            schedule = scheduleVFPortPerformanceTask(index, nodeData, lid,
                    vfName, portNum);
        }
        schedule.refresh();

        return nodeData;
    }

    protected PortSchedule<PortCountersBean> schedulePortPerformanceTask(
            final int index, final ConnectivityTableData dataEntrty,
            final int lid, final short portNum) {
        ICallback<PortCountersBean> callback =
                new CallbackAdapter<PortCountersBean>() {
                    @Override
                    public synchronized void onDone(PortCountersBean pcBean) {
                        if (pcBean == null) {
                            log.error(STLConstants.K3047_PORT_BEAN_COUNTERS_NULL
                                    .getValue());
                            return;
                        }

                        final PerformanceData perfData = new PerformanceData();
                        perfData.setTxPackets(pcBean.getPortXmitPkts());
                        perfData.setRxPackets(pcBean.getPortRcvPkts());
                        perfData.setNumLinkRecoveries(
                                pcBean.getLinkErrorRecovery());
                        perfData.setNumLinkDown(pcBean.getLinkDowned());
                        perfData.setNumLanesDown(pcBean.getNumLanesDown());
                        perfData.setRxErrors(pcBean.getPortRcvErrors());
                        perfData.setRxRemotePhysicalErrors(
                                pcBean.getPortRcvRemotePhysicalErrors());
                        perfData.setTxDiscards(pcBean.getPortXmitDiscards());
                        perfData.setLocalLinkIntegrityErrors(
                                pcBean.getLocalLinkIntegrityErrors());
                        perfData.setExcessiveBufferOverruns(
                                pcBean.getExcessiveBufferOverruns());
                        perfData.setSwitchRelayErrors(
                                pcBean.getPortRcvSwitchRelayErrors());
                        perfData.setTxConstraints(
                                pcBean.getPortXmitConstraintErrors());
                        perfData.setRxConstraints(
                                pcBean.getPortRcvConstraintErrors());
                        // perfData.setVl15Dropped(???);
                        perfData.setPortRcvData(pcBean.getPortRcvData());
                        perfData.setPortXmitData(pcBean.getPortXmitData());

                        perfData.setFmConfigErrors(pcBean.getFmConfigErrors());
                        perfData.setPortMulticastRcvPkts(
                                pcBean.getPortMulticastRcvPkts());
                        perfData.setPortRcvFECN(pcBean.getPortRcvFECN());
                        perfData.setPortRcvBECN(pcBean.getPortRcvBECN());
                        perfData.setPortRcvBubble(pcBean.getPortRcvBubble());

                        perfData.setPortMulticastXmitPkts(
                                pcBean.getPortMulticastXmitPkts());
                        perfData.setPortXmitWait(pcBean.getPortXmitWait());
                        perfData.setPortXmitTimeCong(
                                pcBean.getPortXmitTimeCong());
                        perfData.setPortXmitWastedBW(
                                pcBean.getPortXmitWastedBW());
                        perfData.setPortXmitWaitData(
                                pcBean.getPortXmitWaitData());
                        perfData.setPortMarkFECN(pcBean.getPortMarkFECN());
                        perfData.setUncorrectableErrors(
                                pcBean.getUncorrectableErrors());
                        perfData.setSwPortCongestion(
                                pcBean.getSwPortCongestion());
                        final byte linkQuality =
                                pcBean.getLinkQualityIndicator();
                        Util.runInEDT(new Runnable() {
                            @Override
                            public void run() {
                                dataEntrty.setPerformanceData(perfData);
                                dataEntrty.setLinkQualityData(linkQuality);
                                if (model.getRowCount() > 0
                                        && index < model.getRowCount()) {
                                    model.fireTableRowsUpdated(index, index);
                                }
                            }
                        });
                    }
                };

        Task<PortCountersBean> task = portCounterSubscriber
                .registerPortCounters(lid, portNum, callback);
        PortSchedule<PortCountersBean> ps =
                new PortSchedule<PortCountersBean>(callback, task) {
                    @Override
                    public void refresh() {
                        PortCountersBean counters =
                                taskScheduler.getPerformanceApi()
                                        .getPortCounters(lid, portNum);
                        callback.onDone(counters);
                    }

                    @Override
                    public void clear() {
                        portCounterSubscriber.deregisterPortCounters(task,
                                callback);
                    }
                };

        synchronized (schedules) {
            schedules.put(new Point(lid, portNum), ps);
        }
        return ps;
    }

    protected PortSchedule<VFPortCountersBean> scheduleVFPortPerformanceTask(
            final int index, final ConnectivityTableData dataEntrty,
            final int lid, final String vfName, final short portNum) {
        ICallback<VFPortCountersBean> callback =
                new CallbackAdapter<VFPortCountersBean>() {
                    @Override
                    public synchronized void onDone(VFPortCountersBean pcBean) {
                        if (pcBean == null) {
                            log.error(STLConstants.K3047_PORT_BEAN_COUNTERS_NULL
                                    .getValue());
                            return;
                        }

                        final PerformanceData perfData = new PerformanceData();
                        perfData.setTxPackets(pcBean.getPortVFXmitPkts());
                        perfData.setRxPackets(pcBean.getPortVFRcvPkts());
                        perfData.setTxDiscards(pcBean.getPortVFXmitDiscards());
                        perfData.setPortRcvData(pcBean.getPortVFRcvData());
                        perfData.setPortXmitData(pcBean.getPortVFXmitData());

                        perfData.setPortRcvFECN(pcBean.getPortVFRcvFECN());
                        perfData.setPortRcvBECN(pcBean.getPortVFRcvBECN());
                        perfData.setPortRcvBubble(pcBean.getPortVFRcvBubble());

                        perfData.setPortXmitWait(pcBean.getPortVFXmitWait());
                        perfData.setPortXmitTimeCong(
                                pcBean.getPortVFXmitTimeCong());
                        perfData.setPortXmitWastedBW(
                                pcBean.getPortVFXmitWastedBW());
                        perfData.setPortXmitWaitData(
                                pcBean.getPortVFXmitWaitData());

                        Util.runInEDT(new Runnable() {
                            @Override
                            public void run() {
                                dataEntrty.setPerformanceData(perfData);
                                dataEntrty.setLinkQualityData(
                                        LinkQuality.UNKNOWN.getValue());
                                if (model.getRowCount() > 0
                                        && index < model.getRowCount()) {
                                    model.fireTableRowsUpdated(index, index);
                                }
                            }
                        });
                    }
                };

        Task<VFPortCountersBean> task = vfPortCounterSubscriber
                .registerVFPortCounters(vfName, lid, portNum, callback);
        PortSchedule<VFPortCountersBean> ps =
                new PortSchedule<VFPortCountersBean>(callback, task) {
                    @Override
                    public void refresh() {
                        VFPortCountersBean counters = taskScheduler
                                .getPerformanceApi()
                                .getVFPortCounters(vfName, lid, portNum);
                        callback.onDone(counters);
                    }

                    @Override
                    public void clear() {
                        vfPortCounterSubscriber.deregisterVFPortCounters(task,
                                callback);
                    }
                };

        synchronized (schedules) {
            schedules.put(new Point(lid, portNum), ps);
        }
        return ps;
    }

    protected synchronized void clearScheduledTasks() {
        synchronized (schedules) {
            for (PortSchedule<?> schedule : schedules.values()) {
                schedule.clear();
            }
            schedules.clear();
        }
    }

    protected void updateTable(final List<ConnectivityTableData> dataList) {
        Map<ConnectivityTableData, Integer> newDataMap =
                new HashMap<ConnectivityTableData, Integer>();
        for (int i = 0; i < dataList.size(); i++) {
            newDataMap.put(dataList.get(i), i);
        }
        int[] selRows = view.getSelectedRows();
        List<Integer> newSelRows = new ArrayList<Integer>();
        for (int i = 0; i < selRows.length; i++) {
            ConnectivityTableData data = model.getEntry(selRows[i]);
            Integer index = newDataMap.get(data);
            if (index != null) {
                newSelRows.add(index);
            }
        }
        model.setEntries(dataList);
        model.fireTableDataChanged();
        view.packAll();
        ListSelectionModel selModel = view.getSelectionModel();
        selModel.setValueIsAdjusting(true);
        for (int row : newSelRows) {
            selModel.addSelectionInterval(row, row);
        }
        selModel.setValueIsAdjusting(false);
    }

    public void clear() {
        clearScheduledTasks();
        model.clear();
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                model.fireTableDataChanged();
            }
        });

    }

    abstract class PortSchedule<E> {
        ICallback<E> callback;

        Task<E> task;

        /**
         * Description:
         *
         * @param callback
         * @param task
         */
        public PortSchedule(ICallback<E> callback, Task<E> task) {
            super();
            this.callback = callback;
            this.task = task;
        }

        public abstract void refresh();

        public abstract void clear();
    }
}
