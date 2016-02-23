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
 *  File Name: Task.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10  2015/08/17 18:54:08  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/10 23:11:54  jijunwan
 *  Archive Log:    1) changed task scheduler to support initial refresh rate
 *  Archive Log:    2) improved refresh rate update handling
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/02 15:25:28  rjtierne
 *  Archive Log:    Added new method getCallbackPosition() to check if a specific
 *  Archive Log:    callback is in the callback list
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/01/21 21:19:12  rjtierne
 *  Archive Log:    Removed individual refresh rates for task registration. Now using
 *  Archive Log:    refresh rate supplied by user input in preferences wizard.
 *  Archive Log:    Reinitialization of scheduler service not yet implemented.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/13 15:07:28  jijunwan
 *  Archive Log:    fixed synchronization issues on performance charts
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/08/12 21:05:06  jijunwan
 *  Archive Log:    1) added description to task
 *  Archive Log:    2) applied failure management to TaskScheduler
 *  Archive Log:    3) some code auto-reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/29 22:04:43  jijunwan
 *  Archive Log:    minor changes - added comments, added toString()
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/16 15:43:59  jijunwan
 *  Archive Log:    added CustomStates to task scheduler
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/30 17:34:47  jijunwan
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;

import com.intel.stl.ui.publisher.BatchedCallback.IndexedCallback;

public class Task<E> {
    private final int type;

    private final Object key;

    private final String description;

    private Callable<E> caller;

    private ScheduledFuture<?> future;

    /**
     * We intentionally maintain synchronization on callbacks by ourselves
     * because the syn should cover the calls on the callbacks. For example, if
     * data arrived just before we remove a callback from the task, we need to
     * remove the callback after the data is already been processed by it.
     */
    private final List<ICallback<E>> callBacks = new LinkedList<ICallback<E>>();

    public Task(int type, Object key, String description) {
        super();
        this.type = type;
        this.key = key;
        this.description = description;
    }

    /**
     * @return the name
     */
    public Object getKey() {
        return key;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the callers
     */
    public Callable<E> getCaller() {
        return caller;
    }

    /**
     * @param callers
     *            the callers to set
     */
    public void setCaller(Callable<E> callers) {
        this.caller = callers;
    }

    /**
     * @return the future
     */
    public ScheduledFuture<?> getFuture() {
        return future;
    }

    /**
     * @param future
     *            the future to set
     */
    protected void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

    protected synchronized void addCallback(ICallback<E> callBack) {
        callBacks.add(callBack);
    }

    protected synchronized int getCallbackPosition(ICallback<E> callBack) {

        return callBacks.indexOf(callBack);
    }

    protected synchronized void removeCallback(ICallback<E> callBack) {
        callBacks.remove(callBack);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected synchronized void removeSubCallbacks(ICallback<E[]> callBack) {
        ICallback<E>[] members = callBacks.toArray(new ICallback[0]);
        for (ICallback<E> ck : members) {
            if (ck instanceof IndexedCallback
                    && ((IndexedCallback) ck).belongToCallback(callBack)) {
                callBacks.remove(ck);
            }
        }
    }

    protected synchronized boolean isEmpty() {
        return callBacks.isEmpty();
    }

    public synchronized List<ICallback<E>> clearCallbacks() {
        List<ICallback<E>> res = new ArrayList<ICallback<E>>(callBacks);
        callBacks.clear();
        return res;
    }

    // /**
    // * <i>Description:</i> A general way allows us synchronously handle each
    // * callback. We intentionally hide accessing to callbacks list from
    // outside
    // * to force all processes on the callbacks are under our synchronized
    // * control
    // *
    // * @param handler
    // */
    // @SuppressWarnings("unchecked")
    // protected synchronized void iterateCallbacks(ICallbackHandler<E> handler)
    // {
    // ICallback<E>[] members = callBacks.toArray(new ICallback[0]);
    // for (ICallback<E> callback : members) {
    // handler.handleCallback(callback);
    // }
    // }

    /**
     * @return the callBacks
     */
    public List<ICallback<E>> getCallBacks() {
        return callBacks;
    }

    /**
     * <i>Description:</i> Synchronously process collected data with callbacks.
     * Ideally the callback should handle exceptions in its calls, i.e. #onDone,
     * #onError and #onFinally shouldn't throw exceptions. We are handling them
     * here just in case.
     * 
     * @param result
     */
    protected synchronized void onDone(E result) {
        for (ICallback<E> callback : callBacks) {
            try {
                callback.onDone(result);
            } catch (Exception e) {
                callback.onError(e);
            } finally {
                try {
                    callback.onFinally();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

    /**
     * <i>Description:</i> Synchronously handle external errors, such as
     * exceptions generated when we collect data.
     * 
     * @param error
     */
    protected synchronized void onError(Exception error) {
        for (ICallback<E> callback : callBacks) {
            try {
                callback.onError(error);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + type;
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Task other = (Task) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Task [type=" + type + ", key=" + key + ", description="
                + description + ",  callBacks=" + callBacks + "]";
    }

    protected interface ICallbackHandler<E> {
        void handleCallback(ICallback<E> callback);
    }
}
