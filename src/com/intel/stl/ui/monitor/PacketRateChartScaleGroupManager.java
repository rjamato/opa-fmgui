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
 *  File Name: 
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1.2.2  2015/05/17 18:30:42  jijunwan
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
 *  Archive Log:    Revision 1.2  2014/06/09 21:40:56  jijunwan
 *  Archive Log:    made Chart Group Manager more general
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/06 13:31:06  jypak
 *  Archive Log:    Performance-Performance subpage updates.
 *  Archive Log:    1. Synchronize y-axis(range axis) bound for a group of charts (packet, data).
 *  Archive Log:    2. Auto conversion of y-axis label title and tick label based on the max value of data in the series.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.intel.stl.ui.main.view.IChartRangeUpdater;
import com.intel.stl.ui.main.view.PacketRateChartRangeUpdater;

public class PacketRateChartScaleGroupManager extends ChartScaleGroupManager<TimeSeriesCollection> {

    /* (non-Javadoc)
     * @see com.intel.stl.ui.monitor.ChartScaleGroupManager#getChartRangeUpdater()
     */
    @Override
    IChartRangeUpdater getChartRangeUpdater() {
        return new PacketRateChartRangeUpdater();
    }

    /* (non-Javadoc)
     * @see com.intel.stl.ui.monitor.ChartScaleGroupManager#getMin(org.jfree.data.general.Dataset)
     */
    @Override
    long[] getMinMax(TimeSeriesCollection dataset) {
        long lower = Long.MAX_VALUE;
        long upper = Long.MIN_VALUE;
        
        for(int i=0; i<dataset.getSeriesCount(); i++){
            TimeSeries series = dataset.getSeries(i);
            
            lower = (long)Math.min(lower, series.getMinY());
            upper = (long)Math.max(upper, series.getMaxY());     
        }
        
        return new long[]{lower, upper};
    }

}
