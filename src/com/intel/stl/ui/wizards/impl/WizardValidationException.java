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
 *  File Name: WizardValidationException.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/02/20 21:14:03  rjtierne
 *  Archive Log:    Multinet Wizard: Exception class used to pass exceptions during validation of wizard entry forms
 *  Archive Log:
 *
 *  Overview: Exception class for wizard validation errors
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.impl;

import com.intel.stl.api.FMException;
import com.intel.stl.api.IMessage;

public class WizardValidationException extends FMException {

    private static final long serialVersionUID = 7273129760818821705L;

    public WizardValidationException(IMessage message) {
        super(message);
    }

    public WizardValidationException(IMessage message, Throwable cause) {
        super(message, cause);
    }

    public WizardValidationException(IMessage message, Throwable cause,
            Object... arguments) {
        super(message, cause, arguments);
    }

    public WizardValidationException(IMessage message, Object... arguments) {
        super(message, arguments);
    }
}
