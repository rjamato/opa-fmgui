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
 *  File Name: PortMatcher.java
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

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * match port in the format [xx-xx]. Note we intentionally support more
 * powerful format here support future extended format on FM side
 */
public class PortMatcher {
    private final List<Point> ranges = new ArrayList<Point>();

    private final Set<Integer> candidates = new HashSet<Integer>();

    /**
     * Description:
     * 
     * @param pattern
     */
    public PortMatcher(String pattern) {
        pattern = pattern.trim();
        if (pattern.charAt(0) == '['
                && pattern.charAt(pattern.length() - 1) == ']') {
            pattern = pattern.substring(1, pattern.length() - 1);
        }
        String[] segs = pattern.split(",");
        for (String seg : segs) {
            String[] tmp = seg.split("-");
            if (tmp.length == 2) {
                int min = Integer.parseInt(tmp[0].trim());
                int max = Integer.parseInt(tmp[1].trim());
                if (max < min) {
                    throw new IllegalArgumentException("Invalid number range '"
                            + seg + "'");
                }
                ranges.add(new Point(min, max));
            } else if (tmp.length == 1) {
                int candidate = Integer.parseInt(seg.trim());
                candidates.add(candidate);
            } else {
                throw new IllegalArgumentException("Invalid format '" + seg
                        + "'");
            }
        }
    }

    public boolean match(int num) {
        if (candidates.contains(num)) {
            return true;
        }

        for (Point range : ranges) {
            if (num >= range.x && num <= range.y) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return the ranges
     */
    public List<Point> getRanges() {
        return ranges;
    }

    /**
     * @return the candidates
     */
    public Set<Integer> getCandidates() {
        return candidates;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PortMatcher [ranges=" + ranges + ", candidates=" + candidates
                + "]";
    }

}
