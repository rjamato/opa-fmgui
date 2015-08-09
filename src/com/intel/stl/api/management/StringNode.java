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
 *  File Name: StringNode.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/03/27 15:42:33  jijunwan
 *  Archive Log:    added installVirtualFabric to IAttribute
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/25 18:56:45  jijunwan
 *  Archive Log:    introduced WrapperNode that allows us wrapper any object to Iattribute for xml use
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/24 17:33:18  jijunwan
 *  Archive Log:    introduced IAttribute for attributes defined in xml file
 *  Archive Log:    changed all attributes for Appliation and DG to be an IAttribute
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management;

public class StringNode extends WrapperNode<String> {
    private static final long serialVersionUID = -4079051871380674344L;

    /**
     * Description:
     * 
     * @param type
     */
    public StringNode(String type) {
        this(type, null);
    }

    /**
     * Description:
     * 
     * @param type
     * @param value
     */
    public StringNode(String type, String value) {
        super(type, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.WrapperNode#valueOf(java.lang.String)
     */
    @Override
    protected String valueOf(String str) {
        return str;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.WrapperNode#valeString(java.lang.Object)
     */
    @Override
    protected String valueString(String value) {
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.IAttribute#copy()
     */
    @Override
    public StringNode copy() {
        return new StringNode(type, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "StringNode [type=" + type + ", value=" + value + "]";
    }

}
