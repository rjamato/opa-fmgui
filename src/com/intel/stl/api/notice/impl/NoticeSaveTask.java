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
 *  File Name: NoticeSaveTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/09/26 06:17:56  jijunwan
 *  Archive Log:    130487 - FM GUI: Topology refresh required after enabling Fabric Simulator
 *  Archive Log:    - added more log info
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/08/17 18:49:13  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
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

        log.info("Saving " + notices.length
                + " notices in the background for subnet: " + subnetName);

        dbMgr.saveNotices(subnetName, notices);
        log.info("Notice has been saved");

        return null;
    }

}
