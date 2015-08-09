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
 *  File Name: TrapMKey.java
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
import java.util.Arrays;

public class TrapMKeyBean implements Serializable {
    private static final long serialVersionUID = 3639303016600181495L;
    
    private int lid;
    private int drSLid;
    private byte method;
    private boolean drNotice;
    private boolean drPathTruncated;
    private byte drHopCount;
    private short attributeID;
    private int attributeModifier;
    private long mKey;
    private byte[] drReturnPath;
    
    /**
     * @return the lid
     */
    public int getLid() {
        return lid;
    }

    /**
     * @param lid the lid to set
     */
    public void setLid(int lid) {
        this.lid = lid;
    }

    /**
     * @return the drSLid
     */
    public int getDrSLid() {
        return drSLid;
    }

    /**
     * @param drSLid the drSLid to set
     */
    public void setDrSLid(int drSLid) {
        this.drSLid = drSLid;
    }

    /**
     * @return the method
     */
    public byte getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(byte method) {
        this.method = method;
    }

    /**
     * @return the drNotice
     */
    public boolean isDrNotice() {
        return drNotice;
    }

    /**
     * @param drNotice the drNotice to set
     */
    public void setDrNotice(boolean drNotice) {
        this.drNotice = drNotice;
    }

    /**
     * @return the drPathTruncated
     */
    public boolean isDrPathTruncated() {
        return drPathTruncated;
    }

    /**
     * @param drPathTruncated the drPathTruncated to set
     */
    public void setDrPathTruncated(boolean drPathTruncated) {
        this.drPathTruncated = drPathTruncated;
    }

    /**
     * @return the drHopCount
     */
    public byte getDrHopCount() {
        return drHopCount;
    }

    /**
     * @param drHopCount the drHopCount to set
     */
    public void setDrHopCount(byte drHopCount) {
        this.drHopCount = drHopCount;
    }

    /**
     * @return the attributeID
     */
    public short getAttributeID() {
        return attributeID;
    }

    /**
     * @param attributeID the attributeID to set
     */
    public void setAttributeID(short attributeID) {
        this.attributeID = attributeID;
    }

    /**
     * @return the attributeModifier
     */
    public int getAttributeModifier() {
        return attributeModifier;
    }

    /**
     * @param attributeModifier the attributeModifier to set
     */
    public void setAttributeModifier(int attributeModifier) {
        this.attributeModifier = attributeModifier;
    }

    /**
     * @return the mKey
     */
    public long getMKey() {
        return mKey;
    }

    /**
     * @param mKey the mKey to set
     */
    public void setMKey(long mKey) {
        this.mKey = mKey;
    }

    /**
     * @return the drReturnPath
     */
    public byte[] getDrReturnPath() {
        return drReturnPath;
    }

    /**
     * @param drReturnPath the drReturnPath to set
     */
    public void setDrReturnPath(byte[] drReturnPath) {
        this.drReturnPath = drReturnPath;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TrapMKeyBean [lid=" + lid + ", drSLid=" + drSLid + ", method="
                + method + ", drNotice=" + drNotice + ", drPathTruncated="
                + drPathTruncated + ", drHopCount=" + drHopCount
                + ", attributeID=" + attributeID + ", attributeModifier="
                + attributeModifier + ", mKey=" + mKey + ", drReturnPath="
                + Arrays.toString(drReturnPath) + "]";
    }
    
}
