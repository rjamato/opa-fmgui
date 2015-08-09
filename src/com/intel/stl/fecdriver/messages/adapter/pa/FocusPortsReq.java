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

import com.intel.stl.api.performance.FocusPortsReqBean;
import com.intel.stl.api.performance.ImageIdBean;
import com.intel.stl.api.performance.PAConstants;
import com.intel.stl.common.StringUtils;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_pa.h v1.33
 * 
 * <pre>
 *  typedef struct _STL_FOCUS_PORTS_REQ {
 * [64]     char                    groupName[STL_PM_GROUPNAMELEN]; // \0 terminated
 * [80]     STL_PA_IMAGE_ID_DATA    imageId;
 * [84]     uint32                  select;
 * [88]     uint32                  start;
 * [92]     uint32                  range;
 *  } PACK_SUFFIX STL_FOCUS_PORTS_REQ;
 *  
 *  typedef struct _STL_PA_Image_ID_Data {
 *  	uint64					imageNumber;
 *  	int32					imageOffset;
 *  	uint32					reserved;
 *  } PACK_SUFFIX STL_PA_IMAGE_ID_DATA;
 * 
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class FocusPortsReq extends SimpleDatagram<FocusPortsReqBean> {
    public FocusPortsReq() {
        super(92);
    }

    public void setGroupName(String name) {
        StringUtils.setString(name, buffer, 0, PAConstants.STL_PM_GROUPNAMELEN);
    }

    public void setImageNumber(long imageNumber) {
        buffer.putLong(64, imageNumber);
    }

    public void setImageOffset(int imageOffset) {
        buffer.putInt(72, imageOffset);
    }

    public void setSelect(int select) {
        buffer.putInt(80, select);
    }

    public void setStart(int start) {
        buffer.putInt(84, start);
    }

    public void setRange(int range) {
        buffer.putInt(88, range);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.ComposedDatagram#toObject()
     */
    @Override
    public FocusPortsReqBean toObject() {
        buffer.clear();
        FocusPortsReqBean bean = new FocusPortsReqBean();
        bean.setGroupName(StringUtils.toString(buffer.array(),
                buffer.arrayOffset(), PAConstants.STL_PM_GROUPNAMELEN));
        buffer.position(64);
        bean.setImageId(new ImageIdBean(buffer.getLong(), buffer.getInt()));
        buffer.position(80);
        bean.setSelect(buffer.getInt());
        bean.setStart(buffer.getInt());
        bean.setRange(buffer.getInt());
        return bean;
    }

}
