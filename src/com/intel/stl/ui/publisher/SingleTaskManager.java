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
 *  File Name: SingleTaskManager.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2014/12/11 18:49:26  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/05 23:01:48  jijunwan
 *  Archive Log:    improved to support whether we want to cancel a task while it's running
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/11/05 16:34:09  jijunwan
 *  Archive Log:    added code to ignore CancellationException that is wrapped in ExecutionException
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/05 13:44:29  jijunwan
 *  Archive Log:    added a task manager that is able to cancel previous task to ensure only one task is running
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.publisher;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.ui.common.ICancelIndicator;

/**
 * A task manager that only runs one task at one time. If there is a unfinished
 * previous task, it will cancel it first.
 */
public class SingleTaskManager {
    private static final Logger log = LoggerFactory.getLogger(SingleTaskManager.class);

    private static final boolean DEBUG = false;

    private SwingWorker<?, ?> worker;

    private boolean mayInterruptIfRunning = true;

    /**
     * @return the mayInterruptIfRunning
     */
    public boolean isMayInterruptIfRunning() {
        return mayInterruptIfRunning;
    }

    /**
     * @param mayInterruptIfRunning
     *            the mayInterruptIfRunning to set
     */
    public void setMayInterruptIfRunning(boolean mayInterruptIfRunning) {
        this.mayInterruptIfRunning = mayInterruptIfRunning;
    }

    public synchronized <V> void submit(final CancellableCall<V> caller,
            final ICallback<V> callback) {
        if (worker != null && !worker.isDone()) {
            worker.cancel(mayInterruptIfRunning);
        }

        worker = new SwingWorker<V, Void>() {
            @Override
            protected V doInBackground() throws Exception {
                if (isCancelled()) {
                    log.info("Cancelled task caller " + caller + " callback "
                            + callback);
                    return null;
                }

                if (caller != null) {
                    if (DEBUG) {
                        System.out.println("Start caller " + caller
                                + " at background "
                                + Thread.currentThread().getName()
                                + "with callback " + callback);
                    }
                    V res = caller.call();
                    if (DEBUG) {
                        System.out.println("End caller " + caller);
                    }
                    return res;
                } else {
                    return null;
                }
            }

            /*
             * (non-Javadoc)
             * 
             * @see javax.swing.SwingWorker#done()
             */
            @Override
            protected void done() {
                try {
                    V result = get();
                    if (callback != null) {
                        if (DEBUG) {
                            System.out.println("Start callback " + callback
                                    + " at frontground "
                                    + Thread.currentThread().getName());
                        }
                        callback.onDone(result);
                        if (DEBUG) {
                            System.out.println("End callback " + callback);
                        }
                    }
                } catch (InterruptedException e) {
                    if (DEBUG) {
                        System.out.println("Interrupted caller " + caller
                                + " callback " + callback);
                    }
                } catch (CancellationException e) {
                    log.info("Cancelled task caller " + caller + " callback "
                            + callback);
                    if (DEBUG) {
                        System.out.println("Cancelled task caller " + caller
                                + " callback " + callback);
                    }
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    if (!(cause instanceof CancellationException)) {
                        e.printStackTrace();
                        e.getCause().printStackTrace();
                        if (callback != null) {
                            callback.onError(e.getCause());
                        }
                    }
                } finally {
                    if (callback != null) {
                        callback.onFinally();
                    }
                }
            }
        };

        final ICancelIndicator indicator = caller.getCancelIndicator();
        caller.setCancelIndicator(new ICancelIndicator() {
            @Override
            public boolean isCancelled() {
                return worker.isCancelled()
                        || (indicator != null && indicator.isCancelled());
            }
        });
        worker.execute();
    }

}
