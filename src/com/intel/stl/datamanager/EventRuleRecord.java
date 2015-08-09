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
 *  File Name: EventRuleRecord.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/02/06 15:03:04  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/30 15:37:20  fernande
 *  Archive Log:    Changed hashCode methods to use generated code by Eclipse
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/12 19:59:06  fernande
 *  Archive Log:    Fixed bug in equals implementation
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/20 16:56:12  fernande
 *  Archive Log:    Added basic Entity Manager management to minimize creation of DAOs
 *  Archive Log:    Fixed bugs in database management
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/11 22:08:02  fernande
 *  Archive Log:    Changes to add more entities to database schema
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.datamanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.intel.stl.api.configuration.EventRule;
import com.intel.stl.api.configuration.EventRuleAction;
import com.intel.stl.api.subnet.SubnetDescription;

@Entity
@Table(name = "EVENT_RULES")
public class EventRuleRecord extends DatabaseRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 32)
    private String eventName;

    private EventRule eventRule;

    @ManyToMany
    @JoinTable(name = "EVENT_RULES_SUBNETS", joinColumns = { @JoinColumn(
            name = "eventName", referencedColumnName = "eventName") },
            inverseJoinColumns = { @JoinColumn(name = "subnetId",
                    referencedColumnName = "subnetId") })
    private Set<SubnetRecord> eventSubnets;

    @ManyToMany
    @JoinTable(name = "EVENT_RULES_ACTIONS", joinColumns = { @JoinColumn(
            name = "eventName", referencedColumnName = "eventName") },
            inverseJoinColumns = { @JoinColumn(name = "eventAction",
                    referencedColumnName = "id") })
    private Set<EventActionRecord> eventActions;

    public EventRuleRecord() {
    }

    public EventRuleRecord(EventRule eventRule) {
        this.eventRule = eventRule;
        this.eventName = eventRule.getEventName();
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public EventRule getEventRule() {
        eventRule.setEventName(eventName);
        Set<SubnetRecord> subnets = getEventSubnets();
        Set<SubnetDescription> eventSubnets = new HashSet<SubnetDescription>();
        if (subnets != null) {
            for (SubnetRecord subnet : subnets) {
                eventSubnets.add(subnet.getSubnetDescription());
            }
        }
        eventRule.setEventSubnets(eventSubnets);
        Set<EventActionRecord> actions = getEventActions();
        List<EventRuleAction> eventActions = new ArrayList<EventRuleAction>();
        if (actions != null) {
            for (EventActionRecord action : actions) {
                eventActions.add(action.getAction());
            }
        }
        eventRule.setEventActions(eventActions);
        return eventRule;
    }

    public void setEventRule(EventRule eventRule) {
        this.eventRule = eventRule;
    }

    public Set<SubnetRecord> getEventSubnets() {
        return eventSubnets;
    }

    public void setEventSubnets(Set<SubnetRecord> eventSubnets) {
        this.eventSubnets = eventSubnets;
    }

    public Set<EventActionRecord> getEventActions() {
        return eventActions;
    }

    public void setEventActions(Set<EventActionRecord> eventActions) {
        this.eventActions = eventActions;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime
                        * result
                        + ((eventName == null) ? 0 : eventName.toLowerCase()
                                .hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EventRuleRecord other = (EventRuleRecord) obj;
        if (eventName == null) {
            if (other.eventName != null)
                return false;
        } else if (!eventName.equalsIgnoreCase(other.eventName))
            return false;
        return true;
    }
}
