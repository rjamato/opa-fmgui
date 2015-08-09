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
 *  File Name: FailureRecoverManagement.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2014/12/11 18:32:09  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/14 20:47:10  jijunwan
 *  Archive Log:    turned on debug
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/13 12:43:25  jijunwan
 *  Archive Log:    added sleep for blocked failure recovery
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/12 20:28:40  jijunwan
 *  Archive Log:    init version Failure Management
 *  Archive Log:
 *
 *  Overview: We intentionally do not put this class under package impl because
 *  we will reuse it from UI side which will ignore any classes under impl 
 *  package 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.failure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;

/**
 * This management applies retry strategy to handle failures. Basically, when it
 * sees a failure it will sleep a while and then retry it. If it fails after
 * several tries, it will call {@link ITaskFatal#onFatal()} to treat it as
 * unrecoverable failure.
 * 
 */
public class FailureRecoverManagement implements IFailureManagement {
    private final static Logger log = LoggerFactory
            .getLogger(FailureRecoverManagement.class);

    private static boolean DEBUG = false;

    private static final long SHUTDOWN_TIME = 1000; // 1 sec

    public final static int DEFAULT_TOLERANCE = 3;

    public final static long DEFAULT_MEM_LENGTH = 3 * 60 * 1000; // 3 minutes

    public final static long DEFAULT_RETRY_INTERVAL = 10 * 1000; // 10 sec.

    private final int tolerance;

    private final long memoryLength;

    private final long retryInterval;

    private final Map<Object, FailureItem> items;

    private final ScheduledExecutorService executor;

    public FailureRecoverManagement() {
        this(DEFAULT_TOLERANCE, DEFAULT_MEM_LENGTH, DEFAULT_RETRY_INTERVAL);
    }

    /**
     * Description:
     * 
     * @param tolerance
     * @param memoryLength
     */
    public FailureRecoverManagement(int tolerance, long memoryLength,
            long retryInterval) {
        super();
        this.tolerance = tolerance;
        this.memoryLength = memoryLength;
        this.retryInterval = retryInterval;
        items = new HashMap<Object, FailureItem>();
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * @return the tolerance
     */
    public int getTolerance() {
        return tolerance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.failure.IFailureManagement#submit(com.intel.stl.api
     * .failure.IFailureItem, java.lang.Throwable)
     */
    @Override
    public synchronized void submit(final ITaskFailure<Void> failure,
            Throwable error) {
        if (failure == null) {
            log.info("Failure is null");
            return;
        }

        checkItems();

        FailureType type = failure.getFailureType(error);
        if (type == FailureType.UNRECOVERABLE) {
            if (DEBUG) {
                System.out.println("Task failure " + failure.getTaskId()
                        + " has exception " + error
                        + " that is a fatal failure. Running onFatal...");
            }
            failure.onFatal();
            items.remove(failure.getTaskId());
        } else if (type == FailureType.RECOVERABLE) {
            FailureItem item = items.get(failure.getTaskId());
            if (item == null) {
                item = new FailureItem();
                items.put(failure.getTaskId(), item);
            }
            int count = item.increaseCount();
            if (DEBUG) {
                System.out.println("Task failure " + failure.getTaskId()
                        + " has count " + count);
            }
            if (count >= tolerance) {
                if (DEBUG) {
                    System.out.println("Task failure " + failure.getTaskId()
                            + " is unrecoverable. Running onFatal...");
                }
                // unrecoverable
                failure.onFatal();
                items.remove(failure.getTaskId());
            } else if (failure.getTask() != null) {
                if (DEBUG) {
                    System.out.println("Task failure " + failure.getTaskId()
                            + " is recoverable. Scheduling retry...");
                }
                // retry
                final Callable<?> task = failure.getTask();
                Runnable realTask = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            task.call();
                        } catch (Exception e) {
                            log.info(
                                    "Retry task with exception "
                                            + StringUtils.getErrorMessage(e), e);
                            submit(failure, e);
                        }
                    }
                };
                executor.schedule(realTask, retryInterval,
                        TimeUnit.MILLISECONDS);
            }
        }
    }

    /**
     * We intentionally share memory of FailureItems, so when a user uses block
     * and unblocked failure management together, the item counter still work
     * correctly.
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.failure.IFailureManagement#evaluate(com.intel.stl.api
     * .failure.ITaskFailure, java.lang.Throwable)
     */
    @Override
    public <E> E evaluate(ITaskFailure<E> failure, Throwable error) {
        if (failure == null) {
            log.info("Failure is null");
            return null;
        }

        checkItems();

        FailureType type = failure.getFailureType(error);
        if (type == FailureType.UNRECOVERABLE) {
            if (DEBUG) {
                System.out.println("Task failure " + failure.getTaskId()
                        + " has exception " + error
                        + " that is a fatal failure. Running onFatal...");
            }
            failure.onFatal();
            items.remove(failure.getTaskId());
        } else if (type == FailureType.RECOVERABLE) {
            FailureItem item = items.get(failure.getTaskId());
            if (item == null) {
                item = new FailureItem();
                items.put(failure.getTaskId(), item);
            }
            int count = item.increaseCount();
            if (DEBUG) {
                System.out.println("Task failure " + failure.getTaskId()
                        + " has count " + count);
            }
            if (count >= tolerance) {
                if (DEBUG) {
                    System.out.println("Task failure " + failure.getTaskId()
                            + " is unrecoverable. Running onFatal...");
                }
                // unrecoverable
                failure.onFatal();
                items.remove(failure.getTaskId());
            } else if (failure.getTask() != null) {
                if (DEBUG) {
                    System.out.println("Task failure " + failure.getTaskId()
                            + " is recoverable. Retrying...");
                }
                // retry
                try {
                    Thread.sleep(retryInterval);
                } catch (InterruptedException e1) {
                }
                Callable<E> task = failure.getTask();
                try {
                    return task.call();
                } catch (Exception e) {
                    log.info(
                            "Retry task with exception "
                                    + StringUtils.getErrorMessage(e), e);
                    return evaluate(failure, e);
                }
            }
        }
        return null;
    }

    /**
     * <i>Description:</i> clear old failures that doesn't happen recently
     * 
     */
    protected void checkItems() {
        Object[] ids = items.keySet().toArray();
        for (Object id : ids) {
            FailureItem item = items.get(id);
            long elapsedTime = item.getElapsedTime();
            if (elapsedTime > memoryLength) {
                log.info("Remove old task failure " + id);
                items.remove(id);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.failure.IFailureManagement#cleanup()
     */
    @Override
    public void cleanup() throws InterruptedException {
        items.clear();
        executor.shutdown();
        if (!executor.awaitTermination(SHUTDOWN_TIME, TimeUnit.MILLISECONDS)) {
            log.info("Executor did not terminate in the specified time.");
            List<Runnable> droppedTasks = executor.shutdownNow();
            log.info("Executor was abruptly shut down. " + droppedTasks.size()
                    + " tasks will not be executed.");
        }
    }

    private static class FailureItem {
        private int count;

        private long timestamp;

        public int increaseCount() {
            count += 1;
            timestamp = System.currentTimeMillis();
            return count;
        }

        public long getElapsedTime() {
            return System.currentTimeMillis() - timestamp;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "FailureItem [count=" + count + ", timestamp=" + timestamp
                    + "]";
        }

    }

}
