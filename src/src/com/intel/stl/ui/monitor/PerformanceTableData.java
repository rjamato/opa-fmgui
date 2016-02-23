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
 *  File Name: PerformanceTableData.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.12  2015/08/17 18:53:40  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/06/01 15:01:17  jypak
 *  Archive Log:    PR 128823 - Improve performance tables to include all portcounters fields.
 *  Archive Log:    All port counters fields added to performance table and connectivity table.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/05/28 15:29:22  jypak
 *  Archive Log:    PR 128873 - Add "Flits" in performance table for Data related columns.
 *  Archive Log:    Added "(MB)" to RcvData, XmitData column header.
 *  Archive Log:    Added "(MBps)" to data rates.
 *  Archive Log:    Added data in "Flits" or data rate in "(Flits/sec)" to tool tips.
 *  Archive Log:    Used the TableDataDescription to convert and format the data.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/05/14 17:43:07  jijunwan
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
 *  Archive Log:    Revision 1.8  2015/04/10 18:20:52  jypak
 *  Archive Log:    Fall back to previous way of displaying received/transmitted data in performance page(chart section, table section, counter (error) section).
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/04/08 19:45:03  rjtierne
 *  Archive Log:    Added Tx Data Rate
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/04/07 14:38:27  jypak
 *  Archive Log:    PR 126998 - Received/Transmitted data counters for Device Node and Device ports should show in MB rather than Flits. Fixed by converting units to Byte/KB/MB/GB. Also, tool tips were added to show the units for each value.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/03/05 22:32:13  fisherma
 *  Archive Log:    Added LinkQuality icon to Performance -> Performance tab table.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/04 21:44:17  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/18 21:03:28  jijunwan
 *  Archive Log:    Added link (jump to) capability to Connectivity tables and PortSummary table
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/30 20:35:46  rjtierne
 *  Archive Log:    Made the description of Rx/Tx Data and Packet members and
 *  Archive Log:    setters/getters more specifically described as "Delta" or "Cumulative
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/23 19:25:37  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

import java.io.Serializable;

public class PerformanceTableData implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 4147103783235360605L;

    private final int nodeLid;

    private short portNumber;

    private long portRxRemotePhysicalErrors;

    private long portRxPktsRate;

    private TableDataDescription portRxDataRate;

    private long portRxCumulativePkts;

    private TableDataDescription portRxCumulativeData;

    private long portRxSwitchRelayErrors;

    private long portTxDiscards;

    private long portTxPktsRate;

    private TableDataDescription portTxDataRate;

    private long portTxCumulativePkts;

    private TableDataDescription portTxCumulativeData;

    private long excessiveBufferOverruns;

    private long fmConfigErrors;

    private int linkQuality;

    private long portMulticastRcvPkts;

    private long portRcvErrors;

    private long portRcvConstraintErrors;

    private long portRcvFECN;

    private long portRcvBECN;

    private long portRcvBubble;

    private long portMulticastXmitPkts;

    private long portXmitConstraintErrors;

    private long portXmitWait;

    private long portXmitTimeCong;

    private long portXmitWastedBW;

    private long portXmitWaitData;

    private long localLinkIntegrityErrors;

    private long portMarkFECN;

    private long linkErrorRecovery;

    private long linkDowned;

    private short uncorrectableErrors;

    private long swPortCongestion;

    /**
     * Description:
     * 
     * @param nodeLid
     */
    public PerformanceTableData(int nodeLid) {
        super();
        this.nodeLid = nodeLid;
    }

    /**
     * @return the lid
     */
    public int getNodeLid() {
        return nodeLid;
    }

    /**
     * @return the portNumber
     */
    public short getPortNumber() {
        return portNumber;
    }

    /**
     * @param portNumber
     *            the portNumber to set
     */
    public void setPortNumber(short portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * @return the portRxRemotePhysicalErrors
     */
    public long getPortRxRemotePhysicalErrors() {
        return portRxRemotePhysicalErrors;
    }

    /**
     * @param portRxRemotePhysicalErrors
     *            the portRxRemotePhysicalErrors to set
     */
    public void setPortRxRemotePhysicalErrors(long portRxRemotePhysicalErrors) {
        this.portRxRemotePhysicalErrors = portRxRemotePhysicalErrors;
    }

    /**
     * @return the portRxPktsRate
     */
    public long getPortRxPktsRate() {
        return portRxPktsRate;
    }

    /**
     * @param portRxPktsRate
     *            the portRxPktsRate to set
     */
    public void setPortRxPktsRate(long portRxPktsRate) {
        this.portRxPktsRate = portRxPktsRate;
    }

    /**
     * @return the portRxDataRate
     */
    public TableDataDescription getPortRxDataRate() {
        return portRxDataRate;
    }

    /**
     * @param portRxDataRate
     *            the portRxDataRate to set
     */
    public void setPortRxDataRate(TableDataDescription portRxDataRate) {
        this.portRxDataRate = portRxDataRate;
    }

    /**
     * @return the portRxCumulativePkts
     */
    public long getPortRxCumulativePkts() {
        return portRxCumulativePkts;
    }

    /**
     * @param portRxCumulativePkts
     *            the portRxCumulativePkts to set
     */
    public void setPortRxCumulativePkts(long portRxCumulativePkts) {
        this.portRxCumulativePkts = portRxCumulativePkts;
    }

    /**
     * @return the portRxCumulativeData
     */
    public TableDataDescription getPortRxCumulativeData() {
        return portRxCumulativeData;
    }

    /**
     * @param portRxCumulativeData
     *            the portRxCumulativeData to set
     */
    public void setPortRxCumulativeData(
            TableDataDescription portRxCumulativeData) {
        this.portRxCumulativeData = portRxCumulativeData;
    }

    /**
     * @return the portRxSwitchRelayErrors
     */
    public long getPortRxSwitchRelayErrors() {
        return portRxSwitchRelayErrors;
    }

    /**
     * @param portRxSwitchRelayErrors
     *            the portRxSwitchRelayErrors to set
     */
    public void setPortRxSwitchRelayErrors(long portRxSwitchRelayErrors) {
        this.portRxSwitchRelayErrors = portRxSwitchRelayErrors;
    }

    /**
     * @return the portTxDiscards
     */
    public long getPortTxDiscards() {
        return portTxDiscards;
    }

    /**
     * @param portTxDiscards
     *            the portTxDiscards to set
     */
    public void setPortTxDiscards(long portTxDiscards) {
        this.portTxDiscards = portTxDiscards;
    }

    /**
     * @return the portTxPktsRate
     */
    public long getPortTxPktsRate() {
        return portTxPktsRate;
    }

    /**
     * @param portTxPktsRate
     *            the portTxPktsRate to set
     */
    public void setPortTxPktsRate(long portTxPktsRate) {
        this.portTxPktsRate = portTxPktsRate;
    }

    /**
     * @return the portTxDataRate
     */
    public TableDataDescription getPortTxDataRate() {
        return portTxDataRate;
    }

    /**
     * @param portTxDataRate
     *            the portTxDataRate to set
     */
    public void setPortTxDataRate(TableDataDescription portTxDataRate) {
        this.portTxDataRate = portTxDataRate;
    }

    /**
     * @return the portTxCumulativePkts
     */
    public long getPortTxCumulativePkts() {
        return portTxCumulativePkts;
    }

    /**
     * @param portTxCumulativePkts
     *            the portTxCumulativePkts to set
     */
    public void setPortTxCumulativePkts(long portTxCumulativePkts) {
        this.portTxCumulativePkts = portTxCumulativePkts;
    }

    /**
     * @return the portTxCumulativeData
     */
    public TableDataDescription getPortTxCumulativeData() {
        return portTxCumulativeData;
    }

    /**
     * @param portTxCumulativeData
     *            the portTxCumulativeData to set
     */
    public void setPortTxCumulativeData(
            TableDataDescription portTxCumulativeData) {
        this.portTxCumulativeData = portTxCumulativeData;
    }

    /**
     * @return the excessiveBufferOverruns
     */
    public long getExcessiveBufferOverruns() {
        return excessiveBufferOverruns;
    }

    /**
     * @param excessiveBufferOverruns
     *            the excessiveBufferOverruns to set
     */
    public void setExcessiveBufferOverruns(long excessiveBufferOverruns) {
        this.excessiveBufferOverruns = excessiveBufferOverruns;
    }

    /**
     * @return the fmConfigErrors
     */
    public long getFmConfigErrors() {
        return fmConfigErrors;
    }

    /**
     * @param fmConfigErrors
     *            the fmConfigErrors to set
     */
    public void setFmConfigErrors(long fmConfigErrors) {
        this.fmConfigErrors = fmConfigErrors;
    }

    /**
     * @return portLinkQuality
     */
    public int getLinkQualityValue() {
        return linkQuality;
    }

    /**
     * @param portLinkQuality
     *            the portLinkQuality to set
     */
    public void setLinkQuality(byte linkQuality) {
        this.linkQuality = linkQuality;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * @return the linkQuality
     */
    public int getLinkQuality() {
        return linkQuality;
    }

    /**
     * @return the portMulticastRcvPkts
     */
    public long getPortMulticastRcvPkts() {
        return portMulticastRcvPkts;
    }

    /**
     * @return the portRcvErrors
     */
    public long getPortRcvErrors() {
        return portRcvErrors;
    }

    /**
     * @return the portRcvConstraintErrors
     */
    public long getPortRcvConstraintErrors() {
        return portRcvConstraintErrors;
    }

    /**
     * @return the portRcvFECN
     */
    public long getPortRcvFECN() {
        return portRcvFECN;
    }

    /**
     * @return the portRcvBECN
     */
    public long getPortRcvBECN() {
        return portRcvBECN;
    }

    /**
     * @return the portRcvBubble
     */
    public long getPortRcvBubble() {
        return portRcvBubble;
    }

    /**
     * @return the portMulticastXmitPkts
     */
    public long getPortMulticastXmitPkts() {
        return portMulticastXmitPkts;
    }

    /**
     * @return the portXmitConstraintErrors
     */
    public long getPortXmitConstraintErrors() {
        return portXmitConstraintErrors;
    }

    /**
     * @return the portXmitWait
     */
    public long getPortXmitWait() {
        return portXmitWait;
    }

    /**
     * @return the portXmitTimeCong
     */
    public long getPortXmitTimeCong() {
        return portXmitTimeCong;
    }

    /**
     * @return the portXmitWastedBW
     */
    public long getPortXmitWastedBW() {
        return portXmitWastedBW;
    }

    /**
     * @return the portXmitWaitData
     */
    public long getPortXmitWaitData() {
        return portXmitWaitData;
    }

    /**
     * @return the localLinkIntegrityErrors
     */
    public long getLocalLinkIntegrityErrors() {
        return localLinkIntegrityErrors;
    }

    /**
     * @return the portMarkFECN
     */
    public long getPortMarkFECN() {
        return portMarkFECN;
    }

    /**
     * @return the linkErrorRecovery
     */
    public long getLinkErrorRecovery() {
        return linkErrorRecovery;
    }

    /**
     * @return the linkDowned
     */
    public long getLinkDowned() {
        return linkDowned;
    }

    /**
     * @return the uncorrectableErrors
     */
    public short getUncorrectableErrors() {
        return uncorrectableErrors;
    }

    /**
     * @return the swPortCongestion
     */
    public long getSwPortCongestion() {
        return swPortCongestion;
    }

    /**
     * @param linkQuality
     *            the linkQuality to set
     */
    public void setLinkQuality(int linkQuality) {
        this.linkQuality = linkQuality;
    }

    /**
     * @param portMulticastRcvPkts
     *            the portMulticastRcvPkts to set
     */
    public void setPortMulticastRcvPkts(long portMulticastRcvPkts) {
        this.portMulticastRcvPkts = portMulticastRcvPkts;
    }

    /**
     * @param portRcvErrors
     *            the portRcvErrors to set
     */
    public void setPortRcvErrors(long portRcvErrors) {
        this.portRcvErrors = portRcvErrors;
    }

    /**
     * @param portRcvConstraintErrors
     *            the portRcvConstraintErrors to set
     */
    public void setPortRcvConstraintErrors(long portRcvConstraintErrors) {
        this.portRcvConstraintErrors = portRcvConstraintErrors;
    }

    /**
     * @param portRcvFECN
     *            the portRcvFECN to set
     */
    public void setPortRcvFECN(long portRcvFECN) {
        this.portRcvFECN = portRcvFECN;
    }

    /**
     * @param portRcvBECN
     *            the portRcvBECN to set
     */
    public void setPortRcvBECN(long portRcvBECN) {
        this.portRcvBECN = portRcvBECN;
    }

    /**
     * @param portRcvBubble
     *            the portRcvBubble to set
     */
    public void setPortRcvBubble(long portRcvBubble) {
        this.portRcvBubble = portRcvBubble;
    }

    /**
     * @param portMulticastXmitPkts
     *            the portMulticastXmitPkts to set
     */
    public void setPortMulticastXmitPkts(long portMulticastXmitPkts) {
        this.portMulticastXmitPkts = portMulticastXmitPkts;
    }

    /**
     * @param portXmitConstraintErrors
     *            the portXmitConstraintErrors to set
     */
    public void setPortXmitConstraintErrors(long portXmitConstraintErrors) {
        this.portXmitConstraintErrors = portXmitConstraintErrors;
    }

    /**
     * @param portXmitWait
     *            the portXmitWait to set
     */
    public void setPortXmitWait(long portXmitWait) {
        this.portXmitWait = portXmitWait;
    }

    /**
     * @param portXmitTimeCong
     *            the portXmitTimeCong to set
     */
    public void setPortXmitTimeCong(long portXmitTimeCong) {
        this.portXmitTimeCong = portXmitTimeCong;
    }

    /**
     * @param portXmitWastedBW
     *            the portXmitWastedBW to set
     */
    public void setPortXmitWastedBW(long portXmitWastedBW) {
        this.portXmitWastedBW = portXmitWastedBW;
    }

    /**
     * @param portXmitWaitData
     *            the portXmitWaitData to set
     */
    public void setPortXmitWaitData(long portXmitWaitData) {
        this.portXmitWaitData = portXmitWaitData;
    }

    /**
     * @param localLinkIntegrityErrors
     *            the localLinkIntegrityErrors to set
     */
    public void setLocalLinkIntegrityErrors(long localLinkIntegrityErrors) {
        this.localLinkIntegrityErrors = localLinkIntegrityErrors;
    }

    /**
     * @param portMarkFECN
     *            the portMarkFECN to set
     */
    public void setPortMarkFECN(long portMarkFECN) {
        this.portMarkFECN = portMarkFECN;
    }

    /**
     * @param linkErrorRecovery
     *            the linkErrorRecovery to set
     */
    public void setLinkErrorRecovery(long linkErrorRecovery) {
        this.linkErrorRecovery = linkErrorRecovery;
    }

    /**
     * @param linkDowned
     *            the linkDowned to set
     */
    public void setLinkDowned(long linkDowned) {
        this.linkDowned = linkDowned;
    }

    /**
     * @param uncorrectableErrors
     *            the uncorrectableErrors to set
     */
    public void setUncorrectableErrors(short uncorrectableErrors) {
        this.uncorrectableErrors = uncorrectableErrors;
    }

    /**
     * @param swPortCongestion
     *            the swPortCongestion to set
     */
    public void setSwPortCongestion(long swPortCongestion) {
        this.swPortCongestion = swPortCongestion;
    }

}
