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
 *  File Name: EventTableController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
