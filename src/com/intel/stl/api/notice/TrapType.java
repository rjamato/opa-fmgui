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
 *  File Name: TrapType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/12/11 18:32:35  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 15:14:03  jijunwan
 *  Archive Log:    notice and trap implementation
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.notice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * ref:/ALL_EMB/IbAcess/Common/Inc/stl_sm.h
 * 
 * #define STL_TRAP_GID_NOW_IN_SERVICE     0x40
 * #define STL_TRAP_GID_OUT_OF_SERVICE     0x41
 * #define STL_TRAP_ADD_MULTICAST_GROUP    0x42
 * #define STL_TRAP_DEL_MULTICAST_GROUP    0x43
 * #define STL_TRAP_LINK_PORT_CHANGE_STATE 0x80
 * #define STL_TRAP_LINK_INTEGRITY         0x81
 * #define STL_TRAP_BUFFER_OVERRUN         0x82
 * #define STL_TRAP_FLOW_WATCHDOG          0x83
 * #define STL_TRAP_CHANGE_CAPABILITY      0x90
 * #define STL_TRAP_CHANGE_SYSGUID         0x91
 * #define STL_TRAP_BAD_M_KEY              0x100
 * #define STL_TRAP_BAD_P_KEY              0x101
 * #define STL_TRAP_BAD_Q_KEY              0x102
 * #define STL_TRAP_SWITCH_BAD_PKEY        0x103
 * #define STL_SMA_TRAP_LINK_WIDTH         0x800
 * </pre>
 */
public enum TrapType {
    // traps created by SM
    GID_NOW_IN_SERVICE((short)0x40),  
    GID_OUT_OF_SERVICE((short)0x41),    
    ADD_MULTICAST_GROUP((short)0x42),   
    DEL_MULTICAST_GROUP((short)0x43),   
    LINK_PORT_CHANGE_STATE((short)0x80),
    LINK_INTEGRITY((short)0x81),        
    BUFFER_OVERRUN((short)0x82),        
    FLOW_WATCHDOG((short)0x83),         
    CHANGE_CAPABILITY((short)0x90),     
    CHANGE_SYSGUID((short)0x91),        
    BAD_M_KEY((short)0x100),             
    BAD_P_KEY((short)0x101),             
    BAD_Q_KEY((short)0x102),             
    SWITCH_BAD_PKEY((short)0x103),
    SMA_TRAP_LINK_WIDTH((short)0x800),
    // traps created by FE
    SM_CONNECTION_LOST((short)0x8003),
    SM_CONNECTION_ESTABLISH((short)0x8004),
    // traps created by FEC client
    FE_CONNECTION_LOST((short)0x9001),
    FE_CONNECTION_ESTABLISH((short)0x9002);
    
    private static Logger log = LoggerFactory.getLogger(TrapType.class);
    private final short id;

    private TrapType(short id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public short getId() {
        return id;
    }
    
    public static TrapType getTrapType(short id) {
        for (TrapType type : TrapType.values()) {
            if (type.getId()==id) {
                return type;
            }
        }
        log.warn("Unknown TrapType id "+id);
        return null;
    }
}
