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
 *  File Name: Selection.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/01/11 20:04:31  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/16 16:41:08  jijunwan
 *  Archive Log:    made Selection accessible from UI
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.subnet;

/**
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_pa.h v1.28
 * 
 * <pre>
 * #define STL_PA_SELECT_UTIL_HIGH         0x00020001          // highest first, descending
 * #define STL_PA_SELECT_UTIL_MC_HIGH      0x00020081
 * #define STL_PA_SELECT_UTIL_PKTS_HIGH    0x00020082
 * #define STL_PA_SELECT_UTIL_LOW          0x00020101          // lowest first, ascending
 * #define STL_PA_SELECT_UTIL_MC_LOW       0x00020102
 * #define STL_PA_SELECT_ERR_INTEG         0x00030001          // hightest first, descending
 * #define STL_PA_SELECT_ERR_CONG          0x00030002
 * #define STL_PA_SELECT_ERR_SMA_CONG      0x00030003
 * #define STL_PA_SELECT_ERR_BUBBLE        0x00030004
 * #define STL_PA_SELECT_ERR_SEC           0x00030005
 * #define STL_PA_SELECT_ERR_ROUT          0x00030006
 * </pre>
 */
public enum Selection {
    UTILIZATION_HIGH(0x00020001),
    PACKET_RATE_HIGH(0x00020082),
    UTILIZATION_LOW(0x00020101),
    INTEGRITY_ERRORS_HIGH(0x00030001),
    CONGESTION_ERRORS_HIGH(0x00030002),
    SMA_CONGESTION_ERRORS_HIGH(0x00030003),
    BUBBLE_ERRORS_HIGH(0x00030004),
    SECURITY_ERRORS_HIGH(0x00030005),
    ROUTING_ERRORS_HIGH(0x00030006);

    private final int select;

    private Selection(int select) {
        this.select = select;
    }

    /**
     * @return the select
     */
    public int getSelect() {
        return select;
    }

}
