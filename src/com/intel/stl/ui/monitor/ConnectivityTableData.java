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
 *  File Name: ConnectivityTableData.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.12  2015/08/17 18:53:40  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/08/04 23:00:34  jijunwan
 *  Archive Log:    PR 129821 - connectivity table has no Link Width Down Grade data
 *  Archive Log:    - added related data to data table
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/07/17 15:40:33  rjtierne
 *  Archive Log:    PR 129547 - Need to add Node type and lid to the Connectivity
 *  Archive Log:    - Added node type to the class attributes to be displayed in the connectivity table
 *  Archive Log:    - nodeLidValue was already available and just had to be used by ConnectivityTableModel#getValueAt()
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/07/13 21:57:00  rjtierne
 *  Archive Log:    PR 129355 - Ability to click on cables to get cable info
 *  Archive Log:    - Added the cableInfo attribute to the connectivity table data
 *  Archive Log:    - Changed deviceName to nodeName to reflect text on table displayed
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/06/01 15:01:17  jypak
 *  Archive Log:    PR 128823 - Improve performance tables to include all portcounters fields.
 *  Archive Log:    All port counters fields added to performance table and connectivity table.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/26 20:07:36  fisherma
 *  Archive Log:    Changes to display Link Quality data to port's Performance tab and switch/port configuration table.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/04 21:44:17  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/18 21:03:28  jijunwan
 *  Archive Log:    Added link (jump to) capability to Connectivity tables and PortSummary table
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/26 15:15:27  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/05 17:57:05  jijunwan
 *  Archive Log:    fixed issues on ConnectivityTable to update performance data properly
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/17 19:22:03  rjtierne
 *  Archive Log:    Added slowLinkState attribute to keep track of which links are running slow
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/12 21:36:45  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Data for the Connectivity table
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.STLConstants;

public class ConnectivityTableData implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -3222455776096537707L;

    private final long nodeGuidValue;

    private final int nodeLidValue;

    private final NodeType nodeType;

    private final short portNumValue;

    private boolean slowLinkState;

    private final boolean isNeighbor;

    private String nodeName;

    private final String nodeGUID;

    private String portNumber;

    private String cableInfo;

    private String linkState; // TODO Don't know where to get this

    private String physicalLinkState; // TODO Don't know where to get this

    private String activeLinkWidth;

    private String enabledLinkWidth;

    private String supportedLinkWidth;

    private String activeLinkWidthDnGrdTx;

    private String activeLinkWidthDnGrdRx;

    private String enabledLinkWidthDnGrd;

    private String supportedLinkWidthDnGrd;

    private String activeLinkSpeed;

    private String enabledLinkSpeed;

    private String supportedLinkSpeed;

    private final AtomicReference<PerformanceData> performance;

    private int linkQuality;

    /**
     * Description:
     * 
     */
    public ConnectivityTableData(int nodeLid, long nodeGuidValue,
            NodeType nodeType, short portNumValue, boolean isNeighbor) {
        super();
        this.nodeLidValue = nodeLid;
        this.nodeType = nodeType;
        this.nodeGuidValue = nodeGuidValue;
        this.portNumValue = portNumValue;
        this.isNeighbor = isNeighbor;
        performance = new AtomicReference<PerformanceData>();

        nodeGUID = StringUtils.longHexString(nodeGuidValue);
        if (isNeighbor) {
            portNumber =
                    Integer.toString(portNumValue) + " ("
                            + STLConstants.K0525_NEIGHBOR.getValue() + ")";
        } else {
            portNumber = Integer.toString(portNumValue);
        }
    }

    /**
     * @return the isNeighbor
     */
    public boolean isNeighbor() {
        return isNeighbor;
    }

    /**
     * @return slowLinkState - true if slow link, false if not
     */
    public boolean isSlowLinkState() {
        return slowLinkState;
    }

    public void setSlowLinkState(boolean state) {
        slowLinkState = state;
    }

    /**
     * @return the deviceName
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * @param nodeName
     *            the deviceName to set
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * @return the nodeLidValue
     */
    public int getNodeLidValue() {
        return nodeLidValue;
    }

    /**
     * @return the nodeType
     */
    public NodeType getNodeType() {
        return nodeType;
    }

    /**
     * @return the nodeGUID
     */
    public String getNodeGUID() {
        return nodeGUID;
    }

    /**
     * @return the portNumber
     */
    public String getPortNumber() {
        return portNumber;
    }

    /**
     * @return the portNumValue
     */
    public short getPortNumValue() {
        return portNumValue;
    }

    /**
     * 
     * @return the cableInfo
     */
    public String getCableInfo() {
        return cableInfo;
    }

    /**
     * 
     * @param cableInfo
     *            - the cableInfo to set
     */
    public void setCableInfo(String cableInfo) {
        this.cableInfo = cableInfo;
    }

    /**
     * @return the linkState
     */
    public String getLinkState() {
        return linkState;
    }

    /**
     * @param linkState
     *            the linkState to set
     */
    public void setLinkState(String linkState) {
        this.linkState = linkState;
    }

    /**
     * @return the physicalLinkState
     */
    public String getPhysicalLinkState() {
        return physicalLinkState;
    }

    /**
     * @param physicalLinkState
     *            the physicalLinkState to set
     */
    public void setPhysicalLinkState(String physicalLinkState) {
        this.physicalLinkState = physicalLinkState;
    }

    /**
     * @return the activeLinkWidth
     */
    public String getActiveLinkWidth() {
        return activeLinkWidth;
    }

    /**
     * @param activeLinkWidth
     *            the activeLinkWidth to set
     */
    public void setActiveLinkWidth(String activeLinkWidth) {
        this.activeLinkWidth = activeLinkWidth;
    }

    /**
     * @return the enabledLinkWidth
     */
    public String getEnabledLinkWidth() {
        return enabledLinkWidth;
    }

    /**
     * @param enabledLinkWidth
     *            the enabledLinkWidth to set
     */
    public void setEnabledLinkWidth(String enabledLinkWidth) {
        this.enabledLinkWidth = enabledLinkWidth;
    }

    /**
     * @return the supportedLinkWidth
     */
    public String getSupportedLinkWidth() {
        return supportedLinkWidth;
    }

    /**
     * @param supportedLinkWidth
     *            the supportedLinkWidth to set
     */
    public void setSupportedLinkWidth(String supportedLinkWidth) {
        this.supportedLinkWidth = supportedLinkWidth;
    }

    /**
     * @return the activeLinkWidthDnGrdTx
     */
    public String getActiveLinkWidthDnGrdTx() {
        return activeLinkWidthDnGrdTx;
    }

    /**
     * @param activeLinkWidthDnGrdTx
     *            the activeLinkWidthDnGrdTx to set
     */
    public void setActiveLinkWidthDnGrdTx(String activeLinkWidthDnGrdTx) {
        this.activeLinkWidthDnGrdTx = activeLinkWidthDnGrdTx;
    }

    /**
     * @return the activeLinkWidthDnGrdRx
     */
    public String getActiveLinkWidthDnGrdRx() {
        return activeLinkWidthDnGrdRx;
    }

    /**
     * @param activeLinkWidthDnGrdRx
     *            the activeLinkWidthDnGrdRx to set
     */
    public void setActiveLinkWidthDnGrdRx(String activeLinkWidthDnGrdRx) {
        this.activeLinkWidthDnGrdRx = activeLinkWidthDnGrdRx;
    }

    /**
     * @return the enabledLinkWidthDnGrd
     */
    public String getEnabledLinkWidthDnGrd() {
        return enabledLinkWidthDnGrd;
    }

    /**
     * @param enabledLinkWidthDnGrd
     *            the enabledLinkWidthDnGrd to set
     */
    public void setEnabledLinkWidthDnGrd(String enabledLinkWidthDnGrd) {
        this.enabledLinkWidthDnGrd = enabledLinkWidthDnGrd;
    }

    /**
     * @return the supportedLinkWidthDnGrd
     */
    public String getSupportedLinkWidthDnGrd() {
        return supportedLinkWidthDnGrd;
    }

    /**
     * @param supportedLinkWidthDnGrd
     *            the supportedLinkWidthDnGrd to set
     */
    public void setSupportedLinkWidthDnGrd(String supportedLinkWidthDnGrd) {
        this.supportedLinkWidthDnGrd = supportedLinkWidthDnGrd;
    }

    /**
     * @return the activeLinkSpeed
     */
    public String getActiveLinkSpeed() {
        return activeLinkSpeed;
    }

    /**
     * @param activeLinkSpeed
     *            the activeLinkSpeed to set
     */
    public void setActiveLinkSpeed(String activeLinkSpeed) {
        this.activeLinkSpeed = activeLinkSpeed;
    }

    /**
     * @return the enabledLinkSpeed
     */
    public String getEnabledLinkSpeed() {
        return enabledLinkSpeed;
    }

    /**
     * @param enabledLinkSpeed
     *            the enabledLinkSpeed to set
     */
    public void setEnabledLinkSpeed(String enabledLinkSpeed) {
        this.enabledLinkSpeed = enabledLinkSpeed;
    }

    /**
     * @return the supportedLinkSpeed
     */
    public String getSupportedLinkSpeed() {
        return supportedLinkSpeed;
    }

    /**
     * @param supportedLinkSpeed
     *            the supportedLinkSpeed to set
     */
    public void setSupportedLinkSpeed(String supportedLinkSpeed) {
        this.supportedLinkSpeed = supportedLinkSpeed;
    }

    public PerformanceData getPerformanceData() {
        return performance.get();
    }

    public void setPerformanceData(PerformanceData perfData) {
        performance.set(perfData);
    }

    public void setLinkQualityData(int linkQuality) {
        this.linkQuality = linkQuality;
    }

    public int getLinkQualityData() {
        return linkQuality;
    }

    public void clear() {
        slowLinkState = false;
        nodeName = "";
        linkState = "";
        physicalLinkState = "";
        activeLinkWidth = "";
        enabledLinkWidth = "";
        supportedLinkWidth = "";
        activeLinkWidthDnGrdTx = "";
        activeLinkWidthDnGrdRx = "";
        enabledLinkWidthDnGrd = "";
        supportedLinkWidthDnGrd = "";
        activeLinkSpeed = "";
        enabledLinkSpeed = "";
        supportedLinkSpeed = "";
        linkQuality = 0;
        PerformanceData perfData = getPerformanceData();
        if (perfData != null) {
            perfData.clear();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime * result + (int) (nodeGuidValue ^ (nodeGuidValue >>> 32));
        result = prime * result + portNumValue;
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConnectivityTableData other = (ConnectivityTableData) obj;
        if (nodeGuidValue != other.nodeGuidValue) {
            return false;
        }
        if (portNumValue != other.portNumValue) {
            return false;
        }
        return true;
    }

    public static class PerformanceData {
        private long tx32BitWords; // TODO Don't know where to get this

        private long rx32BitWords; // TODO Don't know where to get this

        private long txPackets;

        private long rxPackets;

        private long portXmitData;

        private long portRcvData;

        private long numSymbolErrors; // TODO Don't know where to get this

        private long numLinkRecoveries; // TODO Don't know where to get this

        private long numLinkDown;

        private long rxErrors;

        private long rxRemotePhysicalErrors;

        private long txDiscards;

        private long localLinkIntegrityErrors;

        private long excessiveBufferOverruns;

        private long switchRelayErrors;

        private long txConstraints;

        private long rxConstraints;

        private long vl15Dropped; // TODO Don't know where to get this

        private long fmConfigErrors;

        private long portMulticastRcvPkts;

        private long portRcvFECN;

        private long portRcvBECN;

        private long portRcvBubble;

        private long portMulticastXmitPkts;

        private long portXmitWait;

        private long portXmitTimeCong;

        private long portXmitWastedBW;

        private long portXmitWaitData;

        private long portMarkFECN;

        private short uncorrectableErrors; // unsigned byte

        private long swPortCongestion;

        /**
         * @return the tx32BitWords
         */
        public long getTx32BitWords() {
            return tx32BitWords;
        }

        /**
         * @param tx32BitWords
         *            the tx32BitWords to set
         */
        public void setTx32BitWords(long tx32BitWords) {
            this.tx32BitWords = tx32BitWords;
        }

        /**
         * @return the rx32BitWords
         */
        public long getRx32BitWords() {
            return rx32BitWords;
        }

        /**
         * @param rx32BitWords
         *            the rx32BitWords to set
         */
        public void setRx32BitWords(long rx32BitWords) {
            this.rx32BitWords = rx32BitWords;
        }

        /**
         * @return the txPackets
         */
        public long getTxPackets() {
            return txPackets;
        }

        /**
         * @param txPackets
         *            the txPackets to set
         */
        public void setTxPackets(long txPackets) {
            this.txPackets = txPackets;
        }

        /**
         * @return the rxPackets
         */
        public long getRxPackets() {
            return rxPackets;
        }

        /**
         * @param rxPackets
         *            the rxPackets to set
         */
        public void setRxPackets(long rxPackets) {
            this.rxPackets = rxPackets;
        }

        /**
         * @return the numSymbolErrors
         */
        public long getNumSymbolErrors() {
            return numSymbolErrors;
        }

        /**
         * @param numSymbolErrors
         *            the numSymbolErrors to set
         */
        public void setNumSymbolErrors(long numSymbolErrors) {
            this.numSymbolErrors = numSymbolErrors;
        }

        /**
         * @return the numLinkRecoveries
         */
        public long getNumLinkRecoveries() {
            return numLinkRecoveries;
        }

        /**
         * @param numLinkRecoveries
         *            the numLinkRecoveries to set
         */
        public void setNumLinkRecoveries(long numLinkRecoveries) {
            this.numLinkRecoveries = numLinkRecoveries;
        }

        /**
         * @return the numLinkDown
         */
        public long getNumLinkDown() {
            return numLinkDown;
        }

        /**
         * @param numLinkDown
         *            the numLinkDown to set
         */
        public void setNumLinkDown(long numLinkDown) {
            this.numLinkDown = numLinkDown;
        }

        /**
         * @return the rxErrors
         */
        public long getRxErrors() {
            return rxErrors;
        }

        /**
         * @param rxErrors
         *            the rxErrors to set
         */
        public void setRxErrors(long rxErrors) {
            this.rxErrors = rxErrors;
        }

        /**
         * @return the rxRemotePhysicalErrors
         */
        public long getRxRemotePhysicalErrors() {
            return rxRemotePhysicalErrors;
        }

        /**
         * @param rxRemotePhysicalErrors
         *            the rxRemotePhysicalErrors to set
         */
        public void setRxRemotePhysicalErrors(long rxRemotePhysicalErrors) {
            this.rxRemotePhysicalErrors = rxRemotePhysicalErrors;
        }

        /**
         * @return the txDiscards
         */
        public long getTxDiscards() {
            return txDiscards;
        }

        /**
         * @param txDiscards
         *            the txDiscards to set
         */
        public void setTxDiscards(long txDiscards) {
            this.txDiscards = txDiscards;
        }

        /**
         * @return the localLinkIntegrityErrors
         */
        public long getLocalLinkIntegrityErrors() {
            return localLinkIntegrityErrors;
        }

        /**
         * @param localLinkIntegrityErrors
         *            the localLinkIntegrityErrors to set
         */
        public void setLocalLinkIntegrityErrors(long localLinkIntegrityErrors) {
            this.localLinkIntegrityErrors = localLinkIntegrityErrors;
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
         * @return the switchRelayErrors
         */
        public long getSwitchRelayErrors() {
            return switchRelayErrors;
        }

        /**
         * @param switchRelayErrors
         *            the switchRelayErrors to set
         */
        public void setSwitchRelayErrors(long switchRelayErrors) {
            this.switchRelayErrors = switchRelayErrors;
        }

        /**
         * @return the txConstraints
         */
        public long getTxConstraints() {
            return txConstraints;
        }

        /**
         * @param txConstraints
         *            the txConstraints to set
         */
        public void setTxConstraints(long txConstraints) {
            this.txConstraints = txConstraints;
        }

        /**
         * @return the rxConstraints
         */
        public long getRxConstraints() {
            return rxConstraints;
        }

        /**
         * @param rxConstraints
         *            the rxConstraints to set
         */
        public void setRxConstraints(long rxConstraints) {
            this.rxConstraints = rxConstraints;
        }

        /**
         * @return the vl15Dropped
         */
        public long getVl15Dropped() {
            return vl15Dropped;
        }

        /**
         * @param vl15Dropped
         *            the vl15Dropped to set
         */
        public void setVl15Dropped(long vl15Dropped) {
            this.vl15Dropped = vl15Dropped;
        }

        /**
         * @return the portXmitData
         */
        public long getPortXmitData() {
            return portXmitData;
        }

        /**
         * @return the portRcvData
         */
        public long getPortRcvData() {
            return portRcvData;
        }

        /**
         * @return the fmConfigErrors
         */
        public long getFmConfigErrors() {
            return fmConfigErrors;
        }

        /**
         * @return the portMulticastRcvPkts
         */
        public long getPortMulticastRcvPkts() {
            return portMulticastRcvPkts;
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
         * @return the portMarkFECN
         */
        public long getPortMarkFECN() {
            return portMarkFECN;
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
         * @param portXmitData
         *            the portXmitData to set
         */
        public void setPortXmitData(long portXmitData) {
            this.portXmitData = portXmitData;
        }

        /**
         * @param portRcvData
         *            the portRcvData to set
         */
        public void setPortRcvData(long portRcvData) {
            this.portRcvData = portRcvData;
        }

        /**
         * @param fmConfigErrors
         *            the fmConfigErrors to set
         */
        public void setFmConfigErrors(long fmConfigErrors) {
            this.fmConfigErrors = fmConfigErrors;
        }

        /**
         * @param portMulticastRcvPkts
         *            the portMulticastRcvPkts to set
         */
        public void setPortMulticastRcvPkts(long portMulticastRcvPkts) {
            this.portMulticastRcvPkts = portMulticastRcvPkts;
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
         * @param portMarkFECN
         *            the portMarkFECN to set
         */
        public void setPortMarkFECN(long portMarkFECN) {
            this.portMarkFECN = portMarkFECN;
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

        public void clear() {
            tx32BitWords = 0;
            rx32BitWords = 0;
            txPackets = 0;
            rxPackets = 0;
            numSymbolErrors = 0;
            numLinkRecoveries = 0;
            numLinkDown = 0;
            rxErrors = 0;
            rxRemotePhysicalErrors = 0;
            txDiscards = 0;
            localLinkIntegrityErrors = 0;
            excessiveBufferOverruns = 0;
            switchRelayErrors = 0;
            txConstraints = 0;
            rxConstraints = 0;
            vl15Dropped = 0;
            fmConfigErrors = 0;
            portMulticastRcvPkts = 0;
            portRcvFECN = 0;
            portRcvBECN = 0;
            portRcvBubble = 0;
            portMulticastXmitPkts = 0;
            portXmitWait = 0;
            portXmitTimeCong = 0;
            portXmitWastedBW = 0;
            portXmitWaitData = 0;
            portMarkFECN = 0;
            uncorrectableErrors = 0;
            swPortCongestion = 0;
        }
    }

}
