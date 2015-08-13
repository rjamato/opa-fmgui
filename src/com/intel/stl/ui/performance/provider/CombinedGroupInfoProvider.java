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
 *  File Name: GroupInfoProvider.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10.2.1  2015/08/12 15:27:14  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/12 21:20:26  jijunwan
 *  Archive Log:    turn off debug
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/12 19:40:11  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/10 23:25:34  jijunwan
 *  Archive Log:    removed refresh rate on caller side since we should be able to directly get it from task scheduler
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/06 20:49:33  jypak
 *  Archive Log:    1. TaskScheduler changed to handle two threads.
 *  Archive Log:    2. All four(VFInfo, VFPortCounters, GroupInfo, PortCounters) attributes history query related updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/03 21:12:34  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/02 15:38:26  rjtierne
 *  Archive Log:    New TaskScheduler architecture; now employs subscribers to submit
 *  Archive Log:    tasks for scheduling.  When update rate is changed on Wizard, TaskScheduler
 *  Archive Log:    uses this new architecture to terminate tasks and service and restart them.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/21 21:19:10  rjtierne
 *  Archive Log:    Removed individual refresh rates for task registration. Now using
 *  Archive Log:    refresh rate supplied by user input in preferences wizard.
 *  Archive Log:    Reinitialization of scheduler service not yet implemented.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/26 15:14:31  jijunwan
 *  Archive Log:    added refresh function to performance charts
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/17 16:25:37  jijunwan
 *  Archive Log:    improvement to support sleep mode so we can reduce FE traffic
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 15:08:58  jijunwan
 *  Archive Log:    new framework for performance data visualization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance.provider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import com.intel.stl.api.performance.GroupInfoBean;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;
import com.intel.stl.ui.publisher.subscriber.GroupInfoSubscriber;
import com.intel.stl.ui.publisher.subscriber.SubscriberType;

public class CombinedGroupInfoProvider extends
        CombinedDataProvider<GroupInfoBean> {

    private final static boolean DEBUG = false;

    /**
     * Description:
     * 
     * @param sourceNames
     */
    public CombinedGroupInfoProvider() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.CombinedDataProvider#refresh(java
     * .lang.String[])
     */
    @Override
    protected GroupInfoBean[] refresh(String[] sourceNames) {
        GroupInfoBean[] res = new GroupInfoBean[sourceNames.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = scheduler.getPerformanceApi().getGroupInfo(sourceNames[i]);
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.performance.CombinedDataProvider#registerTask
     * (int, com.intel.stl.ui.publisher.ICallback)
     */
    @Override
    protected List<Task<GroupInfoBean>> registerTasks(String[] sourceNames,
            ICallback<GroupInfoBean[]> callback) {
        if (DEBUG) {
            System.out.println(this + " registerTask "
                    + Arrays.toString(sourceNames) + " " + callback);
        }
        // Get the group info subscriber from the task scheduler
        GroupInfoSubscriber groupInfoSubscriber =
                (GroupInfoSubscriber) scheduler
                        .getSubscriber(SubscriberType.GROUP_INFO);
        return groupInfoSubscriber.registerGroupInfo(sourceNames, callback);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.performance.CombinedDataProvider#deregisterTask
     * (java.util.List, com.intel.stl.ui.publisher.ICallback)
     */
    @Override
    protected void deregisterTasks(List<Task<GroupInfoBean>> task,
            ICallback<GroupInfoBean[]> callback) {
        if (DEBUG) {
            System.out.println(this + " deregisterTask " + task + " "
                    + callback);
        }
        // Get the group info subscriber from the task scheduler
        GroupInfoSubscriber groupInfoSubscriber =
                (GroupInfoSubscriber) scheduler
                        .getSubscriber(SubscriberType.GROUP_INFO);
        groupInfoSubscriber.deregisterGroupInfo(task, callback);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.CombinedDataProvider#initHistory
     * (java.lang.String[], com.intel.stl.ui.publisher.ICallback)
     */
    @Override
    protected Future<Void> initHistory(String[] sourceNames,
            ICallback<GroupInfoBean[]> callback) {
        if (DEBUG) {
            System.out.println(this + " initHistory "
                    + Arrays.toString(sourceNames) + " " + callback);
        }

        // Get the group info subscriber from the task scheduler
        GroupInfoSubscriber groupInfoSubscriber =
                (GroupInfoSubscriber) scheduler
                        .getSubscriber(SubscriberType.GROUP_INFO);
        return groupInfoSubscriber.initGroupInfoHistory(sourceNames,
                historyType, callback);
    }
}
