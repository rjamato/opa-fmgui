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
 *  File Name: OfflineDisabledReasonViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/01/11 21:36:24  jijunwan
 *  Archive Log:    adapt to latest data structure changes on FM
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 16:51:53  fernande
 *  Archive Log:    Closing the gaps between properties and sa_query
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import com.intel.stl.api.configuration.OfflineDisabledReason;
import com.intel.stl.ui.common.STLConstants;

public enum OfflineDisabledReasonViz {

    NONE(OfflineDisabledReason.NONE,
            STLConstants.K1750_NO_SPECIFIED_REASON.getValue()),
    DISCONNECTED(OfflineDisabledReason.DISCONNECTED,
            STLConstants.K1751_OFFDIS_DISCONNECTED.getValue()),
    LOCAL_MEDIA_NOT_INSTALLED(OfflineDisabledReason.LOCAL_MEDIA_NOT_INSTALLED,
            STLConstants.K1752_OFFDIS_LOCAL_MEDIA_NOT_INSTALLED.getValue()),
    NOT_INSTALLED(OfflineDisabledReason.NOT_INSTALLED,
            STLConstants.K1753_OFFDIS_NOT_INSTALLED.getValue()),
    CHASSIS_CONFIG(OfflineDisabledReason.CHASSIS_CONFIG,
            STLConstants.K1754_OFFDIS_CHASSIS_CONFIG.getValue()),
    END_TO_END_NOT_INSTALLED(OfflineDisabledReason.END_TO_END_NOT_INSTALLED,
            STLConstants.K1755_OFFDIS_END_TO_END_NOT_INSTALLED.getValue()),
    POWER_POLICY(OfflineDisabledReason.POWER_POLICY,
            STLConstants.K1756_OFFDIS_POWER_POLICY.getValue()),
    LINKSPEED_POLICY(OfflineDisabledReason.LINKSPEED_POLICY,
            STLConstants.K1757_OFFDIS_LINKSPEED_POLICY.getValue()),
    LINKWIDTH_POLICY(OfflineDisabledReason.LINKWIDTH_POLICY,
            STLConstants.K1758_OFFDIS_LINKWIDTH_POLICY.getValue()),
    SWITCH_MGMT(OfflineDisabledReason.SWITCH_MGMT,
            STLConstants.K1759_OFFDIS_SWITCH_MGMT.getValue()),
    SMA_DISABLED(OfflineDisabledReason.SMA_DISABLED,
            STLConstants.K1760_OFFDIS_SMA_DISABLED.getValue()),
    TRANSIENT(OfflineDisabledReason.TRANSIENT,
            STLConstants.K1761_OFFDIS_TRANSIENT.getValue());

    private final OfflineDisabledReason reason;

    private final String value;

    private OfflineDisabledReasonViz(OfflineDisabledReason reason, String value) {
        this.reason = reason;
        this.value = value;
    }

    public OfflineDisabledReason getReason() {
        return reason;
    }

    public String getValue() {
        return value;
    }

    public static String getOfflineDisabledReasonStr(byte code) {
        for (OfflineDisabledReasonViz odrv : OfflineDisabledReasonViz.values()) {
            if (odrv.reason.getCode() == code) {
                return odrv.getValue();
            }
        }
        return null;
    }
}
