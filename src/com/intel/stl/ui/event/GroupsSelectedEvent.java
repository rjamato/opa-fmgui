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
 *  File Name: GroupSelectedEvent.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/08/17 18:53:45  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/08/06 17:19:53  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - improved GroupSelectedEvent to GroupsSelectedEvent to support selecting multiple groups
 *  Archive Log:    - fixed couple NPE issues
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/08/05 03:05:58  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - improved JumpToEvent to use string for destination so we can support more pages
 *  Archive Log:    - improved NodeSelectionEvent to NodesSelectionEvent to support multiple nodes
 *  Archive Log:    - improved PortSelectionEvent to PortsSelectionEvent to support multiple ports
 *  Archive Log:    - added PageSelectionEvent to support jumping to a page, so we can support undo of jumping event
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.event;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.ui.monitor.TreeNodeType;

public class GroupsSelectedEvent extends JumpToEvent {
    private final List<Item> items = new ArrayList<Item>();

    public GroupsSelectedEvent(Object origin, String destination) {
        super(origin, destination);
    }

    /**
     * Description:
     * 
     * @param origin
     * @param destination
     * @param name
     * @param type
     */
    public GroupsSelectedEvent(Object origin, String destination, String name,
            TreeNodeType type) {
        super(origin, destination);
        addGroup(name, type);
    }

    public void addGroup(String name, TreeNodeType type) {
        Item item = new Item(name, type);
        items.add(item);
    }

    /**
     * @return the name
     */
    public String getName(int index) {
        return items.get(index).getName();
    }

    /**
     * @return the type
     */
    public TreeNodeType getType(int index) {
        return items.get(index).getType();
    }

    public int getNumGroups() {
        return items.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GroupsSelectedEvent other = (GroupsSelectedEvent) obj;
        if (items == null) {
            if (other.items != null) {
                return false;
            }
        } else if (!items.equals(other.items)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GroupSelectedEvent [items=" + items + ", destination="
                + destination + "]";
    }

    protected static class Item {
        private final String name;

        private final TreeNodeType type;

        /**
         * Description:
         * 
         * @param name
         * @param type
         */
        public Item(String name, TreeNodeType type) {
            super();
            this.name = name;
            this.type = type;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the type
         */
        public TreeNodeType getType() {
            return type;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Item other = (Item) obj;
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            if (type != other.type) {
                return false;
            }
            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Item [name=" + name + ", type=" + type + "]";
        }

    }
}
