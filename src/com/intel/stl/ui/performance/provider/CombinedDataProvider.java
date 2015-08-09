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
 *  File Name: AbstractDataProvider.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.12  2015/02/12 19:40:11  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/02/11 21:14:57  jypak
 *  Archive Log:    1. For 'current' history scope, default max data points need to be set.
 *  Archive Log:    2. History icon fixed.
 *  Archive Log:    3. Home Page performance section trend charts should show history scope selections.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/10 23:25:34  jijunwan
 *  Archive Log:    removed refresh rate on caller side since we should be able to directly get it from task scheduler
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/06 20:49:33  jypak
 *  Archive Log:    1. TaskScheduler changed to handle two threads.
 *  Archive Log:    2. All four(VFInfo, VFPortCounters, GroupInfo, PortCounters) attributes history query related updates.
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.publisher.CallbackAdapter;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;

public abstract class CombinedDataProvider<E> extends AbstractDataProvider<E[]> {
    protected Set<String> sourceNames = new HashSet<String>();

    protected List<Task<E>> tasks;

    protected Future<Void> historyTask;

    private ICallback<E[]> callback;

    /**
     * Description:
     * 
     * @param sourceNames
     */
    public CombinedDataProvider() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.AbstractDataProvider#getSourceNames
     * ()
     */
    @Override
    protected String[] getSourceNames() {
        synchronized (sourceNames) {
            return sourceNames.toArray(new String[0]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.AbstractDataProvider#sameSources
     * (java.lang.String[])
     */
    @Override
    protected boolean sameSources(String[] names) {
        Set<String> newSet = new HashSet<String>();
        if (names != null && names.length > 0) {
            newSet.addAll(Arrays.asList(names));
        }
        synchronized (sourceNames) {
            return newSet.equals(sourceNames);
        }
    }

    @Override
    protected void setSources(String[] names) {
        synchronized (sourceNames) {
            sourceNames.clear();
            if (names != null && names.length > 0) {
                sourceNames.addAll(Arrays.asList(names));
                if (historyType != null && historyType != HistoryType.CURRENT) {
                    historyTask = initHistory(names, getCallback());
                }
                tasks = registerTasks(names, getCallback());
                onRefresh(null);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.IDataProvider#onRefresh(com.intel
     * .stl.ui.common.IProgressObserver)
     */
    @Override
    public void onRefresh(IProgressObserver observer) {
        if (scheduler == null) {
            return;
        }
        scheduler.submitToBackground(new Runnable() {
            @Override
            public void run() {
                String[] sources = getSourceNames();
                if (sources.length > 0) {
                    E[] result = refresh(sources);
                    if (sameSources(sources)) {
                        getCallback().onDone(result);
                    }
                }
            }
        });
    }

    protected ICallback<E[]> getCallback() {
        if (callback == null) {
            callback = new CallbackAdapter<E[]>() {
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
                        fireNewData(result);
                    }
                }
            };
        }
        return callback;
    }

    protected abstract E[] refresh(String[] sourceNames);

    protected abstract List<Task<E>> registerTasks(String[] sourceNames,
            ICallback<E[]> callback);

    protected abstract void deregisterTasks(List<Task<E>> task,
            ICallback<E[]> callback);

    @Override
    public void clearSources() {
        if (scheduler != null) {
            if (tasks != null) {
                deregisterTasks(tasks, callback);
            }

            if (historyTask != null) {
                historyTask.cancel(true);
            }
        }
        synchronized (sourceNames) {
            sourceNames.clear();
        }
    }

    protected abstract Future<Void> initHistory(String[] names,
            ICallback<E[]> callback);

    /**
     * 
     * Description:TrendItem set HistoryType to CombinedDataProvider to
     * calculate the maxDataPoints.
     * 
     * @param type
     */
    @Override
    public void setHistoryType(HistoryType type) {
        super.setHistoryType(type);

        if (historyTask != null && !historyTask.isDone()) {
            historyTask.cancel(true);
        }

        String[] sources = getSourceNames();
        if (sources.length > 0 && historyType != HistoryType.CURRENT) {
            historyTask = initHistory(sources, getCallback());
        }
    }
}
