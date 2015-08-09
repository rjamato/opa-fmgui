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
 *  File Name: PortSource.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/02/04 21:38:03  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/05 23:43:47  jijunwan
 *  Archive Log:    forgot to check in changes on backend
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/08 18:59:21  jijunwan
 *  Archive Log:    added Notice Simulator to simulate notices from FM
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 15:14:03  jijunwan
 *  Archive Log:    notice and trap implementation
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.notice;

import com.intel.stl.api.subnet.NodeType;

public class PortSource extends NodeSource {
    private static final long serialVersionUID = -6342370130884635673L;

    private short portNum;

    public PortSource() {
        super();
    }

    public PortSource(int lid, String nodeName, NodeType type, short portNum) {
        super(lid, nodeName, type);
        this.portNum = portNum;
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

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.notice.NodeSource#getDescription()
     */
    @Override
    public String getDescription() {
        return getNodeName() + ":" + portNum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + portNum;
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PortSource other = (PortSource) obj;
        if (portNum != other.portNum) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PortSource [getNodeName()=" + getNodeName()
                + ", getNodeType()=" + getNodeType() + "getLid()=" + getLid()
                + ", portNum=" + portNum + "]";
    }

}
