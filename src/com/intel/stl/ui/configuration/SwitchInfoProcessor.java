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
 *  File Name: SwitchInfoProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/01/11 21:36:25  jijunwan
 *  Archive Log:    adapt to latest data structure changes on FM
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/22 01:47:47  jijunwan
 *  Archive Log:    renamed
 *  Archive Log:    PropertyPageCategory to DevicePropertyCategory,
 *  Archive Log:    PropertyItem to DevicePropertyItem,
 *  Archive Log:    PropertyPageGroup to DevicePropertyGroup
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/13 21:06:15  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import static com.intel.stl.ui.common.STLConstants.K0383_NA;
import static com.intel.stl.ui.common.STLConstants.K0385_TRUE;
import static com.intel.stl.ui.common.STLConstants.K0386_FALSE;
import static com.intel.stl.ui.model.DeviceProperty.ENH_SWITCH_PORT0;
import static com.intel.stl.ui.model.DeviceProperty.IPCHASSIS_NAME;
import static com.intel.stl.ui.model.DeviceProperty.LIFETIME_VALUE;
import static com.intel.stl.ui.model.DeviceProperty.PORT_STATE_CHANGE;

import com.intel.stl.api.subnet.SwitchInfoBean;
import com.intel.stl.ui.model.DevicePropertyCategory;

public class SwitchInfoProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        SwitchInfoBean switchInfo = context.getSwitchInfo();
        if (switchInfo == null) {
            getEmptySwitchDeviceInfo(category);
            return;
        }
        addProperty(category, IPCHASSIS_NAME, K0383_NA.getValue());
        if (switchInfo.isEnhancedPort0()) {
            addProperty(category, ENH_SWITCH_PORT0, K0385_TRUE.getValue());
        } else {
            addProperty(category, ENH_SWITCH_PORT0, K0386_FALSE.getValue());
        }
        addProperty(category, LIFETIME_VALUE,
                dec(switchInfo.getLifeTimeValue()));
        int portStateChange = 0;
        if (switchInfo.isPortStateChange()) {
            portStateChange = 1;
        }
        addProperty(category, PORT_STATE_CHANGE, dec(portStateChange));
    }

    private void getEmptySwitchDeviceInfo(DevicePropertyCategory category) {
        // TODO: need to come from API
        addProperty(category, IPCHASSIS_NAME, K0383_NA.getValue());
        addProperty(category, ENH_SWITCH_PORT0, "");
        addProperty(category, LIFETIME_VALUE, "");
        addProperty(category, PORT_STATE_CHANGE, "");
    }
}
