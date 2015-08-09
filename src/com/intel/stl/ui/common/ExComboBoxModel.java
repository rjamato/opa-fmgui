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
 *  File Name: ExComboBoxModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/30 14:28:24  jijunwan
 *  Archive Log:    created extended combobox model to support disabled items
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

public class ExComboBoxModel<E> extends DefaultComboBoxModel<E> {
    private static final long serialVersionUID = 386462244488142481L;

    private final List<E> disabledItems = new ArrayList<E>();

    private final Set<Integer> disabledItemIds = new HashSet<Integer>();

    private final boolean adjustSelection;

    /**
     * Description:
     * 
     */
    public ExComboBoxModel(boolean adjustSelection) {
        super();
        this.adjustSelection = adjustSelection;
    }

    /**
     * Description:
     * 
     * @param items
     */
    public ExComboBoxModel(E[] items, boolean adjustSelection) {
        super(items);
        this.adjustSelection = adjustSelection;
    }

    /**
     * Description:
     * 
     * @param v
     */
    public ExComboBoxModel(Vector<E> v, boolean adjustSelection) {
        super(v);
        this.adjustSelection = adjustSelection;
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

    public void addDisabledItem(E item) {
        if (disabledItems.contains(item)) {
            return;
        }

        disabledItems.add(item);
        int index = getIndexOf(item);
        disabledItemIds.add(index);
        adjustSelection();
    }

    public void removeDisabledItem(E item) {
        if (disabledItems.remove(item)) {
            int index = getIndexOf(item);
            disabledItemIds.remove(index);
        }
    }

    public E getFirstAvailableItem() {
        for (int i = 0; i < getSize(); i++) {
            if (!disabledItemIds.contains(i)) {
                return getElementAt(i);
            }
        }
        return null;
    }

    protected void adjustSelection() {
        if (!adjustSelection) {
            return;
        }

        Object selected = getSelectedItem();
        if (selected == null) {
            return;
        }

        // shift to next avaliable one. if reach the end, use the first
        // available one
        E firstAvaiable = null;
        for (E item : disabledItems) {
            if (selected.equals(item)) {
                boolean foundDisabledItem = false;
                for (int i = 0; i < getSize(); i++) {
                    E element = getElementAt(i);

                    if (firstAvaiable == null && !disabledItemIds.contains(i)) {
                        firstAvaiable = element;
                    }

                    if (!foundDisabledItem) {
                        foundDisabledItem =
                                element != null && element.equals(item);
                    } else if (!disabledItemIds.contains(i)) {
                        setSelectedItem(element);
                        return;
                    }
                }
                setSelectedItem(firstAvaiable);
                return;
            }
        }
    }

    protected void chacheDisabledIndices() {
        disabledItemIds.clear();
        for (int i = 0; i < getSize(); i++) {
            E element = getElementAt(i);
            for (E item : disabledItems) {
                if (element != null && element.equals(item)) {
                    disabledItemIds.add(i);
                }
            }
        }
    }

    public boolean isDisabled(int index) {
        return disabledItemIds.contains(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.DefaultComboBoxModel#removeElementAt(int)
     */
    @Override
    public void removeElementAt(int index) {
        E element = getElementAt(index);
        disabledItems.remove(element);
        super.removeElementAt(index);
        chacheDisabledIndices();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.DefaultComboBoxModel#removeElement(java.lang.Object)
     */
    @Override
    public void removeElement(Object anObject) {
        disabledItems.remove(anObject);
        super.removeElement(anObject);
        chacheDisabledIndices();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.DefaultComboBoxModel#removeAllElements()
     */
    @Override
    public void removeAllElements() {
        disabledItems.clear();
        disabledItemIds.clear();
        super.removeAllElements();
    }

}
