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
 *  File Name: AdminView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/08/17 18:53:52  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/08/17 17:46:44  jijunwan
 *  Archive Log:    PR 128973 - Deploy FM conf changes on all SMs
 *  Archive Log:    - improved AdminPage to support adding separator between tabs
 *  Archive Log:    - improved to use canExit to decide weather is able to switch to another tab or page
 *  Archive Log:
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

    public void addSeperator(int size) {
        navPanel.addSeparator(size);
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
