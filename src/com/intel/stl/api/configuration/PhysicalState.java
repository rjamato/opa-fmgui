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
 *  File Name: PhysicalState.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/11 20:04:27  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/18 21:28:32  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:21:11  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import java.util.HashMap;
import java.util.Map;

import com.intel.stl.api.StringUtils;

/**
 * <pre>
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_sm.h v1.115
 * 
 * STL_PORT_PHYS_STATE values continue from IB_PORT_PHYS_STATE
 * // reserved 7-8 
 * #define STL_PORT_PHYS_OFFLINE       9       // offline
 * // reserved 10
 * #define STL_PORT_PHYS_TEST          11      // test
 * 
 * </pre>
 */
public enum PhysicalState {
    NO_ST_CHANGE((byte) 0),
    SLEEP((byte) 1),
    POLLING((byte) 2),
    DISABLED((byte) 3),
    PORT_CONFIG_TRAINING((byte) 4),
    LINKUP((byte) 5),
    LINK_ERROR_RECOVERY((byte) 6),
    OFFLINE((byte) 9),
    TEST((byte) 11);

    private static final Map<Byte, PhysicalState> _map =
            new HashMap<Byte, PhysicalState>() {
                private static final long serialVersionUID = 1L;
                {
                    for (PhysicalState type : PhysicalState.values()) {
                        put(type.id, type);
                    }
                }
            };

    private final byte id;

    private PhysicalState(byte id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public byte getId() {
        return id;
    }

    public static PhysicalState getPhysicalState(byte id) {
        PhysicalState res = _map.get(id);
        if (res != null) {
            return res;
        } else {
            throw new IllegalArgumentException("Unsupported PhysicalState "
                    + StringUtils.byteHexString(id));
        }
    }

}
