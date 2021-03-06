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
 *  File Name: HibernateHelper.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.20  2015/08/17 18:49:34  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/08/10 17:04:48  robertja
 *  Archive Log:    PR128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/07/10 16:17:48  jijunwan
 *  Archive Log:    PR 129447 - Database size increases a lot over a short period of time
 *  Archive Log:    - updated settings to restrict result_max_memory_rows
 *  Archive Log:    - added log.info to print out DB properties
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/07/09 18:55:29  fernande
 *  Archive Log:    PR 129447 - Database size increases a lot over a short period of time. Added logic to enable specifying HSQLDB option the the connection URL, just in case we need to tweak settings for customer issues
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/07/07 23:29:50  jijunwan
 *  Archive Log:    PR 129485 - Out of memory
 *  Archive Log:    - changed to shutdown DB with compact
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/07/07 22:52:21  jijunwan
 *  Archive Log:    PR 129485 - Out of memory
 *  Archive Log:    - changed default table type to cached
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/04/01 17:06:35  rjtierne
 *  Archive Log:    Added printout for hibernate shutdown
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/02/17 19:42:30  fernande
 *  Archive Log:    Changed to save Application Properties in AppInfo record
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/01/11 21:04:01  jijunwan
 *  Archive Log:    changed log level from error to warn for app info since these exceptions are recoverable
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/12/11 18:36:41  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/11/10 17:04:56  fernande
 *  Archive Log:    Adding getAppInfo to the Configuration API
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/11/07 22:09:12  fernande
 *  Archive Log:    Adding MANIFEST.MF processing to retrieve application information (version, build number)
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/21 13:46:03  fernande
 *  Archive Log:    Fix for the Datamanager shutdown process where UserSettings was not persisted to the database randomly.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/08/22 16:41:15  fernande
 *  Archive Log:    Adding support to test database creation (view generated SQL)
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/19 20:08:39  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/11 22:09:28  fernande
 *  Archive Log:    Changes to add more entities to database schema
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/23 19:47:14  fernande
 *  Archive Log:    Saving topology to database
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/13 19:03:08  fernande
 *  Archive Log:    Implemented saveLoggingConfiguration and getLoggingConfiguration
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/07 19:11:34  fernande
 *  Archive Log:    Changes to save Subnets and EventRules to database
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/25 20:32:39  fernande
 *  Archive Log:    Added support for Hibernate
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.dbengine.impl;

import static com.intel.stl.common.STLMessages.STL30016_ENGINE_NOT_STARTED;
import static com.intel.stl.configuration.AppSettings.APP_DB_PATH;
import static com.intel.stl.configuration.AppSettings.DB_CONNECTION_DRIVER;
import static com.intel.stl.configuration.AppSettings.DB_CONNECTION_PASSWORD;
import static com.intel.stl.configuration.AppSettings.DB_CONNECTION_URL;
import static com.intel.stl.configuration.AppSettings.DB_CONNECTION_USER;
import static com.intel.stl.configuration.AppSettings.DB_DATABASE_PROVIDER_NAME;
import static com.intel.stl.configuration.AppSettings.DB_NAME;
import static com.intel.stl.configuration.AppSettings.DB_PERSISTENCE_PROVIDER;
import static com.intel.stl.configuration.AppSettings.DB_PERSISTENCE_PROVIDER_NAME;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.xml.bind.JAXBException;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.DatabaseException;
import com.intel.stl.api.configuration.AppInfo;
import com.intel.stl.common.AppDataUtils;
import com.intel.stl.configuration.AppConfigurationException;
import com.intel.stl.configuration.AppSettings;
import com.intel.stl.datamanager.AppInfoRecord;
import com.intel.stl.dbengine.DatabaseEngine;
import com.intel.stl.xml.UserOptions;
import com.intel.stl.xml.UserOptionsMarshaller;

public class HibernateEngine implements DatabaseEngine {
    private static Logger log = LoggerFactory.getLogger(HibernateEngine.class);

    private static final String DB_ENGINE_NAME = "Hibernate";

    private static final String DB_ENGINE_VERSION = "4.3";

    private static final String APP_INFO_KEY = "FMGuiApp";

    private static final String PERSISTENCE_UNIT = "FMGui";

    private static final String PERSISTENCE_PROVIDER =
            "javax.persistence.provider";

    private static final String PERSISTENCE_JDBC_DRIVER =
            "javax.persistence.jdbc.driver";

    private static final String PERSISTENCE_JDBC_URL =
            "javax.persistence.jdbc.url";

    private static final String PERSISTENCE_JDBC_USER =
            "javax.persistence.jdbc.user";

    private static final String PERSISTENCE_JDBC_PASS =
            "javax.persistence.jdbc.password";

    // Allowable values: "none", "create", "drop-and-create", "drop"
    private static final String PERSISTENCE_SCHEMA_GENERATION =
            "javax.persistence.schema-generation.scripts.action";

    // Allowable values: "none", "create", "drop-and-create", "drop"
    private static final String PERSISTENCE_DATABASE_GENERATION =
            "javax.persistence.schema-generation.database.action";

    private static final String PERSISTENCE_SCHEMA_DROP_TARGET =
            "javax.persistence.schema-generation.scripts.drop-target";

    private static final String PERSISTENCE_SCHEMA_CREATE_TARGET =
            "javax.persistence.schema-generation.scripts.create-target";

    private static final String PERSISTENCE_DATABASE_NAME =
            "javax.persistence.database-product-name";

    private static final String PERSISTENCE_DATABASE_MAJOR_VERSION =
            "javax.persistence.database-major-version";

    private static final String PERSISTENCE_DATABASE_MINOR_VERSION =
            "javax.persistence.database-minor-version";

    public static final String HIBERNATE_SCHEMA_UPDATE =
            "hibernate.hbm2ddl.auto";

    public static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";

    public static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";

    private static final String DB_SETTING_PREFIX = "db.";

    private final AppSettings settings;

    private final String persistenceProvider;

    private final String providerName;

    private final String dbmsName;

    private final String connectionDriver;

    private final String connectionUrl;

    private final String connectionUser;

    private final String connectionPass;

    private EntityManagerFactory factory;

    public HibernateEngine(AppSettings settings) {
        this.settings = settings;
        this.persistenceProvider = getSetting(DB_PERSISTENCE_PROVIDER);
        this.providerName = getSetting(DB_PERSISTENCE_PROVIDER_NAME);
        this.dbmsName = getSetting(DB_DATABASE_PROVIDER_NAME);
        this.connectionDriver = getSetting(DB_CONNECTION_DRIVER);
        String dbmsProps = createDBMSPropertiesString();
        // If DB_CONNECTION_URL is not set in settings.xml (overridden by user)
        // then create a connection URL.
        String connectionUrl = getSetting(DB_CONNECTION_URL);
        if (connectionUrl == null) {
            String appDbFolder = getSetting(APP_DB_PATH);
            String dbFullName;
            if (appDbFolder == null) {
                dbFullName = getSetting(DB_NAME);
            } else {
                dbFullName =
                        appDbFolder + File.separatorChar + getSetting(DB_NAME);
            }
            this.connectionUrl =
                    "jdbc:hsqldb:file:" + dbFullName + ";" + dbmsProps;
        } else {
            this.connectionUrl = connectionUrl;
        }
        this.connectionUser = getSetting(DB_CONNECTION_USER);
        this.connectionPass = getSetting(DB_CONNECTION_PASSWORD);
    }

    @Override
    public void start() {
        Map<String, Object> overrides = createOverrides();
        factory =
                Persistence.createEntityManagerFactory(PERSISTENCE_UNIT,
                        overrides);
    }

    @Override
    public void stop() {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            log.info("Hibernate engine shutting down...");
            em.createNativeQuery("SHUTDOWN COMPACT").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            factory.close();
        }
    }

    @Override
    public EntityManager getEntityManager() throws DatabaseException {
        if (factory == null) {
            DatabaseException dbe =
                    new DatabaseException(STL30016_ENGINE_NOT_STARTED,
                            DB_ENGINE_NAME);
            throw dbe;
        }
        return factory.createEntityManager();
    }

    @Override
    public Connection getConnection() throws DatabaseException {
        // Hibernate does not provide access to the connection
        // Should use the Work API
        return null;
    }

    @Override
    public AppInfo getAppInfo() {
        AppInfo appInfo = null;
        AppInfoRecord appInfoRec;
        try {
            EntityManager em = getEntityManager();
            appInfoRec = em.find(AppInfoRecord.class, APP_INFO_KEY);
            if (appInfoRec == null) {
                return appInfo;
            }
            appInfo = appInfoRec.getAppInfo();
        } catch (DatabaseException e) {
            log.warn(e.getMessage(), e);
            return null;
        } catch (PersistenceException e) {
            log.debug(e.getMessage(), e);
            return null;
        }
        try {
            UserOptions options =
                    UserOptionsMarshaller.unmarshal(appInfoRec
                            .getPropertiesXml());
            appInfo.setPropertiesMap(options.getPreferences());
            return appInfo;
        } catch (JAXBException e) {
            log.warn(e.getMessage(), e);
            return appInfo;
        }
    }

    @Override
    public void saveAppInfo(AppInfo appInfo) {
        String propertiesXml = AppDataUtils.getDefaultUserOptions();
        UserOptions options;
        try {
            options = UserOptionsMarshaller.unmarshal(propertiesXml);
            options.setPreferences(appInfo.getPropertiesMap());
            propertiesXml = UserOptionsMarshaller.marshal(options);
        } catch (JAXBException e) {
            log.error(e.getMessage(), e);
        }
        EntityManager em = getEntityManager();
        AppInfoRecord record = em.find(AppInfoRecord.class, APP_INFO_KEY);
        em.getTransaction().begin();
        if (record == null) {
            record = new AppInfoRecord();
            record.setAppId(APP_INFO_KEY);
            record.setAppInfo(appInfo);
            record.setPropertiesXml(propertiesXml);
            em.persist(record);
        } else {
            record.setAppInfo(appInfo);
            record.setPropertiesXml(propertiesXml);
            em.merge(record);
        }
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public String getConnectionUrl() {
        return this.connectionUrl;
    }

    @Override
    public String getUser() {
        return this.connectionUser;
    }

    @Override
    public String getEngineName() {
        return DB_ENGINE_NAME;
    }

    @Override
    public String getEngineVersion() {
        return DB_ENGINE_VERSION;
    }

    @Override
    public void updateSchema() throws AppConfigurationException {
        if (factory != null) {
            factory.close();
        }
        String appDbFolder = getSetting(APP_DB_PATH);
        String ddlFileName;
        if (appDbFolder == null) {
            ddlFileName = getSetting(DB_NAME) + ".sql";
        } else {
            ddlFileName =
                    appDbFolder + File.separatorChar + getSetting(DB_NAME)
                            + ".sql";
        }
        File ddlFile = new File(ddlFileName);
        if (ddlFile.exists()) {
            ddlFile.delete();
        }
        Map<String, Object> overrides = createOverrides();
        overrides.put(HIBERNATE_SCHEMA_UPDATE, "create");
        overrides.put(PERSISTENCE_SCHEMA_GENERATION, "drop-and-create");
        overrides.put(PERSISTENCE_SCHEMA_DROP_TARGET, ddlFileName);
        overrides.put(PERSISTENCE_SCHEMA_CREATE_TARGET, ddlFileName);
        factory =
                Persistence.createEntityManagerFactory(PERSISTENCE_UNIT,
                        overrides);

    }

    @SuppressWarnings("unchecked")
    public static <T> T unproxy(T entity) {
        T unproxiedEntity = entity;
        Hibernate.initialize(unproxiedEntity);
        if (entity instanceof HibernateProxy) {
            HibernateProxy proxy = (HibernateProxy) unproxiedEntity;
            LazyInitializer initializer = proxy.getHibernateLazyInitializer();
            unproxiedEntity = (T) initializer.getImplementation();
        }
        return unproxiedEntity;
    }

    private Map<String, Object> createOverrides() {
        Map<String, Object> overrides = new HashMap<String, Object>();
        applySetting(overrides, PERSISTENCE_PROVIDER, persistenceProvider);
        applySetting(overrides, PERSISTENCE_JDBC_DRIVER, connectionDriver);
        applySetting(overrides, PERSISTENCE_JDBC_URL, connectionUrl);
        applySetting(overrides, PERSISTENCE_JDBC_USER, connectionUser);
        applySetting(overrides, PERSISTENCE_JDBC_PASS, connectionPass);
        if (providerName != null) {
            String providerSettingPrefix =
                    DB_SETTING_PREFIX + providerName + ".";
            for (Object setting : settings.keySet()) {
                String settingName = setting.toString().toLowerCase();
                if (settingName.startsWith(providerSettingPrefix)) {
                    String configOption = getSetting((String) setting);
                    applySetting(overrides,
                            settingName.substring(DB_SETTING_PREFIX.length()),
                            configOption);
                }
            }
        }
        return overrides;
    }

    private String createDBMSPropertiesString() {
        StringBuffer props = new StringBuffer();
        if (dbmsName != null) {
            String dbmsSettingPrefix = DB_SETTING_PREFIX + dbmsName + ".";
            for (Object setting : settings.keySet()) {
                String settingName = setting.toString().toLowerCase();
                if (settingName.startsWith(dbmsSettingPrefix)) {
                    String configOption = getSetting((String) setting);
                    props.append(settingName.substring(dbmsSettingPrefix
                            .length()));
                    props.append('=');
                    props.append(configOption);
                    props.append(';');
                }
            }
        }
        log.info("DB Properties: '" + props.toString() + "'");
        return props.toString();
    }

    public void generateScripts(String targetDatabase, int targetMajorVersion,
            int targetMinorVersion, String targetFile)
            throws AppConfigurationException {
        File outFile = new File(targetFile);
        if (outFile.exists()) {
            outFile.delete();
        }
        String outFilePath = outFile.getAbsolutePath();
        Map<String, Object> overrides = createOverrides();
        overrides.put(PERSISTENCE_SCHEMA_GENERATION, "drop-and-create");
        overrides.put(PERSISTENCE_SCHEMA_DROP_TARGET, outFilePath);
        overrides.put(PERSISTENCE_SCHEMA_CREATE_TARGET, outFilePath);
        overrides.put(PERSISTENCE_DATABASE_GENERATION, "none");
        overrides.put(PERSISTENCE_DATABASE_NAME, targetDatabase);
        overrides.put(PERSISTENCE_DATABASE_MAJOR_VERSION, targetMajorVersion);
        overrides.put(PERSISTENCE_DATABASE_MINOR_VERSION, targetMinorVersion);

        Persistence.generateSchema(PERSISTENCE_UNIT, overrides);
    }

    private String getSetting(String option) {
        try {
            return settings.getConfigOption(option);
        } catch (AppConfigurationException e) {
            return null;
        }
    }

    private void applySetting(Map<String, Object> map, String settingName,
            Object settingValue) {
        if (settingValue == null) {
            return;
        }
        log.debug("Overriding '" + settingName + "' = " + settingValue);
        map.put(settingName, settingValue);
    }
}
