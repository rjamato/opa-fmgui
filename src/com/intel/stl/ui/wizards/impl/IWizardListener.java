/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2014 Intel Corporation All Rights Reserved.
 * The source code contained or described herein and all documents related to the source code ("Material")
 * are owned by Intel Corporation or its suppliers or licensors. Title to the Material remains with Intel
 * Corporation or its suppliers and licensors. The Material contains trade secrets and proprietary and
 * confidential information of Intel or its suppliers and licensors. The Material is protected by
 * worldwide copyright and trade secret laws and treaty provisions. No part of the Material may be used,
 * copied, reproduced, modified, published, uploaded, posted, transmitted, distributed, or disclosed in
 * any way without Intel's prior express written permission. No license under any patent, copyright,
 * trade secret or other intellectual property right is granted to or conferred upon you by disclosure
 * or delivery of the Materials, either expressly, by implication, inducement, estoppel or otherwise.
 * Any license under such intellectual property rights must be express and approved by Intel in writing.
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

import java.util.List;

import com.intel.stl.api.configuration.AppenderConfig;
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

    void saveLoggingConfiguration(List<AppenderConfig> appenders);

    List<AppenderConfig> getLoggingConfig();

}
