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
 * #define STL_MAD_BLOCK_SIZE			2048
 * #define MAD_BLOCK_SIZE				STL_MAD_BLOCK_SIZE
 * #define	IBA_RMPP_GS_HDRSIZE			(sizeof(MAD_COMMON)+sizeof(RMPP_HEADER))
 * #define	IBA_RMPP_GS_DATASIZE		(MAD_BLOCK_SIZE - IBA_RMPP_GS_HDRSIZE)
 * #define	STL_IBA_RMPP_GS_DATASIZE	IBA_RMPP_GS_DATASIZE
 * 
 * typedef struct	_MAD_RMPP {
 * 	MAD_COMMON			common;
 * 	RMPP_HEADER			RmppHdr;
 * 	uint8				Data[STL_IBA_RMPP_GS_DATASIZE];
 * } PACK_SUFFIX	MAD_RMPP;
 * </pre> 
 * 
 * @author jijunwan
 *
 */
public class RmppMad extends ComposedDatagram<Void> {
	protected CommonMad commonMad;
	protected RmppHeader rmppHeader;
	protected IDatagram<?> data;
	
	public RmppMad() {
		commonMad = new CommonMad();
		addDatagram(commonMad);
		rmppHeader = new RmppHeader();
		addDatagram(rmppHeader);
	}
	
	/**
	 * @param data the data to set
	 */
	public void setData(IDatagram<?> data) {
		if (this.data!=null)
			removeDatagram(this.data);
		this.data = data;
		addDatagram(data);
	}

	public IDatagram<?> getData() {
		return data;
	}

	/**
	 * @return the commonMad
	 */
	public CommonMad getCommonMad() {
		return commonMad;
	}

	/**
	 * @return the rmppHeader
	 */
	public RmppHeader getRmppHeader() {
		return rmppHeader;
	}

}
