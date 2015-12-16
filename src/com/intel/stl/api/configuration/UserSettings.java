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
 *  File Name: UserSettings.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.12  2015/09/25 20:46:48  fernande
 *  Archive Log:    PR129920 - revisit health score calculation. Changed formula to include several factors (or attributes) within the calculation as well as user-defined weights (for now are hard coded).
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/08/17 18:48:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/08/12 19:22:13  fisherma
 *  Archive Log:    Store/retrieve SMTP settings in/from SECTION_PREFERENCE properties.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/08/10 17:04:40  robertja
 *  Archive Log:    PR128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/06/25 21:02:09  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - added a section name for pin information persistence
 *  Archive Log:
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

    public static final String SECTION_PIN_BOARD = "PinBoard";

    // Define here any preference name defined in a section
    public static final String PROPERTY_LASTSUBNETACCESSED =
            "LastSubnetAccessed";

    public static final String PROPERTY_REFRESH_RATE = "RefreshRate";

    public static final String PROPERTY_REFRESH_RATE_UNITS = "RefreshRateUnits";

    public static final String PROPERTY_TIMING_WINDOW = "TimingWindow";

    public static final String PROPERTY_NUM_WORST_NODES = "NumWorstNodes";

    public static final String PROPERTY_MAIL_RECIPIENTS = "mailRecipients";

    public static final String PROPERTY_WEIGHT_NUMSWITCHES =
            "WeightNumSwitches";

    public static final String PROPERTY_WEIGHT_NUMHFIS = "WeightNumHfis";

    public static final String PROPERTY_WEIGHT_NUMISLS = "WeightNumIsls";

    public static final String PROPERTY_WEIGHT_NUMHFILINKS =
            "WeightNumHfiLinks";

    public static final String PROPERTY_WEIGHT_NUMPORTS = "WeightNumPorts";

    public static final String PROPERTY_WEIGHT_NUMNONDEGISLS =
            "WeightNumNonDegIsls";

    public static final String PROPERTY_WEIGHT_NUMNONDEGHFILINKS =
            "WeightNumNonDegHfiLinks";

    private String userName;

    private String userDescription;

    private Map<String, Properties> preferences;

    private List<EventRule> eventRules;

    private Map<ResourceType, List<PropertyGroup>> propertiesDisplayOptions;

    public UserSettings() {

    }

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
