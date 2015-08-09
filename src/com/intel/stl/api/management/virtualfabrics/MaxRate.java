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
 *  File Name: MaxRate.java
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

import com.intel.stl.api.configuration.StaticRate;
import com.intel.stl.api.management.WrapperNode;
import com.intel.stl.api.management.XMLConstants;

/**
 * Maximum static rate for SM to return in any PathRecord or Multicast group for
 * the VirtualFabric. Similar behaviors to MaxMTU. The value can also be stated
 * as Unlimited.
 * If not specified the default MaxMTU will be Unlimited.
 */
public class MaxRate extends WrapperNode<StaticRate> {
    private static final long serialVersionUID = 4236692336423150546L;

    public MaxRate() {
        this(StaticRate.RATE_DONTCARE);
    }

    /**
     * Description:
     * 
     * @param value
     */
    public MaxRate(StaticRate value) {
        super(XMLConstants.MAX_RATE, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.WrapperNode#valueOf(java.lang.String)
     */
    @Override
    protected StaticRate valueOf(String str) {
        if (str.equalsIgnoreCase(XMLConstants.UNLIMITED)) {
            return StaticRate.RATE_DONTCARE;
        } else {
            return StaticRate.getStaticRate(str);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.WrapperNode#valueString(java.lang.Object)
     */
    @Override
    protected String valueString(StaticRate value) {
        if (value == StaticRate.RATE_DONTCARE) {
            return XMLConstants.UNLIMITED;
        } else {
            return value.getName();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.IAttribute#copy()
     */
    @Override
    public MaxRate copy() {
        return new MaxRate(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MaxRate [type=" + type + ", value=" + value + "]";
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
        vf.setMaxRate(this);
    }

    public static MaxRate[] values() {
        StaticRate[] rates = StaticRate.values();
        MaxRate[] res = new MaxRate[rates.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = new MaxRate(rates[i]);
        }
        return res;
    }
}
