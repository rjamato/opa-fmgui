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
 *  File Name: ProducerType.java
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
 * #define NODE_TYPE_ALL 0xffffff
 * 
 * #define NOTICE_PRODUCERTYPE_CA           1
 * #define NOTICE_PRODUCERTYPE_SWITCH       2
 * #define NOTICE_PRODUCERTYPE_ROUTER       3
 * #define NOTICE_PRODUCERTYPE_CLASSMANAGER 4
 * 
 * ref:/ALL_EMB/Esm/ib/src/smi/sa/sa_InformInfo.c
 * static char * getNodeType(uint32_t nodeType)
 * {
 *     switch (nodeType) {
 *     case NOTICE_PRODUCERTYPE_CA:
 *         return "Channel Adapter";
 *     case NOTICE_PRODUCERTYPE_SWITCH:
 *         return "Switch";
 *     case NOTICE_PRODUCERTYPE_ROUTER:
 *         return "Router";
 *     case NOTICE_PRODUCERTYPE_CLASSMANAGER:
 *         return "Subnet Management";
 *     case NODE_TYPE_ALL:
 *         return "All producer types";
 *     default:
 *         return NULL;
 *     }
 * }
 * </pre>
 */
public enum ProducerType {
    CA(1),
    SWITCH(2),
    ROUTER(3),
    SM(4),
    ALL(0xffffff);

    private static Logger log = LoggerFactory.getLogger(ProducerType.class);
    private final int id;

    private ProducerType(int id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    
    public static ProducerType getProducerType(int id) {
        for (ProducerType type : ProducerType.values()) {
            if (type.getId()==id) {
                return type;
            }
        }
        log.warn("Unknown ProducerType id "+id);
        return null;
    }
}
