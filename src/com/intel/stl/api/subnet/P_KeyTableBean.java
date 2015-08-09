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
package com.intel.stl.api.subnet;

/**
 * Title:        P_KeyBean
 * Description:  P_Key element data for the Fabric View API.
 *               Implementation of an element of a partition table.
 *
 * @author jypak
 * @version 0.0
*/ 
 
import java.io.*;


/**
 * An element of a partition table.
 */
public class P_KeyTableBean implements Serializable{
    
	private static final long serialVersionUID = 1L;

	private boolean full;//MembershipType 0=Limited, 1=Full
    private short base;
    
    /**
     * Null Constructor
     */
    public P_KeyTableBean() {

    }
    
    public P_KeyTableBean(boolean full, short base) {
		super();
		this.full = full;
		this.base = base;
	}

	/**
     * Mutator.
     *
     * @param pFull -- true if membership is full, false if limited.
     */
    public void setFull(boolean pFull) {
        full = pFull;
    }
    
    /**
     * Accessor.
     *
     * @return the Virtual Lane
     */
    public boolean getFull() {
        return full;
    }
    

    /**
     * Mutator.
     *
     * @param pBase -- a new key for the Partition Table of a port.
     */
    public void setBase(short pBase) {
    	//Make sure the base value of the P_Key is within the range.  Refer to stl_sm.h:
    	//
    	//      #define MAX_PKEY_BLOCK_NUM			0x7FF
    	//      #define PKEY_BLOCK_NUM_MASK			0x7FF
    	//      #define STL_DEFAULT_PKEY		    0x7FFF
    	//      #define STL_DEFAULT_APP_PKEY		0x8001
    	//      #define STL_DEFAULT_FM_PKEY         0xFFFF
    	//      #define STL_DEFAULT_CLIENT_PKEY		0x7FFF
        if (((pBase >= 0)  && (pBase <= 0x7FFF)) || (pBase == 0xFFFF)) {
            base = pBase;
        }
    }
    
    /**
     * Accessor.
     *
     * @return the weight of a Virtual Lane
     */
    public short getBase() {
        return base;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "P_KeyTableBean [full=" + full + ", base=" + base + "]";
	}
    
}