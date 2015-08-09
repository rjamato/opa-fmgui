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
 *  File Name: Bandwidth.java
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

import java.text.NumberFormat;

import com.intel.stl.api.management.WrapperNode;
import com.intel.stl.api.management.XMLConstants;

/**
 * 1-100% This is the minimum percentage of bandwidth which should be given to
 * this Virtual Fabric relative to other low priority Virtual Fabrics. When
 * there is no contention, this Virtual Fabric could get more than this amount.
 * If unspecified, the SM evenly distributes remaining among all the Virtual
 * Fabrics with unspecified Bandwidth. Total Bandwidth for all enabled Virtual
 * Fabrics with QOS enabled must not exceed 100%. If HighPriority is specified,
 * this field is ignored.
 * 
 */
public class Bandwidth extends WrapperNode<Double> {
    private static final long serialVersionUID = 3254223451844225287L;

    private static final NumberFormat format = NumberFormat
            .getPercentInstance();

    public Bandwidth() {
        this(null);
    }

    /**
     * Description:
     * 
     * @param value
     */
    public Bandwidth(Double value) {
        super(XMLConstants.BANDWIDTH, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.WrapperNode#valueOf(java.lang.String)
     */
    @Override
    protected Double valueOf(String str) {
        Double res = null;
        if (str.charAt(str.length() - 1) == '%') {
            res = Double.parseDouble(str.substring(0, str.length() - 1));
            if (res >= 1 && res <= 100) {
                return res / 100f;
            } else {
                throw new IllegalArgumentException("Invalid value range '"
                        + str + "'. Expect value in 1-100%");
            }
        }
        throw new IllegalArgumentException("Invalid format '" + str
                + "'. Expect value in 1-100%");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.WrapperNode#valueString(java.lang.Object)
     */
    @Override
    protected String valueString(Double value) {
        return format.format(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.IAttribute#copy()
     */
    @Override
    public Bandwidth copy() {
        return new Bandwidth(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Bandwidth [type=" + type + ", value=" + value + "]";
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
        vf.setBandwidth(this);
    }

}
