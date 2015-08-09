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
 *  File Name: GroupConfig.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/02/12 20:20:22  jijunwan
 *  Archive Log:    changed back to use timestamp as part of id
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/06 15:03:04  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/30 15:37:20  fernande
 *  Archive Log:    Changed hashCode methods to use generated code by Eclipse
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/29 20:51:25  fernande
 *  Archive Log:    Fixing NullPointerException for the hashcode too.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/11 22:08:02  fernande
 *  Archive Log:    Changes to add more entities to database schema
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.datamanager;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GroupConfigId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "subnetId")
    private long fabricId;

    @Column(name = "groupName", length = 64)
    private String subnetGroup;

    public long getFabricId() {
        return fabricId;
    }

    public void setFabricId(long fabricId) {
        this.fabricId = fabricId;
    }

    public String getSubnetGroup() {
        return subnetGroup;
    }

    public void setSubnetGroup(String subnetGroup) {
        this.subnetGroup = subnetGroup;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (fabricId ^ (fabricId >>> 32));
        result =
                prime * result
                        + ((subnetGroup == null) ? 0 : subnetGroup.hashCode());
        return result;
    }

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
        GroupConfigId other = (GroupConfigId) obj;
        if (fabricId != other.fabricId) {
            return false;
        }
        if (subnetGroup == null) {
            if (other.subnetGroup != null) {
                return false;
            }
        } else if (!subnetGroup.equalsIgnoreCase(other.subnetGroup)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GroupConfigId [fabricId=" + fabricId + ", subnetGroup="
                + subnetGroup + "]";
    }

}
