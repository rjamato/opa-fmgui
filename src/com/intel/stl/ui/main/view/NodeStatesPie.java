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
 *  File Name: NodeStatesPie.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6.2.1  2015/08/12 15:26:53  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/26 20:29:32  jijunwan
 *  Archive Log:    clear UI when we switch context
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/20 21:23:24  jijunwan
 *  Archive Log:    moved dataset out from NodeStatesPie
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/19 22:12:30  jijunwan
 *  Archive Log:    look and feel adjustment on performance page
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/09 14:17:16  jijunwan
 *  Archive Log:    moved JFreeChart to view side, controller side only take care dataset
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/08 19:25:38  jijunwan
 *  Archive Log:    MVC refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/16 16:20:45  jijunwan
 *  Archive Log:    minor refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:50:38  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/08 19:45:58  jijunwan
 *  Archive Log:    added changable chart style to NodeStatesView
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.main.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.PieDataset;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;

public class NodeStatesPie extends AbstractNodeStatesView {
    private static final long serialVersionUID = -7808133310065940957L;

    private ChartPanel chartPanel;
    private JPanel legendPanel;

    private JLabel[] stateLabels;

    public NodeStatesPie() {
        super();
        initcomponent();
    }

    /**
     * Description:
     * 
     */
    protected void initcomponent() {
        setLayout(new GridBagLayout());
        
        chartPanel = new ChartPanel(null);
        chartPanel.setPreferredSize(new Dimension(120, 90));
        
        GridBagConstraints gc = new GridBagConstraints();
        add(chartPanel, gc);

        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        legendPanel = new JPanel();
        legendPanel.setOpaque(false);
        legendPanel.setLayout(new GridBagLayout());
        add(legendPanel, gc);
    }
    
    public void setDataset(PieDataset dataset, Color[] colors) {
        JFreeChart chart = ComponentFactory.createPlainPieChart(dataset, colors);
        chartPanel.setChart(chart);
        fillLengendPanel(legendPanel, dataset, colors);
    }

    protected void fillLengendPanel(JPanel panel, PieDataset dataset, Color[] colors) {
        panel.removeAll();
        GridBagConstraints gc = new GridBagConstraints();

        gc.fill = GridBagConstraints.BOTH;
        int size = dataset.getItemCount();
        stateLabels = new JLabel[size];
        for (int i = 0; i < size; i++) {
            gc.insets = new Insets(2, 5, 2, 2);
            gc.weightx = 0;
            gc.gridwidth = 1;

            JLabel label = new JLabel(dataset.getKey(i).toString(), Util.generateImageIcon(
                    colors[i], 8, new Insets(1, 1, 1, 1)), JLabel.LEFT);
            label.setFont(UIConstants.H5_FONT);
            label.setForeground(UIConstants.INTEL_DARK_GRAY);
            panel.add(label, gc);

            gc.gridwidth = GridBagConstraints.REMAINDER;
            gc.weightx = 0;
            stateLabels[i] = new JLabel();
            stateLabels[i].setForeground(UIConstants.INTEL_DARK_GRAY);
            stateLabels[i].setFont(UIConstants.H3_FONT);
            panel.add(stateLabels[i], gc);
        }
    }
    
    public void setStates(double[] values, String[] labels, String[] tooltips) {
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

        for (int i = 0; i < values.length; i++) {
            stateLabels[i].setText(labels[i]);
            stateLabels[i].setToolTipText(tooltips[i]);
        }
    }
    
    public void clear() {
        for (int i = 0; i < stateLabels.length; i++) {
            stateLabels[i].setText(STLConstants.K0039_NOT_AVAILABLE.getValue());
            stateLabels[i].setToolTipText(null);
        }
    }
}
