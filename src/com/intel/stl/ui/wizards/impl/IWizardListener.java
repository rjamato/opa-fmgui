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
 *  File Name: IWizardListener.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.14  2015/08/17 18:54:10  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/06/10 19:25:58  rjtierne
 *  Archive Log:    PR 128975 - Can not setup application log
 *  Archive Log:    Removed references to the logger
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/03/30 15:10:59  rjtierne
 *  Archive Log:    Added new methods showStep() and tryToConnect()
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/03/11 15:24:10  rjtierne
 *  Archive Log:    Multinet Wizard: Changed saveSubnet() from public to protected and removed from interface
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/26 16:20:44  fernande
 *  Archive Log:    Changed showSetupWizard so that the wizard can show its view centered on the calling frame.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/20 21:13:23  rjtierne
 *  Archive Log:    Multinet Wizard: New instalment of the multinet wizard targeting synchronization of all sub-wizard data with selected subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/10 19:35:38  fernande
 *  Archive Log:    Changes to handle SubnetConnectionExceptions
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/06 17:36:25  fernande
 *  Archive Log:    Fixed UserSettings not being saved off to the database in the SetupWizard
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/06 15:12:00  fernande
 *  Archive Log:    Changes so that the Setup Wizard depends on the Subnet Manager for all subnet-related operations
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/02 20:37:39  fernande
 *  Archive Log:    Fixing the SetupWizard so that it can define new subnets. Fixed also StackOverflowError exception when switching subnets.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/02 15:28:27  rjtierne
 *  Archive Log:    Gets subnet from the main setup wizard as part of new
 *  Archive Log:    Multi-subnet architecture
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/30 20:28:21  fernande
 *  Archive Log:    Initial changes to support multiple fabric viewers
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/11 21:48:18  jijunwan
 *  Archive Log:    setup wizard improvements
 *  Archive Log:    1) look and feel adjustment
 *  Archive Log:    2) secure FE support
 *  Archive Log:    3) apply wizard on current subnet
 *  Archive Log:    4) message display based on message type rather than directly specifying UI resources
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/10 21:31:05  rjtierne
 *  Archive Log:    New Setup Wizard based on framework
 *  Archive Log:
 *
 *  Overview: Interface for the top level Setup Wizard
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.impl;

import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.api.subnet.SubnetConnectionException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.main.IFabricController;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.intel.stl.ui.wizards.view.IWizardView;

public interface IWizardListener {

    void onPrevious();

    boolean onNext();

    void onApply();

    void onClose();

    void onReset();

    void onDelete();

    void selectStep(String taskName);

    void showStep(String taskName);

    void showView(SubnetDescription subnet, String subnetName,
            IFabricController callingController);

    IWizardView getView();

    void closeStatusPanels();

    boolean tryToConnect() throws SubnetConnectionException;

    boolean tryToConnect(SubnetDescription subnet)
            throws SubnetConnectionException;

    PMConfigBean getPMConfig();

    TaskScheduler getTaskScheduler();

    UserSettings getUserSettings();

    void saveUserSettings();

    boolean isFirstRun();

    SubnetDescription getSubnet();

}
