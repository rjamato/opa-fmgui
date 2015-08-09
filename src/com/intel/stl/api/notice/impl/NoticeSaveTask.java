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
 *  Archive Log:    Revision 1.4  2015/02/06 20:44:02  jypak
 *  Archive Log:    Header comments fixed for archive log to be updated.
 *  Archive Log:
 *
 *  Overview: Save notices for a subnet from FE to database.  
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.api.notice.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.notice.NoticeBean;
import com.intel.stl.configuration.AsyncTask;
import com.intel.stl.datamanager.DatabaseManager;

public class NoticeSaveTask extends AsyncTask<Void> {
    private static Logger log = LoggerFactory.getLogger(NoticeSaveTask.class);

    private final DatabaseManager dbMgr;

    private final String subnetName;

    private final NoticeBean[] notices;

    public NoticeSaveTask(DatabaseManager dbMgr, String subnetName,
            NoticeBean[] noticeData) {
        this.dbMgr = dbMgr;
        this.subnetName = subnetName;
        this.notices = noticeData;
    }

    @Override
    public Void process() throws Exception {
        // System.out.println("Saving notice in the background for subnet: "
        // + subnetName);

        log.info("Saving notice in the background for subnet: " + subnetName);

        dbMgr.saveNotices(subnetName, notices);
        log.info("Notice has been saved");

        return null;
    }

}
