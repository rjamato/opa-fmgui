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
package com.intel.stl.fecdriver.messages.adapter.sa;

import com.intel.stl.api.subnet.VirtualLaneBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sm.h v1.115
 * 
 * <pre>
 * 
 * 	struct {
 *      uint8    PreemptCap;
 * 
 * 		struct { IB_BITFIELD2( uint8,
 * 			Reserved:		3,
 * 			Cap:			5 )		// RO/HS-E Virtual Lanes supported on this port 
 * 		} s2;
 * 
 * 		uint16  HighLimit;			// RW/HS-E Limit of high priority component of 
 * 									//  VL Arbitration table 
 * 									// POD: 0 
 * 		uint16  PreemptingLimit;	// RW/HS-E Limit of preempt component of 
 * 									//  VL Arbitration table 
 * 									// POD: 0 
 * 		uint8   ArbitrationHighCap; // RO/HS-E 
 * 		uint8   ArbitrationLowCap;	// RO/HS-E 
 * 	} VL;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class VirtualLane extends SimpleDatagram<VirtualLaneBean> {

    /**
     * @param length
     */
    public VirtualLane() {
        super(8);
    }

    public void setPreemptCap(byte cap) {
        buffer.put(0, cap);
    }

    public void setCap(byte cap) {
        buffer.put(1, cap);
    }

    public void setHighLimit(short limit) {
        buffer.putShort(2, limit);
    }

    public void setPreemptingLimit(short limit) {
        buffer.putShort(4, limit);
    }

    public void setArbitrationHighCap(byte cap) {
        buffer.putShort(6, cap);
    }

    public void setArbitrationLowCap(byte cap) {
        buffer.putShort(7, cap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public VirtualLaneBean toObject() {
        buffer.clear();
        VirtualLaneBean bean =
                new VirtualLaneBean(buffer.get(), buffer.get(),
                        buffer.getShort(), buffer.getShort(), buffer.get(),
                        buffer.get());
        return bean;
    }

}
