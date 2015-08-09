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
 *  File Name: PerformanceTableData.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8.2.1  2015/05/17 18:30:42  jijunwan
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

    private long portRxDataRate;

    private long portRxCumulativePkts;

    private long portRxCumulativeData;

    private long portRxSwitchRelayErrors;

    private long portTxDiscards;

    private long portTxPktsRate;

    private long portTxDataRate;

    private long portTxCumulativePkts;

    private long portTxCumulativeData;

    private long excessiveBufferOverruns;

    private long fmConfigErrors;

    private int linkQuality;

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
    public long getPortRxDataRate() {
        return portRxDataRate;
    }

    /**
     * @param portRxDataRate
     *            the portRxDataRate to set
     */
    public void setPortRxDataRate(long portRxDataRate) {
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
    public long getPortRxCumulativeData() {
        return portRxCumulativeData;
    }

    /**
     * @param portRxCumulativeData
     *            the portRxCumulativeData to set
     */
    public void setPortRxCumulativeData(long portRxCumulativeData) {
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
    public long getPortTxDataRate() {
        return portTxDataRate;
    }

    /**
     * @param portTxDataRate
     *            the portTxDataRate to set
     */
    public void setPortTxDataRate(long portTxDataRate) {
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
    public long getPortTxCumulativeData() {
        return portTxCumulativeData;
    }

    /**
     * @param portTxCumulativeData
     *            the portTxCumulativeData to set
     */
    public void setPortTxCumulativeData(long portTxCumulativeData) {
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

}
