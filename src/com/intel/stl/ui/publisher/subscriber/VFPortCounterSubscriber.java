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
 *  File Name: VFPortCounterSubscriber.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/08/17 18:53:39  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/04/14 14:42:49  jypak
 *  Archive Log:    Fix to avoid MAD request error for history query.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/04/10 14:18:01  jypak
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
 *  Archive Log:    Revision 1.6  2015/02/10 23:25:36  jijunwan
 *  Archive Log:    removed refresh rate on caller side since we should be able to directly get it from task scheduler
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
 *  Overview: Subscriber class to schedule tasks for collecting virtual fabric
 *  port counter beans
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

import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.ImageIdBean;
import com.intel.stl.api.performance.PAConstants;
import com.intel.stl.api.performance.VFPortCountersBean;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.publisher.BatchedCallback;
import com.intel.stl.ui.publisher.CallbackAdapter;
import com.intel.stl.ui.publisher.HistoryQueryTask;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;

public class VFPortCounterSubscriber extends Subscriber<VFPortCountersBean> {

    private static Logger log = LoggerFactory
            .getLogger(PortCounterSubscriber.class);

    public VFPortCounterSubscriber(IRegisterTask taskScheduler,
            IPerformanceApi perfApi) {
        super(taskScheduler, perfApi);
    }

    public synchronized Task<VFPortCountersBean> registerVFPortCounters(
            final String vfName, final int lid, final short portNum,
            ICallback<VFPortCountersBean> callback) {
        Task<VFPortCountersBean> task =
                new Task<VFPortCountersBean>(
                        PAConstants.STL_PA_ATTRID_GET_VF_PORT_CTRS, vfName
                                + ":" + lid + ":" + portNum,
                        UILabels.STL40011_VFPORTCOUNTERS_TASK.getDescription(
                                lid, portNum));
        Callable<VFPortCountersBean> caller =
                new Callable<VFPortCountersBean>() {
                    @Override
                    public VFPortCountersBean call() throws Exception {
                        VFPortCountersBean portCounters =
                                perfApi.getVFPortCounters(vfName, lid, portNum);
                        return portCounters;
                    }
                };
        try {
            Task<VFPortCountersBean> submittedTask =
                    taskScheduler
                            .scheduleTask(taskList, task, callback, caller);

            return submittedTask;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void deregisterVFPortCounters(
            Task<VFPortCountersBean> task,
            ICallback<VFPortCountersBean> callback) {
        try {
            taskScheduler.removeTask(taskList, task, callback);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized List<Task<VFPortCountersBean>> registerVFPortCounters(
            String vfName, int[] lids, short[] portNums,
            ICallback<VFPortCountersBean[]> callback) {
        List<Task<VFPortCountersBean>> tasks =
                new ArrayList<Task<VFPortCountersBean>>();
        int size = lids.length;
        BatchedCallback<VFPortCountersBean> bCallback =
                new BatchedCallback<VFPortCountersBean>(size, callback,
                        VFPortCountersBean.class);
        for (int i = 0; i < size; i++) {
            Task<VFPortCountersBean> task =
                    registerVFPortCounters(vfName, lids[i], portNums[i],
                            bCallback.getCallback(i));
            tasks.add(task);
        }
        return tasks;
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
    public synchronized List<Task<VFPortCountersBean>> registerVFPortCounters(
            String vfName, int lid, List<Short> portNumList,
            ICallback<VFPortCountersBean[]> callback) {
        List<Task<VFPortCountersBean>> tasks =
                new ArrayList<Task<VFPortCountersBean>>();
        BatchedCallback<VFPortCountersBean> bCallback =
                new BatchedCallback<VFPortCountersBean>(portNumList.size(),
                        callback, VFPortCountersBean.class);
        for (int i = 0; i < portNumList.size(); i++) {
            Task<VFPortCountersBean> task =
                    registerVFPortCounters(vfName, lid, portNumList.get(i),
                            bCallback.getCallback(i));
            tasks.add(task);
        }
        return tasks;
    }

    public synchronized void deregisterVFPortCounters(
            List<Task<VFPortCountersBean>> tasks,
            ICallback<VFPortCountersBean[]> callbacks) {
        try {
            taskScheduler.removeTask(taskList, tasks, callbacks);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public Future<Void> initVFPortCountersHistory(final String vfName,
            final int lid, final short portNum, HistoryType type,
            final ICallback<VFPortCountersBean> callback) {
        double refreshImageOffset = getHistoryStep();
        HistoryQueryTask<VFPortCountersBean> historyQueryTask =
                new HistoryQueryTask<VFPortCountersBean>(refreshImageOffset,
                        taskScheduler.getRefreshRate(), type,
                        CallbackAdapter.asArrayCallbak(callback)) {

                    @Override
                    protected VFPortCountersBean[] queryHistory(
                            long[] imageIDs, int offset) {
                        VFPortCountersBean portCounters =
                                perfApi.getVFPortCountersHistory(vfName, lid,
                                        portNum, imageIDs[0], offset);
                        return new VFPortCountersBean[] { portCounters };
                    }

                    @Override
                    protected ImageIdBean[] queryImageId() {
                        VFPortCountersBean portCountersBean =
                                perfApi.getVFPortCountersHistory(vfName, lid,
                                        portNum, 0L, -2);
                        ImageIdBean imageIdBean = portCountersBean.getImageId();

                        return new ImageIdBean[] { imageIdBean };

                    }

                };
        return submitHistoryQueryTask(historyQueryTask);
    }

    public Future<Void> initVFPortCountersHistory(final String vfName,
            final int lid, final List<Short> portNumList, HistoryType type,
            final ICallback<VFPortCountersBean[]> callback) {
        double refreshImageOffset = getHistoryStep();
        HistoryQueryTask<VFPortCountersBean> historyQueryTask =
                new HistoryQueryTask<VFPortCountersBean>(refreshImageOffset,
                        taskScheduler.getRefreshRate(), type, callback) {

                    @Override
                    protected VFPortCountersBean[] queryHistory(
                            long[] imageIDs, int offset) {
                        VFPortCountersBean[] res =
                                new VFPortCountersBean[portNumList.size()];
                        for (int i = 0; i < portNumList.size(); i++) {
                            VFPortCountersBean portCounters =
                                    perfApi.getVFPortCountersHistory(vfName,
                                            lid, portNumList.get(i),
                                            imageIDs[i], offset);
                            if (portCounters != null) {
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
                            VFPortCountersBean portCountersBean =
                                    perfApi.getVFPortCountersHistory(vfName,
                                            lid, portNumList.get(i), 0L, -2);
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
