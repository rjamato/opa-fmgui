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
 *  File Name: NodeInfoBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/02/23 22:28:00  jijunwan
 *  Archive Log:    added method #toString to help debug
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/04 21:37:55  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *
 *  Overview: Node info from SA populated by the connect manager.
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.api.subnet;

import java.io.Serializable;

import com.intel.stl.api.Utils;

public class NodeInfoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte baseVersion;

    private byte classVersion;

    private NodeType nodeType;

    private short numPorts; // promote to handle unsigned byte, so we handle 256
                            // ports if it happens

    private long sysImageGUID;

    private long nodeGUID;

    private long portGUID;

    private int partitionCap; // promote to handle unsigned short

    private short deviceID;

    private int revision;

    private short localPortNum; // promote to handle unsigned byte, so we handle
                                // 256

    // ports if it happens

    private int vendorID;

    public NodeInfoBean() {
        super();
    }

    public NodeInfoBean(byte baseVersion, byte classVersion, byte nodeType,
            byte numPorts, long sysImageGUID, long nodeGUID, long portGUID,
            short partitionCap, short deviceID, int revision,
            byte localPortNum, int vendorID) {
        super();
        this.baseVersion = baseVersion;
        this.classVersion = classVersion;
        this.nodeType = NodeType.getNodeType(nodeType);
        this.numPorts = Utils.unsignedByte(numPorts);
        this.sysImageGUID = sysImageGUID;
        this.nodeGUID = nodeGUID;
        this.portGUID = portGUID;
        this.partitionCap = Utils.unsignedShort(partitionCap);
        this.deviceID = deviceID;
        this.revision = revision;
        this.localPortNum = Utils.unsignedByte(localPortNum);
        this.vendorID = vendorID;
    }

    /**
     * @return the baseVersion
     */
    public byte getBaseVersion() {
        return baseVersion;
    }

    /**
     * @param baseVersion
     *            the baseVersion to set
     */
    public void setBaseVersion(byte baseVersion) {
        this.baseVersion = baseVersion;
    }

    /**
     * @return the classVersion
     */
    public byte getClassVersion() {
        return classVersion;
    }

    /**
     * @param classVersion
     *            the classVersion to set
     */
    public void setClassVersion(byte classVersion) {
        this.classVersion = classVersion;
    }

    /**
     * @return the nodeType
     */
    public byte getNodeType() {
        return nodeType.getId();
    }

    /**
     * @param nodeType
     *            the nodeType to set
     */
    public void setNodeType(byte nodeType) {
        this.nodeType = NodeType.getNodeType(nodeType);
    }

    public NodeType getNodeTypeEnum() {
        return nodeType;
    }

    public void setNodeTypeEnum(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * @return the numPorts
     */
    public short getNumPorts() {
        return numPorts;
    }

    /**
     * @param numPorts
     *            the numPorts to set
     */
    public void setNumPorts(short numPorts) {
        this.numPorts = numPorts;
    }

    /**
     * @param numPorts
     *            the numPorts to set
     */
    public void setNumPorts(byte numPorts) {
        this.numPorts = Utils.unsignedByte(numPorts);
    }

    /**
     * @return the sysImageGUID
     */
    public long getSysImageGUID() {
        return sysImageGUID;
    }

    /**
     * @param sysImageGUID
     *            the sysImageGUID to set
     */
    public void setSysImageGUID(long sysImageGUID) {
        this.sysImageGUID = sysImageGUID;
    }

    /**
     * @return the nodeGUID
     */
    public long getNodeGUID() {
        return nodeGUID;
    }

    /**
     * @param nodeGUID
     *            the nodeGUID to set
     */
    public void setNodeGUID(long nodeGUID) {
        this.nodeGUID = nodeGUID;
    }

    /**
     * @return the portGUID
     */
    public long getPortGUID() {
        return portGUID;
    }

    /**
     * @param portGUID
     *            the portGUID to set
     */
    public void setPortGUID(long portGUID) {
        this.portGUID = portGUID;
    }

    /**
     * @return the partitionCap
     */
    public int getPartitionCap() {
        return partitionCap;
    }

    /**
     * @param partitionCap
     *            the partitionCap to set
     */
    public void setPartitionCap(int partitionCap) {
        this.partitionCap = partitionCap;
    }

    public void setPartitionCap(short partitionCap) {
        this.partitionCap = Utils.unsignedShort(partitionCap);
    }

    /**
     * @return the deviceID
     */
    public short getDeviceID() {
        return deviceID;
    }

    /**
     * @param deviceID
     *            the deviceID to set
     */
    public void setDeviceID(short deviceID) {
        this.deviceID = deviceID;
    }

    /**
     * @return the revision
     */
    public int getRevision() {
        return revision;
    }

    /**
     * @param revision
     *            the revision to set
     */
    public void setRevision(int revision) {
        this.revision = revision;
    }

    /**
     * @return the localPortNum
     */
    public short getLocalPortNum() {
        return localPortNum;
    }

    /**
     * @param localPortNum
     *            the localPortNum to set
     */
    public void setLocalPortNum(short localPortNum) {
        this.localPortNum = localPortNum;
    }

    /**
     * @param localPortNum
     *            the localPortNum to set
     */
    public void setLocalPortNum(byte localPortNum) {
        this.localPortNum = Utils.unsignedByte(localPortNum);
    }

    /**
     * @return the vendorID
     */
    public int getVendorID() {
        return vendorID;
    }

    /**
     * @param vendorID
     *            the vendorID to set
     */
    public void setVendorID(int vendorID) {
        this.vendorID = vendorID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NodeInfoBean [baseVersion=" + baseVersion + ", classVersion="
                + classVersion + ", nodeType="
                + (nodeType == null ? "null" : nodeType.getId())
                + ", numPorts=" + numPorts + ", sysImageGUID=0x"
                + Long.toHexString(sysImageGUID) + ", nodeGUID=0x"
                + Long.toHexString(nodeGUID) + ", portGUID=0x"
                + Long.toHexString(portGUID) + ", partitionCap=" + partitionCap
                + ", deviceID=" + deviceID + ", revision=" + revision
                + ", localPortNum=" + localPortNum + ", vendorID=" + vendorID
                + "]";
    }

}