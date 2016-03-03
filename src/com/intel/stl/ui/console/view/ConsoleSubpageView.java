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
 *  File Name: ConsoleSubpageView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.15  2015/11/11 13:26:28  robertja
 *  Archive Log:    PR 130278 - Store console tab help pane state on a per-tab basis so that help info is restored when focus returns to a tab.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/08/17 18:54:14  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/06/25 11:55:06  jypak
 *  Archive Log:    PR 129073 - Add help action for Admin Page.
 *  Archive Log:    The help action is added to App, DG, VF,Console page and Console terminal. For now, a help ID and a content are being used as a place holder for each page. Once we get the help contents delivered by technical writer team, the HelpAction will be updated with correct help ID.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/05/27 14:35:27  rjtierne
 *  Archive Log:    128874 - Eliminate login dialog from admin console and integrate into panel
 *  Archive Log:    Removed loginDialogView
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/04/09 21:13:41  rjtierne
 *  Archive Log:    Made method closeConsole() synchronized to fix synchronization problem with
 *  Archive Log:    closing consoles too fast
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/04/02 13:33:06  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/10/07 19:54:59  rjtierne
 *  Archive Log:    In createPersonalizeTab() using equals() method instead of "==" for String comparison
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/01 19:50:14  rjtierne
 *  Archive Log:    To close console, call removeConsole() with console id, not tab number
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/09/23 19:47:01  rjtierne
 *  Archive Log:    Integration of Gritty for Java Console
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/09/09 20:48:44  rjtierne
 *  Archive Log:    Make the command field the focus when a console terminal appears
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/09 20:03:27  rjtierne
 *  Archive Log:    Added default login bean to console dialog to reduce typing
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/09 18:23:39  jijunwan
 *  Archive Log:    only show command on tab
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/09 14:20:54  rjtierne
 *  Archive Log:    Restructured code to accommodate new console login dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/05 21:56:32  jijunwan
 *  Archive Log:    L&F adjustment on Console Views
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 19:53:59  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: View to hold the tabbed pane for the console subpages
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.console.view;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.intel.stl.ui.common.IHelp;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.IntelTabbedPaneUI;
import com.intel.stl.ui.console.ConsoleTerminalController;
import com.intel.stl.ui.console.IConsoleEventListener;
import com.intel.stl.ui.console.IConsoleListener;
import com.intel.stl.ui.console.ITabListener;
import com.intel.stl.ui.console.LoginBean;

public class ConsoleSubpageView extends JPanel implements ITabListener {

    private static final long serialVersionUID = -5831289826804475532L;

    private JTabbedPane tabbedPane;

    private JButton helpBtn;

    private IntelTabbedPaneUI tabUI;

    private JPanel ctrPanel;

    private NewTabView newTabView;

    private LoginBean defaultLoginBean;

    private IConsoleEventListener consoleEventListener;

    private final IHelp consoleHelpListener;

    private final List<ConsoleTabView> tabList =
            new ArrayList<ConsoleTabView>();

    public ConsoleSubpageView(IHelp consoleHelpListener) {
        this.consoleHelpListener = consoleHelpListener;
        initComponents();
    }

    protected void initComponents() {

        // Create the tabbed pane which will be populated when getMainComponent
        // is called from subpages
        tabbedPane = new JTabbedPane();
        tabUI = new IntelTabbedPaneUI();
        ctrPanel = tabUI.getControlPanel();
        ctrPanel.setLayout(new BorderLayout());
        ctrPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 5));
        tabbedPane.setUI(tabUI);
        tabUI.setFont(UIConstants.H4_FONT);
        tabUI.setTabAreaInsets(new Insets(2, 5, 4, 5));
        addNewTab("+");

        // Add change listener to highlight the tabs
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                // Highlight the tabs depending on selection
                Runnable highlightTabs = new Runnable() {
                    @Override
                    public void run() {
                        if (tabbedPane.getSelectedIndex() >= 0) {
                            highlightTabs();     
                            Integer tabbedPaneIndex = tabbedPane.getSelectedIndex();                          
                            // Console indices start at 1, tabbedPane indices start at 0.
                            ConsoleTerminalController  consoleController = 
                            		consoleEventListener.getConsoleController(tabbedPaneIndex + 1);
                            if(consoleController != null){
                            String command = consoleController.getLastCommand();
	                            if(command != null){
		                            consoleHelpListener.parseCommand(command);
		                            consoleHelpListener.updateSelection(command);
	                            }
                            }
                        }
                    }
                };
                Util.runInEDT(highlightTabs);
            }
        });

        helpBtn =
                ComponentFactory.getImageButton(UIImages.HELP_ICON
                        .getImageIcon());
        helpBtn.setToolTipText(STLConstants.K0037_HELP.getValue());
        ctrPanel.add(helpBtn);
    }
    
    public void enableHelp(boolean b) {
        if (helpBtn != null) {
            helpBtn.setEnabled(b);
        }
    }

    public JButton getHelpButton() {
        return helpBtn;
    }

    public void setDefaultLoginBean(LoginBean defaultLoginBean) {
        this.defaultLoginBean = defaultLoginBean;
    }

    public void setConsoleListener(IConsoleEventListener consoleEventListener) {
        this.consoleEventListener = consoleEventListener;
    }

    public JComponent getMainComponent() {
        return tabbedPane;
    }

    public String getCurrentSubpage() {
        int currentTab = tabbedPane.getSelectedIndex();
        if (currentTab < 0) {
            return null;
        } else {
            return tabbedPane.getTitleAt(currentTab);
        }
    }

    protected void addNewTab(String symbol) {
        newTabView = new NewTabView(defaultLoginBean, consoleEventListener);
        tabbedPane.addTab(symbol, new JLabel());
        tabbedPane.setTabComponentAt(tabbedPane.indexOfTab(symbol), newTabView);
        tabbedPane.setEnabledAt(tabbedPane.indexOfTab(symbol), false);
        consoleHelpListener.resetView();
    }

    synchronized public void setTab(IConsoleListener subpage) {

        // Remove the "+" tab and add the new tab
        tabbedPane.remove(tabbedPane.getTabCount() - 1);
        tabbedPane.addTab(subpage.getName(), subpage.getIcon(),
                subpage.getView(), subpage.getDescription());
        ((ConsoleTerminalView) subpage.getView()).setFocus();

        // Add back a new "+" tab
        addNewTab("+");

        createPersonalizeTab(subpage);

        // Set the selected tab
        if (tabbedPane.getTabCount() > 0) {
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 2);
        }

        // Highlight the tabs
        highlightTabs();
    }

    public int getTabCount() {
        return tabbedPane.getTabCount();
    }

    public void createPersonalizeTab(IConsoleListener subpage) {

        int tabIndex = tabbedPane.getTabCount() - 2;

        String title = subpage.getName();
        String[] tabNames = title.split(",");
        ConsoleTabView consoleTabView =
                new ConsoleTabView(tabIndex, tabNames, subpage.getId());
        consoleTabView.addConsoleListener(subpage);
        tabList.add(consoleTabView);
        consoleTabView.addTabListener(this);

        boolean highlight = false;
        String currentSubpage = getCurrentSubpage();
        if (currentSubpage != null) {
            highlight = (currentSubpage.equals(title));
        }
        consoleTabView.setLabelProperties(highlight);

        tabbedPane.setTabComponentAt(consoleTabView.getTabIndex(),
                consoleTabView.getMainComponent());

        // Highlight the tabs
        highlightTabs();
    }

    public void updatePersonalizedTab(String userName, String command) {

        ConsoleTabView ctv =
                (ConsoleTabView) tabbedPane.getTabComponentAt(tabbedPane
                        .getSelectedIndex());

        if (ctv != null) {
            ctv.setUserName(userName);
            ctv.setCommandName(command);
        }

        // Highlight the tabs
        highlightTabs();
    }

    public void highlightTabs() {

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {

            boolean highlight = (i == tabbedPane.getSelectedIndex());

            // Skip the "Add New Tab"
            if (!(tabbedPane.getTabComponentAt(i) instanceof NewTabView)) {
                ConsoleTabView tabView =
                        (ConsoleTabView) tabbedPane.getTabComponentAt(i);
                if (tabView != null) {
                    tabView.setLabelProperties(highlight);
                }
            }

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.console.ITabListener#closeConsole(com.intel.stl.ui.console
     * .view.ConsoleTabView)
     */
    @Override
    public synchronized void closeConsole(ConsoleTabView tabView) {

        // Remove the specified tab and delete it from the list
        consoleEventListener.removeConsole(tabView.getConsoleId());

        int deletedKey = tabView.getTabIndex();
        tabbedPane.remove(tabView.getTabIndex());
        tabList.remove(deletedKey);

        // Set the selected tab
        if (deletedKey == tabList.size()) {
            tabbedPane.setSelectedIndex(deletedKey - 1);
        } else {
            tabbedPane.setSelectedIndex(deletedKey);
        }

        // Re-index the remaining tabs
        int tabIndex = 0;
        for (ConsoleTabView view : tabList) {
            view.setTabIndex(tabIndex);
            tabIndex++;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.ITabListener#enableNewTab(boolean)
     */
    @Override
    public void enableNewTab(boolean state) {

    }

    /**
     * @return the newTabView
     */
    @Override
    public NewTabView getNewTabView() {
        return newTabView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.console.ITabListener#getCurrentTabView()
     */
    @Override
    public ConsoleTabView getCurrentTabView() {
        return tabList.get(tabbedPane.getSelectedIndex());
    }

    /**
     * @return the consoleHelpListener
     */
    public IHelp getConsoleHelpListener() {
        return consoleHelpListener;
    }

}
