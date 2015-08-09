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
package com.intel.stl.ui.common.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;

/**
 * @author jijunwan
 * 
 */
public abstract class JSectionView<E extends ISectionListener> extends JPanel {
    private static final long serialVersionUID = 2453671952562188530L;

    private JPanel titlePanel;

    private JLabel titleLabel;

    private JPanel buttonPanel;

    private JButton helpBtn;

    private E listener;

    protected final JComponent mainComponent;

    public JSectionView(String title) {
        this(title, null);
    }

    public JSectionView(String title, Icon icon) {
        setLayout(new BorderLayout());
        add(getTitlePanel(title, icon), BorderLayout.NORTH);
        mainComponent = getMainComponent();
        if (mainComponent != null) {
            add(mainComponent, BorderLayout.CENTER);
        }
    }

    public void setSectionListener(E listener) {
        this.listener = listener;
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setTitle(String title, Icon icon) {
        titleLabel.setText(title);
        titleLabel.setIcon(icon);
    }

    public void setIcon(Icon icon) {
        titleLabel.setIcon(icon);
    }

    public JButton getHelpButton() {
        return helpBtn;
    }

    protected JPanel getTitlePanel(String title, Icon icon) {
        if (titlePanel != null) {
            return titlePanel;
        }

        titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(0, 0, 2, 0, UIConstants.INTEL_ORANGE),
                BorderFactory.createEmptyBorder(0, 2, 0, 2)));
        titleLabel = createTitleLabel(title);
        titleLabel.setIcon(icon);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(getButtonPanel(), BorderLayout.EAST);
        return titlePanel;
    }

    protected JLabel createTitleLabel(String title) {
        return ComponentFactory.getH3Label(title, Font.PLAIN);
    }

    protected JPanel getButtonPanel() {
        if (buttonPanel != null) {
            return buttonPanel;
        }

        buttonPanel = new JPanel();
        addControlButtons(buttonPanel);
        return buttonPanel;
    }

    protected void addControlButtons(JPanel panel) {
        helpBtn =
                ComponentFactory.getImageButton(UIImages.HELP_ICON
                        .getImageIcon());
        helpBtn.setToolTipText(STLConstants.K0037_HELP.getValue());
        helpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onHelp();
            }
        });
        panel.add(helpBtn);
    }

    protected abstract JComponent getMainComponent();
}
