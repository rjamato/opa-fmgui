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
 *  File Name: AppInfo.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/03/30 22:28:49  jijunwan
 *  Archive Log:    1) improved AppInfo to include buildDate
 *  Archive Log:    2) fixed DBMgr to update AppInfo by buildId or BuildDate
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/17 19:34:08  fernande
 *  Archive Log:    Added a map of Properties to hold application wide information
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/11/07 22:06:17  fernande
 *  Archive Log:    Adding AppInfo to the API to retrieve version information
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AppInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int appVersion;

    private int appRelease;

    private int appModLevel;

    private int appSchemaLevel;

    private String appName;

    private String appBuildId;

    private String appBuildDate;

    private Map<String, Properties> properties =
            new HashMap<String, Properties>();

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public int getAppRelease() {
        return appRelease;
    }

    public void setAppRelease(int appRelease) {
        this.appRelease = appRelease;
    }

    public int getAppModLevel() {
        return appModLevel;
    }

    public void setAppModLevel(int appModLevel) {
        this.appModLevel = appModLevel;
    }

    public String getAppBuildId() {
        return appBuildId;
    }

    public void setAppBuildId(String appBuildId) {
        this.appBuildId = appBuildId;
    }

    /**
     * @return the appBuildDate
     */
    public String getAppBuildDate() {
        return appBuildDate;
    }

    /**
     * @param appBuildDate
     *            the appBuildDate to set
     */
    public void setAppBuildDate(String appBuildDate) {
        this.appBuildDate = appBuildDate;
    }

    public int getAppSchemaLevel() {
        return appSchemaLevel;
    }

    public void setAppSchemaLevel(int appSchemaLevel) {
        this.appSchemaLevel = appSchemaLevel;
    }

    public Map<String, Properties> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Properties> properties) {
        this.properties = properties;
    }

}