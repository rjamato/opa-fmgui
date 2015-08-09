/*
 * INTEL CONFIDENTIAL
 * Copyright (c) 2008-2012 Intel Corporation All Rights Reserved.
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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.Util;

public class SplashScreen extends JWindow {

    private static final long serialVersionUID = 1L;

    private ImageIcon imageIcon = null;

    private BorderLayout borderLayout = new BorderLayout();

    private JLabel imageLabel = new JLabel();

    private JProgressBar progressBar = new JProgressBar(0, 100);

    public SplashScreen() {
        imageLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 3, 0));
        imageIcon = UIImages.SPLASH_IMAGE.getImageIcon();
        imageLabel.setIcon(imageIcon);
        setAlwaysOnTop(true);
        setLayout(borderLayout);
        add(imageLabel, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    public void showScreen() {
        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                setVisible(true);
            }

        });
    }

    public void close() {
        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                setVisible(false);
                dispose();
            }

        });
    }

    public void setProgress(final String message, final int progress) {
        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                progressBar.setValue(progress);
                if (message == null) {
                    progressBar.setStringPainted(false);
                } else {
                    progressBar.setStringPainted(true);
                }
                progressBar.setString(message);
            }

        });
    }

}
