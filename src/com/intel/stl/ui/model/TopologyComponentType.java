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
 *  File Name: TopologyComponentType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/07/10 21:20:26  rjtierne
 *  Archive Log:    Moved PATH type and added UNKNOWN
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/08 20:19:46  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Enumeration for the types of components on the topology graph
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.HashMap;
import java.util.Map;

public enum TopologyComponentType {
    
    UNKNOWN((byte)0),
    FI((byte)1),
    SWITCH((byte)2),
    LINK((byte)3),
    PATH((byte)4);
        
    private static final Map<Byte, TopologyComponentType> componentMap = new HashMap<Byte, TopologyComponentType>() {
        private static final long serialVersionUID = 1L;

        {
            for (TopologyComponentType type : TopologyComponentType.values()) {
                put(type.id, type);
            }
        }
    };
    
    
    private final byte id;
    
    private TopologyComponentType(byte id) {
        this.id = id;        
    }


    /**
     * @return the id
     */
    public byte getId() {
        return id;
    }

    public static TopologyComponentType getComponentType(byte id) {
        return componentMap.get(id);
    }

}
