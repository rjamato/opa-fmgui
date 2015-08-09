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
 *  File Name: Test.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/01/11 20:04:29  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/24 14:38:26  jijunwan
 *  Archive Log:    fixed a bug in data parsing
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.adapter.sa;

import com.intel.stl.api.subnet.MFTRecordBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sa.h v1.92<br>
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sm.h v1.115
 * 
 * <pre>
 * MFTRecord 
 * 
 * NOTES:
 * 		In IB the width of the PORTMASK data type was defined as only 16 
 * 	bits, requiring the SM to iterate over 3 different positions values 
 * 	to retrieve the MFTs for a 48-port switch. 
 * 	For this reason PORTMASK is now defined as 64 bits wide, eliminating 
 * 	the need to use the "position" attribute in the Gen 1 & Gen 2 
 * 	generations of hardware. 
 * 
 * 	As above, a "block" is defined as 64 bytes; therefore a single block 
 * 	will contain 8 MFT records. The consumer should use GetTable() and 
 * 	RMPP to retrieve more than one block. As with the RFT, BlockNum is 
 * 	defined as 21 bits, providing for a total of 2^24 LIDs.
 * 
 * STL Differences:
 * 	PORTMASK is now 64 bits.
 * 	LID is now 32 bits.
 * 	Position is now 2 bits.
 * 	Reserved is now 9 bits.
 * 	BlockNum is now 21 bits.
 * 	Reserved2 removed to preserve word alignment.
 * 
 *  
 * #define STL_MFTB_WIDTH 64
 * #define STL_MFTB_MAX_POSITION 4
 * typedef struct _STL_MULTICAST_FORWARDING_TABLE_RECORD {
 * 	struct {
 * 		uint32		LID; 				// Port 0 of the switch.	
 * 	
 * 		IB_BITFIELD3(uint32, 
 * 				Position:2,			
 * 				Reserved:9,
 * 				BlockNum:21);
 * 	} PACK_SUFFIX RID;
 * 
 * 	STL_MULTICAST_FORWARDING_TABLE MftTable;
 * 	
 * } PACK_SUFFIX STL_MULTICAST_FORWARDING_TABLE_RECORD;
 * 
 * 
 * Multicast Forwarding Table (MFT)
 * 
 * Attribute Modifier as: NNNN NNNN PP0A BBBB BBBB BBBB BBBB BBBB
 *                        N:   Number of blocks
 *                        P:   Position number
 *                        A=1: All blocks starting at B (Set only)
 *                        B:   Block number
 * 
 * The (max) MFT is 2**23 entries (LIDs) long (STL_LID_24 / 2), 256 bits wide.
 * Each MFT block is 8 entries long, 64 bits wide.  The MFT is a
 * 2-dimensional array of blocks[2**20][4].
 * 
 * 
 * typedef uint64  STL_PORTMASK;			// Port mask element (MFT and PGFT 
 * 
 * #define STL_NUM_MFT_ELEMENTS_BLOCK	8	// Num elements per block 
 * #define STL_NUM_MFT_POSITIONS_MASK	4	// Num positions per 256-bit port mask 
 * #define STL_MAX_MFT_BLOCK_NUM		0xFFFFF
 * #define STL_PORT_MASK_WIDTH			64		// Width of STL_PORTMASK in bits 
 * #define STL_MAX_PORTS			255
 * 
 * typedef struct {
 * 	STL_PORTMASK  MftBlock[STL_NUM_MFT_ELEMENTS_BLOCK];
 * 
 * } PACK_SUFFIX STL_MULTICAST_FORWARDING_TABLE;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class MFTRecord extends SimpleDatagram<MFTRecordBean> {

    public MFTRecord() {
        super(72);
    }

    public void setLID(int lid) {
        buffer.putInt(0, lid);
    }

    public void setPosition(byte position) {
        int oldVal = buffer.getInt(4);
        int val = (oldVal & 0x1fffff) | (position << 30);
        buffer.putInt(4, val);
    }

    public void setBlockNum(int num) {
        int oldVal = buffer.getInt(4);
        int val = (oldVal & 0xc0000000) | (num & 0x1fffff);
        buffer.putInt(4, val);
    }

    public void setMftTable(long[] data) {
        if (data.length != SAConstants.STL_NUM_MFT_ELEMENTS_BLOCK) {
            throw new IllegalArgumentException("Invalid data length. Expect "
                    + SAConstants.STL_NUM_MFT_ELEMENTS_BLOCK + ", got "
                    + data.length);
        }

        buffer.position(8);
        for (long val : data) {
            buffer.putLong(val);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public MFTRecordBean toObject() {
        buffer.clear();
        int lid = buffer.getInt();
        int intVal = buffer.getInt();
        byte position = (byte) (intVal >>> 30);
        int blockNum = intVal & 0x1fffff;
        MFTRecordBean bean = new MFTRecordBean(lid, position, blockNum);
        long[] data = new long[SAConstants.STL_NUM_MFT_ELEMENTS_BLOCK];
        for (int i = 0; i < SAConstants.STL_NUM_MFT_ELEMENTS_BLOCK; i++) {
            data[i] = buffer.getLong();
        }
        bean.setMftTable(data);
        return bean;
    }

}
