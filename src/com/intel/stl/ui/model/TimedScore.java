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
 *  File Name: TimedScore.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/09/25 20:53:37  fernande
 *  Archive Log:    PR129920 - revisit health score calculation. Changed formula to include several factors (or attributes) within the calculation as well as user-defined weights (for now are hard coded).
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/08/17 18:53:46  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
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

    private String tip;

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
     * Description:
     * 
     * @param time
     * @param score
     * @param tip
     */
    public TimedScore(long time, double score, String tip) {
        super();
        this.time = time;
        this.score = score;
        this.tip = tip;
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

    /**
     * @return the tip
     */
    public String getTip() {
        return tip;
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
        return "TimedScore [time=" + time + ", score=" + score + ", tip=" + tip
                + "]";
    }

}
