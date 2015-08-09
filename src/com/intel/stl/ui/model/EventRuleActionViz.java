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
 *  File Name: EventRuleActionViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/05/07 20:03:57  fernande
 *  Archive Log:    Changes to save Subnets and EventRules to the database
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import static com.intel.stl.ui.common.STLConstants.K0682_DISPLAY_MESSAGE;
import static com.intel.stl.ui.common.STLConstants.K0683_EMAIL_MESSAGE;

import com.intel.stl.api.configuration.EventRuleAction;

public enum EventRuleActionViz {

    SEND_EMAIL(EventRuleAction.SEND_EMAIL, K0683_EMAIL_MESSAGE.getValue()),
    DISPLAY_MESSAGE(EventRuleAction.DISPLAY_MESSAGE, K0682_DISPLAY_MESSAGE
            .getValue());

    public final static String[] names = new String[EventRuleActionViz.values().length];
    static {
        for (int i = 0; i < names.length; i++) {
            names[i] = EventRuleActionViz.values()[i].name;
        }
    };

    private final EventRuleAction action;

    private final String name;

    private EventRuleActionViz(EventRuleAction action, String name) {
        this.action = action;
        this.name = name;
    }

    public EventRuleAction getEventRuleAction() {
        return action;
    }

    public String getName() {
        return name;
    }

    public static EventRuleActionViz getEventRuleActionVizFor(
            EventRuleAction action) {
        EventRuleActionViz[] values = EventRuleActionViz.values();
        for (int i = 0; i < values.length; i++) {
            if (action == values[i].getEventRuleAction()) {
                return values[i];
            }
        }
        return null;
    }

    public static EventRuleActionViz getEventRuleActionVizFor(String name) {
        EventRuleActionViz[] values = EventRuleActionViz.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].name.equals(name)) {
                return values[i];
            }
        }
        return null;
    }
}
