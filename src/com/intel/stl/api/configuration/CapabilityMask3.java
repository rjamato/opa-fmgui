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
 *  File Name: CapabilityMask3.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/01/11 20:04:27  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 16:39:46  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

/**
 * <pre>
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_sm.h v1.115
 * Capability Mask 3 - a bit set to 1 for affirmation of supported capability
 *  by a given port
 * 
 * typedef union {
 *     uint16  AsReg16;
 *     struct { IB_BITFIELD9( uint16,          // RO/H-PE
 *         CmReserved:                 8,
 *         IsSnoopSupported:           1,      // RO/--PE Packet snoop
 *                                             // Reserved in Gen1
 *         IsAsyncSC2VLSupported:      1,      // RO/H-PE Port 0 indicates whole switch
 *         IsAddrRangeConfigSupported: 1,      // RO/H-PE Can addr range for Multicast
 *                                             // and Collectives be configured
 *                                             // Port 0 indicates whole switch
 *         IsPassThroughSupported:     1,      // RO/--PE Packet pass through
 *                                             // Port 0 indicates whole switch
 *         IsSharedSpaceSupported:     1,      // RO/H-PE Shared Space
 *                                             // Port 0 indicates whole switch
 *         CmReserved2:                1,
 *         IsVLMarkerSupported:        1,      // RO/H-PE VL Marker
 *                                             // Port 0 indicates whole switch
 *         IsVLrSupported:             1 )     // RO/H-PE SC->VL_r table
 *                                             // Reserved in Gen1
 *                                             // Port 0 indicates whole switch
 *     } s; 
 * } STL_CAPABILITY_MASK3;
 * 
 * </pre>
 */
public enum CapabilityMask3 {
    SNOOP_SUPPORTED((short) 0x0080),
    ASYNCSC2VL_SUPPORTED((short) 0x0040),
    ADDRRANGECONFIG_SUPPORTED((short) 0x0020),
    PASSTHROUGH_SUPPORTED((short) 0x0010),
    SHAREDSPACE_SUPPORTED((short) 0x0008),
    VLMARKER_SUPPORTED((short) 0x0002),
    VLR_SUPPORTED((short) 0x0001);

    private final short mask;

    private CapabilityMask3(short mask) {
        this.mask = mask;
    }

    public short getCapabilityMask() {
        return mask;
    }

    public boolean hasThisMask(short val) {
        return ((this.mask & val) != 0);
    }
}
