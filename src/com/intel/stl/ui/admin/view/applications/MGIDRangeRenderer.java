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
 *  File Name: MGIDRangeRenderer.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/03/30 14:25:36  jijunwan
 *  Archive Log:    1) introduced IRendererModel to create renderer only we nee
 *  Archive Log:    2) removed #getName from IAttrRenderer to provide more flexibilities and let IRendererModel to take care which attribute should use which renderer, how to init it properly
 *  Archive Log:    3) improved to support repeatable and non-repeatable attributes. For non-repeatable attributes, we only can add once
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/16 22:08:13  jijunwan
 *  Archive Log:    added device group visualization on UI
 *  Archive Log:    some refactory to make the conf framework to be more general
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/13 20:58:22  jijunwan
 *  Archive Log:    put all constants used in xml file to XMLConstants
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:38:17  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view.applications;

import java.awt.Component;

import com.intel.stl.api.management.applications.MGIDRange;
import com.intel.stl.ui.admin.view.AbstractAttrRenderer;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.view.GIDField;

public class MGIDRangeRenderer extends AbstractAttrRenderer<MGIDRange> {
    private final GIDField minField;

    private final GIDField maxField;

    public MGIDRangeRenderer() {
        super();
        minField = new GIDField(STLConstants.K2114_MIN.getValue());
        maxField = new GIDField(STLConstants.K2115_MAX.getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.view.applications.AbstractAttrRenderer#getFields()
     */
    @Override
    protected Component[] getFields() {
        return new Component[] { minField, maxField };
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#setAttr(java.lang.Object)
     */
    @Override
    public void setAttr(MGIDRange attr) {
        minField.setValue(attr.getMinLower(), attr.getMinUpper());
        maxField.setValue(attr.getMaxLower(), attr.getMaxUpper());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#setEditable(boolean)
     */
    @Override
    public void setEditable(boolean isEditable) {
        minField.setEditable(isEditable);
        maxField.setEditable(isEditable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.IAttrRenderer#getAttr()
     */
    @Override
    public MGIDRange getAttr() {
        long[] minGid = minField.getValue();
        long[] maxGid = maxField.getValue();
        return new MGIDRange(minGid[0], minGid[1], maxGid[0], maxGid[1]);
    }

}
