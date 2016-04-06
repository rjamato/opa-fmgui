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
 *  File Name: MTUByVLChartPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2016/01/27 21:50:53  jijunwan
 *  Archive Log:    PR 132499 - MaxMTU field values needs to be updated to match support SM MTU values
 *  Archive Log:
 *  Archive Log:    - removed 256, 512 and 1024 as suggested
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/08/17 18:54:17  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/05 19:58:41  jijunwan
 *  Archive Log:    code cleanup
 *  Archive Log:
 *
 *  Overview:
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration.view;

import static com.intel.stl.ui.common.STLConstants.K0342_PORT_VL_TITLE;
import static com.intel.stl.ui.common.STLConstants.K1068_MTU;
import static com.intel.stl.ui.model.DeviceProperty.MTU_SERIES;
import static com.intel.stl.ui.model.DeviceProperty.NUM_VL;

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

import com.intel.stl.api.configuration.MTUSize;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.model.DevicePropertyCategory;

public class MTUByVLBarChartPanel extends DevicePropertyCategoryPanel {

    private static final long serialVersionUID = 1L;

    private static final Dimension PREFERRED_CHART_SIZE =
            new Dimension(360, 240);

    private XYSeriesCollection dataset;

    private ChartPanel chartPanel;

    @Override
    public void modelUpdateFailed(DevicePropertyCategory model,
            Throwable caught) {
    }

    @Override
    public void modelChanged(DevicePropertyCategory model) {
        MTUSize[] values =
                (MTUSize[]) model.getProperty(MTU_SERIES).getObject();

        int numVLs = (Integer) model.getProperty(NUM_VL).getObject();

        final XYSeries xyseries = new XYSeries("");

        int x = 0;
        for (MTUSize v : values) {
            double mtuSize = 0.0;
            switch (v) {
                // remove the following as they are no longer supported by the
                // SM
                // case IB_MTU_256:
                // mtuSize = 256;
                // break;
                // case IB_MTU_512:
                // mtuSize = 512;
                // break;
                // case IB_MTU_1024:
                // mtuSize = 1024;
                // break;
                case IB_MTU_2048:
                    mtuSize = 2048;
                    break;
                case IB_MTU_4096:
                    mtuSize = 4096;
                    break;
                case STL_MTU_8192:
                    mtuSize = 8192;
                    break;
                case STL_MTU_10240:
                    mtuSize = 10240;
                    break;
                default:
                    break;
            }

            xyseries.add(x++, mtuSize);
        }
        dataset.addSeries(xyseries);

        chartPanel.getChart().getXYPlot().getDomainAxis().setRange(0, numVLs);
    }

    @Override
    public void initComponents() {
        dataset = new XYSeriesCollection();

        JFreeChart chart = ComponentFactory.createXYBarChart(
                K0342_PORT_VL_TITLE.getValue(), K1068_MTU.getValue(), dataset,
                (XYItemLabelGenerator) null);

        XYPlot plot = chart.getXYPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        final String vlLabel = "<html>" + K0342_PORT_VL_TITLE.getValue() + ": ";
        final String mtuLabel = "<br>" + K1068_MTU.getValue() + ": ";

        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setBarAlignmentFactor(0);
        renderer.setMargin(0.2);

        renderer.setSeriesToolTipGenerator(0, new XYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset dataset, int arg1,
                    int arg2) {
                int vlNum = (int) dataset.getXValue(arg1, arg2);
                int mtuCount = (int) dataset.getYValue(arg1, arg2);
                return vlLabel + vlNum + mtuLabel + mtuCount + "</html>";
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
