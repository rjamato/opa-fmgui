/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: Applications.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/24 17:33:20  jijunwan
 *  Archive Log:    introduced IAttribute for attributes defined in xml file
 *  Archive Log:    changed all attributes for Appliation and DG to be an IAttribute
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/16 22:00:57  jijunwan
 *  Archive Log:    changed package name from application to applications, and from devicegroup to devicegroups
 *  Archive Log:    Added #getType to ServiceID, MGID, LongNode and their subclasses,
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/13 20:56:59  jijunwan
 *  Archive Log:    minor  improvement on FM Application
 *  Archive Log:    Added support on FM DeviceGroup
 *  Archive Log:    put all constants used in xml file to XMLConstants
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:30:36  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:    1) read/write opafm.xml from/to host with backup file support
 *  Archive Log:    2) Application parser
 *  Archive Log:    3) Add/remove and update Application
 *  Archive Log:    4) unique name, reference conflication check
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management.applications;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.intel.stl.api.management.XMLConstants;

@XmlRootElement(name = XMLConstants.APPLICATIONS)
@XmlAccessorType(XmlAccessType.FIELD)
public class Applications {
    @XmlElement(name = XMLConstants.APPLICATION)
    private List<Application> applications;

    public Application getApplication(String name) {
        for (Application app : applications) {
            if (app.getName().equals(name)) {
                return app;
            }
        }
        throw new IllegalArgumentException(
                "Counldn't find Application by name '" + name + "'");
    }

    /**
     * @return the applications
     */
    public List<Application> getApplications() {
        return applications;
    }

    /**
     * @param applications
     *            the applications to set
     */
    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    /**
     * 
     * <i>Description:</i> find applications whose included application list
     * contains the specified application name
     * 
     * @param name
     *            the application name to check
     * @return a list of applications that point to the specified application
     */
    public List<Application> getReferencedApplications(String name) {
        List<Application> res = new ArrayList<Application>();
        IncludeApplication ia = new IncludeApplication(name);
        for (Application app : applications) {
            List<IncludeApplication> children = app.getIncludeApplications();
            if (children != null && children.contains(ia)) {
                res.add(app);
            }
        }
        return res;
    }
}
