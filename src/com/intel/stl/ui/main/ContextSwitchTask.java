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
 *  File Name: ContextSwitchTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/10/16 13:18:05  fernande
 *  Archive Log:    Changes to AbstractTask to support an onFinally method that is guaranteed to be called no matter what happens in the onTaskSuccess and onTaskFailure implementations for a task.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/09 12:59:15  fernande
 *  Archive Log:    Changed the FabricController to use the UI framework and converted Swing workers into AbstractTasks to optimize the switching of contexts and the refreshing of pages. These processes still run under Swing workers, but now each setContext is run on its own Swing worker to improve performance. Also, changed the ProgressObserver mechanism to provide a more accurate progress.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import static com.intel.stl.ui.common.UILabels.STL10108_INIT_PAGE;

import java.util.List;

import com.intel.stl.ui.common.IContextAware;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.ProgressObserver;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.framework.AbstractTask;

public class ContextSwitchTask extends AbstractTask<FabricModel, Void, Void> {

    private final Context newContext;

    private final SubnetSwitchTask master;

    public final IContextAware contextPage;

    private IProgressObserver observer;

    private boolean hasException = false;

    private Throwable executionException;

    public ContextSwitchTask(FabricModel model, Context newContext,
            SubnetSwitchTask master, IContextAware contextPage) {
        super(model);
        this.newContext = newContext;
        this.master = master;
        this.contextPage = contextPage;
    }

    @Override
    public Void processInBackground(Context context) throws Exception {
        master.publishProgressNote(STL10108_INIT_PAGE
                .getDescription(contextPage.getName()));
        int work = contextPage.getContextSwitchWeight().getWeight();
        observer = new ProgressObserver(master, work);
        contextPage.setContext(newContext, observer);
        return null;
    }

    @Override
    public void onTaskSuccess(Void result) {
        master.publishProgressNote(UILabels.STL10112_INIT_PAGE_COMPLETED
                .getDescription(contextPage.getName()));
    }

    @Override
    public void onTaskFailure(Throwable caught) {
        hasException = true;
        executionException = caught;
    }

    @Override
    public void onFinally() {
        observer.onFinish();
        master.checkSubtasks();
    }

    @Override
    public void processIntermediateResults(List<Void> intermediateResults) {
    }

    public boolean hasException() {
        return hasException;
    }

    public Throwable getExecutionException() {
        return executionException;
    }

}
