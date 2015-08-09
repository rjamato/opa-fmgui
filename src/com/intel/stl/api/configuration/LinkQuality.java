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
 *  File Name: PortLinkMode.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/04/16 19:39:59  jijunwan
 *  Archive Log:    updated to handle a bug on DC firmware that provides value 7 not specified in spec
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/11 18:27:31  jijunwan
 *  Archive Log:    PR 126371 - STL1 Spec inconsistencies - LinkQuality indicator
 *  Archive Log:    added LinkQuality, updated PortCounter data structure
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/18 21:28:32  fernande
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
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_pm.h v1.34
 * LinkQualityIndicator values
 * #define STL_LINKQUALITY_EXCELLENT   5   // working as intended
 * #define STL_LINKQUALITY_VERY_GOOD   4   // slightly below preferred,
 *                                         // performance impact < 1%,
 *                                         // no action needed
 * #define STL_LINKQUALITY_GOOD        3   // low end of acceptable,
 *                                         // performance impact ~1-5%,
 *                                         // recommend corrective action on
 *                                         // next maintenance window
 * #define STL_LINKQUALITY_POOR        2   // below acceptable,
 *                                         // performance impact,
 *                                         // recommend timely corrective action
 * #define STL_LINKQUALITY_BAD         1   // far below acceptable,
 *                                         // reduced stability,
 *                                         // immediate corrective action
 * #define STL_LINKQUALITY_NONE        0   // link down
 * 
 * </pre>
 */
public enum LinkQuality {
    UNKNOWN((byte) 7),
    EXCELLENT((byte) 5),
    VERY_GOOD((byte) 4),
    GOOD((byte) 3),
    POOR((byte) 2),
    BAD((byte) 1),
    NONE((byte) 0);

    private final byte value;

    private LinkQuality(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static LinkQuality getLinkQuality(byte value) {
        for (LinkQuality linkQuality : LinkQuality.values()) {
            if (linkQuality.getValue() == value) {
                return linkQuality;
            }
        }
        throw new IllegalArgumentException("Unsupported LinkQuality "
                + StringUtils.byteHexString(value));
    }

}
