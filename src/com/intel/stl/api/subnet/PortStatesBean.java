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
package com.intel.stl.api.subnet;

/**
 * Title:        PortStatesBean
 * Description:  A substructure in Port Info from SA populated by the connect manager.
 * 
 * @author jypak
 * @version 0.0
 */
import java.io.Serializable;

import com.intel.stl.api.configuration.PhysicalState;
import com.intel.stl.api.configuration.PortState;

public class PortStatesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean ledEnabled;

    private boolean isSMConfigurationStarted;

    private boolean neighborNormal;

    private byte offlineDisabledReason;

    private PhysicalState portPhysicalState;

    private PortState portState;

    public PortStatesBean() {
        super();
    }

    public PortStatesBean(boolean ledEnabled, boolean isSMConfigurationStarted,
            boolean neighborNormal, byte offlineDisableReason,
            byte portPhysicalState, byte portState) {
        super();
        this.ledEnabled = ledEnabled;
        this.isSMConfigurationStarted = isSMConfigurationStarted;
        this.neighborNormal = neighborNormal;
        this.offlineDisabledReason = offlineDisableReason;
        this.portPhysicalState =
                PhysicalState.getPhysicalState(portPhysicalState);
        this.portState = PortState.getPortState(portState);
    }

    /**
     * @return the ledEnabled
     */
    public boolean isLedEnabled() {
        return ledEnabled;
    }

    /**
     * @return the isSMConfigurationStarted
     */
    public boolean isSMConfigurationStarted() {
        return isSMConfigurationStarted;
    }

    /**
     * @param isSMConfigurationStarted
     *            the isSMConfigurationStarted to set
     */
    public void setSMConfigurationStarted(boolean isSMConfigurationStarted) {
        this.isSMConfigurationStarted = isSMConfigurationStarted;
    }

    /**
     * @return the neighborNormal
     */
    public boolean isNeighborNormal() {
        return neighborNormal;
    }

    /**
     * @param neighborNormal
     *            the neighborNormal to set
     */
    public void setNeighborNormal(boolean neighborNormal) {
        this.neighborNormal = neighborNormal;
    }

    /**
     * @return the offlineReason
     */
    public byte getOfflineReason() {
        return offlineDisabledReason;
    }

    /**
     * @param offlineReason
     *            the offlineReason to set
     */
    public void setOfflineReason(byte offlineReason) {
        this.offlineDisabledReason = offlineReason;
    }

    /**
     * @return the portPhysicalState
     */
    public PhysicalState getPortPhysicalState() {
        return portPhysicalState;
    }

    /**
     * @param portPhysicalState
     *            the portPhysicalState to set
     */
    public void setPortPhysicalState(byte portPhysicalState) {
        this.portPhysicalState =
                PhysicalState.getPhysicalState(portPhysicalState);
    }

    public void setPortPhysicalState(PhysicalState portPhysicalState) {
        this.portPhysicalState = portPhysicalState;
    }

    /**
     * @return the portState
     */
    public PortState getPortState() {
        return portState;
    }

    /**
     * @param portState
     *            the portState to set
     */
    public void setPortState(byte portState) {
        this.portState = PortState.getPortState(portState);
    }

    public void setPortState(PortState portState) {
        this.portState = portState;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PortStatesBean [ledEnabled=" + ledEnabled
                + ", isSMConfigurationStarted=" + isSMConfigurationStarted
                + ", neighborNormal=" + neighborNormal
                + ", offlineDisabledReason=" + offlineDisabledReason
                + ", portPhysicalState=" + portPhysicalState + ", portState="
                + portState + "]";
    }

}