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
 *  File Name: EventRuleType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/12/10 20:32:40  rjtierne
 *  Archive Log:    Support for saving EventRules to UserSettings
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/11/26 18:56:39  fernande
 *  Archive Log:    Adding support to save EventRules in the UserOptions XML
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for SectionType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="SectionType">
 *   &lt;complexContent>
 *     &lt;sequence maxOccurs="unbounded">
 *       &lt;element name="Entry" type="{}EntryType"/>
 *     &lt;/sequence>
 *     &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventRuleType", propOrder = { "actions" })
@XmlRootElement(name = "EventRule")
public class EventRuleType {

    @XmlElementRef(type = ActionType.class)
    protected List<ActionType> actions;

    @XmlAttribute(required = true)
    protected RuleType type;

    @XmlAttribute(required = true)
    protected RuleSeverity severity;

    /**
     * Gets the value of the type property.
     * 
     * @return possible object is {@link RuleType }
     * 
     */
    public RuleType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *            allowed object is {@link RuleType }
     * 
     */
    public void setType(RuleType value) {
        this.type = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return possible object is {@link RuleType }
     * 
     */
    public RuleSeverity getSeverity() {
        return severity;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *            allowed object is {@link RuleType }
     * 
     */
    public void setSeverity(RuleSeverity value) {
        this.severity = value;
    }

    /**
     * Gets the value of the action property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the action property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getAction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ActionType }
     * 
     * 
     */
    public List<ActionType> getActions() {
        if (actions == null) {
            actions = new ArrayList<ActionType>();
        }
        return this.actions;
    }

    /**
     * @param actions
     *            the actions to set
     */
    public void setActions(List<ActionType> actions) {
        this.actions = actions;
    }
}
