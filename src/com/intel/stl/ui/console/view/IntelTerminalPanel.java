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
 *  Archive Log:    Revision 1.6  2015/10/09 13:40:41  rjtierne
 *  Archive Log:    PR 129027 - Need to handle customized command prompts when detecting commands on console
 *  Archive Log:    - Overriding Gritty TermPanel#consumeRun() to identify what the customized prompt is and
 *  Archive Log:    store it in the controller. Then when a prompt and command string is received the command
 *  Archive Log:    is parsed and sent to the help system.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/08/17 18:54:14  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/06/02 16:38:19  rjtierne
 *  Archive Log:    PR 128824 - Interactive Console only can monitor commands input from key typing
 *  Archive Log:    Override TermPanel's StyledRunConsumer#consumeRun() to get access to buffer
 *  Archive Log:    used to display commands on the screen. Filtered input commands from buffer
 *  Archive Log:    and sent to Help System for topic look up and display.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/06/02 16:16:37  rjtierne
 *  Archive Log:    128824 - Interactive Console only can monitor commands input from key typing
 *  Archive Log:    Override TermPanel's StyledRunConsumer#consumeRun() to get access to buffer
 *  Archive Log:    used to display commands on the screen. Filtered input commands from buffer
 *  Archive Log:    and sent to Help System for topic look up and display.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/05/27 21:53:52  rjtierne
 *  Archive Log:    128874 - Eliminate login dialog from admin console and integrate into panel
 *  Archive Log:    Checked for NULL intelKeyHandle in processKeyEvent to prevent NullPointerException
 *  Archive Log:    when the user clicks on the terminal and hits a key before loggin into a console
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

import com.intel.stl.ui.common.IHelp;
import com.intel.stl.ui.console.IConsole;
import com.wittams.gritty.BackBuffer;
import com.wittams.gritty.ScrollBuffer;
import com.wittams.gritty.Style;
import com.wittams.gritty.StyleState;
import com.wittams.gritty.swing.TermPanel;

public class IntelTerminalPanel extends TermPanel {

    private static final long serialVersionUID = 7434759697031493119L;

    protected KeyListener intelKeyHandler;

    protected boolean keyHandlerEnable = true;

    private final IHelp consoleHelpListener;

    private final IConsole consoleController;

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
            StyleState styleState, IHelp consoleHelpListener,
            IConsole consoleStateController) {
        super(backBuffer, scrollBuffer, styleState);
        this.consoleHelpListener = consoleHelpListener;
        this.consoleController = consoleStateController;
    }

    @Override
    public void consumeRun(int x, int y, Style style, char[] buf, int start,
            int len) {
        super.consumeRun(x, y, style, buf, start, len);
        int old = start % getColumnCount();

        try {
            String cmd = null;
            if (consoleController.isInitialized()) {

                //Get the prompt and command from the buffer
                int begin = start - old;
                int end = len + old;
                cmd = new String(buf, begin, end);
                cmd = cmd.trim();

                // Initialize the prompt so we know where commands are
                if (consoleController.isPromptReady()) {
                    consoleController.setPrompt(cmd);
                    consoleController.setPromptReady(false);
                    System.out.println("prompt = "
                            + consoleController.getPrompt());
                }

                // Next time around the cmd string will be the prompt
                if (cmd.equals("done")) {
                    consoleController.setPromptReady(true);
                }

                cmd = (cmd.equals("done")) ? "toc" : cmd;
                if ((cmd != null) && (consoleController.getPrompt() != null)) {
                    String commandLine =
                            cmd.replace(consoleController.getPrompt(), "")
                                    .trim();
                    String command = commandLine.split(" ")[0];
                    consoleHelpListener.parseCommand(command);
                    consoleHelpListener.updateSelection(command);
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processKeyEvent(final KeyEvent e) {

        if (keyHandlerEnable && (intelKeyHandler != null)) {
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
