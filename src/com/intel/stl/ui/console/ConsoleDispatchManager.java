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
 *  File Name: ConsoleDispatchManager.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.27  2015/11/11 13:26:29  robertja
 *  Archive Log:    PR 130278 - Store console tab help pane state on a per-tab basis so that help info is restored when focus returns to a tab.
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/09/08 21:46:27  jijunwan
 *  Archive Log:    PR 130330 - Windows FM GUI - Admin->Console - switching side tabs causes multiple consoles
 *  Archive Log:    - changed code to distinguish number of connected consoles and number of consoles
 *  Archive Log:    - changed ConsolePage to use number of consoles, so if we have an unconnected console, it doesn't create another new console.
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/08/17 18:54:27  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/05/27 14:33:56  rjtierne
 *  Archive Log:    128874 - Eliminate login dialog from admin console and integrate into panel
 *  Archive Log:    Removed loginDialogView and implemented some reorganization to accommodate removal
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/04/10 14:07:58  rjtierne
 *  Archive Log:    PR 126675 - User cannot execute commands on duplicate Console numbers beyond 10 consoles.
 *  Archive Log:    - Changed value in sessionMap to be AtomicInteger which keeps track of # channels in a session
 *  Archive Log:    - onConnectFail() now closes channel
 *  Archive Log:    - incrementSessionUsers() and decrementSessionUsers() now using thread safe AtomicInteger
 *  Archive Log:    - Added null pointer protection to closeSession()
 *  Archive Log:    - Added closeChannel() which is called in terminalStopped()
 *  Archive Log:    - Implemented isConsoleAllowed() enforce only 10 channels per session
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/04/10 11:31:27  jypak
 *  Archive Log:    Klockwork critical issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/04/09 21:12:20  rjtierne
 *  Archive Log:    126675 - User cannot execute commands on duplicate Console numbers beyond 10 consoles.
 *  Archive Log:    - Changed value in sessionMap to be AtomicInteger which keeps track of # channels in a session
 *  Archive Log:    - onConnectFail() now closes channel
 *  Archive Log:    - incrementSessionUsers() and decrementSessionUsers() now using thread safe AtomicInteger
 *  Archive Log:    - Added null pointer protection to closeSession()
 *  Archive Log:    - Added closeChannel() which is called in terminalStopped()
 *  Archive Log:    - Implemented isConsoleAllowed() enforce only 10 channels per session
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/04/02 13:32:52  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/03/16 17:47:40  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/03/05 17:34:30  jijunwan
 *  Archive Log:    new constants and constant name change
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/10/30 17:02:04  rjtierne
 *  Archive Log:    Use a ConcurrentHashMap to prevent ConcurrentModificationException on shutdown
 *  Archive Log:    when iterating through the map and removing consoles from the map
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/10/29 19:49:09  rjtierne
 *  Archive Log:    When shutting down a console, only close session when the threads using
 *  Archive Log:    them have stopped
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/10/29 14:18:02  rjtierne
 *  Archive Log:    Close session on connection failures
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/10/28 22:23:14  rjtierne
 *  Archive Log:    Corrected intermittent console hanging; still possible exception on session closure
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/10/24 14:35:47  rjtierne
 *  Archive Log:    Pass console id to login dialog in support of unlocking console. Make connection
 *  Archive Log:    failure errors more descriptive. Added onUnlockThread() to authenticate console unlock.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/10/22 15:41:36  rjtierne
 *  Archive Log:    Added null pointer protection to method removeConsole()
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/10/21 14:04:32  rjtierne
 *  Archive Log:    Passing main window for reference when displaying dialogs
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/10/20 20:38:13  rjtierne
 *  Archive Log:    Name of constant STL80003 changed
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/10/13 14:53:52  rjtierne
 *  Archive Log:    Made cleanup more efficient and complete in removeConsole() and closeSession()
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/07 19:55:39  rjtierne
 *  Archive Log:    Moved session code into if-statement to eliminate potential null pointer access
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/01 19:43:57  rjtierne
 *  Archive Log:    Add code to clean up dead terminals and only kill sessions no longer in use
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/09/23 19:47:00  rjtierne
 *  Archive Log:    Integration of Gritty for Java Console
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/09 20:05:11  rjtierne
 *  Archive Log:    Restructured exception handling in SwingWorker
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/09 14:20:55  rjtierne
 *  Archive Log:    Restructured code to accommodate new console login dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/05 21:56:28  jijunwan
 *  Archive Log:    L&F adjustment on Console Views
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/04 21:06:26  rjtierne
 *  Archive Log:    Now only showing connected consoles
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 19:53:57  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: This class listens for requests to create new consoles and 
 *  initialize their terminals, and to close running consoles
 *
 *  @author: rjtierne
 *
 *****************************************************************************/
package com.intel.stl.ui.console;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.console.view.ConsoleSubpageView;
import com.intel.stl.ui.console.view.ConsoleTerminalView;
import com.intel.stl.ui.console.view.ConsoleView;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.view.IFabricView;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ConsoleDispatchManager implements IConsoleEventListener {

    public final static int MAX_NUM_CONSOLES = 50;

    public final static int MAX_NUM_CONSOLES_IN_SESSION = 10;

    public final static int SSH_PORT = 22;

    public final static int REASON_INIT = 1;

    public final static int REASON_UNLOCK = 2;

    private final ConsoleSubpageView subpageView;

    private Context context;

    private IProgressObserver observer;

    private LoginBean defaultLoginBean;

    private ConsoleTerminalController console;

    private ConsoleTerminalView consoleTerminalView;

    private IConsoleLogin consoleLogin;

    private final IConsoleEventListener listener = this;

    private ITabListener tabListener;

    private Thread initConsoleThread = null;

    private Thread unlockThread = null;

    private volatile ConcurrentHashMap<Integer, ConsoleTerminalController> consoleControllers =
            new ConcurrentHashMap<Integer, ConsoleTerminalController>();

    private final LinkedHashMap<Session, AtomicInteger> sessionMap =
            new LinkedHashMap<Session, AtomicInteger>();

    private int consoleNum = 0;

    private final IFabricView owner;

    // Added this comment to correct PR 126675 comment above
    public ConsoleDispatchManager(ConsoleView consoleView, IFabricView owner) {

        this.owner = owner;
        this.subpageView = consoleView.getConsoleSubpageView();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.console.IConsoleDispatchListener#setContext(com.intel
     * .stl .ui.main.Context, com.intel.stl.ui.common.IProgressObserver)
     */
    @Override
    public void setContext(Context context, IProgressObserver observer) {
        this.context = context;
        this.observer = observer;

        SubnetDescription sd = context.getSubnetDescription();
        defaultLoginBean =
                new LoginBean(sd.getCurrentUser(), sd.getCurrentFE().getHost(),
                        String.valueOf(SSH_PORT));
        this.subpageView.setDefaultLoginBean(defaultLoginBean);
    }
    
    @Override
    public ConsoleTerminalController getConsoleController(Integer index){
    	return consoleControllers.get(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.IConsoleDispatchListener#addNewConsole()
     */

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.console.IConsoleEventListener#addNewConsole(booelan,
     * String)
     */
    @Override
    public void addNewConsole(final LoginBean loginBean,
            final boolean showDialog, final String command) {

        Runnable addConsole = new Runnable() {

            @Override
            public void run() {

                // Create new console terminal view and controller
                consoleNum++;

                if (consoleNum <= MAX_NUM_CONSOLES) {
                    consoleTerminalView =
                            new ConsoleTerminalView(owner, subpageView);
                    consoleLogin = consoleTerminalView.getConsoleLogin();
                    consoleLogin.setConsoleEventListener(listener);
                    displayMaxConsoles(false);
                    console =
                            new ConsoleTerminalController(consoleTerminalView,
                                    STLConstants.K2107_ADM_CONSOLE.getValue()
                                            + " " + consoleNum + "," + " ",
                                    STLConstants.K2108_ADM_CONSOLE_DESC
                                            .getValue(), consoleNum,
                                    subpageView.getConsoleHelpListener());
                    console.setContext(context, observer);
                    console.setNewConsoleListener(listener);
                    subpageView.setTab(console);

                    // Provide a maximum for the number of allowable consoles
                    boolean newConsolesAllowed =
                            (consoleNum <= MAX_NUM_CONSOLES);
                    tabListener = subpageView.getNewTabView();
                    tabListener.enableNewTab(newConsolesAllowed);
                    consoleTerminalView.enableNewTab(newConsolesAllowed);

                    if (showDialog) {
                        consoleLogin.showLogin(defaultLoginBean, true,
                                consoleNum);
                    } else {
                        try {
                            initializeConsole(loginBean, command);
                        } catch (NumberFormatException e) {
                            consoleLogin
                                    .showMessage(UILabels.STL80002_INVALID_PORT_NUMBER
                                            .getDescription(loginBean
                                                    .getPortNum()));
                            consoleLogin.killProgress();
                        }
                    }

                } else {
                    consoleNum--;
                    displayMaxConsoles(true);
                }
            }
        };
        Util.runInEDT(addConsole);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.console.IConsoleEventListener#initializeConsole(com.
     * intel.stl.ui.console.LoginBean, java.lang.String)
     */
    @Override
    public void initializeConsole(LoginBean loginBean, String command)
            throws NumberFormatException {

        try {
            tabListener = subpageView.getNewTabView();
            tabListener.enableNewTab(false);
            consoleTerminalView.enableCommanding(false);
            if (command == null) {
                // Add new console with a new session
                console.initializeTerminal(loginBean);

            } else {
                console.initializeTerminal(loginBean, command);
            }
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.console.IConsoleDispatchListener#updateTab(java.lang
     * .String, java.lang.String)
     */
    @Override
    public void updatePersonalizedTab(LoginBean loginBean, String command) {

        // Put the username and command on the subpage tab
        subpageView.updatePersonalizedTab(loginBean.getUserName(), command);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.IConsoleDispatchListener#cleanup()
     */
    @Override
    public void cleanup() {

        // Loop through all of the running consoles and shut them down
        Iterator<Integer> it = consoleControllers.keySet().iterator();

        while (it.hasNext()) {
            removeConsole(consoleControllers.get(it.next()).getId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.IConsoleEventListener#onConnect(boolean,
     * String)
     */
    @Override
    public void onConnect(boolean connected, String command) {

        if (connected) {
            consoleLogin.hideLogin();
            consoleControllers.put(consoleNum, console);

            LoginBean loginBean = console.getLoginInfo();
            console.updateInfoPanel(loginBean);

            // Revise the name of the console
            String cmd = (command == null) ? " " : command;

            if (loginBean != null) {
                console.setName(loginBean.getUserName() + "," + cmd);
                subpageView.updatePersonalizedTab(loginBean.getUserName(), cmd);

                Session session = loginBean.getSession();
                if (sessionMap.get(session) == null) {
                    sessionMap.put(session, new AtomicInteger(1));
                } else {

                    if (sessionMap.get(session) == null) {
                        sessionMap.put(session, new AtomicInteger(1));
                    } else {
                        incrementSessionUsers(session);
                    }
                }
            }
        }

        consoleLogin.killProgress();
        tabListener.enableNewTab(true);
        consoleTerminalView.enableCommanding(true);

        // Use the console initializer to issue a "history" command to the
        // remote host, capture the result, and display it in the command field
        // of the console view
        try {
            ConsoleTerminalController.ConsoleInitializer consoleInitializer =
                    console.new ConsoleInitializer();
            consoleInitializer.initializeCommands(console.getLoginInfo());
        } catch (NumberFormatException e) {
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.console.IConsoleEventListener#onConnectFail(java.lang
     * .Exception)
     */
    @Override
    public void onConnectFail(ConsoleTerminalController console, int reason,
            Exception e) {

        if (e instanceof NumberFormatException) {
            consoleLogin.showMessage(UILabels.STL80002_INVALID_PORT_NUMBER
                    .getDescription(defaultLoginBean.getPortNum()));

        } else if (e instanceof JSchException) {
            String msg = new String();
            if (e.getMessage().compareTo("Auth fail") == 0) {
                msg = UILabels.STL80003_AUTHENTICATION_FAILURE.getDescription();

            } else if (e.getMessage().startsWith("SSH_MSG_DISCONNECT")) {
                msg = e.getMessage();

            } else {
                msg =
                        UILabels.STL80001_CONSOLE_CONNECTION_ERROR
                                .getDescription()
                                + " "
                                + consoleLogin.getHostName()
                                + ": "
                                + e.getMessage();
            }
            consoleLogin.showMessage(msg);
        } else {
            consoleLogin.showMessage(StringUtils.getErrorMessage(e));
        }

        consoleLogin.killProgress();
        tabListener.enableNewTab(true);
        consoleTerminalView.enableCommanding(true);
        if ((console != null) && (reason == REASON_INIT)) {
            console.shutDownConsole();
            closeChannel(console);
            closeSession(console);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.console.IConsoleEventListener#setTabListener(com.intel
     * .stl.ui.console.ITabListener)
     */
    @Override
    public void setTabListener(ITabListener tabListener) {

        this.tabListener = tabListener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.IConsoleEventListener#removeConsole()
     */
    @Override
    synchronized public void removeConsole(int id) {

        // This method is called on clean up and when an individual
        // console tab is closed and requires that the console id
        // to be supplied
        if (consoleControllers.size() > 0) {

            // Find the session for the console controller specified by id
            ConsoleTerminalController currentConsole =
                    consoleControllers.get(id);

            if (currentConsole != null) {
                Session currentSession = currentConsole.getSession();
                decrementSessionUsers(currentSession);
                displayMaxConsoles(false);

                // Shutdown console's processing thread.
                currentConsole.shutDownConsole();
            } // if
        } // if

        if (consoleNum > 0) {
            consoleNum--;
        }
    }

    protected void displayMaxConsoles(boolean enable) {
        // Loop through all of the running consoles and shut them down
        Iterator<Integer> it = consoleControllers.keySet().iterator();

        while (it.hasNext()) {
            ConsoleTerminalController console =
                    consoleControllers.get(it.next());

            if (console != null) {
                ((ConsoleTerminalView) console.getView())
                        .displayMaxConsoles(enable);
            }
        }
    }

    protected void incrementSessionUsers(final Session session) {

        AtomicInteger numUsers = sessionMap.get(session);
        if (numUsers != null) {
            numUsers.incrementAndGet();
        }
        sessionMap.put(session, numUsers);
    }

    protected void decrementSessionUsers(final Session session) {

        if ((sessionMap != null) && (session != null)) {
            AtomicInteger numUsers = sessionMap.get(session);
            if (numUsers != null) {
                numUsers.decrementAndGet();
            }
            sessionMap.put(session, numUsers);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.IConsoleEventListener#getNumConsoles()
     */
    @Override
    public int getNumConsoles(boolean connectedOnly) {
        if (connectedOnly) {
            return consoleControllers.size();
        } else {
            return consoleNum;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.IConsoleEventListener#getLoginDialog()
     */
    @Override
    /**
     * @return the consoleLogin
     */
    public IConsoleLogin getConsoleLogin() {
        return consoleLogin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.console.IConsoleEventListener#initializeConsoleThread
     * (com.intel.stl.ui.console.LoginBean, java.lang.String)
     */
    @Override
    public void initializeConsoleThread(final int consoleId,
            final LoginBean loginBean, final String command) {

        initConsoleThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    initializeConsole(loginBean, command);
                } catch (Exception e) {
                    onConnectFail(consoleControllers.get(consoleId),
                            REASON_INIT, e);
                }
            }
        });

        if ((initConsoleThread != null) && !initConsoleThread.isAlive()) {
            initConsoleThread.start();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.IConsoleEventListener#onUnlock(int)
     */
    @Override
    public void onUnlockThread(final int consoleId, final char[] pw) {

        unlockThread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    ConsoleTerminalController console =
                            consoleControllers.get(consoleId);
                    if (console != null) {
                        console.onUnlock(pw);
                    }
                } catch (JSchException e) {
                    onConnectFail(console, REASON_UNLOCK, e);
                }
            }
        });

        if ((unlockThread != null) && !unlockThread.isAlive()) {
            unlockThread.start();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.console.IConsoleEventListener#closeSession(com.intel
     * .stl.ui.console.ConsoleTerminalController)
     */
    @Override
    public void closeSession(ConsoleTerminalController console) {

        if (console != null) {
            Session session = console.getSession();

            // Shutdown the session through the TTY
            AtomicInteger numUsers = sessionMap.get(session);
            if ((numUsers != null) && (numUsers.get() == 0)) {

                ITty tty = console.getTty();
                if (tty != null) {
                    tty.close();
                    System.out.println("Session closed!\n");
                    sessionMap.remove(session);
                }
            }
        }
    }

    protected void closeChannel(ConsoleTerminalController console) {

        ITty tty = null;

        if (console != null) {
            tty = console.getTty();

            if (tty != null) {
                tty.closeChannel();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.IConsoleEventListener#terminalStopped(int)
     */
    @Override
    synchronized public void terminalStopped(int consoleId) {

        ConsoleTerminalController console = consoleControllers.get(consoleId);

        if ((console != null) && !console.isConnected()) {

            // Close this channel
            closeChannel(console);

            // Shut down the console if the session is no longer in use
            Session session = console.getSession();
            AtomicInteger numUsers = sessionMap.get(session);
            if ((numUsers != null) && (numUsers.get() == 0)) {
                closeSession(console);
            }

            // Remove the console from the map
            consoleControllers.remove(consoleId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.IConsoleEventListener#isConsoleAllowed(
     * ConsoleTerminalController)
     */
    @Override
    public boolean isConsoleAllowed(ConsoleTerminalController console) {

        AtomicInteger numUsers = sessionMap.get(console.getSession());
        int numUsersInt = 0;
        if (numUsers != null) {
            numUsersInt = numUsers.get();
        }
        return numUsersInt < MAX_NUM_CONSOLES_IN_SESSION;
    }
}
