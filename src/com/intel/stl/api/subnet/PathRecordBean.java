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
 *  File Name: PathRecordBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/04 21:37:55  jijunwan
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

package com.intel.stl.api.subnet;

import java.io.Serializable;

import com.intel.stl.api.Utils;

/**
 * @author jijunwan
 * 
 */
public class PathRecordBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private long serviceId;

    private GIDBean dGid;

    private GIDBean sGid;

    private short dLid;

    private short sLid;

    private boolean rawTraffic;

    private int flowLabel;

    private short hopLimit; // promote to handle unsigned byte

    private byte tClass;

    private byte numbPath;

    private boolean reversible;

    private short pKey;

    private byte qosType;

    private short qosPriority; // promote to handle unsigned byte

    private byte sl;

    private byte mtuSelector;

    private byte mtu;

    private byte rateSelector;

    private byte rate;

    private byte pktLifeTimeSelector;

    private byte pktLifeTime;

    private byte preference;

    /**
     * @return the serviceId
     */
    public long getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId
     *            the serviceId to set
     */
    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return the dGid
     */
    public GIDBean getDGid() {
        return dGid;
    }

    /**
     * @param dGid
     *            the dGid to set
     */
    public void setDGid(GIDBean dGid) {
        this.dGid = dGid;
    }

    /**
     * @return the sGid
     */
    public GIDBean getSGid() {
        return sGid;
    }

    /**
     * @param sGid
     *            the sGid to set
     */
    public void setSGid(GIDBean sGid) {
        this.sGid = sGid;
    }

    /**
     * @return the dLid
     */
    public short getDLid() {
        return dLid;
    }

    /**
     * @param dLid
     *            the dLid to set
     */
    public void setDLid(short dLid) {
        this.dLid = dLid;
    }

    /**
     * @return the sLid
     */
    public short getSLid() {
        return sLid;
    }

    /**
     * @param sLid
     *            the sLid to set
     */
    public void setSLid(short sLid) {
        this.sLid = sLid;
    }

    /**
     * @return the rawTraffic
     */
    public boolean getRawTraffic() {
        return rawTraffic;
    }

    /**
     * @param rawTraffic
     *            the rawTraffic to set
     */
    public void setRawTraffic(boolean rawTraffic) {
        this.rawTraffic = rawTraffic;
    }

    /**
     * @return the flowLabel
     */
    public int getFlowLabel() {
        return flowLabel;
    }

    /**
     * @param flowLabel
     *            the flowLabel to set
     */
    public void setFlowLabel(int flowLabel) {
        this.flowLabel = flowLabel;
    }

    /**
     * @return the hopLimit
     */
    public short getHopLimit() {
        return hopLimit;
    }

    /**
     * @param hopLimit
     *            the hopLimit to set
     */
    public void setHopLimit(short hopLimit) {
        this.hopLimit = hopLimit;
    }

    /**
     * @param hopLimit
     *            the hopLimit to set
     */
    public void setHopLimit(byte hopLimit) {
        this.hopLimit = (short) (hopLimit & 0xff);
    }

    /**
     * @return the tClass
     */
    public byte getTClass() {
        return tClass;
    }

    /**
     * @param tClass
     *            the tClass to set
     */
    public void setTClass(byte tClass) {
        this.tClass = tClass;
    }

    /**
     * @return the numbPath
     */
    public byte getNumbPath() {
        return numbPath;
    }

    /**
     * @param numbPath
     *            the numbPath to set
     */
    public void setNumbPath(byte numbPath) {
        this.numbPath = numbPath;
    }

    /**
     * @return the reversible
     */
    public boolean isReversible() {
        return reversible;
    }

    /**
     * @param reversible
     *            the reversible to set
     */
    public void setReversible(boolean reversible) {
        this.reversible = reversible;
    }

    /**
     * @return the pKey
     */
    public short getPKey() {
        return pKey;
    }

    /**
     * @param pKey
     *            the pKey to set
     */
    public void setPKey(short pKey) {
        this.pKey = pKey;
    }

    /**
     * @return the qosType
     */
    public byte getQosType() {
        return qosType;
    }

    /**
     * @param qosType
     *            the qosType to set
     */
    public void setQosType(byte qosType) {
        this.qosType = qosType;
    }

    /**
     * @return the qosPriority
     */
    public short getQosPriority() {
        return qosPriority;
    }

    /**
     * @param qosPriority
     *            the qosPriority to set
     */
    public void setQosPriority(byte qosPriority) {
        this.qosPriority = Utils.unsignedByte(qosPriority);
    }

    /**
     * @return the sl
     */
    public byte getSL() {
        return sl;
    }

    /**
     * @param sl
     *            the sl to set
     */
    public void setSL(byte sl) {
        this.sl = sl;
    }

    /**
     * @return the mtuSelector
     */
    public byte getMtuSelector() {
        return mtuSelector;
    }

    /**
     * @param mtuSelector
     *            the mtuSelector to set
     */
    public void setMtuSelector(byte mtuSelector) {
        this.mtuSelector = mtuSelector;
    }

    /**
     * @return the mtu
     */
    public byte getMtu() {
        return mtu;
    }

    /**
     * @param mtu
     *            the mtu to set
     */
    public void setMtu(byte mtu) {
        this.mtu = mtu;
    }

    /**
     * @return the rateSelector
     */
    public byte getRateSelector() {
        return rateSelector;
    }

    /**
     * @param rateSelector
     *            the rateSelector to set
     */
    public void setRateSelector(byte rateSelector) {
        this.rateSelector = rateSelector;
    }

    /**
     * @return the rate
     */
    public byte getRate() {
        return rate;
    }

    /**
     * @param rate
     *            the rate to set
     */
    public void setRate(byte rate) {
        this.rate = rate;
    }

    /**
     * @return the pktLifeTimeSelector
     */
    public byte getPktLifeTimeSelector() {
        return pktLifeTimeSelector;
    }

    /**
     * @param pktLifeTimeSelector
     *            the pktLifeTimeSelector to set
     */
    public void setPktLifeTimeSelector(byte pktLifeTimeSelector) {
        this.pktLifeTimeSelector = pktLifeTimeSelector;
    }

    /**
     * @return the pktLifeTime
     */
    public byte getPktLifeTime() {
        return pktLifeTime;
    }

    /**
     * @param pktLifeTime
     *            the pktLifeTime to set
     */
    public void setPktLifeTime(byte pktLifeTime) {
        this.pktLifeTime = pktLifeTime;
    }

    /**
     * @return the preference
     */
    public byte getPreference() {
        return preference;
    }

    /**
     * @param preference
     *            the preference to set
     */
    public void setPreference(byte preference) {
        this.preference = preference;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PathRecordBean [serviceId=" + serviceId + ", dGid=" + dGid
                + ", sGid=" + sGid + ", dLid=" + dLid + ", sLid=" + sLid
                + ", rawTraffic=" + rawTraffic + ", flowLabel=" + flowLabel
                + ", hopLimit=" + hopLimit + ", tClass=" + tClass
                + ", numbPath=" + numbPath + ", reversible=" + reversible
                + ", pKey=" + pKey + ", qosType=" + qosType + ", qosPriority="
                + qosPriority + ", sl=" + sl + ", mtuSelector=" + mtuSelector
                + ", mtu=" + mtu + ", rateSelector=" + rateSelector + ", rate="
                + rate + ", pktLifeTimeSelector=" + pktLifeTimeSelector
                + ", pktLifeTime=" + pktLifeTime + ", preference=" + preference
                + "]";
    }

}
