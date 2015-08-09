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
 *  File Name: ShowWarningTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/02/26 22:10:27  fernande
 *  Archive Log:    Fix to refresh UserSettings in SubnetContext after it's updated by the Setup Wizard. Added pending tasks, which for now are only messages that should be displayed after the subnet is initialized. These tasks run after the initialization popup is hidden.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import static com.intel.stl.ui.common.STLConstants.K0031_WARNING;

import java.awt.Component;
import java.util.List;

import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.framework.AbstractTask;

public class ShowWarningTask extends AbstractTask<String, Void, Void> {

    public ShowWarningTask(String model) {
        super(model);
    }

    @Override
    public Void processInBackground(Context context) throws Exception {
        return null;
    }

    @Override
    public void onTaskSuccess(Void result) {
        IFabricController mycontroller = (IFabricController) getController();
        String msg = getModel();
        Util.showWarningMessage((Component) mycontroller.getView(), msg,
                K0031_WARNING.getValue());
    }

    @Override
    public void onTaskFailure(Throwable caught) {

    }

    @Override
    public void onFinally() {
    }

    @Override
    protected void processIntermediateResults(List<Void> intermediateResults) {
    }

}
