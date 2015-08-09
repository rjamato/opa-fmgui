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
 *  File Name: ForwardingInfoProcessor.java
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

import static com.intel.stl.ui.model.DeviceProperty.LINEAR_FDB_CAP;
import static com.intel.stl.ui.model.DeviceProperty.LINEAR_FDB_TOP;
import static com.intel.stl.ui.model.DeviceProperty.MULTICAST_FDB_CAP;
import static com.intel.stl.ui.model.DeviceProperty.MULTICAST_FDB_TOP;
import static com.intel.stl.ui.model.DeviceProperty.PORT_ADRR_RANGE_CONFIG;

import com.intel.stl.api.subnet.SwitchInfoBean;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.model.DevicePropertyCategory;

public class ForwardingInfoProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        SwitchInfoBean switchInfo = context.getSwitchInfo();
        if (switchInfo == null) {
            getEmptySwitchForwardingInfo(category);
            return;
        }
        addProperty(category, LINEAR_FDB_CAP, dec(switchInfo.getLinearFDBCap()));
        addProperty(category, LINEAR_FDB_TOP, dec(switchInfo.getLinearFDBTop()));
        addProperty(category, MULTICAST_FDB_CAP,
                dec(switchInfo.getMulticastFDBCap()));
        addProperty(category, MULTICAST_FDB_TOP,
                dec(switchInfo.getMulticastFDBTop()));
        String trueStr = STLConstants.K0385_TRUE.getValue();
        String falseStr = STLConstants.K0386_FALSE.getValue();
        String value = falseStr;
        if (switchInfo.isAddrRangeConfigSupported()) {
            value = trueStr;
        }
        addProperty(category, PORT_ADRR_RANGE_CONFIG, value);
    }

    private void getEmptySwitchForwardingInfo(DevicePropertyCategory category) {
        addProperty(category, LINEAR_FDB_CAP, "");
        addProperty(category, LINEAR_FDB_TOP, "");
        addProperty(category, MULTICAST_FDB_CAP, "");
        addProperty(category, MULTICAST_FDB_TOP, "");
        addProperty(category, PORT_ADRR_RANGE_CONFIG, "");
    }
}
