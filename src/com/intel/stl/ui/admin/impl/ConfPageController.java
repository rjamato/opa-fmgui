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
 *  File Name: EmptyPageController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.29  2015/10/19 22:30:10  jijunwan
 *  Archive Log:    PR 131091 - On an unsuccessful Failover, the Admin | Applications doesn't show the login window
 *  Archive Log:    - show login panel when not initialized properly with corresponding message
 *  Archive Log:    - added feature to fully enable/disable a login panel
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/09/28 17:54:17  fisherma
 *  Archive Log:    PR 130425 - added cancel button to the Admin tab login page to allow user to cancel out of hung or slow ssh logins.  Cancel action terminates sftp connection and closes remote ssh session. This fix also addresses PR 130386 and 130390.
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/09/21 20:48:43  jijunwan
 *  Archive Log:    PR 130542 - Confusion error message on fetching conf file
 *  Archive Log:    - changed to show credential error message only for JSchException
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/08/28 22:10:01  jijunwan
 *  Archive Log:    PR 130206 - On "Admin" tab, if login fails due to bad credentials, can't leave that tab until login succeeds
 *  Archive Log:    - change to reset isBusy when error happens, so we are free to switch to another tabs or pages
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/08/17 18:54:28  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/08/17 18:34:35  jijunwan
 *  Archive Log:    PR 128973 - Deploy FM conf changes on all SMs
 *  Archive Log:    - improved to ask confirmation when we intend to leave deploy panel while we are deploying FM confs.
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/08/17 17:41:42  jijunwan
 *  Archive Log:    PR 128973 - Deploy FM conf changes on all SMs
 *  Archive Log:    - applied deploy panel, controller on ConfPageController
 *  Archive Log:    - improved ConfPageController to check changes before deploy conf
 *  Archive Log:    - improved ConfPageController to refresh conf file when we already have a ssh session, and show log in panel when we have a ssh session.
 *  Archive Log:    - improved #canExit, so we can not switch to other tabs or pages when we are logging in or deploying conf file
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/08/05 02:52:55  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - apply undo mechanism on Admin page to track tab selection
 *  Archive Log:    - improved ConfPageController to check change when we exit one tab
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/07/28 18:29:13  fisherma
 *  Archive Log:    PR 129219 - Admin page login dialog improvement
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/07/14 17:06:57  jijunwan
 *  Archive Log:    PR 129541 - Should forbid save or deploy when there is invalid edit on management panel
 *  Archive Log:    - display warning message when a user intends to save or deploy while there is invalid edit
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/07/10 19:38:07  jijunwan
 *  Archive Log:    PR 129520 - Empty App or DG list after changes on App or DG
 *  Archive Log:    - when we reset items, it will trigger action event that will lead to view clear that will empty App and DG list. Fixed by removing the listener before we reset items, and adding it back after we finish it.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/06/25 11:55:04  jypak
 *  Archive Log:    PR 129073 - Add help action for Admin Page.
 *  Archive Log:    The help action is added to App, DG, VF,Console page and Console terminal. For now, a help ID and a content are being used as a place holder for each page. Once we get the help contents delivered by technical writer team, the HelpAction will be updated with correct help ID.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/05/14 17:19:44  jijunwan
 *  Archive Log:    PR 128697 - Handle empty list of items
 *  Archive Log:    - Added code to handle null item
 *  Archive Log:    - Added code to clean panel when it gets a null item
 *  Archive Log:    - Enable/disable buttons properly when we get an empty item list or null item
 *  Archive Log:    - Improved to handle item selection when the index is invalid, such as -1
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/05/14 16:36:46  jijunwan
 *  Archive Log:    PR 128686 - Get dirty panel when we directly deploy an unsaved change and then select abandon the changes
 *  Archive Log:    - shift selection up after we abandon the changes
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/05/12 17:39:22  rjtierne
 *  Archive Log:    PR 128624 - Klocwork and FindBugs fixes for UI
 *  Archive Log:    Reorganized code to check orgItems for null before trying synchronize on it.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/05/01 21:29:15  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/04/29 22:03:02  jijunwan
 *  Archive Log:    changed DefaulLoginAssistant to be DOCUMENT_MODAL
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/04/28 21:55:58  jijunwan
 *  Archive Log:    set login dialog's owner
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/04/09 22:51:38  jijunwan
 *  Archive Log:    show warning message before deploying fm conf
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/04/03 21:06:34  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/04/02 16:53:56  jypak
 *  Archive Log:    Spelling error correct
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/04/02 13:33:08  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/03/30 14:26:50  jijunwan
 *  Archive Log:    improved to update App names and DG names properly for VF mgt
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/03/27 15:47:50  jijunwan
 *  Archive Log:    first version of VirtualFabric UI
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/03/25 21:40:36  jijunwan
 *  Archive Log:    improvement on stability
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/03/16 22:08:17  jijunwan
 *  Archive Log:    added device group visualization on UI
 *  Archive Log:    some refactory to make the conf framework to be more general
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/11 21:16:06  jijunwan
 *  Archive Log:    added remove and deploy features
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/10 22:45:36  jijunwan
 *  Archive Log:    improved to do and show validation before we save an application
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:38:20  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl;

import java.awt.Component;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.management.IManagementApi;
import com.intel.stl.ui.admin.ChangeState;
import com.intel.stl.ui.admin.IConfListener;
import com.intel.stl.ui.admin.IItemEditorListener;
import com.intel.stl.ui.admin.IItemListListener;
import com.intel.stl.ui.admin.InvalidEditException;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.admin.view.AbstractConfView;
import com.intel.stl.ui.admin.view.AbstractEditorPanel;
import com.intel.stl.ui.admin.view.ValidationDialog;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.ValidationModel;
import com.intel.stl.ui.console.LoginBean;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.jcraft.jsch.JSchException;

public abstract class ConfPageController<T, E extends AbstractEditorPanel<T>>
        implements IPageController, IConfListener, IItemListListener,
        IItemEditorListener {
    private static final Logger log = LoggerFactory
            .getLogger(ConfPageController.class);

    private final String name;

    private final String description;

    private final ImageIcon icon;

    private final AbstractConfView<T, E> view;

    private String helpID;

    protected IManagementApi mgtApi;

    protected TaskScheduler taskScheduler;

    protected final AbstractEditorController<T, E> edtCtr;

    protected ArrayList<Item<T>> orgItems;

    protected DefaultListModel<Item<T>> workingItems;

    protected Item<T> currentItem;

    protected ValidationModel<T> valModel;

    protected DeployController deployController;

    private boolean restart;

    private boolean isBusy;

    private Future<?> future;

    /**
     * Description:
     * 
     * @param name
     * @param description
     * @param view
     */
    public ConfPageController(String name, String description, ImageIcon icon,
            AbstractConfView<T, E> view) {
        super();
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.view = view;
        installHelp();
        workingItems = new DefaultListModel<Item<T>>();
        view.setListModel(workingItems);
        view.addItemListListener(this);
        view.setConfListener(this);

        E edtPanel = view.getEditorPanel();
        edtPanel.setEditorListener(this);
        edtCtr = creatEditorController(edtPanel);

        valModel = new ValidationModel<T>();
        deployController = new DeployController(view);
    }

    protected void installHelp() {
        String helpId = getHelpID();
        if (helpId != null) {
            view.enableHelp(true);
            HelpAction helpAction = HelpAction.getInstance();
            helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                    helpId, helpAction.getHelpSet());
        } else {
            view.enableHelp(false);
        }
    }

    /**
     * @param helpID
     *            the helpID to set
     */
    public void setHelpID(String helpID) {
        this.helpID = helpID;
        installHelp();
    }

    protected abstract String getHelpID();

    protected AbstractEditorController<T, E> creatEditorController(E editorPanel) {
        return new AbstractEditorController<T, E>(editorPanel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IContextAware#getName()
     */
    @Override
    public String getName() {
        return name;
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
        try {
            mgtApi = context.getManagementApi();
            taskScheduler = context.getTaskScheduler();
            deployController.setContext(context, null);
        } finally {
            if (observer != null) {
                observer.publishProgress(1);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IContextAware#getContextSwitchWeight()
     */
    @Override
    public PageWeight getContextSwitchWeight() {
        return PageWeight.LOW;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IContextAware#getRefreshWeight()
     */
    @Override
    public PageWeight getRefreshWeight() {
        return PageWeight.LOW;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
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
        return icon;
    }

    protected SwingWorker<ArrayList<Item<T>>, Void> getInitWorker() {
        SwingWorker<ArrayList<Item<T>>, Void> worker =
                new SwingWorker<ArrayList<Item<T>>, Void>() {

                    @Override
                    protected ArrayList<Item<T>> doInBackground()
                            throws Exception {

                        ArrayList<Item<T>> res = initData();

                        return res;
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see javax.swing.SwingWorker#done()
                     */
                    @Override
                    protected void done() {
                        try {
                            // Show editor card in the view
                            view.showEditorCard();

                            orgItems = get();
                            if (orgItems == null) {
                                return;
                            }

                            // test empty items
                            // orgItems = new ArrayList<Item<T>>();

                            view.removeItemListListener(ConfPageController.this);
                            workingItems.clear();
                            int first = -1;
                            for (int i = 0; i < orgItems.size(); i++) {
                                Item<T> item = orgItems.get(i);
                                if (first == -1 && item.isEditable()) {
                                    first = i;
                                }
                                workingItems.addElement(getCopy(item,
                                        ChangeState.NONE));
                            }
                            view.addItemListListener(ConfPageController.this);

                            // view.setItems(workingItems);
                            if (currentItem != null) {
                                int index = workingItems.indexOf(currentItem);
                                if (index >= 0) {
                                    first = index;
                                }
                            }

                            if (first >= 0) {
                                currentItem = workingItems.get(first);
                                currentItem.setState(ChangeState.UPDATE);
                                view.setListModel(workingItems);
                                view.selectItem(first);
                                edtCtr.setItem(currentItem, getWorkingItems());
                            }
                        } catch (InterruptedException e) {
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            Util.showError(view, e);
                        } finally {
                            isBusy = false;
                        }
                    }

                };
        return worker;
    }

    protected abstract ArrayList<Item<T>> initData() throws Exception;

    protected Item<T> getCopy(Item<T> item, ChangeState newState) {
        if (item != null) {
            Item<T> newItem =
                    new Item<T>(item.getId(), item.getName(),
                            getCopy(item.getObj()), item.isEditable());
            newItem.setState(newState);
            return newItem;
        } else {
            Item<T> newItem =
                    new Item<T>(System.currentTimeMillis(),
                            STLConstants.K0016_UNKNOWN.getValue(), createObj(),
                            true);
            newItem.setState(newState);
            return newItem;
        }
    }

    protected abstract T getCopy(T obj);

    protected abstract T createObj();

    protected Item<T> getOrgItem(long id) {
        synchronized (orgItems) {
            int index = indexOfOrgItem(id);
            return orgItems.get(index);
        }
    }

    protected int indexOfOrgItem(long id) {
        if (orgItems == null) {
            throw new RuntimeException("No item list!");
        } else {
            synchronized (orgItems) {
                for (int i = 0; i < orgItems.size(); i++) {
                    Item<T> item = orgItems.get(i);
                    if (item.getId() == id) {
                        return i;
                    }
                }
                // this shouldn't happen
                throw new IllegalArgumentException(
                        "Couldn't find item with id=" + id);
            }
        }
    }

    protected Item<T> getWorkingItem(long id) {
        synchronized (workingItems) {
            int index = indexOfWorkingItem(id);
            return workingItems.get(index);
        }
    }

    protected int indexOfWorkingItem(long id) {
        synchronized (workingItems) {
            for (int i = 0; i < workingItems.size(); i++) {
                Item<T> item = workingItems.get(i);
                if (item.getId() == id) {
                    return i;
                }
            }
            // this shouldn't happen
            throw new IllegalArgumentException("Couldn't find item with id="
                    + id);
        }
    }

    @SuppressWarnings("unchecked")
    protected Item<T>[] getWorkingItems() {
        Item<T>[] res = new Item[workingItems.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = workingItems.get(i);
        }
        return res;
    }

    protected boolean changeCheck() {
        if (currentItem == null) {
            return true;
        }

        boolean hasChange = false;
        try {
            edtCtr.updateItem(currentItem);
        } catch (InvalidEditException e) {
            hasChange = true;
        }
        if (hasChange || hasChange(currentItem)) {
            int index = workingItems.indexOf(currentItem);
            int option = view.confirmDiscard();
            if (option != JOptionPane.YES_OPTION) {
                view.selectItem(index);
                return false;
            } else {
                if (currentItem.getState() == ChangeState.ADD) {
                    // discard new added item
                    currentItem = null;
                    workingItems.remove(index);
                } else if (currentItem.getState() == ChangeState.UPDATE) {
                    // change back to org item
                    Item<T> orgItem = getOrgItem(currentItem.getId());
                    Item<T> newItem = getCopy(orgItem, ChangeState.NONE);
                    currentItem = newItem;
                    workingItems.set(index, newItem);
                    edtCtr.setItem(currentItem, getWorkingItems());
                }
                view.updateItems();
            }
        }
        return true;
    }

    protected boolean hasChange(Item<T> workingItem) {
        Item<T> orgItem = null;
        try {
            orgItem = getOrgItem(workingItem.getId());
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out
        // .println((orgItem == null ? "null" : orgItem
        // .getFullDescription())
        // + " "
        // + workingItem.getFullDescription());
        return orgItem == null || !orgItem.equals(workingItem);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.IItemListListener#onSelect(java.lang.String)
     */
    @Override
    public void onSelect(long id) {
        // System.out.println("Select " + id);
        if (id == -1) {
            edtCtr.setItem(null, getWorkingItems());
            currentItem = null;
            return;
        }

        if (currentItem != null) {
            if (currentItem.getId() == id) {
                return;
            }

            if (!changeCheck()) {
                return;
            }
        }

        Item<T> item = getWorkingItem(id);
        // change state to UPDATE for items already existed.
        if (item.getState() == ChangeState.NONE) {
            item.setState(ChangeState.UPDATE);
        }
        edtCtr.setItem(item, getWorkingItems());
        currentItem = item;
        log.info("Select " + item.getFullDescription());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.IItemListListener#onAdd()
     */
    @Override
    public void onAdd() {
        if (!changeCheck()) {
            return;
        }

        Item<T> newItem = getCopy(null, ChangeState.ADD);
        currentItem = newItem;
        workingItems.addElement(newItem);
        view.selectItem(workingItems.size() - 1);
        edtCtr.setItem(newItem, getWorkingItems());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.IItemListListener#onRemove(java.lang.String)
     */
    @Override
    public void onRemove(final long id) {
        if (!changeCheck()) {
            return;
        }

        // if it's new add, we just need to remove on UI side since it's not in
        // backend yet
        if (currentItem == null || currentItem.getState() == ChangeState.ADD) {
            view.selectItem(workingItems.size() - 1);
            return;
        }

        currentItem.setState(ChangeState.REMOVE);
        ValidationDialog vd =
                new ValidationDialog(view,
                        UILabels.STL81101_REMOVE_ITEM
                                .getDescription(currentItem.getName())) {
                    private static final long serialVersionUID =
                            -8807399194554240022L;

                    /*
                     * (non-Javadoc)
                     * 
                     * @see com.intel.stl.ui.common.view.OptionDialog#onCancel()
                     */
                    @Override
                    public void onCancel() {
                        super.onCancel();
                        close();
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see com.intel.stl.ui.common.view.OptionDialog#onOk()
                     */
                    @Override
                    public void onOk() {
                        super.onOk();
                        showMessage(STLConstants.K2130_REMOVING.getValue());
                        SwingWorker<Integer, Void> worker =
                                getRemoveWorker(this, id);
                        worker.execute();
                    }

                };
        valModel.clear();

        vd.setValidationTableModel(valModel);
        vd.enableOk(false);
        vd.showDialog();

        vd.showMessage(STLConstants.K2129_VALIDATING.getValue());
        vd.startProgress();
        ValidationTask<T> vTask = getValidationTask(vd, currentItem);
        vTask.execute();
    }

    protected SwingWorker<Integer, Void> getRemoveWorker(
            final ValidationDialog vd, final long id) {
        SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {

            @Override
            protected Integer doInBackground() throws Exception {
                int index = indexOfWorkingItem(id);
                Item<T> item = workingItems.get(index);
                removeItemObject(item.getName());
                int orgIndex = indexOfOrgItem(id);
                orgItems.remove(orgIndex);

                workingItems.remove(index);
                if (index >= workingItems.size()) {
                    index -= 1;
                }
                currentItem = null;

                return index;
            }

            /*
             * (non-Javadoc)
             * 
             * @see javax.swing.SwingWorker#done()
             */
            @Override
            protected void done() {
                try {
                    Integer index = get();
                    view.updateItems();

                    view.selectItem(index);
                    vd.reportProgress("Done!");
                    vd.close();
                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    vd.showMessage(StringUtils.getErrorMessage(e));
                } finally {
                    vd.stopProgress();
                }
            }

        };
        return worker;
    }

    protected abstract void removeItemObject(String name) throws Exception;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.IItemEditorListener#nameChange(java.lang.String)
     */
    @Override
    public void nameChanged(String name) {
        currentItem.setName(name);
        view.updateItems();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.IItemEditorListener#onSave()
     */
    @Override
    public void onSave() {
        try {
            edtCtr.updateItem(currentItem);
        } catch (InvalidEditException e) {
            Util.showWarningMessage(view, e.getMessage());
            return;
        }

        if (!hasChange(currentItem)) {
            Util.showWarningMessage(view,
                    UILabels.STL81112_NO_CHANGES.getDescription());
            return;
        }

        ValidationDialog vd =
                new ValidationDialog(view,
                        UILabels.STL81100_SAVE_ITEM.getDescription(currentItem
                                .getName())) {
                    private static final long serialVersionUID =
                            -8807399194554240022L;

                    /*
                     * (non-Javadoc)
                     * 
                     * @see com.intel.stl.ui.common.view.OptionDialog#onCancel()
                     */
                    @Override
                    public void onCancel() {
                        super.onCancel();
                        close();
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see com.intel.stl.ui.common.view.OptionDialog#onOk()
                     */
                    @Override
                    public void onOk() {
                        super.onOk();
                        showMessage(STLConstants.K2128_SAVING.getValue());
                        SwingWorker<Void, Void> saveWorker =
                                getSaveWorker(this);
                        saveWorker.execute();
                    }

                };
        valModel.clear();

        vd.setValidationTableModel(valModel);
        vd.enableOk(false);
        vd.showDialog();

        vd.showMessage(STLConstants.K2129_VALIDATING.getValue());
        vd.startProgress();
        ValidationTask<T> vTask = getValidationTask(vd, currentItem);
        vTask.execute();
    }

    protected SwingWorker<Void, Void> getSaveWorker(final ValidationDialog vd) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                String oldName = null;
                int index = -1;
                if (currentItem.getState() == ChangeState.UPDATE) {
                    index = indexOfOrgItem(currentItem.getId());
                    Item<T> oldOrgItem = orgItems.get(index);
                    oldName = oldOrgItem.getName();
                }
                saveItemObject(oldName, currentItem.getObj());
                Item<T> newOrgItem = getCopy(currentItem, ChangeState.NONE);
                if (index >= 0) {
                    orgItems.set(index, newOrgItem);
                } else {
                    orgItems.add(newOrgItem);
                }
                currentItem.setState(ChangeState.UPDATE);
                return null;
            }

            /*
             * (non-Javadoc)
             * 
             * @see javax.swing.SwingWorker#done()
             */
            @Override
            protected void done() {
                try {
                    get();
                    vd.reportProgress("Done!");
                    vd.close();
                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    vd.showMessage(StringUtils.getErrorMessage(e));
                }
            }
        };
        return worker;
    }

    /**
     * 
     * <i>Description:</i> validate an item and put result in the
     * ValidationModel
     * 
     * @param model
     *            the validation model to update
     * @param obj
     *            the item to validate
     */
    protected abstract ValidationTask<T> getValidationTask(
            ValidationDialog dialog, Item<T> item);

    /**
     * <i>Description:</i>
     * 
     * @param obj
     */
    protected abstract void saveItemObject(String oldName, T obj)
            throws Exception;

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.IItemEditorListener#onReset()
     */
    @Override
    public void onReset() {
        int index = workingItems.indexOf(currentItem);
        Item<T> orgItem = getOrgItem(currentItem.getId());
        Item<T> newItem = getCopy(orgItem, ChangeState.NONE);
        workingItems.set(index, newItem);
        view.updateItems();
        edtCtr.setItem(newItem, getWorkingItems());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.IConfListener#onApply(boolean)
     */
    @Override
    public void onApply(boolean restart) {
        this.restart = restart;

        try {
            edtCtr.updateItem(currentItem);
        } catch (InvalidEditException e) {
            Util.showWarningMessage(view, e.getMessage());
            return;
        }

        if (hasChange(currentItem)) {
            int index = workingItems.indexOf(currentItem);
            int option = view.confirmDiscard();
            if (option != JOptionPane.YES_OPTION) {
                return;
            }

            if (currentItem.getState() == ChangeState.ADD) {
                // discard new added item
                currentItem = null;
                workingItems.remove(index);
                view.selectItem(workingItems.size() - 1);
            } else if (currentItem.getState() == ChangeState.UPDATE) {
                // change back to org item
                Item<T> orgItem = getOrgItem(currentItem.getId());
                Item<T> newItem = getCopy(orgItem, ChangeState.UPDATE);
                currentItem = newItem;
                workingItems.set(index, newItem);
                edtCtr.setItem(newItem, getWorkingItems());
            }
            view.updateItems();
        }

        if (!mgtApi.hasChanges()) {
            Util.showWarningMessage(view,
                    UILabels.STL81112_NO_CHANGES.getDescription());
            return;
        }

        view.showDeployCard(mgtApi.getSubnetDescription());
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
        if (mgtApi == null) {
            view.showLoginCard();
            view.setMessage(UILabels.STL10116_NOT_INIT.getDescription());
            view.setLoginEnabled(false);
            return;
        }

        // onEnter cancel any non-null future. Note: shouldn't be any.
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }

        if (mgtApi.isConfigReady()) {
            if (!view.isShowingDeployCard()) {
                view.showEditorCard();
                SwingWorker<ArrayList<Item<T>>, Void> worker = getInitWorker();
                worker.execute();
            }
        } else if (mgtApi.hasSession()) {
            loadConfigFile(null);
        } else {
            // display and ask for log in info
            // Set host name and port number
            view.setLoginEnabled(true);
            view.setHostNameField(mgtApi.getSubnetDescription().getCurrentFE()
                    .getHost());
            view.setUserNameField(mgtApi.getSubnetDescription().getCurrentFE()
                    .getSshUserName());
            view.showLoginCard(); // turn on login card
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onExit()
     */
    @Override
    public void onExit() {
        if (mgtApi == null) {
            return;
        }

        // Save username for ssl login to mgtApi for persistence between tabs
        // If we don't, the user can change user name on one of the tabs, switch
        // to another tab on Admin page and see a different user name being
        // displayed. Same goes for port number
        mgtApi.getSubnetDescription().getCurrentFE()
                .setSshUserName(view.getUserNameFieldStr());
        mgtApi.getSubnetDescription().getCurrentFE()
                .setSshPortNum(Integer.parseInt(view.getPortFieldStr()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onCancel()
     */
    @Override
    public void onCancelLogin() {
        if (future != null) {
            future.cancel(true);
        }

        // Call ManagementApi to cancel fetching of the config file.
        mgtApi.onCancelFetchConfig(mgtApi.getSubnetDescription());
        view.showLoginCard();
        view.clearLoginCard();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#canExit()
     */
    @Override
    public boolean canExit() {
        return !isBusy && deployCheck() && changeCheck();
    }

    protected boolean deployCheck() {
        if (view.isShowingDeployCard() && deployController.isBusy()) {
            int ret = deployController.confirmDiscard();
            if (ret == JOptionPane.YES_OPTION) {
                deployController.onCancel();
            } else {
                return false;
            }
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
        // Make the login card visible to get password from user to fetch a new
        // copy of config file
        if (changeCheck()) {
            if (mgtApi != null) {
                mgtApi.reset();
            }
            onEnter();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#clear()
     */
    @Override
    public void clear() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.IConfListener#prepare(com.intel.stl.ui.console
     * .LoginBean)
     */
    @Override
    public void prepare(LoginBean credentials) {
        isBusy = true;
        int portNum = Integer.parseInt(credentials.getPortNum());
        mgtApi.getSubnetDescription().getCurrentFE().setSshPortNum(portNum);
        mgtApi.getSubnetDescription().getCurrentFE()
                .setSshUserName(credentials.getUserName());
        loadConfigFile(credentials.getPassword());
    }

    protected void loadConfigFile(final char[] password) {
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    mgtApi.fetchConfigFile(password);
                    if (mgtApi.isConfigReady()) {
                        SwingWorker<ArrayList<Item<T>>, Void> worker =
                                getInitWorker();
                        worker.execute();
                    }
                } catch (JSchException e) {
                    e.printStackTrace();
                    view.showLoginCard();
                    view.setMessage(UILabels.STL81111_LOGIN_ERROR
                            .getDescription(StringUtils.getErrorMessage(e)));
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showLoginCard();
                    view.setMessage(StringUtils.getErrorMessage(e));
                } finally {
                    isBusy = false;
                    view.clearLoginCard();
                }
            }
        };

        future = taskScheduler.submitToBackground(task);

    }

}
