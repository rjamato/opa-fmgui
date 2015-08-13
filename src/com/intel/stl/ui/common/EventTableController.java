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
 *  File Name: EventTableController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10.2.1  2015/08/12 15:27:03  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/03/26 11:10:01  jypak
 *  Archive Log:    PR 126613 Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    -The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    -The Event Calculator clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/03/25 11:26:58  jypak
 *  Archive Log:    Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    The Event Calculator and the Event Summary Table clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/12/11 18:44:35  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/09 12:32:57  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext).
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/08/19 18:13:43  jijunwan
 *  Archive Log:    changed IEventListener to handler an event array rather than a list of events
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/29 22:03:59  jijunwan
 *  Archive Log:    removeed listener from old noticeApi
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/29 14:21:16  jijunwan
 *  Archive Log:    thread safe table model, added bound control on table model
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/14 21:43:20  jypak
 *  Archive Log:    Event Summary Table updates.
 *  Archive Log:    1. Replace EventMsgBean with EventDescription.
 *  Archive Log:    2. Update table contents with real data from Notice API.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/21 15:43:05  jijunwan
 *  Archive Log:    Added #clear to be able to clear UI before we switch to another context. In the future, should change it to eventbus
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:46:33  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/09 20:59:42  rjtierne
 *  Archive Log:    Captured result from call to AddEntry() method
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/04 19:14:12  rjtierne
 *  Archive Log:    Added logger and removed test code.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/03/28 15:07:44  rjtierne
 *  Archive Log:    This class serves as the controller for the event table view
 *  Archive Log:
 *
 *  Overview: This class serves as the controller for the event table view
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import static com.intel.stl.ui.common.PageWeight.LOW;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.api.notice.IEventListener;
import com.intel.stl.api.notice.INoticeApi;
import com.intel.stl.ui.common.view.FVTableView;
import com.intel.stl.ui.main.Context;

public class EventTableController implements IEventListener<EventDescription>,
        IContextAware {

    private static final String NAME = "EventTable";

    /**
     * Local attribute for the event table model
     */
    EventTableModel mEvtTblModel;

    /**
     * Local attribute for the event table view
     */
    FVTableView mEvtTblView;

    /**
     * Logger for the EventTableController class
     */
    private static Logger mLog = LoggerFactory
            .getLogger(EventTableController.class);

    private INoticeApi noticeApi;

    /**
     * 
     * Description: Constructor for the EventTableController class
     * 
     * @param pEvtTblModel
     *            - supporting table model
     * 
     * @param pEvtTblView
     *            - table view
     */
    public EventTableController(EventTableModel pEvtTblModel,
            FVTableView pEvtTblView) {
        mEvtTblModel = pEvtTblModel;
        mEvtTblView = pEvtTblView;
    } // EventTableController

    /**
     * 
     * Description: Add an event to the table
     * 
     * @param event
     *            - message describing the event
     */
    public boolean addEvent(final EventDescription event) {
        mLog.info(null, event.getType());

        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                mEvtTblModel.addEntry(event);
                mEvtTblModel.fireTableDataChanged();
            }
        });

        return true;
    } // addEvent

    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                mEvtTblModel.clear();
                mEvtTblModel.fireTableDataChanged();
            }
        });
    }

    @Override
    public void setContext(Context context, IProgressObserver observer) {
        if (noticeApi != null) {
            noticeApi.removeEventListener(this);
        }
        noticeApi = context.getNoticeApi();
        noticeApi.addEventListener(this);
    }

    @Override
    public String getName() {
        return NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.notice.IEventListener#onNewEvent()
     */
    @Override
    public void onNewEvent(EventDescription[] eventList) {
        for (EventDescription event : eventList) {
            addEvent(event);
        }
    }

    @Override
    public PageWeight getContextSwitchWeight() {
        return LOW;
    }

    @Override
    public PageWeight getRefreshWeight() {
        return LOW;
    }

} // class EventTableController
