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
 *  File Name: PreferencesAdapter.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/11/11 18:03:01  fernande
 *  Archive Log:    Support for generic preferences: a new node (Preferences) in the UserOptions XML now allows to define groups of preferences (Section) and key/value pairs (Entry) that are stored in Properties objects are runtime.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PreferencesAdapter extends
        XmlAdapter<Preferences, Map<String, Properties>> {

    @Override
    public Map<String, Properties> unmarshal(Preferences preferences)
            throws Exception {
        List<SectionType> sections = preferences.getSectionTypes();
        Map<String, Properties> prefsMap =
                new HashMap<String, Properties>(sections.size());
        for (SectionType section : sections) {
            String sectionName = section.getName();
            Properties prefProps = new Properties();
            for (EntryType entry : section.getEntry()) {
                prefProps.put(entry.getName(), entry.getValue());
            }
            prefsMap.put(sectionName, prefProps);
        }
        return prefsMap;
    }

    @Override
    public Preferences marshal(Map<String, Properties> prefsMap)
            throws Exception {
        Preferences preferences = new Preferences();
        if (prefsMap != null) {
            Set<String> keys = prefsMap.keySet();
            List<SectionType> sections = preferences.getSectionTypes();
            for (String sectionName : keys) {
                SectionType sectionType = new SectionType();
                sectionType.setName(sectionName);
                List<EntryType> entryList = sectionType.getEntry();
                Properties prefProps = prefsMap.get(sectionName);
                Set<Object> propNames = prefProps.keySet();
                for (Object propName : propNames) {
                    Object propValue = prefProps.get(propName);
                    EntryType entry = new EntryType();
                    entry.setName(propName.toString());
                    entry.setValue(propValue.toString());
                    entryList.add(entry);
                }
                sections.add(sectionType);
            }
        }
        return preferences;
    }

}
