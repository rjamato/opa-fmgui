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
 *  File Name: ScaleManager
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4.2.1  2015/08/12 15:26:58  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/10 11:46:46  jypak
 *  Archive Log:    Updates to make delta data and cumulative data in same unit.
 *  Archive Log:    For Port performance, the DataChartScaleGroupManager is already updating the unit based on upper value among received/transmitted delta data. Introduced UnitDescription as a wrapper class to grab the unit information to be passed to counter (error) section from chart section.
 *  Archive Log:    For Node performance, since we need to convert data for all the ports, the conversion is done in PerformanceTableSection. the units will be decided by the delta data, not the cumulative data because it's smaller. With this update, received delta/cumulative data will be in a same unit and transmitted delta/cumulative data will be in same unit. However, it is possible that received data and transmitted data can be in different units. The charts are in same unit because it goes down to a double precision but table section is all in integer, so, we don't necessarily want to make them always in a same unit for now.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/17 16:53:32  jypak
 *  Archive Log:    PR 126997 The problem was that the graphs were updated before calculating new y axis max point to update y-axis scale. The fix is actually going back to previous implementation not to fire dataset change event before updating y-axis scale with new max value.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/09 21:40:56  jijunwan
 *  Archive Log:    made Chart Group Manager more general
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/06 13:31:06  jypak
 *  Archive Log:    Performance-Performance subpage updates.
 *  Archive Log:    1. Synchronize y-axis(range axis) bound for a group of charts (packet, data).
 *  Archive Log:    2. Auto conversion of y-axis label title and tick label based on the max value of data in the series.
 *  Archive Log:
 *
 *  Overview: Adjust scales (x, y axis range or unit) for a chart.
 *            For now, adjust range (y axis range) based on historic data so that
 *                        
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.main.view.IChartRangeUpdater;

public abstract class ChartScaleGroupManager<E extends Dataset> {
    protected Map<JFreeChart, E> chartDataMap = new HashMap<JFreeChart, E>();

    protected long lower;

    protected long upper;

    protected IChartRangeUpdater rangeUpdater;

    public ChartScaleGroupManager() {
        rangeUpdater = getChartRangeUpdater();
    }

    public void addChart(JFreeChart chart, E dataset) {
        chartDataMap.put(chart, dataset);
    }

    /**
     * 
     * Description: Update relevant JFreeCharts using the IChartRangeUpdater.
     * The access to JFreeCharts is defined in actual implementation for now.
     * 
     */
    public void updateChartsRange() {
        calculateRangeBounds();
        // This manager should have all Charts update in a list.
        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                for (JFreeChart chart : chartDataMap.keySet()) {
                    rangeUpdater.updateChartRange(chart, lower, upper);
                }
            }
        });
    }

    /**
     * 
     * Description: Calculates min/max for all datasets registered.
     * 
     */
    protected void calculateRangeBounds() {
        long lower = Long.MAX_VALUE;
        long upper = Long.MIN_VALUE;

        for (E dataset : chartDataMap.values()) {
            long[] minMax = getMinMax(dataset);
            lower = Math.min(lower, minMax[0]);
            upper = Math.max(upper, minMax[1]);
        }

        this.lower = lower;
        this.upper = upper;
    }

    abstract IChartRangeUpdater getChartRangeUpdater();

    abstract long[] getMinMax(E dataset);
}
