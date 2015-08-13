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
 *  File Name: ConsoleView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10.2.1  2015/08/12 15:27:04  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/03/20 14:32:37  jypak
 *  Archive Log:    Help Set file name changed so that it can work with a delivered JAR.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/03/12 13:22:28  jypak
 *  Archive Log:    Updates to make the Console page's help set file available through fm-gui-consolehelp.jar.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/17 19:32:49  rjtierne
 *  Archive Log:    Adjust position of split pane divider to give more space for help system
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/08 19:05:27  jijunwan
 *  Archive Log:    improved to support both file and resource
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/07 19:54:17  rjtierne
 *  Archive Log:    Changed constructor input parameter "owner" type from Window to IFabricView
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/01 19:54:19  rjtierne
 *  Archive Log:    Restructuring of help utility on console view
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/23 19:59:21  rjtierne
 *  Archive Log:    Restored Help in preparation for work
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/23 19:47:01  rjtierne
 *  Archive Log:    Integration of Gritty for Java Console
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/09 14:20:54  rjtierne
 *  Archive Log:    Restructured code to accommodate new console login dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 19:53:59  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Top level view of the Console page with the subpage and 
 *  help views on a split pane
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.console.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.intel.stl.ui.common.HelpController;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.view.HelpView;
import com.intel.stl.ui.console.ConsoleDispatchManager;
import com.intel.stl.ui.main.view.IFabricView;

public class ConsoleView extends JPanel {

    private static final long serialVersionUID = 3726017200596523114L;

    private ConsoleSubpageView consoleSubpageView;

    private HelpView consoleHelpView;

    private final LoginDialogView loginDialogView;

    private static final String HELP_SET_FILE =
            "GUID-F80D11C1-C5DF-4967-A7BB-F46A2828FEC9.hs";

    private static String title = STLConstants.K1056_FAST_FABRIC_ASSISTANT
            .getValue();

    public ConsoleView(IFabricView owner) {

        loginDialogView = new LoginDialogView(owner);
        initComponents();
    }

    protected void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(1, 5, 2, 5);
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.weightx = 1;
        gc.weighty = 1;

        JSplitPane spltPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        spltPane.setContinuousLayout(true);
        spltPane.setResizeWeight(.2);

        HelpController consoleHelpController =
                new HelpController(title, HELP_SET_FILE);
        consoleHelpView = consoleHelpController.getView();
        spltPane.setRightComponent(consoleHelpView);

        consoleSubpageView =
                new ConsoleSubpageView(loginDialogView, consoleHelpController);
        spltPane.setLeftComponent(consoleSubpageView.getMainComponent());

        add(spltPane, gc);
    }

    public ConsoleSubpageView getConsoleSubpageView() {
        return consoleSubpageView;
    }

    /**
     * @return the consoleHelpView
     */
    public HelpView getConsoleHelpView() {
        return consoleHelpView;
    }

    /**
     * @return the loginDialogView
     */
    public LoginDialogView getLoginDialogView() {
        return loginDialogView;
    }

    public void setConsoleDispatchManager(ConsoleDispatchManager dispatchManager) {
        this.loginDialogView.setConsoleEventListener(dispatchManager);
        this.consoleSubpageView.setConsoleListener(dispatchManager);
    }

}
