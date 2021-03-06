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
 *  File Name: UtilStatsObserver.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/08/17 18:54:20  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/07/02 20:33:54  fernande
 *  Archive Log:    PR 129447 - Database size increases a lot over a short period of time. Moving Blobs to the database; arrays are now being saved to the database as collection tables.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/05 13:37:35  jijunwan
 *  Archive Log:    added null check
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/21 17:30:42  jijunwan
 *  Archive Log:    renamed IDataObserver.Type to DataType, and put it under model package
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 15:08:59  jijunwan
 *  Archive Log:    new framework for performance data visualization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance.observer;

import java.util.List;

import com.intel.stl.api.performance.GroupInfoBean;
import com.intel.stl.api.performance.UtilStatsBean;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.performance.item.HistogramItem;

public class BWHistogramDataObserver extends
        AbstractDataObserver<GroupInfoBean[], HistogramItem> {

    public BWHistogramDataObserver(HistogramItem controller) {
        this(controller, DataType.ALL);
    }

    /**
     * Description:
     * 
     * @param controller
     * @param type
     */
    public BWHistogramDataObserver(HistogramItem controller, DataType type) {
        super(controller, type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.performance.IDataObserver#processData(java.lang
     * .Object)
     */
    @Override
    public void processData(GroupInfoBean[] data) {
        if (data != null && data.length > 0) {
            processData(data[0]);
        }
    }

    public void processData(GroupInfoBean data) {
        if (data == null) {
            return;
        }

        UtilStatsBean[] utils = getUtilStatsBeans(data, type);
        int[] counts = null;
        for (UtilStatsBean util : utils) {
            List<Integer> buckets = util.getBwBuckets();
            if (counts == null) {
                counts = new int[buckets.size()];
            }
            for (int i = 0; i < buckets.size(); i++) {
                counts[i] += buckets.get(i);
            }
        }
        controller.updateHistogram(counts, 1.0);
    }
}
