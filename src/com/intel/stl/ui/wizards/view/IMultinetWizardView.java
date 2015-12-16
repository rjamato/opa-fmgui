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
 *  File Name: IMultinetWizardView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/10/06 20:21:10  fernande
 *  Archive Log:    PR130749 - FM GUI virtual fabric information doesn't match opafm.xml file. Removed external access to textfield
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/08/17 18:53:58  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/08/10 17:55:45  robertja
 *  Archive Log:    PR 128974 - Email notification functionality.
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

    /**
     * <i>Description:</i>
     * 
     * @param recipients
     */
    public void onEmailTest(String recipients);

}
