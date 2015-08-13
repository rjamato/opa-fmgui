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
 *  File Name: PerformanceGroupController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11.2.1  2015/08/12 15:26:41  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/02/13 23:05:35  jijunwan
 *  Archive Log:    PR 126911 - Even though HFI does not represent "Internal" data under opatop, FV still provides drop down for "Internal"
 *  Archive Log:     -- added a feature to be able to disable unsupported types
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/12 19:40:10  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/03 21:12:33  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/09 12:37:54  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/09/15 15:24:30  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/08/26 15:14:34  jijunwan
 *  Archive Log:    added refresh function to performance charts
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/22 18:38:40  jijunwan
 *  Archive Log:    introduced DatasetDescription to support short name and full name (description) for a dataset
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/21 17:30:44  jijunwan
 *  Archive Log:    renamed IDataObserver.Type to DataType, and put it under model package
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/21 17:03:05  jijunwan
 *  Archive Log:    moved ChartsView and ChartsCard to common package
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/17 16:25:39  jijunwan
 *  Archive Log:    improvement to support sleep mode so we can reduce FE traffic
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 15:09:00  jijunwan
 *  Archive Log:    new framework for performance data visualization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.ChartsCard;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.view.ChartsView;
import com.intel.stl.ui.common.view.OptionChartsView;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.ChartGroup;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.model.DatasetDescription;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.performance.item.IPerformanceItem;

public abstract class AbstractGroupController implements IGroupController {
    private final static boolean DEBUG = false;

    protected MBassador<IAppEvent> eventBus;

    protected int maxDataPoints = 10;

    protected DataType type;

    protected HistoryType historyType;

    private boolean isSleepMode;

    protected IPerformanceItem[] allItems;

    protected ChartGroup group;

    /**
     * Description:
     * 
     * @param trendName
     * @param topNName
     * @param histogramName
     */
    public AbstractGroupController(MBassador<IAppEvent> eventBus, String name,
            IPerformanceItem... items) {
        super();
        this.eventBus = eventBus;
        allItems = items;

        Map<String, DatasetDescription> map = initDataset();
        List<ChartsCard> cards = initCards(map);
        if (cards != null && !cards.isEmpty()) {
            group = new ChartGroup(name, cards.get(0).getView());
            for (ChartsCard card : cards) {
                group.addMember(new ChartGroup(card.getView()));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.IGroupController#setDataProvider(java.lang
     * .String)
     */
    @Override
    public void setDataProvider(String name) {
        for (IPerformanceItem item : allItems) {
            if (item != null) {
                item.setDataProvider(name);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.performance.IGroupController#setDataSources(java
     * .lang.String[])
     */
    @Override
    public void setDataSources(String... names) {
        for (IPerformanceItem item : allItems) {
            if (item != null) {
                item.setSources(names);
            }
        }
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(DataType type) {
        this.type = type;

        for (IPerformanceItem item : allItems) {
            if (item != null) {
                item.setType(type);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.IGroupController#setDisableDataType(com.
     * intel.stl.ui.model.DataType[])
     */
    @Override
    public void setDisabledDataTypes(DataType... types) {
        for (ChartGroup cg : group.getMembers()) {
            ChartsView view = cg.getChartView();
            if (view instanceof OptionChartsView) {
                ((OptionChartsView) view).setDisbaledDataTypes(types);
            }
        }
    }

    public void setHistoryType(HistoryType type) {
        this.historyType = type;

        for (IPerformanceItem item : allItems) {
            if (item != null) {
                item.setHistoryType(type);
            }
        }
    }

    protected Map<String, DatasetDescription> initDataset() {
        Map<String, DatasetDescription> map =
                new LinkedHashMap<String, DatasetDescription>();
        for (IPerformanceItem item : allItems) {
            if (item != null) {
                map.put(item.getName(), item.getDatasetDescription());
            }
        }
        return map;
    }

    /**
     * Description:
     * 
     * @param map
     */
    protected abstract List<ChartsCard> initCards(
            Map<String, DatasetDescription> map);

    protected ChartsCard createChartsCard(ChartsView view,
            Map<String, DatasetDescription> datasets, String... datasetNames) {
        List<DatasetDescription> lst = new ArrayList<DatasetDescription>();
        for (String name : datasetNames) {
            if (name != null) {
                lst.add(datasets.get(name));
            }
        }
        return new ChartsCard(view, eventBus, lst);
    }

    /**
     * @return the group
     */
    @Override
    public ChartGroup getGroup() {
        return group;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.performance.IGroupController#setContext(com.intel
     * .stl.ui.main.Context, com.intel.stl.ui.common.IProgressObserver)
     */
    @Override
    public void setContext(Context context, IProgressObserver observer) {
        for (IPerformanceItem item : allItems) {
            if (item != null) {
                item.setContext(context, null);
            }
        }

        if (observer != null) {
            observer.onFinish();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.IGroupController#onRefresh(com.intel.stl
     * .ui.common.IProgressObserver)
     */
    @Override
    public void onRefresh(IProgressObserver observer) {
        for (IPerformanceItem item : allItems) {
            if (item != null) {
                item.onRefresh(null);
            }

            if (observer != null && observer.isCancelled()) {
                break;
            }
        }

        if (observer != null) {
            observer.onFinish();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.performance.IGroupController#setSleepMode(boolean)
     */
    @Override
    public void setSleepMode(boolean b) {
        if (DEBUG) {
            System.out.println("[" + getGroup().getName() + "] sleep mode = "
                    + b);
        }

        isSleepMode = b;
        IPerformanceItem primary = getPrimaryItem();
        // primary should always be active because it will provide data for
        // sparkline display
        primary.setActive(true);
        for (IPerformanceItem item : allItems) {
            if (item != null && item != primary) {
                item.setActive(!b);
            }
        }
    }

    protected IPerformanceItem getPrimaryItem() {
        return allItems[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.performance.IGroupController#isSleepMode()
     */
    @Override
    public boolean isSleepMode() {
        return isSleepMode;
    }
}
