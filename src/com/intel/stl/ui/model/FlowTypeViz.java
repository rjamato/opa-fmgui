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
 *  File Name: PortTypeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/10/15 22:00:21  jijunwan
 *  Archive Log:    display other ports on UI
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/21 13:48:18  jijunwan
 *  Archive Log:    added # internal, external ports on performance page
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.awt.Color;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;

public enum FlowTypeViz {
    INTERNAL(STLConstants.K0076_INTERNAL.getValue(), UIConstants.INTEL_BLUE),
    EXTERNAL(STLConstants.K0077_EXTERNAL.getValue(), UIConstants.INTEL_ORANGE),
    OTHER(STLConstants.K0109_OTHERS.getValue(), UIConstants.INTEL_GRAY);

    private final String name;

    private final Color color;

    /**
     * Description:
     * 
     * @param name
     * @param color
     */
    private FlowTypeViz(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

}
