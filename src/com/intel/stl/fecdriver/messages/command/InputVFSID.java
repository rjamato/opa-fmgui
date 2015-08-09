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
package com.intel.stl.fecdriver.messages.command;

import com.intel.stl.api.StringUtils;

/**
 * @author jijunwan
 * 
 */
public class InputVFSID extends InputImageId {
    private final long vfSID;

    public InputVFSID(long vfSID, long imageNumber, int imageOffset) {
        super(imageNumber, imageOffset);
        this.vfSID = vfSID;
    }

    public InputVFSID(long vfSID) {
        super();
        this.vfSID = vfSID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.message.command.argument.InputArgument#getType()
     */
    @Override
    public InputType getType() {
        return InputType.InputTypeVFSID;
    }

    /**
     * @return the vfName
     */
    @Override
    public long getVfSid() {
        return vfSID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "InputVFSID [vfSID=" + StringUtils.longHexString(vfSID)
                + ", getImageId()=" + getImageId() + "]";
    }

}
