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
 *  File Name: PortCountersBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/02/12 19:30:00  jijunwan
 *  Archive Log:    introduced interface ITimestamped, and all timimg attributes implemented it, so we can easily know which attribute is associated with timestamp
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/04 21:37:53  jijunwan
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
import com.intel.stl.api.Utils;

/**
 * @author jijunwan
 * 
 */
public class PortCountersBean implements ITimestamped, Serializable {
    private static final long serialVersionUID = 1L;

    private int nodeLid;

    private short portNumber; // promote to handle unsigned byte

    private int flags;

    private long portXmitData;

    private long portRcvData;

    private long portXmitPkts;

    private long portRcvPkts;

    private long portMulticastXmitPkts;

    private long portMulticastRcvPkts;

    private long localLinkIntegrityErrors;

    private long fmConfigErrors;

    private long portRcvErrors;

    private long excessiveBufferOverruns;

    private long portRcvConstraintErrors;

    private long portRcvSwitchRelayErrors;

    private long portXmitDiscards;

    private long portXmitConstraintErrors;

    private long portRcvRemotePhysicalErrors;

    private long swPortCongestion;

    private long portXmitWait;

    private long portRcvFECN;

    private long portRcvBECN;

    private long portXmitTimeCong;

    private long portXmitWastedBW;

    private long portXmitWaitData;

    private long portRcvBubble;

    private long portMarkFECN;

    private long linkErrorRecovery; // unsigned int

    private long linkDowned; // unsigned int

    private short uncorrectableErrors; // unsigned byte

    private byte linkQualityIndicator;

    private ImageIdBean imageId;

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
     * @return the portXmitData
     */
    public long getPortXmitData() {
        return portXmitData;
    }

    /**
     * @param portXmitData
     *            the portXmitData to set
     */
    public void setPortXmitData(long portXmitData) {
        this.portXmitData = portXmitData;
    }

    /**
     * @return the portRcvData
     */
    public long getPortRcvData() {
        return portRcvData;
    }

    /**
     * @param portRcvData
     *            the portRcvData to set
     */
    public void setPortRcvData(long portRcvData) {
        this.portRcvData = portRcvData;
    }

    /**
     * @return the portXmitPkts
     */
    public long getPortXmitPkts() {
        return portXmitPkts;
    }

    /**
     * @param portXmitPkts
     *            the portXmitPkts to set
     */
    public void setPortXmitPkts(long portXmitPkts) {
        this.portXmitPkts = portXmitPkts;
    }

    /**
     * @return the portRcvPkts
     */
    public long getPortRcvPkts() {
        return portRcvPkts;
    }

    /**
     * @param portRcvPkts
     *            the portRcvPkts to set
     */
    public void setPortRcvPkts(long portRcvPkts) {
        this.portRcvPkts = portRcvPkts;
    }

    /**
     * @return the portMulticastXmitPkts
     */
    public long getPortMulticastXmitPkts() {
        return portMulticastXmitPkts;
    }

    /**
     * @param portMulticastXmitPkts
     *            the portMulticastXmitPkts to set
     */
    public void setPortMulticastXmitPkts(long portMulticastXmitPkts) {
        this.portMulticastXmitPkts = portMulticastXmitPkts;
    }

    /**
     * @return the portMulticastRcvPkts
     */
    public long getPortMulticastRcvPkts() {
        return portMulticastRcvPkts;
    }

    /**
     * @param portMulticastRcvPkts
     *            the portMulticastRcvPkts to set
     */
    public void setPortMulticastRcvPkts(long portMulticastRcvPkts) {
        this.portMulticastRcvPkts = portMulticastRcvPkts;
    }

    /**
     * @return the localLinkIntegrityErrors
     */
    public long getLocalLinkIntegrityErrors() {
        return localLinkIntegrityErrors;
    }

    /**
     * @param localLinkIntegrityErrors
     *            the localLinkIntegrityErrors to set
     */
    public void setLocalLinkIntegrityErrors(long localLinkIntegrityErrors) {
        this.localLinkIntegrityErrors = localLinkIntegrityErrors;
    }

    /**
     * @return the fmConfigErrors
     */
    public long getFmConfigErrors() {
        return fmConfigErrors;
    }

    /**
     * @param fmConfigErrors
     *            the fmConfigErrors to set
     */
    public void setFmConfigErrors(long fmConfigErrors) {
        this.fmConfigErrors = fmConfigErrors;
    }

    /**
     * @return the portRcvErrors
     */
    public long getPortRcvErrors() {
        return portRcvErrors;
    }

    /**
     * @param portRcvErrors
     *            the portRcvErrors to set
     */
    public void setPortRcvErrors(long portRcvErrors) {
        this.portRcvErrors = portRcvErrors;
    }

    /**
     * @return the excessiveBufferOverruns
     */
    public long getExcessiveBufferOverruns() {
        return excessiveBufferOverruns;
    }

    /**
     * @param excessiveBufferOverruns
     *            the excessiveBufferOverruns to set
     */
    public void setExcessiveBufferOverruns(long excessiveBufferOverruns) {
        this.excessiveBufferOverruns = excessiveBufferOverruns;
    }

    /**
     * @return the portRcvConstraintErrors
     */
    public long getPortRcvConstraintErrors() {
        return portRcvConstraintErrors;
    }

    /**
     * @param portRcvConstraintErrors
     *            the portRcvConstraintErrors to set
     */
    public void setPortRcvConstraintErrors(long portRcvConstraintErrors) {
        this.portRcvConstraintErrors = portRcvConstraintErrors;
    }

    /**
     * @return the portRcvSwitchRelayErrors
     */
    public long getPortRcvSwitchRelayErrors() {
        return portRcvSwitchRelayErrors;
    }

    /**
     * @param portRcvSwitchRelayErrors
     *            the portRcvSwitchRelayErrors to set
     */
    public void setPortRcvSwitchRelayErrors(long portRcvSwitchRelayErrors) {
        this.portRcvSwitchRelayErrors = portRcvSwitchRelayErrors;
    }

    /**
     * @return the portXmitDiscards
     */
    public long getPortXmitDiscards() {
        return portXmitDiscards;
    }

    /**
     * @param portXmitDiscards
     *            the portXmitDiscards to set
     */
    public void setPortXmitDiscards(long portXmitDiscards) {
        this.portXmitDiscards = portXmitDiscards;
    }

    /**
     * @return the portXmitConstraintErrors
     */
    public long getPortXmitConstraintErrors() {
        return portXmitConstraintErrors;
    }

    /**
     * @param portXmitConstraintErrors
     *            the portXmitConstraintErrors to set
     */
    public void setPortXmitConstraintErrors(long portXmitConstraintErrors) {
        this.portXmitConstraintErrors = portXmitConstraintErrors;
    }

    /**
     * @return the portRcvRemotePhysicalErrors
     */
    public long getPortRcvRemotePhysicalErrors() {
        return portRcvRemotePhysicalErrors;
    }

    /**
     * @param portRcvRemotePhysicalErrors
     *            the portRcvRemotePhysicalErrors to set
     */
    public void setPortRcvRemotePhysicalErrors(long portRcvRemotePhysicalErrors) {
        this.portRcvRemotePhysicalErrors = portRcvRemotePhysicalErrors;
    }

    /**
     * @return the swPortCongestion
     */
    public long getSwPortCongestion() {
        return swPortCongestion;
    }

    /**
     * @param swPortCongestion
     *            the swPortCongestion to set
     */
    public void setSwPortCongestion(long swPortCongestion) {
        this.swPortCongestion = swPortCongestion;
    }

    /**
     * @return the portXmitWait
     */
    public long getPortXmitWait() {
        return portXmitWait;
    }

    /**
     * @param portXmitWait
     *            the portXmitWait to set
     */
    public void setPortXmitWait(long portXmitWait) {
        this.portXmitWait = portXmitWait;
    }

    /**
     * @return the portRcvFECN
     */
    public long getPortRcvFECN() {
        return portRcvFECN;
    }

    /**
     * @param portRcvFECN
     *            the portRecvFECN to set
     */
    public void setPortRcvFECN(long portRcvFECN) {
        this.portRcvFECN = portRcvFECN;
    }

    /**
     * @return the portRcvBECN
     */
    public long getPortRcvBECN() {
        return portRcvBECN;
    }

    /**
     * @param portRcvBECN
     *            the portRcvBECN to set
     */
    public void setPortRcvBECN(long portRcvBECN) {
        this.portRcvBECN = portRcvBECN;
    }

    /**
     * @return the portXmitTimeCong
     */
    public long getPortXmitTimeCong() {
        return portXmitTimeCong;
    }

    /**
     * @param portXmitTimeCong
     *            the portXmitTimeCong to set
     */
    public void setPortXmitTimeCong(long portXmitTimeCong) {
        this.portXmitTimeCong = portXmitTimeCong;
    }

    /**
     * @return the portXmitWastedBW
     */
    public long getPortXmitWastedBW() {
        return portXmitWastedBW;
    }

    /**
     * @param portXmitWastedBW
     *            the portXmitWastedBW to set
     */
    public void setPortXmitWastedBW(long portXmitWastedBW) {
        this.portXmitWastedBW = portXmitWastedBW;
    }

    /**
     * @return the portXmitWaitData
     */
    public long getPortXmitWaitData() {
        return portXmitWaitData;
    }

    /**
     * @param portXmitWaitData
     *            the portXmitWaitData to set
     */
    public void setPortXmitWaitData(long portXmitWaitData) {
        this.portXmitWaitData = portXmitWaitData;
    }

    /**
     * @return the portRcvBubble
     */
    public long getPortRcvBubble() {
        return portRcvBubble;
    }

    /**
     * @param portRcvBubble
     *            the portRcvBubble to set
     */
    public void setPortRcvBubble(long portRcvBubble) {
        this.portRcvBubble = portRcvBubble;
    }

    /**
     * @return the portMarkFECN
     */
    public long getPortMarkFECN() {
        return portMarkFECN;
    }

    /**
     * @param portMarkFECN
     *            the portMarkFECN to set
     */
    public void setPortMarkFECN(long portMarkFECN) {
        this.portMarkFECN = portMarkFECN;
    }

    /**
     * @return the linkErrorRecovery
     */
    public long getLinkErrorRecovery() {
        return linkErrorRecovery;
    }

    /**
     * @param linkErrorRecovery
     *            the linkErrorRecovery to set
     */
    public void setLinkErrorRecovery(long linkErrorRecovery) {
        this.linkErrorRecovery = linkErrorRecovery;
    }

    /**
     * @param linkErrorRecovery
     *            the linkErrorRecovery to set
     */
    public void setLinkErrorRecovery(int linkErrorRecovery) {
        this.linkErrorRecovery = Utils.unsignedInt(linkErrorRecovery);
    }

    /**
     * @return the linkDowned
     */
    public long getLinkDowned() {
        return linkDowned;
    }

    /**
     * @param linkDowned
     *            the linkDowned to set
     */
    public void setLinkDowned(long linkDowned) {
        this.linkDowned = linkDowned;
    }

    /**
     * @param linkDowned
     *            the linkDowned to set
     */
    public void setLinkDowned(int linkDowned) {
        this.linkDowned = Utils.unsignedInt(linkDowned);
    }

    /**
     * @return the uncorrectableErrors
     */
    public short getUncorrectableErrors() {
        return uncorrectableErrors;
    }

    /**
     * @param uncorrectableErrors
     *            the uncorrectableErrors to set
     */
    public void setUncorrectableErrors(short uncorrectableErrors) {
        this.uncorrectableErrors = uncorrectableErrors;
    }

    /**
     * @param uncorrectableErrors
     *            the uncorrectableErrors to set
     */
    public void setUncorrectableErrors(byte uncorrectableErrors) {
        this.uncorrectableErrors = Utils.unsignedByte(uncorrectableErrors);
    }

    /**
     * @return the linkQualityIndicator
     */
    public byte getLinkQualityIndicator() {
        return linkQualityIndicator;
    }

    /**
     * @param linkQualityIndicator
     *            the linkQualityIndicator to set
     */
    public void setLinkQualityIndicator(byte linkQualityIndicator) {
        this.linkQualityIndicator = linkQualityIndicator;
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
     * Note that sweepTimestamp is Unix time (seconds since Jan 1st, 1970)
     * 
     * @return the sweepTimestamp
     */
    @Override
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * This field is set at the API level when PortCounters is retrieved from
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
        return "PortCountersBean [nodeLid=" + nodeLid + ", portNumber="
                + portNumber + ", flags=" + flags + ", portXmitData="
                + portXmitData + ", portRcvData=" + portRcvData
                + ", portXmitPkts=" + portXmitPkts + ", portRcvPkts="
                + portRcvPkts + ", portMulticastXmitPkts="
                + portMulticastXmitPkts + ", portMulticastRcvPkts="
                + portMulticastRcvPkts + ", localLinkIntegrityErrors="
                + localLinkIntegrityErrors + ", fmConfigErrors="
                + fmConfigErrors + ", portRcvErrors=" + portRcvErrors
                + ", excessiveBufferOverruns=" + excessiveBufferOverruns
                + ", portRcvConstraintErrors=" + portRcvConstraintErrors
                + ", portRcvSwitchRelayErrors=" + portRcvSwitchRelayErrors
                + ", portXmitDiscards=" + portXmitDiscards
                + ", portXmitConstraintErrors=" + portXmitConstraintErrors
                + ", portRcvRemotePhysicalErrors="
                + portRcvRemotePhysicalErrors + ", swPortCongestion="
                + swPortCongestion + ", portXmitWait=" + portXmitWait
                + ", portRecvFECN=" + portRcvFECN + ", portRecvBECN="
                + portRcvBECN + ", portXmitTimeCong=" + portXmitTimeCong
                + ", portXmitWastedBW=" + portXmitWastedBW
                + ", portXmitWaitData=" + portXmitWaitData + ", portRcvBubble="
                + portRcvBubble + ", portMarkFECN=" + portMarkFECN
                + ", linkErrorRecovery=" + linkErrorRecovery + ", linkDowned="
                + linkDowned + ", uncorrectableErrors=" + uncorrectableErrors
                + ", linkQualityIndicator=" + linkQualityIndicator
                + ", imageId=" + imageId + "]";
    }
}
