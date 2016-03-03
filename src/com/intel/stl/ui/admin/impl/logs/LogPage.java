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
 *  File Name: LogPage.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/11/18 23:54:11  rjtierne
 *  Archive Log:    PR 130965 - ESM support on Log Viewer
 *  Archive Log:    - Removed consideration of the HostType, behaviour is now the same for ESM and HSM
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/10/19 22:30:11  jijunwan
 *  Archive Log:    PR 131091 - On an unsuccessful Failover, the Admin | Applications doesn't show the login window
 *  Archive Log:    - show login panel when not initialized properly with corresponding message
 *  Archive Log:    - added feature to fully enable/disable a login panel
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/10/06 15:51:46  rjtierne
 *  Archive Log:    PR 130390 - Windows FM GUI - Admin tab->Logs side-tab - unable to login to switch SM for log access
 *  Archive Log:    - In method onEnter(), added an additional condition to check hostType=ESM to show the Log Viewer
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/09/25 13:55:39  rjtierne
 *  Archive Log:    PR 130011 - Enhance SM Log Viewer to include Standard and Advanced requirements
 *  Archive Log:    - Code clean up
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/08/17 18:54:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/08/17 14:22:41  rjtierne
 *  Archive Log:    PR 128979 - SM Log display
 *  Archive Log:    This is the first version of the Log Viewer which displays select lines of text from the remote SM log file. Updates include searchable raw text from file, user-defined number of lines to display, refreshing end of file, and paging. This PR is now closed and further updates can be found by referencing PR 130011 - "Enhance SM Log Viewer to include Standard and Advanced requirements".
 *  Archive Log:
 *
 *  Overview: Page controller for the view displayed when the Log tab is selected
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl.logs;

import java.awt.Component;

import javax.swing.ImageIcon;

import com.intel.stl.api.logs.ILogApi;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.admin.FunctionType;
import com.intel.stl.ui.admin.view.logs.LogViewType;
import com.intel.stl.ui.admin.view.logs.SMLogView;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.main.Context;

public class LogPage implements IPageController {

    private final SMLogController smLogController;

    private final SMLogView logView;

    private final FunctionType functionType;

    private ILogApi logApi;

    private SubnetDescription subnet;

    public LogPage(SMLogController controller) {
        this.smLogController = controller;
        this.logView = smLogController.getView();
        functionType = FunctionType.LOGS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IContextAware#getName()
     */
    @Override
    public String getName() {
        return functionType.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.IContextAware#setContext(com.intel.stl.ui.main
     * .Context, com.intel.stl.ui.common.IProgressObserver)
     */
    @Override
    public void setContext(Context context, IProgressObserver observer) {

        logApi = context.getLogApi();
        subnet = context.getSubnetDescription();
        smLogController.setContext(context, observer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IContextAware#getContextSwitchWeight()
     */
    @Override
    public PageWeight getContextSwitchWeight() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IContextAware#getRefreshWeight()
     */
    @Override
    public PageWeight getRefreshWeight() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getDescription()
     */
    @Override
    public String getDescription() {
        return functionType.getDescription();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getView()
     */
    @Override
    public Component getView() {
        return logView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getIcon()
     */
    @Override
    public ImageIcon getIcon() {
        return functionType.getIcon();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#cleanup()
     */
    @Override
    public void cleanup() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onEnter()
     */
    @Override
    public void onEnter() {
        if (logApi.hasSession(subnet)) {
            // smLogController.startLog();
            smLogController.showView(LogViewType.SM_LOG.getValue());
        } else {
            smLogController.showLoginView();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#canExit()
     */
    @Override
    public boolean canExit() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onExit()
     */
    @Override
    public void onExit() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.IPageController#onRefresh(com.intel.stl.ui.common
     * .IProgressObserver)
     */
    @Override
    public void onRefresh(IProgressObserver observer) {
        smLogController.onRefresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#clear()
     */
    @Override
    public void clear() {
    }

}
