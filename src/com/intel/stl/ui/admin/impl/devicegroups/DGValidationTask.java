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
 *  File Name: DGValidatiobTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/08/17 18:54:21  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
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
