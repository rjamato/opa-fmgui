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
 *  File Name: LogginController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11.2.1  2015/08/12 15:27:33  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/04/02 13:33:00  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/03/31 17:48:30  rjtierne
 *  Archive Log:    Added/Implemented setConnectable()
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/20 21:14:35  rjtierne
 *  Archive Log:    Multinet Wizard: Kept logging wizard up to date with its interface, but will be moved to the configuration menu later.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/13 21:32:00  rjtierne
 *  Archive Log:    Multinet Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/06 15:12:17  fernande
 *  Archive Log:    Changes so that the Setup Wizard depends on the Subnet Manager for all subnet-related operations
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/20 19:13:24  rjtierne
 *  Archive Log:    Changed onApply() to return a boolean to indicate success/failure
 *  Archive Log:    Fixed document listener to detect changes in user entries
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/11 21:48:21  jijunwan
 *  Archive Log:    setup wizard improvements
 *  Archive Log:    1) look and feel adjustment
 *  Archive Log:    2) secure FE support
 *  Archive Log:    3) apply wizard on current subnet
 *  Archive Log:    4) message display based on message type rather than directly specifying UI resources
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/23 18:34:25  rjtierne
 *  Archive Log:    New logic to make first-run wizards retain information when switching between wizards
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/12/19 18:51:51  rjtierne
 *  Archive Log:    Initialize appender map & view. Updated onApply to pass logger selections through the input validator
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/10 21:31:09  rjtierne
 *  Archive Log:    New Setup Wizard based on framework
 *  Archive Log:
 *
 *  Overview: Controller for the Logging Wizard
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.impl.logging;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.intel.stl.api.configuration.AppenderConfig;
import com.intel.stl.api.configuration.RollingFileAppender;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.wizards.impl.IWizardListener;
import com.intel.stl.ui.wizards.impl.IWizardTask;
import com.intel.stl.ui.wizards.impl.InteractionType;
import com.intel.stl.ui.wizards.impl.WizardValidationException;
import com.intel.stl.ui.wizards.model.MultinetWizardModel;
import com.intel.stl.ui.wizards.view.logging.LoggingWizardView;

public class LoggingWizardController implements IWizardTask, ILoggingControl {

    private final LoggingWizardView view;

    private List<AppenderConfig> appenders;

    private RollingFileAppender rollingFileAppender;

    private HashMap<String, AppenderConfig> appenderConfigMap = null;

    private boolean done;

    private IWizardListener wizardController;

    private LoggingInputValidator validator;

    private boolean firstPass = true;

    @SuppressWarnings("unused")
    private boolean connectable;

    public LoggingWizardController(LoggingWizardView view) {
        this.view = view;
        this.view.setWizardListener(this);
        this.view.setLoggingControlListener(this);
    }

    protected void initAppenderList() {

        // Get the appender from the Configuration API
        appenders = wizardController.getLoggingConfig();
        if (appenders != null) {
            appenderConfigMap = new HashMap<String, AppenderConfig>();

            for (int i = 0; i < appenders.size(); i++) {
                AppenderConfig appenderConfig = appenders.get(i);
                appenderConfigMap.put(appenderConfig.getName(), appenderConfig);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IConfigTask#getName()
     */
    @Override
    public String getName() {

        return STLConstants.K0669_LOGGING.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IConfigTask#getView()
     */
    @Override
    public JComponent getView() {

        return view;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IConfigTask#init()
     */
    @Override
    public void init() {

        // Singleton logging validator
        validator = LoggingInputValidator.getInstance();

        if (wizardController.isFirstRun() && firstPass) {
            initAppenderList();
            view.initView(appenderConfigMap);
        } else {

            if (!wizardController.isFirstRun()) {
                initAppenderList();
                view.initView(appenderConfigMap);
            }
        }

        firstPass = false;
        view.setDirty(false);
        done = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IConfigTask#setDone(boolean)
     */
    @Override
    public void setDone(boolean done) {
        this.done = done;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IConfigTask#isDone()
     */
    @Override
    public boolean isDone() {

        return done;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IConfigTask#onApply()
     */
    @Override
    // Note: This method used to be called onApply()
    public boolean validateUserEntry() throws WizardValidationException {

        boolean success = false;

        rollingFileAppender =
                (RollingFileAppender) appenderConfigMap
                        .get(STLConstants.K3003_ROLLING_FILE_APPENDER
                                .getValue());
        view.updateAppender(rollingFileAppender);

        int errorCode = validator.validate(rollingFileAppender);
        if (errorCode == LoggingValidatorError.OK.getId()) {
            commitChanges();
            success = true;
        } else {
            view.showMessage(LoggingValidatorError.getValue(errorCode), null,
                    JOptionPane.ERROR_MESSAGE, (Object[]) null);
        }

        return success;
    }

    public void commitChanges() {

        boolean found = false;
        Iterator<AppenderConfig> it = appenders.iterator();

        while (it.hasNext() && !found) {
            AppenderConfig appender = it.next();
            if ((appender instanceof RollingFileAppender)
                    && (appender.getName()
                            .equals(STLConstants.K3003_ROLLING_FILE_APPENDER
                                    .getValue()))) {
                appender = rollingFileAppender;
                found = true;
            }
        }

        wizardController.saveLoggingConfiguration(appenders);
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

        wizardController.selectStep(taskName);
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

        initAppenderList();
        view.initView(appenderConfigMap);
        view.setDirty(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.logging.ILoggingControl#getAppender(java
     * .lang.String)
     */
    @Override
    public AppenderConfig getAppender(String name) {

        return appenderConfigMap.get(name);
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardTask#doInteractiveAction(com.intel
     * .stl.ui.wizards.impl.InteractionType, java.lang.Object)
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

                break;

            default:
                break;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.logging.ILoggingControl#getAppenderName()
     */
    @Override
    public String getAppenderName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFileLocation(String appenderName) {
        AppenderConfig appenderConfig = appenderConfigMap.get(appenderName);
        RollingFileAppender appender = null;
        String location = null;
        if (appenderConfig != null) {
            appender = (RollingFileAppender) appenderConfig;
            location = appender.getFileLocation();
        }
        return location;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#updateModel()
     */
    @Override
    public void updateModel() {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardTask#setWizardController(com.intel
     * .stl.ui.wizards.impl.SetupWizardController)
     */
    @Override
    public void setWizardController(IWizardListener controller) {
        wizardController = controller;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#clear()
     */
    @Override
    public void clear() {
        view.clearPanel();
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

}
