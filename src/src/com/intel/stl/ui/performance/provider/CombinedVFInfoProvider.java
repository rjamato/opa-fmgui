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
 *  Archive Log:    Revision 1.11  2015/08/17 18:54:06  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/06/25 20:42:14  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - improved PerformanceItem to support port counters
 *  Archive Log:    - improved PerformanceItem to use generic ISource to describe data source
 *  Archive Log:    - improved PerformanceItem to use enum DataProviderName to describe data provider name
 *  Archive Log:    - improved PerformanceItem to support creating a copy of PerformanceItem
 *  Archive Log:    - improved TrendItem to share scale with other charts
 *  Archive Log:    - improved SimpleDataProvider to support hsitory data
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

import com.intel.stl.api.performance.VFInfoBean;
import com.intel.stl.ui.performance.GroupSource;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;
import com.intel.stl.ui.publisher.subscriber.SubscriberType;
import com.intel.stl.ui.publisher.subscriber.VFInfoSubscriber;

public class CombinedVFInfoProvider extends
        CombinedDataProvider<VFInfoBean, GroupSource> {
    private final static boolean DEBUG = false;

    /**
     * Description:
     * 
     * @param sourceNames
     */
    public CombinedVFInfoProvider() {
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
    protected VFInfoBean[] refresh(GroupSource[] sourceNames) {
        VFInfoBean[] res = new VFInfoBean[sourceNames.length];
        for (int i = 0; i < res.length; i++) {
            res[i] =
                    scheduler.getPerformanceApi().getVFInfo(
                            sourceNames[i].getGroup());
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
    protected List<Task<VFInfoBean>> registerTasks(GroupSource[] sourceNames,
            ICallback<VFInfoBean[]> callback) {
        if (DEBUG) {
            System.out.println(this + " registerTask "
                    + Arrays.toString(sourceNames) + " " + callback);
        }

        // Get the port counter subscriber from the task scheduler
        VFInfoSubscriber vfInfoSubscriber =
                (VFInfoSubscriber) scheduler
                        .getSubscriber(SubscriberType.VF_INFO);

        String[] groups = new String[sourceNames.length];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = sourceNames[i].getGroup();
        }
        return vfInfoSubscriber.registerVFInfo(groups, callback);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.performance.CombinedDataProvider#deregisterTask
     * (java.util.List, com.intel.stl.ui.publisher.ICallback)
     */
    @Override
    protected void deregisterTasks(List<Task<VFInfoBean>> task,
            ICallback<VFInfoBean[]> callback) {
        if (DEBUG) {
            System.out.println(this + " deregisterTask " + task + " "
                    + callback);
        }

        // Get the port counter subscriber from the task scheduler
        VFInfoSubscriber vfInfoSubscriber =
                (VFInfoSubscriber) scheduler
                        .getSubscriber(SubscriberType.VF_INFO);

        vfInfoSubscriber.deregisterVFInfo(task, callback);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.CombinedDataProvider#initHistory
     * (java.lang.String[], int, int, com.intel.stl.ui.publisher.ICallback)
     */
    @Override
    protected Future<Void> initHistory(GroupSource[] sourceNames,
            ICallback<VFInfoBean[]> callback) {
        if (DEBUG) {
            System.out.println(this + " initHistory "
                    + Arrays.toString(sourceNames) + " " + callback);
        }

        VFInfoSubscriber vfInfoSubscriber =
                (VFInfoSubscriber) scheduler
                        .getSubscriber(SubscriberType.VF_INFO);
        String[] groups = new String[sourceNames.length];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = sourceNames[i].getGroup();
        }
        return vfInfoSubscriber
                .initVFInfoHistory(groups, historyType, callback);

    }

}
