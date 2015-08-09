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
 *  File Name: PerformanceTableModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
