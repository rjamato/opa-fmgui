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
 *  File Name: PortEntry.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/02/04 21:44:16  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/18 14:59:32  jijunwan
 *  Archive Log:    Added jumping to destination support to TopN chart via popup menu
 *  Archive Log:    Added label highlight for chart view
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 20:54:20  jijunwan
 *  Archive Log:    fixed port link
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

public class PortEntry implements Comparable<PortEntry> {
    private final String nodeName;

    private final int nodeLid;

    private final short portNum;

    private Object object;

    /**
     * Description:
     * 
     * @param nodeName
     * @param nodeLid
     * @param portNum
     */
    public PortEntry(String nodeName, int nodeLid, short portNum) {
        super();
        this.nodeName = nodeName;
        this.nodeLid = nodeLid;
        this.portNum = portNum;
    }

    /**
     * @return the nodeName
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * @return the nodeLid
     */
    public int getNodeLid() {
        return nodeLid;
    }

    /**
     * @return the portNum
     */
    public short getPortNum() {
        return portNum;
    }

    /**
     * @return the object
     */
    public Object getObject() {
        return object;
    }

    /**
     * @param object
     *            the object to set
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return nodeName + ":" + portNum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + nodeLid;
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
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PortEntry other = (PortEntry) obj;
        if (nodeLid != other.nodeLid) {
            return false;
        }
        if (portNum != other.portNum) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(PortEntry o) {
        String s1 = toString();
        String s2 = o.toString();
        return s1.compareTo(s2);
    }

}
