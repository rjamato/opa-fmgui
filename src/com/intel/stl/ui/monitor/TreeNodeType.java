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
 *  File Name: TreeNodeType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.14  2015/03/24 17:45:24  jijunwan
 *  Archive Log:    added SystemImage to TreeNodeType
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/10/13 22:15:37  fernande
 *  Archive Log:    Fixing unit test error due to changes in TreeNodeType
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/10/13 21:08:56  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/10/09 21:24:49  jijunwan
 *  Archive Log:    improvement on TreeNodeType:
 *  Archive Log:    1) Added icon to TreeNodeType
 *  Archive Log:    2) Rename PORT to ACTIVE_PORT
 *  Archive Log:    3) Removed NODE
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/06/24 20:22:11  rjtierne
 *  Archive Log:    Changed HCA to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/06/05 17:34:10  jijunwan
 *  Archive Log:    added vFabric into Tree View
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/05/21 14:48:11  rjtierne
 *  Archive Log:    Added NODE enum
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/05/18 22:49:08  rjtierne
 *  Archive Log:    Removed individual device group types from the types
 *  Archive Log:    of tree nodes; using DEVICE_GROUP only.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/15 18:30:12  rjtierne
 *  Archive Log:    Added more specific node types for device groups HFI, SW, and TFI
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/01 16:08:21  rjtierne
 *  Archive Log:    Added enumeration ALL
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/24 18:32:02  rjtierne
 *  Archive Log:    Changed SUBNET to DEVICE_TYPE, refactored misnamed types
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/23 19:54:38  rjtierne
 *  Archive Log:    Added INACTIVE_PORT type
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/23 18:14:18  jijunwan
 *  Archive Log:    fixed a bug on tree build
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 20:47:24  rjtierne
 *  Archive Log:    Relocated from common.view to monitor package
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/17 14:38:56  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Enumeration of tree node types so the tree renderer knows what
 *  kind of icon to assign to various tree elements
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

import javax.swing.ImageIcon;

import com.intel.stl.api.configuration.ResourceType;
import com.intel.stl.ui.common.UIImages;

public enum TreeNodeType {
    ALL(UIImages.SUBNET_ICON),
    HCA_GROUP(UIImages.HFI_GROUP_ICON),
    SWITCH_GROUP(UIImages.SW_GROUP_ICON),
    ROUTER_GROUP(null),
    DEVICE_GROUP(UIImages.DEVICE_GROUP_ICON),
    VIRTUAL_FABRIC(UIImages.VIRTUAL_FABRIC_ICON),
    NODE(null),
    HFI(UIImages.HFI_ICON),
    SWITCH(UIImages.SW_ICON),
    ROUTER(null),
    PORT(null),
    ACTIVE_PORT(UIImages.PORT_ICON),
    INACTIVE_PORT(UIImages.INACTIVE_PORT_ICON),
    SYSTEM_IMAGE(UIImages.SYS_IMG);

    private final UIImages icon;

    /**
     * Description:
     * 
     * @param icon
     */
    private TreeNodeType(UIImages icon) {
        this.icon = icon;
    }

    /**
     * @param icon
     *            the icon to set
     */
    public ImageIcon getIcon() {
        return icon.getImageIcon();
    }

    public static ResourceType getResourceTypeFor(TreeNodeType nodeType) {
        ResourceType resourceType;
        switch (nodeType) {
            case HFI:
                resourceType = ResourceType.HFI;
                break;
            case SWITCH:
                resourceType = ResourceType.SWITCH;
                break;
            case PORT:
                resourceType = ResourceType.PORT;
                break;
            case ACTIVE_PORT:
                resourceType = ResourceType.PORT;
                break;
            default:
                resourceType = null;
        }
        return resourceType;
    }
}
