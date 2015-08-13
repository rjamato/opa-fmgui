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
 *  File Name: IProcess.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6.2.1  2015/08/12 15:27:01  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/03/31 17:48:28  rjtierne
 *  Archive Log:    Added/Implemented setConnectable()
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/20 21:13:23  rjtierne
 *  Archive Log:    Multinet Wizard: New instalment of the multinet wizard targeting synchronization of all sub-wizard data with selected subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/13 21:31:51  rjtierne
 *  Archive Log:    Multinet Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/20 19:11:38  rjtierne
 *  Archive Log:    Changed onApply() to return a boolean to indicate success/failure
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/23 18:27:22  rjtierne
 *  Archive Log:    Added setDirty() to enable wizard controllers force the dirty state in the view when interacting with the status panel
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/10 21:31:05  rjtierne
 *  Archive Log:    New Setup Wizard based on framework
 *  Archive Log:
 *
 *  Overview: Interface for individual sub-wizard controllers
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.impl;

import javax.swing.JComponent;

import com.intel.stl.ui.wizards.model.MultinetWizardModel;

public interface IWizardTask {

    public String getName();

    public JComponent getView();

    public void init();

    public void setDone(boolean done);

    public boolean isDone();

    public boolean validateUserEntry() throws WizardValidationException;

    public void onPrevious();

    public void onReset();

    public void clear();

    public void cleanup();

    public void selectStep(String taskName);

    public boolean isDirty();

    public void setDirty(boolean dirty);

    public void updateModel();

    public void promoteModel(MultinetWizardModel topModel);

    public void doInteractiveAction(InteractionType action, Object... data);

    public void setWizardController(IWizardListener controller);

    public void setConnectable(boolean connectable);
}
