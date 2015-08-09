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
 *  File Name: TerminalCardView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/04/03 21:06:28  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/23 19:47:01  rjtierne
 *  Archive Log:    Integration of Gritty for Java Console
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/05 21:56:32  jijunwan
 *  Archive Log:    L&F adjustment on Console Views
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 19:53:59  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: JCard view on the ConsoleTerminalView containing the SSH terminal
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.console.view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.common.view.JCardView;
import com.intel.stl.ui.console.ConsoleDispatchManager;

public class TerminalCardView extends JCardView<ICardListener> {
    private static final long serialVersionUID = 4439143011519610809L;

    private JPanel mainPanel;

    private JLabel statusLabel;

    public TerminalCardView(String title) {
        super(title);
    }

    public void setTermPanel(IntelTerminalView panel) {

        panel.updateTermPanelDimensions(panel.getPreferredSize());
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.JCardView#getMainComponent()
     */
    @Override
    protected JComponent getMainComponent() {
        if (mainPanel == null) {
            mainPanel = new JPanel(new BorderLayout());
            mainPanel.setOpaque(false);
        }

        return mainPanel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.JCardView#getExtraComponent()
     */
    @Override
    protected JComponent getExtraComponent() {
        if (statusLabel == null) {
            statusLabel = ComponentFactory.getH4Label(null, Font.BOLD);
            statusLabel.setHorizontalAlignment(JLabel.LEADING);
            statusLabel.setForeground(UIConstants.INTEL_DARK_RED);
        }
        return statusLabel;
    }

    /**
     * <i>Description:</i>
     * 
     * @param isSelected
     */
    public void setLocked(boolean isSelected) {
        if (isSelected) {
            statusLabel.setText("[" + STLConstants.K1054_LOCKED.getValue()
                    + "]");
        } else {
            statusLabel.setText("");
        }
    }

    public void displayMaxConsoles(boolean isMaxConsoles) {

        if (isMaxConsoles) {
            statusLabel.setText(UILabels.STL80004_MAX_CONSOLES
                    .getDescription(ConsoleDispatchManager.MAX_NUM_CONSOLES));
        } else {
            statusLabel.setText("");
        }
    }

}
