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
 *  File Name: PreferencesModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/08/17 18:54:50  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/08/12 19:28:12  fisherma
 *  Archive Log:    Store/retrieve SMTP settings in/from SECTION_PREFERENCE properties.  Cleanup unused interface.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/08/11 20:35:56  jijunwan
 *  Archive Log:    PR 129935 - Need proper default value for user preference
 *  Archive Log:    - set default time window to 60 sec
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/08/10 17:55:47  robertja
 *  Archive Log:    PR 128974 - Email notification functionality.
 *  Archive Log:
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

import static com.intel.stl.api.configuration.UserSettings.PROPERTY_MAIL_RECIPIENTS;
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
        preferences.put(PROPERTY_TIMING_WINDOW, "60");
        preferences.put(PROPERTY_NUM_WORST_NODES, "10");
        preferences.put(PROPERTY_MAIL_RECIPIENTS, "");
        preferencesMap.put(SECTION_PREFERENCE, preferences);
    }

    public String getMailRecipients() {
        return preferencesMap.get(SECTION_PREFERENCE).getProperty(
                PROPERTY_MAIL_RECIPIENTS);
    }

    public void setMailRecipients(String recipients) {
        preferencesMap.get(SECTION_PREFERENCE).setProperty(
                PROPERTY_MAIL_RECIPIENTS, recipients);
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

    public Properties getPreferences() {
        return preferencesMap.get(SECTION_PREFERENCE);
    }
}
