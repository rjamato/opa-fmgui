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
 *  File Name: GraphCells.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2.2.1  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/03 22:13:47  jijunwan
 *  Archive Log:    1) added normalization to GraphEdge, so we can identify the same edges represented in different directions
 *  Archive Log:    2) added helper method to the neighbor of a port
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/23 04:56:57  jijunwan
 *  Archive Log:    new topology code to support interactions with a topology graph
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.mxgraph.model.mxCell;

public class GraphCells {
    /**
     * this is useful when we care about the order of nodes and edges in a 
     * single list 
     */
    private List<GraphCell> all = new ArrayList<GraphCell>();
    private List<GraphNode> nodes = new ArrayList<GraphNode>();
    private List<GraphEdge> edges = new ArrayList<GraphEdge>();
    
    public void addNode(GraphNode node) {
        all.add(node);
        nodes.add(node);
    }
    
    public void addEdge(GraphEdge edge) {
        all.add(edge);
        edges.add(edge);
    }
    
    public boolean hasNodes() {
        return !nodes.isEmpty();
    }
    
    public boolean hasEdges() {
        return !edges.isEmpty();
    }

    /**
     * @return the nodes
     */
    public List<GraphNode> getNodes() {
        return nodes;
    }

    /**
     * @return the edges
     */
    public List<GraphEdge> getEdges() {
        return edges;
    }
    
    public Iterator<GraphCell> iterator() {
        return all.iterator();
    }
    
    public void clear() {
        all.clear();
        nodes.clear();
        edges.clear();
    }
    
    public boolean isEmpty() {
        return all.isEmpty();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GraphCells [nodes=" + nodes + ", edges=" + edges + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((edges == null) ? 0 : edges.hashCode());
        result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
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
        GraphCells other = (GraphCells) obj;
        if (edges == null) {
            if (other.edges != null)
                return false;
        } else if (!edges.equals(other.edges))
            return false;
        if (nodes == null) {
            if (other.nodes != null)
                return false;
        } else if (!nodes.equals(other.nodes))
            return false;
        return true;
    }

    public static GraphCells create(Collection<mxCell> cells, boolean normalized) {
        GraphCells res = new GraphCells();
        
        if (cells!=null) {
            for (mxCell cell : cells) {
                if (cell.isVertex()) {
                    GraphNode node = (GraphNode)cell.getValue();
                    res.addNode(node);
                } else if (cell.isEdge()) {
                    GraphNode source = (GraphNode) cell.getSource().getValue();
                    GraphNode target = (GraphNode) cell.getTarget().getValue();
                    GraphEdge edge = new GraphEdge(
                            source.getLid(),
                            target.getLid(), 
                            source.getLinkPorts(target));
                    res.addEdge(normalized ? edge.normalize() : edge);
                }
            }
        }
        return res;
    }
}
