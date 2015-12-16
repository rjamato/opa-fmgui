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
 *  Archive Log:    Revision 1.27  2015/09/29 13:29:14  fernande
 *  Archive Log:    PR129920 - revisit health score calculation. Fixed divide by zero in B2B configuration; the score will be zero if nothing is found in the fabric.
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/09/25 20:54:02  fernande
 *  Archive Log:    PR129920 - revisit health score calculation. Changed formula to include several factors (or attributes) within the calculation as well as user-defined weights (for now are hard coded).
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/08/31 22:33:41  jijunwan
 *  Archive Log:    PR 130197 - Calculated fabric health above 100% when entire fabric is rebooted
 *  Archive Log:    - handle null pointer
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/08/31 22:01:44  jijunwan
 *  Archive Log:    PR 130197 - Calculated fabric health above 100% when entire fabric is rebooted
 *  Archive Log:    - changed to only use information from ImageInfo for calculation
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/08/18 14:28:37  jijunwan
 *  Archive Log:    PR 130033 - Fix critical issues found by Klocwork or FindBugs
 *  Archive Log:    - DateFormat is not thread safe. Changed to create new DateFormat to avoid sharing it among different threads
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/08/17 18:54:08  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/08/17 17:47:52  jijunwan
 *  Archive Log:    added null check
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/08/11 14:10:35  jijunwan
 *  Archive Log:    PR 129917 - No update on event statistics
 *  Archive Log:    - Added a new subscriber to allow periodically getting state summary
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/08/07 19:11:41  jijunwan
 *  Archive Log:    PR 129775 - disable node not available on Worst Node Card
 *  Archive Log:    - improved to display event type
 *  Archive Log:    - improved to disable jumping buttons when event type is PORT_INACTIVE
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/06/29 15:05:48  jypak
 *  Archive Log:    PR 129284 - Incorrect QSFP field name.
 *  Archive Log:    Field name fix has been implemented. Also, introduced a conversion to Date object to add flexibility to display date code.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/05/12 17:43:28  rjtierne
 *  Archive Log:    PR 128624 - Klocwork and FindBugs fixes for UI
 *  Archive Log:    In method clear(), reorganized code to check events for null before trying synchronize on it.
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

import static com.intel.stl.ui.model.HealthScoreAttribute.NUM_HFILINKS;
import static com.intel.stl.ui.model.HealthScoreAttribute.NUM_HFIS;
import static com.intel.stl.ui.model.HealthScoreAttribute.NUM_ISLINKS;
import static com.intel.stl.ui.model.HealthScoreAttribute.NUM_NONDEGRADED_HFILINKS;
import static com.intel.stl.ui.model.HealthScoreAttribute.NUM_NONDEGRADED_ISLINKS;
import static com.intel.stl.ui.model.HealthScoreAttribute.NUM_PORTS;
import static com.intel.stl.ui.model.HealthScoreAttribute.NUM_SWITCHES;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.configuration.EventType;
import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.api.notice.IEventListener;
import com.intel.stl.api.notice.IEventSource;
import com.intel.stl.api.notice.NodeSource;
import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.api.performance.ImageInfoBean;
import com.intel.stl.api.subnet.FabricInfoBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.model.HealthScoreAttribute;
import com.intel.stl.ui.model.NodeScore;
import com.intel.stl.ui.model.StateSummary;
import com.intel.stl.ui.model.TimedScore;
import com.intel.stl.ui.model.UserPreference;
import com.intel.stl.ui.publisher.NodeEvents.EventItem;

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

    /**
     * Initial nodes distribution. NOTE, we set it once when we construct this
     * class. So if we have a subnet with 100 nodes, and then 50 of them are
     * down, we will get a bad health score. If we update <code>nodes</code> to
     * latest distribution, then we will likely get a health score of 100 that
     * doesn't make sense to a user. If the user close FM GUI and then re-launch
     * it, we will use the new nodes distribution as the reference. In another
     * words, we use whatever nodes distribution we get when we start FM GUI as
     * the reference number.
     */
    private final Map<NodeType, Integer> baseNodesDist;

    /**
     * The total number of nodes.
     */
    private final int totalNodes;

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

    private boolean weightsChanged = false;

    private final EnumMap<HealthScoreAttribute, Long> values;

    private final EnumMap<HealthScoreAttribute, Long> totals;

    private final EnumMap<HealthScoreAttribute, Integer> weightSettings;

    private final EnumMap<HealthScoreAttribute, Integer> weights;

    private long[] baseline = new long[] { 0, 0, 0, 0, 0, 0 };

    private int totalWeight;

    private final List<IStateChangeListener> stateChangeListeners =
            new CopyOnWriteArrayList<IStateChangeListener>();

    /**
     * Description:
     * 
     * @param timeWindowInSeconds
     */
    public EventCalculator(EnumMap<NodeType, Integer> nodes,
            UserPreference userPreference) {
        super();
        setTimeWindowInSeconds(userPreference.getTimeWindowInSeconds());
        if (nodes != null) {
            baseNodesDist = Collections.unmodifiableMap(nodes);
        } else {
            baseNodesDist = Collections.emptyMap();
        }
        int sum = 0;
        for (Integer count : baseNodesDist.values()) {
            if (count != null) {
                sum += count;
            }
        }
        this.totalNodes = sum;
        setNumWorstNodes(userPreference.getNumWorstNodes());
        switchStates = new int[NoticeSeverity.values().length];
        hfiStates = new int[NoticeSeverity.values().length];
        events = new LinkedList<NodeEvents>();
        weightSettings =
                new EnumMap<HealthScoreAttribute, Integer>(
                        HealthScoreAttribute.class);
        values =
                new EnumMap<HealthScoreAttribute, Long>(
                        HealthScoreAttribute.class);
        totals =
                new EnumMap<HealthScoreAttribute, Long>(
                        HealthScoreAttribute.class);
        weights =
                new EnumMap<HealthScoreAttribute, Integer>(
                        HealthScoreAttribute.class);
        setHealthScoreWeights(userPreference);
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
     * @return the baseNodesDist
     */
    public Map<NodeType, Integer> getBaseNodesDist() {
        return baseNodesDist;
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
                addEvent(nodeSource, ed.getDate().getTime(), ed.getType(),
                        ed.getSeverity());

                if (DEBUG) {
                    Date date = new Date(ed.getDate().getTime());
                    String dateText = Util.getYYYYMMDDHHMMSS().format(date);

                    System.out.println("new event time " + dateText);
                }
            }
        }
        // Remove old events.
        sweep();

        // Notify listeners of newevent.
        updateListeners();
    }

    protected void addEvent(NodeSource nodeSource, long time, EventType type,
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
            NoticeSeverity newSeverity = ne.addEvent(time, type, severity);
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

        if (events != null && !events.isEmpty()) {
            synchronized (events) {
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

        double healthScore = 0;
        StringBuffer tipBuff = new StringBuffer();
        tipBuff.append("<html>");
        synchronized (critical) {
            for (HealthScoreAttribute attr : values.keySet()) {
                double value = values.get(attr);
                double weight = weights.get(attr);
                double score = (value / totalWeight) * weight;
                healthScore += score;
                if (totals.get(attr) > 0) {
                    long total = totals.get(attr);
                    String desc = attr.getDescription();
                    tipBuff.append(String.format("%1$-25s : %2$6.0f / %3$d",
                            desc, value, total));
                    tipBuff.append("<br>");
                }
            }
        }
        tipBuff.append("</html>");
        // In some instances, we need to round up to show 100% (decimal
        // positions are truncated); this addition is negligible
        healthScore += 0.000000000001;
        return new TimedScore(sweepTime, healthScore * 100, tipBuff.toString());
    }

    /*-
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
     */

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
        }
        size = Math.min(size, nodes.size());
        NodeScore[] res = new NodeScore[size];
        for (int i = 0; i < size; i++) {
            NodeEvents ne = nodes.get(i);
            EventItem item = ne.getLatestEvent();
            if (item != null) {
                res[i] =
                        new NodeScore(ne.getName(), ne.getNodeType(),
                                ne.getLid(), item.getType(), sweepTime,
                                item.getHealthScore());
            }
        }
        Arrays.sort(res);
        return res;
    }

    @Override
    public StateSummary getSummary() {
        // System.out.println("EventCalculaor.getSummary");
        if (!hasSweep) {
            // System.out.println("EventCalculaor.getSummary - hasSweep = null");
            return null;
        }

        sweep();
        StateSummary res = new StateSummary(baseNodesDist);
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

    public boolean updateUserPreference(UserPreference oldUserPreference,
            UserPreference newUserPreference) {
        int oldTimeWindow = oldUserPreference.getTimeWindowInSeconds();
        int newTimeWindow = newUserPreference.getTimeWindowInSeconds();

        boolean userPrefChanged = false;
        // Set time window only if it's not same as old one.
        if (oldTimeWindow < newTimeWindow) {
            setTimeWindowInSeconds(newTimeWindow);
            userPrefChanged = true;
        } else if (oldTimeWindow > newTimeWindow) {
            setTimeWindowInSeconds(newTimeWindow);
            sweep();
            updateListeners();
            userPrefChanged = true;
        }

        int oldWorstNodes = oldUserPreference.getNumWorstNodes();
        int newWorstNodes = newUserPreference.getNumWorstNodes();
        if (oldWorstNodes != newWorstNodes) {
            setNumWorstNodes(newUserPreference.getNumWorstNodes());
            updateListeners();
            userPrefChanged = true;
        }
        setHealthScoreWeights(newUserPreference);
        return userPrefChanged;
    }

    public void processHealthScoreStats(FabricInfoBean fabricInfo,
            ImageInfoBean imageInfo) {
        long numSwitches = fabricInfo.getNumSwitches();
        long numHFIs = fabricInfo.getNumHFIs();
        long numSwitchPorts = imageInfo.getNumSwitchPorts();
        long numHFIPorts = imageInfo.getNumHFIPorts();
        long numISLs =
                fabricInfo.getNumInternalISLs()
                        + fabricInfo.getNumExternalISLs()
                        + fabricInfo.getNumDegradedISLs();
        long numHFILinks =
                fabricInfo.getNumInternalHFILinks()
                        + fabricInfo.getNumExternalHFILinks()
                        + fabricInfo.getNumDegradedHFILinks();
        long[] newTotal =
                new long[] { numSwitches, numHFIs, numSwitchPorts, numHFIPorts,
                        numISLs, numHFILinks };
        boolean baselineChanged = false;
        for (int i = 0; i < baseline.length; i++) {
            if (newTotal[i] > baseline[i]) {
                baselineChanged = true;
                baseline[i] = newTotal[i];
            }
        }
        if (weightsChanged || baselineChanged) {
            resetWeights();
        }
        values.put(NUM_SWITCHES, numSwitches);
        values.put(NUM_HFIS, numHFIs);
        values.put(NUM_ISLINKS, numISLs);
        values.put(NUM_HFILINKS, numHFILinks);
        values.put(NUM_PORTS, numSwitchPorts + numHFIPorts);
        values.put(NUM_NONDEGRADED_ISLINKS,
                baseline[4] - fabricInfo.getNumDegradedISLs());
        values.put(NUM_NONDEGRADED_HFILINKS,
                baseline[5] - fabricInfo.getNumDegradedHFILinks());
    }

    private void resetWeights() {
        int sumBaselinesWeights = 0;

        // Attribute Number of Switches
        int setting = weightSettings.get(NUM_SWITCHES);
        int newWeight;
        if (baseline[0] == 0) {
            newWeight = 0;
        } else {
            newWeight =
                    (int) ((setting == -1) ? ((baseline[2] / baseline[0]) + 1)
                            : setting); // numSwitchPorts / numSwitches + 1
        }
        weights.put(NUM_SWITCHES, newWeight);
        totals.put(NUM_SWITCHES, baseline[0]);
        sumBaselinesWeights += baseline[0] * newWeight;

        // Attribute Number of HFIs
        setting = weightSettings.get(NUM_HFIS);
        if (baseline[1] == 0) {
            newWeight = 0;
        } else {
            newWeight =
                    (int) ((setting == -1) ? ((baseline[3] / baseline[1]) + 1)
                            : setting); // numHFIPorts / numHFIs + 1
        }
        weights.put(NUM_HFIS, newWeight);
        totals.put(NUM_HFIS, baseline[1]);
        sumBaselinesWeights += baseline[1] * newWeight;

        // Attribute Number of Ports
        setting = weightSettings.get(NUM_PORTS);
        weights.put(NUM_PORTS, setting);
        totals.put(NUM_PORTS, baseline[2] + baseline[3]);
        sumBaselinesWeights += (baseline[2] + baseline[3]) * setting;

        // Attribute Number of InterSwitch Links
        setting = weightSettings.get(NUM_ISLINKS);
        weights.put(NUM_ISLINKS, setting);
        totals.put(NUM_ISLINKS, baseline[4]);
        sumBaselinesWeights += baseline[4] * setting;

        // Attribute Number of HFI Links
        setting = weightSettings.get(NUM_HFILINKS);
        weights.put(NUM_HFILINKS, setting);
        totals.put(NUM_HFILINKS, baseline[5]);
        sumBaselinesWeights += baseline[5] * setting;

        // Attribute Number of Non-degraded ISLs
        setting = weightSettings.get(NUM_NONDEGRADED_ISLINKS);
        weights.put(NUM_NONDEGRADED_ISLINKS, setting);
        totals.put(NUM_NONDEGRADED_ISLINKS, baseline[4]);
        sumBaselinesWeights += baseline[4] * setting;

        // Attribute Number of Non-degraded HFI links
        setting = weightSettings.get(NUM_NONDEGRADED_HFILINKS);
        weights.put(NUM_NONDEGRADED_HFILINKS, setting);
        totals.put(NUM_NONDEGRADED_HFILINKS, baseline[5]);
        sumBaselinesWeights += baseline[5] * setting;
        this.totalWeight = sumBaselinesWeights;
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

    protected void setHealthScoreWeights(UserPreference userPreference) {
        weightsChanged = true;
        for (HealthScoreAttribute attr : HealthScoreAttribute.values()) {
            int weight = userPreference.getWeightForHealthScoreAttribute(attr);
            weightSettings.put(attr, weight);
        }
    }

    // For testing
    protected long[] getBaseline() {
        return baseline;
    }

    protected EnumMap<HealthScoreAttribute, Long> getValues() {
        return values;
    }

    protected EnumMap<HealthScoreAttribute, Long> getTotals() {
        return totals;
    }

    protected int getTotalWeight() {
        return totalWeight;
    }
}
