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
 *  File Name: PortType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/03/11 16:53:45  jijunwan
 *  Archive Log:    updated to latest FM (stl_sm.h v1.135)
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/11 20:04:27  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
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
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_sm.h v1.135
 * STL PORT TYPE values imply cable info availabilty and format 
 * #define STL_PORT_TYPE_UNKNOWN       0
 * #define STL_PORT_TYPE_DISCONNECTED      1   // the port is not currently usable - CableInfo not available
 * #define STL_PORT_TYPE_FIXED             2   // A fixed backplane port in a director class switch - All STL ASICS
 * #define STL_PORT_TYPE_VARIABLE          3   // A backplane port in a blade system - possibly mixed configuration
 * #define STL_PORT_TYPE_STANDARD          4   // implies a SFF-8636 defined format for CableInfo (QSFP)
 * #define STL_PORT_TYPE_SI_PHOTONICS      5   // A silicon photonics module - 
 *                                             //implies TBD defined format for CableInfo as defined by Intel SFO group
 * // 6 - 15 Reserved
 * 
 * </pre>
 */
public enum PortType {
    UNKNOWN((byte) 0),
    DISCONNECTED((byte) 1), /*
                             * the port is not currently usable - CableInfo not
                             * available
                             */
    FIXED((byte) 2), /*
                      * A fixed backplane port in a director class switch - All
                      * STL ASICS
                      */
    VARIABLE((byte) 3), /*
                         * A backplane port in a blade system - possibly mixed
                         * configuration
                         */
    STANDARD((byte) 4), /*
                         * implies a SFF-8636 defined format for CableInfo
                         * (QSFP)
                         */
    SI_PHOTONICS((byte) 5); /*
                             * A silicon photonics module - implies TBD defined
                             * format for CableInfo as defined by Intel SFO
                             * group
                             */
    /* 6 - 15 Reserved */

    private final byte value;

    private PortType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static PortType getPortType(byte value) {
        for (PortType portType : PortType.values()) {
            if (portType.value == value) {
                return portType;
            }
        }
        throw new IllegalArgumentException("Unsupported PortType "
                + StringUtils.byteHexString(value));
    }
}
