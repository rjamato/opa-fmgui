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
 *  File Name: SwitchInfoProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/10/06 21:33:23  fernande
 *  Archive Log:    PR130728 - IPChassis Name listed as N/A on Switch Information table. Removed chassis name since information is not provided by FE
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/08/17 18:53:50  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
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

import static com.intel.stl.ui.common.STLConstants.K0385_TRUE;
import static com.intel.stl.ui.common.STLConstants.K0386_FALSE;
import static com.intel.stl.ui.model.DeviceProperty.ENH_SWITCH_PORT0;
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
        // addProperty(category, IPCHASSIS_NAME, K0383_NA.getValue());
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
        // addProperty(category, IPCHASSIS_NAME, K0383_NA.getValue());
        addProperty(category, ENH_SWITCH_PORT0, "");
        addProperty(category, LIFETIME_VALUE, "");
        addProperty(category, PORT_STATE_CHANGE, "");
    }
}
