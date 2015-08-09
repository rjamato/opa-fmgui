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
 *  File Name: PhysicalStateViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/01/11 21:36:24  jijunwan
 *  Archive Log:    adapt to latest data structure changes on FM
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/18 21:31:22  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/29 13:48:52  fernande
 *  Archive Log:    Removed repetitive conversion from FE values to API enums
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/22 21:53:26  fernande
 *  Archive Log:    Refactoring PropertyStrings into enums
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import static com.intel.stl.ui.common.STLConstants.K0368_NO_STATE_CHANGE;
import static com.intel.stl.ui.common.STLConstants.K0374_SLEEP;
import static com.intel.stl.ui.common.STLConstants.K0375_POLLING;
import static com.intel.stl.ui.common.STLConstants.K0376_DISABLED;
import static com.intel.stl.ui.common.STLConstants.K0377_CONFIG_TRAIN;
import static com.intel.stl.ui.common.STLConstants.K0378_LINKUP;
import static com.intel.stl.ui.common.STLConstants.K0379_LINK_ERR_RECOV;
import static com.intel.stl.ui.common.STLConstants.K0381_OFFLINE;
import static com.intel.stl.ui.common.STLConstants.K0382_TEST;

import java.util.EnumMap;

import com.intel.stl.api.configuration.PhysicalState;

public enum PhysicalStateViz {

    NO_ST_CHANGE(PhysicalState.NO_ST_CHANGE, K0368_NO_STATE_CHANGE.getValue()),
    SLEEP(PhysicalState.SLEEP, K0374_SLEEP.getValue()),
    POLLING(PhysicalState.POLLING, K0375_POLLING.getValue()),
    DISABLED(PhysicalState.DISABLED, K0376_DISABLED.getValue()),
    PORT_CONFIG_TRAINING(PhysicalState.PORT_CONFIG_TRAINING, K0377_CONFIG_TRAIN
            .getValue()),
    LINKUP(PhysicalState.LINKUP, K0378_LINKUP.getValue()),
    LINK_ERROR_RECOVERY(PhysicalState.LINK_ERROR_RECOVERY, K0379_LINK_ERR_RECOV
            .getValue()),
    OFFLINE(PhysicalState.OFFLINE, K0381_OFFLINE.getValue()),
    TEST(PhysicalState.TEST, K0382_TEST.getValue());

    private final static EnumMap<PhysicalState, String> physStateMap =
            new EnumMap<PhysicalState, String>(PhysicalState.class);
    static {
        for (PhysicalStateViz psv : PhysicalStateViz.values()) {
            physStateMap.put(psv.state, psv.value);
        }
    };

    private final PhysicalState state;

    private final String value;

    private PhysicalStateViz(PhysicalState state, String value) {
        this.state = state;
        this.value = value;
    }

    public PhysicalState getPhysicalState() {
        return state;
    }

    public String getValue() {
        return value;
    }

    public static PhysicalStateViz getPhysicalStateViz(byte state) {
        for (PhysicalStateViz psv : PhysicalStateViz.values()) {
            if (psv.state.getId() == state) {
                return psv;
            }
        }
        return null;
    }

    public static String getPhysicalStateStr(PhysicalState state) {
        return physStateMap.get(state);
    }
}
