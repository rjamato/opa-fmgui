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
 *  File Name: IFabricController.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.25  2015/09/30 13:26:47  fisherma
 *  Archive Log: PR 129357 - ability to hide inactive ports.  Also fixes PR 129689 - Connectivity table exhibits inconsistent behavior on Performance and Topology pages
 *  Archive Log:
 *  Archive Log: Revision 1.24  2015/08/17 18:53:38  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - changed frontend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.23  2015/08/10 17:30:41  robertja
 *  Archive Log: PR 128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log: Revision 1.22  2015/08/05 02:47:02  jijunwan
 *  Archive Log: PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log: - introduced UndoHandler to manage undo/redo
 *  Archive Log: - added undo/redo to main frame
 *  Archive Log: - improved FabricController to support undoHandler and undo action on page selection
 *  Archive Log: - improved FabricController to support the new page name based IPageListener
 *  Archive Log:
 *  Archive Log: Revision 1.21  2015/06/30 17:50:13  fisherma
 *  Archive Log: PR 129220 - Improvement on secure FE login.
 *  Archive Log:
 *  Archive Log: Revision 1.20  2015/06/25 20:24:55  jijunwan
 *  Archive Log: Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log: - applied pin framework on fabric viewer and simple 'static' cards
 *  Archive Log:
 *  Archive Log: Revision 1.19  2015/06/10 19:58:49  jijunwan
 *  Archive Log: PR 129120 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
 *  Archive Log:
 * 
 *  Overview:
 * 
 *  @author: jijunwan
 * 
 ******************************************************************************/
package com.intel.stl.ui.main;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.ISubnetEventListener;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.framework.ITask;
import com.intel.stl.ui.main.view.IFabricView;
import com.intel.stl.ui.publisher.TaskScheduler;

/**
 * @author jijunwan
 * 
 */
public interface IFabricController extends ISubnetEventListener {
    void doShowInitScreen(Rectangle bounds, boolean maximized);

    void doShowMessageAndExit(String message, String title);

    void doShowErrors(List<Throwable> errors);

    void doShowContent();

    void doClose();

    void reset();

    SubnetDescription getCurrentSubnet();

    void resetSubnet(SubnetDescription subnet);

    Context getCurrentContext();

    void selectSubnet(String subnetName);

    void resetConnectMenu();

    void initializeContext(Context context);

    void resetContext(Context newContext);

    void cleanup();

    IFabricView getView();

    TaskScheduler getTaskScheduler();

    void showSetupWizard(String subnetName);

    void showLoggingConfig();

    void showEmailSettingsDialog();

    void addPendingTask(ITask pendingTask);

    void bringToFront();

    Rectangle getBounds();

    boolean isMaximized();

    void applyHelpAction(ActionEvent event);

    JFrame getViewFrame();

    MBassador<IAppEvent> getEventBus();

    CertsLoginController getCertsLoginController();

    PinBoardController getPinBoardController();

    UndoHandler getUndoHandler();

    boolean getHideInactiveNodes();
}
