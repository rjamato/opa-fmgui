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
 *  File Name: EventProcesser.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2014/11/05 23:00:25  jijunwan
 *  Archive Log:    improved UI update event to batch mode so we can efficiently process multiple notices
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/21 16:31:04  fernande
 *  Archive Log:    Renaming clearup() method name to cleanup() to be consistent with other implementations.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/26 20:34:02  jijunwan
 *  Archive Log:    improvement on event processors
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/02 18:32:31  jijunwan
 *  Archive Log:    continue on exceptions
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/26 15:04:38  jijunwan
 *  Archive Log:    notice listeners on UI side
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.alert;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.api.configuration.EventType;
import com.intel.stl.api.notice.EventDescription;

public abstract class EventProcesser implements IEventObserver {
    private EventType[] targetTypes;

    private final List<EventDescription> targetEvents =
            new ArrayList<EventDescription>();

    /**
     * @param targetTypes
     *            the targetTypes to set
     */
    public void setTargetTypes(EventType... targetTypes) {
        this.targetTypes = targetTypes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.alert.IEventObserver#onEvent(com.intel.stl.api.notice
     * .EventDescription[])
     */
    @Override
    public void onEvents(EventDescription[] events) {
        targetEvents.clear();
        for (EventDescription event : events) {
            if (isTarget(event.getType())) {
                targetEvents.add(event);
            }
        }
        try {
            processEvents(targetEvents);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finishProcess();
    }

    protected boolean isTarget(EventType type) {
        for (EventType targetType : targetTypes) {
            if (targetType == type) {
                return true;
            }
        }
        return false;
    }

    protected abstract void processEvents(List<EventDescription> evts);

    protected abstract void finishProcess();

}
