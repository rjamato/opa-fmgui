/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 * 
 *  Functional Group: Fabric Viewer Application
 * 
 *  File Name: InputVFNameFocus.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.6  2015/08/17 18:49:05  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - change backend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.5  2015/06/10 19:36:36  jijunwan
 *  Archive Log: PR 129153 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
 *  Archive Log:
 * 
 *  Overview:
 * 
 *  @author: jijunwan
 * 
 ******************************************************************************/
package com.intel.stl.fecdriver.messages.command;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.Selection;

/**
 * @author jijunwan
 * 
 */
public class InputVFNameFocus extends InputVFName {
    private final int select;

    private int start = 0;

    private int range = 10;

    public InputVFNameFocus(String vfName, Selection selection) {
        super(vfName);
        select = selection.getSelect();
    }

    public InputVFNameFocus(String vfName, long imageNumber, int imageOffset,
            Selection selection, int range) {
        super(vfName, imageNumber, imageOffset);
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
        return InputType.InputTypeVFNameFocus;
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
        return "InputVFNameFocus [select=" + StringUtils.intHexString(select)
                + ", start=" + start + ", range=" + range + ", getVfName()="
                + getVfName() + ", getImageId()=" + getImageId() + "]";
    }

}
