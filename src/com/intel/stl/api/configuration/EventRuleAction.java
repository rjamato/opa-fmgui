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
 *  File Name: EventRuleAction.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/10 20:32:42  rjtierne
 *  Archive Log:    Support for saving EventRules to UserSettings
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/30 16:05:07  fernande
 *  Archive Log:    Added to allow one to many from EventRule
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import java.util.HashMap;
import java.util.Map;

public enum EventRuleAction {

    SEND_EMAIL,
    DISPLAY_MESSAGE;

    private final static Map<String, EventRuleAction> eventActionMap =
            new HashMap<String, EventRuleAction>();
    static {
        for (EventRuleAction evtAction : EventRuleAction.values()) {
            eventActionMap.put(evtAction.name(), evtAction);
        }
    };

    public static EventRuleAction getEventAction(String eventActionName) {
        EventRuleAction res = eventActionMap.get(eventActionName);
        if (res != null) {
            return res;
        } else {
            throw new IllegalArgumentException(
                    "Unsupported event action name '" + eventActionName + "'");
        }
    }

}
