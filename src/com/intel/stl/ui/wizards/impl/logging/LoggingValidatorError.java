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
 *  File Name: LoggingValidatorErrorType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/04/02 13:33:00  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/20 19:12:17  rjtierne
 *  Archive Log:    Fixed a typo
 *  Archive Log:    Changed INVALID_FORMAT TO INVALID_TYPE
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/19 18:51:39  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Enumeration to serve as error codes for the Logging Input Validator
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.impl.logging;

import java.util.HashMap;

import com.intel.stl.ui.common.UILabels;

public enum LoggingValidatorError {

    MAX_FILE_SIZE_MISSING(0, UILabels.STL50034_MAX_FILE_SIZE_MISSING),
    MAX_FILE_SIZE_INVALID_TYPE(1, UILabels.STL50035_MAX_FILE_SIZE_INVALID_TYPE),
    MAX_FILE_SIZE_FORMAT_EXCEPTION(2,
            UILabels.STL50036_MAX_FILE_SIZE_FORMAT_EXCEPTION),
    MAX_FILE_SIZE_OUT_OF_RANGE(3, UILabels.STL50062_MAX_FILE_SIZE_OUT_OF_RANGE),
    MAX_NUM_FILES_OUT_OF_RANGE(4, UILabels.STL50037_MAX_NUM_FILES_OUT_OF_RANGE),
    MAX_NUM_FILES_MISSING(5, UILabels.STL50038_MAX_NUM_FILES_MISSING),
    MAX_NUM_FILES_INVALID_TYPE(6, UILabels.STL50039_MAX_NUM_FILES_INVALID_TYPE),
    MAX_NUM_FILES_TOO_LARGE(7, UILabels.STL50040_MAX_NUM_FILES_TOO_LARGE),
    MAX_NUM_FILES_FORMAT_EXCEPTION(8,
            UILabels.STL50041_MAX_NUM_FILES_FORMAT_EXCEPTION),
    FILE_LOCATION_MISSING(9, UILabels.STL50042_FILE_LOCATION_MISSING),
    FILE_LOCATION_CREATION_ERROR(10,
            UILabels.STL50043_FILE_LOCATION_CREATION_ERROR),
    FILE_LOCATION_HEADLESS_ERROR(11,
            UILabels.STL50044_FILE_LOCATION_HEADLESS_ERROR),
    FILE_LOCATION_IO_ERROR(12, UILabels.STL50045_FILE_LOCATION_IO_ERROR),
    FILE_LOCATION_DIRECTORY_ERROR(13,
            UILabels.STL50046_FILE_LOCATION_DIRECTORY_ERROR),
    FORMAT_STRING_EMPTY(14, UILabels.STL50047_FORMAT_STRING_EMPTY),
    FORMAT_STRING_INVALID(15, UILabels.STL50048_FORMAT_STRING_INVALID),
    UNSUPPORTED_APPENDER_TYPE(16, UILabels.STL50060_UNSUPPORTED_APPENDER_TYPE),
    INVALID_THRESHOLD_TYPE(17, UILabels.STL50061_INVALID_THRESHOLD_TYPE),
    OK(18, UILabels.STL50063_OK);

    private final static HashMap<Integer, LoggingValidatorError> validateErrorMap =
            new HashMap<Integer, LoggingValidatorError>();

    static {
        for (LoggingValidatorError type : LoggingValidatorError.values()) {
            validateErrorMap.put(type.getId(), type);
        }
    }

    private final int id;

    private final String value;

    public final UILabels label;

    public static Object data;

    private LoggingValidatorError(int id, UILabels label) {

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

        LoggingValidatorError err = validateErrorMap.get(id);
        String description = null;
        if (err != null) {
            description = err.getLabel().getDescription(data);
        }
        return description;
    }

    public static Object getData() {
        return data;
    }
}
