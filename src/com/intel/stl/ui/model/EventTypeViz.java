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
 *  File Name: EventTypeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/08/17 18:53:46  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/15 19:08:15  rjtierne
 *  Archive Log:    Abbreviated "CONNECTION" enumeration names to better fit in the event wizard window
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/13 18:59:54  rjtierne
 *  Archive Log:    Corrected FE_CONNECTION_ESTABLISH enum which contained wrong string
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/29 13:49:57  fernande
 *  Archive Log:    Added missing values from the API enum
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/28 17:25:42  jijunwan
 *  Archive Log:    color severity on event table, by default sort event table by time, by default show event table on home page, show text for enums
 *  Archive Log:
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

import static com.intel.stl.ui.common.STLConstants.K03006_FE_CONN_EST;
import static com.intel.stl.ui.common.STLConstants.K0676_SUBNET_TOPO_CHANGE;
import static com.intel.stl.ui.common.STLConstants.K0677_PORT_ACTIVE;
import static com.intel.stl.ui.common.STLConstants.K0678_PORT_INACTIVE;
import static com.intel.stl.ui.common.STLConstants.K0679_FE_CONN_LOST;
import static com.intel.stl.ui.common.STLConstants.K0680_SM_CONN_LOST;
import static com.intel.stl.ui.common.STLConstants.K0681_SM_CONN_EST;

import com.intel.stl.api.configuration.EventType;

public enum EventTypeViz {

    SM_TOPO_CHANGE(EventType.SM_TOPO_CHANGE, K0676_SUBNET_TOPO_CHANGE
            .getValue()),
    PORT_ACTIVE(EventType.PORT_ACTIVE, K0677_PORT_ACTIVE.getValue()),
    PORT_INACTIVE(EventType.PORT_INACTIVE, K0678_PORT_INACTIVE.getValue()),
    FE_CONN_ESTABLISH(EventType.FE_CONNECTION_ESTABLISH,
            K03006_FE_CONN_EST.getValue()),
    FE_CONN_LOST(EventType.FE_CONNECTION_LOST, K0679_FE_CONN_LOST
            .getValue()),
    SM_CONN_LOST(EventType.SM_CONNECTION_LOST, K0680_SM_CONN_LOST
            .getValue()),
    SM_CONN_ESTABLISH(EventType.SM_CONNECTION_ESTABLISH,
            K0681_SM_CONN_EST.getValue());

    public final static String[] subnetTypes = new String[3];
    static {
        EventTypeViz[] values = EventTypeViz.values();
        int x = 0;
        for (int i = 0; i < values.length; i++) {
            EventTypeViz type = values[i];
            if (type == SM_TOPO_CHANGE || type == PORT_ACTIVE
                    || type == PORT_INACTIVE) {
                subnetTypes[x] = type.name;
                x++;
            }
        }
    };

    public final static String[] miscTypes =
            new String[EventTypeViz.values().length - 3];
    static {
        EventTypeViz[] values = EventTypeViz.values();
        int x = 0;
        for (int i = 0; i < values.length; i++) {
            EventTypeViz type = values[i];
            if (!(type == SM_TOPO_CHANGE || type == PORT_ACTIVE || type == PORT_INACTIVE)) {
                miscTypes[x] = type.name;
                x++;
            }
        }
    };

    private final EventType type;

    private final String name;

    private EventTypeViz(EventType type, String name) {
        this.type = type;
        this.name = name;
    }

    public EventType getEventType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static EventTypeViz getEventTypeVizFor(EventType type) {
        EventTypeViz[] values = EventTypeViz.values();
        for (int i = 0; i < values.length; i++) {
            if (type == values[i].getEventType()) {
                return values[i];
            }
        }
        return null;
    }

    public static EventType getEventTypeFor(String type) {
        EventTypeViz[] values = EventTypeViz.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].name().equals(type)) {
                return values[i].getEventType();
            }
        }
        return null;
    }

    public static String getEventTypeDescription(String type) {
        EventTypeViz[] values = EventTypeViz.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].name().equals(type)) {
                return values[i].name;
            }
        }
        return null;
    }
}
