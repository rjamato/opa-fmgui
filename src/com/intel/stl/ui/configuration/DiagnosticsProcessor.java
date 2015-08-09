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
 *  File Name: DiagnosticsProcessor.java
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

import static com.intel.stl.ui.common.STLConstants.K0383_NA;
import static com.intel.stl.ui.model.DeviceProperty.MASTER_SMSL;
import static com.intel.stl.ui.model.DeviceProperty.M_KEY_VIOLATIONS;
import static com.intel.stl.ui.model.DeviceProperty.PORT_GANGED_DETAILS;
import static com.intel.stl.ui.model.DeviceProperty.PORT_OVERALL_BUFF_SPACE;
import static com.intel.stl.ui.model.DeviceProperty.P_KEY_VIOLATIONS;
import static com.intel.stl.ui.model.DeviceProperty.Q_KEY_VIOLATIONS;
import static com.intel.stl.ui.model.DeviceProperty.REPLAY_DEPTH_BUFFER;
import static com.intel.stl.ui.model.DeviceProperty.REPLAY_DEPTH_WIRE;
import static com.intel.stl.ui.model.DeviceProperty.RESPONSE_TIME;
import static com.intel.stl.ui.model.DeviceProperty.SUBNET_TIMEOUT;

import com.intel.stl.api.subnet.PortInfoBean;
import com.intel.stl.ui.model.DevicePropertyCategory;

public class DiagnosticsProcessor extends BaseCategoryProcessor {

    private static final int FORMAT_MULT_THRESHOLD_FRACTION = 1000;

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        PortInfoBean portInfo = context.getPortInfo();
        boolean isEndPort = context.isEndPort();

        if (portInfo != null) {
            addProperty(category, PORT_OVERALL_BUFF_SPACE,
                    hex(portInfo.getOverallBufferSpace()));
            addProperty(category, REPLAY_DEPTH_BUFFER,
                    hex(portInfo.getBufferDepth()));
            addProperty(category, REPLAY_DEPTH_WIRE,
                    hex(portInfo.getWireDepth()));
            String na = K0383_NA.getValue();
            String value = na;
            if (isEndPort) {
                value = Byte.toString(portInfo.getMasterSMSL());
            }
            addProperty(category, MASTER_SMSL, value);
            value = Byte.toString(portInfo.getMasterSMSL());
            addProperty(category, M_KEY_VIOLATIONS, value);
            value = Short.toString(portInfo.getPKeyViolation());
            addProperty(category, P_KEY_VIOLATIONS, value);
            value = Short.toString(portInfo.getQKeyViolation());
            addProperty(category, Q_KEY_VIOLATIONS, value);
            value = formatTimeout(portInfo.getRespTimeValue());
            addProperty(category, RESPONSE_TIME, value);
            value = formatTimeout(portInfo.getSubnetTimeout());
            addProperty(category, SUBNET_TIMEOUT, value);
        } else {
            addProperty(category, PORT_GANGED_DETAILS, "");
            addProperty(category, PORT_OVERALL_BUFF_SPACE, "");
            addProperty(category, REPLAY_DEPTH_BUFFER, "");
            addProperty(category, REPLAY_DEPTH_WIRE, "");
            addProperty(category, MASTER_SMSL, "");
            addProperty(category, M_KEY_VIOLATIONS, "");
            addProperty(category, P_KEY_VIOLATIONS, "");
            addProperty(category, Q_KEY_VIOLATIONS, "");
            addProperty(category, RESPONSE_TIME, "");
            addProperty(category, SUBNET_TIMEOUT, "");
        }
    }

    private String formatTimeout(byte timeout) {
        double exp = Math.pow(2.0, timeout);
        double val = 4096 * exp; // value in ns
        if (val < FORMAT_MULT_THRESHOLD_FRACTION) {
            return String.format("%.0f ns", val);
        } else {
            val = val / 1000;
            if (val < FORMAT_MULT_THRESHOLD_FRACTION) {
                return String.format("%.0f µs", val);
            } else {
                val = val / 1000;
                if (val < FORMAT_MULT_THRESHOLD_FRACTION) {
                    return String.format("%.0f ms", val);
                } else {
                    val = val / 1000;
                    return String.format("%.0f sec", val);
                }
            }
        }

    }
}
