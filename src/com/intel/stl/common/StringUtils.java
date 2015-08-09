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
