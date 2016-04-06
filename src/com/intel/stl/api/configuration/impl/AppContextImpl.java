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
 *  File Name: AppContextImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.49  2016/01/26 18:36:57  fernande
 *  Archive Log:    PR 132387 - [Dell]: FMGUI Fails to Open Due to Database Lock. Changed the AppComponent interface so that component can provide feedback to the UI.
 *  Archive Log:
 *  Archive Log:    Revision 1.48  2015/08/19 19:25:28  fernande
 *  Archive Log:    PR 128703 - Fail over doesn't work on A0 Fabric. FE Adapter not being shutdown during application shutdown
 *  Archive Log:
 *  Archive Log:    Revision 1.47  2015/08/17 18:48:56  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.46  2015/08/10 17:04:52  robertja
 *  Archive Log:    PR128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.45  2015/07/10 20:42:19  fernande
 *  Archive Log:    PR 129522 - Notice is not written to database due to topology not found. Moved FE Helpers to the session object and changed the order of initialization for the SubnetContext.
 *  Archive Log:
 *  Archive Log:    Revision 1.44  2015/07/09 18:47:04  fernande
 *  Archive Log:    PR 129447 - Database size increases a lot over a short period of time. Added method to expose application settings in the settings.xml file to higher levels in the app
 *  Archive Log:
 *  Archive Log:    Revision 1.43  2015/05/29 20:33:53  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Second wave of changes: the application can be switched between the old adapter and the new; moved out several initialization pieces out of objects constructor to allow subnet initialization with a UI in place; improved generics definitions for FV commands.
 *  Archive Log:
 *  Archive Log:    Revision 1.42  2015/05/26 15:33:04  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. A new FEAdapter is being added to handle requests through SubnetRequestDispatchers, which manage state for each connection to a subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.41  2015/05/18 14:52:05  robertja
 *  Archive Log:    PR128609 - Added failover to subnet clean-up.
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2015/04/22 16:51:39  fernande
 *  Archive Log:    Reorganized the startup sequence so that the UI plugin could initialize its own CertsAssistant. This way, autoconnect subnets would require a password using the UI CertsAssistant instead of the default CertsAssistant.
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2015/04/08 15:16:47  fernande
 *  Archive Log:    Changes to allow for failover to work when the current (initial) FE is not available.
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2015/04/06 11:04:53  jypak
 *  Archive Log:    Klockwork: Back End Critical Without Unit Test. Open issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2015/03/27 20:32:57  fernande
 *  Archive Log:    Removed the AsyncInitializer because at startup, the CertsAssistant is not setup by the UI yet and it falls back to the DefaultCertsAssistant.
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2015/03/16 17:34:19  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2015/03/02 22:30:16  fernande
 *  Archive Log:    Changed AppContext to allow for changes of the subnet name and still retrieve the corresponding SubnetContext.
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2015/02/25 22:03:13  fernande
 *  Archive Log:    Fixes for remove subnet function in the Setup Wizard.
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/02/23 22:22:24  jijunwan
 *  Archive Log:    improved to include/exclude inactive nodes/links in query
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2015/02/17 19:36:42  fernande
 *  Archive Log:    Adding methods to save application wide properties
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/02/12 22:06:32  fernande
 *  Archive Log:    Changes to SubnetManager to manage contexts and views based on the key Host_Ip_Address+Port so that there is one view per subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/02/10 19:29:22  fernande
 *  Archive Log:    SubnetContext are now created after a successful connection is made to the Fabric Executive, otherwise a SubnetConnectionException is triggered. Also, waitForConnect has been postponed until the UI invokes SubnetContext.initialize (thru Context.initialize). This way the UI shows up faster and the UI progress bar reflects more closely the process.
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/02/06 15:00:26  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/02/04 21:29:36  jijunwan
 *  Archive Log:    added Mail Manager
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/01/30 20:25:10  fernande
 *  Archive Log:    Making method available thru the AppContext interface
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/01/11 20:56:31  jijunwan
 *  Archive Log:    support secure FE:
 *  Archive Log:    1) added secured STL Connection to communicate with FE
 *  Archive Log:    2) added cert assistant interface that supports certs conf, persistence and password prompt
 *  Archive Log:    3) added default cert assistant
 *  Archive Log:    4) improved Subnet conf to support secure FE
 *  Archive Log:
 *  Archive Log:    NOTE: the secured connection requires Java 1.7
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2014/12/11 18:31:42  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2014/11/05 22:40:28  fernande
 *  Archive Log:    Adding integration testing for the NoticeManager connecting to a simulated fabric. The test is not complete though, we need to connect thru SSH to the fabric simulator and play with the fabric.
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2014/10/21 13:43:06  fernande
 *  Archive Log:    Adding AppContext shutdown to the shutdown process and closing connections as part of shutdown
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2014/10/14 20:44:51  jijunwan
 *  Archive Log:    1) improved to set SubnetContext invalid when we have network connection issues
 *  Archive Log:    2) improved to recreate SubnetContext when we query for it and the current one is invalid. We also clean up (include shutdown) the old context before we replace it with a new one
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/10/14 11:26:50  jypak
 *  Archive Log:    UI related updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/10/01 22:43:15  jypak
 *  Archive Log:    1. Fixed a bug related to extraneous notice listeners to a STLConnection.
 *  Archive Log:    2. Fixed incorrect map iteration in notice manager shutdown implementation.
 *  Archive Log:    3. Added shutdown invocations to tests to stop notice manager threads.
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/09/30 21:41:55  fernande
 *  Archive Log:    Adding unit tests.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/09/17 16:37:50  fernande
 *  Archive Log:    Changed to get subnet name from connection description
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/09/17 13:14:22  jypak
 *  Archive Log:    1. Removed unnecessary state variables.
 *  Archive Log:    2. Shutdown all notice managers for all subnets.
 *  Archive Log:    3. Comments changed.
 *  Archive Log:    4. Unnecessary thread interruption to set the flag removed.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/09/15 15:15:49  jypak
 *  Archive Log:    Notice Manager JUnit tests and relevant fixes.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/08/28 14:57:01  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/08/12 20:07:40  jijunwan
 *  Archive Log:    1) renamed HexUtils to StringUtils
 *  Archive Log:    2) added a method to StringUtils to get error message for an exception
 *  Archive Log:    3) changed all code to call StringUtils to get error message
 *  Archive Log:    4) some extra ode format change
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/07/11 13:06:57  jypak
 *  Archive Log:    1. Added runtime, non runtime exceptions to be thrown for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    2. Updated exception generating code due to Cache Manager related changes.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/07/07 21:19:13  fernande
 *  Archive Log:    Enabling the CacheManager
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/07/03 21:36:35  fernande
 *  Archive Log:    Adding the CacheManager in support of APIs
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/06/26 14:53:34  jijunwan
 *  Archive Log:    added capability to switch between cache and db based subnet api, by default db is used for subnet api
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/06/23 15:50:59  fernande
 *  Archive Log:    Added waitForConnect when creating a SubnetContext. Since each API may potentially have its own connection, creating a context now may take a while, while each connection is established. Since there isn't any other apparent point in the code where this could be done to avoid API requests being sent while the connection wasn't already established (background database updates were experiencing this), I'm adding it here for the time being until a better solution. In the meantime, the app initialization might seem to hang if connection establishment takes more than expected.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/06/19 20:01:16  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/05/07 19:50:02  fernande
 *  Archive Log:    Changes to save Subnets and EvenRules to database
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/06 15:17:30  jijunwan
 *  Archive Log:    updated configuration and context to include notice
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/04/22 17:22:45  jijunwan
 *  Archive Log:    code clean up
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/21 15:31:01  jijunwan
 *  Archive Log:    hidden Connection from UI
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/17 22:14:09  jijunwan
 *  Archive Log:    Added subnetDescription to SubnetContext
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/17 16:44:56  fernande
 *  Archive Log:    Changed AppContext to provide access to the ConfigurationApi, since it already resides there.
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

import static com.intel.stl.common.STLMessages.STL10020_APPCONTEXT_COMPONENT;
import static com.intel.stl.common.STLMessages.STL10025_STARTING_COMPONENT;
import static com.intel.stl.common.STLMessages.STL10026_STOPPING_COMPONENT;
import static com.intel.stl.common.STLMessages.STL30022_SUBNET_NOT_FOUND;
import static com.intel.stl.common.STLMessages.STL50008_SUBNET_CONNECTION_ERROR;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.AppContext;
import com.intel.stl.api.ICertsAssistant;
import com.intel.stl.api.ISecurityHandler;
import com.intel.stl.api.StartupProgressObserver;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.SubnetContext;
import com.intel.stl.api.configuration.ConfigurationException;
import com.intel.stl.api.configuration.IConfigurationApi;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.SubnetException;
import com.intel.stl.configuration.AppComponent;
import com.intel.stl.configuration.AppComponentRegistry;
import com.intel.stl.configuration.AppConfig;
import com.intel.stl.configuration.AppConfigurationException;
import com.intel.stl.configuration.AppSettings;
import com.intel.stl.configuration.AsyncProcessingService;
import com.intel.stl.configuration.SerialProcessingService;
import com.intel.stl.datamanager.DatabaseManager;
import com.intel.stl.fecdriver.adapter.FEAdapter;
import com.intel.stl.fecdriver.adapter.IAdapter;
import com.intel.stl.fecdriver.adapter.ISMEventListener;
import com.intel.stl.fecdriver.session.ISession;

public class AppContextImpl implements AppComponent, AppContext {
    private static Logger log = LoggerFactory.getLogger(AppContextImpl.class);

    private static final String APPCONTEXT_COMPONENT =
            STL10020_APPCONTEXT_COMPONENT.getDescription();

    private static final String PROGRESS_MESSAGE =
            STL10025_STARTING_COMPONENT.getDescription(APPCONTEXT_COMPONENT);

    private static final String SHUTDOWN_MESSAGE =
            STL10026_STOPPING_COMPONENT.getDescription(APPCONTEXT_COMPONENT);

    private final IAdapter adapter;

    private final DatabaseManager dbMgr;

    private final MailManager mailMgr;

    private final SerialProcessingService processingService;

    private final Map<String, SubnetDescription> subnetsBySubnetName =
            new HashMap<String, SubnetDescription>();

    private final Map<SubnetDescription, SubnetContext> subnetContexts =
            new HashMap<SubnetDescription, SubnetContext>();

    private IConfigurationApi confApi;

    private AppSettings settings;

    private boolean useNewAdapter;

    private final AtomicInteger threadCount = new AtomicInteger(1);

    public AppContextImpl(IAdapter adapter, DatabaseManager dbMgr,
            MailManager mailMgr, AsyncProcessingService processingService) {
        this.adapter = adapter;
        this.dbMgr = dbMgr;
        this.mailMgr = mailMgr;
        this.processingService = processingService;
        if (adapter instanceof FEAdapter) {
            useNewAdapter = true;
        } else {
            useNewAdapter = false;
        }
    }

    public MailManager getMailMgr() {
        return mailMgr;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.intel.stl.api.AppContext#regsiterCertsAssistant(com.intel.stl.api
     * .ICertsAssistant)
     */
    @Override
    public void registerCertsAssistant(ICertsAssistant assistant) {
        adapter.registerCertsAssistant(assistant);
    }

    @Override
    public void registerSecurityHandler(ISecurityHandler securityHandler) {
        adapter.registerSecurityHandler(securityHandler);
    }

    @Override
    public void initialize(AppSettings settings,
            StartupProgressObserver observer) throws AppConfigurationException {
        if (observer != null) {
            observer.setProgress(PROGRESS_MESSAGE);
        }
        this.settings = settings;
        this.confApi = new ConfigurationApi(adapter, dbMgr, mailMgr, settings);
    }

    @Override
    public String getComponentDescription() {
        return APPCONTEXT_COMPONENT;
    }

    @Override
    public int getInitializationWeight() {
        return 15;
    }

    @Override
    public List<SubnetDescription> getSubnets() {
        return dbMgr.getSubnets();
    }

    @Override
    public SubnetContext getSubnetContextFor(SubnetDescription subnet) {
        return getSubnetContextFor(subnet, false);
    }

    @Override
    public SubnetContext getSubnetContextFor(SubnetDescription subnet,
            boolean startBackgroundTasks) {
        // We use a different SubnetDescription object than the UI just to make
        // sure it's not changed while being used by the backend
        SubnetDescription dbSubnet = getSubnet(subnet.getName());
        dbSubnet.setContent(subnet);
        boolean subnetContextCreated = isSubnetContextCreated(dbSubnet);
        if (!subnetContextCreated) {
            addSubnetContext(dbSubnet, startBackgroundTasks);
        }
        SubnetContext subnetContext = subnetContexts.get(dbSubnet);
        return subnetContext;
    }

    @Override
    public String getAppSetting(String settingName, String defaultValue) {
        if (settings != null) {
            return settings.getConfigOption(settingName, defaultValue);
        }
        return defaultValue;
    }

    private boolean isSubnetContextCreated(SubnetDescription subnet) {
        SubnetContext context = subnetContexts.get(subnet);
        return (context != null) && (context.isValid() && !context.isClosed());
    }

    private void addSubnetContext(SubnetDescription subnet,
            boolean startBackgroundTasks) {
        SubnetContext context = subnetContexts.get(subnet);
        if (context == null || !context.isValid() || context.isClosed()) {
            log.info("Creating SubnetContext for subnet " + subnet.getName()
                    + " with startBackgroundTasks=" + startBackgroundTasks);
            createSubnetContext(subnet, startBackgroundTasks);
        }
    }

    protected synchronized void createSubnetContext(SubnetDescription subnet,
            boolean startBackgroundTasks) {
        SubnetContext context = subnetContexts.get(subnet);
        if (context == null || !context.isValid() || context.isClosed()) {
            context = new SubnetContextImpl(subnet, this);
            SubnetContext oldContext = subnetContexts.put(subnet, context);
            if (oldContext != null) {
                oldContext.cleanup();
            }
        }
    }

    @Override
    public IConfigurationApi getConfigurationApi() {
        return confApi;
    }

    @Override
    public void shutdown(StartupProgressObserver observer) {
        if (observer != null) {
            observer.setProgress(SHUTDOWN_MESSAGE);
        }
        processingService.shutdown();
        for (SubnetContext subnetCtx : subnetContexts.values()) {
            try {
                subnetCtx.cleanup();
            } catch (Exception e) {
                log.error("Error shutting down SubnetContext", e);
            }
        }
    }

    @Override
    public void shutdown() {
        log.info("Starting application shutdown");
        AppComponentRegistry registry = AppConfig.getAppComponentRegistry();
        try {
            registry.shutdown();
        } finally {
            log.info("Application component shutdown complete");
            System.gc();
            System.exit(0);
        }
    }

    public DatabaseManager getDatabaseManager() {
        return dbMgr;
    }

    public SerialProcessingService getProcessingService() {
        return processingService;
    }

    public int getNoticeManagerThreadCount() {
        return threadCount.getAndIncrement();
    }

    public boolean getUseNewAdapter() {
        return useNewAdapter;
    }

    protected SubnetDescription getSubnet(String subnetName)
            throws SubnetException {
        SubnetDescription subnet = subnetsBySubnetName.get(subnetName);
        if (subnet == null) {
            subnet = insertNewSubnet(subnetName);
        }
        return subnet;
    }

    protected synchronized SubnetDescription insertNewSubnet(
            String subnetName) {
        SubnetDescription subnet = dbMgr.getSubnet(subnetName);
        if (subnet == null) {
            ConfigurationException ce = new ConfigurationException(
                    STL30022_SUBNET_NOT_FOUND, subnetName);
            throw ce;
        }
        long subnetId = subnet.getSubnetId();
        Iterator<SubnetDescription> it =
                subnetsBySubnetName.values().iterator();
        while (it.hasNext()) {
            if (it.next().getSubnetId() == subnetId) {
                // Remove any old subnetDescription
                it.remove();
            }
        }
        subnetsBySubnetName.put(subnetName, subnet);
        return subnet;
    }

    protected ISession createSession(SubnetDescription subnet,
            ISMEventListener listener) {
        try {
            ISession session = adapter.createSession(subnet, listener);
            if (session != null) {
                session.getSubnetDescription().setName(subnet.getName());
            }
            return session;
        } catch (Exception e) {
            ConfigurationException ce = new ConfigurationException(
                    STL50008_SUBNET_CONNECTION_ERROR, e, subnet.getName(),
                    StringUtils.getErrorMessage(e));
            log.error(StringUtils.getErrorMessage(e), e);
            throw ce;
        }
    }

    // For testing

    /**
     * @return the subnetContexts
     */
    public Map<SubnetDescription, SubnetContext> getSubnetContexts() {
        return subnetContexts;
    }

}
