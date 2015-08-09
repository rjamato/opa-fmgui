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
 *  File Name: TrapCapability.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/01/11 20:04:28  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 15:14:04  jijunwan
 *  Archive Log:    notice and trap implementation
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.adapter.sa.trap;

import com.intel.stl.api.notice.TrapCapabilityBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * <pre>
 *  ref:/ALL_EMB/IbAcess/Common/Inc/stl_sm.h v1.115
 *  
 *   typedef struct {
 * 4     uint32              Lid;
 * 8     uint32              CapabilityMask;
 * 10    uint16              CapabilityMask2;
 * 12    uint16              CapabilityMask3;
 * 14    STL_FIELDUNION4(u,16,
 *                          Reserved:13,
 *                          LinkSpeedEnabledChange:1,
 *                          LinkWidthEnabledChange:1,
 *                          NodeDescriptionChange:1);
 *  } PACK_SUFFIX STL_TRAP_CHANGE_CAPABILITY_DATA;
 * </pre>
 * 
 */
public class TrapCapability extends SimpleDatagram<TrapCapabilityBean> {
    public TrapCapability() {
        super(14);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.fecdriver.messages.adapter.SimpleDatagram#toObject()
     */
    @Override
    public TrapCapabilityBean toObject() {
        buffer.clear();
        TrapCapabilityBean bean = new TrapCapabilityBean();
        bean.setLid(buffer.getInt());
        bean.setCapabilityMask(buffer.getInt());
        bean.setCapabilityMask2(buffer.getShort());
        bean.setCapabilityMask3(buffer.getShort());
        short val = buffer.getShort();
        bean.setLinkSpeedEnabledChange((val & 0x04) == 0x04);
        bean.setLinkWidthEnabledChange((val & 0x02) == 0x02);
        bean.setNodeDescriptionChange((val & 0x01) == 0x01);
        return bean;
    }

}
