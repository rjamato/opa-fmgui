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
 *  File Name: PreemptRank.java
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

import com.intel.stl.api.management.WrapperNode;
import com.intel.stl.api.management.XMLConstants;

/**
 * Preemption capability of scan be configured per Virtual Fabric in terms of a
 * rank which is a value ranging from 0 to 127. Rank 0 indicates that this VF
 * cannot preempt nor be preempted Ranks of a higher value can preempt ranks of
 * a lower value (except ranks of 0). If QOS is disabled then preemption is
 * disabled and the rank is 0.
 */
public class PreemptRank extends WrapperNode<Byte> {
    private static final long serialVersionUID = 7657089661617123039L;

    public PreemptRank() {
        this(null);
    }

    /**
     * Description:
     * 
     * @param type
     * @param value
     */
    public PreemptRank(Byte value) {
        super(XMLConstants.PREEMPT_RANK, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.WrapperNode#valueOf(java.lang.String)
     */
    @Override
    protected Byte valueOf(String str) {
        Byte res = Byte.parseByte(str);
        if (res < 0 || res > 127) {
            throw new IllegalArgumentException(
                    "Invalid range. Expect [0, 127].");
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.WrapperNode#valueString(java.lang.Object)
     */
    @Override
    protected String valueString(Byte value) {
        return Byte.toString(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.IAttribute#copy()
     */
    @Override
    public PreemptRank copy() {
        return new PreemptRank(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PreemptRank [type=" + type + ", value=" + value + "]";
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
        vf.setPreemptRank(this);
    }

}
