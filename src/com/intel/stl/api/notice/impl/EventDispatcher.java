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
 *  File Name: EventDispatcher.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/02/10 19:29:46  fernande
 *  Archive Log:    SubnetContext are now created after a successful connection is made to the Fabric Executive, otherwise a SubnetConnectionException is triggered. Also, waitForConnect has been postponed until the UI invokes SubnetContext.initialize (thru Context.initialize). This way the UI shows up faster and the UI progress bar reflects more closely the process.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/12/11 18:33:07  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/11/04 14:01:34  fernande
 *  Archive Log:    NoticeManager performance improvements. Now notices are processed in batches to the database, resulting in less CopyTopology requests. Increased threading so that database work runs now in parallel with cache updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/02 21:17:37  jijunwan
 *  Archive Log:    fixed issued found by FindBugs
 *  Archive Log:    Some auto-reformate
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/02 18:30:16  jijunwan
 *  Archive Log:    minot code improvement
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/19 18:09:52  jijunwan
 *  Archive Log:    Introduced EventDispatcher to process notices in a separate thread, so we do not block on FE connection
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.notice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.api.notice.IEventListener;

public class EventDispatcher extends Thread {
    private final static Logger log = LoggerFactory
            .getLogger(EventDispatcher.class);

    private boolean stop = false;

    private final List<EventDescription> events =
            new ArrayList<EventDescription>();

    private final List<IEventListener<EventDescription>> eventListeners =
            new CopyOnWriteArrayList<IEventListener<EventDescription>>();

    public void addEvents(List<EventDescription> newEvents) {
        if (newEvents == null || newEvents.isEmpty()) {
            return;

        }

        try {
            synchronized (events) {
                events.addAll(newEvents);
            }
        } finally {
            synchronized (this) {
                notify();
            }
        }

    }

    /**
     * @return the stop
     */
    public boolean isStop() {
        return stop;
    }

    /**
     * @param stop
     *            the stop to set
     */
    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void addEventListener(IEventListener<EventDescription> listener) {
        eventListeners.add(listener);
    }

    public void removeEventListener(IEventListener<EventDescription> listener) {
        eventListeners.remove(listener);
    }

    public void cleanup() {
        stop = true;
        eventListeners.clear();
        synchronized (events) {
            events.clear();
        }
        synchronized (this) {
            notify();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        log.info("Notice EventDispather '" + getName() + "' started");
        stop = false;
        while (!stop) {
            EventDescription[] toProcess = null;
            synchronized (events) {
                toProcess = events.toArray(new EventDescription[0]);
                events.clear();
            }
            if (toProcess != null && toProcess.length > 0) {
                for (IEventListener<EventDescription> listener : eventListeners) {
                    listener.onNewEvent(toProcess);
                }
            } else {
                synchronized (this) {
                    try {
                        if (!stop) {
                            wait();
                        }
                    } catch (InterruptedException e) {
                        log.warn("EventDispatcher interrupted!");
                    }
                }
            }
        }
        log.info("Notice EventDispather '" + getName() + "' stopped");
    }

}
