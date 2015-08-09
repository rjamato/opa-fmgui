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
 *  File Name: BooleanNode.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/27 15:42:33  jijunwan
 *  Archive Log:    added installVirtualFabric to IAttribute
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/25 18:56:45  jijunwan
 *  Archive Log:    introduced WrapperNode that allows us wrapper any object to Iattribute for xml use
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlValue;

import com.intel.stl.api.management.applications.Application;
import com.intel.stl.api.management.devicegroups.DeviceGroup;
import com.intel.stl.api.management.virtualfabrics.VirtualFabric;

public class BooleanNode implements IAttribute, Serializable {
    private static final long serialVersionUID = 8532195493892626199L;

    protected final String type;

    protected boolean isSelected;

    /**
     * Description:
     * 
     * @param type
     */
    public BooleanNode(String type) {
        super();
        this.type = type;
    }

    /**
     * Description:
     * 
     * @param type
     * @param isSelected
     */
    public BooleanNode(String type, boolean isSelected) {
        super();
        this.type = type;
        this.isSelected = isSelected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.IAttribute#getType()
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * @param value
     *            the value to set
     */
    @XmlValue
    public void setValue(String value) {
        if (value.equals("1") || value.equalsIgnoreCase("true")) {
            this.isSelected = true;
        } else if (value.equals("0") || value.equalsIgnoreCase("false")) {
            this.isSelected = false;
        } else {
            throw new IllegalArgumentException("Invalid string '" + value + "'");
        }
    }

    /**
     * @return the value
     */
    public String getValue() {
        return isSelected ? "1" : "0";
    }

    /**
     * @return the isSelected
     */
    public boolean isSelected() {
        return isSelected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.IAttribute#copy()
     */
    @Override
    public IAttribute copy() {
        return new BooleanNode(type, isSelected);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.IAttribute#installApplication(com.intel.
     * stl.api.management.applications.Application)
     */
    @Override
    public void installApplication(Application app) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.IAttribute#installDevieGroup(com.intel.stl
     * .api.management.devicegroups.DeviceGroup)
     */
    @Override
    public void installDevieGroup(DeviceGroup group) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.IAttribute#installVirtualFabric(com.intel
     * .stl.api.management.virtualfabrics.VirtualFabric)
     */
    @Override
    public void installVirtualFabric(VirtualFabric vf) {
        throw new UnsupportedOperationException();
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
        result = prime * result + (isSelected ? 1231 : 1237);
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        BooleanNode other = (BooleanNode) obj;
        if (isSelected != other.isSelected) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
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
        return "BooleanNode [type=" + type + ", isSelected=" + isSelected + "]";
    }

}
