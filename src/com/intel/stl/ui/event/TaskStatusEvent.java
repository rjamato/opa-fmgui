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
 *  File Name: TaskStatusEvent.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/11/05 22:57:33  jijunwan
 *  Archive Log:    improved the stability of turning on/off refresh icon when we response to notice events
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.event;

import com.intel.stl.ui.framework.AbstractEvent;

public class TaskStatusEvent<E> extends AbstractEvent {
    public enum Status {
        STARTED,
        FINISHED,
        CANCELED
    };

    private final E taskId;

    private final double progress;

    private final Status status;

    public TaskStatusEvent(Object source, E taskId, Status status) {
        this(source, taskId, status, 0);
    }

    /**
     * Description:
     * 
     * @param origin
     * @param taskId
     * @param status
     * @param progress
     */
    public TaskStatusEvent(Object origin, E taskId, Status status,
            double progress) {
        super(origin);
        this.taskId = taskId;
        this.status = status;
        this.progress = progress;
    }

    /**
     * @return the taskId
     */
    public E getTaskId() {
        return taskId;
    }

    /**
     * @return the progress
     */
    public double getProgress() {
        return progress;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    public boolean isStarted() {
        return status == Status.STARTED;
    }
}
