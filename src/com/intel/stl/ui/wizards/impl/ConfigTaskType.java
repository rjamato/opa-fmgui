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
 *  File Name: ConfigurationTaskType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/31 17:46:48  rjtierne
 *  Archive Log:    Changed K3022_HOST_REACHABILITY to K3022_HOST_CONNECTIVITY
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/11 15:23:49  rjtierne
 *  Archive Log:    Multinet Wizard: Initial Version
 *  Archive Log:
 *
 *  Overview: Enumeration for the various tasks carried out upon
 *  completion of subnet/event/preferences configuration in the
 *  setup wizard
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.impl;

import com.intel.stl.ui.common.STLConstants;

public enum ConfigTaskType {

    CHECK_HOST(0, STLConstants.K3022_HOST_CONNECTIVITY.getValue()),
    VALIDATE_ENTRY(1, STLConstants.K3023_ENTRY_VALIDATION.getValue()),
    UPDATE_DATABASE(2, STLConstants.K3024_DATABASE_UPDATE.getValue()),
    CONFIGURATION_COMPLETE(3, STLConstants.K3025_CONFIGURATION_COMPLETE
            .getValue());

    int id;

    String name;

    private ConfigTaskType(int id, String name) {

        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
