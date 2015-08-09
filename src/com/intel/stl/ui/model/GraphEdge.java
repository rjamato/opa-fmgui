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
 *  File Name: GraphEdge.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/07/03 22:13:47  jijunwan
 *  Archive Log:    1) added normalization to GraphEdge, so we can identify the same edges represented in different directions
 *  Archive Log:    2) added helper method to the neighbor of a port
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/23 04:56:57  jijunwan
 *  Archive Log:    new topology code to support interactions with a topology graph
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/23 19:47:55  jijunwan
 *  Archive Log:    init version of topology page
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.Map;
import java.util.TreeMap;

public class GraphEdge extends GraphCell {
    private static final long serialVersionUID = 6368674272408088705L;
    private int fromLid;
    private int toLid;
    private Map<Integer, Integer> links;
    
    /**
     * Description: 
     * 
     */
    public GraphEdge() {
        super();
    }

    public GraphEdge(int fromLid, int toLid, Map<Integer, Integer> links) {
        super();
        this.fromLid = fromLid;
        this.toLid = toLid;
        this.links = links;
        this.name = createName();
    }
    
    /**
     * Description: 
     *
     * @param fromLid
     * @param toLid
     * @param links 
     */
    public GraphEdge(String name, int fromLid, int toLid, Map<Integer, Integer> links) {
        super(name);
        this.fromLid = fromLid;
        this.toLid = toLid;
        this.links = links;
    }
    
    public GraphEdge normalize() {
        if (fromLid < toLid) {
            Map<Integer, Integer> reversedLinks = new TreeMap<Integer, Integer>();
            for (Integer key : links.keySet()) {
                reversedLinks.put(links.get(key), key);
            }
            return new GraphEdge(toLid, fromLid, reversedLinks);
        } else {
            return this;
        }
    }
    
    /* (non-Javadoc)
     * @see com.intel.stl.ui.model.GraphCell#isVertex()
     */
    @Override
    public boolean isVertex() {
        return false;
    }

    protected String createName() {
        return fromLid+"-"+toLid;
    }

    /**
     * @return the fromLid
     */
    public int getFromLid() {
        return fromLid;
    }

    /**
     * @param fromLid the fromLid to set
     */
    public void setFromLid(int fromLid) {
        this.fromLid = fromLid;
    }

    /**
     * @return the toLid
     */
    public int getToLid() {
        return toLid;
    }

    /**
     * @param toLid the toLid to set
     */
    public void setToLid(int toLid) {
        this.toLid = toLid;
    }

    /**
     * @return the links
     */
    public Map<Integer, Integer> getLinks() {
        return links;
    }

    /**
     * @param links the links to set
     */
    public void setLinks(Map<Integer, Integer> links) {
        this.links = links;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + fromLid;
        result = prime * result + toLid;
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
        GraphEdge other = (GraphEdge) obj;
        if (fromLid != other.fromLid)
            return false;
        if (toLid != other.toLid)
            return false;
        return true;
    }

    public String toString() {
        return fromLid+"-"+toLid+":"+links.size()+" "+links;
    }
}
