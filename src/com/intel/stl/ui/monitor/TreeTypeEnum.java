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
 *  File Name: TreeTypeEnum.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2014/08/14 17:39:26  fernande
 *  Archive Log:    Closing the gap on device properties being displayed.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/24 18:32:50  rjtierne
 *  Archive Log:    Renamed  SUBNET_TREE to DEVICE_TYPE_TREE
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/23 13:45:37  jijunwan
 *  Archive Log:    improvement on TreeView
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 20:47:24  rjtierne
 *  Archive Log:    Relocated from common.view to monitor package
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/17 14:38:56  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Enumeration of tree types so the tree builder knows what kind
 *  of tree to build
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor;

import com.intel.stl.ui.common.STLConstants;

public enum TreeTypeEnum {

    DEVICE_TYPES_TREE(0, STLConstants.K0407_DEVICE_TYPES.getValue()),
    DEVICE_GROUPS_TREE(1, STLConstants.K0408_DEVICE_GROUPS.getValue()),
    VIRTUAL_FABRICS_TREE(2, STLConstants.K0409_VIRTUAL_FABRICS.getValue()),
    TOP_10_CONGESTED_TREE(3, STLConstants.K0410_CONGESTED_NODES.getValue());

    private final int id;

    private final String name;

    private TreeTypeEnum(int value, String name) {
        id = value;
        this.name = name;
    }

    public int getValue() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

};
