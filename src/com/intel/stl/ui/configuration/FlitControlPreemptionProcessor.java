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
 *  File Name: FlitControlPreemptionProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/10/22 01:47:47  jijunwan
 *  Archive Log:    renamed
 *  Archive Log:    PropertyPageCategory to DevicePropertyCategory,
 *  Archive Log:    PropertyItem to DevicePropertyItem,
 *  Archive Log:    PropertyPageGroup to DevicePropertyGroup
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/13 21:04:11  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import static com.intel.stl.ui.model.DeviceProperty.LARGE_PKT_LIMIT;
import static com.intel.stl.ui.model.DeviceProperty.MAX_SMALL_PKT_LIMIT;
import static com.intel.stl.ui.model.DeviceProperty.MIN_INITIAL;
import static com.intel.stl.ui.model.DeviceProperty.MIN_TAIL;
import static com.intel.stl.ui.model.DeviceProperty.PREEMPTION_LIMIT;
import static com.intel.stl.ui.model.DeviceProperty.SMALL_PKT_LIMIT;

import com.intel.stl.api.subnet.FlitControlBean;
import com.intel.stl.api.subnet.PortInfoBean;
import com.intel.stl.ui.model.DevicePropertyCategory;

public class FlitControlPreemptionProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        PortInfoBean portInfo = context.getPortInfo();

        if (portInfo == null) {
            getEmptyFlitControlPreemption(category);
            return;
        }
        FlitControlBean flitInfo = portInfo.getFlitControl();
        if (flitInfo == null) {
            getEmptyFlitControlPreemption(category);
            return;
        }
        addProperty(category, MIN_INITIAL, hex(flitInfo.getMinInitial()));
        addProperty(category, MIN_TAIL, hex(flitInfo.getMinTail()));
        addProperty(category, LARGE_PKT_LIMIT, hex(flitInfo.getLargePktLimit()));
        addProperty(category, SMALL_PKT_LIMIT, hex(flitInfo.getSmallPktLimit()));
        addProperty(category, MAX_SMALL_PKT_LIMIT,
                hex(flitInfo.getMaxSmallPktLimit()));
        addProperty(category, PREEMPTION_LIMIT,
                hex(flitInfo.getPreemptionLimit()));
    }

    private void getEmptyFlitControlPreemption(DevicePropertyCategory category) {
        addProperty(category, MIN_INITIAL, "");
        addProperty(category, MIN_TAIL, "");
        addProperty(category, LARGE_PKT_LIMIT, "");
        addProperty(category, SMALL_PKT_LIMIT, "");
        addProperty(category, MAX_SMALL_PKT_LIMIT, "");
        addProperty(category, PREEMPTION_LIMIT, "");
    }
}
