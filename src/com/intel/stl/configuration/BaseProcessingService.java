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
 *  File Name: BaseProcessingService.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3.2.1  2015/05/06 19:33:43  jijunwan
 *  Archive Log:    Adding logging of caller's stack trace when a background task fails
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/30 17:27:53  fernande
 *  Archive Log:    Adding logging of caller's stack trace when a background task fails.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/29 14:26:10  fernande
 *  Archive Log:    Adding client stack trace to exceptions for supportability.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/04/03 16:10:28  fernande
 *  Archive Log:    Added the caller's stack trace to any task so that if it fails, you can track down the submitter of the task.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/27 20:39:59  fernande
 *  Archive Log:    Refactored AsyncProcessingService so that other processing services can be created easily from BaseProcessingService.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.configuration;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class BaseProcessingService implements ProcessingService {

    private static final int DEFAULT_POOL_SIZE = 2;

    private static final long DEFAULT_SHUTDOWN_TIMEOUT = 1000;

    private final ExecutorService processingService;

    public BaseProcessingService() {
        this(Executors.newFixedThreadPool(DEFAULT_POOL_SIZE));
    }

    public BaseProcessingService(ExecutorService service) {
        this.processingService = service;
    }

    @Override
    public <T> void submit(AsyncTask<T> task, ResultHandler<T> handler) {
        if (processingService.isShutdown()) {
            return;
        }
        executeTask(task, handler, processingService);
    }

    @Override
    public void shutdown() {
        shutdownService(processingService, DEFAULT_SHUTDOWN_TIMEOUT);
    }

    @Override
    public void shutdown(long timeout) {
        shutdownService(processingService, timeout);
    }

    // We don't want the same task to be submitted twice (to either executor
    // service). As long as the submitter creates a new task every time it
    // submits a task, it is guaranteed that the task will execute. If multiple
    // threads try to submit the same task instance, neither can know which
    // handler will be invoked.
    protected synchronized <T> void executeTask(AsyncTask<T> task,
            ResultHandler<T> handler, ExecutorService executor) {
        FutureTask<T> currFuture = task.getFuture();
        if (currFuture == null) {
            AsyncFutureTask<T> future = new AsyncFutureTask<T>(task, handler);
            task.setFuture(future);
            future.setClientTrace(new Exception("Caller's stack trace"));
            executor.execute(future);
        }
    }

    protected void shutdownService(ExecutorService executor, long millis) {
        executor.shutdown();
        try {
            executor.awaitTermination(millis, TimeUnit.MILLISECONDS);
            if (!executor.isTerminated()) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    protected class AsyncFutureTask<V> extends FutureTask<V> {

        private Exception clientTrace;

        private final ResultHandler<V> handler;

        public AsyncFutureTask(Callable<V> callable, ResultHandler<V> handler) {
            super(callable);
            this.handler = handler;
        }

        @Override
        protected void setException(Throwable t) {
            if (clientTrace != null) {
                t.addSuppressed(clientTrace);
            }
            super.setException(t);
        }

        @Override
        protected void done() {
            if (handler != null) {
                handler.onTaskCompleted(this);
            }
        }

        protected void setClientTrace(Exception clientTrace) {
            this.clientTrace = clientTrace;
        }

        protected Exception getClientTrace() {
            return clientTrace;
        }
    }
}
