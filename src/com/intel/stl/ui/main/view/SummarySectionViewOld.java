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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

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
public class SummarySectionViewOld extends JSectionView<ISectionListener> {
    private static final long serialVersionUID = 7004235726509918990L;

    private JPanel statesPanel;
    private StatisticsViewOld statisticsView;
    private NodeStatesViewOld swStatesPie;
    private NodeStatesViewOld caStatesPie;
    private HealthHistoryView healthHistoryView;
    private WorstNodesView worstNodesView;

    public SummarySectionViewOld() {
        super(STLConstants.K0102_HOME_SUMMARY.getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.common.JSection#getMainPanel()
     */
    protected JPanel getMainComponent() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 5, 2));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        statisticsView = new StatisticsViewOld();
        mainPanel.add(statisticsView);

        statesPanel = getStatesPanel();
        mainPanel.add(statesPanel);

        return mainPanel;
    }

    protected JPanel getStatesPanel() {
        if (statesPanel == null) {
            statesPanel = new JPanel(new BorderLayout(0, 8));

            JPanel topPanel = new JPanel(new GridLayout(1, 2, 8, 2));
            swStatesPie = new NodeStatesViewOld(STLConstants.K0033_SWITCH_STAETES.getValue());
            swStatesPie.setPreferredSize(new Dimension(150, 200));
            swStatesPie.setBackground(UIConstants.INTEL_WHITE);
            topPanel.add(swStatesPie);
            caStatesPie = new NodeStatesViewOld(STLConstants.K0034_HFI_STATES.getValue());
            caStatesPie.setPreferredSize(new Dimension(150, 200));
            caStatesPie.setBackground(UIConstants.INTEL_WHITE);
            topPanel.add(caStatesPie);
            statesPanel.add(topPanel, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 8, 2));
            healthHistoryView = new HealthHistoryView();
            healthHistoryView.setBackground(UIConstants.INTEL_WHITE);
            healthHistoryView.setPreferredSize(new Dimension(150, 100));
            bottomPanel.add(healthHistoryView);
            worstNodesView = new WorstNodesView();
            worstNodesView.setBackground(UIConstants.INTEL_WHITE);
            bottomPanel.add(worstNodesView);
            statesPanel.add(bottomPanel, BorderLayout.SOUTH);
        }
        return statesPanel;
    }

    /**
     * @return the statisticsView
     */
    public StatisticsViewOld getStatisticsView() {
        return statisticsView;
    }

    /**
     * @return the swStatesPie
     */
    public NodeStatesViewOld getSwStatesPie() {
        return swStatesPie;
    }

    /**
     * @return the caStatesPie
     */
    public NodeStatesViewOld getCaStatesPie() {
        return caStatesPie;
    }

    /**
     * @return the healthHistoryView
     */
    public HealthHistoryView getHealthHistoryView() {
        return healthHistoryView;
    }

    /**
     * @return the worstNodesView
     */
    public WorstNodesView getWorstNodesView() {
        return worstNodesView;
    }

}
