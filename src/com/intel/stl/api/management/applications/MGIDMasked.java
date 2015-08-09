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
 *  File Name: MGIDMasked.java
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

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.Utils;
import com.intel.stl.api.management.XMLConstants;

public class MGIDMasked extends MGID {
    private static final long serialVersionUID = -2856166700407224171L;

    private long maskLower;

    private long maskUpper;

    public MGIDMasked() {
        this(0, 0, 0, 0);
    }

    /**
     * Description:
     * 
     * @param lower
     * @param upper
     * @param maskLower
     * @param maskUpper
     */
    public MGIDMasked(long lower, long upper, long maskLower, long maskUpper) {
        super(XMLConstants.MGID_MASKED, lower, upper);
        this.maskLower = maskLower;
        this.maskUpper = maskUpper;
    }

    /**
     * @return the maskLower
     */
    public long getMaskLower() {
        return maskLower;
    }

    /**
     * @return the maskUpper
     */
    public long getMaskUpper() {
        return maskUpper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.MGID#setVal(java.lang.String)
     */
    @Override
    public void setVal(String val) {
        String[] segs = val.split("\\*");
        super.setVal(segs[0]);
        if (segs.length > 0) {
            segs = segs[1].split(":");
            maskLower = Utils.toLong(segs[0]);
            if (segs.length > 0) {
                maskUpper = Utils.toLong(segs[1]);
            }
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
        int result = super.hashCode();
        result = prime * result + (int) (maskLower ^ (maskLower >>> 32));
        result = prime * result + (int) (maskUpper ^ (maskUpper >>> 32));
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
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MGIDMasked other = (MGIDMasked) obj;
        if (maskLower != other.maskLower) {
            return false;
        }
        if (maskUpper != other.maskUpper) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.application.MGID#copy()
     */
    @Override
    public MGIDMasked copy() {
        return new MGIDMasked(lower, upper, maskLower, maskUpper);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return super.toString() + "*" + StringUtils.longHexString(maskLower)
                + ":" + StringUtils.longHexString(maskUpper);
    }
}
