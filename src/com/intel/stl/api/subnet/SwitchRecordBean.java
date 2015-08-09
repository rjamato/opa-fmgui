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

/**
 * Title:        SwitchRecordBean
 * Description:  Switch Record from SA populated by the connect manager.
 * 
 * @author jypak
 * @version 0.0
*/
import java.io.*;

public class SwitchRecordBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private SwitchInfoBean switchInfo;
    private int lid;

	public SwitchRecordBean() {
		super();
	}
	
	public SwitchRecordBean(int lid, SwitchInfoBean switchInfo) {
		super();
		this.lid = lid;
		this.switchInfo = switchInfo;
	}


	/**
	 * @return the switchInfo
	 */
	public SwitchInfoBean getSwitchInfo() {
		return switchInfo;
	}
	/**
	 * @param switchInfo the switchInfo to set
	 */
	public void setSwitchInfo(SwitchInfoBean switchInfo) {
		this.switchInfo = switchInfo;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SwitchRecordBean [lid=" + lid + ", switchInfo=" + switchInfo
				+ "]";
	}

}