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
package com.intel.stl.api.subnet;

/**
 * Title:        VirtualLaneBean
 * Description:  A substructure in Port Info for the Fabric View API
 * 
 * @author jypak
 * @version 0.0
 */
import java.io.Serializable;

import com.intel.stl.api.Utils;

public class VirtualLaneBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private short preemptCap; // promote to handler unsigned byte

    private byte cap;

    private int highLimit; // promote to handler unsigned short

    private int preemptingLimit; // promote to handler unsigned short

    private short arbitrationHighCap; // promote to handler unsigned byte

    private short arbitrationLowCap; // promote to handler unsigned byte

    public VirtualLaneBean() {
        super();
    }

    public VirtualLaneBean(byte preemptCap, byte cap, short highLimit,
            short preemptingLimit, byte arbitrationHighCap,
            byte arbitrationLowCap) {
        super();
        this.preemptCap = Utils.unsignedByte(preemptCap);
        this.cap = cap;
        this.highLimit = Utils.unsignedShort(highLimit);
        this.preemptingLimit = Utils.unsignedShort(preemptingLimit);
        this.arbitrationHighCap = Utils.unsignedByte(arbitrationHighCap);
        this.arbitrationLowCap = Utils.unsignedByte(arbitrationLowCap);
    }

    /**
     * @return the preemptCap
     */
    public short getPreemptCap() {
        return preemptCap;
    }

    /**
     * @param preemptCap
     *            the preemptCap to set
     */
    public void setPreemptCap(short preemptCap) {
        this.preemptCap = preemptCap;
    }

    /**
     * @param initType
     *            the initType to set
     */
    public void setPreemptCap(byte preemptCap) {
        this.preemptCap = Utils.unsignedByte(preemptCap);
    }

    /**
     * @return the cap
     */
    public byte getCap() {
        return cap;
    }

    /**
     * @param cap
     *            the cap to set
     */
    public void setCap(byte cap) {
        this.cap = cap;
    }

    /**
     * @return the highLimit
     */
    public int getHighLimit() {
        return highLimit;
    }

    /**
     * @param highLimit
     *            the highLimit to set
     */
    public void setHighLimit(int highLimit) {
        this.highLimit = highLimit;
    }

    /**
     * @param highLimit
     *            the highLimit to set
     */
    public void setHighLimit(short highLimit) {
        this.highLimit = Utils.unsignedShort(highLimit);
    }

    /**
     * @return the preemptingLimit
     */
    public int getPreemptingLimit() {
        return preemptingLimit;
    }

    /**
     * @param preemptingLimit
     *            the preemptingLimit to set
     */
    public void setPreemptingLimit(int preemptingLimit) {
        this.preemptingLimit = preemptingLimit;
    }

    /**
     * @param preemptingLimit
     *            the preemptingLimit to set
     */
    public void setPreemptingLimit(short preemptingLimit) {
        this.preemptingLimit = Utils.unsignedShort(preemptingLimit);
    }

    /**
     * @return the arbitrationHighCap
     */
    public short getArbitrationHighCap() {
        return arbitrationHighCap;
    }

    /**
     * @param arbitrationHighCap
     *            the arbitrationHighCap to set
     */
    public void setArbitrationHighCap(short arbitrationHighCap) {
        this.arbitrationHighCap = arbitrationHighCap;
    }

    /**
     * @param arbitrationHighCap
     *            the arbitrationHighCap to set
     */
    public void setArbitrationHighCap(byte arbitrationHighCap) {
        this.arbitrationHighCap = Utils.unsignedByte(arbitrationHighCap);
    }

    /**
     * @return the arbitrationLowCap
     */
    public short getArbitrationLowCap() {
        return arbitrationLowCap;
    }

    /**
     * @param arbitrationLowCap
     *            the arbitrationLowCap to set
     */
    public void setArbitrationLowCap(short arbitrationLowCap) {
        this.arbitrationLowCap = arbitrationLowCap;
    }

    /**
     * @param arbitrationLowCap
     *            the arbitrationLowCap to set
     */
    public void setArbitrationLowCap(byte arbitrationLowCap) {
        this.arbitrationLowCap = Utils.unsignedByte(arbitrationLowCap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VirtualLaneBean [preemptCap=" + preemptCap + ", cap=" + cap
                + ", highLimit=" + highLimit + ", preemptingLimit="
                + preemptingLimit + ", arbitrationHighCap="
                + arbitrationHighCap + ", arbitrationLowCap="
                + arbitrationLowCap + "]";
    }

}