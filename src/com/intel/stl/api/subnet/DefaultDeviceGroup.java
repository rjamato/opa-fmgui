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
 *  File Name: DefaultDeviceGroup.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/16 14:31:42  jijunwan
 *  Archive Log:    renamed DevieGroup to DefaultDeviceGroup because it's an enum of default DGs, plus we need to use DeviceGroup for the DG definition used in opafm.xml
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/15 18:20:23  rjtierne
 *  Archive Log:    Added map to enum
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:24:32  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.subnet;

import java.util.HashMap;
import java.util.Map;

public enum DefaultDeviceGroup {
    ALL(0, "All"),
    HFI(1, "HFIs"),
    SW(2, "SWs"),
    TFI(3, "TFIs");

    private final String name;

    private final int id;

    private static final Map<String, DefaultDeviceGroup> map =
            new HashMap<String, DefaultDeviceGroup>() {

                /**
                 * Serial Version UID
                 */
                private static final long serialVersionUID =
                        -2334542879744460474L;

                {
                    for (DefaultDeviceGroup type : DefaultDeviceGroup.values()) {
                        put(type.name, type);
                    }
                }
            };

    private DefaultDeviceGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public static DefaultDeviceGroup getType(String name) {
        return map.get(name);
    }

    public int getId() {
        return this.id;
    }
}
