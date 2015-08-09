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
 *  File Name: IntelTabbedPaneUI.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7.2.1  2015/05/06 19:40:33  jijunwan
 *  Archive Log:    L&F improvement on drawing focus border
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/04/30 20:56:53  jijunwan
 *  Archive Log:    added drawing focus border when it gets focus
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/05/09 21:13:42  jijunwan
 *  Archive Log:    minor performance tab look & feel adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/09 21:00:44  jijunwan
 *  Archive Log:    added property; fixed remembering last subpage issue; fixed position problem on IntelTabbedPane
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/08 19:25:43  jijunwan
 *  Archive Log:    MVC refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/02 18:21:53  rjtierne
 *  Archive Log:    Instead of extending class to tweak attributes
 *  Archive Log:    provided new setter methods instead. Derived
 *  Archive Log:    class was removed
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/29 19:14:23  jijunwan
 *  Archive Log:    extended IntelTabbedPaneUI to support a control panel on top-right corner
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/16 15:11:34  jijunwan
 *  Archive Log:    added Intel style Tab and Button
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.intel.stl.ui.common.UIConstants;

public class IntelTabbedPaneUI extends BasicTabbedPaneUI {
    private Color tabBackground;

    private Color tabForeground;

    private Color selectedBackground;

    private Color selectedForeground;

    private Color tabBorder;

    private Insets tabOuterInsets;

    private CtrPanel controlPanel;

    public JPanel getControlPanel() {
        if (controlPanel == null) {
            controlPanel = new CtrPanel();
            controlPanel.setOpaque(false);
            if (tabPane != null) {
                tabPane.add(controlPanel);
            }
        }
        return controlPanel;
    }

    public void setTabBackground(Color tabBackground) {
        this.tabBackground = tabBackground;
    }

    public void setTabForeground(Color tabForeground) {
        this.tabForeground = tabForeground;
    }

    public void setSelectedBackground(Color selectedBackground) {
        this.selectedBackground = selectedBackground;
    }

    public void setSelectedForeground(Color selectedForeground) {
        this.selectedForeground = selectedForeground;
    }

    public void setTabBorder(Color tabBorder) {
        this.tabBorder = tabBorder;
    }

    public void setTabOuterInsets(Insets tabOuterInsets) {
        this.tabOuterInsets = tabOuterInsets;
    }

    public void setTabAreaInsets(Insets tabAreaInsets) {
        this.tabAreaInsets = tabAreaInsets;
    }

    public void setFont(Font fontStyle) {
        tabPane.setFont(fontStyle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#installDefaults()
     */
    @Override
    protected void installDefaults() {
        super.installDefaults();
        tabPane.setOpaque(false);
        tabPane.setBackground(UIConstants.INTEL_WHITE);
        tabPane.setFont(UIConstants.H3_FONT.deriveFont(Font.BOLD));
        tabBorder = UIConstants.INTEL_MEDIUM_DARK_BLUE;
        tabBackground = UIConstants.INTEL_BLUE;
        tabForeground = UIConstants.INTEL_WHITE;
        selectedBackground = UIConstants.INTEL_WHITE;
        selectedForeground = UIConstants.INTEL_DARK_GRAY;
        tabInsets = new Insets(2, 5, 2, 5);
        tabOuterInsets = new Insets(1, 5, 0, 5);
        tabAreaInsets = new Insets(2, 5, 6, 5);
        selectedTabPadInsets = new Insets(2, 2, 0, 1);
        contentBorderInsets = new Insets(5, 2, 3, 3);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#installComponents()
     */
    @Override
    protected void installComponents() {
        if (controlPanel != null) {
            tabPane.add(controlPanel);
        }
        super.installComponents();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#uninstallComponents()
     */
    @Override
    protected void uninstallComponents() {
        super.uninstallComponents();
        if (controlPanel != null) {
            tabPane.remove(controlPanel);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabAreaHeight(int,
     * int, int)
     */
    @Override
    protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount,
            int maxTabHeight) {
        int height =
                super.calculateTabAreaHeight(tabPlacement, horizRunCount,
                        maxTabHeight);
        if (controlPanel != null && horizRunCount > 0) {
            int ctrHeight =
                    controlPanel.getPreferredSize().height
                            + tabAreaInsets.bottom;
            if (height < ctrHeight) {
                tabAreaInsets.top += ctrHeight - height;
                height = ctrHeight;
            }
            int w = controlPanel.getPreferredSize().width;
            if (tabAreaInsets.right < w) {
                tabAreaInsets.right += w;
            }
        }
        return height;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabHeight(int,
     * int, int)
     */
    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex,
            int fontHeight) {
        return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight)
                + tabOuterInsets.top + tabOuterInsets.bottom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabWidth(int, int,
     * java.awt.FontMetrics)
     */
    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex,
            FontMetrics metrics) {
        return super.calculateTabWidth(tabPlacement, tabIndex, metrics)
                + tabOuterInsets.left + tabOuterInsets.right;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.plaf.basic.BasicTabbedPaneUI#paintTabBackground(java.awt.
     * Graphics, int, int, int, int, int, int, boolean)
     */
    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement,
            int tabIndex, int x, int y, int w, int h, boolean isSelected) {

        g.setColor(!isSelected || selectedBackground == null ? tabBackground
                : selectedBackground);
        switch (tabPlacement) {
            case LEFT:
                throw new UnsupportedOperationException(
                        "Unsupported tab placement : LEFT");
            case RIGHT:
                throw new UnsupportedOperationException(
                        "Unsupported tab placement : RIGHT");
            case BOTTOM:
                throw new UnsupportedOperationException(
                        "Unsupported tab placement : BOTTOM");
            case TOP:
            default:
                g.fillRect(x + tabOuterInsets.left, y + tabOuterInsets.top, w
                        - tabOuterInsets.left - tabOuterInsets.right, h
                        - tabOuterInsets.top - tabOuterInsets.bottom);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.plaf.basic.BasicTabbedPaneUI#paintTabBorder(java.awt.Graphics
     * , int, int, int, int, int, int, boolean)
     */
    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
            int x, int y, int w, int h, boolean isSelected) {
        g.setColor(tabBorder);
        switch (tabPlacement) {
            case LEFT:
                throw new UnsupportedOperationException(
                        "Unsupported tab placement : LEFT");
            case RIGHT:
                throw new UnsupportedOperationException(
                        "Unsupported tab placement : RIGHT");
            case BOTTOM:
                throw new UnsupportedOperationException(
                        "Unsupported tab placement : BOTTOM");
            case TOP:
            default:
                g.drawLine(x + tabOuterInsets.left, y + tabOuterInsets.top, x
                        + w - tabOuterInsets.right, y + tabOuterInsets.top);
                g.drawLine(x + tabOuterInsets.left, y + tabOuterInsets.top, x
                        + tabOuterInsets.left, y + h - tabOuterInsets.bottom);
                g.drawLine(x + w - tabOuterInsets.right,
                        y + tabOuterInsets.top, x + w - tabOuterInsets.right, y
                                + h - tabOuterInsets.bottom);
                g.drawLine(x, y + h - tabOuterInsets.bottom, x
                        + tabOuterInsets.left, y + h - tabOuterInsets.bottom);
                g.drawLine(x + w - tabOuterInsets.right, y + h
                        - tabOuterInsets.bottom, x + w, y + h
                        - tabOuterInsets.bottom);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.plaf.basic.BasicTabbedPaneUI#paintContentBorderTopEdge(java
     * .awt.Graphics, int, int, int, int, int, int)
     */
    @Override
    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement,
            int selectedIndex, int x, int y, int w, int h) {
        Rectangle selRect =
                selectedIndex < 0 ? null
                        : getTabBounds(selectedIndex, calcRect);

        g.setColor(tabBackground);

        // Draw unbroken line if tabs are not on TOP, OR
        // selected tab is not in run adjacent to content, OR
        // selected tab is not visible (SCROLL_TAB_LAYOUT)
        //
        if (tabPlacement != TOP || selectedIndex < 0
                || (selRect.y + selRect.height + 1 < y)
                || (selRect.x < x || selRect.x > x + w)) {
            // g.drawLine(x, y, x+w-2, y);
        } else {
            // Break line to show visual connection to selected tab
            g.fillRect(x, y, selRect.x - x + tabOuterInsets.left + 1,
                    tabAreaInsets.bottom);
            if (selRect.x + selRect.width < x + w - 2) {
                g.fillRect(selRect.x + selRect.width - tabOuterInsets.right, y,
                        w - selRect.x - selRect.width + tabOuterInsets.right,
                        tabAreaInsets.bottom);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.plaf.basic.BasicTabbedPaneUI#paintContentBorderLeftEdge(java
     * .awt.Graphics, int, int, int, int, int, int)
     */
    @Override
    protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement,
            int selectedIndex, int x, int y, int w, int h) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.plaf.basic.BasicTabbedPaneUI#paintContentBorderBottomEdge
     * (java.awt.Graphics, int, int, int, int, int, int)
     */
    @Override
    protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement,
            int selectedIndex, int x, int y, int w, int h) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.plaf.basic.BasicTabbedPaneUI#paintContentBorderRightEdge(
     * java.awt.Graphics, int, int, int, int, int, int)
     */
    @Override
    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement,
            int selectedIndex, int x, int y, int w, int h) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.plaf.basic.BasicTabbedPaneUI#paintText(java.awt.Graphics,
     * int, java.awt.Font, java.awt.FontMetrics, int, java.lang.String,
     * java.awt.Rectangle, boolean)
     */
    @Override
    protected void paintText(Graphics g, int tabPlacement, Font font,
            FontMetrics metrics, int tabIndex, String title,
            Rectangle textRect, boolean isSelected) {
        if (isSelected && selectedForeground != null) {
            tabPane.setForegroundAt(tabIndex, selectedForeground);
        } else {
            tabPane.setForegroundAt(tabIndex, tabForeground);
        }
        super.paintText(g, tabPlacement, font, metrics, tabIndex, title,
                textRect, isSelected);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.plaf.basic.BasicTabbedPaneUI#paintFocusIndicator(java.awt
     * .Graphics, int, java.awt.Rectangle[], int, java.awt.Rectangle,
     * java.awt.Rectangle, boolean)
     */
    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement,
            Rectangle[] rects, int tabIndex, Rectangle iconRect,
            Rectangle textRect, boolean isSelected) {
        Rectangle tabRect = rects[tabIndex];
        if (tabPane.hasFocus() && isSelected) {
            int x, y, w, h;
            g.setColor(focus);
            switch (tabPlacement) {
                case LEFT:
                    throw new UnsupportedOperationException(
                            "Unsupported tab placement : LEFT");
                case RIGHT:
                    throw new UnsupportedOperationException(
                            "Unsupported tab placement : LEFT");
                case BOTTOM:
                    throw new UnsupportedOperationException(
                            "Unsupported tab placement : LEFT");
                case TOP:
                default:
                    x = tabRect.x + tabOuterInsets.left + 3;
                    y = tabRect.y + tabOuterInsets.top + 3;
                    w =
                            tabRect.width - tabOuterInsets.left
                                    - tabOuterInsets.right - 6;
                    h =
                            tabRect.height - tabOuterInsets.top
                                    - tabOuterInsets.bottom - 5;
            }
            BasicGraphicsUtils.drawDashedRect(g, x, y, w, h);
        }
    }

    /**
     * Invoked by <code>installUI</code> to create a layout manager object to
     * manage the <code>JTabbedPane</code>.
     * 
     * @return a layout manager object
     * 
     * @see TabbedPaneLayout
     * @see javax.swing.JTabbedPane#getTabLayoutPolicy
     */
    @Override
    protected LayoutManager createLayoutManager() {
        return new LayoutWrapper(super.createLayoutManager());
    }

    private class CtrPanel extends JPanel implements UIResource {
        private static final long serialVersionUID = 1L;

    }

    private class LayoutWrapper extends TabbedPaneLayout {
        private final LayoutManager real;

        public LayoutWrapper(LayoutManager real) {
            super();
            this.real = real;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
         * java.awt.Component)
         */
        @Override
        public void addLayoutComponent(String name, Component comp) {
            real.addLayoutComponent(name, comp);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
         */
        @Override
        public void removeLayoutComponent(Component comp) {
            real.removeLayoutComponent(comp);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
         */
        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return real.preferredLayoutSize(parent);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
         */
        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return real.minimumLayoutSize(parent);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
         */
        @Override
        public void layoutContainer(Container parent) {
            real.layoutContainer(parent);
            Insets insets = tabPane.getInsets();
            if (controlPanel != null) {
                Dimension ps = controlPanel.getPreferredSize();
                int h =
                        calculateTabAreaHeight(tabPane.getTabPlacement(),
                                runCount, maxTabHeight);
                controlPanel.setBounds(tabPane.getWidth() - insets.right
                        - ps.width,
                        Math.max(0, h - ps.height - tabAreaInsets.bottom),
                        ps.width, ps.height);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout#
         * calculateLayoutInfo()
         */
        @Override
        public void calculateLayoutInfo() {
            if (real instanceof TabbedPaneLayout) {
                ((TabbedPaneLayout) real).calculateLayoutInfo();
            }
        }

    }

}
