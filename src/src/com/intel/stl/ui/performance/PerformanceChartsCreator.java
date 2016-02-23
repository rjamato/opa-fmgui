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
 *  File Name: PerformanceChartsCreator.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/08/17 18:53:49  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/08/10 17:06:27  jypak
 *  Archive Log:    PR 129919 - Change name from signal integrity to integrity.
 *  Archive Log:    STLConstants and class names are changed.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/08/10 13:49:00  robertja
 *  Archive Log:    PR 129358 - PR 129556 - Refine congestion chart labeling.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/06/30 22:31:42  jijunwan
 *  Archive Log:    PR 129215 - Need short chart name to support pin capability
 *  Archive Log:    - use short name as pin card title
 *  Archive Log:    - improved pin argument to include full name and provide data source description
 *  Archive Log:    - fixed improper full name issues on trend charts
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/03/27 15:48:34  jijunwan
 *  Archive Log:    changed K0072_SECURITY_ERROR to K0072_SECURITY
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/17 23:22:18  jijunwan
 *  Archive Log:    PR 127106 - Suggest to use same bucket range for Group Err Summary as shown in "opatop" command to plot performance graphs in FV
 *  Archive Log:     - changed error histogram chart to bar chart to show the new data ranges: 0-25%, 25-50%, 50-75%, 75-100% and 100+%
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/20 21:19:52  jijunwan
 *  Archive Log:    Bug 126612 - "Num of ports" which represent y-axis in Histogram should use only Whole numbers for plotting graph
 *  Archive Log:    changed range axis to use IntegerTickUnits.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/11 21:38:26  jijunwan
 *  Archive Log:    added bubble error charts on UI
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/02 22:16:59  jijunwan
 *  Archive Log:    fixed string comparison issues
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/22 18:39:44  jijunwan
 *  Archive Log:    moved IChartCreator to common package
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 21:38:05  jijunwan
 *  Archive Log:    added 3 type error counters
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/16 15:19:32  jijunwan
 *  Archive Log:    applied new performance framework and performance group viz to support bandwidth, packet rate, congestion and integrity data
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/09 14:17:16  jijunwan
 *  Archive Log:    moved JFreeChart to view side, controller side only take care dataset
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.IChartCreator;

public class PerformanceChartsCreator implements IChartCreator {
    private static final PerformanceChartsCreator instance =
            new PerformanceChartsCreator();

    private PerformanceChartsCreator() {
    }

    public static PerformanceChartsCreator instance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.view.IChartCreator#createChart(java.lang.String,
     * org.jfree.data.general.Dataset)
     */
    @Override
    public JFreeChart createChart(String name, Dataset dataset) {
        if (STLConstants.K0871_BANDWIDTH_TREND.getValue().equals(name)) {
            return getBwTrendChart(dataset);
        } else if (Util.matchPattern(
                UILabels.STL10200_TOPN_BANDWIDTH.getDescription(), name)) {
            return getBwTopNChart(dataset);
        } else if (STLConstants.K0045_BANDWIDTH_HISTOGRAM.getValue().equals(
                name)) {
            return getBwHistogramChart(dataset);
        } else if (STLConstants.K0872_PACKECT_RATE_TREND.getValue()
                .equals(name)) {
            return getPrTrendChart(dataset);
        } else if (Util.matchPattern(
                UILabels.STL10205_TOPN_PACKET_RATE.getDescription(), name)) {
            return getPrTopNChart(dataset);
        } else if (STLConstants.K0874_CONGESTION_TREND.getValue().equals(name)
                || STLConstants.K0873_INTEGRITY_TREND.getValue().equals(
                        name)
                || STLConstants.K0875_SMA_CONGESTION_TREND.getValue().equals(
                        name)
                || STLConstants.K0876_BUBBLE_TREND.getValue().equals(name)
                || STLConstants.K0877_SECURITY_TREND.getValue().equals(name)
                || STLConstants.K0878_ROUTING_TREND.getValue().equals(name)) {
            return getErrorTrendChart(dataset);
        } else if (Util.matchPattern(
                UILabels.STL10201_TOPN_CONGESTION.getDescription(), name)
                || Util.matchPattern(UILabels.STL10206_TOPN_SIGNAL_INTEGRITY
                        .getDescription(), name)
                || Util.matchPattern(
                        UILabels.STL10207_TOPN_SMA_CONGESTION.getDescription(),
                        name)
                || Util.matchPattern(
                        UILabels.STL10213_TOPN_BUBBLE.getDescription(), name)
                || Util.matchPattern(
                        UILabels.STL10208_TOPN_SECURITY.getDescription(), name)
                || Util.matchPattern(
                        UILabels.STL10209_TOPN_ROUTING.getDescription(), name)) {
            return getErrorTopNChart(dataset);
        } else if (STLConstants.K0046_CONGESTION_HISTOGRAM.getValue().equals(
                name)
                || STLConstants.K0068_INTEGRITY_HISTOGRAM.getValue()
                        .equals(name)
                || STLConstants.K0071_SMA_CONGESTION_HISTOGRAM.getValue()
                        .equals(name)
                || STLConstants.K0488_BUBBLE_HISTOGRAM.getValue().equals(name)
                || STLConstants.K0073_SECURITY_HISTOGRAM.getValue()
                        .equals(name)
                || STLConstants.K0075_ROUTING_HISTOGRAM.getValue().equals(name)) {
            return getErrorHistogramChart(dataset);
        }
        return null;
    }

    protected JFreeChart getBwTrendChart(Dataset dataset) {
        JFreeChart chart =
                ComponentFactory.createXYAreaChart(
                        STLConstants.K0035_TIME.getValue(),
                        STLConstants.K0040_MBPS.getValue(),
                        (XYDataset) dataset, true);
        return chart;
    }

    protected JFreeChart getBwTopNChart(Dataset dataset) {
        JFreeChart chart =
                ComponentFactory.createTopNBarChart(
                        STLConstants.K0040_MBPS.getValue(),
                        (CategoryDataset) dataset);
        return chart;
    }

    protected JFreeChart getBwHistogramChart(Dataset dataset) {
        JFreeChart chart =
                ComponentFactory.createXYBarChart(
                        STLConstants.K0053_CAPABILITY.getValue(),
                        STLConstants.K0044_NUM_PORTS.getValue(),
                        (IntervalXYDataset) dataset,
                        (XYItemLabelGenerator) null);
        NumberAxis axis = (NumberAxis) chart.getXYPlot().getDomainAxis();
        axis.setTickUnit(new NumberTickUnit(0.2, UIConstants.PERCENTAGE));

        axis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }

    protected JFreeChart getPrTrendChart(Dataset dataset) {
        JFreeChart chart =
                ComponentFactory.createXYAreaChart(
                        STLConstants.K0035_TIME.getValue(),
                        STLConstants.K0066_KPPS.getValue(),
                        (XYDataset) dataset, true);
        return chart;
    }

    protected JFreeChart getPrTopNChart(Dataset dataset) {
        JFreeChart chart =
                ComponentFactory.createTopNBarChart(
                        STLConstants.K0066_KPPS.getValue(),
                        (CategoryDataset) dataset);
        return chart;
    }

    protected JFreeChart getErrorTrendChart(Dataset dataset) {
        JFreeChart chart =
                ComponentFactory.createXYAreaChart(
                        STLConstants.K0035_TIME.getValue(),
                        STLConstants.K0125_NUM_EVENTS.getValue(),
                        (XYDataset) dataset, true);
        // NumberAxis yAxis = (NumberAxis)chart.getXYPlot().getRangeAxis();
        // yAxis.setRange(ErrorChartTickUnit.LOWER_BOUND,
        // ErrorChartTickUnit.UPPER_BOUND);
        // yAxis.setStandardTickUnits(new ErrorChartTickUnit().genTickUnits());
        return chart;
    }

    protected JFreeChart getErrorTopNChart(Dataset dataset) {
        JFreeChart chart =
                ComponentFactory.createTopNBarChart(
                        STLConstants.K0126_EVENT_RATE.getValue(),
                        (CategoryDataset) dataset);
        return chart;
    }

    protected JFreeChart getErrorHistogramChart(Dataset dataset) {
        JFreeChart chart =
                ComponentFactory.createBarChart(
                        STLConstants.K0127_PERCENT_OF_THRESHOLD.getValue(),
                        STLConstants.K0044_NUM_PORTS.getValue(),
                        (CategoryDataset) dataset);
        NumberAxis axis = (NumberAxis) chart.getCategoryPlot().getRangeAxis();
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }

}
