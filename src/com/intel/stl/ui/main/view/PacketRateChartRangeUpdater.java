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
 *  File Name: 
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
 *  Archive Log:    Revision 1.5  2015/04/10 11:46:47  jypak
 *  Archive Log:    Updates to make delta data and cumulative data in same unit.
 *  Archive Log:    For Port performance, the DataChartScaleGroupManager is already updating the unit based on upper value among received/transmitted delta data. Introduced UnitDescription as a wrapper class to grab the unit information to be passed to counter (error) section from chart section.
 *  Archive Log:    For Node performance, since we need to convert data for all the ports, the conversion is done in PerformanceTableSection. the units will be decided by the delta data, not the cumulative data because it's smaller. With this update, received delta/cumulative data will be in a same unit and transmitted delta/cumulative data will be in same unit. However, it is possible that received data and transmitted data can be in different units. The charts are in same unit because it goes down to a double precision but table section is all in integer, so, we don't necessarily want to make them always in a same unit for now.
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
import java.text.NumberFormat;
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
import com.intel.stl.ui.monitor.UnitDescription;

public class PacketRateChartRangeUpdater implements IChartRangeUpdater {
    private long tickUnit = 1L;

    private String unitStr = "";

    private String unitDes = "";

    private static final int thousand = 1000;

    private static final int million = 1000000;

    private static final int billion = 1000000000;

    private final boolean debug = false;

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
                            new PktsRateToolTipGenerator(
                                    "<html><b>{0}</b><br> Time: {1}<br> Data: {2}</html>",
                                    new SimpleDateFormat("HH:mm:ss"),
                                    new DecimalFormat("#,##0.00" + " "
                                            + unitDes)));
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

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IChartRange#createTickUnits(long)
     */
    @Override
    public TickUnitSource createTickUnits(long upper) {
        long upperOrig = upper;
        // Decide how many digits we will skip using / (division operator).
        long tenMultiplier = 1L;
        long ten = 10L;
        long numOfTicks = 10L;

        while (upper >= ten) {
            if (tenMultiplier >= Long.MAX_VALUE / ten) {
                // Overflow will happen, so, break out of loop and set it to
                // giga.
                break;
            } else {
                tenMultiplier = tenMultiplier * ten;
            }
            upper = upper / ten;
        }

        if (tenMultiplier >= billion) {
            tickUnit = billion;
            unitStr = STLConstants.K0753_GPPS.getValue();
            unitDes = STLConstants.K3307_GPPS_DESCRIPTION.getValue();
        } else if (tenMultiplier >= million) {
            tickUnit = million;
            unitStr = STLConstants.K0752_MPPS.getValue();
            unitDes = STLConstants.K3306_MPPS_DESCRIPTION.getValue();
        } else if (tenMultiplier >= thousand) {
            tickUnit = thousand;
            unitStr = STLConstants.K0751_KPPS.getValue();
            unitDes = STLConstants.K3305_KPPS_DESCRIPTION.getValue();
        } else {
            // If upper is less than 1000, reset unitStr to empty string.
            tickUnit = 1L;
            unitStr = STLConstants.K0750_PPS.getValue();
            unitDes = STLConstants.K3304_PPS_DESCRIPTION.getValue();
            return null;
        }

        TickUnits units = new TickUnits();
        if (debug) {
            System.out.println("upperOrig=" + upperOrig);
            System.out.println("tickUnit=" + tickUnit);
        }

        double unit =
                Math.max(upperOrig / (numOfTicks * tickUnit), 1) * tickUnit;
        units.add(new ShiftedNumberTickUnit(unit, tickUnit));
        return units;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IChartRangeUpdater#getUnitDesctiption()
     */
    @Override
    public UnitDescription getUnitDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    class PktsRateToolTipGenerator extends StandardXYToolTipGenerator {
        private static final long serialVersionUID = -2277170573673294327L;

        /**
         * Description:
         * 
         * @param formatString
         * @param xFormat
         * @param yFormat
         */
        public PktsRateToolTipGenerator(String formatString,
                DateFormat xFormat, NumberFormat yFormat) {
            super(formatString, xFormat, yFormat);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.jfree.chart.labels.AbstractXYItemLabelGenerator#createItemArray
         * (org.jfree.data.xy.XYDataset, int, int)
         */
        @Override
        protected Object[] createItemArray(XYDataset dataset, int series,
                int item) {
            Object[] result = new Object[3];
            result[0] = dataset.getSeriesKey(series).toString();

            double x = dataset.getXValue(series, item);
            if (Double.isNaN(x) || dataset.getX(series, item) == null) {
                result[1] = "null";
            } else {
                DateFormat xDateFormat = this.getXDateFormat();
                if (xDateFormat != null) {
                    result[1] = xDateFormat.format(new Date((long) x));
                } else {
                    result[1] = this.getXFormat().format(x);
                }
            }

            double y = dataset.getYValue(series, item);
            if (Double.isNaN(y) && dataset.getY(series, item) == null) {
                result[2] = getNullYString();
            } else {
                DateFormat yDateFormat = this.getYDateFormat();
                if (yDateFormat != null) {
                    result[2] = yDateFormat.format(new Date((long) y));
                } else {
                    result[2] = this.getYFormat().format(y / tickUnit);
                }
            }
            return result;
        }

    }
}
