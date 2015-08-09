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
 *  File Name: GetDevicePropertiesTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.19  2015/04/02 13:32:57  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/01/11 21:36:25  jijunwan
 *  Archive Log:    adapt to latest data structure changes on FM
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/10/22 01:47:47  jijunwan
 *  Archive Log:    renamed
 *  Archive Log:    PropertyPageCategory to DevicePropertyCategory,
 *  Archive Log:    PropertyItem to DevicePropertyItem,
 *  Archive Log:    PropertyPageGroup to DevicePropertyGroup
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/10/21 16:33:59  fernande
 *  Archive Log:    Customization of Properties display (Show Options/Apply Options)
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/10/16 13:21:10  fernande
 *  Archive Log:    Changes to AbstractTask to support an onFinally method that is guaranteed to be called no matter what happens in the onTaskSuccess and onTaskFailure implementations for a task.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/10/13 21:06:15  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/09/05 15:42:38  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/09/02 19:24:31  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/08/22 16:52:48  fernande
 *  Archive Log:    Closing the gaps between properties and sa_query
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/08/18 21:30:27  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/08/15 21:45:05  jijunwan
 *  Archive Log:    handle exceptions so we show N/A when data not available
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/08/14 17:37:20  fernande
 *  Archive Log:    Closing the gap on device properties being displayed.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/08/12 20:58:07  jijunwan
 *  Archive Log:    1) renamed HexUtils to StringUtils
 *  Archive Log:    2) added a method to StringUtils to get error message for an exception
 *  Archive Log:    3) changed all code to call StringUtils to get error message
 *  Archive Log:    4) some extra ode format change
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/08/05 18:39:06  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/04 21:16:35  fernande
 *  Archive Log:    Changed DeviceProperties to be more extensible and support adding more property categories and property groups
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/29 15:50:56  fernande
 *  Archive Log:    Fix for labels
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/29 13:48:02  fernande
 *  Archive Log:    Removed repetitive conversion from FE values to API enums
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/22 21:56:28  fernande
 *  Archive Log:    Adding background task to retrieve device properties and populate model
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import java.util.List;

import com.intel.stl.api.configuration.PropertyCategory;
import com.intel.stl.api.configuration.PropertyGroup;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.framework.AbstractTask;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.DeviceProperties;
import com.intel.stl.ui.model.DevicePropertyCategory;
import com.intel.stl.ui.model.DevicePropertyGroup;
import com.intel.stl.ui.model.PropertyGroupViz;
import com.intel.stl.ui.monitor.tree.FVResourceNode;

public class GetDevicePropertiesTask extends
        AbstractTask<DeviceProperties, Void, Void> {

    private final FVResourceNode node;

    private final IProgressObserver observer;

    /**
     * Description: background task in charge of retrieving property information
     * for a node
     * 
     * @param model
     */
    public GetDevicePropertiesTask(DeviceProperties model, FVResourceNode node,
            IProgressObserver observer) {
        super(model);
        this.node = node;
        this.observer = observer;
    }

    @Override
    public Void processInBackground(Context context) throws Exception {
        UserSettings userSettings = context.getUserSettings();
        List<PropertyGroup> groups = null;
        if (userSettings != null) {
            groups =
                    userSettings.getPropertiesDisplayOptions().get(
                            model.getResourceType());
        }
        CategoryProcessorContext categoryCtx =
                new CategoryProcessorContext(node, context);

        // Categories must be selected in the model first and then optionally
        // PropertyGroups can be defined and you can specify which categories
        // you want included in each group
        if (groups != null) {
            for (PropertyGroup group : groups) {
                if (group.isDisplayed()) {
                    DevicePropertyGroup pageGroup = createGroup(group);
                    for (PropertyCategory category : group.getCategories()) {
                        model.addCategory(category);
                        pageGroup.addCategory(category.getResourceCategory());
                    }
                    model.addPropertyGroup(pageGroup);
                }
            }
        }

        for (DevicePropertyCategory pageCategory : model.getCategories()) {
            pageCategory.populate(categoryCtx);
        }
        return null;
    }

    @Override
    public void onTaskSuccess(Void result) {
    }

    @Override
    public void onTaskFailure(Throwable caught) {
        caught.printStackTrace();
    }

    @Override
    public void onFinally() {
        if (observer != null) {
            observer.onFinish();
        }
    }

    @Override
    public void processIntermediateResults(List<Void> intermediateResults) {
    }

    protected DevicePropertyGroup createGroup(PropertyGroup propertyGroup) {
        DevicePropertyGroup group = new DevicePropertyGroup();
        String title = propertyGroup.getTitle();
        if (title == null || title.length() == 0) {
            String groupName = propertyGroup.getName();
            title = PropertyGroupViz.getPropertyGroupViz(groupName).getTitle();
        }
        group.setGroupName(title);
        return group;
    }

}
