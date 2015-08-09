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
 *  File Name: SwitchIPAddressProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/01/11 21:33:35  jijunwan
 *  Archive Log:    support ipv4 and ipv6 and creating links for ip addresses
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

import static com.intel.stl.ui.model.DeviceProperty.IP_ADDR_IPV4;
import static com.intel.stl.ui.model.DeviceProperty.IP_ADDR_IPV6;

import com.intel.stl.api.subnet.SwitchInfoBean;
import com.intel.stl.ui.model.DevicePropertyCategory;

public class SwitchIPAddressProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        SwitchInfoBean switchInfo = context.getSwitchInfo();
        if (switchInfo == null) {
            getEmptySwitchIpAddr(category);
            return;
        }
        addProperty(category, IP_ADDR_IPV6,
                getIpV6Addr(switchInfo.getIpAddrIPV6()));
        // addProperty(category, IP_ADDR_PRIMARY,
        // getIpV6Addr(switchInfo.getIpAddrPrimary()));
        addProperty(category, IP_ADDR_IPV4,
                getIpV4Addr(switchInfo.getIpAddrIPV4()));
    }

    private void getEmptySwitchIpAddr(DevicePropertyCategory category) {
        addProperty(category, IP_ADDR_IPV6, "");
        addProperty(category, IP_ADDR_IPV4, "");
    }

}
