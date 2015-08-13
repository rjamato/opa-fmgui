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
 *  File Name: UserPreferenceProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3.2.1  2015/08/12 15:26:59  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
