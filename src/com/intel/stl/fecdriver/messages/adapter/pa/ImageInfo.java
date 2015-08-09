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
import com.intel.stl.api.performance.ImageInfoBean;
import com.intel.stl.api.performance.SMInfoDataBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.common.StringUtils;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_pa.h v1.45
 * 
 * <pre>
 *  typedef struct _STL_SMINFO_DATA {
 * [4] 	STL_LID_32				lid;
 * [5] 	IB_BITFIELD2(uint8,
 *  		priority : 4,
 *  		state : 4)
 * [6] 	uint8					portNumber;
 * [8] 	uint16					reserved;
 * [16] 	uint64					smPortGuid;
 * [80] 	char					smNodeDesc[64]; // can be 64 char w/o \0
 *  } PACK_SUFFIX STL_SMINFO_DATA;
 *  
 *  typedef struct _STL_IMAGE_INFO_DATA {
 * [16] 	STL_PA_IMAGE_ID_DATA	imageId;
 * [24] 	uint64					sweepStart;
 * [28] 	uint32					sweepDuration;
 * [30] 	uint16					numHFIPorts;
 * [32] 	uint16					reserved3;
 * [34]	    uint16					reserved;
 * [36] 	uint16					numSwitchNodes;
 * [40] 	uint32					numSwitchPorts;
 * [44] 	uint32					numLinks;
 * [48] 	uint32					numSMs;
 * [52] 	uint32					numFailedNodes;
 * [56] 	uint32					numFailedPorts;
 * [60] 	uint32					numSkippedNodes;
 * [64] 	uint32					numSkippedPorts;
 * [68] 	uint32					numUnexpectedClearPorts;
 * [72] 	uint32					reserved2;
 * [232] 	STL_SMINFO_DATA			SMInfo[2];
 *  } PACK_SUFFIX STL_IMAGE_INFO_DATA;
 *  
 *  typedef struct _STL_PA_Image_ID_Data {
 *  	uint64					imageNumber;
 *  	int32					imageOffset;
 *  	uint32					reserved;
 *  } PACK_SUFFIX STL_PA_IMAGE_ID_DATA;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class ImageInfo extends SimpleDatagram<ImageInfoBean> {
    public ImageInfo() {
        super(232);
    }

    public void setImageNumber(long imageNumber) {
        buffer.putLong(0, imageNumber);
    }

    public void setImageOffset(int imageOffset) {
        buffer.putInt(8, imageOffset);
    }

    private SMInfoDataBean getSMInfoBean(int position) {
        buffer.position(position);
        SMInfoDataBean bean = new SMInfoDataBean();
        bean.setLid(buffer.getInt());
        byte byteVal = buffer.get();
        bean.setPriority((byte) ((byteVal >>> 4) & 0x0f));
        bean.setState((byte) (byteVal & 0x0f));
        bean.setPortNumber(buffer.get());
        bean.setSmPortGuid(buffer.getLong(position + 8));
        byte[] raw = new byte[SAConstants.NODE_DESC_LENGTH];
        buffer.position(position + 16);
        buffer.get(raw);
        String name =
                StringUtils.toString(raw, 0, SAConstants.NODE_DESC_LENGTH);
        bean.setSmNodeDesc(name);
        return bean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public ImageInfoBean toObject() {
        buffer.clear();
        ImageInfoBean bean = new ImageInfoBean();
        bean.setImageId(new ImageIdBean(buffer.getLong(), buffer.getInt()));
        buffer.position(16);
        bean.setSweepStart(buffer.getLong());
        bean.setSweepDuration(buffer.getInt());
        bean.setNumHFIPorts(buffer.getShort());
        buffer.getShort(); // reserved
        buffer.getShort(); // reserved
        bean.setNumSwitchNodes(buffer.getShort());
        bean.setNumSwitchPorts(buffer.getInt());
        bean.setNumLinks(buffer.getInt());
        bean.setNumSMs(buffer.getInt());
        bean.setNumFailedNodes(buffer.getInt());
        bean.setNumFailedPorts(buffer.getInt());
        bean.setNumSkippedNodes(buffer.getInt());
        bean.setNumSkippedPorts(buffer.getInt());
        bean.setNumUnexpectedClearPorts(buffer.getInt());
        SMInfoDataBean[] smInfo =
                new SMInfoDataBean[] { getSMInfoBean(72), getSMInfoBean(152) };
        bean.setSMInfo(smInfo);
        return bean;
    }

}
