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
 *  File Name: FlitControlBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/02/04 21:37:55  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *
 *  Overview: A substructure in Port Info for the Fabric View API
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.api.subnet;

import java.io.Serializable;

import com.intel.stl.api.Utils;

public class FlitControlBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // Interleave
    // private short ilAsReg16;
    private byte distanceSupported;

    private byte distanceEnabled;

    private byte maxNestLevelTxEnabled;

    private byte maxNestLevelRxSupported;

    // Preemption
    private int minInitial; // promote to handle unsigned short

    private int minTail; // promote to handle unsigned short

    private short largePktLimit; // promote to handle unsigned byte

    private short smallPktLimit; // promote to handle unsigned byte

    private short maxSmallPktLimit; // promote to handle unsigned byte

    private short preemptionLimit; // promote to handle unsigned byte

    public FlitControlBean() {
        super();
    }

    public FlitControlBean(byte distanceSupported, byte distanceEnabled,
            byte maxNestLevelTxEnabled, byte maxNestLevelRxSupported,
            short minInitial, short minTail, byte largePktLimit,
            byte smallPktLimit, byte maxSmallPktLimit, byte preemptionLimit) {
        super();
        this.distanceSupported = distanceSupported;
        this.distanceEnabled = distanceEnabled;
        this.maxNestLevelTxEnabled = maxNestLevelTxEnabled;
        this.maxNestLevelRxSupported = maxNestLevelRxSupported;
        this.minInitial = Utils.unsignedShort(minInitial);
        this.minTail = Utils.unsignedShort(minTail);
        this.largePktLimit = Utils.unsignedByte(largePktLimit);
        this.smallPktLimit = Utils.unsignedByte(smallPktLimit);
        this.maxSmallPktLimit = Utils.unsignedByte(maxSmallPktLimit);
        this.preemptionLimit = Utils.unsignedByte(preemptionLimit);
    }

    /**
     * @return the distanceSupported
     */
    public byte getDistanceSupported() {
        return distanceSupported;
    }

    /**
     * @param distanceSupported
     *            the distanceSupported to set
     */
    public void setDistanceSupported(byte distanceSupported) {
        this.distanceSupported = distanceSupported;
    }

    /**
     * @return the distanceEnabled
     */
    public byte getDistanceEnabled() {
        return distanceEnabled;
    }

    /**
     * @param distanceEnabled
     *            the distanceEnabled to set
     */
    public void setDistanceEnabled(byte distanceEnabled) {
        this.distanceEnabled = distanceEnabled;
    }

    /**
     * @return the maxNestLevelTxEnabled
     */
    public byte getMaxNestLevelTxEnabled() {
        return maxNestLevelTxEnabled;
    }

    /**
     * @param maxNestLevelTxEnabled
     *            the maxNestLevelTxEnabled to set
     */
    public void setMaxNestLevelTxEnabled(byte maxNestLevelTxEnabled) {
        this.maxNestLevelTxEnabled = maxNestLevelTxEnabled;
    }

    /**
     * @return the maxNestLevelRxSupported
     */
    public byte getMaxNestLevelRxSupported() {
        return maxNestLevelRxSupported;
    }

    /**
     * @param maxNestLevelRxSupported
     *            the maxNestLevelRxSupported to set
     */
    public void setMaxNestLevelRxSupported(byte maxNestLevelRxSupported) {
        this.maxNestLevelRxSupported = maxNestLevelRxSupported;
    }

    /**
     * @return the minInitial
     */
    public int getMinInitial() {
        return minInitial;
    }

    /**
     * @param minInitial
     *            the minInitial to set
     */
    public void setMinInitial(int minInitial) {
        this.minInitial = minInitial;
    }

    /**
     * @param minInitial
     *            the minInitial to set
     */
    public void setMinInitial(short minInitial) {
        this.minInitial = Utils.unsignedShort(minInitial);
    }

    /**
     * @return the minTail
     */
    public int getMinTail() {
        return minTail;
    }

    /**
     * @param minTail
     *            the minTail to set
     */
    public void setMinTail(int minTail) {
        this.minTail = minTail;
    }

    /**
     * @param minTail
     *            the minTail to set
     */
    public void setMinTail(short minTail) {
        this.minTail = Utils.unsignedShort(minTail);
    }

    /**
     * @return the largePktLimit
     */
    public short getLargePktLimit() {
        return largePktLimit;
    }

    /**
     * @param largePktLimit
     *            the largePktLimit to set
     */
    public void setLargePktLimit(short largePktLimit) {
        this.largePktLimit = largePktLimit;
    }

    /**
     * @param largePktLimit
     *            the largePktLimit to set
     */
    public void setLargePktLimit(byte largePktLimit) {
        this.largePktLimit = Utils.unsignedByte(largePktLimit);
    }

    /**
     * @return the smallPktLimit
     */
    public short getSmallPktLimit() {
        return smallPktLimit;
    }

    /**
     * @param smallPktLimit
     *            the smallPktLimit to set
     */
    public void setSmallPktLimit(short smallPktLimit) {
        this.smallPktLimit = smallPktLimit;
    }

    /**
     * @param smallPktLimit
     *            the smallPktLimit to set
     */
    public void setSmallPktLimit(byte smallPktLimit) {
        this.smallPktLimit = Utils.unsignedByte(smallPktLimit);
    }

    /**
     * @return the maxSmallPktLimit
     */
    public short getMaxSmallPktLimit() {
        return maxSmallPktLimit;
    }

    /**
     * @param maxSmallPktLimit
     *            the maxSmallPktLimit to set
     */
    public void setMaxSmallPktLimit(short maxSmallPktLimit) {
        this.maxSmallPktLimit = maxSmallPktLimit;
    }

    /**
     * @param maxSmallPktLimit
     *            the maxSmallPktLimit to set
     */
    public void setMaxSmallPktLimit(byte maxSmallPktLimit) {
        this.maxSmallPktLimit = Utils.unsignedByte(maxSmallPktLimit);
    }

    /**
     * @return the preemptionLimit
     */
    public short getPreemptionLimit() {
        return preemptionLimit;
    }

    /**
     * @param preemptionLimit
     *            the preemptionLimit to set
     */
    public void setPreemptionLimit(short preemptionLimit) {
        this.preemptionLimit = preemptionLimit;
    }

    /**
     * @param preemptionLimit
     *            the preemptionLimit to set
     */
    public void setPreemptionLimit(byte preemptionLimit) {
        this.preemptionLimit = Utils.unsignedByte(preemptionLimit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FlitControlBean [distanceSupported=" + distanceSupported
                + ", distanceEnabled=" + distanceEnabled
                + ", maxNestLevelTxEnabled=" + maxNestLevelTxEnabled
                + ", maxNestLevelRxSupported=" + maxNestLevelRxSupported
                + ", minInitial=" + minInitial + ", minTail=" + minTail
                + ", largePktLimit=" + largePktLimit + ", smallPktLimit="
                + smallPktLimit + ", maxSmallPktLimit=" + maxSmallPktLimit
                + ", preemptionLimit=" + preemptionLimit + "]";
    }

}