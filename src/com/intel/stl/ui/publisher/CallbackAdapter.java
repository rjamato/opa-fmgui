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
 *  File Name: CallBackAdapter.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
            log.error(StringUtils.getErrorMessage(e), e);
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
