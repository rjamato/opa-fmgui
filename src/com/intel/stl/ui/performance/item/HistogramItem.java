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
 *  File Name: HistogramItem.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/08/17 18:53:43  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/06/30 22:28:49  jijunwan
 *  Archive Log:    PR 129215 - Need short chart name to support pin capability
 *  Archive Log:    - introduced short name to performance items
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/06/25 20:42:13  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - improved PerformanceItem to support port counters
 *  Archive Log:    - improved PerformanceItem to use generic ISource to describe data source
 *  Archive Log:    - improved PerformanceItem to use enum DataProviderName to describe data provider name
 *  Archive Log:    - improved PerformanceItem to support creating a copy of PerformanceItem
 *  Archive Log:    - improved TrendItem to share scale with other charts
 *  Archive Log:    - improved SimpleDataProvider to support hsitory data
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/17 23:22:14  jijunwan
 *  Archive Log:    PR 127106 - Suggest to use same bucket range for Group Err Summary as shown in "opatop" command to plot performance graphs in FV
 *  Archive Log:     - changed error histogram chart to bar chart to show the new data ranges: 0-25%, 25-50%, 50-75%, 75-100% and 100+%
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/22 18:45:02  jijunwan
 *  Archive Log:    renamed description to fullName
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/22 18:38:38  jijunwan
 *  Archive Log:    introduced DatasetDescription to support short name and full name (description) for a dataset
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

import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.performance.GroupSource;

public abstract class HistogramItem extends
        AbstractPerformanceItem<GroupSource> {
    protected XYSeriesCollection dataset;

    private final Object copyCritical = new Object();

    public HistogramItem(String shortName, String fullName) {
        this(shortName, fullName, DEFAULT_DATA_POINTS);
    }

    /**
     * Description:
     * 
     * @param sourceName
     * @param maxDataPoints
     */
    public HistogramItem(String shortName, String fullName, int maxDataPoints) {
        super(STLConstants.K0079_HISTOGRAM.getValue(), shortName, fullName,
                maxDataPoints);
        initDataProvider();
        initDataset();
    }

    public HistogramItem(HistogramItem item) {
        super(item);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.item.AbstractPerformanceItem#copyDataset
     * (com.intel.stl.ui.performance.item.AbstractPerformanceItem)
     */
    @Override
    protected void copyDataset(AbstractPerformanceItem<GroupSource> item) {
        try {
            HistogramItem hi = (HistogramItem) item;
            synchronized (hi.copyCritical) {
                dataset = (XYSeriesCollection) hi.dataset.clone();
            }
        } catch (CloneNotSupportedException e) {
            // shouldn't happen
            e.printStackTrace();
        }
    }

    protected void initDataset() {
        dataset = createHistogramDataset();
    }

    protected XYSeriesCollection createHistogramDataset() {
        return new XYSeriesCollection();
    }

    @Override
    public Dataset getDataset() {
        return dataset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.item.AbstractPerformanceItem#isJumpable()
     */
    @Override
    protected boolean isJumpable() {
        return false;
    }

    public void updateHistogram(final int[] values, final double range) {
        if (dataset == null || values == null || values.length <= 1) {
            return;
        }

        final XYSeries xyseries = new XYSeries(name);
        double x = 0;
        double step = range / values.length;
        for (int i = 0; i < values.length; i++) {
            xyseries.add(x, values[i]);
            x += step;
        }

        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                synchronized (copyCritical) {
                    dataset.setNotify(false);
                    dataset.removeAllSeries();
                    dataset.addSeries(xyseries);
                    dataset.setIntervalPositionFactor(0);
                    dataset.setIntervalWidth(range / values.length);
                    dataset.setNotify(true);
                }
            }
        });
    }

    @Override
    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                synchronized (copyCritical) {
                    if (dataset != null) {
                        dataset.removeAllSeries();
                    }
                }
            }
        });
    }
}
