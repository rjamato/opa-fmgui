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

import java.io.Serializable;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.Utils;

/**
 * @author jijunwan
 * 
 */
public class SMInfoBean implements Serializable {
    private static final long serialVersionUID = 6396097702305645183L;

    private long portGuid;

    private long smKey;

    private long actCount; // promote to handle unsigned int

    private long elapsedTime; // promote to handle unsigned int

    private byte priority;

    private byte elevatedPriority;

    private byte initialPriority;

    private byte smStateCurrent;

    /**
     * @return the guid
     */
    public long getPortGuid() {
        return portGuid;
    }

    /**
     * @param guid
     *            the guid to set
     */
    public void setPortGuid(long guid) {
        this.portGuid = guid;
    }

    /**
     * @return the smKey
     */
    public long getSmKey() {
        return smKey;
    }

    /**
     * @param smKey
     *            the smKey to set
     */
    public void setSmKey(long smKey) {
        this.smKey = smKey;
    }

    /**
     * @return the actCount
     */
    public long getActCount() {
        return actCount;
    }

    /**
     * @param actCount
     *            the actCount to set
     */
    public void setActCount(long actCount) {
        this.actCount = actCount;
    }

    /**
     * @param actCount
     *            the actCount to set
     */
    public void setActCount(int actCount) {
        this.actCount = Utils.unsignedInt(actCount);
    }

    /**
     * @return the elapsedTime
     */
    public long getElapsedTime() {
        return elapsedTime;
    }

    /**
     * @param elapsedTime
     *            the elapsedTime to set
     */
    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /**
     * @param elapsedTime
     *            the elapsedTime to set
     */
    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = Utils.unsignedInt(elapsedTime);
    }

    /**
     * @return the priority
     */
    public byte getPriority() {
        return priority;
    }

    /**
     * @param priority
     *            the priority to set
     */
    public void setPriority(byte priority) {
        this.priority = priority;
    }

    /**
     * @return the elevatedPriority
     */
    public byte getElevatedPriority() {
        return elevatedPriority;
    }

    /**
     * @param elevatedPriority
     *            the elevatedPriority to set
     */
    public void setElevatedPriority(byte elevatedPriority) {
        this.elevatedPriority = elevatedPriority;
    }

    /**
     * @return the initialPriority
     */
    public byte getInitialPriority() {
        return initialPriority;
    }

    /**
     * @param initialPriority
     *            the initialPriority to set
     */
    public void setInitialPriority(byte initialPriority) {
        this.initialPriority = initialPriority;
    }

    /**
     * @return the smStateCurrent
     */
    public byte getSmStateCurrent() {
        return smStateCurrent;
    }

    /**
     * @param smStateCurrent
     *            the smStateCurrent to set
     */
    public void setSmStateCurrent(byte smStateCurrent) {
        this.smStateCurrent = smStateCurrent;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SMInfoBean [guid=" + StringUtils.longHexString(portGuid)
                + ", smKey=" + StringUtils.longHexString(smKey) + ", actCount="
                + StringUtils.longHexString(actCount) + ", elapsedTime="
                + elapsedTime + ", priority=" + priority
                + ", elevatedPriority=" + elevatedPriority
                + ", initialPriority=" + initialPriority + ", smStateCurrent="
                + smStateCurrent + "]";
    }

}
