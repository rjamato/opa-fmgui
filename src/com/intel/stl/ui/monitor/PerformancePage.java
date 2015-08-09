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
 *  File Name: PerformancePage.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.22  2015/04/03 21:06:27  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/11/05 22:57:35  jijunwan
 *  Archive Log:    improved the stability of turning on/off refresh icon when we response to notice events
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/10/14 11:32:09  jypak
 *  Archive Log:    UI updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/10/09 12:35:09  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/09/15 15:24:32  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/09/02 19:24:29  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/08/26 15:15:27  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/07/11 19:26:54  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/06/26 15:49:10  jijunwan
 *  Archive Log:    performance improvement - share tree model among pages so we do not build model several times
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/06/26 15:00:15  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/05/23 19:22:53  rjtierne
 *  Archive Log:    Implemented method clear() as required by the implemented
 *  Archive Log:    interface. Currently no tasks to deregister.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/05/16 04:30:38  jijunwan
 *  Archive Log:    Added code to deregister from task scheduler; Added Page Listener to listen enter or exit a (sub)page
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/05/01 16:31:25  rjtierne
 *  Archive Log:    Wrong name on CVS Header
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/05/01 16:30:48  rjtierne
 *  Archive Log:    Added CVS header
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor;

import static com.intel.stl.ui.common.PageWeight.MEDIUM;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.event.NodeUpdateEvent;
import com.intel.stl.ui.event.TaskStatusEvent;
import com.intel.stl.ui.event.TaskStatusEvent.Status;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.monitor.tree.FVTreeManager;
import com.intel.stl.ui.monitor.view.PerformanceTreeView;

public class PerformancePage implements IPageController {

    /**
     * Performance Tree Controller
     */
    PerformanceTreeController mPerfTreeController;

    private final PerformanceTreeView mPerfTreeView;

    private final MBassador<IAppEvent> eventBus;

    public PerformancePage(PerformanceTreeView view,
            MBassador<IAppEvent> eventBus, FVTreeManager treeBuilder) {
        mPerfTreeView = view;
        mPerfTreeController =
                new PerformanceTreeController(view, eventBus, treeBuilder);
        this.eventBus = eventBus;
        eventBus.subscribe(this);
    }

    /**
     * @param context
     *            the context to set
     */
    @Override
    public void setContext(Context context, IProgressObserver observer) {
        mPerfTreeController.setContext(context, observer);
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
        mPerfTreeController.onRefresh(observer);
    }

    /**
     * 
     * <i>Description:</i>The publish method will block(synch call) until all
     * other handlers are invoked. However, we cannot guarantee the order of
     * notice update complete order for different notices if multiple
     * invocations interleave.
     * 
     * @param evt
     */
    @Handler(priority = 1)
    public synchronized void onNodeUpdate(NodeUpdateEvent evt) {
        TaskStatusEvent<NodeUpdateEvent> taskEvent =
                new TaskStatusEvent<NodeUpdateEvent>(this, evt, Status.STARTED);
        eventBus.publish(taskEvent);
        try {
            mPerfTreeController.onNodeUpdate(evt);
        } finally {
            taskEvent =
                    new TaskStatusEvent<NodeUpdateEvent>(this, evt,
                            Status.FINISHED);
            eventBus.publish(taskEvent);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.IPage#getName()
     */
    @Override
    public String getName() {
        return STLConstants.K0200_PERFORMANCE.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.IPage#getDescription()
     */
    @Override
    public String getDescription() {
        return STLConstants.K0201_PERFORMANCE_DESCRIPTION.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.IPage#getView()
     */
    @Override
    public JPanel getView() {
        return mPerfTreeView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.IPage#getIcon()
     */
    @Override
    public ImageIcon getIcon() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPage#cleanup()
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
