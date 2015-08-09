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
 *  File Name: HostAdderPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/30 15:12:48  rjtierne
 *  Archive Log:    Updated panel backgrounds to use static variable
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/20 21:07:59  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.view.subnet;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.wizards.view.MultinetWizardView;

public class HostAdderPanel extends JPanel {

    private static final long serialVersionUID = 2622454818918629829L;

    private static HostAdderPanel instance;

    private final IHostInfoListener hostInfoListener;

    private HostAdderPanel(IHostInfoListener hostInfoListener) {
        this.hostInfoListener = hostInfoListener;
        initComponents();
    }

    public static HostAdderPanel getInstance(IHostInfoListener hostInfoListener) {

        if (instance == null) {
            instance = new HostAdderPanel(hostInfoListener);
        }

        return instance;
    }

    protected void initComponents() {

        setLayout(new BorderLayout());
        setBackground(MultinetWizardView.WIZARD_COLOR);

        JLabel lblAddHost =
                ComponentFactory.getH4Label(
                        STLConstants.K3036_ADD_NEW_HOST.getValue(), Font.BOLD);
        lblAddHost.setForeground(UIConstants.INTEL_BLUE);
        lblAddHost.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        lblAddHost.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hostInfoListener.addHost();
            }
        });

        lblAddHost.setOpaque(true);
        lblAddHost.setHorizontalAlignment(JLabel.LEFT);
        lblAddHost.setBackground(UIConstants.INTEL_BORDER_GRAY);
        add(lblAddHost, BorderLayout.WEST);
    }
}
