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
 *  File Name: PortCapabilitiesProcessor.java
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

import static com.intel.stl.ui.common.STLConstants.K0383_NA;
import static com.intel.stl.ui.model.DeviceProperty.AUTO_MIGR_SUPPORTED;
import static com.intel.stl.ui.model.DeviceProperty.CONN_LABEL_SUPPORTED;
import static com.intel.stl.ui.model.DeviceProperty.DEVICE_MGMT_SUPPORTED;
import static com.intel.stl.ui.model.DeviceProperty.NOTICE_SUPPORTED;
import static com.intel.stl.ui.model.DeviceProperty.PORT_ADRR_RANGE_CONFIG;
import static com.intel.stl.ui.model.DeviceProperty.PORT_ASYNC_SC2VL;
import static com.intel.stl.ui.model.DeviceProperty.PORT_PASSTHRU;
import static com.intel.stl.ui.model.DeviceProperty.PORT_SHARED_SPACE;
import static com.intel.stl.ui.model.DeviceProperty.PORT_SNOOP;
import static com.intel.stl.ui.model.DeviceProperty.PORT_VL15_MULTICAST;
import static com.intel.stl.ui.model.DeviceProperty.PORT_VLR;
import static com.intel.stl.ui.model.DeviceProperty.PORT_VL_MARKER;
import static com.intel.stl.ui.model.DeviceProperty.SUBNET_MANAGER;
import static com.intel.stl.ui.model.DeviceProperty.VENDOR_CLASS_SUPPORTED;

import com.intel.stl.api.configuration.CapabilityMask;
import com.intel.stl.api.configuration.CapabilityMask3;
import com.intel.stl.api.subnet.PortInfoBean;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.model.DevicePropertyCategory;

public class PortCapabilitiesProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        PortInfoBean portInfo = context.getPortInfo();
        if (portInfo != null) {
            String trueStr = STLConstants.K0385_TRUE.getValue();
            String falseStr = STLConstants.K0386_FALSE.getValue();
            int val = portInfo.getCapabilityMask();
            String value = falseStr;
            if (CapabilityMask.HAS_SM.hasThisMask(val)) {
                value = trueStr;
            }
            addProperty(category, SUBNET_MANAGER, value);
            value = falseStr;
            if (CapabilityMask.HAS_NOTICE.hasThisMask(val)) {
                value = trueStr;
            }
            addProperty(category, NOTICE_SUPPORTED, value);
            value = falseStr;
            if (CapabilityMask.HAS_AUTOMIGRATION.hasThisMask(val)) {
                value = trueStr;
            }
            addProperty(category, AUTO_MIGR_SUPPORTED, value);
            value = falseStr;
            if (CapabilityMask.HAS_CONNECTIONMANAGEMENT.hasThisMask(val)) {
                value = trueStr;
            }
            addProperty(category, CONN_LABEL_SUPPORTED, value);
            value = falseStr;
            if (CapabilityMask.HAS_DEVICEMANAGEMENT.hasThisMask(val)) {
                value = trueStr;
            }
            addProperty(category, DEVICE_MGMT_SUPPORTED, value);
            value = falseStr;
            if (CapabilityMask.HAS_VENDORCLASS.hasThisMask(val)) {
                value = trueStr;
            }
            addProperty(category, VENDOR_CLASS_SUPPORTED, value);

            short val3 = portInfo.getCapabilityMask3();
            value = falseStr;
            if (CapabilityMask3.SNOOP_SUPPORTED.hasThisMask(val3)) {
                value = trueStr;
            }
            addProperty(category, PORT_SNOOP, value);
            value = falseStr;
            if (CapabilityMask3.ASYNCSC2VL_SUPPORTED.hasThisMask(val3)) {
                value = trueStr;
            }
            addProperty(category, PORT_ASYNC_SC2VL, value);
            value = falseStr;
            if (CapabilityMask3.ADDRRANGECONFIG_SUPPORTED.hasThisMask(val3)) {
                value = trueStr;
            }
            addProperty(category, PORT_ADRR_RANGE_CONFIG, value);
            value = falseStr;
            if (CapabilityMask3.PASSTHROUGH_SUPPORTED.hasThisMask(val3)) {
                value = trueStr;
            }
            addProperty(category, PORT_PASSTHRU, value);
            value = falseStr;
            if (CapabilityMask3.SHAREDSPACE_SUPPORTED.hasThisMask(val3)) {
                value = trueStr;
            }
            addProperty(category, PORT_SHARED_SPACE, value);
            value = falseStr;
            if (CapabilityMask3.VLMARKER_SUPPORTED.hasThisMask(val3)) {
                value = trueStr;
            }
            addProperty(category, PORT_VL_MARKER, value);
            value = falseStr;
            if (CapabilityMask3.VLR_SUPPORTED.hasThisMask(val3)) {
                value = trueStr;
            }
            addProperty(category, PORT_VLR, value);
        } else {
            String na = K0383_NA.getValue();
            addProperty(category, SUBNET_MANAGER, na);
            addProperty(category, NOTICE_SUPPORTED, na);
            addProperty(category, AUTO_MIGR_SUPPORTED, na);
            addProperty(category, CONN_LABEL_SUPPORTED, na);
            addProperty(category, DEVICE_MGMT_SUPPORTED, na);
            addProperty(category, VENDOR_CLASS_SUPPORTED, na);
            addProperty(category, PORT_SNOOP, na);
            addProperty(category, PORT_ASYNC_SC2VL, na);
            addProperty(category, PORT_ADRR_RANGE_CONFIG, na);
            addProperty(category, PORT_PASSTHRU, na);
            addProperty(category, PORT_SHARED_SPACE, na);
            addProperty(category, PORT_VL15_MULTICAST, na);
            addProperty(category, PORT_VL_MARKER, na);
            addProperty(category, PORT_VLR, na);
        }
    }
}
