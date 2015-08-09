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
 *  File Name: AppsPageController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/03/27 15:47:52  jijunwan
 *  Archive Log:    first version of VirtualFabric UI
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/16 22:08:16  jijunwan
 *  Archive Log:    added device group visualization on UI
 *  Archive Log:    some refactory to make the conf framework to be more general
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/10 22:45:34  jijunwan
 *  Archive Log:    improved to do and show validation before we save an application
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:38:20  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl.applications;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;

import com.intel.stl.api.management.applications.Application;
import com.intel.stl.ui.admin.ChangeState;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.admin.impl.ConfPageController;
import com.intel.stl.ui.admin.impl.ValidationTask;
import com.intel.stl.ui.admin.view.ValidationDialog;
import com.intel.stl.ui.admin.view.applications.AppsEditorPanel;
import com.intel.stl.ui.admin.view.applications.AppsSubpageView;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.main.Context;

public class AppsPageController extends
        ConfPageController<Application, AppsEditorPanel> {
    private Set<String> reserved;

    /**
     * Description:
     * 
     * @param name
     * @param description
     * @param icon
     * @param view
     */
    public AppsPageController(String name, String description, ImageIcon icon,
            AppsSubpageView view) {
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
        reserved = mgtApi.getReservedApplications();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.ConfPageController#getItems()
     */
    @Override
    protected ArrayList<Item<Application>> initData() throws Exception {
        List<Application> apps = mgtApi.getApplications();
        ArrayList<Item<Application>> res = new ArrayList<Item<Application>>();
        for (Application app : apps) {
            boolean isEditable = isEditable(app);
            Item<Application> item =
                    new Item<Application>(res.size(), app.getName(), app,
                            isEditable);
            item.setState(ChangeState.NONE);
            res.add(item);
        }
        return res;
    }

    /**
     * <i>Description:</i>
     * 
     * @param app
     * @return
     */
    protected boolean isEditable(Application app) {
        String name = app.getName();
        return !reserved.contains(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.ConfPageController#getCopy(java.lang.Object)
     */
    @Override
    protected Application getCopy(Application obj) {
        return obj.copy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.ConfPageController#createObj()
     */
    @Override
    protected Application createObj() {
        return new Application();
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
        mgtApi.removeApplication(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.ConfPageController#saveItemObject(java.lang
     * .Object)
     */
    @Override
    protected void saveItemObject(String oldName, Application obj)
            throws Exception {
        if (oldName != null) {
            mgtApi.updateApplication(oldName, obj);
        } else {
            mgtApi.addApplication(obj);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.ConfPageController#getValidationTask(com.
     * intel.stl.ui.admin.view.ValidationDialog, com.intel.stl.ui.admin.Item)
     */
    @Override
    protected ValidationTask<Application> getValidationTask(
            ValidationDialog dialog, Item<Application> item) {
        AppValidationTask task =
                new AppValidationTask(dialog, valModel, orgItems, item);
        return task;
    }

}
