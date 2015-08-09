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
 *  File Name: PreferencesValidatorError.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/04/02 13:33:09  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/20 19:12:32  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Enumeration to serve as error codes for the User Preferences 
 *  Input Validator
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.impl.preferences;

import java.util.HashMap;

import com.intel.stl.ui.common.UILabels;

public enum PreferencesValidatorError {

    // Refresh Rate
    REFRESH_RATE_MISSING(0, UILabels.STL50064_REFRESH_RATE_MISSING),
    REFRESH_RATE_INVALID_TYPE(1, UILabels.STL50065_REFRESH_RATE_INVALID_TYPE),
    REFRESH_RATE_OUT_OF_RANGE(2, UILabels.STL50066_REFRESH_RATE_OUT_OF_RANGE),
    REFRESH_RATE_THRESHOLD_ERROR(3,
            UILabels.STL50067_REFRESH_RATE_THRESHOLD_ERROR),
    REFRESH_RATE_FORMAT_EXCEPTION(4,
            UILabels.STL50068_REFRESH_RATE_FORMAT_EXCEPTION),

    // Refresh Rate Units
    REFRESH_RATE_UNITS_MISSING(5, UILabels.STL50069_REFRESH_RATE_UNITS_MISSING),
    REFRESH_RATE_UNITS_INVALID_TYPE(6,
            UILabels.STL50070_REFRESH_RATE_UNITS_INVALID_TYPE),
    REFRESH_RATE_UNITS_OUT_OF_RANGE(7,
            UILabels.STL50071_REFRESH_RATE_UNITS_OUT_OF_RANGE),
    REFRESH_RATE_UNITS_FORMAT_EXCEPTION(8,
            UILabels.STL50072_REFRESH_RATE_UNITS_FORMAT_EXCEPTION),

    // Timing Window
    TIMING_WINDOW_MISSING(9, UILabels.STL50073_TIMING_WINDOW_MISSING),
    TIMING_WINDOW_INVALID_TYPE(10, UILabels.STL50074_TIMING_WINDOW_INVALID_TYPE),
    TIMING_WINDOW_OUT_OF_RANGE(11, UILabels.STL50075_TIMING_WINDOW_OUT_OF_RANGE),
    TIMING_WINDOW_FORMAT_EXCEPTION(12,
            UILabels.STL50076_TIMING_WINDOW_FORMAT_EXCEPTION),

    // # Worst Nodes
    NUM_WORST_NODES_MISSING(13, UILabels.STL50077_NUM_WORST_NODES_MISSING),
    NUM_WORST_NODES_INVALID_TYPE(14,
            UILabels.STL50078_NUM_WORST_NODES_INVALID_TYPE),
    NUM_WORST_NODES_OUT_OF_RANGE(15,
            UILabels.STL50079_NUM_WORST_NODES_OUT_OF_RANGE),
    NUM_WORST_NODES_FORMAT_EXCEPTION(16,
            UILabels.STL50080_NUM_WORST_NODES_FORMAT_EXCEPTION),

    OK(17, UILabels.STL50063_OK);

    private final static HashMap<Integer, PreferencesValidatorError> validateErrorMap =
            new HashMap<Integer, PreferencesValidatorError>();

    static {
        for (PreferencesValidatorError type : PreferencesValidatorError
                .values()) {
            validateErrorMap.put(type.getId(), type);
        }
    }

    private final int id;

    private final String value;

    public final UILabels label;

    public static Object[] data;

    private PreferencesValidatorError(int id, UILabels label) {

        this.id = id;
        this.label = label;
        this.value = label.getDescription();
    }

    public UILabels getLabel() {
        return label;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static String getValue(int id) {

        PreferencesValidatorError err = validateErrorMap.get(id);
        String description = null;
        if (err != null) {
            description = err.getLabel().getDescription(data);
        }
        return description;
    }

    public static Object[] getData() {
        return data;
    }
}
