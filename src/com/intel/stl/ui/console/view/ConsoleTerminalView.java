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
 *  File Name: ConsoleTerminalView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.19.2.1  2015/05/06 19:39:23  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/05/01 21:29:17  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/04/28 22:09:04  jijunwan
 *  Archive Log:    removed title argument from #showErrorMessage
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/04/10 14:07:10  rjtierne
 *  Archive Log:    PR 126675 - User cannot execute commands on duplicate Console numbers beyond 10 consoles.
 *  Archive Log:    In method commandSendAction(), verified that # channels doesn't exceed 10 before creating
 *  Archive Log:    a new one.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/04/09 21:14:19  rjtierne
 *  Archive Log:    126675 - User cannot execute commands on duplicate Console numbers beyond 10 consoles.
 *  Archive Log:    In method commandSendAction(), verified that # channels doesn't exceed 10 before creating
 *  Archive Log:    a new one.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/03/05 17:34:32  jijunwan
 *  Archive Log:    new constants and constant name change
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/10/29 21:42:01  rjtierne
 *  Archive Log:    Provide null pointer protection for command combo box
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/10/28 22:19:29  rjtierne
 *  Archive Log:    Added setter to toggle enable on command field
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/10/24 14:37:00  rjtierne
 *  Archive Log:    Moved view enable logic out of btnLock action listener and replaced with display
 *  Archive Log:    of login dialog and invocation of new method toggleLock()
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/10/22 15:42:43  rjtierne
 *  Archive Log:    Added null pointer protection to call to updateSelection()
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/10/21 14:05:14  rjtierne
 *  Archive Log:    Moved view components out of controller and into view. Referencing the
 *  Archive Log:    main window to center dialogs
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/10/17 19:59:17  rjtierne
 *  Archive Log:    Force help system to navigate to "Report Types" after -o option is typed
 *  Archive Log:    for iba_report
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/10/17 19:35:10  rjtierne
 *  Archive Log:    Calling parser to process user input to navigate help system. Remove
 *  Archive Log:    command filter so all commands will pass
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/13 14:56:46  rjtierne
 *  Archive Log:    Remove redundant key listener
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/01 19:53:03  rjtierne
 *  Archive Log:    Add auto complete to combo box.  Display error dialog if illegal attempt is made to send illegal command
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/09/23 19:47:01  rjtierne
 *  Archive Log:    Integration of Gritty for Java Console
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/09 20:48:44  rjtierne
 *  Archive Log:    Make the command field the focus when a console terminal appears
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/09 20:04:36  rjtierne
 *  Archive Log:    Don't update current console tab if command is executed on a new tab
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/09 18:23:12  jijunwan
 *  Archive Log:    disabled command combobox
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/05 21:56:32  jijunwan
 *  Archive Log:    L&F adjustment on Console Views
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 19:53:59  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: View for the overall console subpage containing:
 *      1. Info panel with Host, Port, and User
 *      2. Command panel with command field and current/new radio buttons
 *      3. SSH console into selected remote system
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.console.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.IntelComboBoxUI;
import com.intel.stl.ui.console.ConsoleDispatchManager;
import com.intel.stl.ui.console.IConsoleListener;
import com.intel.stl.ui.console.LoginBean;
import com.intel.stl.ui.main.view.IFabricView;

public class ConsoleTerminalView extends JPanel {

    private static final long serialVersionUID = 4230538658726413678L;

    private JPanel ctrPanel;

    private JPanel serverInfoPanel;

    private JLabel lblHostValue;

    private JLabel lblPortValue;

    private JLabel lblUserValue;

    private JButton btnLock;

    private JComboBox<String> boxCommand;

    private JButton btnSend;

    private JPanel optionPanel;

    private JRadioButton rbtnCurrentTab;

    private JRadioButton rbtnNewTab;

    private TerminalCardView terminalCardView;

    private String hostName = new String("");

    private String portNum = new String("");

    private String userName = new String("");

    private LoginBean loginBean;

    private IConsoleListener consoleListener;

    private IFabricView owner;

    // Added this comment to correct PR 126675 comment above
    public ConsoleTerminalView(IFabricView owner) {
        super();
        initComponents();
        createButtonGroup();
    }

    protected void initComponents() {
        setLayout(new BorderLayout(0, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 2, 2, 2));
        JPanel panel = getControlPanel();
        add(panel, BorderLayout.NORTH);

        terminalCardView =
                new TerminalCardView(STLConstants.K2107_ADM_CONSOLE.getValue());
        add(terminalCardView, BorderLayout.CENTER);
    }

    protected JPanel getControlPanel() {
        if (ctrPanel == null) {
            ctrPanel = new JPanel(new GridBagLayout());
            ctrPanel.setBorder(BorderFactory.createTitledBorder((String) null));
            ctrPanel.setBackground(UIConstants.INTEL_WHITE);
            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.insets = new Insets(1, 10, 2, 5);
            gc.gridwidth = 1;
            gc.weightx = 0;

            JLabel server =
                    ComponentFactory.getH4Label(
                            STLConstants.K1053_SERVER_INFO.getValue(),
                            Font.BOLD);
            ctrPanel.add(server, gc);

            gc.weightx = 1;
            JPanel serverInfo = getServerInfoPanel();
            ctrPanel.add(serverInfo, gc);

            gc.weightx = 0;
            gc.gridwidth = GridBagConstraints.REMAINDER;
            btnLock =
                    ComponentFactory
                            .getIntelDeleteButton(STLConstants.K1051_LOCK
                                    .getValue());
            btnLock.setSelected(true);
            btnLock.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean isSelected = btnLock.isSelected();

                    if (isSelected) {
                        consoleListener.onLock(isSelected);

                        toggleLock(!isSelected);

                        btnLock.setSelected(!isSelected);
                        if (isSelected) {
                            btnLock.setText(STLConstants.K1052_UNLOCK
                                    .getValue());
                        } else {
                            btnLock.setText(STLConstants.K1051_LOCK.getValue());
                        }
                    } else {
                        consoleListener.showLoginDialog();
                    }
                }
            });
            ctrPanel.add(btnLock, gc);

            gc.gridwidth = 1;
            JLabel cmd =
                    ComponentFactory.getH4Label(
                            STLConstants.K1044_COMMAND_TITLE.getValue(),
                            Font.BOLD);
            ctrPanel.add(cmd, gc);

            gc.weightx = 1;
            boxCommand = new JComboBox<String>();
            boxCommand.setUI(new IntelComboBoxUI());
            boxCommand.setEditable(true);
            AutoCompleteDecorator.decorate(boxCommand);
            boxCommand.getEditor().getEditorComponent()
                    .addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent event) {
                            if (event.getKeyChar() == KeyEvent.VK_ENTER) {
                                commandSendAction();
                            }
                        }
                    });
            boxCommand.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent arg0) {

                    // Pass everything that was typed on the command line
                    // to the parser
                    String entry =
                            ((JTextField) boxCommand.getEditor()
                                    .getEditorComponent()).getText();

                    if (entry.startsWith("iba_report -o ")) {
                        entry = "iba_report -reporttypes";
                    }
                    consoleListener.getHelpController().parseCommand(entry);

                    // Only pass the command name to the Help comboBox
                    if (entry.split(" ").length > 0) {
                        consoleListener.getHelpController().updateSelection(
                                entry.split(" ")[0]);
                    }
                }

            });
            ctrPanel.add(boxCommand, gc);

            gc.weightx = 0;
            gc.gridwidth = GridBagConstraints.REMAINDER;
            btnSend =
                    ComponentFactory
                            .getIntelActionButton(STLConstants.K1045_SEND
                                    .getValue());
            btnSend.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    commandSendAction();
                }
            });
            ctrPanel.add(btnSend, gc);

            gc.gridwidth = 2;
            gc.weightx = 1;
            JPanel optionPanel = getOptionPanel();
            ctrPanel.add(optionPanel, gc);
        }
        return ctrPanel;
    }

    protected JPanel getServerInfoPanel() {
        if (serverInfoPanel == null) {
            serverInfoPanel = new JPanel(new GridBagLayout());
            serverInfoPanel.setOpaque(false);

            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.insets = new Insets(1, 2, 2, 2);
            gc.gridwidth = 1;
            gc.weightx = 0;

            JLabel lblHost =
                    ComponentFactory.getH4Label(
                            STLConstants.K0051_HOST.getValue() + ": ",
                            Font.PLAIN);
            serverInfoPanel.add(lblHost, gc);

            gc.weightx = 1;
            lblHostValue = ComponentFactory.getH4Label("N/A", Font.ITALIC);
            serverInfoPanel.add(lblHostValue, gc);

            gc.weightx = 0;
            JLabel lblPort =
                    ComponentFactory.getH4Label(
                            STLConstants.K1035_CONFIGURATION_PORT.getValue()
                                    + ": ", Font.PLAIN);
            serverInfoPanel.add(lblPort, gc);

            gc.weightx = 1;
            lblPortValue = ComponentFactory.getH4Label("N/A", Font.ITALIC);
            serverInfoPanel.add(lblPortValue, gc);

            gc.weightx = 0;
            JLabel lblUser =
                    ComponentFactory.getH4Label(
                            STLConstants.K0602_USER_NAME.getValue() + ": ",
                            Font.PLAIN);
            serverInfoPanel.add(lblUser, gc);

            gc.gridwidth = GridBagConstraints.REMAINDER;
            gc.weightx = 1;
            lblUserValue = ComponentFactory.getH4Label("N/A", Font.ITALIC);
            serverInfoPanel.add(lblUserValue, gc);
        }
        return serverInfoPanel;
    }

    protected JPanel getOptionPanel() {
        if (optionPanel == null) {
            optionPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 5, 0));
            optionPanel.setOpaque(false);

            rbtnCurrentTab =
                    new JRadioButton(STLConstants.K1046_CURRENT_TAB.getValue());
            rbtnCurrentTab.setOpaque(false);
            rbtnCurrentTab.setFont(UIConstants.H5_FONT);
            rbtnCurrentTab.setForeground(UIConstants.INTEL_DARK_GRAY);
            rbtnCurrentTab.setSelected(true);
            optionPanel.add(rbtnCurrentTab);

            rbtnNewTab =
                    new JRadioButton(STLConstants.K1047_NEW_TAB.getValue());
            rbtnNewTab.setOpaque(false);
            rbtnNewTab.setFont(UIConstants.H5_FONT);
            rbtnNewTab.setForeground(UIConstants.INTEL_DARK_GRAY);
            rbtnNewTab.setSelected(false);
            optionPanel.add(rbtnNewTab);
        }
        return optionPanel;
    }

    protected void commandSendAction() {
        String command = (String) boxCommand.getSelectedItem();

        // If selection is current tab, then put the
        // command on this console's queue. Otherwise, create a new
        // console and add the command to that queue.
        if ((command != null) && (command.length() != 0)) {
            addCommand(command);
            boxCommand.getEditor().selectAll();

            if (isCurrentTabSelected()) {
                consoleListener.addToQueue(command);
                consoleListener.updatePersonalizedTab(command);

            } else if (isNewTabSelected()) {

                if (consoleListener.isConsoleAllowed()) {
                    consoleListener.addNewConsole(command);
                    boxCommand.setSelectedIndex(-1);
                    rbtnNewTab.setSelected(false);
                    rbtnCurrentTab.setSelected(true);
                } else {
                    Util.showErrorMessage(
                            this,
                            UILabels.STL80009_MAX_CHANNELS_IN_SESSION
                                    .getDescription(ConsoleDispatchManager.MAX_NUM_CONSOLES_IN_SESSION));
                }
            }
            boxCommand.getEditor().setItem("");
        }

    }

    public void addCommand(String cmd) {
        for (int i = 0; i < boxCommand.getItemCount(); i++) {
            String history = boxCommand.getItemAt(i);
            if (cmd.equals(history)) {
                return;
            }
        }

        if (boxCommand != null) {
            boxCommand.addItem(cmd);
        }
    }

    public void setCmdFieldEnable(boolean enable) {
        boxCommand.setEnabled(enable);
    }

    protected void createButtonGroup() {
        ButtonGroup rbtnGroup = new ButtonGroup();
        rbtnGroup.add(rbtnCurrentTab);
        rbtnGroup.add(rbtnNewTab);
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName
     *            the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
        this.lblHostValue.setText(hostName);
    }

    /**
     * @return the portNum
     */
    public String getPortNum() {
        return portNum;
    }

    /**
     * @param portNum
     *            the portNum to set
     */
    public void setPortNum(String portNum) {
        this.portNum = portNum;
        this.lblPortValue.setText(portNum);
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
        this.lblUserValue.setText(userName);
    }

    /**
     * @return the terminalView
     */
    public void setTermPanel(IntelTerminalView panel) {
        terminalCardView.setTermPanel(panel);
    }

    public void displayMaxConsoles(boolean enable) {
        terminalCardView.displayMaxConsoles(enable);
    }

    public void setConsoleListener(IConsoleListener listener) {
        consoleListener = listener;
    }

    public boolean isCurrentTabSelected() {
        return rbtnCurrentTab.isSelected();
    }

    public boolean isNewTabSelected() {
        return rbtnNewTab.isSelected();
    }

    public void setFocus() {

        boxCommand.requestFocus();
    }

    public void enableCommanding(boolean enable) {
        boxCommand.setEnabled(enable);
        btnSend.setEnabled(enable);
    }

    public void enableNewTab(boolean enable) {
        rbtnNewTab.setEnabled(enable);
    }

    /**
     * @return the loginBean
     */
    public LoginBean getLoginBean() {
        return loginBean;
    }

    /**
     * @param loginBean
     *            the loginBean to set
     */
    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public Object[] getPassword(String username) {
        final JPasswordField pwdFld = new JPasswordField();

        int result =
                JOptionPane.showConfirmDialog((Component) owner, new Object[] {
                        STLConstants.K1065_ENTER_PASSWORD.getValue() + " "
                                + userName, pwdFld },
                        STLConstants.K1064_SESSION_AUTHENTICATION.getValue(),
                        JOptionPane.OK_CANCEL_OPTION);

        String password = new String(pwdFld.getPassword());

        return new Object[] { result, password };
    }

    public void toggleLock(boolean isSelected) {
        terminalCardView.setLocked(!isSelected);
        btnSend.setEnabled(isSelected);
        boxCommand.setEnabled(isSelected);
        consoleListener.getTerminal().enableKeyHandler(isSelected);

        if (!isSelected) {
            btnLock.setText(STLConstants.K1052_UNLOCK.getValue());
        } else {
            btnLock.setText(STLConstants.K1051_LOCK.getValue());
        }

        btnLock.setSelected(isSelected);
    }
}
