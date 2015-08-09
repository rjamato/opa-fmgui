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
 *  File Name: LoggingThreshold.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/12/19 17:49:57  rjtierne
 *  Archive Log:    Changed order of enumerations and added id's
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/15 20:43:57  fernande
 *  Archive Log:    Initial changes to the logging configuration backend support for logback.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

public enum LoggingThreshold {

    OFF(0),
    INFO(1),
    DEBUG(2),
    WARN(3),
    ERROR(4),
    FATAL(5),
    ALL(6);

    private final int id;

    private LoggingThreshold(int id) {

        this.id = id;
    }

    public int getId() {
        return id;
    }
}
