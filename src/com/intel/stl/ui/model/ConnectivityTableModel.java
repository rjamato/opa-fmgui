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
 *  File Name: ConnectivityTableModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
                case DEVICE_NAME:
                    value = nodeData.getDeviceName();
                    break;

                case NODE_GUID:
                    value = nodeData.getNodeGUID();
                    break;

                case PORT_NUMBER:
                    value = nodeData.getPortNumber();
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

            case LINK_RECOVERIES:
                value = perfData.getNumLinkRecoveries();
                break;

            case LINK_DOWN:
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

            case LOCAL_LINK_INTEGRITY_ERRRORS:
                value = perfData.getLocalLinkIntegrityErrors();
                break;

            case EXCESSIVE_BUFFER_OVERRUNS:
                value = perfData.getExcessiveBufferOverruns();
                break;

            case SWITCH_RELAY_ERRRORS:
                value = perfData.getSwitchRelayErrors();
                break;

            case TX_PORT_CONSTRAINT:
                value = perfData.getTxConstraints();
                break;

            case RX_PORT_CONSTRAINT:
                value = perfData.getRxConstraints();
                break;

            default:
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
