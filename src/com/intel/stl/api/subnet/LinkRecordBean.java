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
 * Title:        LinkRecordBean
 * Description:  Link Record from SA populated by the connect manager.
 *               Implementation of a representation of a connection between two ports.
 *               An abstraction.
 *               
 * @author jypak
 * @version 0.0
 */
import java.io.Serializable;

import com.intel.stl.api.Utils;

public class LinkRecordBean implements Serializable {
    private static final long serialVersionUID = 8951077237091003130L;

    private short fromPortIndex; // promote to handle unsigned byte

    private int fromLID;

    private short toPortIndex; // promote to handle unsigned byte

    private int toLID;

    // State is based on source
    private boolean active = true;

    public LinkRecordBean() {
        super();
    }

    public LinkRecordBean(int fromLID, byte fromPortIndex, int toLID,
            byte toPortIndex) {
        this(fromLID, Utils.unsignedByte(fromPortIndex), toLID, Utils
                .unsignedByte(toPortIndex));
    }

    /**
     * Description:
     * 
     * @param fromLID
     * @param fromPortIndex
     * @param toLID
     * @param toPortIndex
     */
    public LinkRecordBean(int fromLID, short fromPortIndex, int toLID,
            short toPortIndex) {
        super();
        this.fromLID = fromLID;
        this.fromPortIndex = fromPortIndex;
        this.toLID = toLID;
        this.toPortIndex = toPortIndex;
    }

    /**
     * @return the fromPortIndex
     */
    public short getFromPortIndex() {
        return fromPortIndex;
    }

    /**
     * @param fromPortIndex
     *            the fromPortIndex to set
     */
    public void setFromPortIndex(short fromPortIndex) {
        this.fromPortIndex = fromPortIndex;
    }

    /**
     * @param fromPortIndex
     *            the fromPortIndex to set
     */
    public void setFromPortIndex(byte fromPortIndex) {
        this.fromPortIndex = Utils.unsignedByte(fromPortIndex);
    }

    /**
     * @return the fromLid
     */
    public int getFromLID() {
        return fromLID;
    }

    /**
     * @param fromLid
     *            the fromLid to set
     */
    public void setFromLID(int fromLid) {
        this.fromLID = fromLid;
    }

    /**
     * @return the toPortIndex
     */
    public short getToPortIndex() {
        return toPortIndex;
    }

    /**
     * @param toPortIndex
     *            the toPortIndex to set
     */
    public void setToPortIndex(short toPortIndex) {
        this.toPortIndex = toPortIndex;
    }

    /**
     * @param toPortIndex
     *            the toPortIndex to set
     */
    public void setToPortIndex(byte toPortIndex) {
        this.toPortIndex = Utils.unsignedByte(toPortIndex);
    }

    /**
     * @return the toLid
     */
    public int getToLID() {
        return toLID;
    }

    /**
     * @param toLid
     *            the toLid to set
     */
    public void setToLID(int toLid) {
        this.toLID = toLid;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active
     *            the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LinkRecordBean [fromPortIndex=" + fromPortIndex + ", fromLID="
                + fromLID + ", toPortIndex=" + toPortIndex + ", toLID=" + toLID
                + ", active=" + active + "]";
    }

}