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
 *  File Name: ChartsView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7.2.1  2015/08/12 15:26:33  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/17 23:22:16  jijunwan
 *  Archive Log:    PR 127106 - Suggest to use same bucket range for Group Err Summary as shown in "opatop" command to plot performance graphs in FV
 *  Archive Log:     - changed error histogram chart to bar chart to show the new data ranges: 0-25%, 25-50%, 50-75%, 75-100% and 100+%
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/05 21:35:38  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/02 21:26:19  jijunwan
 *  Archive Log:    fixed issued found by FindBugs
 *  Archive Log:    Some auto-reformate
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/18 14:59:30  jijunwan
 *  Archive Log:    Added jumping to destination support to TopN chart via popup menu
 *  Archive Log:    Added label highlight for chart view
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/05 21:52:31  jijunwan
 *  Archive Log:    improved IntelComboBoxUI to support editable Combo Box
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/22 18:38:39  jijunwan
 *  Archive Log:    introduced DatasetDescription to support short name and full name (description) for a dataset
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/21 17:03:08  jijunwan
 *  Archive Log:    moved ChartsView and ChartsCard to common package
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/07/16 20:54:21  jijunwan
 *  Archive Log:    fixed port link
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/07/16 15:15:08  jijunwan
 *  Archive Log:    new "Tabbed" performance view with a sparkline
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/11 19:26:15  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/09 21:40:55  jijunwan
 *  Archive Log:    made Chart Group Manager more general
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/06 13:31:05  jypak
 *  Archive Log:    Performance-Performance subpage updates.
 *  Archive Log:    1. Synchronize y-axis(range axis) bound for a group of charts (packet, data).
 *  Archive Log:    2. Auto conversion of y-axis label title and tick label based on the max value of data in the series.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/09 14:17:16  jijunwan
 *  Archive Log:    moved JFreeChart to view side, controller side only take care dataset
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/08 19:25:38  jijunwan
 *  Archive Log:    MVC refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/30 15:07:20  rjtierne
 *  Archive Log:    Changes to reflect renamed JCard
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/16 22:36:07  jijunwan
 *  Archive Log:    added Intel style combobox
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:51:18  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/08 17:32:56  jijunwan
 *  Archive Log:    introduced new summary section for "Home Page"
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/03 20:52:18  jijunwan
 *  Archive Log:    on going work on "Home" page
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;

import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.model.DatasetDescription;

public class ChartsView extends JCardView<IChartsCardListener> {
    private static final long serialVersionUID = -2134542205733463526L;

    private JPanel ctrPanel;

    private JComboBox<DatasetDescription> chartList;

    private ActionListener chartListListener;

    private JPanel mainPanel;

    private final IChartCreator chartCreator;

    private JumpChartPanel chartPanel;

    private Map<String, ChartWrap> charts;

    /**
     * Description:
     * 
     * @param title
     * @param controller
     */
    public ChartsView(String title, IChartCreator chartCreator) {
        super(title);
        setPreferredSize(new Dimension(270, 250));
        this.chartCreator = chartCreator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.common.JCard#getExtraComponent()
     */
    @Override
    protected JComponent getExtraComponent() {
        if (ctrPanel == null) {
            ctrPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
        }
        return ctrPanel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.common.JCard#getMainComponent()
     */
    @Override
    protected JComponent getMainComponent() {
        if (mainPanel == null) {
            mainPanel = new JPanel(new BorderLayout(0, 0));
            mainPanel.setOpaque(false);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

            chartPanel = new JumpChartPanel(null);
            chartPanel.setOpaque(false);
            chartPanel.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    chartPanel.setMaximumDrawHeight(e.getComponent()
                            .getHeight());
                    chartPanel.setMaximumDrawWidth(e.getComponent().getWidth());
                    chartPanel.setMinimumDrawWidth(e.getComponent().getWidth());
                    chartPanel.setMinimumDrawHeight(e.getComponent()
                            .getHeight());
                }
            });
            mainPanel.add(chartPanel, BorderLayout.CENTER);
        }
        return mainPanel;
    }

    public void setDatasets(List<DatasetDescription> datasets) {
        setChartNames(datasets);
        charts = new HashMap<String, ChartWrap>();
        if (datasets != null) {
            for (DatasetDescription dd : datasets) {
                String name = dd.getName();
                Dataset dataset = dd.getDataset();
                charts.put(
                        name,
                        new ChartWrap(chartCreator.createChart(
                                dd.getFullName(), dataset), dd.isJumpable()));
            }
        }
    }

    protected void setChartNames(List<DatasetDescription> datasets) {
        if (ctrPanel == null) {
            // shouldn't happen
            throw new RuntimeException(
                    "Something weird happend! chartPanel is null");
        }

        if (datasets == null || datasets.isEmpty()) {
            return;
        } else if (datasets.size() == 1) {
            DatasetDescription dd = datasets.get(0);
            setTitle(dd.getName(), dd.getFullName());
        } else {
            setTitle(null);
            chartList =
                    new JComboBox<DatasetDescription>(
                            datasets.toArray(new DatasetDescription[0]));
            chartList.setBorder(BorderFactory.createEmptyBorder());
            IntelComboBoxUI ui = new IntelComboBoxUI() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * com.intel.stl.ui.common.view.IntelComboBoxUI#getValueString
                 * (java.lang.Object)
                 */
                @Override
                protected String getValueString(Object value) {
                    return ((DatasetDescription) value).getName();
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * com.intel.stl.ui.common.view.IntelComboBoxUI#getValueTooltip
                 * (java.lang.Object)
                 */
                @Override
                protected String getValueTooltip(Object value) {
                    return ((DatasetDescription) value).getFullName();
                }

            };
            ui.setArrowButtonTooltip(UILabels.STL10103_MORE_SELECTIONS
                    .getDescription());
            ui.setArrowButtonBorder(null);
            chartList.setUI(ui);
            setListListener();
            ctrPanel.add(chartList);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.view.JCardView#setCardListener(com.intel.stl.
     * ui.common.view.ICardListener)
     */
    @Override
    public void setCardListener(IChartsCardListener listener) {
        if (chartPanel == null) {
            // shouldn't happen
            throw new RuntimeException(
                    "Something weird happend! chartPanel is null");
        }

        IChartsCardListener oldListener = this.listener;
        super.setCardListener(listener);
        setListListener();

        if (oldListener != null) {
            chartPanel.removeListener(oldListener);
        }
        if (listener != null) {
            chartPanel.addListener(listener);
        }
    }

    protected void setListListener() {
        if (chartList == null || listener == null) {
            return;
        }

        if (chartListListener != null) {
            chartList.removeActionListener(chartListListener);
        }
        chartListListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DatasetDescription dd =
                        (DatasetDescription) chartList.getSelectedItem();
                listener.onSelectChart(dd.getName());
            }

        };
        chartList.addActionListener(chartListListener);
    }

    public void setChart(String name) {
        if (chartPanel == null) {
            // shouldn't happen
            throw new RuntimeException(
                    "Something weird happend! chartPanel is null");
        }

        ChartWrap cw = getChartWrap(name);
        chartPanel.setChart(cw.chart, cw.isJumpable);
        validate();
    }

    public JFreeChart getChart(String name) {
        return getChartWrap(name).chart;
    }

    public JFreeChart getSparkline(String name) {
        if (charts == null) {
            return null;
        }

        JFreeChart chart = getChartWrap(name).chart;
        if (chart == null) {
            return null;
        }

        XYPlot plot = chart.getXYPlot();
        if (plot == null) {
            return null;
        }

        XYDataset dataset = plot.getDataset();
        return ComponentFactory.createXYAreaSparkline(dataset);
    }

    protected ChartWrap getChartWrap(String name) {
        ChartWrap res = charts.get(name);
        if (res != null) {
            return res;
        } else {
            throw new IllegalArgumentException("Couldn't find chart '" + name
                    + "'");
        }
    }

    private class ChartWrap {
        JFreeChart chart;

        boolean isJumpable;

        /**
         * Description:
         * 
         * @param chart
         * @param isJumpable
         */
        public ChartWrap(JFreeChart chart, boolean isJumpable) {
            super();
            this.chart = chart;
            this.isJumpable = isJumpable;
        }

    }
}
