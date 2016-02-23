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
 *  File Name: EventType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/08/17 18:48:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/08/11 20:35:13  jijunwan
 *  Archive Log:    PR 129935 - Need proper default value for user preference
 *  Archive Log:    - set default notice severity for each event type
 *  Archive Log:    - use default notice severity from event type as the init value for event table
 *  Archive Log:
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

import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.api.notice.TrapType;

/**
 * See STL spec table 20-2 for the default severity level
 */
public enum EventType {
    SM_TOPO_CHANGE(0, EventClass.SUBNET_EVENTS, NoticeSeverity.ERROR),
    PORT_ACTIVE(1, EventClass.SUBNET_EVENTS, NoticeSeverity.INFO),
    PORT_INACTIVE(2, EventClass.SUBNET_EVENTS, NoticeSeverity.WARNING),
    FE_CONNECTION_LOST(3, EventClass.MISCELLANEOUS_EVENTS,
            NoticeSeverity.CRITICAL),
    FE_CONNECTION_ESTABLISH(4, EventClass.MISCELLANEOUS_EVENTS,
            NoticeSeverity.INFO),
    SM_CONNECTION_LOST(5, EventClass.MISCELLANEOUS_EVENTS,
            NoticeSeverity.CRITICAL),
    SM_CONNECTION_ESTABLISH(6, EventClass.MISCELLANEOUS_EVENTS,
            NoticeSeverity.INFO);

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

    private NoticeSeverity defaultSeverity;

    private EventType(int id, EventClass eventClass, NoticeSeverity severity) {
        this.id = id;
        this.eventClass = eventClass;
        this.defaultSeverity = severity;
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

    /**
     * @return the defaultSeverity
     */
    public NoticeSeverity getDefaultSeverity() {
        return defaultSeverity;
    }

    public static EventType getEventType(String eventTypeName) {
        return eventTypeMap.get(eventTypeName);
    }
}
