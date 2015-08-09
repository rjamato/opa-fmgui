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

import com.intel.stl.api.performance.GroupListBean;
import com.intel.stl.api.performance.PAConstants;
import com.intel.stl.common.StringUtils;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_pa.h v1.45
 * 
 * <pre>
 * typedef struct _STL_PA_Group_List {
 * 	char					groupNames[STL_PM_GROUPNAMELEN];	// \0 terminated
 * } PACK_SUFFIX STL_PA_GROUP_LIST;
 * #define STL_PM_GROUPNAMELEN		64
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class GroupList extends SimpleDatagram<GroupListBean> {
    public GroupList() {
        super(PAConstants.STL_PM_GROUPNAMELEN);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.ComposedDatagram#toObject()
     */
    @Override
    public GroupListBean toObject() {
        buffer.clear();
        byte[] raw = buffer.array();
        String name =
                StringUtils.toString(raw, buffer.arrayOffset(),
                        PAConstants.STL_PM_GROUPNAMELEN);
        GroupListBean bean = new GroupListBean(name);
        return bean;
    }

}
