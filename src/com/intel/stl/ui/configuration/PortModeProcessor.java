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
 *  File Name: PortModeProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/01/27 19:44:31  jijunwan
 *  Archive Log:    updated PortInfo to match FM 314 (stl_sm.h v1.125)
 *  Archive Log:      - removed IsSCtoSCMappingEnabled
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/22 01:47:47  jijunwan
 *  Archive Log:    renamed
 *  Archive Log:    PropertyPageCategory to DevicePropertyCategory,
 *  Archive Log:    PropertyItem to DevicePropertyItem,
 *  Archive Log:    PropertyPageGroup to DevicePropertyGroup
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/13 21:05:30  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import static com.intel.stl.ui.common.STLConstants.K0385_TRUE;
import static com.intel.stl.ui.common.STLConstants.K0386_FALSE;
import static com.intel.stl.ui.model.DeviceProperty.ACTIVE_OPTIMIZE;
import static com.intel.stl.ui.model.DeviceProperty.PASSTHRU;
import static com.intel.stl.ui.model.DeviceProperty.TRAP_QUERY_16B;
import static com.intel.stl.ui.model.DeviceProperty.VL_MARKER;

import com.intel.stl.api.subnet.PortInfoBean;
import com.intel.stl.ui.model.DevicePropertyCategory;

public class PortModeProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        PortInfoBean portInfo = context.getPortInfo();

        if (portInfo == null) {
            getEmptyPortMode(category);
            return;
        }
        String trueStr = K0385_TRUE.getValue();
        String falseStr = K0386_FALSE.getValue();
        String value = falseStr;
        if (portInfo.isActiveOptimizeEnabled()) {
            value = trueStr;
        }
        addProperty(category, ACTIVE_OPTIMIZE, value);
        value = falseStr;
        if (portInfo.isPassThroughEnabled()) {
            value = trueStr;
        }
        addProperty(category, PASSTHRU, value);
        value = falseStr;
        if (portInfo.isVLMarkerEnabled()) {
            value = trueStr;
        }
        addProperty(category, VL_MARKER, value);
        value = falseStr;
        if (portInfo.is16BTrapQueryEnabled()) {
            value = trueStr;
        }
        addProperty(category, TRAP_QUERY_16B, value);
    }

    private void getEmptyPortMode(DevicePropertyCategory category) {
        addProperty(category, ACTIVE_OPTIMIZE, "");
        addProperty(category, PASSTHRU, "");
        addProperty(category, VL_MARKER, "");
        addProperty(category, TRAP_QUERY_16B, "");
    }
}
