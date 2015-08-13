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
 *  Archive Log:    Revision 1.32.2.1  2015/08/12 15:22:06  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
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

import static com.intel.stl.configuration.CacheManager.PA_HELPER;
import static com.intel.stl.configuration.CacheManager.SA_HELPER;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.intel.stl.api.ISubnetEventListener;
import com.intel.stl.api.SubnetContext;
import com.intel.stl.api.SubnetEvent;
import com.intel.stl.api.configuration.IConfigurationApi;
import com.intel.stl.api.configuration.UserNotFoundException;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.management.IManagementApi;
import com.intel.stl.api.management.impl.ManagementApi;
import com.intel.stl.api.notice.INoticeApi;
import com.intel.stl.api.notice.impl.NoticeApi;
import com.intel.stl.api.notice.impl.NoticeManagerImpl;
import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.impl.PAHelper;
import com.intel.stl.api.performance.impl.PerformanceApi;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.SMRecordBean;
import com.intel.stl.api.subnet.SubnetConnectionException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.impl.SAHelper;
import com.intel.stl.api.subnet.impl.SubnetApi;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.configuration.CacheManagerImpl;
import com.intel.stl.fecdriver.ApplicationEvent;
import com.intel.stl.fecdriver.ConnectionEvent;
import com.intel.stl.fecdriver.IConnection;
import com.intel.stl.fecdriver.IConnectionEventListener;
import com.intel.stl.fecdriver.IConnectionFactory;
import com.intel.stl.fecdriver.IFailoverEventListener;

public class SubnetContextImpl implements SubnetContext,
        IConnectionEventListener, IFailoverEventListener {
    public static final String PROGRESS_AMOUNT_PROPERTY = "ProgressAmount";

    public static final String PROGRESS_NOTE_PROPERTY = "ProgressNote";

    protected static final String NM_THREAD_PREFIX = "nmthread-";

    private final SubnetDescription subnet;

    private final IConfigurationApi confApi;

    private final ISubnetApi subnetApi;

    private final IPerformanceApi perfApi;

    private final INoticeApi noticeApi;

    private final IManagementApi managementApi;

    private final NoticeManagerImpl noticeMgr;

    private final CacheManagerImpl cacheMgr;

    private final String noticeManagerThreadName;

    private final boolean useDB;

    private final List<IConnection> connections;

    private final List<ISubnetEventListener> subnetEventListeners =
            new CopyOnWriteArrayList<ISubnetEventListener>();

    private final boolean startBackgroundTasks;

    private boolean initialized = false;

    private boolean valid = true;

    private boolean closed = false;

    private Throwable lastError;

    private UserSettings userSettings;

    private PropertyChangeSupport failoverProgress;

    public SubnetContextImpl(SubnetDescription subnet,
            AppContextImpl appContext, IConnectionFactory connFactory,
            boolean startBackgroundTasks) {
        this.subnet = subnet;
        this.confApi = appContext.getConfigurationApi();
        this.startBackgroundTasks = startBackgroundTasks;
        this.useDB = appContext.getUseDb();
        failoverProgress = new PropertyChangeSupport(this);

        IConnection subnetApiConn = connFactory.createConnection(subnet);
        IConnection perfApiConn = connFactory.createConnection(subnet);
        IConnection noticeApiConn = connFactory.createConnection(subnet);
        this.noticeManagerThreadName =
                NM_THREAD_PREFIX + appContext.getNoticeManagerThreadCount();
        this.cacheMgr =
                new CacheManagerImpl(appContext.getDatabaseManager(),
                        appContext.getProcessingService());
        subnetApiConn.addConnectionEventListener(this);
        subnetApiConn.addFailoverEventListener(this);
        perfApiConn.addConnectionEventListener(this);
        perfApiConn.addFailoverEventListener(this);
        noticeApiConn.addConnectionEventListener(this);
        noticeApiConn.addFailoverEventListener(this);
        connections = new ArrayList<IConnection>(3);
        this.subnetApi = getSubnetApi(subnetApiConn, cacheMgr);
        this.perfApi = getPerformanceApi(perfApiConn, cacheMgr);
        this.noticeApi = getNoticeApi(noticeApiConn, cacheMgr);
        this.managementApi = getManagementApi(subnet);
        // Pass the STLConnection created by the noticeApi for now. notice
        // API might not use the connection but just leave it there for now.
        this.noticeMgr = getNoticeManager(noticeApiConn, cacheMgr, noticeApi);
        connections.add(subnetApiConn);
        connections.add(perfApiConn);
        connections.add(noticeApiConn);

    }

    @Override
    public IConfigurationApi getConfigurationApi() {
        return confApi;
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
     * @see com.intel.stl.api.SubnetContext#getManagementApi()
     */
    @Override
    public IManagementApi getManagementApi() {
        return managementApi;
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
        userSettings = confApi.getUserSettings(subnet.getName(), userName);
        noticeApi.setUserSettings(userSettings);
    }

    @Override
    public void setRandom(boolean random) {
        this.noticeMgr.setRandom(random);
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
            if (userSettings != null) {
                try {
                    confApi.saveUserSettings(subnet.getName(), userSettings);
                } catch (Exception e) {
                    // Ignore any errors
                }
            }
            confApi.cleanup();
            subnetApi.cleanup();
            perfApi.cleanup();
        } finally {
            try {
                noticeApi.cleanup();
            } finally {
                if (!closed) {
                    for (IConnection conn : connections) {
                        conn.close();
                    }
                }
                this.closed = true;
                noticeMgr.cleanup();
                noticeMgr.shutdown();
                cacheMgr.cleanup();
            }
        }
    }

    @Override
    public synchronized void initialize() throws SubnetConnectionException {
        if (initialized) {
            return;
        }
        for (IConnection conn : connections) {
            try {
                conn.waitForConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Start the notice manager thread here.
        Thread noticeMgrThread = new Thread(noticeMgr);
        noticeMgrThread.setName(noticeManagerThreadName);
        noticeMgrThread.start();
        cacheMgr.initialize(useDB, startBackgroundTasks);
        List<SMRecordBean> smList = subnetApi.getSMs();
        subnet.setSMList(smList);
        fireSubnetManagerConnectedEvent();
        initialized = true;
    }

    @Override
    public void connectionClose(ConnectionEvent event) {
        // TODO: This logic should be improved to attempt recovery and/or
        // failover. Also, we need to implement the IErrorHandler for the APIs;
        // that handler should received errors that cannot be recovered and take
        // action on behalf of the API (those errors should bubble up from here,
        // if recovery/failover fails).
        valid = false;
        lastError = event.getReason();
    }

    @Override
    public void connectionError(ConnectionEvent event) {
        // More work related to a connection error is done at the Context level;
        // a Context adds itself as a SubnetEventListener for the SubnetContext.
        valid = false;
        lastError = event.getReason();
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

    private IManagementApi getManagementApi(SubnetDescription subnet) {
        return new ManagementApi(subnet);
    }

    private ISubnetApi getSubnetApi(IConnection conn, CacheManagerImpl cacheMgr) {
        SAHelper helper = new SAHelper(conn);
        cacheMgr.registerHelper(SA_HELPER, helper);
        ISubnetApi subnetApi = new SubnetApi(cacheMgr);
        return subnetApi;
    }

    private IPerformanceApi getPerformanceApi(IConnection conn,
            CacheManagerImpl cacheMgr) {
        PAHelper helper = new PAHelper(conn);
        cacheMgr.registerHelper(PA_HELPER, helper);
        IPerformanceApi perfApi = new PerformanceApi(cacheMgr);
        return perfApi;
    }

    private NoticeApi getNoticeApi(IConnection conn, CacheManagerImpl cacheMgr) {
        NoticeApi noticeApi = new NoticeApi(conn, cacheMgr);
        return noticeApi;
    }

    private NoticeManagerImpl getNoticeManager(IConnection conn,
            CacheManager cacheMgr, INoticeApi noticeApi) {
        SAHelper helper = new SAHelper(conn);
        NoticeManagerImpl noticeMgr =
                new NoticeManagerImpl(conn, cacheMgr, noticeApi, helper);
        return noticeMgr;
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

    public NoticeManagerImpl getNoticeManager() {
        return noticeMgr;
    }

}
