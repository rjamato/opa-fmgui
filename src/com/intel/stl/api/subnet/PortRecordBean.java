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
 * Title:        PortRecordBean
 * Description:  Port Record from SA populated by the connect manager.
 * 
 * @author jypak
 * @version 0.0
 */
import static com.intel.stl.api.configuration.PortState.ACTIVE;

import java.io.Serializable;
import java.util.Arrays;

import com.intel.stl.api.Utils;

public class PortRecordBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int NaN = -1;

    // Header
    private int endPortLID = NaN;

    private short portNum = NaN; // promote to handle unsigned byte

    private PortInfoBean portInfo = null;

    private PortDownReasonBean[] linkDownReasons;

    public PortRecordBean() {
        super();
    }

    public PortRecordBean(int endPortLID, byte portNum, PortInfoBean portInfo) {
        super();
        this.endPortLID = endPortLID;
        this.portNum = Utils.unsignedByte(portNum);
        this.portInfo = portInfo;
    }

    /**
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + endPortLID;
        result = prime * result + portNum;
        return result;
    }

    /**
     * @returns true if equals, false if not
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
        PortRecordBean other = (PortRecordBean) obj;
        if (endPortLID != other.endPortLID) {
            return false;
        }
        if (portNum != other.portNum) {
            return false;
        }
        return true;
    }

    /**
     * @return the endPortLID
     */
    public int getEndPortLID() {
        return endPortLID;
    }

    /**
     * @param endPortLID
     *            the endPortLID to set
     */
    public void setEndPortLID(int endPortLID) {
        this.endPortLID = endPortLID;
    }

    /**
     * @return the portNum
     */
    public short getPortNum() {
        return portNum;
    }

    /**
     * @param portNum
     *            the portNum to set
     */
    public void setPortNum(short portNum) {
        this.portNum = portNum;
    }

    /**
     * @param portNum
     *            the portNum to set
     */
    public void setPortNum(byte portNum) {
        this.portNum = Utils.unsignedByte(portNum);
    }

    /**
     * @return the portInfo
     */
    public PortInfoBean getPortInfo() {
        return portInfo;
    }

    /**
     * @param portInfo
     *            the portInfo to set
     */
    public void setPortInfo(PortInfoBean portInfo) {
        this.portInfo = portInfo;
    }

    /**
     * @return the linkDownReasons
     */
    public PortDownReasonBean[] getLinkDownReasons() {
        return linkDownReasons;
    }

    /**
     * @param linkDownReasons
     *            the linkDownReasons to set
     */
    public void setLinkDownReasons(PortDownReasonBean[] linkDownReasons) {
        this.linkDownReasons = linkDownReasons;
    }

    /**
     * @return the state
     */
    public boolean isActive() {
        if (portInfo == null || portInfo.getPortStates() == null
                || portInfo.getPortStates().getPortState() == null) {
            return false;
        }
        return portInfo.getPortStates().getPortState() == ACTIVE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PortRecordBean [endPortLID=" + endPortLID + ", portNum="
                + portNum + ", portInfo=" + portInfo + ", linkDownReasons="
                + Arrays.toString(linkDownReasons) + "]";
    }

}