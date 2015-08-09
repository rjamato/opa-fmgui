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
package com.intel.stl.fecdriver.messages.adapter.pa;

import com.intel.stl.api.performance.ImageIdBean;
import com.intel.stl.api.performance.PAConstants;
import com.intel.stl.api.performance.VFPortCountersBean;
import com.intel.stl.common.StringUtils;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_pa.h v1.33
 * 
 * <pre>
 *  typedef struct _STL_PA_VF_PORT_COUNTERS_DATA {
 * [4]     uint32              nodeLid;
 * [5]     uint8               portNumber;
 * [8]     uint8               reserved[3];
 * [12]    uint32              flags;
 * [16]    uint32              reserved1;
 * [80]    char                vfName[STL_PM_VFNAMELEN];
 * [88]    uint64              vfSID;
 * [104]   STL_PA_IMAGE_ID_DATA imageId;
 * [112]   uint64              portVFXmitData;
 * [120]   uint64              portVFRcvData;
 * [128]   uint64              portVFXmitPkts;
 * [136]   uint64              portVFRcvPkts;
 * [144]   uint64              portVFXmitDiscards;
 * [152]   uint64              swPortVFCongestion;
 * [160]   uint64              portVFXmitWait;
 * [168]   uint64              portVFRcvFECN;
 * [176]   uint64              portVFRcvBECN;
 * [184]   uint64              portVFXmitTimeCong;
 * [192]   uint64              portVFXmitWastedBW;
 * [200]   uint64              portVFXmitWaitData;
 * [208]   uint64              portVFRcvBubble;
 * [216]   uint64              portVFMarkFECN;
 *  } PACK_SUFFIX STL_PA_VF_PORT_COUNTERS_DATA;
 *  
 *  typedef struct _STL_PA_Image_ID_Data {
 * [8] 	uint64					imageNumber;
 * [12] 	int32					imageOffset;
 * [16] 	uint32					reserved;
 *  } PACK_SUFFIX STL_PA_IMAGE_ID_DATA;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class VFPortCounters extends SimpleDatagram<VFPortCountersBean> {
    public VFPortCounters() {
        super(216);
    }

    public void setNodeLid(int lid) {
        buffer.putInt(0, lid);
    }

    public void setPortNumber(byte num) {
        buffer.put(4, num);
    }

    public void setFlags(int flag) {
        buffer.putInt(8, flag);
    }

    public void setVfName(String name) {
        StringUtils
                .setString(name, buffer, 16, PAConstants.STL_PM_GROUPNAMELEN);
    }

    public void setVfSID(long sid) {
        buffer.putLong(80, sid);
    }

    public void setImageNumber(long imageNumber) {
        buffer.putLong(88, imageNumber);
    }

    public void setImageOffset(int imageOffset) {
        buffer.putInt(96, imageOffset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public VFPortCountersBean toObject() {
        buffer.clear();
        VFPortCountersBean bean = new VFPortCountersBean();
        bean.setNodeLid(buffer.getInt());
        bean.setPortNumber(buffer.get());
        buffer.position(8);
        bean.setFlags(buffer.getInt());
        bean.setVfName(StringUtils.toString(buffer.array(),
                buffer.arrayOffset() + 16, PAConstants.STL_PM_VFNAMELEN));
        buffer.position(80);
        bean.setVfSID(buffer.getLong());
        bean.setImageId(new ImageIdBean(buffer.getLong(), buffer.getInt()));
        buffer.position(104);
        bean.setPortVFXmitData(buffer.getLong());
        bean.setPortVFRcvData(buffer.getLong());
        bean.setPortVFXmitPkts(buffer.getLong());
        bean.setPortVFRcvPkts(buffer.getLong());
        bean.setPortVFXmitDiscards(buffer.getLong());
        bean.setSwPortVFCongestion(buffer.getLong());
        bean.setPortVFXmitWait(buffer.getLong());
        bean.setPortVFRcvFECN(buffer.getLong());
        bean.setPortVFRcvBECN(buffer.getLong());
        bean.setPortVFXmitTimeCong(buffer.getLong());
        bean.setPortVFXmitWastedBW(buffer.getLong());
        bean.setPortVFXmitWaitData(buffer.getLong());
        bean.setPortVFRcvBubble(buffer.getLong());
        bean.setPortVFMarkFECN(buffer.getLong());
        return bean;
    }

}
