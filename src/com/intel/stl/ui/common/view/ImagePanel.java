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
 *  File Name: ImagePanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/30 22:30:14  jijunwan
 *  Archive Log:    a generic image panel that use a image as background
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.JPanel;

/**
 * A panel use a image as background
 */
public class ImagePanel extends JPanel {
    private static final long serialVersionUID = 1931660137919651275L;

    public enum Style {
        // scale image to fit this panel
        FIT_PANEL,
        // scale image to fit this panel's width
        FIT_PANEL_WIDTH,
        // scale image to fit this panel's height
        FIT_PANEL_HEIGHT,
        // just blindly draw image from left-top corner without any scaling
        FIXED,
        // resize panel to fit the image
        FIT_IMAGE,
        // resize panel to fit the image's width
        FIT_IMAGE_WIDTH,
        // resize panel to fit the image's height
        FIT_IMAGE_HEIGHT;
    };

    private final Image image;

    private final Style style;

    /**
     * Description:
     * 
     * @param image
     * @param style
     */
    public ImagePanel(Image image, Style style) {
        super();
        this.image = image;
        this.style = style;
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see javax.swing.JComponent#getMinimumSize()
    // */
    // @Override
    // public Dimension getMinimumSize() {
    // if (style == Style.FIT_IMAGE) {
    // Insets insets = getInsets();
    // int w = insets.left + image.getWidth(null) + insets.right;
    // int h = insets.top + image.getHeight(null) + insets.bottom;
    // return new Dimension(w, h);
    // } else if (style == Style.FIT_IMAGE_WIDTH) {
    // Insets insets = getInsets();
    // int w = insets.left + image.getWidth(null) + insets.right;
    // return new Dimension(w, super.getMinimumSize().height);
    // } else if (style == Style.FIT_IMAGE_HEIGHT) {
    // Insets insets = getInsets();
    // int h = insets.top + image.getHeight(null) + insets.bottom;
    // return new Dimension(super.getMinimumSize().width, h);
    // } else {
    // return super.getMinimumSize();
    // }
    // }
    //
    // /*
    // * (non-Javadoc)
    // *
    // * @see java.awt.Component#getSize()
    // */
    // @Override
    // public Dimension getSize() {
    // if (style == Style.FIT_IMAGE) {
    // Insets insets = getInsets();
    // int w = insets.left + image.getWidth(null) + insets.right;
    // int h = insets.top + image.getHeight(null) + insets.bottom;
    // return new Dimension(w, h);
    // } else if (style == Style.FIT_IMAGE_WIDTH) {
    // Insets insets = getInsets();
    // int w = insets.left + image.getWidth(null) + insets.right;
    // return new Dimension(w, super.getSize().height);
    // } else if (style == Style.FIT_IMAGE_HEIGHT) {
    // Insets insets = getInsets();
    // int h = insets.top + image.getHeight(null) + insets.bottom;
    // return new Dimension(super.getSize().width, h);
    // } else {
    // return super.getSize();
    // }
    // }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        if (style == Style.FIT_IMAGE) {
            return new Dimension(getImageDesiredWidth(),
                    getImageDesiredHeight());
        } else if (style == Style.FIT_IMAGE_WIDTH) {
            return new Dimension(getImageDesiredWidth(),
                    super.getPreferredSize().height);
        } else if (style == Style.FIT_IMAGE_HEIGHT) {
            return new Dimension(super.getPreferredSize().width,
                    getImageDesiredHeight());
        } else {
            return super.getPreferredSize();
        }
    }

    protected int getImageDesiredWidth() {
        Insets insets = getInsets();
        int w = insets.left + image.getWidth(null) + insets.right;
        return w;
    }

    protected int getImageDesiredHeight() {
        Insets insets = getInsets();
        int h = insets.top + image.getHeight(null) + insets.bottom;
        return h;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Insets insets = getInsets();
        switch (style) {
            case FIT_PANEL: {
                Dimension size = getSize();
                int width = (int) size.getWidth() - insets.left - insets.right;
                int height =
                        (int) size.getHeight() - insets.top - insets.bottom;
                g2d.drawImage(image, insets.left, insets.top, width, height,
                        this);
            }
            case FIT_PANEL_WIDTH: {
                Dimension size = getSize();
                int width = (int) size.getWidth() - insets.left - insets.right;
                double scale = (double) width / image.getWidth(this);
                int height = (int) (scale * image.getHeight(this));
                g2d.drawImage(image, insets.left, insets.top, width, height,
                        this);
            }
            case FIT_PANEL_HEIGHT: {
                Dimension size = getSize();
                int height =
                        (int) size.getHeight() - insets.top - insets.bottom;
                double scale = (double) height / image.getHeight(this);
                int width = (int) (scale * image.getWidth(this));
                g2d.drawImage(image, insets.left, insets.top, width, height,
                        this);
            }
            case FIXED:
            case FIT_IMAGE:
            case FIT_IMAGE_WIDTH:
            case FIT_IMAGE_HEIGHT: {
                g2d.drawImage(image, insets.left, insets.top, this);
            }
        }
    }

}
