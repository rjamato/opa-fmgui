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
 *  File Name: UserSettings.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/02/20 21:10:41  rjtierne
 *  Archive Log:    Fixed typo: Changed SECTION_PREFERENE to SECTION_PREFERENCE
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/10 23:04:57  jijunwan
 *  Archive Log:    changed name from "performance" to "preference"
 *  Archive Log:    added exception for unknown names
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/15 19:07:14  rjtierne
 *  Archive Log:    Added new performance section and associated properties:
 *  Archive Log:    refresh rate, refresh rate units, timing window, and # worst nodes
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/10 20:32:42  rjtierne
 *  Archive Log:    Support for saving EventRules to UserSettings
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/11 18:02:06  fernande
 *  Archive Log:    Support for generic preferences: a new node (Preferences) in the UserOptions XML now allows to define groups of preferences (Section) and key/value pairs (Entry) that are stored in Properties objects are runtime.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/29 18:53:56  fernande
 *  Archive Log:    Changing UserSettings to support Properties Display options
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/19 20:00:50  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class UserSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    // Define here any sections you define in Preferences
    public static final String SECTION_USERSTATE = "UserState";

    public static final String SECTION_PREFERENCE = "Preference";

    // Define here any preference name defined in a section
    public static final String PROPERTY_LASTSUBNETACCESSED =
            "LastSubnetAccessed";

    public static final String PROPERTY_REFRESH_RATE = "RefreshRate";

    public static final String PROPERTY_REFRESH_RATE_UNITS = "RefreshRateUnits";

    public static final String PROPERTY_TIMING_WINDOW = "TimingWindow";

    public static final String PROPERTY_NUM_WORST_NODES = "NumWorstNodes";

    private String userName;

    private String userDescription;

    private Map<String, Properties> preferences;

    private List<EventRule> eventRules;

    private Map<ResourceType, List<PropertyGroup>> propertiesDisplayOptions;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public Map<String, Properties> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, Properties> preferences) {
        this.preferences = preferences;
    }

    public Properties getUserState() {
        return getProperties(SECTION_USERSTATE);
    }

    public Properties getUserPreference() {
        return getProperties(SECTION_PREFERENCE);
    }

    protected Properties getProperties(String name) {
        Properties res = preferences.get(name);
        if (res != null) {
            return res;
        } else {
            throw new RuntimeException("Couldn't find Properties for '" + name
                    + "'");
        }
    }

    public List<EventRule> getEventRules() {
        return eventRules;
    }

    public void setEventRules(List<EventRule> eventRules) {
        this.eventRules = eventRules;
    }

    public Map<ResourceType, List<PropertyGroup>> getPropertiesDisplayOptions() {
        return propertiesDisplayOptions;
    }

    public void setPropertiesDisplayOptions(
            Map<ResourceType, List<PropertyGroup>> propertiesDisplayOptions) {
        this.propertiesDisplayOptions = propertiesDisplayOptions;
    }

}
