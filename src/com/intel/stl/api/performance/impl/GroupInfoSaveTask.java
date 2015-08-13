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
 *  File Name: GroupInfoSaveTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4.2.1  2015/08/12 15:22:12  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/18 20:47:56  fernande
 *  Archive Log:    Enabling GroupInfo saving after fixing issues in the application
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/15 22:01:58  fernande
 *  Archive Log:    Disabling save to database: foreign key not existing for special groups. Need to generate special records for those special groups at startup
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/15 21:18:09  fernande
 *  Archive Log:    Adding unit test for PerformanceApi and fixes for some issues found.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/12 19:56:07  fernande
 *  Archive Log:    We now save ImageInfo and GroupInfo to the database. As they are retrieved by the UI, they are buffered and then saved at certain thresholds.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.performance.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.intel.stl.api.performance.GroupInfoBean;
import com.intel.stl.configuration.AsyncTask;
import com.intel.stl.datamanager.DatabaseManager;

public class GroupInfoSaveTask extends AsyncTask<Void> {
    private final DatabaseManager dbMgr;

    private final PAHelper helper;

    private final ConcurrentLinkedQueue<GroupInfoBean> groupInfoBuffer;

    public GroupInfoSaveTask(PAHelper helper, DatabaseManager dbMgr,
            ConcurrentLinkedQueue<GroupInfoBean> groupInfoBuffer) {
        // Check for not null arguments (submitter should handle this rather
        // than the background task)
        checkArguments(helper, dbMgr, groupInfoBuffer);
        this.helper = helper;
        this.dbMgr = dbMgr;
        this.groupInfoBuffer = groupInfoBuffer;
    }

    @Override
    public Void process() throws Exception {
        String subnetName = helper.getSubnetDescription().getName();
        List<GroupInfoBean> saveList =
                new ArrayList<GroupInfoBean>(groupInfoBuffer.size());
        while (!groupInfoBuffer.isEmpty()) {
            GroupInfoBean groupInfo = groupInfoBuffer.poll();
            if (groupInfo != null) {
                saveList.add(groupInfo);
            }
        }
        if (saveList.size() > 0) {
            dbMgr.saveGroupInfos(subnetName, saveList);
        }
        return null;
    }

}
