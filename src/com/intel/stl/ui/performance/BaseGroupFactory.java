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

package com.intel.stl.ui.performance;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.PinDescription.PinID;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.HelpAction;
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
import com.intel.stl.ui.performance.item.IntegrityHistogramItem;
import com.intel.stl.ui.performance.item.IntegrityTopNItem;
import com.intel.stl.ui.performance.item.IntegrityTrendItem;

public class BaseGroupFactory {
    private static DataType[] ERR_DATA_TYPE = new DataType[] { DataType.ALL,
            DataType.INTERNAL, DataType.EXTERNAL };

    public static BaseGroupController createBandwidthGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0041_BANDWIDTH.getValue(),
                        new BWTrendItem(), new BWHistogramItem(),
                        new BWTopNItem(topN), DataType.values(),
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.PERF_BW);
        setUnitGroupHelp(res);
        return res;
    }

    public static BaseGroupController createPacketRateGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0065_PACKET_RATE.getValue(),
                        new PRTrendItem(), null, new PRTopNItem(topN),
                        DataType.values(), HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.PERF_PR);
        setUnitGroupHelp(res);
        return res;
    }

    private static void setUnitGroupHelp(BaseGroupController group) {
        HelpAction helpAction = HelpAction.getInstance();
        group.setHelpIDs(helpAction.getTrend(), helpAction.getHistogram(),
                helpAction.getTopN());
    }

    public static BaseGroupController createCongestionGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0043_CONGESTION_ERROR.getValue(),
                        new CGTrendItem(), new CGHistogramItem(),
                        new CGTopNItem(topN), ERR_DATA_TYPE,
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.PERF_CG);
        setErrorGroupHelp(res);
        return res;
    }

    public static BaseGroupController createSignalIntegrityGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0067_INTEGRITY_ERROR.getValue(),
                        new IntegrityTrendItem(), new IntegrityHistogramItem(),
                        new IntegrityTopNItem(topN), ERR_DATA_TYPE,
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.PERF_SI);
        setErrorGroupHelp(res);
        return res;
    }

    public static BaseGroupController createSmaCongestionGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0070_SMA_CONGESTION_ERROR.getValue(),
                        new SCTrendItem(), new SCHistogramItem(),
                        new SCTopNItem(topN), ERR_DATA_TYPE,
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.PERF_SC);
        setErrorGroupHelp(res);
        return res;
    }

    public static BaseGroupController createBubbleGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0487_BUBBLE_ERROR.getValue(),
                        new BBTrendItem(), new BBHistogramItem(),
                        new BBTopNItem(topN), ERR_DATA_TYPE,
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.PERF_BB);
        setErrorGroupHelp(res);
        return res;
    }

    public static BaseGroupController createSecurityGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0072_SECURITY.getValue(),
                        new SETrendItem(), new SEHistogramItem(),
                        new SETopNItem(topN), ERR_DATA_TYPE,
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.PERF_SE);
        setErrorGroupHelp(res);
        return res;
    }

    public static BaseGroupController createRoutingGroup(
            MBassador<IAppEvent> eventBus, int topN, DataType type,
            HistoryType historyType, GroupSource... sourceNames) {
        BaseGroupController res =
                new OptionBaseGroupController(eventBus,
                        STLConstants.K0074_ROUTING_ERROR.getValue(),
                        new RETrendItem(), new REHistogramItem(),
                        new RETopNItem(topN), ERR_DATA_TYPE,
                        HistoryType.values());
        res.setDataSources(sourceNames);
        res.setType(type);
        res.setHistoryType(historyType);
        res.setPinID(PinID.PERF_RT);
        setErrorGroupHelp(res);
        return res;
    }

    // when we have help for each counter type, we will do this in each
    // createXXXGroup method
    private static void setErrorGroupHelp(BaseGroupController group) {
        HelpAction helpAction = HelpAction.getInstance();
        group.setHelpIDs(helpAction.getErrorGroup(),
                helpAction.getErrorGroup(), helpAction.getErrorGroup());
    }
}
