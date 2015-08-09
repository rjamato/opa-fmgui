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
 *  File Name: BatchedCallBak.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/04/09 22:53:03  jijunwan
 *  Archive Log:    improved BatchedCallback to support refreshing data
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/02 15:24:21  rjtierne
 *  Archive Log:    Made getCallback() public so it can be used by subscribers
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/12/11 18:49:26  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/08/15 21:47:12  jijunwan
 *  Archive Log:    included class name in log output
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/12 21:05:06  jijunwan
 *  Archive Log:    1) added description to task
 *  Archive Log:    2) applied failure management to TaskScheduler
 *  Archive Log:    3) some code auto-reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/23 04:54:37  jijunwan
 *  Archive Log:    improved batched tasks to ignore incomplete data. This is useful when data refresh time interval is smaller than data processing time. When this happens, we selectively ignore some data, so we can automatically adapt to current system's limit.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/30 19:47:55  jijunwan
 *  Archive Log:    batched schedule to call multiple tasks and then process all results together
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/30 17:34:47  jijunwan
 *  Archive Log:    rename *CallBack to *Callback
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/30 17:29:23  jijunwan
 *  Archive Log:    rename ApiBroker to TaskScheduler; added pooled backgorund services to TaskScheduler so we can use it to run background tasks; improved to support schedule batched task and callbacks
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.publisher;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchedCallback<E> {
    private static final Logger log = LoggerFactory
            .getLogger(BatchedCallback.class);

    private final Class<E> klass;

    private final int size;

    private final ICallback<E[]> finalCallback;

    private final ICallback<E>[] callbacks;

    private E[] temp;

    private int count;

    private E[] results;

    private final int[] ids;

    private int targetId;

    private int maxId;

    private final Set<Throwable> processedErrors;

    @SuppressWarnings("unchecked")
    public BatchedCallback(int size, ICallback<E[]> callback, Class<E> klass) {
        this.size = size;
        this.finalCallback = callback;
        this.klass = klass;
        callbacks = new ICallback[size];
        for (int i = 0; i < size; i++) {
            callbacks[i] = new IndexedCallback(i);
        }
        processedErrors = new HashSet<Throwable>();
        temp = (E[]) Array.newInstance(klass, size);
        ids = new int[size];
        targetId = maxId = 1;
    }

    public ICallback<E> getCallback(int index) {
        return callbacks[index];
    }

    @SuppressWarnings("unchecked")
    protected synchronized void addResult(int index, E result) {
        ids[index] += 1;
        if (ids[index] > targetId) {
            if (maxId < ids[index]) {
                maxId = ids[index];
            }
            log.warn(klass.getSimpleName() + " Result " + index + " (id="
                    + ids[index] + " targetId=" + targetId + " maxId=" + maxId
                    + ") already exist! You may have too small refresh rate.");
        } else if (ids[index] == targetId) {
            temp[index] = result;
            count += 1;
            finalCallback.onProgress((double) count / size);
            if (count == size) {
                // log.info("Release id="+targetId+" maxId="+maxId);
                results = (E[]) Array.newInstance(klass, size);
                System.arraycopy(temp, 0, results, 0, size);
                temp = (E[]) Array.newInstance(klass, size);
                count = 0;
                for (int i = 0; i < size; i++) {
                    ids[i] = ids[i] - maxId;
                }
                maxId = 1;
                targetId = 1;
                processedErrors.clear();
                // log.info("Init next release "+Arrays.toString(ids));
                finalCallback.onDone(results);
            }
        }
    }

    protected void clear() {
        processedErrors.clear();
        targetId = maxId = 1;
        for (int i = 0; i < size; i++) {
            temp[i] = null;
            ids[i] = 0;
        }
    }

    protected class IndexedCallback extends CallbackAdapter<E> {
        private final int index;

        public IndexedCallback(int index) {
            super();
            this.index = index;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.intel.stl.ui.publisher.CallBackAdapter#onDone(java.lang.Object)
         */
        @Override
        public void onDone(E result) {
            addResult(index, result);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.intel.stl.ui.publisher.CallBackAdapter#onError(java.lang.Throwable
         * [])
         */
        @Override
        public void onError(Throwable... errors) {
            Set<Throwable> newErrors = new HashSet<Throwable>();
            for (Throwable error : errors) {
                if (!processedErrors.contains(error)) {
                    newErrors.add(error);
                } else {
                    processedErrors.add(error);
                }
            }
            finalCallback.onError(newErrors.toArray(new Throwable[0]));
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.intel.stl.ui.publisher.CallbackAdapter#reset()
         */
        @Override
        public void reset() {
            clear();
        }

        public boolean belongToCallback(ICallback<E[]> callback) {
            return finalCallback == callback;
        }
    }
}
