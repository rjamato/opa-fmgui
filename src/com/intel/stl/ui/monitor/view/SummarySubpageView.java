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
 *  File Name: SummarySubpageView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
