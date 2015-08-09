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
 *  File Name: FabricController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.107.2.1  2015/05/06 19:39:13  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.108  2015/05/01 21:29:06  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.107  2015/04/29 22:05:29  jijunwan
 *  Archive Log:    1) show parent's title on error dialog
 *  Archive Log:    2) add Intel log icon on title bar when no parent frame
 *  Archive Log:
 *  Archive Log:    Revision 1.106  2015/04/28 22:13:18  jijunwan
 *  Archive Log:    1) introduced component owner to Context, so when we have errors in data collection, preparation etc, we now there the error dialog should go
 *  Archive Log:    2) improved TaskScheduler to show error message on proper frame
 *  Archive Log:
 *  Archive Log:    Revision 1.105  2015/04/21 21:18:01  fernande
 *  Archive Log:    PR 127653 - FM GUI errors after connection loss. ExecutorServices used in TopologyView are now created in FabricController to shut them down when the connection is lost.
 *  Archive Log:
 *  Archive Log:    Revision 1.104  2015/04/21 16:49:44  fernande
 *  Archive Log:    Displaying message box after failover fails, instructing user to Refresh
 *  Archive Log:
 *  Archive Log:    Revision 1.103  2015/04/16 17:43:36  fernande
 *  Archive Log:    Added field that holds the subnet name that the SubnetManager has assigned to this controller. Added also logic to shutdown the graphService and the outlineService when closing the viewer or when connection to the FE is lost.
 *  Archive Log:
 *  Archive Log:    Revision 1.102  2015/04/15 17:40:49  jijunwan
 *  Archive Log:    added null check
 *  Archive Log:
 *  Archive Log:    Revision 1.101  2015/04/10 20:16:38  fernande
 *  Archive Log:    Fixes issues with the SubnetManager where the subnet name is not set when an error occurs
 *  Archive Log:
 *  Archive Log:    Revision 1.100  2015/04/09 22:53:59  jijunwan
 *  Archive Log:    after failover complete, refresh tasks by reset refresh rate
 *  Archive Log:
 *  Archive Log:    Revision 1.99  2015/04/08 15:20:39  fernande
 *  Archive Log:    Changes to allow for failover to work when the current (initial) FE is not available.
 *  Archive Log:
 *  Archive Log:    Revision 1.98  2015/04/03 21:06:22  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.97  2015/03/31 19:54:54  fisherma
 *  Archive Log:    Added method to get app info for the about dialog.
 *  Archive Log:
 *  Archive Log:    Revision 1.96  2015/03/31 16:21:47  fernande
 *  Archive Log:    Failover support. Adding interfaces and implementations to display in the UI the failover progress.
 *  Archive Log:
 *  Archive Log:    Revision 1.95  2015/03/30 22:47:09  jijunwan
 *  Archive Log:    1) added header for AboutDialog
 *  Archive Log:    2) moved to main.view
 *  Archive Log:
 *  Archive Log:    Revision 1.94  2015/03/30 22:36:12  jijunwan
 *  Archive Log:    improved AboutDialog
 *  Archive Log:
 *  Archive Log:    Revision 1.93  2015/03/27 20:50:37  fernande
 *  Archive Log:    Adding support for failover
 *  Archive Log:
 *  Archive Log:    Revision 1.92  2015/03/26 13:55:27  fisherma
 *  Archive Log:    About Dialog:  Add application name as parameter to the dialog's title.  Updated third party licenses html file.  Added code to display third party tools and licenses information in the About dialog.
 *  Archive Log:
 *  Archive Log:    Revision 1.91  2015/03/26 11:10:02  jypak
 *  Archive Log:    PR 126613 Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    -The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    -The Event Calculator clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.90  2015/03/25 11:26:59  jypak
 *  Archive Log:    Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    The Event Calculator and the Event Summary Table clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.89  2015/03/18 20:53:24  fisherma
 *  Archive Log:    Adding AboutDialog and new images for the dialog.  Updated build.xml file to copy the html file containing copyright text into the 'help' directory inside the jar file.
 *  Archive Log:
 *  Archive Log:    Revision 1.88  2015/03/10 18:43:15  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *  Archive Log:    Revision 1.87  2015/03/02 22:34:33  fernande
 *  Archive Log:    Fixes to handle a change in subnet name; if subnet name is changed, the model in FabricController is now updated as well as the name in the SubnetDescription in the subnet context to that the view and the back end are now in sync.
 *  Archive Log:
 *  Archive Log:    Revision 1.86  2015/02/26 22:10:27  fernande
 *  Archive Log:    Fix to refresh UserSettings in SubnetContext after it's updated by the Setup Wizard. Added pending tasks, which for now are only messages that should be displayed after the subnet is initialized. These tasks run after the initialization popup is hidden.
 *  Archive Log:
 *  Archive Log:    Revision 1.85  2015/02/26 16:20:07  fernande
 *  Archive Log:    Fixes NPE when there are no subnets opened and the subnetmanager tries to save screen state to the database. Changed showSetupWizard so that the wizard can show its view centered on the calling frame.
 *  Archive Log:
 *  Archive Log:    Revision 1.84  2015/02/26 13:47:42  fernande
 *  Archive Log:    Subnets are not being stopped properly. Fixed resetView
 *  Archive Log:
 *  Archive Log:    Revision 1.83  2015/02/25 22:05:49  fernande
 *  Archive Log:    Fixes for remove subnet function in the Setup Wizard.
 *  Archive Log:
 *  Archive Log:    Revision 1.82  2015/02/25 14:32:19  fernande
 *  Archive Log:    Fix to use the current frame position if it is an empty frame. otherwise open a new frame with the previous saved position.
 *  Archive Log:
 *  Archive Log:    Revision 1.81  2015/02/24 14:47:19  fernande
 *  Archive Log:    Changes to the UI to display only subnet with the Autoconnect option at startup. If no subnet is defined as Autoconnect, then a blank screen is shown.
 *  Archive Log:
 *  Archive Log:    Revision 1.80  2015/02/19 21:42:17  fernande
 *  Archive Log:    Adding support to restore viewers to their previous screen state (Maximized/Screen location) and to start all subnets set to AutoConnect. If none is found, the last subnet is started.
 *  Archive Log:
 *  Archive Log:    Revision 1.79  2015/02/16 05:18:19  jijunwan
 *  Archive Log:    PR 127076 - assorted FV errors observed
 *  Archive Log:     - introduced frame visibility and name check to ensure application will shutdown even if we have uncaptured errors
 *  Archive Log:
 *  Archive Log:    Revision 1.78  2015/02/13 17:52:24  jijunwan
 *  Archive Log:    1) added refresh function back, so when we have a network issue, the refresh button will allow us to reinitialize everything.
 *  Archive Log:    2) changed from showing message and exiting to just showing message since we are under multi-subnet mode. A user may choose another subnet instead of directly shutdown the application
 *  Archive Log:
 *  Archive Log:    Revision 1.77  2015/02/12 22:07:11  fernande
 *  Archive Log:    Changes to SubnetManager to manage contexts and views based on the key Host_Ip_Address+Port so that there is one view per subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.76  2015/02/10 19:35:14  fernande
 *  Archive Log:    Changes to handle SubnetConnectionExceptions
 *  Archive Log:
 *  Archive Log:    Revision 1.75  2015/02/09 23:07:20  jijunwan
 *  Archive Log:    removed debug print out
 *  Archive Log:
 *  Archive Log:    Revision 1.74  2015/02/09 21:55:15  jijunwan
 *  Archive Log:    associate subnet name to FabricController since it should support only one subnet. put some tasks on SwingWorker so we do not query DB or FE on EDT
 *  Archive Log:
 *  Archive Log:    Revision 1.73  2015/02/06 15:10:31  fernande
 *  Archive Log:    Changes to use the new subnetId for SubnetDescription instead of the subnet name
 *  Archive Log:
 *  Archive Log:    Revision 1.72  2015/02/05 15:06:12  jijunwan
 *  Archive Log:    tried to improve stability on multi-subnet support
 *  Archive Log:
 *  Archive Log:    Revision 1.71  2015/02/02 20:36:35  fernande
 *  Archive Log:    Fixing the SetupWizard so that it can define new subnets. Fixed also StackOverflowError exception when switching subnets.
 *  Archive Log:
 *  Archive Log:    Revision 1.70  2015/01/30 20:59:43  fernande
 *  Archive Log:    Changing the default close operation
 *  Archive Log:
 *  Archive Log:    Revision 1.69  2015/01/30 20:27:16  fernande
 *  Archive Log:    Initial changes to support multiple fabric viewers
 *  Archive Log:
 *  Archive Log:    Revision 1.68  2015/01/21 21:21:17  rjtierne
 *  Archive Log:    Supplying preferences wizard with sweep interval through Context
 *  Archive Log:    for comparison with refresh rate supplied by user input. Also providing
 *  Archive Log:    task scheduler to preferences wizard so user supplied refresh rate can
 *  Archive Log:    be updated.
 *  Archive Log:
 *  Archive Log:    Revision 1.67  2015/01/11 21:49:25  jijunwan
 *  Archive Log:    changed to apply wizard on current subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.66  2014/12/23 19:33:18  rjtierne
 *  Archive Log:    No longer passing isFirstRun to view since controller has most accurate state; view now gets first run state from controller
 *  Archive Log:
 *  Archive Log:    Revision 1.65  2014/12/11 18:52:55  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.64  2014/12/10 20:52:24  rjtierne
 *  Archive Log:    Support for new Setup Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.63  2014/11/11 18:06:50  fernande
 *  Archive Log:    Support for generic preferences: a new node (Preferences) in the UserOptions XML now allows to define groups of preferences (Section) and key/value pairs (Entry) that are stored in Properties objects are runtime.
 *  Archive Log:
 *  Archive Log:    Revision 1.62  2014/11/05 22:57:34  jijunwan
 *  Archive Log:    improved the stability of turning on/off refresh icon when we response to notice events
 *  Archive Log:
 *  Archive Log:    Revision 1.61  2014/11/05 19:05:13  fernande
 *  Archive Log:    Fixed an issue where defining a new subnet does not trigger a save topology task, causing notice processing to fail.
 *  Archive Log:
 *  Archive Log:    Revision 1.60  2014/11/05 16:17:43  jijunwan
 *  Archive Log:    fixed a potential bug
 *  Archive Log:
 *  Archive Log:    Revision 1.59  2014/10/22 01:24:39  jijunwan
 *  Archive Log:    moved TopologyView to view package
 *  Archive Log:
 *  Archive Log:    Revision 1.58  2014/10/21 16:36:45  fernande
 *  Archive Log:    Fixes for the shutdown sequence. Now there is a onWindowClose() method that does shutdown stuff on the EDT and cleanup gets invoked from the FabricPlugin in a non-EDT thread. Added NoticeEventListener to the shutdown process.
 *  Archive Log:
 *  Archive Log:    Revision 1.57  2014/10/14 20:50:31  jijunwan
 *  Archive Log:    improved to recreate Context when we connect to or refresh a subnet and the cached one is invalid
 *  Archive Log:
 *  Archive Log:    Revision 1.56  2014/10/14 11:32:11  jypak
 *  Archive Log:    UI updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.55  2014/10/09 16:32:52  fernande
 *  Archive Log:    Changed to allow overriding of subpages for unit testing.
 *  Archive Log:
 *  Archive Log:    Revision 1.54  2014/10/09 14:57:06  fernande
 *  Archive Log:    Changing saving of UserSettings to when application closes (cleanup())
 *  Archive Log:
 *  Archive Log:    Revision 1.53  2014/10/09 12:59:15  fernande
 *  Archive Log:    Changed the FabricController to use the UI framework and converted Swing workers into AbstractTasks to optimize the switching of contexts and the refreshing of pages. These processes still run under Swing workers, but now each setContext is run on its own Swing worker to improve performance. Also, changed the ProgressObserver mechanism to provide a more accurate progress.
 *  Archive Log:
 *  Archive Log:    Revision 1.51  2014/09/23 19:47:11  rjtierne
 *  Archive Log:    Integration of Gritty for Java Console
 *  Archive Log:
 *  Archive Log:    Revision 1.50  2014/09/18 14:57:42  jijunwan
 *  Archive Log:    supported jumpTo events
 *  Archive Log:
 *  Archive Log:    Revision 1.49  2014/09/15 15:24:26  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.48  2014/09/09 18:27:02  jijunwan
 *  Archive Log:    minor change - set Frame title to default and append subnet name at the end of subnet switching
 *  Archive Log:
 *  Archive Log:    Revision 1.47  2014/09/09 14:20:57  rjtierne
 *  Archive Log:    Restructured code to accommodate new console login dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.46  2014/09/04 21:09:33  rjtierne
 *  Archive Log:    Pass mainFrame window to the console page
 *  Archive Log:
 *  Archive Log:    Revision 1.45  2014/09/02 19:24:35  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.44  2014/09/02 19:04:13  jijunwan
 *  Archive Log:    added subnet name to frame title
 *  Archive Log:
 *  Archive Log:    Revision 1.43  2014/08/26 15:16:12  jijunwan
 *  Archive Log:    added refresh function to the framework
 *  Archive Log:
 *  Archive Log:    Revision 1.42  2014/08/22 19:54:20  rjtierne
 *  Archive Log:    Add new Console page
 *  Archive Log:
 *  Archive Log:    Revision 1.41  2014/08/19 18:14:20  jijunwan
 *  Archive Log:    introduced cleanup method to do cleanup before we shutdown an app
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2014/08/12 21:02:44  jijunwan
 *  Archive Log:    moved show dialogs to Util class, so in the future if we want to change L&F, we only need to change one place
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2014/07/21 16:29:34  jijunwan
 *  Archive Log:    minor adjustment on control logic
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2014/07/16 16:54:47  fernande
 *  Archive Log:    Adding Subnet > Connect To menu
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2014/07/11 19:23:23  fernande
 *  Archive Log:    Adding event bus and linking from UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2014/07/11 13:16:25  jypak
 *  Archive Log:    Added runtime, non runtime exceptions handler for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    As of now, all different exceptions are generally handled as 'Exception' but when we define how to handle differently for different exception, based on the error code, handler (catch block will be different). Also, we are thinking of centralized 'failure recovery' process to handle all exceptions in one place.
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2014/07/07 18:18:20  jijunwan
 *  Archive Log:    improved to handle switching subnet when one is still in the process of initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2014/06/26 15:49:12  jijunwan
 *  Archive Log:    performance improvement - share tree model among pages so we do not build model several times
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2014/06/26 15:00:16  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2014/06/23 15:15:41  fernande
 *  Archive Log:    Fix for switching subnets: tryConnect is now a transient connection.
 *  Archive Log:    Last accessed subnet is now saved to UserSettings
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2014/06/19 20:13:34  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2014/06/02 21:49:21  jijunwan
 *  Archive Log:    removed unused old style home page
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2014/05/30 21:59:08  jijunwan
 *  Archive Log:    moved all random generation to API side, and added a menu item to allow a user turn on/off randomization
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2014/05/29 14:25:05  jijunwan
 *  Archive Log:    jfreechart dataset is not thread safe, put all dataset related operation into EDT, so they will synchronize
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2014/05/28 17:25:44  jijunwan
 *  Archive Log:    color severity on event table, by default sort event table by time, by default show event table on home page, show text for enums
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2014/05/23 18:39:11  jypak
 *  Archive Log:    Logging Configuration updates:
 *  Archive Log:
 *  Archive Log:    DatabaseException, ConfigurationException are relayed to a user via pop up dialog.
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2014/05/16 04:35:04  jijunwan
 *  Archive Log:    commented out topology code
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2014/05/14 21:43:21  jypak
 *  Archive Log:    Event Summary Table updates.
 *  Archive Log:    1. Replace EventMsgBean with EventDescription.
 *  Archive Log:    2. Update table contents with real data from Notice API.
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2014/05/13 13:03:56  jypak
 *  Archive Log:    Event Summary Bar panel in pin board implementation.
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/05/08 19:25:36  jijunwan
 *  Archive Log:    MVC refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/05/07 20:20:31  fernande
 *  Archive Log:    Changes to save Subnets and EventRules to the database
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/05/01 15:59:15  rjtierne
 *  Archive Log:    References to PerformanceView have been changed
 *  Archive Log:    to PerformanceTreeView
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/04/30 17:29:22  jijunwan
 *  Archive Log:    rename ApiBroker to TaskScheduler; added pooled backgorund services to TaskScheduler so we can use it to run background tasks; improved to support schedule batched task and callbacks
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/04/30 14:58:09  rjtierne
 *  Archive Log:    Changes to reflect renamed IPage
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/04/23 16:32:36  jijunwan
 *  Archive Log:    temporarily change performance page's name and description for demo purpose
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/04/22 20:42:14  rjtierne
 *  Archive Log:    Removed references to RicksTestPage
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/04/22 17:25:20  jijunwan
 *  Archive Log:    checking subnet and update UI properly
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/04/21 22:18:38  jijunwan
 *  Archive Log:    subnet switching logic
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/04/21 15:43:07  jijunwan
 *  Archive Log:    Added #clear to be able to clear UI before we switch to another context. In the future, should change it to eventbus
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/04/20 03:46:15  jijunwan
 *  Archive Log:    added log
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/04/20 03:18:19  jijunwan
 *  Archive Log:    support context switch and do cleanup when we close GUI
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/04/19 20:44:24  jypak
 *  Archive Log:    Config API updates and null checks added.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/04/18 22:33:17  fernande
 *  Archive Log:    Changes to plug the Setup Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/04/18 15:59:23  jijunwan
 *  Archive Log:    better exception handling
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/04/17 16:59:48  jijunwan
 *  Archive Log:    integrate SetupWizard into main frame
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/04/17 14:41:37  rjtierne
 *  Archive Log:    RicksTestPage no longer takes parameter
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/16 16:20:43  jijunwan
 *  Archive Log:    minor refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/16 15:17:43  jijunwan
 *  Archive Log:    keep old style "Home Page", apply ApiBroker to update data
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/15 20:31:40  fernande
 *  Archive Log:    Changes to defer creation of APIs until a subnet is chosen
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:50:38  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/04/09 21:08:03  rjtierne
 *  Archive Log:    Changed the "Properties" package import name to "properties" to accommodate the upper case change.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/04/09 19:44:12  rjtierne
 *  Archive Log:    Now using the new FVMainFrame which includes
 *  Archive Log:    the event table and pin board
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/04/09 17:58:10  jincoope
 *  Archive Log:    Added page for viewing properties
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/08 17:33:00  jijunwan
 *  Archive Log:    introduced new summary section for "Home Page"
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/03/28 15:23:31  rjtierne
 *  Archive Log:    Added Rick's test page to the view
 *  Archive Log:
 *
 *  Overview: FabricController class creates initial view and installs pages on
 *  a tabbed pane for testing
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import static com.intel.stl.ui.common.UILabels.STL60008_CONN_LOST;
import static com.intel.stl.ui.common.UILabels.STL60009_PRESS_REFRESH;

import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import net.engio.mbassy.IPublicationErrorHandler;
import net.engio.mbassy.PublicationError;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.listener.Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.SubnetContext;
import com.intel.stl.api.SubnetEvent;
import com.intel.stl.api.configuration.AppInfo;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.admin.impl.AdminPage;
import com.intel.stl.ui.admin.view.AdminView;
import com.intel.stl.ui.alert.NoticeEventListener;
import com.intel.stl.ui.common.EventSummaryBarPanelController;
import com.intel.stl.ui.common.EventTableController;
import com.intel.stl.ui.common.IContextAware;
import com.intel.stl.ui.common.IEventSummaryBarListener;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.EventSummaryBarPanel;
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.event.NodeSelectedEvent;
import com.intel.stl.ui.event.PortSelectedEvent;
import com.intel.stl.ui.event.TaskStatusEvent;
import com.intel.stl.ui.framework.AbstractController;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.framework.ITask;
import com.intel.stl.ui.main.view.AboutDialog;
import com.intel.stl.ui.main.view.FabricView;
import com.intel.stl.ui.main.view.HomeView;
import com.intel.stl.ui.main.view.IFabricView;
import com.intel.stl.ui.main.view.IPageListener;
import com.intel.stl.ui.monitor.PerformancePage;
import com.intel.stl.ui.monitor.tree.FVTreeManager;
import com.intel.stl.ui.monitor.view.PerformanceTreeView;
import com.intel.stl.ui.network.GraphService;
import com.intel.stl.ui.network.OutlineService;
import com.intel.stl.ui.network.TopologyPage;
import com.intel.stl.ui.network.view.TopologyView;
import com.intel.stl.ui.publisher.TaskScheduler;

public class FabricController extends
        AbstractController<FabricModel, FabricView, FabricController> implements
        IFabricController, IPageListener, IEventSummaryBarListener,
        PropertyChangeListener {
    public static final String PROGRESS_AMOUNT_PROPERTY = "ProgressAmount";

    public static final String PROGRESS_NOTE_PROPERTY = "ProgressNote";

    private static Logger log = LoggerFactory.getLogger(FabricController.class);

    private IFabricView mainFrame;

    private final List<IPageController> pages =
            new CopyOnWriteArrayList<IPageController>();

    private int refreshCount;

    private final ISubnetManager subnetMgr;

    private final EventSummaryBarPanel eventSummaryBarPanel;

    private final EventSummaryBarPanelController eventSummaryBarPanelController;

    private final EventTableController eventTableController;

    private boolean hasEventTableToggled;

    private ITask backgroundTask;

    private final List<ITask> pendingTasks = Collections
            .synchronizedList(new ArrayList<ITask>());

    private final FVTreeManager builder;

    private final NoticeEventListener noticeListener;

    private int pageLoadWork;

    private int backgroundTotalWork;

    private double backgroundWork;

    private Rectangle lastBounds;

    private boolean maximized;

    private GraphService graphService;

    private OutlineService outlineService;

    // This name is set by the SubnetManager to manage this controller. It
    // should match the SubnetDescription in the model and in the Context.
    private String subnetName;

    private final HelpAction helpAction;

    public FabricController(String subnetName, FabricView view,
            ISubnetManager subnetMgr, MBassador<IAppEvent> eventBus) {
        super(new FabricModel(), view, eventBus);
        this.subnetMgr = subnetMgr;
        this.subnetName = subnetName;
        setupEventBus();
        this.builder = new FVTreeManager();
        noticeListener = new NoticeEventListener(eventBus);
        this.mainFrame = view.getView();

        eventSummaryBarPanel = mainFrame.getEventSummaryBarPanel();
        eventSummaryBarPanelController =
                new EventSummaryBarPanelController(eventSummaryBarPanel);
        eventSummaryBarPanelController.setEventSummaryBarListener(this);

        eventTableController = mainFrame.getEventTableController();

        helpAction = HelpAction.getInstance();
        helpAction.enableHelpKey(view.getMainFrame());
    }

    @Override
    public void applyHelpAction(ActionEvent e) {
        helpAction.actionPerformed(e);
    }

    protected void setupEventBus() {
        eventBus.addErrorHandler(new IPublicationErrorHandler() {
            @Override
            public void handleError(PublicationError error) {
                log.error(null, error);
                error.getCause().printStackTrace();
            }
        });
        eventBus.subscribe(this);
    }

    /**
     * Invoked by the FabricPlugin to initialize the UI
     */
    @Override
    public void init() {
        installPages();
    }

    /**
     * Add pages to <code>pages</code>
     */
    protected void installPages() {
        HomePage homePage = createHomePage();
        pages.add(homePage);

        PerformancePage perPage = createPerformancePage();
        pages.add(perPage);

        TopologyPage topologyPage = createTopologyPage();
        pages.add(topologyPage);

        AdminPage adminPage = createAdminPage();
        pages.add(adminPage);

        pageLoadWork = 0;
        for (IPageController page : pages) {
            pageLoadWork += page.getContextSwitchWeight().getWeight();
        }

    }

    // The following createXXX methods are overridden in unit tests
    protected HomePage createHomePage() {
        return new HomePage(new HomeView(), eventBus);
    }

    protected PerformancePage createPerformancePage() {
        return new PerformancePage(new PerformanceTreeView(), eventBus, builder);
    }

    protected TopologyPage createTopologyPage() {
        // These two services are being created here so that they can be shut
        // down when the connection is lost.
        graphService = new GraphService();
        outlineService = new OutlineService();
        TopologyView topologyView =
                new TopologyView(graphService, outlineService);
        return new TopologyPage(topologyView, eventBus, builder);
    }

    protected AdminPage createAdminPage() {
        Window owner =
                (mainFrame != null && (mainFrame instanceof Window)) ? (Window) mainFrame
                        : null;
        return new AdminPage(new AdminView((IFabricView) owner), eventBus);
    }

    protected NoticeEventListener getNoticeEventListener() {
        return noticeListener;
    }

    protected boolean isAddRandomValues() {
        return model.isAddRandomValues();
    }

    @Override
    public IFabricView getView() {
        return mainFrame;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.IFabricController#getCurrentSubnet()
     */
    @Override
    public SubnetDescription getCurrentSubnet() {
        return model.getCurrentSubnet();
    }

    @Override
    public Context getCurrentContext() {
        return super.getContext();
    }

    @Override
    public void initializeContext(Context context) {
        this.subnetName = context.getSubnetDescription().getName();
        checkBackgroundTask();
        context.setOwner(view.getMainFrame());
        // Show progress on UI
        mainFrame.setSubnetName(subnetName);
        mainFrame.setReady(false);
        mainFrame.showProgress(
                UILabels.STL10104_INIT_SUBNET.getDescription(subnetName), true);
        mainFrame.setProgress(0);
        // Prepare context
        // - apply random values for demo purpose
        boolean addRandomValues = model.isAddRandomValues();
        context.setRandom(addRandomValues);
        context.getPerformanceApi().setRandom(addRandomValues);
        // - set the notice listener
        context.getNoticeApi().addEventListener(noticeListener);

        List<IContextAware> contextPages = new ArrayList<IContextAware>();
        contextPages.add(builder);
        contextPages.addAll(pages);
        contextPages.add(eventSummaryBarPanelController);
        contextPages.add(eventTableController);
        backgroundTotalWork =
                builder.getContextSwitchWeight().getWeight()
                        + eventSummaryBarPanelController
                                .getContextSwitchWeight().getWeight()
                        + eventTableController.getContextSwitchWeight()
                                .getWeight();
        backgroundTotalWork += pageLoadWork;
        backgroundWork = 0.0;
        backgroundTask = new SubnetSwitchTask(model, context, contextPages);
        backgroundTask.addPropertyChangeListener(this);
        mainFrame.setTitle(STLConstants.K0001_FABRIC_VIEWER_TITLE.getValue());
        submitTask(backgroundTask);
    }

    @Override
    public void resetContext(Context newContext) {
        checkBackgroundTask();
        newContext.setOwner(view.getMainFrame());
        SubnetDescription subnet = newContext.getSubnetDescription();
        this.subnetName = subnet.getName();
        mainFrame.setSubnetName(subnetName);
        mainFrame.setReady(false);
        mainFrame.showProgress(
                UILabels.STL10104_INIT_SUBNET.getDescription(subnetName), true);
        mainFrame.setProgress(0);

        List<IContextAware> contextPages = new ArrayList<IContextAware>();
        contextPages.add(builder);
        contextPages.addAll(pages);
        contextPages.add(eventSummaryBarPanelController);
        contextPages.add(eventTableController);
        backgroundTotalWork =
                builder.getContextSwitchWeight().getWeight()
                        + eventSummaryBarPanelController
                                .getContextSwitchWeight().getWeight()
                        + eventTableController.getContextSwitchWeight()
                                .getWeight();
        backgroundTotalWork += pageLoadWork;
        backgroundWork = 0.0;
        backgroundTask = new SubnetSwitchTask(model, newContext, contextPages);
        backgroundTask.addPropertyChangeListener(this);
        mainFrame.setTitle(STLConstants.K0001_FABRIC_VIEWER_TITLE.getValue());
        submitTask(backgroundTask);
    }

    /**
     * 
     * <i>Description:</i>Once all page updates are done, reset the refresh
     * button.
     * 
     * @param evt
     */
    @Handler
    protected synchronized void onNoticeTaskStatus(TaskStatusEvent<?> evt) {
        if (evt.isStarted()) {
            refreshCount += 1;
        } else {
            refreshCount -= 1;
        }

        if (refreshCount == 1 && evt.isStarted()) {
            Util.runInEDT(new Runnable() {
                @Override
                public void run() {
                    mainFrame.setRefreshRunning(true);
                }
            });
        } else if (refreshCount == 0) {
            Util.runInEDT(new Runnable() {
                @Override
                public void run() {
                    mainFrame.setRefreshRunning(false);
                }
            });
        }
    }

    public synchronized void onRefresh() {
        checkBackgroundTask();

        Context context = getContext();
        if (context != null && context.isValid()) {
            mainFrame.setReady(false);
            mainFrame.showProgress(UILabels.STL10110_REFRESHING_PAGES
                    .getDescription(getCurrentSubnet().getName()), true);
            mainFrame.setProgress(0);

            backgroundTotalWork = pageLoadWork;
            backgroundWork = 0.0;
            backgroundTask = new SubnetRefreshTask(model, builder, pages);
            backgroundTask.addPropertyChangeListener(this);
            submitTask(backgroundTask);
        } else {
            selectSubnet(subnetName);
        }
    }

    private void checkBackgroundTask() {
        if (backgroundTask != null && !backgroundTask.isDone()) {
            backgroundTask.removePropertyChangeListener(this);
            try {
                backgroundTask.cancel(true);
            } catch (CancellationException ce) {
                // If the background task is actually running, here is where we
                // get the CancellationException; ignore it, since that's what
                // we want.
            }
        }
    }

    @Override
    public void onTaskSuccess() {
        // Since the backgroundTask can spawn multiple threads under this same
        // controller, we need to trigger a model changed event only when
        // backgroundTask has finished (and not when the sub threads finish).
        // So we override the triggering of the model change event here and do
        // it in the onTaskSuccess method of the backgroundTask
    }

    @Override
    public void onTaskFailure(Throwable caught) {
        // Same as in onTaskSucess()
    }

    /**
     * This method is invoked on the EDT by the backgroundTask
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PROGRESS_AMOUNT_PROPERTY == evt.getPropertyName()) {
            double progress = (Double) evt.getNewValue();
            backgroundWork = backgroundWork + progress;
            double percentProgress =
                    (backgroundWork / backgroundTotalWork) * 100;
            // System.out.println("=========== " + percentProgress);
            if (percentProgress > 100) {
                percentProgress = 100.00;
            }
            mainFrame.setProgress((int) percentProgress);
        } else if (PROGRESS_NOTE_PROPERTY == evt.getPropertyName()) {
            String note = (String) evt.getNewValue();
            if (mainFrame == null) {
                System.out.println("=========== " + note);
                return;
            }
            mainFrame.setProgressNote(note);
        }
    }

    @Override
    public void selectSubnet(final String subnetName) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            /*
             * (non-Javadoc)
             * 
             * @see javax.swing.SwingWorker#doInBackground()
             */
            @Override
            protected Void doInBackground() throws Exception {
                subnetMgr.selectSubnet(subnetName);
                return null;
            }

            /*
             * (non-Javadoc)
             * 
             * @see javax.swing.SwingWorker#done()
             */
            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                    Util.showError(mainFrame.getView(), e);
                }
            }

        };
        worker.execute();
    }

    @Override
    public void resetConnectMenu() {
        if (mainFrame != null) {
            mainFrame.resetConnectMenu();
        }
    }

    @Override
    public void resetSubnet(SubnetDescription subnet) {
        SubnetDescription currSubnet = model.getCurrentSubnet();
        this.subnetName = subnet.getName();
        if (currSubnet != null) {
            currSubnet.setName(subnetName);
        }
        Context context = getContext();
        if (context != null) {
            context.getSubnetDescription().setName(subnetName);
        }
        this.notifyModelChanged();
    }

    /**
     * 
     * Description: clear context for context switch TODO: need to make a
     * decision whether we should stop data collection on old context. Right
     * now, we stop it since we only consider one subnet
     * 
     * @param context
     */
    private void clearContext(Context context) {
        context.getTaskScheduler().clear();
        context.getNoticeApi().removeEventListener(noticeListener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.IFabricController#doShowInitScreen()
     */
    @Override
    public void doShowInitScreen(Rectangle bounds, boolean maximized) {
        if (mainFrame != null) {
            mainFrame.showInitScreen(bounds, maximized);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.hpc.stl.ui.IFabricController#doShowMessageAndExit(java.lang
     * .String)
     */
    @Override
    public void doShowMessageAndExit(final String message, final String title) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                if (mainFrame != null) {
                    mainFrame.showMessageAndExit(message, title);
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.hpc.stl.ui.IFabricController#doShowErrorsAndExit(java.util.
     * List)
     */
    @Override
    public void doShowErrors(final List<Throwable> errors) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                if (mainFrame != null) {
                    mainFrame.showErrors(errors);
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.IFabricController#doShowContent()
     */
    @Override
    public void doShowContent() {
        if (mainFrame != null) {
            mainFrame.showContent(pages);
        }
    }

    public List<SubnetDescription> getSubnets() {
        return subnetMgr.getSubnets();
    }

    @Override
    public void reset() {
        this.subnetName = null;
        try {
            resetView();
        } finally {
            init();
            eventBus.shutdown();
            eventBus = new MBassador<IAppEvent>(BusConfiguration.Default());
            Context context = getContext();
            if (context != null) {
                clearContext(context);
                setContext(null);
            }
        }
    }

    @Override
    public void doClose() {
        if (mainFrame != null) {
            mainFrame.close();
            mainFrame = null;
        }
    }

    @Override
    public void showSetupWizard(String subnetName) {
        subnetMgr.showSetupWizard(subnetName, this);
    }

    @Override
    public void addPendingTask(ITask task) {
        pendingTasks.add(task);
        if (mainFrame.isReady()) {
            processPendingTasks();
        }
    }

    public void processPendingTasks() {
        synchronized (pendingTasks) {
            Iterator<ITask> it = pendingTasks.iterator();
            while (it.hasNext()) {
                submitTask(it.next());
                it.remove();
            }
        }
    }

    /**
     * 
     * <i>Description:</i> invoked by the view when the UI is closed (running on
     * the EDT).
     * 
     */
    public void onWindowClose() {
        resetView(true);
    }

    public void onMenuClose() {
        resetView(false);
    }

    private void resetView(boolean forceWindowClose) {
        try {
            resetView();
        } finally {
            // SubnetManager starts a thread on its own
            subnetMgr.stopSubnet(subnetName, forceWindowClose);
        }
    }

    private void resetView() {
        try {
            graphService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            outlineService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (IPageController page : pages) {
            page.cleanup();
        }
        pages.clear();

        try {
            model.setCurrentSubnet(null);
            notifyModelChanged();
        } finally {
            lastBounds = mainFrame.getFrameBounds();
            maximized = mainFrame.isFrameMaximized();
        }
    }

    @Override
    public void bringToFront() {
        if (mainFrame != null) {
            Util.runInEDT(new Runnable() {
                @Override
                public void run() {
                    mainFrame.bringToFront();
                }
            });
        }
    }

    @Override
    public Rectangle getBounds() {
        if (mainFrame == null) {
            return lastBounds;
        }
        return mainFrame.getFrameBounds();
    }

    @Override
    public boolean isMaximized() {
        if (mainFrame == null) {
            return maximized;
        }
        return mainFrame.isFrameMaximized();
    }

    /**
     * <i>Description:</i> invoked by the Subnet Manager when stopping a subnet
     * (running on a non-EDT thread).
     */
    @Override
    public void cleanup() {
        this.subnetName = null;
        eventBus.shutdown();
        noticeListener.shutdown();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IPageListener#canPageChange(int, int)
     */
    @Override
    public boolean canPageChange(int oldPageId, int newPageId) {
        if (oldPageId >= 0) {
            IPageController oldPage = pages.get(oldPageId);
            return oldPage.canExit();
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IPageListener#onPageChanged(int, int)
     */
    @Override
    public void onPageChanged(int oldPageId, int newPageId) {
        if (oldPageId >= 0) {
            if (oldPageId == 0 && !hasEventTableToggled) {
                mainFrame.hideEventSummaryTable();
            }
            IPageController oldPage = pages.get(oldPageId);
            oldPage.onExit();
        }
        if (newPageId >= 0) {
            if (newPageId == 0 && !hasEventTableToggled) {
                mainFrame.showEventSummaryTable();
            }
            IPageController newPage = pages.get(newPageId);
            newPage.onEnter();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.IEventSummaryBarListener#showEventSummaryTable()
     */
    @Override
    public void toggleEventSummaryTable() {
        mainFrame.toggleEventSummaryTable();
        if (!hasEventTableToggled) {
            hasEventTableToggled = true;
        }
    }

    /**
     * Description:
     * 
     * @param selected
     */
    public void applyRandomValue(boolean selected) {
        model.setAddRandomValues(selected);
        Context context = getContext();
        if (context != null) {
            // apply random values for demo purpose
            context.setRandom(selected);
            context.getPerformanceApi().setRandom(selected);
        }
    }

    public void startSimulatedFailover() {
        Context context = getContext();
        if (context != null && model.getCurrentSubnet() != null) {
            // Start a simulated failover
            SubnetDescription subnet = model.getCurrentSubnet();
            context.getConfigurationApi().startSimulatedFailover(
                    subnet.getName());
        }
    }

    @Handler
    protected void onNodeSelected(NodeSelectedEvent event) {
        jumpToDestination(event.getDestination());
    }

    @Handler
    protected void onPortSelected(PortSelectedEvent event) {
        jumpToDestination(event.getDestination());
    }

    protected void jumpToDestination(JumpDestination dest) {
        switch (dest) {
            case PERFORMANCE:
                mainFrame.setCurrentTab(pages.get(1));
                break;
            case TOPOLOGY:
                mainFrame.setCurrentTab(pages.get(2));
                break;
            default:
                log.warn("Unsupported destination " + dest);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.framework.AbstractController#initModel()
     */
    @Override
    public void initModel() {
    }

    // For testing

    protected List<IPageController> getPages() {
        return pages;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.IFabricController#getTaskScheduler()
     */
    @Override
    public TaskScheduler getTaskScheduler() {

        Context context = getContext();
        TaskScheduler taskScheduler = null;

        if (context != null) {
            taskScheduler = context.getTaskScheduler();
        }

        return taskScheduler;
    }

    @Override
    public void onSubnetManagerConnectionLost(SubnetEvent event) {
        SubnetContext subnetCtx = (SubnetContext) event.getSource();
        SubnetDescription subnet = subnetCtx.getSubnetDescription();
        backgroundWork = 0.0;
        if (subnet != null) {
            backgroundTotalWork = subnet.getFEList().size();
        }
        subnetCtx.addFailoverProgressListener(this);
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                mainFrame.setReady(false);
                mainFrame.setProgressNote("");
                mainFrame.showProgress(
                        UILabels.STL10113_CONNECTION_LOST.getDescription(),
                        true);
                mainFrame.setProgress(0);
            }
        });
    }

    @Override
    public void onFailoverCompleted(SubnetEvent event) {
        Context context = getContext();
        if (context != null) {
            context.removeFailoverProgressListener(this);
            TaskScheduler ts = context.getTaskScheduler();
            ts.updateRefreshRate(ts.getRefreshRate());
        }
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                mainFrame.showProgress(null, false);
                mainFrame.setReady(true);
            }
        });
    }

    @Override
    public void onFailoverFailed(SubnetEvent event) {
        Context context = getContext();
        if (context != null) {
            context.removeFailoverProgressListener(this);
        }
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                if (mainFrame != null) {
                    mainFrame.showProgress(null, false);
                    mainFrame.setReady(true);
                    mainFrame.showMessage(
                            STL60009_PRESS_REFRESH.getDescription(),
                            STL60008_CONN_LOST.getDescription());
                }
            }
        });
    }

    @Override
    public void onSubnetManagerConnected(SubnetEvent event) {
        // Nothing to do here
    }

    public void showAboutDialog() {
        if (subnetMgr != null) {
            AppInfo appInfo = subnetMgr.getConfigurationApi().getAppInfo();
            String appVersion =
                    appInfo.getAppVersion() + "." + appInfo.getAppRelease()
                            + "." + appInfo.getAppModLevel();
            AboutDialog.showAboutDialog((javax.swing.JFrame) mainFrame,
                    appInfo.getAppName(), appVersion, appInfo.getAppBuildId(),
                    appInfo.getAppBuildDate());
        }
    }

}
