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
 *  File Name: UserPreferenceProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/04/06 11:14:16  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. Open issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/04/02 17:04:00  jypak
 *  Archive Log:    Null checks added.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/30 18:34:41  jypak
 *  Archive Log:    Introduce a UserSettingsProcessor to handle different use cases for user settings via Setup Wizard.
 *  Archive Log:
 *
 *  Overview: Depending on what's how changed, we want to calculate events differently. 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.publisher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.intel.stl.api.configuration.EventRule;
import com.intel.stl.api.configuration.EventType;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.ui.model.UserPreference;

public class UserSettingsProcessor {

    private UserPreference userPreference;

    private final EventCalculator evtCal;

    private final Map<EventType, NoticeSeverity> eventSeverityMap =
            new HashMap<EventType, NoticeSeverity>();

    public UserSettingsProcessor(UserSettings userSettings,
            EventCalculator evtCal) {
        this.evtCal = evtCal;
        List<EventRule> eventRules = null;
        if (userSettings != null) {
            this.userPreference =
                    new UserPreference(userSettings.getUserPreference());
            eventRules = userSettings.getEventRules();
        }
        if (eventRules != null) {
            for (EventRule er : eventRules) {
                eventSeverityMap.put(er.getEventType(), er.getEventSeverity());
            }
        }
    }

    public void process(UserSettings newSettings) {
        List<EventRule> newList = null;
        Properties preference = null;
        if (newSettings != null) {
            newList = newSettings.getEventRules();
            preference = newSettings.getUserPreference();
        }
        boolean severityChanged = false;
        if (newList != null) {
            for (EventRule er : newList) {
                EventType eventType = er.getEventType();
                NoticeSeverity oldSeverity = eventSeverityMap.get(eventType);
                NoticeSeverity newSeverity = er.getEventSeverity();
                if (oldSeverity != newSeverity) {
                    eventSeverityMap.put(eventType, newSeverity);
                    severityChanged = true;
                }
            }
        }

        if (severityChanged) {
            evtCal.clear();
            return;
        }

        UserPreference newPreference = new UserPreference(preference);
        int oldTimeWindow = userPreference.getTimeWindowInSeconds();
        int newTimeWindow = newPreference.getTimeWindowInSeconds();

        boolean userPrefChanged = false;
        // Set time window only if it's not same as old one.
        if (oldTimeWindow < newTimeWindow) {
            evtCal.setTimeWindowInSeconds(newTimeWindow);
            userPrefChanged = true;
        } else if (oldTimeWindow > newTimeWindow) {
            evtCal.setTimeWindowInSeconds(newTimeWindow);
            evtCal.sweep();
            evtCal.updateListeners();
            userPrefChanged = true;
        }

        int newWorstNodes = newPreference.getNumWorstNodes();
        if (userPreference.getNumWorstNodes() != newWorstNodes) {
            evtCal.setNumWorstNodes(newPreference.getNumWorstNodes());
            evtCal.updateListeners();
            userPrefChanged = true;
        }

        if (userPrefChanged) {
            userPreference = newPreference;
        }

    }
}
