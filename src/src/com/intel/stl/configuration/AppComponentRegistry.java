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
 *  File Name: AppComponentRegistry.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.52  2015/12/03 19:53:36  fernande
 *  Archive Log: PR 131863 - Klocwork Issue on AppComponentRegistry. Added finally to close input stream.
 *  Archive Log:
 *  Archive Log: Revision 1.51  2015/11/18 21:08:38  fernande
 *  Archive Log: PR127008 - Allow a user delete DB files during uninstallation. Added code to invoke a script the first time the application is invoked.
 *  Archive Log:
 *  Archive Log: Revision 1.50  2015/09/02 15:55:49  fernande
 *  Archive Log: PR 130220 - FM GUI "about" window displays unmatched version and build #. Passing the OPA FM version thru the manifest.
 *  Archive Log:
 *  Archive Log: Revision 1.49  2015/09/01 15:32:40  fernande
 *  Archive Log: PR 130220 - FM GUI "about" window displays unmatched version and build #. Changed the default app version to 10.
 *  Archive Log:
 *  Archive Log: Revision 1.48  2015/08/19 19:26:45  fernande
 *  Archive Log: PR 128703 - Fail over doesn't work on A0 Fabric. Adding shutdown method to AppComponent interface for application shutdown.
 *  Archive Log:
 *  Archive Log: Revision 1.47  2015/08/17 18:48:40  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - change backend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.46  2015/08/17 14:22:56  rjtierne
 *  Archive Log: PR 128979 - SM Log display
 *  Archive Log: This is the first version of the Log Viewer which displays select lines of text from the remote SM log file. Updates include searchable raw text from file, user-defined number of lines to display, refreshing end of file, and paging. This PR is now closed and further updates can be found by referencing PR 130011 - "Enhance SM Log Viewer to include Standard and Advanced requirements".
 *  Archive Log:
 *  Archive Log: Revision 1.45  2015/08/10 17:04:49  robertja
 *  Archive Log: PR128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log: Revision 1.44  2015/07/09 18:51:49  fernande
 *  Archive Log: PR 129447 - Database size increases a lot over a short period of time. Added method to expose application settings in the settings.xml file to higher levels in the app
 *  Archive Log:
 *  Archive Log: Revision 1.43  2015/06/17 15:35:28  fisherma
 *  Archive Log: PR129220 - partial fix for the login changes.
 *  Archive Log:
 *  Archive Log: Revision 1.42  2015/06/10 19:36:34  jijunwan
 *  Archive Log: PR 129153 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
 *  Archive Log:
 * 
 *  Overview:
 * 
 *  @author: Fernando Fernandez
 * 
 ******************************************************************************/

package com.intel.stl.configuration;

import static com.intel.stl.common.STLMessages.STL10001_SETTINGS_FILE_FORMAT_INVALID;
import static com.intel.stl.common.STLMessages.STL10002_IO_EXCEPTION_READING_SETTINGS;
import static com.intel.stl.common.STLMessages.STL10004_SECURITY_EXCEPTION_IN_FOLDER;
import static com.intel.stl.common.STLMessages.STL10005_DATABASE_ENGINE_NOTSUPPORTED;
import static com.intel.stl.common.STLMessages.STL10016_INITIALIZING_COMPONENT_REGISTRY;
import static com.intel.stl.common.STLMessages.STL10017_CHECKING_MULTI_APP_INSTANCES;
import static com.intel.stl.common.STLMessages.STL10025_STARTING_COMPONENT;
import static com.intel.stl.common.STLMessages.STL10102_MULTI_INSTANCES;
import static com.intel.stl.configuration.AppSettings.APP_ADAPTER_USENEW;
import static com.intel.stl.configuration.AppSettings.APP_BUILD_DATE;
import static com.intel.stl.configuration.AppSettings.APP_BUILD_ID;
import static com.intel.stl.configuration.AppSettings.APP_DATA_PATH;
import static com.intel.stl.configuration.AppSettings.APP_DB_PATH;
import static com.intel.stl.configuration.AppSettings.APP_INTEL_PATH;
import static com.intel.stl.configuration.AppSettings.APP_MODLEVEL;
import static com.intel.stl.configuration.AppSettings.APP_NAME;
import static com.intel.stl.configuration.AppSettings.APP_OPA_FM;
import static com.intel.stl.configuration.AppSettings.APP_RELEASE;
import static com.intel.stl.configuration.AppSettings.APP_SCHEMA_LEVEL;
import static com.intel.stl.configuration.AppSettings.APP_UI_PLUGIN;
import static com.intel.stl.configuration.AppSettings.APP_VERSION;
import static com.intel.stl.configuration.AppSettings.DB_PERSISTENCE_PROVIDER_NAME;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileLock;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.AppContext;
import com.intel.stl.api.DefaultCertsAssistant;
import com.intel.stl.api.DefaultSecurityHandler;
import com.intel.stl.api.FMGui;
import com.intel.stl.api.configuration.impl.AppContextImpl;
import com.intel.stl.api.configuration.impl.MailManager;
import com.intel.stl.common.AppDataUtils;
import com.intel.stl.common.STLMessages;
import com.intel.stl.datamanager.DatabaseManager;
import com.intel.stl.datamanager.impl.DatabaseManagerImpl;
import com.intel.stl.dbengine.DatabaseEngine;
import com.intel.stl.dbengine.impl.HibernateEngine;
import com.intel.stl.fecdriver.adapter.FEAdapter;
import com.intel.stl.fecdriver.adapter.IAdapter;
import com.intel.stl.fecdriver.impl.STLAdapter;
import com.intel.stl.fecdriver.network.ssh.impl.JSchSessionFactory;

/**
 * @author Fernando Fernandez
 * 
 */
public class AppComponentRegistry {

    public static final String COMPONENT_DATABASE_MANAGER = "dbmgr";

    public static final String COMPONENT_MAIL_MANAGER = "mailmgr";

    public static final String COMPONENT_FEADAPTER = "feadapter";

    public static final String COMPONENT_APPLICATION_CONTEXT = "appcontext";

    public static final String COMPONENT_UI_PLUGIN = "uiplugin";

    public static final String APP_SETTINGS = "appsettings";

    private static final String ENGINE_HIBERNATE = "HIBERNATE";

    private static final String MANIFEST_IMPLEMENTATIONTITLE =
            "Implementation-Title";

    private static final String MANIFEST_IMPLEMENTATIONVERSION =
            "Implementation-Version";

    private static final String MANIFEST_SCHEMAVERSION = "Schema-Version";

    private static final String MANIFEST_OPAFM_VERSION = "Intel-OPAFM-Version";

    private static final String MANIFEST_BUILDID = "Intel-Build-Id";

    private static final String MANIFEST_BUILD_DATE = "Intel-Build-Date";

    private static final String APP_DEFAULT_NAME = "Intel Fabric Manager GUI";

    private static final int APP_DEFAULT_VERSION = 10;

    private static final int APP_DEFAULT_RELEASE = 0;

    private static final int APP_DEFAULT_MODLEVEL = 0;

    private static final String NOT_AVAILABLE = "N/A";

    // Up one this number if you want the database to be recreated;
    private static final int APP_DEFAULT_SCHEMALEVEL = 1;

    private static final int PROGRESS_DELAY = 500;

    private static Logger log = LoggerFactory
            .getLogger(AppComponentRegistry.class);

    private final Map<String, AppComponent> components;

    private AppSettings settings = null;

    private FMGui uiComponent;

    private AsyncProcessingService asyncService;

    public AppComponentRegistry() {
        components = new LinkedHashMap<String, AppComponent>();
    }

    public void initialize() throws AppConfigurationException {
        log.info(STL10016_INITIALIZING_COMPONENT_REGISTRY.getDescription());
        initializeAppSettings();
        initializeAppFolders();
        createComponents();
        // after the UIPlugin has been successfully initialized, we can start
        // driving it
        AppContext appContext = getAppContext();
        initializeUIPlugin(appContext);
        uiComponent.showProgress(
                STL10017_CHECKING_MULTI_APP_INSTANCES.getDescription(), 0);
        if (hasRunningApplication()) {
            throw new AppConfigurationException(
                    STL10102_MULTI_INSTANCES.getDescription());
        }
        sleep(PROGRESS_DELAY);
        // initialize all components in the order they were created
        double totalWeight = 5.0; // we add 5 to account for the previous check
        for (String componentName : components.keySet()) {
            AppComponent component = components.get(componentName);
            totalWeight += component.getInitializationWeight();
        }
        double progress = 5.0;
        for (String componentName : components.keySet()) {
            double percent = ((progress / totalWeight) * 100) + 0.5;
            AppComponent component = components.get(componentName);
            String componentDescription = component.getComponentDescription();
            uiComponent.showProgress(STL10025_STARTING_COMPONENT
                    .getDescription(componentDescription), (int) percent);
            component.initialize(settings);
            progress += component.getInitializationWeight();
            sleep(PROGRESS_DELAY);
        }
        sleep(PROGRESS_DELAY);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    private void createComponents() throws AppConfigurationException {
        asyncService = new AsyncProcessingService();
        createDatabaseManager();
        createMailManager();
        createAppContext();
    }

    public AppSettings getAppSettings() {
        return settings;
    }

    public FMGui getUIComponent() {
        return uiComponent;
    }

    public DatabaseManager getDatabaseManager()
            throws AppConfigurationException {
        return (DatabaseManager) getComponent(COMPONENT_DATABASE_MANAGER);
    }

    public MailManager getMailManager() throws AppConfigurationException {
        return (MailManager) getComponent(COMPONENT_MAIL_MANAGER);
    }

    public AppContext getAppContext() throws AppConfigurationException {
        return (AppContext) getComponent(COMPONENT_APPLICATION_CONTEXT);
    }

    public AppComponent getComponent(String componentName)
            throws AppConfigurationException {
        AppComponent res = components.get(componentName);
        if (res != null) {
            return res;
        } else {
            String msg =
                    STLMessages.STL10024_NO_COMPONENT_FOUND
                            .getDescription(componentName);
            log.error(msg);
            throw new AppConfigurationException(msg);
        }
    }

    public void registerComponent(String componentName, AppComponent component) {
        components.put(componentName, component);
    }

    public void deregisterComponent(String componentName) {
        components.remove(componentName);
    }

    private void initializeAppSettings() throws AppConfigurationException {
        try {
            String appName = APP_DEFAULT_NAME;
            Integer appVersion = new Integer(APP_DEFAULT_VERSION);
            Integer appRelease = new Integer(APP_DEFAULT_RELEASE);
            Integer appModLevel = new Integer(APP_DEFAULT_MODLEVEL);
            Integer schLevel = new Integer(APP_DEFAULT_SCHEMALEVEL);
            String opaFMVersion = NOT_AVAILABLE;
            String appBuildId = NOT_AVAILABLE;
            String appBuildDate = NOT_AVAILABLE;
            settings = getApplicationSettings();
            Manifest manifest = getManifest();
            if (manifest != null) {
                Attributes attribs = manifest.getMainAttributes();
                Name key = new Name(MANIFEST_IMPLEMENTATIONTITLE);
                appName = (String) attribs.get(key);
                key = new Name(MANIFEST_IMPLEMENTATIONVERSION);
                String strVersion = (String) attribs.get(key);
                String[] verParts =
                        strVersion == null ? new String[] { "0", "0" }
                                : strVersion.split("[.]");
                appVersion = parseInt(verParts[0], appVersion);
                appRelease = parseInt(verParts[1], appRelease);
                appModLevel = parseInt(verParts[2], appModLevel);
                log.info(appName + " v" + strVersion);
                key = new Name(MANIFEST_SCHEMAVERSION);
                String value = (String) attribs.get(key);
                schLevel = parseInt(value, schLevel);
                log.info("Schema level: " + schLevel);
                key = new Name(MANIFEST_OPAFM_VERSION);
                opaFMVersion = (String) attribs.get(key);
                log.info("OPA FM version: " + opaFMVersion);
                key = new Name(MANIFEST_BUILDID);
                appBuildId = (String) attribs.get(key);
                log.info("Build id: " + appBuildId);
                key = new Name(MANIFEST_BUILD_DATE);
                appBuildDate = (String) attribs.get(key);
                log.info("Build date: " + appBuildDate);
            }
            settings.setConfigOption(APP_NAME, appName);
            settings.setConfigOption(APP_VERSION, appVersion);
            settings.setConfigOption(APP_RELEASE, appRelease);
            settings.setConfigOption(APP_MODLEVEL, appModLevel);
            settings.setConfigOption(APP_OPA_FM, opaFMVersion);
            settings.setConfigOption(APP_BUILD_ID, appBuildId);
            settings.setConfigOption(APP_BUILD_DATE, appBuildDate);
            settings.setConfigOption(APP_SCHEMA_LEVEL, schLevel);
        } catch (InvalidPropertiesFormatException e) {
            String msg = STL10001_SETTINGS_FILE_FORMAT_INVALID.getDescription();
            log.error(msg, e);
            AppConfigurationException ace =
                    new AppConfigurationException(msg, e);
            throw ace;
        } catch (IOException e) {
            String msg =
                    STL10002_IO_EXCEPTION_READING_SETTINGS.getDescription();
            log.error(msg, e);
            AppConfigurationException ace =
                    new AppConfigurationException(msg, e);
            throw ace;
        }
    }

    private Integer parseInt(String value, Integer defaultValue) {
        Number res;
        try {
            res = NumberFormat.getInstance().parse(value);
        } catch (ParseException e) {
            return defaultValue;
        }
        return res.intValue();
    }

    private Manifest getManifest() throws IOException {
        URL registryUrl =
                AppComponentRegistry.class
                        .getResource(AppComponentRegistry.class.getSimpleName()
                                + ".class");
        URLConnection urlConn = registryUrl.openConnection();
        if (urlConn instanceof JarURLConnection) {
            // Running from a jar, we would get the manifest through this path
            JarURLConnection jarUrl = (JarURLConnection) urlConn;
            return jarUrl.getManifest();
        } else {
            // For unit tests, we would follow this path
            Manifest manifest = null;
            Enumeration<URL> manifests =
                    getClass().getClassLoader().getResources(
                            "META-INF/MANIFEST.MF");
            while (manifests.hasMoreElements()) {
                InputStream is = manifests.nextElement().openStream();
                try {
                    manifest = new Manifest(is);
                } finally {
                    is.close();
                }
                Attributes attribs = manifest.getMainAttributes();
                Name key = new Name(MANIFEST_BUILDID);
                if (attribs.get(key) != null) {
                    break;
                }
            }
            return manifest;
        }
    }

    private void initializeAppFolders() throws AppConfigurationException {
        String folder = null;
        try {
            folder = getIntelDataPath();
            File intelDir = new File(folder);
            if (!intelDir.exists()) {
                intelDir.mkdir();
            }
            settings.setConfigOption(APP_INTEL_PATH, folder);
            folder = getApplicationDataPath();
            File appDir = new File(folder);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            if (AppDataUtils.isPostSetupNeeded()) {
                executePostSetup();
            }
            settings.setConfigOption(APP_DATA_PATH, folder);
            folder = getDatabaseDataPath();
            File dbDir = new File(folder);
            if (!dbDir.exists()) {
                dbDir.mkdir();
            }
            settings.setConfigOption(APP_DB_PATH, folder);
        } catch (SecurityException e) {
            String msg =
                    STL10004_SECURITY_EXCEPTION_IN_FOLDER
                            .getDescription(folder);
            log.error(msg, e);
            AppConfigurationException ace =
                    new AppConfigurationException(msg, e);
            throw ace;
        }
    }

    private void executePostSetup() {
        String script = AppDataUtils.getPostSetupScript();
        if (script == null) {
            return;
        }
        log.info("Invoking post setup script {}", script);
        File scriptFile = new File(script);
        if (scriptFile.exists()) {
            ProcessBuilder pb = new ProcessBuilder(script);
            pb.redirectErrorStream(true);
            Process proc;
            InputStream is = null;
            try {
                proc = pb.start();
                is = proc.getInputStream();
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(is));
                String line = null;
                while ((line = br.readLine()) != null) {
                    log.info("PostSetup: " + line);
                }
                proc.waitFor();
            } catch (IOException e) {
                log.error(
                        "IOException while reading output from post setup script",
                        e);
            } catch (InterruptedException e) {
                log.error("Post setup script was interrupted", e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        log.error(
                                "IOException while closing output from post setup script",
                                e);
                    }
                }
            }
        } else {
            log.error("Post setup script not found.");
        }

    }

    private void initializeUIPlugin(AppContext appContext)
            throws AppConfigurationException {
        String pluginClass = settings.getConfigOption(APP_UI_PLUGIN);
        try {
            uiComponent = (FMGui) Class.forName(pluginClass).newInstance();
            uiComponent.init(appContext);
        } catch (Exception e) {
            AppConfigurationException ace =
                    new AppConfigurationException("Class load error", e);
            throw ace;
        }
    }

    protected boolean hasRunningApplication() {
        String file = getLockFilePath();
        final AppLock appLock = new AppLock(file);
        boolean locked = appLock.getLock();
        if (locked) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    appLock.releaseLock();
                }
            });
        }
        return !locked;
    }

    private void createDatabaseManager() throws AppConfigurationException {
        String engineType =
                settings.getConfigOption(DB_PERSISTENCE_PROVIDER_NAME);
        DatabaseEngine engine;
        if (ENGINE_HIBERNATE.equalsIgnoreCase(engineType)) {
            engine = new HibernateEngine(settings);
        } else {
            String errMsg =
                    STL10005_DATABASE_ENGINE_NOTSUPPORTED
                            .getDescription(engineType);
            AppConfigurationException ace =
                    new AppConfigurationException(errMsg);
            throw ace;
        }

        AppComponent dbMgr = new DatabaseManagerImpl(engine);
        registerComponent(COMPONENT_DATABASE_MANAGER, dbMgr);
        return;
    }

    private void createMailManager() throws AppConfigurationException {
        MailManager mailMgr = new MailManager(getDatabaseManager());
        registerComponent(COMPONENT_MAIL_MANAGER, mailMgr);
        return;
    }

    private void createAppContext() throws AppConfigurationException {
        IAdapter adapter;
        String adapterUseNew =
                settings.getConfigOption(APP_ADAPTER_USENEW, "true");
        boolean useNewAdapter = Boolean.parseBoolean(adapterUseNew);
        if (useNewAdapter) {
            FEAdapter feAdapter =
                    new FEAdapter(getDatabaseManager(), asyncService);
            registerComponent(COMPONENT_FEADAPTER, feAdapter);
            adapter = feAdapter;
        } else {
            adapter = STLAdapter.instance();
            adapter.registerCertsAssistant(new DefaultCertsAssistant());
            adapter.registerSecurityHandler(new DefaultSecurityHandler());
        }
        AppComponent appContext =
                new AppContextImpl(adapter, getDatabaseManager(),
                        getMailManager(), asyncService);
        registerComponent(COMPONENT_APPLICATION_CONTEXT, appContext);
    }

    protected AppSettings getApplicationSettings()
            throws InvalidPropertiesFormatException, IOException {
        return AppDataUtils.getApplicationSettings();
    }

    protected String getIntelDataPath() {
        return AppDataUtils.getIntelDataPath();
    }

    protected String getApplicationDataPath() {
        return AppDataUtils.getApplicationDataPath();
    }

    protected String getDatabaseDataPath() {
        return AppDataUtils.getDatabaseDataPath();
    }

    protected String getLockFilePath() {
        return AppDataUtils.getLockFilePath();
    }

    protected AsyncProcessingService getAsyncProcessingService() {
        return asyncService;
    }

    public void shutdown() {
        List<String> componentName = new ArrayList<String>(components.keySet());
        // Shutdown in the reverse order components were created
        for (int i = componentName.size() - 1; i >= 0; i--) {
            AppComponent component = components.get(componentName.get(i));
            log.info("Shutting down component {}...",
                    component.getComponentDescription());
            try {
                component.shutdown();
            } catch (Exception e) {
                log.warn("Exception occurred during {} shutdown.",
                        component.getComponentDescription(), e);
            }
        }
        try {
            STLAdapter.instance().close();
        } finally {
            try {
                JSchSessionFactory.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AppLock {

        private final File lockFile;

        private RandomAccessFile randomAccessFile;

        private FileLock fileLock;

        public AppLock(String lockFile) {
            this.lockFile = new File(lockFile);
            this.lockFile.deleteOnExit();
        }

        public boolean getLock() {
            try {
                randomAccessFile = new RandomAccessFile(lockFile, "rw");
                fileLock = randomAccessFile.getChannel().tryLock();
                if (fileLock != null) {
                    return true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        public void releaseLock() {
            try {
                fileLock.release();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    lockFile.delete();
                }
            }
        }
    }
}
