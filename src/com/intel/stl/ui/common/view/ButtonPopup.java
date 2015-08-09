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
 *  File Name: ButtonPopup.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/12/10 20:52:22  rjtierne
 *  Archive Log:    Support for new Setup Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/24 20:09:00  jijunwan
 *  Archive Log:    adjust popup panel's location to ensure it's not out of screen
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/21 16:33:19  fernande
 *  Archive Log:    Component used to display a popup window associated with a button
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import static java.awt.event.KeyEvent.VK_ESCAPE;
import static javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.Popup;
import javax.swing.SwingUtilities;

import com.intel.stl.ui.common.Util;

/**
 * ButtonPopup is a generic popup component that can be used together with a
 * button. The contents of the popup is user-defined through a component. It
 * supports the usual show() and hide() methods of a popup, but additionally, it
 * provides support for closing the popup when focus is lost, and also for
 * closing the popup when ESC is pressed. The main difference between both
 * behaviors is that onHide() is not called when ESC is pressed.
 */
public abstract class ButtonPopup extends Popup implements WindowFocusListener,
        MouseListener {

    private final static String RESET_ACTION = "resetPopup";

    public final static short BUTTON_PRESSED = 0;

    public final static short FOCUS_LOST = 1;

    public final static short ESC_PRESSED = 2;

    private JWindow popup = null;

    private short hideReason = BUTTON_PRESSED;

    private boolean hideReasonNotSet = true;

    private final AbstractButton button;

    private final JComponent component;

    private boolean isFrame = true;

    public ButtonPopup(AbstractButton button, JComponent component) {
        super();
        this.component = component;
        this.button = button;
        this.button.addMouseListener(this);
    }

    public ButtonPopup(AbstractButton button, JComponent component,
            boolean isFrame) {
        super();
        this.component = component;
        this.button = button;
        this.button.addMouseListener(this);
        this.isFrame = isFrame;

    }

    @Override
    public void show() {
        onShow();
        if (popup == null) {
            createPopup();
        }
        Point location = button.getLocation();
        SwingUtilities.convertPointToScreen(location, button.getParent());
        int dy =
                button.getHeight()
                        + (button.getBorder() == null ? 0 : button.getBorder()
                                .getBorderInsets(button).bottom);
        location.translate(0, dy);
        Dimension dim = component.getPreferredSize();
        location =
                Util.adjustPoint(new Rectangle(location.x, location.y,
                        dim.width, dim.height), SwingUtilities
                        .getWindowAncestor(button));
        popup.setLocation(location);
        popup.addWindowFocusListener(this);
        popup.setSize(dim);
        popup.validate();
        popup.setVisible(true);
        popup.requestFocus();
        hideReasonNotSet = true;
    }

    @Override
    public void hide() {
        popup.setVisible(false);
        popup.removeWindowFocusListener(this);
        onHide();
        hideReasonNotSet = true;
    }

    public boolean isVisible() {
        return popup != null && popup.isVisible();
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        // No-operation
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (popup.isVisible()) {
                    if (hideReasonNotSet) {
                        hideReason = FOCUS_LOST;
                    }
                    button.doClick();
                }
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // No-operation
    }

    /*
     * This event is used to set the proper reason why the popup is being
     * hidden; mousePressed is invoked before the windowLostFocus event for the
     * popup and we use this fact to differentiate two scenarios: 1) popup loses
     * focus because the button is pressed (hideReason should be
     * BUTTON_PRESSED); 2) popup loses focus because user clicks outside the
     * popup but not on the button (hideReason should be FOCUS_LOST). This way,
     * the implementor can code different behaviors for all three conditions.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        hideReason = BUTTON_PRESSED;
        hideReasonNotSet = false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // No-operation
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // No-operation
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // No-operation
    }

    public JComponent getContentPane() {
        return component;
    }

    public short getHideReason() {
        return hideReason;
    }

    protected void reset() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (popup.isVisible()) {
                    hideReason = ESC_PRESSED;
                    button.doClick();
                }
            }
        });
    }

    /**
     * 
     * <i>Description:</i> invoked before the popup is displayed
     * 
     */
    public abstract void onShow();

    /**
     * 
     * <i>Description:</i> invoked after the popup is closed, except when ESC is
     * pressed
     * 
     */
    public abstract void onHide();

    private void createPopup() {

        if (isFrame) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(button);
            popup = new JWindow(frame);
        } else {
            JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(button);
            popup = new JWindow(dialog);
        }

        popup.setFocusable(true);
        popup.setContentPane(component);
        component.setBorder(new JPopupMenu().getBorder());
        component.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(VK_ESCAPE, 0), RESET_ACTION);
        Action resetAction = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        };
        component.getActionMap().put(RESET_ACTION, resetAction);
        popup.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    reset();
                }
            }
        });
    }
}
