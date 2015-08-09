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
 *  File Name: PKey.java
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

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.management.WrapperNode;
import com.intel.stl.api.management.XMLConstants;

/**
 * By default the FM will pick an available PKey.
 * However if desired a user selected PKey can be specified. Specification may
 * be necessary such that it is known apriori for applications which do not use
 * SA PathRecord queries, such as MPIs which use non-IBTA compliant mechanisms
 * for job startup.
 * The PKey is a 16 bit value, the high bit will be ignored. The FM will use the
 * appropriate high bit based on the Security and Member/LimitedMember status
 * per device.
 */
public class PKey extends WrapperNode<Short> {
    private static final long serialVersionUID = -1120411441338040401L;

    public PKey() {
        this(null);
    }

    /**
     * Description:
     * 
     * @param type
     * @param value
     */
    public PKey(Short value) {
        super(XMLConstants.PKEY, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.WrapperNode#valueOf(java.lang.String)
     */
    @Override
    protected Short valueOf(String str) {
        Integer res = Integer.decode(str);
        return res.shortValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.WrapperNode#valueString(java.lang.Object)
     */
    @Override
    protected String valueString(Short value) {
        return StringUtils.shortHexString(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.NumberNode#copy()
     */
    @Override
    public PKey copy() {
        return new PKey(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PKey [type=" + type + ", value=" + value + "]";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.WrapperNode#installVirtualFabric(com.intel
     * .stl.api.management.virtualfabrics.VirtualFabric)
     */
    @Override
    public void installVirtualFabric(VirtualFabric vf) {
        vf.setPKey(this);
    }

}
