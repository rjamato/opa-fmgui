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
 *  File Name: BaseFailureEvaluator.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/08/12 20:28:40  jijunwan
 *  Archive Log:    init version Failure Management
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.failure;

public class BaseFailureEvaluator implements IFailureEvaluator {
    private Class<?>[] recoverableErros;

    private Class<?>[] unrecoverableErros;

    /**
     * @param recoverableErros
     *            the recoverableErros to set
     */
    public void setRecoverableErrors(Class<?>... recoverableErros) {
        this.recoverableErros = recoverableErros;
    }

    /**
     * @param unrecoverableErros
     *            the unrecoverableErros to set
     */
    public void setUnrecoverableErrors(Class<?>... unrecoverableErros) {
        this.unrecoverableErros = unrecoverableErros;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.failure.IFailureEvaluator#getType(java.lang.Throwable)
     */
    @Override
    public FailureType getType(Throwable error) {
        if (error == null) {
            return FailureType.IGNORE;
        } else if (hasError(error, unrecoverableErros)) {
            return FailureType.UNRECOVERABLE;
        } else if (hasError(error, recoverableErros)) {
            return FailureType.RECOVERABLE;
        } else {
            return FailureType.IGNORE;
        }
    }

    protected boolean hasError(Throwable target, Class<?>[] ref) {
        if (ref == null) {
            return false;
        }

        Class<?> targetKlass = target.getClass();
        for (Class<?> klass : ref) {
            if (klass != null && klass.isAssignableFrom(targetKlass)) {
                return true;
            }
        }
        return false;
    }
}
