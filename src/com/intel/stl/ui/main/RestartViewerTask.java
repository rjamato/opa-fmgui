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
 *  File Name: RestartViewerTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.1  2015/05/06 19:39:13  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/05/01 21:29:06  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/04/28 22:08:56  jijunwan
 *  Archive Log:    removed title argument from #showErrorMessage
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/22 22:31:54  fisherma
 *  Archive Log:    Removing html tags from error messages.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/08 15:20:39  fernande
 *  Archive Log:    Changes to allow for failover to work when the current (initial) FE is not available.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/02 22:34:33  fernande
 *  Archive Log:    Fixes to handle a change in subnet name; if subnet name is changed, the model in FabricController is now updated as well as the name in the SubnetDescription in the subnet context to that the view and the back end are now in sync.
 *  Archive Log:
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

import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.framework.AbstractTask;

public class RestartViewerTask extends AbstractTask<String, Context, Void> {

    private final SubnetManager subnetMgr;

    private final SubnetDescription subnet;

    private final String existingHostId;

    private final String newHostId;

    public RestartViewerTask(String model, SubnetManager subnetMgr,
            SubnetDescription subnet, String existingHostId, String newHostId) {
        super(model);
        this.subnetMgr = subnetMgr;
        this.subnet = subnet;
        this.existingHostId = existingHostId;
        this.newHostId = newHostId;
    }

    @Override
    public Context processInBackground(Context context) throws Exception {
        Context newContext = subnetMgr.createContext(subnet);
        return newContext;
    }

    @Override
    public void onTaskSuccess(final Context newContext) {
        final IFabricController restartingController =
                (IFabricController) getController();
        String msg = getModel();
        Util.showWarningMessage((Component) restartingController.getView(),
                msg, K0031_WARNING.getValue());
        // subnetMgr.resetHost(existingHostId, newHostId, restartingController,
        // newContext);
        restartingController.resetContext(newContext);
    }

    @Override
    public void onTaskFailure(Throwable caught) {
        IFabricController thisController = (IFabricController) getController();
        Util.showError((Component) thisController.getView(), caught);
        Context context = thisController.getCurrentContext();
        context.close();
    }

    @Override
    public void onFinally() {

    }

    @Override
    protected void processIntermediateResults(List<Void> intermediateResults) {
    }

}
