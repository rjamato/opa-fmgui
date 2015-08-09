/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: testme.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/31 20:42:50  fisherma
 *  Archive Log:    Minor UI appearance changes to the setup wizard.
 *  Archive Log:
 *
 *  Overview:
 *
 *  @author: fisherma
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.AbstractBorder;

public class RoundedCornersBorder extends AbstractBorder {

    private Color color;

    private int thickness = 1;

    // INTEL's corner radius per UI spec is 2 pixels
    private final int radii = 2;

    private Insets insets = null;

    private BasicStroke stroke = null;

    private int strokePad;

    RenderingHints hints;

    // This will create border with rounded corners of color "color",
    // 1 pixel wide and 2-pixels padding around its component (insets)
    public RoundedCornersBorder(Color color) {
        new RoundedCornersBorder(color, 1, 2);
    }

    // This will create a border with rounded corners of color "color",
    // "thickness" pixels wide and white space padding (insets) around its
    // component
    // of 'padding' pixels wide.
    public RoundedCornersBorder(Color color, int thickness, int padding) {
        this.thickness = thickness;
        // this.radii = radii;
        this.color = color;

        stroke = new BasicStroke(thickness);
        strokePad = thickness / 2;

        hints =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = padding + strokePad;
        // int bottomPad = pad + strokePad;
        // insets = new Insets(pad, pad, bottomPad, pad);
        insets = new Insets(pad, pad, pad, pad);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width,
            int height) {

        Graphics2D g2 = (Graphics2D) g;

        int bottomLineY = height - thickness;

        RoundRectangle2D.Double bubble =
                new RoundRectangle2D.Double(0 + strokePad, 0 + strokePad, width
                        - thickness, bottomLineY, radii, radii);

        Area area = new Area(bubble);

        g2.setRenderingHints(hints);

        // Paint the BG color of the parent, everywhere outside the clip
        // of the text bubble.
        Component parent = c.getParent();
        if (parent != null) {
            Color bg = parent.getBackground();
            Rectangle rect = new Rectangle(0, 0, width, height);
            Area borderRegion = new Area(rect);
            borderRegion.subtract(area);
            g2.setClip(borderRegion);
            g2.setColor(bg);
            g2.fillRect(0, 0, width, height);
            g2.setClip(null);
        }

        g2.setColor(color);
        g2.setStroke(stroke);
        g2.draw(area);
    }
}
