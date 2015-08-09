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
 *  File Name: Qos.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/27 15:42:32  jijunwan
 *  Archive Log:    added installVirtualFabric to IAttribute
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/25 19:10:04  jijunwan
 *  Archive Log:    first version of VirtualFabric support
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management.virtualfabrics;

import com.intel.stl.api.management.BooleanNode;
import com.intel.stl.api.management.XMLConstants;

/**
 * 0=disable, 1=enable, if 0 the QOS settings are ignored
 */
public class Qos extends BooleanNode {
    private static final long serialVersionUID = 716939520521221978L;

    /**
     * Description:
     * 
     * @param type
     */
    public Qos() {
        this(false);
    }

    /**
     * Description:
     * 
     * @param type
     * @param isSelected
     */
    public Qos(boolean isSelected) {
        super(XMLConstants.QOS, isSelected);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.BooleanNode#copy()
     */
    @Override
    public Qos copy() {
        return new Qos(isSelected());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.BooleanNode#installVirtualFabric(com.intel
     * .stl.api.management.virtualfabrics.VirtualFabric)
     */
    @Override
    public void installVirtualFabric(VirtualFabric vf) {
        vf.setQos(this);
    }

}
