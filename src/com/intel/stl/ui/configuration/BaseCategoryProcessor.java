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
 *  File Name: BaseCategoryProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/01/11 21:33:35  jijunwan
 *  Archive Log:    support ipv4 and ipv6 and creating links for ip addresses
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/22 01:47:47  jijunwan
 *  Archive Log:    renamed
 *  Archive Log:    PropertyPageCategory to DevicePropertyCategory,
 *  Archive Log:    PropertyItem to DevicePropertyItem,
 *  Archive Log:    PropertyPageGroup to DevicePropertyGroup
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/21 16:33:59  fernande
 *  Archive Log:    Customization of Properties display (Show Options/Apply Options)
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
import static com.intel.stl.ui.common.STLConstants.K0388_OR;

import java.util.List;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.configuration.LinkWidthMask;
import com.intel.stl.ui.model.DeviceProperty;
import com.intel.stl.ui.model.DevicePropertyCategory;
import com.intel.stl.ui.model.DevicePropertyItem;
import com.intel.stl.ui.model.LinkWidthMaskViz;

/**
 * Defines generic functions for all implementations of
 * ResourceCategoryProcessor. Keep in mind that instances of this class are kept
 * in memory within ResourceCategoryMap and are reused. They are called from
 * PropertyPageCategory (see PropertyPageCategory.populate()) to populate
 * properties for the category. Therefore, they should be reentrant (stateless);
 * this is why all of these functions are defined as static.
 * 
 */
public abstract class BaseCategoryProcessor implements
        ResourceCategoryProcessor {

    @Override
    public abstract void process(ICategoryProcessorContext context,
            DevicePropertyCategory category);

    protected static void addProperty(DevicePropertyCategory category,
            DeviceProperty key, String value) {
        DevicePropertyItem property = new DevicePropertyItem(key, value);
        category.addPropertyItem(property);
    }

    protected static String getLinkWidthString(short val) {
        StringBuilder lwStr = new StringBuilder();
        String join = "";
        String or = " " + K0388_OR.getValue() + " ";
        List<LinkWidthMask> masks = LinkWidthMask.getWidthMasks(val);
        for (LinkWidthMask mask : masks) {
            lwStr.append(join);
            lwStr.append(LinkWidthMaskViz.getLinkWidthMaskStr(mask));
            join = or;
        }
        return lwStr.toString();
    }

    protected static String hex(long value) {
        return StringUtils.longHexString(value);
    }

    protected static String hex(short value) {
        return StringUtils.shortHexString(value);
    }

    protected static String hex(int value) {
        return StringUtils.intHexString(value);
    }

    protected static String hex(byte value) {
        return StringUtils.byteHexString(value);
    }

    protected static String dec(byte value) {
        int unsignedValue = value & 0xff;
        return Integer.toString(unsignedValue);
    }

    protected static String dec(int value) {
        long unsignedValue = value & 0xffffffff;
        return Long.toString(unsignedValue);
    }

    protected static String dec(short value) {
        int unsignedValue = value & 0xffff;
        return Integer.toString(unsignedValue);
    }

    protected static String getIpV6Addr(byte[] ipBytes) {
        if (ipBytes == null) {
            return K0383_NA.getValue();
        }
        return StringUtils.getIpV6Addr(ipBytes);
    }

    protected static String getIpV4Addr(byte[] ipBytes) {
        if (ipBytes == null) {
            return K0383_NA.getValue();
        }
        return StringUtils.getIpV4Addr(ipBytes);
    }
}
