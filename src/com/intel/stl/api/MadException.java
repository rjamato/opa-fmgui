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
 *  File Name: MadException.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/16 05:10:35  jijunwan
 *  Archive Log:    added cmd input argument information to MadException
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/29 20:06:46  jijunwan
 *  Archive Log:    added exception for mad failure
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api;

import com.intel.stl.common.STLMessages;

public class MadException extends FMException {
    private static final long serialVersionUID = 4063493800672669073L;

    public MadException(Class type, short attributeId, short code, String desc) {
        super(STLMessages.STL2000_MAD_FAILED, type.getSimpleName(), StringUtils
                .shortHexString(attributeId), StringUtils.shortHexString(code),
                desc);
    }
}
