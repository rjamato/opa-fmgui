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
 *  File Name: PreferencesBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/10 23:18:06  jijunwan
 *  Archive Log:    changed to store refreshRate rather than refreshRateinSeconds, store TimeUnit by name rather than ordinary
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/20 19:12:32  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Java bean class to hold the data entered in the users 
 *  preferences wizard
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.impl.preferences;

public class PreferencesBean {

    private String refreshRate;

    private String refreshRateUnits;

    private String timingWindow;

    private String numWorstNodes;

    public String getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(String refreshRate) {
        this.refreshRate = refreshRate;
    }

    public String getRefreshRateUnits() {
        return refreshRateUnits;
    }

    public void setRefreshRateUnits(String refreshRateUnits) {
        this.refreshRateUnits = refreshRateUnits;
    }

    public String getTimingWindow() {
        return timingWindow;
    }

    public void setTimingWindowInSeconds(String timingWindow) {
        this.timingWindow = timingWindow;
    }

    public String getNumWorstNodes() {
        return numWorstNodes;
    }

    public void setNumWorstNodes(String numWorstNodes) {
        this.numWorstNodes = numWorstNodes;
    }

}
