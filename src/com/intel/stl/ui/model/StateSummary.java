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
 *  File Name: StateSummary.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
