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
 *  File Name: TxPktRateItem.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/08/17 18:53:43  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/06/30 22:28:49  jijunwan
 *  Archive Log:    PR 129215 - Need short chart name to support pin capability
 *  Archive Log:    - introduced short name to performance items
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/06/25 20:42:13  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - improved PerformanceItem to support port counters
 *  Archive Log:    - improved PerformanceItem to use generic ISource to describe data source
 *  Archive Log:    - improved PerformanceItem to use enum DataProviderName to describe data provider name
 *  Archive Log:    - improved PerformanceItem to support creating a copy of PerformanceItem
 *  Archive Log:    - improved TrendItem to share scale with other charts
 *  Archive Log:    - improved SimpleDataProvider to support hsitory data
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance.item;

import com.intel.stl.api.performance.PortCountersBean;
import com.intel.stl.api.performance.VFPortCountersBean;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.performance.PortSourceName;
import com.intel.stl.ui.performance.observer.CounterRateObserver;
import com.intel.stl.ui.performance.observer.VFCounterRateObserver;
import com.intel.stl.ui.performance.provider.DataProviderName;
import com.intel.stl.ui.performance.provider.PortCountersProvider;
import com.intel.stl.ui.performance.provider.VFPortCountersProvider;

public class TxPktRateItem extends PortCountersItem {

    public TxPktRateItem() {
        this(DEFAULT_DATA_POINTS);
    }

    public TxPktRateItem(int maxDataPoints) {
        super(STLConstants.K0829_TRAN_PACKETS_RATE.getValue(),
                STLConstants.K0870_SHORT_TRAN_PKTS_RATE.getValue(),
                STLConstants.K0829_TRAN_PACKETS_RATE.getValue(), maxDataPoints);
    }

    public TxPktRateItem(TxPktRateItem item) {
        super(item);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.item.AbstractPerformanceItem#initDataProvider
     * ()
     */
    @Override
    protected void initDataProvider() {
        PortCountersProvider provider = new PortCountersProvider();
        CounterRateObserver observer = new CounterRateObserver(this) {
            @Override
            protected long getValue(PortCountersBean bean) {
                return bean.getPortXmitPkts();
            }
        };
        registerDataProvider(DataProviderName.PORT, provider, observer);

        VFPortCountersProvider vfProvider = new VFPortCountersProvider();
        VFCounterRateObserver vfObserver = new VFCounterRateObserver(this) {
            @Override
            protected long getValue(VFPortCountersBean bean) {
                return bean.getPortVFXmitPkts();
            }
        };
        registerDataProvider(DataProviderName.VF_PORT, vfProvider, vfObserver);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.performance.item.IPerformanceItem#copy()
     */
    @Override
    public IPerformanceItem<PortSourceName> copy() {
        return new TxPktRateItem(this);
    }

}
