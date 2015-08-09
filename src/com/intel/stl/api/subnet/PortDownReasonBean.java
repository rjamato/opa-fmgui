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
 *  File Name: PortDownReasonBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/02/06 00:26:56  jijunwan
 *  Archive Log:    added neighbor link down reason to match FM 325
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/11 23:09:28  jijunwan
 *  Archive Log:    renamed PortUtils to Utils
 *  Archive Log:    moved convertFromUnixTime to Utils
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/05 19:25:01  jypak
 *  Archive Log:    Link Down Error Log updates
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/09 18:36:46  jijunwan
 *  Archive Log:    updated PortInfoRecord, SMInfo to the latest data structure
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.subnet;

import java.io.Serializable;
import java.util.Date;

import com.intel.stl.api.Utils;

public class PortDownReasonBean implements Serializable {
    private static final long serialVersionUID = -4990045846635321180L;

    private byte neighborLinkDownReason;

    private byte linkDownReason;

    private long timestamp;

    /**
     * Description:
     * 
     */
    public PortDownReasonBean() {
        super();
    }

    /**
     * Description:
     * 
     * @param linkDownReason
     * @param timestamp
     */
    public PortDownReasonBean(byte neighborLinkDownReason, byte linkDownReason,
            long timestamp) {
        super();
        this.linkDownReason = linkDownReason;
        this.neighborLinkDownReason = neighborLinkDownReason;
        this.timestamp = timestamp;
    }

    /**
     * @return the linkDownReason
     */
    public byte getLinkDownReason() {
        return linkDownReason;
    }

    /**
     * @param linkDownReason
     *            the linkDownReason to set
     */
    public void setLinkDownReason(byte linkDownReason) {
        this.linkDownReason = linkDownReason;
    }

    /**
     * @return the neighborLinkDownReason
     */
    public byte getNeighborLinkDownReason() {
        return neighborLinkDownReason;
    }

    /**
     * @param neighborLinkDownReason
     *            the neighborLinkDownReason to set
     */
    public void setNeighborLinkDownReason(byte neighborLinkDownReason) {
        this.neighborLinkDownReason = neighborLinkDownReason;
    }

    /**
     * @return the timestamp
     */
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

    /**
     * 
     * <i>Description:</i> returns timestamp as Date
     * 
     * @return timestamp converted to Date
     */
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
        return "PortDownReasonBean [linkDownReason=" + linkDownReason
                + ", neighborLinkDownReason=" + neighborLinkDownReason
                + ", timestamp=" + timestamp + "]";
    }

}
