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
 *  File Name: StaDetailsPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.9  2014/10/22 01:20:40  jijunwan
 *  Archive Log:    text alignment adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/15 22:00:15  jijunwan
 *  Archive Log:    display other ports on UI
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/08 14:24:04  jijunwan
 *  Archive Log:    minor change - rename caXXX to fiXXX
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/08 14:07:02  jijunwan
 *  Archive Log:    removed route from state chart per feedback we got
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/26 20:29:32  jijunwan
 *  Archive Log:    clear UI when we switch context
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/19 22:12:30  jijunwan
 *  Archive Log:    look and feel adjustment on performance page
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/08 19:25:38  jijunwan
 *  Archive Log:    MVC refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/21 22:20:06  jijunwan
 *  Archive Log:    minor change on subnet statistic view
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/16 16:20:45  jijunwan
 *  Archive Log:    minor refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:50:38  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/08 19:45:58  jijunwan
 *  Archive Log:    added changable chart style to NodeStatesView
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/08 17:32:56  jijunwan
 *  Archive Log:    introduced new summary section for "Home Page"
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.main.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.model.NodeTypeViz;

/**
 * per feedback we got, we do not show Router info here. we intentionally
 * change it on UI side rather than backend because we need to support it
 * again in the future
 */
public class StaDetailsPanel extends JPanel {
    private static final long serialVersionUID = -8248761594760146918L;

    private final static double[] STATE_THRESHOLDS = new double[] { 0.2, 0.5,
            1.0 };

    private final static Color[] STATE_COLORS = new Color[] {
            UIConstants.INTEL_GRAY, UIConstants.INTEL_LIGHT_GRAY,
            UIConstants.INTEL_TABLE_BORDER_GRAY };

    private final NodeTypeViz[] types;

    private JLabel numberLabel;

    private JLabel nameLabel;

    private ChartPanel failedChartPanel;

    private JLabel failedNumberLabel;

    private JLabel failedNameLabel;

    private ChartPanel skippedChartPanel;

    private JLabel skippedNumberLabel;

    private JLabel skippedNameLabel;

    private ChartPanel typeChartPanel;

    private JLabel[] typeNumberLabels;

    private JLabel[] typeNameLabels;

    public StaDetailsPanel(NodeTypeViz[] types) {
        super();
        this.types = types;
        initComponent();
    }

    /**
     * Description:
     * 
     * @param name
     */
    protected void initComponent() {
        setLayout(new BorderLayout(0, 10));
        setOpaque(false);
        setBorder(BorderFactory.createTitledBorder((Border) null));

        JPanel titlePanel = new JPanel(new BorderLayout(5, 1));
        titlePanel.setOpaque(false);
        numberLabel =
                ComponentFactory
                        .getH1Label(
                                STLConstants.K0039_NOT_AVAILABLE.getValue(),
                                Font.PLAIN);
        numberLabel.setHorizontalAlignment(JLabel.RIGHT);
        titlePanel.add(numberLabel, BorderLayout.CENTER);
        nameLabel = ComponentFactory.getH3Label("", Font.PLAIN);
        nameLabel.setHorizontalAlignment(JLabel.LEFT);
        nameLabel.setVerticalAlignment(JLabel.BOTTOM);
        titlePanel.add(nameLabel, BorderLayout.EAST);
        add(titlePanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        GridBagLayout gridBag = new GridBagLayout();
        mainPanel.setLayout(gridBag);
        GridBagConstraints gc = new GridBagConstraints();

        gc.insets = new Insets(2, 2, 2, 2);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.gridwidth = 1;
        gc.weighty = 0;
        failedChartPanel = new ChartPanel(null);
        failedChartPanel.setPreferredSize(new Dimension(60, 20));
        mainPanel.add(failedChartPanel, gc);

        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 0;
        failedNumberLabel =
                ComponentFactory.getH2Label(
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), Font.BOLD);
        failedNumberLabel.setForeground(UIConstants.INTEL_DARK_RED);
        failedNumberLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(failedNumberLabel, gc);

        gc.gridwidth = GridBagConstraints.REMAINDER;
        failedNameLabel =
                ComponentFactory.getH5Label(
                        STLConstants.K0020_FAILED.getValue(), Font.PLAIN);
        failedNameLabel.setVerticalAlignment(JLabel.BOTTOM);
        mainPanel.add(failedNameLabel, gc);

        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.gridwidth = 1;
        skippedChartPanel = new ChartPanel(null);
        skippedChartPanel.setPreferredSize(new Dimension(60, 20));
        mainPanel.add(skippedChartPanel, gc);

        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 0;
        skippedNumberLabel =
                ComponentFactory.getH2Label(
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), Font.BOLD);
        skippedNumberLabel.setForeground(UIConstants.INTEL_DARK_ORANGE);
        skippedNumberLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(skippedNumberLabel, gc);

        gc.gridwidth = GridBagConstraints.REMAINDER;
        skippedNameLabel =
                ComponentFactory.getH5Label(
                        STLConstants.K0021_SKIPPED.getValue(), Font.PLAIN);
        skippedNameLabel.setVerticalAlignment(JLabel.BOTTOM);
        mainPanel.add(skippedNameLabel, gc);

        gc.weighty = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(8, 2, 2, 2);
        gc.weightx = 1;
        gc.gridwidth = 1;
        gc.gridheight = types.length;
        typeChartPanel = new ChartPanel(null);
        typeChartPanel.setPreferredSize(new Dimension(80, 60));
        mainPanel.add(typeChartPanel, gc);

        typeNumberLabels = new JLabel[types.length];
        typeNameLabels = new JLabel[types.length];
        gc.fill = GridBagConstraints.BOTH;
        gc.gridheight = 1;
        gc.insets = new Insets(12, 2, 2, 2);
        for (int i = 0; i < types.length; i++) {
            if (i == 1) {
                gc.insets = new Insets(2, 2, 2, 2);
            }

            gc.weightx = 0;
            gc.gridwidth = 1;
            typeNumberLabels[i] = createNumberLabel();
            mainPanel.add(typeNumberLabels[i], gc);

            gc.gridwidth = GridBagConstraints.REMAINDER;
            typeNameLabels[i] = createNameLabel(types[i].getName());
            mainPanel.add(typeNameLabels[i], gc);
        }

        gc.fill = GridBagConstraints.BOTH;
        mainPanel.add(Box.createGlue(), gc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JLabel createNumberLabel() {
        JLabel label =
                ComponentFactory
                        .getH4Label(
                                STLConstants.K0039_NOT_AVAILABLE.getValue(),
                                Font.PLAIN);
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setVerticalAlignment(JLabel.BOTTOM);
        return label;
    }

    private JLabel createNameLabel(String name) {
        JLabel label = ComponentFactory.getH5Label(name, Font.PLAIN);
        label.setVerticalAlignment(JLabel.BOTTOM);
        return label;
    }

    /**
     * @return the types
     */
    public NodeTypeViz[] getTypes() {
        return types;
    }

    @Override
    public void setName(String name) {
        nameLabel.setText(name);
    }

    public void setFailedDataset(DefaultCategoryDataset dataset) {
        JFreeChart chart =
                ComponentFactory.createBulletChart(dataset, STATE_THRESHOLDS,
                        STATE_COLORS);
        failedChartPanel.setChart(chart);
    }

    public void setSkipedDataset(DefaultCategoryDataset dataset) {
        JFreeChart chart =
                ComponentFactory.createBulletChart(dataset, STATE_THRESHOLDS,
                        STATE_COLORS);
        skippedChartPanel.setChart(chart);
    }

    public void setTypeDataset(DefaultPieDataset dataset, Color[] colors) {
        JFreeChart chart =
                ComponentFactory.createPlainPieChart(dataset, colors);
        typeChartPanel.setChart(chart);
    }

    public void setTotalNumber(String value) {
        numberLabel.setText(value);
    }

    public void setFailed(String value) {
        failedNumberLabel.setText(value);
    }

    public void setSkipped(String value) {
        skippedNumberLabel.setText(value);
    }

    public void setTypeInfo(NodeTypeViz type, String number, String label) {
        for (int i = 0; i < types.length; i++) {
            if (types[i] == type) {
                typeNumberLabels[i].setText(number);
                typeNameLabels[i].setText(label);
                return;
            }
        }
        throw new IllegalArgumentException("Unsupported Node Type " + type);
    }

    public void clear() {
        String na = STLConstants.K0039_NOT_AVAILABLE.getValue();
        setTotalNumber(na);
        setFailed(na);
        setSkipped(na);
        for (int i = 0; i < types.length; i++) {
            typeNumberLabels[i].setText(na);
        }
    }
}
