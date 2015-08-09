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
 *  File Name: FabricView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/03/10 18:43:11  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/22 01:16:57  jijunwan
 *  Archive Log:    some simplifications on MVC framework
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/09 13:00:25  fernande
 *  Archive Log:    Changed the FabricController to use the UI framework and converted Swing workers into AbstractTasks to optimize the switching of contexts and the refreshing of pages. These processes still run under Swing workers, but now each setContext is run on its own Swing worker to improve performance. Also, changed the ProgressObserver mechanism to provide a more accurate progress.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.main.view;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.intel.stl.ui.framework.AbstractView;
import com.intel.stl.ui.main.FabricController;
import com.intel.stl.ui.main.FabricModel;

/**
 * This component is just a passthrough between FabricController and FVMainFrame
 * for our MVC framework plumbing. This is needed because in our MVC framework,
 * all views extend from JPanel, but FabricController is the controller for the
 * main application window, which is a JFrame. We could make every view extend
 * from java.awt.Container, but this would result in a lot of functionality
 * needed to support JPanels and JFrames. For only one JFrame, this approach
 * seemed more feasible.
 * 
 */
public class FabricView extends AbstractView<FabricModel, FabricController> {

    private static final long serialVersionUID = 1L;

    private final FVMainFrame mainFrame;

    public FabricView(FVMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public IFabricView getView() {
        return mainFrame;
    }

    public FVMainFrame getMainFrame() {
        return mainFrame;
    }

    @Override
    public void modelUpdateFailed(FabricModel model, Throwable caught) {
        mainFrame.modelUpdateFailed(model, caught);
    }

    @Override
    public void modelChanged(FabricModel model) {
        mainFrame.modelChanged(model);
    }

    @Override
    public JComponent getMainComponent() {
        // This panel is not used at all
        return new JPanel();
    }

    @Override
    public void initComponents() {
        mainFrame.initComponents();
    }

    @Override
    public void setController(FabricController controller) {
        super.setController(controller);
        mainFrame.setController(controller);
    }
}
