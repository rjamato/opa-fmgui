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
 * Title:        NodeRecordBean
 * Description:  Node Record from SA populated by the connect manager.
 * 
 * @author jypak
 * @version 0.0
 */
import java.io.Serializable;

public class NodeRecordBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private NodeInfoBean nodeInfo;

    private int lid;

    private String nodeDesc;

    // this should be set by DB
    private transient boolean active = true;

    public NodeRecordBean() {
        super();
    }

    public NodeRecordBean(NodeInfoBean nodeInfo, int lid, String nodeDesc) {
        super();
        this.nodeInfo = nodeInfo;
        this.lid = lid;
        this.nodeDesc = nodeDesc;
    }

    public NodeType getNodeType() {
        if (nodeInfo == null) {
            return null;
        }
        return nodeInfo.getNodeTypeEnum();
    }

    public void setNodeType(NodeType nodeType) {
        if (nodeInfo != null) {
            nodeInfo.setNodeTypeEnum(nodeType);
        }
    }

    /**
     * @return the nodeInfo
     */
    public NodeInfoBean getNodeInfo() {
        return nodeInfo;
    }

    /**
     * @param nodeInfo
     *            the nodeInfo to set
     */
    public void setNodeInfo(NodeInfoBean nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

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
     * @return the state
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setActive(boolean state) {
        this.active = state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NodeRecordBean [nodeInfo=" + nodeInfo + ", lid=" + lid
                + ", nodeDesc=" + nodeDesc + ", active=" + active + "]";
    }
}