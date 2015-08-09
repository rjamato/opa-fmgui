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
 *  File Name: VFPortCountersBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/02/12 19:30:00  jijunwan
 *  Archive Log:    introduced interface ITimestamped, and all timimg attributes implemented it, so we can easily know which attribute is associated with timestamp
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/04 21:37:53  jijunwan
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
public class VFPortCountersBean implements ITimestamped, Serializable {
    private static final long serialVersionUID = 1L;

    private int nodeLid;

    private short portNumber; // promote to handle unsigned byte

    private int flags;

    private String vfName;

    private long vfSID;

    private ImageIdBean imageId;

    private long portVFXmitData;

    private long portVFRcvData;

    private long portVFXmitPkts;

    private long portVFRcvPkts;

    private long portVFXmitDiscards;

    private long swPortVFCongestion;

    private long portVFXmitWait;

    private long portVFRcvFECN;

    private long portVFRcvBECN;

    private long portVFXmitTimeCong;

    private long portVFXmitWastedBW;

    private long portVFXmitWaitData;

    private long portVFRcvBubble;

    private long portVFMarkFECN;

    private long timestamp;

    /**
     * @return the nodeLid
     */
    public int getNodeLid() {
        return nodeLid;
    }

    /**
     * @param nodeLid
     *            the nodeLid to set
     */
    public void setNodeLid(int nodeLid) {
        this.nodeLid = nodeLid;
    }

    /**
     * @return the portNumber
     */
    public short getPortNumber() {
        return portNumber;
    }

    /**
     * @param portNumber
     *            the portNumber to set
     */
    public void setPortNumber(short portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * @param portNumber
     *            the portNumber to set
     */
    public void setPortNumber(byte portNumber) {
        this.portNumber = Utils.unsignedByte(portNumber);
    }

    /**
     * @return the flags
     */
    public int getFlags() {
        return flags;
    }

    /**
     * @param flags
     *            the flags to set
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    public boolean isDelta() {
        return (flags & 0x01) == 0x01;
    }

    public boolean hasUnexpectedClear() {
        return (flags & 0x02) == 0x02;
    }

    /**
     * @return the vfName
     */
    public String getVfName() {
        return vfName;
    }

    /**
     * @param vfName
     *            the vfName to set
     */
    public void setVfName(String vfName) {
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
     * @return the portVFXmitData
     */
    public long getPortVFXmitData() {
        return portVFXmitData;
    }

    /**
     * @param portVFXmitData
     *            the portVFXmitData to set
     */
    public void setPortVFXmitData(long portVFXmitData) {
        this.portVFXmitData = portVFXmitData;
    }

    /**
     * @return the portVFRcvData
     */
    public long getPortVFRcvData() {
        return portVFRcvData;
    }

    /**
     * @param portVFRcvData
     *            the portVFRcvData to set
     */
    public void setPortVFRcvData(long portVFRcvData) {
        this.portVFRcvData = portVFRcvData;
    }

    /**
     * @return the portVFXmitPkts
     */
    public long getPortVFXmitPkts() {
        return portVFXmitPkts;
    }

    /**
     * @param portVFXmitPkts
     *            the portVFXmitPkts to set
     */
    public void setPortVFXmitPkts(long portVFXmitPkts) {
        this.portVFXmitPkts = portVFXmitPkts;
    }

    /**
     * @return the portVFRcvPkts
     */
    public long getPortVFRcvPkts() {
        return portVFRcvPkts;
    }

    /**
     * @param portVFRcvPkts
     *            the portVFRcvPkts to set
     */
    public void setPortVFRcvPkts(long portVFRcvPkts) {
        this.portVFRcvPkts = portVFRcvPkts;
    }

    /**
     * @return the portVFXmitDiscards
     */
    public long getPortVFXmitDiscards() {
        return portVFXmitDiscards;
    }

    /**
     * @param portVFXmitDiscards
     *            the portVFXmitDiscards to set
     */
    public void setPortVFXmitDiscards(long portVFXmitDiscards) {
        this.portVFXmitDiscards = portVFXmitDiscards;
    }

    /**
     * @return the swPortVFCongestion
     */
    public long getSwPortVFCongestion() {
        return swPortVFCongestion;
    }

    /**
     * @param swPortVFCongestion
     *            the swPortVFCongestion to set
     */
    public void setSwPortVFCongestion(long swPortVFCongestion) {
        this.swPortVFCongestion = swPortVFCongestion;
    }

    /**
     * @return the portVFXmitWait
     */
    public long getPortVFXmitWait() {
        return portVFXmitWait;
    }

    /**
     * @param portVFXmitWait
     *            the portVFXmitWait to set
     */
    public void setPortVFXmitWait(long portVFXmitWait) {
        this.portVFXmitWait = portVFXmitWait;
    }

    /**
     * @return the portVFRcvFECN
     */
    public long getPortVFRcvFECN() {
        return portVFRcvFECN;
    }

    /**
     * @param portVFRcvFECN
     *            the portVFRcvFECN to set
     */
    public void setPortVFRcvFECN(long portVFRcvFECN) {
        this.portVFRcvFECN = portVFRcvFECN;
    }

    /**
     * @return the portVFRcvBECN
     */
    public long getPortVFRcvBECN() {
        return portVFRcvBECN;
    }

    /*
     * @param portVFRcvBECN
     * the portVFRcvBECN to set
     */
    public void setPortVFRcvBECN(long portVFRcvBECN) {
        this.portVFRcvBECN = portVFRcvBECN;
    }

    /**
     * @return the portVFXmitTimeCong
     */
    public long getPortVFXmitTimeCong() {
        return portVFXmitTimeCong;
    }

    /**
     * @param portVFXmitTimeCong
     *            the portVFXmitTimeCong to set
     */
    public void setPortVFXmitTimeCong(long portVFXmitTimeCong) {
        this.portVFXmitTimeCong = portVFXmitTimeCong;
    }

    /**
     * @return the portVFXmitWastedBW
     */
    public long getPortVFXmitWastedBW() {
        return portVFXmitWastedBW;
    }

    /**
     * @param portVFXmitWastedBW
     *            the portVFXmitWastedBW to set
     */
    public void setPortVFXmitWastedBW(long portVFXmitWastedBW) {
        this.portVFXmitWastedBW = portVFXmitWastedBW;
    }

    /**
     * @return the portVFXmitWaitData
     */
    public long getPortVFXmitWaitData() {
        return portVFXmitWaitData;
    }

    /**
     * @param portVFXmitWaitData
     *            the portVFXmitWaitData to set
     */
    public void setPortVFXmitWaitData(long portVFXmitWaitData) {
        this.portVFXmitWaitData = portVFXmitWaitData;
    }

    /**
     * @return the portVFRcvBubble
     */
    public long getPortVFRcvBubble() {
        return portVFRcvBubble;
    }

    /**
     * @param portVFRcvBubble
     *            the portVFRcvBubble to set
     */
    public void setPortVFRcvBubble(long portVFRcvBubble) {
        this.portVFRcvBubble = portVFRcvBubble;
    }

    /**
     * @return the portVFMarkFECN
     */
    public long getPortVFMarkFECN() {
        return portVFMarkFECN;
    }

    /**
     * @param portVFMarkFECN
     *            the portVFMarkFECN to set
     */
    public void setPortVFMarkFECN(long portVFMarkFECN) {
        this.portVFMarkFECN = portVFMarkFECN;
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
     * This field is set at the API level when VFPortCounters is retrieved from
     * FE. At that time, the ImageInfo is also retrieved from buffers or from
     * the FE and sweepTimestamp is initialized to sweepStart. Note that
     * sweepStart is Unix time (seconds since Jan 1st, 1970)
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
        return "VFPortCountersBean [nodeLid="
                + StringUtils.intHexString(nodeLid) + ", portNumber="
                + portNumber + ", flags=" + StringUtils.intHexString(flags)
                + ", vfName=" + vfName + ", vfSID="
                + StringUtils.longHexString(vfSID) + ", imageId=" + imageId
                + ", portVFXmitData=" + portVFXmitData + ", portVFRcvData="
                + portVFRcvData + ", portVFXmitPkts=" + portVFXmitPkts
                + ", portVFRcvPkts=" + portVFRcvPkts + ", portVFXmitDiscards="
                + portVFXmitDiscards + ", swPortVFCongestion="
                + swPortVFCongestion + ", portVFXmitWait=" + portVFXmitWait
                + ", portVFRecvFECN=" + portVFRcvFECN + ", portVFRecvBECN="
                + portVFRcvBECN + ", portVFXmitTimeCong=" + portVFXmitTimeCong
                + ", portVFXmitWastedBW=" + portVFXmitWastedBW
                + ", portVFXmitWaitData=" + portVFXmitWaitData
                + ", portVFRcvBubble=" + portVFRcvBubble + ", portVFMarkFECN="
                + portVFMarkFECN + "]";
    }

}
