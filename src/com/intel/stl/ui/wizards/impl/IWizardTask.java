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
 *  File Name: IProcess.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
