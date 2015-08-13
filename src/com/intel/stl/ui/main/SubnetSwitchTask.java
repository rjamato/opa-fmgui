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
 *  File Name: SubnetSwitchTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.19.2.1  2015/08/12 15:26:34  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/04/22 22:31:53  fisherma
 *  Archive Log:    Removing html tags from error messages.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/04/06 11:14:08  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. Open issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/04/02 13:32:54  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/03/16 17:44:56  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/03/02 22:34:33  fernande
 *  Archive Log:    Fixes to handle a change in subnet name; if subnet name is changed, the model in FabricController is now updated as well as the name in the SubnetDescription in the subnet context to that the view and the back end are now in sync.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/02/12 22:11:16  fernande
 *  Archive Log:    Changes to key viewers and contexts using Host_IP_Address+Port so that there is one subnet per viewer
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/02/05 15:06:12  jijunwan
 *  Archive Log:    tried to improve stability on multi-subnet support
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/01/30 20:27:16  fernande
 *  Archive Log:    Initial changes to support multiple fabric viewers
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/01/11 21:50:29  jijunwan
 *  Archive Log:    added connection failure reason
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/12/11 18:52:55  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/11/11 18:06:50  fernande
 *  Archive Log:    Support for generic preferences: a new node (Preferences) in the UserOptions XML now allows to define groups of preferences (Section) and key/value pairs (Entry) that are stored in Properties objects are runtime.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/11/05 19:05:13  fernande
 *  Archive Log:    Fixed an issue where defining a new subnet does not trigger a save topology task, causing notice processing to fail.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/22 01:16:19  jijunwan
 *  Archive Log:    some simplifications on MVC framework
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/16 13:18:05  fernande
 *  Archive Log:    Changes to AbstractTask to support an onFinally method that is guaranteed to be called no matter what happens in the onTaskSuccess and onTaskFailure implementations for a task.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/15 22:26:04  jijunwan
 *  Archive Log:    fixed a cleanup issue
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/14 20:50:31  jijunwan
 *  Archive Log:    improved to recreate Context when we connect to or refresh a subnet and the cached one is invalid
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/14 11:32:11  jypak
 *  Archive Log:    UI updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/09 14:58:02  fernande
 *  Archive Log:    Resetting the LastAccessedSubnet at the end of successful subnet switch
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

import static com.intel.stl.api.configuration.UserSettings.PROPERTY_LASTSUBNETACCESSED;
import static com.intel.stl.api.configuration.UserSettings.SECTION_USERSTATE;
import static com.intel.stl.ui.main.FabricController.PROGRESS_NOTE_PROPERTY;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.IContextAware;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.framework.AbstractTask;
import com.intel.stl.ui.framework.IController;

public class SubnetSwitchTask extends
        AbstractTask<FabricModel, Context, String> {

    private static Logger log = LoggerFactory.getLogger(SubnetSwitchTask.class);

    private boolean previousContextCleared;

    private Context oldContext;

    private final List<ContextSwitchTask> subtasks;

    private final List<IContextAware> contextPages;

    private final Context newContext;

    public SubnetSwitchTask(FabricModel model, Context newContext,
            List<IContextAware> contextPages) {
        super(model);
        this.newContext = newContext;
        this.contextPages = contextPages;
        this.subtasks = new ArrayList<ContextSwitchTask>();
    }

    /**
     * @return the newContext
     */
    public Context getNewContext() {
        return newContext;
    }

    /**
     * @return the oldContext
     */
    public Context getOldContext() {
        return oldContext;
    }

    /**
     * @param oldContext
     *            the oldContext to set
     */
    public void setOldContext(Context oldContext) {
        this.oldContext = oldContext;
    }

    @Override
    public Context processInBackground(Context context) throws Exception {
        oldContext = context;
        previousContextCleared = false;
        newContext.initialize();

        final FabricController controller = (FabricController) getController();
        SubnetDescription newSubnet = newContext.getSubnetDescription();
        SubnetDescription currentSubnet = null;
        if (oldContext != null) {
            currentSubnet = oldContext.getSubnetDescription();
            log.info("Switching to subnet '" + newSubnet + "' from '"
                    + currentSubnet + "'...");
        } else {
            log.info("Switching to subnet '" + newSubnet + "'...");
        }

        publishProgressNote(UILabels.STL10105_CREATE_CONTEXT.getDescription());

        // any connection issues should have occurred by now
        // start switching subnets

        // only switch context when we are sure the new subnet is working
        if (oldContext != null) {
            log.info("Clearing context for '" + currentSubnet + "'");
            publishProgressNote(UILabels.STL10107_CLEAR_CONTEXT
                    .getDescription());
            oldContext.close();
            previousContextCleared = true;
        }

        for (IContextAware page : contextPages) {
            ContextSwitchTask subtask =
                    new ContextSwitchTask(model, newContext, this, page);
            subtasks.add(subtask);
            controller.submitTask(subtask);
        }
        boolean allDone = false;
        while (!allDone) {
            waitForSubtasks();
            boolean completed = true;
            for (ContextSwitchTask subtask : subtasks) {
                if (subtask.isDone() || subtask.isCancelled()) {
                } else {
                    completed = false;
                }
            }
            if (completed) {
                allDone = true;
            }
        }

        return newContext;
    }

    private synchronized void waitForSubtasks() {
        try {
            wait(500L);
        } catch (InterruptedException e) {
        }
    }

    /**
     * This method is invoked by a ContextSwitchTask to mark its end
     */
    public synchronized void checkSubtasks() {
        notify();
    }

    @Override
    public void onTaskSuccess(Context result) {
        for (ContextSwitchTask subtask : subtasks) {
            if (subtask.hasException()) {
                onTaskFailure(subtask.getExecutionException());
                return;
            }
        }
        IController controller = getController();
        controller.setContext(result);
        // Update the model
        SubnetDescription oldSubnet = model.getCurrentSubnet();
        SubnetDescription resSubnet = result.getSubnetDescription();
        model.setCurrentSubnet(resSubnet);
        model.setPreviousSubnet(oldSubnet);
        UserSettings userSettings = result.getUserSettings();
        Properties usrState = null;
        if (userSettings != null) {
            usrState = userSettings.getPreferences().get(SECTION_USERSTATE);
            if (usrState == null) {
                usrState = new Properties();
                userSettings.getPreferences().put(SECTION_USERSTATE, usrState);
            }
        }

        if (usrState != null) {
            usrState.setProperty(PROPERTY_LASTSUBNETACCESSED,
                    resSubnet.getName());
        }
        controller.notifyModelChanged();
    }

    @Override
    public void onTaskFailure(Throwable caught) {
        SubnetDescription newSubnet = newContext.getSubnetDescription();
        StringBuffer sb = new StringBuffer();
        sb.append(UILabels.STL50050_CONNECTION_FAIL.getDescription(
                newSubnet.getName(), newSubnet.getCurrentFE().getHost()));
        sb.append("\n" + StringUtils.getErrorMessage(caught));
        if (!previousContextCleared && oldContext != null) {
            SubnetDescription currentSubnet = oldContext.getSubnetDescription();
            sb.append("\n");
            sb.append(UILabels.STL50051_USE_OLD_SUBNET.getDescription(
                    currentSubnet.getName(), currentSubnet.getCurrentFE()
                            .getHost()));
        }
        model.setErrorMessage(sb.toString());
        getController().notifyModelUpdateFailed(caught);
    }

    @Override
    public void onFinally() {
    }

    @Override
    public void processIntermediateResults(List<String> intermediateResults) {
    }

    public void publishProgressNote(String note) {
        firePropertyChange(PROGRESS_NOTE_PROPERTY, null, note);
    }
}
