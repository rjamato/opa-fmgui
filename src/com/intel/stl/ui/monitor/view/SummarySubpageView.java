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
 *  File Name: SummarySubpageView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/08/17 18:54:24  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/19 15:19:59  rjtierne
 *  Archive Log:    Removed txtLabel
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/02 21:54:53  rjtierne
 *  Archive Log:    Initialized main panel and scrollpane
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/01 16:14:58  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.intel.stl.ui.common.view.JSectionView;

public class SummarySubpageView extends JPanel {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2465696391555897980L;
    
    private JPanel mainPanel;
    
    private JScrollPane scrollPane;
    
    
    public SummarySubpageView() {
        super();
        initComponents();
    }
    
    
    private void initComponents() {
        
        setLayout(new BorderLayout());
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    
    /**
     * @param sectionViews
     */
    public void installSectionViews(List<JSectionView<?>> sectionViews) {
        GridBagLayout gridBag = new GridBagLayout();
        mainPanel.setLayout(gridBag);
        GridBagConstraints gc = new GridBagConstraints();

        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(1, 5, 2, 5);
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.weightx = 1;
        gc.weighty = 1;
        for (JSectionView<?> section : sectionViews) {
            mainPanel.add(section, gc);
        }
    }
    
    
    
    public JComponent getMainComponent() {
        return this;
    }    
}
