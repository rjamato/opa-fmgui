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
 *  File Name: ListPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/25 19:47:28  jijunwan
 *  Archive Log:    put list in a scrollpane
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/24 17:46:09  jijunwan
 *  Archive Log:    init version of DeviceGroup editor
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view.devicegroups;

import java.awt.Component;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import com.intel.stl.ui.admin.impl.devicegroups.SelectionWrapper;
import com.intel.stl.ui.common.UIConstants;

public class ListPanel<E> extends JScrollPane {
    private static final long serialVersionUID = 8708700061726534862L;

    private final JList<SelectionWrapper<E>> list;

    /**
     * Description:
     * 
     */
    public ListPanel() {
        super();
        list = new JList<SelectionWrapper<E>>();
        list.setCellRenderer(new ListRenderer());
        setViewportView(list);
    }

    class ListRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = -3056242589407591880L;

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.DefaultListCellRenderer#getListCellRendererComponent(
         * javax.swing.JList, java.lang.Object, int, boolean, boolean)
         */
        @SuppressWarnings("unchecked")
        @Override
        public Component getListCellRendererComponent(JList<?> list,
                Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JLabel res =
                    (JLabel) super.getListCellRendererComponent(list, value,
                            index, isSelected, cellHasFocus);
            if (value instanceof SelectionWrapper) {
                SelectionWrapper<E> sw = (SelectionWrapper<E>) value;
                if (sw.isSelected()) {
                    res.setEnabled(false);
                }
            }
            if (!isSelected) {
                res.setOpaque(true);
                res.setBackground(index % 2 == 0 ? UIConstants.INTEL_WHITE
                        : UIConstants.INTEL_TABLE_ROW_GRAY);
            }
            return res;
        }
    }

    public void setModel(ListModel<SelectionWrapper<E>> model) {
        list.setModel(model);
    }

    /**
     * <i>Description:</i>
     * 
     * @param selectionModel
     */
    public void setSelectionModel(ListSelectionModel selectionModel) {
        list.setSelectionModel(selectionModel);
    }

    /**
     * <i>Description:</i>
     * 
     * @return
     */
    public List<SelectionWrapper<E>> getSelectedValuesList() {
        return list.getSelectedValuesList();
    }

    /**
     * <i>Description:</i>
     * 
     */
    public void clearSelection() {
        list.clearSelection();
    }
}
