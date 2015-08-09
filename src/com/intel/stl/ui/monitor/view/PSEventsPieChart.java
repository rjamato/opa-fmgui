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
 *  File Name: PSEventsPie.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/05/18 22:54:00  rjtierne
 *  Archive Log:    Reorganized panel layout and added setTypeDataset() to update
 *  Archive Log:    the event severity pie chart.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/13 13:54:09  rjtierne
 *  Archive Log:    Initial Release
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.model.StateLongTypeViz;

public class PSEventsPieChart extends JPanel {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 7866706629944517223L;
    
    private DefaultPieDataset pieDataset;
    
    private JLabel[] stateLabels;

    private ChartPanel pieChartPanel;
    
    
    
    
    public PSEventsPieChart() {
        super();
        initComponents();
    }
    
    
    protected void initComponents() {        
        setLayout(new BorderLayout());

        pieDataset = new DefaultPieDataset();
        StateLongTypeViz[] states = StateLongTypeViz.values();
        for (int i = 0; i < states.length; i++) {
            pieDataset.setValue(states[i], 0);
        }

        //Create the pie chart panel and put it on this panel
        pieChartPanel = new ChartPanel(ComponentFactory.createPlainPieChart(
                pieDataset, StateLongTypeViz.colors));
        pieChartPanel.setPreferredSize(new Dimension(80, 8));
        add(pieChartPanel);

        //Create the legend panel and put it on this panel
        JPanel legendPanel = getLengendPanel();
        add(legendPanel, BorderLayout.EAST);
    }

    protected JPanel getLengendPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.fill = GridBagConstraints.BOTH;
        StateLongTypeViz[] states = StateLongTypeViz.values();
        stateLabels = new JLabel[states.length];
        for (int i = 0; i < states.length; i++) {
            StateLongTypeViz state = states[i];
            gc.insets = new Insets(2, 5, 2, 2);
            gc.weightx = 0;
            gc.gridwidth = 1;

            JLabel label = new JLabel(state.getName(), Util.generateImageIcon(
                    state.getColor(), 8, new Insets(1, 1, 1, 1)), JLabel.LEFT);
            label.setFont(UIConstants.H5_FONT);
            label.setForeground(UIConstants.INTEL_DARK_GRAY);
            panel.add(label, gc);

            gc.gridwidth = GridBagConstraints.REMAINDER;
            gc.weightx = 0;
            stateLabels[i] = new JLabel();
            stateLabels[i].setForeground(UIConstants.INTEL_DARK_GRAY);
            stateLabels[i].setFont(UIConstants.H5_FONT);
            panel.add(stateLabels[i], gc);
        }
        return panel;
    }
    
    
    public void setTypeDataset(DefaultPieDataset dataset) {
        JFreeChart chart = ComponentFactory.createPlainPieChart(
                dataset, StateLongTypeViz.colors);
        pieChartPanel.setChart(chart);
    }


    public void setTypes(double[] values, String[] labels, String[] tooltips) {
        if (values.length != stateLabels.length) {
            throw new IllegalArgumentException(
                    "Incorrect array size. Expected " + stateLabels.length
                            + " values, got " + values.length + " values.");
        }
        if (labels.length != stateLabels.length) {
            throw new IllegalArgumentException(
                    "Incorrect array size. Expected " + stateLabels.length
                            + " labels, got " + labels.length + " labels.");
        }
        if (tooltips.length != stateLabels.length) {
            throw new IllegalArgumentException(
                    "Incorrect array size. Expected " + stateLabels.length
                            + " tooltips, got " + tooltips.length
                            + " tooltips.");
        }

        StateLongTypeViz[] states = StateLongTypeViz.values();
        for (int i = 0; i < values.length; i++) {
            pieDataset.setValue(states[i], values[i]);
            stateLabels[i].setText(labels[i]);
            stateLabels[i].setToolTipText(tooltips[i]);
        }
    }
  
}
