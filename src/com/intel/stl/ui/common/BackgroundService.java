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
 *  File Name: BackgroundService.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/04/29 14:28:09  fernande
 *  Archive Log:    Adding client stack trace to exceptions for supportability.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/04/16 17:40:28  fernande
 *  Archive Log:    After a BackgroundService is shutdown by the application, RejectedExecutionExceptions are ignored
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/04/10 20:15:40  fernande
 *  Archive Log:    Adding wrapper class for a ExecutorService in order to be able to control exceptions thrown by threads under the ExecutorService
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackgroundService implements IBackgroundService {

    private static final long DEFAULT_SHUTDOWN_TIMEOUT = 1000;

    private final ExecutorService service;

    private final AtomicBoolean shutdown = new AtomicBoolean(false);

    private final long timeout;

    public BackgroundService(ExecutorService service) {
        this.service = service;
        this.timeout = DEFAULT_SHUTDOWN_TIMEOUT;
    }

    @Override
    public Future<Void> submit(Runnable runnable) {
        Exception e = new Exception("Caller's stack trace");
        BackgroundTask<Void> task =
                new BackgroundTask<Void>(runnable, shutdown, e);
        try {
            service.submit(task);
        } catch (RejectedExecutionException ree) {
            if (!shutdown.get()) {
                throw ree;
            }
        }
        return task;
    }

    @Override
    public <V> Future<V> submit(Callable<V> callable) {
        Exception e = new Exception("Caller's stack trace");
        BackgroundTask<V> task = new BackgroundTask<V>(callable, shutdown, e);
        service.submit(task);
        return task;
    }

    @Override
    public void shutdown() {
        shutdown.set(true);
        service.shutdown();
        try {
            service.awaitTermination(timeout, TimeUnit.MILLISECONDS);
            if (!service.isTerminated()) {
                service.shutdownNow();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isShutdown() {
        return shutdown.get();
    }

    private class BackgroundTask<V> extends FutureTask<V> {

        private final AtomicBoolean shutdown;

        private final Exception clientTrace;

        public BackgroundTask(Callable<V> callable, AtomicBoolean shutdown,
                Exception clientTrace) {
            super(callable);
            this.shutdown = shutdown;
            this.clientTrace = clientTrace;
        }

        public BackgroundTask(Runnable runnable, AtomicBoolean shutdown,
                Exception clientTrace) {
            super(runnable, null);
            this.shutdown = shutdown;
            this.clientTrace = clientTrace;
        }

        @Override
        protected void setException(Throwable t) {
            if (shutdown.get()) {
                System.out.println("Exception ignored after shutdown: " + t);
            } else {
                if (clientTrace != null) {
                    t.addSuppressed(clientTrace);
                }
                super.setException(t);
            }

        }

    }
}
