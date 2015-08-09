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
import com.intel.stl.api.subnet.Selection;

/**
 * @author jijunwan
 * 
 */
public class InputVFSIDFocus extends InputVFSID {
    private final int select;

    private int start = 0;

    private int range = 10;

    public InputVFSIDFocus(long vfSID, Selection selection) {
        super(vfSID);
        select = selection.getSelect();
    }

    public InputVFSIDFocus(long vfSID, long imageNumber, int imageOffset,
            Selection selection, int range) {
        super(vfSID, imageNumber, imageOffset);
        this.select = selection.getSelect();
        this.range = range;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.message.command.argument.InputGroupName#getType()
     */
    @Override
    public InputType getType() {
        return InputType.InputTypeVFSIDFocus;
    }

    /**
     * @return the start
     */
    @Override
    public int getStart() {
        return start;
    }

    /**
     * @param start
     *            the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the range
     */
    @Override
    public int getRange() {
        return range;
    }

    /**
     * @param range
     *            the range to set
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * @return the select
     */
    @Override
    public int getSelect() {
        return select;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "InputVFSIDFocus [select=" + select + ", start=" + start
                + ", range=" + range + ", getVfSid()="
                + StringUtils.longHexString(getVfSid()) + ", getImageId()="
                + getImageId() + "]";
    }

}
