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
 *  File Name: AdminView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/03/05 17:38:18  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/11/17 17:12:42  jijunwan
 *  Archive Log:    added Log to admin page
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/24 13:12:51  rjtierne
 *  Archive Log:    Setting views is already on EDT through mouse click event
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/07 21:50:03  jijunwan
 *  Archive Log:    L&F adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/07 19:53:07  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.admin.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import com.intel.stl.ui.main.view.IFabricView;

public class AdminView extends JPanel {

    private static final long serialVersionUID = 2544634742975926197L;

    private final IFabricView owner;

    private JPanel viewPanel;

    private NavigationPanel navPanel;

    public AdminView(IFabricView owner) {

        super();
        this.owner = owner;
        initComponents();
    }

    protected void initComponents() {

        setLayout(new BorderLayout());

        navPanel = new NavigationPanel();
        add(navPanel, BorderLayout.WEST);

        viewPanel = new JPanel();
        viewPanel.setLayout(new CardLayout());
        viewPanel.setOpaque(false);
        // viewPanel.setBorder(BorderFactory
        // .createLineBorder(UIConstants.INTEL_BORDER_GRAY));
        add(viewPanel, BorderLayout.CENTER);
    }

    public void addChangeListener(ChangeListener listener) {
        navPanel.addListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        navPanel.removeListener(listener);
    }

    public void addViewCard(Icon icon, Component card, String name) {
        navPanel.addItem(name, icon);
        viewPanel.add(card, name);
    }

    public void setView(String name) {
        navPanel.selectItem(name);

        // Show the desired card
        CardLayout c = (CardLayout) (viewPanel.getLayout());
        c.show(viewPanel, name);
        repaint();
    }

    /**
     * @return the owner
     */
    public IFabricView getOwner() {
        return owner;
    }

}
