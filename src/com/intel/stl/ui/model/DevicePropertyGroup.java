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
 *  File Name: PropertyGroup.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/10/22 02:05:17  jijunwan
 *  Archive Log:    made property model more general
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/13 21:08:07  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/14 17:39:03  fernande
 *  Archive Log:    Closing the gap on device properties being displayed.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/04 21:19:44  fernande
 *  Archive Log:    Changes to make DeviceProperties more extensible and to be able to access properties by DeviceProperty key or by DeviceCategory key (group of properties)
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/22 21:54:21  fernande
 *  Archive Log:    Adding models to support device properties
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.intel.stl.api.configuration.ResourceCategory;

public class DevicePropertyGroup extends PropertyGroup<DevicePropertyCategory> {

    private final List<ResourceCategory> categories =
            new ArrayList<ResourceCategory>();

    private final EnumMap<ResourceCategory, DevicePropertyCategory> categoryMap =
            new EnumMap<ResourceCategory, DevicePropertyCategory>(
                    ResourceCategory.class);

    public List<ResourceCategory> getCategories() {
        return categories;
    }

    public void addCategory(ResourceCategory category) {
        categories.add(category);
    }

    public DevicePropertyCategory getCategory(ResourceCategory category) {
        return categoryMap.get(category);
    }

    @Override
    public void addPropertyCategory(DevicePropertyCategory category) {
        super.addPropertyCategory(category);
        categoryMap.put(category.getCategory(), category);
    }
}
