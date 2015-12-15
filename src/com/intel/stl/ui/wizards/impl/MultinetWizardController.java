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
 *  File Name: MultinetWizardController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.28  2015/08/17 18:54:10  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/08/10 17:55:46  robertja
 *  Archive Log:    PR 128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/07/17 20:50:32  jijunwan
 *  Archive Log:    PR 129594 - Apply new input verification on setup wizard
 *  Archive Log:    - forbid switching tabs or apply other actions when there is invalid edit
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/06/10 19:25:58  rjtierne
 *  Archive Log:    PR 128975 - Can not setup application log
 *  Archive Log:    Removed references to the logger
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/05/22 16:21:21  jypak
 *  Archive Log:    PR 128869 - Add online help on setup wizard.
 *  Archive Log:    Added help button to MultinetWizardView. The contents will be update when it's available.
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/05/11 12:35:46  rjtierne
 *  Archive Log:    PR 128585 - Fix errors found by Klocwork and FindBugs
 *  Archive Log:    In onTab(), added null pointer protection to task, and display error if it's null
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/05/05 21:49:42  rjtierne
 *  Archive Log:    Changed onTab() to accept the tab name which is used to select the
 *  Archive Log:    corresponding task
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/05/05 20:01:15  jijunwan
 *  Archive Log:    fixed typo
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/05/01 22:31:52  jijunwan
 *  Archive Log:    changed back to use getMessage for our own exceptions
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/05/01 21:29:16  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/04/28 22:09:03  jijunwan
 *  Archive Log:    removed title argument from #showErrorMessage
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/04/27 21:47:39  rjtierne
 *  Archive Log:    - PR 128358 - Fabric Viewer not Working:
 *  Archive Log:    In method onRun() catch IllegalArgumentException when calling getCurrentFE() and display error
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/04/21 21:17:26  rjtierne
 *  Archive Log:    Added setDirty() to enable/disable Apply and Reset button depending on whether any
 *  Archive Log:    of the wizard views have changed
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/04/02 13:33:04  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/04/01 17:04:17  rjtierne
 *  Archive Log:    Enable/disable welcome Ok button on view before and after the
 *  Archive Log:    configuration thread is executed.  Also updated models at the start
 *  Archive Log:    of configuration thread to fix connection bug
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/03/31 17:49:10  rjtierne
 *  Archive Log:    - Added cancelConfiguration() to cancel configuration thread from view
 *  Archive Log:    - Added some new exception handling
 *  Archive Log:    - Implemented getHostIp() and isHostConnectable()
 *  Archive Log:    - Background thread now calling isHostConnectable() instead of isHostReachable()
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/03/30 15:11:14  rjtierne
 *  Archive Log:    - Added method showStep() to show a task without initializing it
 *  Archive Log:    - Corrected EventWizard table update; now calling showStep() instead of selectStep() in onTab()
 *  Archive Log:    - Added new tryToConnect() to take a subnet so individual backup hosts can be tested
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/03/25 17:56:17  rjtierne
 *  Archive Log:    Added maps to keep track of secure certificate files for each subnet
 *  Archive Log:    and clear key factories at appropriate times
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/03/20 21:05:59  rjtierne
 *  Archive Log:    - Implemented onTab() to update the model and select the current task to fix problem
 *  Archive Log:    with tab selection during subnet creation
 *  Archive Log:    - Only enable the "Run" button when the subnet is fully configured
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/03/16 17:46:00  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/03/11 15:24:27  rjtierne
 *  Archive Log:    Multinet Wizard: Added new logic to save the subnet and update the
 *  Archive Log:    database on a background task with updates to the Welcome panel. Add new inner class
 *  Archive Log:    ConfigureSubnetTask to perform final host reachability, entry validation, and database
 *  Archive Log:    update tasks and update the welcome window.
 *  Archive Log:    updateDatabase() now throws exception so the welcome window is updated with failed status
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/27 15:34:32  rjtierne
 *  Archive Log:    Fixed preferences model/view sync issue when creating new subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/26 22:51:13  rjtierne
 *  Archive Log:    - Added method isHostReachable() to test for the existence of a host
 *  Archive Log:    - Added new functionality to setMultinetTasks() so the first tab (Subnet)
 *  Archive Log:    will always appear when the wizard is first opened
 *  Archive Log:    - Simplified method updateDatabase()
 *  Archive Log:    - Removed outdated feature to display error when switching between
 *  Archive Log:    sub-wizards without saving
 *  Archive Log:    - Added change listener to tabbed pane to update the Next button label
 *  Archive Log:    depending on which tab is selected
 *  Archive Log:    - When a subnet button is clicked, update the subnet in the controller
 *  Archive Log:    to correct model synchronization issues
 *  Archive Log:    - Handle subnet name changes in onNext()
 *  Archive Log:    - Save changes if Run is selected before Apply
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/26 16:20:44  fernande
 *  Archive Log:    Changed showSetupWizard so that the wizard can show its view centered on the calling frame.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/25 17:59:09  rjtierne
 *  Archive Log:    - Using new WizardType enumeration to specify model to update to improve efficiency
 *  Archive Log:    - Passing subnet to the wizard view so the current subnet is highlighted
 *  Archive Log:    - Implemented subnet deletion
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/23 15:06:00  rjtierne
 *  Archive Log:    Added new method getNewWizardStatus() so sub-wizards can tell
 *  Archive Log:    if a new wizard configuration is in progress
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/20 21:13:23  rjtierne
 *  Archive Log:    Multinet Wizard: New instalment of the multinet wizard targeting synchronization of all sub-wizard data with selected subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/13 21:30:27  rjtierne
 *  Archive Log:    Multinet Wizard: Initial Version
 *  Archive Log:
 *
 *  Overview: Top level Multinet Setup Wizard
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.intel.stl.api.configuration.ConfigurationException;
import com.intel.stl.api.configuration.EventRule;
import com.intel.stl.api.configuration.UserNotFoundException;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.api.subnet.SubnetConnectionException;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.main.IFabricController;
import com.intel.stl.ui.main.ISubnetManager;
import com.intel.stl.ui.main.view.IFabricView;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.intel.stl.ui.wizards.impl.event.EventWizardController;
import com.intel.stl.ui.wizards.impl.preferences.PreferencesWizardController;
import com.intel.stl.ui.wizards.impl.subnet.SubnetWizardController;
import com.intel.stl.ui.wizards.model.IModelChangeListener;
import com.intel.stl.ui.wizards.model.IWizardModel;
import com.intel.stl.ui.wizards.model.MultinetWizardModel;
import com.intel.stl.ui.wizards.model.event.EventRulesTableModel;
import com.intel.stl.ui.wizards.model.event.EventsModel;
import com.intel.stl.ui.wizards.model.preferences.PreferencesModel;
import com.intel.stl.ui.wizards.model.subnet.SubnetModel;
import com.intel.stl.ui.wizards.view.IMultinetWizardView;
import com.intel.stl.ui.wizards.view.ITaskView;
import com.intel.stl.ui.wizards.view.MultinetWizardView;
import com.intel.stl.ui.wizards.view.event.EventWizardView;
import com.intel.stl.ui.wizards.view.preferences.PreferencesWizardView;
import com.intel.stl.ui.wizards.view.subnet.SubnetWizardView;

public class MultinetWizardController implements IMultinetWizardListener,
        IModelChangeListener<IWizardModel> {

    private final MultinetWizardView view;

    private final ISubnetManager subnetMgr;

    private final List<IMultinetWizardTask> tasks;

    private IWizardTask currentTask;

    private final IWizardTask firstTask;

    private final IWizardTask lastTask;

    private String userName;

    private UserSettings userSettings;

    @SuppressWarnings("unused")
    private IFabricController controller;

    private boolean isFirstRun;

    private final MultinetWizardModel wizardModel;

    private SubnetWizardView subnetView;

    private static MultinetWizardController instance;

    private SubnetWizardController subnetController;

    private EventWizardController eventController;

    private SubnetDescription subnet;

    private boolean workerStatus;

    private ConfigureSubnetTask configTask;

    /**
     * 
     * Description: Private constructor for the singleton
     * MultinetWizardController
     * 
     * @param view
     *            view for the setup wizard
     */
    private MultinetWizardController(MultinetWizardView view,
            MultinetWizardModel wizardModel, ISubnetManager subnetMgr) {

        this.view = view;
        this.wizardModel = wizardModel;
        this.subnetMgr = subnetMgr;
        this.isFirstRun = subnetMgr.isFirstRun();
        this.view.setWizardListener(this);

        tasks = getTasks(subnetMgr);
        installTasks(tasks);
        firstTask = tasks.get(0);
        lastTask = tasks.get(tasks.size() - 1);

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getSetupWizard(), helpAction.getHelpSet());
    }

    public static MultinetWizardController getInstance(IFabricView owner,
            ISubnetManager subnetMgr) {

        if (instance == null) {
            MultinetWizardModel wizardModel = new MultinetWizardModel();
            MultinetWizardView wizardView =
                    new MultinetWizardView(owner, wizardModel);
            instance =
                    new MultinetWizardController(wizardView, wizardModel,
                            subnetMgr);
        }

        return instance;
    }

    /**
     * 
     * <i>Description: Builds a list of IConfigTask controllers</i>
     * 
     * @return tasks - list of IConfigTask
     */
    protected List<IMultinetWizardTask> getTasks(ISubnetManager subnetMgr) {

        List<IMultinetWizardTask> tasks = new ArrayList<IMultinetWizardTask>();
        List<IModelChangeListener<IWizardModel>> modelListeners =
                new ArrayList<IModelChangeListener<IWizardModel>>();

        // Add this object as a listener to the top model
        modelListeners.add(this);

        // Subnet Wizard
        SubnetModel subnetModel = new SubnetModel();
        subnetView = new SubnetWizardView(view);
        subnetController =
                new SubnetWizardController(subnetView, subnetModel, subnetMgr);
        subnetView.setWizardListener(subnetController);
        wizardModel.setSubnetModel(subnetModel);
        modelListeners.add(subnetController);
        tasks.add(subnetController);

        // Event Wizard
        EventRulesTableModel eventRulesTableModel = new EventRulesTableModel();
        EventsModel eventModel = new EventsModel(eventRulesTableModel);
        EventWizardView eventView =
                new EventWizardView(eventRulesTableModel, view);
        eventController = new EventWizardController(eventView, eventModel);
        eventView.setWizardListener(eventController);
        wizardModel.setEventsModel(eventModel);
        modelListeners.add(eventController);
        tasks.add(eventController);

        // User Preferences
        PreferencesModel preferencesModel = new PreferencesModel();
        PreferencesWizardView preferencesView =
                new PreferencesWizardView(view, preferencesModel);
        PreferencesWizardController preferencesController =
                new PreferencesWizardController(preferencesView,
                        preferencesModel);
        preferencesView.setWizardListener(preferencesController);
        wizardModel.setPreferencesModel(preferencesModel);
        modelListeners.add(preferencesController);
        tasks.add(preferencesController);

        // Register all the sub-wizard controllers with the top model
        for (IModelChangeListener<IWizardModel> listener : modelListeners) {
            wizardModel.addModelListener(listener);
        }

        return tasks;
    }

    protected IWizardTask getTaskByName(String name) {

        boolean found = false;
        IWizardTask currentTask = null;
        Iterator<IMultinetWizardTask> it = tasks.iterator();

        while (!found && it.hasNext()) {

            currentTask = it.next();
            found = (currentTask.getName().equals(name));
        }

        return currentTask;
    }

    protected void installTasks(List<IMultinetWizardTask> tasks) {
        for (IMultinetWizardTask task : tasks) {
            task.setWizardController(this);
        }

        if (subnetMgr.getSubnets().size() > 0) {
            view.setTabs(tasks, false, false);
        }
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

        boolean validTab = view.previousTab();
        if (validTab) {
            selectStep(previousTask.getName());

            if (currentTask != null) {
                // If the first task is current, disable the previous button
                if (currentTask.getName().equals(firstTask.getName())) {
                    view.enablePrevious(false);
                }
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
            currentTask.promoteModel(wizardModel);

            if (success) {
                // At first, enable both buttons
                view.enableNext(true);
                view.enablePrevious(true);

                // Move to the next tab or change to the welcome window
                boolean validTab = view.nextTab();
                if (validTab) {
                    selectStep(tasks.get(tasks.indexOf(currentTask) + 1)
                            .getName());
                }
            }

        } catch (WizardValidationException e) {
            view.showErrorMessage(STLConstants.K0030_ERROR.getValue(),
                    e.getMessage());
        }

        return success;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IMultinetWizardListener#onTab()
     */
    @Override
    public void onTab(String tabName) {
        IWizardTask task =
                (tabName != null) ? getTaskByName(tabName) : currentTask;

        if (task != null) {
            task.promoteModel(wizardModel);
            showStep(tasks.get(tasks.indexOf(task)).getName());
        } else {
            view.showErrorMessage(STLConstants.K0030_ERROR.getValue(),
                    STLConstants.K3043_INVALID_WIZARD_TASK.getValue());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IMultinetWizardListener#onFinish()
     */
    @Override
    public void onFinish() {
        configTask = new ConfigureSubnetTask();
        configTask.execute();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#cancelConfiguration
     * ()
     */
    @Override
    public void cancelConfiguration() {
        if (configTask != null) {
            configTask.cancel(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#validateEntry()
     */
    @Override
    public void validateEntry() {

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {

            @Override
            protected Boolean doInBackground() throws Exception {

                boolean success = false;

                try {
                    success = currentTask.validateUserEntry();
                } catch (WizardValidationException e) {
                    view.showErrorMessage(STLConstants.K0030_ERROR.getValue(),
                            e.getMessage());
                }

                return success;
            }

            @Override
            protected void done() {
                try {
                    workerStatus = get();
                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                    view.showErrorMessage(STLConstants.K0030_ERROR.getValue(),
                            e);
                }
            }
        };
        worker.execute();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IMultinetWizardListener#isValidEntry()
     */
    @Override
    public boolean isValidEntry() {

        return workerStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#setWorkerStatus()
     */
    @Override
    public void resetWorkerStatus() {

        workerStatus = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IMultinetWizardListener#checkHost()
     */
    @Override
    public void checkHost() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#saveConfiguration()
     */
    @Override
    public void saveConfiguration() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#onApply()
     */
    @Override
    public void onApply() {

    }

    protected boolean updateDatabase() throws Exception {

        boolean result = false;

        // Save the current subnet to the database, update the models/maps
        boolean success = saveSubnet();

        if (success) {
            // Get the current user settings from the subnet manager
            SubnetDescription subnetDescription = view.getSelectedSubnet();
            String currentSubnetName = null;
            if (subnetDescription != null) {
                currentSubnetName = subnetDescription.getName();
            }
            UserSettings userSettings =
                    subnetMgr.getUserSettings(currentSubnetName, userName);

            // Save the event rules to the user settings
            EventRulesTableModel eventRulesTable =
                    wizardModel.getEventsModel().getEventsRulesModel();
            userSettings.setEventRules(eventRulesTable.getEventRules());

            // Save the user preferences to the user settings
            PreferencesModel preferencesModel =
                    wizardModel.getPreferencesModel();
            userSettings.setPreferences(preferencesModel.getPreferencesMap());

            // /////////////////////////DEBUG//////////////////////////
            // List<String> recipients = new ArrayList<String>();
            // recipients.add("robert.amato@intel.com");
            // userSettings.setMailRecipients(recipients);
            // // ////////////////////////////////////////////////////////
            // Save the user settings to the database
            subnetMgr.saveUserSettings(currentSubnetName, userSettings);

            // Notify view that new wizard is complete
            view.setNewWizardInProgress(false);

            // Loop through all tasks and set dirty to false
            for (IMultinetWizardTask task : tasks) {
                task.setDirty(false);
            }

            result = true;
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#onClose()
     */
    @Override
    public void onClose() {

        view.closeWizard();
        this.isFirstRun = false;
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

        // Get the task by name
        IWizardTask task = getTaskByName(taskName);

        // Initialize the current task and show the view
        if (task != null) {
            currentTask = task;
            currentTask.init();
        }
        view.showTaskView(taskName);

        // If this is not the last task, keep next button "Next"
        if (lastTask != null) {
            if (task != null && !task.getName().equals(lastTask.getName())) {
                view.updateNextButton(STLConstants.K0622_NEXT.getValue());
            }
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

        // Get the task by name
        IWizardTask task = getTaskByName(taskName);

        // Initialize the current task and show the view
        currentTask = task;
        view.showTaskView(taskName);

        // If this is not the last task, keep next button "Next"
        if (lastTask != null) {
            if (task != null && !task.getName().equals(lastTask.getName())) {
                view.updateNextButton(STLConstants.K0622_NEXT.getValue());
            }
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

        this.subnet = subnet;
        if ((subnet.getName() == null) && (subnetMgr.getSubnets().size() > 0)) {
            subnet = subnetMgr.getSubnets().get(0);
        }

        this.userName = userName;
        IFabricView mainFrame = callingController.getView();

        List<SubnetDescription> subnets = subnetMgr.getSubnets();
        if (subnets.size() > 0) {
            updateModels(subnet);
            view.initButtonMap(subnetMgr.getSubnets(), subnet);
        }

        try {
            this.userSettings =
                    subnetMgr.getUserSettings(subnet.getName(), userName);
        } catch (UserNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // By default select first step
        for (IWizardTask task : tasks) {
            currentTask = task;
            selectStep(task.getName());
        }
        currentTask = tasks.get(0);
        view.enablePrevious(false);

        view.showWizard(subnet, subnetMgr.isFirstRun(), mainFrame);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IMultinetWizardListener#getView()
     */
    @Override
    public IMultinetWizardView getView() {

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
     * @see com.intel.stl.ui.wizards.impl.IMultinetWizardListener#onRun()
     */
    @Override
    public boolean onRun() {

        boolean result = false;
        String hostName = null;
        String subnetName = wizardModel.getSubnetModel().getSubnet().getName();

        try {
            hostName =
                    wizardModel.getSubnetModel().getSubnet().getCurrentFE()
                            .getHost();
        } catch (IllegalArgumentException e) {
            Util.showError(view, e);
        }

        if (isHostConnectable()) {
            try {
                subnetMgr.startSubnet(subnetName);
                result = true;
            } catch (SubnetConnectionException e) {
                view.showErrorMessage(STLConstants.K0030_ERROR.getValue(),
                        e.getMessage(),
                        STLConstants.K2004_CONNECTION.getValue());
            }
        } else {
            view.showErrorMessage(STLConstants.K0030_ERROR.getValue(),
                    UILabels.STL50050_CONNECTION_FAIL.getDescription(
                            subnetName, hostName));
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#onReset()
     */
    @Override
    public void onReset() {

        for (IWizardTask task : tasks) {
            task.onReset();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#onNewSubnet()
     */
    @Override
    public void onNewSubnet() {

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
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#deleteSubnet(com
     * .intel.stl.api.subnet.SubnetDescription)
     */
    @Override
    public void deleteSubnet(SubnetDescription subnet) {
        try {
            subnetMgr.removeSubnet(subnet);
        } catch (SubnetDataNotFoundException e) {
            view.showErrorMessage(
                    STLConstants.K0329_PORT_SUBNET_MANAGER.getValue() + " "
                            + STLConstants.K0030_ERROR.getValue(),
                    e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#getSubnets()
     */
    @Override
    public List<SubnetDescription> getSubnets() {

        return subnetMgr.getSubnets();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IMultinetWizardListener#getTasks()
     */
    @Override
    public List<IMultinetWizardTask> getTasks() {

        return tasks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.model.IModelChangeListener#onModelChange(com
     * .intel.stl.ui.wizards.model.IWizardModel)
     */
    @Override
    public void onModelChange(IWizardModel m) {

        MultinetWizardModel model = (MultinetWizardModel) m;

        // Update the navigation panel
        view.updateNavPanel();

        // Update the subnetMap in the view
        view.updateSubnetMap(model.getSubnetModel().getSubnet());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#getHostIp(java.
     * lang.String)
     */
    @Override
    public String getHostIp(String hostName) {

        String ipAddress = null;

        try {
            ipAddress = subnetMgr.getHostIp(hostName);
        } catch (SubnetConnectionException e) {
            view.showErrorMessage(STLConstants.K0030_ERROR.getValue(),
                    e.getMessage());
        }

        return ipAddress;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IMultinetWizardListener#isReachable()
     */
    @Override
    public boolean isHostReachable() {

        return subnetMgr.isHostReachable(wizardModel.getSubnetModel()
                .getSubnet().getCurrentFE().getHost());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#isHostConnectable()
     */
    @Override
    public boolean isHostConnectable() {

        boolean result = false;

        try {
            result =
                    subnetMgr.isHostConnectable(wizardModel.getSubnetModel()
                            .getSubnet());
        } catch (ConfigurationException e) {
            view.showErrorMessage(STLConstants.K2004_CONNECTION.getValue()
                    + " " + STLConstants.K0030_ERROR.getValue(), e.getMessage());
        }

        return result;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#tryToConnect()
     */
    @Override
    public boolean tryToConnect() {

        boolean result = false;

        try {
            result =
                    subnetMgr.tryToConnect(wizardModel.getSubnetModel()
                            .getSubnet());
        } catch (SubnetConnectionException e) {
            view.showErrorMessage(STLConstants.K2004_CONNECTION.getValue()
                    + " " + STLConstants.K0030_ERROR.getValue(), e.getMessage());
        }
        return result;
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

        boolean result = false;

        try {
            result = subnetMgr.tryToConnect(subnet);
        } catch (SubnetConnectionException e) {
            view.showErrorMessage(STLConstants.K2004_CONNECTION.getValue()
                    + " " + STLConstants.K0030_ERROR.getValue(), e.getMessage());
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#getPMConfig()
     */
    @Override
    public PMConfigBean getPMConfig() {

        PMConfigBean pmConfigBean = null;
        pmConfigBean =
                subnetMgr.getPMConfig(wizardModel.getSubnetModel().getSubnet());

        return pmConfigBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#getTaskScheduler()
     */
    @Override
    public TaskScheduler getTaskScheduler() {

        return subnetMgr.getTaskScheduler(wizardModel.getSubnetModel()
                .getSubnet());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#saveUserSettings()
     */
    @Override
    public void saveUserSettings() {

        subnetMgr.saveUserSettings(wizardModel.getSubnetModel().getSubnet()
                .getName(), userSettings);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardListener#getSubnet()
     */
    @Override
    public SubnetDescription getSubnet() {

        return wizardModel.getSubnetModel().getSubnet();
    }

    protected boolean saveSubnet() {

        boolean result = false;

        try {
            SubnetDescription savedSubnet =
                    subnetMgr.saveSubnet(wizardModel.getSubnetModel()
                            .getSubnet());

            // Update the subnet map with the saved subnet containing the ID
            view.updateMaps(savedSubnet);

            // Update the Subnet Model with the saved subnet (containing the
            // ID)
            wizardModel.getSubnetModel().setSubnet(savedSubnet);
            wizardModel.notifyModelChange(WizardType.SUBNET);
            result = true;

        } catch (SubnetDataNotFoundException e) {
            view.showErrorMessage(STLConstants.K2004_CONNECTION.getValue()
                    + " " + STLConstants.K0030_ERROR.getValue(), e.getMessage());
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IMultinetWizardListener#getNewSubnet()
     */
    @Override
    public SubnetDescription getNewSubnet() {
        return subnetController.getNewSubnet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#retrieveUserSettings
     * ()
     */
    @Override
    public UserSettings retrieveUserSettings() {

        return retrieveUserSettings(subnet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#retrieveUserSettings
     */
    @Override
    public UserSettings retrieveUserSettings(SubnetDescription subnet) {

        UserSettings userSettings = null;

        try {
            userSettings =
                    subnetMgr.getUserSettings(subnet.getName(), userName);

        } catch (UserNotFoundException e) {
            view.showErrorMessage(STLConstants.K0030_ERROR.getValue(),
                    e.getMessage());
        }
        return userSettings;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#getCurrentSubnet()
     */
    @Override
    public SubnetDescription getCurrentSubnet() {
        return view.getSelectedSubnet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#getCurrentTask()
     */
    @Override
    public IWizardTask getCurrentTask() {
        return currentTask;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#setCurrentTask(
     * com.intel.stl.ui.wizards.impl.IWizardTask)
     */
    @Override
    public void setCurrentTask(int taskPosition) {
        if (taskPosition >= 0) {
            this.currentTask = tasks.get(taskPosition);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IMultinetWizardListener#updateModels()
     */
    @Override
    public void updateModels(SubnetDescription subnet) {

        // Set the Subnet model
        wizardModel.getSubnetModel().setSubnet(subnet);

        // Set the Events model
        UserSettings userSettings = retrieveUserSettings(subnet);
        List<EventRule> userEventRules = null;
        Map<String, Properties> preferencesMap = null;
        if (userSettings != null) {
            userEventRules = userSettings.getEventRules();
            preferencesMap = userSettings.getPreferences();
        }
        EventRulesTableModel eventRulesTableModel =
                eventController.updateEventRulesTableModel(userEventRules);
        wizardModel.getEventsModel().setEventsRulesModel(eventRulesTableModel);

        // Set the Preferences Model
        wizardModel.getPreferencesModel().setPreferencesMap(preferencesMap);
        // Update the all models
        wizardModel.notifyModelChange();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IMultinetWizardListener#clearTasks()
     */
    @Override
    public void clearTasks() {

        for (IMultinetWizardTask task : tasks) {
            task.clear();
            task.promoteModel(wizardModel);
        }

        // Update all models
        wizardModel.notifyModelChange();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#getNewWizardStatus
     * ()
     */
    @Override
    public boolean isNewWizard() {

        return view.getNewWizardStatus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#setCurrentSubnet
     * (com.intel.stl.api.subnet.SubnetDescription)
     */
    @Override
    public void setCurrentSubnet(SubnetDescription subnet) {

        this.subnet = subnet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#getSubnetView()
     */
    @Override
    public SubnetWizardView getSubnetView() {

        return subnetView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#clearSubnetFactories
     * ()
     */
    @Override
    public void clearSubnetFactories(SubnetDescription subnet) {
        subnetMgr.clearSubnetFactories(subnet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean dirty) {
        view.enableApply(dirty);
        view.enableReset(dirty);
    }

    private class ConfigureSubnetTask extends
            SwingWorker<Void, ConfigTaskStatus> {

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.SwingWorker#doInBackground()
         */
        @Override
        protected Void doInBackground() throws Exception {

            // Disable the Ok button on the welcome panel
            view.setWelcomeOkEnabled(false);

            // Update the models and promote
            for (IWizardTask task : tasks) {
                task.updateModel();
                task.promoteModel(wizardModel);
            }

            // Check if the host is reachable and publish the result
            view.setProgress(ConfigTaskType.CHECK_HOST);
            boolean isConnectable = isHostConnectable();
            ConfigTaskStatus connectable =
                    new ConfigTaskStatus(ConfigTaskType.CHECK_HOST,
                            isConnectable);
            publish(connectable);

            // If host is reachable validate and publish
            view.setProgress(ConfigTaskType.VALIDATE_ENTRY);

            int numPass = 0;
            boolean status = false;
            for (IWizardTask task : tasks) {
                task.setConnectable(isConnectable);
                boolean pass = task.validateUserEntry();
                if (pass) {
                    numPass++;
                }
            }
            status = (numPass == tasks.size()) ? true : false;

            currentTask.promoteModel(wizardModel);
            ConfigTaskStatus valid =
                    new ConfigTaskStatus(ConfigTaskType.VALIDATE_ENTRY, status);
            publish(valid);

            try {
                // Update the database and publish the result
                view.setProgress(ConfigTaskType.UPDATE_DATABASE);
                ConfigTaskStatus updated =
                        new ConfigTaskStatus(ConfigTaskType.UPDATE_DATABASE,
                                updateDatabase());
                publish(updated);
            } catch (Exception e) {
                publish(new ConfigTaskStatus(ConfigTaskType.UPDATE_DATABASE,
                        false));
            }

            return null;
        }

        /*-
         * Note that process runs on the EDT
         * @see http://docs.oracle.com/javase/7/docs/api/javax/swing/SwingWorker.html#process(java.util.List)
         */
        @Override
        protected void process(List<ConfigTaskStatus> statusList) {

            for (ConfigTaskStatus status : statusList) {
                view.updateConfigStatus(status);
            }
        }

        @Override
        protected void done() {

            try {
                get();
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            view.markTasksClean();
            view.enableNavPanel(true);
            view.enableSubnetModifiers(true);

            // Enable the Ok button on the welcome panel
            view.setWelcomeOkEnabled(true);

        }
    } // class ConfigureSubnetTask

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardListener#onEmailTest(java
     * .lang.String)
     */
    @Override
    public void onEmailTest(String recipients) {
        subnetMgr.onEmailTest(recipients);
    }
}
