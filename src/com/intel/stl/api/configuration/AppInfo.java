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
 *  File Name: AppInfo.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3.2.1  2015/08/12 15:21:40  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
