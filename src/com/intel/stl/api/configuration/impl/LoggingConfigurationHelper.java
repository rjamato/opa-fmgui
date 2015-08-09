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
 *  File Name: LoggingConfigurationHelper.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/02/03 04:44:40  jijunwan
 *  Archive Log:    fixed NPE issues found by clocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/01/08 22:41:18  fernande
 *  Archive Log:    Logback configuration support
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/12/17 21:17:42  fernande
 *  Archive Log:    Backend changes to update log config.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/12/15 20:44:48  fernande
 *  Archive Log:    Initial changes to the logging configuration backend support for logback.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/11 18:31:42  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/12 20:07:40  jijunwan
 *  Archive Log:    1) renamed HexUtils to StringUtils
 *  Archive Log:    2) added a method to StringUtils to get error message for an exception
 *  Archive Log:    3) changed all code to call StringUtils to get error message
 *  Archive Log:    4) some extra ode format change
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/19 20:01:16  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/13 19:01:57  fernande
 *  Archive Log:    Implemented saveLoggingConfiguration and getLoggingConfiguration
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration.impl;

import static com.intel.stl.api.configuration.Log4jSetting.APPENDER_TYPE;
import static com.intel.stl.api.configuration.Log4jSetting.FILE;
import static com.intel.stl.common.AppDataUtils.FM_GUI_DIR;
import static com.intel.stl.common.AppDataUtils.LOGCONFIG_FILE;
import static com.intel.stl.common.STLMessages.STL50001_ERROR_PARSING_LOGGING_CONFIG;
import static com.intel.stl.common.STLMessages.STL50002_ERROR_READING_LOG4JPROPERTIES;
import static com.intel.stl.common.STLMessages.STL50003_ERROR_CLOSING_LOG4JPROPERTIES;
import static com.intel.stl.common.STLMessages.STL50004_UNSUPPORTED_APPENDER_TYPE;
import static com.intel.stl.common.STLMessages.STL50005_ERROR_UPDATING_LOGGING_CONFIG;
import static com.intel.stl.common.STLMessages.STL50006_UNKNOWN_LOG4J_SETTING;
import static com.intel.stl.common.STLMessages.STL50007_ERROR_UPDATING_CUSTOMSETTINGS;
import static com.intel.stl.configuration.AppSettings.APP_LOG_FOLDER;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.configuration.AppenderConfig;
import com.intel.stl.api.configuration.ConfigurationException;
import com.intel.stl.api.configuration.ConsoleAppender;
import com.intel.stl.api.configuration.Log4jSetting;
import com.intel.stl.api.configuration.LoggingConfiguration;
import com.intel.stl.api.configuration.RollingFileAppender;
import com.intel.stl.common.AppDataUtils;

public class LoggingConfigurationHelper {

    private static final String LOG4J_APPENDER_PREFIX = "log4j.appender.";

    private static final String DEFAULT_LOG_FILENAME = "FabricViewer.log";

    private static final String LOGDIR_SYS_PROP = "${" + FM_GUI_DIR + "}";

    protected static String log4jPropertiesFileName = LOGCONFIG_FILE;

    private static Logger log = LoggerFactory
            .getLogger(LoggingConfigurationHelper.class);

    protected static String LOG4J_TEMP_PROPERTIES = "log4jnew.properties";

    public static LoggingConfiguration getLoggingConfiguration(
            String appDataPath) throws ConfigurationException {
        String log4jPropertiesFilePath =
                appDataPath + File.separatorChar + log4jPropertiesFileName;
        File propertiesFile = new File(log4jPropertiesFilePath);
        BufferedReader input = getLog4jPropertiesReader(propertiesFile);
        List<String> appenderNames = getAppenderNames();
        List<Map<String, String>> appenderConfigs =
                parseLog4jProperties(appenderNames, input);

        LoggingConfiguration loggingConf = new LoggingConfiguration();
        List<AppenderConfig> appenders = new ArrayList<AppenderConfig>();

        for (int i = 0; i < appenderNames.size(); i++) {
            String name = appenderNames.get(i);
            Map<String, String> appenderConfig = appenderConfigs.get(i);
            SupportedAppenderType type =
                    SupportedAppenderType
                            .getSupportedAppenderTypeFor(appenderConfig
                                    .get(APPENDER_TYPE.getValue()));
            if (type == null) {
                ConfigurationException ce =
                        new ConfigurationException(
                                STL50004_UNSUPPORTED_APPENDER_TYPE);
                log.error(StringUtils.getErrorMessage(ce));
                throw ce;
            }
            AppenderConfig appender = null;
            switch (type) {
                case CONSOLE_APPENDER: {
                    appender = new ConsoleAppender();
                    break;
                }
                case ROLLINGFILE_APPENDER: {
                    appender = new RollingFileAppender();
                    break;
                }
            }
            if (appender != null) {
                appender.setName(name);
                appenders.add(appender);
            }
        }
        loggingConf.setAppenders(appenders);
        return loggingConf;
    }

    public static void updateLoggingConfiguration(String appDataPath,
            LoggingConfiguration config) throws ConfigurationException {
        String log4jPropertiesFilePath =
                appDataPath + File.separatorChar + log4jPropertiesFileName;
        File propertiesFile = new File(log4jPropertiesFilePath);
        String parentFolder = propertiesFile.getParent();
        String tempLog4jProperties =
                parentFolder + File.separatorChar + LOG4J_TEMP_PROPERTIES;
        File tempFile = new File(tempLog4jProperties);
        BufferedReader input = getLog4jPropertiesReader(propertiesFile);
        try {
            Writer output = getLog4jPropertiesWriter(tempFile);
            updateLog4jProperties(config, input, output, appDataPath);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                String emsg =
                        STL50003_ERROR_CLOSING_LOG4JPROPERTIES.getDescription();
                log.error(emsg, e);
            }
        }

        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd_HH-mm-SS");
        Date now = new Date();
        String reName = log4jPropertiesFileName + "." + formatter.format(now);
        String log4jPropertiesBackup =
                appDataPath + File.separatorChar + reName;
        File backupFile = new File(log4jPropertiesBackup);

        try {
            boolean renameOK = propertiesFile.renameTo(backupFile);
            renameOK = tempFile.renameTo(propertiesFile);
        } catch (SecurityException se) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50005_ERROR_UPDATING_LOGGING_CONFIG, se,
                            StringUtils.getErrorMessage(se));
            log.error(StringUtils.getErrorMessage(ce), se);
            throw ce;
        }
    }

    protected static List<Map<String, String>> parseLog4jProperties(
            List<String> appenderNames, BufferedReader input)
            throws ConfigurationException {
        int size = appenderNames.size();
        String[] appenderPrefix = new String[size];
        List<Map<String, String>> appenderConfigs =
                new ArrayList<Map<String, String>>();
        for (int i = 0; i < appenderNames.size(); i++) {
            String appenderName = appenderNames.get(i);
            appenderPrefix[i] = LOG4J_APPENDER_PREFIX + appenderName;
            Map<String, String> appenderConfig = new HashMap<String, String>();
            appenderConfigs.add(appenderConfig);
        }
        String line = null;
        int lineNum = 0;
        try {
            while ((line = input.readLine()) != null) {
                lineNum++;
                if ((line.length() == 0) || (line.charAt(0) == '#')
                        || (line.charAt(0) == '!')) {
                    continue;
                } else {
                    for (int i = 0; i < size; i++) {
                        if (line.length() > appenderPrefix[i].length()) {
                            if (line.startsWith(appenderPrefix[i])) {
                                Map<String, String> appenderConfig =
                                        appenderConfigs.get(i);
                                parseLine(line, appenderPrefix[i],
                                        appenderConfig);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50002_ERROR_READING_LOG4JPROPERTIES, e, lineNum,
                            StringUtils.getErrorMessage(e));
            log.error(StringUtils.getErrorMessage(ce), e);
            throw ce;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                String emsg =
                        STL50003_ERROR_CLOSING_LOG4JPROPERTIES.getDescription();
                log.error(emsg, e);
            }
        }
        return appenderConfigs;
    }

    protected static void updateLog4jProperties(LoggingConfiguration config,
            BufferedReader input, Writer output, String appDataPath)
            throws ConfigurationException {
        List<AppenderConfig> appenders = config.getAppenders();
        int size = appenders.size();
        String[] appenderPrefix = new String[size];
        boolean[] appenderUpdated = new boolean[size];
        for (int i = 0; i < size; i++) {
            String appenderName = appenders.get(i).getName();
            appenderPrefix[i] = LOG4J_APPENDER_PREFIX + appenderName;
            appenderUpdated[i] = false;
        }

        try {
            String log4jUpdated =
                    getUpdatedLog4jProperties(input, appenders, appenderPrefix,
                            appenderUpdated, appDataPath);
            output.write(log4jUpdated);
        } catch (IOException e) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50005_ERROR_UPDATING_LOGGING_CONFIG, e,
                            StringUtils.getErrorMessage(e));
            log.error(StringUtils.getErrorMessage(ce), e);
            throw ce;
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                log.error(StringUtils.getErrorMessage(e), e);
            }
        }
    }

    private static String getUpdatedLog4jProperties(BufferedReader input,
            List<AppenderConfig> appenders, String[] appenderPrefix,
            boolean[] appenderUpdated, String appDataPath)
            throws ConfigurationException {
        String line = null;
        int lineNum = 0;
        int size = appenderPrefix.length;
        String ls = System.getProperty("line.separator");
        StringBuilder contents = new StringBuilder();
        try {
            while ((line = input.readLine()) != null) {
                lineNum++;
                if ((line.length() == 0) || (line.charAt(0) == '#')
                        || (line.charAt(0) == '!')) {
                    contents.append(line);
                    contents.append(ls);
                } else {
                    boolean matched = false;
                    for (int i = 0; i < size; i++) {
                        if (line.length() > appenderPrefix[i].length()) {
                            if (line.startsWith(appenderPrefix[i])) {
                                matched = true;
                                if (!appenderUpdated[i]) {
                                    outputAppenderConfig(appenders.get(i),
                                            contents, appDataPath);
                                    appenderUpdated[i] = true;
                                }
                                break;
                            }
                        }
                    }
                    if (!matched) {
                        contents.append(line);
                        contents.append(ls);
                    }
                }
            }
        } catch (IOException e) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50002_ERROR_READING_LOG4JPROPERTIES, e, lineNum,
                            StringUtils.getErrorMessage(e));
            log.error(StringUtils.getErrorMessage(ce), e);
            throw ce;
        }
        return contents.toString();
    }

    private static void outputAppenderConfig(AppenderConfig config,
            StringBuilder contents, String appDataPath)
            throws ConfigurationException {
        // Keeping the log4j code
        // Map<String, String> settings = config.getSettings();
        Map<String, String> settings = new HashMap<String, String>();
        Set<String> keys = settings.keySet();
        String ls = System.getProperty("line.separator");
        String appenderName = config.getName();
        String logDir = System.getProperty(FM_GUI_DIR);
        if (logDir != null) {
            logDir = logDir.replace("/", File.separator);
        }
        String appenderClass = "";

        contents.append(LOG4J_APPENDER_PREFIX);
        contents.append(appenderName);
        contents.append('=');
        contents.append(appenderClass);
        contents.append(ls);
        for (String key : keys) {
            if (key.equals(APPENDER_TYPE.getValue())) {
                continue;
            } else {
                contents.append(LOG4J_APPENDER_PREFIX);
                contents.append(appenderName);
                contents.append('.');
                contents.append(key);
                contents.append('=');
                if (key.equals(FILE.getValue())) {
                    String logFileName = settings.get(key);
                    File logFile = new File(logFileName);
                    String fileName = logFile.getName();
                    String log4jFileLocation = LOGDIR_SYS_PROP + "/" + fileName;
                    contents.append(log4jFileLocation);
                    String filePath = logFile.getParent();
                    if (!filePath.equals(logDir)) {
                        try {
                            Properties customSettings =
                                    AppDataUtils.getCustomSettings(appDataPath);
                            customSettings.put(APP_LOG_FOLDER, filePath);
                            AppDataUtils.saveCustomSettings(customSettings,
                                    appDataPath);
                            System.setProperty(FM_GUI_DIR,
                                    filePath.replace(File.separator, "/"));
                        } catch (Exception e) {
                            ConfigurationException ce =
                                    new ConfigurationException(
                                            STL50007_ERROR_UPDATING_CUSTOMSETTINGS,
                                            e, StringUtils.getErrorMessage(e));
                            log.error(StringUtils.getErrorMessage(ce), e);
                            throw ce;
                        }
                    }
                } else {
                    contents.append(settings.get(key));
                }
                contents.append(ls);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static List<String> getAppenderNames() {
        // Logger root = Logger.getRootLogger();
        // Enumeration<Appender> appenders = root.getAllAppenders();
        List<String> appenderNames = new ArrayList<String>();
        // while (appenders.hasMoreElements()) {
        // Appender appender = appenders.nextElement();
        // appenderNames.add(appender.getName());
        // }
        return appenderNames;
    }

    private static BufferedReader getLog4jPropertiesReader(File file)
            throws ConfigurationException {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50002_ERROR_READING_LOG4JPROPERTIES, e, 0,
                            StringUtils.getErrorMessage(e));
            log.error(StringUtils.getErrorMessage(ce), e);
            throw ce;
        }
        return input;
    }

    private static Writer getLog4jPropertiesWriter(File file)
            throws ConfigurationException {
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50005_ERROR_UPDATING_LOGGING_CONFIG, e,
                            StringUtils.getErrorMessage(e));
            log.error(StringUtils.getErrorMessage(ce), e);
            throw ce;
        }
        return output;
    }

    protected static void parseLine(String line, String prefix,
            Map<String, String> config) throws ConfigurationException {
        int l = line.length();
        int s = prefix.length();
        int x = s;
        while ((x < l)
                && ((line.charAt(x) != ' ') && (line.charAt(x) != '=') && (line
                        .charAt(x) != ':'))) {
            x++;
        }

        String logDir = System.getProperty(FM_GUI_DIR);
        String setting = "";
        Log4jSetting log4jSetting = null;
        if (x > s) {
            if (line.charAt(s) == '.' && (x - s) > 1) {
                String settingKey = line.substring(s + 1, x).toLowerCase();
                log4jSetting = Log4jSetting.getLog4jSettingFor(settingKey);
                if (log4jSetting == null) {
                    setting = settingKey;
                    log.warn(STL50006_UNKNOWN_LOG4J_SETTING
                            .getDescription(setting));
                } else {
                    setting = log4jSetting.getValue();
                }
            } else {
                logParsingError(line, s);
            }
        } else {
            if (line.charAt(x) == ' ' || line.charAt(x) == '='
                    || line.charAt(x) == ':') {
                setting = APPENDER_TYPE.getValue();
            }
        }
        while ((x < l) && (line.charAt(x) == ' ')) {
            x++;
        }
        if ((x < l) && (line.charAt(x) == '=' || line.charAt(x) == ':')) {
            x++;
            String value = "";
            if (x < l) {
                value = line.substring(x);
                value = value.trim();
            }
            if (setting.length() > 0 && value.length() > 0) {
                if (log4jSetting == FILE) {
                    if (value.contains(LOGDIR_SYS_PROP)) {
                        value = value.replace(LOGDIR_SYS_PROP, logDir);
                        value = value.replace("/", File.separator);
                    }
                }
                config.put(setting, value);
            }
        } else {
            logParsingError(line, x);
        }
    }

    private static void logParsingError(String line, int offset)
            throws ConfigurationException {
        String markedLine = "";
        int l = line.length();
        if (offset < (l - 1)) {
            markedLine =
                    line.substring(0, offset) + "["
                            + line.substring(offset, offset + 1) + "]"
                            + line.substring(offset + 1);
        } else {
            if (offset < l) {
                markedLine =
                        line.substring(0, offset - 1) + "["
                                + line.substring(offset, offset + 1) + "]";
            } else {
                markedLine = line + "[]";
            }
        }
        ConfigurationException ce =
                new ConfigurationException(
                        STL50001_ERROR_PARSING_LOGGING_CONFIG, offset,
                        markedLine);
        log.error(StringUtils.getErrorMessage(ce));
        throw ce;
    }

}
