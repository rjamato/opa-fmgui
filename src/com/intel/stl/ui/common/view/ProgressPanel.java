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
 *  File Name: ProgressPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/11/05 16:16:56  jijunwan
 *  Archive Log:    improvement on progress display - only show progress when it's not finished yet
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/26 14:57:18  jijunwan
 *  Archive Log:    added progress observer and progress panel
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;

public class ProgressPanel extends JPanel {
    private static final long serialVersionUID = 1912574664805410774L;

    private JLabel label;

    private JLabel running;

    private JProgressBar progressBar;

    private final int margin = 20;

    /**
     * Description:
     * 
     * @param isIndeterminate
     */
    public ProgressPanel(boolean isIndeterminate) {
        super();
        initComponent(isIndeterminate);
    }

    /**
     * Description:
     * 
     */
    protected void initComponent(boolean isIndeterminate) {
        setOpaque(true);
        setBackground(new Color(255, 255, 255, 230));
        setBorder(BorderFactory
                .createCompoundBorder(BorderFactory.createLineBorder(
                        UIConstants.INTEL_BLUE, 2), BorderFactory
                        .createEmptyBorder(margin, margin, margin, margin)));

        setLayout(new BorderLayout(2, 5));
        label = ComponentFactory.getH2Label("", Font.PLAIN);
        add(label, BorderLayout.NORTH);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(UIConstants.INTEL_WHITE);
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        running = new JLabel();
        running.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        panel.add(running, BorderLayout.WEST);

        progressBar = new JProgressBar(0, 100);
        progressBar.setUI(new BasicProgressBarUI());
        progressBar.setBorderPainted(false);
        if (isIndeterminate) {
            progressBar.setIndeterminate(true);
        }
        panel.add(progressBar, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
    }

    /**
     * 
     * Description:
     * 
     * @param value
     *            progress value in range [0, 100]. a value of -1 will set
     *            progress bar to Indeterminate state
     */
    public void setProgress(int value) {
        if (value > 0 && progressBar.isIndeterminate()) {
            progressBar.setIndeterminate(false);
        }
        progressBar.setValue(value);
    }

    public double getPercentComplete() {
        return progressBar.getPercentComplete();
    }

    public void setProgressNote(String text) {
        if (text != null) {
            if (!progressBar.isStringPainted()) {
                progressBar.setStringPainted(true);
            }
            progressBar.setString(text);
        } else if (text == null && progressBar.isStringPainted()) {
            progressBar.setStringPainted(false);
        }
    }

    public void setLabel(String text) {
        label.setText(text);
        running.setIcon(text == null ? null : UIImages.RUNNING.getImageIcon());
    }
}
