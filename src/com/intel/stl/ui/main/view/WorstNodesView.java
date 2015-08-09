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
package com.intel.stl.ui.main.view;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.jdesktop.swingx.JXHyperlink;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.JCardView;
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.model.NodeScore;

/**
 * @author jijunwan
 * 
 */
public class WorstNodesView extends JCardView<IWorstNodesListener> implements
        ActionListener {
    private static final long serialVersionUID = 3495065577765787438L;

    // Fixed interval value close to typical ClickInterval on Windows platform
    // Needed to work around bug in JDK on Mac OS X.
    private static int clickInterval = 200;
    
    private final static String NODE_SCORE = "NodeScore";

    public static final int NUM_NODES = 10;

    private JPanel mainPanel;

    private JPanel nodesPanel;

    private JXHyperlink moreBtn;

    private final WorstNodePopup popupComp;

    private final PopupFactory factory;

    private Popup popup;

    private int size;

    private int rows, columns;

    private MouseEvent mouseEvent;

    private final Timer timer;

    /**
     * @param title
     * @param controller
     */
    public WorstNodesView() {
        super(STLConstants.K0106_WORST_NODES.getValue());
        
        try {
        	clickInterval = ((Integer) Toolkit
                    .getDefaultToolkit().getDesktopProperty("awt.multiClickInterval")).intValue();
        } catch (Exception e) {
                // On some platforms this property is not set in AWT, so
                // continue execution with initially set value of 200
                System.err.println("awt.multiClickInterval is not available on " 
                                   + System.getProperty("os.name"));
        } 
        
        // this is unnecessary, but can stop klocwork from complaining
        getMainComponent();
        factory = PopupFactory.getSharedInstance();
        popupComp = new WorstNodePopup();
        timer = new Timer(clickInterval, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.common.JCard#getMainPanel()
     */
    @Override
    protected JPanel getMainComponent() {
        if (mainPanel != null) {
            return mainPanel;
        }

        mainPanel = new JPanel();
        GridBagLayout gridBag = new GridBagLayout();
        mainPanel.setLayout(gridBag);
        GridBagConstraints gc = new GridBagConstraints();

        gc.fill = GridBagConstraints.BOTH;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.insets = new Insets(0, 5, 0, 1);
        gc.weightx = 1;
        gc.weighty = 1;
        JPanel panel = getNodesPanel();
        mainPanel.add(panel, gc);

        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = new Insets(2, 1, 5, 5);
        gc.weightx = 0;
        gc.weighty = 0;
        moreBtn =
                new JXHyperlink(new AbstractAction(
                        STLConstants.K0036_MORE.getValue()) {
                    private static final long serialVersionUID =
                            4612692450375442721L;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        listener.onMore();
                    }

                });
        moreBtn.setUnclickedColor(UIConstants.INTEL_BLUE);
        mainPanel.add(moreBtn, gc);

        return mainPanel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.view.JCardView#setCardListener(com.intel.stl.
     * ui.common.view.ICardListener)
     */
    @Override
    public void setCardListener(IWorstNodesListener listener) {
        super.setCardListener(listener);
        popupComp.setListener(listener);
    }

    protected JPanel getNodesPanel() {
        if (nodesPanel == null) {
            nodesPanel = new JPanel(new GridBagLayout());
            nodesPanel.setOpaque(false);
            nodesPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 2, 2));
            setSize(NUM_NODES);
        }
        return nodesPanel;
    }

    public void setSize(int size) {
        this.size = size;
        int ratio = 5;
        // int ratio = nodesPanel.getHeight()==0 ? 5:
        // nodesPanel.getWidth()/nodesPanel.getHeight();
        rows = (int) Math.ceil(Math.sqrt(size / ratio));
        columns = ratio * rows;
    }

    public void updateNodes2(NodeScore[] nodes) {
        nodesPanel.removeAll();
        for (NodeScore node : nodes) {
            // JLabelBar label = new
            // JLabelBar(Integer.toString((int)node.getScore()), JLabel.CENTER,
            // node.getScore()/100, node.getColor(), JLabel.LEFT);
            // label.setBarSize(3);
            // label.setBarInsets(new Insets(0, 2, 2, 0));
            // label.setForeground(UIConstants.INTEL_DARK_GRAY);
            JLabel label =
                    ComponentFactory.getH6Label(node.getScoreString(),
                            Font.BOLD);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setForeground(node.getColor());
            label.setToolTipText(node.getDescription());
            nodesPanel.add(label);
        }
        for (int i = 0; i < nodes.length; i++) {
            final NodeScore node = nodes[i];
            JLabel label = new JLabel(node.getIcon());
            label.setToolTipText(node.getDescription());
            label.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!SwingUtilities.isLeftMouseButton(e)) {
                        return;
                    }

                    mouseEvent = e;
                    if (timer.isRunning() && !e.isConsumed()
                            && e.getClickCount() > 1) {
                        // double click
                        timer.stop();
                        NodeScore node =
                                (NodeScore) ((JLabel) e.getComponent())
                                        .getClientProperty(NODE_SCORE);
                        listener.jumpTo(node.getLid(), node.getType(),
                                JumpDestination.DEFAULT);
                    } else {
                        timer.restart();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    e.getComponent().setCursor(
                            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    e.getComponent().setCursor(
                            Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            });
            nodesPanel.add(label);
        }
        validate();
        repaint();
    }

    public void updateNodes(NodeScore[] nodes) {
        nodesPanel.removeAll();
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
        for (int i = 0; i < nodes.length; i++) {
            NodeScore node = nodes[i];
            JLabel label = new JLabel(node.getIcon());
            label.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                    .createLineBorder(UIConstants.INTEL_BORDER_GRAY),
                    BorderFactory.createMatteBorder(0, 0, 5, 0,
                            nodes[i].getColor())));
            label.setToolTipText(nodes[i].getDescription());
            label.putClientProperty(NODE_SCORE, node);
            label.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!SwingUtilities.isLeftMouseButton(e)) {
                        return;
                    }

                    mouseEvent = e;
                    if (timer.isRunning() && !e.isConsumed()
                            && e.getClickCount() > 1) {
                        // double click
                        timer.stop();
                        NodeScore node =
                                (NodeScore) ((JLabel) e.getComponent())
                                        .getClientProperty(NODE_SCORE);
                        listener.jumpTo(node.getLid(), node.getType(),
                                JumpDestination.DEFAULT);
                    } else {
                        timer.restart();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    e.getComponent().setCursor(
                            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    e.getComponent().setCursor(
                            Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            });
            gc.gridwidth =
                    ((i + 1) % columns == 0) ? GridBagConstraints.REMAINDER : 1;
            nodesPanel.add(label, gc);
        }
        validate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // single click
        timer.stop();
        JLabel label = (JLabel) mouseEvent.getComponent();
        // ignore if this label was already removed
        if (label.getParent() == null) {
            return;
        }
        NodeScore node = (NodeScore) label.getClientProperty(NODE_SCORE);
        popupComp.setNode(node);
        Point p = getPopupLocation(mouseEvent, popupComp);
        popup =
                factory.getPopup(mouseEvent.getComponent(), popupComp, p.x, p.y);
        popupComp.setPopup(popup);
        popup.show();
    }

    protected Point getPopupLocation(MouseEvent e, Component comp) {
        Point p = e.getPoint();
        SwingUtilities.convertPointToScreen(p, e.getComponent());
        p.x = Math.max(0, p.x - 5);
        p.y = Math.max(0, p.y - 5);
        Dimension d = comp.getPreferredSize();
        Rectangle rec = new Rectangle(p.x, p.y, d.width, d.height);
        p = Util.adjustPoint(rec, SwingUtilities.getWindowAncestor(this));
        return p;
    }

    public void clear() {
        nodesPanel.removeAll();
    }

}
