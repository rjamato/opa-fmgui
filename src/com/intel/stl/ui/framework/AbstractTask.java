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
 *  File Name: FVBaseTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/03/10 22:47:28  jijunwan
 *  Archive Log:    fixed warning
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/26 22:07:37  fernande
 *  Archive Log:    Removed dependency for AbstractTask on IModel descendants to enable pending task in FabricController
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/22 01:15:24  jijunwan
 *  Archive Log:    some simplifications on MVC framework
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/16 13:17:29  fernande
 *  Archive Log:    Changes to AbstractTask to support an onFinally method that is guaranteed to be called no matter what happens in the onTaskSuccess and onTaskFailure implementations for a task.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/09 12:54:07  fernande
 *  Archive Log:    Added support for PropertyChange events bubbling up from an SwingWorker, thru an AbstractTask into the controller.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/22 21:48:13  fernande
 *  Archive Log:    Changes to framework to support model update lifecycle by notifying views of changes
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/09 13:31:46  fernande
 *  Archive Log:    Moving MVC framework to its own package and renaming for consistency
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/08 20:27:15  fernande
 *  Archive Log:    Basic MVC framework with SwingWorker support
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.framework;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.intel.stl.ui.main.Context;

public abstract class AbstractTask<M, T, V> implements ITask {

    protected final M model;

    private final BackgroundWorker<T, V> worker;

    private final PropertyChangeSupport support;

    private IController controller;

    private final AtomicBoolean submitted = new AtomicBoolean(false);

    public AbstractTask(M model) {
        this.model = model;
        this.worker = new BackgroundWorker<T, V>() {

            @Override
            protected T doInBackground() throws Exception {
                return processInBackground(context);
            }

            @Override
            protected void done() {
                T result;
                try {
                    result = get();
                    onTaskSuccess(result);
                    owner.onTaskSuccess();
                } catch (InterruptedException e) {
                    onTaskFailure(e);
                    owner.onTaskFailure(e);
                } catch (ExecutionException e) {
                    onTaskFailure(e.getCause());
                    owner.onTaskFailure(e.getCause());
                } finally {
                    onFinally();
                }
            }

            @Override
            protected void process(List<V> results) {
                processIntermediateResults(results);
            }
        };
        this.support = worker.getPropertyChangeSupport();
    }

    public M getModel() {
        return model;
    }

    @Override
    public void execute(IController owner) {
        if (submitted.compareAndSet(false, true)) {
            this.controller = owner;
            worker.setOwner(controller);
            this.worker.execute();
        }
    }

    public abstract T processInBackground(Context context) throws Exception;

    public abstract void onTaskSuccess(T result);

    public abstract void onTaskFailure(Throwable caught);

    public abstract void onFinally();

    /**
     * 
     * <i>Description:</i> processes intermediate results published by the task;
     * 
     * @param intermediateResults
     *            the intermediate results published by the process in the
     *            background
     */
    protected abstract void processIntermediateResults(
            List<V> intermediateResults);

    @Override
    public boolean isSubmitted() {
        return submitted.get();
    }

    @Override
    public boolean isDone() {
        return worker.isDone();
    }

    @Override
    public boolean isCancelled() {
        return worker.isCancelled();
    }

    @Override
    public void cancel(boolean mayInterruptIfRunning) {
        worker.cancel(mayInterruptIfRunning);
    }

    @Override
    public void firePropertyChange(String propertyName, Object oldValue,
            Object newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        worker.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        worker.removePropertyChangeListener(listener);
    }

    @SafeVarargs
    protected final void publish(V... chunks) {
        worker.publishIntermediateResults(chunks);
    }

    protected final void setProgress(int progress) {
        worker.setTaskProgress(progress);
    }

    protected final IController getController() {
        return controller;
    }
}
