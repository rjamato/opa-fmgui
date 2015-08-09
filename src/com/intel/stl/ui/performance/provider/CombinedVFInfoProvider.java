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
 *  File Name: GroupInfoProvider.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;
import com.intel.stl.ui.publisher.subscriber.SubscriberType;
import com.intel.stl.ui.publisher.subscriber.VFInfoSubscriber;

public class CombinedVFInfoProvider extends CombinedDataProvider<VFInfoBean> {
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
    protected VFInfoBean[] refresh(String[] sourceNames) {
        VFInfoBean[] res = new VFInfoBean[sourceNames.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = scheduler.getPerformanceApi().getVFInfo(sourceNames[i]);
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
    protected List<Task<VFInfoBean>> registerTasks(String[] sourceNames,
            ICallback<VFInfoBean[]> callback) {
        if (DEBUG) {
            System.out.println(this + " registerTask "
                    + Arrays.toString(sourceNames) + " " + callback);
        }

        // Get the port counter subscriber from the task scheduler
        VFInfoSubscriber vfInfoSubscriber =
                (VFInfoSubscriber) scheduler
                        .getSubscriber(SubscriberType.VF_INFO);

        return vfInfoSubscriber.registerVFInfo(sourceNames, callback);
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
    protected Future<Void> initHistory(String[] sourceNames,
            ICallback<VFInfoBean[]> callback) {
        if (DEBUG) {
            System.out.println(this + " initHistory "
                    + Arrays.toString(sourceNames) + " " + callback);
        }

        VFInfoSubscriber vfInfoSubscriber =
                (VFInfoSubscriber) scheduler
                        .getSubscriber(SubscriberType.VF_INFO);
        return vfInfoSubscriber.initVFInfoHistory(sourceNames, historyType,
                callback);

    }

}
