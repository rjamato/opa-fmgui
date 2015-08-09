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
public class TraceRecordBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private short idGeneration;

    private byte nodeType;

    private short entryPort; // promote to handle unsigned byte

    private short exitPort; // promote to handle unsigned byte

    private long nodeId;

    private long chassisId;

    private long entryPortId;

    private long exitPortId;

    public TraceRecordBean() {
        super();
    }

    public TraceRecordBean(short idGeneration, byte nodeType, byte entryPort,
            byte exitPort, long nodeId, long chassisId, long entryPortId,
            long exirPortId) {
        super();
        this.idGeneration = idGeneration;
        this.nodeType = nodeType;
        this.entryPort = Utils.unsignedByte(entryPort);
        this.exitPort = Utils.unsignedByte(exitPort);
        this.nodeId = nodeId;
        this.chassisId = chassisId;
        this.entryPortId = entryPortId;
        this.exitPortId = exirPortId;
    }

    /**
     * @return the idGeneration
     */
    public short getIdGeneration() {
        return idGeneration;
    }

    /**
     * @param idGeneration
     *            the idGeneration to set
     */
    public void setIdGeneration(short idGeneration) {
        this.idGeneration = idGeneration;
    }

    /**
     * @return the nodeType
     */
    public byte getNodeType() {
        return nodeType;
    }

    /**
     * @param nodeType
     *            the nodeType to set
     */
    public void setNodeType(byte nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * @return the entryPort
     */
    public short getEntryPort() {
        return entryPort;
    }

    /**
     * @param entryPort
     *            the entryPort to set
     */
    public void setEntryPort(short entryPort) {
        this.entryPort = entryPort;
    }

    /**
     * @param entryPort
     *            the entryPort to set
     */
    public void setEntryPort(byte entryPort) {
        this.entryPort = Utils.unsignedByte(entryPort);
    }

    /**
     * @return the exitPort
     */
    public short getExitPort() {
        return exitPort;
    }

    /**
     * @param exitPort
     *            the exitPort to set
     */
    public void setExitPort(short exitPort) {
        this.exitPort = exitPort;
    }

    /**
     * @param exitPort
     *            the exitPort to set
     */
    public void setExitPort(byte exitPort) {
        this.exitPort = Utils.unsignedByte(exitPort);
    }

    /**
     * @return the nodeId
     */
    public long getNodeId() {
        return nodeId;
    }

    /**
     * @param nodeId
     *            the nodeId to set
     */
    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * @return the chassisId
     */
    public long getChassisId() {
        return chassisId;
    }

    /**
     * @param chassisId
     *            the chassisId to set
     */
    public void setChassisId(long chassisId) {
        this.chassisId = chassisId;
    }

    /**
     * @return the entryPortId
     */
    public long getEntryPortId() {
        return entryPortId;
    }

    /**
     * @param entryPortId
     *            the entryPortId to set
     */
    public void setEntryPortId(long entryPortId) {
        this.entryPortId = entryPortId;
    }

    /**
     * @return the exirPortId
     */
    public long getExitPortId() {
        return exitPortId;
    }

    /**
     * @param exirPortId
     *            the exirPortId to set
     */
    public void setExitPortId(long exirPortId) {
        this.exitPortId = exirPortId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TraceRecordBean [idGeneration="
                + StringUtils.longHexString(idGeneration) + ", nodeType="
                + nodeType + ", entryPort=" + entryPort + ", exitPort="
                + exitPort + ", nodeId=" + StringUtils.longHexString(nodeId)
                + ", chassisId=" + StringUtils.longHexString(chassisId)
                + ", entryPortId=" + StringUtils.longHexString(entryPortId)
                + ", exitPortId=" + StringUtils.longHexString(exitPortId) + "]";
    }

}
