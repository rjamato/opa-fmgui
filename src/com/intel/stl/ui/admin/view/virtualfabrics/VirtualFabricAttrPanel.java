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
 *  File Name: VFAttrPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/30 14:25:37  jijunwan
 *  Archive Log:    1) introduced IRendererModel to create renderer only we nee
 *  Archive Log:    2) removed #getName from IAttrRenderer to provide more flexibilities and let IRendererModel to take care which attribute should use which renderer, how to init it properly
 *  Archive Log:    3) improved to support repeatable and non-repeatable attributes. For non-repeatable attributes, we only can add once
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/27 15:47:46  jijunwan
 *  Archive Log:    first version of VirtualFabric UI
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view.virtualfabrics;

import com.intel.stl.ui.admin.impl.virtualfabrics.VirtualFabricRendererModel;
import com.intel.stl.ui.admin.view.AbstractAttrPanel;
import com.intel.stl.ui.common.ExComboBoxModel;

public class VirtualFabricAttrPanel extends AbstractAttrPanel {
    private static final long serialVersionUID = 7079382835741909390L;

    private final VirtualFabricsEditorPanel parent;

    /**
     * Description:
     * 
     * @param parent
     */
    public VirtualFabricAttrPanel(VirtualFabricsEditorPanel parent,
            VirtualFabricRendererModel rendererModel) {
        super(rendererModel);
        this.parent = parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.AbstractAttrPanel#onAddAttr()
     */
    @Override
    protected void onAddAttr() {
        parent.beginEdit(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.AbstractAttrPanel#onRemoveAttr()
     */
    @Override
    protected void onRemoveAttr() {
        parent.removeEditor(this);
    }

    public void addDisabledAttr(String name) {
        ExComboBoxModel<String> model =
                (ExComboBoxModel<String>) typeList.getModel();
        model.addDisabledItem(name);
        repaint();
    }

    public void removeDisabledAttr(String name) {
        ExComboBoxModel<String> model =
                (ExComboBoxModel<String>) typeList.getModel();
        model.removeDisabledItem(name);
        repaint();
    }

    public void setDisabledAttrs(String[] names) {
        ExComboBoxModel<String> model =
                (ExComboBoxModel<String>) typeList.getModel();
        model.setDisabledItem(names);
        repaint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.view.AbstractAttrPanel#onChangeRenderer(java.lang
     * .String, java.lang.String)
     */
    @Override
    protected void onChangeRenderer(String oldRenderer, String newRenderer) {
        parent.changeEditorRenderer(oldRenderer, newRenderer);
    }

}
