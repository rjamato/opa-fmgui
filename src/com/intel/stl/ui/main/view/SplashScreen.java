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
/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: SplashScreen.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.7  2016/01/26 19:07:00  fernande
 *  Archive Log: PR 132387 - [Dell]: FMGUI Fails to Open Due to Database Lock. Added support for a new image to be displayed on application shutdown
 *  Archive Log:
 *  Archive Log: Revision 1.6  2015/08/17 18:54:02  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - changed frontend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.5  2015/06/10 19:58:57  jijunwan
 *  Archive Log: PR 129120 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
 *  Archive Log:
 *
 *  Overview:
 *
 *  @author:
 *
 ******************************************************************************/

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

    private final BorderLayout borderLayout = new BorderLayout();

    private final JLabel imageLabel = new JLabel();

    private final JProgressBar progressBar = new JProgressBar(0, 100);

    private boolean closed = false;

    public SplashScreen() {
        imageLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 3, 0));
        setAlwaysOnTop(true);
        setLayout(borderLayout);
        add(imageLabel, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);
        setSplashImage();
    }

    public void setSplashImage() {
        imageIcon = UIImages.SPLASH_IMAGE.getImageIcon();
        imageLabel.setIcon(imageIcon);
        pack();
        setLocationRelativeTo(null);
    }

    public void setShutdownImage() {
        imageIcon = UIImages.SHUTDOWN_IMAGE.getImageIcon();
        imageLabel.setIcon(imageIcon);
        pack();
        setLocationRelativeTo(null);
    }

    public void showScreen() {
        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                closed = false;
                setVisible(true);
            }

        });
    }

    public void close() {
        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                closed = true;
                setVisible(false);
                dispose();
            }

        });
    }

    public boolean isClosed() {
        return closed;
    }

    public void setProgress(final int progress) {
        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                progressBar.setValue(progress);
            }

        });
    }

    public void setProgress(final String message) {
        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                if (message == null) {
                    progressBar.setStringPainted(false);
                } else {
                    progressBar.setStringPainted(true);
                }
                progressBar.setString(message);
            }

        });
    }

    public void setProgress(final String message, final int progress) {
        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                setProgress(progress);
                setProgress(message);
            }

        });
    }

}
