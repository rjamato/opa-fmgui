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
 *  File Name: ListRenderer.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/03/30 14:25:39  jijunwan
 *  Archive Log:    1) introduced IRendererModel to create renderer only we nee
 *  Archive Log:    2) removed #getName from IAttrRenderer to provide more flexibilities and let IRendererModel to take care which attribute should use which renderer, how to init it properly
 *  Archive Log:    3) improved to support repeatable and non-repeatable attributes. For non-repeatable attributes, we only can add once
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/27 15:47:49  jijunwan
 *  Archive Log:    first version of VirtualFabric UI
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/24 17:43:51  jijunwan
 *  Archive Log:    applied IAttribute on the item editor framework
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/16 22:08:15  jijunwan
 *  Archive Log:    added device group visualization on UI
 *  Archive Log:    some refactory to make the conf framework to be more general
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.intel.stl.api.management.IAttribute;
import com.intel.stl.api.management.WrapperNode;
import com.intel.stl.ui.common.view.IntelComboBoxUI;

public class ListRenderer<E extends IAttribute> implements IAttrRenderer<E> {
    private final JComboBox<E> list;

    public ListRenderer() {
        this(null);
    }

    public ListRenderer(E[] data) {
        list = data == null ? new JComboBox<E>() : new JComboBox<E>(data);
        IntelComboBoxUI ui = new IntelComboBoxUI() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * com.intel.stl.ui.common.view.IntelComboBoxUI#getValueString(java
             * .lang.Object)
             */
            @SuppressWarnings("unchecked")
            @Override
            protected String getValueString(Object value) {
                if (value == null) {
                    return "";
                } else {
                    return getStringForList((E) value);
                }
            }

        };
        list.setUI(ui);
    }

    protected String getStringForList(E value) {
        if (value instanceof WrapperNode) {
            return ((WrapperNode<?>) value).getObject().toString();
        } else {
            return value.toString();
        }
    }

    public void setList(E[] names) {
        list.setModel(new DefaultComboBoxModel<E>(names));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#setAttr(java.lang.Object)
     */
    @Override
    public void setAttr(E attr) {
        list.setSelectedItem(attr);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#setEditable(boolean)
     */
    @Override
    public void setEditable(boolean isEditable) {
        list.setEnabled(isEditable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#getAttr()
     */
    @SuppressWarnings("unchecked")
    @Override
    public E getAttr() {
        return (E) list.getSelectedItem();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#getView()
     */
    @Override
    public JComponent getView() {
        return list;
    }

}
