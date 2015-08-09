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
package com.intel.stl.ui.main;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.List;

import com.intel.stl.api.ISubnetEventListener;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.framework.ITask;
import com.intel.stl.ui.main.view.IFabricView;
import com.intel.stl.ui.publisher.TaskScheduler;

/**
 * @author jijunwan
 * 
 */
public interface IFabricController extends ISubnetEventListener {
    void init();

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

    void addPendingTask(ITask pendingTask);

    void bringToFront();

    Rectangle getBounds();

    boolean isMaximized();

    void applyHelpAction(ActionEvent event);
}
