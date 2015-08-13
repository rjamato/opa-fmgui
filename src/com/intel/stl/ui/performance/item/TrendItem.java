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
 *  File Name: TrendItem.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11.2.1  2015/08/12 15:26:55  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/02/17 23:22:14  jijunwan
 *  Archive Log:    PR 127106 - Suggest to use same bucket range for Group Err Summary as shown in "opatop" command to plot performance graphs in FV
 *  Archive Log:     - changed error histogram chart to bar chart to show the new data ranges: 0-25%, 25-50%, 50-75%, 75-100% and 100+%
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/12 19:40:02  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/03 21:12:29  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/12/11 18:49:01  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/13 15:07:54  jijunwan
 *  Archive Log:    improved debug info
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/09/09 18:26:07  jijunwan
 *  Archive Log:    1) introduced ISourceObserver to provide flexibility on dataset preparation when we change data sources
 *  Archive Log:    2) Applied ISourceObserver to fix the synchronization issue happens when we switch data sources
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/08/26 20:52:32  jijunwan
 *  Archive Log:    cleanup
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/26 15:14:32  jijunwan
 *  Archive Log:    added refresh function to performance charts
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jfree.data.general.Dataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Util;

/**
 * This controller collects data from a data provider, then applies a data
 * observer to process the data, and then update its internal dataset that is
 * used for a JFreeChart. <br>
 * 
 * To use this class to create trend data for a JFreeChart, we should follow the
 * following steps:
 * <ol>
 * <li>Construct the class with <code>name</code> and <code>maxDataPoints</code>
 * <li>Provide data provider and a data observer that define how we collect and
 * process data
 * <li>Set sources to specify the target data sources, such as Device Groups or
 * VFabrics, from which where we will collect data
 * </ol>
 */
public abstract class TrendItem extends AbstractPerformanceItem {
    private final static Logger log = LoggerFactory.getLogger(TrendItem.class);

    private final static boolean DEBUG = false;

    protected TimeSeriesCollection dataset;

    protected List<TimeSeries> trendSeries;

    public TrendItem(String fullName) {
        this(fullName, DEFAULT_DATA_POINTS);
    }

    /**
     * Description:
     * 
     * @param name
     *            Name of this TrendItem, it will be the name displayed on UI
     * @param maxDataPoints
     *            the history length of this trend. It's defined by number of
     *            data points on a Chart
     */
    public TrendItem(String fullName, int maxDataPoints) {
        super(STLConstants.K0078_TREND.getValue(), fullName, maxDataPoints);
        initDataProvider();
        initDataset();
    }

    /**
     * Description:
     * 
     */
    protected void initDataset() {
        dataset = createTrendDataset();
        if (dataset != null) {
            trendSeries = createTrendSeries(sourceNames);
            for (int i = trendSeries.size() - 1; i >= 0; i--) {
                dataset.addSeries(trendSeries.get(i));
            }
        }
    }

    protected TimeSeriesCollection createTrendDataset() {
        return new TimeSeriesCollection();
    }

    protected List<TimeSeries> createTrendSeries(String[] series) {
        List<TimeSeries> res = new ArrayList<TimeSeries>();
        if (series != null) {
            for (String name : series) {
                TimeSeries all = new TimeSeries(name);
                res.add(all);
            }
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.performance.IPerformanceItem#getDataset()
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.item.AbstractPerformanceItem#sourcesRemoved
     * (java.lang.String[])
     */
    @Override
    public void sourcesRemoved(String[] names) {
        if (DEBUG) {
            System.out.println(currentProviderName + ":" + getName() + " "
                    + getFullName() + ": sourcesRemoved "
                    + Arrays.toString(names));
        }
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                dataset.removeAllSeries();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.item.AbstractPerformanceItem#sourcesToAdd
     * (java.lang.String[])
     */
    @Override
    public void sourcesToAdd(final String[] names) {
        if (DEBUG) {
            System.out.println(currentProviderName + ":" + getName() + " "
                    + getFullName() + ": sourcesToAdd "
                    + Arrays.toString(names));
        }
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                if (names != null && names.length > 0) {
                    trendSeries = createTrendSeries(names);
                    for (int i = trendSeries.size() - 1; i >= 0; i--) {
                        dataset.addSeries(trendSeries.get(i));
                    }
                }
            }
        });
    }

    public void updateTrend(final long value, final Date date, final String name) {
        if (dataset == null) {
            return;
        }

        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                TimeSeries series = dataset.getSeries(name);
                if (series != null) {
                    series.addOrUpdate(new Second(date), value);
                    if (series.getItemCount() > maxDataPoints) {
                        series.delete(0, 0);
                    } else {
                        series.fireSeriesChanged();
                    }
                } else {
                    log.warn(currentProviderName + ":" + getName() + " "
                            + getFullName() + ": Couldn't find TimeSeries '"
                            + name + "'");
                    // throw new RuntimeException("Couldn't find TimeSeries '"
                    // + name + "'");
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.performance.IPerformanceItem#clear()
     */
    @Override
    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                if (dataset != null) {
                    for (int i = 0; i < dataset.getSeriesCount(); i++) {
                        dataset.getSeries(i).clear();
                    }
                }
            }
        });
    }
}
