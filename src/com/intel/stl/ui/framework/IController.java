/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 *  Archive Log:    Revision 1.4  2015/08/17 18:53:57  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
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
