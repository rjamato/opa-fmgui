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
 *  File Name: NoticeCalculator.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.16.2.1  2015/08/12 15:26:59  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/03/30 18:34:41  jypak
 *  Archive Log:    Introduce a UserSettingsProcessor to handle different use cases for user settings via Setup Wizard.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/03/26 11:10:04  jypak
 *  Archive Log:    PR 126613 Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    -The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    -The Event Calculator clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/03/25 11:27:01  jypak
 *  Archive Log:    Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    The Event Calculator and the Event Summary Table clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/02/10 23:29:59  jijunwan
 *  Archive Log:    removed unused sweepRate
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/02/06 15:56:39  robertja
 *  Archive Log:    PR 126597 Initialize status summary panels in the absence of event-driven updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/12/11 18:49:26  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/12/08 16:01:53  robertja
 *  Archive Log:    Sweep old events before notifying listeners of new events.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/10/28 15:10:22  robertja
 *  Archive Log:    Change Home page and Performance page status panel updates from poll-driven to event-driven.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/08/19 18:13:44  jijunwan
 *  Archive Log:    changed IEventListener to handler an event array rather than a list of events
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/08/05 18:39:07  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/05 18:32:53  jijunwan
 *  Archive Log:    changed Channel Adapter to Fabric Interface
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/19 22:08:53  jijunwan
 *  Archive Log:    moved filter from EventCalculator to StateSummary, so we can have better consistent result
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/16 15:17:14  jijunwan
 *  Archive Log:    Added filter capability to EventCalculator
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/08 19:03:24  jijunwan
 *  Archive Log:    backend support for states based on notices
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/06 20:24:35  jijunwan
 *  Archive Log:    fixed typo
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 15:20:24  jijunwan
 *  Archive Log:    added state and heal score calculation
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.publisher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.api.notice.IEventListener;
import com.intel.stl.api.notice.IEventSource;
import com.intel.stl.api.notice.NodeSource;
import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.model.NodeScore;
import com.intel.stl.ui.model.StateSummary;
import com.intel.stl.ui.model.TimedScore;

public class EventCalculator implements IEventListener<EventDescription>,
        IStateMonitor {
    private static Logger log = LoggerFactory.getLogger(EventCalculator.class);

    private static final boolean DEBUG = false;

    public static EnumMap<NoticeSeverity, Double> HEALTH_WEIGHTS =
            new EnumMap<NoticeSeverity, Double>(NoticeSeverity.class) {
                private static final long serialVersionUID =
                        2065678798638714805L;
                {
                    put(NoticeSeverity.INFO, 1.0);
                    put(NoticeSeverity.WARNING, 0.8);
                    put(NoticeSeverity.ERROR, 0.3);
                    put(NoticeSeverity.CRITICAL, 0.1);
                }
            };

    private static Comparator<NodeEvents> HEALTH_COMPARATOR =
            new Comparator<NodeEvents>() {

                @Override
                public int compare(NodeEvents o1, NodeEvents o2) {
                    int res =
                            Double.compare(o1.getHealthScore(),
                                    o2.getHealthScore());
                    if (res == 0) {
                        int s1 = o1.getSize();
                        int s2 = o2.getSize();
                        res = s1 > s2 ? -1 : (s1 < s2 ? 1 : 0);
                    }
                    if (res == 0) {
                        int l1 = o1.getLid();
                        int l2 = o2.getLid();
                        res = l1 > l2 ? 1 : (l1 < l2 ? -1 : 0);
                    }
                    // TODO: may need to consider compare by traffic when they
                    // are tie?
                    return res;
                }

            };

    private int totalNodes;

    private volatile int numWorstNodes;

    private volatile int timeWindow;

    private LinkedList<NodeEvents> events;

    private final int[] switchStates;

    private final int[] hfiStates;

    private final Object critical = new Object();

    private List<NodeEvents> eventsImage;

    private int[] switchStatesImage;

    private int[] hfiStatesImage;

    private long sweepTime;

    private boolean hasSweep;

    private final List<IStateChangeListener> stateChangeListeners =
            new CopyOnWriteArrayList<IStateChangeListener>();

    /**
     * Description:
     * 
     * @param timeWindowInSeconds
     */
    public EventCalculator(int timeWindowInSeconds, int totalNodes,
            int numWorstNodes) {
        super();
        this.timeWindow = timeWindowInSeconds * 1000;

        this.totalNodes = totalNodes;
        this.numWorstNodes = numWorstNodes;
        switchStates = new int[NoticeSeverity.values().length];
        hfiStates = new int[NoticeSeverity.values().length];
        events = new LinkedList<NodeEvents>();
        // Initialize switch and HFI states.
        sweep();

    }

    public int getTimeWindowInSeconds() {
        return timeWindow / 1000;
    }

    /**
     * @param timeWindowInSeconds
     *            the timeWindowInSeconds to set
     */
    public void setTimeWindowInSeconds(int timeWindowInSeconds) {
        this.timeWindow = timeWindowInSeconds * 1000;
    }

    /**
     * @return the numWorstNodes
     */
    public int getNumWorstNodes() {
        return numWorstNodes;
    }

    /**
     * @param numWorstNodes
     *            the numWorstNodes to set
     */
    public void setNumWorstNodes(int numWorstNodes) {
        this.numWorstNodes = numWorstNodes;
    }

    /**
     * @return the totalNodes
     */
    public int getTotalNodes() {
        return totalNodes;
    }

    /**
     * @param totalNodes
     *            the totalNodes to set
     */
    public void setTotalNodes(int totalNodes) {
        this.totalNodes = totalNodes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.notice.IEventListener#onNewEvent()
     */
    @Override
    public void onNewEvent(EventDescription[] data) {
        // Filter events and add to queue for processing.
        for (EventDescription ed : data) {
            IEventSource source = ed.getSource();
            if (source instanceof NodeSource) {
                NodeSource nodeSource = (NodeSource) source;
                addEvent(nodeSource, ed.getDate().getTime(), ed.getSeverity());

                if (DEBUG) {
                    Date date = new Date(ed.getDate().getTime());
                    SimpleDateFormat df2 =
                            new SimpleDateFormat("dd/MM/yy HH:mm:ss.sss");
                    String dateText = df2.format(date);

                    System.out.println("new event time " + dateText);
                }
            }
        }
        // Remove old events.
        sweep();

        // Notify listeners of newevent.
        updateListeners();
    }

    protected void addEvent(NodeSource nodeSource, long time,
            NoticeSeverity severity) {
        synchronized (events) {
            NodeEvents ne = new NodeEvents(nodeSource);
            NoticeSeverity oldSeverity = null;
            int index = events.indexOf(ne);
            if (index >= 0) {
                ne = events.get(index);
                oldSeverity = ne.getOverallSeverity();
            } else {
                events.add(ne);
            }
            NoticeSeverity newSeverity = ne.addEvent(time, severity);
            // System.out.println("AddEvent "+oldSeverity+" "+newSeverity+" "+ne);
            updateStates(nodeSource.getNodeType(), oldSeverity, newSeverity);
        }
    }

    public void updateListeners() {
        StateSummary summary = getSummary();
        for (IStateChangeListener listener : stateChangeListeners) {
            listener.onStateChange(summary);
        }
    }

    /**
     * 
     * <i>Description:</i>If a user change severity level in setup wizard, clear
     * all events before we apply the new severity levels.
     * 
     * @param userSettings
     */
    public void clear() {
        synchronized (events) {
            if (events != null && !events.isEmpty()) {
                events.clear();
                synchronized (critical) {
                    eventsImage.clear();

                    for (int i = 0; i < switchStatesImage.length; i++) {
                        switchStatesImage[i] = 0;
                        switchStates[i] = 0;
                    }

                    for (int i = 0; i < hfiStatesImage.length; i++) {
                        hfiStatesImage[i] = 0;
                        hfiStates[i] = 0;
                    }

                    updateListeners();
                }
            }
        }
    }

    protected void updateStates(NodeType type, NoticeSeverity oldSeverity,
            NoticeSeverity newSeverity) {
        // System.out.println("updateStates "+type+" "+oldSeverity+" "+newSeverity);
        if (type == NodeType.SWITCH) {
            if (oldSeverity != null) {
                switchStates[oldSeverity.ordinal()] -= 1;
            }
            if (newSeverity != null) {
                switchStates[newSeverity.ordinal()] += 1;
            }
        } else if (type == NodeType.HFI) {
            if (oldSeverity != null) {
                hfiStates[oldSeverity.ordinal()] -= 1;
            }
            if (newSeverity != null) {
                hfiStates[newSeverity.ordinal()] += 1;
            }
        }
        // System.out.println("switchStates "+Arrays.toString(switchStates));
        // System.out.println("hfiStates "+Arrays.toString(hfiStates));
    }

    protected void clearEvents(long cutTime) {
        synchronized (events) {
            while (!events.isEmpty()
                    && events.get(0).getEarlistTime() < cutTime) {
                NodeEvents ne = events.remove(0);
                NoticeSeverity oldSeverity = ne.getOverallSeverity();
                NoticeSeverity newSeverity = ne.clear(cutTime);
                if (DEBUG) {
                    Date date = new Date(cutTime);
                    SimpleDateFormat df2 =
                            new SimpleDateFormat("dd/MM/yy HH:mm:ss.sss");
                    String dateText = df2.format(date);

                    System.out.println("clearEvents " + oldSeverity + " "
                            + newSeverity + " " + dateText + " " + ne);
                }

                if (newSeverity != oldSeverity) {
                    updateStates(ne.getNodeType(), oldSeverity, newSeverity);
                }
                if (newSeverity != null) {
                    // re-organize the list to put the NodeEvents in correct
                    // position
                    int index = Collections.binarySearch(events, ne);
                    if (index >= 0) {
                        events.add(index, ne);
                    } else {
                        events.add(-index - 1, ne);
                    }
                }
            }
        }

    }

    /**
     * Description:
     * 
     */
    protected void sweep() {
        sweep(System.currentTimeMillis());
    }

    protected void sweep(long time) {
        sweepTime = time;
        synchronized (events) {
            clearEvents(sweepTime - timeWindow);
            List<NodeEvents> eventsCopy = new ArrayList<NodeEvents>();
            for (NodeEvents ne : events) {
                eventsCopy.add(ne.copy());
            }

            int[] switchStatesCopy = new int[switchStates.length];
            System.arraycopy(switchStates, 0, switchStatesCopy, 0,
                    switchStatesCopy.length);
            int[] hfiStatesCopy = new int[hfiStates.length];
            System.arraycopy(hfiStates, 0, hfiStatesCopy, 0,
                    hfiStatesCopy.length);

            synchronized (critical) {
                eventsImage = eventsCopy;
                switchStatesImage = switchStatesCopy;
                hfiStatesImage = hfiStatesCopy;
            }
        }
        if (!hasSweep) {
            hasSweep = true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.publisher.IStateMonitor#getHealthScore()
     */
    @Override
    public TimedScore getHealthScore() {
        if (totalNodes <= 0 || !hasSweep) {
            return null;
        }

        double penaltySum = 0;
        synchronized (critical) {
            for (int i = 0; i < switchStatesImage.length; i++) {
                penaltySum +=
                        switchStatesImage[i]
                                * (1 - HEALTH_WEIGHTS.get(NoticeSeverity
                                        .values()[i]));
            }
            for (int i = 0; i < hfiStatesImage.length; i++) {
                penaltySum +=
                        hfiStatesImage[i]
                                * (1 - HEALTH_WEIGHTS.get(NoticeSeverity
                                        .values()[i]));
            }
        }
        return new TimedScore(sweepTime, (1.0 - penaltySum / totalNodes) * 100);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.publisher.IStateMonitor#getSwitchStates()
     */
    @Override
    public EnumMap<NoticeSeverity, Integer> getSwitchStates() {
        if (!hasSweep) {
            return null;
        }

        EnumMap<NoticeSeverity, Integer> res =
                new EnumMap<NoticeSeverity, Integer>(NoticeSeverity.class);
        synchronized (critical) {
            for (int i = 0; i < switchStatesImage.length; i++) {
                res.put(NoticeSeverity.values()[i], switchStatesImage[i]);
            }
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.publisher.IStateMonitor#getHFIStates()
     */
    @Override
    public EnumMap<NoticeSeverity, Integer> getHFIStates() {
        if (!hasSweep) {
            return null;
        }

        EnumMap<NoticeSeverity, Integer> res =
                new EnumMap<NoticeSeverity, Integer>(NoticeSeverity.class);
        synchronized (critical) {
            for (int i = 0; i < hfiStatesImage.length; i++) {
                res.put(NoticeSeverity.values()[i], hfiStatesImage[i]);
            }
        }
        return res;
    }

    public NodeScore[] getWorstNodes(int size) {
        if (!hasSweep) {
            return null;
        }

        List<NodeEvents> nodes = null;
        synchronized (critical) {
            nodes = new ArrayList<NodeEvents>(eventsImage);
            size = Math.min(size, eventsImage.size());
        }
        Collections.sort(nodes, HEALTH_COMPARATOR);
        NodeScore[] res = new NodeScore[size];
        for (int i = 0; i < size; i++) {
            NodeEvents ne = nodes.get(i);
            res[i] =
                    new NodeScore(ne.getName(), ne.getNodeType(), ne.getLid(),
                            sweepTime, ne.getHealthScore());
        }
        return res;
    }

    @Override
    public StateSummary getSummary() {
        // System.out.println("EventCalculaor.getSummary");
        if (!hasSweep) {
            // System.out.println("EventCalculaor.getSummary - hasSweep = null");
            return null;
        }

        StateSummary res = new StateSummary();
        if (DEBUG) {
            System.out.println("All Events");
            for (NodeEvents ne : eventsImage) {
                System.out.println(" " + ne);
            }
        }
        synchronized (events) {
            res.setHealthScore(getHealthScore());
            res.setSwitchStates(getSwitchStates());
            res.setHfiStates(getHFIStates());
            res.setWorstNodes(getWorstNodes(numWorstNodes));
            res.setEvents(eventsImage);
        }
        if (DEBUG) {
            System.out.println("HealthScore " + res.getHealthScore());
            System.out.println("SwitchStates " + res.getSwitchStates());
            System.out.println("HfiStates " + res.getHfiStates());
            System.out.println("WorstNodes");
            for (NodeScore ns : res.getWorstNodes()) {
                System.out.println(" " + ns);
            }
            System.out.println("All Events");
            for (NodeEvents ne : eventsImage) {
                System.out.println(" " + ne);
            }
        }
        return res;
    }

    public void addListener(IStateChangeListener listener) {
        // System.out.println("EventCalculator.addListener called - "
        // + listener.toString());
        stateChangeListeners.add(listener);
    }

    public void removeListener(IStateChangeListener listener) {
        // System.out.println("EventCalculator.removeListener called -"
        // + listener.toString());
        stateChangeListeners.remove(listener);
    }

    public void cleanup() {
        events = null;
        eventsImage = null;
    }
}
