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
 *  File Name: IntelTaskPaneUI.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/10/09 21:26:02  jijunwan
 *  Archive Log:    Intel Style TaskPane
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;

public class IntelTaskPaneUI extends BasicTaskPaneUI {
    private Font titleFont = UIConstants.H4_FONT.deriveFont(Font.BOLD);

    private Color titleColor = UIConstants.INTEL_DARK_GRAY;

    private Color titleOverColor = UIConstants.INTEL_BLUE;

    private Color titleBackground = UIConstants.INTEL_WHITE;

    private Color titleBorder = UIConstants.INTEL_BORDER_GRAY;

    private Color contentBackground = UIConstants.INTEL_WHITE;

    private UIImages expandImg = UIImages.UP_ICON;

    private UIImages collapseImg = UIImages.DOWN_ICON;

    private Insets contentInsets = new Insets(2, 2, 2, 2);

    /*
     * (non-Javadoc)
     * 
     * @see org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI#installDefaults()
     */
    @Override
    protected void installDefaults() {
        super.installDefaults();
        group.setFont(titleFont);
        group.getContentPane().setBackground(contentBackground);
    }

    /**
     * @param titleFont
     *            the titleFont to set
     */
    public void setTitleFont(Font titleFont) {
        this.titleFont = titleFont;
    }

    /**
     * @param titleColor
     *            the titleColor to set
     */
    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
    }

    /**
     * @param titleOverColor
     *            the titleOverColor to set
     */
    public void setTitleOverColor(Color titleOverColor) {
        this.titleOverColor = titleOverColor;
    }

    /**
     * @param titleBackground
     *            the titleBackground to set
     */
    public void setTitleBackground(Color titleBackground) {
        this.titleBackground = titleBackground;
    }

    /**
     * @param titleBorder
     *            the titleBorder to set
     */
    public void setTitleBorder(Color titleBorder) {
        this.titleBorder = titleBorder;
    }

    /**
     * @param contentBackground
     *            the contentBackground to set
     */
    public void setContentBackground(Color contentBackground) {
        this.contentBackground = contentBackground;
    }

    /**
     * @param expandImg
     *            the expandImg to set
     */
    public void setExpandImg(UIImages expandImg) {
        this.expandImg = expandImg;
    }

    /**
     * @param collapseImg
     *            the collapseImg to set
     */
    public void setCollapseImg(UIImages collapseImg) {
        this.collapseImg = collapseImg;
    }

    /**
     * @param contentInsets
     *            the contentInsets to set
     */
    public void setContentInsets(Insets contentInsets) {
        this.contentInsets = contentInsets;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI#createPaneBorder()
     */
    @Override
    protected Border createPaneBorder() {
        return new IntelPaneBorder();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI#createContentPaneBorder()
     */
    @Override
    protected Border createContentPaneBorder() {
        Color borderColor = UIManager.getColor("TaskPane.borderColor");
        return new CompoundBorder(new ContentPaneBorder(borderColor),
                BorderFactory.createEmptyBorder(contentInsets.top,
                        contentInsets.left, contentInsets.bottom,
                        contentInsets.right));
    }

    class IntelPaneBorder extends PaneBorder {

        /**
         * Description:
         * 
         */
        public IntelPaneBorder() {
            super();
            titleForeground = titleColor;
            titleBackgroundGradientStart = titleBackground;
            titleOver = titleOverColor;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI.PaneBorder#
         * paintTitleBackground(org.jdesktop.swingx.JXTaskPane,
         * java.awt.Graphics)
         */
        @Override
        protected void paintTitleBackground(JXTaskPane group, Graphics g) {
            super.paintTitleBackground(group, g);
            g.setColor(titleBorder);
            g.drawRect(0, 0, group.getWidth() - 1, getTitleHeight(group) - 1);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI.PaneBorder#
         * paintExpandedControls(org.jdesktop.swingx.JXTaskPane,
         * java.awt.Graphics, int, int, int, int)
         */
        @Override
        protected void paintExpandedControls(JXTaskPane group, Graphics g,
                int x, int y, int width, int height) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            if (group.isCollapsed()) {
                g.drawImage(collapseImg.getImage(), x, y, group);
            } else {
                g.drawImage(expandImg.getImage(), x, y, group);
            }

            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI.PaneBorder#
         * isMouseOverBorder()
         */
        @Override
        protected boolean isMouseOverBorder() {
            return true;
        }

    }
}
