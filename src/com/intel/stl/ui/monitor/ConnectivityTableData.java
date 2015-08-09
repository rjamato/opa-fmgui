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
 *  File Name: ConnectivityTableData.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
import com.intel.stl.ui.common.STLConstants;

public class ConnectivityTableData implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -3222455776096537707L;

    private final long nodeGuidValue;

    private final int nodeLidValue;

    private final short portNumValue;

    private boolean slowLinkState;

    private final boolean isNeighbor;

    private String deviceName;

    private final String nodeGUID;

    private String portNumber;

    private String linkState; // TODO Don't know where to get this

    private String physicalLinkState; // TODO Don't know where to get this

    private String activeLinkWidth;

    private String enabledLinkWidth;

    private String supportedLinkWidth;

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
            short portNumValue, boolean isNeighbor) {
        super();
        this.nodeLidValue = nodeLid;
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
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * @param deviceName
     *            the deviceName to set
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * @return the nodeLidValue
     */
    public int getNodeLidValue() {
        return nodeLidValue;
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
    
    public void setLinkQualityData(int linkQuality){
    	this.linkQuality = linkQuality;
    }
    
    public int getLinkQualityData(){
        return linkQuality;
    }

    public void clear() {
        slowLinkState = false;
        deviceName = "";
        linkState = "";
        physicalLinkState = "";
        activeLinkWidth = "";
        enabledLinkWidth = "";
        supportedLinkWidth = "";
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
        }
    }

}
