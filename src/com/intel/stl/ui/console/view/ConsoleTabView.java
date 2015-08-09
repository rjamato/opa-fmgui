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
 *  File Name: ConsoleTabView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/04/09 21:13:58  rjtierne
 *  Archive Log:    Put call to closeConsole() in a separate thread to fix synchronization problem with closing consoles too fast
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/01 19:51:16  rjtierne
 *  Archive Log:    Provide console tab view with console id so the listener terminates the correct console
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/09 18:23:39  jijunwan
 *  Archive Log:    only show command on tab
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/05 21:56:32  jijunwan
 *  Archive Log:    L&F adjustment on Console Views
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 19:53:59  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: View for the tab on a console subpage tabbed pane to house
 *  the labels for user name, command name, and close button
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.console.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.console.IConsoleListener;
import com.intel.stl.ui.console.ITabListener;

public class ConsoleTabView extends JPanel {

    private static final long serialVersionUID = -4025941288575285019L;

    private static final byte USER_NAME_IDX = 0;

    private static final byte COMMAND_NAME_IDX = 1;

    private JLabel lblCloseTab;

    private JLabel lblUserName;

    private JLabel lblCommandName;

    private final String[] tabNames;

    private ITabListener tabListener;

    private int tabIndex;

    private final int consoleId;

    @SuppressWarnings("unused")
    private IConsoleListener consoleListener;

    private final ConsoleTabView tabView = this;

    public ConsoleTabView(int tabIndex, String[] tabNames, int consoleId) {
        super();
        this.tabIndex = tabIndex;
        this.tabNames = tabNames;
        this.consoleId = consoleId;
        initComponent();
    }

    protected void initComponent() {

        setLayout(new GridBagLayout());
        setOpaque(false);
        lblUserName = ComponentFactory.getH4Label("", Font.PLAIN);

        lblCloseTab = new JLabel(UIImages.CLOSE_GRAY.getImageIcon());
        lblCloseTab.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(0, 2, 0, 0, UIConstants.INTEL_BORDER_GRAY),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)));

        lblCommandName = ComponentFactory.getH4Label("", Font.PLAIN);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(1, 2, 1, 2);
        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 0;
        gc.gridy = 0;
        add(lblUserName, gc);

        gc.gridx = 1;
        gc.gridheight = 2;
        gc.weighty = 1;
        gc.insets = new Insets(1, 2, 1, 2);
        add(lblCloseTab, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weighty = 0;
        gc.insets = new Insets(1, 2, 1, 2);
        add(lblCommandName, gc);

        setUserName(tabNames[USER_NAME_IDX]);
        setCommandName(tabNames[COMMAND_NAME_IDX]);
    }

    public void addConsoleListener(IConsoleListener listener) {
        this.consoleListener = listener;
    }

    public void addTabListener(ITabListener listener) {

        tabListener = listener;

        lblCloseTab.addMouseListener((new MouseAdapter() {
            private Icon oldIcon;

            @Override
            public void mouseClicked(MouseEvent e) {

                Runnable runit = new Runnable() {

                    @Override
                    public void run() {
                        tabListener.closeConsole(tabView);
                    }
                };
                runit.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                oldIcon = lblCloseTab.getIcon();
                lblCloseTab.setIcon(UIImages.CLOSE_RED.getImageIcon());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblCloseTab.setIcon(oldIcon);
            }

        }));
    }

    public JPanel getMainComponent() {
        return this;
    }

    /**
     * @return the lblUserName
     */
    public String getUserName() {
        return lblUserName.getText();
    }

    /**
     * @param lblUserName
     *            the lblUserName to set
     */
    public void setUserName(String userName) {
        this.lblUserName.setText(userName);
    }

    /**
     * @return the lblCommandName
     */
    public String getCommandName() {
        return lblCommandName.getText();
    }

    /**
     * @param lblCommandName
     *            the lblCommandName to set
     */
    public void setCommandName(String command) {
        this.lblCommandName.setText(getCommandName(command));
    }

    protected String getCommandName(String cmd) {
        if (cmd == null) {
            return "";
        }

        int pos = cmd.indexOf(' ');
        if (pos >= 0) {
            return cmd.substring(0, pos);
        } else {
            return cmd;
        }
    }

    /**
     * @return the tabIndex
     */
    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int index) {
        tabIndex = index;
    }

    /**
     * @return the consoleId
     */
    public int getConsoleId() {
        return consoleId;
    }

    public void setLabelProperties(boolean highlight) {

        if (highlight) {
            lblUserName.setBackground(UIConstants.INTEL_WHITE);
            lblUserName.setForeground(UIConstants.INTEL_DARK_GRAY);

            lblCloseTab.setIcon(UIImages.CLOSE_GRAY.getImageIcon());

            lblCommandName.setBackground(UIConstants.INTEL_WHITE);
            lblCommandName.setForeground(UIConstants.INTEL_DARK_GRAY);
        } else {
            lblUserName.setBackground(UIConstants.INTEL_BLUE);
            lblUserName.setForeground(UIConstants.INTEL_WHITE);

            lblCloseTab.setIcon(UIImages.CLOSE_WHITE.getImageIcon());

            lblCommandName.setBackground(UIConstants.INTEL_BLUE);
            lblCommandName.setForeground(UIConstants.INTEL_WHITE);
        }
    }

}
