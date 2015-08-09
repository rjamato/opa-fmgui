/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: DescMatcher.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/24 17:46:10  jijunwan
 *  Archive Log:    init version of DeviceGroup editor
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl.devicegroups;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * match node desc with the following format<br>
 * <code>*</code> Wildcard is 0 or more of any combination of: (A-Z)(a-z)(0-9)
 * "-" "," "=" "." "_"<br>
 * <code>?</code> Wildcard is 0 or 1 of any combination of: (A-Z)(a-z)(0-9) "-"
 * "," "=" "." "_" --><br>
 * <code>[##-##]</code> can be used to specify a range of numbers.
 * ie: node[1-4] matches nodes named node1, node2, node3, node4
 * 
 */
public class DescMatcher {
    private final static String validChars = "[A-Za-z0-9\\-=._]";

    private final Pattern pattern;

    public DescMatcher(String pattern) {
        if (pattern.indexOf(':') >= 0) {
            throw new IllegalArgumentException("Invalid format '" + pattern
                    + "'. Please exclude the port pattern string.");
        }
        pattern = rangePattern(pattern);
        pattern = pattern.replaceAll("\\*", validChars + "*");
        pattern = pattern.replaceAll("\\?", validChars + "?");
        this.pattern = Pattern.compile("^" + pattern + "$");
    }

    // assume only one [xx-xx] in the pattern
    protected String rangePattern(String pattern) {
        int pos1 = pattern.indexOf('[');
        if (pos1 < 0) {
            return pattern;
        }
        int pos2 = pattern.indexOf(']');
        if (pos2 < 0) {
            return pattern;
        }

        String rangeStr = pattern.substring(pos1 + 1, pos2);
        String[] segs = rangeStr.split("-");
        if (segs.length != 2) {
            throw new IllegalArgumentException("Invalid range format '"
                    + rangeStr + "'");
        }
        int min = Integer.parseInt(segs[0].trim());
        int max = Integer.parseInt(segs[1].trim());
        StringBuffer sb = new StringBuffer();
        for (int i = min; i <= max; i++) {
            if (sb.length() == 0) {
                sb.append("(" + i);
            } else {
                sb.append("|" + i);
            }
        }
        sb.append(")");
        return pattern.substring(0, pos1) + sb.toString()
                + pattern.substring(pos2 + 1, pattern.length());
    }

    public boolean match(String str) {
        Matcher m = pattern.matcher(str);
        return m.matches();
    }
}
