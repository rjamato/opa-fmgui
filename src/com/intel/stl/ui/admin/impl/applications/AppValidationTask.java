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
 *  File Name: AppValidationTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/08/17 18:54:53  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/03 16:53:34  jijunwan
 *  Archive Log:    improved range check tohandle unsigned values
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/16 22:08:16  jijunwan
 *  Archive Log:    added device group visualization on UI
 *  Archive Log:    some refactory to make the conf framework to be more general
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/11 21:16:03  jijunwan
 *  Archive Log:    added remove and deploy features
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/10 22:45:34  jijunwan
 *  Archive Log:    improved to do and show validation before we save an application
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
import java.util.concurrent.Callable;

import com.intel.stl.api.management.applications.Application;
import com.intel.stl.api.management.applications.MGID;
import com.intel.stl.api.management.applications.MGIDRange;
import com.intel.stl.api.management.applications.ServiceID;
import com.intel.stl.api.management.applications.ServiceIDRange;
import com.intel.stl.ui.admin.ChangeState;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.admin.impl.ValidationTask;
import com.intel.stl.ui.admin.view.ValidationDialog;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.ValidationItem;
import com.intel.stl.ui.common.ValidationModel;

public class AppValidationTask extends ValidationTask<Application> {

    /**
     * Description:
     * 
     * @param dialog
     * @param model
     * @param items
     * @param toCheck
     */
    public AppValidationTask(ValidationDialog dialog,
            ValidationModel<Application> model, List<Item<Application>> items,
            Item<Application> toCheck) {
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
            ValidationItem<Application> vi = uniqueNameCheck(toCheck);
            if (vi != null) {
                publish(vi);
                count += 1;
            }

            count += valueCheck(toCheck);
        }

        if (toCheck.getState() != ChangeState.ADD) {
            dialog.reportProgress(STLConstants.K2127_REF_CHECK.getValue()
                    + "...");
            ValidationItem<Application> vi = referenceCheck(toCheck);
            if (vi != null) {
                publish(vi);
                count += 1;
            }
        }
        return count;
    }

    protected ValidationItem<Application> uniqueNameCheck(Item<Application> app) {
        long id = app.getId();
        String name = app.getObj().getName();
        for (Item<Application> item : items) {
            if (item.getId() != id && item.getObj().getName().equals(name)) {
                return new ValidationItem<Application>(
                        STLConstants.K2124_NAME_CHECK.getValue(),
                        UILabels.STL81001_DUP_NAME.getDescription(),
                        UILabels.STL81002_DUP_NAME_SUG.getDescription());
            }
        }
        return null;
    }

    protected int valueCheck(Item<Application> item) {
        dialog.reportProgress(STLConstants.K2125_VALUE_CHECK.getValue() + "...");

        int count = 0;
        Application app = item.getObj();
        List<ServiceID> sids = app.getServiceIDs();
        if (sids != null) {
            for (ServiceID sid : sids) {
                if (sid instanceof ServiceIDRange) {
                    ValidationItem<Application> vi =
                            rangeCheck((ServiceIDRange) sid);
                    if (vi != null) {
                        publish(vi);
                        count += 1;
                    }
                }
            }
        }
        List<MGID> mgids = app.getMgids();
        if (mgids != null) {
            for (MGID mgid : mgids) {
                if (mgid instanceof MGIDRange) {
                    ValidationItem<Application> vi =
                            rangeCheck((MGIDRange) mgid);
                    if (vi != null) {
                        publish(vi);
                        count += 1;
                    }
                }
            }
        }
        return count;
    }

    protected ValidationItem<Application> rangeCheck(ServiceIDRange range) {
        if (!unsignedRangeCheck(range.getMin(), range.getMax())) {
            return new ValidationItem<Application>(
                    STLConstants.K2125_VALUE_CHECK.getValue(),
                    UILabels.STL81006_INVALID_IDRANGE.getDescription(range
                            .toString()),
                    UILabels.STL81002_DUP_NAME_SUG.getDescription());
        } else {
            return null;
        }
    }

    protected ValidationItem<Application> rangeCheck(MGIDRange range) {
        boolean invalid = true;
        if (unsignedRangeCheck(range.getMinLower(), range.getMaxLower())) {
            invalid = false;
        } else if (range.getMinLower() == range.getMaxLower()) {
            invalid =
                    !unsignedRangeCheck(range.getMinUpper(),
                            range.getMaxUpper());
        }

        if (invalid) {
            return new ValidationItem<Application>(
                    STLConstants.K2125_VALUE_CHECK.getValue(),
                    UILabels.STL81008_INVALID_GIDRANGE.getDescription(range
                            .toString()),
                    UILabels.STL81009_CHANGE_GID.getDescription());
        } else {
            return null;
        }
    }

    protected boolean unsignedRangeCheck(long min, long max) {
        if (min >= 0) {
            return max < 0 || max > min;
        } else {
            return max < 0 && max < min;
        }
    }

    protected ValidationItem<Application> referenceCheck(Item<Application> app) {
        final String oldName = getApplication(app.getId()).getName();
        final String newName = app.getObj().getName();
        if (app.getState() != ChangeState.REMOVE && oldName.equals(newName)) {
            return null;
        }

        final List<Application> refs = getRelatedApps(oldName);
        if (refs.isEmpty()) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (Application ref : refs) {
            if (sb.length() == 0) {
                sb.append("'" + ref.getName() + "'");
            } else {
                sb.append("; '" + ref.getName() + "'");
            }
        }
        if (app.getState() == ChangeState.REMOVE) {
            final ValidationItem<Application> res =
                    new ValidationItem<Application>(
                            STLConstants.K2127_REF_CHECK.getValue(),
                            UILabels.STL81003_REF_CONF.getDescription(sb
                                    .toString()),
                            UILabels.STL81004_REMOVE_REF.getDescription());
            Callable<Application[]> quickFix = new Callable<Application[]>() {

                @Override
                public Application[] call() throws Exception {
                    Application[] newApps = new Application[refs.size()];
                    for (int i = 0; i < refs.size(); i++) {
                        Application app = refs.get(i).copy();
                        app.removeIncludeApplication(oldName);
                    }
                    fixedIssue(res);
                    return newApps;
                }

            };
            res.setQuickFix(quickFix);
            return res;
        } else if (app.getState() == ChangeState.UPDATE) {
            final ValidationItem<Application> res =
                    new ValidationItem<Application>(
                            STLConstants.K2127_REF_CHECK.getValue(),
                            UILabels.STL81003_REF_CONF.getDescription(sb
                                    .toString()),
                            UILabels.STL81005_UPDATE_REF.getDescription());
            Callable<Application[]> quickFix = new Callable<Application[]>() {

                @Override
                public Application[] call() throws Exception {
                    Application[] newApps = new Application[refs.size()];
                    for (int i = 0; i < refs.size(); i++) {
                        Application app = refs.get(i).copy();
                        int index = app.indexOfIncludeApplication(oldName);
                        app.removeIncludeApplication(oldName);
                        app.insertIncludeApplication(index, newName);
                    }
                    fixedIssue(res);
                    return newApps;
                }

            };
            res.setQuickFix(quickFix);
            return res;
        } else {
            return null;
        }
    }

    protected Application getApplication(long id) {
        for (Item<Application> item : items) {
            if (item.getId() == id) {
                return item.getObj();
            }
        }
        throw new IllegalArgumentException("Couldn't find item by id=" + id);
    }

    protected List<Application> getRelatedApps(String name) {
        List<Application> res = new ArrayList<Application>();
        for (Item<Application> item : items) {
            Application app = item.getObj();
            if (app.doesIncludeApplication(name)) {
                res.add(app);
                break;
            }
        }
        return res;
    }

}
