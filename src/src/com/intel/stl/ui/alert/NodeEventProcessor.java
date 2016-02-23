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
 *  File Name: NodeEventObserver.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/08/17 18:53:35  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/05/20 19:07:37  jijunwan
 *  Archive Log:    fixed unit test issues
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/05/19 19:18:16  jijunwan
 *  Archive Log:    PR 128797 - Notice update failed to update related notes
 *  Archive Log:    - put related nodes into lids array
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/11 18:44:10  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/05 23:00:25  jijunwan
 *  Archive Log:    improved UI update event to batch mode so we can efficiently process multiple notices
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.engio.mbassy.bus.MBassador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.configuration.EventType;
import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.api.notice.IEventSource;
import com.intel.stl.api.notice.NodeSource;
import com.intel.stl.ui.event.NodeUpdateEvent;
import com.intel.stl.ui.framework.IAppEvent;

public class NodeEventProcessor extends EventBusProcessor<NodeUpdateEvent> {
    private static Logger log = LoggerFactory
            .getLogger(NodeEventProcessor.class);

    /**
     * Description:
     * 
     * @param eventBus
     */
    public NodeEventProcessor(MBassador<IAppEvent> eventBus) {
        super(eventBus);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.alert.EventBusProcessor#getTargetTypes()
     */
    @Override
    protected EventType[] getTargetTypes() {
        return new EventType[] { EventType.SM_TOPO_CHANGE,
                EventType.PORT_ACTIVE, EventType.PORT_INACTIVE,
                EventType.SM_CONNECTION_ESTABLISH, EventType.SM_CONNECTION_LOST };
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.alert.EventBusProcessor#createBusEventCollection()
     */
    @Override
    protected Collection<NodeUpdateEvent> createBusEventCollection() {
        return new LinkedHashSet<NodeUpdateEvent>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.alert.EventBusProcessor#toBusEvent(com.intel.stl.api
     * .notice.EventDescription)
     */
    @Override
    protected NodeUpdateEvent toBusEvent(List<EventDescription> evts) {
        Set<Integer> nodesToUpdate = new LinkedHashSet<Integer>();
        for (EventDescription evt : evts) {
            IEventSource source = evt.getSource();
            if (source instanceof NodeSource) {
                nodesToUpdate.add(((NodeSource) source).getLid());
            } else {
                log.info("Unsupported event source " + source);
            }
            Set<Integer> relatedNodes = evt.getRelatedNodes();
            if (relatedNodes != null) {
                nodesToUpdate.addAll(relatedNodes);
            }
        }
        int[] lids = new int[nodesToUpdate.size()];
        int i = 0;
        for (Integer lid : nodesToUpdate) {
            lids[i++] = lid;
        }
        return new NodeUpdateEvent(lids, this);
    }
}
