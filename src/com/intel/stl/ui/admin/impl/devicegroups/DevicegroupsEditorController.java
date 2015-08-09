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
 *  File Name: DevicegroupsEditorController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/24 17:46:10  jijunwan
 *  Archive Log:    init version of DeviceGroup editor
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl.devicegroups;

import java.util.List;

import com.intel.stl.api.management.IAttribute;
import com.intel.stl.api.management.devicegroups.DeviceGroup;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.admin.impl.AbstractEditorController;
import com.intel.stl.ui.admin.view.devicegroups.DevicegroupsEditorPanel;
import com.intel.stl.ui.main.Context;

public class DevicegroupsEditorController extends
        AbstractEditorController<DeviceGroup, DevicegroupsEditorPanel>
        implements IAttributeListener {
    private final DevicegroupSelectionController selectionCtr;

    public DevicegroupsEditorController(DevicegroupsEditorPanel view) {
        super(view);
        view.setAttributeListener(this);
        selectionCtr =
                new DevicegroupSelectionController(view.getSelctionPanel());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.AbstractEditorController#setContext(com.intel
     * .stl.ui.main.Context)
     */
    @Override
    public void setContext(Context context) {
        super.setContext(context);
        selectionCtr.setContext(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.AbstractEditorController#setItem(com.intel
     * .stl.ui.admin.Item, com.intel.stl.ui.admin.Item[])
     */
    @Override
    public void setItem(Item<DeviceGroup> item, Item<DeviceGroup>[] items) {
        selectionCtr.setItem(item, items);
        super.setItem(item, items);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.devicegroups.IAttributeListener#onAdd(java
     * .long.String)
     */
    @Override
    public void onAdd(String selectorName) {
        List<IAttribute> attrs = selectionCtr.getSelections(selectorName);
        for (IAttribute attr : attrs) {
            view.addAttr(attr);
        }
        selectionCtr.clearViewSelections(selectorName);
        selectionCtr.addSelections(selectorName, attrs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.devicegroups.IAttributeListener#onRemove(
     * com.intel.stl.api.management.IAttribute)
     */
    @Override
    public void onRemove(IAttribute attr) {
        selectionCtr.removeSelection(attr);
    }
}
