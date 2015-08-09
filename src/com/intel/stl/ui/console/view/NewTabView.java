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
 *  File Name: NewTabView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2014/09/23 19:47:01  rjtierne
 *  Archive Log:    Integration of Gritty for Java Console
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/09 20:03:27  rjtierne
 *  Archive Log:    Added default login bean to console dialog to reduce typing
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/09 14:20:54  rjtierne
 *  Archive Log:    Restructured code to accommodate new console login dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 19:53:59  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: View for the tab on a console subpage tabbed pane to house
 *  the labels for adding ("+") a new console.
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.console.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.console.IConsoleEventListener;
import com.intel.stl.ui.console.ITabListener;
import com.intel.stl.ui.console.LoginBean;

public class NewTabView extends JPanel implements ITabListener {

    private static final long serialVersionUID = 6934999496353209087L;

    private final IConsoleEventListener consoleEventListener;

    private JLabel lblPlus;

    private final LoginBean defaultLoginBean;

    public NewTabView(LoginDialogView loginDialogView,
            LoginBean defaultLoginBean,
            IConsoleEventListener consoleEventListener) {

        // this.loginDialogView = loginDialogView;
        this.defaultLoginBean = defaultLoginBean;
        this.consoleEventListener = consoleEventListener;
        initComponents();
    }

    protected void initComponents() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.INTEL_BLUE);

        lblPlus = ComponentFactory.getH2Label("+", Font.PLAIN);
        lblPlus.setBackground(UIConstants.INTEL_BLUE);
        lblPlus.setForeground(UIConstants.INTEL_WHITE);
        add(lblPlus, BorderLayout.CENTER);
        addPlusLabelMouseListener();
    }

    protected void addPlusLabelMouseListener() {
        lblPlus.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                consoleEventListener.addNewConsole(defaultLoginBean, true, "");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblPlus.setForeground(UIConstants.INTEL_RED);

            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblPlus.setForeground(UIConstants.INTEL_WHITE);

            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.console.ITabListener#closeConsole(com.intel.stl.ui.console
     * .view.ConsoleTabView)
     */
    @Override
    public void closeConsole(ConsoleTabView tabView) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.ITabListener#enableNewTab(boolean)
     */
    @Override
    public void enableNewTab(boolean enabled) {

        lblPlus.setEnabled(enabled);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.ITabListener#getNewTabView()
     */
    @Override
    public NewTabView getNewTabView() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.ITabListener#getCurrentTabView()
     */
    @Override
    public ConsoleTabView getCurrentTabView() {
        // TODO Auto-generated method stub
        return null;
    }

}
