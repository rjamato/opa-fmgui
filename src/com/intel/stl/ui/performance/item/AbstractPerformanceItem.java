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
 *  File Name: AbstractPerformanceItem.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.14.2.1  2015/08/12 15:26:55  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/02/17 23:22:14  jijunwan
 *  Archive Log:    PR 127106 - Suggest to use same bucket range for Group Err Summary as shown in "opatop" command to plot performance graphs in FV
 *  Archive Log:     - changed error histogram chart to bar chart to show the new data ranges: 0-25%, 25-50%, 50-75%, 75-100% and 100+%
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/02/13 20:56:45  jijunwan
 *  Archive Log:    changed to set data type or history type for all data providers or observers
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/02/12 19:40:02  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/02/11 21:15:03  jypak
 *  Archive Log:    1. For 'current' history scope, default max data points need to be set.
 *  Archive Log:    2. History icon fixed.
 *  Archive Log:    3. Home Page performance section trend charts should show history scope selections.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/03 21:12:29  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/10/13 15:07:54  jijunwan
 *  Archive Log:    improved debug info
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/09 12:38:38  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/09/09 18:26:07  jijunwan
 *  Archive Log:    1) introduced ISourceObserver to provide flexibility on dataset preparation when we change data sources
 *  Archive Log:    2) Applied ISourceObserver to fix the synchronization issue happens when we switch data sources
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/08/26 15:14:32  jijunwan
 *  Archive Log:    added refresh function to performance charts
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/22 18:45:02  jijunwan
 *  Archive Log:    renamed description to fullName
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/22 18:38:38  jijunwan
 *  Archive Log:    introduced DatasetDescription to support short name and full name (description) for a dataset
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/21 17:30:46  jijunwan
 *  Archive Log:    renamed IDataObserver.Type to DataType, and put it under model package
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/17 16:25:36  jijunwan
 *  Archive Log:    improvement to support sleep mode so we can reduce FE traffic
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 15:08:56  jijunwan
 *  Archive Log:    new framework for performance data visualization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance.item;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jfree.data.general.Dataset;

import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.model.DatasetDescription;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.performance.observer.IDataObserver;
import com.intel.stl.ui.performance.provider.IDataProvider;
import com.intel.stl.ui.performance.provider.ISourceObserver;

public abstract class AbstractPerformanceItem implements IPerformanceItem,
        ISourceObserver {
    public final static int DEFAULT_DATA_POINTS = 100;

    private final static boolean DEBUG = false;

    // Make it volatile so that the value set by other thread can be used.
    // Updating chart time series is done in EDT thread so this variable has to
    // be a volatile.
    protected volatile int maxDataPoints;

    protected final String name;

    protected String description;

    protected String[] sourceNames;

    protected DatasetDescription datasetDescription;

    private final Map<String, IDataProvider<?>> providers =
            new HashMap<String, IDataProvider<?>>();

    private final Map<String, IDataObserver<?>> observers =
            new HashMap<String, IDataObserver<?>>();

    protected String currentProviderName;

    private Context currentContext;

    private boolean isActive;

    public AbstractPerformanceItem(String name, String fullName) {
        this(name, fullName, DEFAULT_DATA_POINTS);
    }

    /**
     * Description:
     * 
     * @param name
     * @param observer
     */
    public AbstractPerformanceItem(String name, String fullName,
            int maxDataPoints) {
        super();
        this.name = name;
        this.description = fullName;
        this.maxDataPoints = maxDataPoints;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    @Override
    public String getFullName() {
        return description;
    }

    /**
     * @return the maxDataPoints
     */
    public int getMaxDataPoints() {
        return maxDataPoints;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.item.IPerformanceItem#getDatasetDescription
     * ()
     */
    @Override
    public DatasetDescription getDatasetDescription() {
        if (datasetDescription == null && getDataset() != null) {
            datasetDescription =
                    new DatasetDescription(name, description, getDataset(),
                            isJumpable());
        }
        return datasetDescription;
    }

    protected abstract void initDataProvider();

    protected abstract Dataset getDataset();

    protected abstract boolean isJumpable();

    @Override
    public <E> void registerDataProvider(String name,
            IDataProvider<E> provider, IDataObserver<E> observer) {
        providers.put(name, provider);
        observers.put(name, observer);
        provider.addObserver(observer);
        provider.addSourceObserver(this);
        if (currentProviderName == null) {
            currentProviderName = name;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.item.IPerformanceItem#setDataProvider(java
     * .lang.String)
     */
    @Override
    public void setDataProvider(String name) {
        if (currentProviderName != null && currentProviderName.equals(name)) {
            return;
        }

        IDataProvider<?> provider = getCurrentProvider();
        if (provider != null) {
            provider.clear();
        }

        currentProviderName = name;
        if (sourceNames != null) {
            clear();
        }
        sourceNames = null;
        // if (sourceNames!=null) {
        // setSources(sourceNames);
        // }
    }

    protected IDataProvider<?> getCurrentProvider() {
        if (currentProviderName == null) {
            return null;
        } else {
            return providers.get(currentProviderName);
        }
    }

    protected IDataObserver<?> getCurrentObserver() {
        if (currentProviderName == null) {
            return null;
        } else {
            return observers.get(currentProviderName);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.performance.IPerformanceItem#setSources(java.
     * lang.String[])
     */
    @Override
    public void setSources(String... sourceNames) {
        if (DEBUG) {
            System.out.println("[" + currentProviderName + ":" + getName()
                    + " " + getFullName() + "] setSources "
                    + Arrays.toString(sourceNames));
        }

        if (sameSources(sourceNames)) {
            onRefresh(null);
            return;
        }

        clear();

        this.sourceNames = sourceNames;
        IDataProvider<?> provider = getCurrentProvider();
        if (provider != null && isActive) {
            provider.setContext(currentContext, null, sourceNames);
        }
    }

    protected boolean sameSources(String[] sources) {
        if (sourceNames == null) {
            return sources == null;
        } else if (sources == null) {
            return true;
        }

        Set<String> set1 = new HashSet<String>(Arrays.asList(sourceNames));
        Set<String> set2 = new HashSet<String>(Arrays.asList(sources));
        return set1.equals(set2);
    }

    protected String getPrimarySource() {
        if (sourceNames == null || sourceNames.length == 0) {
            return null;
        } else {
            return sourceNames[0];
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.ISourceObserver#sourcesToRemove
     * (java.lang.String[])
     */
    @Override
    public void sourcesToRemove(String[] names) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.ISourceObserver#sourcesToAdd(java
     * .lang.String[])
     */
    @Override
    public void sourcesToAdd(String[] names) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.ISourceObserver#sourcesRemoved(
     * java.lang.String[])
     */
    @Override
    public void sourcesRemoved(String[] names) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.provider.ISourceObserver#sourcesAdded(java
     * .lang.String[])
     */
    @Override
    public void sourcesAdded(String[] names) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.performance.IPerformanceItem#setContext(com.intel
     * .stl.ui.main.Context, com.intel.stl.ui.common.IProgressObserver)
     */
    @Override
    public void setContext(Context context, IProgressObserver observer) {
        if (DEBUG) {
            System.out.println("[" + currentProviderName + ":" + getName()
                    + " " + getFullName() + "] setContext " + context
                    + " isActive = " + isActive);
        }

        clear();
        IDataProvider<?> provider = getCurrentProvider();
        if (provider != null && isActive) {
            provider.setContext(context, observer, sourceNames);
        }

        if (observer != null) {
            observer.onFinish();
        }
        if (currentContext != context) {
            currentContext = context;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.item.IPerformanceItem#onRefresh(com.intel
     * .stl.ui.common.IProgressObserver)
     */
    @Override
    public void onRefresh(IProgressObserver observer) {
        if (DEBUG) {
            System.out.println("[" + currentProviderName + ":" + getName()
                    + " " + getFullName() + "] refresh");
        }
        IDataProvider<?> provider = getCurrentProvider();
        if (provider != null && isActive) {
            provider.onRefresh(observer);
        }
        if (observer != null) {
            observer.onFinish();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.performance.IPerformanceItem#setType(com.intel
     * .stl.ui.common.performance.IDataObserver.Type)
     */
    @Override
    public void setType(DataType type) {
        clear();
        for (IDataObserver<?> observer : observers.values()) {
            observer.setType(type);
        }
    }

    /**
     * 
     * Description: Set HistoryType.
     * 
     * @param type
     */
    @Override
    public void setHistoryType(HistoryType type) {
        clear();

        if (currentContext != null && type != HistoryType.CURRENT) {
            int refreshRate =
                    currentContext.getTaskScheduler().getRefreshRate();
            maxDataPoints = type.getMaxDataPoints(refreshRate);
        } else {
            maxDataPoints = DEFAULT_DATA_POINTS;
        }

        for (IDataProvider<?> provider : providers.values()) {
            provider.setHistoryType(type);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.item.IPerformanceItem#setActive(boolean)
     */
    @Override
    public void setActive(boolean b) {
        if (DEBUG) {
            System.out.println("  <" + currentProviderName + ":" + getName()
                    + " " + getFullName() + "> active = " + b);
        }

        if (isActive == b) {
            return;
        }

        isActive = b;
        IDataProvider<?> provider = getCurrentProvider();
        if (provider != null) {
            if (b && currentContext != null) {
                if (DEBUG) {
                    System.out.println("    <" + currentProviderName + ":"
                            + getName() + " " + getFullName()
                            + "> to set active for " + provider + " with "
                            + currentContext);
                }
                provider.setContext(currentContext, null, sourceNames);
            } else {
                provider.clear();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.performance.item.IPerformanceItem#isActive()
     */
    @Override
    public boolean isActive() {
        return isActive;
    }

}
