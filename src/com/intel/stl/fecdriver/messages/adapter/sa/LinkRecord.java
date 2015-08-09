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
package com.intel.stl.fecdriver.messages.adapter.sa;

import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: ref: /ALL_EMB/IbAcess/Common/Inc/stl_sa.h v1.92
 * 
 * <pre>
 * LinkRecord
 * 
 * STL Differences:
 * 		LIDs lengthened
 * 		Reserved field added to preserve alignment.
 * 
 * typedef struct _STL_LINK_RECORD {
 * 	struct {
 * 		uint32	FromLID;		
 * 		uint8	FromPort;	
 * 	} PACK_SUFFIX RID;
 * 	
 * 	uint8		ToPort;	
 * 
 * 	uint16		Reserved;
 * 	
 * 	uint32		ToLID;	
 * 
 * } PACK_SUFFIX STL_LINK_RECORD;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class LinkRecord extends SimpleDatagram<LinkRecordBean> {
    public LinkRecord() {
        super(12);
    }

    public void setFromLID(int lid) {
        buffer.putInt(0, lid);
    }

    public int getFromLID() {
        return buffer.getInt(0);
    }

    public void setFromPort(byte port) {
        buffer.put(4, port);
    }

    public void setToPort(byte port) {
        buffer.put(5, port);
    }

    public void setToLid(int lid) {
        buffer.putInt(8, lid);
    }

    @Override
    public LinkRecordBean toObject() {
        buffer.clear();
        int fromLID = buffer.getInt();
        byte fromPortIndex = buffer.get();
        byte toPortIndex = buffer.get();
        buffer.position(8);
        int toLID = buffer.getInt();
        LinkRecordBean bean =
                new LinkRecordBean(fromLID, fromPortIndex, toLID, toPortIndex);
        return bean;
    }
}
