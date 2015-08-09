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
 *  File Name: TrafficChartsCreator.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.1  2015/05/17 18:30:44  jijunwan
 *  Archive Log:    PR 127700 - Delta data on host performance display is accumulating
 *  Archive Log:    - corrected delta value calculation
 *  Archive Log:    - changed to display data/pkts rate rather than delta on chart and table
 *  Archive Log:    - updated chart unit to show rate
 *  Archive Log:    - renamed the following classes to reflect we are dealing with rate
 *  Archive Log:      DataChartRangeUpdater -> DataRateChartRangeUpdater
 *  Archive Log:      PacketChartRangeUpdater -> PacketRateChartRangeUpdater
 *  Archive Log:      DataChartScaleGroupManager -> DataRateChartScaleGroupManager
 *  Archive Log:      PacketChartScaleGroupManager -> PacketRateChartScaleGroupManager
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/18 17:30:45  jypak
 *  Archive Log:    PR 126999 Graph names are changed to include 'Delta' in the middle of the names. Also, added tool tips to the title label, so when a user hover the mouse to the title (for combo box selection of charts, hover on the label values), the explanation about the charts pops up.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/02 22:16:58  jijunwan
 *  Archive Log:    fixed string comparison issues
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/22 18:39:43  jijunwan
 *  Archive Log:    moved IChartCreator to common package
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/29 03:07:05  jijunwan
 *  Archive Log:    minor adjustment on performance subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/09 14:17:17  jijunwan
 *  Archive Log:    moved JFreeChart to view side, controller side only take care dataset
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor.view;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.IChartCreator;

public class TrafficChartsCreator implements IChartCreator {
    private static final TrafficChartsCreator instance =
            new TrafficChartsCreator();

    private TrafficChartsCreator() {
    }

    public static TrafficChartsCreator instance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.view.IChartCreator#createChart(java.lang.String,
     * org.jfree.data.general.Dataset)
     */
    @Override
    public JFreeChart createChart(String name, Dataset dataset) {
        if (STLConstants.K0828_REC_PACKETS_RATE.getValue().equals(name)
                || STLConstants.K0829_TRAN_PACKETS_RATE.getValue()
                        .equals(name)) {
            return getPacketsRateChart(dataset);
        } else if (STLConstants.K0830_REC_DATA_RATE.getValue().equals(name)
                || STLConstants.K0831_TRAN_DATA_RATE.getValue().equals(name)) {
            return getDataRateChart(dataset);
        }
        return null;
    }

    protected JFreeChart getPacketsRateChart(Dataset dataset) {
        JFreeChart chart =
                ComponentFactory.createXYAreaChart(
                        STLConstants.K0035_TIME.getValue(),
                        STLConstants.K0750_PPS.getValue(),
                        (XYDataset) dataset, false);

        NumberAxis rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);
        return chart;
    }

    protected JFreeChart getDataRateChart(Dataset dataset) {
        JFreeChart chart =
                ComponentFactory.createXYAreaChart(
                        STLConstants.K0035_TIME.getValue(),
                        STLConstants.K0703_MB_PER_SEC.getValue(), (XYDataset) dataset,
                        false);

        NumberAxis rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);
        return chart;
    }

}
