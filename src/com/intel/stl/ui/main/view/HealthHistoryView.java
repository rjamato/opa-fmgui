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
package com.intel.stl.ui.main.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.common.view.JCardView;

/**
 * @author jijunwan
 * 
 */
public class HealthHistoryView extends JCardView<ICardListener> {
    private static final long serialVersionUID = -7089992232091159132L;

    private JPanel mainPanel;

    private ChartPanel chartPanel;

    private JLabel startTimeLabel;

    private JLabel endTimeLabel;

    private TextTitle currentValue;

    private String scoreString = STLConstants.K0039_NOT_AVAILABLE.getValue();

    private Color scoreColor = UIConstants.INTEL_DARK_GRAY;

    /**
     * @param title
     * @param controller
     */
    public HealthHistoryView() {
        super(STLConstants.K0105_HEALTH_HISTORY.getValue());
        // this is unnecessary, but can stop klocwork from complaining
        getMainComponent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.common.JCard#getMainPanel()
     */
    @Override
    protected JPanel getMainComponent() {
        if (mainPanel != null) {
            return mainPanel;
        }

        mainPanel = new JPanel();
        GridBagLayout gridBag = new GridBagLayout();
        mainPanel.setLayout(gridBag);
        GridBagConstraints gc = new GridBagConstraints();

        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        chartPanel = new ChartPanel(null);
        // chart.PanelsetBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
        // UIConstants.INTEL_BORDER_GRAY));
        chartPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                chartPanel.setMaximumDrawHeight(e.getComponent().getHeight());
                chartPanel.setMaximumDrawWidth(e.getComponent().getWidth());
                chartPanel.setMinimumDrawWidth(e.getComponent().getWidth());
                chartPanel.setMinimumDrawHeight(e.getComponent().getHeight());
            }
        });
        mainPanel.add(chartPanel, gc);

        gc.gridwidth = 1;
        gc.insets = new Insets(0, 5, 0, 5);
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.WEST;
        gc.weighty = 0;
        startTimeLabel = ComponentFactory.getH5Label("start", Font.PLAIN);
        mainPanel.add(startTimeLabel, gc);

        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.anchor = GridBagConstraints.EAST;
        endTimeLabel = ComponentFactory.getH5Label("end", Font.PLAIN);
        mainPanel.add(endTimeLabel, gc);

        return mainPanel;
    }

    public void setDataset(final IntervalXYDataset dataset) {
        JFreeChart chart =
                ComponentFactory.createStepAreaChart(dataset,
                        new XYItemLabelGenerator() {
                            @Override
                            public String generateLabel(XYDataset dataset,
                                    int series, int item) {
                                Number val = dataset.getY(series, item);
                                return UIConstants.INTEGER.format(val
                                        .intValue());
                            }
                        });
        chart.addProgressListener(new ChartProgressListener() {
            @Override
            public void chartProgress(ChartProgressEvent event) {
                if (event.getType() == ChartProgressEvent.DRAWING_FINISHED
                        && currentValue != null) {
                    currentValue.setText(scoreString);
                    currentValue.setPaint(scoreColor);
                }
            }
        });
        XYPlot plot = chart.getXYPlot();
        plot.getRangeAxis().setRange(0, 105);
        plot.getRenderer().setSeriesPaint(0, UIConstants.INTEL_BLUE);
        currentValue =
                new TextTitle(scoreString,
                        UIConstants.H1_FONT.deriveFont(Font.BOLD));
        currentValue.setPaint(scoreColor);
        // currentValue.setBackgroundPaint(new Color(255, 255, 255, 128));
        currentValue.setPosition(RectangleEdge.BOTTOM);
        XYTitleAnnotation xytitleannotation =
                new XYTitleAnnotation(0.49999999999999998D,
                        0.49999999999999998D, currentValue,
                        RectangleAnchor.CENTER);
        xytitleannotation.setMaxWidth(0.47999999999999998D);
        plot.addAnnotation(xytitleannotation);

        chartPanel.setChart(chart);
    }

    /**
     * Description:
     * 
     * @param format
     * @param format2
     */
    public void setTimeDuration(String start, String end) {
        startTimeLabel.setText(start);
        endTimeLabel.setText(end);
        validate();
    }

    public void setCurrentScore(String score, Color color) {
        this.scoreString = score;
        this.scoreColor = color;
    }
}
