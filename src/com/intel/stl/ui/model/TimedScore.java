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
 *  File Name: TimedScore.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/10/02 21:26:13  jijunwan
 *  Archive Log:    fixed issued found by FindBugs
 *  Archive Log:    Some auto-reformate
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/09 21:18:56  jijunwan
 *  Archive Log:    improved status visualization
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/08 19:03:23  jijunwan
 *  Archive Log:    backend support for states based on notices
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.awt.Color;
import java.io.Serializable;

import com.intel.stl.ui.common.UIConstants;

public class TimedScore implements Serializable {
    private static final long serialVersionUID = -6309931522503519445L;

    private long time;

    private double score;

    /**
     * Description:
     * 
     */
    public TimedScore() {
        super();
    }

    /**
     * Description:
     * 
     * @param time
     * @param score
     */
    public TimedScore(long time, double score) {
        super();
        this.time = time;
        this.score = score;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    public String getScoreString() {
        return Integer.toString((int) score);
    }

    public Color getColor() {
        double score = getScore();
        if (score < 25) {
            return UIConstants.INTEL_DARK_RED;
        } else if (score < 50) {
            return UIConstants.INTEL_DARK_ORANGE;
        } else if (score < 75) {
            return UIConstants.INTEL_YELLOW;
        } else {
            return UIConstants.INTEL_DARK_GREEN;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TimedScore [time=" + time + ", score=" + score + "]";
    }

}
