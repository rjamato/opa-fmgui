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
 *  File Name: PerformanceView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/02/24 14:23:23  jypak
 *  Archive Log:    1. Show Border, Alternating Rows control panel added to the PerformanceErrorsSection.
 *  Archive Log:    2. Undo change of Performance Chart Section title to "Performancefor port Performance subpage.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/05/28 22:21:57  jijunwan
 *  Archive Log:    added port preview to performance subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/21 14:49:59  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: This class is the Performance "Node" and "Port" view.  The two
 *  views of this type are swapped on a CardLayout panel in the PerformanceSubpageView,
 *  depending on whether a node or port is selected.
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
import javax.swing.JSplitPane;

import com.intel.stl.ui.common.view.JSectionView;

public class PerformanceView extends JPanel {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2465696391555897980L;

    private JPanel mainPanel;

    private JSplitPane splitPane;

    public PerformanceView() {
        super();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0.2);
        splitPane.setDividerSize(4);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(1, 5, 2, 5);
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.weightx = 1;
        gc.weighty = 1;

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        mainPanel.add(splitPane, gc);

        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * @param sectionViews
     */
    public void installSectionViews(List<JSectionView<?>> sectionViews) {

        splitPane.setTopComponent(sectionViews.get(0));
        splitPane.setBottomComponent(sectionViews.get(1));
    }

    public JComponent getMainComponent() {
        return this;
    }
}
