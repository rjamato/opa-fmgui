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
 *  File Name: BaseCategoryProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/08/17 18:53:50  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/07/21 20:47:05  jijunwan
 *  Archive Log:    PR 129619 - Support IPv4 address for SA
 *  Archive Log:    - change to return n/a is address is all zeros
 *  Archive Log:
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
        if (ipBytes == null || isZeros(ipBytes)) {
            return K0383_NA.getValue();
        }
        return StringUtils.getIpV6Addr(ipBytes);
    }

    protected static String getIpV4Addr(byte[] ipBytes) {
        if (ipBytes == null || isZeros(ipBytes)) {
            return K0383_NA.getValue();
        }
        return StringUtils.getIpV4Addr(ipBytes);
    }

    protected static boolean isZeros(byte[] ipBytes) {
        for (byte val : ipBytes) {
            if (val != 0) {
                return false;
            }
        }
        return true;
    }
}
