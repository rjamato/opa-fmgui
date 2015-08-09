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
 *  File Name: PortStateViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/01/11 21:36:23  jijunwan
 *  Archive Log:    adapt to latest data structure changes on FM
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/18 21:31:22  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/29 13:48:52  fernande
 *  Archive Log:    Removed repetitive conversion from FE values to API enums
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/22 21:52:34  fernande
 *  Archive Log:    Refactoring PropertyStrings into individual enums
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import static com.intel.stl.ui.common.STLConstants.K0368_NO_STATE_CHANGE;
import static com.intel.stl.ui.common.STLConstants.K0369_DOWN;
import static com.intel.stl.ui.common.STLConstants.K0370_INIT;
import static com.intel.stl.ui.common.STLConstants.K0371_ARMED;
import static com.intel.stl.ui.common.STLConstants.K0372_ACTIVE;

import java.util.EnumMap;

import com.intel.stl.api.configuration.PortState;

public enum PortStateViz {
    NO_ST_CHANGE(PortState.NO_ST_CHANGE, K0368_NO_STATE_CHANGE.getValue()),
    DOWN(PortState.DOWN, K0369_DOWN.getValue()),
    INITIALIZE(PortState.INITIALIZE, K0370_INIT.getValue()),
    ARMED(PortState.ARMED, K0371_ARMED.getValue()),
    ACTIVE(PortState.ACTIVE, K0372_ACTIVE.getValue());

    private final static EnumMap<PortState, String> portStateMap =
            new EnumMap<PortState, String>(PortState.class);
    static {
        for (PortStateViz psv : PortStateViz.values()) {
            portStateMap.put(psv.state, psv.value);
        }
    };

    private final PortState state;

    private final String value;

    private PortStateViz(PortState state, String value) {
        this.state = state;
        this.value = value;
    }

    public PortState getPortState() {
        return state;
    }

    public String getValue() {
        return value;
    }

    public static PortStateViz getPortStateViz(byte state) {
        for (PortStateViz psv : PortStateViz.values()) {
            if (psv.state.getId() == state) {
                return psv;
            }
        }
        return null;
    }

    public static String getPortStateStr(PortState state) {
        return portStateMap.get(state);
    }
}
