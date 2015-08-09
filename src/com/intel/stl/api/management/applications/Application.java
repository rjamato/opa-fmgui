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
 *  File Name: ApplicatiobBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/03/27 15:43:26  jijunwan
 *  Archive Log:    improvement on #copy for Application, DeviceGroup and VirtualFabric
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/24 17:33:20  jijunwan
 *  Archive Log:    introduced IAttribute for attributes defined in xml file
 *  Archive Log:    changed all attributes for Appliation and DG to be an IAttribute
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/16 22:00:57  jijunwan
 *  Archive Log:    changed package name from application to applications, and from devicegroup to devicegroups
 *  Archive Log:    Added #getType to ServiceID, MGID, LongNode and their subclasses,
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/13 20:56:59  jijunwan
 *  Archive Log:    minor  improvement on FM Application
 *  Archive Log:    Added support on FM DeviceGroup
 *  Archive Log:    put all constants used in xml file to XMLConstants
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/10 22:41:44  jijunwan
 *  Archive Log:    improved to show progress while we log into a host
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
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.intel.stl.api.management.XMLConstants;

@XmlRootElement(name = XMLConstants.APPLICATION)
@XmlAccessorType(XmlAccessType.FIELD)
public class Application {

    @XmlElement(name = XMLConstants.NAME)
    private String name;

    @XmlElements({
            @XmlElement(name = XMLConstants.SERVICEID, type = ServiceID.class),
            @XmlElement(name = XMLConstants.SERVICEID_RANGE,
                    type = ServiceIDRange.class),
            @XmlElement(name = XMLConstants.SERVICEID_MASKED,
                    type = ServiceIDMasked.class) })
    private List<ServiceID> serviceIDs;

    @XmlElements({
            @XmlElement(name = XMLConstants.MGID, type = MGID.class),
            @XmlElement(name = XMLConstants.MGID_RANGE, type = MGIDRange.class),
            @XmlElement(name = XMLConstants.MGID_MASKED,
                    type = MGIDMasked.class) })
    private List<MGID> mgids;

    @XmlElement(name = XMLConstants.SELECT, type = String.class)
    @XmlJavaTypeAdapter(AppSelectAdapter.class)
    private List<AppSelect> selects;

    @XmlElement(name = XMLConstants.INCLUDE_APPLICATION,
            type = IncludeApplication.class)
    private List<IncludeApplication> includeApplications;

    /**
     * Description:
     * 
     */
    public Application() {
    }

    /**
     * Description:
     * 
     * @param name
     */
    public Application(String name) {
        super();
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public void addServiceID(ServiceID id) {
        if (id == null) {
            throw new IllegalArgumentException("null ServiceID is not allowed.");
        }

        if (serviceIDs == null) {
            serviceIDs = new ArrayList<ServiceID>();
        }
        serviceIDs.add(id);
    }

    /**
     * @return the serviceIDs
     */
    public List<ServiceID> getServiceIDs() {
        return serviceIDs;
    }

    public void addMGID(MGID id) {
        if (id == null) {
            throw new IllegalArgumentException("null MGID is not allowed.");
        }

        if (mgids == null) {
            mgids = new ArrayList<MGID>();
        }
        mgids.add(id);
    }

    /**
     * @return the mgids
     */
    public List<MGID> getMgids() {
        return mgids;
    }

    public void addSelect(AppSelect sel) {
        if (sel == null) {
            throw new IllegalArgumentException("null Select is not allowed.");
        }

        if (selects == null) {
            selects = new ArrayList<AppSelect>();
        }
        selects.add(sel);
    }

    /**
     * @return the selects
     */
    public List<AppSelect> getSelects() {
        return selects;
    }

    public void addIncludeApplication(IncludeApplication name) {
        if (name == null) {
            throw new IllegalArgumentException(
                    "null IncludedApplication is not allowed.");
        } else if (name.getValue().equals(this.name)) {
            throw new IllegalArgumentException("Can not reference to itself.");
        }

        if (includeApplications == null) {
            includeApplications = new ArrayList<IncludeApplication>();
        }
        includeApplications.add(name);
    }

    public void addIncludeApplication(String name) {
        if (name == null) {
            throw new IllegalArgumentException(
                    "null IncludedApplication is not allowed.");
        } else if (name.equals(this.name)) {
            throw new IllegalArgumentException("Can not reference to itself.");
        }

        if (includeApplications == null) {
            includeApplications = new ArrayList<IncludeApplication>();
        }
        includeApplications.add(new IncludeApplication(name));
    }

    public void removeIncludeApplication(String name) {
        IncludeApplication ia = new IncludeApplication(name);
        if (includeApplications != null) {
            includeApplications.remove(ia);
        }
    }

    public void insertIncludeApplication(int index, String name) {
        if (includeApplications != null) {
            includeApplications.add(index, new IncludeApplication(name));
        }
    }

    public int indexOfIncludeApplication(String name) {
        IncludeApplication ia = new IncludeApplication(name);
        if (includeApplications != null) {
            return includeApplications.indexOf(ia);
        }
        return -1;
    }

    public boolean doesIncludeApplication(String name) {
        IncludeApplication ia = new IncludeApplication(name);
        if (includeApplications != null) {
            return includeApplications.contains(ia);
        }
        return false;
    }

    /**
     * @return the includedApplications
     */
    public List<IncludeApplication> getIncludeApplications() {
        if (includeApplications == null) {
            return null;
        } else {
            return Collections.unmodifiableList(includeApplications);
        }
    }

    public void clear() {
        if (serviceIDs != null) {
            serviceIDs.clear();
        }
        if (mgids != null) {
            mgids.clear();
        }
        if (selects != null) {
            selects.clear();
        }
        if (includeApplications != null) {
            includeApplications.clear();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime
                        * result
                        + ((includeApplications == null) ? 0
                                : includeApplications.hashCode());
        result = prime * result + ((mgids == null) ? 0 : mgids.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((selects == null) ? 0 : selects.hashCode());
        result =
                prime * result
                        + ((serviceIDs == null) ? 0 : serviceIDs.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Application other = (Application) obj;
        if (includeApplications == null) {
            if (other.includeApplications != null) {
                return false;
            }
        } else if (!includeApplications.equals(other.includeApplications)) {
            return false;
        }
        if (mgids == null) {
            if (other.mgids != null) {
                return false;
            }
        } else if (!mgids.equals(other.mgids)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (selects == null) {
            if (other.selects != null) {
                return false;
            }
        } else if (!selects.equals(other.selects)) {
            return false;
        }
        if (serviceIDs == null) {
            if (other.serviceIDs != null) {
                return false;
            }
        } else if (!serviceIDs.equals(other.serviceIDs)) {
            return false;
        }
        return true;
    }

    public Application copy() {
        Application res = new Application(name);
        if (serviceIDs != null) {
            res.serviceIDs = new ArrayList<ServiceID>(serviceIDs.size());
            for (ServiceID sid : serviceIDs) {
                res.serviceIDs.add(sid.copy());
            }
        }
        if (mgids != null) {
            res.mgids = new ArrayList<MGID>(mgids.size());
            for (MGID mgid : mgids) {
                res.mgids.add(mgid.copy());
            }
        }
        if (selects != null) {
            res.selects = new ArrayList<AppSelect>(selects);
        }
        if (includeApplications != null) {
            res.includeApplications =
                    new ArrayList<IncludeApplication>(
                            includeApplications.size());
            for (IncludeApplication ia : includeApplications) {
                res.includeApplications.add(ia.copy());
            }
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Application [name=" + name + ", serviceIDs=" + serviceIDs
                + ", mgids=" + mgids + ", selects=" + selects
                + ", includeApplications=" + includeApplications + "]";
    }

}
