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
 *  File Name: SubnetContextImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.48  2015/12/17 21:51:07  jijunwan
 *  Archive Log:    PR 132124 - Newly created VF not displayed after reboot of SM
 *  Archive Log:    - improved the arch to do cache reset
 *  Archive Log:
 *  Archive Log:    Revision 1.47  2015/10/06 15:49:51  rjtierne
 *  Archive Log:    PR 130390 - Windows FM GUI - Admin tab->Logs side-tab - unable to login to switch SM for log access
 *  Archive Log:    - Changed finally clause in cleanup() to call the cleanup() in ManagementApi to shutdown the
 *  Archive Log:    current session and remove the session from the map.  This way the user is required to log into the
 *  Archive Log:    Admin page again when a subnet is brought up subsequent times.
 *  Archive Log:
 *  Archive Log:    Revision 1.46  2015/08/17 18:48:56  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.45  2015/08/17 14:22:59  rjtierne
 *  Archive Log:    PR 128979 - SM Log display
 *  Archive Log:    This is the first version of the Log Viewer which displays select lines of text from the remote SM log file. Updates include searchable raw text from file, user-defined number of lines to display, refreshing end of file, and paging. This PR is now closed and further updates can be found by referencing PR 130011 - "Enhance SM Log Viewer to include Standard and Advanced requirements".
 *  Archive Log:
 *  Archive Log:    Revision 1.44  2015/07/30 19:29:58  fernande
 *  Archive Log:    PR 129592 - removing a subnet a user is monitoring cause internal DB exception. Added flag to SubnetContext indicating the subnet has been deleted. If the flag is set, no saving of subnet information occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.43  2015/07/20 17:18:53  jijunwan
 *  Archive Log:    PR 126645 - Topology Page does not show correct data after port disable/enable event
 *  Archive Log:    - improved to notify UI after DB task is done
 *  Archive Log:
 *  Archive Log:    Revision 1.42  2015/07/19 20:54:51  jijunwan
 *  Archive Log:    PR 126645 - Topology Page does not show correct data after port disable/enable event
 *  Archive Log:    - fixed sync issue on DB update and cached node distribution
 *  Archive Log:
 *  Archive Log:    Revision 1.41  2015/07/10 20:42:19  fernande
 *  Archive Log:    PR 129522 - Notice is not written to database due to topology not found. Moved FE Helpers to the session object and changed the order of initialization for the SubnetContext.
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2015/07/09 18:47:04  fernande
 *  Archive Log:    PR 129447 - Database size increases a lot over a short period of time. Added method to expose application settings in the settings.xml file to higher levels in the app
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2015/06/16 15:49:24  fernande
 *  Archive Log:    PR 129034 Support secure FE. Fixed setting of valid flag when failover fails.
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2015/06/08 15:56:05  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Stabilizing the new FEAdapter code. Under some conditions, the NoticeProcessTasks returns a null instead of a Future; checking for this condition
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2015/06/01 15:52:29  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Stabilizing the new FEAdapter code. Adding switch to start the NoticeManager if STLAdapter is in use, do not start it if FEAdapter is in use
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2015/05/29 20:33:53  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Second wave of changes: the application can be switched between the old adapter and the new; moved out several initialization pieces out of objects constructor to allow subnet initialization with a UI in place; improved generics definitions for FV commands.
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2015/05/26 20:28:55  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Fixing NPE due to the fact that the NoticeManager is not even created yet when the first notice is generated.
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2015/05/26 15:33:04  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. A new FEAdapter is being added to handle requests through SubnetRequestDispatchers, which manage state for each connection to a subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/05/18 14:52:05  robertja
 *  Archive Log:    PR128609 - Added failover to subnet clean-up.
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2015/04/27 21:45:23  rjtierne
 *  Archive Log:    - Removed method setUserSettings() to prevent overwriting existing user settings
 *  Archive Log:    - Passed SAHelper to NoticeMangerImpl so the TopologyUpdateTask() can be called when
 *  Archive Log:    trying to process notices without a topology in the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/04/22 16:51:39  fernande
 *  Archive Log:    Reorganized the startup sequence so that the UI plugin could initialize its own CertsAssistant. This way, autoconnect subnets would require a password using the UI CertsAssistant instead of the default CertsAssistant.
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/04/21 16:47:47  fernande
 *  Archive Log:    Fix for NPE because failoverProgress should be created before any connection
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/04/16 17:33:58  fernande
 *  Archive Log:    Fixed an issue where when a subnet is deleted and the viewer is closed, saving user settings will fail and an error generated. The error is now ignored
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/04/15 18:42:10  fernande
 *  Archive Log:    Improved handling of TimeoutExceptions during failover: TaskScheduler is shutdown during failover and timeouts during failover do not trigger an additional failover process.
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/04/15 17:04:57  rjtierne
 *  Archive Log:    Implemented new method setUserSettings()
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/04/08 15:16:47  fernande
 *  Archive Log:    Changes to allow for failover to work when the current (initial) FE is not available.
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/04/06 21:12:38  fernande
 *  Archive Log:    Improving the handling of connection errors
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/04/03 16:07:29  fernande
 *  Archive Log:    Added SM list to the bean for failover
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/03/31 16:14:33  fernande
 *  Archive Log:    Failover support. Adding interfaces and implementations to display in the UI the failover progress.
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/03/27 20:33:22  fernande
 *  Archive Log:    Adding support for failover
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/03/26 14:41:17  robertja
 *  Archive Log:    Specify SMFailover exception thrown by connectionLost.
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/03/26 11:09:56  jypak
 *  Archive Log:    PR 126613 Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    -The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    -The Event Calculator clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/03/25 11:26:54  jypak
 *  Archive Log:    Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    The Event Calculator and the Event Summary Table clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/03/24 18:41:54  robertja
 *  Archive Log:    Instantiate FailoverManager before registering for fail-over notifications.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/03/23 13:54:52  robertja
 *  Archive Log:    New fail-over functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/03/16 17:34:19  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/03/11 16:57:16  jijunwan
 *  Archive Log:    minor change - may help us in the future on unit test
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/03/05 17:30:38  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:    1) read/write opafm.xml from/to host with backup file support
 *  Archive Log:    2) Application parser
 *  Archive Log:    3) Add/remove and update Application
 *  Archive Log:    4) unique name, reference conflication check
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/03/02 22:30:16  fernande
 *  Archive Log:    Changed AppContext to allow for changes of the subnet name and still retrieve the corresponding SubnetContext.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/02/20 22:24:39  fernande
 *  Archive Log:    Fix to invalidate the subnet context when there is an error in the connection and create a new one when it's requested for again.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/02/10 19:29:22  fernande
 *  Archive Log:    SubnetContext are now created after a successful connection is made to the Fabric Executive, otherwise a SubnetConnectionException is triggered. Also, waitForConnect has been postponed until the UI invokes SubnetContext.initialize (thru Context.initialize). This way the UI shows up faster and the UI progress bar reflects more closely the process.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/09 21:29:54  jijunwan
 *  Archive Log:    added clean up to APIs that intend to close STLConections
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/06 15:00:26  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/21 13:40:59  fernande
 *  Archive Log:    Fixing spelling error in method name
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/14 20:44:51  jijunwan
 *  Archive Log:    1) improved to set SubnetContext invalid when we have network connection issues
 *  Archive Log:    2) improved to recreate SubnetContext when we query for it and the current one is invalid. We also clean up (include shutdown) the old context before we replace it with a new one
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/14 11:26:50  jypak
 *  Archive Log:    UI related updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/08/19 18:10:53  jijunwan
 *  Archive Log:    introduced cleanup method to do cleanup before we shutdown an app
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/19 20:01:16  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/06 15:17:30  jijunwan
 *  Archive Log:    updated configuration and context to include notice
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/17 22:14:09  jijunwan
 *  Archive Log:    Added subnetDescription to SubnetContext
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/15 20:28:53  fernande
 *  Archive Log:    Changes to defer creation of APIs until a subnet is chosen
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.ISubnetEventListener;
import com.intel.stl.api.SubnetContext;
import com.intel.stl.api.SubnetEvent;
import com.intel.stl.api.configuration.IConfigurationApi;
import com.intel.stl.api.configuration.UserNotFoundException;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.logs.ILogApi;
import com.intel.stl.api.logs.impl.LogApi;
import com.intel.stl.api.management.IManagementApi;
import com.intel.stl.api.management.impl.ManagementApi;
import com.intel.stl.api.notice.INoticeApi;
import com.intel.stl.api.notice.NoticeBean;
import com.intel.stl.api.notice.NoticeWrapper;
import com.intel.stl.api.notice.impl.NoticeApi;
import com.intel.stl.api.notice.impl.NoticeManagerImpl;
import com.intel.stl.api.notice.impl.NoticeProcessTask;
import com.intel.stl.api.notice.impl.NoticeSimulator;
import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.impl.PerformanceApi;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.SMRecordBean;
import com.intel.stl.api.subnet.SubnetConnectionException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.impl.SubnetApi;
import com.intel.stl.configuration.BaseCache;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.configuration.CacheManagerImpl;
import com.intel.stl.configuration.ResultHandler;
import com.intel.stl.configuration.SerialProcessingService;
import com.intel.stl.datamanager.DatabaseManager;
import com.intel.stl.fecdriver.ApplicationEvent;
import com.intel.stl.fecdriver.adapter.ISMEventListener;
import com.intel.stl.fecdriver.session.ISession;

public class SubnetContextImpl implements SubnetContext, ISMEventListener {
    private static Logger log = LoggerFactory
            .getLogger(SubnetContextImpl.class);

    public static final String PROGRESS_AMOUNT_PROPERTY = "ProgressAmount";

    public static final String PROGRESS_NOTE_PROPERTY = "ProgressNote";

    protected static final String NM_THREAD_PREFIX = "nmthread-";

    private ISubnetApi subnetApi;

    private IPerformanceApi perfApi;

    private IManagementApi managementApi;

    private ILogApi logApi;

    private String noticeManagerThreadName;

    private boolean initialized = false;

    private boolean valid = true;

    private boolean closed = false;

    private boolean deleted = false;

    private Throwable lastError;

    private UserSettings userSettings;

    private ISession session;

    private final boolean useNewAdapter;

    private final List<ISubnetEventListener> subnetEventListeners =
            new CopyOnWriteArrayList<ISubnetEventListener>();

    private NoticeManagerImpl noticeMgr;

    private NoticeSimulator simulator;

    private Long randomSeed;

    private final AtomicBoolean topologyUpdateTaskStarted = new AtomicBoolean(
            false);

    private INoticeApi noticeApi;

    private final CacheManagerImpl cacheMgr;

    private final PropertyChangeSupport failoverProgress;

    private final AppContextImpl appContext;

    private final SubnetDescription subnet;

    public SubnetContextImpl(SubnetDescription subnet, AppContextImpl appContext) {
        this.subnet = subnet;
        this.appContext = appContext;
        this.useNewAdapter = appContext.getUseNewAdapter();
        this.failoverProgress = new PropertyChangeSupport(this);
        this.cacheMgr = new CacheManagerImpl(this);
    }

    @Override
    public IConfigurationApi getConfigurationApi() {
        return appContext.getConfigurationApi();
    }

    @Override
    public ISubnetApi getSubnetApi() {
        return subnetApi;
    }

    @Override
    public IPerformanceApi getPerformanceApi() {
        return perfApi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.SubnetContext#getNoticeApi()
     */
    @Override
    public INoticeApi getNoticeApi() {
        return noticeApi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.SubnetContext#getLoggerApi()
     */
    @Override
    public ILogApi getLogApi() {
        return logApi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.SubnetContext#getManagementApi()
     */
    @Override
    public IManagementApi getManagementApi() {
        return managementApi;
    }

    public SerialProcessingService getProcessingService() {
        return appContext.getProcessingService();
    }

    public DatabaseManager getDatabaseManager() {
        return appContext.getDatabaseManager();
    }

    public CacheManager getCacheManager() {
        return cacheMgr;
    }

    public ISession getSession() {
        return session;
    }

    @Override
    public String getAppSetting(String settingName, String defaultValue) {
        return appContext.getAppSetting(settingName, defaultValue);
    }

    /**
     * Refresh only if the user setting is null.
     */
    @Override
    public UserSettings getUserSettings(String userName)
            throws UserNotFoundException {
        if (userSettings == null) {
            refreshUserSettings(userName);
        }
        return userSettings;
    }

    @Override
    public void refreshUserSettings(String userName)
            throws UserNotFoundException {
        userSettings =
                getConfigurationApi().getUserSettings(subnet.getName(),
                        userName);
        noticeApi.setUserSettings(userSettings);
    }

    @Override
    public void setRandom(boolean random) {
        if (random) {
            if (simulator == null) {
                simulator = new NoticeSimulator(cacheMgr);
                if (randomSeed != null) {
                    simulator.setSeed(randomSeed);
                }
            }
            simulator.addEventListener(this);
            simulator.run();
        } else {
            if (simulator != null) {
                simulator.removeEventListener(this);
                simulator.stop();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.SubnetContext#getSubnetDescription()
     */
    @Override
    public SubnetDescription getSubnetDescription() {
        return subnet;
    }

    @Override
    public void addSubnetEventListener(ISubnetEventListener listener) {
        subnetEventListeners.add(listener);
    }

    @Override
    public void removeSubnetEventListener(ISubnetEventListener listener) {
        subnetEventListeners.remove(listener);
    }

    @Override
    public void addFailoverProgressListener(PropertyChangeListener listener) {
        failoverProgress.addPropertyChangeListener(listener);
    }

    @Override
    public void removeFailoverProgressListener(PropertyChangeListener listener) {
        failoverProgress.removePropertyChangeListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.SubnetContext#cleanup()
     */
    @Override
    public void cleanup() {
        try {
            PropertyChangeListener[] listeners =
                    failoverProgress.getPropertyChangeListeners();
            for (PropertyChangeListener listener : listeners) {
                failoverProgress.removePropertyChangeListener(listener);
            }
            subnetEventListeners.clear();
            if (userSettings != null && !deleted) {
                try {
                    getConfigurationApi().saveUserSettings(subnet.getName(),
                            userSettings);
                } catch (Exception e) {
                    // Ignore any errors
                }
            }
            try {
                getConfigurationApi().cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                subnetApi.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                perfApi.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                logApi.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                managementApi.cleanup();
            }

        } finally {
            try {
                if (simulator != null) {
                    simulator.stop();
                }
                noticeApi.cleanup();
            } finally {
                if (!closed) {
                    session.close();
                }
                this.closed = true;
                if (!useNewAdapter) {
                    try {
                        noticeMgr.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        noticeMgr.shutdown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    cacheMgr.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public synchronized void initialize() throws SubnetConnectionException {
        if (initialized) {
            return;
        }
        // Keep in mind that this is layered initialization, from bottom up:
        // - First the FE Adapter
        this.session = appContext.createSession(subnet, this);
        // - Then the caches (they need the session)
        cacheMgr.initialize();
        submitTopologyUpdateTaskIfNeeded();
        // - Now the APIs (they need the caches)
        this.subnetApi = new SubnetApi(this);
        this.perfApi = new PerformanceApi(this);
        this.noticeApi = new NoticeApi(this);
        this.managementApi = new ManagementApi(subnet);
        this.logApi = new LogApi(this);

        // Start the notice manager thread here.
        if (!useNewAdapter) {
            this.noticeMgr = new NoticeManagerImpl(cacheMgr, noticeApi);
            Thread noticeMgrThread = new Thread(noticeMgr);
            this.noticeManagerThreadName =
                    NM_THREAD_PREFIX + appContext.getNoticeManagerThreadCount();
            noticeMgrThread.setName(noticeManagerThreadName);
            noticeMgrThread.start();
        }
        List<SMRecordBean> smList = subnetApi.getSMs();
        subnet.setSMList(smList);
        fireSubnetManagerConnectedEvent();
        initialized = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.SubnetContext#isValid()
     */
    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.SubnetContext#reset()
     */
    @Override
    public void reset() {
        // clear extra cached data in subnetApi
        subnetApi.reset();
        // clear extra cached data in perfApi
        perfApi.reset();

        // clear all caches
        cacheMgr.reset();
        // re-init DB data
        cacheMgr.startTopologyUpdateTask();
    }

    @Override
    public void onNewEvent(NoticeBean[] notices) {
        for (NoticeBean notice : notices) {
            log.info("Get " + notice);
        }

        if (useNewAdapter) {
            processNotices(notices);
        } else {
            noticeMgr.onNewEvent(notices);
        }
    }

    @Override
    public void onFailoverStart(ApplicationEvent event) {
        SubnetEvent subnetEvent = new SubnetEvent(this);
        for (ISubnetEventListener listener : subnetEventListeners) {
            try {
                listener.onSubnetManagerConnectionLost(subnetEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailoverEnd(ApplicationEvent event) {
        Throwable cause = event.getReason();
        SubnetEvent subnetEvent = new SubnetEvent(event.getSource(), cause);
        if (cause == null) {
            for (ISubnetEventListener listener : subnetEventListeners) {
                try {
                    listener.onFailoverCompleted(subnetEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            fireSubnetManagerConnectedEvent();
        } else {
            valid = false;
            for (ISubnetEventListener listener : subnetEventListeners) {
                listener.onFailoverFailed(subnetEvent);
            }
        }
    }

    @Override
    public void onFailoverProgress(ApplicationEvent event) {
        Object obj = event.getSource();
        if (obj instanceof String) {
            String note = (String) obj;
            failoverProgress.firePropertyChange(PROGRESS_NOTE_PROPERTY, null,
                    note);
        } else if (obj instanceof Double) {
            Double progress = (Double) obj;
            failoverProgress.firePropertyChange(PROGRESS_AMOUNT_PROPERTY, null,
                    progress);
        }
    }

    /**
     * @return the lastConnectionError
     */
    public Throwable getLastConnectionError() {
        return lastError;
    }

    private void fireSubnetManagerConnectedEvent() {
        SubnetEvent subnetEvent = new SubnetEvent(subnet);
        for (ISubnetEventListener listener : subnetEventListeners) {
            try {
                listener.onSubnetManagerConnected(subnetEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void processNotices(final NoticeBean[] notices) {
        submitTopologyUpdateTaskIfNeeded();
        // Once the notices are saved, create a NoticeProcessTask
        // and submit it.
        // It's an asynchronous task so even though there is
        // processing speed difference between
        // NoticeSaveTask and NoticeProcessTask, we don't care.
        final NoticeProcessTask noticeProcessTask =
                new NoticeProcessTask(subnet.getName(),
                        appContext.getDatabaseManager(), cacheMgr);

        // Note that the NoticeProcessTask runs in the serial
        // thread, which means that only one task is processed at
        // once. This is done for two reasons: first, if an outburst
        // of notices comes our way, NoticeProcessTask will pick up
        // whatever number has been enqueued and process them in one
        // task; secondly, since there is a potential to trigger a
        // copy of the whole topology if new nodes and links are
        // added, this process would need to be unique (like the
        // SaveTopologyTask, which uses the same thread) so that two
        // tasks do not step on each other.
        getProcessingService().submitSerial(noticeProcessTask,
                new ResultHandler<Future<Boolean>>() {
                    @Override
                    public void onTaskCompleted(
                            Future<Future<Boolean>> processResult) {
                        try {
                            Future<Boolean> dbFuture = processResult.get();
                            if (dbFuture != null) {
                                // NoticeProcessTask may return a null instead
                                // of a Future if there are no notices to
                                // process

                                Boolean topologyChanged = dbFuture.get();
                                if (topologyChanged) {
                                    // Special case for DBNodeCahce that the
                                    // Node distribution depends on nodes in DB.
                                    // So we must update after the DB
                                    // updatetopologyChanged.
                                    ((BaseCache) cacheMgr.acquireNodeCache())
                                            .setCacheReady(false);

                                    log.info("Topology changed as a result of processing notices");
                                }

                                // notify after DB is ready
                                List<NoticeWrapper> noticeWrappers =
                                        noticeProcessTask.getNoticeWrappers();
                                noticeApi
                                        .addNewEventDescriptions(noticeWrappers
                                                .toArray(new NoticeWrapper[0]));

                            }
                        } catch (InterruptedException e) {
                            log.error("notice process task was interrupted", e);
                        } catch (ExecutionException e) {
                            Exception executionException =
                                    (Exception) e.getCause();
                            // TODO, we should inform the UI of the
                            // error (perhaps a
                            // newEventDescription?)
                            log.error(
                                    "Exception caught during notice process task",
                                    executionException);
                        } catch (Exception e) {
                            log.error(
                                    "Exception caught during notice process task",
                                    e);
                        }
                    }
                });
    }

    private void submitTopologyUpdateTaskIfNeeded() {
        if (!topologyUpdateTaskStarted.get()) {
            if (topologyUpdateTaskStarted.compareAndSet(false, true)) {
                cacheMgr.startTopologyUpdateTask();
            }
        }
    }

    public NoticeManagerImpl getNoticeManager() {
        return noticeMgr;
    }

}
