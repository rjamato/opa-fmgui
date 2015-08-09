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

//typedef struct {
//	struct {
//		uint32	LID;	
//		IB_BITFIELD2(uint32, 
//				Reserved:14,
//				BlockNum:18);
//	} PACK_SUFFIX RID;
//
//	/* 8 bytes */
//
//	uint8 		LinearFdbData[64];
//	
//	/* 72 bytes */
//} PACK_SUFFIX STL_LINEAR_FORWARDING_TABLE_RECORD;
//
//typedef struct {
//	PORT  LftBlock[MAX_LFT_ELEMENTS_BLOCK];
//
//} PACK_SUFFIX STL_LINEAR_FORWARDING_TABLE;

/**
 * Title:        LFTRecordBean
 * Description:  Linear Forwarding Table Record from SA populated by the connect manager.
 * 
 * @author jypak
 * @version 0.0
*/
import java.io.*;
import java.util.Arrays;

public class LFTRecordBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int lid;
	private int blockNum;
	private byte[] linearFdbData;
	
	public LFTRecordBean() {
		super();
	}
	
	public LFTRecordBean(int lid, int blockNum, byte[] linearFdbData) {
		super();
		this.lid = lid;
		this.blockNum = blockNum;
		this.linearFdbData = linearFdbData;
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
	 * @return the linearFdbData
	 */
	public byte[] getLinearFdbData() {
		return linearFdbData;
	}
	/**
	 * @param linearFdbData the linearFdbData to set
	 */
	public void setLinearFdbData(byte[] linearFdbData) {
		this.linearFdbData = linearFdbData;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LFTRecordBean [lid=" + lid + ", blockNum=" + blockNum
				+ ", linearFdbData=" + Arrays.toString(linearFdbData) + "]";
	}

}