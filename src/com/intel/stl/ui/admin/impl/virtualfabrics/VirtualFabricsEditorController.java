/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: VirtualFabricsEditorController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/27 15:47:47  jijunwan
 *  Archive Log:    first version of VirtualFabric UI
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl.virtualfabrics;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.api.management.applications.Application;
import com.intel.stl.api.management.applications.ApplicationException;
import com.intel.stl.api.management.devicegroups.DeviceGroup;
import com.intel.stl.api.management.devicegroups.DeviceGroupException;
import com.intel.stl.api.management.virtualfabrics.VirtualFabric;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.admin.impl.AbstractEditorController;
import com.intel.stl.ui.admin.view.virtualfabrics.VirtualFabricsEditorPanel;

public class VirtualFabricsEditorController extends
        AbstractEditorController<VirtualFabric, VirtualFabricsEditorPanel> {
    /**
     * Description:
     * 
     * @param view
     */
    public VirtualFabricsEditorController(VirtualFabricsEditorPanel view) {
        super(view);
    }

    @Override
    public void initData() throws Exception {
        List<String> applications = getAppliationNames();
        view.setApplicationNames(applications);

        List<String> devicegroups = getDeviceGroupNames();
        view.setDeviceGroupNames(devicegroups);
    }

    protected List<String> getAppliationNames() throws ApplicationException {
        List<Application> apps = mgtApi.getApplications();
        List<String> names = new ArrayList<String>(apps.size());
        for (Application app : apps) {
            names.add(app.getName());
        }
        return names;
    }

    protected List<String> getDeviceGroupNames() throws DeviceGroupException {
        List<DeviceGroup> groups = mgtApi.getDeviceGroups();
        List<String> names = new ArrayList<String>(groups.size());
        for (DeviceGroup group : groups) {
            names.add(group.getName());
        }
        return names;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.AbstractEditorController#setItem(com.intel
     * .stl.ui.admin.Item, com.intel.stl.ui.admin.Item[])
     */
    @Override
    public void setItem(Item<VirtualFabric> item, Item<VirtualFabric>[] items) {
        super.setItem(item, items);
    }

}
