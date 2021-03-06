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
 *  File Name: LoggingConfigController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/08/17 18:54:26  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/06/26 13:38:13  rjtierne
 *  Archive Log:    PR 128975 - Can not setup application log
 *  Archive Log:    - During initialization, retrieve the root log level from the root logger in LoggingConfiguration
 *  Archive Log:    - Before saving logging configuration, store the root log level in the LoggerConfig
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/06/10 19:24:07  rjtierne
 *  Archive Log:    PR 128975 - Can not setup application log
 *  Archive Log:    Moved from the wizards package to the new logger package with updates to accommodate
 *  Archive Log:    changes to the LoggingConfiguration in the back end
 *  Archive Log:    - LoggingWizardController renamed to LoggingConfigController
 *  Archive Log:    - LoggingWizardView renamed to LoggingConfigView
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
package com.intel.stl.ui.logger.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.intel.stl.api.configuration.AppenderConfig;
import com.intel.stl.api.configuration.LoggerConfig;
import com.intel.stl.api.configuration.LoggingConfiguration;
import com.intel.stl.api.configuration.LoggingThreshold;
import com.intel.stl.api.configuration.RollingFileAppender;
import com.intel.stl.ui.logger.config.view.LoggingConfigView;
import com.intel.stl.ui.main.ISubnetManager;
import com.intel.stl.ui.main.view.FVMainFrame;
import com.intel.stl.ui.wizards.impl.WizardValidationException;

public class LoggingConfigController implements ILoggingControl {

    private static LoggingConfigController instance;

    private final LoggingConfigView view;

    private List<AppenderConfig> appenders;

    private LoggingThreshold rootLogLevel;

    private HashMap<String, AppenderConfig> appenderConfigMap = null;

    private boolean done;

    private final ISubnetManager subnetMgr;

    private LoggingInputValidator validator;

    @SuppressWarnings("unused")
    private boolean connectable;

    private LoggingConfigController(LoggingConfigView view,
            ISubnetManager subnetMgr) {
        this.view = view;
        this.view.setLoggingControlListener(this);
        this.subnetMgr = subnetMgr;
    }

    public static LoggingConfigController getInstance(FVMainFrame owner,
            ISubnetManager subnetMgr) {

        if (instance == null) {
            instance =
                    new LoggingConfigController(new LoggingConfigView(owner),
                            subnetMgr);

        }

        return instance;
    }

    protected void initAppenderList() {

        // Get the appender from the Configuration API
        appenders = getLoggingConfig().getAppenders();
        if (appenders != null) {
            appenderConfigMap = new HashMap<String, AppenderConfig>();

            for (int i = 0; i < appenders.size(); i++) {
                AppenderConfig appenderConfig = appenders.get(i);
                appenderConfigMap.put(appenderConfig.getName(), appenderConfig);
            }
        }
    }

    protected void init() {

        // Singleton logging validator
        validator = LoggingInputValidator.getInstance();

        rootLogLevel = getLoggingConfig().getRootLogger().getLevel();
        initAppenderList();
        view.initView(appenderConfigMap);
        view.setDirty(false);
        done = false;
    }

    // protected void saveLoggingConfig(List<AppenderConfig> appenders) {
    protected void saveLoggingConfig(LoggingConfiguration loggingConfig) {

        subnetMgr.saveLoggingConfiguration(loggingConfig);
    }

    // public List<AppenderConfig> getLoggingConfig() {
    public LoggingConfiguration getLoggingConfig() {

        return subnetMgr.getLoggingConfig();
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {

        return done;
    }

    @Override
    public boolean onOk() throws WizardValidationException {

        boolean success = false;

        // Create the logging configuration
        LoggingConfiguration loggingConfig = new LoggingConfiguration();
        List<AppenderConfig> appenders =
                new ArrayList<AppenderConfig>(appenderConfigMap.values());
        loggingConfig.setAppenders(appenders);
        loggingConfig.setRootLogger(new LoggerConfig("Root", rootLogLevel));
        view.updateAppender(loggingConfig);

        // Validate the logging configuration
        int errorCode = validator.validate(loggingConfig);
        if (errorCode == LoggingValidatorError.OK.getId()) {
            saveLoggingConfig(loggingConfig);
            success = true;
        } else {
            view.showErrorMessage(LoggingValidatorError.getValue(errorCode));
        }

        return success;
    }

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

    public boolean isDirty() {

        return view.isDirty();
    }

    public void setDirty(boolean dirty) {

        view.setDirty(dirty);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.logging.ILoggingControl#getAppenderName()
     */
    @Override
    public String getAppenderName() {
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

    public void updateModel() {
        // TODO Auto-generated method stub

    }

    public void clear() {
        view.clearPanel();
    }

    public void setConnectable(boolean connectable) {
        this.connectable = connectable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.logger.config.ILoggingControl#showLoggingConfig()
     */
    @Override
    public void showLoggingConfig() {
        init();
        view.showLoggingConfig();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.logger.config.ILoggingControl#getRootLoggingLevel()
     */
    @Override
    public LoggingThreshold getRootLoggingLevel() {
        return rootLogLevel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.logger.config.ILoggingControl#setRootLoggingLevel(com
     * .intel.stl.api.configuration.LoggingThresholdViz)
     */
    @Override
    public void setRootLoggingLevel(LoggingThreshold level) {
        rootLogLevel = level;
    }

}
