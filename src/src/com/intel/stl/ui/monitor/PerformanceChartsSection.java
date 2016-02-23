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
 *  File Name: PerformanceChartsSection.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.41  2015/08/17 18:53:40  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2015/08/05 04:04:47  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - applied undo mechanism on Performance Page
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2015/06/25 20:50:02  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - applied pin framework on dynamic cards that can have different data sources
 *  Archive Log:    - change to use port counter performance item
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2015/06/09 18:37:21  jijunwan
 *  Archive Log:    PR 129069 - Incorrect Help action
 *  Archive Log:    - moved help action from view to controller
 *  Archive Log:    - only enable help button when we have HelpID
 *  Archive Log:    - fixed incorrect HelpIDs
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2015/05/14 19:19:23  jijunwan
 *  Archive Log:    PR 127700 - Delta data on host performance display is accumulating
 *  Archive Log:    - some code cleanup
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2015/05/14 17:43:07  jijunwan
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

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.BaseSectionController;
import com.intel.stl.ui.common.ICardController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.ObserverAdapter;
import com.intel.stl.ui.common.PinDescription.PinID;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.main.UndoHandler;
import com.intel.stl.ui.main.view.IDataTypeListener;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.monitor.view.PerformanceChartsSectionView;
import com.intel.stl.ui.performance.PortGroupController;
import com.intel.stl.ui.performance.PortSourceName;
import com.intel.stl.ui.performance.item.RxDataRateItem;
import com.intel.stl.ui.performance.item.RxPktRateItem;
import com.intel.stl.ui.performance.item.TxDataRateItem;
import com.intel.stl.ui.performance.item.TxPktRateItem;
import com.intel.stl.ui.performance.provider.DataProviderName;

public class PerformanceChartsSection extends
        BaseSectionController<ISectionListener, PerformanceChartsSectionView> {
    private final PortGroupController groupController;

    private UndoHandler undoHandler;

    /**
     * Description:
     * 
     * @param view
     * @param eventBus
     */
    public PerformanceChartsSection(PerformanceChartsSectionView view,
            boolean isNode, MBassador<IAppEvent> eventBus) {
        super(view, eventBus);

        RxPktRateItem rxPkt = new RxPktRateItem();
        RxDataRateItem rxData = new RxDataRateItem();
        TxPktRateItem txPkt = new TxPktRateItem();
        TxDataRateItem txData = new TxDataRateItem();
        groupController =
                new PortGroupController(eventBus,
                        STLConstants.K0200_PERFORMANCE.getValue(), rxPkt,
                        rxData, txPkt, txData, HistoryType.values());
        // no need to set origin because we have no group selection

        if (groupController.getRxCard() != null
                && groupController.getTxCard() != null) {
            view.installCardViews(groupController.getRxCard().getView(),
                    groupController.getTxCard().getView());
        }
        groupController.setSleepMode(false);

        HelpAction helpAction = HelpAction.getInstance();
        if (isNode) {
            setHelpID(helpAction.getNodePerf());
            groupController.setHelpIDs(helpAction.getNodeRcvPkts(),
                    helpAction.getNodeTranPkts());
        } else {
            setHelpID(helpAction.getPortPerf());
            groupController.setHelpIDs(helpAction.getPortRcvPkts(),
                    helpAction.getPortTranPkts());
        }
    }

    public void setPinID(PinID pinID) {
        groupController.setPinID(pinID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.ISectionController#getCards()
     */
    @Override
    public ICardController<?>[] getCards() {
        return groupController.getCards().toArray(new ICardController[0]);
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

    public void setContext(Context context, IProgressObserver observer) {
        if (observer == null) {
            observer = new ObserverAdapter();
        }
        groupController.setContext(context, observer);

        if (context != null && context.getController() != null) {
            undoHandler = context.getController().getUndoHandler();
        }

        view.setHistoryTypeListener(new IDataTypeListener<HistoryType>() {
            @Override
            // Each time different port is selected, each chart will be
            // defaulted to show current. Only when different HistoryScope is
            // selected by user, chart will show different history range.
            public void onDataTypeChange(HistoryType oldType,
                    HistoryType newType) {
                setHistoryType(newType);

                if (undoHandler != null && !undoHandler.isInProgress()) {
                    UndoableSectionHistorySelection sel =
                            new UndoableSectionHistorySelection(
                                    PerformanceChartsSection.this, oldType,
                                    newType);
                    undoHandler.addUndoAction(sel);
                }
            }
        });
        observer.onFinish();
    }

    public void onRefresh(IProgressObserver observer) {
        if (observer == null) {
            observer = new ObserverAdapter();
        }
        groupController.onRefresh(observer);
        observer.onFinish();
    }

    public void setSource(PortSourceName source) {
        if (source.getVfName() != null) {
            groupController.setDataProvider(DataProviderName.VF_PORT);
        } else {
            groupController.setDataProvider(DataProviderName.PORT);
        }
        groupController.setDataSources(new PortSourceName[] { source });
    }

    public void setHistoryType(HistoryType type) {
        groupController.setHistoryType(type);
        view.setHistoryType(type);
    }

    public void updateLinkQualityIcon(byte linkQuality) {
        view.setLinkQualityValue(linkQuality);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseSectionController#clear()
     */
    @Override
    public void clear() {
        super.clear();
        groupController.clear();
    }

}
