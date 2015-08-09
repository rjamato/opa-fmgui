/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: OptionDialog.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/11 21:16:02  jijunwan
 *  Archive Log:    added remove and deploy features
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/10 22:45:33  jijunwan
 *  Archive Log:    improved to do and show validation before we save an application
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.intel.stl.api.IAssistant;
import com.intel.stl.ui.common.Util;

public abstract class OptionDialog extends JDialog implements IAssistant {
    private static final long serialVersionUID = 4480551678945585493L;

    private final Object waitObj;

    protected JProgressBar progressBar;

    protected JPanel ctrPanel;

    // -------------- Option Buttons ---------------//
    private JButton okButton;

    private JButton cancelButton;

    protected int option;

    /**
     * 
     * Description:
     * 
     * @param owner
     * @param title
     */
    public OptionDialog(Component owner, String title, int optionType) {
        super(SwingUtilities.getWindowAncestor(owner), title);
        initComponent(optionType);
        waitObj = new Object();
    }

    protected void initComponent(int optionType) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        JComponent mainPanel = getMainComponent();
        panel.add(mainPanel, BorderLayout.CENTER);
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(true);
        // progressBar.setVisible(false);
        panel.add(progressBar, BorderLayout.SOUTH);
        getContentPane().add(panel, BorderLayout.CENTER);

        ctrPanel = new JPanel();
        intsallButtons(ctrPanel, optionType);
        getContentPane().add(ctrPanel, BorderLayout.SOUTH);

        setSize();
        setLocationRelativeTo(getOwner());
    }

    /**
     * <i>Description:</i>
     * 
     */
    protected void setSize() {
        pack();
    }

    /**
     * <i>Description:</i>
     * 
     * @param ctrPanel2
     */
    protected void intsallButtons(JPanel ctrPanel, int optionType) {
        ctrPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        if (optionType == JOptionPane.OK_OPTION
                || optionType == JOptionPane.OK_CANCEL_OPTION) {
            okButton = new JButton("Ok");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onOk();
                    option = JOptionPane.OK_OPTION;
                    synchronized (waitObj) {
                        waitObj.notify();
                    }
                }
            });
            ctrPanel.add(okButton);
        }

        if (optionType == JOptionPane.CANCEL_OPTION
                || optionType == JOptionPane.OK_CANCEL_OPTION) {
            cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onCancel();
                    option = JOptionPane.CANCEL_OPTION;
                    synchronized (waitObj) {
                        waitObj.notify();
                    }
                }
            });
            ctrPanel.add(cancelButton);
        }
    }

    /**
     * <i>Description:</i>
     * 
     */
    public void onCancel() {
    }

    /**
     * <i>Description:</i>
     * 
     */
    public void onOk() {
    }

    public void enableOk(boolean b) {
        okButton.setEnabled(b);
    }

    public void enableCancel(boolean b) {
        cancelButton.setEnabled(b);
    }

    /**
     * <i>Description:</i>
     * 
     * @return
     */
    protected abstract JComponent getMainComponent();

    public void showDialog() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.IAssistant#getOption(java.lang.Throwable)
     */
    @Override
    public int getOption(Throwable error) {
        if (SwingUtilities.isEventDispatchThread()) {
            throw new RuntimeException("Can not call from EDT!");
        }
        option = -1;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // progressBar.setVisible(false);
                progressBar.setString(null);
                setVisible(true);
            }
        });

        waitForInput();
        return option;
    }

    protected void waitForInput() {
        while (option == -1) {
            try {
                synchronized (waitObj) {
                    waitObj.wait();
                }
            } catch (InterruptedException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.ILoginAssistant#startProgress()
     */
    @Override
    public void startProgress() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisible(true);
                progressBar.setIndeterminate(true);
                repaint();
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
                progressBar.setValue(100);
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
                onClose();
                setVisible(false);
            }
        });
    }

    /**
     * 
     * <i>Description:</i> any work we intend to do before we close this dialog
     * 
     */
    protected abstract void onClose();
}
