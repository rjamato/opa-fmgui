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
 *  File Name: GroupInfoSubscriber.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.12  2016/02/09 20:23:09  jijunwan
 *  Archive Log:    PR 132575 - [PSC] Null pointer message in FM GUI
 *  Archive Log:
 *  Archive Log:    - some minor improvements
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/08/17 18:53:39  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/07/31 21:11:19  fernande
 *  Archive Log:    PR 129631 - Ports table sometimes not getting performance related data. Improved logging.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/04/13 22:11:55  jijunwan
 *  Archive Log:    fixed history imageNum issue
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/04/10 14:18:02  jypak
 *  Archive Log:    Use image ID of history data queried with offset -1 rather than current image ID. Using current image ID for history query doesn't work.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/03/02 15:28:08  jypak
 *  Archive Log:    History query has been done with current live image ID '0' which isn't correct. Updates here are:
 *  Archive Log:    1. Get the image ID from current image.
 *  Archive Log:    2. History queries are done with this image ID.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/12 19:40:07  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/10 23:27:45  jijunwan
 *  Archive Log:    minor improvement
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/10 21:26:00  jypak
 *  Archive Log:    1. Introduced SwingWorker for history query initialization for progress status updates.
 *  Archive Log:    2. Fixed the list of future for history query in TaskScheduler. Now it can have all the Future entries created.
 *  Archive Log:    3. When selecting history type, just cancel the history query not sheduled query.
 *  Archive Log:    4. The refresh rate is now from user settings not from the config api.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/06 20:49:35  jypak
 *  Archive Log:    1. TaskScheduler changed to handle two threads.
 *  Archive Log:    2. All four(VFInfo, VFPortCounters, GroupInfo, PortCounters) attributes history query related updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/03 21:12:32  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/02 15:36:15  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Subscriber class to schedule tasks for collecting group info beans
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.publisher.subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.performance.GroupInfoBean;
import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.ImageIdBean;
import com.intel.stl.api.performance.PAConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.publisher.BatchedCallback;
import com.intel.stl.ui.publisher.HistoryQueryTask;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;

public class GroupInfoSubscriber extends Subscriber<GroupInfoBean> {
    private final static boolean DUMP_DATA = false;

    private static Logger log =
            LoggerFactory.getLogger(GroupInfoSubscriber.class);

    public GroupInfoSubscriber(IRegisterTask taskScheduler,
            IPerformanceApi perfApi) {
        super(taskScheduler, perfApi);
    }

    public synchronized Task<GroupInfoBean> registerGroupInfo(
            final String group, ICallback<GroupInfoBean> callback) {
        Task<GroupInfoBean> task = new Task<GroupInfoBean>(
                PAConstants.STL_PA_ATTRID_GET_GRP_INFO, group,
                UILabels.STL40008_GROUPINFO_TASK.getDescription(group));
        Callable<GroupInfoBean> caller = new Callable<GroupInfoBean>() {
            @Override
            public GroupInfoBean call() throws Exception {
                GroupInfoBean groupInfo = perfApi.getGroupInfo(group);
                if (DUMP_DATA) {
                    System.out.println(group + " " + groupInfo);
                }
                return groupInfo;
            }
        };
        try {
            // Task<GroupInfoBean> res =
            // registerTask(groupInfoTasks, task, callback, caller);
            Task<GroupInfoBean> submittedTask = taskScheduler
                    .scheduleTask(taskList, task, callback, caller);

            return submittedTask;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public synchronized void deregisterGroupInfo(Task<GroupInfoBean> task,
            ICallback<GroupInfoBean> callback) {
        try {
            taskScheduler.removeTask(taskList, task, callback);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public synchronized List<Task<GroupInfoBean>> registerGroupInfo(
            final String[] groups, ICallback<GroupInfoBean[]> callback) {
        List<Task<GroupInfoBean>> tasks = new ArrayList<Task<GroupInfoBean>>();
        BatchedCallback<GroupInfoBean> bCallback =
                new BatchedCallback<GroupInfoBean>(groups.length, callback,
                        GroupInfoBean.class);
        for (int i = 0; i < groups.length; i++) {
            Task<GroupInfoBean> task =
                    registerGroupInfo(groups[i], bCallback.getCallback(i));
            tasks.add(task);
        }
        return tasks;
    }

    public synchronized void deregisterGroupInfo(
            List<Task<GroupInfoBean>> tasks,
            ICallback<GroupInfoBean[]> callbacks) {
        try {
            taskScheduler.removeTask(taskList, tasks, callbacks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public Future<Void> initGroupInfoHistory(final String[] groups,
            HistoryType type, ICallback<GroupInfoBean[]> callback) {
        double refreshImageOffset = getHistoryStep();
        HistoryQueryTask<GroupInfoBean> historyQueryTask =
                new HistoryQueryTask<GroupInfoBean>(refreshImageOffset,
                        taskScheduler.getRefreshRate(), type, callback) {

                    @Override
                    protected GroupInfoBean[] queryHistory(long[] imageIDs,
                            int offset) {
                        GroupInfoBean[] res = new GroupInfoBean[groups.length];
                        for (int i = 0; i < groups.length; i++) {
                            GroupInfoBean gib = perfApi.getGroupInfoHistory(
                                    groups[i], imageIDs[i], offset);
                            if (gib != null) {
                                res[i] = gib;
                                if (DUMP_DATA) {
                                    System.out.println(imageIDs[i] + " "
                                            + offset + " " + groups[i] + " "
                                            + gib.getTimestamp() + " "
                                            + gib.getTimestampDate() + gib);
                                }
                            } else {
                                return null;
                            }
                        }
                        return res;
                    }

                    @Override
                    protected ImageIdBean[] queryImageId() {
                        ImageIdBean[] imageIdBeans =
                                new ImageIdBean[groups.length];
                        for (int i = 0; i < groups.length; i++) {
                            GroupInfoBean groupInfoBean = perfApi
                                    .getGroupInfoHistory(groups[i], 0L, -2);
                            ImageIdBean imageIdBean =
                                    groupInfoBean.getImageId();

                            imageIdBeans[i] = imageIdBean;
                        }

                        return imageIdBeans;
                    }
                };
        return submitHistoryQueryTask(historyQueryTask);
    }
}
