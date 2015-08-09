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

package com.intel.stl.dbengine.impl;

import static com.intel.stl.common.STLMessages.STL30006_SQLEXCEPTION;
import static com.intel.stl.common.STLMessages.STL30007_ERROR_STARTING_DB_ENGINE;
import static com.intel.stl.configuration.AppSettings.APP_DB_PATH;
import static com.intel.stl.configuration.AppSettings.DB_CONNECTION_PASSWORD;
import static com.intel.stl.configuration.AppSettings.DB_CONNECTION_URL;
import static com.intel.stl.configuration.AppSettings.DB_CONNECTION_USER;
import static com.intel.stl.configuration.AppSettings.DB_NAME;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import org.hsqldb.Server;
import org.hsqldb.jdbc.JDBCPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.DatabaseException;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.configuration.AppInfo;
import com.intel.stl.common.STLMessages;
import com.intel.stl.configuration.AppConfigurationException;
import com.intel.stl.configuration.AppSettings;
import com.intel.stl.dbengine.DatabaseEngine;

/**
 * This class is responsible for initializing, starting up and shutting down the
 * database under HyperSQL
 * 
 * @author Fernando Fernandez
 * 
 */
public class HSqlDbEngine implements DatabaseEngine {
    private static Logger log = LoggerFactory.getLogger(HSqlDbEngine.class);

    private static final String DB_ENGINE_NAME = "HSQLDB";

    private static final String DB_ENGINE_VERSION = "2.3";

    public static final String DB_DEFINITION_FILE = "hsqldbdef.sql";

    private static final String HSQLDB_SERVER_URL = "jdbc:hsqldb:hsql:";

    private static final int HSQLDB_SERVER_DEFAULT_PORT = 9001;

    private static final String LOCALHOST_SERVER = "localhost";

    private static final String SLASHES = "//";

    private String databaseFolder;

    private String databaseName;

    private String connectionUrl;

    private String user;

    private Server server;

    private final JDBCPool pool;

    public HSqlDbEngine(AppSettings settings) throws AppConfigurationException {
        applySettings(settings);
        this.pool = new JDBCPool();
        String password = settings.getConfigOption(DB_CONNECTION_PASSWORD);
        pool.setUrl(connectionUrl);
        pool.setUser(user);
        pool.setPassword(password);
    }

    public HSqlDbEngine(AppSettings settings, JDBCPool pool)
            throws AppConfigurationException {
        applySettings(settings);
        this.pool = pool;
        String password = settings.getConfigOption(DB_CONNECTION_PASSWORD);
        pool.setUrl(connectionUrl);
        pool.setUser(user);
        pool.setPassword(password);
    }

    private void applySettings(AppSettings settings)
            throws AppConfigurationException {
        this.databaseFolder = settings.getConfigOption(APP_DB_PATH);
        this.databaseName = settings.getConfigOption(DB_NAME);
        this.user = settings.getConfigOption(DB_CONNECTION_USER);
        String connectionUrl;
        try {
            connectionUrl = settings.getConfigOption(DB_CONNECTION_URL);
        } catch (AppConfigurationException e) {
            log.info(e.getMessage());
            connectionUrl = null;
        }
        if (connectionUrl == null) {
            this.connectionUrl =
                    "jdbc:hsqldb:file:" + databaseFolder + File.separatorChar
                            + databaseName;
        } else {
            this.connectionUrl = connectionUrl;
        }

    }

    @Override
    public void start() throws DatabaseException {
        String urlPrefix = getConnectionUrlPrefix();
        String host = getConnectionUrlHost();
        if (HSQLDB_SERVER_URL.equalsIgnoreCase(urlPrefix)
                && LOCALHOST_SERVER.equalsIgnoreCase(host)) {
            server = new Server();
            server.setDaemon(true);
            server.setDatabasePath(0, databaseFolder);
            server.setDatabaseName(0, databaseName);
            int port = getConnectionUrlPort();
            server.setPort(port);
            server.start();
        }
    }

    @Override
    public void stop() throws DatabaseException {
        Connection conn = getConnection();
        try {
            PreparedStatement shutdown = conn.prepareStatement("SHUTDOWN");
            try {
                shutdown.execute();
            } finally {
                shutdown.close();
            }
            conn.close();
        } catch (SQLException e) {
            log.error(
                    STLMessages.STL30006_SQLEXCEPTION.getDescription(
                            e.getErrorCode(), StringUtils.getErrorMessage(e)),
                    e);
        }
    }

    @Override
    public void updateSchema() throws AppConfigurationException {
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement drop =
                    conn.prepareStatement("DROP SCHEMA PUBLIC CASCADE");
            try {
                drop.execute();
            } finally {
                drop.close();
            }
            DatabaseUtils.defineDatabase(conn, DB_DEFINITION_FILE);
            if (!DatabaseUtils.checkDatabase(conn)) {
                DatabaseUtils.defineDatabase(conn, DB_DEFINITION_FILE);
            } else {

            }
        } catch (SQLException e) {
            // TODO Define message for this condition
            AppConfigurationException ace =
                    new AppConfigurationException("SQLException", e);
            throw ace;
        } catch (DatabaseException e) {
            // TODO Define message for this condition
            AppConfigurationException ace =
                    new AppConfigurationException("DatabaseException", e);
            throw ace;
        }

        try {
            conn.close();
        } catch (SQLException e) {
            String errMsg =
                    STL30007_ERROR_STARTING_DB_ENGINE.getDescription(
                            DB_ENGINE_NAME, "close()", e.getErrorCode());
            log.error(errMsg, e);
            AppConfigurationException ace =
                    new AppConfigurationException(errMsg, e);
            throw ace;
        }
    }

    private void checkSchemaTimestamp() throws DatabaseException {
        long defTimestamp =
                DatabaseUtils
                        .getDatabaseDefinitionTimestamp(DB_DEFINITION_FILE);
        // File dbTimestampFile = new File(databaseFolder + File.separatorChar
        // + databaseName + DB_TIMESTAMP_EXT);
        // long dbTimestamp =
        // DatabaseUtils.getDatabaseTimestamp(dbTimestampFile);
        // Date dbSchemaDate = new Date(dbTimestamp);

    }

    @Override
    public Connection getConnection() throws DatabaseException {
        Connection conn = null;
        try {
            conn = pool.getConnection();
        } catch (SQLException e) {
            DatabaseException dbe =
                    new DatabaseException(STL30006_SQLEXCEPTION, e,
                            e.getErrorCode(), StringUtils.getErrorMessage(e));
            log.error(dbe.getMessage(), e);
            throw dbe;
        }
        return conn;
    }

    @Override
    public EntityManager getEntityManager() {
        // No JPA support here
        return null;
    }

    @Override
    public String getConnectionUrl() {
        return connectionUrl;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getEngineName() {
        return DB_ENGINE_NAME;
    }

    @Override
    public String getEngineVersion() {
        return DB_ENGINE_VERSION;
    }

    private void renameDbFolder() {
        Date dateSuffix = new Date();
        String rename =
                databaseFolder
                        + "-"
                        + new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss")
                                .format(dateSuffix);
        File dbFolderRename = new File(rename);
        File dbFolder = new File(databaseFolder);
        if (!dbFolder.renameTo(dbFolderRename)) {

        }
    }

    @Override
    public AppInfo getAppInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveAppInfo(AppInfo info) throws DatabaseException {
        // TODO Auto-generated method stub

    }

    private String getConnectionUrlPrefix() {
        return connectionUrl.substring(0, HSQLDB_SERVER_URL.length());
    }

    private String getConnectionUrlHost() {
        int x = HSQLDB_SERVER_URL.length();
        if (x <= connectionUrl.length()) {
            return "";
        }
        String slashes = connectionUrl.substring(x, x + 2);
        if (SLASHES.equalsIgnoreCase(slashes)) {
            x = x + 2;
        }
        int colon = connectionUrl.indexOf(":", x);
        if (colon >= 0) {
            return connectionUrl.substring(x, colon);
        } else {
            int slash = connectionUrl.indexOf("/", x);
            if (slash >= 0) {
                return connectionUrl.substring(x, slash);
            } else {
                return connectionUrl.substring(x);
            }
        }
    }

    private int getConnectionUrlPort() {
        int x = HSQLDB_SERVER_URL.length();
        if (x < connectionUrl.length()) {
            return HSQLDB_SERVER_DEFAULT_PORT;
        }
        int colon = connectionUrl.indexOf(":", x);
        if (colon >= 0) {
            int slash = connectionUrl.indexOf("/", colon + 1);
            if (slash >= 0) {
                return Integer.parseInt(connectionUrl.substring(colon + 1,
                        slash));
            } else {
                return Integer.parseInt(connectionUrl.substring(colon + 1));
            }
        } else {
            return HSQLDB_SERVER_DEFAULT_PORT;
        }
    }
}
