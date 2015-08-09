/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2014 Intel Corporation All Rights Reserved.
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
 *  File Name: FVTableModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/03/10 22:46:15  jijunwan
 *  Archive Log:    code clean up
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/12/10 20:52:20  rjtierne
 *  Archive Log:    Support for new Setup Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/17 19:19:35  rjtierne
 *  Archive Log:    Added getter method to retrieve a table row entry
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/29 22:04:44  jijunwan
 *  Archive Log:    minor changes - added comments, added toString()
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/29 14:21:16  jijunwan
 *  Archive Log:    thread safe table model, added bound control on table model
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/28 22:19:33  jijunwan
 *  Archive Log:    synchronized table model
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:46:33  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/09 21:02:41  rjtierne
 *  Archive Log:    Captured result from call to AddEntry() method
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/04 19:24:16  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Abstract model class to support a table
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class FVTableModel<E> extends AbstractTableModel {
    protected static final long serialVersionUID = 7593641813548886227L;

    public static final int DEFAULT_CAPABILITY = 1024;

    /**
     * Table header column names
     */
    protected String[] mColumnNames;

    /**
     * List of entries
     */
    protected List<E> mEntryList = new ArrayList<E>();

    protected int mCapability = DEFAULT_CAPABILITY;

    /**
     * Table model should be updated in EDT. The following synchronization code
     * is just for in case we accidentally do update from other threads. Maybe
     * we should remove these syn code to force us update a table model in EDT
     * assuming that we can easily figure out we are updating from wrong thread.
     */
    protected Object critical = new Object();

    /**
     * @return the mCapability
     */
    public int getCapability() {
        return mCapability;
    }

    /**
     * @param mCapability
     *            the mCapability to set
     */
    public void setCapability(int capability) {
        this.mCapability = capability;
    }

    /**
     * Description: Override getColumnName to set the column headings.
     * 
     * @param column
     *            - integer indicating the column number
     * 
     * @return result - name of the column heading
     * 
     */
    @Override
    public String getColumnName(int column) {
        if (mColumnNames == null) {
            return "";
        } else {
            return mColumnNames[column];
        }
    }

    /**
     * 
     * Description: Initializes the local column names
     * 
     * @param pColumnNames
     *            - string array of column names
     */
    protected void setColumnNames(String[] pColumnNames) {
        mColumnNames = pColumnNames;
    } // setColumnNames

    /**
     * 
     * Description: Add an entry to the list, appending a new row to the table
     * 
     * @param pEntry
     *            - incoming entry to set in the list
     * 
     * @return N/a
     */
    public boolean addEntry(E pEntry) {
        boolean result = false;

        synchronized (critical) {
            while (mEntryList.size() > mCapability) {
                mEntryList.remove(0);
            }
            result = mEntryList.add(pEntry);
        }

        return result;
    } // addEntry

    public void setEntries(List<E> entryList) {
        synchronized (critical) {
            this.mEntryList = entryList;
        }
    }

    /**
     * Description: Remove an event message from the list, deleting the
     * specified row in the table
     * 
     * @param pRow
     *            - row number
     * 
     * @return N/a
     * 
     */
    public void removeEntry(E pEntry) {
        synchronized (critical) {
            mEntryList.remove(pEntry);
        }
    } // removeEntry

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        synchronized (critical) {
            return mEntryList.size();
        }
    } // getRowCount

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return (mColumnNames == null) ? 0 : mColumnNames.length;

    }

    public E getEntry(int row) {
        return mEntryList.get(row);
    }

    @Override
    public abstract boolean isCellEditable(int rowIndex, int columnIndex);
} // abstract class FVTableModel
