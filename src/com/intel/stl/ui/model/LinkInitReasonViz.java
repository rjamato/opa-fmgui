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
 *  File Name: (LinkDownReasonViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/02/02 16:27:17  jijunwan
 *  Archive Log:    matched FM 320 (stl_sm.h v1.26) - added linkInitReason
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/11 18:02:04  jijunwan
 *  Archive Log:    PR 126417 - Explore expansion and refinement of LinkDownReason codes
 *  Archive Log:    updated to the latest version stl_sm.h v1.115
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/18 21:31:22  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import static com.intel.stl.ui.common.STLConstants.K0383_NA;
import static com.intel.stl.ui.common.STLConstants.K1781_LINK_INIT_LINKUP;
import static com.intel.stl.ui.common.STLConstants.K1782_LINK_INIT_FLAPPING;
import static com.intel.stl.ui.common.STLConstants.K1788_LINK_INIT_OUTSIDE_POLICY;
import static com.intel.stl.ui.common.STLConstants.K1789_LINK_INIT_QUARANTINED;
import static com.intel.stl.ui.common.STLConstants.K1790_LINK_INIT_INSUFIC_CAPABILITY;

import java.util.HashMap;
import java.util.Map;

import com.intel.stl.api.configuration.LinkInitReason;

public enum LinkInitReasonViz {
    NONE(LinkInitReason.NONE, K0383_NA.getValue()),
    LINKUP(LinkInitReason.LINKUP, K1781_LINK_INIT_LINKUP.getValue()),
    FLAPPING(LinkInitReason.FLAPPING, K1782_LINK_INIT_FLAPPING.getValue()),
    OUTSIDE_POLICY(LinkInitReason.OUTSIDE_POLICY,
            K1788_LINK_INIT_OUTSIDE_POLICY.getValue()),
    QUARANTINED(LinkInitReason.QUARANTINED, K1789_LINK_INIT_QUARANTINED
            .getValue()),
    INSUFIC_CAPABILITY(LinkInitReason.INSUFIC_CAPABILITY,
            K1790_LINK_INIT_INSUFIC_CAPABILITY.getValue());

    private static final Map<Byte, String> linkInitReasonMap =
            new HashMap<Byte, String>();
    static {
        for (LinkInitReasonViz lirv : LinkInitReasonViz.values()) {
            linkInitReasonMap.put(lirv.reason.getCode(), lirv.value);
        }
    };

    private final LinkInitReason reason;

    private final String value;

    private LinkInitReasonViz(LinkInitReason reason, String value) {
        this.reason = reason;
        this.value = value;
    }

    public LinkInitReason getReason() {
        return reason;
    }

    public String getValue() {
        return value;
    }

    public static String getLinkInitReasonStr(byte reason) {
        return linkInitReasonMap.get(reason);
    }
}
