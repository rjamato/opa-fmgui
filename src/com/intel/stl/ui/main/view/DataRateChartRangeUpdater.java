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
 *  Archive Log:    Revision 1.1.2.4  2015/08/12 15:26:53  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.1.2.3  2015/05/21 21:09:45  jijunwan
 *  Archive Log:    PR 128855 - Incorrect value conversion on flits
 *  Archive Log:    - added 1 fit = 8 bytes to value conversion
 *  Archive Log:    - changed to use MB rather than MiB
 *  Archive Log:    - some code lean up
 *  Archive Log:
 *  Archive Log:    Revision 1.1.2.2  2015/05/17 18:30:45  jijunwan
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
 *  Archive Log:    Revision 1.7  2015/04/16 19:36:54  jijunwan
 *  Archive Log:    improved to handle the case where data value is larger than 100G
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/04/10 11:46:47  jypak
 *  Archive Log:    Updates to make delta data and cumulative data in same unit.
 *  Archive Log:    For Port performance, the DataChartScaleGroupManager is already updating the unit based on upper value among received/transmitted delta data. Introduced UnitDescription as a wrapper class to grab the unit information to be passed to counter (error) section from chart section.
 *  Archive Log:    For Node performance, since we need to convert data for all the ports, the conversion is done in PerformanceTableSection. the units will be decided by the delta data, not the cumulative data because it's smaller. With this update, received delta/cumulative data will be in a same unit and transmitted delta/cumulative data will be in same unit. However, it is possible that received data and transmitted data can be in different units. The charts are in same unit because it goes down to a double precision but table section is all in integer, so, we don't necessarily want to make them always in a same unit for now.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/13 14:45:34  jypak
 *  Archive Log:    PR 126996 The graphs are being correctly plotted. The tool tip contents for the graphs have been fixed to show same unit as the graphs.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/09 21:40:55  jijunwan
 *  Archive Log:    made Chart Group Manager more general
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/09 12:02:36  jypak
 *  Archive Log:    Perf-Perf subpage fixes.
 *  Archive Log:    1. Number of packets unit name changes.
 *  Archive Log:    2. When the converted upper value is less than the the predefined number of ticks (10), the tickSizeFactor should be set to 1
 *  Archive Log:        so that lower detailed tick value can be displayed.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/06 22:37:32  jypak
 *  Archive Log:    Perf-Perf subpage fixes.
 *  Archive Log:    1. When switch unit, tick label disappear.  In that case, needed to reset the standard tick to integer tick unit.
 *  Archive Log:    2. When tickSizeFactor is zero, set it to non-zero.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/06 13:31:05  jypak
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

package com.intel.stl.ui.main.view;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.monitor.UnitDescription;

public class DataRateChartRangeUpdater implements IChartRangeUpdater {

    private String unitStr = "";

    private String unitDes = "";

    private static final long KB = 1000;

    private static final long MB = 1000000;

    private static final long GB = 1000000000;

    private double tickUnitSize = 1L;

    @Override
    public void updateChartRange(JFreeChart chart, long lower, long upper) {
        XYPlot xyplot = (XYPlot) chart.getPlot();
        NumberAxis range = (NumberAxis) xyplot.getRangeAxis();
        range.setRangeWithMargins(lower, upper);
        range.setLowerBound(0);

        // If upper is less than 1000, don't do anything to convert the y-axis
        // label and
        // convert the unit tick.
        TickUnitSource unitSrc = createTickUnits(upper);
        if (unitSrc != null) {
            // Change tick values only if upper is above 1000.
            range.setStandardTickUnits(unitSrc);

            xyplot.getRenderer()
                    .setBaseToolTipGenerator(
                            new StandardXYToolTipGenerator(
                                    "<html><b>{0}</b><br> Time: {1}<br> Data: {2}</html>",
                                    new SimpleDateFormat("HH:mm:ss"),
                                    new DecimalFormat("#,##0.00" + " "
                                            + unitDes)) {

                                private static final long serialVersionUID =
                                        4825888117284967486L;

                                @Override
                                protected Object[] createItemArray(
                                        XYDataset dataset, int series, int item) {

                                    String nullXString = "null";
                                    String nullYString = "null";

                                    Object[] result = new Object[3];
                                    result[0] =
                                            dataset.getSeriesKey(series)
                                                    .toString();

                                    double x = dataset.getXValue(series, item);
                                    if (Double.isNaN(x)
                                            && dataset.getX(series, item) == null) {
                                        result[1] = nullXString;
                                    } else {
                                        DateFormat xDateFormat =
                                                this.getXDateFormat();
                                        if (xDateFormat != null) {
                                            result[1] =
                                                    xDateFormat
                                                            .format(new Date(
                                                                    (long) x));
                                        } else {

                                            result[1] =
                                                    this.getXFormat().format(x);
                                        }
                                    }

                                    double y = dataset.getYValue(series, item);
                                    if (Double.isNaN(y)
                                            && dataset.getY(series, item) == null) {
                                        result[2] = nullYString;
                                    } else {
                                        DateFormat yDateFormat =
                                                this.getYDateFormat();
                                        if (yDateFormat != null) {
                                            result[2] =
                                                    yDateFormat
                                                            .format(new Date(
                                                                    (long) y));
                                        } else {
                                            result[2] =
                                                    this.getYFormat().format(
                                                            y / tickUnitSize);
                                        }
                                    }
                                    return result;
                                }

                            });
        } else {
            range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        }
        // y-axis label
        setChartRangeLabel(range);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.view.IChartRange#setChartRangeLabel(org.jfree.chart
     * .axis.NumberAxis)
     */
    @Override
    public void setChartRangeLabel(NumberAxis range) {
        range.setLabel(unitStr);
    }

    @Override
    public UnitDescription getUnitDescription() {
        return new UnitDescription(unitStr, tickUnitSize);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IChartRange#createTickUnits(long)
     */
    @Override
    public TickUnitSource createTickUnits(long upper) {
        long upperOrig = upper * UIConstants.BYTE_PER_FLIT;
        long numberOfTicks = 10L;

        if (upperOrig < KB) {
            // If upper is less than 1024, don't change anything.
            unitStr = STLConstants.K0701_BYTE_PER_SEC.getValue();
            unitDes = STLConstants.K3300_BPS_DESCRIPTION.getValue();
            tickUnitSize = 1 / UIConstants.BYTE_PER_FLIT;
            return null;
        } else if (upperOrig < MB) {
            unitStr = STLConstants.K0702_KB_PER_SEC.getValue();
            unitDes = STLConstants.K3301_KBPS_DESCRIPTION.getValue();
            tickUnitSize = KB / UIConstants.BYTE_PER_FLIT;
        } else if (upperOrig < GB) {
            unitStr = STLConstants.K0703_MB_PER_SEC.getValue();
            unitDes = STLConstants.K3302_MBPS_DESCRIPTION.getValue();
            tickUnitSize = MB / UIConstants.BYTE_PER_FLIT;
        } else {
            unitStr = STLConstants.K0704_GB_PER_SEC.getValue();
            unitDes = STLConstants.K3303_GBPS_DESCRIPTION.getValue();
            tickUnitSize = GB / UIConstants.BYTE_PER_FLIT;
        }

        TickUnits units = new TickUnits();
        double unit =
                Math.max(upper / (numberOfTicks * tickUnitSize), 1)
                        * tickUnitSize;
        units.add(new ShiftedNumberTickUnit(unit, tickUnitSize));
        return units;
    }
}
