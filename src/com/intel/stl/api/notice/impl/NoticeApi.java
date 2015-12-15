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
 *  File Name: NoticeApi.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.36  2015/09/10 15:27:35  fernande
 *  Archive Log:    PR 129940 - Persisted notices have no time stamp data. Added timestamp to NoticeBean
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2015/08/17 18:49:13  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2015/07/10 20:43:33  fernande
 *  Archive Log:    PR 129522 - Notice is not written to database due to topology not found. Moved FE Helpers to the session object and changed the order of initialization for the SubnetContext.
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/06/18 21:05:21  fernande
 *  Archive Log:    PR 128977 Application log needs to support multi-subnet. -  Deleting non used file HostSelector.
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2015/05/29 20:34:19  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Second wave of changes: the application can be switched between the old adapter and the new; moved out several initialization pieces out of objects constructor to allow subnet initialization with a UI in place; improved generics definitions for FV commands.
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/05/26 15:34:02  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. A new FEAdapter is being added to handle requests through SubnetRequestDispatchers, which manage state for each connection to a subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/05/19 19:07:18  jijunwan
 *  Archive Log:    PR 128797 - Notice update failed to update related notes
 *  Archive Log:    - created a new class NoticeWrapper to store information about related nodes, and then pass this infor to EventDescription that will allow UI to upate related nodes
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/05/01 21:37:41  jijunwan
 *  Archive Log:    fixed typo found by FindBug
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/04/27 21:45:39  rjtierne
 *  Archive Log:    Moved call to getEventType() to the try-block so exception will be caught
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/03/27 20:35:06  fernande
 *  Archive Log:    Changed to use the interface IConnection instead of the concrete implementation
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/03/26 11:09:59  jypak
 *  Archive Log:    PR 126613 Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    -The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    -The Event Calculator clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/03/25 11:26:56  jypak
 *  Archive Log:    Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    The Event Calculator and the Event Summary Table clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/03/16 17:34:56  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/02/06 15:01:33  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2014/10/14 20:44:49  jijunwan
 *  Archive Log:    1) improved to set SubnetContext invalid when we have network connection issues
 *  Archive Log:    2) improved to recreate SubnetContext when we query for it and the current one is invalid. We also clean up (include shutdown) the old context before we replace it with a new one
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/10/14 11:26:48  jypak
 *  Archive Log:    UI related updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/10/02 21:17:37  jijunwan
 *  Archive Log:    fixed issued found by FindBugs
 *  Archive Log:    Some auto-reformate
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/10/01 22:43:13  jypak
 *  Archive Log:    1. Fixed a bug related to extraneous notice listeners to a STLConnection.
 *  Archive Log:    2. Fixed incorrect map iteration in notice manager shutdown implementation.
 *  Archive Log:    3. Added shutdown invocations to tests to stop notice manager threads.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/09/26 14:57:56  jijunwan
 *  Archive Log:    fixed a typo
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/09/15 15:04:08  jijunwan
 *  Archive Log:    fixed a bug on notice simulation that happens when we turn on random after it was turned off
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/09/05 15:33:16  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/09/02 18:30:16  jijunwan
 *  Archive Log:    minot code improvement
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/08/28 14:56:53  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/08/27 17:02:35  fernande
 *  Archive Log:    Database changes to add Notice and ImageInfo tables to the schema database
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/08/22 21:38:10  fernande
 *  Archive Log:    Adding support for saving notices and imageinfos to the database
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/08/19 18:09:52  jijunwan
 *  Archive Log:    Introduced EventDispatcher to process notices in a separate thread, so we do not block on FE connection
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/08/12 20:31:09  jijunwan
 *  Archive Log:    code clean up
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/07/11 13:07:02  jypak
 *  Archive Log:    1. Added runtime, non runtime exceptions to be thrown for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    2. Updated exception generating code due to Cache Manager related changes.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/03 21:36:57  fernande
 *  Archive Log:    Adding the CacheManager in support of APIs
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/19 20:02:16  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/19 16:13:19  jijunwan
 *  Archive Log:    added null check
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/30 21:50:17  jijunwan
 *  Archive Log:    moved all random generation to API side, and added a method to allow us turn on/off randomization
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/08 18:59:20  jijunwan
 *  Archive Log:    added Notice Simulator to simulate notices from FM
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/07 14:23:55  jijunwan
 *  Archive Log:    added TrapSysguid
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/06 20:24:01  jijunwan
 *  Archive Log:    fixed typo
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 15:14:06  jijunwan
 *  Archive Log:    notice and trap implementation
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.notice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.MDC;

import com.intel.stl.api.configuration.EventRule;
import com.intel.stl.api.configuration.EventType;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.configuration.impl.SubnetContextImpl;
import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.api.notice.FESource;
import com.intel.stl.api.notice.GenericNoticeAttrBean;
import com.intel.stl.api.notice.IEventListener;
import com.intel.stl.api.notice.IEventSource;
import com.intel.stl.api.notice.INoticeApi;
import com.intel.stl.api.notice.NodeSource;
import com.intel.stl.api.notice.NoticeBean;
import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.api.notice.NoticeWrapper;
import com.intel.stl.api.notice.PortSource;
import com.intel.stl.api.notice.TrapLinkBean;
import com.intel.stl.api.notice.TrapSwitchPKeyBean;
import com.intel.stl.api.notice.TrapSysguidBean;
import com.intel.stl.api.notice.TrapType;
import com.intel.stl.api.subnet.GIDBean;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetException;
import com.intel.stl.api.subnet.impl.NodeCache;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.fecdriver.messages.adapter.sa.trap.TrapDetail;

public class NoticeApi implements INoticeApi {
    private static final String THREAD_NAME_PREFIX = "nedthread-";

    private static boolean DEBUG = false;

    private final SubnetContextImpl subnetContext;

    private final CacheManager cacheMgr;

    private final EventDispatcher worker;

    private final Map<EventType, NoticeSeverity> eventSeverityMap =
            new HashMap<EventType, NoticeSeverity>();

    public NoticeApi(SubnetContextImpl subnetContext) {
        worker = new EventDispatcher();
        this.subnetContext = subnetContext;
        this.cacheMgr = subnetContext.getCacheManager();

        startWorker();
    }

    protected void startWorker() {
        MDC.put("subnet", subnetContext.getSubnetDescription().getName());
        worker.setLoggingContextMap(MDC.getCopyOfContextMap());
        worker.setDaemon(true);
        worker.setName(THREAD_NAME_PREFIX
                + subnetContext.getSubnetDescription().getSubnetId());
        worker.start();
    }

    @Override
    public void addNewEventDescriptions(NoticeWrapper[] data) {
        if (DEBUG) {
            for (NoticeWrapper bean : data) {
                System.out.println(bean);
            }
        }
        List<EventDescription> events = new ArrayList<EventDescription>();
        for (NoticeWrapper nw : data) {
            EventDescription eventDescription = asEventDescription(nw);
            events.add(eventDescription);
        }
        worker.addEvents(events);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.notice.INoticeApi#addNoticeListener(com.intel.stl.api
     * .notice.INoticeListener)
     */
    @Override
    public void addEventListener(IEventListener<EventDescription> listener) {
        worker.addEventListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.notice.INoticeApi#removeNoticeListener(com.intel.stl
     * .api.notice.INoticeListener)
     */
    @Override
    public void removeEventListener(IEventListener<EventDescription> listener) {
        worker.removeEventListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.notice.INoticeApi#cleanup()
     */
    @Override
    public void cleanup() {
        worker.cleanup();
        worker.setStop(true);
    }

    protected EventDescription asEventDescription(NoticeWrapper noticeWrapper) {
        NoticeBean bean = noticeWrapper.getNotice();
        EventDescription res = new EventDescription();
        res.setId(bean.getId());
        res.setDate(new Date(bean.getReceiveTimestamp()));
        res.setRelatedNodes(noticeWrapper.getRelatedNodes());
        GenericNoticeAttrBean attr =
                (GenericNoticeAttrBean) bean.getAttributes();
        TrapType trap = TrapType.getTrapType(attr.getTrapNumber());
        IEventSource source = null;

        try {

            EventType eventType = EventType.getEventType(trap);
            res.setType(eventType);

            // Override default severity
            res.setSeverity(eventSeverityMap.get(eventType));

            switch (trap) {
                case SM_CONNECTION_LOST:
                case SM_CONNECTION_ESTABLISH:
                    source = getSMSource(bean);
                    break;
                case FE_CONNECTION_LOST:
                case FE_CONNECTION_ESTABLISH:
                    source = getFESource(bean);
                    break;
                case GID_NOW_IN_SERVICE:
                case GID_OUT_OF_SERVICE:
                case ADD_MULTICAST_GROUP:
                case DEL_MULTICAST_GROUP:
                    source = getEndPortSource(bean);
                    break;
                case LINK_INTEGRITY:
                case BUFFER_OVERRUN:
                case FLOW_WATCHDOG:
                    source = getPortSource(bean);
                    break;
                case LINK_PORT_CHANGE_STATE:
                case CHANGE_CAPABILITY:
                case BAD_M_KEY:
                case SMA_TRAP_LINK_WIDTH:
                case BAD_P_KEY:
                case BAD_Q_KEY:
                    int lid = TrapDetail.getLid(bean.getData());
                    source = getNodeSource(lid);
                    break;
                case SWITCH_BAD_PKEY:
                    TrapSwitchPKeyBean key =
                            TrapDetail.getTrapSwitchPKey(bean.getData());
                    source = getNodeSource(key.getLid1());
                    break;
                case CHANGE_SYSGUID:
                    TrapSysguidBean sysguid =
                            TrapDetail.getTrapSysguid(bean.getData());
                    source = getNodeSource(sysguid.getLid());
                    break;
                default:
                    source = getNodeSource(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            res.setSource(source);
        }
        return res;
    }

    protected NodeSource getSMSource(NoticeBean bean) throws Exception {
        int lid = bean.getIssuerLID();
        NodeRecordBean node = null;
        NodeCache nodeCache = cacheMgr.acquireNodeCache();
        node = nodeCache.getNode(lid);
        NodeType type = node.getNodeType();
        return new NodeSource(lid, node.getNodeDesc(), type);
    }

    protected NodeSource getNodeSource(int lid) throws Exception {
        NodeRecordBean node = null;
        NodeCache nodeCache = cacheMgr.acquireNodeCache();
        node = nodeCache.getNode(lid);
        NodeType type = node.getNodeType();
        return new NodeSource(lid, node.getNodeDesc(), type);
    }

    protected FESource getFESource(NoticeBean bean) {
        HostInfo hi = subnetContext.getSubnetDescription().getCurrentFE();
        return new FESource(hi.getHost(), hi.getPort());
    }

    protected PortSource getEndPortSource(NoticeBean bean) {
        NodeCache nodeCache = cacheMgr.acquireNodeCache();
        GIDBean gid = TrapDetail.getGID(bean.getData());
        NodeRecordBean node = null;
        try {
            // This node might be from database. We don't know if it's in fabric
            // or not.
            node = nodeCache.getNode(gid.getInterfaceID());
            NodeType type = node.getNodeType();

            return new PortSource(node.getLid(), node.getNodeDesc(), type, node
                    .getNodeInfo().getLocalPortNum());
        } catch (Exception e) {
            IllegalArgumentException iae =
                    new IllegalArgumentException("Invalid GID " + gid);
            iae.initCause(e);
            throw iae;
        }
    }

    protected PortSource getPortSource(NoticeBean bean) throws SubnetException,
            SubnetDataNotFoundException {
        NodeCache nodeCache = cacheMgr.acquireNodeCache();
        TrapLinkBean link = TrapDetail.getTrapLink(bean.getData());
        NodeRecordBean node = null;
        node = nodeCache.getNode(link.getLid());
        NodeType type = NodeType.getNodeType(node.getNodeInfo().getNodeType());
        return new PortSource(link.getLid(), node.getNodeDesc(), type,
                link.getPort());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.notice.INoticeApi#setUserSettings(com.intel.stl.api
     * .configuration.UserSettings)
     */
    @Override
    public void setUserSettings(UserSettings userSettings) {
        List<EventRule> eventRules = userSettings.getEventRules();
        if (eventRules != null) {
            for (EventRule er : eventRules) {
                eventSeverityMap.put(er.getEventType(), er.getEventSeverity());
            }
        }
    }

}
