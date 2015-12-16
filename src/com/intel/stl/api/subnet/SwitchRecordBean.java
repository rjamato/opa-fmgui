/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 * 
 *  Functional Group: Fabric Viewer Application
 * 
 *  File Name: SwitchRecordBean.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.3  2015/08/17 18:48:38  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - change backend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.2  2015/06/10 19:36:30  jijunwan
 *  Archive Log: PR 129153 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
 *  Archive Log:
 * 
 *  Overview:
 * 
 *  @author: jypak
 * 
 ******************************************************************************/
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
