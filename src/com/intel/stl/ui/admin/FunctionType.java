/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: FunctionType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/04/08 19:42:44  rjtierne
 *  Archive Log:    Replaced constant K2103_ADM_DGS with K0408_DEVICE_GROUPS
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:38:19  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin;

import javax.swing.ImageIcon;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIImages;

public enum FunctionType {
    APPLICATIONS(STLConstants.K2101_ADM_APPS.getValue(),
            STLConstants.K2102_ADM_APPS_DESC.getValue(),
            UIImages.APPS_LARGE_ICON),
    DEVICE_GROUPS(STLConstants.K0408_DEVICE_GROUPS.getValue(),
            STLConstants.K2104_ADM_DGS_DESC.getValue(),
            UIImages.DEVICE_GROUP_LARGE_ICON),
    VIRTUAL_FABRICS(STLConstants.K2105_ADM_VFS.getValue(),
            STLConstants.K2106_ADM_VFS_DESC.getValue(),
            UIImages.VIRTUAL_FABRIC_LARGE_ICON),
    CONSOLE(STLConstants.K2107_ADM_CONSOLE.getValue(),
            STLConstants.K2108_ADM_CONSOLE_DESC.getValue(),
            UIImages.CONSOLE_ICON),
    LOG(STLConstants.K2109_ADM_LOG.getValue(), STLConstants.K2110_ADM_LOG_DESC
            .getValue(), UIImages.LOG_ICON);

    private final String name;

    private final String description;

    private final UIImages image;

    /**
     * Description:
     * 
     * @param name
     * @param description
     * @param icon
     */
    private FunctionType(String name, String description, UIImages image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the icon
     */
    public ImageIcon getIcon() {
        return image.getImageIcon();
    }

}
