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
 *  File Name: SMInfoDataBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/04 21:37:53  jijunwan
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

import com.intel.stl.api.Utils;

/**
 * @author jijunwan
 * 
 */
public class SMInfoDataBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int lid;

    private byte priority;

    private byte state;

    private short portNumber; // unsigned byte

    private long smPortGuid;

    private String smNodeDesc;

    /**
     * @return the lid
     */
    public int getLid() {
        return lid;
    }

    /**
     * @param lid
     *            the lid to set
     */
    public void setLid(int lid) {
        this.lid = lid;
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
     * @return the state
     */
    public byte getState() {
        return state;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(byte state) {
        this.state = state;
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
     * @return the smPortGuid
     */
    public long getSmPortGuid() {
        return smPortGuid;
    }

    /**
     * @param smPortGuid
     *            the smPortGuid to set
     */
    public void setSmPortGuid(long smPortGuid) {
        this.smPortGuid = smPortGuid;
    }

    /**
     * @return the smNodeDesc
     */
    public String getSmNodeDesc() {
        return smNodeDesc;
    }

    /**
     * @param smNodeDesc
     *            the smNodeDesc to set
     */
    public void setSmNodeDesc(String smNodeDesc) {
        this.smNodeDesc = smNodeDesc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SMInfoDataBean [lid=" + lid + ", priority=" + priority
                + ", state=" + state + ", portNumber=" + portNumber
                + ", smPortGuid=0x" + Long.toHexString(smPortGuid)
                + ", smNodeDesc=" + smNodeDesc + "]";
    }
}
