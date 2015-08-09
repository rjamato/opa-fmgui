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
 *  File Name: VFValidationTask.java
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

import java.util.List;

import com.intel.stl.api.management.virtualfabrics.VirtualFabric;
import com.intel.stl.ui.admin.ChangeState;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.admin.impl.ValidationTask;
import com.intel.stl.ui.admin.view.ValidationDialog;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.ValidationItem;
import com.intel.stl.ui.common.ValidationModel;

public class VFValidationTask extends ValidationTask<VirtualFabric> {

    /**
     * Description:
     * 
     * @param dialog
     * @param model
     * @param items
     * @param toCheck
     */
    public VFValidationTask(ValidationDialog dialog,
            ValidationModel<VirtualFabric> model,
            List<Item<VirtualFabric>> items, Item<VirtualFabric> toCheck) {
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
            ValidationItem<VirtualFabric> vi = uniqueNameCheck(toCheck);
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
    protected ValidationItem<VirtualFabric> uniqueNameCheck(
            Item<VirtualFabric> toCheck) {
        long id = toCheck.getId();
        String name = toCheck.getObj().getName();
        for (Item<VirtualFabric> item : items) {
            if (item.getId() != id && item.getObj().getName().equals(name)) {
                return new ValidationItem<VirtualFabric>(
                        STLConstants.K2124_NAME_CHECK.getValue(),
                        UILabels.STL81001_DUP_NAME.getDescription(),
                        UILabels.STL81002_DUP_NAME_SUG.getDescription());
            }
        }
        return null;
    }

}
