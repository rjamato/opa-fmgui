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
 *  File Name: FullController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/08/17 18:53:49  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/06/30 22:31:42  jijunwan
 *  Archive Log:    PR 129215 - Need short chart name to support pin capability
 *  Archive Log:    - use short name as pin card title
 *  Archive Log:    - improved pin argument to include full name and provide data source description
 *  Archive Log:    - fixed improper full name issues on trend charts
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/06/25 20:50:03  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - applied pin framework on dynamic cards that can have different data sources
 *  Archive Log:    - change to use port counter performance item
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/06/09 18:37:23  jijunwan
 *  Archive Log:    PR 129069 - Incorrect Help action
 *  Archive Log:    - moved help action from view to controller
 *  Archive Log:    - only enable help button when we have HelpID
 *  Archive Log:    - fixed incorrect HelpIDs
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/03/10 18:43:17  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/17 23:22:18  jijunwan
 *  Archive Log:    PR 127106 - Suggest to use same bucket range for Group Err Summary as shown in "opatop" command to plot performance graphs in FV
 *  Archive Log:     - changed error histogram chart to bar chart to show the new data ranges: 0-25%, 25-50%, 50-75%, 75-100% and 100+%
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/15 15:24:30  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/22 18:41:42  jijunwan
 *  Archive Log:    added DataType support for chart view
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/21 17:03:05  jijunwan
 *  Archive Log:    moved ChartsView and ChartsCard to common package
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/16 21:38:05  jijunwan
 *  Archive Log:    added 3 type error counters
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
import java.util.List;
import java.util.Map;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.ChartsCard;
import com.intel.stl.ui.common.view.ChartsView;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.model.DatasetDescription;
import com.intel.stl.ui.performance.item.AbstractPerformanceItem;
import com.intel.stl.ui.performance.item.IPerformanceItem;
import com.intel.stl.ui.performance.item.TopNItem;
import com.intel.stl.ui.performance.item.TrendItem;

public class BaseGroupController extends AbstractGroupController<GroupSource> {
    private ChartsCard trendCard, topNCard, histogramCard;

    /**
     * Description:
     * 
     * @param eventBus
     * @param trendItem
     * @param histogramItem
     * @param topNItem
     * @param sourceNames
     */
    @SuppressWarnings("unchecked")
    public BaseGroupController(MBassador<IAppEvent> eventBus, String name,
            TrendItem<GroupSource> trendItem,
            AbstractPerformanceItem<GroupSource> histogramItem,
            TopNItem topNItem) {
        super(eventBus, name, new IPerformanceItem[] { trendItem,
                histogramItem, topNItem });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.performance.AbstractGroupController#initCards
     * (java.util.Map)
     */
    @Override
    protected List<ChartsCard> initCards(Map<String, DatasetDescription> map) {
        List<ChartsCard> res = new ArrayList<ChartsCard>();
        if (allItems[0] != null) {
            trendCard = createTrendCard(allItems[0], map);
            res.add(trendCard);
        }

        if (allItems[1] != null) {
            histogramCard = createHistogramCard(allItems[1], map);
            res.add(histogramCard);
        }

        if (allItems[2] != null) {
            topNCard = createTopNCard(allItems[2], map);
            res.add(topNCard);
        }
        return res;
    }

    public void setHelpIDs(String trendHelpID, String histogramHelpID,
            String topNHelpID) {
        if (trendCard != null) {
            trendCard.setHelpID(trendHelpID);
        }
        if (histogramCard != null) {
            histogramCard.setHelpID(histogramHelpID);
        }
        if (topNCard != null) {
            topNCard.setHelpID(topNHelpID);
        }
    }

    protected ChartsCard createTrendCard(IPerformanceItem<GroupSource> item,
            Map<String, DatasetDescription> map) {
        return createOptionCard(item, map, true, false);
    }

    protected ChartsCard createHistogramCard(
            IPerformanceItem<GroupSource> item,
            Map<String, DatasetDescription> map) {
        return createCard(item, map);
    }

    protected ChartsCard createTopNCard(IPerformanceItem<GroupSource> item,
            Map<String, DatasetDescription> map) {
        return createCard(item, map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.AbstractGroupController#getItemView(com.
     * intel.stl.ui.performance.item.IPerformanceItem)
     */
    @Override
    protected ChartsView getItemView(IPerformanceItem<GroupSource> item) {
        if (allItems[0] == item) {
            return trendCard.getView();
        } else if (allItems[1] == item) {
            return histogramCard.getView();
        } else if (allItems[2] == item) {
            return topNCard.getView();
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.AbstractGroupController#getChartArgument
     * (com.intel.stl.ui.performance.item.IPerformanceItem)
     */
    @Override
    protected ChartArgument<GroupSource> getChartArgument(
            IPerformanceItem<GroupSource> item) {
        GroupChartArgument arg = new GroupChartArgument();
        String name = item.getName();
        arg.setName(name);
        arg.setFullName(item.getFullName());
        arg.setProvider(item.getCurrentProviderName().name());
        if (item.getType() != null) {
            arg.setDataType(item.getType());
        }
        if (item.getHistoryType() != null) {
            arg.setHistoryType(item.getHistoryType());
        }
        arg.setSources(item.getSources());
        return arg;
    }

}
