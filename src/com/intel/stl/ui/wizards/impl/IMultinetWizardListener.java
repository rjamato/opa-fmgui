/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: IMultinetWizardListener.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/04/21 21:17:27  rjtierne
 *  Archive Log:    Added setDirty() to enable/disable Apply and Reset button depending on whether any
 *  Archive Log:    of the wizard views have changed
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/03/31 17:47:25  rjtierne
 *  Archive Log:    - Added/implemented methods getHostIp() and isHostConnectable()
 *  Archive Log:    - Removed call to checkConnectivity() in startSubnet()
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/03/25 17:56:17  rjtierne
 *  Archive Log:    Added maps to keep track of secure certificate files for each subnet
 *  Archive Log:    and clear key factories at appropriate times
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/03/20 21:05:41  rjtierne
 *  Archive Log:    Added method onTab()
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/03/11 15:24:27  rjtierne
 *  Archive Log:    Multinet Wizard: Added new logic to save the subnet and update the
 *  Archive Log:    database on a background task with updates to the Welcome panel. Add new inner class
 *  Archive Log:    ConfigureSubnetTask to perform final host reachability, entry validation, and database
 *  Archive Log:    update tasks and update the welcome window.
 *  Archive Log:    updateDatabase() now throws exception so the welcome window is updated with failed status
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/27 15:34:32  rjtierne
 *  Archive Log:    Fixed preferences model/view sync issue when creating new subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/26 22:51:14  rjtierne
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
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.impl;

import java.util.List;

import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.wizards.view.IMultinetWizardView;
import com.intel.stl.ui.wizards.view.subnet.SubnetWizardView;

public interface IMultinetWizardListener extends IWizardListener {

    public void onNewSubnet();

    public void onTab();

    @Override
    public void onDelete();

    public boolean onRun();

    public void onFinish();

    public void deleteSubnet(SubnetDescription subnet);

    public List<SubnetDescription> getSubnets();

    public List<IMultinetWizardTask> getTasks();

    @Override
    public IMultinetWizardView getView();

    public SubnetDescription getNewSubnet();

    public SubnetDescription getCurrentSubnet();

    public UserSettings retrieveUserSettings();

    public UserSettings retrieveUserSettings(SubnetDescription subnet);

    public IWizardTask getCurrentTask();

    public void setCurrentTask(int taskPosition);

    public void updateModels(SubnetDescription subnet);

    public void clearTasks();

    public boolean isNewWizard();

    public void setCurrentSubnet(SubnetDescription subnet);

    public String getHostIp(String hostName);

    public boolean isHostReachable();

    public boolean isHostConnectable();

    public boolean isValidEntry();

    public void validateEntry();

    public void checkHost();

    public void saveConfiguration();

    public void resetWorkerStatus();

    public SubnetWizardView getSubnetView();

    public void clearSubnetFactories(SubnetDescription subnet);

    public void cancelConfiguration();

    public void setDirty(boolean dirty);

}
