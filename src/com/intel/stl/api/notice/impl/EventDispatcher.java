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
 *  File Name: EventDispatcher.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10  2015/08/17 18:49:13  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/07/19 21:14:23  jijunwan
 *  Archive Log:    PR 129578 - Event table entries dont obviously correspond to log file
 *  Archive Log:    - added event to log file at info level
 *  Archive Log:    - improved toString for EventDescription and PortSource
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/06/18 21:05:21  fernande
 *  Archive Log:    PR 128977 Application log needs to support multi-subnet. -  Deleting non used file HostSelector.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/05/12 17:37:33  rjtierne
 *  Archive Log:    PR 128623 - Klocwork and FindBugs fixes for backend
 *  Archive Log:    Synchronized blocks in addEvents(), cleanup(), and run() did not modify
 *  Archive Log:    any mutable state. Now synchronizing on mutex instead of "this" (FindBugs: Naked Notify).
 *  Archive Log:
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
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.api.notice.IEventListener;

public class EventDispatcher extends Thread {
    private final static Logger log = LoggerFactory
            .getLogger(EventDispatcher.class);

    private final Object mutex = new Object();

    private boolean stop = false;

    private final List<EventDescription> events =
            new ArrayList<EventDescription>();

    private final List<IEventListener<EventDescription>> eventListeners =
            new CopyOnWriteArrayList<IEventListener<EventDescription>>();

    private Map<String, String> loggingContextMap;

    public void addEvents(List<EventDescription> newEvents) {
        if (newEvents == null || newEvents.isEmpty()) {
            return;
        }

        try {
            synchronized (events) {
                events.addAll(newEvents);
            }
        } finally {
            synchronized (mutex) {
                mutex.notify();
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
        synchronized (mutex) {
            mutex.notify();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        if (loggingContextMap != null) {
            MDC.setContextMap(loggingContextMap);
        }
        log.info("Notice EventDispather '" + getName() + "' started");
        stop = false;
        while (!stop) {
            EventDescription[] toProcess = null;
            synchronized (events) {
                toProcess = events.toArray(new EventDescription[0]);
                events.clear();
            }
            if (toProcess != null && toProcess.length > 0) {
                for (EventDescription event : toProcess) {
                    log.info("Notify event: " + event);
                }
                for (IEventListener<EventDescription> listener : eventListeners) {
                    listener.onNewEvent(toProcess);
                }
            } else {
                synchronized (mutex) {
                    try {
                        if (!stop) {
                            mutex.wait();
                        }
                    } catch (InterruptedException e) {
                        log.warn("Notice EventDispatcher interrupted!");
                    }
                }
            }
        }
        log.info("Notice EventDispather '" + getName() + "' stopped");
    }

    public void setLoggingContextMap(Map<String, String> loggingContextMap) {
        this.loggingContextMap = loggingContextMap;
    }

}
