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
 *  File Name: PerformanceTableColumns.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7.2.2  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
