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
 *  File Name: TrapSwitchPKey.java
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

import com.intel.stl.api.notice.TrapSwitchPKeyBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;
import com.intel.stl.fecdriver.messages.adapter.sa.GID;

/**
 * <pre>
 * ref:/ALL_EMB/IbAcess/Common/Inc/stl_sm.h v1.115
 * 
 * typedef struct {
 *     STL_FIELDUNION9(u,16,
 *                 Lid1:1, Lid2:1, PKey:1, SL:1,
 *                 QP1:1, QP2:1, Gid1:1, Gid2:1,
 *                 Reserved:8);
 *     uint16      PKey;
 *     //  4 bytes 
 *     uint32      Lid1;
 *     uint32      Lid2;
 *     STL_FIELDUNION2(u2,8,
 *                 SL:5,
 *                 Reserved:3);
 *     uint8       Reserved[3];
 *     //  16 bytes 
 *     IB_GID      Gid1;
 *     //  32 bytes 
 *     IB_GID      Gid2;
 *     //  48 bytes 
 *     STL_FIELDUNION2(qp1,32,
 *                 qp:24,
 *                 Reserved:8);
 *     //  52 bytes 
 *     STL_FIELDUNION2(qp2,32,
 *                 qp:24,
 *                 Reserved:8);
 *     //  56 bytes 
 * } PACK_SUFFIX STL_TRAP_SWITCH_BAD_PKEY_DATA;
 * </pre>
 */
public class TrapSwitchPKey extends SimpleDatagram<TrapSwitchPKeyBean> {

    public TrapSwitchPKey() {
        super(56);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.fecdriver.messages.adapter.SimpleDatagram#toObject()
     */
    @Override
    public TrapSwitchPKeyBean toObject() {
        buffer.clear();
        TrapSwitchPKeyBean bean = new TrapSwitchPKeyBean();
        short shortVal = buffer.getShort();
        bean.setHasLid1((shortVal & 0x8000) == 0x8000);
        bean.setHasLid2((shortVal & 0x4000) == 0x4000);
        bean.setHasPKey((shortVal & 0x2000) == 0x2000);
        bean.setHasSL((shortVal & 0x1000) == 0x1000);
        bean.setHasQP1((shortVal & 0x0800) == 0x0800);
        bean.setHasQP2((shortVal & 0x0400) == 0x0400);
        bean.setHasGid1((shortVal & 0x0200) == 0x0200);
        bean.setHasGid2((shortVal & 0x0100) == 0x0100);
        bean.setpKey(buffer.getShort());
        bean.setLid1(buffer.getInt());
        bean.setLid2(buffer.getInt());
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
