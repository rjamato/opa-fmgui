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
 *  File Name: BWHistogramItem.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/08/17 18:53:43  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/06/30 22:28:49  jijunwan
 *  Archive Log:    PR 129215 - Need short chart name to support pin capability
 *  Archive Log:    - introduced short name to performance items
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/06/25 20:42:13  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - improved PerformanceItem to support port counters
 *  Archive Log:    - improved PerformanceItem to use generic ISource to describe data source
 *  Archive Log:    - improved PerformanceItem to use enum DataProviderName to describe data provider name
 *  Archive Log:    - improved PerformanceItem to support creating a copy of PerformanceItem
 *  Archive Log:    - improved TrendItem to share scale with other charts
 *  Archive Log:    - improved SimpleDataProvider to support hsitory data
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/17 23:22:14  jijunwan
 *  Archive Log:    PR 127106 - Suggest to use same bucket range for Group Err Summary as shown in "opatop" command to plot performance graphs in FV
 *  Archive Log:     - changed error histogram chart to bar chart to show the new data ranges: 0-25%, 25-50%, 50-75%, 75-100% and 100+%
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
import com.intel.stl.ui.performance.GroupSource;
import com.intel.stl.ui.performance.observer.ErrorHistogramDataObserver;
import com.intel.stl.ui.performance.observer.VFErrorHistogramDataObserver;
import com.intel.stl.ui.performance.provider.CombinedGroupInfoProvider;
import com.intel.stl.ui.performance.provider.CombinedVFInfoProvider;
import com.intel.stl.ui.performance.provider.DataProviderName;

/**
 * Sma Congestion Errors Histogram
 */
public class SCHistogramItem extends ErrHistogramItem {
    public SCHistogramItem() {
        super(STLConstants.K0860_SHORT_SMACONG_HISTOGRAM.getValue(),
                STLConstants.K0071_SMA_CONGESTION_HISTOGRAM.getValue());
    }

    public SCHistogramItem(SCHistogramItem item) {
        super(item);
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
                        return err.getSmaCongestionErrors();
                    }
                };
        registerDataProvider(DataProviderName.PORT_GROUP, provider, observer);

        CombinedVFInfoProvider vfProvider = new CombinedVFInfoProvider();
        VFErrorHistogramDataObserver vfObserver =
                new VFErrorHistogramDataObserver(this) {
                    @Override
                    protected long getValue(ErrBucketBean err) {
                        return err.getSmaCongestionErrors();
                    }
                };
        registerDataProvider(DataProviderName.VIRTUAL_FABRIC, vfProvider,
                vfObserver);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.performance.item.IPerformanceItem#copy()
     */
    @Override
    public IPerformanceItem<GroupSource> copy() {
        return new SCHistogramItem(this);
    }

}
