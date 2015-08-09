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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * @author jijunwan
 * 
 */
public class DistributionBarPanel extends JPanel {
    private static final long serialVersionUID = 5519998835959335916L;

    private long[] cumulativeSum;

    private Color[] colors;

    private final int barHeight = 8;

    private JLabel[] labels;

    public DistributionBarPanel() {
        super();
        setLayout(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        // setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    public void init(String[] itemLabels, ImageIcon[] icons) {
        if (itemLabels.length != icons.length) {
            throw new IllegalArgumentException(
                    "Inconsistent array size. itemLabels=" + itemLabels.length
                            + " icons=" + icons.length);
        }

        labels = new JLabel[itemLabels.length];
        for (int i = 0; i < itemLabels.length; i++) {
            labels[i] = new JLabel(itemLabels[i], icons[i], JLabel.LEFT);
            labels[i].setVerticalAlignment(JLabel.BOTTOM);
            // labels[i].setBorder(BorderFactory.createLineBorder(Color.GREEN));
            add(labels[i]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#setBorder(javax.swing.border.Border)
     */
    @Override
    public void setBorder(Border border) {
        Border newBorder =
                BorderFactory.createCompoundBorder(border,
                        BorderFactory.createEmptyBorder(16, 0, 0, 0));
        super.setBorder(newBorder);
    }

    public void update(String[] newLabels, long[] cumulativeSum, Color[] colors) {
        if (newLabels.length != labels.length) {
            throw new IllegalArgumentException(
                    "Incorrect number of newLabels. Expected " + labels.length
                            + " items, got " + newLabels.length + " items");
        }
        if (cumulativeSum.length != labels.length) {
            throw new IllegalArgumentException(
                    "Incorrect number of cumulativeSum. Expected "
                            + labels.length + " items, got "
                            + cumulativeSum.length + " items");
        }
        if (colors.length != labels.length) {
            throw new IllegalArgumentException(
                    "Incorrect number of newLabels. Expected " + labels.length
                            + " items, got " + colors.length + " items");
        }

        this.cumulativeSum = cumulativeSum;
        this.colors = colors;
        for (int i = 0; i < colors.length; i++) {
            labels[i].setText(newLabels[i]);
        }
        repaint();
        validate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension res = super.getPreferredSize();
        res.height += barHeight;
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cumulativeSum == null) {
            return;
        }

        Insets insets = getInsets();
        int width = getWidth() - insets.left - insets.right;
        int x = insets.left;
        int y = Math.max(insets.top - barHeight - 2, 0);

        int nextX = 0;
        double scale = (double) width / cumulativeSum[cumulativeSum.length - 1];
        for (int i = 0; i < cumulativeSum.length; i++) {
            x = nextX;
            nextX = (int) (cumulativeSum[i] * scale);
            g.setColor(colors[i]);
            g.fillRect(x, y, nextX - x, barHeight);
        }
    }

}
