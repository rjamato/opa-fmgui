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
 *  File Name: FVTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/10/09 12:54:07  fernande
 *  Archive Log:    Added support for PropertyChange events bubbling up from an SwingWorker, thru an AbstractTask into the controller.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/09 13:31:46  fernande
 *  Archive Log:    Moving MVC framework to its own package and renaming for consistency
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/08 20:27:15  fernande
 *  Archive Log:    Basic MVC framework with SwingWorker support
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.framework;

import java.beans.PropertyChangeListener;

/*
 * A task that performs work for a controller in the MVC framework
 */

public interface ITask {

    /**
     * 
     * <i>Description:</i> executes this task through a Swing worker
     * 
     * @param owner
     *            the controller that owns this task
     */
    void execute(IController owner);

    /**
     * 
     * <i>Description:</i> returns the submission status of this task
     * 
     * @return boolean
     */
    boolean isSubmitted();

    /**
     * 
     * <i>Description:</i> returns the completion status of this task
     * 
     * @return boolean
     */
    boolean isDone();

    /**
     * 
     * <i>Description:</i> returns the cancellation status of this task
     * 
     * @return boolean
     */
    boolean isCancelled();

    /**
     * 
     * <i>Description:</i> cancels this task
     * 
     * @param mayInterruptIfRunning
     *            flag to cancel this task even if it is running
     */
    void cancel(boolean mayInterruptIfRunning);

    /**
     * 
     * <i>Description:</i> adds a PropertyChangeListener for this task
     * 
     * @param listener
     *            the listener interested in property changes
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * 
     * <i>Description:</i> removes a PropertyChangeListener
     * 
     * @param listener
     *            the listener no longer interested in property changes
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * 
     * <i>Description:</i> fires a PropertyChange event
     * 
     * @param propertyName
     *            the name of the property changing
     * @param oldValue
     *            the old value the property had
     * @param newValue
     *            the new value the property has
     */
    void firePropertyChange(String propertyName, Object oldValue,
            Object newValue);

}
