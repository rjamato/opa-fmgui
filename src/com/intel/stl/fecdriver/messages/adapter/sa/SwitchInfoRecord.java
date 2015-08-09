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

import com.intel.stl.api.subnet.SwitchRecordBean;
import com.intel.stl.fecdriver.messages.adapter.ComposedDatagram;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sa.h v1.92
 * 
 * <pre>
 * SwitchInfoRecord
 * 
 * STL Differences
 * 	Old LID/Reserved RID replaced with 32 bit LID.
 * 	Reserved added to align SwitchInfoData.
 * 
 * typedef struct {
 * 	struct {
 * 		uint32 	LID;
 * 	} PACK_SUFFIX RID;
 * 
 * 	uint32		Reserved;		
 * 
 * 	STL_SWITCH_INFO	SwitchInfoData; 	
 * } PACK_SUFFIX STL_SWITCHINFO_RECORD;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class SwitchInfoRecord extends ComposedDatagram<SwitchRecordBean> {
    private SimpleDatagram<Void> header = null;

    private SwitchInfo switchInfo = null;

    public SwitchInfoRecord() {
        header = new SimpleDatagram<Void>(8);
        addDatagram(header);

        switchInfo = new SwitchInfo();
        addDatagram(switchInfo);
    }

    public void setLid(int lid) {
        header.getByteBuffer().putInt(0, lid);
    }

    public int getLid() {
        return header.getByteBuffer().getInt(0);
    }

    /**
     * @return the switchInfo
     */
    public SwitchInfo getSwitchInfo() {
        return switchInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.ComposedDatagram#toObject()
     */
    @Override
    public SwitchRecordBean toObject() {
        SwitchRecordBean bean =
                new SwitchRecordBean(getLid(), switchInfo.toObject());
        return bean;
    }

}
