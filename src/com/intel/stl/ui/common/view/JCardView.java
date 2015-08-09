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
 *  Functional Group: FabricViewer
 *
 *  File Name: JCard.java
 *
 *  Archive Source: 
 *
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;

/**
 * @author jijunwan
 * 
 */
public abstract class JCardView<E extends ICardListener> extends JPanel {
    private static final long serialVersionUID = 381185145876115191L;

    protected E listener;

    private String title;

    private JPanel titlePanel;

    private JLabel titleLabel;

    private JToolBar toolBar;

    private JButton helpBtn;

    private JButton pinBtn;

    private ActionListener helpListener;

    private ActionListener pinListener;

    private boolean showTitle;

    private Color boderColor = UIConstants.INTEL_BORDER_GRAY;

    private int borderRound = 15;

    private int borderThick = 2;

    public JCardView(String title) {
        this(title, true);
    }

    public JCardView(String title, boolean showTitle) {
        this.title = title;
        this.showTitle = showTitle;

        setLayout(new BorderLayout(0, 0));
        setBackground(UIConstants.INTEL_WHITE);
        setBorder(BorderFactory.createEmptyBorder(borderThick, borderThick,
                borderThick, borderThick));
        setOpaque(false);

        if (showTitle) {
            JPanel panel = getTitlePanel(title);
            if (panel != null) {
                add(panel, BorderLayout.NORTH);
            }
        }

        JComponent mainComp = getMainComponent();
        if (mainComp != null) {
            mainComp.setOpaque(false);
            add(mainComp, BorderLayout.CENTER);
        }
    }

    protected JPanel getTitlePanel(String title) {
        if (titlePanel == null) {
            titlePanel = new JPanel(new BorderLayout(0, 0));
            titlePanel.setOpaque(false);
            titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0,
                    UIConstants.INTEL_PALE_BLUE));

            if (title != null) {
                titleLabel = ComponentFactory.getH4Label(title, Font.PLAIN);
                titleLabel.setForeground(UIConstants.INTEL_DARK_GRAY);
                titleLabel.setBorder(BorderFactory
                        .createEmptyBorder(0, 5, 0, 5));
                titlePanel.add(titleLabel, BorderLayout.WEST);
            }

            toolBar = new JToolBar();
            toolBar.setOpaque(false);
            toolBar.setFloatable(false);
            titlePanel.add(toolBar, BorderLayout.EAST);
            addControlButtons(toolBar);

            JComponent comp = getExtraComponent();
            if (comp != null) {
                comp.setOpaque(false);
                titlePanel.add(comp, BorderLayout.CENTER);
            }
        }
        return titlePanel;
    }

    protected JComponent getExtraComponent() {
        return null;
    }

    protected void addControlButtons(JToolBar toolBar) {
        pinBtn =
                ComponentFactory.getImageButton(UIImages.PIN_ICON
                        .getImageIcon());
        pinBtn.setToolTipText(STLConstants.K0038_PIN_TOOLTIP.getValue());
        toolBar.add(pinBtn);

        helpBtn =
                ComponentFactory.getImageButton(UIImages.HELP_ICON
                        .getImageIcon());
        helpBtn.setToolTipText(STLConstants.K0037_HELP.getValue());
        toolBar.add(helpBtn);
    }

    protected void setPinListener() {
        if (pinListener != null) {
            pinBtn.removeActionListener(pinListener);
        }
        pinListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onPin();
            }
        };
        pinBtn.addActionListener(pinListener);
    }

    protected void setHelpListener() {
        if (helpListener != null) {
            helpBtn.removeActionListener(helpListener);
        }
        helpListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onPin();
            }
        };
        helpBtn.addActionListener(helpListener);
    }

    public JButton getHelpButton() {
        return helpBtn;
    }

    public void setCardListener(final E listener) {
        this.listener = listener;
        if (listener != null) {
            setPinListener();
            setHelpListener();
        }
    }

    public void setTitle(String title) {
        setTitle(title, null);
    }

    public void setTitle(String title, String tooltip) {
        this.title = title;
        if (showTitle && titleLabel != null) {
            titleLabel.setText(title);
            titleLabel.setToolTipText(tooltip);
            validate();
        }
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param boderColor
     *            the boderColor to set
     */
    public void setBoderColor(Color boderColor) {
        this.boderColor = boderColor;
    }

    /**
     * @param round
     *            the round to set
     */
    public void setBorderRound(int round) {
        this.borderRound = round;
    }

    /**
     * @param borderThick
     *            the borderThick to set
     */
    public void setBorderThick(int borderThick) {
        this.borderThick = borderThick;
    }

    protected abstract JComponent getMainComponent();

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Stroke oldStroke = graphics.getStroke();
        Color oldColor = graphics.getColor();

        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width - borderThick, height - borderThick,
                borderRound, borderRound);

        if (borderThick > 0) {
            graphics.setColor(boderColor);
            graphics.setStroke(new BasicStroke(borderThick));
            graphics.drawRoundRect(0, 0, width - borderThick, height
                    - borderThick, borderRound, borderRound);
        }

        graphics.setStroke(oldStroke);
        graphics.setColor(oldColor);
    }

}
