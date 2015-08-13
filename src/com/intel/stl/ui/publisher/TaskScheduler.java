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
 *  File Name: Scheduler.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.41.2.1  2015/08/12 15:26:59  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.41  2015/04/28 22:13:17  jijunwan
 *  Archive Log:    1) introduced component owner to Context, so when we have errors in data collection, preparation etc, we now there the error dialog should go
 *  Archive Log:    2) improved TaskScheduler to show error message on proper frame
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2015/04/06 21:21:41  fernande
 *  Archive Log:    Added logic to ignore RejectExecutedExceptions when the scheduler is shutdown. Also added thread names.
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2015/04/01 19:48:52  jijunwan
 *  Archive Log:    fixed a bug on task removing that only removes even tasks
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2015/03/26 12:04:55  jypak
 *  Archive Log:    Updates for passing correct refresh rate to task scheduler. When the subnet is connected, compare the sweep interval from the subnet and the refresh rate provided by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2015/02/12 19:40:04  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2015/02/10 23:11:54  jijunwan
 *  Archive Log:    1) changed task scheduler to support initial refresh rate
 *  Archive Log:    2) improved refresh rate update handling
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2015/02/10 21:25:59  jypak
 *  Archive Log:    1. Introduced SwingWorker for history query initialization for progress status updates.
 *  Archive Log:    2. Fixed the list of future for history query in TaskScheduler. Now it can have all the Future entries created.
 *  Archive Log:    3. When selecting history type, just cancel the history query not sheduled query.
 *  Archive Log:    4. The refresh rate is now from user settings not from the config api.
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2015/02/06 20:49:40  jypak
 *  Archive Log:    1. TaskScheduler changed to handle two threads.
 *  Archive Log:    2. All four(VFInfo, VFPortCounters, GroupInfo, PortCounters) attributes history query related updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/02/06 15:51:49  robertja
 *  Archive Log:    PR 126598 Add periodic refresh of Health Score, based on refresh rate, in the absence of event-driven updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2015/02/05 21:21:46  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/02/05 19:14:38  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/02/05 18:49:33  robertja
 *  Archive Log:    Clean up debug code.
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/02/05 18:18:12  robertja
 *  Archive Log:    Added support for refresh and initialization on a fabric with no problems (events).
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/02/03 21:12:35  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/02/02 15:38:29  rjtierne
 *  Archive Log:    New TaskScheduler architecture; now employs subscribers to submit
 *  Archive Log:    tasks for scheduling.  When update rate is changed on Wizard, TaskScheduler
 *  Archive Log:    uses this new architecture to terminate tasks and service and restart them.
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/01/21 21:19:12  rjtierne
 *  Archive Log:    Removed individual refresh rates for task registration. Now using
 *  Archive Log:    refresh rate supplied by user input in preferences wizard.
 *  Archive Log:    Reinitialization of scheduler service not yet implemented.
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/01/20 12:16:46  jypak
 *  Archive Log:    Short Term PA history changes.
 *  Archive Log:
 *  Archive Log:    1. Performance API updates for history queries.
 *  Archive Log:    2. PA Helper updates.
 *  Archive Log:    3. TaskScheduler expansion.
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2014/12/11 18:49:26  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2014/10/28 15:10:22  robertja
 *  Archive Log:    Change Home page and Performance page status panel updates from poll-driven to event-driven.
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2014/10/13 15:07:28  jijunwan
 *  Archive Log:    fixed synchronization issues on performance charts
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/09/12 20:10:22  fernande
 *  Archive Log:    Changed to use getLatestImageInfo, which caches ImageInfo for use by getGroupInfo
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/08/26 14:58:56  jijunwan
 *  Archive Log:    added couple helper methods
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/08/19 18:14:45  jijunwan
 *  Archive Log:    clear resource before shutdown
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/08/15 21:46:36  jijunwan
 *  Archive Log:    adapter to the new GroupConfig and FocusPorts data structures
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/08/12 21:05:06  jijunwan
 *  Archive Log:    1) added description to task
 *  Archive Log:    2) applied failure management to TaskScheduler
 *  Archive Log:    3) some code auto-reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/08/05 17:57:06  jijunwan
 *  Archive Log:    fixed issues on ConnectivityTable to update performance data properly
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/07/29 15:46:05  rjtierne
 *  Archive Log:    Scheduled periodic Connectivity table updates
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/07/16 15:05:33  jijunwan
 *  Archive Log:    fixed a bug on task registration
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/07/11 13:16:31  jypak
 *  Archive Log:    Added runtime, non runtime exceptions handler for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    As of now, all different exceptions are generally handled as 'Exception' but when we define how to handle differently for different exception, based on the error code, handler (catch block will be different). Also, we are thinking of centralized 'failure recovery' process to handle all exceptions in one place.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/06/23 04:55:30  jijunwan
 *  Archive Log:    synchronization improvement
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/06/19 20:14:44  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/06/05 17:17:19  jijunwan
 *  Archive Log:    added VF related tasks to TaskScheduler
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/05/29 14:17:55  jijunwan
 *  Archive Log:    added cleanup to new added task lists
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/05/23 19:17:51  rjtierne
 *  Archive Log:    Added method registerPortCounters() to return an array
 *  Archive Log:    of PortCountersBean associated with a single node and
 *  Archive Log:    its ports.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/05/19 22:08:53  jijunwan
 *  Archive Log:    moved filter from EventCalculator to StateSummary, so we can have better consistent result
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/16 15:43:59  jijunwan
 *  Archive Log:    added CustomStates to task scheduler
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/16 04:30:42  jijunwan
 *  Archive Log:    Added code to deregister from task scheduler; Added Page Listener to listen enter or exit a (sub)page
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/08 19:03:24  jijunwan
 *  Archive Log:    backend support for states based on notices
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/30 19:47:55  jijunwan
 *  Archive Log:    batched schedule to call multiple tasks and then process all results together
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/30 17:34:47  jijunwan
 *  Archive Log:    rename *CallBack to *Callback
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/30 17:29:23  jijunwan
 *  Archive Log:    rename ApiBroker to TaskScheduler; added pooled backgorund services to TaskScheduler so we can use it to run background tasks; improved to support schedule batched task and callbacks
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/04/20 03:16:38  jijunwan
 *  Archive Log:    added #clear to support switch between contexts
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/17 14:46:05  rjtierne
 *  Archive Log:    Commented out print statement
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/16 16:52:29  jijunwan
 *  Archive Log:    reference to new accessible Selection and PAConstant
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/16 16:20:49  jijunwan
 *  Archive Log:    minor refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/16 15:16:13  jijunwan
 *  Archive Log:    added ApiBroker to schedule a task to repeatedly get data from a FEC driver
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/
package com.intel.stl.ui.publisher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.FMException;
import com.intel.stl.api.failure.BaseFailureEvaluator;
import com.intel.stl.api.failure.BaseTaskFailure;
import com.intel.stl.api.failure.FailureManager;
import com.intel.stl.api.failure.FatalException;
import com.intel.stl.api.failure.IFailureManagement;
import com.intel.stl.api.failure.ITaskFailure;
import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.publisher.subscriber.FocusPortCounterSubscriber;
import com.intel.stl.ui.publisher.subscriber.GroupInfoSubscriber;
import com.intel.stl.ui.publisher.subscriber.IRegisterTask;
import com.intel.stl.ui.publisher.subscriber.ImageInfoSubscriber;
import com.intel.stl.ui.publisher.subscriber.PortCounterSubscriber;
import com.intel.stl.ui.publisher.subscriber.Subscriber;
import com.intel.stl.ui.publisher.subscriber.SubscriberType;
import com.intel.stl.ui.publisher.subscriber.VFFocusPortSubscriber;
import com.intel.stl.ui.publisher.subscriber.VFInfoSubscriber;
import com.intel.stl.ui.publisher.subscriber.VFPortCounterSubscriber;
import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;

public class TaskScheduler implements IRegisterTask {

    private static Logger log = LoggerFactory.getLogger(TaskScheduler.class);

    private final static long SHUTDOWN_TIME = 2000;

    private final static int POOL_SIZE = 2;

    private static final String TSB_THREAD_PREFIX = "tsbthread-";

    private static final String TSS_THREAD_PREFIX = "tssthread-";

    private final String name;

    private final Context context;

    private final IPerformanceApi perfApi;

    private ScheduledExecutorService scheduledService;

    private final ExecutorService backgroundService;

    private final IFailureManagement failureMgr;

    private final BaseFailureEvaluator failureEvaluator;

    private int refreshRate; // seconds

    private boolean shutdownInProgress = false;

    // List of external entities to be notified when the refresh rate changes.

    private final List<IRefreshRateListener> refreshRateChangeListeners =
            new ArrayList<IRefreshRateListener>();

    private final HashMap<SubscriberType, Subscriber<?>> subscriberPool =
            new HashMap<SubscriberType, Subscriber<?>>();

    public TaskScheduler(Context context, int refreshRate) {
        this(context, POOL_SIZE, refreshRate);
    }

    public TaskScheduler(Context context, int poolSize, int refreshRate) {
        this.context = context;
        this.perfApi = context.getPerformanceApi();
        this.name = context.getSubnetDescription().getName();
        this.refreshRate = refreshRate;

        ThreadFactory tssFactory = new ServiceThreadFactory(TSS_THREAD_PREFIX);
        scheduledService =
                Executors.newScheduledThreadPool(poolSize, tssFactory);
        ThreadFactory tsbFactory = new ServiceThreadFactory(TSB_THREAD_PREFIX);
        backgroundService =
                Executors.newFixedThreadPool(poolSize * 2, tsbFactory);

        failureEvaluator = new BaseFailureEvaluator();
        failureEvaluator.setRecoverableErrors(RuntimeException.class,
                TimeoutException.class);
        failureEvaluator.setUnrecoverableErrors(IOException.class);

        failureMgr = FailureManager.getManager();

        // Initialize the subscribers
        subscriberPool.put(SubscriberType.PORT_COUNTER,
                new PortCounterSubscriber(this, perfApi));
        subscriberPool.put(SubscriberType.VF_PORT_COUNTER,
                new VFPortCounterSubscriber(this, perfApi));
        subscriberPool.put(SubscriberType.GROUP_INFO, new GroupInfoSubscriber(
                this, perfApi));
        subscriberPool.put(SubscriberType.IMAGE_INFO, new ImageInfoSubscriber(
                this, perfApi));
        subscriberPool.put(SubscriberType.VF_INFO, new VFInfoSubscriber(this,
                perfApi));
        subscriberPool.put(SubscriberType.FOCUS_PORTS,
                new FocusPortCounterSubscriber(this, perfApi));
        subscriberPool.put(SubscriberType.VF_FOCUS_PORTS,
                new VFFocusPortSubscriber(this, perfApi));

        log.info("Refresh Rate = " + refreshRate);
    }

    /**
     * @return the perfApi
     */
    public IPerformanceApi getPerformanceApi() {
        return perfApi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.publisher.subscriber.IRegisterTask#getSubscriber(com
     * .intel.stl.ui.publisher.subscriber.SubscriberType)
     */
    @Override
    // public <E> Subscriber<E> getSubscriber(SubscriberType subscriberType) {
    public Subscriber<?> getSubscriber(SubscriberType subscriberType) {
        Subscriber<?> res = subscriberPool.get(subscriberType);
        if (res != null) {
            return res;
        } else {
            throw new IllegalArgumentException("Couldn't find Subscriber '"
                    + subscriberType + "'");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.publisher.subscriber.IRegisterTask#getRefreshRate()
     */
    @Override
    public int getRefreshRate() {
        return refreshRate;
    } /*
       * (non-Javadoc)
       * 
       * @see
       * com.intel.stl.ui.publisher.subscriber.IRegisterTask#updateRefreshRate
       * (int)
       */

    @Override
    public void updateRefreshRate(int refreshRate) {

        // Update the refresh rate
        this.refreshRate = refreshRate;
        log.info("Refresh Rate changed to: " + refreshRate);

        ScheduledExecutorService oldScheduledService = scheduledService;
        // create an new ExecutorService, the new registered tasks will be
        // scheduled on it
        scheduledService = Executors.newScheduledThreadPool(POOL_SIZE);
        // call subscriber to move its tasks to the new created ExecutorService
        for (Subscriber<?> subscriber : subscriberPool.values()) {
            subscriber.rescheduleTasks();
        }

        // Notify registered listeners of rate change.
        for (IRefreshRateListener listener : this.refreshRateChangeListeners) {
            listener.onRefreshRateChange(this.refreshRate);
        }

        // it should be safe to shutdown the old ExecutorService without impact
        // on UI
        try {
            shutdownServiceNow(oldScheduledService);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.publisher.subscriber.IRegisterTask#submit(java.lang.
     * Runnable)
     */
    @Override
    public Future<?> submitToBackground(Runnable task) {
        Future<?> future = null;
        try {
            future = backgroundService.submit(task);
        } catch (RejectedExecutionException ree) {
            if (!shutdownInProgress) {
                throw ree;
            }
        }
        return future;
    }

    @Override
    public <E> Future<E> submitToBackground(Callable<E> task) {
        Future<E> future = null;
        try {
            future = backgroundService.submit(task);
        } catch (RejectedExecutionException ree) {
            if (!shutdownInProgress) {
                throw ree;
            }
        }
        return future;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.publisher.subscriber.IRegisterTask#submitTask(java.util
     * .List, com.intel.stl.ui.publisher.Task,
     * com.intel.stl.ui.publisher.ICallback, java.util.concurrent.Callable)
     */
    @Override
    public <E> Task<E> scheduleTask(List<Task<E>> tasks, Task<E> task,
            ICallback<E> callback, Callable<E> caller) {

        return registerTask(tasks, task, callback, caller);
    }

    protected <E> Task<E> registerTask(List<Task<E>> tasks, Task<E> task,
            ICallback<E> callback, final Callable<E> caller) {
        if (tasks == null) {
            return null;
        }

        synchronized (tasks) {
            int index = tasks.indexOf(task);
            if (index >= 0) {
                task = tasks.get(index);
                task.addCallback(callback);
                log.info("Register Task " + task);
                log.info("Add callback to already running task " + task);
            } else {
                task.addCallback(callback);
                task.setCaller(caller);
                final Task<E> taskFinal = task;
                log.info("Schedule task " + task + " with rate " + refreshRate
                        + " sec.");
                ScheduledFuture<?> future =
                        scheduledService.scheduleAtFixedRate(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    E result = caller.call();
                                    taskFinal.onDone(result);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    try {
                                        taskFinal.onError(e);
                                    } finally {
                                        handleFailure(taskFinal, e);
                                    }
                                }
                            }
                        }, 0, refreshRate, TimeUnit.SECONDS);
                task.setFuture(future);
                tasks.add(task);
            }
            return task;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.publisher.subscriber.IRegisterTask#removeTask(java.util
     * .List, com.intel.stl.ui.publisher.Task,
     * com.intel.stl.ui.publisher.ICallback)
     */
    @Override
    public <E> void removeTask(List<Task<E>> tasks, Task<E> task,
            ICallback<E> callback) {

        deregisterTask(tasks, task, callback);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.publisher.subscriber.IRegisterTask#removeTaskArray(java
     * .util.List, java.util.List, com.intel.stl.ui.publisher.ICallback)
     */
    @Override
    public <E> void removeTask(List<Task<E>> taskList, List<Task<E>> tasks,
            ICallback<E[]> callbacks) {

        deregisterTask(taskList, tasks, callbacks);

    }

    protected <E> void deregisterTask(List<Task<E>> tasks, Task<E> target,
            ICallback<E> callback) {
        if (tasks == null) {
            return;
        }

        synchronized (tasks) {
            log.info("Deregister Task " + target);
            int index = tasks.indexOf(target);
            if (index >= 0) {
                Task<E> realTask = tasks.get(index);
                realTask.removeCallback(callback);
                if (realTask.isEmpty()) {
                    log.info("Stop Task " + target);
                    try {
                        realTask.getFuture().cancel(true);
                    } finally {
                        tasks.remove(index);
                    }
                }
            }
        }
    }

    protected <E> void deregisterTask(final List<Task<E>> tasks,
            List<Task<E>> targets, final ICallback<E[]> callback) {
        if (tasks == null) {
            return;
        }

        synchronized (tasks) {
            for (int i = targets.size() - 1; i >= 0; i--) {
                Task<E> target = targets.get(i);
                log.info("Deregister Task " + target);
                int index = tasks.indexOf(target);
                if (index >= 0) {
                    Task<E> realTask = tasks.get(index);
                    realTask.removeSubCallbacks(callback);
                    if (realTask.isEmpty()) {
                        log.info("Stop Task " + target);
                        try {
                            realTask.getFuture().cancel(true);
                        } finally {
                            tasks.remove(index);
                        }
                    }
                }
            }
        }
    }

    protected <E> void handleFailure(final Task<E> task, final Exception e) {
        Throwable error = e;
        if (e instanceof FMException) {
            Throwable tmp = e.getCause();
            if (tmp != null) {
                error = tmp;
            }
        }
        ITaskFailure<Void> taskFailure =
                new BaseTaskFailure<Void>(task, failureEvaluator) {

                    @Override
                    public Callable<Void> getTask() {
                        // we do NOT retry!
                        return null;
                    }

                    @Override
                    public void onFatal() {
                        log.info("Fatal Failure - Stop task! " + task);
                        FatalException fe = new FatalException(e);
                        // We need to be careful what we show on each callback
                        // on the error. The current approach is displaying
                        // a error message here, and then each callback takes
                        // local responsibility to do things like cleaning up.
                        task.onError(fe);
                        Util.showErrorMessage(context.getOwner(),
                                UILabels.STL40013_FATAL_FAILURE
                                        .getDescription(task.getDescription()));
                        // this exception will stop the schedule
                        throw fe;
                    }

                };
        failureMgr.submit(taskFailure, error);
    }

    public void clear() {
        // Traverse the subscribers in the subscriber pool, cancel the tasks
        // and clear out the task list
        for (Subscriber<?> subscriber : subscriberPool.values()) {
            subscriber.cancelTasks();
        } // for
    }

    /**
     * Description:
     * 
     * @throws InterruptedException
     * 
     */
    public void shutdown() throws InterruptedException {
        shutdownInProgress = true;
        try {
            clear();
        } finally {
            try {
                failureMgr.cleanup();
            } finally {
                try {
                    shutdownService(scheduledService);
                } finally {
                    shutdownService(backgroundService);
                }
            }
        }

        log.info("Shutdown " + getClass().getName() + " for subnet '" + name
                + "'");
    }

    private void shutdownService(ExecutorService service)
            throws InterruptedException {

        // Shut down the service
        service.shutdown();

        // Wait for termination to complete
        if (!service.awaitTermination(SHUTDOWN_TIME, TimeUnit.MILLISECONDS)) {
            log.info("Executor did not terminate in the specified time.");
            List<Runnable> droppedTasks = service.shutdownNow();
            log.info("Executor was abruptly shut down. " + droppedTasks.size()
                    + " tasks will not be executed.");
        }
    }

    private void shutdownServiceNow(ExecutorService service)
            throws InterruptedException {

        // Shut down the service immediately!
        service.shutdownNow();

        // Wait just in case it doesn't 'actually' shut down right away
        if (!service.awaitTermination(SHUTDOWN_TIME, TimeUnit.MILLISECONDS)) {
            log.info("Executor did not terminate in the specified time.");
            List<Runnable> droppedTasks = service.shutdownNow();
            log.info("Executor was abruptly shut down. " + droppedTasks.size()
                    + " tasks will not be executed.");
        }
    }

    // Register refresh rate listeners.
    public void addListener(IRefreshRateListener listener) {
        refreshRateChangeListeners.add(listener);
    }

    // De-register refresh rate listeners.
    public void removeListener(IRefreshRateListener listener) {
        refreshRateChangeListeners.remove(listener);
    }

    private class ServiceThreadFactory implements ThreadFactory {

        private final String prefix;

        private final AtomicInteger threadCount = new AtomicInteger(1);

        public ServiceThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            String threadName = prefix + threadCount.getAndIncrement();
            return new Thread(r, threadName);
        }

    }
}
