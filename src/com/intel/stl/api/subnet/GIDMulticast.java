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

import java.util.Arrays;

/**
 * The following static classes refer to the different usages of the GID, as
 * stated in the IB specification (section 4.1.1) and the corresponding header
 * file in the FM implementation. They are not used in the GUI application; they
 * are included here for completeness /ALL_EMB/IbAcess/Common/Inc/ib_type.h
 * 
 * @author jijunwan
 * 
 */
public class GIDMulticast extends GIDBean {
    private static final long serialVersionUID = 1L;

    private byte formatPrefix;

    private byte flags;

    private byte scope;

    private byte[] groupId;

    public GIDMulticast() {
        super();
    }

    public GIDMulticast(byte formatPrefix, byte flags, byte scope,
            byte[] groupId) {
        super();
        this.formatPrefix = formatPrefix;
        this.flags = flags;
        this.scope = scope;
        this.groupId = groupId;
    }

    /**
     * @return the formatPrefix
     */
    public byte getFormatPrefix() {
        return formatPrefix;
    }

    /**
     * @param formatPrefix
     *            the formatPrefix to set
     */
    public void setFormatPrefix(byte formatPrefix) {
        this.formatPrefix = formatPrefix;
    }

    /**
     * @return the flags
     */
    public byte getFlags() {
        return flags;
    }

    /**
     * @param flags
     *            the flags to set
     */
    public void setFlags(byte flags) {
        this.flags = flags;
    }

    /**
     * @return the scope
     */
    public byte getScope() {
        return scope;
    }

    /**
     * @param scope
     *            the scope to set
     */
    public void setScope(byte scope) {
        this.scope = scope;
    }

    /**
     * @return the groupId
     */
    public byte[] getGroupId() {
        return groupId;
    }

    /**
     * @param groupId
     *            the groupId to set
     */
    public void setGroupId(byte[] groupId) {
        this.groupId = groupId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Multicast [formatPrefix=" + formatPrefix + ", flags=" + flags
                + ", scope=" + scope + ", groupId=" + Arrays.toString(groupId)
                + "]";
    }

}
