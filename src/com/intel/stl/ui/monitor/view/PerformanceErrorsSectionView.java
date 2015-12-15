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
 * Archive Log: Revision 1.8  2015/08/17 18:54:24  jijunwan
 * Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 * Archive Log: - changed frontend files' headers
 * Archive Log:
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
