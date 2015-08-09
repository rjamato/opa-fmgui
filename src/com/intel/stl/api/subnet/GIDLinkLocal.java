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
 * The following static classes refer to the different usages of the GID, as
 * stated in the IB specification (section 4.1.1) and the corresponding header
 * file in the FM implementation. They are not used in the GUI application; they
 * are included here for completeness /ALL_EMB/IbAcess/Common/Inc/ib_type.h
 * 
 * @author jijunwan
 * 
 */
public class GIDLinkLocal extends GIDBean {
    private static final long serialVersionUID = 1L;

    private short formatPrefix;

    public GIDLinkLocal() {
        super();
    }

    public GIDLinkLocal(short formatPrefix, long interfaceID) {
        super();
        this.formatPrefix = formatPrefix;
        this.interfaceID = interfaceID;
    }

    /**
     * @return the formatPrefix
     */
    public short getFormatPrefix() {
        return formatPrefix;
    }

    /**
     * @param formatPrefix
     *            the formatPrefix to set
     */
    public void setFormatPrefix(short formatPrefix) {
        this.formatPrefix = formatPrefix;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LinkLocal [formatPrefix=0x" + Long.toHexString(formatPrefix)
                + ", interfaceID=0x" + Long.toHexString(interfaceID) + "]";
    }

}