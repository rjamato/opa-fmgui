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
 *  File Name: AbstractTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/08/17 18:53:39  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/04/09 22:53:04  jijunwan
 *  Archive Log:    improved BatchedCallback to support refreshing data
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/12 19:40:07  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/10 23:11:57  jijunwan
 *  Archive Log:    1) changed task scheduler to support initial refresh rate
 *  Archive Log:    2) improved refresh rate update handling
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
 *  Overview: Abstract class from which all subscribers are derived
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.publisher.subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import com.intel.stl.api.ITimestamped;
import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.ui.publisher.HistoryQueryTask;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;

public abstract class Subscriber<E> {

    protected List<Task<E>> taskList = new ArrayList<Task<E>>();

    protected final IRegisterTask taskScheduler;

    protected final IPerformanceApi perfApi;

    /**
     * Description:
     * 
     * @param taskScheduler
     * @param perfApi
     */
    public Subscriber(IRegisterTask taskScheduler, IPerformanceApi perfApi) {
        super();
        this.taskScheduler = taskScheduler;
        this.perfApi = perfApi;
    }

    public synchronized void cancelTask(Task<E> task) {
        if (task != null) {
            task.getFuture().cancel(true);
        }
    }

    public synchronized void cancelTasks() {
        for (Task<E> task : taskList) {
            if (task != null) {
                task.getFuture().cancel(true);
            }
        }

        taskList.clear();
    }

    public synchronized void rescheduleTasks() {
        List<Task<E>> newTaskList = new ArrayList<Task<E>>();
        for (Task<E> task : taskList) {
            if (task != null) {
                // remove callbacks first, so changes on this task have no
                // impact on UI
                List<ICallback<E>> callbacks = task.clearCallbacks();
                // it's safe to cancel now since callbacks are detached from the
                // task
                task.getFuture().cancel(true);
                // schedule tasks on taskScheduler's ExecutorService that should
                // be a new created one
                for (ICallback<E> callback : callbacks) {
                    callback.reset();
                    taskScheduler.scheduleTask(newTaskList, task, callback,
                            task.getCaller());
                }
            }
        }
        taskList = newTaskList;
    }

    protected <T extends ITimestamped> Future<Void> submitHistoryQueryTask(
            HistoryQueryTask<T> task) {
        Future<Void> future = taskScheduler.submitToBackground(task);
        task.setFuture(future);
        return future;
    }

    protected double getHistoryStep() {
        int refreshRate = taskScheduler.getRefreshRate();
        PMConfigBean conf = perfApi.getPMConfig();
        int sweepInterval = conf.getSweepInterval();
        double step = Math.max(1, (double) refreshRate / sweepInterval);
        return step;
    }

}
