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
 *  File Name: UtilStatsObserver.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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

import com.intel.stl.api.performance.UtilStatsBean;
import com.intel.stl.api.performance.VFInfoBean;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.performance.item.HistogramItem;

public class VFBWHistogramDataObserver extends AbstractDataObserver<VFInfoBean[], HistogramItem> {

    public VFBWHistogramDataObserver(HistogramItem controller) {
        this(controller, DataType.ALL);
    }
    
    /**
     * Description: 
     *
     * @param controller
     * @param type 
     */
    public VFBWHistogramDataObserver(HistogramItem controller, DataType type) {
        super(controller, type);
    }

    /* (non-Javadoc)
     * @see com.intel.stl.ui.common.performance.IDataObserver#processData(java.lang.Object)
     */
    @Override
    public void processData(VFInfoBean[] data) {
        if (data!=null && data.length>0) {
            processData(data[0]);
        }
    }
    
    public void processData(VFInfoBean data) {
        if (data==null) {
            return;
        }
        
        UtilStatsBean[] utils = getUtilStatsBeans(data, type);
        int[] counts = null;
        for (UtilStatsBean util : utils) {
            Integer[] buckets = util.getBwBuckets();
            if (counts==null) {
                counts = new int[buckets.length];
            }
            for (int i=0; i<buckets.length; i++) {
                counts[i] += buckets[i];
            }
        }
        controller.updateHistogram(counts, 1.0);
    }
}
