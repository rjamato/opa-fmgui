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

/**
 * @author jijunwan
 * 
 */
public class GIDBean implements Serializable {
    private static final long serialVersionUID = -1431214650310217689L;

    private long subnetPrefix;

    protected long interfaceID;

    public GIDBean() {
    }

    public GIDBean(long subnetPrefix, long interfaceId) {
        this.subnetPrefix = subnetPrefix;
        this.interfaceID = interfaceId;
    }

    /**
     * @return the subnetPrefix
     */
    public long getSubnetPrefix() {
        return subnetPrefix;
    }

    /**
     * @param subnetPrefix
     *            the subnetPrefix to set
     */
    public void setSubnetPrefix(long subnetPrefix) {
        this.subnetPrefix = subnetPrefix;
    }

    /**
     * @return the interfaceID
     */
    public long getInterfaceID() {
        return interfaceID;
    }

    /**
     * @param interfaceID
     *            the interfaceID to set
     */
    public void setInterfaceID(long interfaceId) {
        this.interfaceID = interfaceId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GIDBean [subnetPrefix=0x" + Long.toHexString(subnetPrefix)
                + ", interfaceID=0x" + Long.toHexString(interfaceID) + "]";
    }
}
