/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: UserPreference.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/04/02 17:03:59  jypak
 *  Archive Log:    Null checks added.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/23 15:05:36  rjtierne
 *  Archive Log:    Changed getRefreshRateUnit() to change the property string to upper
 *  Archive Log:    case before getting the TimeUnit value
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/20 21:12:14  rjtierne
 *  Archive Log:    Fixed typo
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/10 23:11:55  jijunwan
 *  Archive Log:    1) changed task scheduler to support initial refresh rate
 *  Archive Log:    2) improved refresh rate update handling
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.intel.stl.api.configuration.UserSettings;

/**
 * Helper class that wraps Properties and provides easy access to user
 * preferences. In the future when we change backend as a general way to store
 * user data, and it's frontend's responsibility to generate and parser data,
 * this class can be the main class to do the job.
 */
public class UserPreference {
    private final Properties properties;

    /**
     * Description:
     * 
     * @param properties
     */
    public UserPreference(Properties properties) {
        super();
        this.properties = properties;
    }

    public int getRefreshRate() {
        return getInt(UserSettings.PROPERTY_REFRESH_RATE);
    }

    public TimeUnit getRefreshRateUnit() {
        String name = UserSettings.PROPERTY_REFRESH_RATE_UNITS;
        String str = null;
        if (properties != null) {
            str = properties.getProperty(name);
        }
        if (str != null) {
            return TimeUnit.valueOf(str.toUpperCase());
        } else {
            throw new IllegalArgumentException("Couldn't find property for '"
                    + name + "'");
        }
    }

    public int getRefreshRateInSeconds() {
        int rate = getRefreshRate();
        TimeUnit unit = getRefreshRateUnit();
        return (int) TimeUnit.SECONDS.convert(rate, unit);
    }

    public int getTimeWindowInSeconds() {
        return getInt(UserSettings.PROPERTY_TIMING_WINDOW);
    }

    public int getNumWorstNodes() {
        return getInt(UserSettings.PROPERTY_NUM_WORST_NODES);
    }

    private int getInt(String name) {
        String str = null;
        if (properties != null) {
            str = properties.getProperty(name);
        }
        if (str != null) {
            return Integer.valueOf(str);
        } else {
            throw new IllegalArgumentException("Counldn't find property for '"
                    + name + "'");
        }
    }
}
