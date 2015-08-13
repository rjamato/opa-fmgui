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
 *  File Name: StateSummary.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2.2.1  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/19 22:08:56  jijunwan
 *  Archive Log:    moved filter from EventCalculator to StateSummary, so we can have better consistent result
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/08 19:03:23  jijunwan
 *  Archive Log:    backend support for states based on notices
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.ui.publisher.IEventFilter;
import com.intel.stl.ui.publisher.NodeEvents;

public class StateSummary implements Serializable {
    private static final long serialVersionUID = 8192055300607833656L;
    
    private TimedScore healthScore;
    private EnumMap<NoticeSeverity, Integer> switchStates;
    private EnumMap<NoticeSeverity, Integer> hfiStates;
    private NodeScore[] worstNodes;
    private List<NodeEvents> events;
    
    /**
     * @return the healthScore
     */
    public TimedScore getHealthScore() {
        return healthScore;
    }

    /**
     * @param healthScore the healthScore to set
     */
    public void setHealthScore(TimedScore healthScore) {
        this.healthScore = healthScore;
    }

    /**
     * @return the switchStates
     */
    public EnumMap<NoticeSeverity, Integer> getSwitchStates() {
        return switchStates;
    }

    /**
     * @param switchStates the switchStates to set
     */
    public void setSwitchStates(EnumMap<NoticeSeverity, Integer> switchStates) {
        this.switchStates = switchStates;
    }

    /**
     * @return the hfiStates
     */
    public EnumMap<NoticeSeverity, Integer> getHfiStates() {
        return hfiStates;
    }

    /**
     * @param hfiStates the hfiStates to set
     */
    public void setHfiStates(EnumMap<NoticeSeverity, Integer> hfiStates) {
        this.hfiStates = hfiStates;
    }

    /**
     * @return the worstNodes
     */
    public NodeScore[] getWorstNodes() {
        return worstNodes;
    }

    /**
     * @param worstNodes the worstNodes to set
     */
    public void setWorstNodes(NodeScore[] worstNodes) {
        this.worstNodes = worstNodes;
    }

    /**
     * @return the events
     */
    public List<NodeEvents> getEvents() {
        return events;
    }

    /**
     * @param events the events to set
     */
    public void setEvents(List<NodeEvents> events) {
        this.events = events;
    }

    /**
     * 
     *  Description: get custom states
     *  
     *  @param filter the filter applied to indicate the nodes we are interested in.
     *  <code>null</code> means accept all nodes.  
     *  @return
     */
    public EnumMap<NoticeSeverity, Integer> getStates(IEventFilter filter) {
        EnumMap<NoticeSeverity, Integer> res = 
                new EnumMap<NoticeSeverity, Integer>(NoticeSeverity.class);
        int[] counts = new int[NoticeSeverity.values().length];
        for (NodeEvents e : events) {
            if (filter==null || filter.accept(e.getLid(), e.getNodeType())) {
                NoticeSeverity overallSeverity = e.getOverallSeverity();
                if (overallSeverity!=null) {
                    counts[overallSeverity.ordinal()] += 1;
                }
            }
        }
        for (int i=0; i<counts.length; i++) {
            res.put(NoticeSeverity.values()[i], counts[i]);
        }
        return res;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "StateSummary [healthScore=" + healthScore + ", switchStates="
                + switchStates + ", hfiStates=" + hfiStates + ", worstNodes="
                + Arrays.toString(worstNodes) + ", events="+events+"]";
    }

}
