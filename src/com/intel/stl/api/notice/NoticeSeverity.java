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
 *  File Name: NoticeSeverity.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2014/12/11 18:32:35  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/10 20:32:43  rjtierne
 *  Archive Log:    Support for saving EventRules to UserSettings
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/06 15:14:03  jijunwan
 *  Archive Log:    notice and trap implementation
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/30 16:07:05  fernande
 *  Archive Log:    Changed to use in EventRule
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:22:31  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.notice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>Note</b>: We will use NoticeSeverity ordinal to compare severity level. We
 * must ensure that severity levels are listed at increase order!
 */
public enum NoticeSeverity {
    INFO(0),
    WARNING(1),
    ERROR(2),
    CRITICAL(3);

    private final static Map<String, NoticeSeverity> severityMap =
            new HashMap<String, NoticeSeverity>();
    static {
        for (NoticeSeverity severity : NoticeSeverity.values()) {
            severityMap.put(severity.name(), severity);
        }
    };

    private static Logger log = LoggerFactory.getLogger(NoticeSeverity.class);

    private final int id;

    private NoticeSeverity(int id) {
        this.id = id;
    }

    public static NoticeSeverity getNoticeSeverity(NoticeType type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case FATAL:
                return NoticeSeverity.CRITICAL;
            case URGENT:
                return NoticeSeverity.ERROR;
            case SECURITY:
                return NoticeSeverity.WARNING;
            case SM:
                return NoticeSeverity.WARNING;
            case INFO:
                return NoticeSeverity.INFO;
            default:
                log.warn("Unknown NotieType " + type);
                return null;
        }
    }

    public static NoticeSeverity getNoticeSeverity(byte type) {
        return getNoticeSeverity(NoticeType.getNoticeType(type));
    }

    public static NoticeSeverity getNoticeSeverity(int id) {

        NoticeSeverity severity = null;

        switch (id) {
            case 0:
                severity = INFO;
                break;

            case 1:
                severity = WARNING;
                break;

            case 2:
                severity = ERROR;
                break;

            case 3:
                severity = CRITICAL;
                break;
        }

        return severity;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    public static NoticeSeverity getNoticeSeverity(String severityName) {
        return severityMap.get(severityName);
    }
}
