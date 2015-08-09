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
 *  File Name: VLArbTableBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/04 21:37:55  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *
 *  Overview: VLArb Table element data for the Fabric View API.
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.api.subnet;

import java.io.Serializable;

import com.intel.stl.api.Utils;

/**
 * An element of a partition table.
 */
public class VLArbTableBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte VL;

    private short weight; // promote to handle unsigned byte

    public VLArbTableBean() {
        super();
    }

    public VLArbTableBean(byte vL, byte weight) {
        super();
        VL = vL;
        this.weight = Utils.unsignedByte(weight);
    }

    /**
     * Mutator.
     * 
     * @param pVL
     *            -- a new Virtual Lane for a port.
     */
    public void setVL(byte pVL) {
        if ((pVL >= 0) && (pVL <= 15)) {
            VL = pVL;
        }
    }

    /**
     * Accessor.
     * 
     * @return the Virtual Lane
     */
    public byte getVL() {
        return VL;
    }

    /**
     * @param weight
     *            the weight to set
     */
    public void setWeight(short weight) {
        this.weight = weight;
    }

    /**
     * Mutator.
     * 
     * @param pWeight
     *            -- a new weight for a Virtual Lane for a port.
     */
    public void setWeight(byte pWeight) {
        this.weight = Utils.unsignedByte(pWeight);
    }

    /**
     * Accessor.
     * 
     * @return the weight of a Virtual Lane
     */
    public short getWeight() {
        return weight;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VLArbTableBean [VL=" + VL + ", weight=" + weight + "]";
    }

}