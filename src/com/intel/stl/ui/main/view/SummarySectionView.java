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
package com.intel.stl.ui.main.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.common.view.JSectionView;

/**
 * @author jijunwan
 * 
 */
public class SummarySectionView extends JSectionView<ISectionListener> {
    private static final long serialVersionUID = 7004235726509918990L;

    private JPanel mainPanel;
    private StatisticsView statisticsView;
    
    private JPanel statesPanel;
    private StatusView statusView;
    private HealthHistoryView healthView;
    private WorstNodesView worstNodesView;

    public SummarySectionView() {
        super(STLConstants.K0102_HOME_SUMMARY.getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.common.JSection#getMainPanel()
     */
    protected JPanel getMainComponent() {
        if (mainPanel==null) {
            mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 3, 5));
            
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(0, 0, 0, 5);
            gc.weightx = 1;
            gc.weighty = 1;
            gc.gridwidth = 1;
            gc.fill = GridBagConstraints.BOTH;
            
            statisticsView =new StatisticsView();
            mainPanel.add(statisticsView, gc);

            gc.insets = new Insets(0, 0, 0, 0);
            gc.gridwidth = GridBagConstraints.REMAINDER;
            statesPanel = getStatesPanel();
            mainPanel.add(statesPanel, gc);
        }
        return mainPanel;
    }

    protected JPanel getStatesPanel() {
        if (statesPanel == null) {
            statesPanel = new JPanel(new GridLayout(1, 2, 8, 2));

            statusView = new StatusView();
            statusView.setBackground(UIConstants.INTEL_WHITE);
            statesPanel.add(statusView);
            
            JPanel rightPanel = new JPanel(new GridLayout(2, 1, 8, 2));
            healthView = new HealthHistoryView();
            healthView.setBackground(UIConstants.INTEL_WHITE);
            healthView.setPreferredSize(new Dimension(150, 100));
            rightPanel.add(healthView);
            worstNodesView = new WorstNodesView();
            worstNodesView.setBackground(UIConstants.INTEL_WHITE);
            rightPanel.add(worstNodesView);
            statesPanel.add(rightPanel);
        }
        return statesPanel;
    }

    /**
     * @return the statisticsView
     */
    public StatisticsView getStatisticsView() {
        return statisticsView;
    }

    /**
     * @return the swStatesView
     */
    public StatusView getStatusView() {
        return statusView;
    }

    /**
     * @return the healthView
     */
    public HealthHistoryView getHealthView() {
        return healthView;
    }

    /**
     * @return the worstNodesView
     */
    public WorstNodesView getWorstNodesView() {
        return worstNodesView;
    }

}
