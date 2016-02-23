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
 * I N T E L C O R P O R A T I O N
 * 
 * Functional Group: Fabric Viewer Application
 * 
 * File Name: LoggingConfiguration.java
 * 
 * Archive Source: $Source$
 * 
 * Archive Log: $Log$
 * Archive Log: Revision 1.6  2015/08/17 18:48:36  jijunwan
 * Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 * Archive Log: - change backend files' headers
 * Archive Log:
 * Archive Log: Revision 1.5  2015/06/26 13:37:09  rjtierne
 * Archive Log: PR 128975 - Can not setup application log
 * Archive Log: - Override toString() to provide a better description of class attributes during debug.
 * Archive Log: - Since the root log level comes from a higher level logger, the rootLogLevel has been changed
 * Archive Log: to LoggerConfig rootLogger from which the root level can be obtained.
 * Archive Log:
 * Archive Log: Revision 1.4  2015/06/10 19:14:26  rjtierne
 * Archive Log: PR 128975 - Can not setup application log
 * Archive Log: Now using this class as a wrapper for the list of appenders, list of loggers, and root level logger
 * Archive Log: 
 * Archive Log: Revision 1.3 2014/05/13 18:31:47 fernande 
 * Archive Log: Implemented saveLoggingConfiguration and getLoggingConfiguration
 * Archive Log: 
 * Archive Log: Revision 1.2 2014/04/19 14:31:34 fernande 
 * Archive Log: More changes to plug the Setup wizard 
 * Archive Log: 
 * Archive Log: Revision 1.1 2014/04/18 18:27:05 jypak 
 * Archive Log: Config API for Setup Wizard Migration. 
 * Archive Log: 
 * Archive Log: Revision 1.1 2014/04/12 20:39:17 fernande 
 * Archive Log: Initial version 
 * Archive Log: 
 * Archive Log: Revision 1.1 2014/04/09 16:35:11 jypak 
 * Archive Log: Setup Wizard. The SetupWizardMain class control the wizard
 * to come up as an initial or a non-initial setup wizard. Once the Setup Wizard
 * is incorporated to the whole GUI, the main class should be discarded. 
 * Archive Log:
 * 
 * Overview: Java representation of the logger configuration file containing
 * the root logger, list of appenders, and list of loggers.
 * 
 * @author jypak
 * 
 ******************************************************************************/

package com.intel.stl.api.configuration;

import java.io.Serializable;
import java.util.List;

public class LoggingConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    private LoggerConfig rootLogger = null;

    private List<AppenderConfig> appenders = null;

    private List<LoggerConfig> loggers = null;

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

    public List<LoggerConfig> getLoggers() {
        return loggers;
    }

    public void setLoggers(List<LoggerConfig> loggers) {
        this.loggers = loggers;
    }

    public LoggerConfig getRootLogger() {
        return rootLogger;
    }

    public void setRootLogger(LoggerConfig rootLogger) {
        this.rootLogger = rootLogger;
    }

    @Override
    public String toString() {

        return "LoggingConfiguration [rootLogger=" + getRootLogger()
                + ", appenders=" + getAppenders() + "loggers=" + getLoggers()
                + "]";
    }
}
