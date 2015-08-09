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

import com.intel.stl.api.subnet.SMInfoBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sm.h v1.115
 * 
 * <pre>
 * SMInfo
 * 
 * Attribute Modifier as: 0 (not used)
 * 
 * typedef struct {
 * 	uint64  PortGUID;   			// RO This SM's perception of the GUID 
 * 								// of the master SM 
 * 	uint64  SM_Key; 			// RO Key of this SM. This is shown as 0 unless 
 * 								// the requesting SM is proven to be the 
 * 								// master, or the requester is otherwise 
 * 								// authenticated 
 * 	uint32  ActCount;   		// RO Counter that increments each time the SM 
 * 								// issues a SMP or performs other management 
 * 								// activities. Used as a 'heartbeat' indicator 
 * 								// by standby SMs 
 * 	uint32  ElapsedTime;   		// RO Time (in seconds): time Master SM has been 
 * 								// Master, or time since Standby SM was last 
 * 								// updated by Master 
 * 	union {
 * 		uint16  AsReg16;
 * 		struct { IB_BITFIELD4( uint16,
 * 			Priority:			4, 	// RO Administratively assigned priority for this 
 * 									// SM. Can be reset by master SM 
 * 			ElevatedPriority:	4,	// RO This SM's elevated priority 
 * 			InitialPriority:	4,	// RO This SM's initial priority 
 * 			SMStateCurrent:		4 )	// RO This SM's current state (see SM_STATE) 
 * 		} s;
 * 	} u;
 * 		
 * } PACK_SUFFIX STL_SM_INFO;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class SMInfo extends SimpleDatagram<SMInfoBean> {
    public SMInfo() {
        super(26);
    }

    public void setGuid(long guid) {
        buffer.putLong(0, guid);
    }

    public void setSMKey(long key) {
        buffer.putLong(8, key);
    }

    public void setActCount(int count) {
        buffer.putInt(16, count);
    }

    public void setElapsedTime(int seconds) {
        buffer.putInt(20, seconds);
    }

    public void setPriorities(short priorities) {
        buffer.putShort(24, priorities);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public SMInfoBean toObject() {
        buffer.clear();
        SMInfoBean bean = new SMInfoBean();
        bean.setPortGuid(buffer.getLong());
        bean.setSmKey(buffer.getLong());
        bean.setActCount(buffer.getInt());
        bean.setElapsedTime(buffer.getInt());
        short shortVal = buffer.getShort();
        bean.setPriority((byte) ((shortVal >>> 12) & 0x0f));
        bean.setElevatedPriority((byte) ((shortVal >>> 8) & 0x0f));
        bean.setInitialPriority((byte) ((shortVal >>> 4) & 0x0f));
        bean.setSmStateCurrent((byte) (shortVal & 0x0f));
        return bean;
    }

}
