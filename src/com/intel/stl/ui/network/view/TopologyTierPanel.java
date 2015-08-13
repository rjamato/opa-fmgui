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
 *  File Name: TopTierPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1.2.1  2015/08/12 15:27:06  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
