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
 *  File Name: IntelTerminalPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1.2.1  2015/08/12 15:27:04  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
