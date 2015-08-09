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
 *  File Name: ConsolePage.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.15  2015/04/03 21:06:25  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/03/16 17:47:40  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/03/05 17:34:30  jijunwan
 *  Archive Log:    new constants and constant name change
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/10/17 19:28:56  rjtierne
 *  Archive Log:    Check for nullpointer when using context
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/10/09 13:04:06  fernande
 *  Archive Log:    Adding IContextAware interface to generalize setting up Context
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/10/07 19:54:11  rjtierne
 *  Archive Log:    Changed constructor input parameter "owner" type from Window to IFabricView
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/10/01 19:44:56  rjtierne
 *  Archive Log:    Launch console login dialog if no consoles exist
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/09/23 19:47:00  rjtierne
 *  Archive Log:    Integration of Gritty for Java Console
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/09/15 15:24:34  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/09/09 20:03:28  rjtierne
 *  Archive Log:    Added default login bean to console dialog to reduce typing
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/09 14:20:55  rjtierne
 *  Archive Log:    Restructured code to accommodate new console login dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/04 21:06:52  rjtierne
 *  Archive Log:    Pass mainFrame window to the console page
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/02 18:34:34  jijunwan
 *  Archive Log:    minor change
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/26 15:15:33  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 19:53:57  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Top level page controller for the Console Page
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.console;

import static com.intel.stl.ui.common.PageWeight.LOW;

import java.awt.Component;

import javax.swing.ImageIcon;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.console.view.ConsoleView;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.view.IFabricView;

public class ConsolePage implements IPageController {

    private final static int SSH_PORT = 22;

    private final ConsoleView consoleView;

    private final IConsoleEventListener dispatchManager;

    private Context context;

    private LoginBean defaultLoginBean;

    public ConsolePage(ConsoleView consoleView, IFabricView owner,
            IConsoleEventListener dispatchManager, MBassador<IAppEvent> eventBus) {

        this.consoleView = consoleView;
        this.dispatchManager = dispatchManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.IPageController#setContext(com.intel.stl.ui.main
     * .Context, com.intel.stl.ui.common.IProgressObserver)
     */
    @Override
    public void setContext(Context context, IProgressObserver observer) {
        this.context = context;
        dispatchManager.setContext(context, observer);
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
        observer.onFinish();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getName()
     */
    @Override
    public String getName() {
        return STLConstants.K2107_ADM_CONSOLE.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getDescription()
     */
    @Override
    public String getDescription() {
        return STLConstants.K2108_ADM_CONSOLE_DESC.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getView()
     */
    @Override
    public Component getView() {
        return consoleView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getIcon()
     */
    @Override
    public ImageIcon getIcon() {
        return UIImages.CONSOLE_ICON.getImageIcon();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#cleanup()
     */
    @Override
    public void cleanup() {
        dispatchManager.cleanup();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onEnter()
     */
    @Override
    public void onEnter() {

        if (dispatchManager.getNumConsoles() <= 0) {
            if (context != null) {
                SubnetDescription sd = context.getSubnetDescription();
                defaultLoginBean =
                        new LoginBean(sd.getCurrentUser(), sd.getCurrentFE()
                                .getHost(), String.valueOf(SSH_PORT));
            }
            dispatchManager.addNewConsole(defaultLoginBean, true, null);
        }
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
     * @see com.intel.stl.ui.common.IPageController#canExit()
     */
    @Override
    public boolean canExit() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#clear()
     */
    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public PageWeight getContextSwitchWeight() {
        return LOW;
    }

    @Override
    public PageWeight getRefreshWeight() {
        return LOW;
    }
}
