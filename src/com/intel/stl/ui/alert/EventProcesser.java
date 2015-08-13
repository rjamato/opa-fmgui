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
 *  File Name: EventProcesser.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.1  2015/08/12 15:26:57  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
