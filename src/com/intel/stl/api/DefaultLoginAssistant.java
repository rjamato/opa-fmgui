/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: DefaultLoginAssistant.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.2  2015/08/12 15:21:59  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.5.2.1  2015/05/06 19:32:08  jijunwan
 *  Archive Log:    L&F adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/05/04 19:55:38  jijunwan
 *  Archive Log:    very minor change that sets focus request in another invokeLater call rather than after #setVisible in a single invokeLater call
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/05/01 21:31:36  jijunwan
 *  Archive Log:    L&F adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/04/29 22:02:09  jijunwan
 *  Archive Log:    changed DefaulLoginAssistant to be DOCUMENT_MODAL
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/28 21:55:01  jijunwan
 *  Archive Log:    improved LoginAssistant to support setting owner
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/03 20:56:43  jijunwan
 *  Archive Log:    prevent closing a login dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/10 22:41:41  jijunwan
 *  Archive Log:    improved to show progress while we log into a host
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:30:40  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:    1) read/write opafm.xml from/to host with backup file support
 *  Archive Log:    2) Application parser
 *  Archive Log:    3) Add/remove and update Application
 *  Archive Log:    4) unique name, reference conflication check
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.intel.stl.common.STLMessages;

public class DefaultLoginAssistant extends JDialog implements ILoginAssistant {
    private static final long serialVersionUID = -4070734448078356098L;

    private String userName;

    private String hostName;

    private JPanel loginPanel;

    private JLabel msgLabel;

    private JTextField hostField;

    private JFormattedTextField portField;

    private JTextField userField;

    private JPasswordField passwordField;

    private JProgressBar progressBar;

    private JButton okButton;

    private JButton cancelButton;

    private int option;

    /**
     * Description:
     * 
     * @param subnet
     */
    public DefaultLoginAssistant(Window parent, final String hostName,
            String userName) {
        super(parent, STLMessages.STL61010_LOGIN.getDescription());
        if (parent != null) {
            setModalityType(ModalityType.DOCUMENT_MODAL);
        } else {
            setModalityType(ModalityType.APPLICATION_MODAL);
        }
        initComponent();
        init(hostName, userName);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
    }

    protected void initComponent() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        loginPanel = getLoginPanel();
        panel.add(loginPanel, BorderLayout.NORTH);
        progressBar = new JProgressBar();
        progressBar.setStringPainted(false);
        progressBar.setIndeterminate(false);
        // progressBar.setUI(new BasicProgressBarUI());
        panel.add(progressBar, BorderLayout.CENTER);
        getContentPane().add(panel, BorderLayout.CENTER);

        panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        panel.setBackground(Color.white);
        okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                option = JOptionPane.OK_OPTION;
                synchronized (hostName) {
                    hostName.notify();
                }
            }
        });
        panel.add(okButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                option = JOptionPane.CANCEL_OPTION;
                passwordField.setText(null);
                synchronized (hostName) {
                    hostName.notify();
                }
            }
        });
        panel.add(cancelButton);
        getContentPane().add(panel, BorderLayout.SOUTH);

        setSize(250, 200);
        setLocationRelativeTo(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.ILoginAssistant#init(com.intel.stl.api.subnet.
     * SubnetDescription)
     */
    @Override
    public void init(String hostName, String userName) {
        this.hostName = hostName;
        this.userName = userName;
        hostField.setText(hostName);
        userField.setText(userName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.ILoginAssistant#getPort()
     */
    @Override
    public int getPort() {
        Integer val = (Integer) portField.getValue();
        return val.intValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.ILoginAssistant#getUserName()
     */
    @Override
    public String getUserName() {
        return userName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.ILoginAssistant#getPassword()
     */
    @Override
    public char[] getPassword() {
        char[] res = passwordField.getPassword();
        passwordField.setText(null);
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.ILoginAssistant#promptLogIn()
     */
    @Override
    public int getOption(Throwable error) {
        option = -1;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setStringPainted(false);
                progressBar.setIndeterminate(false);
                progressBar.setString(null);
                passwordField.setText(null);
                setLocationRelativeTo(getParent());
                setVisible(true);
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                passwordField.requestFocus();
            }
        });

        waitForInput();
        if (option == JOptionPane.OK_OPTION) {
            userName = userField.getText();
        }
        return option;
    }

    protected void waitForInput() {
        synchronized (hostName) {
            while (option == -1) {
                try {
                    hostName.wait();
                } catch (InterruptedException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected JPanel getLoginPanel() {
        if (loginPanel == null) {
            loginPanel = new JPanel(new GridBagLayout());
            loginPanel.setOpaque(false);
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(2, 2, 2, 2);
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.weightx = 1;
            gc.gridwidth = GridBagConstraints.REMAINDER;
            msgLabel = new JLabel();
            loginPanel.add(msgLabel, gc);

            gc.gridwidth = 1;
            gc.weightx = 0;
            JLabel label =
                    new JLabel(STLMessages.STL61011_HOST.getDescription());
            loginPanel.add(label, gc);

            gc.gridwidth = GridBagConstraints.REMAINDER;
            gc.weightx = 1;
            hostField = new JTextField(hostName);
            hostField.setEnabled(false);
            loginPanel.add(hostField, gc);

            gc.gridwidth = 1;
            gc.weightx = 0;
            label = new JLabel(STLMessages.STL61014_PORT.getDescription());
            loginPanel.add(label, gc);

            gc.gridwidth = GridBagConstraints.REMAINDER;
            gc.weightx = 1;
            portField = new JFormattedTextField(new Integer(22));
            loginPanel.add(portField, gc);

            gc.gridwidth = 1;
            gc.weightx = 0;
            label = new JLabel(STLMessages.STL61012_USER_NAME.getDescription());
            loginPanel.add(label, gc);

            gc.gridwidth = GridBagConstraints.REMAINDER;
            gc.weightx = 1;
            userField = new JTextField(userName);
            loginPanel.add(userField, gc);

            gc.gridwidth = 1;
            gc.weightx = 0;
            label = new JLabel(STLMessages.STL61013_PASSWORD.getDescription());
            loginPanel.add(label, gc);

            gc.gridwidth = GridBagConstraints.REMAINDER;
            gc.weightx = 1;
            passwordField = new JPasswordField();
            passwordField.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    option = JOptionPane.OK_OPTION;
                    synchronized (hostName) {
                        hostName.notify();
                    }
                }

            });
            loginPanel.add(passwordField, gc);
        }
        return loginPanel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.ILoginAssistant#startProgress()
     */
    @Override
    public void startProgress() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // progressBar.setVisible(true);
                progressBar.setStringPainted(true);
                progressBar.setIndeterminate(true);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.ILoginAssistant#stopProgress()
     */
    @Override
    public void stopProgress() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setIndeterminate(false);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.ILoginAssistant#reportProgress(java.lang.String)
     */
    @Override
    public void reportProgress(final String note) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setString(note);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.ILoginAssistant#close()
     */
    @Override
    public void close() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                passwordField.setText(null);
                setVisible(false);
            }
        });
    }

}
