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

package com.intel.stl.configuration;

import java.util.Properties;
import java.util.Set;

import com.intel.stl.common.STLMessages;

/**
 * @author Fernando Fernandez
 * 
 */
public class AppSettings {

    // Application configuration options
    public static final String APP_DB_SUBNET = "app.db.subnet";

    public static final String APP_DB_SUBNET_INCLUDE_INACTIVE =
            "app.db.subnet.include_inactive";

    public static final String APP_DB_PATH = "app.db.path";

    public static final String APP_UI_PLUGIN = "app.ui.plugin";

    public static final String APP_LOG_FOLDER = "app.log.folder";

    // Database configuration options
    public static final String DB_ENGINE = "db.engine";

    public static final String DB_NAME = "db.name";

    public static final String DB_PERSISTENCE_PROVIDER =
            "db.persistence.provider";

    public static final String DB_PERSISTENCE_PROVIDER_NAME =
            "db.persistence.provider.name";

    public static final String DB_CONNECTION_DRIVER = "db.connection.driver";

    public static final String DB_CONNECTION_URL = "db.connection.url";

    public static final String DB_CONNECTION_USER = "db.connection.user";

    public static final String DB_CONNECTION_PASSWORD =
            "db.connection.password";

    public static final String DB_HIBERNATE_DIALECT = "db.hibernate.dialect";

    // The following application versioning values are set from the manifest and
    // cannot be overwritten
    public static final String APP_NAME = "app.name";

    public static final String APP_VERSION = "app.version";

    public static final String APP_RELEASE = "app.release";

    public static final String APP_MODLEVEL = "app.modlevel";

    public static final String APP_BUILD_ID = "app.build.id";

    public static final String APP_BUILD_DATE = "app.build.date";

    public static final String APP_SCHEMA_LEVEL = "app.schema.level";

    // The following application configuration options are set internally and
    // cannot be overwritten
    public static final String APP_INTEL_PATH = "app.intel.path";

    public static final String APP_DATA_PATH = "app.data.path";

    private final Properties properties;

    public AppSettings(Properties properties) {
        this.properties = properties;
    }

    public String getConfigOption(String option)
            throws AppConfigurationException {
        checkSettingExists(option);
        return properties.getProperty(option);
    }

    public void setConfigOption(String option, String value) {
        properties.put(option, value);
    }

    public void setConfigOption(String option, Object value) {
        properties.put(option, value);
    }

    public void removeConfigOption(String option) {
        properties.remove(option);
    }

    public Set<Object> keySet() {
        return properties.keySet();
    }

    public String getAppName() throws AppConfigurationException {
        checkSettingExists(APP_NAME);
        return (String) properties.get(APP_NAME);
    }

    public Integer getAppVersion() throws AppConfigurationException {
        checkSettingExists(APP_VERSION);
        return (Integer) properties.get(APP_VERSION);
    }

    public Integer getAppRelease() throws AppConfigurationException {
        checkSettingExists(APP_RELEASE);
        return (Integer) properties.get(APP_RELEASE);
    }

    public Integer getAppModLevel() throws AppConfigurationException {
        checkSettingExists(APP_MODLEVEL);
        return (Integer) properties.get(APP_MODLEVEL);
    }

    public String getAppBuildId() throws AppConfigurationException {
        checkSettingExists(APP_BUILD_ID);
        return (String) properties.get(APP_BUILD_ID);
    }

    public String getAppBuildDate() throws AppConfigurationException {
        checkSettingExists(APP_BUILD_DATE);
        return (String) properties.get(APP_BUILD_DATE);
    }

    public Integer getAppSchemaLevel() throws AppConfigurationException {
        checkSettingExists(APP_SCHEMA_LEVEL);
        return (Integer) properties.get(APP_SCHEMA_LEVEL);
    }

    private void checkSettingExists(String setting)
            throws AppConfigurationException {
        if (!properties.containsKey(setting)) {
            AppConfigurationException ace =
                    new AppConfigurationException(
                            STLMessages.STL10007_CONFIGURATION_OPTION_NOT_SET
                                    .getDescription(setting));
            throw ace;
        }

    }

}
