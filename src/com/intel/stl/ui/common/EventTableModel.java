/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: EventTableModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/08/17 18:54:12  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/07/21 21:51:44  jijunwan
 *  Archive Log:    PR 129633 - Incorrect date sort on event table
 *  Archive Log:    - changed table model to use date as value, and the cell renderer uses string to display date
 *  Archive Log:    - changed date format to include am/pm
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/06/29 15:05:43  jypak
 *  Archive Log:    PR 129284 - Incorrect QSFP field name.
 *  Archive Log:    Field name fix has been implemented. Also, introduced a conversion to Date object to add flexibility to display date code.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/12/10 20:52:20  rjtierne
 *  Archive Log:    Support for new Setup Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/09/02 18:33:22  jijunwan
 *  Archive Log:    handle null event source
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/29 14:21:16  jijunwan
 *  Archive Log:    thread safe table model, added bound control on table model
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/28 22:19:33  jijunwan
 *  Archive Log:    synchronized table model
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/28 17:25:43  jijunwan
 *  Archive Log:    color severity on event table, by default sort event table by time, by default show event table on home page, show text for enums
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/14 21:43:20  jypak
 *  Archive Log:    Event Summary Table updates.
 *  Archive Log:    1. Replace EventMsgBean with EventDescription.
 *  Archive Log:    2. Update table contents with real data from Notice API.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/21 15:43:05  jijunwan
 *  Archive Log:    Added #clear to be able to clear UI before we switch to another context. In the future, should change it to eventbus
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:46:33  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/09 21:00:10  rjtierne
 *  Archive Log:    Moved table column name creation into constructor
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/04 19:15:57  rjtierne
 *  Archive Log:    Changed to extend custom abstract class FVTableModel. Changed column index names
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/03/28 15:08:43  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Supporting model for the event table
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.api.notice.IEventSource;

public class EventTableModel extends FVTableModel<EventDescription> {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -5075022331424320175L;

    /**
     * Column numbers for setValueAt() and getValueAt() methods
     */
    public static final int TIME_IDX = 0;

    public static final int SEVERITY_IDX = 1;

    public static final int SOURCE_IDX = 2;

    public static final int DESCRIPTION_IDX = 3;

    /**
     * 
     * Description: Constructor for the EventTableModel class
     * 
     * @param N
     *            /a
     * 
     * @return EventTableModel
     * 
     */
    public EventTableModel() {

        String[] columnNames =
                new String[] { STLConstants.K0401_TIME.getValue(),
                        STLConstants.K0402_SEVERITY.getValue(),
                        STLConstants.K0403_SOURCE.getValue(),
                        STLConstants.K0405_DESCRIPTION.getValue() };

        setColumnNames(columnNames);
    } // EventTableModel

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
    public Object getValueAt(int pRow, int pCol) {

        Object value = null;

        EventDescription eventMsg = null;
        synchronized (critical) {
            eventMsg = this.mEntryList.get(pRow);
        }
        if (eventMsg == null) {
            return null;
        }

        switch (pCol) {

            case TIME_IDX:
                value = eventMsg.getDate();
                break;

            case SEVERITY_IDX:
                value = eventMsg.getSeverity();
                break;

            case SOURCE_IDX:
                IEventSource source = eventMsg.getSource();
                value =
                        source == null ? STLConstants.K0383_NA.getValue()
                                : source.getDescription();
                break;

            case DESCRIPTION_IDX:
                value = eventMsg.getType();
                break;

            default:
                // NOP
                break;
        } // switch

        return value;
    } // getValueAt

    /**
     * Description:
     * 
     */
    public void clear() {
        synchronized (critical) {
            mEntryList.clear();
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

} // class EventTableModel
