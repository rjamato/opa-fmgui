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
 *  File Name: PreferencesWizardController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.28  2015/08/17 18:54:52  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/08/12 19:28:15  fisherma
 *  Archive Log:    Store/retrieve SMTP settings in/from SECTION_PREFERENCE properties.  Cleanup unused interface.
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/08/10 17:55:51  robertja
 *  Archive Log:    PR 128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/07/17 20:48:28  jijunwan
 *  Archive Log:    PR 129594 - Apply new input verification on setup wizard
 *  Archive Log:    - introduced isEditValid to allow us check whether we have valid edit
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/05/01 21:29:10  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/04/28 22:09:00  jijunwan
 *  Archive Log:    removed title argument from #showErrorMessage
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/04/21 21:17:52  rjtierne
 *  Archive Log:    - In setDirty(), call the setDirty() method in the MultinetWizardController to
 *  Archive Log:    set the Apply and Reset button enables in its view.
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/03/31 17:48:51  rjtierne
 *  Archive Log:    - Added setConnectable()
 *  Archive Log:    - Checking connectable instead of calling isHostReachable() in validateUserEntry()
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/03/30 15:11:39  rjtierne
 *  Archive Log:    Catching ConfigurationException in validateUserEntry() when reachable hosts can't be validated
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/03/23 15:14:43  rjtierne
 *  Archive Log:    Restored the refresh rate update feature.  The refresh rate is now updated in the
 *  Archive Log:    TaskScheduler for configured subnets.  This will not work for subnets that
 *  Archive Log:    are being configured since no context is present.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/03/11 15:24:40  rjtierne
 *  Archive Log:    Multinet Wizard: In validateUserEntry(), update the preferences model with the latest user entries only once,
 *  Archive Log:    and return success status based on successful validation and reachability
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/02/27 15:34:30  rjtierne
 *  Archive Log:    Fixed preferences model/view sync issue when creating new subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/02/26 22:52:29  rjtierne
 *  Archive Log:    Only attempt to get PMConfig if host is reachable
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/02/25 17:59:23  rjtierne
 *  Archive Log:    Initialized pmConfig to null
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/02/23 15:06:33  rjtierne
 *  Archive Log:    Simplified processing of refresh rate units
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/02/20 21:13:26  rjtierne
 *  Archive Log:    Multinet Wizard: New instalment of the multinet wizard targeting synchronization of all sub-wizard data with selected subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/02/13 21:31:53  rjtierne
 *  Archive Log:    Multinet Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/02/10 23:18:06  jijunwan
 *  Archive Log:    changed to store refreshRate rather than refreshRateinSeconds, store TimeUnit by name rather than ordinary
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/09 21:56:37  jijunwan
 *  Archive Log:    put save task in SwingWorker
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/06 17:36:42  fernande
 *  Archive Log:    Fixed UserSettings not being saved off to the database in the SetupWizard
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/06 15:12:34  fernande
 *  Archive Log:    Changes so that the Setup Wizard depends on the Subnet Manager for all subnet-related operations
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/02 20:38:13  fernande
 *  Archive Log:    Fixing the SetupWizard so that it can define new subnets. Fixed also StackOverflowError exception when switching subnets.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/02 15:26:53  rjtierne
 *  Archive Log:    Gets subnet from the main setup wizard as part of new
 *  Archive Log:    Multi-subnet architecture
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/30 20:29:15  fernande
 *  Archive Log:    Initial changes to support multiple fabric viewers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/21 21:21:19  rjtierne
 *  Archive Log:    Supplying preferences wizard with sweep interval through Context
 *  Archive Log:    for comparison with refresh rate supplied by user input. Also providing
 *  Archive Log:    task scheduler to preferences wizard so user supplied refresh rate can
 *  Archive Log:    be updated.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/20 19:12:47  rjtierne
 *  Archive Log:    Validating preferences wizard user input before; only storing info if valid.
 *  Archive Log:    On first run, maintain user entries when navigating between wizards.
 *  Archive Log:    Changed onApply() to return a boolean to indicate success/failure
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/15 19:09:23  rjtierne
 *  Archive Log:    Provide access to user settings, retrieve user performance preferences and store in view
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/13 19:00:41  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Controller for the User Preferences Wizard
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.impl.preferences;

import javax.swing.JComponent;

import com.intel.stl.api.configuration.ConfigurationException;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.intel.stl.ui.wizards.impl.IMultinetWizardListener;
import com.intel.stl.ui.wizards.impl.IMultinetWizardTask;
import com.intel.stl.ui.wizards.impl.IWizardListener;
import com.intel.stl.ui.wizards.impl.InteractionType;
import com.intel.stl.ui.wizards.impl.WizardValidationException;
import com.intel.stl.ui.wizards.model.IModelChangeListener;
import com.intel.stl.ui.wizards.model.IWizardModel;
import com.intel.stl.ui.wizards.model.MultinetWizardModel;
import com.intel.stl.ui.wizards.model.preferences.PreferencesModel;
import com.intel.stl.ui.wizards.view.preferences.PreferencesWizardView;

public class PreferencesWizardController implements IMultinetWizardTask,
        IModelChangeListener<IWizardModel> {

    private final PreferencesWizardView view;

    private PreferencesModel preferencesModel;

    @SuppressWarnings("unused")
    private IWizardListener wizardController;

    private IMultinetWizardListener multinetWizardController;

    private boolean done;

    private PreferencesInputValidator validator;

    private boolean firstPass = true;

    private boolean connectable;

    public PreferencesWizardController(PreferencesWizardView view) {
        this.view = view;
    }

    public PreferencesWizardController(PreferencesWizardView view,
            PreferencesModel preferencesModel) {
        this(view);
        view.setDirty(false);
        this.preferencesModel = preferencesModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#getName()
     */
    @Override
    public String getName() {

        return STLConstants.K3005_PREFERENCES.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#getView()
     */
    @Override
    public JComponent getView() {

        return view;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#init()
     */
    @Override
    public void init() {

        // Singleton logging validator
        validator = PreferencesInputValidator.getInstance();

        if (firstPass || !multinetWizardController.isNewWizard()) {
            view.resetPanel();
        }

        firstPass = false;
        view.setDirty(false);
        done = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#setDone(boolean)
     */
    @Override
    public void setDone(boolean done) {

        this.done = done;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#isDone()
     */
    @Override
    public boolean isDone() {

        return done;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#onApply()
     */
    @Override
    public boolean validateUserEntry() throws WizardValidationException {

        boolean success = false;

        // Only try to get the PMConfig if the host is reachable
        int sweepInterval = 0;
        if (connectable) {

            try {
                // The call to getPMConfig() may fail silently since the user
                // may be setting up subnets that are known to be disconnected
                PMConfigBean pmConfig = null;
                pmConfig = multinetWizardController.getPMConfig();

                if (pmConfig != null) {
                    sweepInterval = pmConfig.getSweepInterval();
                }

            } catch (ConfigurationException e) {
            } catch (Exception e) {
                Util.showError(view, e);
                return false;
            }
        }

        // Since it is possible to be unable to connect to the host,
        // update the model whether it is valid or not
        updateModel();

        int errorCode = validator.validate(preferencesModel, sweepInterval);
        if (errorCode == PreferencesValidatorError.OK.getId()) {
            // Update the refresh rate in the task scheduler
            TaskScheduler taskScheduler =
                    multinetWizardController.getTaskScheduler();
            if (taskScheduler != null) {
                taskScheduler.updateRefreshRate(Integer
                        .valueOf(preferencesModel.getRefreshRateInSeconds()));
            }

            success = true;
        } else {
            view.logMessage(PreferencesValidatorError.getValue(errorCode));

        }

        return (success && connectable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#onPrevious()
     */
    @Override
    public void onPrevious() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#onReset()
     */
    @Override
    public void onReset() {

        view.resetPanel();
        view.setDirty(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#cleanup()
     */
    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardTask#selectStep(java.lang.String)
     */
    @Override
    public void selectStep(String taskName) {

        multinetWizardController.selectStep(taskName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#isDirty()
     */
    @Override
    public boolean isDirty() {

        return view.isDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean dirty) {

        view.setDirty(dirty);
        multinetWizardController.setDirty(dirty);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardTask#doInteractiveAction(com.intel
     * .stl.ui.wizards.impl.InteractionType, java.lang.Object[])
     */
    @Override
    public void doInteractiveAction(InteractionType action, Object... data) {

        switch (action) {

            case CHANGE_WIZARDS:

                if (data == null) {
                    return;
                }

                String taskName = (String) data[0];

                if (taskName != null) {
                    onReset();
                    view.closeStatusPanel();
                    selectStep(taskName);
                }
                break;

            case SAVE_LOGGING:
                // NOP
                break;

            default:
                break;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#updateModel()
     */
    @Override
    public void updateModel() {

        // Update the local preferences model
        preferencesModel.setRefreshRateInSeconds(view.getRefreshRate());
        preferencesModel.setRefreshRateUnits(view.getRefreshRateUnits());
        preferencesModel
                .setTimingWindowInSeconds(view.getTimeWindowInSeconds());
        preferencesModel.setNumWorstNodes(view.getNumWorstNodes());
        preferencesModel.setMailRecipients(view.getEmailList());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardTask#promoteModel(com.intel.stl.
     * ui.wizards.model.MultinetWizardModel)
     */
    @Override
    public void promoteModel(MultinetWizardModel topModel) {

        // Promote the preferences model to the top model
        topModel.setPreferencesModel(preferencesModel);
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
        view.updateView(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardTask#setWizardController(com.intel
     * .stl.ui.wizards.impl.IMultinetWizardListener)
     */
    @Override
    public void setWizardController(IMultinetWizardListener controller) {
        multinetWizardController = controller;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardTask#setWizardController(com.intel
     * .stl.ui.wizards.impl.IWizardListener)
     */
    @Override
    public void setWizardController(IWizardListener controller) {

        this.wizardController = controller;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#clear()
     */
    @Override
    public void clear() {
        view.clearPanel();
        preferencesModel.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#setConnectable(boolean)
     */
    @Override
    public void setConnectable(boolean connectable) {
        this.connectable = connectable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#isEditValid()
     */
    @Override
    public boolean isEditValid() {
        return view.isEditValid();
    }

    public void onEmailTest(String recipients) {
        multinetWizardController.onEmailTest(recipients);
    }

}
