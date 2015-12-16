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
 *  File Name: 
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/08/17 18:53:40  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/06/25 20:53:46  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - improved to share scale between all chart (include pinned chart) with the same data type
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/05/14 17:43:07  jijunwan
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
 *  Archive Log:    Revision 1.3  2015/04/10 11:46:46  jypak
 *  Archive Log:    Updates to make delta data and cumulative data in same unit.
 *  Archive Log:    For Port performance, the DataChartScaleGroupManager is already updating the unit based on upper value among received/transmitted delta data. Introduced UnitDescription as a wrapper class to grab the unit information to be passed to counter (error) section from chart section.
 *  Archive Log:    For Node performance, since we need to convert data for all the ports, the conversion is done in PerformanceTableSection. the units will be decided by the delta data, not the cumulative data because it's smaller. With this update, received delta/cumulative data will be in a same unit and transmitted delta/cumulative data will be in same unit. However, it is possible that received data and transmitted data can be in different units. The charts are in same unit because it goes down to a double precision but table section is all in integer, so, we don't necessarily want to make them always in a same unit for now.
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
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.intel.stl.ui.main.view.DataRateChartRangeUpdater;
import com.intel.stl.ui.main.view.IChartRangeUpdater;

public class DataRateChartScaleGroupManager extends
        ChartScaleGroupManager<TimeSeriesCollection> {
    private final static DataRateChartScaleGroupManager instance =
            new DataRateChartScaleGroupManager();

    public static DataRateChartScaleGroupManager getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.ChartScaleGroupManager#getChartRangeUpdater()
     */
    @Override
    IChartRangeUpdater getChartRangeUpdater() {
        if (rangeUpdater == null) {
            rangeUpdater = new DataRateChartRangeUpdater();
        }
        return rangeUpdater;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.ChartScaleGroupManager#getMin(org.jfree.data
     * .general.Dataset)
     */
    @Override
    long[] getMinMax(TimeSeriesCollection dataset) {
        long lower = Long.MAX_VALUE;
        long upper = Long.MIN_VALUE;

        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            TimeSeries series = dataset.getSeries(i);

            lower = (long) Math.min(lower, series.getMinY());
            upper = (long) Math.max(upper, series.getMaxY());
        }

        return new long[] { lower, upper };
    }

}
