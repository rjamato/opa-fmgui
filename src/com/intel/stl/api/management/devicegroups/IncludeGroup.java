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
 *  File Name: IncludeGroup.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/27 15:43:25  jijunwan
 *  Archive Log:    improvement on #copy for Application, DeviceGroup and VirtualFabric
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/24 17:33:19  jijunwan
 *  Archive Log:    introduced IAttribute for attributes defined in xml file
 *  Archive Log:    changed all attributes for Appliation and DG to be an IAttribute
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management.devicegroups;

import com.intel.stl.api.management.StringNode;
import com.intel.stl.api.management.XMLConstants;

public class IncludeGroup extends StringNode {
    private static final long serialVersionUID = 5830464612435935672L;

    /**
     * Description:
     * 
     */
    public IncludeGroup() {
        this(null);
    }

    public IncludeGroup(String name) {
        super(XMLConstants.INCLUDE_GROUP, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.StringNode#toString()
     */
    @Override
    public String toString() {
        return getValue();
    }

    public static IncludeGroup[] toArry(String[] names) {
        IncludeGroup[] res = new IncludeGroup[names.length];
        for (int i = 0; i < names.length; i++) {
            res[i] = new IncludeGroup(names[i]);
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.StringNode#installDevieGroup(com.intel.stl
     * .api.management.devicegroups.DeviceGroup)
     */
    @Override
    public void installDevieGroup(DeviceGroup group) {
        group.addIncludeGroup(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.StringNode#copy()
     */
    @Override
    public IncludeGroup copy() {
        return new IncludeGroup(value);
    }

}
