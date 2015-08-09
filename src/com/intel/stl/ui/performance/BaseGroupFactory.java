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
 *  File Name: FullViewFactory.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10  2015/03/27 15:48:34  jijunwan
 *  Archive Log:    changed K0072_SECURITY_ERROR to K0072_SECURITY
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/06 20:49:39  jypak
 *  Archive Log:    1. TaskScheduler changed to handle two threads.
 *  Archive Log:    2. All four(VFInfo, VFPortCounters, GroupInfo, PortCounters) attributes history query related updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/03 21:12:33  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/01/30 04:12:56  jijunwan
 *  Archive Log:    PR 126775 - "Bubble" error metric graph is not being plotted even though "opatop" shows bubble errors
 *  Archive Log:     - the chart used wrong data. corrected to use bubble counter
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/11 21:38:26  jijunwan
 *  Archive Log:    added bubble error charts on UI
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/15 15:24:30  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/22 18:41:42  jijunwan
 *  Archive Log:    added DataType support for chart view
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/21 17:30:44  jijunwan
 *  Archive Log:    renamed IDataObserver.Type to DataType, and put it under model package
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/16 21:38:05  jijunwan
 *  Archive Log:    added 3 type error counters
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 15:09:00  jijunwan
 *  Archive Log:    new framework for performance data visualization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.performance.item.BBHistogramItem;
import com.intel.stl.ui.performance.item.BBTopNItem;
import com.intel.stl.ui.performance.item.BBTrendItem;
import com.intel.stl.ui.performance.item.BWHistogramItem;
import com.intel.stl.ui.performance.item.BWTopNItem;
import com.intel.stl.ui.performance.item.BWTrendItem;
import com.intel.stl.ui.performance.item.CGHistogramItem;
import com.intel.stl.ui.performance.item.CGTopNItem;
import com.intel.stl.ui.performance.item.CGTrendItem;
import com.intel.stl.ui.performance.item.PRTopNItem;
import com.intel.stl.ui.performance.item.PRTrendItem;
import com.intel.stl.ui.performance.item.REHistogramItem;
import com.intel.stl.ui.performance.item.RETopNItem;
import com.intel.stl.ui.performance.item.RETrendItem;
import com.intel.stl.ui.performance.item.SCHistogramItem;
import com.intel.stl.ui.performance.item.SCTopNItem;
import com.intel.stl.ui.performance.item.SCTrendItem;
import com.intel.stl.ui.performance.item.SEHistogramItem;
import com.intel.stl.ui.performance.item.SETopNItem;
import com.intel.stl.ui.performance.item.SETrendItem;
import com.intel.stl.ui.performance.item.SIHistogramItem;
import com.intel.stl.ui.performance.item.SITopNItem;
import com.intel.stl.ui.performance.item.SITrendItem;

public class BaseGroupFactory {
    private static DataType[] ERR_DATA_TYPE = new DataType[] { DataType.ALL,
            DataType.INTERNAL, DataType.EXTERNAL };

    public static BaseGroupController createBandwidthGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, String... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0041_BANDWIDTH.getValue(),
                        new BWTrendItem(), new BWHistogramItem(),
                        new BWTopNItem(topN), DataType.values(),
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        return res;
    }

    public static BaseGroupController createPacketRateGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, String... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0065_PACKET_RATE.getValue(),
                        new PRTrendItem(), null, new PRTopNItem(topN),
                        DataType.values(), HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        return res;
    }

    public static BaseGroupController createCongestionGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, String... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0043_CONGESTION_ERROR.getValue(),
                        new CGTrendItem(), new CGHistogramItem(),
                        new CGTopNItem(topN), ERR_DATA_TYPE,
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        return res;
    }

    public static BaseGroupController createSignalIntegrityGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, String... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0067_SIGNAL_INTEGRITY_ERROR.getValue(),
                        new SITrendItem(), new SIHistogramItem(),
                        new SITopNItem(topN), ERR_DATA_TYPE,
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        return res;
    }

    public static BaseGroupController createSmaCongestionGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, String... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0070_SMA_CONGESTION_ERROR.getValue(),
                        new SCTrendItem(), new SCHistogramItem(),
                        new SCTopNItem(topN), ERR_DATA_TYPE,
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        return res;
    }

    public static BaseGroupController createBubbleGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, String... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0487_BUBBLE_ERROR.getValue(),
                        new BBTrendItem(), new BBHistogramItem(),
                        new BBTopNItem(topN), ERR_DATA_TYPE,
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        return res;
    }

    public static BaseGroupController createSecurityGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, String... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0072_SECURITY.getValue(),
                        new SETrendItem(), new SEHistogramItem(),
                        new SETopNItem(topN), ERR_DATA_TYPE,
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        return res;
    }

    public static BaseGroupController createRoutingGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, String... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0074_ROUTING_ERROR.getValue(),
                        new RETrendItem(), new REHistogramItem(),
                        new RETopNItem(topN), ERR_DATA_TYPE,
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        return res;
    }
}
