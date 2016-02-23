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
 *  File Name: PortCountersItem.java
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

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.time.TimeSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.ui.performance.PortSourceName;

public abstract class PortCountersItem extends TrendItem<PortSourceName> {
    private static final Logger log = LoggerFactory
            .getLogger(PortCountersItem.class);

    /**
     * Description:
     * 
     * @param fullName
     * @param maxDataPoints
     */
    public PortCountersItem(String shortName, String fullName, int maxDataPoints) {
        super(shortName, fullName, maxDataPoints);
    }

    /**
     * Description:
     * 
     * @param name
     * @param fullName
     * @param maxDataPoints
     */
    public PortCountersItem(String name, String shortName, String fullName,
            int maxDataPoints) {
        super(name, shortName, fullName, maxDataPoints);
    }

    /**
     * Description:
     * 
     * @param item
     */
    public PortCountersItem(PortCountersItem item) {
        super(item);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.item.TrendItem#createTrendSeries(com.intel
     * .stl.ui.performance.ISource[])
     */
    @Override
    protected List<TimeSeries> createTrendSeries(PortSourceName[] series) {
        List<TimeSeries> res = new ArrayList<TimeSeries>();
        if (series != null) {
            for (PortSourceName serie : series) {
                TimeSeries all = new TimeSeries(serie.getPrettyName());
                res.add(all);
            }
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.item.TrendItem#getTimeSeries(java.lang.String
     * )
     */
    @Override
    protected TimeSeries getTimeSeries(PortSourceName name) {
        for (PortSourceName sn : sourceNames) {
            if (sn.equals(name)) {
                return dataset.getSeries(sn.getPrettyName());
            }
        }
        log.warn(name.sourceName() + " is not registed source!");
        return null;
        // throw new IllegalArgumentException(name.sourceName()
        // + " is not registed source!");
    }
}
