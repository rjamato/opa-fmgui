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
package com.intel.stl.fecdriver;

import java.io.IOException;
import java.util.Properties;

import com.intel.stl.api.subnet.SubnetDescription;

public interface FEResourceAdapter<E extends IConnection> {
    public static final String HOST = "Host";

    public static final String PORT = "Port";

    public static final String USER = "user";

    public static final String PASSWORD = "password";

    /**
     * 
     * @return Version of the FE protocol header see STL_Vol2 Section
     *         3.2.5.2.3.1
     */
    int getProtocolVersion();

    /**
     * Retrieves the driver's version
     * 
     * @return version string
     */
    String getVersion();

    /**
     * Sets the maximum time in seconds that this resource adapter will wait
     * while attempting to connect to a FE.
     * 
     * @param seconds
     */
    void setLoginTimeout(int seconds);

    /**
     * Gets the maximum time in seconds that this resource adapter will wait
     * while attempting to connect to a FE.
     * 
     * @return
     */
    int getLoginTimeout();

    E connect(SubnetDescription subnet, Properties info) throws IOException;

    E connect(SubnetDescription subnet) throws IOException;
}
