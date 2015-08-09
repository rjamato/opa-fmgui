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
 *  File Name: BWHistogramItem.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/17 23:22:14  jijunwan
 *  Archive Log:    PR 127106 - Suggest to use same bucket range for Group Err Summary as shown in "opatop" command to plot performance graphs in FV
 *  Archive Log:     - changed error histogram chart to bar chart to show the new data ranges: 0-25%, 25-50%, 50-75%, 75-100% and 100+%
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/30 04:12:58  jijunwan
 *  Archive Log:    PR 126775 - "Bubble" error metric graph is not being plotted even though "opatop" shows bubble errors
 *  Archive Log:     - the chart used wrong data. corrected to use bubble counter
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 21:38:04  jijunwan
 *  Archive Log:    added 3 type error counters
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

import com.intel.stl.api.performance.ErrBucketBean;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.monitor.TreeNodeType;
import com.intel.stl.ui.performance.observer.ErrorHistogramDataObserver;
import com.intel.stl.ui.performance.observer.VFErrorHistogramDataObserver;
import com.intel.stl.ui.performance.provider.CombinedGroupInfoProvider;
import com.intel.stl.ui.performance.provider.CombinedVFInfoProvider;

/**
 * Sma Congestion Errors Histogram
 */
public class BBHistogramItem extends ErrHistogramItem {

    public BBHistogramItem() {
        super(STLConstants.K0488_BUBBLE_HISTOGRAM.getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.performance.item.HistogramItem#initDataProvider()
     */
    @Override
    protected void initDataProvider() {
        CombinedGroupInfoProvider provider = new CombinedGroupInfoProvider();
        ErrorHistogramDataObserver observer =
                new ErrorHistogramDataObserver(this) {
                    @Override
                    protected long getValue(ErrBucketBean err) {
                        return err.getBubbleErrors();
                    }
                };
        registerDataProvider(TreeNodeType.DEVICE_GROUP.name(), provider,
                observer);

        CombinedVFInfoProvider vfProvider = new CombinedVFInfoProvider();
        VFErrorHistogramDataObserver vfObserver =
                new VFErrorHistogramDataObserver(this) {
                    @Override
                    protected long getValue(ErrBucketBean err) {
                        return err.getBubbleErrors();
                    }
                };
        registerDataProvider(TreeNodeType.VIRTUAL_FABRIC.name(), vfProvider,
                vfObserver);
    }

}
