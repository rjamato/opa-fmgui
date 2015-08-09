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
 *  File Name: Validator.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/01/20 19:11:18  rjtierne
 *  Archive Log:    Added generic integer range checker method
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/08 18:49:52  jijunwan
 *  Archive Log:    added our own Validator class to do general validations for user input
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

public class Validator {
    /**
     * 
     * <i>Description:</i> Checks if the field isn't null and length of the
     * field is greater than zero not including whitespace.
     * 
     * @param value
     * @return
     */
    public static boolean isBlankOrNull(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * 
     * <i>Description:</i> Checks if the value's length is less than or equal to
     * the max. equal to the max.
     * 
     * @param value
     * @param maxLen
     * @return
     */
    public static boolean maxLength(String value, int maxLen) {
        return value == null || value.length() <= maxLen;
    }

    public static boolean integerInRange(int value, int min, int max) {
        return ((min <= value) && (value <= max));
    }

}
