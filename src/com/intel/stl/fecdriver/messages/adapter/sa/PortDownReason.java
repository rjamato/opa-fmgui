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
 *  File Name: PortDownReason.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/02/06 00:26:59  jijunwan
 *  Archive Log:    added neighbor link down reason to match FM 325
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/11 20:04:29  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/09 18:36:45  jijunwan
 *  Archive Log:    updated PortInfoRecord, SMInfo to the latest data structure
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.adapter.sa;

import com.intel.stl.api.subnet.PortDownReasonBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref /ALL_EMB/IbAcess/Common/Inc/stl_sa.h v1.92
 * 
 * <pre>
 * typedef struct {
 *     uint8 Reserved[6];
 *     uint8 NeighborLinkDownReason;
 *     uint8 LinkDownReason;
 *     uint64 Timestamp;
 * } PACK_SUFFIX STL_LINKDOWN_REASON;
 * </pre>
 */
public class PortDownReason extends SimpleDatagram<PortDownReasonBean> {

    /**
     * Description:
     * 
     * @param length
     */
    public PortDownReason() {
        super(16);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.fecdriver.messages.adapter.SimpleDatagram#toObject()
     */
    @Override
    public PortDownReasonBean toObject() {
        buffer.clear();
        buffer.position(6);
        PortDownReasonBean bean =
                new PortDownReasonBean(buffer.get(), buffer.get(),
                        buffer.getLong());
        return bean;
    }

}
