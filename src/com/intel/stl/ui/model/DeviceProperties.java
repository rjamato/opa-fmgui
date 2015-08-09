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
 *  File Name: DeviceProperties.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2014/10/22 02:05:18  jijunwan
 *  Archive Log:    made property model more general
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/13 21:08:07  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
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

import static com.intel.stl.ui.common.UILabels.STL90001_DEVICE_TYPE_NOT_SET;
import static com.intel.stl.ui.common.UILabels.STL90002_DEVICE_CATEGORY_NOT_APPLICABLE;
import static com.intel.stl.ui.common.UILabels.STL90003_DEVICE_CATEGORY_NOT_SELECTED;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.intel.stl.api.configuration.PropertyCategory;
import com.intel.stl.api.configuration.ResourceCategory;
import com.intel.stl.api.configuration.ResourceType;

public class DeviceProperties extends PropertySet<DevicePropertyGroup> {

    private final EnumMap<DeviceProperty, DevicePropertyItem> propertyMap =
            new EnumMap<DeviceProperty, DevicePropertyItem>(
                    DeviceProperty.class);

    private final EnumMap<ResourceCategory, DevicePropertyCategory> categoryMap =
            new EnumMap<ResourceCategory, DevicePropertyCategory>(
                    ResourceCategory.class);

    private ResourceType resourceType;

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType deviceType) {
        this.resourceType = deviceType;
        clear();
    }

    public DevicePropertyItem getProperty(DeviceProperty property) {
        return propertyMap.get(property);
    }

    public List<DevicePropertyCategory> getCategories() {
        List<DevicePropertyCategory> categoryList =
                new ArrayList<DevicePropertyCategory>(categoryMap.size());
        categoryList.addAll(categoryMap.values());
        return categoryList;
    }

    public void addCategory(PropertyCategory category) {
        if (resourceType == null) {
            throw new RuntimeException(
                    STL90001_DEVICE_TYPE_NOT_SET.getDescription());
        }
        ResourceCategory resourceCategory = category.getResourceCategory();
        if (!resourceCategory.isApplicableTo(resourceType)) {
            throw new RuntimeException(
                    STL90002_DEVICE_CATEGORY_NOT_APPLICABLE.getDescription(
                            resourceCategory.name(), resourceType.name()));
        }
        DevicePropertyCategory existing = categoryMap.get(category);
        if (existing != null) {
            return;
        }
        DevicePropertyCategory newCategory =
                new PropertyPageCategoryProxy(category);
        categoryMap.put(resourceCategory, newCategory);
    }

    public DevicePropertyCategory getCategory(ResourceCategory category) {
        return categoryMap.get(category);
    }

    @Override
    public void addPropertyGroup(DevicePropertyGroup propertyGroup) {
        for (ResourceCategory category : propertyGroup.getCategories()) {
            DevicePropertyCategory propertyCategory = categoryMap.get(category);
            if (propertyCategory == null) {
                throw new RuntimeException(
                        STL90003_DEVICE_CATEGORY_NOT_SELECTED.getDescription(
                                category.name(), resourceType.name()));
            }
            propertyGroup.addPropertyCategory(propertyCategory);
        }
        super.addPropertyGroup(propertyGroup);
    }

    private class PropertyPageCategoryProxy extends DevicePropertyCategory {

        public PropertyPageCategoryProxy(PropertyCategory category) {
            super(category);
        }

        @Override
        public void addPropertyItem(DevicePropertyItem item) {
            propertyMap.put(item.getKey(), item);
            super.addPropertyItem(item);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.model.PropertySet#clear()
     */
    @Override
    public void clear() {
        super.clear();
        propertyMap.clear();
        categoryMap.clear();
    }

}
