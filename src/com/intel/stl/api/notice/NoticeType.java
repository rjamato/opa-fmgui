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
 *  File Name: NoticeType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/12/11 18:32:35  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 15:14:03  jijunwan
 *  Archive Log:    notice and trap implementation
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.notice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * ref:/ALL_EMB/Esm/ib/include/ib_mad.h
 * #define  TRAP_ALL    0xffff
 *  
 * #define  NOTICE_TYPE_FATAL       0
 * #define  NOTICE_TYPE_URGENT      1
 * #define  NOTICE_TYPE_SECURITY    2
 * #define  NOTICE_TYPE_SM          3
 * #define  NOTICE_TYPE_INFO        4
 * #define  NOTICE_TYPE_EMPTY       0x7f
 * 
 * ref:/ALL_EMB/Esm/ib/src/smi/sa/sa_InformInfo.c
 * static char * getType(uint16_t type)
 * {
 *  switch (type) {
 *  case NOTICE_TYPE_FATAL:
 *      return "Fatal";
 *  case NOTICE_TYPE_URGENT:
 *      return "Urgent";
 *  case NOTICE_TYPE_SECURITY:
 *      return "Security";
 *  case NOTICE_TYPE_SM:
 *      return "Subnet Management";
 *  case NOTICE_TYPE_INFO:
 *      return "Informational";
 *  case TRAP_ALL:
 *      return "All Types";
 *  default:
 *      return NULL;
 *  }
 * }
 * </pre>
 * We will ignore TRAP_ALL because notice type is only 7 bits, it doesn't
 * make sense here.
 */
public enum NoticeType {
    FATAL((byte)0),
    URGENT((byte)1),
    SECURITY((byte)2),
    SM((byte)3),
    INFO((byte)4),
    EMPTY((byte)0x7f);

    private static Logger log = LoggerFactory.getLogger(NoticeType.class);
    private final byte id;

    private NoticeType(byte id) {
        this.id = id;
    }
    
    /**
     * @return the id
     */
    public byte getId() {
        return id;
    }


    public static NoticeType getNoticeType(byte id) {
        for (NoticeType type : NoticeType.values()) {
            if (type.getId()==id) {
                return type;
            }
        }
        log.warn("Unknown NoticeType id "+id);
        return null;
    }
}
