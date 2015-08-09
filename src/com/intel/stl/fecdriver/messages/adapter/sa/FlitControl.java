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

import com.intel.stl.api.subnet.FlitControlBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sm.h v1.115
 * 
 * <pre>
 * 	struct {						// Flit control LinkBounce 
 * 		union {
 * 			uint16  AsReg16;
 * 			struct { IB_BITFIELD5( uint16,	// Flit interleaving 
 * 				Reserved:			2,
 * 				DistanceSupported:	2,	// RO/HS-E Supported Flit distance mode 
 * 										// (see STL_PORT_FLIT_DISTANCE_MODE_XXX) 
 * 				DistanceEnabled:	2,	// RW/HS-E Enabled Flit distance mode 
 * 										// (see STL_PORT_FLIT_DISTANCE_MODE_XXX) 
 * 										// LUD: mode1 
 * 				MaxNestLevelTxEnabled:		5,	// RW/HS-E Max nest level enabled Flit Tx 
 * 												// LUD: 0 
 * 				MaxNestLevelRxSupported:	5 )	// RO/HS-E Max nest level supported Flit Rx 
 * 			} s;
 * 		} Interleave;
 * 
 * 		struct {				// Flit preemption 
 * 			uint16  MinInitial;	// RW/HS-E Min bytes before preemption Head Flit 
 * 									// Range 8 to 10240 bytes 
 * 			uint16  MinTail;	// RW/HS-E Min bytes before preemption Tail Flit 
 * 									// Range 8 to 10240 bytes 
 * 			uint8   LargePktLimit;	// RW/HS-E Size of packet that can be preempted 
 * 									// Packet Size >= 512+(512*LargePktLimit) 
 * 									// Packet Size Range >=512 to >=8192 bytes 
 * 			uint8   SmallPktLimit;	// RW/HS-E Size of packet that can preempt 
 * 									// Packet Size <= 32+(32*SmallPktLimit) 
 * 									// Packet Size Range <=32 to <=8192 bytes 
 * 									// MaxSmallPktLimit sets upper bound allowed 
 * 			uint8   MaxSmallPktLimit;// RO/HS-E Max size of small packet limit 
 * 									// Packet Size <= 32+(32*MaxSmallPktLimit) 
 * 									// Packet Size Range <=32 to <=8192 bytes 
 * 			uint8   PreemptionLimit;// RW/HS-E Num bytes of preemption 
 * 									// limit = (256*PreemptionLimit) 
 * 									// Limit range 0 to 65024, 0xff=unlimited 
 * 		} Preemption;
 * 
 * 	} FlitControl;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class FlitControl extends SimpleDatagram<FlitControlBean> {
    public FlitControl() {
        super(10);
    }

    public void setInterleave(short level) {
        buffer.putShort(0, level);
    }

    public void setMinInitial(short minInitial) {
        buffer.putShort(2, minInitial);
    }

    public void setMinTail(short minTail) {
        buffer.putShort(4, minTail);
    }

    public void setLargePktLimit(byte size) {
        buffer.put(6, size);
    }

    public void setSmallPktLimit(byte size) {
        buffer.put(7, size);
    }

    public void setMaxSmallPktLimit(byte size) {
        buffer.put(8, size);
    }

    public void setPreemptionLimit(byte size) {
        buffer.put(9, size);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public FlitControlBean toObject() {
        buffer.clear();
        short val = buffer.getShort();
        byte distanceSupported = (byte) ((val >>> 12) & 0x3);
        byte distanceEnabled = (byte) ((val >>> 10) & 0x3);
        byte maxNestLevelTxEnabled = (byte) ((val >>> 5) & 0x1f);
        byte maxNestLevelRxSupported = (byte) (val & 0x1f);
        FlitControlBean bean =
                new FlitControlBean(distanceSupported, distanceEnabled,
                        maxNestLevelTxEnabled, maxNestLevelRxSupported,
                        buffer.getShort(), buffer.getShort(), buffer.get(),
                        buffer.get(), buffer.get(), buffer.get());
        return bean;
    }
}
