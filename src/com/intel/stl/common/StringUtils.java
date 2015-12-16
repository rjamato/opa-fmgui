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
 *  File Name: StringUtils.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.6  2015/08/17 18:49:06  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - change backend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.5  2015/06/10 19:36:45  jijunwan
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
package com.intel.stl.common;

import java.nio.ByteBuffer;

import com.intel.stl.api.performance.PAConstants;

/**
 * @author jijunwan
 * 
 */
public class StringUtils {
    public static String toString(byte[] raw, int offset, int maxLen) {
        if (raw == null) {
            throw new IllegalArgumentException("Byte array is null");
        }
        if (offset >= raw.length) {
            throw new IllegalArgumentException("Invalid offset (" + offset
                    + ">=" + raw.length + ")");
        }
        int pos = offset;
        int end = offset + Math.min(maxLen, raw.length - offset);
        while (pos < end && raw[pos] != 0) {
            pos += 1;
        }
        return pos > offset ? new String(raw, offset, pos - offset) : "";
    }

    public static void setString(String str, ByteBuffer buffer, int startPos,
            int maxLen) throws IllegalArgumentException {
        if (str == null || buffer == null) {
            return;
        }

        if (str.length() >= maxLen) {
            throw new IllegalArgumentException("Invalid string length "
                    + str.length() + " > " + PAConstants.STL_PM_GROUPNAMELEN
                    + ".");
        }
        buffer.position(startPos);
        buffer.put(str.getBytes());
    }
}
