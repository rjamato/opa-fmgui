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

import com.intel.stl.api.subnet.P_KeyTableBean;
import com.intel.stl.api.subnet.P_KeyTableRecordBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sa.h v1.92<br>
 * /ALL_EMB/IbAcess/Common/Inc/stl_sm.h v1.115
 * 
 * <pre>
 * P_KeyTableRecord
 * 
 * STL Differences:
 * 	LID extended to 32 bits.
 * 	Reserved shortened to restore alignment.
 * 
 * typedef struct {
 * 	struct {
 * 		uint32	LID;
 * 		uint16	Blocknum;
 * 		uint8	PortNum;
 * 	} PACK_SUFFIX RID;
 * 	
 * 	uint8		Reserved;	 
 * 	
 * 	STL_PARTITION_TABLE	PKeyTblData;
 * 	
 * } PACK_SUFFIX STL_P_KEY_TABLE_RECORD;
 * 
 * typedef struct {
 * 
 * 	STL_PKEY_ELEMENT PartitionTableBlock[NUM_PKEY_ELEMENTS_BLOCK];	// RW List of P_Key Block elements
 * 
 * } PACK_SUFFIX STL_PARTITION_TABLE;
 *  
 * typedef union {
 * 	uint16  AsReg16;
 * 	struct { IB_BITFIELD2( uint16,
 * 		MembershipType:		1,				// 0=Limited, 1=Full 
 * 		P_KeyBase:			15 )			// Base value of the P_Key that 
 * 											//  the endnode will use to check 
 * 											//  against incoming packets 
 * 	} s;
 * 
 * } PACK_SUFFIX STL_PKEY_ELEMENT;
 * 
 * #define NUM_PKEY_ELEMENTS_BLOCK		(PARTITION_TABLE_BLOCK_SIZE)
 * #define PARTITION_TABLE_BLOCK_SIZE 32
 * 
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class PKeyTableRecord extends SimpleDatagram<P_KeyTableRecordBean> {

    public PKeyTableRecord() {
        super(72);
    }

    public void setLID(int lid) {
        buffer.putInt(0, lid);
    }

    public void setBlockNum(short num) {
        buffer.putShort(4, num);
    }

    public void setPortNum(byte num) {
        buffer.put(6, num);
    }

    public void setPKeyTableData(short[] data) {
        if (data.length != SAConstants.NUM_PKEY_ELEMENTS_BLOCK) {
            throw new IllegalArgumentException("Invalid data length. Expect "
                    + SAConstants.NUM_PKEY_ELEMENTS_BLOCK + ",  got "
                    + data.length + ".");
        }

        buffer.position(8);
        for (int i = 0; i < SAConstants.NUM_PKEY_ELEMENTS_BLOCK; i++) {
            buffer.putShort(data[i]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public P_KeyTableRecordBean toObject() {
        buffer.clear();
        int lid = buffer.getInt();
        short blockNum = buffer.getShort();
        byte portNum = buffer.get();

        buffer.position(8);
        P_KeyTableBean[] pKeyTableData =
                new P_KeyTableBean[SAConstants.NUM_PKEY_ELEMENTS_BLOCK];
        for (int i = 0; i < SAConstants.NUM_PKEY_ELEMENTS_BLOCK; i++) {
            short val = buffer.getShort();
            pKeyTableData[i] =
                    new P_KeyTableBean((val & 0x8000) == 0x8000,
                            (short) (val & 0x7fff));
        }
        P_KeyTableRecordBean bean =
                new P_KeyTableRecordBean(lid, blockNum, portNum, pKeyTableData);
        return bean;
    }

}
