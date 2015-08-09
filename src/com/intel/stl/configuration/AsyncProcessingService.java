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
 *  File Name: AsyncInitializer.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/03/27 20:39:59  fernande
 *  Archive Log:    Refactored AsyncProcessingService so that other processing services can be created easily from BaseProcessingService.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/24 18:49:56  fernande
 *  Archive Log:    Initial batch of fixes for notice processing. Notice updates not working yet but need a stable environment for Bob.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/18 20:48:50  fernande
 *  Archive Log:    Enabling GroupInfo saving after fixing issues in the application
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/12 19:58:06  fernande
 *  Archive Log:    We now save ImageInfo and GroupInfo to the database. As they are retrieved by the UI, they are buffered and then saved at certain thresholds.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/03 21:34:19  fernande
 *  Archive Log:    Adding the CacheManager in support of APIs
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/19 20:05:24  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class AsyncProcessingService extends BaseProcessingService implements
        SerialProcessingService {

    private static final int DEFAULT_POOL_SIZE = 2;

    private static final long SERIAL_SHUTDOWN_TIMEOUT = 10000; // 10 secs

    // This value is expressly set to 1 to serialize tasks. DO NOT CHANGE to any
    // value other than 1
    private static final int SERIAL_POOL_SIZE = 1;

    private final ExecutorService serialService;

    public AsyncProcessingService() {
        super(Executors.newFixedThreadPool(DEFAULT_POOL_SIZE,
                new ProcessingServiceThreadFactory()));

        ThreadFactory pssThreadFactory = new SerialServiceThreadFactory();
        serialService =
                Executors
                        .newFixedThreadPool(SERIAL_POOL_SIZE, pssThreadFactory);
    }

    @Override
    public <T> void submitSerial(AsyncTask<T> task, ResultHandler<T> handler) {
        if (serialService.isShutdown()) {
            return;
        }
        executeTask(task, handler, serialService);
    }

    @Override
    public void shutdown() {
        shutdownService(serialService, SERIAL_SHUTDOWN_TIMEOUT);
        super.shutdown();
    }

    @Override
    public void shutdown(long timeout) {
        shutdownService(serialService, timeout);
        super.shutdown(timeout);
    }
}
