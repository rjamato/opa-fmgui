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
 *  Archive Log:    Revision 1.8.2.1  2015/08/12 15:27:14  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/12 19:40:11  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/10 23:25:34  jijunwan
 *  Archive Log:    removed refresh rate on caller side since we should be able to directly get it from task scheduler
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/10 21:26:01  jypak
 *  Archive Log:    1. Introduced SwingWorker for history query initialization for progress status updates.
 *  Archive Log:    2. Fixed the list of future for history query in TaskScheduler. Now it can have all the Future entries created.
 *  Archive Log:    3. When selecting history type, just cancel the history query not sheduled query.
 *  Archive Log:    4. The refresh rate is now from user settings not from the config api.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/03 21:12:34  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/21 21:19:10  rjtierne
 *  Archive Log:    Removed individual refresh rates for task registration. Now using
 *  Archive Log:    refresh rate supplied by user input in preferences wizard.
 *  Archive Log:    Reinitialization of scheduler service not yet implemented.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/13 15:07:29  jijunwan
 *  Archive Log:    fixed synchronization issues on performance charts
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/18 20:55:09  fernande
 *  Archive Log:    Changed to set the refresh rate to the PM sweep interval, if PM configuration available. Later we need to see how this value can set by a user and saved (maybe in UserSettings?)
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/09 18:26:06  jijunwan
 *  Archive Log:    1) introduced ISourceObserver to provide flexibility on dataset preparation when we change data sources
 *  Archive Log:    2) Applied ISourceObserver to fix the synchronization issue happens when we switch data sources
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance.provider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.performance.observer.IDataObserver;
import com.intel.stl.ui.publisher.TaskScheduler;

public abstract class AbstractDataProvider<E> implements IDataProvider<E> {
    private final static boolean DEBUG = false;

    protected TaskScheduler scheduler;

    protected List<IDataObserver<E>> observers =
            new CopyOnWriteArrayList<IDataObserver<E>>();

    protected List<ISourceObserver> sourceObservers =
            new CopyOnWriteArrayList<ISourceObserver>();

    protected HistoryType historyType;

    @Override
    public void setContext(Context context, IProgressObserver progressObserver,
            String... sourceNames) {
        if (DEBUG) {
            System.out.println("setContext " + context + " with sources "
                    + Arrays.toString(sourceNames));
        }

        if (context == null) {
            clear();
            return;
        }

        boolean sameSources = sameSources(sourceNames);
        if (!hasScheduler(context)) {
            clear();
            scheduler = context.getTaskScheduler();
        } else if (sameSources) {
            onRefresh(null);
            return;
        } else {
            clear();
        }

        fireSourcesToAdd(sourceNames);
        try {
            setSources(sourceNames);
        } finally {
            fireSourcesAdded(sourceNames);
        }
    }

    protected abstract String[] getSourceNames();

    protected abstract boolean sameSources(String[] names);

    protected abstract void setSources(String[] names);

    protected boolean hasScheduler(Context context) {
        return scheduler != null && context.getTaskScheduler() == scheduler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.performance.IDataProvider#addObserver(com.intel
     * .stl.ui.common.performance.IDataObserver)
     */
    @Override
    public void addObserver(IDataObserver<E> observer) {
        observers.add(observer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.performance.IDataProvider#removeObserver(com.
     * intel.stl.ui.common.performance.IDataObserver)
     */
    @Override
    public void removeObserver(IDataObserver<E> observer) {
        observers.remove(observer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.IDataProvider#addSourceObserver
     * (com.intel.stl.ui.performance.provider.ISourceObserver)
     */
    @Override
    public void addSourceObserver(ISourceObserver observer) {
        sourceObservers.add(observer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.IDataProvider#removeSourceObserver
     * (com.intel.stl.ui.performance.provider.ISourceObserver)
     */
    @Override
    public void removeSourceObserver(ISourceObserver observer) {
        sourceObservers.remove(observer);
    }

    protected void fireNewData(E data) {
        for (IDataObserver<E> observer : observers) {
            observer.processData(data);
        }
    }

    protected void fireSourcesToRemove(String[] names) {
        for (ISourceObserver observer : sourceObservers) {
            observer.sourcesToRemove(names);
        }
    }

    protected void fireSourcesToAdd(String[] names) {
        for (ISourceObserver observer : sourceObservers) {
            observer.sourcesToAdd(names);
        }
    }

    protected void fireSourcesRemoved(String[] names) {
        for (ISourceObserver observer : sourceObservers) {
            observer.sourcesRemoved(names);
        }
    }

    protected void fireSourcesAdded(String[] names) {
        for (ISourceObserver observer : sourceObservers) {
            observer.sourcesAdded(names);
        }
    }

    @Override
    public void setHistoryType(HistoryType type) {
        this.historyType = type;
    }

    /**
     * @return the historyType
     */
    public HistoryType getHistoryType() {
        return historyType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.performance.provider.IDataProvider#clear()
     */
    @Override
    public void clear() {
        String[] oldSources = getSourceNames();
        if (oldSources != null && oldSources.length >= 0) {
            fireSourcesToRemove(oldSources);
        }
        try {
            clearSources();
        } finally {
            if (oldSources != null && oldSources.length >= 0) {
                fireSourcesRemoved(oldSources);
            }
        }
    }

    protected abstract void clearSources();

}
