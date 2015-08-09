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
package com.intel.stl.fecdriver.messages.adapter;

/**
 * ref: /ALL_EMB/IbAccess/Common/Inc/ib_generalServices.h
 * <pre>
 * typedef struct _RMPP_HEADER {
 * 	uint8		RmppVersion;		// version of RMPP implemented 
 * 									// must be 0 if RMPP_FLAG.Active=0 
 * 	uint8		RmppType;			// type of RMPP packet 
 * 	RMPP_FLAG	RmppFlags;
 * 	uint8		RmppStatus;
 * 	union {
 * 		uint32		AsReg32;
 * 		uint32		SegmentNum;	// DATA and ACK 
 * 		uint32		Reserved1;	// ABORT and STOP 
 * 	} u1;
 * 	union {
 * 		uint32		AsReg32;
 * 		uint32		PayloadLen;		// first and last DATA 
 * 		uint32		NewWindowLast;	// ACK 
 * 		uint32		Reserved2;		// ABORT, STOP and middle DATA 
 * 	} u2;
 * } PACK_SUFFIX RMPP_HEADER, *PRMPP_HEADER;
 * </pre> 
 * @author jijunwan
 *
 */
public class RmppHeader extends SimpleDatagram<Void> {
	
	public RmppHeader() {
		super(12);
	}
	
	public void setRmppVersion(byte version) {
		buffer.put(0, version);
	}
	
	public void setRmppType(byte type) {
		buffer.put(1, type);
	}
	
	public void setRmppFlags(byte flags) {
		buffer.put(2, flags);
	}
	
	public void setRmppStatus(byte status) {
		buffer.put(3, status);
	}
	
	public void setSegmentNum(int num) {
		buffer.putInt(4, num);
	}
	
	public void setReserved1(int value) {
		buffer.putInt(4, value);
	}
	
	public void setPayloadLen(int payloadLen) {
		buffer.putInt(8, payloadLen);
	}
	
	public void setNewWindowLast(int newWindowLast) {
		buffer.putInt(8, newWindowLast);
	}
	
	public void Reserved2(int value) {
		buffer.putInt(8, value);
	}
}
