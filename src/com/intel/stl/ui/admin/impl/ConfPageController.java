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
 *  File Name: EmptyPageController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.13.2.1  2015/05/06 19:39:21  jijunwan
 *  Archive Log:    changed to directly show exception(s)
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
import java.awt.Window;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.intel.stl.api.DefaultLoginAssistant;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.management.IManagementApi;
import com.intel.stl.ui.admin.ChangeState;
import com.intel.stl.ui.admin.IConfListener;
import com.intel.stl.ui.admin.IItemEditorListener;
import com.intel.stl.ui.admin.IItemListListener;
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
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.publisher.TaskScheduler;

public abstract class ConfPageController<T, E extends AbstractEditorPanel<T>>
        implements IPageController, IConfListener, IItemListListener,
        IItemEditorListener {
    private final String name;

    private final String description;

    private final ImageIcon icon;

    private final AbstractConfView<T, E> view;

    protected IManagementApi mgtApi;

    protected TaskScheduler taskScheduler;

    protected final AbstractEditorController<T, E> edtCtr;

    protected ArrayList<Item<T>> orgItems;

    protected DefaultListModel<Item<T>> workingItems;

    protected Item<T> currentItem;

    protected ValidationModel<T> valModel;

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
        workingItems = new DefaultListModel<Item<T>>();
        view.setListModel(workingItems);
        view.addItemListListener(this);
        view.setConfListener(this);

        E edtPanel = view.getEditorPanel();
        edtPanel.setEditorListener(this);
        edtCtr = creatEditorController(edtPanel);

        valModel = new ValidationModel<T>();
    }

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
            Component owner = context.getOwner();
            Window win = null;
            if (owner instanceof Window) {
                win = (Window) owner;
            } else {
                Component root = SwingUtilities.getRoot(owner);
                if (root instanceof Window) {
                    win = (Window) root;
                }
            }
            mgtApi.setLoginAssistant(new DefaultLoginAssistant(win, context
                    .getSubnetDescription().getCurrentFE().getHost(), context
                    .getSubnetDescription().getCurrentUser()));
            taskScheduler = context.getTaskScheduler();
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
                            orgItems = get();
                            if (orgItems == null) {
                                return;
                            }

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
        synchronized (orgItems) {
            if (orgItems == null) {
                // this shouldn't happen
                throw new RuntimeException("No item list!");
            }
            for (int i = 0; i < orgItems.size(); i++) {
                Item<T> item = orgItems.get(i);
                if (item.getId() == id) {
                    return i;
                }
            }
            // this shouldn't happen
            throw new IllegalArgumentException("Couldn't find item with id="
                    + id);
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

        edtCtr.updateItem(currentItem);
        if (hasChange(currentItem)) {
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
        System.out
                .println((orgItem == null ? "null" : orgItem
                        .getFullDescription())
                        + " "
                        + workingItem.getFullDescription());
        return orgItem == null || !orgItem.equals(workingItem);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.IItemListListener#onSelect(java.lang.String)
     */
    @Override
    public void onSelect(long id) {
        System.out.println("Select " + id);
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
        System.out.println("Select " + item.getFullDescription());
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
        } catch (Exception e) {
            e.printStackTrace();
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
                currentItem.setState(ChangeState.NONE);
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
    public void onApply(final boolean restart) {
        edtCtr.updateItem(currentItem);
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

        Util.showWarningMessage(view,
                UILabels.STL81110_DEPLOY_MSG.getDescription(),
                STLConstants.K0031_WARNING.getValue());
        taskScheduler.submitToBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    mgtApi.deploy(restart);
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg =
                            UILabels.STL81000_DEPLOY_ERROR
                                    .getDescription(StringUtils
                                            .getErrorMessage(e));
                    Util.showErrorMessage(view, msg);
                }
            }
        });
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
        SwingWorker<ArrayList<Item<T>>, Void> worker = getInitWorker();
        worker.execute();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onExit()
     */
    @Override
    public void onExit() {
        // TODO: if has change, deploy to FM
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
     * @see
     * com.intel.stl.ui.common.IPageController#onRefresh(com.intel.stl.ui.common
     * .IProgressObserver)
     */
    @Override
    public void onRefresh(IProgressObserver observer) {
        if (changeCheck()) {
            SwingWorker<ArrayList<Item<T>>, Void> worker = getInitWorker();
            worker.execute();
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

}
