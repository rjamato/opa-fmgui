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
 *  File Name: SC2VLTMTProcessor.java
 *
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import static com.intel.stl.ui.model.DeviceProperty.SC;
import static com.intel.stl.ui.model.DeviceProperty.VLT;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.api.subnet.SC2VLMTRecordBean;
import com.intel.stl.ui.model.DevicePropertyCategory;
import com.intel.stl.ui.model.DevicePropertyItem;

public class SC2VLTMTProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        PortRecordBean portBean = context.getPort();
        ISubnetApi subnetApi = context.getContext().getSubnetApi();

        // For node type Switch and port 0, don't process and display
        // N/A screen.
        if (portBean == null || portBean.getEndPortLID() == 0) {
            return;
        }

        SC2VLMTRecordBean sc2vltmtRec =
                subnetApi.getSC2VLTMT(portBean.getEndPortLID(),
                        portBean.getPortNum());
        if (sc2vltmtRec != null) {
            getSC2VLTMT(category, sc2vltmtRec.getData());
        } else {
            return;
        }
    }

    private void getSC2VLTMT(DevicePropertyCategory category, byte[] sc2vlt) {
        List<Double> sc2vltSeries = new ArrayList<Double>();

        for (int i = 0; i < sc2vlt.length; i++) {
            String doubleValue = dec((short) (sc2vlt[i] & 0xff));
            sc2vltSeries.add(Double.parseDouble(doubleValue));
        }

        double[] series = new double[sc2vltSeries.size()];
        for (int i = 0; i < sc2vltSeries.size(); i++) {
            series[i] = sc2vltSeries.get(i);
        }

        DevicePropertyItem property = new DevicePropertyItem(VLT, series);
        category.addPropertyItem(property);

        DevicePropertyItem sc =
                new DevicePropertyItem(SC, new Integer(SAConstants.STL_MAX_VLS));
        category.addPropertyItem(sc);
    }

}
