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
 *  File Name: DGValidatiobTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
import java.util.concurrent.Callable;

import com.intel.stl.api.management.devicegroups.DeviceGroup;
import com.intel.stl.ui.admin.ChangeState;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.admin.impl.ValidationTask;
import com.intel.stl.ui.admin.view.ValidationDialog;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.ValidationItem;
import com.intel.stl.ui.common.ValidationModel;

public class DGValidationTask extends ValidationTask<DeviceGroup> {

    /**
     * Description:
     * 
     * @param dialog
     * @param model
     * @param items
     * @param toCheck
     */
    public DGValidationTask(ValidationDialog dialog,
            ValidationModel<DeviceGroup> model, List<Item<DeviceGroup>> items,
            Item<DeviceGroup> toCheck) {
        super(dialog, model, items, toCheck);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.SwingWorker#doInBackground()
     */
    @Override
    protected Integer doInBackground() throws Exception {
        if (toCheck.getState() == ChangeState.NONE) {
            return 0;
        }

        int count = 0;
        if (toCheck.getState() == ChangeState.UPDATE
                || toCheck.getState() == ChangeState.ADD) {
            dialog.reportProgress(STLConstants.K2124_NAME_CHECK.getValue()
                    + "...");
            ValidationItem<DeviceGroup> vi = uniqueNameCheck(toCheck);
            if (vi != null) {
                publish(vi);
                count += 1;
            }
        }

        if (toCheck.getState() != ChangeState.ADD) {
            dialog.reportProgress(STLConstants.K2127_REF_CHECK.getValue()
                    + "...");
            ValidationItem<DeviceGroup> vi = referenceCheck(toCheck);
            if (vi != null) {
                publish(vi);
                count += 1;
            }
        }
        return count;
    }

    /**
     * <i>Description:</i>
     * 
     * @param toCheck
     * @return
     */
    protected ValidationItem<DeviceGroup> uniqueNameCheck(
            Item<DeviceGroup> toCheck) {
        long id = toCheck.getId();
        String name = toCheck.getObj().getName();
        for (Item<DeviceGroup> item : items) {
            if (item.getId() != id && item.getObj().getName().equals(name)) {
                return new ValidationItem<DeviceGroup>(
                        STLConstants.K2124_NAME_CHECK.getValue(),
                        UILabels.STL81001_DUP_NAME.getDescription(),
                        UILabels.STL81002_DUP_NAME_SUG.getDescription());
            }
        }
        return null;
    }

    /**
     * <i>Description:</i>
     * 
     * @param toCheck
     * @return
     */
    protected ValidationItem<DeviceGroup> referenceCheck(Item<DeviceGroup> dg) {
        final String oldName = getDeviceGroup(dg.getId()).getName();
        final String newName = dg.getObj().getName();
        if (dg.getState() != ChangeState.REMOVE && oldName.equals(newName)) {
            return null;
        }

        final List<DeviceGroup> refs = getRelatedDGs(oldName);
        if (refs.isEmpty()) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (DeviceGroup ref : refs) {
            if (sb.length() == 0) {
                sb.append("'" + ref.getName() + "'");
            } else {
                sb.append("; '" + ref.getName() + "'");
            }
        }
        if (dg.getState() == ChangeState.REMOVE) {
            final ValidationItem<DeviceGroup> res =
                    new ValidationItem<DeviceGroup>(
                            STLConstants.K2127_REF_CHECK.getValue(),
                            UILabels.STL81003_REF_CONF.getDescription(sb
                                    .toString()),
                            UILabels.STL81004_REMOVE_REF.getDescription());
            Callable<DeviceGroup[]> quickFix = new Callable<DeviceGroup[]>() {

                @Override
                public DeviceGroup[] call() throws Exception {
                    DeviceGroup[] newDGs = new DeviceGroup[refs.size()];
                    for (int i = 0; i < refs.size(); i++) {
                        DeviceGroup dg = refs.get(i).copy();
                        dg.removeIncludeGroup(oldName);
                    }
                    fixedIssue(res);
                    return newDGs;
                }

            };
            res.setQuickFix(quickFix);
            return res;
        } else if (dg.getState() == ChangeState.UPDATE) {
            final ValidationItem<DeviceGroup> res =
                    new ValidationItem<DeviceGroup>(
                            STLConstants.K2127_REF_CHECK.getValue(),
                            UILabels.STL81003_REF_CONF.getDescription(sb
                                    .toString()),
                            UILabels.STL81005_UPDATE_REF.getDescription());
            Callable<DeviceGroup[]> quickFix = new Callable<DeviceGroup[]>() {

                @Override
                public DeviceGroup[] call() throws Exception {
                    DeviceGroup[] newDGs = new DeviceGroup[refs.size()];
                    for (int i = 0; i < refs.size(); i++) {
                        DeviceGroup dg = refs.get(i).copy();
                        int index = dg.indexOfIncludeGroup(oldName);
                        dg.removeIncludeGroup(oldName);
                        dg.insertIncludeGroup(index, newName);
                    }
                    fixedIssue(res);
                    return newDGs;
                }

            };
            res.setQuickFix(quickFix);
            return res;
        } else {
            return null;
        }
    }

    /**
     * <i>Description:</i>
     * 
     * @param id
     * @return
     */
    private DeviceGroup getDeviceGroup(long id) {
        for (Item<DeviceGroup> item : items) {
            if (item.getId() == id) {
                return item.getObj();
            }
        }
        throw new IllegalArgumentException("Couldn't find item by id=" + id);
    }

    /**
     * <i>Description:</i>
     * 
     * @param oldName
     * @return
     */
    private List<DeviceGroup> getRelatedDGs(String name) {
        List<DeviceGroup> res = new ArrayList<DeviceGroup>();
        for (Item<DeviceGroup> item : items) {
            DeviceGroup dg = item.getObj();
            if (dg.doesIncludeGroup(name)) {
                res.add(dg);
                break;
            }
        }
        return res;
    }

}
