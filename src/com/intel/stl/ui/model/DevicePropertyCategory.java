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
 *  File Name: PropertyCategory.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/05 19:57:09  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/22 02:05:18  jijunwan
 *  Archive Log:    made property model more general
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/13 21:08:07  fernande
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

import java.util.Collection;
import java.util.EnumMap;

import com.intel.stl.api.configuration.PropertyCategory;
import com.intel.stl.api.configuration.ResourceCategory;
import com.intel.stl.ui.configuration.ICategoryProcessorContext;
import com.intel.stl.ui.configuration.ResourceCategoryMap;

public class DevicePropertyCategory extends ModelCollection<DevicePropertyItem>
        implements IPropertyCategory<DevicePropertyItem> {

    private final EnumMap<DeviceProperty, DevicePropertyItem> propertyMap =
            new EnumMap<DeviceProperty, DevicePropertyItem>(
                    DeviceProperty.class);

    private final PropertyCategory category;

    private final ResourceCategoryMap categoryMap;

    public DevicePropertyCategory(PropertyCategory category) {
        this.category = category;
        ResourceCategory resourceCat = category.getResourceCategory();
        this.categoryMap =
                ResourceCategoryMap.getResourceCategoryMapFor(resourceCat);
    }

    public ResourceCategory getCategory() {
        return category.getResourceCategory();
    }

    @Override
    public String getKeyHeader() {
        String keyHeader = null;
        if (category.isShowHeader()) {
            String usrKeyHeader = category.getKeyHeader();
            if (usrKeyHeader == null || usrKeyHeader.length() == 0) {
                keyHeader = categoryMap.getDefaultKeyHeader();
            } else {
                keyHeader = usrKeyHeader;
            }
        }
        return keyHeader;
    }

    @Override
    public String getValueHeader() {
        String valueHeader = null;
        if (category.isShowHeader()) {
            String usrValueHeader = category.getKeyHeader();
            if (usrValueHeader == null || usrValueHeader.length() == 0) {
                valueHeader = categoryMap.getDefaultValueHeader();
            } else {
                valueHeader = usrValueHeader;
            }
        }
        return valueHeader;
    }

    public EnumMap<DeviceProperty, DevicePropertyItem> getProperties() {
        return propertyMap;
    }

    public int getNumProperties() {
        return propertyMap.size();
    }

    public DevicePropertyItem getProperty(DeviceProperty property) {
        DevicePropertyItem res = propertyMap.get(property);
        if (res != null) {
            return res;
        } else {
            throw new IllegalArgumentException("Couldn't find DeviceProperty "
                    + property);
        }
    }

    public void addPropertyItem(DevicePropertyItem item) {
        propertyMap.put(item.getKey(), item);
        addItem(item);
    }

    public void populate(ICategoryProcessorContext context) {
        categoryMap.getProcessor().process(context, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.model.IPropertyCategory#getItems()
     */
    @Override
    public Collection<DevicePropertyItem> getItems() {
        return getList();
    }

}
