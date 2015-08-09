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
 *  File Name: LogbackConfigurationHelper.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/02/03 04:44:40  jijunwan
 *  Archive Log:    fixed NPE issues found by clocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/08 22:41:18  fernande
 *  Archive Log:    Logback configuration support
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/12/17 21:40:46  fernande
 *  Archive Log:    Fix for saving settings.xml file
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/17 21:17:43  fernande
 *  Archive Log:    Backend changes to update log config.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/15 20:44:49  fernande
 *  Archive Log:    Initial changes to the logging configuration backend support for logback.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration.impl;

import static com.intel.stl.common.AppDataUtils.FM_GUI_DIR;
import static com.intel.stl.common.AppDataUtils.LOGCONFIG_FILE;
import static com.intel.stl.common.STLMessages.STL50002_ERROR_READING_LOG4JPROPERTIES;
import static com.intel.stl.common.STLMessages.STL50004_UNSUPPORTED_APPENDER_TYPE;
import static com.intel.stl.common.STLMessages.STL50005_ERROR_UPDATING_LOGGING_CONFIG;
import static com.intel.stl.common.STLMessages.STL50007_ERROR_UPDATING_CUSTOMSETTINGS;
import static com.intel.stl.configuration.AppSettings.APP_LOG_FOLDER;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.configuration.AppenderConfig;
import com.intel.stl.api.configuration.ConfigurationException;
import com.intel.stl.api.configuration.ConsoleAppender;
import com.intel.stl.api.configuration.RollingFileAppender;
import com.intel.stl.common.AppDataUtils;

public class LogbackConfigurationHelper {

    private static Logger log = LoggerFactory
            .getLogger(LogbackConfigurationHelper.class);

    private static final String FMGUI_APPENDER = "RollingFile-1";

    private static final String LOGDIR_SYS_PROP = "${" + FM_GUI_DIR + "}";

    protected static String logbackConfigFileName = LOGCONFIG_FILE;

    protected static String LOGCONFIG_TEMP_XML = "logconfignew.xml";

    private static final Set<String> internalLogs;

    static {
        Set<String> logs = new HashSet<String>();
        logs.add("HibernateSql");
        logs.add("DbMgr");
        internalLogs = Collections.unmodifiableSet(logs);
    }

    public static List<AppenderConfig> getLoggingConfiguration(
            String appDataPath) {
        String logConfigurationFilePath =
                appDataPath + File.separatorChar + logbackConfigFileName;
        File configFile = getConfigFile(logConfigurationFilePath);
        return getLoggingConfiguration(configFile);
    }

    public static void updateLoggingConfiguration(String appDataPath,
            List<AppenderConfig> config) {
        String logConfigurationFilePath =
                appDataPath + File.separatorChar + logbackConfigFileName;
        File configFile = getConfigFile(logConfigurationFilePath);
        saveLoggingConfiguration(config, configFile, appDataPath);
    }

    public static List<AppenderConfig> getLoggingConfiguration(File logConfig) {
        List<AppenderConfig> appenders = new ArrayList<AppenderConfig>();
        LogbackConfigFactory factory = new LogbackConfigFactory(logConfig);
        NodeList nodes = factory.getAppenders();
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            String appenderName = e.getAttribute("name");
            if (internalLogs.contains(appenderName)) {
                continue;
            }
            String appenderClass = e.getAttribute("class");
            SupportedAppenderType type =
                    SupportedAppenderType
                            .getSupportedAppenderTypeFor(appenderClass);
            if (type == null) {
                log.error(STL50004_UNSUPPORTED_APPENDER_TYPE
                        .getDescription(appenderClass), e);
            } else {
                AppenderConfig appender = null;
                switch (type) {
                    case CONSOLE_APPENDER: {
                        ConsoleAppender consoleAppender = new ConsoleAppender();
                        consoleAppender.populateFromNode(e, factory);
                        appender = consoleAppender;
                        break;
                    }
                    case ROLLINGFILE_APPENDER: {
                        RollingFileAppender fileAppender =
                                new RollingFileAppender();
                        fileAppender.populateFromNode(e, factory);
                        resetFileLocationIfNeeded(fileAppender);
                        appender = fileAppender;
                        break;
                    }
                }
                appenders.add(appender);
            }
        }
        return appenders;
    }

    public static void saveLoggingConfiguration(List<AppenderConfig> config,
            File logConfig, String appDataPath) {
        LogbackConfigFactory factory = new LogbackConfigFactory(logConfig);

        updateDocument(factory, config, appDataPath);

        String parentFolder = logConfig.getParent();
        String tempLogConfig =
                parentFolder + File.separatorChar + LOGCONFIG_TEMP_XML;
        File tempFile = new File(tempLogConfig);

        factory.saveConfig(tempFile);

        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd_HH-mm-SS");
        Date now = new Date();
        String reName = LOGCONFIG_FILE + "." + formatter.format(now);
        String logConfigBackup = parentFolder + File.separatorChar + reName;
        File backupFile = new File(logConfigBackup);

        try {
            boolean renameOK = logConfig.renameTo(backupFile);
            renameOK = tempFile.renameTo(logConfig);
        } catch (SecurityException se) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50005_ERROR_UPDATING_LOGGING_CONFIG, se,
                            StringUtils.getErrorMessage(se));
            log.error(StringUtils.getErrorMessage(ce), se);
            throw ce;
        }
    }

    protected static void updateDocument(LogbackConfigFactory factory,
            List<AppenderConfig> config, String appDataPath) {
        if (config == null) {
            return;
        }
        NodeList nodes = factory.getAppenders();
        Map<String, AppenderConfig> appenders =
                new HashMap<String, AppenderConfig>();
        for (AppenderConfig appender : config) {
            String appenderName = appender.getName();
            appenders.put(appenderName, appender);
        }
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            String appenderName = e.getAttribute("name");
            if (FMGUI_APPENDER.equals(appenderName)) {
                // We need to reset FM_GUI_DIR before anything else because
                // FileLocation is replaced with the environment variable when
                // we update the DOM
                AppenderConfig appender = appenders.get(appenderName);
                if (appender != null) {
                    resetLogDir((RollingFileAppender) appender, appDataPath);
                }
                break;
            }
        }
        Element lastUserAppender = null;
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            String appenderName = e.getAttribute("name");
            if (internalLogs.contains(appenderName)) {
                continue;
            }
            lastUserAppender = e;
            AppenderConfig appender = appenders.get(appenderName);
            if (appender == null) {
                Node parent = e.getParentNode();
                parent.removeChild(e);
                log.info("Removing appender '" + appenderName
                        + "' from logging configuration.");
            } else {
                appender.updateNode(e, factory);
                appenders.remove(appenderName);
            }
        }
        if (appenders.size() > 0 && lastUserAppender != null) {
            for (String name : appenders.keySet()) {
                AppenderConfig newAppender = appenders.get(name);
                Node newElement = newAppender.createNode(factory);
                lastUserAppender.getParentNode().insertBefore(newElement,
                        lastUserAppender.getNextSibling());
            }

        }
    }

    private static void resetFileLocationIfNeeded(RollingFileAppender appender) {
        String location = appender.getFileLocation();
        String logDir = System.getProperty(FM_GUI_DIR);
        if (location != null && location.contains(LOGDIR_SYS_PROP)) {
            location = location.replace(LOGDIR_SYS_PROP, logDir);
            location = location.replace("/", File.separator);
            appender.setFileLocation(location);
        }
        String namePattern = appender.getFileNamePattern();
        if (namePattern != null && namePattern.contains(LOGDIR_SYS_PROP)) {
            namePattern = namePattern.replace(LOGDIR_SYS_PROP, logDir);
            namePattern = namePattern.replace("/", File.separator);
            appender.setFileNamePattern(namePattern);
        }
    }

    private static void resetLogDir(RollingFileAppender appender,
            String appDataPath) {
        File logFile = new File(appender.getFileLocation());
        String logDir = System.getProperty(FM_GUI_DIR);
        String filePath = logFile.getParent();
        if (!filePath.equals(logDir)) {
            try {
                Properties customSettings =
                        AppDataUtils.getCustomSettings(appDataPath);
                customSettings.put(APP_LOG_FOLDER, filePath);
                AppDataUtils.saveCustomSettings(customSettings, appDataPath);
                System.setProperty(FM_GUI_DIR,
                        filePath.replace(File.separator, "/"));
            } catch (Exception e) {
                ConfigurationException ce =
                        new ConfigurationException(
                                STL50007_ERROR_UPDATING_CUSTOMSETTINGS, e,
                                StringUtils.getErrorMessage(e));
                log.error(StringUtils.getErrorMessage(ce), e);
                throw ce;
            }
        }
    }

    private static File getConfigFile(String configFilePath) {
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50002_ERROR_READING_LOG4JPROPERTIES, 0,
                            "File not found");
            log.error(StringUtils.getErrorMessage(ce));
            throw ce;
        }
        return configFile;
    }

}
