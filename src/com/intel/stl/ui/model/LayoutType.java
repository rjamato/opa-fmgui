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
 *  File Name: LayoutType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/05/27 22:09:05  jijunwan
 *  Archive Log:    added Tree_Line layout, added collapsable image
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/23 19:47:55  jijunwan
 *  Archive Log:    init version of topology page
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import com.intel.stl.ui.common.STLConstants;

public enum LayoutType {
    FORCE_DIRECTED(
            STLConstants.K1009_FORCE_DIRECTED.getValue(), 
            STLConstants.K1010_FORCE_DIRECTED_DESCRIPTION.getValue()),
    HIERARCHICAL(
            STLConstants.K1011_HIERARCHICAL.getValue(), 
            STLConstants.K1012_HIERARCHICAL_DESCRIPTION.getValue()),
    TREE_CIRCLE(
            STLConstants.K1013_TREE_CIRCLE.getValue(), 
            STLConstants.K1014_TREE_CIRCLE_DESCRIPTION.getValue()),
    TREE_SLASH(
            STLConstants.K1015_TREE_SLASH.getValue(), 
            STLConstants.K1016_TREE_SLASH_DESCRIPTION.getValue()),
    TREE_LINE(
            STLConstants.K1017_TREE_LINE.getValue(), 
            STLConstants.K1018_TREE_LINE_DESCRIPTION.getValue());
    
    private final String name;
    private final String description;
    
    /**
     * Description: 
     *
     * @param name
     * @param description 
     */
    private LayoutType(String name, String description) {
        this.name = name;
        this.description = description;
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
    
}
