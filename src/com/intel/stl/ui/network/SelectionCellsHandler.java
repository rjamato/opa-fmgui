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
 *  File Name: SelectionCellsHandler.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/04/10 20:20:00  fernande
 *  Archive Log:    Changed TopologyView to be passed two background services (graphService and outlineService) which now reside in FabricController and can be properly shutdown when an error occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/11/03 23:06:12  jijunwan
 *  Archive Log:    improvement on topology view - drawing graph on background
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import java.awt.event.MouseEvent;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.intel.stl.ui.common.IBackgroundService;
import com.mxgraph.swing.handler.mxSelectionCellsHandler;

/**
 * Wrapper of mxSelectionCellsHandler that executes with a specified
 * ExecutorService
 */
public class SelectionCellsHandler extends mxSelectionCellsHandler {
    private IBackgroundService updateService;

    private final AtomicInteger taskId = new AtomicInteger();

    /**
     * Description:
     * 
     * @param graphComponent
     * @param updateService
     */
    public SelectionCellsHandler(TopGraphComponent graphComponent) {
        super(graphComponent);
    }

    /**
     * @param updateService
     *            the updateService to set
     */
    public void setUpdateService(IBackgroundService updateService) {
        this.updateService = updateService;
    }

    protected void runInServeice(final Runnable runner,
            final boolean replaceable) {
        final int id = taskId.incrementAndGet();
        updateService.submit(new Runnable() {
            @Override
            public void run() {
                if (replaceable && id != taskId.get()) {
                    return;
                }

                runner.run();
            }
        });
    }

    protected <E> E runAndWait(final Callable<E> caller,
            final boolean replaceable) {
        final int id = taskId.incrementAndGet();
        Future<E> task = updateService.submit(new Callable<E>() {
            @Override
            public E call() throws Exception {
                if (replaceable && id != taskId.get()) {
                    return null;
                }

                return caller.call();
            }
        });
        E res = null;
        try {
            res = task.get();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.mxgraph.swing.handler.mxSelectionCellsHandler#mousePressed(java.awt
     * .event.MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent e) {
        runInServeice(new Runnable() {
            @Override
            public void run() {
                SelectionCellsHandler.super.mousePressed(e);
            }
        }, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.mxgraph.swing.handler.mxSelectionCellsHandler#mouseMoved(java.awt
     * .event.MouseEvent)
     */
    @Override
    public void mouseMoved(final MouseEvent e) {
        runInServeice(new Runnable() {
            @Override
            public void run() {
                SelectionCellsHandler.super.mouseMoved(e);
            }
        }, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.mxgraph.swing.handler.mxSelectionCellsHandler#mouseDragged(java.awt
     * .event.MouseEvent)
     */
    @Override
    public void mouseDragged(final MouseEvent e) {
        runInServeice(new Runnable() {
            @Override
            public void run() {
                SelectionCellsHandler.super.mouseDragged(e);
            }
        }, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.mxgraph.swing.handler.mxSelectionCellsHandler#mouseReleased(java.
     * awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(final MouseEvent e) {
        runInServeice(new Runnable() {
            @Override
            public void run() {
                SelectionCellsHandler.super.mouseReleased(e);
            }
        }, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.mxgraph.swing.handler.mxSelectionCellsHandler#getToolTipText(java
     * .awt.event.MouseEvent)
     */
    @Override
    public String getToolTipText(final MouseEvent e) {
        return runAndWait(new Callable<String>() {
            @Override
            public String call() {
                return SelectionCellsHandler.super.getToolTipText(e);
            }
        }, true);
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see
    // * com.mxgraph.swing.handler.mxSelectionCellsHandler#paintHandles(java.awt
    // * .Graphics)
    // */
    // @Override
    // public void paintHandles(final Graphics g) {
    // super.paintHandles(g);
    // }

    /*
     * (non-Javadoc)
     * 
     * @see com.mxgraph.swing.handler.mxSelectionCellsHandler#reset()
     */
    @Override
    public void reset() {
        runInServeice(new Runnable() {
            @Override
            public void run() {
                SelectionCellsHandler.super.reset();
            }
        }, false);
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see com.mxgraph.swing.handler.mxSelectionCellsHandler#refresh()
    // */
    // @Override
    // public void refresh() {
    // runAndWait(new Runnable() {
    // @Override
    // public void run() {
    // SelectionCellsHandler.super.refresh();
    // }
    // }, false);
    // }

}
