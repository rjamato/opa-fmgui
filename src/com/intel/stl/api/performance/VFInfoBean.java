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
 *  File Name: VFInfoBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/02/12 19:30:00  jijunwan
 *  Archive Log:    introduced interface ITimestamped, and all timimg attributes implemented it, so we can easily know which attribute is associated with timestamp
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/04 21:37:53  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.performance;

import java.io.Serializable;
import java.util.Date;

import com.intel.stl.api.ITimestamped;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.Utils;

/**
 * @author jijunwan
 * 
 */
public class VFInfoBean implements ITimestamped, Serializable {
    private static final long serialVersionUID = 1L;

    private String vfName;

    private long vfSID;

    private ImageIdBean imageId;

    private long numPorts; // unsigned int

    private UtilStatsBean internalUtilStats;

    private ErrStatBean internalErrors;

    private byte maxInternalRate;

    private byte minInternalRate;

    private int maxInternalMBps;

    private long timestamp;

    /**
     * @return the groupName
     */
    public String getVfName() {
        return vfName;
    }

    /**
     * @param groupName
     *            the groupName to set
     */
    public void setVfName(String vfName) {
        if (vfName.length() > PAConstants.STL_PM_VFNAMELEN) {
            throw new IllegalArgumentException("Invalid string length: "
                    + vfName.length() + " > " + PAConstants.STL_PM_VFNAMELEN);
        }

        this.vfName = vfName;
    }

    /**
     * @return the vfSID
     */
    public long getVfSID() {
        return vfSID;
    }

    /**
     * @param vfSID
     *            the vfSID to set
     */
    public void setVfSID(long vfSID) {
        this.vfSID = vfSID;
    }

    /**
     * @return the imageId
     */
    public ImageIdBean getImageId() {
        return imageId;
    }

    /**
     * @param imageId
     *            the imageId to set
     */
    public void setImageId(ImageIdBean imageId) {
        this.imageId = imageId;
    }

    /**
     * @return the numPorts
     */
    public long getNumPorts() {
        return numPorts;
    }

    /**
     * @param numPorts
     *            the numPorts to set
     */
    public void setNumPorts(long numPorts) {
        this.numPorts = numPorts;
    }

    /**
     * @param numPorts
     *            the numPorts to set
     */
    public void setNumPorts(int numPorts) {
        this.numPorts = Utils.unsignedInt(numPorts);
    }

    /**
     * @return the internalUtilStats
     */
    public UtilStatsBean getInternalUtilStats() {
        return internalUtilStats;
    }

    /**
     * @param internalUtilStats
     *            the internalUtilStats to set
     */
    public void setInternalUtilStats(UtilStatsBean internalUtilStats) {
        this.internalUtilStats = internalUtilStats;
    }

    /**
     * @return the internalErrors
     */
    public ErrStatBean getInternalErrors() {
        return internalErrors;
    }

    /**
     * @param internalErrors
     *            the internalErrors to set
     */
    public void setInternalErrors(ErrStatBean internalErrors) {
        this.internalErrors = internalErrors;
    }

    /**
     * @return the maxInternalRate
     */
    public byte getMaxInternalRate() {
        return maxInternalRate;
    }

    /**
     * @param maxInternalRate
     *            the maxInternalRate to set
     */
    public void setMaxInternalRate(byte maxInternalRate) {
        this.maxInternalRate = maxInternalRate;
    }

    /**
     * @return the minInternalRate
     */
    public byte getMinInternalRate() {
        return minInternalRate;
    }

    /**
     * @param minInternalRate
     *            the minInternalRate to set
     */
    public void setMinInternalRate(byte minInternalRate) {
        this.minInternalRate = minInternalRate;
    }

    /**
     * @return the maxInternalMBps
     */
    public int getMaxInternalMBps() {
        return maxInternalMBps;
    }

    /**
     * @param maxInternalMBps
     *            the maxInternalMBps to set
     */
    public void setMaxInternalMBps(int maxInternalMBps) {
        this.maxInternalMBps = maxInternalMBps;
    }

    /**
     * Note that sweepTimestamp is Unix time (seconds since Jan 1st, 1970)
     * 
     * @return the sweepTimestamp
     */
    @Override
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * This field is set at the API level when VFInfo is retrieved from FE. At
     * that time, the ImageInfo is also retrieved from buffers or from the FE
     * and sweepTimestamp is initialized to sweepStart. Note that sweepStart is
     * Unix time (seconds since Jan 1st, 1970)
     * 
     * @param sweepTimestamp
     *            the sweepTimestamp to set
     */
    public void setTimestamp(long sweepTimestamp) {
        this.timestamp = sweepTimestamp;
    }

    /**
     * 
     * <i>Description:</i> returns sweepTimestamp as Date
     * 
     * @return sweepStart converted to Date
     */
    @Override
    public Date getTimestampDate() {
        return Utils.convertFromUnixTime(timestamp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VFInfoBean [vfName=" + vfName + ", vfSID="
                + StringUtils.longHexString(vfSID) + ", imageId=" + imageId
                + ", numPorts=" + numPorts + ", internalUtilStats="
                + internalUtilStats + ", internalErrors=" + internalErrors
                + ", maxInternalRate="
                + StringUtils.byteHexString(maxInternalRate)
                + ", minInternalRate="
                + StringUtils.byteHexString(minInternalRate)
                + ", maxInternalMBps=" + maxInternalMBps + "]";
    }
}
