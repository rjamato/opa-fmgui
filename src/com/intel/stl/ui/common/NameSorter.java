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

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: NameSorter.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/02/05 20:21:23  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/05 20:13:06  jijunwan
 *  Archive Log:    change back
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/05 19:41:29  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/29 16:33:43  jijunwan
 *  Archive Log:    new implementation of mixed string sorter
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import java.util.Comparator;

public class NameSorter implements Comparator<String> {
    private final static NameSorter instance = new NameSorter();

    private NameSorter() {
    }

    public static NameSorter instance() {
        return instance;
    }

    @Override
    public int compare(String o1, String o2) {
        // null string
        if (o1 == null) {
            if (o2 != null) {
                return -1;
            } else {
                return 0;
            }
        } else if (o2 == null) {
            return 1;
        }
        // empty string
        if (o1.isEmpty()) {
            if (!o2.isEmpty()) {
                return -1;
            } else {
                return 0;
            }
        } else if (o2.isEmpty()) {
            return 1;
        }
        // strings not empty
        return new MixedString(o1).compareTo(new MixedString(o2));
    }

    private static class MixedString implements Comparable<MixedString> {
        private Long prefix;

        private final String mainString;

        private Long suffix;

        public MixedString(String str) {
            int prePos = 0;
            int len = str.length();
            int sufPos = len;
            while (prePos < len && Character.isDigit(str.charAt(prePos++))) {
                ;
            }
            prePos -= 1;
            if (prePos > 0) {
                prefix = Long.valueOf(str.substring(0, prePos));
            }
            if (prePos < len - 1) {
                while (Character.isDigit(str.charAt(--sufPos))) {
                    ;
                }
                sufPos += 1;
                if (sufPos < len) {
                    suffix = Long.valueOf(str.substring(sufPos, len));
                }
            }
            mainString =
                    prePos < sufPos - 1 ? str.substring(prePos, sufPos) : null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(MixedString o) {
            // special case pure number is "smaller" than pure string
            if (mainString == null) {
                if (o.mainString != null) {
                    return -1;
                }
            } else if (o.mainString == null) {
                return 1;
            }

            int res = compare(prefix, o.prefix);
            if (res != 0) {
                return res;
            }

            if (mainString != null) {
                res = mainString.compareTo(o.mainString);
                if (res != 0) {
                    return res;
                }
            }

            return compare(suffix, o.suffix);
        }

        /**
         * Description: The method Long#compareTo doesn't consider null
         * point situation, so we need to implement our own.
         * 
         * @param v1
         * @param v2
         * @return
         */
        private int compare(Long v1, Long v2) {
            if (v1 == null) {
                if (v2 != null) {
                    return -1;
                } else {
                    return 0;
                }
            } else {
                if (v2 == null) {
                    return 1;
                } else {
                    return v1.compareTo(v2);
                }
            }
        }
    }
}
