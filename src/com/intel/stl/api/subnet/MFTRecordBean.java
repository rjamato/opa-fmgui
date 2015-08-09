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

/*
 * MFTRecord 
 * 
 * NOTES:
 * 		In IB the width of the PORTMASK data type was defined as only 16 
 *		bits, requiring the SM to iterate over 3 different positions values 
 *		to retrieve the MFTs for a 48-port switch. 
 *		For this reason PORTMASK is now defined as 64 bits wide, eliminating 
 *		the need to use the "position" attribute in the Gen 1 & Gen 2 
 *		generations of hardware. 
 *
 *		As above, a "block" is defined as 64 bytes; therefore a single block 
 *		will contain 8 MFT records. The consumer should use GetTable() and 
 *		RMPP to retrieve more than one block. As with the RFT, BlockNum is 
 *		defined as 21 bits, providing for a total of 2^24 LIDs.
 *
 * STL Differences:
 *		PORTMASK is now 64 bits.
 *		LID is now 32 bits.
 *		Position is now 2 bits.
 *		Reserved is now 9 bits.
 *		BlockNum is now 21 bits.
 *		Reserved2 removed to preserve word alignment.
 */

//typedef struct _STL_MULTICAST_FORWARDING_TABLE_RECORD {
//	struct {
//		uint32		LID; 				// Port 0 of the switch.	
//	
//		STL_FIELDUNION3(u1, 32,
//				Position:2,			
//				Reserved:9,
//				BlockNum:21);
//	} PACK_SUFFIX RID;
//
//	STL_MULTICAST_FORWARDING_TABLE MftTable;
//	
//} PACK_SUFFIX STL_MULTICAST_FORWARDING_TABLE_RECORD;

//typedef struct {
//	STL_PORTMASK  MftBlock[STL_NUM_MFT_ELEMENTS_BLOCK];
//
//} PACK_SUFFIX STL_MULTICAST_FORWARDING_TABLE;

/**
 * Title:        MFTRecordBean
 * Description:  Multicast Forwarding Table Record from SA populated by the connect manager.
 * 
 * @author jypak
 * @version 0.0
*/
import java.io.*;
import java.util.Arrays;

public class MFTRecordBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int lid;
	private byte position;
	private int blockNum;
	
	private long[] mftTable;
    //How about number of blocks? List<LFTRecordBean> size will be it.
	
	public MFTRecordBean() {
		super();
	}
	
	public MFTRecordBean(int lid, byte position, int blockNum) {
		super();
		this.lid = lid;
		this.position = position;
		this.blockNum = blockNum;
	}

	/**
	 * @return the lid
	 */
	public int getLid() {
		return lid;
	}

	/**
	 * @param lid the lid to set
	 */
	public void setLid(int lid) {
		this.lid = lid;
	}

	/**
	 * @return the position
	 */
	public byte getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(byte position) {
		this.position = position;
	}

	/**
	 * @return the blockNum
	 */
	public int getBlockNum() {
		return blockNum;
	}

	/**
	 * @param blockNum the blockNum to set
	 */
	public void setBlockNum(int blockNum) {
		this.blockNum = blockNum;
	}

	/**
	 * @return the mftTable
	 */
	public long[] getMftTable() {
		return mftTable;
	}

	/**
	 * @param mftTable the mftTable to set
	 */
	public void setMftTable(long[] mftTable) {
		this.mftTable = mftTable;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MFTRecordBean [lid=" + lid + ", position=" + position
				+ ", blockNum=" + blockNum + ", mftTable="
				+ Arrays.toString(mftTable) + "]";
	}

}