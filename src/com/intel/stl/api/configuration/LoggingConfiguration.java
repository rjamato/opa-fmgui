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

package com.intel.stl.api.configuration;

import java.io.Serializable;
import java.util.List;

/*******************************************************************************
 * I N T E L C O R P O R A T I O N
 * 
 * Functional Group: Fabric Viewer Application
 * 
 * File Name: LoggingConfiguration.java
 * 
 * Archive Source: $Source:
 * /cvs/vendor/intel/fmgui/server/src/main/java/com/intel
 * /stl/api/configuration/LoggingConfiguration.java,v $
 * 
 * Archive Log: $Log$
 * Archive Log: Revision 1.3.2.1  2015/08/12 15:21:40  jijunwan
 * Archive Log: PR 129955 - Need to change file header's copyright text to BSD license text
 * Archive Log:
 * Archive Log: Revision 1.3  2014/05/13 18:31:47  fernande
 * Archive Log: Implemented saveLoggingConfiguration and getLoggingConfiguration
 * Archive Log: Archive Log: Revision 1.2
 * 2014/04/19 14:31:34 fernande Archive Log: More changes to plug the Setup
 * wizard Archive Log: Archive Log: Revision 1.1 2014/04/18 18:27:05 jypak
 * Archive Log: Config API for Setup Wizard Migration. Archive Log: Archive Log:
 * Revision 1.1 2014/04/12 20:39:17 fernande Archive Log: Initial version
 * Archive Log: Archive Log: Revision 1.1 2014/04/09 16:35:11 jypak Archive Log:
 * Setup Wizard. The SetupWizardMain class control the wizard to come up as an
 * initial or a non-initial setup wizard. Once the Setup Wizard is incorporated
 * to the whole GUI, the main class should be discarded. Archive Log:
 * 
 * Overview: A bean to be transferred between the Config API
 * (LoggingConfigurationController) and SetupWizard for logging configuration
 * info.
 * 
 * @author jypak
 * 
 ******************************************************************************/
public class LoggingConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<AppenderConfig> appenders = null;

    /**
     * @return the list of appenders
     */
    public List<AppenderConfig> getAppenders() {
        return appenders;
    }

    /**
     * @param appenders
     *            the list of appenders to set
     */
    public void setAppenders(List<AppenderConfig> appenders) {
        this.appenders = appenders;
    }

}
