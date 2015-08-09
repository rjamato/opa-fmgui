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
 *  File Name: SatusView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/05 19:09:22  jijunwan
 *  Archive Log:    fixed a issue reported by klocwork that is actually not a problem
 *  Archive Log:
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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.jfree.data.general.PieDataset;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.JCardView;
import com.intel.stl.ui.model.ChartStyle;

public class StatusView extends JCardView<IChartStyleListener> {
    private static final long serialVersionUID = 8374679365582635122L;

    private JButton styleBtn;

    private ChartStyle style = ChartStyle.PIE;

    private ImageIcon barIcon, pieIcon;

    private JPanel mainPanel;

    private NodeStatusPanel swPanel;

    private NodeStatusPanel fiPanel;

    /**
     * Description:
     * 
     * @param title
     */
    public StatusView() {
        super(STLConstants.K0062_STATUS.getValue());
        // this is unnecessary, but can stop klocwork from complaining
        getMainComponent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.JCardView#getMainComponent()
     */
    @Override
    protected JComponent getMainComponent() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            BoxLayout layout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
            mainPanel.setLayout(layout);

            JLabel label =
                    ComponentFactory.getH4Label(
                            STLConstants.K0063_SW_STATUS.getValue(), Font.BOLD);
            label.setBorder(BorderFactory.createEmptyBorder(2, 15, 0, 0));
            label.setAlignmentX(0.0f);
            mainPanel.add(label);

            swPanel = new NodeStatusPanel();
            swPanel.setOpaque(false);
            swPanel.setAlignmentX(0.0f);
            mainPanel.add(swPanel);

            label =
                    ComponentFactory.getH4Label(
                            STLConstants.K0064_FI_STATUS.getValue(), Font.BOLD);
            label.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 0));
            label.setAlignmentX(0.0f);
            mainPanel.add(label);

            fiPanel = new NodeStatusPanel();
            fiPanel.setOpaque(false);
            fiPanel.setAlignmentX(0.0f);
            mainPanel.add(fiPanel);
        }
        return mainPanel;
    };

    /**
     * @return the swPanel
     */
    public NodeStatusPanel getSwPanel() {
        return swPanel;
    }

    /**
     * @return the fiPanel
     */
    public NodeStatusPanel getFiPanel() {
        return fiPanel;
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

    public void setStyle(ChartStyle style) {
        this.style = style;
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

    public void setSwDataset(PieDataset dataset, Color[] colors) {
        swPanel.setDataset(dataset, colors);
    }

    public void setFiDataset(PieDataset dataset, Color[] colors) {
        fiPanel.setDataset(dataset, colors);
    }

    public void setSwStates(double[] values, String[] labels, String[] tooltips) {
        swPanel.setStates(values, labels, tooltips);
    }

    public void setFiStates(double[] values, String[] labels, String[] tooltips) {
        fiPanel.setStates(values, labels, tooltips);
    }

    public void clear() {
        swPanel.clear();
        fiPanel.clear();
    }

}
