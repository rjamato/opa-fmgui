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
 *  File Name: TaskBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/12 19:40:07  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/02 15:36:15  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: TaskBean class holds task related information
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.publisher.subscriber;

import java.util.List;
import java.util.concurrent.Callable;

import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;

/**
 * Description: TaskBean class to hold the task information
 * 
 * @param <E>
 * 
 * @param taskList
 *            list of tasks
 * 
 * @param tasks
 *            task in the list
 * 
 * @param callback
 *            method to call when scheduled time has arrived
 * 
 * @param caller
 *            object that registered the task
 */
class TaskBean<E> {

    private final Task<E> task;

    private ICallback<E> callback = null;

    private ICallback<E[]> callbacks = null;

    private final Callable<E> caller;

    public TaskBean(Task<E> task, ICallback<E> callback, Callable<E> caller) {

        this.task = task;
        this.callback = callback;
        this.caller = caller;
    }

    public TaskBean(List<Task<?>> taskList, Task<E> task,
            ICallback<E[]> callbacks, Callable<E> caller) {

        this.task = task;
        this.callbacks = callbacks;
        this.caller = caller;
    }

    public Task<E> getTask() {
        return task;
    }

    public ICallback<E> getCallback() {
        return callback;
    }

    public ICallback<E[]> getCallbacks() {
        return callbacks;
    }

    public Callable<E> getCaller() {
        return caller;
    }

    @Override
    public String toString() {

        return "Task [task=" + task.toString() + ", callback=" + callback
                + ", caller=" + caller + "]";

    }
} // class TaskBean

