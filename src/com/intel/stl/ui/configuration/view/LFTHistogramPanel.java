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
 *  File Name: LFTHistogramPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.1  2015/08/12 15:27:09  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/22 02:08:13  jijunwan
 *  Archive Log:    renamed
 *  Archive Log:    PropertyPageCategory to DevicePropertyCategory,
 *  Archive Log:    PropertyItem to DevicePropertyItem,
 *  Archive Log:    PropertyPageGroup to DevicePropertyGroup
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/13 21:06:47  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/09 13:04:36  fernande
 *  Archive Log:    Adding IContextAware interface to generalize setting up Context
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/26 20:49:14  jijunwan
 *  Archive Log:    improved to support switches with different number of ports
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/14 17:37:51  fernande
 *  Archive Log:    Closing the gap on device properties being displayed.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration.view;

import static com.intel.stl.ui.common.STLConstants.K0390_NUM_LIDS;
import static com.intel.stl.ui.common.STLConstants.K0427_PORT_NUMBER;
import static com.intel.stl.ui.model.DeviceProperty.LFT_SERIES;
import static com.intel.stl.ui.model.DeviceProperty.NUM_PORTS;

import java.awt.Dimension;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYDataset;

import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.model.DevicePropertyCategory;

public class LFTHistogramPanel extends DevicePropertyCategoryPanel {

    private static final long serialVersionUID = 1L;

    private static final Dimension PREFERRED_CHART_SIZE = new Dimension(360,
            240);

    private HistogramDataset dataset;

    private ChartPanel chartPanel;

    @Override
    public void modelUpdateFailed(DevicePropertyCategory model, Throwable caught) {
    }

    @Override
    public void modelChanged(DevicePropertyCategory model) {
        double[] values = (double[]) model.getProperty(LFT_SERIES).getObject();
        int numPorts = (Integer) model.getProperty(NUM_PORTS).getObject();
        numPorts = numPorts + 1; // Account for port 0
        dataset.addSeries("Port", values, numPorts, 0.0, numPorts);
        chartPanel.getChart().getXYPlot().getDomainAxis().setRange(0, numPorts);
    }

    @Override
    public void initComponents() {
        dataset = new HistogramDataset();
        JFreeChart chart =
                ComponentFactory.createHistogramChart(
                        K0427_PORT_NUMBER.getValue(),
                        K0390_NUM_LIDS.getValue(), dataset);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        final String portLabel = "<html>" + K0427_PORT_NUMBER.getValue() + ": ";
        final String lidCountLabel = "<br>" + K0390_NUM_LIDS.getValue() + ": ";
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesToolTipGenerator(0, new XYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset dataset, int arg1, int arg2) {
                int portNum = (int) dataset.getXValue(arg1, arg2);
                int lidCount = (int) dataset.getYValue(arg1, arg2);
                return portLabel + portNum + lidCountLabel + lidCount
                        + "</html>";
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
