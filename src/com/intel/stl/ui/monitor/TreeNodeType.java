/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 *  Archive Log:    Revision 1.14.2.1  2015/08/12 15:26:58  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
