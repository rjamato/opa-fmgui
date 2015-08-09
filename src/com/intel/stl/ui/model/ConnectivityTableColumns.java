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
 *  File Name: ConnectivityTableColumns.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/04/08 19:44:01  rjtierne
 *  Archive Log:    PR 126844 - Can make Port counter names in UIs more concise.
 *  Archive Log:    Added a tool tip field to the enumeration using fast fabric tool names
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/26 20:07:39  fisherma
 *  Archive Log:    Changes to display Link Quality data to port's Performance tab and switch/port configuration table.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/12 21:31:44  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Enum for the Connectivity table columns
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.model;

import com.intel.stl.ui.common.STLConstants;

public enum ConnectivityTableColumns {
    DEVICE_NAME(0, STLConstants.K0500_NODE_NAME.getValue(),
            STLConstants.K0500_NODE_NAME.getValue()),

    NODE_GUID(0, STLConstants.K0363_NODE_GUID.getValue(),
            STLConstants.K0363_NODE_GUID.getValue()),

    PORT_NUMBER(1, STLConstants.K0404_PORT_NUMBER.getValue(),
            STLConstants.K0404_PORT_NUMBER.getValue()),

    LINK_STATE(2, STLConstants.K0505_LINK_STATE.getValue(),
            STLConstants.K0505_LINK_STATE.getValue()),

    PHYSICAL_LINK_STATE(3, STLConstants.K0506_PHYSICAL_LINK_STATE.getValue(),
            STLConstants.K0506_PHYSICAL_LINK_STATE.getValue()),

    LINK_QUALITY(26, STLConstants.K2068_LINK_QUALITY.getValue(),
            STLConstants.K3204_LINK_QUALITY_DESCRIPTION.getValue()),

    ACTIVE_LINK_WIDTH(4, STLConstants.K0319_PORT_LINK_WIDTH.getValue(),
            STLConstants.K0319_PORT_LINK_WIDTH.getValue()),

    ENABLED_LINK_WIDTH(5, STLConstants.K0507_LINK_WIDTH_ENABLED.getValue(),
            STLConstants.K0507_LINK_WIDTH_ENABLED.getValue()),

    SUPPORTED_LINK_WIDTH(6, STLConstants.K0508_LINK_WIDTH_SUPPORTED.getValue(),
            STLConstants.K0508_LINK_WIDTH_SUPPORTED.getValue()),

    ACTIVE_LINK_SPEED(7, STLConstants.K0509_ACTIVE_LINK_SPEED.getValue(),
            STLConstants.K0509_ACTIVE_LINK_SPEED.getValue()),

    ENABLED_LINK_SPEED(8, STLConstants.K0510_LINK_SPEED_ENABLED.getValue(),
            STLConstants.K0510_LINK_SPEED_ENABLED.getValue()),

    SUPPORTED_LINK_SPEED(9, STLConstants.K0511_LINK_SPEED_SUPPORTED.getValue(),
            STLConstants.K0511_LINK_SPEED_SUPPORTED.getValue()),

    TX_PACKETS(12, STLConstants.K0734_TX_CUMULATIVE_PACKETS.getValue(),
            STLConstants.K3215_TX_CUMULATIVE_PACKETS_DESCRIPTION.getValue()),

    RX_PACKETS(13, STLConstants.K0728_RX_CUMULATIVE_PACKETS.getValue(),
            STLConstants.K3209_RX_CUMULATIVE_PACKETS_DESCRIPTION.getValue()),

    // SYMBOL_ERRORS(14, STLConstants.K0516_SYMBOL_ERRORS.getValue()),

    LINK_RECOVERIES(15, STLConstants.K0517_LINK_RECOVERIES.getValue(),
            STLConstants.K3203_LINK_RECOVERIES_DESCRIPTION.getValue()),

    LINK_DOWN(16, STLConstants.K0518_LINK_DOWN.getValue(),
            STLConstants.K3202_LINK_DOWN_DESCRIPTION.getValue()),

    RX_ERRORS(17, STLConstants.K0519_RX_ERRORS.getValue(),
            STLConstants.K3208_RX_ERRORS_DESCRIPTION.getValue()),

    RX_REMOTE_PHYSICAL_ERRRORS(18, STLConstants.K0520_RX_REMOTE_PHY_ERRORS
            .getValue(), STLConstants.K3210_RX_REMOTE_PHY_ERRORS_DESCRIPTION
            .getValue()),

    TX_DISCARDS(19, STLConstants.K0731_TX_DISCARDS.getValue(),
            STLConstants.K3214_TX_DISCARDS_DESCRIPTION.getValue()),

    LOCAL_LINK_INTEGRITY_ERRRORS(20, STLConstants.K0718_LOCAL_LINK_INTEG_ERR
            .getValue(), STLConstants.K3205_LOCAL_LINK_INTEG_ERR_DESCRIPTION
            .getValue()),

    EXCESSIVE_BUFFER_OVERRUNS(21, STLConstants.K0719_EXCESS_BUFF_OVERRUNS
            .getValue(), STLConstants.K3200_EXCESS_BUFF_OVERRUNS_DESCRIPTION
            .getValue()),

    SWITCH_RELAY_ERRRORS(22, STLConstants.K0717_REC_SW_REL_ERR.getValue(),
            STLConstants.K3211_REC_SW_REL_ERR_DESCRIPTION.getValue()),

    TX_PORT_CONSTRAINT(23, STLConstants.K0521_TX_PORT_CONSTRAINT.getValue(),
            STLConstants.K3212_TX_PORT_CONSTRAINT_DESCRIPTION.getValue()),

    RX_PORT_CONSTRAINT(24, STLConstants.K0522_RX_PORT_CONSTRAINT.getValue(),
            STLConstants.K3206_RX_PORT_CONSTRAINT_DESCRIPTION.getValue());

    private final int id;

    private final String title;

    private final String toolTip;

    private ConnectivityTableColumns(int id, String title, String toolTip) {
        this.id = id;
        this.title = title;
        this.toolTip = toolTip;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getToolTip() {
        return this.toolTip;
    }

}
