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
 *  File Name: PerformanceTableColumns.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7.2.1  2015/05/17 18:30:46  jijunwan
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
 *  Archive Log:    Revision 1.7  2015/04/08 19:44:01  rjtierne
 *  Archive Log:    PR 126844 - Can make Port counter names in UIs more concise.
 *  Archive Log:    Added a tool tip field to the enumeration using fast fabric tool names
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/03/05 22:32:16  fisherma
 *  Archive Log:    Added LinkQuality icon to Performance -> Performance tab table.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/30 19:41:40  rjtierne
 *  Archive Log:    Made the description of Rx/Tx Data and Packet constants more
 *  Archive Log:    specifically described as "Delta" data for the Performance table
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/29 22:06:42  jijunwan
 *  Archive Log:    support both delta and cumulative portCounters
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/28 17:44:33  rjtierne
 *  Archive Log:    Changed string constant to Port # instead of Rx Port
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/23 19:13:14  rjtierne
 *  Archive Log:    Added id field to the enum
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/21 14:44:06  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Enum for the Performance table columns
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import com.intel.stl.ui.common.STLConstants;

public enum PerformanceTableColumns {
    PORT_NUM(STLConstants.K0404_PORT_NUMBER.getValue(),
            STLConstants.K0404_PORT_NUMBER.getValue()),

    LINK_QUALITY(STLConstants.K2068_LINK_QUALITY.getValue(),
            STLConstants.K3204_LINK_QUALITY_DESCRIPTION.getValue()),

    RX_PKTS_RATE(STLConstants.K0726_RX_PKTS_RATE.getValue(),
             STLConstants.K3218_RX_PACKETS_RATE_DESCRIPTION.getValue()),

    RX_DATA_RATE(STLConstants.K0727_RX_DATA_RATE.getValue(),
             STLConstants.K3219_RX_DATA_RATE_DESCRIPTION.getValue()),

    RX_PACKETS(STLConstants.K0728_RX_CUMULATIVE_PACKETS.getValue(),
            STLConstants.K3209_RX_CUMULATIVE_PACKETS_DESCRIPTION.getValue()),

    RX_DATA(STLConstants.K0729_RX_CUMULATIVE_DATA.getValue(),
            STLConstants.K3207_RX_CUMULATIVE_DATA_DESCRIPTION.getValue()),

    RX_REMOTE_PHY_ERRORS(STLConstants.K0520_RX_REMOTE_PHY_ERRORS.getValue(),
            STLConstants.K3210_RX_REMOTE_PHY_ERRORS_DESCRIPTION.getValue()),

    RX_SWITCH_ERRORS(STLConstants.K0717_REC_SW_REL_ERR.getValue(),
            STLConstants.K3211_REC_SW_REL_ERR_DESCRIPTION.getValue()),

    TX_PKTS_RATE(STLConstants.K0733_TX_PKTS_RATE.getValue(),
            STLConstants.K3220_TX_PACKETS_RATE_DESCRIPTION.getValue()),

    TX_DATA_RATE(STLConstants.K0736_TX_DATA_RATE.getValue(),
            STLConstants.K3221_TX_DATA_RATE_DESCRIPTION.getValue()),

    TX_PACKETS(STLConstants.K0734_TX_CUMULATIVE_PACKETS.getValue(),
            STLConstants.K3215_TX_CUMULATIVE_PACKETS_DESCRIPTION.getValue()),

    TX_DATA(STLConstants.K0735_TX_CUMULATIVE_DATA.getValue(),
            STLConstants.K3213_TX_CUMULATIVE_DATA_DESCRIPTION.getValue()),

    TX_DISCARDS(STLConstants.K0731_TX_DISCARDS.getValue(),
            STLConstants.K3214_TX_DISCARDS_DESCRIPTION.getValue()),

    BUFFER_OVERRUNS(STLConstants.K0719_EXCESS_BUFF_OVERRUNS.getValue(),
            STLConstants.K3200_EXCESS_BUFF_OVERRUNS_DESCRIPTION.getValue()),

    FM_CONFIG_ERRORS(STLConstants.K0737_FM_CONFIG_ERRRORS.getValue(),
            STLConstants.K3201_FM_CONFIG_ERR_DESCRIPTION.getValue());

    private final String title;

    private final String toolTip;

    private PerformanceTableColumns(String title, String toolTip) {
        this.title = title;
        this.toolTip = toolTip;
    }

    public String getTitle() {
        return this.title;
    }

    public String getToolTip() {
        return this.toolTip;
    }
}
