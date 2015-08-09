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
 *  File Name: IntelTerminalPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/10/20 20:36:04  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.console.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.wittams.gritty.BackBuffer;
import com.wittams.gritty.ScrollBuffer;
import com.wittams.gritty.StyleState;
import com.wittams.gritty.swing.TermPanel;

public class IntelTerminalPanel extends TermPanel {

    private static final long serialVersionUID = 7434759697031493119L;

    protected KeyListener intelKeyHandler;

    protected boolean keyHandlerEnable = true;

    /**
     * Description:
     * 
     * @param backBuffer
     * 
     * @param scrollBuffer
     * 
     * @param styleState
     */
    public IntelTerminalPanel(BackBuffer backBuffer, ScrollBuffer scrollBuffer,
            StyleState styleState) {
        super(backBuffer, scrollBuffer, styleState);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void processKeyEvent(final KeyEvent e) {

        if (keyHandlerEnable) {
            final int id = e.getID();
            if (id == KeyEvent.KEY_PRESSED) {
                intelKeyHandler.keyPressed(e);
            } else if (id == KeyEvent.KEY_RELEASED) {
                /* keyReleased(e); */
            } else if (id == KeyEvent.KEY_TYPED) {
                intelKeyHandler.keyTyped(e);
            }
            e.consume();
        }
    }

    @Override
    public void setKeyHandler(KeyListener keyHandler) {
        intelKeyHandler = keyHandler;
    }

    public void enableKeyHandler(boolean enable) {
        keyHandlerEnable = enable;
    }

}
