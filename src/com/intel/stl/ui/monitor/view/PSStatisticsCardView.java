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
 *  File Name: PSStatisticsCardView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2014/10/22 16:40:14  jijunwan
 *  Archive Log:    separated other ports viz for the ports not in a subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/15 22:00:23  jijunwan
 *  Archive Log:    display other ports on UI
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/21 13:48:15  jijunwan
 *  Archive Log:    added # internal, external ports on performance page
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/09 19:49:03  rjtierne
 *  Archive Log:    Renamed getEventsPanel() to getPortsPanel()
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/09 19:27:45  rjtierne
 *  Archive Log:    Renamed from PerfSummaryStatisticsView and completely
 *  Archive Log:    changed after MVC Refactoring
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/08 21:11:03  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: View for the statistics card on the Performance Summary subpage
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.common.view.JCardView;
import com.intel.stl.ui.model.FlowTypeViz;
import com.intel.stl.ui.model.NodeTypeViz;

public class PSStatisticsCardView extends JCardView<ICardListener> {
    private static final long serialVersionUID = -5447526254155197323L;

    private JPanel mainPanel;

    private PSNodesDetailsPanel nodesPanel;

    private PSPortsDetailsPanel portsPanel;

    /**
     * @param title
     * @param controller
     */
    public PSStatisticsCardView(String title) {
        super(title);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.common.JCard#getMainPanel()
     */
    @Override
    protected JPanel getMainComponent() {
        if (mainPanel != null) {
            return mainPanel;
        }

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 5));

        JPanel body = new JPanel(new GridLayout(1, 2, 15, 1));
        body.setOpaque(false);

        nodesPanel = new PSNodesDetailsPanel();
        body.add(nodesPanel);

        NodeTypeViz[] nodeTypes =
                new NodeTypeViz[] { NodeTypeViz.SWITCH, NodeTypeViz.HFI };
        FlowTypeViz[] flowTypes =
                new FlowTypeViz[] { FlowTypeViz.INTERNAL, FlowTypeViz.EXTERNAL };
        portsPanel = new PSPortsDetailsPanel(nodeTypes, flowTypes);
        body.add(portsPanel);

        mainPanel.add(body, BorderLayout.CENTER);

        return mainPanel;
    }

    protected JLabel createNumberLabel(String text) {
        JLabel label = ComponentFactory.getH2Label(text, Font.BOLD);
        label.setHorizontalAlignment(JLabel.LEFT);
        return label;
    }

    protected JLabel createNameLabel(String text) {
        JLabel label = ComponentFactory.getH5Label(text, Font.PLAIN);
        label.setHorizontalAlignment(JLabel.LEFT);
        return label;
    }

    public PSNodesDetailsPanel getNodesPanel() {
        return nodesPanel;
    }

    public PSPortsDetailsPanel getPortsPanel() {
        return portsPanel;
    }

}
