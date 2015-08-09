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
 *  File Name: DevicegroupsPageController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/03/27 15:47:51  jijunwan
 *  Archive Log:    first version of VirtualFabric UI
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/24 17:46:10  jijunwan
 *  Archive Log:    init version of DeviceGroup editor
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/16 22:08:18  jijunwan
 *  Archive Log:    added device group visualization on UI
 *  Archive Log:    some refactory to make the conf framework to be more general
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl.devicegroups;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;

import com.intel.stl.api.management.devicegroups.DeviceGroup;
import com.intel.stl.ui.admin.ChangeState;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.admin.impl.AbstractEditorController;
import com.intel.stl.ui.admin.impl.ConfPageController;
import com.intel.stl.ui.admin.impl.ValidationTask;
import com.intel.stl.ui.admin.view.ValidationDialog;
import com.intel.stl.ui.admin.view.devicegroups.DevicegroupsEditorPanel;
import com.intel.stl.ui.admin.view.devicegroups.DevicegroupsSubpageView;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.main.Context;

public class DevicegroupsPageController extends
        ConfPageController<DeviceGroup, DevicegroupsEditorPanel> {
    private Set<String> reserved;

    /**
     * Description:
     * 
     * @param name
     * @param description
     * @param icon
     * @param view
     */
    public DevicegroupsPageController(String name, String description,
            ImageIcon icon, DevicegroupsSubpageView view) {
        super(name, description, icon, view);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.ConfPageController#setContext(com.intel.stl
     * .ui.main.Context, com.intel.stl.ui.common.IProgressObserver)
     */
    @Override
    public void setContext(Context context, IProgressObserver observer) {
        super.setContext(context, observer);
        edtCtr.setContext(context);
        reserved = mgtApi.getReservedDeviceGroups();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.ConfPageController#creatEditorController(
     * com.intel.stl.ui.admin.view.AbstractEditorPanel)
     */
    @Override
    protected AbstractEditorController<DeviceGroup, DevicegroupsEditorPanel> creatEditorController(
            DevicegroupsEditorPanel editorPanel) {
        return new DevicegroupsEditorController(editorPanel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.ConfPageController#getItems()
     */
    @Override
    protected ArrayList<Item<DeviceGroup>> initData() throws Exception {
        List<DeviceGroup> groups = mgtApi.getDeviceGroups();
        ArrayList<Item<DeviceGroup>> res = new ArrayList<Item<DeviceGroup>>();
        for (DeviceGroup group : groups) {
            // System.out.println(group);
            boolean isEditable = isEditable(group);
            Item<DeviceGroup> item =
                    new Item<DeviceGroup>(res.size(), group.getName(), group,
                            isEditable);
            item.setState(ChangeState.NONE);
            res.add(item);
        }
        return res;
    }

    /**
     * <i>Description:</i>
     * 
     * @param group
     * @return
     */
    private boolean isEditable(DeviceGroup group) {
        String name = group.getName();
        return !reserved.contains(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.ConfPageController#getCopy(java.lang.Object)
     */
    @Override
    protected DeviceGroup getCopy(DeviceGroup obj) {
        return obj.copy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.ConfPageController#createObj()
     */
    @Override
    protected DeviceGroup createObj() {
        return new DeviceGroup();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.ConfPageController#removeItemObject(java.
     * lang.String)
     */
    @Override
    protected void removeItemObject(String name) throws Exception {
        mgtApi.removeDeviceGroup(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.ConfPageController#getValidationTask(com.
     * intel.stl.ui.admin.view.ValidationDialog, com.intel.stl.ui.admin.Item)
     */
    @Override
    protected ValidationTask<DeviceGroup> getValidationTask(
            ValidationDialog dialog, Item<DeviceGroup> item) {
        DGValidationTask task =
                new DGValidationTask(dialog, valModel, orgItems, item);
        return task;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.ConfPageController#saveItemObject(java.lang
     * .String, java.lang.Object)
     */
    @Override
    protected void saveItemObject(String oldName, DeviceGroup obj)
            throws Exception {
        if (oldName != null) {
            mgtApi.updateDeviceGroup(oldName, obj);
        } else {
            mgtApi.addDeviceGroup(obj);
        }
    }

}
