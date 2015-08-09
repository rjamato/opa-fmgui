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
 *  File Name: EventRulesTableModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/02/13 21:29:17  rjtierne
 *  Archive Log:    Multinet Wizard: Initial Version
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/13 19:00:24  rjtierne
 *  Archive Log:    Changed method getValueAt() to return event type instead of event description
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/10 21:31:08  rjtierne
 *  Archive Log:    New Setup Wizard based on framework
 *  Archive Log:
 *
 *  Overview: Table model for the Event Wizard
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.model.event;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.api.configuration.EventRule;
import com.intel.stl.ui.common.FVTableModel;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.model.EventClassViz;
import com.intel.stl.ui.model.EventTypeViz;
import com.intel.stl.ui.wizards.impl.event.EventRulesTableColumns;
import com.intel.stl.ui.wizards.view.event.ITableListener;

public class EventRulesTableModel extends FVTableModel<EventRule> implements
        ITableListener {

    private static final long serialVersionUID = 5546605189508267737L;

    private int selectedRow;

    public EventRulesTableModel() {

        String[] columnNames =
                new String[EventRulesTableColumns.values().length];
        for (int i = 0; i < EventRulesTableColumns.values().length; i++) {
            columnNames[i] = EventRulesTableColumns.values()[i].getTitle();
        }
        setColumnNames(columnNames);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if ((columnIndex == 2) || (columnIndex == 3)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Description: Override getColumnName to set the column headings
     * 
     * @param column
     *            - integer indicating the column number
     * 
     * @return result - name of the column heading
     * 
     */
    @Override
    public String getColumnName(int column) {
        return mColumnNames[column];
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int row, int col) {
        Object value = null;

        EventRule eventRule = null;
        synchronized (critical) {
            eventRule = this.mEntryList.get(row);
        }

        switch (EventRulesTableColumns.values()[col]) {
            case EVENT_CLASS:
                value =
                        EventClassViz.getEventTypeClassFor(eventRule
                                .getEventType().getEventClass());
                break;

            case EVENT_TYPE:
                value =
                        EventTypeViz.getEventTypeVizFor(eventRule
                                .getEventType());
                break;

            case EVENT_SEVERITY:
                value = eventRule.getEventSeverity().name();
                break;

            case EVENT_ACTION:
                if (eventRule.getEventActions().size() <= 0) {
                    value = STLConstants.K0799_NO_ACTIONS.getValue();
                } else {
                    value = STLConstants.K0675_ACTION.getValue();
                }

                break;

            default:
                // NOP
                break;
        }

        return value;

    }

    /**
     * Description:
     * 
     */
    public void clear() {
        mEntryList.clear();
    }

    public int getEntrySize() {
        return mEntryList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.event.IComboListener#updateTable(java.util
     * .List)
     */
    @Override
    public void updateTable(final List<EventRule> rules) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                setEntries(rules);
                fireTableDataChanged();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.event.ITableListener#getEventRules()
     */
    @Override
    public List<EventRule> getEventRules() {

        List<EventRule> eventRules = new ArrayList<EventRule>();

        for (int row = 0; row < getEntrySize(); row++) {
            eventRules.add(getEntry(row));
        }

        return eventRules;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.event.ITableListener#getSelectedRow()
     */
    @Override
    public int getSelectedRow() {

        return selectedRow;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.event.ITableListener#setSelectedRow(int)
     */
    @Override
    public void setSelectedRow(int row) {

        selectedRow = row;
    }
}
