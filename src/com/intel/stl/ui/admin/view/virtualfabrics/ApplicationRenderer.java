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
 *  File Name: ApplicationRenderer.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/30 14:40:51  jijunwan
 *  Archive Log:    added subclasses for ListRenderer to avoid the compiler problem on Hudson server
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view.virtualfabrics;

import com.intel.stl.api.management.virtualfabrics.ApplicationName;
import com.intel.stl.ui.admin.view.ListRenderer;

public class ApplicationRenderer extends ListRenderer<ApplicationName> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.view.ListRenderer#getStringForList(com.intel.stl
     * .api.management.IAttribute)
     */
    @Override
    protected String getStringForList(ApplicationName value) {
        return value.getObject();
    }

}
