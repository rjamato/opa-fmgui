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
 *  File Name: MainAppFrame.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.76  2015/09/30 13:26:49  fisherma
 *  Archive Log:    PR 129357 - ability to hide inactive ports.  Also fixes PR 129689 - Connectivity table exhibits inconsistent behavior on Performance and Topology pages
 *  Archive Log:
 *  Archive Log:    Revision 1.75  2015/09/25 13:41:31  jijunwan
 *  Archive Log:    PR 130611 - Event Fields missing after closing and reconnecting to the same fabric
 *  Archive Log:    - minor change to ensure we remove old component
 *  Archive Log:
 *  Archive Log:    Revision 1.74  2015/09/08 20:56:48  jijunwan
 *  Archive Log:    PR 130327 - Windows FM GUI - problems adjusting size of log at bottom of home tab
 *  Archive Log:    - set minimum size for related panels
 *  Archive Log:    - reset divider location when the event table's size is less than its minimum size
 *  Archive Log:
 *  Archive Log:    Revision 1.73  2015/09/08 18:34:15  jijunwan
 *  Archive Log:    PR 130277 - FM GUI Locked up due to [AWT-EventQueue-0] ERROR - Unsupported MTUSize 0x0d java.lang.IllegalArgumentException: Unsupported MTUSize 0x0d
 *  Archive Log:    - moved isDev to FMGuiPlugin so both backend and frontend can access it
 *  Archive Log:
 *  Archive Log:    Revision 1.72  2015/09/08 14:59:06  jijunwan
 *  Archive Log:    PR 130277 - FM GUI Locked up due to [AWT-EventQueue-0] ERROR - Unsupported MTUSize 0x0d java.lang.IllegalArgumentException: Unsupported MTUSize 0x0d
 *  Archive Log:    - moved isDev logic to backend
 *  Archive Log:
 *  Archive Log:    Revision 1.71  2015/08/17 18:54:02  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.70  2015/08/10 17:30:07  robertja
 *  Archive Log:    PR 128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.69  2015/08/05 02:47:03  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - introduced UndoHandler to manage undo/redo
 *  Archive Log:    - added undo/redo to main frame
 *  Archive Log:    - improved FabricController to support undoHandler and undo action on page selection
 *  Archive Log:    - improved FabricController to support the new page name based IPageListener
 *  Archive Log:
 *  Archive Log:    Revision 1.68  2015/07/13 22:24:53  jijunwan
 *  Archive Log:    PR 129533 - Close application without confirmation on changes
 *  Archive Log:    - check changes before we close frame
 *  Archive Log:
 *  Archive Log:    Revision 1.67  2015/06/30 17:50:10  fisherma
 *  Archive Log:    PR 129220 - Improvement on secure FE login.
 *  Archive Log:
 *  Archive Log:    Revision 1.66  2015/06/25 20:24:57  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - applied pin framework on fabric viewer and simple 'static' cards
 *  Archive Log:
 *  Archive Log:    Revision 1.65  2015/06/10 19:25:01  rjtierne
 *  Archive Log:    PR 128975 - Can not setup application log
 *  Archive Log:    Added new logging menu item under the Configure menu to display the logging
 *  Archive Log:    configuration dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.64  2015/05/01 21:29:07  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.63  2015/04/29 22:05:27  jijunwan
 *  Archive Log:    1) show parent's title on error dialog
 *  Archive Log:    2) add Intel log icon on title bar when no parent frame
 *  Archive Log:
 *  Archive Log:    Revision 1.62  2015/04/28 22:08:58  jijunwan
 *  Archive Log:    removed title argument from #showErrorMessage
 *  Archive Log:
 *  Archive Log:    Revision 1.61  2015/04/27 18:01:37  jijunwan
 *  Archive Log:    only show "random value" menu in dev version
 *  Archive Log:
 *  Archive Log:    Revision 1.60  2015/04/22 22:31:56  fisherma
 *  Archive Log:    Removing html tags from error messages.
 *  Archive Log:
 *  Archive Log:    Revision 1.59  2015/04/21 16:50:12  fernande
 *  Archive Log:    Adding method to display a message box
 *  Archive Log:
 *  Archive Log:    Revision 1.58  2015/04/08 20:33:57  fernande
 *  Archive Log:    Adding enhancement to showProgress to queue up and dequeue down labels as they are presented
 *  Archive Log:
 *  Archive Log:    Revision 1.57  2015/04/03 21:06:24  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.56  2015/04/03 14:42:40  rjtierne
 *  Archive Log:    Replaced frame initialization panel with an instructive welcome screen
 *  Archive Log:
 *  Archive Log:    Revision 1.55  2015/04/01 13:34:22  fernande
 *  Archive Log:    Fix for racing condition where the FV main frame is left with the init panel instead of the content panel.
 *  Archive Log:
 *  Archive Log:    Revision 1.54  2015/03/31 16:21:16  fernande
 *  Archive Log:    Failover support. Adding interfaces and implementations to display in the UI the failover progress. Added option to simulate failover
 *  Archive Log:
 *  Archive Log:    Revision 1.53  2015/03/30 22:36:10  jijunwan
 *  Archive Log:    improved AboutDialog
 *  Archive Log:
 *  Archive Log:    Revision 1.52  2015/03/26 13:55:30  fisherma
 *  Archive Log:    About Dialog:  Add application name as parameter to the dialog's title.  Updated third party licenses html file.  Added code to display third party tools and licenses information in the About dialog.
 *  Archive Log:
 *  Archive Log:    Revision 1.51  2015/03/18 20:53:26  fisherma
 *  Archive Log:    Adding AboutDialog and new images for the dialog.  Updated build.xml file to copy the html file containing copyright text into the 'help' directory inside the jar file.
 *  Archive Log:
 *  Archive Log:    Revision 1.50  2015/03/10 18:43:11  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *  Archive Log:    Revision 1.49  2015/02/26 22:11:05  fernande
 *  Archive Log:    Changes to support pending tasks.
 *  Archive Log:
 *  Archive Log:    Revision 1.48  2015/02/25 22:06:30  fernande
 *  Archive Log:    Fixes for remove subnet function in the Setup Wizard.
 *  Archive Log:
 *  Archive Log:    Revision 1.47  2015/02/25 14:33:31  fernande
 *  Archive Log:    Fix to use the current frame position if it is an empty frame. otherwise open a new frame with the previous saved position.
 *  Archive Log:
 *  Archive Log:    Revision 1.46  2015/02/24 14:47:42  fernande
 *  Archive Log:    Changes to the UI to display only subnet with the Autoconnect option at startup. If no subnet is defined as Autoconnect, then a blank screen is shown.
 *  Archive Log:
 *  Archive Log:    Revision 1.45  2015/02/19 21:42:40  fernande
 *  Archive Log:    Adding support to restore viewers to their previous screen state (Maximized/Screen location) and to start all subnets set to AutoConnect. If none is found, the last subnet is started.
 *  Archive Log:
 *  Archive Log:    Revision 1.44  2015/02/16 05:18:18  jijunwan
 *  Archive Log:    PR 127076 - assorted FV errors observed
 *  Archive Log:     - introduced frame visibility and name check to ensure application will shutdown even if we have uncaptured errors
 *  Archive Log:
 *  Archive Log:    Revision 1.43  2015/02/09 21:55:55  jijunwan
 *  Archive Log:    associate subnet name to FVMainFrame since it should support only one subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.42  2015/02/05 15:06:13  jijunwan
 *  Archive Log:    tried to improve stability on multi-subnet support
 *  Archive Log:
 *  Archive Log:    Revision 1.41  2015/02/02 20:37:14  fernande
 *  Archive Log:    Fixing the SetupWizard so that it can define new subnets. Fixed also StackOverflowError exception when switching subnets.
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2015/01/30 20:56:40  fernande
 *  Archive Log:    Changing the default close operation so that SubnetManager can control closing of Windows
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2015/01/30 20:27:59  fernande
 *  Archive Log:    Initial changes to support multiple fabric viewers
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2015/01/29 21:32:30  jijunwan
 *  Archive Log:    turned on debug info
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2015/01/11 21:49:25  jijunwan
 *  Archive Log:    changed to apply wizard on current subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2014/11/13 17:39:10  fernande
 *  Archive Log:    Fixed issue where errors during plugin init would not show up in the UI
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2014/11/05 19:05:33  fernande
 *  Archive Log:    Fixed an issue where defining a new subnet does not trigger a save topology task, causing notice processing to fail.
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2014/11/05 16:16:55  jijunwan
 *  Archive Log:    improvement on progress display - only show progress when it's not finished yet
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2014/10/24 20:10:53  jijunwan
 *  Archive Log:    minor L&F adjustment - set refresh button's disabled state color
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2014/10/21 16:37:11  fernande
 *  Archive Log:    Customization of Properties display (Show Options/Apply Options)
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2014/10/14 11:32:10  jypak
 *  Archive Log:    UI updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2014/10/09 13:00:25  fernande
 *  Archive Log:    Changed the FabricController to use the UI framework and converted Swing workers into AbstractTasks to optimize the switching of contexts and the refreshing of pages. These processes still run under Swing workers, but now each setContext is run on its own Swing worker to improve performance. Also, changed the ProgressObserver mechanism to provide a more accurate progress.
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2014/09/09 14:20:56  rjtierne
 *  Archive Log:    Restructured code to accommodate new console login dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2014/09/04 21:10:23  rjtierne
 *  Archive Log:    Added new interface methods for retrieving application window size & position
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2014/09/02 19:04:12  jijunwan
 *  Archive Log:    added subnet name to frame title
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2014/08/26 15:16:11  jijunwan
 *  Archive Log:    added refresh function to the framework
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2014/08/12 21:02:43  jijunwan
 *  Archive Log:    moved show dialogs to Util class, so in the future if we want to change L&F, we only need to change one place
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2014/07/16 16:54:18  fernande
 *  Archive Log:    Adding Subnet > Connect To menu
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2014/07/11 19:26:15  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2014/06/26 15:00:17  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/05/30 21:59:09  jijunwan
 *  Archive Log:    moved all random generation to API side, and added a menu item to allow a user turn on/off randomization
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/05/28 17:25:35  jijunwan
 *  Archive Log:    color severity on event table, by default sort event table by time, by default show event table on home page, show text for enums
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/05/23 18:39:09  jypak
 *  Archive Log:    Logging Configuration updates:
 *  Archive Log:
 *  Archive Log:    DatabaseException, ConfigurationException are relayed to a user via pop up dialog.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/05/16 04:30:37  jijunwan
 *  Archive Log:    Added code to deregister from task scheduler; Added Page Listener to listen enter or exit a (sub)page
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/05/14 21:43:22  jypak
 *  Archive Log:    Event Summary Table updates.
 *  Archive Log:    1. Replace EventMsgBean with EventDescription.
 *  Archive Log:    2. Update table contents with real data from Notice API.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/05/13 13:03:54  jypak
 *  Archive Log:    Event Summary Bar panel in pin board implementation.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/05/08 19:25:38  jijunwan
 *  Archive Log:    MVC refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/04/30 15:07:59  rjtierne
 *  Archive Log:    Changes to reflect renamed IPage
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/04/29 19:14:22  jijunwan
 *  Archive Log:    extended IntelTabbedPaneUI to support a control panel on top-right corner
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/04/21 15:43:06  jijunwan
 *  Archive Log:    Added #clear to be able to clear UI before we switch to another context. In the future, should change it to eventbus
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/04/20 03:18:18  jijunwan
 *  Archive Log:    support context switch and do cleanup when we close GUI
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/04/18 15:59:24  jijunwan
 *  Archive Log:    better exception handling
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/04/18 13:48:27  jypak
 *  Archive Log:    String constants and UI messages updates. Look and Feel updates. Additional updates regarding MVC.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/04/17 20:10:37  jijunwan
 *  Archive Log:    new main frame layout
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/04/17 16:53:44  jijunwan
 *  Archive Log:    integrate SetupWizard into main frame
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/04/17 15:09:28  jijunwan
 *  Archive Log:    added Intel Logo, fixed a minor issue on splash screen
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/04/17 14:45:29  rjtierne
 *  Archive Log:    Removed split pane from pin board, and
 *  Archive Log:    minor reformatting
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/16 16:20:45  jijunwan
 *  Archive Log:    minor refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/16 15:17:44  jijunwan
 *  Archive Log:    keep old style "Home Page", apply ApiBroker to update data
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/14 17:05:56  fernande
 *  Archive Log:    Fix thread error where object is not yet created
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:51:18  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/09 21:08:52  rjtierne
 *  Archive Log:    Renamed the events package back to tables
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/09 19:45:46  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: New main frame with split panes to separate the Pages from the
 *  pinboard and the event table at the bottom of every screen
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.main.view;

import static com.intel.stl.ui.common.STLConstants.K0001_FABRIC_VIEWER_TITLE;
import static com.intel.stl.ui.common.STLConstants.K0007_SUBNET;
import static com.intel.stl.ui.common.STLConstants.K0054_CONFIGURE;
import static com.intel.stl.ui.common.STLConstants.K0069_CONNECT_TO;
import static com.intel.stl.ui.common.STLConstants.K0112_ONLINE_HELP;
import static com.intel.stl.ui.common.STLConstants.K0669_LOGGING;
import static com.intel.stl.ui.common.STLConstants.K0689_WIZARD;
import static com.intel.stl.ui.common.STLConstants.K0740_CLOSE;
import static com.intel.stl.ui.common.STLConstants.K5001_EMAIL_MENU_ITEM_TEXT;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.VerticalLayout;

import com.intel.stl.api.FMGuiPlugin;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.EventTableController;
import com.intel.stl.ui.common.EventTableModel;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.EventSummaryBarPanel;
import com.intel.stl.ui.common.view.EventTableView;
import com.intel.stl.ui.common.view.IntelTabbedPaneUI;
import com.intel.stl.ui.common.view.ProgressPanel;
import com.intel.stl.ui.main.FabricController;
import com.intel.stl.ui.main.FabricModel;

public class FVMainFrame extends JFrame implements IFabricView, ChangeListener {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 6591005475727108668L;

    private static final String INIT_PANEL = "init panel";

    private static final String CONTENT_PANEL = "content panel";

    private static final String SUBNET_PROPERTY = "subnet";

    /**
     * Horizontally split pane for the top level of the application. Separates
     * the pages from the pin-board.
     */
    private JSplitPane mSplPnTopLevel;

    private JPanel mInitTopLevel;

    private CardLayout mCardLayout;

    private JSplitPane leftPane;

    /**
     * Tabbed pane that holds the pages
     */
    private JTabbedPane mTabbedTopLevel;

    private JToolBar toolBar;

    private JButton refreshBtn;

    private JButton undoBtn;

    private JButton redoBtn;

    /**
     * Pin Board Panel
     */
    private JPanel mPnlPinBoard;

    private PinBoardView pinBoardView;

    private EventSummaryBarPanel eventSummaryBarPanel;

    /**
     * Event Table Model
     */
    private EventTableModel mEventTableModel;

    /**
     * Event Table View
     */
    private EventTableView mEventTableView;

    /**
     * Event Table Controller
     */
    private EventTableController mEventTableController;

    /**
     * The controller for this view
     */
    private FabricController controller;

    private JMenu connecttoMenu;

    private JMenuItem closeMenu;

    private JMenuItem wizardMenu;

    private JMenuItem loggingMenu;

    private JMenuItem randomMenu;

    private JMenuItem hideNodesMenu;

    private JMenuItem onlineHelpMenu;

    private JMenuItem aboutMenu;

    private JMenuItem emailMenu;

    private JPanel glassPanel;

    private String subnetName;

    private ProgressPanel progressPanel;

    private String currentTab;

    private Dimension screenSize;

    private Rectangle screenBounds;

    private IPageListener listener;

    private final List<String> progressLabels = new ArrayList<String>();

    /**
     * 
     * Description: Constructor for the MainAppFrame class
     * 
     */
    public FVMainFrame(String subnetName) {
        super(K0001_FABRIC_VIEWER_TITLE.getValue());
        this.subnetName = subnetName;
        setName(K0001_FABRIC_VIEWER_TITLE.getValue() + "_" + subnetName);
    } // MainAppFrame

    /**
     * @param subnetName
     *            the subnetName to set
     */
    @Override
    public void setSubnetName(String subnetName) {
        this.subnetName = subnetName;
    }

    /**
     * @return the subnetName
     */
    @Override
    public String getSubnetName() {
        return subnetName;
    }

    @Override
    public void showInitScreen(Rectangle bounds, boolean maximized) {
        // Get property value for the hideNodesMenu
        // and trigger the action to make it appear checked
        if (controller.getHideInactiveNodes()) {
            hideNodesMenu.doClick();
        }

        createConnectMenu();
        mCardLayout.show(getContentPane(), INIT_PANEL);
        setBounds(bounds);
        setVisible(true);
        // Maximizing should be done after the frame is set visible
        if (maximized) {
            setExtendedState(getExtendedState() | Frame.MAXIMIZED_BOTH);
        }
    } // showInitScreen

    public void setController(FabricController controller) {
        this.controller = controller;
    }

    public void initComponents() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // Set the main frame screen dimensions
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.width = (int) (screenSize.width * 0.8);
        screenSize.height = (int) (screenSize.height * 0.8);
        setPreferredSize(screenSize);

        // set Icon images
        Image[] images =
                new Image[] { UIImages.LOGO_24.getImage(),
                        UIImages.LOGO_32.getImage(),
                        UIImages.LOGO_64.getImage(),
                        UIImages.LOGO_128.getImage() };
        setIconImages(Arrays.asList(images));
        // set menus
        installMenus();

        // set cards, so we an switch between content panel and init panel
        JPanel panel = (JPanel) getContentPane();
        mCardLayout = new CardLayout();
        panel.setLayout(mCardLayout);

        // mSplPnTopLevel = createContentPane();
        // panel.add(CONTENT_PANEL, mSplPnTopLevel);

        mInitTopLevel = createInitPanel();
        panel.add(INIT_PANEL, mInitTopLevel);

        progressPanel = new ProgressPanel(false);
        glassPanel = new JPanel();
        glassPanel.setOpaque(false);

        glassPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
        glassPanel.add(progressPanel, gc);

        setGlassPane(glassPanel);

        installActions();

        pack();
    }

    private JSplitPane createContentPane() {

        // Create a split pane and add it to the top level panel
        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        pane.setContinuousLayout(true);
        pane.setResizeWeight(.7);
        pane.setDividerSize(5);

        // Create a panel for the pin board
        mPnlPinBoard = new JPanel();
        mPnlPinBoard.setLayout(new BorderLayout());
        mPnlPinBoard.setOpaque(false);
        mPnlPinBoard.setMinimumSize(new Dimension(100, 300));

        eventSummaryBarPanel = new EventSummaryBarPanel();
        eventSummaryBarPanel.setVisible(true);
        mPnlPinBoard.add(eventSummaryBarPanel, BorderLayout.NORTH);
        mPnlPinBoard.setVisible(true);
        mPnlPinBoard.setBackground(UIConstants.INTEL_WHITE);

        pinBoardView = new PinBoardView();
        pinBoardView.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(pinBoardView);
        scrollPane.getViewport().setBackground(UIConstants.INTEL_WHITE);
        mPnlPinBoard.add(scrollPane, BorderLayout.CENTER);
        // Put the pin board on the right component of the main split pane
        pane.setRightComponent(mPnlPinBoard);

        // Create split pane on left side
        leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftPane.setContinuousLayout(true);
        leftPane.setResizeWeight(0.8);
        leftPane.setDividerSize(4);
        leftPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (mEventTableView.getSize().height < mEventTableView
                        .getMinimumSize().height) {
                    int newLoc =
                            leftPane.getSize().height
                                    - mEventTableView.getMinimumSize().height
                                    - 4;
                    leftPane.setDividerLocation(newLoc);
                }
            }
        });

        // Create the tabbed pane which will be populated when showContent() is
        // called
        mTabbedTopLevel = new JTabbedPane() {
            private static final long serialVersionUID = -638815127814812316L;

            @Override
            public void setSelectedIndex(int index) {
                if (listener == null
                        || listener.canPageChange(currentTab,
                                mTabbedTopLevel.getTitleAt(index))) {
                    super.setSelectedIndex(index);
                }
            }
        };
        mTabbedTopLevel.addChangeListener(this);

        IntelTabbedPaneUI tabUi = new IntelTabbedPaneUI();
        JPanel ctrPanel = tabUi.getControlPanel();
        initTabCtrlPanel(ctrPanel);
        mTabbedTopLevel.setUI(tabUi);
        leftPane.setTopComponent(mTabbedTopLevel);

        // Add the event table
        mEventTableModel = new EventTableModel();
        mEventTableView = new EventTableView(mEventTableModel);
        mEventTableView.setMinimumSize(new Dimension(200, 64));
        mEventTableView.setVisible(false);
        mEventTableController =
                new EventTableController(mEventTableModel, mEventTableView);
        leftPane.setBottomComponent(mEventTableView);

        // Put left pane to the left component of the main split pane
        pane.setLeftComponent(leftPane);

        return pane;
    }

    private void initTabCtrlPanel(JPanel ctrPanel) {
        ctrPanel.setLayout(new BorderLayout());
        ctrPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        JLabel intelLogo = new JLabel(UIImages.LOGO_32.getImageIcon());
        ctrPanel.add(intelLogo, BorderLayout.EAST);
        Component toolbar = createTabToolbar();
        ctrPanel.add(toolbar, BorderLayout.CENTER);
    }

    protected Component createTabToolbar() {
        JPanel panel = new JPanel();
        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        undoBtn = new JButton(UIImages.UNDO.getImageIcon());
        toolBar.add(undoBtn);

        redoBtn = new JButton(UIImages.REDO.getImageIcon());
        toolBar.add(redoBtn);

        refreshBtn =
                new JButton(STLConstants.K0107_REFRESH.getValue(),
                        UIImages.REFRESH.getImageIcon()) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void setEnabled(boolean b) {
                        super.setEnabled(b);
                        setForeground(b ? UIConstants.INTEL_MEDIUM_DARK_BLUE
                                : UIConstants.INTEL_GRAY);
                    }
                };
        refreshBtn.setFont(UIConstants.H4_FONT);
        refreshBtn.setForeground(UIConstants.INTEL_MEDIUM_DARK_BLUE);
        refreshBtn.setEnabled(false);
        toolBar.add(refreshBtn);

        panel.add(toolBar);
        return panel;
    }

    private JPanel createInitPanel() {
        JPanel pnlInit = new JPanel(new BorderLayout());
        pnlInit.setBackground(UIConstants.INTEL_BLUE);

        // Welcome Label
        JLabel lblWelcome =
                ComponentFactory.getH2Label(
                        UILabels.STL50093_WELCOME_FM_GUI.getDescription(),
                        Font.BOLD);
        lblWelcome.setForeground(UIConstants.INTEL_WHITE);
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        pnlInit.add(lblWelcome, BorderLayout.NORTH);

        // Add space between panels
        pnlInit.add(Box.createVerticalStrut(25));

        // Instruction panel and labels
        JPanel pnlInstructions = new JPanel(new VerticalLayout(20));
        pnlInstructions.setBackground(UIConstants.INTEL_WHITE);
        pnlInstructions.add(Box.createVerticalStrut(50));

        JLabel lblSelectSubnet =
                ComponentFactory.getH3Label(
                        UILabels.STL50091_CONNECT_TO_SUBNET.getDescription(),
                        Font.ITALIC);
        lblSelectSubnet.setHorizontalAlignment(SwingConstants.CENTER);
        pnlInstructions.add(lblSelectSubnet);

        JLabel lblOr =
                ComponentFactory.getH3Label(STLConstants.K3040_OR.getValue(),
                        Font.ITALIC);
        lblOr.setHorizontalAlignment(SwingConstants.CENTER);
        pnlInstructions.add(lblOr);

        JLabel lblConfigureSubnet =
                ComponentFactory.getH3Label(
                        UILabels.STL50092_CONFIGURE_SUBNET.getDescription(),
                        Font.ITALIC);
        lblConfigureSubnet.setHorizontalAlignment(SwingConstants.CENTER);
        pnlInstructions.add(lblConfigureSubnet);

        // Add instruction panel to init panel
        pnlInit.add(pnlInstructions, BorderLayout.CENTER);
        return pnlInit;
    }

    private void installMenus() {
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        JMenu subnet = new JMenu(K0007_SUBNET.getValue());
        menubar.add(subnet);
        connecttoMenu = new JMenu(K0069_CONNECT_TO.getValue());
        subnet.add(connecttoMenu);
        closeMenu = new JMenuItem(K0740_CLOSE.getValue());
        subnet.add(closeMenu);

        JMenu conf = new JMenu(K0054_CONFIGURE.getValue());
        menubar.add(conf);
        wizardMenu =
                new JMenuItem(K0689_WIZARD.getValue(),
                        UIImages.SETTING_ICON.getImageIcon());
        conf.add(wizardMenu);
        loggingMenu =
                new JMenuItem(K0669_LOGGING.getValue(),
                        UIImages.LOG_MENU_ICON.getImageIcon());
        conf.add(loggingMenu);

        emailMenu =
                new JMenuItem(K5001_EMAIL_MENU_ITEM_TEXT.getValue(),
                        UIImages.EMAIL_ICON.getImageIcon());
        conf.add(emailMenu);

        if (FMGuiPlugin.IS_DEV) {
            randomMenu =
                    ComponentFactory
                            .getIntelCheckBoxMenuItem(STLConstants.K0057_RANDOM
                                    .getValue());
            conf.add(randomMenu);
        }

        hideNodesMenu =
                ComponentFactory
                        .getIntelCheckBoxMenuItem(STLConstants.K5013_HIDE_INACTIVE_NODES_MENU_STR
                                .getValue());
        conf.add(hideNodesMenu);

        JMenu help = new JMenu(STLConstants.K0037_HELP.getValue());
        onlineHelpMenu =
                new JMenuItem(K0112_ONLINE_HELP.getValue(),
                        UIImages.HELP_ICON.getImageIcon());
        help.add(onlineHelpMenu);

        String aboutMenuStr = STLConstants.K3100_ABOUT_DIALOG.getValue();
        aboutMenu = new JMenuItem(aboutMenuStr);
        aboutMenu.setMnemonic(aboutMenuStr.charAt(0));

        help.add(aboutMenu);
        menubar.add(help);
    }

    private void installActions() {
        setAboutDialogAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showAboutDialog();
            }
        });
        setWindowAction(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (listener == null
                        || listener.canPageChange(currentTab, null)) {
                    dispose();
                    controller.onWindowClose();
                }
            }
        });
        setCloseAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener == null
                        || listener.canPageChange(currentTab, null)) {
                    controller.onMenuClose();
                }
            }
        });
        setWizardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showSetupWizard(subnetName);
            }
        });
        setLoggingAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showLoggingConfig();
            }
        });
        setEmailSettingsAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showEmailSettingsDialog();
            }
        });

        if (FMGuiPlugin.IS_DEV) {
            setRandomAction(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean addRandomValues =
                            ((JCheckBoxMenuItem) e.getSource()).isSelected();
                    if ((e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK) {
                        controller.startSimulatedFailover();
                    } else {
                        controller.applyRandomValue(addRandomValues);
                    }
                }
            });
        }
        setHideNodesAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.getCurrentSubnet() == null) {
                    // do nothing if not any subnet connected
                    return;
                }

                boolean hideInactiveNodes =
                        ((JCheckBoxMenuItem) e.getSource()).isSelected();
                controller.onHideInactiveNodes(hideInactiveNodes);
            }
        });
        setHelpAction(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.applyHelpAction(e);
            }
        });
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent event) {
                if (!isFrameMaximized()) {
                    screenBounds = getBounds();
                }

            }

            @Override
            public void componentResized(ComponentEvent event) {
                if (!isFrameMaximized()) {
                    screenBounds = getBounds();
                }
            }
        });
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        String oldTab = currentTab;
        int index = mTabbedTopLevel.getSelectedIndex();
        currentTab = mTabbedTopLevel.getTitleAt(index);
        if (listener != null) {
            listener.onPageChanged(oldTab, currentTab);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.hpc.stl.ui.IFabricView#showMessageAndExit(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void showMessageAndExit(String message, String title) {
        Util.showErrorMessage(this, message);
        controller.onWindowClose();
    }

    @Override
    public void showMessage(String message, String title) {
        Util.showErrorMessage(this, message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.IFabricView#showErrors(java.util.List)
     */
    @Override
    public void showErrors(List<Throwable> errors) {
        mInitTopLevel.removeAll();
        BoxLayout layout = new BoxLayout(mInitTopLevel, BoxLayout.Y_AXIS);
        mInitTopLevel.setLayout(layout);

        int numErrors = errors.size();
        JLabel label =
                numErrors == 1 ? ComponentFactory.getIntelH1Label(
                        UILabels.STL10101_ONE_ERROR_INIT_APP.getDescription(),
                        Font.PLAIN) : ComponentFactory.getIntelH1Label(
                        UILabels.STL10100_ERRORS_INIT_APP.getDescription(errors
                                .size()), Font.PLAIN);
        mInitTopLevel.add(label);
        for (Throwable e : errors) {
            label =
                    ComponentFactory
                            .getIntelH3Label(getErrorMsg(e), Font.PLAIN);
            mInitTopLevel.add(label);
        }
        mCardLayout.show(getContentPane(), INIT_PANEL);
        validate();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    protected String getErrorMsg(Throwable e) {
        String res = StringUtils.getErrorMessage(e);
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.IFabricView#showContent(java.util.List,
     * java.util.List)
     */
    @Override
    public void showContent(List<IPageController> pages) {
        mTabbedTopLevel.removeChangeListener(this);
        for (IPageController page : pages) {
            mTabbedTopLevel.addTab(page.getName(), page.getIcon(),
                    page.getView(), page.getDescription());
        }
        int index = mTabbedTopLevel.getSelectedIndex();
        currentTab = mTabbedTopLevel.getTitleAt(index);
        mTabbedTopLevel.addChangeListener(this);

        mCardLayout.show(getContentPane(), CONTENT_PANEL);

        Dimension d1 = mTabbedTopLevel.getMinimumSize();
        Dimension d2 = mEventTableView.getMinimumSize();
        int other = getJMenuBar().getHeight() + 4; // divider size
        int newMinHeight = d1.height + d2.height + other;
        setMinimumSize(new Dimension(getMinimumSize().width, newMinHeight));

        validate();
    }

    @Override
    public void close() {
        if (listener == null || listener.canPageChange(currentTab, null)) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    @Override
    public void resetConnectMenu() {
        createConnectMenu();

        validate();
    }

    private void createConnectMenu() {
        connecttoMenu.removeAll();
        List<SubnetDescription> subnets = controller.getSubnets();
        for (SubnetDescription subnet : subnets) {
            JMenuItem subnetConnectTo = createMenuItem(subnet);
            connecttoMenu.add(subnetConnectTo);
        }
    }

    private JMenuItem createMenuItem(SubnetDescription subnet) {
        String subnetName = subnet.getName();
        JMenuItem newMenuItem = new JMenuItem(subnetName);
        newMenuItem.putClientProperty(SUBNET_PROPERTY, subnetName);
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem menuItem = (JMenuItem) e.getSource();
                String subnetName =
                        (String) menuItem.getClientProperty(SUBNET_PROPERTY);
                controller.selectSubnet(subnetName);
            }
        });
        return newMenuItem;
    }

    @Override
    public void setCurrentTab(IPageController page) {
        int ix = mTabbedTopLevel.indexOfComponent(page.getView());
        if (ix >= 0) {
            mTabbedTopLevel.setSelectedIndex(ix);
            currentTab = mTabbedTopLevel.getTitleAt(ix);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.view.IFabricView#setWizardAtion(java.awt.event.
     * ActionListener)
     */
    @Override
    public void setWizardAction(ActionListener listener) {
        wizardMenu.addActionListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.view.IFabricView#setLoggingAction(java.awt.event
     * .ActionListener)
     */
    @Override
    public void setLoggingAction(ActionListener listener) {
        loggingMenu.addActionListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.view.IFabricView#setAboutDialogAction(java.awt.
     * event .ActionListener)
     */
    @Override
    public void setAboutDialogAction(ActionListener listener) {
        aboutMenu.addActionListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IFabricView#setEmailSettingsAction(
     * java.awt.event.ActionListener)
     */
    @Override
    public void setEmailSettingsAction(ActionListener listener) {
        emailMenu.addActionListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.view.IFabricView#setRandomAtion(java.awt.event.
     * ActionListener)
     */
    @Override
    public void setRandomAction(ActionListener listener) {
        randomMenu.addActionListener(listener);
    }

    public void setHelpAction(ActionListener listener) {
        onlineHelpMenu.addActionListener(listener);
    };

    public void setHideNodesAction(ActionListener listener) {
        hideNodesMenu.addActionListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.view.IFabricView#setWindowAction(java.awt.event
     * .WindowListener)
     */
    @Override
    public void setWindowAction(WindowListener listener) {
        addWindowListener(listener);
    }

    public void setCloseAction(ActionListener listener) {
        closeMenu.addActionListener(listener);
    }

    /**
     * 
     * Description:
     * 
     * @param listener
     */
    @Override
    public void setPageListener(final IPageListener listener) {
        this.listener = listener;
    }

    @Override
    public void setRefreshAction(ActionListener listener) {
        refreshBtn.addActionListener(listener);
    }

    @Override
    public void setRefreshRunning(boolean isRunning) {
        if (refreshBtn != null) {
            refreshBtn.setIcon(isRunning ? UIImages.RUNNING.getImageIcon()
                    : UIImages.REFRESH.getImageIcon());
        }
    }

    @Override
    public void setUndoAction(Action action) {
        undoBtn.setAction(action);
    }

    @Override
    public void setRedoAction(Action action) {
        redoBtn.setAction(action);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IFabricView#cleanup()
     */
    @Override
    public void cleanup() {
        dispose();
    }

    @Override
    public void clear() {
        mCardLayout.show(getContentPane(), INIT_PANEL);
        subnetName = null;
        setTitle(K0001_FABRIC_VIEWER_TITLE.getValue());
        mTabbedTopLevel.removeChangeListener(this);
        mTabbedTopLevel.removeAll();
        currentTab = null;
        mTabbedTopLevel.addChangeListener(this);
    }

    @Override
    public void bringToFront() {
        super.setVisible(true);
    }

    @Override
    public Rectangle getFrameBounds() {
        if (isFrameMaximized()) {
            return screenBounds;
        }
        return getBounds();
    }

    @Override
    public boolean isFrameMaximized() {
        return ((getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.view.IFabricView#setEventSummaryBarPanelController
     * (com.intel.stl.ui.common.EventSummaryBarPanelController)
     */
    @Override
    public EventSummaryBarPanel getEventSummaryBarPanel() {
        return this.eventSummaryBarPanel;
    }

    @Override
    public void setReady(boolean ready) {
        mTabbedTopLevel.setEnabled(ready);
        refreshBtn.setEnabled(ready);
        if (ready) {
            controller.processPendingTasks();
        }
    }

    @Override
    public boolean isReady() {
        return refreshBtn.isEnabled();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IFabricView#showEventSummaryTable()
     */
    @Override
    public void toggleEventSummaryTable() {
        if (mEventTableView.isVisible()) {
            mEventTableView.setVisible(false);
            leftPane.remove(mEventTableView);
        } else {
            mEventTableView.setVisible(true);
            leftPane.setRightComponent(mEventTableView);
        }
    }

    @Override
    public void showEventSummaryTable() {
        mEventTableView.setVisible(true);
        leftPane.setRightComponent(mEventTableView);
    }

    @Override
    public void hideEventSummaryTable() {
        mEventTableView.setVisible(false);
        leftPane.remove(mEventTableView);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IFabricView#getEventTableController()
     */
    @Override
    public EventTableController getEventTableController() {
        return mEventTableController;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.view.IEventRules#displayErrorMessage(java.lang
     * .String, java.lang.Exception)
     */
    @Override
    public void displayErrorMessage(String windowTitle, Exception exception) {
        Util.showError(this, exception);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.view.IFabricView#showProgress(java.lang.String,
     * boolean)
     */
    @Override
    public void showProgress(final String label, final boolean b) {
        if (b) {
            progressLabels.add(label);
            progressPanel.setLabel(label);
        } else {
            int last = progressLabels.size() - 1;
            if (last >= 0) {
                progressLabels.remove(last);
                last = progressLabels.size() - 1;
                if (last >= 0) {
                    progressPanel.setLabel(progressLabels.get(last));
                } else {
                    progressPanel.setLabel("");
                }
            }
        }
        glassPanel.setVisible(b);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IFabricView#setProgress(int)
     */
    @Override
    public void setProgress(final int progress) {
        if (!glassPanel.isVisible() && progress < 100) {
            glassPanel.setVisible(true);
        }
        progressPanel.setProgress(progress);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.view.IFabricView#setProgressNote(java.lang.String)
     */
    @Override
    public void setProgressNote(final String note) {
        if (!glassPanel.isVisible() && progressPanel.getPercentComplete() < 1.0) {
            glassPanel.setVisible(true);
        }
        progressPanel.setProgressNote(note);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IFabricView#getView()
     */
    @Override
    public Component getView() {
        return this;
    }

    /**
     * @return the pinBoardView
     */
    public PinBoardView getPinBoardView() {
        return pinBoardView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IFabricView#getScreenSize()
     */
    @Override
    public Dimension getScreenSize() {

        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public void setScreenSize(Dimension dimension) {
        setPreferredSize(dimension);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IFabricView#getScreenPosition()
     */
    @Override
    public Point getScreenPosition() {
        return new Point(getX(), getY());
    }

    public void modelUpdateFailed(FabricModel model, Throwable caught) {
        showProgress(null, false);
        setReady(true);
        if (caught instanceof InterruptedException
                || caught instanceof CancellationException) {
            return;
        }
        caught.printStackTrace();
        Util.showErrorMessage(this, model.getErrorMessage());
    }

    public void modelChanged(FabricModel model) {
        showProgress(null, false);
        SubnetDescription subnet = model.getCurrentSubnet();
        if (subnet == null) {
            subnetName = null;
            setupContentPane();
            mCardLayout.show(getContentPane(), INIT_PANEL);
            setTitle(K0001_FABRIC_VIEWER_TITLE.getValue());
        } else {
            setReady(true);
            subnetName = subnet.getName();
            mCardLayout.show(getContentPane(), CONTENT_PANEL);
            setTitle(K0001_FABRIC_VIEWER_TITLE.getValue() + " - " + subnetName);
        }
    }

    private void setupContentPane() {
        JPanel panel = (JPanel) getContentPane();
        if (mSplPnTopLevel != null) {
            panel.remove(mSplPnTopLevel);
        }
        mSplPnTopLevel = createContentPane();
        panel.add(CONTENT_PANEL, mSplPnTopLevel);
        setRefreshAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.onRefresh();
            }
        });
        setPageListener(controller);
    }

    public Component installGlassPanel(Component comp) {
        Component oldComp = null;
        if (glassPanel.getComponentCount() == 1) {
            oldComp = glassPanel.getComponent(0);
        } else if (glassPanel.getComponentCount() > 1) {
            // shouldn't happen
            throw new RuntimeException("Invalid glass panel!");
        }

        glassPanel.removeAll();
        glassPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
        glassPanel.add(comp, gc);
        glassPanel.updateUI();

        return oldComp;
    }

} // class MainAppFrame
