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
import com.intel.stl.api.performance.PortCountersBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_pa.h v1.33
 * 
 * <pre>
 * typedef struct _STL_PORT_COUNTERS_DATA {
 * 	uint32				nodeLid;
 * 	uint8				portNumber;
 * 	uint8				reserved[3];
 * 	uint32				flags;
 * 	uint32				reserved1;
 * 	uint64				portXmitData;
 * 	uint64				portRcvData;
 * 	uint64				portXmitPkts;
 * 	uint64				portRcvPkts;
 * 	uint64				portMulticastXmitPkts;
 * 	uint64				portMulticastRcvPkts;
 * 	uint64				localLinkIntegrityErrors;
 * 	uint64				fmConfigErrors;
 * 	uint64				portRcvErrors;
 * 	uint64				excessiveBufferOverruns;
 * 	uint64				portRcvConstraintErrors;
 * 	uint64				portRcvSwitchRelayErrors;
 * 	uint64				portXmitDiscards;
 * 	uint64				portXmitConstraintErrors;
 * 	uint64				portRcvRemotePhysicalErrors;
 * 	uint64				swPortCongestion;
 * 	uint64				portXmitWait;
 * 	uint64				portRcvFECN;
 * 	uint64				portRcvBECN;
 * 	uint64				portXmitTimeCong;
 * 	uint64				portXmitWastedBW;
 * 	uint64				portXmitWaitData;
 * 	uint64				portRcvBubble;
 * 	uint64				portMarkFECN;
 * 	uint32				linkErrorRecovery;
 * 	uint32				linkDowned;
 * 	uint8				uncorrectableErrors;
 * 	union {
 * 		uint8			AsReg8;
 * 		struct {		IB_BITFIELD2(uint8,
 * 						  reserved : 5,
 * 						  linkQualityIndicator : 3)
 * 		} PACK_SUFFIX s;
 * 	} lq;
 *  uint8               reserved2[6];
 * 	STL_PA_IMAGE_ID_DATA imageId;
 * } PACK_SUFFIX STL_PORT_COUNTERS_DATA;
 * 
 * typedef struct _STL_PA_Image_ID_Data {
 * 	uint64					imageNumber;
 * 	int32					imageOffset;
 * 	uint32					reserved;
 * } PACK_SUFFIX STL_PA_IMAGE_ID_DATA;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class PortCounters extends SimpleDatagram<PortCountersBean> {
    public PortCounters() {
        super(240);
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

    public void setImageNumber(long imageNumber) {
        buffer.putLong(224, imageNumber);
    }

    public void setImageOffset(int imageOffset) {
        buffer.putInt(232, imageOffset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public PortCountersBean toObject() {
        buffer.clear();
        PortCountersBean bean = new PortCountersBean();
        bean.setNodeLid(buffer.getInt());
        bean.setPortNumber(buffer.get());
        buffer.position(8);
        bean.setFlags(buffer.getInt());
        buffer.position(16);
        bean.setPortXmitData(buffer.getLong());
        bean.setPortRcvData(buffer.getLong());
        bean.setPortXmitPkts(buffer.getLong());
        bean.setPortRcvPkts(buffer.getLong());
        bean.setPortMulticastXmitPkts(buffer.getLong());
        bean.setPortMulticastRcvPkts(buffer.getLong());
        bean.setLocalLinkIntegrityErrors(buffer.getLong());
        bean.setFmConfigErrors(buffer.getLong());
        bean.setPortRcvErrors(buffer.getLong());
        bean.setExcessiveBufferOverruns(buffer.getLong());
        bean.setPortRcvConstraintErrors(buffer.getLong());
        bean.setPortRcvSwitchRelayErrors(buffer.getLong());
        bean.setPortXmitDiscards(buffer.getLong());
        bean.setPortXmitConstraintErrors(buffer.getLong());
        bean.setPortRcvRemotePhysicalErrors(buffer.getLong());
        bean.setSwPortCongestion(buffer.getLong());
        bean.setPortXmitWait(buffer.getLong());
        bean.setPortRcvFECN(buffer.getLong());
        bean.setPortRcvBECN(buffer.getLong());
        bean.setPortXmitTimeCong(buffer.getLong());
        bean.setPortXmitWastedBW(buffer.getLong());
        bean.setPortXmitWaitData(buffer.getLong());
        bean.setPortRcvBubble(buffer.getLong());
        bean.setPortMarkFECN(buffer.getLong());
        bean.setLinkErrorRecovery(buffer.getInt());
        bean.setLinkDowned(buffer.getInt());
        bean.setUncorrectableErrors(buffer.get());
        byte byteVal = buffer.get();
        bean.setLinkQualityIndicator((byte) (byteVal & 0x07));
        buffer.position(224);
        bean.setImageId(new ImageIdBean(buffer.getLong(), buffer.getInt()));
        return bean;
    }

}
