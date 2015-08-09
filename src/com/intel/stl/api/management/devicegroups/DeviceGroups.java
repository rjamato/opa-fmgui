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
 *  File Name: DeviceGroups.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/24 17:33:19  jijunwan
 *  Archive Log:    introduced IAttribute for attributes defined in xml file
 *  Archive Log:    changed all attributes for Appliation and DG to be an IAttribute
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/16 22:00:58  jijunwan
 *  Archive Log:    changed package name from application to applications, and from devicegroup to devicegroups
 *  Archive Log:    Added #getType to ServiceID, MGID, LongNode and their subclasses,
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/13 20:56:58  jijunwan
 *  Archive Log:    minor  improvement on FM Application
 *  Archive Log:    Added support on FM DeviceGroup
 *  Archive Log:    put all constants used in xml file to XMLConstants
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management.devicegroups;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.intel.stl.api.management.XMLConstants;

@XmlRootElement(name = XMLConstants.DEVICE_GROUPS)
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceGroups {
    @XmlElement(name = XMLConstants.DEVICE_GROUP)
    private List<DeviceGroup> groups;

    /**
     * @return the groups
     */
    public List<DeviceGroup> getGroups() {
        return groups;
    }

    /**
     * @param groups
     *            the groups to set
     */
    public void setGroups(List<DeviceGroup> groups) {
        this.groups = groups;
    }

    public DeviceGroup getGroup(String name) {
        for (DeviceGroup group : groups) {
            if (group.getName().equals(name)) {
                return group;
            }
        }

        throw new IllegalArgumentException(
                "Counldn't find Device Group by name '" + name + "'");
    }

    /**
     * 
     * <i>Description:</i> find device groups whose included group list
     * contains the specified group name
     * 
     * @param name
     * @return
     */
    public List<DeviceGroup> getReferencedGroups(String name) {
        List<DeviceGroup> res = new ArrayList<DeviceGroup>();
        IncludeGroup ig = new IncludeGroup(name);
        for (DeviceGroup group : groups) {
            List<IncludeGroup> children = group.getIncludeGroups();
            if (children != null && children.contains(ig)) {
                res.add(group);
            }
        }
        return res;
    }
}
