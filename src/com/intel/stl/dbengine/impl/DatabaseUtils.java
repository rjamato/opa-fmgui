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

import static com.intel.stl.common.STLMessages.STL10009_ERROR_READING_FILE;
import static com.intel.stl.common.STLMessages.STL30003_DATABASE_DEFINITION_FILE_NOT_FOUND;
import static com.intel.stl.common.STLMessages.STL30004_ERROR_READING_DATABASE_DEFINITION;
import static com.intel.stl.common.STLMessages.STL30005_ERRORS_DURING_DATABASE_DEFINITION;
import static com.intel.stl.common.STLMessages.STL30013_ERROR_SAVING_ENTITY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.DatabaseException;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.configuration.EventRuleAction;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.common.STLMessages;
import com.intel.stl.datamanager.EventActionRecord;
import com.intel.stl.datamanager.NodeTypeRecord;

/**
 * @author Fernando Fernandez
 * 
 */
public class DatabaseUtils {

    private static final String SQL_CMD_SEPARATOR = "GO";

    private static final String SQL_SELECT_COUNT = "SELECT COUNT(*) FROM ";

    protected static String[] SQL_CHECK_TABLES = { "Subnet", "Node", "Port",
            "Topology" };

    private static Logger log = LoggerFactory.getLogger(DatabaseUtils.class);

    public static DatabaseException createPersistDatabaseException(
            Throwable cause, Class<?> entityClass, Object entityId) {
        Throwable last = cause;
        while (last.getCause() != null) {
            last = last.getCause();
        }
        String entity = entityClass.getSimpleName();
        DatabaseException dbe =
                new DatabaseException(STL30013_ERROR_SAVING_ENTITY, cause,
                        entity, entityId, StringUtils.getErrorMessage(last));
        return dbe;
    }

    public static boolean checkDatabase(Connection conn) {
        boolean databaseCreated = true;
        try {
            Statement stmt = conn.createStatement();
            try {
                for (int i = 0; i < SQL_CHECK_TABLES.length; i++) {
                    try {

                        ResultSet rs =
                                stmt.executeQuery(SQL_SELECT_COUNT
                                        + SQL_CHECK_TABLES[i] + ";");
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (SQLException e) {
                        // This is the error code for HSqlDb; should make it
                        // less
                        // dependent
                        if (e.getErrorCode() != -5501) {
                            log.error(STLMessages.STL30006_SQLEXCEPTION
                                    .getDescription(e.getErrorCode(),
                                            e.getMessage()));
                        }
                        databaseCreated = false;
                    }
                }
            } finally {
                stmt.close();
            }
            return databaseCreated;
        } catch (SQLException e) {
            log.error(STLMessages.STL30006_SQLEXCEPTION.getDescription(
                    e.getErrorCode(), e.getMessage()));
            databaseCreated = false;
            return databaseCreated;
        }
    }

    public static void defineDatabase(Connection conn, String definitionFile)
            throws DatabaseException {
        URL dbDefUrl = DatabaseUtils.class.getResource(definitionFile);

        if (dbDefUrl != null) {
            InputStream istream = null;
            BufferedReader in = null;
            try {
                istream = dbDefUrl.openStream();
            } catch (IOException e) {
                DatabaseException dbe =
                        new DatabaseException(
                                STL30004_ERROR_READING_DATABASE_DEFINITION, e);
                log.error(dbe.getMessage(), e);
                throw dbe;
            }
            in = new BufferedReader(new InputStreamReader(istream));
            try {
                String line = in.readLine();
                StringBuffer sqlCmd = new StringBuffer();
                boolean definitionError = false;
                while (line != null) {
                    String trimmedLine = line.trim();
                    if (SQL_CMD_SEPARATOR.equalsIgnoreCase(trimmedLine)) {
                        String sqlStr = sqlCmd.toString();
                        if (!executeSQLCmd(conn, sqlStr)) {
                            definitionError = true;
                        }
                        sqlCmd = new StringBuffer();
                    } else {
                        sqlCmd.append(trimmedLine);
                        sqlCmd.append(" ");
                    }
                    line = in.readLine();
                }
                if (definitionError) {
                    DatabaseException dbe =
                            new DatabaseException(
                                    STL30005_ERRORS_DURING_DATABASE_DEFINITION);
                    throw dbe;
                }
            } catch (IOException e) {
                DatabaseException dbe =
                        new DatabaseException(
                                STL30004_ERROR_READING_DATABASE_DEFINITION, e);
                log.error(dbe.getMessage(), e);
                throw dbe;
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        } else {
            DatabaseException dbe =
                    new DatabaseException(
                            STLMessages.STL30003_DATABASE_DEFINITION_FILE_NOT_FOUND);
            throw dbe;
        }
    }

    public static boolean executeSQLCmd(Connection conn, String sqlCmd) {
        log.debug("Executing SQL command: " + sqlCmd);
        boolean result = true;
        try {
            Statement stmt = conn.createStatement();
            try {
                stmt.execute(sqlCmd);
            } finally {
                stmt.close();
            }
        } catch (SQLException se) {
            log.error(STLMessages.STL30002_DATABASE_ENGINE_ERROR
                    .getDescription(se.getErrorCode(), sqlCmd), se);
            result = false;
        }
        return result;
    }

    public static void populateRequiredTables(EntityManager em) {
        NodeType[] types = NodeType.class.getEnumConstants();
        EventRuleAction[] actions = EventRuleAction.class.getEnumConstants();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        for (int i = 0; i < types.length; i++) {
            NodeTypeRecord type = new NodeTypeRecord();
            type.setId(types[i].getId());
            type.setNodeType(types[i]);
            em.persist(type);
        }
        for (int i = 0; i < actions.length; i++) {
            EventActionRecord action = new EventActionRecord();
            action.setId(actions[i].name());
            action.setAction(actions[i]);
            em.persist(action);
        }
        tx.commit();
    }

    public static long getDatabaseDefinitionTimestamp(String definitionFile)
            throws DatabaseException {
        URL dbDefUrl = DatabaseUtils.class.getResource(definitionFile);
        long timestamp = 0;
        if (dbDefUrl != null) {
            try {
                URLConnection urlConn = dbDefUrl.openConnection();
                timestamp = urlConn.getLastModified();
            } catch (IOException e) {
                DatabaseException dbe =
                        new DatabaseException(STL10009_ERROR_READING_FILE, e,
                                StringUtils.getErrorMessage(e));
                log.error(dbe.getMessage(), e);
                throw dbe;
            }
        } else {
            DatabaseException dbe =
                    new DatabaseException(
                            STL30003_DATABASE_DEFINITION_FILE_NOT_FOUND);
            throw dbe;
        }
        return timestamp;
    }

    public static long getDatabaseTimestamp(File timestampFile)
            throws DatabaseException {
        long timestamp = 0;
        try {
            BufferedReader in =
                    new BufferedReader(new FileReader(timestampFile));
            try {
                String line = in.readLine();
                if (line != null) {
                    timestamp = Long.parseLong(line);
                }
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            DatabaseException dbe =
                    new DatabaseException(STL10009_ERROR_READING_FILE, e,
                            timestampFile.getAbsolutePath(),
                            StringUtils.getErrorMessage(e));
            log.error(dbe.getMessage(), e);
            throw dbe;
        }
        return timestamp;
    }

}
