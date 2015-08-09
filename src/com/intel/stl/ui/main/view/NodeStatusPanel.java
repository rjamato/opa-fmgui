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
 *  File Name: NodeStatusPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/07/09 21:18:59  jijunwan
 *  Archive Log:    improved status visualization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.main.view;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.data.general.PieDataset;

import com.intel.stl.ui.model.ChartStyle;

public class NodeStatusPanel extends JPanel {
    private static final long serialVersionUID = -5896416689826992723L;
    
    private ChartStyle style = ChartStyle.PIE;
    private CardLayout layout;
    private NodeStatesBar barPanel;
    private NodeStatesPie piePanel;
    
    public NodeStatusPanel() {
        super();
        
        layout = new CardLayout();
        setLayout(layout);

        barPanel = new NodeStatesBar();
        barPanel.setOpaque(false);
        add(barPanel, ChartStyle.BAR.name());

        piePanel = new NodeStatesPie();
        piePanel.setOpaque(false);
        add(piePanel, ChartStyle.PIE.name());

        layout.show(this, ChartStyle.PIE.name());
    }
    
    public void setStyle(ChartStyle style) {
        this.style = style;
        layout.show(this, style.name());
    }

    public void setDataset(PieDataset dataset, Color[] colors) {
        piePanel.setDataset(dataset, colors);
    }
    
    public void setStates(double[] values, String[] labels, String[] tooltips) {
        if (style == ChartStyle.PIE) {
            piePanel.setStates(values, labels, tooltips);
        } else if (style == ChartStyle.BAR) {
            barPanel.setStates(values, labels, tooltips);
        }
    }
    
    public void clear() {
        barPanel.clear();
        piePanel.clear();
    }

}
