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
 *  File Name: EventSummaryBarPanelController
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7.2.1  2015/08/12 15:27:03  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
