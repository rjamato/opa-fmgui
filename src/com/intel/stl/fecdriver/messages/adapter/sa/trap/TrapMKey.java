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
 *  File Name: TrapMKey.java
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

import com.intel.stl.api.notice.TrapMKeyBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * <pre>
 * ref:/ALL_EMB/IbAcess/Common/Inc/stl_sm.h v1.115
 * 
 * typedef struct {
 *     uint32      Lid;
 *     uint32      DRSLid;
 *     //  8 bytes
 *     uint8       Method;
 *     STL_FIELDUNION3(u,8,
 *                 DRNotice:1,
 *                 DRPathTruncated:1,
 *                 DRHopCount:6);
 *     uint16      AttributeID;
 *     //  12 bytes 
 *     uint32      AttributeModifier;
 *     //  16 bytes
 *     uint64      MKey;
 *     //  24 bytes
 *     uint8       DRReturnPath[30]; // We can make this longer....
 *     //  54 bytes
 * } PACK_SUFFIX STL_TRAP_BAD_M_KEY_DATA;
 * </pre>
 */
public class TrapMKey extends SimpleDatagram<TrapMKeyBean> {
    public TrapMKey() {
        super(54);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.fecdriver.messages.adapter.SimpleDatagram#toObject()
     */
    @Override
    public TrapMKeyBean toObject() {
        buffer.clear();
        TrapMKeyBean bean = new TrapMKeyBean();
        bean.setLid(buffer.getInt());
        bean.setDrSLid(buffer.getInt());
        bean.setMethod(buffer.get());
        byte byteVal = buffer.get();
        bean.setDrNotice((byteVal & 0x80) == 0x80);
        bean.setDrPathTruncated((byteVal & 0x40) == 0x40);
        bean.setDrHopCount((byte) (byteVal & 0x3f));
        bean.setAttributeID(buffer.getShort());
        bean.setAttributeModifier(buffer.getInt());
        bean.setMKey(buffer.getLong());
        byte[] byteArray = new byte[30];
        buffer.get(byteArray);
        bean.setDrReturnPath(byteArray);
        return bean;
    }

}
