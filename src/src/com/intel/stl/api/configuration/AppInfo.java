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
 *  Archive Log:    Revision 1.9  2015/09/30 12:47:05  fisherma
 *  Archive Log:    PR 129357 - ability to hide inactive ports.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/09/02 15:55:31  fernande
 *  Archive Log:    PR 130220 - FM GUI "about" window displays unmatched version and build #. Passing the OPA FM version thru the manifest.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/08/17 18:48:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/08/10 17:04:40  robertja
 *  Archive Log:    PR128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/07/10 20:40:58  fernande
 *  Archive Log:    PR 129522 - Notice is not written to database due to topology not found. Added constants for use in UI
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/07/09 18:40:19  fernande
 *  Archive Log:    PR 129447 - Database size increases a lot over a short period of time. Added method to expose application settings in the settings.xml file to higher levels in the app
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
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class AppInfo implements Serializable {

    public final static String PROPERTIES_SUBNET_FRAMES = "SubnetFrames";

    public final static String PROPERTIES_SUBNET_STATE_SUFFIX = "-State";

    public final static String PROPERTIES_SMTP_SETTINGS = "SMTPSettings";

    public final static String PROPERTIES_FM_GUI_APP = "FMGUIApp";

    private static final long serialVersionUID = 1L;

    public static final String PROPERTIES_DATABASE = "Database";

    private int appVersion;

    private int appRelease;

    private int appModLevel;

    private int appSchemaLevel;

    private String appName;

    private String opaFmVersion;

    private String appBuildId;

    private String appBuildDate;

    private Map<String, Properties> propertiesMap =
            new ConcurrentHashMap<String, Properties>();

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 
     * <i>Description:</i> returns the FM GUI internal version number
     * 
     * @return
     */
    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * 
     * <i>Description:</i> returns the FM GUI internal release number
     * 
     * @return
     */
    public int getAppRelease() {
        return appRelease;
    }

    public void setAppRelease(int appRelease) {
        this.appRelease = appRelease;
    }

    /**
     * 
     * <i>Description:</i> returns the FM GUI internal modification level
     * 
     * @return
     */
    public int getAppModLevel() {
        return appModLevel;
    }

    public void setAppModLevel(int appModLevel) {
        this.appModLevel = appModLevel;
    }

    /**
     * 
     * <i>Description:</i> returns the Build Id, as set in RELEASE_TAG
     * 
     * @return
     */
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

    /**
     * 
     * <i>Description:</i> returns the application database schema level
     * 
     * @return
     */
    public int getAppSchemaLevel() {
        return appSchemaLevel;
    }

    public void setAppSchemaLevel(int appSchemaLevel) {
        this.appSchemaLevel = appSchemaLevel;
    }

    /**
     * 
     * <i>Description:</i> returns the OPA FM version corresponding to this
     * release of the FM GUI
     * 
     * @return the OPA FM version
     */
    public String getOpaFmVersion() {
        return opaFmVersion;
    }

    /**
     * 
     * <i>Description:</i> set the OPA FM version
     * 
     * @param opaFmVersion
     */
    public void setOpaFmVersion(String opaFmVersion) {
        this.opaFmVersion = opaFmVersion;
    }

    public Map<String, Properties> getPropertiesMap() {
        return this.propertiesMap;
    }

    public void setPropertiesMap(Map<String, Properties> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

    public void setProperty(String string, Properties properties) {
        if ((string != null) && (properties != null)) {
            propertiesMap.put(string, properties);
        }
    }

    public Properties getProperty(String string) {
        if (string != null) {
            return propertiesMap.get(string);
        } else {
            return new Properties();
        }
    }

}
