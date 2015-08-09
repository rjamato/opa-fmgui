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
 *  File Name: EventBusProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2014/11/05 23:00:25  jijunwan
 *  Archive Log:    improved UI update event to batch mode so we can efficiently process multiple notices
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/21 16:31:04  fernande
 *  Archive Log:    Renaming clearup() method name to cleanup() to be consistent with other implementations.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/03 15:34:50  jijunwan
 *  Archive Log:    minor improvement
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/02 21:26:16  jijunwan
 *  Archive Log:    fixed issued found by FindBugs
 *  Archive Log:    Some auto-reformate
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/26 20:34:02  jijunwan
 *  Archive Log:    improvement on event processors
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/15 15:24:25  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
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

import java.util.Collection;
import java.util.List;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.configuration.EventType;
import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.ui.framework.AbstractEvent;
import com.intel.stl.ui.framework.IAppEvent;

public abstract class EventBusProcessor<E extends AbstractEvent> extends
        EventProcesser {
    private final static boolean DEBUG = false;

    private final MBassador<IAppEvent> eventBus;

    private final Collection<E> eventsToSend;

    private final BusWorker worker;

    private boolean toStop;

    /**
     * Description:
     * 
     * @param eventBus
     */
    public EventBusProcessor(MBassador<IAppEvent> eventBus) {
        super();
        this.eventBus = eventBus;
        setTargetTypes(getTargetTypes());
        eventsToSend = createBusEventCollection();

        worker = new BusWorker();
        startWorker(worker);
    }

    protected void startWorker(Runnable worker) {
        Thread thread = new Thread(worker);
        thread.setDaemon(true);
        thread.start();
    }

    protected abstract EventType[] getTargetTypes();

    protected abstract Collection<E> createBusEventCollection();

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.alert.EventProcesser#processEvent(com.intel.stl.api.
     * notice.EventDescription)
     */
    @Override
    protected void processEvents(List<EventDescription> evts) {
        if (toStop) {
            return;
        }

        E event = toBusEvent(evts);
        synchronized (eventsToSend) {
            eventsToSend.add(event);
        }
    }

    protected abstract E toBusEvent(List<EventDescription> evts);

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.alert.EventProcesser#finishProcess()
     */
    @Override
    protected void finishProcess() {
        if (toStop) {
            return;
        }

        synchronized (worker) {
            worker.notify();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.alert.EventProcesser#clearup()
     */
    @Override
    public void cleanup() {
        synchronized (eventsToSend) {
            eventsToSend.clear();
        }
        synchronized (worker) {
            worker.toStop();
            worker.notify();
        }
    }

    class BusWorker implements Runnable {

        public void toStop() {
            toStop = true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            while (!toStop) {
                Object[] events = null;
                synchronized (eventsToSend) {
                    events = eventsToSend.toArray();
                    eventsToSend.clear();
                }
                if (events != null && events.length > 0) {
                    if (DEBUG) {
                        System.out.println("Processing " + events.length
                                + " events...");
                    }
                    for (Object event : events) {
                        if (DEBUG) {
                            System.out.println("Publish  " + event);
                        }
                        eventBus.publish((E) event);
                    }
                } else {
                    synchronized (this) {
                        try {
                            if (!toStop) {
                                wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }
}
