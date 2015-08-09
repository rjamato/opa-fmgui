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

import com.intel.stl.api.subnet.LFTRecordBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sa.h v1.92
 * 
 * <pre>
 * LFTRecord 
 * 
 * Blocks are still defined as 64 bytes long to be consistent with IB.
 * 
 * STL Differences:
 * 
 * 	LID extended to 32 bits.
 * BlockNum extended to 18 bits.
 * 
 * typedef struct {
 * 	struct {
 * 		uint32	LID;	
 * 		IB_BITFIELD2(uint32, 
 * 				Reserved:14,
 * 				BlockNum:18);
 * 	} PACK_SUFFIX RID;
 * 
 * 	// 8 bytes 
 * 
 * 	uint8 		LinearFdbData[64];
 * 	
 * 	// 72 bytes 
 * } PACK_SUFFIX STL_LINEAR_FORWARDING_TABLE_RECORD;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class LFTRecord extends SimpleDatagram<LFTRecordBean> {

    public LFTRecord() {
        super(72);
    }

    public void setLID(int lid) {
        buffer.putInt(0, lid);
    }

    public void setBlockNum(int num) {
        buffer.putInt(4, num & 0x03ffff);
    }

    public void setLinearFdbData(byte[] data) {
        if (data.length != SAConstants.FDB_DATA_LENGTH) {
            throw new IllegalArgumentException("Invalid array length. Expect "
                    + SAConstants.FDB_DATA_LENGTH + ", got" + data.length);
        }

        buffer.position(8);
        buffer.put(data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public LFTRecordBean toObject() {
        buffer.clear();
        int lid = buffer.getInt();
        int blockNum = buffer.getInt() & 0x03ffff;
        byte[] data = new byte[SAConstants.FDB_DATA_LENGTH];
        buffer.get(data);
        LFTRecordBean bean = new LFTRecordBean(lid, blockNum, data);
        return bean;
    }

}
