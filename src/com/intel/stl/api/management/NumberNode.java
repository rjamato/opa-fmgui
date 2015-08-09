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
 *  File Name: LongNode.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/25 18:56:45  jijunwan
 *  Archive Log:    introduced WrapperNode that allows us wrapper any object to Iattribute for xml use
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/24 17:33:18  jijunwan
 *  Archive Log:    introduced IAttribute for attributes defined in xml file
 *  Archive Log:    changed all attributes for Appliation and DG to be an IAttribute
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/16 22:02:28  jijunwan
 *  Archive Log:    Added #getType to LongNode
 *  Archive Log:    Added devicegroup to management api
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/13 20:57:01  jijunwan
 *  Archive Log:    minor  improvement on FM Application
 *  Archive Log:    Added support on FM DeviceGroup
 *  Archive Log:    put all constants used in xml file to XMLConstants
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.Utils;

public class NumberNode extends WrapperNode<Long> {
    private static final long serialVersionUID = -6499327419926282896L;

    /**
     * Description:
     * 
     */
    public NumberNode(String type) {
        this(type, 0);
    }

    /**
     * Description:
     * 
     * @param value
     */
    public NumberNode(String type, long value) {
        super(type, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.WrapperNode#valueOf(java.lang.String)
     */
    @Override
    protected Long valueOf(String str) {
        return Utils.toLong(str);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.WrapperNode#valeString(java.lang.Object)
     */
    @Override
    protected String valueString(Long value) {
        return StringUtils.longHexString(value);
    }

    /**
     * <i>Description:</i>
     * 
     * @return
     */
    @Override
    public NumberNode copy() {
        return new NumberNode(type, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return StringUtils.longHexString(value);
    }

}
