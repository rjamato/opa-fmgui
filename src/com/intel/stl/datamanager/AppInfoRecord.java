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
 *  Archive Log:    Revision 1.2  2015/02/17 19:39:01  fernande
 *  Archive Log:    Adding column in the AppInfoRecord to save UserOptionsXml. Only the Preferences field is being used, everything else is taken from the UserOptions default.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/11/07 22:08:12  fernande
 *  Archive Log:    Adding MANIFEST.MF processing to retrieve application information (version, build number)
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/25 20:31:42  fernande
 *  Archive Log:    Added support for Hibernate
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.datamanager;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.intel.stl.api.configuration.AppInfo;

@Entity
@Table(name = "APPINFO")
public class AppInfoRecord extends DatabaseRecord {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 8)
    private String appId;

    // Maximum size of XML document is 20K; currently, the default UserOptions
    // is 3K
    @Lob
    @Column(length = 20480)
    private String propertiesXml;

    @Embedded
    private AppInfo appInfo;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPropertiesXml() {
        return propertiesXml;
    }

    public void setPropertiesXml(String propertiesXml) {
        this.propertiesXml = propertiesXml;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime
                        * result
                        + ((appId == null) ? 0 : appId.toLowerCase().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AppInfoRecord other = (AppInfoRecord) obj;
        if (appId == null) {
            if (other.appId != null)
                return false;
        } else if (!appId.equalsIgnoreCase(other.appId))
            return false;
        return true;
    }
}
