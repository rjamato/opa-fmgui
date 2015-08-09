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
 *  File Name: EventType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/12/11 18:31:04  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/10 20:32:41  rjtierne
 *  Archive Log:    Support for saving EventRules to UserSettings
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 15:16:31  jijunwan
 *  Archive Log:    moved EventType from notice to configuration since it's used form event configuration
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/30 16:06:32  fernande
 *  Archive Log:    Initial version
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.notice.TrapType;

public enum EventType {
    SM_TOPO_CHANGE(0, EventClass.SUBNET_EVENTS),
    PORT_ACTIVE(1, EventClass.SUBNET_EVENTS),
    PORT_INACTIVE(2, EventClass.SUBNET_EVENTS),
    FE_CONNECTION_LOST(3, EventClass.MISCELLANEOUS_EVENTS),
    FE_CONNECTION_ESTABLISH(4, EventClass.MISCELLANEOUS_EVENTS),
    SM_CONNECTION_LOST(5, EventClass.MISCELLANEOUS_EVENTS),
    SM_CONNECTION_ESTABLISH(6, EventClass.MISCELLANEOUS_EVENTS);

    private final static Map<String, EventType> eventTypeMap =
            new HashMap<String, EventType>();
    static {
        for (EventType evtType : EventType.values()) {
            eventTypeMap.put(evtType.name(), evtType);
        }
    };

    private static Logger log = LoggerFactory.getLogger(EventType.class);

    private int id;

    private EventClass eventClass;

    private EventType(int id, EventClass eventClass) {
        this.id = id;
        this.eventClass = eventClass;
    }

    public static EventType getEventType(TrapType type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case GID_NOW_IN_SERVICE:
                return EventType.PORT_ACTIVE;
            case GID_OUT_OF_SERVICE:
                return EventType.PORT_INACTIVE;
            case LINK_PORT_CHANGE_STATE:
                return EventType.SM_TOPO_CHANGE;
            case SM_CONNECTION_LOST:
                return EventType.SM_CONNECTION_LOST;
            case SM_CONNECTION_ESTABLISH:
                return EventType.SM_CONNECTION_ESTABLISH;
            case FE_CONNECTION_LOST:
                return EventType.FE_CONNECTION_LOST;
            case FE_CONNECTION_ESTABLISH:
                return EventType.FE_CONNECTION_ESTABLISH;
            default:
                throw new IllegalArgumentException("Unsupported TrapType "
                        + type);
        }
    }

    public static EventType getEventType(short type) {
        return getEventType(TrapType.getTrapType(type));
    }

    public int getId() {
        return id;
    }

    public EventClass getEventClass() {
        return eventClass;
    }

    public static EventType getEventType(String eventTypeName) {
        return eventTypeMap.get(eventTypeName);
    }
}
