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
 *  File Name: PortCounterSubscriber.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10  2015/04/14 14:42:49  jypak
 *  Archive Log:    Fix to avoid MAD request error for history query.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/04/10 14:18:02  jypak
 *  Archive Log:    Use image ID of history data queried with offset -1 rather than current image ID. Using current image ID for history query doesn't work.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/03/02 15:28:08  jypak
 *  Archive Log:    History query has been done with current live image ID '0' which isn't correct. Updates here are:
 *  Archive Log:    1. Get the image ID from current image.
 *  Archive Log:    2. History queries are done with this image ID.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/12 19:40:07  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/10 23:29:32  jijunwan
 *  Archive Log:    1) removed taskList update. It's already updated by task scheduler. This will cause duplicate tasks.
 *  Archive Log:    2) user refresh rate from task scheduler
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/10 21:26:00  jypak
 *  Archive Log:    1. Introduced SwingWorker for history query initialization for progress status updates.
 *  Archive Log:    2. Fixed the list of future for history query in TaskScheduler. Now it can have all the Future entries created.
 *  Archive Log:    3. When selecting history type, just cancel the history query not sheduled query.
 *  Archive Log:    4. The refresh rate is now from user settings not from the config api.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/06 20:49:35  jypak
 *  Archive Log:    1. TaskScheduler changed to handle two threads.
 *  Archive Log:    2. All four(VFInfo, VFPortCounters, GroupInfo, PortCounters) attributes history query related updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/04 21:44:21  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/03 21:12:32  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/02 15:36:15  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Subscriber class to schedule tasks for collecting port counter beans
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.publisher.subscriber;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.ImageIdBean;
import com.intel.stl.api.performance.PAConstants;
import com.intel.stl.api.performance.PortCountersBean;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.publisher.BatchedCallback;
import com.intel.stl.ui.publisher.CallbackAdapter;
import com.intel.stl.ui.publisher.HistoryQueryTask;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;

public class PortCounterSubscriber extends Subscriber<PortCountersBean> {
    private static Logger log = LoggerFactory
            .getLogger(PortCounterSubscriber.class);

    private final static boolean DUMP_DATA = false;

    public PortCounterSubscriber(IRegisterTask taskScheduler,
            IPerformanceApi perfApi) {
        super(taskScheduler, perfApi);
    }

    /**
     * 
     * <i>Description: Register to receive updates of a single port counter </i>
     * 
     * @param lid
     *            local id for the port of interest
     * 
     * @param portNum
     *            port number
     * 
     * @param callback
     *            method to call once update is complete
     * 
     * @return submittedTask task submitted to the task scheduler for processing
     */
    public synchronized Task<PortCountersBean> registerPortCounters(
            final int lid, final short portNum,
            ICallback<PortCountersBean> callback) {
        Task<PortCountersBean> task =
                new Task<PortCountersBean>(
                        PAConstants.STL_PA_ATTRID_GET_PORT_CTRS, lid + ":"
                                + portNum,
                        UILabels.STL40010_PORTCOUNTERS_TASK.getDescription(lid,
                                portNum));
        Callable<PortCountersBean> caller = new Callable<PortCountersBean>() {
            @Override
            public PortCountersBean call() throws Exception {
                PortCountersBean portCounters =
                        perfApi.getPortCounters(lid, portNum);
                if (DUMP_DATA) {
                    System.out
                            .println(lid + ":" + portNum + " " + portCounters);
                }
                return portCounters;
            }
        };
        try {
            Task<PortCountersBean> submittedTask =
                    taskScheduler
                            .scheduleTask(taskList, task, callback, caller);
            return submittedTask;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     * <i>Description: De-register a task that receives updates of a single port
     * counter</i>
     * 
     * @param task
     *            task to be cancelled
     * 
     * @param callback
     *            callback to removed
     */
    public synchronized void deregisterPortCounters(
            Task<PortCountersBean> task, ICallback<PortCountersBean> callback) {

        try {
            taskScheduler.removeTask(taskList, task, callback);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 
     * <i>Description: Register to receive updates of an array of port counters
     * </i>
     * 
     * @param lids
     *            array of local ids for the port of interest
     * 
     * @param portNums
     *            array of port number
     * 
     * @param callbacks
     *            array of methods to call once update is complete
     * 
     * @return submittedTask task submitted to the task scheduler for processing
     */
    public synchronized List<Task<PortCountersBean>> registerPortCountersArray(
            int[] lids, short[] portNums,
            ICallback<PortCountersBean[]> callbacks) {

        int size = lids.length;
        BatchedCallback<PortCountersBean> bCallback =
                new BatchedCallback<PortCountersBean>(size, callbacks,
                        PortCountersBean.class);
        for (int i = 0; i < size; i++) {
            registerPortCounters(lids[i], portNums[i], bCallback.getCallback(i));
        }
        return taskList;
    }

    public synchronized void deregisterPortCountersArray(
            List<Task<PortCountersBean>> tasks,
            ICallback<PortCountersBean[]> callbacks) {
        try {
            taskScheduler.removeTask(taskList, tasks, callbacks);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 
     * Description: This method returns an array of PortCounterBeans for the
     * node specified by lid and the list ports.
     * 
     * @param lid
     *            - lid for a specific node
     * 
     * @param portNumList
     *            - list of port numbers associated with lid
     * 
     * @param rateInSeconds
     *            - rate at which to invoke the callback
     * 
     * @param callback
     *            - method to call
     * 
     * @return - array of PortCounterBeans associated with specified node
     */
    public synchronized List<Task<PortCountersBean>> registerPortCounters(
            int lid, List<Short> portNumList,
            ICallback<PortCountersBean[]> callback) {

        BatchedCallback<PortCountersBean> bCallback =
                new BatchedCallback<PortCountersBean>(portNumList.size(),
                        callback, PortCountersBean.class);

        log.info("portNumList.size() = " + portNumList.size());
        for (int i = 0; i < portNumList.size(); i++) {
            registerPortCounters(lid, portNumList.get(i),
                    bCallback.getCallback(i));
        }
        return taskList;
    }

    /**
     * 
     * Description: Initiate Port Counters history with an offset calculated
     * with FV refresh rate and sweep interval. Usually, the refresh rate is
     * higher than sweep interval,so, we just want to do a sampling within a
     * refresh. We would like to return each history for each offset and UI just
     * update data set for JfreeChart.
     * 
     * @param lid
     * @param portNum
     * @param maxDataPoints
     * @param refreshRate
     * @param callback
     */
    public Future<Void> initPortCountersHistory(final int lid,
            final short portNum, HistoryType type,
            final ICallback<PortCountersBean> callback) {
        double refreshImageOffset = getHistoryStep();
        HistoryQueryTask<PortCountersBean> historyQueryTask =
                new HistoryQueryTask<PortCountersBean>(refreshImageOffset,
                        taskScheduler.getRefreshRate(), type,
                        CallbackAdapter.asArrayCallbak(callback)) {

                    @Override
                    protected PortCountersBean[] queryHistory(long[] imageIDs,
                            int offset) {
                        PortCountersBean portCounters =
                                perfApi.getPortCountersHistory(lid, portNum,
                                        imageIDs[0], offset);
                        if (DUMP_DATA) {
                            System.out.println(imageIDs[0] + " " + offset + " "
                                    + lid + ":" + portNum
                                    + portCounters.getTimestampDate() + " "
                                    + portCounters);
                        }
                        return new PortCountersBean[] { portCounters };
                    }

                    @Override
                    protected ImageIdBean[] queryImageId() {
                        PortCountersBean portCountersBean =
                                perfApi.getPortCountersHistory(lid, portNum,
                                        0L, -2);
                        ImageIdBean imageIdBean = portCountersBean.getImageId();

                        return new ImageIdBean[] { imageIdBean };

                    }
                };
        return submitHistoryQueryTask(historyQueryTask);
    }

    public Future<Void> initPortCountersHistory(final int lid,
            final List<Short> portNumList, HistoryType type,
            final ICallback<PortCountersBean[]> callback) {
        double refreshImageOffset = getHistoryStep();
        HistoryQueryTask<PortCountersBean> historyQueryTask =
                new HistoryQueryTask<PortCountersBean>(refreshImageOffset,
                        taskScheduler.getRefreshRate(), type, callback) {

                    @Override
                    protected PortCountersBean[] queryHistory(long[] imageIDs,
                            int offset) {
                        PortCountersBean[] res =
                                new PortCountersBean[portNumList.size()];
                        for (int i = 0; i < portNumList.size(); i++) {
                            PortCountersBean portCounters =
                                    perfApi.getPortCountersHistory(lid,
                                            portNumList.get(i), imageIDs[i],
                                            offset);
                            if (portCounters != null) {
                                if (DUMP_DATA) {
                                    System.out.println(imageIDs[i] + " "
                                            + offset + " " + lid + ":"
                                            + portNumList.get(i)
                                            + portCounters.getTimestampDate()
                                            + " " + portCounters);
                                }
                                res[i] = portCounters;
                            } else {
                                return null;
                            }
                        }
                        return res;
                    }

                    @Override
                    protected ImageIdBean[] queryImageId() {
                        ImageIdBean[] imageIdBeans =
                                new ImageIdBean[portNumList.size()];
                        for (int i = 0; i < portNumList.size(); i++) {
                            PortCountersBean portCountersBean =
                                    perfApi.getPortCountersHistory(lid,
                                            portNumList.get(i), 0L, -2);
                            ImageIdBean imageIdBean =
                                    portCountersBean.getImageId();

                            imageIdBeans[i] = imageIdBean;
                        }

                        return imageIdBeans;
                    }
                };
        return submitHistoryQueryTask(historyQueryTask);
    }

}
