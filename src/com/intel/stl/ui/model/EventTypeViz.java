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
 *  File Name: EventTypeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
