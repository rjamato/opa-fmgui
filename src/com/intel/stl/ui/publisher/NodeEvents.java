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
 *  File Name: NodeEvents.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3.2.1  2015/08/12 15:26:59  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/19 22:08:53  jijunwan
 *  Archive Log:    moved filter from EventCalculator to StateSummary, so we can have better consistent result
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/08 19:03:24  jijunwan
 *  Archive Log:    backend support for states based on notices
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 20:25:16  jijunwan
 *  Archive Log:    moved NodeEvents one level up, so we can do unit test
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.publisher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.intel.stl.api.notice.NodeSource;
import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.api.subnet.NodeType;

public class NodeEvents implements Serializable, Comparable<NodeEvents> {
    private static final long serialVersionUID = 8632566237911275093L;
    
    private int lid;
    private String name;
    private NodeType nodeType;
    private List<NoticeSeverity> eventSeverity = new ArrayList<NoticeSeverity>();
    private List<Long> eventTime = new ArrayList<Long>(); // in ms
    private NoticeSeverity overallSeverity;
    
    public NodeEvents() {
    }
    
    public NodeEvents(NodeSource source) {
        lid = source.getLid();
        nodeType = source.getNodeType();
        name = source.getNodeName();
    }
    
    /**
     * @return the lid
     */
    public int getLid() {
        return lid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the nodeType
     */
    public NodeType getNodeType() {
        return nodeType;
    }
    
    public long getEarlistTime() {
        return eventTime.isEmpty() ? -1 : eventTime.get(0);
    }
    
    public int getSize() {
        return eventTime.size();
    }

    /**
     * @return the overallSeverity
     */
    public synchronized NoticeSeverity getOverallSeverity() {
        return overallSeverity;
    }
    
    public synchronized NoticeSeverity clear(long earliestTime) {
        boolean recalculateSeverity = false;
        while (!eventTime.isEmpty() && eventTime.get(0)<earliestTime) {
            eventTime.remove(0);
            NoticeSeverity severity = eventSeverity.remove(0);
            if (!recalculateSeverity && severity==overallSeverity) {
                recalculateSeverity = true;
            }
        }
        if (recalculateSeverity) {
            overallSeverity = calculateSeverity();
        }
        return overallSeverity;
    }
    
    public synchronized NoticeSeverity addEvent(long time, NoticeSeverity severity) {
        eventTime.add(time);
        eventSeverity.add(severity);
        if (overallSeverity==null || severity.ordinal() > overallSeverity.ordinal()) {
            overallSeverity = severity;
        }
        return overallSeverity;
    }
    
    protected NoticeSeverity calculateSeverity() {
        NoticeSeverity res = null;
        for (NoticeSeverity severity : eventSeverity) {
            if (res==null || severity.ordinal()>res.ordinal()) {
                res = severity;
            }
        }
        return res;
    }
    
    /**
     * 
     *  Description: score in [0, 100]
     *  
     *  @return
     */
    public double getHealthScore() {
        NoticeSeverity severity = getOverallSeverity();
        if (severity==null) {
            return 0;
        }
        return EventCalculator.HEALTH_WEIGHTS.get(severity)*100; 
    }

    /**
     * 
     *  Description: deep copy of the objet
     *  
     *  @return
     */
    public NodeEvents copy() {
        NodeEvents res = new NodeEvents();
        res.lid = this.lid;
        res.name = new String(this.name);
        res.nodeType = this.nodeType;
        res.eventTime = new ArrayList<Long>(this.eventTime);
        res.eventSeverity = new ArrayList<NoticeSeverity>(this.eventSeverity);
        res.overallSeverity = this.overallSeverity;
        return res;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + lid;
        return result;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NodeEvents other = (NodeEvents) obj;
        if (lid != other.lid)
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(NodeEvents o) {
        long t1 = getEarlistTime();
        long t2 = o.getEarlistTime();
        return t1>t2 ? 1 : (t1<t2 ? -1 : 0);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NodeEvents [lid=" + lid + ", nodeType=" + nodeType
                + ", overallSeverity=" + overallSeverity
                + ", eventSeverity=" + eventSeverity + ", eventTime="
                + eventTime + "]";
    }

}
