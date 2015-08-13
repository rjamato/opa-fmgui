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
 *  File Name: HistoryType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4.2.1  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/03/12 19:29:04  jypak
 *  Archive Log:    When JComboBox.setRenderer is invoked, if an enum is the combo box item, the default labels displayed for the combo box list are the returned results of the default toString method of the enum which are the enum types. The toString is overridden to display consistent name for HistoryType.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/12 19:40:06  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/04 12:14:48  jypak
 *  Archive Log:    Incorrect comment header. Archive history rows were added.
 *  Archive Log:
 *  
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import com.intel.stl.ui.common.STLConstants;

public enum HistoryType {
    CURRENT(0, STLConstants.K1114_CURRENT.getValue()),
    ONE(1, Integer.toString(1) + STLConstants.K1112_HOURS.getValue()),
    TWO(2, Integer.toString(2) + STLConstants.K1112_HOURS.getValue()),
    SIX(6, Integer.toString(6) + STLConstants.K1112_HOURS.getValue()),
    DAY(24, Integer.toString(24) + STLConstants.K1112_HOURS.getValue());

    private final String name;

    // length of history in hours
    private final int value;

    /**
     * 
     * Description:
     * 
     * @param value
     * @param name
     */
    private HistoryType(int value, String name) {
        this.name = name;
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int getLengthInSeconds() {
        return value * 3600;
    }

    public int getMaxDataPoints(int refreshRate) {
        // Calculate maxDataPoints based on history type, refresh rate
        int maxDataPoints = getLengthInSeconds() / refreshRate;
        return maxDataPoints;
    }

    @Override
    public String toString() {
        return name;
    }
}
