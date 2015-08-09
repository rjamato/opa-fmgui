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
 *  Functional Group: FabricViewer
 *
 *  File Name: NodeStatesView.java
 *
 *  Archive Source: 
 *
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.jfree.data.general.PieDataset;

import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.JCardView;
import com.intel.stl.ui.model.ChartStyle;

/**
 * @author jijunwan
 * 
 */
public class NodeStatesView extends JCardView<IChartStyleListener> {
    private static final long serialVersionUID = -8330957415988551326L;

    private JPanel mainPanel;

    private CardLayout layout;

    private NodeStatesBar barPanel;

    private NodeStatesPie piePanel;

    private JButton styleBtn;

    private ChartStyle style = ChartStyle.PIE;

    private ImageIcon barIcon, pieIcon;

    /**
     * @param title
     * @param controller
     */
    public NodeStatesView(String title) {
        super(title);
        // this is unnecessary, but can stop klocwork from complaining
        getMainComponent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.common.JCard#getMainPanel()
     */
    @Override
    protected JPanel getMainComponent() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            layout = new CardLayout();
            mainPanel.setLayout(layout);

            barPanel = new NodeStatesBar();
            barPanel.setOpaque(false);
            mainPanel.add(barPanel, ChartStyle.BAR.name());

            piePanel = new NodeStatesPie();
            piePanel.setOpaque(false);
            mainPanel.add(piePanel, ChartStyle.PIE.name());

            layout.show(mainPanel, ChartStyle.PIE.name());
        }
        return mainPanel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.hpc.stl.ui.common.JCard#addControlButtons(javax.swing.JToolBar)
     */
    @Override
    protected void addControlButtons(JToolBar toolBar) {
        styleBtn =
                ComponentFactory.getImageButton(UIImages.BAR_ICON
                        .getImageIcon());
        styleBtn.setToolTipText(UILabels.STL40002_TO_BAR.getDescription());
        toolBar.add(styleBtn);

        super.addControlButtons(toolBar);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.view.JCardView#setCardListener(com.intel.stl.
     * ui.common.view.ICardListener)
     */
    @Override
    public void setCardListener(final IChartStyleListener listener) {
        super.setCardListener(listener);
        styleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onStyleChange(style);
            }
        });
    }

    /**
     * Description:
     * 
     * @param dataset
     * @param colors
     */
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

    public void setStyle(ChartStyle style) {
        this.style = style;
        layout.show(mainPanel, style.name());
        if (style == ChartStyle.BAR) {
            if (pieIcon == null) {
                pieIcon = UIImages.PIE_ICON.getImageIcon();
            }
            styleBtn.setIcon(pieIcon);
            styleBtn.setToolTipText(UILabels.STL40003_TO_PIE.getDescription());
        } else if (style == ChartStyle.PIE) {
            if (barIcon == null) {
                barIcon = UIImages.BAR_ICON.getImageIcon();
            }
            styleBtn.setIcon(barIcon);
            styleBtn.setToolTipText(UILabels.STL40002_TO_BAR.getDescription());
        }
    }

    public void clear() {
        barPanel.clear();
        piePanel.clear();
    }
}
