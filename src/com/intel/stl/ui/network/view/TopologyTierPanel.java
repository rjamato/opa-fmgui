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
 *  File Name: TopTierPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/10/22 02:21:27  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.view.JHorizontalBar;
import com.intel.stl.ui.model.NodeTypeViz;

public class TopologyTierPanel extends JPanel {
    private static final long serialVersionUID = -3464073606658949262L;

    private TotalPanel totalPanel;

    private PortsPanel portsPanel;

    private PortsPanel slowPortsPanel;

    private PortsPanel degPortsPanel;

    /**
     * Description:
     * 
     */
    public TopologyTierPanel() {
        super();
        initComponents();
    }

    /**
     * <i>Description:</i>
     * 
     */
    private void initComponents() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(2, 2, 2, 5);

        gc.weightx = 0;
        totalPanel = new TotalPanel();
        add(totalPanel, gc);

        gc.weightx = 1;
        portsPanel = new PortsPanel(3);
        add(portsPanel, gc);

        gc.weightx = 1;
        slowPortsPanel = new PortsPanel(2);
        add(slowPortsPanel, gc);

        gc.weightx = 1;
        degPortsPanel = new PortsPanel(2);
        add(degPortsPanel, gc);
    }

    public void updateTotal(String numNodes, String numPorts) {
        totalPanel.setContent(numNodes, numPorts);
    }

    public void updatePortsDistribution(double[] normalizedVals,
            String[] labels, String[] tooltips) {
        portsPanel.setContent(normalizedVals, labels, tooltips);
    }

    public void updateSlowPortsDistribution(double[] normalizedVals,
            String[] labels, String[] tooltips) {
        slowPortsPanel.setContent(normalizedVals, labels, tooltips);
    }

    public void updateDegPortsDistribution(double[] normalizedVals,
            String[] labels, String[] tooltips) {
        degPortsPanel.setContent(normalizedVals, labels, tooltips);
    }

    class TotalPanel extends JPanel {
        private static final long serialVersionUID = -499235218493665238L;

        private JLabel nodesLabel;

        private JLabel portsLabel;

        public TotalPanel() {
            super();
            initComponents();
        }

        protected void initComponents() {
            setOpaque(false);
            setLayout(new GridBagLayout());

            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.insets = new Insets(2, 2, 2, 5);

            gc.weightx = 1;
            nodesLabel = new JLabel();
            add(nodesLabel, gc);

            gc.weightx = 0;
            gc.gridwidth = GridBagConstraints.REMAINDER;
            JLabel label = new JLabel(NodeTypeViz.SWITCH.getPluralName());
            add(label, gc);

            gc.weightx = 1;
            gc.gridwidth = 1;
            portsLabel = new JLabel();
            add(portsLabel, gc);

            gc.weightx = 0;
            gc.gridwidth = GridBagConstraints.REMAINDER;
            label = new JLabel(STLConstants.K0024_ACTIVE_PORTS.getValue());
            add(label, gc);
        }

        public void setContent(String numNodes, String numPorts) {
            nodesLabel.setText(numNodes);
            portsLabel.setText(numPorts);
            repaint();
        }
    }

    class PortsPanel extends JPanel {
        private static final long serialVersionUID = -4597232952448379838L;

        private final int size;

        private JHorizontalBar[] bars;

        private JLabel[] nameLabels;

        public PortsPanel(int size) {
            super();
            this.size = size;
            initComponents();
        }

        protected void initComponents() {
            setOpaque(false);
            setLayout(new GridBagLayout());

            bars = new JHorizontalBar[size];
            nameLabels = new JLabel[size];
            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.insets = new Insets(2, 2, 2, 5);
            for (int i = 0; i < size; i++) {
                gc.gridwidth = 1;
                gc.weightx = 1;
                bars[i] = new JHorizontalBar();
                bars[i].setUpperMargin(0.8);
                bars[i].setLowerMargin(0);
                bars[i].setPreferredSize(new Dimension(60, 25));
                add(bars[i], gc);

                gc.gridwidth = GridBagConstraints.REMAINDER;
                gc.weightx = 0;
                nameLabels[i] = new JLabel();
                add(nameLabels[i], gc);
            }
        }

        public void setContent(double[] normalizedVals, String[] labels,
                String[] tooltips) {
            if (normalizedVals.length != size) {
                throw new IllegalArgumentException(
                        "Incorrect normalized values size. Expecte " + size
                                + ", got " + normalizedVals.length);
            }
            if (labels.length != size) {
                throw new IllegalArgumentException(
                        "Incorrect labels size. Expected " + size + ", got "
                                + labels.length);
            }
            if (tooltips != null && tooltips.length != size) {
                throw new IllegalArgumentException(
                        "Incorrect tooltips size. Expected " + size + ", got "
                                + tooltips.length);
            }

            for (int i = 0; i < size; i++) {
                bars[i].setText("" + normalizedVals[i]);
                bars[i].setNormalizedValue(normalizedVals[i]);
                nameLabels[i].setText(labels[i]);
                if (tooltips != null) {
                    nameLabels[i].setToolTipText(tooltips[i]);
                } else {
                    nameLabels[i].setToolTipText(null);
                }
            }
            repaint();
        }
    }
}
