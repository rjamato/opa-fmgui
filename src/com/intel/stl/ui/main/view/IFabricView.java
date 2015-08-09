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
package com.intel.stl.ui.main.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.List;

import com.intel.stl.ui.common.EventTableController;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.view.EventSummaryBarPanel;

/**
 * @author jijunwan
 * 
 */
public interface IFabricView {
    void setSubnetName(String subnetName);

    String getSubnetName();

    void showInitScreen(Rectangle bounds, boolean maximized);

    void setTitle(String title);

    void showMessageAndExit(String message, String title);

    void showMessage(String message, String title);

    void displayErrorMessage(String windowTitle, Exception exception);

    void showErrors(List<Throwable> errors);

    void showContent(List<IPageController> pages);

    void close();

    void resetConnectMenu();

    void setCurrentTab(IPageController page);

    void setReady(boolean b);

    boolean isReady();

    void showProgress(String label, boolean b);

    void setProgress(int progress);

    void setProgressNote(String note);

    void toggleEventSummaryTable();

    void showEventSummaryTable();

    void hideEventSummaryTable();

    /**
     * 
     * Description: this method should be unnecessary if we have a good MVC
     * design pattern. On view we should have no any cleanup to do since it
     * suppose to be a dummy renderer. We are adding this method just in case.
     * For now, this is mainly for stopping timer used for testing purpose.
     * 
     */
    void cleanup();

    void clear();

    void bringToFront();

    Rectangle getFrameBounds();

    boolean isFrameMaximized();

    void setWizardAction(ActionListener listener);

    void setRandomAction(ActionListener listener);

    void setWindowAction(WindowListener listener);

    void setRefreshAction(ActionListener listener);

    void setRefreshRunning(boolean isRunning);

    void setPageListener(IPageListener listener);

    EventSummaryBarPanel getEventSummaryBarPanel();

    EventTableController getEventTableController();

    Component getView();

    Dimension getScreenSize();

    void setScreenSize(Dimension dimension);

    Point getScreenPosition();

}
