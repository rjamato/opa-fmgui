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
 *  File Name: PerformanceTableModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.12.2.2  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.12.2.1  2015/05/17 18:30:46  jijunwan
 *  Archive Log:    PR 127700 - Delta data on host performance display is accumulating
 *  Archive Log:    - corrected delta value calculation
 *  Archive Log:    - changed to display data/pkts rate rather than delta on chart and table
 *  Archive Log:    - updated chart unit to show rate
 *  Archive Log:    - renamed the following classes to reflect we are dealing with rate
 *  Archive Log:      DataChartRangeUpdater -> DataRateChartRangeUpdater
 *  Archive Log:      PacketChartRangeUpdater -> PacketRateChartRangeUpdater
 *  Archive Log:      DataChartScaleGroupManager -> DataRateChartScaleGroupManager
 *  Archive Log:      PacketChartScaleGroupManager -> PacketRateChartScaleGroupManager
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/04/08 19:44:51  rjtierne
 *  Archive Log:    PR 126844 - Can make Port counter names in UIs more concise.
 *  Archive Log:    Changed constants accessed to use fast fabric tool names
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/03/05 22:32:16  fisherma
 *  Archive Log:    Added LinkQuality icon to Performance -> Performance tab table.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/12/10 20:52:23  rjtierne
 *  Archive Log:    Support for new Setup Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/09/18 20:57:40  jijunwan
 *  Archive Log:    removed dead code
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/06/05 17:34:59  jijunwan
 *  Archive Log:    integrate vFabric into performance pages
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/05/30 20:33:38  rjtierne
 *  Archive Log:    Fixed calls to get Tx/Rx Delta and Cumulative data and packets
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/29 14:21:17  jijunwan
 *  Archive Log:    thread safe table model, added bound control on table model
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/29 03:57:18  jijunwan
 *  Archive Log:    performance table adjustment: sort by number, hide columns to save space
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/28 22:19:34  jijunwan
 *  Archive Log:    synchronized table model
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/28 17:45:30  rjtierne
 *  Archive Log:    Catch exception in getValueAt() method when entry list is empty
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/23 19:15:57  rjtierne
 *  Archive Log:    Implemented getValueAt() method
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/21 14:44:06  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Model for the Performance table
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.model;

import com.intel.stl.ui.common.FVTableModel;
import com.intel.stl.ui.monitor.PerformanceTableData;

public class PerformanceTableModel extends FVTableModel<PerformanceTableData> {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 6716106545100111380L;

    /**
     * 
     * Description: Constructor for the PerformanceTableModel class
     * 
     * @return PerformanceTableModel
     * 
     */
    public PerformanceTableModel() {

        String[] columnNames =
                new String[PerformanceTableColumns.values().length];
        for (int i = 0; i < PerformanceTableColumns.values().length; i++) {
            columnNames[i] = PerformanceTableColumns.values()[i].getTitle();
        }
        setColumnNames(columnNames);
    } // PerformanceTableModel

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
     * TODO: This would be useful if we decide to set renderers based on object
     * type placed in each cell.
     * 
     * 
     * public Class getColumnClass(int column){ Class clazz = Object.class;
     * 
     * try { clazz = getValueAt(0, column).getClass(); } catch (Exception e){ //
     * nothing to do, proceed. }
     * 
     * return clazz; }
     */

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int pRow, int pCol) {

        Object value = null;

        long num = -1;

        PerformanceTableData portData = null;
        synchronized (critical) {
            portData = this.mEntryList.get(pRow);
        }

        try {
            if (portData != null) {
                switch (PerformanceTableColumns.values()[pCol]) {
                    case PORT_NUM:
                        value = portData.getPortNumber();
                        break;

                    case LINK_QUALITY:
                        value = portData.getLinkQualityValue();
                        break;

                    case RX_REMOTE_PHY_ERRORS:
                        num = portData.getPortRxRemotePhysicalErrors();
                        value = num == -1 ? null : num;
                        break;

                    case RX_PKTS_RATE:
                        value = portData.getPortRxPktsRate();
                        break;

                    case RX_DATA_RATE:
                        value = portData.getPortRxDataRate();
                        break;

                    case RX_PACKETS:
                        value = portData.getPortRxCumulativePkts();
                        break;

                    case RX_DATA:
                        value = portData.getPortRxCumulativeData();
                        break;

                    case RX_SWITCH_ERRORS:
                        num = portData.getPortRxSwitchRelayErrors();
                        value = num == -1 ? null : num;
                        break;

                    case TX_DISCARDS:
                        value = portData.getPortTxDiscards();
                        break;

                    case TX_PKTS_RATE:
                        value = portData.getPortTxPktsRate();
                        break;

                    case TX_DATA_RATE:
                        value = portData.getPortTxDataRate();
                        break;

                    case TX_PACKETS:
                        value = portData.getPortTxCumulativePkts();
                        break;

                    case TX_DATA:
                        value = portData.getPortTxCumulativeData();
                        break;

                    case BUFFER_OVERRUNS:
                        num = portData.getExcessiveBufferOverruns();
                        value = num == -1 ? null : num;
                        break;

                    case FM_CONFIG_ERRORS:
                        num = portData.getFmConfigErrors();
                        value = num == -1 ? null : num;
                        break;

                    default:
                        // NOP
                        break;
                } // switch
            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return value;
    } // getValueAt

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
     * @see com.intel.stl.ui.common.FVTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

} // class PerformanceTableModel
