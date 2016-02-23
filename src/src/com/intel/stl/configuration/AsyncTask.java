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
 *  File Name: AsyncTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.9  2015/08/17 18:48:40  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/06/18 21:07:36  fernande
 *  Archive Log:    PR 128977 Application log needs to support multi-subnet. - Adding support for Logback's Mapped Diagnostic Context
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

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.intel.stl.api.StringUtils;
import com.intel.stl.common.STLMessages;

public abstract class AsyncTask<T> implements Callable<T> {

    protected static Logger log = LoggerFactory.getLogger(AsyncTask.class);

    private BaseProcessingService.AsyncFutureTask<T> future;

    private Map<String, String> loggingContextMap;

    @Override
    public T call() throws Exception {
        if (loggingContextMap != null) {
            MDC.setContextMap(loggingContextMap);
        }
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

    protected void setLoggingContextMap(Map<String, String> loggingContextMap) {
        this.loggingContextMap = loggingContextMap;
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
