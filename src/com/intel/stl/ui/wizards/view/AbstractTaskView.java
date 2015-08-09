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
 *  File Name: BaseWizardPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/04/06 22:53:49  jijunwan
 *  Archive Log:    first round wizard polishment
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/30 15:12:51  rjtierne
 *  Archive Log:    Updated panel backgrounds to use static variable
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/11 15:25:12  rjtierne
 *  Archive Log:    Multinet Wizard: Removed holder panel since the status panel is no longer being used.
 *  Archive Log:    Added method logMessage() to replace showMessage() on the status panel.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/11 21:48:17  jijunwan
 *  Archive Log:    setup wizard improvements
 *  Archive Log:    1) look and feel adjustment
 *  Archive Log:    2) secure FE support
 *  Archive Log:    3) apply wizard on current subnet
 *  Archive Log:    4) message display based on message type rather than directly specifying UI resources
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.view;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.ui.wizards.impl.IWizardTask;
import com.intel.stl.ui.wizards.impl.InteractionType;

public abstract class AbstractTaskView extends JPanel implements ITaskView {

    private static final long serialVersionUID = 3149874617834752585L;

    private static Logger log = LoggerFactory.getLogger(AbstractTaskView.class);

    private StatusPanel statusPanel;

    public AbstractTaskView() {
    }

    /**
     * Description:
     * 
     * @param title
     */
    public AbstractTaskView(String title) {
        super();
        initComponents(title);
        resetPanel();
    }

    protected void initComponents(String title) {
        setOpaque(true);
        setBackground(MultinetWizardView.WIZARD_COLOR);
        setLayout(new BorderLayout());

        // Add the status panel at the top of the view
        statusPanel = new StatusPanel();
        statusPanel.getContentPane().setBackground(
                MultinetWizardView.WIZARD_COLOR);
        JComponent optionComp = getOptionComponent();
        add(optionComp, BorderLayout.CENTER);
    }

    protected abstract JComponent getOptionComponent();

    public void setWizardListener(IWizardTask listener) {
        statusPanel.setWizardTaskController(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.ITaskView#openStatusPanel()
     */
    @Override
    public void openStatusPanel() {
        statusPanel.openStatusPanel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.ITaskView#closeStatusPanel()
     */
    @Override
    public void closeStatusPanel() {
        statusPanel.closeStatusPanel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.ITaskView#toggleStatusPanel()
     */
    @Override
    public void toggleStatusPanel() {
        statusPanel.toggleStatusPanel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.ITaskView#showMessage(java.lang.String,
     * java.lang.String, com.intel.stl.ui.wizards.impl.InteractionType, int,
     * Object)
     */
    @Override
    public void showMessage(String message, InteractionType action,
            int messageType, Object... data) {
        statusPanel.showMessage(message, action, messageType, data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.ITaskView#showMessage(java.lang.String,
     * java.lang.String, com.intel.stl.ui.wizards.impl.InteractionType, int,
     * Object)
     */
    @Override
    public void logMessage(String message) {
        log.error(message);
    }
}
