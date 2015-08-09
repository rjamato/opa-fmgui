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
 *  File Name: ExJXList.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/04/02 11:57:35  jypak
 *  Archive Log:    Use a ListDataListener to process removing items from this extended JList.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/27 18:40:02  jijunwan
 *  Archive Log:    moved extended JList and ComboBox to package common.view
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/17 15:56:18  jypak
 *  Archive Log:    DataType, HistoryType JComboBox have been replaced with button popup to save space.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.jdesktop.swingx.JXList;

/**
 * Extended JComboBox that support disabled items
 */
public class ExJXList<E> extends JXList {
    private static final long serialVersionUID = 7774901357006599064L;

    private final List<E> disabledItems = new ArrayList<E>();

    private final Set<Integer> disabledItemIds = new HashSet<Integer>();

    private Color disabledColor;

    private ListModel<?> dataModel;

    /**
     * Description:
     * 
     * @param items
     */
    public ExJXList(E[] items) {
        super(items);
        super.setCellRenderer(new DisabledItemsRenderer());
        dataModel = super.getModel();
        dataModel.addListDataListener(new ExListDataListener());
    }

    /**
     * @param disabledColor
     *            the disabledColor to set
     */
    public void setDisabledColor(Color disabledColor) {
        this.disabledColor = disabledColor;
    }

    public void remove(int anIndex) {
        @SuppressWarnings("unchecked")
        E toRemove = (E) dataModel.getElementAt(anIndex);
        disabledItems.remove(toRemove);
        chacheDisabledIndices();
        adjustSelection();
    }

    @SuppressWarnings("unchecked")
    public void setDisabledItem(E... items) {
        disabledItems.clear();
        disabledItemIds.clear();
        if (items == null || items.length == 0) {
            return;
        }

        disabledItems.addAll(Arrays.asList(items));
        chacheDisabledIndices();
        adjustSelection();
    }

    protected void chacheDisabledIndices() {
        disabledItemIds.clear();
        for (int i = 0; i < dataModel.getSize(); i++) {
            @SuppressWarnings("unchecked")
            E element = (E) dataModel.getElementAt(i);
            for (E item : disabledItems) {
                if (element != null && element.equals(item)) {
                    disabledItemIds.add(i);
                }
            }
        }
    }

    protected void adjustSelection() {
        Object selected = super.getSelectedValue();
        if (selected == null) {
            return;
        }

        for (E item : disabledItems) {
            if (selected.equals(item)) {
                boolean foundDisabledItem = false;
                for (int i = 0; i < dataModel.getSize(); i++) {
                    @SuppressWarnings("unchecked")
                    E element = (E) dataModel.getElementAt(i);
                    if (!foundDisabledItem) {
                        foundDisabledItem =
                                element != null && element.equals(item);
                    } else if (!disabledItemIds.contains(i)) {
                        super.setSelectedValue(element, true);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void setSelectedIndex(int index) {
        if (!disabledItemIds.contains(index)) {
            super.setSelectedIndex(index);
        }
    }

    /**
     * Description:
     * 
     * @param items
     */
    public ExJXList(Vector<E> items) {
        super(items);
        super.setCellRenderer(new DisabledItemsRenderer());
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
            if (disabledItemIds.contains(index)) {
                if (disabledColor != null) {
                    label.setForeground(disabledColor);
                } else {
                    label.setForeground(UIManager
                            .getColor("Label.disabledForeground"));
                }

            }
            return label;
        }
    }

    private class ExListDataListener implements ListDataListener {

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.ListDataListener#intervalAdded(javax.swing.event
         * .ListDataEvent)
         */
        @Override
        public void intervalAdded(ListDataEvent e) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event
         * .ListDataEvent)
         */
        @Override
        public void intervalRemoved(ListDataEvent e) {
            // TODO Auto-generated method stub
            int low = e.getIndex0();
            int high = e.getIndex1();
            while (low <= high) {
                remove(low);
                low++;
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.ListDataListener#contentsChanged(javax.swing.event
         * .ListDataEvent)
         */
        @Override
        public void contentsChanged(ListDataEvent e) {
            // TODO Auto-generated method stub

        }

    }
}
