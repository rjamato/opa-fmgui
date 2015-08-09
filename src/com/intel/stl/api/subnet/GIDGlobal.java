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
 * @author jijunwan
 * 
 */
public class GIDGlobal extends GIDBean {
    private static final long serialVersionUID = 1L;

    private long subnetPrefix;

    private long interfaceId;

    public GIDGlobal() {
        super();
    }

    public GIDGlobal(long subnetPrefix, long interfaceId) {
        super();
        this.subnetPrefix = subnetPrefix;
        this.interfaceId = interfaceId;
    }

    /**
     * @return the subnetPrefix
     */
    @Override
    public long getSubnetPrefix() {
        return subnetPrefix;
    }

    /**
     * @param subnetPrefix
     *            the subnetPrefix to set
     */
    @Override
    public void setSubnetPrefix(long subnetPrefix) {
        this.subnetPrefix = subnetPrefix;
    }

    /**
     * @return the interfaceID
     */
    @Override
    public long getInterfaceID() {
        return interfaceId;
    }

    /**
     * @param interfaceID
     *            the interfaceID to set
     */
    @Override
    public void setInterfaceID(long interfaceId) {
        this.interfaceId = interfaceId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GIDGlobal [subnetPrefix=0x" + Long.toHexString(subnetPrefix)
                + ", interfaceID=0x" + Long.toHexString(interfaceId) + "]";
    }

}
