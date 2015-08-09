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
 *  File Name: IMultinetWizardView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7.2.1  2015/05/06 19:39:17  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/05/01 21:29:09  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/03/20 21:07:32  rjtierne
 *  Archive Log:    Added updateSelectedButtonText() & stopSubnetConnectionTest()
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/03/11 15:25:27  rjtierne
 *  Archive Log:    - Multinet Wizard: Add Welcome Panel to provide instructions and report status information during
 *  Archive Log:    subnet configuration.
 *  Archive Log:    - Moved the subnet name field from the subnet view to this view.
 *  Archive Log:    - Added welcome panel to provide instructions and status
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/26 22:53:02  rjtierne
 *  Archive Log:    Updated setMultinetTasks() prototype
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/26 16:21:07  fernande
 *  Archive Log:    Changed showSetupWizard so that the wizard can show its view centered on the calling frame.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/25 18:00:56  rjtierne
 *  Archive Log:    - Implemented subnet deletion
 *  Archive Log:    - Using new WizardType enumeration to specify model to update to improve efficiency
 *  Archive Log:    - Simplified method onClose()
 *  Archive Log:    - Standardized warning dialogue into one showWarningDialog()
 *  Archive Log:    - Added null pointer protection
 *  Archive Log:    - Removed button redundancy in maps
 *  Archive Log:    - Using current subnet to select the correct subnet sub-wizards to display
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/20 21:16:10  rjtierne
 *  Archive Log:    Multinet Wizard: New instalment of the multinet wizard targeting display of subnet specific data for all sub-wizards; using a unique model for each wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/13 21:30:26  rjtierne
 *  Archive Log:    Multinet Wizard: Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.view;

import java.util.List;

import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.main.view.IFabricView;
import com.intel.stl.ui.wizards.impl.IMultinetWizardListener;
import com.intel.stl.ui.wizards.impl.IMultinetWizardTask;
import com.intel.stl.ui.wizards.model.MultinetWizardModel;

public interface IMultinetWizardView extends IWizardView {

    public void update(MultinetWizardModel model);

    public void updateNavPanel();

    public void initButtonMap(List<SubnetDescription> subnets,
            SubnetDescription currentSubnet);

    public void showErrorMessage(String title, String... msgs);

    public void showErrorMessage(String title, Throwable... errors);

    public void updateSelectedButtonText(String name);

    public void setWizardListener(IMultinetWizardListener listener);

    public void setTabs(List<IMultinetWizardTask> tasks, boolean newSubnet,
            boolean wizardStartup);

    public void showWizard(SubnetDescription subnet, boolean isFirstRun,
            IFabricView mainFrame);

    public void setSubnetName(String name);

    public String getSubnetName();

    public void stopSubnetConnectionTest();

    public void assignDocumentListeners(DocumentListener... docListeners);

    public void setTxtfldSubnetName(String value);

    public JTextField getTxtfldSubnetName();

}
