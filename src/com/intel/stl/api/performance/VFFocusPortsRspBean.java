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
 *  File Name: VFFocusPortsRspBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/04/09 03:29:21  jijunwan
 *  Archive Log:    updated to match FM 390
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/12 19:30:00  jijunwan
 *  Archive Log:    introduced interface ITimestamped, and all timimg attributes implemented it, so we can easily know which attribute is associated with timestamp
 *  Archive Log:
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
import java.util.Date;

import com.intel.stl.api.ITimestamped;
import com.intel.stl.api.Utils;

/**
 * @author jijunwan
 * 
 */
public class VFFocusPortsRspBean implements ITimestamped, Serializable {
    private static final long serialVersionUID = 1L;

    private ImageIdBean imageId;

    private int nodeLid;

    private short portNumber; // unsigned byte

    private byte rate;

    private byte mtu;

    private long value; // list sorting factor

    private long value2; // good place for GUID

    private String nodeDesc;

    private int neighborLid;

    private short neighborPortNumber; // unsigned byte

    private long neighborValue;

    private long neighborGuid;

    private String neighborNodeDesc;

    private long timestamp;

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
     * @return the value
     */
    public long getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(long value) {
        this.value = value;
    }

    /**
     * @return the value2
     */
    public long getValue2() {
        return value2;
    }

    /**
     * @param value2
     *            the value2 to set
     */
    public void setValue2(long value2) {
        this.value2 = value2;
    }

    /**
     * @return the nodeDesc
     */
    public String getNodeDesc() {
        return nodeDesc;
    }

    /**
     * @param nodeDesc
     *            the nodeDesc to set
     */
    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    /**
     * @return the neighborLid
     */
    public int getNeighborLid() {
        return neighborLid;
    }

    /**
     * @param neighborLid
     *            the neighborLid to set
     */
    public void setNeighborLid(int neighborLid) {
        this.neighborLid = neighborLid;
    }

    /**
     * @return the neighborPortNumber
     */
    public short getNeighborPortNumber() {
        return neighborPortNumber;
    }

    /**
     * @param neighborPortNumber
     *            the neighborPortNumber to set
     */
    public void setNeighborPortNumber(short neighborPortNumber) {
        this.neighborPortNumber = neighborPortNumber;
    }

    /**
     * @param neighborPortNumber
     *            the neighborPortNumber to set
     */
    public void setNeighborPortNumber(byte neighborPortNumber) {
        this.neighborPortNumber = neighborPortNumber;
    }

    /**
     * @return the neighborValue
     */
    public long getNeighborValue() {
        return neighborValue;
    }

    /**
     * @param neighborValue
     *            the neighborValue to set
     */
    public void setNeighborValue(long neighborValue) {
        this.neighborValue = neighborValue;
    }

    /**
     * @return the neighborGuid
     */
    public long getNeighborGuid() {
        return neighborGuid;
    }

    /**
     * @param neighborGuid
     *            the neighborGuid to set
     */
    public void setNeighborGuid(long neighborGuid) {
        this.neighborGuid = neighborGuid;
    }

    /**
     * @return the neighborNodeDesc
     */
    public String getNeighborNodeDesc() {
        return neighborNodeDesc;
    }

    /**
     * @param neighborNodeDesc
     *            the neighborNodeDesc to set
     */
    public void setNeighborNodeDesc(String neighborNodeDesc) {
        this.neighborNodeDesc = neighborNodeDesc;
    }

    /**
     * @return the timestamp
     */
    @Override
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.ITimestamped#getTimestampDate()
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
        return "VFFocusPortRspBean [nodeLid=" + nodeLid + ", portNumber="
                + portNumber + ", rate=" + rate + ", mtu=" + mtu + ", value=0x"
                + Long.toHexString(value) + ", value2=0x"
                + Long.toHexString(value2) + ", nodeDesc=" + nodeDesc
                + ", neighborLid=" + neighborLid + ", neighborPortNumber="
                + neighborPortNumber + ", neighborValue=0x"
                + Long.toHexString(neighborValue) + ", neighborGuid=0x"
                + Long.toHexString(neighborGuid) + ", neighborNodeDesc="
                + neighborNodeDesc + "]";
    }
}
