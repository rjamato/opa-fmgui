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
 *  File Name: HoqLifeRenderer.java
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.text.DecimalFormat;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import com.intel.stl.api.management.virtualfabrics.HoqLife;
import com.intel.stl.api.management.virtualfabrics.HoqLife.TimeOut;
import com.intel.stl.api.management.virtualfabrics.HoqLife.TimeOut.Unit;
import com.intel.stl.ui.admin.view.AbstractAttrRenderer;
import com.intel.stl.ui.common.view.IntelComboBoxUI;

public class HoqLifeRenderer extends AbstractAttrRenderer<HoqLife> {
    private final JPanel panel;

    private final JFormattedTextField field;

    private final JComboBox<Unit> unitBox;

    public HoqLifeRenderer() {
        super();
        panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);
        field = new JFormattedTextField(new DecimalFormat("###"));
        field.setValue(new Integer(0));
        panel.add(field, BorderLayout.CENTER);
        unitBox = new JComboBox<Unit>(Unit.values());
        IntelComboBoxUI ui = new IntelComboBoxUI() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * com.intel.stl.ui.common.view.IntelComboBoxUI#getValueString(java
             * .lang.Object)
             */
            @Override
            protected String getValueString(Object value) {
                return ((Unit) value).name().toLowerCase();
            }

        };
        unitBox.setUI(ui);
        unitBox.setPreferredSize(new Dimension(50, 20));
        panel.add(unitBox, BorderLayout.EAST);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#setAttr(com.intel.stl.api.
     * management.IAttribute)
     */
    @Override
    public void setAttr(HoqLife attr) {
        TimeOut to = attr.getObject();
        field.setValue(to.getValue());
        unitBox.setSelectedItem(to.getUnit());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#setEditable(boolean)
     */
    @Override
    public void setEditable(boolean isEditable) {
        field.setEditable(isEditable);
        unitBox.setEnabled(isEditable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#getAttr()
     */
    @Override
    public HoqLife getAttr() {
        int value = ((Number) field.getValue()).intValue();
        Unit unit = (Unit) unitBox.getSelectedItem();
        return new HoqLife(new TimeOut(value, unit));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.AbstractAttrRenderer#getFields()
     */
    @Override
    protected Component[] getFields() {
        return new Component[] { panel };
    }

}
