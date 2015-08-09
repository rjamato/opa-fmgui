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

import java.io.Serializable;

/**
 * @author jijunwan
 *
 */
public class SMRecordBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int lid;
	private SMInfoBean smInfo;
	
	public SMRecordBean() {
		super();
	}
	
	public SMRecordBean(int lid, SMInfoBean smInfo) {
		super();
		this.lid = lid;
		this.smInfo = smInfo;
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
	 * @return the smInfo
	 */
	public SMInfoBean getSmInfo() {
		return smInfo;
	}
	/**
	 * @param smInfo the smInfo to set
	 */
	public void setSmInfo(SMInfoBean smInfo) {
		this.smInfo = smInfo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SMRecordBean [lid=" + lid + ", smInfo=" + smInfo + "]";
	}
	
}
