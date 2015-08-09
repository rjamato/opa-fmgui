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
 *  File Name: HistoryType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
