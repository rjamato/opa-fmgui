/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: StaticRate.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/25 18:54:03  jijunwan
 *  Archive Log:    added StaticRate that uses IB definition that is close to STL definition
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import com.intel.stl.api.StringUtils;

/**
 * <pre>
 * ref: /ALL_EMB/IbAccess/Common/Inc/ib_types.h v1.64
 * 
 * Static rate for link.
 * This enum is used in all SM, CM and ADDRESS_VECTOR APIs
 * 
 * typedef enum _IB_STATIC_RATE {
 *     IB_STATIC_RATE_DONTCARE = 0,    // allowed for SA query 
 *                                     // for ADDRESS_VECTOR means local port rate 
 *     IB_STATIC_RATE_1GB = 1,     // obsolete, now reserved 
 *     IB_STATIC_RATE_1X = 2,      // depricated, use IB_STATIC_RATE_2_5G 
 *     IB_STATIC_RATE_2_5G = 2,    // 2.5 Gb/sec (1X SDR) 
 *     IB_STATIC_RATE_MIN = 2,     // lowest standard rate 
 *     IB_STATIC_RATE_4X = 3,      // depricated, use IB_STATIC_RATE_10G 
 *     IB_STATIC_RATE_10G = 3,     // 10.0 Gb/sec (4X SDR, 1X QDR) 
 *     IB_STATIC_RATE_12X = 4,     // depricated, use IB_STATIC_RATE_30G 
 *     IB_STATIC_RATE_30G = 4,     // 30.0 Gb/sec (12X SDR) 
 *     IB_STATIC_RATE_5G = 5,      // 5.0 Gb/sec (1X DDR) 
 *     IB_STATIC_RATE_20G = 6,     // 20.0 Gb/sec (4X DDR, 8X SDR) 
 *     IB_STATIC_RATE_40G = 7,     // 40.0 Gb/sec (4X QDR, 8X DDR) 
 *     IB_STATIC_RATE_60G = 8,     // 60.0 Gb/sec (12X DDR) 
 *     IB_STATIC_RATE_80G = 9,     // 80.0 Gb/sec (8X QDR) 
 *     IB_STATIC_RATE_120G = 10,   // 120.0 Gb/sec (12X QDR) 
 *     IB_STATIC_RATE_LAST_QDR = 10,   // last QDR value, all FDR/EDR must be after this 
 *     IB_STATIC_RATE_14G = 11,    // 14.0625 Gb/sec (1X FDR) 
 *     IB_STATIC_RATE_56G = 12,    // 56.25 Gb/sec (4X FDR) 
 *     IB_STATIC_RATE_112G = 13,   // 112.5 Gb/sec (8X FDR) 
 *     IB_STATIC_RATE_168G = 14,   // 168.75 Gb/sec (12X FDR) 
 *     IB_STATIC_RATE_25G = 15,    // 25.78125 Gb/sec (1X EDR) 
 *     IB_STATIC_RATE_100G = 16,   // 103.125 Gb/sec (4X EDR) 
 *     IB_STATIC_RATE_200G = 17,   // 206.25 Gb/sec (8X EDR) 
 *     IB_STATIC_RATE_300G = 18,   // 309.375 Gb/sec (12X EDR) 
 *     IB_STATIC_RATE_MAX = 18,    // highest standard rate 
 *     IB_STATIC_RATE_LAST = 18    // last valid value 
 *     // 19-63 reserved 
 * } IB_STATIC_RATE;
 * 
 * /ALL_EMB/IbAccess/Common/Inc/stl_helper.h v1.65
 * Convert static rate to text 
 * static __inline const char*
 * StlStaticRateToText(uint32 rate)
 * {
 *     switch (rate)
 *     {
 *         case IB_STATIC_RATE_DONTCARE:
 *             return "any";
 *         case IB_STATIC_RATE_1GB:
 *             return "1g";
 *         case IB_STATIC_RATE_2_5G:
 *             return "2.5g";
 *         case IB_STATIC_RATE_10G:
 *             return "10g";
 *         case IB_STATIC_RATE_30G:
 *             return "30g";
 *         case IB_STATIC_RATE_5G:
 *             return "5g";
 *         case IB_STATIC_RATE_20G:
 *             return "20g";
 *         case IB_STATIC_RATE_40G:
 *             return "37.5g";             // STL_STATIC_RATE_37_5G;
 *         case IB_STATIC_RATE_60G:
 *             return "60g";
 *         case IB_STATIC_RATE_80G:
 *             return "75g";               // STL_STATIC_RATE_75G;
 *         case IB_STATIC_RATE_120G:
 *             return "120g";
 *         case IB_STATIC_RATE_14G:
 *             return "12.5g";             // STL_STATIC_RATE_12_5G;
 *         case IB_STATIC_RATE_25G:
 *             return "25g";               // 25.78125g
 *         case IB_STATIC_RATE_56G:
 *             return "50g";               // STL_STATIC_RATE_50G;
 *         case IB_STATIC_RATE_100G:
 *             return "100g";              // 103.125g
 *         case IB_STATIC_RATE_112G:
 *             return "112g";              // 112.5g
 *         case IB_STATIC_RATE_200G:
 *             return "200g";              // 206.25g
 *         case IB_STATIC_RATE_168G:
 *             return "168g";              // 168.75g
 *         case IB_STATIC_RATE_300G:
 *             return "300g";              // 309.375g
 *         default:
 *             return "???";
 *     }
 * 
 * </pre>
 */
public enum StaticRate {
    RATE_DONTCARE((byte) 0, "any"),
    RATE_1GB((byte) 1, "1g"),
    RATE_2_5GB((byte) 2, "2.5g"),
    RATE_10G((byte) 2, "10g"),
    RATE_30GB((byte) 2, "30g"),
    RATE_5G((byte) 2, "5g"),
    RATE_20GB((byte) 1, "20g"),
    RATE_40GB((byte) 2, "37.5g"),
    RATE_60G((byte) 2, "60g"),
    RATE_80GB((byte) 1, "75g"),
    RATE_120GB((byte) 2, "120g"),
    RATE_14G((byte) 2, "12.5g"),
    RATE_25GB((byte) 1, "25g"),
    RATE_56GB((byte) 2, "50g"),
    RATE_100G((byte) 2, "100g"),
    RATE_112GB((byte) 1, "112g"),
    RATE_200GB((byte) 2, "200g"),
    RATE_168G((byte) 2, "168g"),
    RATE_300G((byte) 2, "300g");

    private final byte id;

    private final String name;

    /**
     * Description:
     * 
     * @param id
     * @param name
     */
    private StaticRate(byte id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return the id
     */
    public byte getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public static StaticRate getStaticRate(byte inval) {
        for (StaticRate sr : StaticRate.values()) {
            if (sr.getId() == inval) {
                return sr;
            }
        }
        throw new IllegalArgumentException("Unsupported StaticRate "
                + StringUtils.byteHexString(inval));
    }

    public static StaticRate getStaticRate(String name) {
        for (StaticRate sr : StaticRate.values()) {
            if (sr.getName().equalsIgnoreCase(name)) {
                return sr;
            }
        }
        throw new IllegalArgumentException("Unsupported StaticRate '" + name
                + "'");
    }
}
