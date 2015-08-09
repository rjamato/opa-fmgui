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
 *  File Name: GroupInfoSaveTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
