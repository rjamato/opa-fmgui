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
 *  File Name: SubnetManager.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.64  2015/10/29 12:11:43  robertja
 *  Archive Log:    PR 131014 MailNotifier is now updated if user changes events or recipients in wizard after start-up.
 *  Archive Log:
 *  Archive Log:    Revision 1.63  2015/08/17 18:53:38  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.62  2015/08/10 23:05:49  fernande
 *  Archive Log:    PR 129853 - FM GUI fails to install on SLES12. Fixing unit tests for SubnetManager
 *  Archive Log:
 *  Archive Log:    Revision 1.61  2015/08/10 22:41:18  fernande
 *  Archive Log:    PR 129853 - FM GUI fails to install on SLES12. Adding unit tests for SubnetManager
 *  Archive Log:
 *  Archive Log:    Revision 1.60  2015/08/10 17:30:41  robertja
 *  Archive Log:    PR 128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.59  2015/07/30 19:34:33  fernande
 *  Archive Log:    PR 129592 - removing a subnet a user is monitoring cause internal DB exception. Added flag to SubnetContext indicating the subnet has been deleted. If the flag is set, no saving of subnet information occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.58  2015/07/10 20:50:13  fernande
 *  Archive Log:    PR 129522 - Notice is not written to database due to topology not found. Moved FE Helpers to the session object and changed the order of initialization for the SubnetContext.
 *  Archive Log:
 *  Archive Log:    Revision 1.57  2015/07/09 17:58:40  jijunwan
 *  Archive Log:    PR 129509 - Shall refresh UI after failover completed
 *  Archive Log:    - reset ManagementApi after failover completed
 *  Archive Log:    - refresh UI after failover completed
 *  Archive Log:    - updated comments
 *  Archive Log:
 *  Archive Log:    Revision 1.56  2015/06/22 13:11:55  jypak
 *  Archive Log:    PR 128980 - Be able to search devices by name or lid.
 *  Archive Log:    New feature added to enable search devices by name, lid or node guid. The search results are displayed as a tree and when a result node from the tree is selected, original tree is expanded and the corresponding node is highlighted.
 *  Archive Log:
 *  Archive Log:    Revision 1.55  2015/06/18 21:10:58  fernande
 *  Archive Log:    PR 128977 Application log needs to support multi-subnet. - Adding support for Logback's Mapped Diagnostic Context
 *  Archive Log:
 *  Archive Log:    Revision 1.54  2015/06/10 21:07:17  jijunwan
 *  Archive Log:    PR 129120 - Some old files have no proper file header. They cannot record change logs
 *  Archive Log:    - manual correction on files that our tool cannot  identify
 *  Archive Log:
 *  Archive Log:    Revision 1.53  2015/06/10 19:24:48  rjtierne
 *  Archive Log:    PR 128975 - Can not setup application log
 *  Archive Log:    Changed references of List<AppenderConfig> to LoggingConfiguration
 *  Archive Log:
 *  Archive Log:    Revision 1.52  2015/05/29 20:43:46  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Second wave of changes: the application can be switched between the old adapter and the new; moved out several initialization pieces out of objects constructor to allow subnet initialization with a UI in place; improved generics definitions for FV commands.
 *  Archive Log:
 *  Archive Log:    Revision 1.51  2015/05/26 15:53:23  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. A new FEAdapter is being added to handle requests through SubnetRequestDispatchers, which manage state for each connection to a subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.50  2015/05/12 17:41:03  rjtierne
 *  Archive Log:    PR 128624 - Klocwork and FindBugs fixes for UI
 *  Archive Log:    In background task of StartSubnetsTask(), local variable lastController is a assigned
 *  Archive Log:    a value and never used
 *  Archive Log:
 *  Archive Log:    Revision 1.49  2015/05/11 12:35:16  rjtierne
 *  Archive Log:    Removed MULTINET constant and condition to invoke the SetupWizardView and
 *  Archive Log:    SetupWizardController. These classes are no longer in use and have been removed from
 *  Archive Log:    the project.
 *  Archive Log:
 *  Archive Log:    Revision 1.48  2015/05/01 21:29:06  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.47  2015/04/29 19:14:03  rjtierne
 *  Archive Log:    In saveSubnet(), handle the case to update the "Connect To" menu in the default window.
 *  Archive Log:
 *  Archive Log:    Revision 1.46  2015/04/28 22:08:56  jijunwan
 *  Archive Log:    removed title argument from #showErrorMessage
 *  Archive Log:
 *  Archive Log:    Revision 1.45  2015/04/28 19:54:40  fernande
 *  Archive Log:    Fixed issue where new subnet opens in a new window even though an empty frame is available.
 *  Archive Log:
 *  Archive Log:    Revision 1.44  2015/04/27 21:47:27  rjtierne
 *  Archive Log:    - No longer calling setUserSettings in saveUserSettings so subnet context isn't overwritten
 *  Archive Log:    - Suppressed warnings since restartViewer is not being used
 *  Archive Log:
 *  Archive Log:    Revision 1.43  2015/04/22 22:31:54  fisherma
 *  Archive Log:    Removing html tags from error messages.
 *  Archive Log:
 *  Archive Log:    Revision 1.42  2015/04/16 17:44:52  fernande
 *  Archive Log:    Fixed several issues where no context was found for a subnet, resulting in bad UI behaviour.
 *  Archive Log:
 *  Archive Log:    Revision 1.41  2015/04/15 18:51:16  fernande
 *  Archive Log:    Improved handling of TimeoutExceptions during failover: TaskScheduler is shutdown during failover and timeouts during failover do not trigger an additional failover process. Refresh functionality has been fixed
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2015/04/15 17:05:52  rjtierne
 *  Archive Log:    Update subnet context with the latest userSettings in saveUserSettings().
 *  Archive Log:    This fixes the problem with Event Wizard not saving user settings consistently.
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2015/04/08 20:32:21  fernande
 *  Archive Log:    Adding support for Timeout exception going to failover processing
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2015/04/08 15:20:39  fernande
 *  Archive Log:    Changes to allow for failover to work when the current (initial) FE is not available.
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2015/04/03 14:31:43  rjtierne
 *  Archive Log:    PR 127079 - always auto connects to FM. Changed getSubnetsToStart() so only auto-connect subnets start at initialization.
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2015/04/02 13:32:54  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2015/03/31 19:54:54  fisherma
 *  Archive Log:    Added method to get app info for the about dialog.
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2015/03/31 17:47:24  rjtierne
 *  Archive Log:    - Added/implemented methods getHostIp() and isHostConnectable()
 *  Archive Log:    - Removed call to checkConnectivity() in startSubnet()
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/03/30 22:36:12  jijunwan
 *  Archive Log:    improved AboutDialog
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2015/03/30 15:10:44  rjtierne
 *  Archive Log:    Added protection against NullPointerException if subnet is deleted when not running
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/03/27 20:50:03  fernande
 *  Archive Log:    Adding support for failover
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/03/26 13:55:27  fisherma
 *  Archive Log:    About Dialog:  Add application name as parameter to the dialog's title.  Updated third party licenses html file.  Added code to display third party tools and licenses information in the About dialog.
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/03/25 17:56:18  rjtierne
 *  Archive Log:    Added maps to keep track of secure certificate files for each subnet
 *  Archive Log:    and clear key factories at appropriate times
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/03/24 15:14:21  fernande
 *  Archive Log:    Changes to cache KeyManagerFactories and TrustManagerFactories to avoid requests for password.
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/03/16 17:44:56  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/03/02 22:34:33  fernande
 *  Archive Log:    Fixes to handle a change in subnet name; if subnet name is changed, the model in FabricController is now updated as well as the name in the SubnetDescription in the subnet context to that the view and the back end are now in sync.
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/02/26 22:50:56  rjtierne
 *  Archive Log:    Added method isHostReachable() to test for the existence of a host
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/02/26 22:10:27  fernande
 *  Archive Log:    Fix to refresh UserSettings in SubnetContext after it's updated by the Setup Wizard. Added pending tasks, which for now are only messages that should be displayed after the subnet is initialized. These tasks run after the initialization popup is hidden.
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/02/26 16:20:07  fernande
 *  Archive Log:    Fixes NPE when there are no subnets opened and the subnetmanager tries to save screen state to the database. Changed showSetupWizard so that the wizard can show its view centered on the calling frame.
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/02/25 22:05:49  fernande
 *  Archive Log:    Fixes for remove subnet function in the Setup Wizard.
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/02/25 14:32:19  fernande
 *  Archive Log:    Fix to use the current frame position if it is an empty frame. otherwise open a new frame with the previous saved position.
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/02/24 14:47:19  fernande
 *  Archive Log:    Changes to the UI to display only subnet with the Autoconnect option at startup. If no subnet is defined as Autoconnect, then a blank screen is shown.
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/02/20 22:30:17  fernande
 *  Archive Log:    Fix to remember the last position on screen for a subnet so that if it is reopened within the same session, it is opened in the same location
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/02/20 17:00:05  fernande
 *  Archive Log:    Fix where application wouldn't start the first time and show the setup wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/02/19 21:42:17  fernande
 *  Archive Log:    Adding support to restore viewers to their previous screen state (Maximized/Screen location) and to start all subnets set to AutoConnect. If none is found, the last subnet is started.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/02/16 05:18:19  jijunwan
 *  Archive Log:    PR 127076 - assorted FV errors observed
 *  Archive Log:     - introduced frame visibility and name check to ensure application will shutdown even if we have uncaptured errors
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/02/13 21:28:28  rjtierne
 *  Archive Log:    Multinet Wizard: Added MULTINET flag to switch between old and
 *  Archive Log:    new wizard; will be removed later.  Also, wizards are now singletons
 *  Archive Log:    and can no longer be instantiated
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/02/12 22:16:35  fernande
 *  Archive Log:    Fix to use isFirstRun from application
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/02/12 22:11:01  fernande
 *  Archive Log:    Changes to key viewers and contexts using Host_IP_Address+Port so that there is one subnet per viewer
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/02/10 23:06:45  jijunwan
 *  Archive Log:    1) changed to use consistent user name
 *  Archive Log:    2) added null check
 *  Archive Log:    3) implemented getTaskScheduler
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/02/10 19:34:41  fernande
 *  Archive Log:    Changes to handle SubnetConnectionExceptions
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/09 22:07:51  jijunwan
 *  Archive Log:    temporary solution that enables us delivery with the following rules:
 *  Archive Log:    1) use subnet name as the "id" for subnet definition
 *  Archive Log:    2) new subnet name will create a new subnet no matter where we open the setup wizard
 *  Archive Log:    3) subnet definition with existed name will cause update on the existed subnet
 *  Archive Log:    4) Any fabric controller/viewer only combine with one subnet. define a new subnet, or update a subnet not opened yet, will lead to a new fabric farme
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/06 17:35:56  fernande
 *  Archive Log:    Fixed UserSettings not being saved off to the database in the SetupWizard
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/06 15:51:45  fernande
 *  Archive Log:    Fix for showSetupWizard where no subnet is passed
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/06 15:10:16  fernande
 *  Archive Log:    Changes to use the new subnetId for SubnetDescription instead of the subnet name
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/02 20:36:35  fernande
 *  Archive Log:    Fixing the SetupWizard so that it can define new subnets. Fixed also StackOverflowError exception when switching subnets.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/30 20:59:07  fernande
 *  Archive Log:    Changed default operation on window close
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/30 20:27:16  fernande
 *  Archive Log:    Initial changes to support multiple fabric viewers
 *  Archive Log:
 *
 *  Overview:
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import static com.intel.stl.api.configuration.AppInfo.PROPERTIES_SUBNET_FRAMES;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.SwingWorker;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;
import net.engio.mbassy.bus.error.PublicationError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.intel.stl.api.AppContext;
import com.intel.stl.api.ICertsAssistant;
import com.intel.stl.api.ISubnetEventListener;
import com.intel.stl.api.SubnetContext;
import com.intel.stl.api.SubnetEvent;
import com.intel.stl.api.Utils;
import com.intel.stl.api.configuration.AppInfo;
import com.intel.stl.api.configuration.ConfigurationException;
import com.intel.stl.api.configuration.IConfigurationApi;
import com.intel.stl.api.configuration.LoggingConfiguration;
import com.intel.stl.api.configuration.UserNotFoundException;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetConnectionException;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.SubnetDescription.Status;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.FVMainFrame;
import com.intel.stl.ui.main.view.FabricView;
import com.intel.stl.ui.main.view.IFabricView;
import com.intel.stl.ui.main.view.SplashScreen;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.intel.stl.ui.wizards.impl.IWizardListener;
import com.intel.stl.ui.wizards.impl.MultinetWizardController;

public class SubnetManager implements ISubnetManager, ISubnetEventListener {
    private static Logger log = LoggerFactory.getLogger(SubnetManager.class);

    private final static String PROPERTIES_SUBNET_STATE_SUFFIX = "-State";

    private final static double DEFAULT_SCREEN_SIZE_PERCENTAGE = 0.8;

    private static final int FRAME_OFFSET = 20;

    private static final String SM_THREAD_PREFIX = "smthread-";

    private final AtomicInteger threadCount = new AtomicInteger(1);

    private final String userName = "defaultuser";

    private final AppContext appContext;

    private final ICertsAssistant certsAssistant;

    protected final GraphicsDevice[] device;

    private final GraphicsDevice defaultDevice;

    private Rectangle lastBounds;

    protected boolean isFirstRun;

    protected LinkedHashMap<Long, SubnetDescription> subnets;

    // This lock is used whenever the controllers and contexts maps are read or
    // written
    private final Object tablesLock = new Object();

    protected final Map<SubnetDescription, Context> contexts =
            new HashMap<SubnetDescription, Context>();

    protected final Map<SubnetDescription, Rectangle> bounds =
            new HashMap<SubnetDescription, Rectangle>();

    protected final Map<SubnetDescription, Boolean> windowStates =
            new HashMap<SubnetDescription, Boolean>();

    protected final Map<String, Rectangle> savedBounds =
            new HashMap<String, Rectangle>();

    protected final Map<String, Boolean> savedStates =
            new HashMap<String, Boolean>();

    protected final AtomicReference<IFabricController> lastViewer =
            new AtomicReference<IFabricController>(null);

    public SubnetManager(AppContext appContext,
            ICertsAssistant certsAssistant) {
        this.appContext = appContext;
        this.certsAssistant = certsAssistant;
        GraphicsEnvironment ge = getLocalGraphicsEnvironment();
        device = ge.getScreenDevices();
        defaultDevice = ge.getDefaultScreenDevice();
    }

    @Override
    public void init(boolean isFirstRun) {
        this.isFirstRun = isFirstRun;
        loadSubnets();
        loadFrameStates();
    }

    /**
     * Adds a subnet to the set of managed subnets and saves it to the database.
     * This method should be invoked from the EDT
     * 
     * @throws SubnetDataNotFoundException
     */
    @Override
    public SubnetDescription saveSubnet(SubnetDescription subnet)
            throws SubnetDataNotFoundException {
        if (subnet == null) {
            log.error("Attempting to save a null subnet");
            return null;
        }
        log.debug("saveSubnet {}", subnet);
        IConfigurationApi confApi = appContext.getConfigurationApi();
        if (subnet.getSubnetId() == 0) {
            // New subnet definition
            subnet.setLastStatus(Status.INVALID);
            SubnetDescription savedSubnet = confApi.defineSubnet(subnet);
            subnets.put(savedSubnet.getSubnetId(), savedSubnet);
            log.debug("New subnet: ", savedSubnet);
            resetConnectMenus();
            return savedSubnet;
        } else {
            Context context = contexts.get(subnet);
            if (context != null) {
                SubnetDescription ctxSubnet = context.getSubnetDescription();
                if (ctxSubnet.getSubnetId() == subnet.getSubnetId()
                        && !ctxSubnet.getName()
                                .equalsIgnoreCase(subnet.getName())) {
                    IFabricController controller = context.getController();
                    if (controller != null) {
                        controller.resetSubnet(subnet);
                    }
                    resetConnectMenus();
                }
            } else if (lastViewer.get() != null) {
                lastViewer.get().resetConnectMenu();
            }
            confApi.updateSubnet(subnet);
            return subnet;
        }
    }

    /**
     * Removes a subnet from the set of managed subnets and from the database.
     * This method should be invoked from the EDT
     * 
     * @throws SubnetDataNotFoundException
     */
    @Override
    public void removeSubnet(SubnetDescription subnet)
            throws SubnetDataNotFoundException {
        log.debug("removeSubnet {}; subnets = {}", subnet, subnets.size());

        IConfigurationApi confApi = appContext.getConfigurationApi();
        long subnetId = subnet.getSubnetId();
        Context context = contexts.get(subnet);
        if (context != null) {
            context.setDeleted(true);
        }
        removeHost(subnet, false);
        confApi.removeSubnet(subnetId);
        subnets.remove(subnetId);
        resetConnectMenus();
    }

    private void resetConnectMenus() {
        for (Context context : contexts.values()) {
            IFabricController controller = context.getController();
            controller.resetConnectMenu();
        }
        if (lastViewer.get() != null) {
            lastViewer.get().resetConnectMenu();
        }
    }

    /**
     * Saves the userSettings for the specified subnet. This method should be
     * invoked from the EDT.
     */
    @Override
    public void saveUserSettings(String subnetName, UserSettings userSettings) {
        SubnetDescription subnet = getSubnet(subnetName);
        appContext.getConfigurationApi().saveUserSettings(subnetName,
                userSettings);
        Context context = contexts.get(subnet);
        if (context != null) {
            context.refreshUserSettings();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.ISubnetManager#getHostIp(java.lang.String)
     */
    @Override
    public String getHostIp(String hostName) throws SubnetConnectionException {
        return appContext.getConfigurationApi().getHostIp(hostName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.ISubnetManager#isReachable(java.lang.String)
     */
    @Override
    public boolean isHostReachable(String hostName) {
        return appContext.getConfigurationApi().isHostReachable(hostName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.ISubnetManager#isConnectable(com.intel.stl.api.
     * subnet.SubnetDescription)
     */
    @Override
    public boolean isHostConnectable(SubnetDescription subnet)
            throws ConfigurationException {

        return appContext.getConfigurationApi().isHostConnectable(subnet);
    }

    @Override
    public boolean tryToConnect(SubnetDescription subnet)
            throws SubnetConnectionException {
        return appContext.getConfigurationApi().tryToConnect(subnet);
    }

    @Override
    public PMConfigBean getPMConfig(SubnetDescription subnet) {
        return appContext.getConfigurationApi().getPMConfig(subnet);
    }

    @Override
    public SubnetDescription getNewSubnet() {
        SubnetDescription newSubnet = new SubnetDescription();
        newSubnet.setSubnetId(0L);
        return newSubnet;
    }

    /**
     * Starts a FabricViewer for the specified subnet, if not already started.
     * This method is intended to be called from the EDT and therefore it's not
     * synchronized
     */
    @Override
    public void startSubnet(String subnetName)
            throws SubnetConnectionException {
        SubnetDescription subnet = getSubnet(subnetName);
        startSubnet(subnet);
    }

    @Override
    public void stopSubnet(String subnetName, boolean forceWindowClose) {
        log.debug("Stopping subnet '{}'. Existing contexts = {}", subnetName,
                contexts.size());
        try {
            if (subnetName != null) {
                SubnetDescription subnet = getSubnet(subnetName);
                removeHost(subnet, forceWindowClose);
            }
        } finally {
            if (subnetName == null) {
                if (contexts.size() == 0 || !hasRunningSubnetFrames()) {
                    saveFrameStates();
                    shutdownApplication();
                }
            }
        }
    }

    protected boolean hasRunningSubnetFrames() {
        Frame[] frames = Frame.getFrames();
        for (Frame frame : frames) {
            if (frame.isVisible() && frame.getName().startsWith(
                    STLConstants.K0001_FABRIC_VIEWER_TITLE.getValue())) {
                return true;
            }
        }
        log.debug("No visible subnet frames!");
        return false;
    }

    protected void shutdownApplication() {
        startNewThread(new Runnable() {

            @Override
            public void run() {
                appContext.shutdown();
            }

        });
    }

    /**
     * Selects the specified subnet to display. If a fabric viewer is already
     * showing it, focus is given to the window; if no fabric viewer is
     * available, it's started. This method is supposed to be invoked from the
     * EDT
     */
    @Override
    public void selectSubnet(final String subnetName)
            throws SubnetConnectionException {
        startSubnet(subnetName);
    }

    @Override
    public List<SubnetDescription> getSubnets() {
        return Collections.unmodifiableList(
                new ArrayList<SubnetDescription>(subnets.values()));
    }

    @Override
    public boolean isFirstRun() {
        return isFirstRun;
    }

    @Override
    public UserSettings getUserSettings(String subnetName, String userName)
            throws UserNotFoundException {
        return appContext.getConfigurationApi().getUserSettings(subnetName,
                userName);
    }

    /**
     * Gets the TaskScheduler associated with the specified subnet. If not
     * context has been created, it returns null
     */
    @Override
    public TaskScheduler getTaskScheduler(SubnetDescription subnet) {
        Context context = contexts.get(subnet);
        if (context == null) {
            return null;
        }
        return context.getTaskScheduler();
    }

    @Override
    public void saveLoggingConfiguration(LoggingConfiguration loggingConfig) {
        appContext.getConfigurationApi()
                .saveLoggingConfiguration(loggingConfig);
    }

    @Override
    public LoggingConfiguration getLoggingConfig() {
        return appContext.getConfigurationApi().getLoggingConfig();
    }

    @Override
    public synchronized void startSubnets(SplashScreen splashScreen) {
        final AtomicBoolean splashClosed = new AtomicBoolean(false);
        List<SubnetDescription> toStart = getSubnetsToStart();

        if (toStart.size() == 0 && !isFirstRun) {
            final IFabricController controller = createFabricController(null);
            final Rectangle bounds = getBounds();
            lastViewer.set(controller);
            Util.runInEDT(new Runnable() {
                @Override
                public void run() {
                    controller.doShowInitScreen(bounds, false);
                }
            });
        } else {
            StartSubnetsTask startSubnetsTask =
                    new StartSubnetsTask(toStart, splashScreen);
            startSubnetsTask.execute();
            try {
                startSubnetsTask.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (isFirstRun) {
            final IFabricController controller = createFabricController(null);
            final Rectangle bounds = getBounds();
            lastViewer.set(controller);
            Util.runInEDT(new Runnable() {
                @Override
                public void run() {
                    controller.doShowInitScreen(bounds, false);
                    controller.showSetupWizard(null);
                    isFirstRun = false;
                }
            });
        }
        if (!splashClosed.get()) {
            splashScreen.close();
        }
    }

    @Override
    public void showSetupWizard(String subnetName,
            IFabricController controller) {

        IFabricView mainFrame = controller.getView();
        IWizardListener wizardController;

        wizardController =
                MultinetWizardController.getInstance(mainFrame, this);
        SubnetDescription subnet = null;
        if (subnetName != null && subnetName.length() > 0) {
            subnet = getSubnet(subnetName);
        }
        if (subnet == null) {
            subnet = new SubnetDescription();
        }
        wizardController.showView(subnet, userName, controller);
    }

    @Override
    public void cleanup() {
        for (Context context : contexts.values()) {
            IFabricController controller = null;
            try {
                context.cleanup();
                controller = context.getController();
                if (controller != null) {
                    controller.cleanup();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (controller != null) {
                    closeViewer(controller);
                }
            }
        }
        contexts.clear();
        subnets.clear();
    }

    @Override
    public void clearSubnetFactories(SubnetDescription subnet) {
        certsAssistant.clearSubnetFactories(subnet);
    }

    @Override
    public IConfigurationApi getConfigurationApi() {
        return appContext.getConfigurationApi();
    }

    @Override
    public void onFailoverCompleted(SubnetEvent event) {
        // Nothing to do
    }

    @Override
    public void onFailoverFailed(SubnetEvent event) {
        // Nothing to do
    }

    @Override
    public void onSubnetManagerConnected(SubnetEvent event) {
        SubnetDescription subnet = (SubnetDescription) event.getSource();
        VerifySubnetsTask verifyTask = new VerifySubnetsTask(subnet);
        verifyTask.execute();
    }

    @Override
    public void onSubnetManagerConnectionLost(SubnetEvent event) {
        // Nothing to do
    }

    protected Context createContext(SubnetDescription subnet,
            IFabricController controller) throws Exception {
        try {
            SubnetContext subnetCtx =
                    appContext.getSubnetContextFor(subnet, true);
            subnetCtx.addSubnetEventListener(this);
            return new Context(subnetCtx, controller, userName);
        } catch (Exception e) {
            log.error("Error creating Context for subnet '{}'",
                    subnet.getName(), e);
            throw e;
        }
    }

    private void removeHost(SubnetDescription subnet,
            boolean forceWindowClose) {
        IFabricController controller = null;
        Context context;
        synchronized (tablesLock) {
            context = contexts.remove(subnet);
            if (context != null) {
                controller = context.getController();
            }

            if (controller != null) {
                Rectangle frameBounds = controller.getBounds();
                boolean maximized = controller.isMaximized();
                if (frameBounds != null) {
                    bounds.put(subnet, frameBounds);
                    savedBounds.put(subnet.getName(), frameBounds);
                    windowStates.put(subnet, maximized);
                }
            }
        }
        if (context != null) {
            stopSubnet(controller, context, forceWindowClose);
        }
    }

    private void startSubnet(final SubnetDescription subnet)
            throws SubnetConnectionException {

        List<SubnetDescription> toStart = new ArrayList<SubnetDescription>(1);
        toStart.add(subnet);
        StartSubnetsTask startSubnetsTask = new StartSubnetsTask(toStart, null);
        startSubnetsTask.execute();
    }

    private List<SubnetDescription> getSubnetsToStart() {
        List<SubnetDescription> startSubnets =
                new ArrayList<SubnetDescription>(subnets.values());

        List<SubnetDescription> autoConnectSubnets =
                new ArrayList<SubnetDescription>();

        // sort in ascending order by status timestamp
        Collections.sort(startSubnets, new Comparator<SubnetDescription>() {
            @Override
            public int compare(SubnetDescription o1, SubnetDescription o2) {
                return Long.compare(o1.getStatusTimestamp(),
                        o2.getStatusTimestamp());
            }
        });
        // only keep autoconnect subnets
        for (SubnetDescription subnet : subnets.values()) {
            if (subnet.isAutoConnect()) {
                autoConnectSubnets.add(subnet);
            }
        }
        return autoConnectSubnets;
    }

    private void showInitialFrame(String subnetName,
            IFabricController controller) {
        Rectangle bounds = getBounds(subnetName);
        boolean maximized = getMaximized(subnetName);
        showInitScreen(controller, bounds, maximized);
    }

    private void showInitScreen(final IFabricController controller,
            final Rectangle bounds, final boolean maximized) {
        if (controller == null) {
            return;
        }
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                controller.doShowInitScreen(bounds, maximized);
            }
        });
    }

    private Rectangle getBounds(String subnetName) {
        if (subnetName == null) {
            return getBounds();
        }
        Rectangle bounds = savedBounds.get(subnetName);
        if (bounds != null) {
            if (!boundsDisplayable(bounds)) {
                bounds = getBounds();
            }
        } else {
            bounds = getBounds();
        }
        return bounds;
    }

    protected GraphicsEnvironment getLocalGraphicsEnvironment() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment();
    }

    protected boolean boundsDisplayable(Rectangle bounds) {
        boolean displayable = false;
        for (int i = 0; i < device.length; i++) {
            if (device[i].getDefaultConfiguration().getBounds()
                    .contains(bounds.getLocation())) {
                displayable = true;
                break;
            }
        }
        return displayable;
    }

    private boolean getMaximized(String subnetName) {
        if (subnetName == null) {
            return false;
        }
        Boolean maximized = savedStates.get(subnetName);
        if (maximized == null) {
            return false;
        }
        return maximized;
    }

    /**
     * Saves the states of all frames that have been opened and that
     * successfully connected
     * 
     * Frame state is saved by subnet name, not by host id; the reason is that,
     * when we display a new frame, host connection is not yet established but
     * we need to show the frame in its previous location.
     */
    private void saveFrameStates() {
        Properties subnetFrames = new Properties();

        AppInfo appInfo = appContext.getConfigurationApi().getAppInfo();
        appInfo.setProperty(PROPERTIES_SUBNET_FRAMES, subnetFrames);
        for (Long subnetId : subnets.keySet()) {
            SubnetDescription subnet = subnets.get(subnetId);
            Rectangle frameBounds = bounds.get(subnet);
            Boolean frameMaximized = windowStates.get(subnet);
            if (frameBounds != null && frameMaximized != null) {
                String subnetName = subnet.getName();
                subnetFrames.put(subnetName, subnetName);
                Properties frameLocation = new Properties();
                frameLocation.put("x", frameBounds.x);
                frameLocation.put("y", frameBounds.y);
                frameLocation.put("width", frameBounds.width);
                frameLocation.put("height", frameBounds.height);
                frameLocation.put("state", frameMaximized);
                appInfo.setProperty(subnetName + PROPERTIES_SUBNET_STATE_SUFFIX,
                        frameLocation);
            }
        }
        appContext.getConfigurationApi().saveAppInfo(appInfo);
    }

    private void loadFrameStates() {
        AppInfo appInfo = appContext.getConfigurationApi().getAppInfo();
        Map<String, Properties> appProps = appInfo.getPropertiesMap();
        Properties subnetFrames = appProps.get(PROPERTIES_SUBNET_FRAMES);
        if (subnetFrames != null) {
            for (Object key : subnetFrames.keySet()) {
                String subnetName = (String) key;
                Properties frameProps = appProps
                        .get(subnetName + PROPERTIES_SUBNET_STATE_SUFFIX);
                if (frameProps != null) {
                    try {
                        int x = Integer.parseInt(frameProps.getProperty("x"));
                        int y = Integer.parseInt(frameProps.getProperty("y"));
                        int width = Integer
                                .parseInt(frameProps.getProperty("width"));
                        int height = Integer
                                .parseInt(frameProps.getProperty("height"));
                        boolean maximized = Boolean
                                .parseBoolean(frameProps.getProperty("state"));
                        Rectangle frameBounds =
                                new Rectangle(x, y, width, height);
                        savedBounds.put(subnetName, frameBounds);
                        savedStates.put(subnetName, maximized);
                    } catch (NumberFormatException e) {

                    }
                }
            }
        }

    }

    private Rectangle getBounds() {
        Rectangle frameBounds = null;
        GraphicsConfiguration screenConfig =
                defaultDevice.getDefaultConfiguration();
        Rectangle screenBounds = screenConfig.getBounds();
        if (lastBounds == null) {
            int width =
                    (int) (screenBounds.width * DEFAULT_SCREEN_SIZE_PERCENTAGE);
            int height = (int) (screenBounds.height
                    * DEFAULT_SCREEN_SIZE_PERCENTAGE);
            int x = screenBounds.x + ((screenBounds.width - width) / 2);
            int y = screenBounds.y + ((screenBounds.height - height) / 2);
            frameBounds = new Rectangle(x, y, width, height);
            lastBounds = frameBounds;
        } else {
            int x = lastBounds.x + FRAME_OFFSET;
            int y = lastBounds.y + FRAME_OFFSET;
            frameBounds =
                    new Rectangle(x, y, lastBounds.width, lastBounds.height);
            if (!screenBounds.contains(frameBounds)) {
                if ((x + lastBounds.width) > screenBounds.x
                        + screenBounds.width) {
                    x = screenBounds.x;
                }
                if ((y + lastBounds.height) > screenBounds.y
                        + screenBounds.height) {
                    y = screenBounds.y;
                }
                frameBounds = new Rectangle(x, y, lastBounds.width,
                        lastBounds.height);
            }
            lastBounds = frameBounds;
        }
        return frameBounds;
    }

    private void stopSubnet(final IFabricController controller,
            final Context context, final boolean forceWindowClose) {
        final boolean keepLastViewer = (contexts.size() == 0);
        if (keepLastViewer && controller != null) {
            lastViewer.set(controller);
        }
        final String subnetName = context.getSubnetDescription().getName();
        MDC.put("subnet", subnetName);
        final Map<String, String> configMap = MDC.getCopyOfContextMap();
        MDC.remove("subnet");
        startNewThread(new Runnable() {
            @Override
            public void run() {
                MDC.setContextMap(configMap);
                try {
                    if (context != null) {
                        context.close();
                    }
                } finally {
                    try {
                        if (controller != null) {
                            if (keepLastViewer || !forceWindowClose) {
                                controller.reset();
                            } else {
                                controller.cleanup();
                            }
                        }
                    } finally {
                        if (!keepLastViewer || forceWindowClose) {
                            log.info("Closing viewer for subnet '{}'",
                                    subnetName);
                            closeViewer(controller);
                        }
                    }
                }
            }
        });
    }

    private void startNewThread(Runnable runnable) {
        Thread newThread = new Thread(runnable);
        String threadName = SM_THREAD_PREFIX + threadCount.getAndIncrement();
        newThread.setName(threadName);
        newThread.start();
    }

    private void closeViewer(final IFabricController controller) {
        if (controller == null) {
            return;
        }
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                controller.doClose();
            }
        });
    }

    private void loadSubnets() {
        List<SubnetDescription> dbSubnets = appContext.getSubnets();
        if (dbSubnets != null) {
            subnets = new LinkedHashMap<Long, SubnetDescription>();
            for (SubnetDescription dbSubnet : dbSubnets) {
                subnets.put(dbSubnet.getSubnetId(), dbSubnet);
            }
        }
    }

    @SuppressWarnings("unused")
    private void restartViewer(final SubnetDescription subnet,
            final IFabricController controller, String existingHostId,
            String newHostId) {
        RestartViewerTask restarter = new RestartViewerTask(
                "<html>This fabric viewer will restart</html>", this, subnet,
                existingHostId, newHostId);
        controller.addPendingTask(restarter);
    }

    protected IFabricController createFabricController(String subnetName) {
        FabricView view = new FabricView(new FVMainFrame(subnetName));

        MBassador<IAppEvent> eventBus =
                new MBassador<IAppEvent>(new IPublicationErrorHandler() {
                    @Override
                    public void handleError(PublicationError error) {
                        log.error(null, error);
                        error.getCause().printStackTrace();
                    }
                });
        IFabricController controller =
                new FabricController(subnetName, view, this, eventBus);
        return controller;
    }

    @Override
    public SubnetDescription getSubnet(String subnetName) {
        for (SubnetDescription subnet : subnets.values()) {
            if (subnet.getName().equalsIgnoreCase(subnetName)) {
                return subnet;
            }
        }
        IllegalArgumentException iae = new IllegalArgumentException(
                "Cannot find subnet with the name '" + subnetName + "'");
        throw iae;
    }

    /**
     * This class is used to start a list of subnets. Fabric viewers are being
     * created and displayed and any connectivity issues are shown in the
     * progress bar (including failover progress if needed).
     */
    /*-
     * The general sequence to start a controller is as follows:
     * 1) Create the FabricController; get it to display on screen (showInit)
     * 2) Create the Context for the subnet; any errors while creating the context should be shown on the displayed FabricController
     * 3) Start the FabricController; at this point, we store both the FabricController and the Context for general use
     *
     */
    private class StartSubnetsTask
            extends SwingWorker<Void, SubnetDescription> {

        private final List<SubnetDescription> subnets;

        private final SplashScreen splashScreen;

        private final List<Throwable> errors = new ArrayList<Throwable>();

        private IFabricController lastController;

        private boolean splashClosed = false;

        private final Map<String, String> loggingContextMap;

        public StartSubnetsTask(List<SubnetDescription> subnets,
                SplashScreen splashScreen) {
            this.subnets = subnets;
            this.splashScreen = splashScreen;
            // Just making sure the Context Map is created
            MDC.put("subnet", "all");
            MDC.remove("subnet");
            loggingContextMap = MDC.getCopyOfContextMap();
        }

        @Override
        protected Void doInBackground() throws Exception {
            MDC.setContextMap(loggingContextMap);
            for (SubnetDescription subnet : subnets) {
                try {
                    Context context;
                    IFabricController controller;
                    IFabricController newController = null;
                    // Update tables
                    synchronized (tablesLock) {
                        context = contexts.get(subnet);
                        if (context == null) {
                            // First, create a new Context and a new
                            // FabricController (just in case doing so fails)
                            IFabricController tempController;
                            IFabricController lastController = lastViewer.get();
                            if (lastController != null && lastController
                                    .getCurrentContext() == null) {
                                if (lastViewer.compareAndSet(lastController,
                                        null)) {
                                    tempController = lastController;
                                } else {
                                    newController = createFabricController(
                                            subnet.getName());
                                    tempController = newController;
                                }
                            } else {
                                newController = createFabricController(
                                        subnet.getName());
                                tempController = newController;
                            }
                            // Now put them together so that there is no chance
                            // that one is there without the other
                            Context newContext =
                                    createContext(subnet, tempController);
                            contexts.put(subnet, newContext);
                            newContext.addSubnetEventListener(tempController);
                            this.lastController = tempController;
                        }
                    }
                    if (context == null) {
                        if (newController != null) {
                            showInitialFrame(subnet.getName(), newController);
                        }
                        publish(subnet);
                    } else {
                        if (!context.isValid()) {
                            controller = context.getController();
                            context = createContext(subnet, controller);
                            synchronized (tablesLock) {
                                contexts.put(subnet, context);
                            }
                            context.addSubnetEventListener(controller);
                            publish(subnet);
                        } else {
                            checkSubnetAlreadyDisplayed(subnet.getName(),
                                    context);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errors.add(e);
                }
            }
            return null;
        }

        /*-
         * Note that done runs on the EDT
         * @see http://docs.oracle.com/javase/7/docs/api/javax/swing/SwingWorker.html#done()
         */
        @Override
        protected void done() {
            if (errors.size() > 0) {
                if (lastController == null) {
                    lastController = createFabricController(null);
                    showInitialFrame(null, lastController);
                }
                Util.showErrors((Component) lastController.getView(), errors);
            }
        }

        /*-
         * Note that process runs on the EDT
         * @see http://docs.oracle.com/javase/7/docs/api/javax/swing/SwingWorker.html#process(java.util.List)
         */
        @Override
        protected void process(List<SubnetDescription> subnets) {
            for (SubnetDescription subnet : subnets) {
                log.info("Starting subnet {}", subnet.getName());
                Context context;
                IFabricController controller = null;
                context = contexts.get(subnet);
                if (context != null) {
                    controller = context.getController();
                }
                if (controller != null) {
                    controller.doShowContent();
                    controller.initializeContext(context);
                }
                if (!splashClosed) {
                    if (splashScreen != null) {
                        splashScreen.close();
                    }
                    splashClosed = true;
                }
            }
        }

        private void checkSubnetAlreadyDisplayed(String subnetName,
                Context context) {
            IFabricController controller = context.getController();
            controller.bringToFront();
            SubnetDescription targetSubnet = context.getSubnetDescription();
            if (!subnetName.equalsIgnoreCase(targetSubnet.getName())) {
                log.debug("Subnet " + subnetName
                        + " definition resolves to this subnet "
                        + targetSubnet.getName());
                StringBuffer sb = new StringBuffer();
                sb.append("Specified subnet '");
                sb.append(subnetName);
                sb.append("' resolves to this subnet '");
                sb.append(targetSubnet.getName());
                sb.append("'");
                ShowWarningTask warn = new ShowWarningTask(sb.toString());
                controller.addPendingTask(warn);
            }
        }
    }

    private class VerifySubnetsTask
            extends SwingWorker<Void, SubnetDescription> {

        private final SubnetDescription connectedSubnet;

        public VerifySubnetsTask(SubnetDescription connectedSubnet) {
            this.connectedSubnet = connectedSubnet;
        }

        @Override
        protected Void doInBackground() throws Exception {
            HostInfo hostInfo = connectedSubnet.getCurrentFE();
            String connIp = hostInfo.getInetAddress().getHostAddress();
            log.info("Verifying subnet '{}' connected to FE at {}",
                    connectedSubnet.getName(), connIp);
            for (SubnetDescription subnet : contexts.keySet()) {
                if (!connectedSubnet.equals(subnet)) {
                    HostInfo currHost = subnet.getCurrentFE();
                    String currIp = currHost.getInetAddress().getHostAddress();
                    if (connIp.equals(currIp)
                            && hostInfo.getPort() == currHost.getPort()) {
                        System.out.println("Connected subnet " + connectedSubnet
                                + "(" + connIp
                                + ") connects to the same FE than subnet "
                                + subnet + ")");
                    }
                }
            }
            return null;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.ISubnetManager#onEmailTest(com.intel.stl.api.subnet
     * .SubnetDescription, java.lang.String)
     */
    @Override
    public void onEmailTest(String recipients) {

        if ((recipients != null) && (recipients.isEmpty() == false)) {
            String subject =
                    UILabels.STL92001_TEST_EMAIL_SUBJECT.getDescription();
            String body = "";
            List<String> recipientsList = Utils.concatenatedStringToList(
                    recipients, UIConstants.MAIL_LIST_DELIMITER);
            getConfigurationApi().submitMessage(subject, body, recipientsList);
        }
    }

	@Override
	public Context getContext(SubnetDescription subnetDescription) {
		return contexts.get(subnetDescription);
	}
}
