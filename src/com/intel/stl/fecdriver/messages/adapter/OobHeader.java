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

import com.intel.stl.common.Constants;

/**
 * There is difference between spec and iba_fequery. I'm using iba_fequery. see
 * /ALL_EMB/IbaTools/iba_fequery/fe_net.h
 * 
 * <pre>
 * typedef struct __OOBHeader {
 * 	uint32_t 		HeaderVersion; 		// Version of the FE protocol header 
 * 	uint32_t 		Length; 			// Length of the message data payload
 * 	uint32_t 		Reserved[2]; 		// Reserved
 * } OOBHeader;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class OobHeader extends SimpleDatagram<Void> {

    public OobHeader() {
        super(16);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vieo.fv.resource.stl.data.SimpleDatagram#initData()
     */
    @Override
    protected void initData() {
        super.initData();
        setVersion();
    }

    public void setVersion() {
        setVersion(Constants.PROTOCAL_VERSION);
    }

    public void setVersion(int version) {
        buffer.putInt(0, version);
    }

    public void setPayloadSize(int length) {
        buffer.putInt(4, length);
    }

}
