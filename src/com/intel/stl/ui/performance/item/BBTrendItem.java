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
 *  File Name: BWTrendItem.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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

import com.intel.stl.api.performance.ErrStatBean;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.monitor.TreeNodeType;
import com.intel.stl.ui.performance.observer.ErrorDataObserver;
import com.intel.stl.ui.performance.observer.VFErrorDataObserver;
import com.intel.stl.ui.performance.provider.CombinedGroupInfoProvider;
import com.intel.stl.ui.performance.provider.CombinedVFInfoProvider;

/**
 * Bubble Errors Trend
 */
public class BBTrendItem extends TrendItem {

    /**
     * Description:
     * 
     * @param name
     */
    public BBTrendItem() {
        this(DEFAULT_DATA_POINTS);
    }

    /**
     * Description:
     * 
     * @param name
     * @param maxDataPoints
     */
    public BBTrendItem(int maxDataPoints) {
        super(STLConstants.K0487_BUBBLE_ERROR.getValue(), maxDataPoints);
    }

    @Override
    protected void initDataProvider() {
        CombinedGroupInfoProvider provider = new CombinedGroupInfoProvider();
        ErrorDataObserver observer = new ErrorDataObserver(this) {
            @Override
            protected long getValue(ErrStatBean error) {
                return error.getErrorMaximums().getBubbleErrors();
            }
        };
        registerDataProvider(TreeNodeType.DEVICE_GROUP.name(), provider,
                observer);

        CombinedVFInfoProvider vfProvider = new CombinedVFInfoProvider();
        VFErrorDataObserver vfObserver = new VFErrorDataObserver(this) {
            @Override
            protected long getValue(ErrStatBean error) {
                return error.getErrorMaximums().getBubbleErrors();
            }
        };
        registerDataProvider(TreeNodeType.VIRTUAL_FABRIC.name(), vfProvider,
                vfObserver);
    }
}
