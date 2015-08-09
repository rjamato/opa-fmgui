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
 * Title:        SwitchInfoBean
 * Description:  Switch info structure for the Fabric View API
 * 
 * @author jypak
 * @version 0.0
 */
import java.io.Serializable;
import java.util.Arrays;

import com.intel.stl.api.Utils;

public class SwitchInfoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int linearFDBCap;

    private int multicastFDBCap;

    private int linearFDBTop;

    private int multicastFDBTop;

    private int collectiveCap;

    private int collectiveTop;

    private byte[] ipAddrIPV6;

    private byte[] ipAddrIPV4;

    // u1
    private byte lifeTimeValue;

    private boolean portStateChange;

    private int partitionEnforcementCap; // promote to handle unsigned short

    private short portGroupCap; // promote to handle unsigned byte

    private short portGroupTop; // promote to handle unsigned byte

    // Routing mode
    private byte routingModeSupported;

    private byte routingModeEnabled;

    // u2
    private boolean enhancedPort0;

    private byte collectiveMask;

    private byte multicastMask;

    // adaptive routing
    private boolean adaptiveRoutingEnable;

    private boolean adaptiveRoutingPause;

    private byte adaptiveRoutingAlgorithm;

    private byte adaptiveRoutingFrequency;

    private boolean adaptiveRoutingLostRoutesOnly;

    private byte adaptiveRoutingThreshold;

    // typedef union {
    // uint16 AsReg16;
    // struct { IB_BITFIELD4( uint16,
    // Reserved: 13,
    // IsAddrRangeConfigSupported: 1, /* Can addr range for Multicast */
    // /* and Collectives be configured */
    // IsAdaptiveRoutingTier1Supported: 1,
    // IsAdaptiveRoutingSupported: 1 )
    // } s;
    // } SWITCH_CAPABILITY_MASK;
    // capabilityMask;
    private boolean isAddrRangeConfigSupported;

    private boolean isAdaptiveRoutingSupported;

    private short capabilityMaskCollectives;

    /**
     * @return the inearFDBCap
     */
    public int getLinearFDBCap() {
        return linearFDBCap;
    }

    /**
     * @param inearFDBCap
     *            the inearFDBCap to set
     */
    public void setLinearFDBCap(int inearFDBCap) {
        this.linearFDBCap = inearFDBCap;
    }

    /**
     * @return the multicastFDBCap
     */
    public int getMulticastFDBCap() {
        return multicastFDBCap;
    }

    /**
     * @param multicastFDBCap
     *            the multicastFDBCap to set
     */
    public void setMulticastFDBCap(int multicastFDBCap) {
        this.multicastFDBCap = multicastFDBCap;
    }

    /**
     * @return the linearFDBTop
     */
    public int getLinearFDBTop() {
        return linearFDBTop;
    }

    /**
     * @param linearFDBTop
     *            the linearFDBTop to set
     */
    public void setLinearFDBTop(int linearFDBTop) {
        this.linearFDBTop = linearFDBTop;
    }

    /**
     * @return the multicastFDBTop
     */
    public int getMulticastFDBTop() {
        return multicastFDBTop;
    }

    /**
     * @param multicastFDBTop
     *            the multicastFDBTop to set
     */
    public void setMulticastFDBTop(int multicastFDBTop) {
        this.multicastFDBTop = multicastFDBTop;
    }

    /**
     * @return the collectiveCap
     */
    public int getCollectiveCap() {
        return collectiveCap;
    }

    /**
     * @param collectiveCap
     *            the collectiveCap to set
     */
    public void setCollectiveCap(int collectiveCap) {
        this.collectiveCap = collectiveCap;
    }

    /**
     * @return the collectiveTop
     */
    public int getCollectiveTop() {
        return collectiveTop;
    }

    /**
     * @param collectiveTop
     *            the collectiveTop to set
     */
    public void setCollectiveTop(int collectiveTop) {
        this.collectiveTop = collectiveTop;
    }

    /**
     * @return the ipAddrIPV6
     */
    public byte[] getIpAddrIPV6() {
        return ipAddrIPV6;
    }

    /**
     * @param ipAddrIPV6
     *            the ipAddrIPV6 to set
     */
    public void setIpAddrIPV6(byte[] ipAddrIPV6) {
        this.ipAddrIPV6 = ipAddrIPV6;
    }

    /**
     * @return the ipAddrIPV4
     */
    public byte[] getIpAddrIPV4() {
        return ipAddrIPV4;
    }

    /**
     * @param ipAddrIPV4
     *            the ipAddrIPV4 to set
     */
    public void setIpAddrIPV4(byte[] ipAddrIPV4) {
        this.ipAddrIPV4 = ipAddrIPV4;
    }

    /**
     * @return the lifeTimeValue
     */
    public byte getLifeTimeValue() {
        return lifeTimeValue;
    }

    /**
     * @param lifeTimeValue
     *            the lifeTimeValue to set
     */
    public void setLifeTimeValue(byte lifeTimeValue) {
        this.lifeTimeValue = lifeTimeValue;
    }

    /**
     * @return the portStateChange
     */
    public boolean isPortStateChange() {
        return portStateChange;
    }

    /**
     * @param portStateChange
     *            the portStateChange to set
     */
    public void setPortStateChange(boolean portStateChange) {
        this.portStateChange = portStateChange;
    }

    /**
     * @return the partitionEnforcementCap
     */
    public int getPartitionEnforcementCap() {
        return partitionEnforcementCap;
    }

    /**
     * @param partitionEnforcementCap
     *            the partitionEnforcementCap to set
     */
    public void setPartitionEnforcementCap(int partitionEnforcementCap) {
        this.partitionEnforcementCap = partitionEnforcementCap;
    }

    /**
     * @param partitionEnforcementCap
     *            the partitionEnforcementCap to set
     */
    public void setPartitionEnforcementCap(short partitionEnforcementCap) {
        this.partitionEnforcementCap =
                Utils.unsignedShort(partitionEnforcementCap);
    }

    /**
     * @return the portGroupCap
     */
    public short getPortGroupCap() {
        return portGroupCap;
    }

    /**
     * @param portGroupCap
     *            the portGroupCap to set
     */
    public void setPortGroupCap(short portGroupCap) {
        this.portGroupCap = portGroupCap;
    }

    /**
     * @param portGroupCap
     *            the portGroupCap to set
     */
    public void setPortGroupCap(byte portGroupCap) {
        this.portGroupCap = Utils.unsignedByte(portGroupCap);
    }

    /**
     * @return the portGroupTop
     */
    public short getPortGroupTop() {
        return portGroupTop;
    }

    /**
     * @param portGroupTop
     *            the portGroupTop to set
     */
    public void setPortGroupTop(short portGroupTop) {
        this.portGroupTop = portGroupTop;
    }

    /**
     * @param portGroupCap
     *            the portGroupCap to set
     */
    public void setPortGroupTop(byte portGroupTop) {
        this.portGroupTop = Utils.unsignedByte(portGroupTop);
    }

    /**
     * @return the routingModeSupported
     */
    public byte getRoutingModeSupported() {
        return routingModeSupported;
    }

    /**
     * @param routingModeSupported
     *            the routingModeSupported to set
     */
    public void setRoutingModeSupported(byte routingModeSupported) {
        this.routingModeSupported = routingModeSupported;
    }

    /**
     * @return the routingModeEnabled
     */
    public byte getRoutingModeEnabled() {
        return routingModeEnabled;
    }

    /**
     * @param routingModeEnabled
     *            the routingModeEnabled to set
     */
    public void setRoutingModeEnabled(byte routingModeEnabled) {
        this.routingModeEnabled = routingModeEnabled;
    }

    /**
     * @return the enhancedPort0
     */
    public boolean isEnhancedPort0() {
        return enhancedPort0;
    }

    /**
     * @param enhancedPort0
     *            the enhancedPort0 to set
     */
    public void setEnhancedPort0(boolean enhancedPort0) {
        this.enhancedPort0 = enhancedPort0;
    }

    /**
     * @return the collectiveMask
     */
    public byte getCollectiveMask() {
        return collectiveMask;
    }

    /**
     * @param collectiveMask
     *            the collectiveMask to set
     */
    public void setCollectiveMask(byte collectiveMask) {
        this.collectiveMask = collectiveMask;
    }

    /**
     * @return the multicastMask
     */
    public byte getMulticastMask() {
        return multicastMask;
    }

    /**
     * @param multicastMask
     *            the multicastMask to set
     */
    public void setMulticastMask(byte multicastMask) {
        this.multicastMask = multicastMask;
    }

    /**
     * @return the adaptiveRoutingThreshold
     */
    public byte getAdaptiveRoutingThreshold() {
        return adaptiveRoutingThreshold;
    }

    /**
     * @param adaptiveRoutingThreshold
     *            the adaptiveRoutingThreshold to set
     */
    public void setAdaptiveRoutingThreshold(byte adaptiveRoutingThreshold) {
        this.adaptiveRoutingThreshold = adaptiveRoutingThreshold;
    }

    /**
     * @return the lostRoutesOnly
     */
    public boolean isAdaptiveRoutingLostRoutesOnly() {
        return adaptiveRoutingLostRoutesOnly;
    }

    /**
     * @param lostRoutesOnly
     *            the lostRoutesOnly to set
     */
    public void setAdaptiveRoutingLostRoutesOnly(boolean lostRoutesOnly) {
        this.adaptiveRoutingLostRoutesOnly = lostRoutesOnly;
    }

    /**
     * @return the adaptiveRoutingPause
     */
    public boolean isAdaptiveRoutingPause() {
        return adaptiveRoutingPause;
    }

    /**
     * @param adaptiveRoutingPause
     *            the adaptiveRoutingPause to set
     */
    public void setAdaptiveRoutingPause(boolean adaptiveRoutingPause) {
        this.adaptiveRoutingPause = adaptiveRoutingPause;
    }

    /**
     * @return the adaptiveRoutingEnable
     */
    public boolean isAdaptiveRoutingEnable() {
        return adaptiveRoutingEnable;
    }

    /**
     * @param adaptiveRoutingEnable
     *            the adaptiveRoutingEnable to set
     */
    public void setAdaptiveRoutingEnable(boolean adaptiveRoutingEnable) {
        this.adaptiveRoutingEnable = adaptiveRoutingEnable;
    }

    /**
     * @return the adaptiveRoutingFrequency
     */
    public byte getAdaptiveRoutingFrequency() {
        return adaptiveRoutingFrequency;
    }

    /**
     * @param adaptiveRoutingFrequency
     *            the adaptiveRoutingFrequency to set
     */
    public void setAdaptiveRoutingFrequency(byte adaptiveRoutingFrequency) {
        this.adaptiveRoutingFrequency = adaptiveRoutingFrequency;
    }

    /**
     * @return the adaptiveRoutingFrequency
     */
    public byte getAdaptiveRoutingAlgorithm() {
        return adaptiveRoutingAlgorithm;
    }

    /**
     * @param adaptiveRoutingFrequency
     *            the adaptiveRoutingFrequency to set
     */
    public void setAdaptiveRoutingAlgorithm(byte adaptiveRoutingAlgorithm) {
        this.adaptiveRoutingAlgorithm = adaptiveRoutingAlgorithm;
    }

    /**
     * @return the isAddrRangeConfigSupported
     */
    public boolean isAddrRangeConfigSupported() {
        return isAddrRangeConfigSupported;
    }

    /**
     * @param isAddrRangeConfigSupported
     *            the isAddrRangeConfigSupported to set
     */
    public void setAddrRangeConfigSupported(boolean isAddrRangeConfigSupported) {
        this.isAddrRangeConfigSupported = isAddrRangeConfigSupported;
    }

    /**
     * @return the isAdaptiveRoutingSupported
     */
    public boolean isAdaptiveRoutingSupported() {
        return isAdaptiveRoutingSupported;
    }

    /**
     * @param isAdaptiveRoutingSupported
     *            the isAdaptiveRoutingSupported to set
     */
    public void setAdaptiveRoutingSupported(boolean isAdaptiveRoutingSupported) {
        this.isAdaptiveRoutingSupported = isAdaptiveRoutingSupported;
    }

    /**
     * @return the capabilityMaskCollectives
     */
    public short getCapabilityMaskCollectives() {
        return capabilityMaskCollectives;
    }

    /**
     * @param capabilityMaskCollectives
     *            the capabilityMaskCollectives to set
     */
    public void setCapabilityMaskCollectives(short capabilityMaskCollectives) {
        this.capabilityMaskCollectives = capabilityMaskCollectives;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SwitchInfoBean [linearFDBCap=" + linearFDBCap
                + ", multicastFDBCap=" + multicastFDBCap + ", linearFDBTop="
                + linearFDBTop + ", multicastFDBTop=" + multicastFDBTop
                + ", collectiveCap=" + collectiveCap + ", collectiveTop="
                + collectiveTop + ", ipAddrIPV6=" + Arrays.toString(ipAddrIPV6)
                + ", ipAddrIPV4=" + Arrays.toString(ipAddrIPV4)
                + ", portStateChange=" + portStateChange
                + ", partitionEnforcementCap=" + partitionEnforcementCap
                + ", routingModeSupported=" + routingModeSupported
                + ", routingModeEnabled=" + routingModeEnabled
                + ", enhancedPort0=" + enhancedPort0 + ", collectiveMask="
                + collectiveMask + ", multicastMask=" + multicastMask
                + ", adaptiveRoutingLostRoutesOnly="
                + adaptiveRoutingLostRoutesOnly + ", adaptiveRoutingThreshol="
                + adaptiveRoutingThreshold + ", adaptiveRoutingPause="
                + adaptiveRoutingPause + ", adaptiveRoutingEnable="
                + adaptiveRoutingEnable + ", adaptiveRoutingFrequency="
                + adaptiveRoutingFrequency + ", isAddrRangeConfigSupported="
                + isAddrRangeConfigSupported + ", isAdaptiveRoutingSupported="
                + isAdaptiveRoutingSupported + ", capabilityMaskCollectives="
                + capabilityMaskCollectives + "]";
    }

}