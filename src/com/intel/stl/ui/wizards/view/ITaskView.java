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
 *  File Name: ITaskView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
