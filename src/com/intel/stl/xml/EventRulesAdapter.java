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
 *  File Name: EventRulesAdapter.java
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

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.intel.stl.api.configuration.EventRule;
import com.intel.stl.api.configuration.EventRuleAction;
import com.intel.stl.api.configuration.EventType;
import com.intel.stl.api.notice.NoticeSeverity;

public class EventRulesAdapter extends XmlAdapter<EventRules, List<EventRule>> {

    @Override
    public List<EventRule> unmarshal(EventRules rules) throws Exception {

        // New list of event rules to return
        List<EventRule> eventRulesList = new ArrayList<EventRule>();

        // Traverse JaxB event rules and convert to Java event rules
        for (EventRuleType rule : rules.getEventRuleTypes()) {

            // Create a list of event rule actions
            List<EventRuleAction> eventActions =
                    new ArrayList<EventRuleAction>();
            for (ActionType action : rule.getActions()) {
                eventActions.add(EventRuleAction.getEventAction(action
                        .getName().name()));
            }

            // Create a list of event rules
            eventRulesList.add(new EventRule(EventType.getEventType(rule
                    .getType().name()), NoticeSeverity.getNoticeSeverity(rule
                    .getSeverity().name()), eventActions));
        }

        return eventRulesList;
    }

    @Override
    public EventRules marshal(List<EventRule> eventRulesList) throws Exception {

        // Get the list from the event rules so it can be populated
        EventRules eventRules = new EventRules();
        List<EventRuleType> eventTypeList = eventRules.getEventRuleTypes();

        // Traverse the event rules list provided, extract the values, and
        // put them in the event type list
        if (eventRulesList != null) {
            for (EventRule rule : eventRulesList) {
                EventRuleType eventType = new EventRuleType();
                eventType.setType(RuleType
                        .fromValue(rule.getEventType().name()));
                eventType.setSeverity(RuleSeverity.fromValue(rule
                        .getEventSeverity().name()));

                // Convert the list of actions to ActionType
                List<ActionType> actionTypes = new ArrayList<ActionType>();
                for (EventRuleAction action : rule.getEventActions()) {
                    ActionType actionType = new ActionType();
                    actionType.setName(ActionName.fromValue(action.name()));
                    actionTypes.add(actionType);
                }
                eventType.setActions(actionTypes);

                // Add the event type to the event type list
                eventTypeList.add(eventType);
            } // for
        } // if

        return eventRules;
    }
}
