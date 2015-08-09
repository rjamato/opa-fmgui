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

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: PortConfig.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/01/11 20:04:26  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/08 18:03:12  jijunwan
 *  Archive Log:    fixed a bug in data parsing
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/15 21:38:01  jijunwan
 *  Archive Log:    1) implemented the new GroupConfig and FocusPorts queries that use separated req and rsp data structure
 *  Archive Log:    2) adapter our drive and db code to the new data structure
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/16 16:48:30  jijunwan
 *  Archive Log:    made SAConstants and PAonstants acessible from UI
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:31:10  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/01 21:37:08  jijunwan
 *  Archive Log:    Added PA attributes GroupConfig
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.adapter.pa;

import com.intel.stl.api.performance.GroupConfigRspBean;
import com.intel.stl.api.performance.ImageIdBean;
import com.intel.stl.api.performance.PortConfigBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.common.StringUtils;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_pa.h v1.33
 * 
 * <pre>
 *  typedef struct _STL_PA_Group_Cfg_Rsp {
 * [16]     STL_PA_IMAGE_ID_DATA    imageId;
 * [24]     uint64                  nodeGUID;
 * [88]     char                    nodeDesc[64];
 * [92]     uint32                  nodeLid;
 * [93]     uint8                   portNumber;
 * [96]     uint8                   reserved[3];
 *  } PACK_SUFFIX STL_PA_PM_GROUP_CFG_RSP;
 * </pre>
 */
public class GroupConfigRsp extends SimpleDatagram<GroupConfigRspBean> {
    public GroupConfigRsp() {
        super(96);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public GroupConfigRspBean toObject() {
        buffer.clear();
        GroupConfigRspBean bean = new GroupConfigRspBean();
        bean.setImageId(new ImageIdBean(buffer.getLong(), buffer.getInt()));
        PortConfigBean port = new PortConfigBean();
        buffer.position(16);
        port.setNodeGUID(buffer.getLong());
        port.setNodeDesc(StringUtils.toString(buffer.array(),
                buffer.arrayOffset() + 24, SAConstants.NODE_DESC_LENGTH));
        buffer.position(88);
        port.setNodeLid(buffer.getInt());
        port.setPortNumber(buffer.get());
        bean.setPort(port);
        return bean;
    }

}
