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
 *  File Name: NodeEventObserver.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/02/04 21:44:25  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.engio.mbassy.bus.MBassador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.configuration.EventType;
import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.api.notice.IEventSource;
import com.intel.stl.api.notice.PortSource;
import com.intel.stl.ui.event.PortUpdateEvent;
import com.intel.stl.ui.framework.IAppEvent;

public class PortEventProcessor extends EventBusProcessor<PortUpdateEvent> {
    private static Logger log = LoggerFactory
            .getLogger(PortEventProcessor.class);

    /**
     * Description:
     * 
     * @param eventBus
     */
    public PortEventProcessor(MBassador<IAppEvent> eventBus) {
        super(eventBus);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.alert.EventBusProcessor#getTargetTypes()
     */
    @Override
    protected EventType[] getTargetTypes() {
        return new EventType[] { EventType.PORT_ACTIVE,
                EventType.PORT_INACTIVE, };
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.alert.EventBusProcessor#createBusEventCollection()
     */
    @Override
    protected Collection<PortUpdateEvent> createBusEventCollection() {
        return new LinkedHashSet<PortUpdateEvent>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.alert.EventBusProcessor#toBusEvent(com.intel.stl.api
     * .notice.EventDescription)
     */
    @Override
    protected PortUpdateEvent toBusEvent(List<EventDescription> evts) {
        Set<PortSource> tmp = new LinkedHashSet<PortSource>(evts.size());
        for (EventDescription evt : evts) {
            IEventSource source = evt.getSource();
            if (source instanceof PortSource) {
                tmp.add((PortSource) source);
            } else {
                log.info("Unsupported event source " + source);
            }

        }
        Iterator<PortSource> it = tmp.iterator();
        int[] lids = new int[tmp.size()];
        short[] ports = new short[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            PortSource ps = it.next();
            lids[i] = ps.getLid();
            ports[i] = ps.getPortNum();
        }
        return new PortUpdateEvent(lids, ports, this);
    }
}
