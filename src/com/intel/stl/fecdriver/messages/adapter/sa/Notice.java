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
 *  File Name: NoticeAttr.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/01/11 20:04:29  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/22 21:38:47  fernande
 *  Archive Log:    Adding support for saving notices and imageinfos to the database
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/06 15:14:05  jijunwan
 *  Archive Log:    notice and trap implementation
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/16 15:07:32  jijunwan
 *  Archive Log:    added support to FE notice
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.adapter.sa;

import java.nio.ByteBuffer;

import com.intel.stl.api.notice.NoticeBean;
import com.intel.stl.fecdriver.messages.adapter.ComposedDatagram;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_mad.h v1.19
 * 
 * <pre>
 * Notice 
 * 
 * All STL fabrics should use the STL Notice structure when communicating with
 * STL devices and applications. When forwarding notices to IB applications,
 * the SM shall translate them into IB format, when IB equivalents exist.
 * 
 * STL Differences:
 *      IssuerLID is now 32 bits.
 *      Moved fields to maintain word alignment.
 *      Data and ClassTrapSpecificData combined into a single field.
 * 
 * typedef struct {
 *  union {
 *      // Generic Notice attributes 
 *      struct //_GENERIC {
 *          STL_FIELDUNION3(u, 32,
 *              IsGeneric:1,            // RO
 *              Type:7,                 // RO
 *              ProducerType:24);       // RO
 *          uint16  TrapNumber;         // RO
 *      } PACK_SUFFIX Generic;
 * 
 *      // Vendor specific Notice attributes 
 *      struct //_VENDOR {
 *          STL_FIELDUNION3(u, 32,
 *              IsGeneric:1,            // RO
 *              Type:7,                 // RO
 *              VendorID:24);           // RO
 *          uint16  DeviceID;           // RO
 *      } PACK_SUFFIX Vendor;
 *  } PACK_SUFFIX Attributes;
 * 
 *  STL_FIELDUNION2(Stats, 16, 
 *              Toggle:1,               // RW
 *              Count:15);              // RW
 * 
 *  // 8 bytes 
 *  uint32      IssuerLID;              // RO: Extended for STL
 *  uint32      Reserved2;              // Added for qword alignment
 *  // 16 bytes 
 *  IB_GID      IssuerGID;              // RO
 *  // 32 bytes 
 *  uint8       Data[64];               // RO. 
 *  // 96 bytes 
 *  uint8       ClassData[0];           // RO. Variable length.
 * } PACK_SUFFIX STL_NOTICE;
 * </pre>
 */
public class Notice extends ComposedDatagram<NoticeBean> {
    private final SimpleDatagram<Void> header;

    private SimpleDatagram<Void> classData;

    public Notice() {
        header = new SimpleDatagram<Void>(96);
        addDatagram(header);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.ComposedDatagram#wrap(byte[],
     * int)
     */
    @Override
    public int wrap(byte[] data, int offset) {
        int pos = header.wrap(data, offset);
        int size = data.length - offset;
        classData = new SimpleDatagram<Void>(size - header.getLength());
        pos = classData.wrap(data, pos);
        addDatagram(classData);
        return pos;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.ComposedDatagram#toObject()
     */
    @Override
    public NoticeBean toObject() {
        NoticeBean bean = new NoticeBean(true);

        ByteBuffer buffer = header.getByteBuffer();
        buffer.clear();

        int intVal = buffer.getInt();
        boolean isGeneric = (intVal & 0x80000000) == 0x80000000;
        if (isGeneric) {
            NoticeAttr.Generic attr = new NoticeAttr.Generic();
            attr.wrap(buffer.array(), buffer.arrayOffset());
            bean.setAttributes(attr.toObject());
        } else {
            NoticeAttr.Vendor attr = new NoticeAttr.Vendor();
            attr.wrap(buffer.array(), buffer.arrayOffset());
            bean.setAttributes(attr.toObject());
        }
        buffer.position(6);
        short shortVal = buffer.getShort();
        bean.setToggle((shortVal & 0x8000) == 0x8000);
        bean.setNoticeCount((short) (shortVal & 0x7fff));
        bean.setIssuerLID(buffer.getInt());
        GID.Global gid = new GID.Global();
        gid.wrap(buffer.array(), buffer.arrayOffset() + 16);
        bean.setIssuerGID(gid.toObject());
        buffer.position(32);
        byte[] byteArray = new byte[64];
        buffer.get(byteArray);
        bean.setData(byteArray);

        buffer = classData.getByteBuffer();
        buffer.clear();
        byteArray = new byte[classData.getLength()];
        buffer.get(byteArray);
        bean.setClassData(byteArray);
        return bean;
    }

}
