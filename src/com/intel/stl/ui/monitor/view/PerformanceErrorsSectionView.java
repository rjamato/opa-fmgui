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
package com.intel.stl.ui.monitor.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.configuration.view.IPropertyListener;
import com.intel.stl.ui.main.view.PerfErrorsCardView;

/*******************************************************************************
 * I N T E L C O R P O R A T I O N
 * 
 * Functional Group: Fabric Viewer Application
 * 
 * File Name: PerfErrorsItem.java
 * 
 * Archive Source: $Source:
 * /cvs/vendor/intel/fmgui/client/src/main/java/com/intel
 * /stl/ui/monitor/view/PerformanceErrorsSectionView.java,v $
 * 
 * Archive Log: $Log$
 * Archive Log: Revision 1.7  2015/04/01 21:24:44  jijunwan
 * Archive Log: adjustment on layout
 * Archive Log: Archive Log:
 * Revision 1.6 2015/02/25 13:57:41 jypak Archive Log: Correct comment header
 * Archive Log:
 * 
 * Overview: Performance Errors Item
 * 
 * @author: jypak
 * 
 ******************************************************************************/

public class PerformanceErrorsSectionView extends
        JSectionView<ISectionListener> {
    private static final long serialVersionUID = 317632809509908254L;

    private PerfErrorsCardView errorsCardView;

    private PerfErrorsCardView otherCardView;

    private JToolBar toolBar;

    private JButton borderBtn;

    private JButton alternationBtn;

    private IPropertyListener listener;

    public PerformanceErrorsSectionView() {
        super(STLConstants.K0741_COUNTERS.getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.common.JSection#getMainPanel()
     */
    @Override
    protected JComponent getMainComponent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        mainPanel.add(getCtrlPanel(), BorderLayout.NORTH);
        errorsCardView =
                new PerfErrorsCardView(
                        STLConstants.K0705_PORT_COUNTERS.getValue());

        JPanel propCardPanel = new JPanel();
        propCardPanel.setLayout(new GridBagLayout());
        propCardPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 2, 2));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(2, 2, 2, 2);
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridwidth = 1;
        gc.fill = GridBagConstraints.BOTH;
        propCardPanel.add(errorsCardView, gc);

        gc.gridwidth = GridBagConstraints.REMAINDER;
        otherCardView =
                new PerfErrorsCardView(
                        STLConstants.K0715_OTHER_COUNTERS.getValue());
        propCardPanel.add(otherCardView, gc);
        mainPanel.add(propCardPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    protected Component getCtrlPanel() {
        if (toolBar == null) {
            toolBar = new JToolBar();
            addStyleButtons();
        }
        return toolBar;
    }

    protected void addStyleButtons() {
        borderBtn =
                new JButton(STLConstants.K0530_SHOW_BORDER.getValue(),
                        UIImages.HIDE_BORDER.getImageIcon());
        borderBtn.setOpaque(false);
        borderBtn.setFocusable(false);
        borderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isSelected =
                        borderBtn.getIcon() != UIImages.SHOW_BORDER
                                .getImageIcon();
                setShowBorder(isSelected);
                if (listener != null) {
                    listener.onShowBorder(isSelected);
                }
            }
        });
        toolBar.add(borderBtn);

        alternationBtn =
                new JButton(STLConstants.K0533_UNI_ROWS.getValue(),
                        UIImages.UNI_ROWS.getImageIcon());
        alternationBtn.setOpaque(false);
        alternationBtn.setFocusable(false);
        alternationBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isSelected =
                        alternationBtn.getIcon() != UIImages.ALT_ROWS
                                .getImageIcon();
                setShowAlternation(isSelected);
                if (listener != null) {
                    listener.onShowAlternation(isSelected);
                }
            }
        });
        toolBar.add(alternationBtn);
    }

    public void setStyleListener(IPropertyListener listener) {
        this.listener = listener;
    }

    protected void setShowBorder(boolean isSelected) {
        if (isSelected) {
            borderBtn.setIcon(UIImages.SHOW_BORDER.getImageIcon());
            borderBtn.setText(STLConstants.K0530_SHOW_BORDER.getValue());
        } else {
            borderBtn.setIcon(UIImages.HIDE_BORDER.getImageIcon());
            borderBtn.setText(STLConstants.K0531_HIDE_BORDER.getValue());
        }
    }

    protected void setShowAlternation(boolean isSelected) {
        if (isSelected) {
            alternationBtn.setIcon(UIImages.ALT_ROWS.getImageIcon());
            alternationBtn.setText(STLConstants.K0532_ALT_ROWS.getValue());
        } else {
            alternationBtn.setIcon(UIImages.UNI_ROWS.getImageIcon());
            alternationBtn.setText(STLConstants.K0533_UNI_ROWS.getValue());
        }
    }

    /**
     * @return the rxErrorsCardView
     */
    public PerfErrorsCardView getErrorsCardView() {
        return errorsCardView;
    }

    public PerfErrorsCardView getOtherCardView() {
        return otherCardView;
    }

}
