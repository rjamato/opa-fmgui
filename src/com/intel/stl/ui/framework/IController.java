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
 *  File Name: FVController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/10/22 01:15:24  jijunwan
 *  Archive Log:    some simplifications on MVC framework
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/22 21:48:13  fernande
 *  Archive Log:    Changes to framework to support model update lifecycle by notifying views of changes
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

import com.intel.stl.ui.main.Context;

public interface IController {

    /**
     * 
     * Description: sets the context for the controller
     * 
     * @param context
     *            the context to be used by this controller
     */
    void setContext(Context context);

    /**
     * 
     * Description: returns the context in use by this controller
     * 
     * @return the context in use by this controller
     */
    Context getContext();

    /**
     * 
     * Description: invoked during MVC initialization to initialize the model.
     * The view is notified after this initialization, even if the controller
     * decides that no initialization is needed.
     * 
     */
    void initModel();

    /**
     * 
     * Description: submits a task for execution. The task is run under a Swing
     * Worker so that Swing Event Dispatch Thread is not blocked
     * 
     * @param task
     *            the task to be executed
     */
    void submitTask(ITask task);

    /**
     * 
     * Description: invoked by a task to signal that the task has completed
     * successfully. If more than one task is executed, the controller is
     * responsible to determine which one has completed.
     * 
     */
    void onTaskSuccess();

    /**
     * 
     * Description: invoked by a task to signal that the task has failed.
     * 
     * @param caught
     *            the error caught during task execution
     */
    void onTaskFailure(Throwable caught);

    /**
     * 
     * Description: notify all listeners that a change to this model has just
     * occurred
     * 
     */
    void notifyModelChanged();

    /**
     * 
     * Description: notify all listeners an error occurred while attempting to
     * update this model
     * 
     * @param caught
     */
    void notifyModelUpdateFailed(Throwable caught);
}
