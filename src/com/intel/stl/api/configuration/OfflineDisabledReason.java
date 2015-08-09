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
 *  File Name: OfflineDisabledReason.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
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

import com.intel.stl.api.StringUtils;

/**
 * <pre>
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_sm.h v1.115
 * Offline Disabled Reason, indicated as follows: 
 * 
 * #define STL_OFFDIS_REASON_NONE                  0   // Nop/No specified reason
 * #define STL_OFFDIS_REASON_DISCONNECTED          1   // not connected in design
 * #define STL_OFFDIS_REASON_LOCAL_MEDIA_NOT_INSTALLED 2   // Module not installed
 *                                                         // in connector (QSFP,
 *                                                         // SiPh_x16, etc)
 * #define STL_OFFDIS_REASON_NOT_INSTALLED         3   // internal link not
 *                                                     // installed, neighbor FRU
 *                                                     // absent
 * #define STL_OFFDIS_REASON_CHASSIS_CONFIG        4   // Chassis mgmt forced
 *                                                     // offline due to incompat
 *                                                     // or absent neighbor FRU
 * // reserved 5
 * #define STL_OFFDIS_REASON_END_TO_END_NOT_INSTALLED  6  // local module present
 *                                                        // but unable to detect
 *                                                        // end to optical link
 * // reserved 7
 * #define STL_OFFDIS_REASON_POWER_POLICY          8   // enabling port would
 *                                                     // exceed power policy 
 * #define STL_OFFDIS_REASON_LINKSPEED_POLICY      9   // enabled speed unable to
 *                                                     // be met due to persistent
 *                                                     // cause 
 * #define STL_OFFDIS_REASON_LINKWIDTH_POLICY      10  // enabled width unable to
 *                                                     // be met due to persistent
 *                                                     // cause 
 * // reserved 11
 * #define STL_OFFDIS_REASON_SWITCH_MGMT           12  // user disabled via switch
 *                                                     // mangement interface
 *                                                     
 * #define STL_OFFDIS_REASON_SMA_DISABLED          13  // user disabled via SMA
 *                                                     // Set to phys port state
 *                                                     // disabled
 * // reserved 14
 * #define STL_OFFDIS_REASON_TRANSIENT             15  // Transient offline as part
 *                                                     // of sync with neighbor
 *                                                     // phys port state machine
 * </pre>
 */
public enum OfflineDisabledReason {
    NONE((byte) 0),
    DISCONNECTED((byte) 1),
    LOCAL_MEDIA_NOT_INSTALLED((byte) 2),
    NOT_INSTALLED((byte) 3),
    CHASSIS_CONFIG((byte) 4),
    END_TO_END_NOT_INSTALLED((byte) 6),
    POWER_POLICY((byte) 8),
    LINKSPEED_POLICY((byte) 9),
    LINKWIDTH_POLICY((byte) 10),
    SWITCH_MGMT((byte) 12),
    SMA_DISABLED((byte) 13),
    TRANSIENT((byte) 15);

    private final byte code;

    private OfflineDisabledReason(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static OfflineDisabledReason getOfflineDisabledReason(byte code) {
        for (OfflineDisabledReason reason : OfflineDisabledReason.values()) {
            if (reason.code == code) {
                return reason;
            }
        }
        throw new IllegalArgumentException("Unsupported OfflineDisabledReason "
                + StringUtils.byteHexString(code));
    }
}
