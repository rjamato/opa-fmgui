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
 *  File Name: NoticeListener.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/10/21 16:31:04  fernande
 *  Archive Log:    Renaming clearup() method name to cleanup() to be consistent with other implementations.
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

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.api.notice.IEventListener;
import com.intel.stl.ui.framework.IAppEvent;

public class NoticeEventListener implements IEventListener<EventDescription> {
    private final IEventObserver[] observers;

    /**
     * Description:
     * 
     * @param eventBus
     */
    public NoticeEventListener(MBassador<IAppEvent> eventBus) {
        super();
        observers =
                new IEventObserver[] { new NodeEventProcessor(eventBus),
                        new PortEventProcessor(eventBus) };
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.notice.IEventListener#onNewEvent(java.lang.Object[])
     */
    @Override
    public void onNewEvent(EventDescription[] data) {
        for (IEventObserver observer : observers) {
            observer.onEvents(data);
        }
    }

    public void shutdown() {
        for (IEventObserver observer : observers) {
            observer.cleanup();
        }
    }

}
