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
 *  File Name: IConsoleEventListener.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8.2.1  2015/08/12 15:27:18  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/04/10 14:08:25  rjtierne
 *  Archive Log:    PR 126675 - User cannot execute commands on duplicate Console numbers beyond 10 consoles.
 *  Archive Log:    Added isConsoleAllowed() to interface
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/04/09 21:13:00  rjtierne
 *  Archive Log:    126675 - User cannot execute commands on duplicate Console numbers beyond 10 consoles.
 *  Archive Log:    Added isConsoleAllowed() to interface
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/29 19:49:09  rjtierne
 *  Archive Log:    When shutting down a console, only close session when the threads using
 *  Archive Log:    them have stopped
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/28 22:23:14  rjtierne
 *  Archive Log:    Corrected intermittent console hanging; still possible exception on session closure
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/24 14:36:25  rjtierne
 *  Archive Log:    Added new methods getLoginDialog() and onUnlockThread() to interface
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/01 19:46:50  rjtierne
 *  Archive Log:    Added initializeConsoleThread() and getNumConsoles()
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/23 19:47:00  rjtierne
 *  Archive Log:    Integration of Gritty for Java Console
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 19:53:57  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Interface to listen for events from the console terminal. The
 *  ConsoleDispatchListener implements this interface to listen for events to
 *  add new consoles and shutdown existing ones.
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.console;

import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.console.view.LoginDialogView;
import com.intel.stl.ui.main.Context;

// Added this comment to correct PR 126675 comment above
public interface IConsoleEventListener {

    public void setContext(Context context, IProgressObserver observer);

    public void addNewConsole(final LoginBean loginBean, boolean showDialog,
            final String command);

    public void initializeConsole(LoginBean loginBean, String command)
            throws NumberFormatException;

    public void initializeConsoleThread(int consoleId, LoginBean loginBean,
            String command);

    public void updatePersonalizedTab(LoginBean loginBean, String command);

    public void onConnect(boolean connected, String command);

    public void onConnectFail(ConsoleTerminalController console, Exception e);

    public void cleanup();

    public void setTabListener(ITabListener tabListener);

    public void removeConsole(int id);

    public int getNumConsoles();

    public LoginDialogView getLoginDialog();

    public void onUnlockThread(int consoleId, String pw);

    public void closeSession(ConsoleTerminalController console);

    public void terminalStopped(int consoleId);

    public boolean isConsoleAllowed(ConsoleTerminalController console);
}
