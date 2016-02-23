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
 *  File Name: NotieSimulator.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/08/17 18:49:13  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/07/29 16:43:42  fernande
 *  Archive Log:    PR 129631 - Ports table sometimes not getting performance related data . Fixed simulation of notices to overload application and attempt to reproduce the issue.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/23 22:22:23  jijunwan
 *  Archive Log:    improved to include/exclude inactive nodes/links in query
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/03 04:51:18  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/11 23:09:29  jijunwan
 *  Archive Log:    renamed PortUtils to Utils
 *  Archive Log:    moved convertFromUnixTime to Utils
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/12/11 18:33:07  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/14 11:26:48  jypak
 *  Archive Log:    UI related updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/26 14:57:56  jijunwan
 *  Archive Log:    fixed a typo
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/09/05 15:33:16  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/08/28 14:56:53  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/08/22 21:38:10  fernande
 *  Archive Log:    Adding support for saving notices and imageinfos to the database
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/08/19 18:09:52  jijunwan
 *  Archive Log:    Introduced EventDispatcher to process notices in a separate thread, so we do not block on FE connection
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/08/05 18:35:23  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/03 21:36:57  fernande
 *  Archive Log:    Adding the CacheManager in support of APIs
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/19 20:02:16  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/05 18:31:18  jijunwan
 *  Archive Log:    rename NodeType.CHANNEL_ADAPTER to NodeType.FI
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/30 21:50:17  jijunwan
 *  Archive Log:    moved all random generation to API side, and added a method to allow us turn on/off randomization
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/08 18:59:20  jijunwan
 *  Archive Log:    added Notice Simulator to simulate notices from FM
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.notice.impl;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.notice.GenericNoticeAttrBean;
import com.intel.stl.api.notice.IEventListener;
import com.intel.stl.api.notice.NoticeBean;
import com.intel.stl.api.notice.NoticeType;
import com.intel.stl.api.notice.ProducerType;
import com.intel.stl.api.notice.TrapType;
import com.intel.stl.api.subnet.GIDGlobal;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.impl.NodeCache;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.fecdriver.messages.adapter.sa.GID;

public class NoticeSimulator {
    private static Logger log = LoggerFactory.getLogger(NoticeSimulator.class);

    private static final String SIM_THREAD_PREFIX = "simthread-";

    private List<NodeRecordBean> nodes;

    private final List<IEventListener<NoticeBean>> listeners =
            new CopyOnWriteArrayList<IEventListener<NoticeBean>>();

    private final CacheManager cacheMgr;

    private Thread worker;

    private boolean stop;

    private final Random random = new Random();

    private final int minSleepTime = 10000; // 1 sec

    private final INoticeCreator hfiActiveCreator = new INoticeCreator() {

        @Override
        public NoticeBean createNotice(NodeRecordBean node) {
            NoticeBean res = new NoticeBean(true);
            GenericNoticeAttrBean attr = new GenericNoticeAttrBean();
            attr.setGeneric(true);
            attr.setType(NoticeType.INFO.getId());
            attr.setProducerType(ProducerType.CA.getId());
            attr.setTrapNumber(TrapType.GID_NOW_IN_SERVICE.getId());
            res.setAttributes(attr);
            res.setIssuerLID(node.getLid());
            res.setIssuerGID(new GIDGlobal());
            GID.Global gid = new GID.Global();
            gid.build(true);
            gid.setInterfaceId(node.getNodeInfo().getPortGUID());
            res.setData(gid.getByteBuffer().array());
            return res;
        }

    };

    private final INoticeCreator hfiInactiveCreator = new INoticeCreator() {

        @Override
        public NoticeBean createNotice(NodeRecordBean node) {
            NoticeBean res = new NoticeBean(true);
            GenericNoticeAttrBean attr = new GenericNoticeAttrBean();
            attr.setGeneric(true);
            attr.setType(NoticeType.FATAL.getId());
            attr.setProducerType(ProducerType.CA.getId());
            attr.setTrapNumber(TrapType.GID_OUT_OF_SERVICE.getId());
            res.setAttributes(attr);
            res.setIssuerLID(node.getLid());
            res.setIssuerGID(new GIDGlobal());
            GID.Global gid = new GID.Global();
            gid.build(true);
            gid.setInterfaceId(node.getNodeInfo().getPortGUID());
            res.setData(gid.getByteBuffer().array());
            return res;
        }

    };

    private final INoticeCreator linkChangeCreator = new INoticeCreator() {

        @Override
        public NoticeBean createNotice(NodeRecordBean node) {
            NoticeBean res = new NoticeBean(true);
            GenericNoticeAttrBean attr = new GenericNoticeAttrBean();
            attr.setGeneric(true);
            attr.setType(NoticeType.URGENT.getId());
            attr.setProducerType(ProducerType.SWITCH.getId());
            attr.setTrapNumber(TrapType.LINK_PORT_CHANGE_STATE.getId());
            res.setAttributes(attr);
            res.setIssuerLID(node.getLid());
            res.setIssuerGID(new GIDGlobal());
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(node.getLid());
            res.setData(buffer.array());
            return res;
        }

    };

    /**
     * Description:
     * 
     * @param subnetApi
     */
    public NoticeSimulator(CacheManager cacheMgr) {
        super();
        this.cacheMgr = cacheMgr;
    }

    /**
     * Description:
     * 
     * @param seed
     */
    public void setSeed(long seed) {
        random.setSeed(seed);
    }

    public synchronized void run() {
        if (worker != null && !stop) {
            return;
        }

        stop = false;
        worker = new Thread(new Runnable() {
            @Override
            public void run() {
                simulate();
            }
        });
        SubnetDescription subnet =
                cacheMgr.getSAHelper().getSubnetDescription();
        worker.setName(SIM_THREAD_PREFIX + subnet.getSubnetId());
        worker.start();
    }

    public synchronized void stop() {
        stop = true;
    }

    protected void simulate() {
        while (!stop) {
            long sleepTime = (long) (random.nextDouble() * 2000) + minSleepTime;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
            if (nodes == null) {
                NodeCache nodeCache = cacheMgr.acquireNodeCache();
                try {
                    nodes = nodeCache.getNodes(false);
                } catch (SubnetDataNotFoundException e) {
                }
            }
            if (nodes != null) {
                NoticeBean[] notices = new NoticeBean[10];
                for (int i = 0; i < notices.length; i++) {
                    notices[i] = createNotice();
                }
                String subnetName =
                        cacheMgr.getSAHelper().getSubnetDescription().getName();
                cacheMgr.getDatabaseManager().saveNotices(subnetName, notices);
                fireNotice(notices);
            }
        }
    }

    protected NoticeBean createNotice() {
        int nodeIndex = random.nextInt(nodes.size());
        NodeRecordBean node = nodes.get(nodeIndex);
        NoticeBean notice = null;
        if (node.getNodeInfo().getNodeType() == NodeType.HFI.getId()) {
            if (random.nextBoolean()) {
                notice = hfiActiveCreator.createNotice(node);
            } else {
                notice = hfiInactiveCreator.createNotice(node);
            }
        } else {
            notice = linkChangeCreator.createNotice(node);
        }
        return notice;
    }

    protected void fireNotice(NoticeBean[] notices) {
        for (IEventListener<NoticeBean> listener : listeners) {
            listener.onNewEvent(notices);
        }
    }

    public void addEventListener(IEventListener<NoticeBean> listener) {
        listeners.add(listener);
    }

    public void removeEventListener(IEventListener<NoticeBean> listener) {
        listeners.remove(listener);
    }

    private interface INoticeCreator {
        NoticeBean createNotice(NodeRecordBean node);
    }

}
