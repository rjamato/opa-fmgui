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
 *  File Name: FMRuntimeException.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/08/12 19:56:41  jijunwan
 *  Archive Log:    made FMException to be more general, so can potentially reuse it from UI side
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/30 17:00:13  fernande
 *  Archive Log:    Establishing overall exception handling strategy for the app
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api;


public class FMRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FMRuntimeException(IMessage message) {
        super(message.getDescription());
        this.errorCode = message.getErrorCode();
    }

    public FMRuntimeException(IMessage message, Throwable cause) {
        super(message.getDescription(), cause);
        this.errorCode = message.getErrorCode();
    }

    public FMRuntimeException(IMessage message, Throwable cause,
            Object... arguments) {
        super(message.getDescription(arguments), cause);
        this.errorCode = message.getErrorCode();
    }

    public FMRuntimeException(IMessage message, Object... arguments) {
        super(message.getDescription(arguments));
        this.errorCode = message.getErrorCode();
    }

    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
