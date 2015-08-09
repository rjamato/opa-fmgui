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
 *  File Name: MGID.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/03/27 15:42:35  jijunwan
 *  Archive Log:    added installVirtualFabric to IAttribute
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/24 17:33:20  jijunwan
 *  Archive Log:    introduced IAttribute for attributes defined in xml file
 *  Archive Log:    changed all attributes for Appliation and DG to be an IAttribute
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/16 22:00:57  jijunwan
 *  Archive Log:    changed package name from application to applications, and from devicegroup to devicegroups
 *  Archive Log:    Added #getType to ServiceID, MGID, LongNode and their subclasses,
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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlValue;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.Utils;
import com.intel.stl.api.management.IAttribute;
import com.intel.stl.api.management.XMLConstants;
import com.intel.stl.api.management.devicegroups.DeviceGroup;
import com.intel.stl.api.management.virtualfabrics.VirtualFabric;

public class MGID implements IAttribute, Serializable {
    private static final long serialVersionUID = 6558030997560055165L;

    protected String type;

    protected long lower;

    protected long upper;

    public MGID() {
        this(0, 0);
    }

    /**
     * Description:
     * 
     * @param lower
     * @param upper
     */
    public MGID(long lower, long upper) {
        this(XMLConstants.MGID, lower, upper);
    }

    public MGID(String type, long lower, long upper) {
        this.type = type;
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * @return the lower
     */
    public long getLower() {
        return lower;
    }

    /**
     * @return the upper
     */
    public long getUpper() {
        return upper;
    }

    /**
     * @param val
     *            the xml string to set
     */
    @XmlValue
    public void setVal(String val) {
        String[] segs = val.split(":");
        lower = Utils.toLong(segs[0]);
        if (segs.length > 0) {
            upper = Utils.toLong(segs[1]);
        }
    }

    public String getVal() {
        return toString();
    }

    /**
     * @return the type
     */
    @Override
    public String getType() {
        return type;
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
        app.addMGID(this);
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
        result = prime * result + (int) (lower ^ (lower >>> 32));
        result = prime * result + (int) (upper ^ (upper >>> 32));
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
        MGID other = (MGID) obj;
        if (lower != other.lower) {
            return false;
        }
        if (upper != other.upper) {
            return false;
        }
        return true;
    }

    @Override
    public MGID copy() {
        return new MGID(lower, upper);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return StringUtils.longHexString(lower) + ":"
                + StringUtils.longHexString(upper);
    }

}
