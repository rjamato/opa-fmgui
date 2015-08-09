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
 *  File Name: PerformanceChartsSection.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.35.2.1  2015/05/17 18:30:42  jijunwan
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
 *  Archive Log:    Revision 1.35  2015/04/17 18:26:24  rjtierne
 *  Archive Log:    Added null pointer protection in processPortCounters() to prevent NPE when port is not available
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2015/04/17 16:13:49  jypak
 *  Archive Log:    Fix to use sweep time from image info for VF port counter performance charts.
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/04/16 19:32:22  jijunwan
 *  Archive Log:    put range update in EDT to sync with data update, so we will always update data first and then update range. This possibly will solve the issue reported in PR 126997
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2015/04/10 18:20:52  jypak
 *  Archive Log:    Fall back to previous way of displaying received/transmitted data in performance page(chart section, table section, counter (error) section).
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/04/10 11:46:46  jypak
 *  Archive Log:    Updates to make delta data and cumulative data in same unit.
 *  Archive Log:    For Port performance, the DataChartScaleGroupManager is already updating the unit based on upper value among received/transmitted delta data. Introduced UnitDescription as a wrapper class to grab the unit information to be passed to counter (error) section from chart section.
 *  Archive Log:    For Node performance, since we need to convert data for all the ports, the conversion is done in PerformanceTableSection. the units will be decided by the delta data, not the cumulative data because it's smaller. With this update, received delta/cumulative data will be in a same unit and transmitted delta/cumulative data will be in same unit. However, it is possible that received data and transmitted data can be in different units. The charts are in same unit because it goes down to a double precision but table section is all in integer, so, we don't necessarily want to make them always in a same unit for now.
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/04/01 19:54:05  jijunwan
 *  Archive Log:    fixed the following bugs
 *  Archive Log:    1) no link quality clear when we change preview port
 *  Archive Log:    2) loss data when we go from node performance view to port performance view and then go back to node performance view
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/04/01 16:43:09  jijunwan
 *  Archive Log:    update link quality immediately rather than waiting for the next time point
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/03/26 17:38:19  jypak
 *  Archive Log:    Online Help updates for additional panels.
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/03/10 18:43:12  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/02/26 20:07:36  fisherma
 *  Archive Log:    Changes to display Link Quality data to port's Performance tab and switch/port configuration table.
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/02/24 14:23:20  jypak
 *  Archive Log:    1. Show Border, Alternating Rows control panel added to the PerformanceErrorsSection.
 *  Archive Log:    2. Undo change of Performance Chart Section title to "Performancefor port Performance subpage.
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/02/18 17:30:46  jypak
 *  Archive Log:    PR 126999 Graph names are changed to include 'Delta' in the middle of the names. Also, added tool tips to the title label, so when a user hover the mouse to the title (for combo box selection of charts, hover on the label values), the explanation about the charts pops up.
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/02/17 16:53:32  jypak
 *  Archive Log:    PR 126997 The problem was that the graphs were updated before calculating new y axis max point to update y-axis scale. The fix is actually going back to previous implementation not to fire dataset change event before updating y-axis scale with new max value.
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/02/12 19:40:05  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/02/11 21:14:58  jypak
 *  Archive Log:    1. For 'current' history scope, default max data points need to be set.
 *  Archive Log:    2. History icon fixed.
 *  Archive Log:    3. Home Page performance section trend charts should show history scope selections.
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/02/06 20:49:36  jypak
 *  Archive Log:    1. TaskScheduler changed to handle two threads.
 *  Archive Log:    2. All four(VFInfo, VFPortCounters, GroupInfo, PortCounters) attributes history query related updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/02/04 21:44:17  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/11/05 17:17:26  jijunwan
 *  Archive Log:    adapt to use timestamp on FM side
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/10/27 20:58:13  jijunwan
 *  Archive Log:    adapt to use timestamp on FM side
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/09/15 15:24:32  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/08/26 14:31:16  jijunwan
 *  Archive Log:    added chart names
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/07/22 18:38:43  jijunwan
 *  Archive Log:    introduced DatasetDescription to support short name and full name (description) for a dataset
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/07/21 17:03:07  jijunwan
 *  Archive Log:    moved ChartsView and ChartsCard to common package
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/07/11 19:26:54  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/06/23 16:29:44  jijunwan
 *  Archive Log:    minor change on dataset notification
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/06/23 13:52:31  rjtierne
 *  Archive Log:    Add some exception handling and call addOrUpdate() in updateTrend()
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/06/09 21:41:39  jijunwan
 *  Archive Log:    applied ChartGroup to node performance subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/06/05 19:48:35  jijunwan
 *  Archive Log:    we will have problem to handler delta style port counters. changed code to force us use non-delta style port counters
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/05 17:34:58  jijunwan
 *  Archive Log:    integrate vFabric into performance pages
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/29 22:06:40  jijunwan
 *  Archive Log:    support both delta and cumulative portCounters
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/29 14:25:06  jijunwan
 *  Archive Log:    jfreechart dataset is not thread safe, put all dataset related operation into EDT, so they will synchronize
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/29 03:07:06  jijunwan
 *  Archive Log:    minor adjustment on performance subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/28 22:21:58  jijunwan
 *  Archive Log:    added port preview to performance subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/28 19:40:30  rjtierne
 *  Archive Log:    Added Tx/Rx Packet cards and initialized
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/21 14:44:53  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: This is the "Charts" section controller for the Performance "Node" 
 *  view which holds the Tx/Rx packets cards
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.engio.mbassy.bus.MBassador;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.intel.stl.api.configuration.LinkQuality;
import com.intel.stl.api.performance.PortCountersBean;
import com.intel.stl.api.performance.VFPortCountersBean;
import com.intel.stl.ui.common.BaseSectionController;
import com.intel.stl.ui.common.ChartsCard;
import com.intel.stl.ui.common.ICardController;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ChartsView;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.model.DatasetDescription;
import com.intel.stl.ui.monitor.DeltaConverter.Delta;
import com.intel.stl.ui.monitor.view.PerformanceChartsSectionView;

public class PerformanceChartsSection extends
        BaseSectionController<ISectionListener, PerformanceChartsSectionView> {
    private short portNum = -1;

    public final static int DEFAULT_DATA_POINTS = 10;

    private volatile int maxDataPoints = DEFAULT_DATA_POINTS;

    private TimeSeries rxPacketsTimeSeries;

    private TimeSeries txPacketsTimeSeries;

    private TimeSeries rxDataTimeSeries;

    private TimeSeries txDataTimeSeries;

    private ChartsCard rxCard;

    private ChartsCard txCard;

    private final DeltaConverter rxPacketsDC = new DeltaConverter("rxPackets");

    private final DeltaConverter txPacketsDC = new DeltaConverter("txPackets");

    private final DeltaConverter rxDataDC = new DeltaConverter("rxData");

    private final DeltaConverter txDataDC = new DeltaConverter("txData");

    private long lastUpdateTime;

    private final PacketRateChartScaleGroupManager packetManager;

    private final DataRateChartScaleGroupManager dataManager;

    private final boolean isNode;

    public PerformanceChartsSection(PerformanceChartsSectionView view,
            boolean isNode, MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        this.isNode = isNode;
        packetManager = new PacketRateChartScaleGroupManager();
        dataManager = new DataRateChartScaleGroupManager();

        initRxCard();
        initTxCard();

        HelpAction helpAction = HelpAction.getInstance();
        if (isNode) {
            helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                    helpAction.getNodePerf(), helpAction.getHelpSet());
        } else {
            helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                    helpAction.getPortPerf(), helpAction.getHelpSet());
        }
    }

    private void initRxCard() {
        List<DatasetDescription> lst = new ArrayList<DatasetDescription>();

        String name = STLConstants.K0828_REC_PACKETS_RATE.getValue();
        rxPacketsTimeSeries = new TimeSeries(name);
        TimeSeriesCollection packDataset = new TimeSeriesCollection();
        packDataset.addSeries(rxPacketsTimeSeries);
        DatasetDescription rxPacketsDd =
                new DatasetDescription(name, packDataset);
        lst.add(rxPacketsDd);

        name = STLConstants.K0830_REC_DATA_RATE.getValue();
        rxDataTimeSeries = new TimeSeries(name);
        TimeSeriesCollection dataDataset = new TimeSeriesCollection();
        dataDataset.addSeries(rxDataTimeSeries);
        DatasetDescription rxDataDd = new DatasetDescription(name, dataDataset);
        lst.add(rxDataDd);

        ChartsView chartsView = view.getRxCardView();

        HelpAction helpAction = HelpAction.getInstance();
        if (isNode) {
            helpAction.getHelpBroker().enableHelpOnButton(
                    chartsView.getHelpButton(), helpAction.getNodeRcvPkts(),
                    helpAction.getHelpSet());
        } else {
            helpAction.getHelpBroker().enableHelpOnButton(
                    chartsView.getHelpButton(), helpAction.getPortRcvPkts(),
                    helpAction.getHelpSet());
        }

        rxCard = new ChartsCard(chartsView, eventBus, lst);
        rxPacketsDd.setFullName(STLConstants.K0832_RATE_MEANING.getValue());
        rxDataDd.setFullName(STLConstants.K0832_RATE_MEANING.getValue());

        packetManager.addChart(chartsView
                .getChart(STLConstants.K0828_REC_PACKETS_RATE.getValue()),
                packDataset);
        dataManager.addChart(chartsView
                .getChart(STLConstants.K0830_REC_DATA_RATE.getValue()),
                dataDataset);
    }

    private void initTxCard() {
        List<DatasetDescription> lst = new ArrayList<DatasetDescription>();

        String name = STLConstants.K0829_TRAN_PACKETS_RATE.getValue();
        txPacketsTimeSeries = new TimeSeries(name);
        TimeSeriesCollection packDataset = new TimeSeriesCollection();
        packDataset.addSeries(txPacketsTimeSeries);
        DatasetDescription txPacketsDd =
                new DatasetDescription(name, packDataset);
        lst.add(txPacketsDd);

        name = STLConstants.K0831_TRAN_DATA_RATE.getValue();
        txDataTimeSeries = new TimeSeries(name);
        TimeSeriesCollection dataDataset = new TimeSeriesCollection();
        dataDataset.addSeries(txDataTimeSeries);
        DatasetDescription txDataDd = new DatasetDescription(name, dataDataset);
        lst.add(txDataDd);

        ChartsView chartsView = view.getTxCardView();

        HelpAction helpAction = HelpAction.getInstance();
        if (isNode) {
            helpAction.getHelpBroker().enableHelpOnButton(
                    chartsView.getHelpButton(), helpAction.getNodeTranPkts(),
                    helpAction.getHelpSet());
        } else {
            helpAction.getHelpBroker().enableHelpOnButton(
                    chartsView.getHelpButton(), helpAction.getPortTranPkts(),
                    helpAction.getHelpSet());
        }

        txCard = new ChartsCard(view.getTxCardView(), eventBus, lst);
        txPacketsDd.setFullName(STLConstants.K0832_RATE_MEANING.getValue());
        txDataDd.setFullName(STLConstants.K0832_RATE_MEANING.getValue());

        packetManager.addChart(chartsView
                .getChart(STLConstants.K0829_TRAN_PACKETS_RATE.getValue()),
                packDataset);
        dataManager.addChart(chartsView
                .getChart(STLConstants.K0831_TRAN_DATA_RATE.getValue()),
                dataDataset);
    }

    /**
     * @param maxDataPoints
     *            the maxDataPoints to set
     */
    public void setMaxDataPoints(int maxDataPoints) {
        this.maxDataPoints = maxDataPoints;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.ISection#getCards()
     */
    @Override
    public ICardController<?>[] getCards() {
        return new ICardController[] { rxCard, txCard };
    }

    public synchronized void processPortCounters(PortCountersBean countersBean) {

        if (countersBean == null) {
            return;
        }

        Date time = countersBean.getTimestampDate();
        if (time.getTime() == lastUpdateTime) {
            return;
        }
        lastUpdateTime = time.getTime();

        try {
            if (isNode) {
                if (countersBean.getPortNumber() != portNum) {
                    portNum = countersBean.getPortNumber();
                    Util.runInEDT(new Runnable() {
                        @Override
                        public void run() {
                            view.setTitle(UILabels.STL60100_PORT_PREVIEW
                                    .getDescription(Integer.toString(portNum)));
                        }
                    });
                }
            }

            if (countersBean.hasUnexpectedClear()) {
                clear();
            }
            if (countersBean.isDelta()) {
                // it will be complicate to handle delta style data. To get
                // correct
                // cumulative data, we need the initial data when we clear
                // counters.
                // without a database that keeps tracking data from the very
                // beginning, this is almost impossible. Plus we also need
                // to clear it when a user click clear counter button. So it's
                // better to let FM to handle it and we force ourselves to use
                // no delta style port counter data
                throw new IllegalArgumentException(
                        "We do not support delta style PortCounters");
            } else {
                long rcvPkts = countersBean.getPortRcvPkts();
                Delta delta = rxPacketsDC.addValue(rcvPkts, time);
                if (delta != null) {
                    updateTrend(rxPacketsTimeSeries, delta.getRate(),
                            delta.getTime());
                }
                long xmitPkts = countersBean.getPortXmitPkts();
                delta = txPacketsDC.addValue(xmitPkts, time);
                if (delta != null) {
                    updateTrend(txPacketsTimeSeries, delta.getRate(),
                            delta.getTime());
                }
                updatePacketsRange();

                long rcvData = countersBean.getPortRcvData();
                delta = rxDataDC.addValue(rcvData, time);
                if (delta != null) {
                    updateTrend(rxDataTimeSeries, delta.getRate(),
                            delta.getTime());
                }
                long xmitData = countersBean.getPortXmitData();
                delta = txDataDC.addValue(xmitData, time);
                if (delta != null) {
                    updateTrend(txDataTimeSeries, delta.getRate(),
                            delta.getTime());
                }
                updateDataRange();

                updateLinkQualityIcon(countersBean.getLinkQualityIndicator());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public synchronized void processVFPortCounters(
            VFPortCountersBean countersBean) {
        Date time = countersBean.getTimestampDate();
        if (time.getTime() == lastUpdateTime) {
            return;
        }
        lastUpdateTime = time.getTime();

        try {
            if (isNode) {
                if (countersBean.getPortNumber() != portNum) {
                    portNum = countersBean.getPortNumber();
                    Util.runInEDT(new Runnable() {
                        @Override
                        public void run() {
                            view.setTitle(UILabels.STL60100_PORT_PREVIEW
                                    .getDescription(Integer.toString(portNum)));
                        }
                    });
                }
            }

            if (countersBean.hasUnexpectedClear()) {
                clear();
            }
            if (countersBean.isDelta()) {
                // it will be complicate to handle delta style data. To get
                // correct
                // cumulative data, we need the initial data when we clear
                // counters.
                // without a database that keeps tracking data from the very
                // beginning, this is almost impossible. Plus we also need
                // to clear it when a user click clear counter button. So it's
                // better to let FM to handle it and we force ourselves to use
                // no delta style port counter data
                throw new IllegalArgumentException(
                        "We do not support delta style PortCounters");
            } else {
                long rcvPkts = countersBean.getPortVFRcvPkts();
                Delta delta = rxPacketsDC.addValue(rcvPkts, time);
                if (delta != null) {
                    updateTrend(rxPacketsTimeSeries, delta.getRate(),
                            delta.getTime());
                }
                long xmitPkts = countersBean.getPortVFXmitPkts();
                delta = txPacketsDC.addValue(xmitPkts, time);
                if (delta != null) {
                    updateTrend(txPacketsTimeSeries, delta.getRate(),
                            delta.getTime());
                }
                updatePacketsRange();

                long rcvData = countersBean.getPortVFRcvData();
                delta = rxDataDC.addValue(rcvData, time);
                if (delta != null) {
                    updateTrend(rxDataTimeSeries, delta.getRate(),
                            delta.getTime());
                }
                long xmitData = countersBean.getPortVFXmitData();
                delta = txDataDC.addValue(xmitData, time);
                if (delta != null) {
                    updateTrend(txDataTimeSeries, delta.getRate(),
                            delta.getTime());
                }
                updateDataRange();

                updateLinkQualityIcon(LinkQuality.UNKNOWN.getValue());
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    protected void updateTrend(final TimeSeries dataset, final Number value,
            final Date date) {
        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                dataset.setNotify(false);
                dataset.addOrUpdate(new Second(date), value);
                if (dataset.getItemCount() > maxDataPoints) {
                    dataset.delete(0, 0);
                }
                dataset.setNotify(true);
            }

        });
    }

    /**
     * 
     * Description: Set Max btn rx and tx to range bound at the same time.
     * 
     */
    public void updatePacketsRange() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                packetManager.updateChartsRange();
            }
        });
    }

    public void updateDataRange() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                dataManager.updateChartsRange();
            }
        });
    }

    public void updateLinkQualityIcon(byte linkQuality) {
        view.setLinkQualityValue(linkQuality);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.ISection#clear()
     */
    @Override
    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                rxPacketsTimeSeries.clear();
                rxDataTimeSeries.clear();
                txPacketsTimeSeries.clear();
                txDataTimeSeries.clear();
                if (isNode) {
                    view.setTitle(UILabels.STL60100_PORT_PREVIEW
                            .getDescription(""));
                }
                portNum = -1;
                rxPacketsDC.clear();
                txPacketsDC.clear();
                rxDataDC.clear();
                txDataDC.clear();
                view.clearQualityValue();
            }
        });
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
}
