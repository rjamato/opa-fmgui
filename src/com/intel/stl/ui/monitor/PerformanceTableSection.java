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
 *  File Name: PerformanceTableSection.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.27.2.2  2015/08/12 15:26:58  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.27.2.1  2015/05/17 18:30:42  jijunwan
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
 *  Archive Log:    Revision 1.27  2015/04/17 18:28:15  rjtierne
 *  Archive Log:    In updateTable(), checking for dataList size > 0 in thread to prevent index
 *  Archive Log:    out of bounds exception when port not available
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/04/10 18:20:52  jypak
 *  Archive Log:    Fall back to previous way of displaying received/transmitted data in performance page(chart section, table section, counter (error) section).
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/04/10 11:46:46  jypak
 *  Archive Log:    Updates to make delta data and cumulative data in same unit.
 *  Archive Log:    For Port performance, the DataChartScaleGroupManager is already updating the unit based on upper value among received/transmitted delta data. Introduced UnitDescription as a wrapper class to grab the unit information to be passed to counter (error) section from chart section.
 *  Archive Log:    For Node performance, since we need to convert data for all the ports, the conversion is done in PerformanceTableSection. the units will be decided by the delta data, not the cumulative data because it's smaller. With this update, received delta/cumulative data will be in a same unit and transmitted delta/cumulative data will be in same unit. However, it is possible that received data and transmitted data can be in different units. The charts are in same unit because it goes down to a double precision but table section is all in integer, so, we don't necessarily want to make them always in a same unit for now.
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/04/08 19:45:03  rjtierne
 *  Archive Log:    Added Tx Data Rate
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/04/07 14:38:27  jypak
 *  Archive Log:    PR 126998 - Received/Transmitted data counters for Device Node and Device ports should show in MB rather than Flits. Fixed by converting units to Byte/KB/MB/GB. Also, tool tips were added to show the units for each value.
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/04/01 19:54:05  jijunwan
 *  Archive Log:    fixed the following bugs
 *  Archive Log:    1) no link quality clear when we change preview port
 *  Archive Log:    2) loss data when we go from node performance view to port performance view and then go back to node performance view
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/03/26 17:38:19  jypak
 *  Archive Log:    Online Help updates for additional panels.
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/03/05 22:32:13  fisherma
 *  Archive Log:    Added LinkQuality icon to Performance -> Performance tab table.
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/02/05 21:21:45  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/02/04 21:44:17  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/09/18 21:36:50  jijunwan
 *  Archive Log:    fixed a issue that incorrectly use portNum for rowIndex
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/09/18 21:03:28  jijunwan
 *  Archive Log:    Added link (jump to) capability to Connectivity tables and PortSummary table
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/09/15 15:24:32  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/07/11 19:26:54  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/06/16 21:02:08  jijunwan
 *  Archive Log:    added null check
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/06/05 19:48:35  jijunwan
 *  Archive Log:    we will have problem to handler delta style port counters. changed code to force us use non-delta style port counters
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/06/05 17:34:58  jijunwan
 *  Archive Log:    integrate vFabric into performance pages
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/06/03 20:49:39  rjtierne
 *  Archive Log:    Renamed list of PerformanceTableData from data to dataList.
 *  Archive Log:    Corrected error in createPortEntry() when initializing RxRemotePhysicalErrors
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/06/02 19:58:10  rjtierne
 *  Archive Log:    Revamped code to consider tx/rx cumulative and delta packets
 *  Archive Log:    and data on an individual port basis
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/05/30 21:18:34  rjtierne
 *  Archive Log:    Removed absolute value from delta calculations
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/05/30 20:47:25  rjtierne
 *  Archive Log:    Fixed comments above packet and data accumulator attributes
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/30 20:37:03  rjtierne
 *  Archive Log:    Calculated or set delta and cumulative data in PerformanceTableData
 *  Archive Log:    depending on whether querying for delta or cumulative port counts
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/29 14:25:06  jijunwan
 *  Archive Log:    jfreechart dataset is not thread safe, put all dataset related operation into EDT, so they will synchronize
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/28 22:21:58  jijunwan
 *  Archive Log:    added port preview to performance subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/28 17:50:02  rjtierne
 *  Archive Log:    Now using PerformanceXTableView instead of PerformanceTableView
 *  Archive Log:    to use the JXTable
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/23 19:26:53  rjtierne
 *  Archive Log:    Created PerformanceTableController and implemented methods
 *  Archive Log:    updateTable() and clear().
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/21 14:46:59  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: This is the "Table" section controller for the Performance "Node" 
 *  view which holds the performance table
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.performance.PortCountersBean;
import com.intel.stl.api.performance.VFPortCountersBean;
import com.intel.stl.ui.common.BaseSectionController;
import com.intel.stl.ui.common.ICardController;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.model.PerformanceTableModel;
import com.intel.stl.ui.monitor.view.PerformanceXTableView;

public class PerformanceTableSection extends
        BaseSectionController<ISectionListener, JSectionView<ISectionListener>> {

    /**
     * Performance Table Model
     */
    private final PerformanceTableModel tableModel;

    /**
     * Performance Table View
     */
    private final PerformanceXTableView tableView;

    /**
     * Port Data Accumulator
     */
    private PortDataAccumulator portDataAcc;

    /**
     * Map between a port number and its Port Data Accumulator
     */
    private final Map<Short, PortDataAccumulator> portDataAccMap =
            new HashMap<Short, PortDataAccumulator>();

    /**
     * Map between a port number and its latest data timestamp
     */
    private final Map<Short, Long> lastAccessMap = new HashMap<Short, Long>();

    private List<PerformanceTableData> currentDataList =
            new ArrayList<PerformanceTableData>();

    /**
     * Description:
     * 
     * @param view
     */
    public PerformanceTableSection(PerformanceTableModel tableModel,
            PerformanceXTableView tableView,
            JSectionView<ISectionListener> tableSectionView,
            MBassador<IAppEvent> eventBus) {
        super(tableSectionView, eventBus);
        this.tableModel = tableModel;
        this.tableView = tableView;

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(
                tableSectionView.getHelpButton(),
                helpAction.getPerfNodePortsTable(), helpAction.getHelpSet());
    }

    /**
     * 
     * Description: updates the table
     * 
     * @param event
     *            - event message
     */
    public void updateTable(PortCountersBean[] beanList,
            final int previewPortIndex) {
        final List<PerformanceTableData> dataList =
                new ArrayList<PerformanceTableData>();
        for (PortCountersBean bean : beanList) {
            if (bean != null) {
                PerformanceTableData portData = createPortEntry(bean);
                dataList.add(portData);
            }
        } // for

        currentDataList = dataList;
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                // TODO update table rather than replace it
                tableModel.setEntries(dataList);
                tableModel.fireTableDataChanged();
                if ((previewPortIndex >= 0) && (dataList.size() > 0)) {
                    tableView.setSelectedPort(previewPortIndex);
                }
            }
        });
    }

    public void updateTable(VFPortCountersBean[] beanList,
            final int previewPortIndex) {
        final List<PerformanceTableData> data =
                new ArrayList<PerformanceTableData>();
        for (VFPortCountersBean bean : beanList) {
            PerformanceTableData portData = createPortEntry(bean);
            data.add(portData);
        } // for
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                // TODO update table rather than replace it
                tableModel.setEntries(data);
                tableModel.fireTableDataChanged();
                if (previewPortIndex >= 0) {
                    tableView.setSelectedPort(previewPortIndex);
                }
            }
        });
    }

    /**
     * 
     * Description: Creates an data entry object for the performance table
     * 
     * @param bean
     *            - port counters bean associated with a given port
     * 
     * @return performance table data
     */
    public PerformanceTableData createPortEntry(PortCountersBean bean) {
        long rxDataRate = 0;
        long txDataRate = 0;
        PerformanceTableData portData =
                new PerformanceTableData(bean.getNodeLid());

        // If there is no entry in the accumulator map, create a new entry
        portDataAcc = portDataAccMap.get(bean.getPortNumber());
        if (portDataAcc == null) {
            portDataAcc = new PortDataAccumulator();
            portDataAccMap.put(bean.getPortNumber(), portDataAcc);
        }

        // If there is no entry in the first access map, create one
        if (lastAccessMap.get(bean.getPortNumber()) == null) {
            lastAccessMap.put(bean.getPortNumber(), bean.getTimestamp());
        }

        // If this bean has been unexpected cleared, reset this ports first
        // access status
        if (bean.hasUnexpectedClear()) {
            lastAccessMap.put(bean.getPortNumber(), bean.getTimestamp());
        }

        // Initialize port values
        portData.setPortNumber(bean.getPortNumber());
        portData.setPortRxRemotePhysicalErrors(bean
                .getPortRcvRemotePhysicalErrors());
        portData.setPortRxDataRate(rxDataRate);
        portData.setPortTxDataRate(txDataRate);
        portData.setPortRxSwitchRelayErrors(bean.getPortRcvSwitchRelayErrors());
        portData.setPortTxDiscards(bean.getPortXmitDiscards());
        portData.setExcessiveBufferOverruns(bean.getExcessiveBufferOverruns());
        portData.setFmConfigErrors(bean.getFmConfigErrors());
        portData.setLinkQuality(bean.getLinkQualityIndicator());

        if (bean.isDelta()) {
            // it will be complicate to handle delta style data. To get correct
            // cumulative data, we need the initial data when we clear counters.
            // without a database that keeps tracking data from the very
            // beginning, this is almost impossible. Plus we also need
            // to clear it when a user click clear counter button. So it's
            // better to let FM to handle it and we force ourselves to use
            // no delta style port counter data
            throw new IllegalArgumentException(
                    "We do not support delta style PortCounters");
        } else {
            long rxPackets = bean.getPortRcvPkts();
            long rxData = bean.getPortRcvData();
            long txPackets = bean.getPortXmitPkts();
            long txData = bean.getPortXmitData();

            // Clean calculation for each port entry.
            // Calculate the delta packets and data
            if (bean.getTimestamp() > lastAccessMap.get(bean.getPortNumber())) {
                long deltaTime =
                        bean.getTimestamp()
                                - lastAccessMap.get(bean.getPortNumber());
                portData.setPortRxPktsRate((rxPackets - portDataAcc
                        .getRxCumulativePacket()) / deltaTime);
                portData.setPortRxDataRate((rxData - portDataAcc
                        .getRxCumulativeData()) / deltaTime);
                portData.setPortTxPktsRate((txPackets - portDataAcc
                        .getTxCumulativePacket()) / deltaTime);
                portData.setPortTxDataRate((txData - portDataAcc
                        .getTxCumulativeData()) / deltaTime);
            }

            // Store the cumulative packets and data
            portData.setPortRxCumulativePkts(rxPackets);
            portData.setPortRxCumulativeData(rxData);
            portData.setPortTxCumulativePkts(txPackets);
            portData.setPortTxCumulativeData(txData);

            // Collect the most recent cumulative values
            portDataAcc.setRxCumulativePacket(rxPackets);
            portDataAcc.setRxCumulativeData(rxData);
            portDataAcc.setTxCumulativePacket(txPackets);
            portDataAcc.setTxCumulativeData(txData);
            lastAccessMap.put(bean.getPortNumber(), bean.getTimestamp());
        }

        // Update the map with the latest accumulators
        portDataAccMap.put(bean.getPortNumber(), portDataAcc);

        return portData;
    } // updatePerformanceTable

    public PerformanceTableData createPortEntry(VFPortCountersBean bean) {
        long rxDataRate = 0;
        long txDataRate = 0;
        PerformanceTableData portData =
                new PerformanceTableData(bean.getNodeLid());

        // If there is no entry in the accumulator map, create a new entry
        portDataAcc = portDataAccMap.get(bean.getPortNumber());
        if (portDataAcc == null) {
            portDataAcc = new PortDataAccumulator();
            portDataAccMap.put(bean.getPortNumber(), portDataAcc);
        }

        // If there is no entry in the first access map, create one
        if (lastAccessMap.get(bean.getPortNumber()) == null) {
            lastAccessMap.put(bean.getPortNumber(), bean.getTimestamp());
        }

        // If this bean has been unexpected cleared, reset this ports first
        // access status
        if (bean.hasUnexpectedClear()) {
            lastAccessMap.put(bean.getPortNumber(), bean.getTimestamp());
        }

        // Initialize port values
        portData.setPortNumber(bean.getPortNumber());
        portData.setPortRxRemotePhysicalErrors(-1);
        portData.setPortRxDataRate(rxDataRate);
        portData.setPortTxDataRate(txDataRate);
        portData.setPortRxSwitchRelayErrors(-1);
        portData.setPortTxDiscards(bean.getPortVFXmitDiscards());
        portData.setExcessiveBufferOverruns(-1);
        portData.setFmConfigErrors(-1);

        if (bean.isDelta()) {
            // it will be complicate to handle delta style data. To get correct
            // cumulative data, we need the initial data when we clear counters.
            // without a database that keeps tracking data from the very
            // beginning, this is almost impossible. Plus we also need
            // to clear it when a user click clear counter button. So it's
            // better to let FM to handle it and we force ourselves to use
            // no delta style port counter data
            throw new IllegalArgumentException(
                    "We do not support delta style PortCounters");
        } else {
            long rxPackets = bean.getPortVFRcvPkts();
            long rxData = bean.getPortVFRcvData();
            long txPackets = bean.getPortVFXmitPkts();
            long txData = bean.getPortVFXmitData();

            // Clean calculation for each port entry.
            // Calculate the delta packets and data
            if (bean.getTimestamp() > lastAccessMap.get(bean.getPortNumber())) {
                long deltaTime =
                        bean.getTimestamp()
                                - lastAccessMap.get(bean.getPortNumber());
                portData.setPortRxPktsRate((rxPackets - portDataAcc
                        .getRxCumulativePacket()) / deltaTime);
                portData.setPortRxDataRate((rxData - portDataAcc
                        .getRxCumulativeData()) / deltaTime);
                portData.setPortTxPktsRate((txPackets - portDataAcc
                        .getTxCumulativePacket()) / deltaTime);
                portData.setPortTxDataRate((txData - portDataAcc
                        .getTxCumulativeData()) / deltaTime);
            }

            // Store the cumulative packets and data
            portData.setPortRxCumulativePkts(rxPackets);
            portData.setPortRxCumulativeData(rxData);
            portData.setPortTxCumulativePkts(txPackets);
            portData.setPortTxCumulativeData(txData);

            // Collect the most recent cumulative values
            portDataAcc.setRxCumulativePacket(rxPackets);
            portDataAcc.setRxCumulativeData(rxData);
            portDataAcc.setTxCumulativePacket(txPackets);
            portDataAcc.setTxCumulativeData(txData);
            lastAccessMap.put(bean.getPortNumber(), bean.getTimestamp());
        }

        // Update the map with the latest accumulators
        portDataAccMap.put(bean.getPortNumber(), portDataAcc);

        return portData;
    } // updatePerformanceTable

    public List<PerformanceTableData> getCurrentDataList() {
        return currentDataList;
    }

    /**
     * Description: clear the table
     */
    @Override
    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                portDataAccMap.clear();
                lastAccessMap.clear();
                tableModel.clear();
                tableModel.fireTableDataChanged();
            }
        });
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

    /**
     * @return the tableView
     */
    public PerformanceXTableView getTableView() {
        return tableView;
    }

}
