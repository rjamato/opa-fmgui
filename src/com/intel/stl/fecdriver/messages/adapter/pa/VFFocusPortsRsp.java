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
import com.intel.stl.api.performance.VFFocusPortsRspBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.common.StringUtils;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_pa.h version 1.45
 * 
 * <pre>
 *  typedef struct _STL_PA_VF_FOCUS_PORTS_RSP {
 * [16]     STL_PA_IMAGE_ID_DATA    imageId;
 * [20]     uint32                  nodeLid;
 * [21]     uint8                   portNumber;
 * [22]     uint8                   rate;   // IB_STATIC_RATE - 5 bit value
 * [23]     uint8                   mtu;    // enum IB_MTU - 4 bit value
 * [24]     uint8                   reserved;
 * [32]     uint64                  value;      // list sorting factor
 * [40]     uint64                  value2;     // good place for GUID
 * [104]     char                    nodeDesc[64]; // can be 64 char w/o \0
 * [108]     uint32                  neighborLid;
 * [109]     uint8                   neighborPortNumber;
 * [112]     uint8                   reserved3[3];
 * [120]     uint64                  neighborValue;
 * [128]     uint64                  neighborGuid;
 * [192]     char                    neighborNodeDesc[64]; // can be 64 char w/o \0
 *  } PACK_SUFFIX STL_PA_VF_FOCUS_PORTS_RSP;
 *   
 *    typedef struct _STL_PA_Image_ID_Data {
 *        uint64                  imageNumber;
 *        int32                   imageOffset;
 *        uint32                  reserved;
 *    } PACK_SUFFIX STL_PA_IMAGE_ID_DATA;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class VFFocusPortsRsp extends SimpleDatagram<VFFocusPortsRspBean> {
    public VFFocusPortsRsp() {
        super(192);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public VFFocusPortsRspBean toObject() {
        buffer.clear();
        VFFocusPortsRspBean bean = new VFFocusPortsRspBean();
        bean.setImageId(new ImageIdBean(buffer.getLong(), buffer.getInt()));
        buffer.position(16);
        bean.setNodeLid(buffer.getInt());
        bean.setPortNumber(buffer.get());
        bean.setRate(buffer.get());
        bean.setMtu(buffer.get());
        buffer.position(24);
        bean.setValue(buffer.getLong());
        bean.setValue2(buffer.getLong());
        bean.setNodeDesc(StringUtils.toString(buffer.array(),
                buffer.arrayOffset() + 40, SAConstants.NODE_DESC_LENGTH));
        buffer.position(104);
        bean.setNeighborLid(buffer.getInt());
        bean.setNeighborPortNumber(buffer.get());
        buffer.position(112);
        bean.setNeighborValue(buffer.getLong());
        bean.setNeighborGuid(buffer.getLong());
        bean.setNeighborNodeDesc(StringUtils.toString(buffer.array(),
                buffer.arrayOffset() + 128, SAConstants.NODE_DESC_LENGTH));
        return bean;
    }

}
