/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 *  Archive Log:    Revision 1.3  2015/08/17 18:48:38  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
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
