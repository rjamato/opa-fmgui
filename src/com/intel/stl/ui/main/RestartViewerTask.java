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
 *  File Name: RestartViewerTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.2  2015/08/12 15:26:34  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
