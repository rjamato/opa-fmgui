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

package com.intel.stl.api.subnet;

import java.util.HashMap;
import java.util.Map;

public enum NodeType {
    UNKNOWN((byte) 0),
    HFI((byte) 1),
    SWITCH((byte) 2),
    ROUTER((byte) 3),
    OTHER((byte) -1);

    private static final Map<Byte, NodeType> _map =
            new HashMap<Byte, NodeType>() {
                private static final long serialVersionUID = 1L;

                {
                    for (NodeType type : NodeType.values()) {
                        put(type.id, type);
                    }
                }
            };

    private final byte id;

    private NodeType(byte id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public byte getId() {
        return id;
    }

    public static NodeType getNodeType(byte id) {
        return _map.get(id);
    }

}
