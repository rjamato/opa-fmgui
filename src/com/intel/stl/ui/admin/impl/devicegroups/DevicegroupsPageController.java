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
 *  File Name: DevicegroupsPageController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/08/17 18:54:21  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/06/25 11:55:07  jypak
 *  Archive Log:    PR 129073 - Add help action for Admin Page.
 *  Archive Log:    The help action is added to App, DG, VF,Console page and Console terminal. For now, a help ID and a content are being used as a place holder for each page. Once we get the help contents delivered by technical writer team, the HelpAction will be updated with correct help ID.
 *  Archive Log:
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
import com.intel.stl.ui.main.HelpAction;

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

    @Override
    public String getHelpID() {
        return HelpAction.getInstance().getAdminDg();
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
