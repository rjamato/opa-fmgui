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
 *  File Name: VirtualFabricsPageController.java
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
import java.util.Set;

import javax.swing.ImageIcon;

import com.intel.stl.api.management.virtualfabrics.VirtualFabric;
import com.intel.stl.ui.admin.ChangeState;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.admin.impl.AbstractEditorController;
import com.intel.stl.ui.admin.impl.ConfPageController;
import com.intel.stl.ui.admin.impl.ValidationTask;
import com.intel.stl.ui.admin.view.AbstractConfView;
import com.intel.stl.ui.admin.view.ValidationDialog;
import com.intel.stl.ui.admin.view.virtualfabrics.VirtualFabricsEditorPanel;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.main.Context;

public class VirtualFabricsPageController extends
        ConfPageController<VirtualFabric, VirtualFabricsEditorPanel> {
    private Set<String> reserved;

    /**
     * Description:
     * 
     * @param name
     * @param description
     * @param icon
     * @param view
     */
    public VirtualFabricsPageController(String name, String description,
            ImageIcon icon,
            AbstractConfView<VirtualFabric, VirtualFabricsEditorPanel> view) {
        super(name, description, icon, view);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.ConfPageController#creatEditorController(
     * com.intel.stl.ui.admin.view.AbstractEditorPanel)
     */
    @Override
    protected AbstractEditorController<VirtualFabric, VirtualFabricsEditorPanel> creatEditorController(
            VirtualFabricsEditorPanel editorPanel) {
        return new VirtualFabricsEditorController(editorPanel);
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
        reserved = mgtApi.getReservedVirtualFabrics();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.ConfPageController#getItems()
     */
    @Override
    protected ArrayList<Item<VirtualFabric>> initData() throws Exception {
        edtCtr.initData();

        List<VirtualFabric> vfs = mgtApi.getVirtualFabrics();
        ArrayList<Item<VirtualFabric>> res =
                new ArrayList<Item<VirtualFabric>>();
        for (VirtualFabric vf : vfs) {
            boolean isEditable = isEditable(vf);
            Item<VirtualFabric> item =
                    new Item<VirtualFabric>(res.size(), vf.getName(), vf,
                            isEditable);
            item.setState(ChangeState.NONE);
            res.add(item);
        }
        return res;
    }

    /**
     * <i>Description:</i>
     * 
     * @param vf
     * @return
     */
    private boolean isEditable(VirtualFabric vf) {
        String name = vf.getName();
        return !reserved.contains(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.ConfPageController#getCopy(java.lang.Object)
     */
    @Override
    protected VirtualFabric getCopy(VirtualFabric obj) {
        return obj.copy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.ConfPageController#createObj()
     */
    @Override
    protected VirtualFabric createObj() {
        return new VirtualFabric();
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
        mgtApi.removeVirtualFabric(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.ConfPageController#getValidationTask(com.
     * intel.stl.ui.admin.view.ValidationDialog, com.intel.stl.ui.admin.Item)
     */
    @Override
    protected ValidationTask<VirtualFabric> getValidationTask(
            ValidationDialog dialog, Item<VirtualFabric> item) {
        VFValidationTask task =
                new VFValidationTask(dialog, valModel, orgItems, item);
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
    protected void saveItemObject(String oldName, VirtualFabric obj)
            throws Exception {
        if (oldName != null) {
            mgtApi.updateVirtualFabric(oldName, obj);
        } else {
            mgtApi.addVirtualFabric(obj);
        }
    }

}
