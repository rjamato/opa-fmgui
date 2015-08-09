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
 *  File Name: AdminPage.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.13  2015/04/07 22:13:18  jijunwan
 *  Archive Log:    turn off "Log" feature on Admin page
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/04/03 21:06:34  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/03/30 14:26:50  jijunwan
 *  Archive Log:    improved to update App names and DG names properly for VF mgt
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/03/27 15:47:50  jijunwan
 *  Archive Log:    first version of VirtualFabric UI
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/03/16 22:08:17  jijunwan
 *  Archive Log:    added device group visualization on UI
 *  Archive Log:    some refactory to make the conf framework to be more general
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/03/10 22:45:36  jijunwan
 *  Archive Log:    improved to do and show validation before we save an application
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/03/05 17:38:20  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/11/17 17:12:43  jijunwan
 *  Archive Log:    added Log to admin page
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/11/05 22:57:36  jijunwan
 *  Archive Log:    improved the stability of turning on/off refresh icon when we response to notice events
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/21 14:04:33  rjtierne
 *  Archive Log:    Passing main window for reference when displaying dialogs
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/14 11:32:16  jypak
 *  Archive Log:    UI updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/09 12:30:53  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext and refresh).
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/07 19:53:19  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.admin.impl;

import static com.intel.stl.ui.common.PageWeight.MEDIUM;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.admin.FunctionType;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.admin.impl.applications.AppsPageController;
import com.intel.stl.ui.admin.impl.devicegroups.DevicegroupsPageController;
import com.intel.stl.ui.admin.impl.virtualfabrics.VirtualFabricsPageController;
import com.intel.stl.ui.admin.view.AdminView;
import com.intel.stl.ui.admin.view.BlankView;
import com.intel.stl.ui.admin.view.BlankView.BlankEditorPanel;
import com.intel.stl.ui.admin.view.NavigationPanel.IconPanel;
import com.intel.stl.ui.admin.view.ValidationDialog;
import com.intel.stl.ui.admin.view.applications.AppsSubpageView;
import com.intel.stl.ui.admin.view.devicegroups.DevicegroupsSubpageView;
import com.intel.stl.ui.admin.view.virtualfabrics.VirtualFabricsSubpageView;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.console.ConsoleDispatchManager;
import com.intel.stl.ui.console.ConsolePage;
import com.intel.stl.ui.console.view.ConsoleView;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;

public class AdminPage implements IPageController, ChangeListener {

    private final AdminView view;

    private final List<IPageController> subpages;

    private IPageController currentPage;

    private final MBassador<IAppEvent> eventBus;

    private boolean isShowing;

    public AdminPage(AdminView view, MBassador<IAppEvent> eventBus) {
        this.view = view;
        view.addChangeListener(this);
        subpages = getAdmSubpages();
        installSubpages(subpages);

        this.eventBus = eventBus;
        eventBus.subscribe(this);
    }

    protected void installSubpages(List<IPageController> subpages) {
        for (IPageController subpage : subpages) {
            view.addViewCard(subpage.getIcon(), subpage.getView(),
                    subpage.getName());
        }
        view.setView(subpages.get(0).getName());
        currentPage = subpages.get(0);
    }

    protected List<IPageController> getAdmSubpages() {
        List<IPageController> res = new ArrayList<IPageController>();

        res.add(createAppsPage());
        res.add(createDGsPage());
        res.add(createVFsPage());
        res.add(createConsolePage());
        // res.add(createBlankPage(FunctionType.LOG));

        return res;
    }

    protected IPageController createAppsPage() {
        FunctionType type = FunctionType.APPLICATIONS;
        AppsSubpageView view = new AppsSubpageView(type.getName());
        AppsPageController appsPage =
                new AppsPageController(type.getName(), type.getDescription(),
                        type.getIcon(), view);
        return appsPage;
    }

    protected IPageController createDGsPage() {
        FunctionType type = FunctionType.DEVICE_GROUPS;
        DevicegroupsSubpageView view =
                new DevicegroupsSubpageView(type.getName());
        DevicegroupsPageController appsPage =
                new DevicegroupsPageController(type.getName(),
                        type.getDescription(), type.getIcon(), view);
        return appsPage;
    }

    protected IPageController createVFsPage() {
        FunctionType type = FunctionType.VIRTUAL_FABRICS;
        VirtualFabricsSubpageView view =
                new VirtualFabricsSubpageView(type.getName());
        VirtualFabricsPageController appsPage =
                new VirtualFabricsPageController(type.getName(),
                        type.getDescription(), type.getIcon(), view);
        return appsPage;
    }

    protected IPageController createBlankPage(FunctionType type) {
        BlankView view = new BlankView(type.getName());
        ConfPageController<Object, BlankEditorPanel> page =
                new ConfPageController<Object, BlankEditorPanel>(
                        type.getName(), type.getDescription(), type.getIcon(),
                        view) {

                    @Override
                    protected AbstractEditorController<Object, BlankEditorPanel> creatEditorController(
                            BlankEditorPanel editorPanel) {
                        return null;
                    }

                    @Override
                    protected ArrayList<Item<Object>> initData()
                            throws Exception {
                        return null;
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see
                     * com.intel.stl.ui.admin.impl.ConfPageController#getCopy
                     * (java.lang.Object)
                     */
                    @Override
                    protected Object getCopy(Object obj) {
                        return null;
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see
                     * com.intel.stl.ui.admin.impl.ConfPageController#createObj
                     * ()
                     */
                    @Override
                    protected Object createObj() {
                        return null;
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see com.intel.stl.ui.admin.impl.ConfPageController#
                     * removeItemObject(java.lang.String)
                     */
                    @Override
                    protected void removeItemObject(String name)
                            throws Exception {
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see
                     * com.intel.stl.ui.admin.impl.ConfPageController#saveItemObject
                     * (java.lang.Object)
                     */
                    @Override
                    protected void saveItemObject(String oldName, Object obj)
                            throws Exception {
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see com.intel.stl.ui.admin.impl.ConfPageController#
                     * getValidationTask
                     * (com.intel.stl.ui.admin.view.ValidationDialog,
                     * com.intel.stl.ui.admin.Item)
                     */
                    @Override
                    protected ValidationTask<Object> getValidationTask(
                            ValidationDialog dialog, Item<Object> item) {
                        return null;
                    }

                };
        return page;
    }

    protected IPageController createConsolePage() {
        ConsoleView consoleView = new ConsoleView(view.getOwner());
        ConsoleDispatchManager dispatchManager =
                new ConsoleDispatchManager(consoleView, view.getOwner());
        consoleView.setConsoleDispatchManager(dispatchManager);
        ConsolePage consolePage =
                new ConsolePage(consoleView, view.getOwner(), dispatchManager,
                        eventBus);
        return consolePage;
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
        IProgressObserver[] subObservers = null;
        if (observer != null) {
            subObservers = observer.createSubObservers(subpages.size());
        }
        for (int i = 0; i < subpages.size(); i++) {
            IPageController subpage = subpages.get(i);
            subpage.setContext(context, subObservers == null ? null
                    : subObservers[i]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent
     * )
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        IconPanel panel = (IconPanel) e.getSource();
        String name = panel.getName();
        for (IPageController page : subpages) {
            if (page.getName().equals(name)) {
                if (currentPage != null) {
                    if (currentPage instanceof ConfPageController) {
                        ConfPageController<?, ?> cpc =
                                (ConfPageController<?, ?>) currentPage;
                        if (!cpc.changeCheck()) {
                            return;
                        }
                    }
                    currentPage.onExit();
                }

                currentPage = page;
                view.setView(name);
                page.onEnter();
                break;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getName()
     */
    @Override
    public String getName() {
        return STLConstants.K1057_ADMIN.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getDescription()
     */
    @Override
    public String getDescription() {
        return STLConstants.K1058_ADMIN_DESCRIPTION.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getView()
     */
    @Override
    public Component getView() {
        return view;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getIcon()
     */
    @Override
    public ImageIcon getIcon() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#cleanup()
     */
    @Override
    public void cleanup() {
        for (IPageController subpage : subpages) {
            subpage.cleanup();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onEnter()
     */
    @Override
    public void onEnter() {
        isShowing = true;
        if (currentPage != null) {
            currentPage.onEnter();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onExit()
     */
    @Override
    public void onExit() {
        if (currentPage != null) {
            currentPage.onExit();
        }
        isShowing = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#canExit()
     */
    @Override
    public boolean canExit() {
        if (currentPage != null && currentPage instanceof ConfPageController) {
            ConfPageController<?, ?> cpc =
                    (ConfPageController<?, ?>) currentPage;
            return cpc.changeCheck();
        }
        return true;
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
        if (isShowing && currentPage != null) {
            currentPage.onRefresh(observer);
        }
        observer.onFinish();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#clear()
     */
    @Override
    public void clear() {
    }

    @Override
    public PageWeight getContextSwitchWeight() {
        return MEDIUM;
    }

    @Override
    public PageWeight getRefreshWeight() {
        return MEDIUM;
    }

}
