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
 * ref: /ALL_EMB/IbaTools/iba_fequery/fe_net.h
 * <pre>
 * typedef struct _OOBPacket {
 * 	OOBHeader Header;
 * 	MAD_RMPP MadData;
 * } OOBPacket;
 * </pre> 
 * @author jijunwan
 *
 */
public class OobPacket extends ComposedDatagram<Void> {
	private OobHeader oobHeader;
	private RmppMad rmppMad;
	private long expireTime;
	
	public OobPacket() {
		oobHeader = new OobHeader();
		addDatagram(oobHeader);
	}
	
	public OobPacket(RmppMad rmppMad) {
		this();
		this.rmppMad = rmppMad;
		addDatagram(rmppMad);
	}

	/**
	 * @param rmppMad the rmppMad to set
	 */
	public void setRmppMad(RmppMad rmppMad) {
		this.rmppMad = rmppMad;
		addDatagram(rmppMad);
	}

	/**
	 * @return the rmppMad
	 */
	public RmppMad getRmppMad() {
		return rmppMad;
	}

	/**
	 * @return the oobHeader
	 */
	public OobHeader getOobHeader() {
		return oobHeader;
	}
	
	public void fillPayloadSize() {
		int len = getLength() - oobHeader.getLength();
		oobHeader.setPayloadSize(len);
	}

	/**
	 * @return the expireTime
	 */
	public long getExpireTime() {
		return expireTime;
	}

	/**
	 * @param expireTime the expireTime to set
	 */
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	
}
