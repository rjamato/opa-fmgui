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
package com.intel.stl.api;


/**
 * @author jijunwan
 * 
 */
public class StringUtils {

    public static String byteHexString(byte value) {
        return String.format("0x%02x", Byte.valueOf(value));
    }

    public static String shortHexString(short value) {
        return String.format("0x%04x", Short.valueOf(value));
    }

    public static String intHexString(int value) {
        return String.format("0x%08x", Integer.valueOf(value));
    }

    public static String longHexString(long value) {
        return String.format("0x%016x", Long.valueOf(value));
    }

    public static String getErrorMessage(Throwable error) {
        if (error == null) {
            return null;
        }

        while (error.getCause() != null) {
            error = error.getCause();
        }
        String msg = error.getLocalizedMessage();
        if (msg == null || msg.isEmpty()) {
            msg = error.getClass().getSimpleName();
        }
        return msg;
    }

    public static String getIpV4Addr(byte[] ipBytes) {
        if (ipBytes == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ipBytes.length; i++) {
            int value = ipBytes[i] & 0xff;
            if (i == 0) {
                sb.append(value);
            } else {
                sb.append("." + value);
            }
        }
        return sb.toString();
    }

    // See IETF RFC 5952 for the canonical format used here to render an IPv6
    // address
    public static String getIpV6Addr(byte[] ipBytes) {
        if (ipBytes == null) {
            return null;
        }
        // Find max sequence of 16-bit zeros
        int compressSize = 0;
        int compressStart = -1;
        for (int i = 0; i < ipBytes.length; i = i + 2) {
            int numZeroGroups = 0;
            int j = i;
            while (j < ipBytes.length && ipBytes[j] == 0 && ipBytes[j + 1] == 0) {
                j = j + 2;
                numZeroGroups++;
            }
            if (numZeroGroups > compressSize) {
                compressSize = numZeroGroups;
                compressStart = i;
            }
        }
        StringBuffer buff = new StringBuffer(40);
        if (compressSize < 2) {
            bytesToHex(buff, ipBytes, 0, ipBytes.length);
        } else {
            bytesToHex(buff, ipBytes, 0, compressStart);
            buff.append("::");
            bytesToHex(buff, ipBytes, (compressStart + (compressSize * 2)),
                    ipBytes.length);
        }
        return buff.toString();
    }

    private static void bytesToHex(StringBuffer buff, byte[] src, int start,
            int end) {
        String groupSeparator = "";
        for (int i = start; i < end; i = i + 2) {
            buff.append(groupSeparator);
            int group = ((src[i] << 8) & 0xff00) | ((src[i + 1]) & 0xff);
            buff.append(Integer.toHexString(group));
            groupSeparator = ":";
        }
    }
}
