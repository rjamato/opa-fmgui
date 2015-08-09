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
 *  File Name: IFailureItem.java
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

import java.util.concurrent.Callable;

/**
 * Description of a TASK failure that allows us identify the task that caused
 * the failure, and apply proper behaviors, such as retry or treat it as fatal
 * failure that is unrecoverable.
 */
public interface ITaskFailure<E> {
    /**
     * 
     * <i>Description:</i> ID that identifies the task that caused the failure
     * 
     * @return the id
     */
    Object getTaskId();

    /**
     * 
     * <i>Description:</i> The task can be used to retry to recover the failure
     * 
     * @return the task
     */
    Callable<E> getTask();

    /**
     * 
     * <i>Description:</i> transfer an <code>error</code> exception to a
     * FailureType so we a Failure Manager can handle the failure properly
     * 
     * @param error
     *            the exception that cause the failure
     * @return the {@link FailureType}
     */
    FailureType getFailureType(Throwable error);

    /**
     * 
     * <i>Description:</i> called when the failure is identified as
     * unrecoverable
     * 
     */
    void onFatal();
}
