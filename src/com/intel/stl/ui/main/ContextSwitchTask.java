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
 *  File Name: ContextSwitchTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2.2.1  2015/08/12 15:26:34  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
