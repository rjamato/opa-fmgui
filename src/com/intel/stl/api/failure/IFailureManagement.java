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
 *  File Name: IFailureManagement.java
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

public interface IFailureManagement {
    /**
     * 
     * <i>Description:</i> submit a task failure to the failure manager when we
     * see an exception. The failure item ({@link ITaskFailure}) will provide a
     * task that can be used to retry to see if we can recover. When we do this,
     * the failure manager should consider two possible situations:
     * <ol>
     * <li>The task itself will throw exceptions. The manager should capture
     * these exceptions and call the provided failure item's
     * {@link ITaskFailure#onFatal} when it sees it as unrecoverable.
     * <li>The task will indirectly cause another call to this
     * {@link #submit(ITaskFailure, Throwable)} method, for example a task that
     * sends a command to a remote server.
     * </ol>
     * 
     * @param failure
     *            the failure item. see {@link ITaskFailure}
     * @param error
     *            the reason we submit a failure item. The value can be
     *            <code>null</code> and it's depend on the implementation to
     *            interpret it. For example, one implementation may treat
     *            <code>null</code> as task being executed successfully and uses
     *            this information to clear or reset its memory about this task.
     *            Another implementation may just ignore it and clear or reset
     *            memory about a task purely based on time, such as any failures
     *            no update for 3 minutes are treated as successfully recovered.
     *            One of the advantages of this time based approach is that the
     *            caller needn't to call this method every time no matter
     *            whether it has an exception or not.
     */
    void submit(ITaskFailure<Void> failure, Throwable error);

    /**
     * 
     * <i>Description:</i> Blocked version
     * {@link #submit(ITaskFailure, Throwable)}. This method will wait until the
     * failure is recovered or it is identified as unrecoverable and
     * {@link ITaskFailure#onFatal()} is executed.
     * 
     * @param failure
     * @param error
     * @return task result
     */
    <E> E evaluate(ITaskFailure<E> failure, Throwable error);

    /**
     * 
     * Description: cleanup this failure manager, such as release resources.
     * 
     * @throws InterruptedException
     * 
     */
    void cleanup() throws InterruptedException;
}
