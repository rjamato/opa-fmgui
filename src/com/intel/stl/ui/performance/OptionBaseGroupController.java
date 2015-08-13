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
 *  File Name: FullController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.1  2015/08/12 15:26:41  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/17 23:22:18  jijunwan
 *  Archive Log:    PR 127106 - Suggest to use same bucket range for Group Err Summary as shown in "opatop" command to plot performance graphs in FV
 *  Archive Log:     - changed error histogram chart to bar chart to show the new data ranges: 0-25%, 25-50%, 50-75%, 75-100% and 100+%
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/05 21:35:39  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/03 21:12:33  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/15 15:24:30  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/22 18:41:42  jijunwan
 *  Archive Log:    added DataType support for chart view
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/21 17:03:05  jijunwan
 *  Archive Log:    moved ChartsView and ChartsCard to common package
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

import java.util.Map;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.ChartsCard;
import com.intel.stl.ui.common.view.ChartsView;
import com.intel.stl.ui.common.view.OptionChartsView;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.IDataTypeListener;
import com.intel.stl.ui.model.ChartGroup;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.model.DatasetDescription;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.performance.item.AbstractPerformanceItem;
import com.intel.stl.ui.performance.item.IPerformanceItem;
import com.intel.stl.ui.performance.item.TopNItem;
import com.intel.stl.ui.performance.item.TrendItem;

public class OptionBaseGroupController extends BaseGroupController {

    /**
     * Description:
     * 
     * @param eventBus
     * @param trendItem
     * @param histogramItem
     * @param topNItem
     * @param types
     */
    public OptionBaseGroupController(MBassador<IAppEvent> eventBus,
            String name, TrendItem trendItem,
            AbstractPerformanceItem histogramItem, TopNItem topNItem,
            DataType[] types, HistoryType[] historyTypes) {
        super(eventBus, name, trendItem, histogramItem, topNItem);
        if (group != null) {
            installTypes(types);
            installTimeScopes(historyTypes);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.BaseGroupController#createTrendCard(com.
     * intel.stl.ui.performance.item.IPerformanceItem, java.util.Map)
     */
    @Override
    protected ChartsCard createTrendCard(IPerformanceItem item,
            Map<String, DatasetDescription> map) {
        return createOptionCard(item, map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.BaseGroupController#createHistogramCard(
     * com.intel.stl.ui.performance.item.IPerformanceItem, java.util.Map)
     */
    @Override
    protected ChartsCard createHistogramCard(IPerformanceItem item,
            Map<String, DatasetDescription> map) {
        return createOptionCard(item, map);
    }

    protected ChartsCard createOptionCard(final IPerformanceItem item,
            Map<String, DatasetDescription> map) {
        String name = item.getName();
        final OptionChartsView view =
                new OptionChartsView(name, PerformanceChartsCreator.instance());
        view.setDataTypeListener(new IDataTypeListener<DataType>() {
            @Override
            public void onDataTypeChange(DataType type) {
                item.setType(type);
                view.setType(type);
            }
        });

        ChartsCard chartsCard = createChartsCard(view, map, name);
        // HistoryTypeListener is only needed for the TrendItem not
        // histogram/top n.
        if (item instanceof TrendItem) {
            view.setHistoryTypeListener(new IDataTypeListener<HistoryType>() {
                @Override
                public void onDataTypeChange(HistoryType type) {
                    // Get the refresh rate here and calculate the maxDataPoints
                    // here.
                    item.setHistoryType(type);
                    view.setHistoryType(type);
                }
            });
        }

        return chartsCard;
    }

    protected void installTypes(DataType... types) {
        ChartsView view = group.getChartView();
        if (view instanceof OptionChartsView) {
            ((OptionChartsView) view).setTypes(types);
        }

        for (ChartGroup member : group.getMembers()) {
            ChartsView mView = member.getChartView();
            if (mView != view && mView instanceof OptionChartsView) {
                ((OptionChartsView) mView).setTypes(types);
            }
        }
    }

    /**
     * 
     * Description:Only install time scopes for trend chart not history nor top
     * N charts.
     * 
     * @param types
     */
    protected void installTimeScopes(HistoryType... types) {
        ChartsView view = group.getChartView();
        if (view instanceof OptionChartsView) {
            ((OptionChartsView) view).setHistoryTypes(types);
        }
    }
}
