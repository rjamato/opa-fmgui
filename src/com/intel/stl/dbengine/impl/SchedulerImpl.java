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

package com.intel.stl.dbengine.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.datamanager.DatabaseCall;
import com.intel.stl.dbengine.DatabaseServer;
import com.intel.stl.dbengine.Scheduler;

public class SchedulerImpl implements Scheduler {
    private static Logger log = LoggerFactory.getLogger(SchedulerImpl.class);

    private final ExecutorService pool;

    private final DatabaseServer server;

    public SchedulerImpl(DatabaseServer server, int poolSize) {
        this.server = server;
        ThreadFactory threadFactory = new DatabaseThreadFactory(server);
        pool = Executors.newFixedThreadPool(poolSize, threadFactory);
    }

    @Override
    public synchronized <T> Future<T> enqueue(DatabaseCall<T> workItem) {
        workItem.setDatabaseServer(server);
        workItem.setClientTrace(new Exception("Caller's stack trace"));
        Future<T> future = pool.submit(workItem);
        workItem.setFuture(future);
        return future;
    }

    @Override
    public void shutdown() {
        log.info("Scheduler shutdown in progress");
        pool.shutdown();
        try {
            pool.awaitTermination(2L, TimeUnit.SECONDS);
            if (!pool.isTerminated()) {
                pool.shutdownNow();
            }
            log.info("Scheduler shutdown complete.");
        } catch (InterruptedException e) {
            log.info("Scheduler shutdown interrupted.", e);
        }
    }

}
