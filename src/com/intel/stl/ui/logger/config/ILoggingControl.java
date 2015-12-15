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
 *  File Name: ILoggingControl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/08/17 18:54:26  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/06/10 19:24:07  rjtierne
 *  Archive Log:    PR 128975 - Can not setup application log
 *  Archive Log:    Moved from the wizards package to the new logger package with updates to accommodate
 *  Archive Log:    changes to the LoggingConfiguration in the back end
 *  Archive Log:    - LoggingWizardController renamed to LoggingConfigController
 *  Archive Log:    - LoggingWizardView renamed to LoggingConfigView
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/12/19 18:44:14  rjtierne
 *  Archive Log:    Added new method getFileLocation()
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/10 21:31:09  rjtierne
 *  Archive Log:    New Setup Wizard based on framework
 *  Archive Log:
 *
 *  Overview: Interface customized for the Logging Wizard Controller
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.logger.config;

import com.intel.stl.api.configuration.AppenderConfig;
import com.intel.stl.api.configuration.LoggingThreshold;
import com.intel.stl.ui.wizards.impl.WizardValidationException;

public interface ILoggingControl {

    public String getAppenderName();

    public AppenderConfig getAppender(String name);

    public String getFileLocation(String appenderName);

    public void showLoggingConfig();

    public boolean onOk() throws WizardValidationException;

    public void onReset();

    public LoggingThreshold getRootLoggingLevel();

    public void setRootLoggingLevel(LoggingThreshold level);

}
