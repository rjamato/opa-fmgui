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
 *  File Name: SC2SLMTProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/04/21 17:54:13  jypak
 *  Archive Log:    Header comments fixed.
 *  Archive Log:
 *  
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import static com.intel.stl.ui.model.DeviceProperty.SC;
import static com.intel.stl.ui.model.DeviceProperty.SL;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.api.subnet.SC2SLMTRecordBean;
import com.intel.stl.ui.model.DevicePropertyCategory;
import com.intel.stl.ui.model.DevicePropertyItem;
import com.intel.stl.ui.monitor.tree.FVResourceNode;

public class SC2SLMTProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        FVResourceNode node = context.getResourceNode();

        ISubnetApi subnetApi = context.getContext().getSubnetApi();
        int lid = node.getId();
        // TODO: Is it always supposed to be only one record returned?
        SC2SLMTRecordBean sc2slmtRec = subnetApi.getSC2SLMT(lid);
        if (sc2slmtRec != null) {
            getSC2SLMT(category, sc2slmtRec.getData());
        } else {
            return;
        }
    }

    private void getSC2SLMT(DevicePropertyCategory category, byte[] sc2sl) {
        List<Double> sc2slSeries = new ArrayList<Double>();

        for (int i = 0; i < sc2sl.length; i++) {
            String doubleValue = dec((short) (sc2sl[i] & 0xff));
            sc2slSeries.add(Double.parseDouble(doubleValue));
        }

        double[] series = new double[sc2slSeries.size()];
        for (int i = 0; i < sc2slSeries.size(); i++) {
            series[i] = sc2slSeries.get(i);
        }

        DevicePropertyItem property = new DevicePropertyItem(SL, series);
        category.addPropertyItem(property);

        DevicePropertyItem sc =
                new DevicePropertyItem(SC, new Integer(SAConstants.STL_MAX_SLS));
        category.addPropertyItem(sc);
    }

}
