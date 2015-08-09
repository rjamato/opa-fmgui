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
 *  File Name: HistogramItem.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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

public abstract class HistogramItem extends AbstractPerformanceItem {
    protected XYSeriesCollection dataset;

    public HistogramItem(String fullName) {
        this(fullName, DEFAULT_DATA_POINTS);
    }

    /**
     * Description:
     * 
     * @param name
     * @param maxDataPoints
     */
    public HistogramItem(String fullName, int maxDataPoints) {
        super(STLConstants.K0079_HISTOGRAM.getValue(), fullName, maxDataPoints);
        initDataProvider();
        initDataset();
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
                dataset.setNotify(false);
                dataset.removeAllSeries();
                dataset.addSeries(xyseries);
                dataset.setIntervalPositionFactor(0);
                dataset.setIntervalWidth(range / values.length);
                dataset.setNotify(true);
            }
        });
    }

    @Override
    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                if (dataset != null) {
                    dataset.removeAllSeries();
                }
            }
        });
    }
}
