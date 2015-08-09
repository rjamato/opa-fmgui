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
 *  File Name: AsyncTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6.2.1  2015/05/06 19:33:43  jijunwan
 *  Archive Log:    Adding logging of caller's stack trace when a background task fails
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/04/30 17:27:53  fernande
 *  Archive Log:    Adding logging of caller's stack trace when a background task fails.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/12/11 18:34:55  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/24 18:49:56  fernande
 *  Archive Log:    Initial batch of fixes for notice processing. Notice updates not working yet but need a stable environment for Bob.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/15 21:18:53  fernande
 *  Archive Log:    Adding unit test for PerformanceApi and fixes for some issues found.
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

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.common.STLMessages;

public abstract class AsyncTask<T> implements Callable<T> {

    protected static Logger log = LoggerFactory.getLogger(AsyncTask.class);

    private BaseProcessingService.AsyncFutureTask<T> future;

    @Override
    public T call() throws Exception {
        try {
            T result = process();
            return result;
        } catch (RejectedExecutionException re) {
            log.info(STLMessages.STL60005_EXCEPTION_EXECUTING_TASK
                    .getDescription(this.getClass().getSimpleName(),
                            StringUtils.getErrorMessage(re)), re);
            log.info(STLMessages.STL60007_SUBMITTERS_TRACE.getDescription(),
                    future.getClientTrace());
            throw re;
        } catch (Exception e) {
            log.error(STLMessages.STL60005_EXCEPTION_EXECUTING_TASK
                    .getDescription(this.getClass().getSimpleName(),
                            StringUtils.getErrorMessage(e)), e);
            log.error(STLMessages.STL60007_SUBMITTERS_TRACE.getDescription(),
                    future.getClientTrace());
            throw e;
        }
    }

    public FutureTask<T> getFuture() {
        return future;
    }

    protected void setFuture(BaseProcessingService.AsyncFutureTask<T> future) {
        this.future = future;
    }

    public void checkArguments(Object... arguments) {
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i] == null) {
                IllegalArgumentException iae =
                        new IllegalArgumentException(
                                STLMessages.STL60004_ARGUMENT_CANNOT_BE_NULL
                                        .getDescription((i + 1), arguments[i]
                                                .getClass().getCanonicalName()));
                throw iae;
            }
        }
    }

    public abstract T process() throws Exception;
}
