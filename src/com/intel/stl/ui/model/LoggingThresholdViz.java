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
 *  File Name: LoggingThresholdViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/12/19 18:31:45  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Visual enumeration for the threshold combo box on the Event
 *  Wizard view
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.intel.stl.api.configuration.LoggingThreshold;
import com.intel.stl.ui.common.STLConstants;

public enum LoggingThresholdViz {

    OFF(LoggingThreshold.OFF, STLConstants.K0699_OFF.getValue()),
    INFO(LoggingThreshold.INFO, STLConstants.K0631_INFO.getValue()),
    DEBUG(LoggingThreshold.DEBUG, STLConstants.K0630_DEBUG.getValue()),
    WARN(LoggingThreshold.WARN, STLConstants.K3002_WARN.getValue()),
    ERROR(LoggingThreshold.ERROR, STLConstants.K0030_ERROR.getValue()),
    FATAL(LoggingThreshold.FATAL, STLConstants.K0632_FATAL.getValue()),
    ALL(LoggingThreshold.ALL, STLConstants.K0698_ALL.getValue());

    private final static EnumMap<LoggingThreshold, String> loggingThresholdNameMap =
            new EnumMap<LoggingThreshold, String>(LoggingThreshold.class);
    static {
        for (LoggingThresholdViz thresholdViz : LoggingThresholdViz.values()) {
            loggingThresholdNameMap.put(thresholdViz.threshold,
                    thresholdViz.name);
        }
    };

    private final static Map<String, LoggingThreshold> loggingThresholdMap =
            new HashMap<String, LoggingThreshold>();
    static {
        for (LoggingThresholdViz thresholdViz : LoggingThresholdViz.values()) {
            loggingThresholdMap.put(thresholdViz.getName(),
                    thresholdViz.getThreshold());
        }
    }

    private final LoggingThreshold threshold;

    private final String name;

    private final int id;

    private LoggingThresholdViz(LoggingThreshold threshold, String name) {

        this.name = name;
        this.threshold = threshold;
        this.id = threshold.getId();
    }

    public LoggingThreshold getThreshold() {
        return threshold;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static String getLoggingThresholdName(LoggingThreshold threshold) {
        return loggingThresholdNameMap.get(threshold);
    }

    public static LoggingThreshold getLoggingThreshold(String name) {
        return loggingThresholdMap.get(name);
    }

}
