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
 *  File Name: LoginDialogView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.14.2.1  2015/05/06 19:41:24  jijunwan
 *  Archive Log:    L&F adjustment on Login Dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/05/01 21:30:38  jijunwan
 *  Archive Log:    1) changed LoginDialogView to DOCUMENT_MODAL
 *  Archive Log:    2) moved title border, and added title for the dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/04/29 05:41:49  fisherma
 *  Archive Log:    Fixing incorrect user name being shown in the Login Dialog on "Unlock" button click.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/04/09 21:14:32  rjtierne
 *  Archive Log:    Added key listener to password field to the user can hit <Enter> to launch a console
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/04/02 13:33:06  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/04/01 13:20:30  jijunwan
 *  Archive Log:    added null check
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/10/30 17:01:01  rjtierne
 *  Archive Log:    Clear the status text area when the Ok button is pressed
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/10/28 22:18:41  rjtierne
 *  Archive Log:    Put hideDialog() on the EDT to correct synch problem with setVisible()
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/24 14:37:10  rjtierne
 *  Archive Log:    Added new conditions to existing logic to accommodate dynamic dialog usage. The
 *  Archive Log:    login dialog is now displayed at console initialization and console unlock.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/07 19:54:17  rjtierne
 *  Archive Log:    Changed constructor input parameter "owner" type from Window to IFabricView
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/01 19:58:09  rjtierne
 *  Archive Log:    Added thread to initialize console. Added UI enhancement to snap mouse to OK button on dialog.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/23 21:18:22  rjtierne
 *  Archive Log:    Removed unecessary line of code
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/23 19:47:01  rjtierne
 *  Archive Log:    Integration of Gritty for Java Console
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/09 20:03:27  rjtierne
 *  Archive Log:    Added default login bean to console dialog to reduce typing
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/09 16:45:52  rjtierne
 *  Archive Log:    Added combo boxes for username and hostname
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/09 14:18:40  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Custom dialog view for logging into a remote host
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.console.view;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.IntelComboBoxUI;
import com.intel.stl.ui.console.IConsoleEventListener;
import com.intel.stl.ui.console.IConsoleLogin;
import com.intel.stl.ui.console.ITabListener;
import com.intel.stl.ui.console.LoginBean;
import com.intel.stl.ui.main.view.IFabricView;

public class LoginDialogView extends JDialog implements IConsoleLogin {

    private static final long serialVersionUID = -8589239292130514515L;

    private JComboBox<String> cboxUserName;

    private JTextField txtFldPassword;

    private JComboBox<String> cboxHostName;

    private JTextField txtFldPortNum;

    private JProgressBar progressBar;

    private JTextArea txtAreaStatus;

    private JPanel optionPanel;

    private JButton btnOk;

    private JButton btnCancel;

    private final IFabricView mainFrame;

    private DocumentListener setDirtyListener;

    private IConsoleEventListener consoleEventListener;

    private ITabListener tabListener;

    private LoginBean loginBean;

    private final Point dialogLocation = new Point(0, 0);

    private final JDialog dialog = this;

    private boolean newConsole;

    private int consoleId = 0;

    public LoginDialogView(IFabricView owner) {
        super((Window) owner, STLConstants.K1050_CONSOLE_LOGIN.getValue(),
                ModalityType.DOCUMENT_MODAL);

        mainFrame = owner;
        createDocumentListener();
        initComponents();
    }

    protected void initComponents() {

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2));
        mainPanel.setBackground(UIConstants.INTEL_WHITE);
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setOpaque(true);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(1, 5, 2, 5);
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.weightx = 1;

        // Main Panel Content
        // Add user name field
        mainPanel
                .add(ComponentFactory.getH5Label(
                        STLConstants.K0602_USER_NAME.getValue(), Font.BOLD), gc);
        cboxUserName = new JComboBox<String>();
        cboxUserName.setUI(new IntelComboBoxUI());
        cboxUserName.setEditable(true);
        cboxUserName.setPreferredSize(new Dimension(300, 20));
        JTextComponent tc1 =
                (JTextComponent) cboxUserName.getEditor().getEditorComponent();
        tc1.getDocument().addDocumentListener(setDirtyListener);
        mainPanel.add(cboxUserName, gc);

        // Add password field
        mainPanel.add(
                ComponentFactory.getH5Label(
                        STLConstants.K1049_PASSWORD.getValue(), Font.BOLD), gc);
        txtFldPassword = new JPasswordField();
        txtFldPassword.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    okButtonAction();
                }
            }
        });
        txtFldPassword.getDocument().addDocumentListener(setDirtyListener);
        mainPanel.add(txtFldPassword, gc);

        // Add host name field
        mainPanel.add(ComponentFactory.getH5Label(
                STLConstants.K0051_HOST.getValue(), Font.BOLD), gc);
        cboxHostName = new JComboBox<String>();
        cboxHostName.setUI(new IntelComboBoxUI());
        cboxHostName.setEditable(true);
        JTextComponent tc2 =
                (JTextComponent) cboxHostName.getEditor().getEditorComponent();
        tc2.getDocument().addDocumentListener(setDirtyListener);
        mainPanel.add(cboxHostName, gc);

        // Add port number field
        mainPanel.add(
                ComponentFactory.getH5Label(
                        STLConstants.K0404_PORT_NUMBER.getValue(), Font.BOLD),
                gc);
        txtFldPortNum = new JTextField();
        txtFldPortNum.getDocument().addDocumentListener(setDirtyListener);
        mainPanel.add(txtFldPortNum, gc);
        add(mainPanel, BorderLayout.NORTH);

        // Add the status panel
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setOpaque(true);
        statusPanel.setBackground(UIConstants.INTEL_WHITE);
        add(statusPanel, BorderLayout.CENTER);

        // Add a progress bar to the status panel
        progressBar = new JProgressBar(0, 100);
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(true);
        progressBar.setBackground(UIConstants.INTEL_WHITE);
        statusPanel.add(progressBar, BorderLayout.NORTH);

        // Add a status label to the status panel
        txtAreaStatus = new JTextArea();
        txtAreaStatus.setOpaque(true);
        txtAreaStatus.setEditable(false);
        txtAreaStatus.setLineWrap(true);
        txtAreaStatus.setWrapStyleWord(false);
        txtAreaStatus.setFont(UIConstants.H5_FONT);
        txtAreaStatus.setBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED));
        txtAreaStatus.setBackground(UIConstants.INTEL_WHITE);
        txtAreaStatus.setPreferredSize(new Dimension(1, 50));
        statusPanel.add(txtAreaStatus, BorderLayout.SOUTH);

        // Option Panel Content (OK, Cancel)
        optionPanel = new JPanel();
        optionPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnOk =
                ComponentFactory.getIntelActionButton(STLConstants.K0645_OK
                        .getValue());
        btnOk.setEnabled(false);
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okButtonAction();
            }
        });
        optionPanel.add(btnOk);

        btnCancel =
                ComponentFactory.getIntelCancelButton(STLConstants.K0621_CANCEL
                        .getValue());
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (newConsole) {
                    tabListener.closeConsole(tabListener.getCurrentTabView());
                }
                killProgress();
                hideDialog();
                setVisible(false);
            }
        });
        optionPanel.add(btnCancel);

        optionPanel.setBackground(UIConstants.INTEL_WHITE);
        add(optionPanel, BorderLayout.SOUTH);

        setAlwaysOnTop(true);
        setResizable(false);
        setBackground(UIConstants.INTEL_WHITE);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        pack();

    }

    public void createDocumentListener() {

        setDirtyListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                setDirty();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setDirty();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setDirty();
            }
        };
    }

    protected void okButtonAction() {

        // Clear the status info
        txtAreaStatus.setText("");

        LoginBean loginBean = new LoginBean();
        loginBean.setUserName(getUserName());
        loginBean.setPassword(getPassword());
        loginBean.setHostName(getHostName());
        loginBean.setPortNum(getPortNum());

        startProgress();

        if (newConsole) {
            consoleEventListener.initializeConsoleThread(consoleId, loginBean,
                    null);
            addCBoxItem(getUserName(), cboxUserName);
            addCBoxItem(getHostName(), cboxHostName);
        } else {
            if (consoleId > 0) {
                consoleEventListener.onUnlockThread(consoleId, getPassword());
            }
        }
    }

    public void addCBoxItem(String item, JComboBox<String> cbox) {
        if (item == null) {
            return;
        }

        for (int i = 0; i < cbox.getItemCount(); i++) {
            String history = cbox.getItemAt(i);
            if (item.equals(history)) {
                cbox.setSelectedIndex(i);
                return;
            }
        }
        cbox.addItem(item);
    }

    public void setConsoleEventListener(
            IConsoleEventListener consoleEventListener) {
        this.consoleEventListener = consoleEventListener;
    }

    public void setTabListener(ITabListener tabListener) {
        this.tabListener = tabListener;
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

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.IConsoleLogin#hideDialog()
     */
    @Override
    public void hideDialog() {

        Runnable hideIt = new Runnable() {

            @Override
            public void run() {
                txtFldPassword.setText("");

                if (isVisible()) {
                    setVisible(false);
                }
            }
        };
        Util.runInEDT(hideIt);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.IConsoleLogin#showMessage(java.lang.String)
     */
    @Override
    public void showMessage(String message) {
        txtAreaStatus.setText(message);
    }

    private void centerDialog(JDialog dialog) {

        dialogLocation.x =
                mainFrame.getScreenPosition().x
                        + mainFrame.getScreenSize().width / 2
                        - dialog.getWidth() / 2;

        dialogLocation.y =
                mainFrame.getScreenPosition().y
                        + mainFrame.getScreenSize().height / 2
                        - dialog.getHeight() / 2;

        dialog.setLocation(dialogLocation);
    } // centerDialog

    /**
     * @return the user name
     */
    public String getUserName() {
        return String.valueOf(cboxUserName.getSelectedItem());
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return txtFldPassword.getText();
    }

    /**
     * @return the host name
     */
    public String getHostName() {
        return String.valueOf(cboxHostName.getSelectedItem());
    }

    /**
     * @return the port number
     */
    public String getPortNum() {
        return txtFldPortNum.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.IConsoleLogin#startProgress()
     */
    @Override
    public void startProgress() {

        Runnable startIt = new Runnable() {
            @Override
            public void run() {
                if (progressBar != null) {
                    progressBar.setIndeterminate(true);
                    progressBar.setString(STLConstants.K0606_CONNECTING
                            .getValue());
                    progressBar.setStringPainted(true);
                    progressBar.setBorderPainted(true);
                }
            }
        };
        Util.runInEDT(startIt);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.IConsoleLogin#killProgress()
     */
    @Override
    public void killProgress() {

        Runnable killIt = new Runnable() {

            @Override
            public void run() {
                if (progressBar != null) {
                    progressBar.setIndeterminate(false);
                    progressBar.setBorderPainted(false);
                    progressBar.setString("");
                    progressBar.setValue(100);
                }
            }
        };
        Util.runInEDT(killIt);
    }

    protected void setDirty() {

        killProgress();
        progressBar.setValue(0);

        if (txtAreaStatus != null) {
            showMessage(null);
        }

        if ((String.valueOf(cboxUserName.getEditor().getItem()).length() > 0)
                && (getPassword().length() > 0)
                && (String.valueOf(cboxHostName.getEditor().getItem()).length() > 0)
                && (getPortNum().length() > 0)) {
            btnOk.setEnabled(true);
        } else {
            btnOk.setEnabled(false);
        }

    }

    protected void showDialog(final LoginBean loginBean) {

        if (loginBean != null) {
            setLoginBean(loginBean);

            // Populate the fields with the default login information
            addCBoxItem(loginBean.getUserName(), cboxUserName);
            addCBoxItem(loginBean.getHostName(), cboxHostName);
            txtFldPortNum.setText(loginBean.getPortNum());
        }

        // Enable the OK button
        if (String.valueOf(cboxUserName.getEditor().getItem()).length() == 0) {
            cboxUserName.requestFocus();
        } else if (txtFldPassword.getText().length() == 0) {
            txtFldPassword.requestFocus();
        } else if (String.valueOf(cboxHostName.getEditor().getItem()).length() == 0) {
            cboxHostName.requestFocus();
        } else if (txtFldPortNum.getText().length() == 0) {
            txtFldPortNum.requestFocus();
        }

        centerDialog(dialog);

        try {
            Robot robot = new Robot();
            int x =
                    dialogLocation.x + optionPanel.getX() + btnOk.getX()
                            + btnOk.getWidth() / 2;
            int y =
                    dialogLocation.y + optionPanel.getY() + btnOk.getY()
                            + btnOk.getHeight();

            robot.mouseMove(x, y);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.console.IConsoleLogin#showDialog(com.intel.stl.ui.console
     * .LoginBean, boolean, int)
     */
    @Override
    public void showDialog(LoginBean loginBean, boolean newConsole,
            int consoleId) {

        this.newConsole = newConsole;
        this.consoleId = consoleId;

        // Set only password field enabled if this is an existing
        // console that was locked
        if (newConsole) {
            cboxUserName.setEnabled(true);
            txtFldPassword.setEnabled(true);
            cboxHostName.setEnabled(true);
            txtFldPortNum.setEnabled(true);
        } else {
            cboxUserName.setEnabled(false);
            txtFldPassword.setEnabled(true);
            cboxHostName.setEnabled(false);
            txtFldPortNum.setEnabled(false);
        }

        showDialog(loginBean);

        this.consoleId = 0;
    }
}
