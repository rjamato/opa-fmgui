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

import com.intel.stl.api.notice.GenericNoticeAttrBean;
import com.intel.stl.api.notice.NoticeAttrBean;
import com.intel.stl.api.notice.VendorNoticeAttrBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_mad.h v1.19
 * 
 * <pre>
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
 * </pre>
 */
public abstract class NoticeAttr<E extends NoticeAttrBean> extends
        SimpleDatagram<E> {

    public NoticeAttr() {
        super(6);
    }

    public boolean isGeneric() {
        int intVal = buffer.getInt(0);
        return (intVal & 0x80000000) == 0x80000000;
    }

    public static class Generic extends NoticeAttr<GenericNoticeAttrBean> {
        /*
         * (non-Javadoc)
         * 
         * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
         */
        @Override
        public GenericNoticeAttrBean toObject() {
            GenericNoticeAttrBean bean = new GenericNoticeAttrBean();
            buffer.clear();
            int intVal = buffer.getInt();
            bean.setGeneric((intVal & 0x80000000) == 0x80000000);
            bean.setType((byte) ((intVal >>> 24) & 0x7f));
            bean.setProducerType(intVal & 0xffffff);
            bean.setTrapNumber(buffer.getShort());
            return bean;
        }
    }

    public static class Vendor extends NoticeAttr<VendorNoticeAttrBean> {
        /*
         * (non-Javadoc)
         * 
         * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
         */
        @Override
        public VendorNoticeAttrBean toObject() {
            VendorNoticeAttrBean bean = new VendorNoticeAttrBean();
            buffer.clear();
            int intVal = buffer.getInt(0);
            bean.setGeneric((intVal & 0x80000000) == 0x80000000);
            bean.setType((byte) ((intVal >>> 24) & 0x7f));
            bean.setVendorID(intVal & 0xffffff);
            bean.setDeviceID(buffer.getShort());
            return bean;
        }
    }

}
