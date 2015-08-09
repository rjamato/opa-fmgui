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
 *  File Name: EventClassViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/01/15 19:08:04  rjtierne
 *  Archive Log:    Abbreviated MISCELLANEOUS_EVENTS to better fit in the event wizard window
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/10 20:49:23  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Visualization enum for the EventClass
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import com.intel.stl.api.configuration.EventClass;
import com.intel.stl.ui.common.STLConstants;

public enum EventClassViz {

    SUBNET_EVENTS(EventClass.SUBNET_EVENTS, STLConstants.K0684_SUBNET_EVENTS
            .getValue()),
    MISC_EVENTS(EventClass.MISCELLANEOUS_EVENTS,
            STLConstants.K0685_MISC_EVENTS.getValue());

    private final EventClass eventClass;

    private final String name;

    private EventClassViz(EventClass eventClass, String name) {
        this.eventClass = eventClass;
        this.name = name;
    }

    public EventClass getEventClass() {
        return eventClass;
    }

    public String getName() {
        return name;
    }

    public static EventClassViz getEventTypeClassFor(EventClass evtClass) {
        EventClassViz[] values = EventClassViz.values();
        for (int i = 0; i < values.length; i++) {
            if (evtClass == values[i].getEventClass()) {
                return values[i];
            }
        }
        return null;
    }

}
