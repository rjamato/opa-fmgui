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
 *  File Name: ITaskView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/08/17 18:53:58  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/03/11 15:25:12  rjtierne
 *  Archive Log:    Multinet Wizard: Removed holder panel since the status panel is no longer being used.
 *  Archive Log:    Added method logMessage() to replace showMessage() on the status panel.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/20 21:16:10  rjtierne
 *  Archive Log:    Multinet Wizard: New instalment of the multinet wizard targeting display of subnet specific data for all sub-wizards; using a unique model for each wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/13 21:31:54  rjtierne
 *  Archive Log:    Multinet Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/01/11 21:48:17  jijunwan
 *  Archive Log:    setup wizard improvements
 *  Archive Log:    1) look and feel adjustment
 *  Archive Log:    2) secure FE support
 *  Archive Log:    3) apply wizard on current subnet
 *  Archive Log:    4) message display based on message type rather than directly specifying UI resources
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/12/19 18:58:46  rjtierne
 *  Archive Log:    Added array of Objects to showMessage() to handle interactive tasks when user must interact with StatusPanel.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/11 20:01:09  rjtierne
 *  Archive Log:    Removed the disabling of Next/Apply buttons to make Wizard functional again
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/10 21:31:03  rjtierne
 *  Archive Log:    New Setup Wizard based on framework
 *  Archive Log:
 *
 *  Overview: Interface for a Wizard's view
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.view;

import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.wizards.impl.InteractionType;
import com.intel.stl.ui.wizards.model.MultinetWizardModel;

public interface ITaskView {

    public void setSubnet(SubnetDescription subnet);

    public void resetPanel();

    public void openStatusPanel();

    public void closeStatusPanel();

    public void toggleStatusPanel();

    public void setDirty(boolean dirty);

    public boolean isDirty();

    public void showMessage(String message, InteractionType action,
            int messageType, Object... data);

    public void logMessage(String message);

    public void updateView(MultinetWizardModel model);

}
