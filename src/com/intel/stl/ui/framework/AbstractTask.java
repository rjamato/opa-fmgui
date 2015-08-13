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
 *  File Name: FVBaseTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7.2.1  2015/08/12 15:26:48  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
