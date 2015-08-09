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
 *  File Name: NoticeSaveTask.java
 *  
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.15.2.1  2015/05/06 19:26:32  jijunwan
 *  Archive Log:    fixed confusion method name issue found by FinfBug
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/05/01 21:37:41  jijunwan
 *  Archive Log:    fixed typo found by FindBug
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/04/06 21:14:13  fernande
 *  Archive Log:    Improving the handling of connection errors. When a connection error occurs, the request for PMConfig shouldn't wait but throw an exception, and this exception should not prevent the notices from being processed
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/04/06 11:04:54  jypak
 *  Archive Log:    Klockwork: Back End Critical Without Unit Test. Open issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/03/16 14:31:43  jijunwan
 *  Archive Log:    renamed DevieGroup to DefaultDeviceGroup because it's an enum of default DGs, plus we need to use DeviceGroup for the DG definition used in opafm.xml
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/02/23 22:40:40  jijunwan
 *  Archive Log:    PR 126610 - Subnet summary under Home page does not show correct data after port disable/enable event
 *  Archive Log:     - changed to user trap data to identify source node
 *  Archive Log:     - improved to distinguish active and inactive nodes/links
 *  Archive Log:     - improved to include related nodes before and after a notice
 *  Archive Log:     - improved to wait PM until it has updated data after a notice
 *  Archive Log:     - other improvement that update cache properly
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/02/06 20:44:02  jypak
 *  Archive Log:    Header comments fixed for archive log to be updated.
 *  Archive Log:
 *
 *  Overview: Process notices based on FM query. Update each caches and then
 *  update the notices in database according to the response for the process.
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.api.notice.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.notice.GenericNoticeAttrBean;
import com.intel.stl.api.notice.NoticeBean;
import com.intel.stl.api.notice.TrapType;
import com.intel.stl.api.performance.GroupInfoBean;
import com.intel.stl.api.performance.ImageIdBean;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.api.performance.impl.PAHelper;
import com.intel.stl.api.subnet.DefaultDeviceGroup;
import com.intel.stl.api.subnet.GIDBean;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.impl.SAHelper;
import com.intel.stl.configuration.AsyncTask;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.datamanager.DatabaseManager;
import com.intel.stl.datamanager.NoticeStatus;
import com.intel.stl.fecdriver.messages.adapter.sa.trap.TrapDetail;

public class NoticeProcessTask extends AsyncTask<Future<Boolean>> {
    private static Logger log = LoggerFactory
            .getLogger(NoticeProcessTask.class);

    private final DatabaseManager dbMgr;

    private final String subnetName;

    private final CacheManager cacheMgr;

    private final SAHelper helper;

    public NoticeProcessTask(String subnetName, DatabaseManager dbMgr,
            CacheManager cacheMgr) {
        this.dbMgr = dbMgr;
        this.subnetName = subnetName;
        this.cacheMgr = cacheMgr;
        this.helper = cacheMgr.getSAHelper();
    }

    /**
     * process the list of notices. The thread pool size is 2, so, two
     * NoticeProcessingService can be processing at same time, so make this a
     * synchronized method.
     * 
     */
    @Override
    public Future<Boolean> process() throws Exception {
        // This is done to make sure that the subnet is defined in the database
        // and that a topology has been saved for it.
        dbMgr.getTopologyId(subnetName);

        log.info("Retrieving notices in the background for subnet: "
                + subnetName);

        // This atomically gets notices in RECEIVED status and change their
        // status to INFLIGHT
        List<NoticeBean> notices =
                dbMgr.getNotices(subnetName, NoticeStatus.RECEIVED,
                        NoticeStatus.INFLIGHT);
        if (notices.size() == 0) {
            log.info("No notices to process for subnet: " + subnetName);
            return null;
        }

        // PM doesn't update itself immediately after notices. It update in the
        // next sweep. To ensure we get correct data, we need wait here until
        // we are sure PM is updated, i.e. latest image number changed at least
        // once
        long t = System.currentTimeMillis();
        boolean success;
        try {
            // When there is a connection error, this will fail, causing the
            // notice not to be processed
            success = waitPM();
        } catch (Exception e) {
            success = false;
        }
        log.info("waited " + (System.currentTimeMillis() - t)
                + " ms for PM. State: success=" + success);

        // We pack as much work as possible using multiple threads; but first we
        // need to get information from the FM
        List<NoticeProcess> noticePrcs = createNoticeProcesses(notices);

        // Start the thread to update the database; no wait, just keep a hold of
        // the Future.
        Future<Boolean> result = dbMgr.processNotices(subnetName, noticePrcs);

        // And now let the CacheManager update its caches in parallel. Keep
        // in mind that nodes, links and ports are seen by two threads
        // concurrently, so in the database be careful to update only persisted
        // status fields (in TopologyNodeRecord and in TopologyLinkRecord),
        // never the objects themselves, which can potentially affect the caches
        // themselves.
        for (NoticeProcess noticeProcess : noticePrcs) {
            try {
                cacheMgr.updateCaches(noticeProcess);
                // Update the complete flag for each notice.
                if (noticeProcess.getNotice() != null) {
                    dbMgr.updateNotice(subnetName, noticeProcess.getNotice()
                            .getId(), NoticeStatus.PROCESSED);
                }
            } catch (Exception e) {
                log.error("Error while updating caches for notice "
                        + noticeProcess.getNotice().getId() + ": "
                        + noticeProcess.getNotice(), e);
                if (noticeProcess.getNotice() != null) {
                    dbMgr.updateNotice(subnetName, noticeProcess.getNotice()
                            .getId(), NoticeStatus.FEERROR);
                }
            }
        }
        log.info("Notices have been processed");
        return result;
    }

    private List<NoticeProcess> createNoticeProcesses(List<NoticeBean> notices) {
        List<NoticeProcess> noticePrcs = new ArrayList<NoticeProcess>();
        for (NoticeBean notice : notices) {
            TrapType trapType = getTrapType(notice);
            long guid = -1;
            int lid = -1;
            NodeRecordBean node = null;
            Set<Integer> relatedNodes = new HashSet<Integer>();
            try {
                switch (trapType) {
                    case GID_NOW_IN_SERVICE: {
                        GIDBean gid = TrapDetail.getGID(notice.getData());
                        guid = gid.getInterfaceID();
                        node = helper.getNode(guid);
                        if (node != null) {
                            lid = node.getLid();
                            List<LinkRecordBean> links = helper.getLinks(lid);
                            relatedNodes.addAll(getRelatedNodes(links));
                        }
                        break;
                    }
                    case GID_OUT_OF_SERVICE: {
                        GIDBean gid = TrapDetail.getGID(notice.getData());
                        guid = gid.getInterfaceID();
                        node = dbMgr.getNode(subnetName, guid);
                        if (node != null) {
                            lid = node.getLid();
                            LinkRecordBean link =
                                    dbMgr.getLinkBySource(subnetName, lid,
                                            (short) 1);
                            relatedNodes.add(link.getToLID());
                        }
                        break;
                    }
                    case LINK_PORT_CHANGE_STATE:
                        lid = TrapDetail.getLid(notice.getData());
                        node = helper.getNode(lid);
                        List<LinkRecordBean> links =
                                dbMgr.getLinks(subnetName, lid);
                        relatedNodes.addAll(getRelatedNodes(links));
                        links = helper.getLinks(lid);
                        relatedNodes.addAll(getRelatedNodes(links));
                        break;
                    default:
                        log.warn("Unsupported notice " + notice);
                        continue;
                }
                if (node == null) {
                    log.error("Node information not found in FM or DB for GUID="
                            + StringUtils.longHexString(guid)
                            + " or LID="
                            + lid + " mentioned in notice: " + notice);
                    dbMgr.updateNotice(subnetName, notice.getId(),
                            NoticeStatus.FEERROR);
                    continue;
                }

                NoticeProcess np = createNoticeProcess(notice, trapType, node);
                if (np != null) {
                    noticePrcs.add(np);
                }

                for (int relatedLid : relatedNodes) {
                    node = helper.getNode(relatedLid);
                    np =
                            createNoticeProcess(null,
                                    TrapType.LINK_PORT_CHANGE_STATE, node);
                    if (np != null) {
                        noticePrcs.add(np);
                    }
                }
            } catch (Exception e) {
                log.error("Error while processing notice " + notice.getId()
                        + ": " + notice, e);
                dbMgr.updateNotice(subnetName, notice.getId(),
                        NoticeStatus.FEERROR);
                continue;
            }
        }

        return noticePrcs;
    }

    protected Set<Integer> getRelatedNodes(List<LinkRecordBean> links) {
        Set<Integer> res = new HashSet<Integer>();
        for (LinkRecordBean link : links) {
            res.add(link.getToLID());
        }
        return res;
    }

    protected NoticeProcess createNoticeProcess(NoticeBean notice,
            TrapType trapType, NodeRecordBean node) throws Exception {
        NoticeProcess np = new NoticeProcess(notice);
        np.setTrapType(trapType);
        int lid = 0;
        if (node != null) {
            lid = node.getLid();
        } else {
            return null;
        }

        np.setLid(lid);
        np.setNode(node);
        // For the current three trap types we support we would need to
        // refresh port information; this might need to be fine tuned when other
        // traps are added
        np.setPorts(helper.getPorts(lid));
        np.setLinks(helper.getLinks(lid));
        return np;
    }

    private TrapType getTrapType(NoticeBean notice) {
        // We only get Generic now.
        // Retrieve Node LID based on the notice
        GenericNoticeAttrBean attr =
                (GenericNoticeAttrBean) notice.getAttributes();
        return TrapType.getTrapType(attr.getTrapNumber());
    }

    /**
     * 
     * <i>Description:</i> wait until PM get updated for the notices. In the
     * worst case we will need to wait for sweep time. Whether PM is updated is
     * judged by the change of image number
     * 
     */
    protected boolean waitPM() throws Exception {
        PAHelper helper = cacheMgr.getPAHelper();
        PMConfigBean conf = helper.getPMConfig();
        int sweep = 0;
        if (conf != null) {
            sweep = conf.getSweepInterval(); // in seconds
        }
        GroupInfoBean gi =
                helper.getGroupInfo(DefaultDeviceGroup.ALL.getName());
        ImageIdBean image = null;
        if (gi != null) {
            image = gi.getImageId();
        }
        long id = 0L;
        if (image != null) {
            id = image.getImageNumber();
        }
        int count = (int) (sweep * 1.1 / 0.2);
        long imageNumber = id;
        while (id == imageNumber && count > 0) {
            Thread.sleep(200);
            gi = helper.getGroupInfo(DefaultDeviceGroup.ALL.getName());
            if (gi != null) {
                image = gi.getImageId();
                if (image != null) {
                    imageNumber = image.getImageNumber();
                }
            }
            count -= 1;
        }
        return count > 0;
    }

}
