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
 *  File Name: ValidationModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/10 22:45:35  jijunwan
 *  Archive Log:    improved to do and show validation before we save an application
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

public class ValidationModel<T> extends FVTableModel<ValidationItem<T>> {
    private static final long serialVersionUID = 2851099371218885049L;

    public ValidationModel() {
        setColumnNames(ValidationItem.NAMES);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ValidationItem<T> item = getEntry(rowIndex);
        if (columnIndex == 0) {
            return item.getType();
        } else if (columnIndex == 1) {
            return item.getIssues();
        } else if (columnIndex == 2) {
            return item.getSuggestions();
        } else if (columnIndex == 3) {
            return item.getQuickFix();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex != 3) {
            return String.class;
        } else {
            return Boolean.class;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.FVTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void clear() {
        synchronized (critical) {
            mEntryList.clear();
        }
    }
}
