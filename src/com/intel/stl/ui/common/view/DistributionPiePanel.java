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
package com.intel.stl.ui.common.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * @author jijunwan
 * 
 */
public class DistributionPiePanel extends JPanel {
    private static final long serialVersionUID = -8922967649586607241L;

    private ChartPanel chartPanel;

    private JPanel labelPanel;

    private JLabel[] labels;

    public DistributionPiePanel() {
        super(new BorderLayout(0, 0));
        
        chartPanel = new ChartPanel(null);
        add(chartPanel, BorderLayout.CENTER);
        
        labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        add(labelPanel, BorderLayout.SOUTH);
    }
    
    public void setDataset(DefaultPieDataset dataset, Color[] colors) {
        JFreeChart chart = ComponentFactory.createPlainPieChart(dataset, colors);
        chartPanel.setChart(chart);
    }
    
    public void setLabels(String[] itemNames, ImageIcon[] icons,
            int labelColumns) {
        if (icons.length != itemNames.length) {
            throw new IllegalArgumentException("Inconsistent number of items. "
                    + " itemNames=" + itemNames.length + " icons=" + icons.length);
        }
        
        labels = new JLabel[icons.length];
        for (int i = 0; i < icons.length; i++) {
            labels[i] = new JLabel(itemNames[i], icons[i], JLabel.LEFT);
        }

        int rows = 1;
        if (labelColumns <= 0) {
            labelPanel.setLayout(new FlowLayout());
            for (JLabel label : labels) {
                labelPanel.add(label);
            }
        } else {
            BoxLayout layout = new BoxLayout(labelPanel, BoxLayout.X_AXIS);
            labelPanel.setLayout(layout);
            JPanel[] columns = new JPanel[labelColumns];
            for (int i = 0; i < columns.length; i++) {
                labelPanel.add(Box.createHorizontalGlue());
                columns[i] = new JPanel();
                columns[i].setOpaque(false);
                columns[i].setBorder(BorderFactory
                        .createEmptyBorder(2, 3, 2, 3));
                BoxLayout cLayout = new BoxLayout(columns[i], BoxLayout.Y_AXIS);
                columns[i].setLayout(cLayout);
                labelPanel.add(columns[i]);
            }
            labelPanel.add(Box.createHorizontalGlue());
            rows = (int) Math.ceil((double) labels.length / labelColumns);
            int index = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < labelColumns; j++) {
                    columns[i].add(index < labels.length ? labels[index] : Box
                            .createGlue());
                    index += 1;
                }
            }
        }
    }

    public void update(String[] itemLabels) {
        if (itemLabels.length != labels.length) {
            throw new IllegalArgumentException(
                    "Incorrect array size. Expected " + labels.length
                            + " items, got " + itemLabels.length + " items.");
        }

        chartPanel.getChart().fireChartChanged();

        for (int i = 0; i < itemLabels.length; i++) {
            labels[i].setText(itemLabels[i]);
        }
        validate();
    }

}
