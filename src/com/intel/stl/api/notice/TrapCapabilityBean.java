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
 *  File Name: TrapCapabilityBean.java
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

public class TrapCapabilityBean implements Serializable {
    private static final long serialVersionUID = 93229705407669540L;
    
    private int lid;
    private int capabilityMask;
    private short capabilityMask2;
    private short capabilityMask3;
    private boolean linkSpeedEnabledChange;
    private boolean linkWidthEnabledChange;
    private boolean NodeDescriptionChange;
    
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
     * @return the capabilityMask
     */
    public int getCapabilityMask() {
        return capabilityMask;
    }

    /**
     * @param capabilityMask the capabilityMask to set
     */
    public void setCapabilityMask(int capabilityMask) {
        this.capabilityMask = capabilityMask;
    }

    /**
     * @return the capabilityMask2
     */
    public short getCapabilityMask2() {
        return capabilityMask2;
    }

    /**
     * @param capabilityMask2 the capabilityMask2 to set
     */
    public void setCapabilityMask2(short capabilityMask2) {
        this.capabilityMask2 = capabilityMask2;
    }

    /**
     * @return the capabilityMask3
     */
    public short getCapabilityMask3() {
        return capabilityMask3;
    }

    /**
     * @param capabilityMask3 the capabilityMask3 to set
     */
    public void setCapabilityMask3(short capabilityMask3) {
        this.capabilityMask3 = capabilityMask3;
    }

    /**
     * @return the linkSpeedEnabledChange
     */
    public boolean isLinkSpeedEnabledChange() {
        return linkSpeedEnabledChange;
    }

    /**
     * @param linkSpeedEnabledChange the linkSpeedEnabledChange to set
     */
    public void setLinkSpeedEnabledChange(boolean linkSpeedEnabledChange) {
        this.linkSpeedEnabledChange = linkSpeedEnabledChange;
    }

    /**
     * @return the linkWidthEnabledChange
     */
    public boolean isLinkWidthEnabledChange() {
        return linkWidthEnabledChange;
    }

    /**
     * @param linkWidthEnabledChange the linkWidthEnabledChange to set
     */
    public void setLinkWidthEnabledChange(boolean linkWidthEnabledChange) {
        this.linkWidthEnabledChange = linkWidthEnabledChange;
    }

    /**
     * @return the nodeDescriptionChange
     */
    public boolean isNodeDescriptionChange() {
        return NodeDescriptionChange;
    }

    /**
     * @param nodeDescriptionChange the nodeDescriptionChange to set
     */
    public void setNodeDescriptionChange(boolean nodeDescriptionChange) {
        NodeDescriptionChange = nodeDescriptionChange;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TrapCapabilityBean [lid=" + lid + ", capabilityMask="
                + capabilityMask + ", capabilityMask2=" + capabilityMask2
                + ", capabilityMask3=" + capabilityMask3
                + ", linkSpeedEnabledChange=" + linkSpeedEnabledChange
                + ", linkWidthEnabledChange=" + linkWidthEnabledChange
                + ", NodeDescriptionChange=" + NodeDescriptionChange + "]";
    }
    
}
