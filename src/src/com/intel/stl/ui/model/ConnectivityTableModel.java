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
 *  File Name: ConnectivityTableModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.13  2015/11/02 23:54:31  jijunwan
 *  Archive Log:    PR 131396 - Incorrect Connectivity Table for a VF port
 *  Archive Log:    - changed model to display N/A when a value is not avaiable
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/08/17 18:53:46  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/08/04 23:00:32  jijunwan
 *  Archive Log:    PR 129821 - connectivity table has no Link Width Down Grade data
 *  Archive Log:    - added related data to data table
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/07/17 15:39:59  rjtierne
 *  Archive Log:    PR 129547 - Need to add Node type and lid to the Connectivity
 *  Archive Log:    Added two new switch cases to getValueAt() to support the retrieval of node lid
 *  Archive Log:    and type values
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/07/13 21:56:17  rjtierne
 *  Archive Log:    PR 129355 - Ability to click on cables to get cable info
 *  Archive Log:    - Added case CABLE_INFO in method getValueAt() to return the cable info
 *  Archive Log:    - Changed DEVICE_NAME to NODE_NAME to match string value of enumeration
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/06/01 15:01:22  jypak
 *  Archive Log:    PR 128823 - Improve performance tables to include all portcounters fields.
 *  Archive Log:    All port counters fields added to performance table and connectivity table.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/04/08 19:44:35  rjtierne
 *  Archive Log:    Removed SYMBOL_ERRORS, TX_32BIT_WORDS, RX_32BIT_WORDS, and VL_15_DROPPED
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/26 20:07:39  fisherma
 *  Archive Log:    Changes to display Link Quality data to port's Performance tab and switch/port configuration table.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/11 21:51:42  jijunwan
 *  Archive Log:    added method to detect whether the model is empty or not
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/10 20:52:23  rjtierne
 *  Archive Log:    Support for new Setup Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/02 21:37:54  jijunwan
 *  Archive Log:    fixed issues found by FindBugs
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/05 17:57:07  jijunwan
 *  Archive Log:    fixed issues on ConnectivityTable to update performance data properly
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/12 21:31:44  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Model for the Connectivity Table
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.model;

import com.intel.stl.ui.common.FVTableModel;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.monitor.ConnectivityTableData;
import com.intel.stl.ui.monitor.ConnectivityTableData.PerformanceData;

public class ConnectivityTableModel extends FVTableModel<ConnectivityTableData> {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 8267890546744729698L;

    /**
     * 
     * Description: Constructor for the ConnectivityTableModel class
     * 
     * @param N
     *            /a
     * 
     * @return ConnectivityTableModel
     * 
     */
    public ConnectivityTableModel() {
        String[] columnNames =
                new String[ConnectivityTableColumns.values().length];
        for (int i = 0; i < ConnectivityTableColumns.values().length; i++) {
            columnNames[i] = ConnectivityTableColumns.values()[i].getTitle();
        }
        setColumnNames(columnNames);
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
    public Object getValueAt(int pRow, int pCol) {

        Object value = STLConstants.K0383_NA.getValue();

        ConnectivityTableData nodeData = null;
        synchronized (critical) {
            nodeData = this.mEntryList.get(pRow);
        }
        if (nodeData == null) {
            return value;
        }
        PerformanceData perfData = nodeData.getPerformanceData();

        try {
            ConnectivityTableColumns col =
                    ConnectivityTableColumns.values()[pCol];
            switch (col) {
                case NODE_NAME:
                    value = nodeData.getNodeName();
                    break;

                case NODE_LID:
                    value = nodeData.getNodeLidValue();
                    break;

                case NODE_TYPE:
                    value = nodeData.getNodeType();
                    break;

                case NODE_GUID:
                    value = nodeData.getNodeGUID();
                    break;

                case PORT_NUMBER:
                    value = nodeData.getPortNumber();
                    break;

                case CABLE_INFO:
                    value = nodeData.getCableInfo();
                    break;

                case LINK_STATE:
                    value =
                            (nodeData.getLinkState() != null) ? nodeData
                                    .getLinkState().toString() : "";
                    break;

                case PHYSICAL_LINK_STATE:
                    value =
                            (nodeData.getPhysicalLinkState() != null) ? nodeData
                                    .getPhysicalLinkState().toString() : "";
                    break;

                case LINK_QUALITY:
                    value = nodeData.getLinkQualityData();
                    break;

                case ACTIVE_LINK_WIDTH:
                    value = nodeData.getActiveLinkWidth();
                    break;

                case ENABLED_LINK_WIDTH:
                    value = nodeData.getEnabledLinkWidth();
                    break;

                case SUPPORTED_LINK_WIDTH:
                    value = nodeData.getSupportedLinkWidth();
                    break;

                case ACTIVE_LINK_WIDTH_DG_TX:
                    value = nodeData.getActiveLinkWidthDnGrdTx();
                    break;

                case ACTIVE_LINK_WIDTH_DG_RX:
                    value = nodeData.getActiveLinkWidthDnGrdRx();
                    break;

                case ENABLED_LINK_WIDTH_DG:
                    value = nodeData.getEnabledLinkWidthDnGrd();
                    break;

                case SUPPORTED_LINK_WIDTH_DG:
                    value = nodeData.getSupportedLinkWidthDnGrd();
                    break;

                case ACTIVE_LINK_SPEED:
                    value = nodeData.getActiveLinkSpeed();
                    break;

                case ENABLED_LINK_SPEED:
                    value = nodeData.getEnabledLinkSpeed();
                    break;

                case SUPPORTED_LINK_SPEED:
                    value = nodeData.getSupportedLinkSpeed();
                    break;

                default:
                    // NOP
                    if (perfData != null) {
                        value = getPerformanceValueAt(perfData, col);
                    }
                    break;
            } // switch
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return value;
    } // getValueAt

    protected Object getPerformanceValueAt(PerformanceData perfData,
            ConnectivityTableColumns col) {
        Object value = null;
        switch (col) {
            case TX_PACKETS:
                value = perfData.getTxPackets();
                break;

            case RX_PACKETS:
                value = perfData.getRxPackets();
                break;

            case LINK_ERROR_RECOVERIES:
                value = perfData.getNumLinkRecoveries();
                break;

            case LINK_DOWNED:
                value = perfData.getNumLinkDown();
                break;

            case RX_ERRORS:
                value = perfData.getRxErrors();
                break;

            case RX_REMOTE_PHYSICAL_ERRRORS:
                value = perfData.getRxRemotePhysicalErrors();
                break;

            case TX_DISCARDS:
                value = perfData.getTxDiscards();
                break;

            case LOCAL_LINK_INTEGRITY:
                value = perfData.getLocalLinkIntegrityErrors();
                break;

            case EXCESSIVE_BUFFER_OVERRUNS:
                value = perfData.getExcessiveBufferOverruns();
                break;

            case RX_SWITCH_RELAY_ERRRORS:
                value = perfData.getSwitchRelayErrors();
                break;

            case TX_CONSTRAINT:
                value = perfData.getTxConstraints();
                break;

            case RX_CONSTRAINT:
                value = perfData.getRxConstraints();
                break;

            case RX_DATA:
                value = perfData.getPortRcvData();
                break;

            case TX_DATA:
                value = perfData.getPortXmitData();
                break;

            case FM_CONFIG_ERRORS:
                value = perfData.getFmConfigErrors();
                break;

            case RX_MC_PACKETS:
                value = perfData.getPortMulticastRcvPkts();
                break;

            case RX_FECN:
                value = perfData.getPortRcvFECN();
                break;

            case RX_BECN:
                value = perfData.getPortRcvBECN();
                break;

            case RX_BUBBLE:
                value = perfData.getPortRcvBubble();
                break;

            case TX_MC_PACKETS:
                value = perfData.getPortMulticastXmitPkts();
                break;

            case TX_WAIT:
                value = perfData.getPortXmitWait();
                break;

            case TX_TIME_CONG:
                value = perfData.getPortXmitTimeCong();
                break;

            case TX_WASTED_BW:
                value = perfData.getPortXmitWastedBW();
                break;

            case TX_WAIT_DATA:
                value = perfData.getPortXmitWaitData();
                break;

            case MARK_FECN:
                value = perfData.getPortMarkFECN();
                break;

            case UNCORRECTABLE_ERRORS:
                value = perfData.getUncorrectableErrors();
                break;

            case SW_PORT_CONGESTION:
                value = perfData.getSwPortCongestion();
                break;

            default:
                break;
        }
        if (value == null) {
            value = STLConstants.K0383_NA.getValue();
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

    public boolean isEmpty() {
        return mEntryList.isEmpty();
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

}
