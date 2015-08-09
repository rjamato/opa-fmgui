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
 *  File Name: TrapKeyBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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

import java.io.Serializable;

import com.intel.stl.api.subnet.GIDBean;

public class TrapKeyBean implements Serializable {
    private static final long serialVersionUID = -7106995880090277274L;
    
    private int lid1;
    private int lid2;
    private int key;
    private byte sl;
    private GIDBean gid1;
    private GIDBean gid2;
    private int qp1;
    private int qp2;
    
    /**
     * @return the lid1
     */
    public int getLid1() {
        return lid1;
    }

    /**
     * @param lid1 the lid1 to set
     */
    public void setLid1(int lid1) {
        this.lid1 = lid1;
    }

    /**
     * @return the lid2
     */
    public int getLid2() {
        return lid2;
    }

    /**
     * @param lid2 the lid2 to set
     */
    public void setLid2(int lid2) {
        this.lid2 = lid2;
    }

    /**
     * @return the key
     */
    public int getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(int key) {
        this.key = key;
    }

    /**
     * @return the sl
     */
    public byte getSl() {
        return sl;
    }

    /**
     * @param sl the sl to set
     */
    public void setSl(byte sl) {
        this.sl = sl;
    }

    /**
     * @return the gid1
     */
    public GIDBean getGid1() {
        return gid1;
    }

    /**
     * @param gid1 the gid1 to set
     */
    public void setGid1(GIDBean gid1) {
        this.gid1 = gid1;
    }

    /**
     * @return the gid2
     */
    public GIDBean getGid2() {
        return gid2;
    }

    /**
     * @param gid2 the gid2 to set
     */
    public void setGid2(GIDBean gid2) {
        this.gid2 = gid2;
    }

    /**
     * @return the pq1
     */
    public int getQp1() {
        return qp1;
    }

    /**
     * @param pq1 the pq1 to set
     */
    public void setQp1(int pq1) {
        this.qp1 = pq1;
    }

    /**
     * @return the pq2
     */
    public int getQp2() {
        return qp2;
    }

    /**
     * @param pq2 the pq2 to set
     */
    public void setQp2(int pq2) {
        this.qp2 = pq2;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TrapKeyBean [lid1=" + lid1 + ", lid2=" + lid2 + ", key=" + key
                + ", sl=" + sl + ", gid1=" + gid1 + ", gid2=" + gid2 + ", pq1="
                + qp1 + ", pq2=" + qp2 + "]";
    }
    
}
