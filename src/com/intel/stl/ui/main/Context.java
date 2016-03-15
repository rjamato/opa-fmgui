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
 *  File Name: Context.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.53  2015/12/17 21:51:11  jijunwan
 *  Archive Log:    PR 132124 - Newly created VF not displayed after reboot of SM
 *  Archive Log:    - improved the arch to do cache reset
 *  Archive Log:
 *  Archive Log:    Revision 1.52  2015/09/25 20:52:27  fernande
 *  Archive Log:    PR129920 - revisit health score calculation. Changed formula to include several factors (or attributes) within the calculation as well as user-defined weights (for now are hard coded).
 *  Archive Log:
 *  Archive Log:    Revision 1.51  2015/08/31 22:26:33  jijunwan
 *  Archive Log:    PR 130197 - Calculated fabric health above 100% when entire fabric is rebooted
 *  Archive Log:    - changed to only use information from ImageInfo for calculation
 *  Archive Log:
 *  Archive Log:    Revision 1.50  2015/08/17 18:53:38  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.49  2015/08/17 14:22:42  rjtierne
 *  Archive Log:    PR 128979 - SM Log display
 *  Archive Log:    This is the first version of the Log Viewer which displays select lines of text from the remote SM log file. Updates include searchable raw text from file, user-defined number of lines to display, refreshing end of file, and paging. This PR is now closed and further updates can be found by referencing PR 130011 - "Enhance SM Log Viewer to include Standard and Advanced requirements".
 *  Archive Log:
 *  Archive Log:    Revision 1.48  2015/08/11 14:11:09  jijunwan
 *  Archive Log:    PR 129917 - No update on event statistics
 *  Archive Log:    - Added a new subscriber to allow periodically getting state summary
 *  Archive Log:
 *  Archive Log:    Revision 1.47  2015/08/10 17:30:41  robertja
 *  Archive Log:    PR 128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.46  2015/07/30 19:34:18  fernande
 *  Archive Log:    PR 129592 - removing a subnet a user is monitoring cause internal DB exception. Added flag to SubnetContext indicating the subnet has been deleted. If the flag is set, no saving of subnet information occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.44  2015/07/09 17:58:40  jijunwan
 *  Archive Log:    PR 129509 - Shall refresh UI after failover completed
 *  Archive Log:    - reset ManagementApi after failover completed
 *  Archive Log:    - refresh UI after failover completed
 *  Archive Log:    - updated comments
 *  Archive Log:
 *  Archive Log:    Revision 1.43  2015/06/30 17:50:13  fisherma
 *  Archive Log:    PR 129220 - Improvement on secure FE login.
 *  Archive Log:
 *  Archive Log:    Revision 1.42  2015/06/17 15:40:27  fisherma
 *  Archive Log:    PR129220 - partial fix for the login changes.
 *  Archive Log:
 *  Archive Log:    Revision 1.41  2015/06/08 16:12:35  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Stabilizing the new FEAdapter code. Changed to use the Performance API instead of the ConfigurationApi since Context is already running with a SubnetRequestDispatcher and shouldn't use a temporary session
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2015/05/29 20:43:46  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Second wave of changes: the application can be switched between the old adapter and the new; moved out several initialization pieces out of objects constructor to allow subnet initialization with a UI in place; improved generics definitions for FV commands.
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2015/05/22 13:53:52  robertja
 *  Archive Log:    PR128703 Resume performance data tasks after completion of fail-over.
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2015/05/11 12:35:06  rjtierne
 *  Archive Log:    Removed printed stack trace for ConfigurationException in method close().
 *  Archive Log:    This error is ignored since the only time it happens is when a subnet is
 *  Archive Log:    deleted while running, which is an acceptable condition. The error is now
 *  Archive Log:    logged only when a SubnetDataNotFoundException occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2015/04/29 19:13:51  rjtierne
 *  Archive Log:    When saving a subnet to the database in method close(), run on the EDT to prevent
 *  Archive Log:    collision with deletion of the subnet when running in a window.
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2015/04/28 22:13:18  jijunwan
 *  Archive Log:    1) introduced component owner to Context, so when we have errors in data collection, preparation etc, we now there the error dialog should go
 *  Archive Log:    2) improved TaskScheduler to show error message on proper frame
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2015/04/21 21:19:20  fernande
 *  Archive Log:    PR 127653 - FM GUI errors after connection loss. TaskScheduler is shut down when a connection is lost to stop generating requests until failover is complete. At that point, it is restarted.
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2015/04/20 15:59:47  fernande
 *  Archive Log:    Fix for NPE when failover occurs when a subnet is being started and the TaskScheduler has not been initialized yet.
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/04/15 18:51:16  fernande
 *  Archive Log:    Improved handling of TimeoutExceptions during failover: TaskScheduler is shutdown during failover and timeouts during failover do not trigger an additional failover process. Refresh functionality has been fixed
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2015/04/08 15:20:39  fernande
 *  Archive Log:    Changes to allow for failover to work when the current (initial) FE is not available.
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/04/06 21:20:28  fernande
 *  Archive Log:    Context adds itself as a SubnetEventListener to be notified by the SubnetContext of issues with the subnet. In particular, when failover fails, the TaskScheduler is shutdown as well as the SubnetContext.
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/04/02 13:32:54  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/03/31 16:21:47  fernande
 *  Archive Log:    Failover support. Adding interfaces and implementations to display in the UI the failover progress.
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/03/30 18:34:40  jypak
 *  Archive Log:    Introduce a UserSettingsProcessor to handle different use cases for user settings via Setup Wizard.
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/03/27 20:50:03  fernande
 *  Archive Log:    Adding support for failover
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/03/26 12:04:56  jypak
 *  Archive Log:    Updates for passing correct refresh rate to task scheduler. When the subnet is connected, compare the sweep interval from the subnet and the refresh rate provided by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/03/26 11:10:02  jypak
 *  Archive Log:    PR 126613 Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    -The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    -The Event Calculator clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/03/25 11:53:20  jypak
 *  Archive Log:    Header comments changed
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/03/25 11:52:27  jypak
 *  Archive Log:    Header comments changed
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/
package com.intel.stl.ui.main;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.CertsDescription;
import com.intel.stl.api.FMException;
import com.intel.stl.api.FMKeyStoreException;
import com.intel.stl.api.FMTrustStoreException;
import com.intel.stl.api.IConnectionAssistant;
import com.intel.stl.api.ISubnetEventListener;
import com.intel.stl.api.SSLStoreCredentialsDeniedException;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.SubnetContext;
import com.intel.stl.api.SubnetEvent;
import com.intel.stl.api.configuration.ConfigurationException;
import com.intel.stl.api.configuration.IConfigurationApi;
import com.intel.stl.api.configuration.UserNotFoundException;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.logs.ILogApi;
import com.intel.stl.api.management.IManagementApi;
import com.intel.stl.api.notice.INoticeApi;
import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.SubnetConnectionException;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.SubnetDescription.Status;
import com.intel.stl.ui.alert.NoticeEventListener;
import com.intel.stl.ui.alert.NoticeNotifier;
import com.intel.stl.ui.alert.NotifierFactory;
import com.intel.stl.ui.alert.NotifierType;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.model.UserPreference;
import com.intel.stl.ui.publisher.EventCalculator;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.intel.stl.ui.publisher.UserSettingsProcessor;

/**
 * @author jijunwan
 * 
 */
public class Context implements ISubnetEventListener, IConnectionAssistant {
    private static final Logger log = LoggerFactory.getLogger(Context.class);

    public static final long TIME_OUT = 30000; // 30 sec

    private TaskScheduler scheduler;

    private EventCalculator evtCal;

    private UserSettingsProcessor userSettingsProcessor;

    private List<Throwable> errors;

    private final SubnetContext subnetContext;

    private final IFabricController controller;

    private final String userName;

    private UserPreference userPreference;

    private final Component owner;

    private final NoticeEventListener noticeListener;

    private NoticeNotifier emailNotifier;

    public Context(SubnetContext subnetContext, IFabricController controller,
            String userName) {
        this.subnetContext = subnetContext;
        this.userName = userName;
        this.controller = controller;
        this.owner = controller.getViewFrame();
        this.noticeListener = new NoticeEventListener(controller.getEventBus());
        this.subnetContext.addSubnetEventListener(this);
        this.subnetContext.getSubnetDescription().setConnectionAssistant(this);
    }

    /**
     * @return the owner
     */
    public Component getOwner() {
        return owner;
    }

    /**
     * @return the controller
     */
    public IFabricController getController() {
        return controller;
    }

    public EventCalculator getEvtCal() {
        return evtCal;
    }

    public void initialize() throws SubnetConnectionException {
        subnetContext.initialize();

        getNoticeApi().addEventListener(noticeListener);
        refreshUserSettings();
        UserSettings userSettings = getUserSettings();
        userPreference = new UserPreference(userSettings);

        EnumMap<NodeType, Integer> nodes = null;
        try {
            nodes = subnetContext.getSubnetApi().getNodesTypeDist(false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        evtCal = new EventCalculator(nodes, userPreference);
        this.subnetContext.getNoticeApi().addEventListener(evtCal);

        PMConfigBean pmConf = getPerformanceApi().getPMConfig();
        int refreshRate = userPreference.getRefreshRateInSeconds();
        if (pmConf != null) {
            int sweepInterval = pmConf.getSweepInterval();
            if (refreshRate > sweepInterval) {
                this.scheduler = new TaskScheduler(this, refreshRate);
            } else {
                this.scheduler = new TaskScheduler(this, sweepInterval);
            }
        } else {
            this.scheduler = new TaskScheduler(this, refreshRate);
        }

        this.emailNotifier =
                NotifierFactory.createNotifier(NotifierType.MAIL, this);
        // AppInfo appInfo = this.getConfigurationApi().getAppInfo();
        this.subnetContext.getNoticeApi().addEventListener(emailNotifier);
    }

    public NoticeNotifier getEmailNotifier() {
        return emailNotifier;
    }

    public IConfigurationApi getConfigurationApi() {
        return subnetContext.getConfigurationApi();
    }

    public ISubnetApi getSubnetApi() {
        return subnetContext.getSubnetApi();
    }

    public IPerformanceApi getPerformanceApi() {
        return subnetContext.getPerformanceApi();
    }

    public INoticeApi getNoticeApi() {
        return subnetContext.getNoticeApi();
    }

    public ILogApi getLogApi() {
        return subnetContext.getLogApi();
    }

    public IManagementApi getManagementApi() {
        return subnetContext.getManagementApi();
    }

    public UserSettings getUserSettings() {
        UserSettings userSettings = null;
        try {
            userSettings = subnetContext.getUserSettings(userName);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return userSettings;
    }

    public void refreshUserSettings() {
        try {
            subnetContext.refreshUserSettings(userName);

            if (evtCal != null) {
                UserSettings userSettings = getUserSettings();
                if (userSettingsProcessor == null) {
                    userSettingsProcessor =
                            new UserSettingsProcessor(userSettings, evtCal);
                } else {
                    userSettingsProcessor.process(userSettings);
                }
            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setRandom(boolean random) {
        subnetContext.setRandom(random);
    }

    public void addSubnetEventListener(ISubnetEventListener listener) {
        subnetContext.addSubnetEventListener(listener);
    }

    public void removeSubnetEventListener(ISubnetEventListener listener) {
        subnetContext.removeSubnetEventListener(listener);
    }

    public void addFailoverProgressListener(PropertyChangeListener listener) {
        subnetContext.addFailoverProgressListener(listener);
    }

    public void removeFailoverProgressListener(PropertyChangeListener listener) {
        subnetContext.removeFailoverProgressListener(listener);
    }

    public void setDeleted(boolean deleted) {
        subnetContext.setDeleted(deleted);
    }

    /**
     * @return the apiBroker
     */
    public TaskScheduler getTaskScheduler() {
        return scheduler;
    }

    public SubnetDescription getSubnetDescription() {
        return subnetContext.getSubnetDescription();
    }

    public void close() {

        final Status status =
                subnetContext.isValid() ? Status.VALID : Status.INVALID;
        final IConfigurationApi confApi = subnetContext.getConfigurationApi();
        SubnetDescription subnet = subnetContext.getSubnetDescription();
        final long subnetId = subnet.getSubnetId();

        // Saving the subnet description to the database is done on the EDT
        // to prevent collision with deletion of the subnet while it is running
        // in a window. The EDT will serialize these two operations so there is
        // no resource contention.
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                try {
                    SubnetDescription subnetDesc = confApi.getSubnet(subnetId);
                    subnetDesc.setLastStatus(status);

                    SubnetDescription currentSubnet =
                            subnetContext.getSubnetDescription();
                    subnetDesc.getCurrentFE().setSshUserName(
                            currentSubnet.getCurrentFE().getSshUserName());
                    subnetDesc.getCurrentFE().setSshPortNum(
                            currentSubnet.getCurrentFE().getSshPortNum());
                    confApi.updateSubnet(subnetDesc);
                } catch (SubnetDataNotFoundException e) {
                    log.error(e.getMessage(), e);
                } catch (ConfigurationException e) {
                }
            }
        });
        cleanup();
    }

    public void cleanup() {
        try {
            if (scheduler != null) {
                scheduler.shutdown();
            }
            if (noticeListener != null) {
                noticeListener.shutdown();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            subnetContext.cleanup();
        }
    }

    public boolean isValid() {
        return subnetContext.isValid();
    }

    @Override
    public void onFailoverCompleted(SubnetEvent event) {
        // This method is intentionally left empty.
        // FabricController.onFailoverCompleted will handle the following
        // 1) reschedule of tasks after fail-over.
        // 2) reset ManagementApi so we will reload opafm.xml
        // 3) refresh UI to update to the latest FM state
    }

    @Override
    public void onFailoverFailed(SubnetEvent event) {
        log.debug("Context is cleaning up after failover failed");
        cleanup();
    }

    @Override
    public void onSubnetManagerConnectionLost(SubnetEvent event) {
        log.debug("Stopping the task scheduler");
        // Stop the TaskScheduler when a connection is lost to avoid more
        // requests until failover is completed
        try {
            scheduler.suspendServiceDuringFailover();
        } catch (Exception e) {
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.ISubnetEventListener#onSubnetManagerConnected(com.intel
     * .stl.api.SubnetEvent)
     */
    @Override
    public void onSubnetManagerConnected(SubnetEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public CertsDescription getSSLStoreCredentials(HostInfo hostInfo)
            throws SSLStoreCredentialsDeniedException {

        CertsDescription certs = null;
        CertsLoginController loginCtr = controller.getCertsLoginController();

        if (errors != null) {
            // Parse if there is a certificate password error
            for (Throwable error : errors) {
                System.out.println("error=" + error);
                if (error instanceof FMKeyStoreException) {
                    Throwable cause = getPasswordException(error);
                    if (cause != null) {
                        loginCtr.setKeyStorePwdError(StringUtils
                                .getErrorMessage(cause));
                    } else {
                        loginCtr.setKeyStoreLocError(StringUtils
                                .getErrorMessage(error));
                    }
                } else if (error instanceof FMTrustStoreException) {
                    Throwable cause = getPasswordException(error);
                    if (cause != null) {
                        loginCtr.setTrustStorePwdError(StringUtils
                                .getErrorMessage(cause));
                    } else {
                        loginCtr.setTrustStoreLocError(StringUtils
                                .getErrorMessage(error));
                    }
                } else {
                    log.warn("Unsupported error", error);
                }
            }
        } else {
            errors = new ArrayList<Throwable>();
        }

        certs = loginCtr.getSSLCredentials(hostInfo);

        // Cleanup errors from this iteration
        if (errors != null) {
            errors.clear();
        }

        return certs;
    }

    @Override
    public void onSSLStoreError(FMException fmException) {
        errors.add(fmException);
    }

    public void reset() {
        subnetContext.reset();
    }

    private Throwable getPasswordException(Throwable e) {
        while (e.getCause() != null) {
            e = e.getCause();
        }
        if ((e instanceof UnrecoverableKeyException)
                && e.getMessage().contains("Password")) {
            return e;
        }
        return null;
    }
}
