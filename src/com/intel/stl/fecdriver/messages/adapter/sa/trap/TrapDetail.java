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
 *  File Name: TrapDetail.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/01/11 20:04:28  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/27 17:08:51  fernande
 *  Archive Log:    Database changes to add Notice and ImageInfo tables to the schema database
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/07 14:23:54  jijunwan
 *  Archive Log:    added TrapSysguid
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

import java.nio.ByteBuffer;

import com.intel.stl.api.notice.TrapCapabilityBean;
import com.intel.stl.api.notice.TrapKeyBean;
import com.intel.stl.api.notice.TrapLinkBean;
import com.intel.stl.api.notice.TrapMKeyBean;
import com.intel.stl.api.notice.TrapSwitchPKeyBean;
import com.intel.stl.api.notice.TrapSysguidBean;
import com.intel.stl.api.subnet.GIDBean;
import com.intel.stl.fecdriver.messages.adapter.sa.GID;

public class TrapDetail {
    /**
     * Description:
     * 
     * <pre>
     * ref:/ALL_EMB/IbAcess/Common/Inc/stl_sm.h v1.115
     * 
     * typedef struct {
     *     IB_GID      Gid;
     * } PACK_SUFFIX STL_TRAP_GID;
     * 
     * #define STL_TRAP_GID_NOW_IN_SERVICE_DATA STL_TRAP_GID
     * #define STL_TRAP_GID_OUT_OF_SERVICE_DATA STL_TRAP_GID
     * #define STL_TRAP_GID_ADD_MULTICAST_GROUP_DATA STL_TRAP_GID
     * #define STL_TRAP_GID_DEL_MULTICAST_GROUP_DATA STL_TRAP_GID
     * </pre>
     * 
     * @param data
     * @return
     */
    public static GIDBean getGID(byte[] data) {
        GID.Global gid = new GID.Global();
        gid.wrap(data, 0);
        return gid.toObject();
    }

    /**
     * Description:
     * 
     * <pre>
     * ref:/ALL_EMB/IbAcess/Common/Inc/stl_sm.h
     * 
     * typedef struct {
     *     uint32      Lid;
     * } PACK_SUFFIX STL_TRAP_PORT_CHANGE_STATE_DATA;
     * </pre>
     * 
     * @param data
     * @return
     */
    public static int getLid(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data, 0, 4);
        return buffer.getInt();
    }

    public static TrapLinkBean getTrapLink(byte[] data) {
        TrapLink link = new TrapLink();
        link.wrap(data, 0);
        return link.toObject();
    }

    public static TrapCapabilityBean getTrapCapability(byte[] data) {
        TrapCapability cap = new TrapCapability();
        cap.wrap(data, 0);
        return cap.toObject();
    }

    /**
     * Description:
     * 
     * <pre>
     * ref:/ALL_EMB/IbAcess/Common/Inc/stl_sm.h
     * 
     * typedef struct {
     *     uint64      SystemImageGuid;
     *     uint32      Lid;
     * } PACK_SUFFIX STL_TRAP_SYSGUID_CHANGE_DATA;
     * </pre>
     * 
     * @param data
     * @return
     */
    public static TrapSysguidBean getTrapSysguid(byte[] data) {
        TrapSysguid sysguid = new TrapSysguid();
        sysguid.wrap(data, 0);
        return sysguid.toObject();
    }

    public static TrapMKeyBean getTrapMKey(byte[] data) {
        TrapMKey mKey = new TrapMKey();
        mKey.wrap(data, 0);
        return mKey.toObject();
    }

    public static TrapKeyBean getTrapKey(byte[] data) {
        TrapKey key = new TrapKey();
        key.wrap(data, 0);
        return key.toObject();
    }

    public static TrapSwitchPKeyBean getTrapSwitchPKey(byte[] data) {
        TrapSwitchPKey key = new TrapSwitchPKey();
        key.wrap(data, 0);
        return key.toObject();
    }

    /**
     * 
     * Description:
     * 
     * <pre>
     * ref:/ALL_EMB/IbAcess/Common/Inc/stl_sm.h
     * 
     * LinkWidth of at least one port of switch at <ReportingLID> has changed 
     * typedef struct {
     *     uint32  ReportingLID;           
     * } PACK_SUFFIX STL_SMA_TRAP_DATA_LINK_WIDTH;
     * </pre>
     * 
     * @param data
     * @return
     */
    public static int getReportingLid(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data, 0, 4);
        return buffer.getInt();
    }
}
