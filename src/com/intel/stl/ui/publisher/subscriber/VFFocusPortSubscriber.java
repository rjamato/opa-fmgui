/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: VFFocusPortSubscriber.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/02/12 19:40:07  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/06 20:49:34  jypak
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
 *  Overview: Subscriber class to schedule tasks for collecting virtual fabric
 *  focus port counter beans
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.publisher.subscriber;

import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.VFFocusPortsRspBean;
import com.intel.stl.api.subnet.Selection;
import com.intel.stl.ui.publisher.FocusPortsTask;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;

public class VFFocusPortSubscriber extends
        Subscriber<List<VFFocusPortsRspBean>> {

    private static Logger log = LoggerFactory
            .getLogger(VFFocusPortSubscriber.class);

    public VFFocusPortSubscriber(IRegisterTask taskScheduler,
            IPerformanceApi perfApi) {
        super(taskScheduler, perfApi);
    }

    public synchronized Task<List<VFFocusPortsRspBean>> registerVFFocusPorts(
            final String name, final Selection selection, final int range,
            ICallback<List<VFFocusPortsRspBean>> callback) {
        Task<List<VFFocusPortsRspBean>> task =
                new FocusPortsTask<List<VFFocusPortsRspBean>>(name, selection,
                        range, true);
        Callable<List<VFFocusPortsRspBean>> caller =
                new Callable<List<VFFocusPortsRspBean>>() {
                    @Override
                    public List<VFFocusPortsRspBean> call() throws Exception {
                        List<VFFocusPortsRspBean> ports =
                                perfApi.getVFFocusPorts(name, selection, range);
                        return ports;
                    }
                };
        try {
            Task<List<VFFocusPortsRspBean>> submittedTask =
                    taskScheduler
                            .scheduleTask(taskList, task, callback, caller);
            return submittedTask;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void deregisterVFFocusPorts(
            Task<List<VFFocusPortsRspBean>> task,
            ICallback<List<VFFocusPortsRspBean>> callback) {
        try {
            taskScheduler.removeTask(taskList, task, callback);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
