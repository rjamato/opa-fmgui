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
 *  File Name: SC2VLTMTBarChartPanel.java
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration.view;

import static com.intel.stl.ui.common.STLConstants.K1105_SC;
import static com.intel.stl.ui.common.STLConstants.K1109_VLT;
import static com.intel.stl.ui.model.DeviceProperty.SC;
import static com.intel.stl.ui.model.DeviceProperty.VLT;

import java.awt.Dimension;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.model.DevicePropertyCategory;

public class SC2VLTMTBarChartPanel extends DevicePropertyCategoryPanel {

    private static final long serialVersionUID = 1L;

    private static final Dimension PREFERRED_CHART_SIZE = new Dimension(360,
            240);

    private XYSeriesCollection dataset;

    private ChartPanel chartPanel;

    @Override
    public void modelUpdateFailed(DevicePropertyCategory model, Throwable caught) {
    }

    @Override
    public void modelChanged(DevicePropertyCategory model) {
        double[] values = (double[]) model.getProperty(VLT).getObject();

        int numSCs = (Integer) model.getProperty(SC).getObject();

        final XYSeries xyseries = new XYSeries("");

        int x = 0;
        for (double v : values) {

            xyseries.add(x++, v);
        }
        dataset.addSeries(xyseries);

        chartPanel.getChart().getXYPlot().getDomainAxis().setRange(0, numSCs);
    }

    @Override
    public void initComponents() {
        dataset = new XYSeriesCollection();

        JFreeChart chart =
                ComponentFactory.createXYBarChart(K1105_SC.getValue(),
                        K1109_VLT.getValue(), dataset,
                        (XYItemLabelGenerator) null);

        XYPlot plot = chart.getXYPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        final String scLabel = "<html>" + K1105_SC.getValue() + ": ";
        final String vltLabel = "<br>" + K1109_VLT.getValue() + ": ";

        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setBarAlignmentFactor(0);
        renderer.setMargin(0.2);

        renderer.setSeriesToolTipGenerator(0, new XYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset dataset, int arg1, int arg2) {
                int scNum = (int) dataset.getXValue(arg1, arg2);
                int vltCount = (int) dataset.getYValue(arg1, arg2);
                return scLabel + scNum + vltLabel + vltCount + "</html>";
            }
        });
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(PREFERRED_CHART_SIZE);
        propsPanel.add(chartPanel);
    }
}
