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
 *  File Name: PreferencesModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/02/23 15:06:54  rjtierne
 *  Archive Log:    Replaced hard coded string constants with STLConstants
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/20 21:15:16  rjtierne
 *  Archive Log:    Multinet Wizard: Data models for all wizards for data storage and display
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/13 21:29:16  rjtierne
 *  Archive Log:    Multinet Wizard: Initial Version
 *  Archive Log:
 *
 *  Overview: Model to store the preferences wizard settings
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.model.preferences;

import static com.intel.stl.api.configuration.UserSettings.PROPERTY_NUM_WORST_NODES;
import static com.intel.stl.api.configuration.UserSettings.PROPERTY_REFRESH_RATE;
import static com.intel.stl.api.configuration.UserSettings.PROPERTY_REFRESH_RATE_UNITS;
import static com.intel.stl.api.configuration.UserSettings.PROPERTY_TIMING_WINDOW;
import static com.intel.stl.api.configuration.UserSettings.SECTION_PREFERENCE;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.intel.stl.ui.common.STLConstants;

public class PreferencesModel {

    private Map<String, Properties> preferencesMap =
            new HashMap<String, Properties>();

    public PreferencesModel() {
        initialize();
    }

    public void initialize() {
        Properties preferences = new Properties();
        preferences.put(PROPERTY_REFRESH_RATE, "10");
        preferences.put(PROPERTY_REFRESH_RATE_UNITS,
                STLConstants.K0012_SECONDS.getValue());
        preferences.put(PROPERTY_TIMING_WINDOW, "10");
        preferences.put(PROPERTY_NUM_WORST_NODES, "10");
        preferencesMap.put(SECTION_PREFERENCE, preferences);
    }

    public String getRefreshRateInSeconds() {
        return preferencesMap.get(SECTION_PREFERENCE).getProperty(
                PROPERTY_REFRESH_RATE);
    }

    public void setRefreshRateInSeconds(String refreshRateInSeconds) {
        preferencesMap.get(SECTION_PREFERENCE).setProperty(
                PROPERTY_REFRESH_RATE, refreshRateInSeconds);
    }

    public String getRefreshRateUnits() {

        return preferencesMap.get(SECTION_PREFERENCE).getProperty(
                PROPERTY_REFRESH_RATE_UNITS);
    }

    public void setRefreshRateUnits(String refreshRateUnits) {

        preferencesMap.get(SECTION_PREFERENCE).setProperty(
                PROPERTY_REFRESH_RATE_UNITS, refreshRateUnits);
    }

    public String getTimingWindowInSeconds() {
        return preferencesMap.get(SECTION_PREFERENCE).getProperty(
                PROPERTY_TIMING_WINDOW);
    }

    public void setTimingWindowInSeconds(String timingWindow) {
        preferencesMap.get(SECTION_PREFERENCE).setProperty(
                PROPERTY_TIMING_WINDOW, timingWindow);
    }

    public String getNumWorstNodes() {
        return preferencesMap.get(SECTION_PREFERENCE).getProperty(
                PROPERTY_NUM_WORST_NODES);
    }

    public void setNumWorstNodes(String numWorstNodes) {
        preferencesMap.get(SECTION_PREFERENCE).setProperty(
                PROPERTY_NUM_WORST_NODES, numWorstNodes);
    }

    public void clear() {
        if (preferencesMap != null) {
            initialize();
        }
    }

    public Map<String, Properties> getPreferencesMap() {
        return preferencesMap;
    }

    public void setPreferencesMap(Map<String, Properties> preferencesMap) {
        this.preferencesMap = preferencesMap;
    }
}
