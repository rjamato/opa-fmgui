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
 *  File Name: AbstractDataProvider.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/08/17 18:54:06  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/06/26 23:03:51  jijunwan
 *  Archive Log:    PR 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - improvement on stability
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/06/25 20:42:14  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - improved PerformanceItem to support port counters
 *  Archive Log:    - improved PerformanceItem to use generic ISource to describe data source
 *  Archive Log:    - improved PerformanceItem to use enum DataProviderName to describe data provider name
 *  Archive Log:    - improved PerformanceItem to support creating a copy of PerformanceItem
 *  Archive Log:    - improved TrendItem to share scale with other charts
 *  Archive Log:    - improved SimpleDataProvider to support hsitory data
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/03 21:12:34  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/02 15:38:26  rjtierne
 *  Archive Log:    New TaskScheduler architecture; now employs subscribers to submit
 *  Archive Log:    tasks for scheduling.  When update rate is changed on Wizard, TaskScheduler
 *  Archive Log:    uses this new architecture to terminate tasks and service and restart them.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/21 21:19:10  rjtierne
 *  Archive Log:    Removed individual refresh rates for task registration. Now using
 *  Archive Log:    refresh rate supplied by user input in preferences wizard.
 *  Archive Log:    Reinitialization of scheduler service not yet implemented.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/13 15:07:29  jijunwan
 *  Archive Log:    fixed synchronization issues on performance charts
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/09 18:26:06  jijunwan
 *  Archive Log:    1) introduced ISourceObserver to provide flexibility on dataset preparation when we change data sources
 *  Archive Log:    2) Applied ISourceObserver to fix the synchronization issue happens when we switch data sources
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/26 15:14:31  jijunwan
 *  Archive Log:    added refresh function to performance charts
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/17 16:25:37  jijunwan
 *  Archive Log:    improvement to support sleep mode so we can reduce FE traffic
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 15:08:58  jijunwan
 *  Archive Log:    new framework for performance data visualization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance.provider;

import java.util.concurrent.Future;

import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.performance.ISource;
import com.intel.stl.ui.performance.observer.IDataObserver;
import com.intel.stl.ui.publisher.CallbackAdapter;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;

public abstract class SimpleDataProvider<E, S extends ISource> extends
        AbstractDataProvider<E, S> {

    protected S sourceName;

    protected Task<E> task;

    protected Future<Void> historyTask;

    private ICallback<E> callback;

    private ICallback<E[]> batchedCallback;

    /**
     * Description:
     * 
     * @param sourceName
     */
    public SimpleDataProvider() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.AbstractDataProvider#getSourceNames
     * ()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected S[] getSourceNames() {
        return sourceName == null ? null : (S[]) new ISource[] { sourceName };
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.AbstractDataProvider#sameSources
     * (java.lang.String[])
     */
    @Override
    protected boolean sameSources(S[] names) {
        S name = names == null || names.length == 0 ? null : names[0];
        if (name != null) {
            return name.equals(sourceName);
        } else if (sourceName != null) {
            return sourceName.equals(name);
        } else {
            return true;
        }
    }

    @Override
    protected void setSources(S[] names) {
        sourceName = names == null || names.length == 0 ? null : names[0];
        if (sourceName != null) {
            for (IDataObserver<E> observer : observers) {
                observer.reset();
            }

            if (historyType != null && historyType != HistoryType.CURRENT) {
                historyTask = initHistory(sourceName, getBatchedCallback());
            }
            task = registerTask(sourceName, getCallback());
            onRefresh(null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.IDataProvider#onRefresh(com.intel
     * .stl.ui.common.IProgressObserver, java.lang.String[])
     */
    @Override
    public void onRefresh(IProgressObserver observer) {
        if (scheduler == null) {
            return;
        }
        scheduler.submitToBackground(new Runnable() {
            @Override
            public void run() {
                S source = sourceName;
                if (source != null) {
                    E result = refresh(source);
                    if (source.equals(sourceName)) {
                        getCallback().onDone(result);
                    }
                }
            }
        });
    }

    protected ICallback<E> getCallback() {
        if (callback == null) {
            callback = new CallbackAdapter<E>() {
                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * com.intel.hpc.stl.ui.publisher.CallBackAdapter#onDone(java
                 * .lang .Object)
                 */
                @Override
                public synchronized void onDone(E result) {
                    if (result != null) {
                        fireNewData(result);
                    }
                }
            };
        }
        return callback;
    }

    protected abstract E refresh(S sourceName);

    protected abstract Task<E> registerTask(S sourceName, ICallback<E> callback);

    protected abstract void deregisterTask(Task<E> task, ICallback<E> callback);

    @Override
    public void clearSources() {
        if (scheduler != null) {
            if (task != null) {
                deregisterTask(task, callback);
            }
            if (historyTask != null) {
                historyTask.cancel(true);
            }
        }
        sourceName = null;
    }

    protected ICallback<E[]> getBatchedCallback() {
        if (batchedCallback == null) {
            batchedCallback = new CallbackAdapter<E[]>() {
                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * com.intel.hpc.stl.ui.publisher.CallBackAdapter#onDone(java
                 * .lang .Object)
                 */
                @Override
                public synchronized void onDone(E[] result) {
                    if (result != null) {
                        for (E element : result) {
                            fireNewData(element);
                        }
                    }
                }
            };
        }
        return batchedCallback;
    }

    protected abstract Future<Void> initHistory(S sourceName,
            ICallback<E[]> callback);

    /**
     * HistoryType is only used for CombinedDataProvider not for
     * SimpleDataProvider for now. It's added for future use.
     */
    @Override
    public void setHistoryType(HistoryType type) {
        super.setHistoryType(type);
        for (IDataObserver<E> observer : observers) {
            observer.reset();
        }

        if (historyTask != null && !historyTask.isDone()) {
            historyTask.cancel(true);
        }

        if (sourceName != null && scheduler != null
                && historyType != HistoryType.CURRENT) {
            historyTask = initHistory(sourceName, getBatchedCallback());
        }
    }

}
