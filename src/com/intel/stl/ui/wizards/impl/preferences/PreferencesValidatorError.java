/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 *  Archive Log:    Revision 1.3  2015/08/17 18:54:52  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
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
