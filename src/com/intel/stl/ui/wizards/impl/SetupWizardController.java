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
 *  File Name: SetupWizardController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.24.2.1  2015/08/12 15:27:01  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/04/02 13:33:04  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/03/30 15:11:27  rjtierne
 *  Archive Log:    - Added method showStep() to show a task without initializing it
 *  Archive Log:    - Added new tryToConnect() to take a subnet so individual backup hosts can be tested
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/03/11 15:24:10  rjtierne
 *  Archive Log:    Multinet Wizard: Changed saveSubnet() from public to protected and removed from interface
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/02/26 16:20:44  fernande
 *  Archive Log:    Changed showSetupWizard so that the wizard can show its view centered on the calling frame.
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/02/20 21:13:23  rjtierne
 *  Archive Log:    Multinet Wizard: New instalment of the multinet wizard targeting synchronization of all sub-wizard data with selected subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/02/13 21:31:51  rjtierne
 *  Archive Log:    Multinet Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/02/12 22:12:10  fernande
 *  Archive Log:    Changes to key viewers and contexts using Host_IP_Address+Port so that there is one subnet per viewer
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/02/10 19:35:38  fernande
 *  Archive Log:    Changes to handle SubnetConnectionExceptions
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/02/10 16:35:47  rjtierne
 *  Archive Log:    Commented out code that saves settings to the database when
 *  Archive Log:    closing the setup wizard.  This code is unnecessary and causes
 *  Archive Log:    a concurrency issue with HSql.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/02/09 21:58:50  jijunwan
 *  Archive Log:    put tasks on SwingWorker so we do not query DB or FE on EDT.
 *  Archive Log:    Always use a copy of subnet description
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/02/06 17:36:25  fernande
 *  Archive Log:    Fixed UserSettings not being saved off to the database in the SetupWizard
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/02/06 15:12:00  fernande
 *  Archive Log:    Changes so that the Setup Wizard depends on the Subnet Manager for all subnet-related operations
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/02/02 20:37:39  fernande
 *  Archive Log:    Fixing the SetupWizard so that it can define new subnets. Fixed also StackOverflowError exception when switching subnets.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/02/02 15:28:27  rjtierne
 *  Archive Log:    Gets subnet from the main setup wizard as part of new
 *  Archive Log:    Multi-subnet architecture
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/01/30 20:28:21  fernande
 *  Archive Log:    Initial changes to support multiple fabric viewers
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/01/21 21:21:21  rjtierne
 *  Archive Log:    Supplying preferences wizard with sweep interval through Context
 *  Archive Log:    for comparison with refresh rate supplied by user input. Also providing
 *  Archive Log:    task scheduler to preferences wizard so user supplied refresh rate can
 *  Archive Log:    be updated.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/01/20 19:11:53  rjtierne
 *  Archive Log:    Passing userSettings to SubnetWizardController
 *  Archive Log:    Passing preferencesController to preferencesView for interface access.
 *  Archive Log:    Check if onApply() was successful before closing the wizard dialog.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/01/15 19:08:30  rjtierne
 *  Archive Log:    Passed userSettings into the preferences wizard controller to store/retrieve performance properties
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/13 19:00:10  rjtierne
 *  Archive Log:    Added User Preferences wizard view and controller to getTasks()
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/11 21:48:18  jijunwan
 *  Archive Log:    setup wizard improvements
 *  Archive Log:    1) look and feel adjustment
 *  Archive Log:    2) secure FE support
 *  Archive Log:    3) apply wizard on current subnet
 *  Archive Log:    4) message display based on message type rather than directly specifying UI resources
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/23 18:32:31  rjtierne
 *  Archive Log:    Disabled status panel when wizard is run for the first time
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/12/19 18:43:37  rjtierne
 *  Archive Log:    Changed argument list for calls to showMessage()
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/11 20:01:54  rjtierne
 *  Archive Log:    Temporarily disabled Logging Wizard until logback is implemented
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/10 21:31:05  rjtierne
 *  Archive Log:    New Setup Wizard based on framework
 *  Archive Log:
 *
 *  Overview: Top level Setup Wizard
 *
 *  @author: jijunwan, rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import com.intel.stl.api.configuration.AppenderConfig;
import com.intel.stl.api.configuration.UserNotFoundException;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.api.subnet.SubnetConnectionException;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.main.IFabricController;
import com.intel.stl.ui.main.ISubnetManager;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.intel.stl.ui.wizards.impl.event.EventWizardController;
import com.intel.stl.ui.wizards.impl.logging.LoggingWizardController;
import com.intel.stl.ui.wizards.impl.preferences.PreferencesWizardController;
import com.intel.stl.ui.wizards.impl.subnet.SubnetWizardController;
import com.intel.stl.ui.wizards.model.event.EventRulesTableModel;
import com.intel.stl.ui.wizards.view.ITaskView;
import com.intel.stl.ui.wizards.view.IWizardView;
import com.intel.stl.ui.wizards.view.event.EventWizardView;
import com.intel.stl.ui.wizards.view.logging.LoggingWizardView;
import com.intel.stl.ui.wizards.view.preferences.PreferencesWizardView;
import com.intel.stl.ui.wizards.view.subnet.SubnetWizardView;

public class SetupWizardController implements IWizardListener {

    private final IWizardView view;

    private final ISubnetManager subnetMgr;

    private final List<IWizardTask> tasks;

    private IWizardTask currentTask;

    private final IWizardTask firstTask;

    private final IWizardTask lastTask;

    private SubnetDescription subnet;

    @SuppressWarnings("unused")
    private String userName = "default";

    private UserSettings userSettings;

    private boolean isFirstRun;

    private static SetupWizardController instance;

    /**
     * 
     * Description Constructor for the SetupWizardController:
     * 
     * @param view
     *            view for the setup wizard
     */
    private SetupWizardController(IWizardView view, ISubnetManager subnetMgr) {
        this.view = view;
        this.subnetMgr = subnetMgr;
        this.isFirstRun = subnetMgr.isFirstRun();
        this.view.setWizardListener(this);

        tasks = getTasks(subnetMgr);
        installTasks(tasks);
        firstTask = tasks.get(0);
        lastTask = tasks.get(tasks.size() - 1);
    }

    public static SetupWizardController getInstance(IWizardView view,
            ISubnetManager subnetMgr) {

        if (instance == null) {
            instance = new SetupWizardController(view, subnetMgr);
        }

        return instance;
    }

    /**
     * 
     * <i>Description: Builds a list of IConfigTask controllers</i>
     * 
     * @return tasks - list of IConfigTask
     */
    protected List<IWizardTask> getTasks(ISubnetManager subnetMgr) {

        List<IWizardTask> tasks = new ArrayList<IWizardTask>();

        // Subnet Wizard
        SubnetWizardView subnetView = new SubnetWizardView(view);
        SubnetWizardController subnetController =
                new SubnetWizardController(subnetView, subnetMgr);
        subnetView.setWizardListener(subnetController);
        tasks.add(subnetController);

        // Event Wizard
        EventWizardView eventView =
                new EventWizardView(new EventRulesTableModel(), view);
        EventWizardController eventController =
                new EventWizardController(eventView);
        eventView.setWizardListener(eventController);
        tasks.add(eventController);

        // Logging Wizard
        LoggingWizardView loggingView = (new LoggingWizardView(view));
        LoggingWizardController loggingController =
                new LoggingWizardController(loggingView);

        tasks.add(loggingController);

        // User Preferences
        PreferencesWizardView preferencesView = new PreferencesWizardView(view);
        PreferencesWizardController preferencesController =
                new PreferencesWizardController(preferencesView);
        preferencesView.setWizardListener(preferencesController);
        tasks.add(preferencesController);

        return tasks;
    }

    protected IWizardTask getTaskByName(String name) {

        boolean found = false;
        IWizardTask currentTask = null;
        Iterator<IWizardTask> it = tasks.iterator();

        while (!found && it.hasNext()) {

            currentTask = it.next();
            found = (currentTask.getName().equals(name));
        }

        return currentTask;
    }

    protected void installTasks(List<IWizardTask> tasks) {
        for (IWizardTask task : tasks) {
            task.setWizardController(this);
        }
        view.setTasks(tasks);
    }

    public void setReady(boolean b) {
    }

    public void cleanup() {

        for (IWizardTask task : tasks) {
            try {
                task.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#onPrevious()
     */
    @Override
    public void onPrevious() {

        IWizardTask previousTask = tasks.get(tasks.indexOf(currentTask) - 1);

        // Assume both buttons to be enabled and move to the previous task
        view.enableNext(true);
        view.enablePrevious(true);
        previousTask.onPrevious();
        selectStep(previousTask.getName());

        if (currentTask != null) {
            // If the first task is current, disable the previous button
            if (currentTask.getName().equals(firstTask.getName())) {
                view.enablePrevious(false);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#onNext()
     */
    @Override
    public boolean onNext() {

        boolean success = false;

        // Commit the changes
        try {
            success = currentTask.validateUserEntry();
            currentTask.setDone(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (success) {
            // At first, enable both buttons
            view.enableNext(true);
            view.enablePrevious(true);

            // If the button displays "Next", move to the next task
            if (view.isNextButton()) {
                selectStep(tasks.get(tasks.indexOf(currentTask) + 1).getName());
            } else {
                // Otherwise the button displays "Finish", so close the wizard
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        onClose();
                    }
                }).start();
            }
        }

        // If the last task is current, change the next button to "Finish"
        if ((currentTask != null)
                && currentTask.getName().equals(lastTask.getName())) {
            view.updateNextButton(STLConstants.K0627_FINISH.getValue());
        }
        return success;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#onApply()
     */
    @Override
    public void onApply() {
        try {
            currentTask.validateUserEntry();
            currentTask.setDone(true);
        } catch (Exception e) {
            ((ITaskView) currentTask.getView()).showMessage(e.getMessage(),
                    null, JOptionPane.ERROR_MESSAGE, (Object[]) null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#onClose()
     */
    @Override
    public void onClose() {
        view.closeWizard();
        isFirstRun = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardListener#selectStep(java.lang.String
     * )
     */
    @Override
    public void selectStep(String taskName) {

        // Check if the current task is already active
        boolean isActive = currentTask.getName().equals(taskName);

        // Get the task by name
        IWizardTask task = getTaskByName(taskName);

        // If current task isn't done yet, display message to verify that
        // user wants to abandon the operation.
        boolean abortOperation =
                ((currentTask != null) && (!currentTask.isDone()) && !isActive && currentTask
                        .isDirty());

        if ((abortOperation) && (!isFirstRun)) {
            ((ITaskView) currentTask.getView()).showMessage(
                    UILabels.STL50016_MIDDLE_WIZARD_CONFIG
                            .getDescription(currentTask.getName()),
                    InteractionType.CHANGE_WIZARDS, JOptionPane.ERROR_MESSAGE,
                    taskName);
        } else {
            if (task != null) {
                currentTask = task;
                currentTask.init();
            }
            view.showTaskView(taskName);
        }

        // If this is not the last task, keep next button "Next"
        if (task != null && !task.getName().equals(lastTask.getName())) {
            view.updateNextButton(STLConstants.K0622_NEXT.getValue());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardListener#showStep(java.lang.String)
     */
    @Override
    public void showStep(String taskName) {

        // Check if the current task is already active
        boolean isActive = currentTask.getName().equals(taskName);

        // Get the task by name
        IWizardTask task = getTaskByName(taskName);

        // If current task isn't done yet, display message to verify that
        // user wants to abandon the operation.
        boolean abortOperation =
                ((currentTask != null) && (!currentTask.isDone()) && !isActive && currentTask
                        .isDirty());

        if ((abortOperation) && (!isFirstRun)) {
            ((ITaskView) currentTask.getView()).showMessage(
                    UILabels.STL50016_MIDDLE_WIZARD_CONFIG
                            .getDescription(currentTask.getName()),
                    InteractionType.CHANGE_WIZARDS, JOptionPane.ERROR_MESSAGE,
                    taskName);
        } else {
            currentTask = task;
            view.showTaskView(taskName);
        }

        // If this is not the last task, keep next button "Next"
        if (task != null && !task.getName().equals(lastTask.getName())) {
            view.updateNextButton(STLConstants.K0622_NEXT.getValue());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#showView(boolean)
     */
    @Override
    public void showView(SubnetDescription subnet, String userName,
            IFabricController callingController) {
        this.subnet = subnet == null ? new SubnetDescription() : subnet.copy();
        String subnetName = null;
        if (subnet != null) {
            subnetName = subnet.getName();
        }
        this.userName = userName;
        try {
            this.userSettings = subnetMgr.getUserSettings(subnetName, userName);
        } catch (UserNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // By default select first step
        currentTask = firstTask;
        selectStep(currentTask.getName());
        view.enablePrevious(false);

        view.showWizard(subnetMgr.isFirstRun());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#getView()
     */
    @Override
    public IWizardView getView() {

        return view;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#closeStatusPanels()
     */
    @Override
    public void closeStatusPanels() {
        for (IWizardTask task : tasks) {
            ((ITaskView) task.getView()).closeStatusPanel();
        }
    }

    @Override
    public UserSettings getUserSettings() {
        return userSettings;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#isFirstRun()
     */
    @Override
    /**
     * @return the isFirstRun
     */
    public boolean isFirstRun() {
        return isFirstRun;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#onReset()
     */
    @Override
    public void onReset() {

        currentTask.onReset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#onDelete()
     */
    @Override
    public void onDelete() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#getSubnet()
     */
    @Override
    public SubnetDescription getSubnet() {
        return subnet;
    }

    @Override
    public boolean tryToConnect() throws SubnetConnectionException {
        return subnetMgr.tryToConnect(subnet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardListener#tryToConnect(com.intel.
     * stl.api.subnet.SubnetDescription)
     */
    @Override
    public boolean tryToConnect(SubnetDescription subnet)
            throws SubnetConnectionException {
        return subnetMgr.tryToConnect(subnet);
    }

    @Override
    public PMConfigBean getPMConfig() {
        return subnetMgr.getPMConfig(subnet);
    }

    @Override
    public TaskScheduler getTaskScheduler() {
        return subnetMgr.getTaskScheduler(subnet);
    }

    @Override
    public void saveLoggingConfiguration(List<AppenderConfig> appenders) {
        subnetMgr.saveLoggingConfiguration(appenders);
    }

    @Override
    public List<AppenderConfig> getLoggingConfig() {
        return subnetMgr.getLoggingConfig();
    }

    @Override
    public void saveUserSettings() {
        subnetMgr.saveUserSettings(subnet.getName(), userSettings);
    }

    protected void saveSubnet() {
        try {
            subnetMgr.saveSubnet(subnet);
        } catch (SubnetDataNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
