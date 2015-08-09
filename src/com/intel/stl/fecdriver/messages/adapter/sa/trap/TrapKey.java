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
 *  File Name: TrapKey.java
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

import com.intel.stl.api.notice.TrapKeyBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;
import com.intel.stl.fecdriver.messages.adapter.sa.GID;

/**
 * <pre>
 * ref:/ALL_EMB/IbAcess/Common/Inc/stl_sm.h v1.115
 * 
 * typedef struct {
 *     uint32      Lid1;
 *     uint32      Lid2;
 *     //  8 bytes
 *     uint32      Key;    // pkey or qkey
 *     STL_FIELDUNION2(u,8,
 *                 SL:5,
 *                 Reserved:3);
 *     uint8       Reserved[3];
 *     //  16 bytes 
 *     IB_GID      Gid1;
 *     //  32 bytes 
 *     IB_GID      Gid2;
 *     //  48 bytes 
 *     STL_FIELDUNION2(qp1,32,
 *                 Reserved:8,
 *                 qp:24);
 *     //  52 bytes 
 *     STL_FIELDUNION2(qp2,32,
 *                 Reserved:8,
 *                 qp:24);
 *     //  56 bytes 
 * } PACK_SUFFIX STL_TRAP_BAD_KEY_DATA;
 *     
 * #define STL_TRAP_BAD_P_KEY_DATA STL_TRAP_BAD_KEY_DATA
 * #define STL_TRAP_BAD_Q_KEY_DATA STL_TRAP_BAD_KEY_DATA
 * </pre>
 */
public class TrapKey extends SimpleDatagram<TrapKeyBean> {

    public TrapKey() {
        super(56);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.fecdriver.messages.adapter.SimpleDatagram#toObject()
     */
    @Override
    public TrapKeyBean toObject() {
        buffer.clear();
        TrapKeyBean bean = new TrapKeyBean();
        bean.setLid1(buffer.getInt());
        bean.setLid2(buffer.getInt());
        bean.setKey(buffer.getInt());
        byte byteVal = buffer.get();
        bean.setSl((byte) ((byteVal >>> 3) & 0x1f));
        GID.Global gid1 = new GID.Global();
        gid1.wrap(buffer.array(), buffer.arrayOffset() + 16);
        bean.setGid1(gid1.toObject());
        GID.Global gid2 = new GID.Global();
        gid2.wrap(buffer.array(), buffer.arrayOffset() + 32);
        bean.setGid1(gid2.toObject());
        buffer.position(48);
        int intVal = buffer.getInt();
        bean.setQp1(intVal & 0xfff);
        intVal = buffer.getInt();
        bean.setQp2(intVal & 0xfff);
        return bean;
    }

}
