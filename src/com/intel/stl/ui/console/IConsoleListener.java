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
 *  File Name: IConsoleListener.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/04/10 14:08:39  rjtierne
 *  Archive Log:    PR 126675 - User cannot execute commands on duplicate Console numbers beyond 10 consoles.
 *  Archive Log:    Added isConsoleAllowed() to interface
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/04/09 21:13:00  rjtierne
 *  Archive Log:    126675 - User cannot execute commands on duplicate Console numbers beyond 10 consoles.
 *  Archive Log:    Added isConsoleAllowed() to interface
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/10/29 19:49:09  rjtierne
 *  Archive Log:    When shutting down a console, only close session when the threads using
 *  Archive Log:    them have stopped
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/28 22:23:14  rjtierne
 *  Archive Log:    Corrected intermittent console hanging; still possible exception on session closure
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/24 14:36:37  rjtierne
 *  Archive Log:    Added new methods onLock(), onUnlock(), getLoginDialog(), getTerminal(),
 *  Archive Log:    and showLoginDialog() to interface.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/20 20:37:25  rjtierne
 *  Archive Log:    Added new method authenticateSession() to interface
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/01 19:47:19  rjtierne
 *  Archive Log:    Added getHelpController() and getSession()
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/23 19:47:00  rjtierne
 *  Archive Log:    Integration of Gritty for Java Console
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/09 14:20:55  rjtierne
 *  Archive Log:    Restructured code to accommodate new console login dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/05 21:56:28  jijunwan
 *  Archive Log:    L&F adjustment on Console Views
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 19:53:57  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Interface to listen for events from a console terminal view.
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.console;

import java.awt.Point;

import com.intel.stl.ui.common.IHelp;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.console.view.IntelTerminalView;
import com.intel.stl.ui.console.view.LoginDialogView;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

// Added this comment to correct PR 126675 comment above
public interface IConsoleListener extends IPageController {

    public void initializeTerminal(LoginBean loginBean)
            throws NumberFormatException;

    public void initializeTerminal(LoginBean loginBean, String command);

    public void updateInfoPanel(LoginBean loginBean);

    public void addToQueue(String command);

    public void addNewConsole(String command);

    public void shutDownConsole();

    public void updatePersonalizedTab(String command);

    public void onLock(boolean isSelected);

    public void onUnlock(String pw) throws JSchException;

    public void onConnect(boolean sessionRunning);

    public void onConnectFail(Exception e);

    public int getId();

    public void setCursor(Point position);

    public IHelp getHelpController();

    public Session getSession();

    public boolean authenticateSession(String pw) throws JSchException;

    public LoginDialogView getLoginDialog();

    public IntelTerminalView getTerminal();

    public void showLoginDialog();

    public void closeSession();

    public void terminalStopped();

    public boolean isConsoleAllowed();

}
