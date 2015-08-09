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
 *  File Name: PortConfigBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/02/04 21:37:53  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/12 20:07:44  jijunwan
 *  Archive Log:    1) renamed HexUtils to StringUtils
 *  Archive Log:    2) added a method to StringUtils to get error message for an exception
 *  Archive Log:    3) changed all code to call StringUtils to get error message
 *  Archive Log:    4) some extra ode format change
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/03 20:49:31  jijunwan
 *  Archive Log:    added VF related PA attributes: GetVFList, GetVFInfo, GetVFConfig, GetVFPortCounters and GetVFFocusPorts
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:23:05  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/01 21:37:06  jijunwan
 *  Archive Log:    Added PA attributes GroupConfig
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.performance;

import java.io.Serializable;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.Utils;

public class PortConfigBean implements Serializable {
    private static final long serialVersionUID = -215247442438927532L;

    private long nodeGUID;

    private String nodeDesc;

    private int nodeLid;

    private short portNumber; // promote to handle unsigned byte

    public PortConfigBean() {
    }

    public PortConfigBean(long nodeGUID, String nodeDesc, int nodeLid,
            short portNumber) {
        super();
        this.nodeGUID = nodeGUID;
        this.nodeDesc = nodeDesc;
        this.nodeLid = nodeLid;
        this.portNumber = portNumber;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PortConfigBean [nodeGUID="
                + StringUtils.longHexString(nodeGUID) + ", nodeDesc="
                + nodeDesc + ", nodeLid=" + StringUtils.intHexString(nodeLid)
                + ", portNumber=" + portNumber + "]";
    }

}
