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
 *  File Name: ExCombobox.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/30 14:28:23  jijunwan
 *  Archive Log:    created extended combobox model to support disabled items
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/27 18:40:02  jijunwan
 *  Archive Log:    moved extended JList and ComboBox to package common.view
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/13 23:05:37  jijunwan
 *  Archive Log:    PR 126911 - Even though HFI does not represent "Internal" data under opatop, FV still provides drop down for "Internal"
 *  Archive Log:     -- added a feature to be able to disable unsupported types
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;

import com.intel.stl.ui.common.ExComboBoxModel;

/**
 * Extended JComboBox that support disabled items
 */
public class ExComboBox<E> extends JComboBox<E> {
    private static final long serialVersionUID = 7774901357006599064L;

    private ExComboBoxModel<E> model;

    private Color disabledColor;

    /**
     * Description:
     * 
     */
    public ExComboBox() {
        super();
        super.setRenderer(new DisabledItemsRenderer());
    }

    /**
     * Description:
     * 
     * @param aModel
     */
    public ExComboBox(ExComboBoxModel<E> aModel) {
        super(aModel);
        super.setRenderer(new DisabledItemsRenderer());
    }

    /**
     * Description:
     * 
     * @param items
     */
    public ExComboBox(E[] items) {
        this(new ExComboBoxModel<E>(items, true));
    }

    /**
     * Description:
     * 
     * @param items
     */
    public ExComboBox(Vector<E> items) {
        this(new ExComboBoxModel<E>(items, true));
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComboBox#setModel(javax.swing.ComboBoxModel)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setModel(ComboBoxModel<E> aModel) {
        if (!(aModel instanceof ExComboBoxModel)) {
            throw new IllegalArgumentException("Model must be a "
                    + ExComboBoxModel.class.getSimpleName() + " model!");
        }
        model = (ExComboBoxModel) aModel;
        super.setModel(aModel);
    }

    /**
     * @param disabledColor
     *            the disabledColor to set
     */
    public void setDisabledColor(Color disabledColor) {
        this.disabledColor = disabledColor;
    }

    @Override
    public void setSelectedIndex(int index) {
        if (!model.isDisabled(index)) {
            super.setSelectedIndex(index);
        }
    }

    private class DisabledItemsRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = -5395462996710992524L;

        @SuppressWarnings("rawtypes")
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label =
                    (JLabel) super.getListCellRendererComponent(list, value,
                            index, isSelected, cellHasFocus);
            decorateDisabledCell(label, model.isDisabled(index));
            return label;
        }
    }

    protected void decorateDisabledCell(JLabel label, boolean isDisabled) {
        if (isDisabled) {
            if (disabledColor != null) {
                label.setForeground(disabledColor);
            } else {
                label.setForeground(UIManager
                        .getColor("Label.disabledForeground"));
            }
        }
    }
}
