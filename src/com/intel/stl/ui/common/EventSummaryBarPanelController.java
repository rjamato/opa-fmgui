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
 *  File Name: EventSummaryBarPanelController
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2014/12/08 16:00:02  robertja
 *  Archive Log:    Set new context after removing any listeners to old context.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/28 15:10:20  robertja
 *  Archive Log:    Change Home page and Performance page status panel updates from poll-driven to event-driven.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/09 12:32:57  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext).
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/19 22:08:51  jijunwan
 *  Archive Log:    moved filter from EventCalculator to StateSummary, so we can have better consistent result
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/16 04:30:39  jijunwan
 *  Archive Log:    Added code to deregister from task scheduler; Added Page Listener to listen enter or exit a (sub)page
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/14 21:43:20  jypak
 *  Archive Log:    Event Summary Table updates.
 *  Archive Log:    1. Replace EventMsgBean with EventDescription.
 *  Archive Log:    2. Update table contents with real data from Notice API.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/13 13:03:53  jypak
 *  Archive Log:    Event Summary Bar panel in pin board implementation.
 *  Archive Log:
 *
 *  Overview: A controller for the severity count summary panel (EventSummaryBarPanel) 
 *            that goes to pin board. 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import static com.intel.stl.ui.common.PageWeight.LOW;

import java.util.EnumMap;

import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.ui.common.view.EventSummaryBarPanel;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.StateSummary;
import com.intel.stl.ui.publisher.IStateChangeListener;

public class EventSummaryBarPanelController implements IContextAware,
        IStateChangeListener {

    private static final String NAME = "EventSummaryBar";

    private final EventSummaryBarPanel view;

    private Context context;

    private IEventSummaryBarListener iEventSummaryBarListener;

    public EventSummaryBarPanelController(
            EventSummaryBarPanel eventSummaryBarPanel) {
        this.view = eventSummaryBarPanel;
    }

    public void setEventSummaryBarListener(
            IEventSummaryBarListener iEventSummaryBarListener) {
        this.iEventSummaryBarListener = iEventSummaryBarListener;
        view.setEventSummaryBarListener(this.iEventSummaryBarListener);
    }

    /**
     * @param context
     *            the context to set
     */
    @Override
    public void setContext(Context context, IProgressObserver observer) {
        if (this.context != null) {
            this.context.getEvtCal().removeListener(this);
        }

        this.context = context;

        this.context.getEvtCal().addListener(this);

    }

    @Override
    public String getName() {
        return NAME;
    }

    protected void processStateSummary(StateSummary stateSummary) {
        // Update Event Summary into the EventSummaryBarPanel
        final EnumMap<NoticeSeverity, Integer> states =
                stateSummary.getStates(null);
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                view.updateEventSeverity(states);
            }
        });
    }

    @Override
    public PageWeight getContextSwitchWeight() {
        return LOW;
    }

    @Override
    public PageWeight getRefreshWeight() {
        return LOW;
    }

    @Override
    public void onStateChange(StateSummary summary) {
        if (summary != null) {
            // System.out.println("EventSummary.onStateChange called.");
            processStateSummary(summary);
        }
    }

    @Override
    public String toString() {
        return "EventSummaryBarPanelController";
    }
}