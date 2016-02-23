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
 *  File Name: CallBackAdapter.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10  2015/08/17 18:54:08  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/07/31 21:09:52  fernande
 *  Archive Log:    PR 129631 - Ports table sometimes not getting performance related data. Changed to handle a request cancellation exceptions and not log stack traces in those cases.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/04/09 22:53:03  jijunwan
 *  Archive Log:    improved BatchedCallback to support refreshing data
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/12 19:40:04  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/12/11 18:49:26  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/22 02:22:39  jijunwan
 *  Archive Log:    minor change to print out exception stack trace
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/12 20:58:06  jijunwan
 *  Archive Log:    1) renamed HexUtils to StringUtils
 *  Archive Log:    2) added a method to StringUtils to get error message for an exception
 *  Archive Log:    3) changed all code to call StringUtils to get error message
 *  Archive Log:    4) some extra ode format change
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/05 13:41:23  jijunwan
 *  Archive Log:    improved ICallback to support finally call
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/23 04:54:37  jijunwan
 *  Archive Log:    improved batched tasks to ignore incomplete data. This is useful when data refresh time interval is smaller than data processing time. When this happens, we selectively ignore some data, so we can automatically adapt to current system's limit.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/30 17:34:47  jijunwan
 *  Archive Log:    rename *CallBack to *Callback
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.performance.PerformanceRequestCancelledException;

public class CallbackAdapter<E> implements ICallback<E> {
    private static Logger log = LoggerFactory.getLogger(CallbackAdapter.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.publisher.ICallBack#onDone(java.lang.Object)
     */
    @Override
    public void onDone(E result) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.hpc.stl.ui.publisher.ICallBack#onError(java.lang.Throwable[])
     */
    @Override
    public void onError(Throwable... errors) {
        for (Throwable e : errors) {
            if (e instanceof PerformanceRequestCancelledException) {
                // Do not log; it was done already in the TasScheduler
            } else {
                log.error(StringUtils.getErrorMessage(e), e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.publisher.ICallBack#onProgress(double)
     */
    @Override
    public void onProgress(double progress) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.publisher.ICallback#onFinal()
     */
    @Override
    public void onFinally() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.publisher.ICallback#reset()
     */
    @Override
    public void reset() {
    }

    public static <E> ICallback<E[]> asArrayCallbak(final ICallback<E> callback) {
        return new CallbackAdapter<E[]>() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * com.intel.stl.ui.publisher.CallbackAdapter#onDone(java.lang.Object
             * )
             */
            @Override
            public void onDone(E[] result) {
                callback.onDone(result[0]);
            }

        };
    }
}
