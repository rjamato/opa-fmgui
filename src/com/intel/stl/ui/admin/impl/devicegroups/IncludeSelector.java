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
 *  File Name: IncludeSelector.java
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

import java.util.Collections;
import java.util.List;

import com.intel.stl.api.management.devicegroups.IncludeGroup;
import com.intel.stl.ui.admin.view.devicegroups.ListPanel;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.main.Context;

public class IncludeSelector extends ListSelector<IncludeGroup> {
    private List<IncludeGroup> groups;

    /**
     * Description:
     * 
     * @param view
     */
    public IncludeSelector(ListPanel<IncludeGroup> view) {
        super(view);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getDescription()
     */
    @Override
    public String getDescription() {
        return UILabels.STL81052_DG_INCLUDE_DESC.getDescription();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IContextAware#getName()
     */
    @Override
    public String getName() {
        return STLConstants.K2138_INCLUDE.getValue();
    }

    /**
     * @param groups
     *            the groups to set
     */
    public synchronized void setGroups(List<IncludeGroup> groups) {
        model = createModel(groups);
        view.setModel(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.devicegroups.ListSelector#getListData(com
     * .intel.stl.ui.main.Context)
     */
    @Override
    protected List<IncludeGroup> getListData(Context context) {
        // we do not init data here. instead we always set list data via method
        // #setGroups
        return Collections.emptyList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.devicegroups.ListSelector#getElementDesc(
     * com.intel.stl.api.management.IAttribute)
     */
    @Override
    protected String getElementDesc(IncludeGroup element) {
        return element.getValue();
    }

}
