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
 *  File Name: ResourceTypeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/10/21 16:38:01  fernande
 *  Archive Log:    Customization of Properties display (Show Options/Apply Options)
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import com.intel.stl.api.configuration.ResourceType;
import com.intel.stl.ui.common.STLConstants;

public enum ResourceTypeViz {

    HFI(ResourceType.HFI, STLConstants.K0005_HOST_FABRIC_INTERFACE.getValue()),
    PORT(ResourceType.PORT, STLConstants.K1035_CONFIGURATION_PORT.getValue()),
    SWITCH(ResourceType.SWITCH, STLConstants.K0004_SWITCH.getValue());

    private final ResourceType resourceType;

    private final String value;

    private ResourceTypeViz(ResourceType resourceType, String value) {
        this.resourceType = resourceType;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public static ResourceTypeViz getResourceTypeVizFor(
            ResourceType resourceType) {
        for (ResourceTypeViz rtv : ResourceTypeViz.values()) {
            if (rtv.resourceType == resourceType) {
                return rtv;
            }
        }
        return null;
    }

}
