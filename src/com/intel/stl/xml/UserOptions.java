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
 *  File Name: UserOptions.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2014/12/10 20:32:40  rjtierne
 *  Archive Log:    Support for saving EventRules to UserSettings
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/26 18:57:25  fernande
 *  Archive Log:    Adding support to save EventRules in the UserOptions XML
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/11/11 18:03:25  fernande
 *  Archive Log:    Support for generic preferences: a new node (Preferences) in the UserOptions XML now allows to define groups of preferences (Section) and key/value pairs (Entry) that are stored in Properties objects are runtime.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/29 18:51:55  fernande
 *  Archive Log:    Adding UserOptions XML and  saving it to the database. Includes XML schema validation.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.xml;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.intel.stl.api.configuration.EventRule;
import com.intel.stl.api.configuration.PropertyGroup;
import com.intel.stl.api.configuration.ResourceType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}Preferences"/>
 *         &lt;element ref="{}EventRules"/>
 *         &lt;element ref="{}PropertiesDisplay"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "preferences", "eventRules",
        "propertiesDisplay" })
@XmlRootElement(name = "UserOptions")
public class UserOptions {

    @XmlElement(name = "Preferences", required = true)
    @XmlJavaTypeAdapter(PreferencesAdapter.class)
    protected Map<String, Properties> preferences;

    @XmlElement(name = "EventRules", required = true)
    @XmlJavaTypeAdapter(EventRulesAdapter.class)
    protected List<EventRule> eventRules;

    @XmlElement(name = "PropertiesDisplay", required = true)
    @XmlJavaTypeAdapter(PropertiesDisplayAdapter.class)
    protected Map<ResourceType, List<PropertyGroup>> propertiesDisplay;

    /**
     * Gets the value of the lastSubnetAccessed property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public Map<String, Properties> getPreferences() {
        return preferences;
    }

    /**
     * Sets the value of the lastSubnetAccessed property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPreferences(Map<String, Properties> value) {
        this.preferences = value;
    }

    /**
     * @return the eventRules
     */
    public List<EventRule> getEventRules() {
        return eventRules;
    }

    /**
     * @param eventRules
     *            the eventRules to set
     */
    public void setEventRules(List<EventRule> eventRules) {
        this.eventRules = eventRules;
    }

    /**
     * Gets the value of the propertiesDisplay property.
     * 
     * @return possible object is {@link PropertiesDisplay }
     * 
     */
    public Map<ResourceType, List<PropertyGroup>> getPropertiesDisplay() {
        return propertiesDisplay;
    }

    /**
     * Sets the value of the propertiesDisplay property.
     * 
     * @param value
     *            allowed object is {@link PropertiesDisplay }
     * 
     */
    public void setPropertiesDisplay(
            Map<ResourceType, List<PropertyGroup>> value) {
        this.propertiesDisplay = value;
    }
}
