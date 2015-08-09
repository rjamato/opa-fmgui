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
 *  File Name: ValidationTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/10 22:45:36  jijunwan
 *  Archive Log:    improved to do and show validation before we save an application
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.intel.stl.api.StringUtils;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.admin.view.ValidationDialog;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.ValidationItem;
import com.intel.stl.ui.common.ValidationModel;

public abstract class ValidationTask<T> extends
        SwingWorker<Integer, ValidationItem<T>> {
    protected final ValidationDialog dialog;

    protected ValidationModel<T> model;

    protected final List<Item<T>> items;

    protected final Item<T> toCheck;

    public ValidationTask(ValidationDialog dialog, ValidationModel<T> model,
            List<Item<T>> items, Item<T> toCheck) {
        this.dialog = dialog;
        this.model = model;
        this.items = items;
        this.toCheck = toCheck;
    }

    @Override
    protected void done() {
        Integer result;
        try {
            result = get();
            onTaskSuccess(result);
        } catch (InterruptedException e) {
            onTaskFailure(e);
        } catch (ExecutionException e) {
            onTaskFailure(e.getCause());
        } finally {
            onFinally();
        }
    }

    public void onTaskSuccess(Integer result) {
        if (result == 0) {
            dialog.showMessage(UILabels.STL81103_NO_ISSUES_FOUND
                    .getDescription());
            dialog.enableOk(true);
        } else {
            String msg =
                    result == 1 ? UILabels.STL81104_ONE_ISSUE_FOUND
                            .getDescription() : UILabels.STL81102_ISSUES_FOUND
                            .getDescription(result);
            dialog.showMessage(msg);
            dialog.enableOk(false);
        }
    }

    public void onTaskFailure(Throwable caught) {
        caught.printStackTrace();
        dialog.showMessage(StringUtils.getErrorMessage(caught));
        dialog.enableOk(false);
    }

    public void onFinally() {
        dialog.reportProgress("100%");
        dialog.stopProgress();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.framework.AbstractTask#processIntermediateResults(java
     * .util.List)
     */
    @Override
    protected void process(List<ValidationItem<T>> intermediateResults) {
        for (ValidationItem<T> issue : intermediateResults) {
            model.addEntry(issue);
        }
        model.fireTableDataChanged();
        dialog.updateIssues();
    }

    protected void fixedIssue(ValidationItem<T> issue) {
        model.removeEntry(issue);
        model.fireTableDataChanged();
        onTaskSuccess(model.getRowCount());
        dialog.updateIssues();
    }
}
