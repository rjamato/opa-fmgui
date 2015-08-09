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
 *  File Name: MTUByVLProcessor.java
 *
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import static com.intel.stl.ui.model.DeviceProperty.MTU_SERIES;
import static com.intel.stl.ui.model.DeviceProperty.NUM_VL;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.api.configuration.MTUSize;
import com.intel.stl.api.subnet.PortInfoBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.ui.model.DevicePropertyCategory;
import com.intel.stl.ui.model.DevicePropertyItem;

public class MTUByVLProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        PortInfoBean portInfo = context.getPortInfo();
        if (portInfo != null) {
            getNeighborMTUByVL(category, portInfo.getNeighborVL0MTU(),
                    portInfo.getNeighborVL1MTU());
        }

    }

    private void getNeighborMTUByVL(DevicePropertyCategory category,
            byte[] vl0mtu, byte[] vl1mtu) {
        List<MTUSize> mtuSeries = new ArrayList<MTUSize>();

        for (int i = 0; i < vl0mtu.length; i++) {
            mtuSeries.add(MTUSize.getMTUSize(vl0mtu[i]));
            mtuSeries.add(MTUSize.getMTUSize(vl1mtu[i]));
        }

        MTUSize[] series = new MTUSize[mtuSeries.size()];
        for (int i = 0; i < mtuSeries.size(); i++) {
            series[i] = mtuSeries.get(i);
        }

        DevicePropertyItem property =
                new DevicePropertyItem(MTU_SERIES, series);
        category.addPropertyItem(property);

        DevicePropertyItem numVLs =
                new DevicePropertyItem(NUM_VL, new Integer(
                        SAConstants.STL_MAX_VLS));
        category.addPropertyItem(numVLs);
    }

}
