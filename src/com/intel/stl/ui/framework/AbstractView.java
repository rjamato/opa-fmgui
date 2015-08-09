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
 *  File Name: FVBaseView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2014/10/22 01:15:24  jijunwan
 *  Archive Log:    some simplifications on MVC framework
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/09 12:54:07  fernande
 *  Archive Log:    Added support for PropertyChange events bubbling up from an SwingWorker, thru an AbstractTask into the controller.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/14 17:38:32  fernande
 *  Archive Log:    Closing the gap on device properties being displayed.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/22 21:48:13  fernande
 *  Archive Log:    Changes to framework to support model update lifecycle by notifying views of changes
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/09 13:31:46  fernande
 *  Archive Log:    Moving MVC framework to its own package and renaming for consistency
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/08 20:27:15  fernande
 *  Archive Log:    Basic MVC framework with SwingWorker support
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.framework;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class AbstractView<M extends AbstractModel, C extends IController>
        extends JPanel implements IView<M, C>, IModelListener<M> {

    private static final long serialVersionUID = 1L;

    protected C controller;

    protected JComponent mainComponent;

    @Override
    public C getController() {
        return controller;
    }

    @Override
    public void setController(C controller) {
        this.controller = controller;
    }

    @Override
    public abstract void modelUpdateFailed(M model, Throwable caught);

    @Override
    public abstract void modelChanged(M model);

    @Override
    public Component add(Component comp) {
        return mainComponent.add(comp);
    }

    @Override
    public Component add(Component comp, int index) {
        return mainComponent.add(comp, index);
    }

    @Override
    public void add(Component comp, Object constraints) {
        mainComponent.add(comp, constraints);
    }

    /**
     * Only the mainComponent should be laid out on this component; we override
     * the add(Component comp) methods to make sure that no extending class
     * inadvertently adds a component. Extenders should add components to the
     * mainComponent.
     */
    @Override
    public void initView() {
        this.mainComponent = getMainComponent();
        setLayout(new BorderLayout());
        super.add(mainComponent);
        initComponents();
    }

    /**
     * Defines the main component for this widget; this component is added to
     * the view and occupies all the screen area of the view (see initView())
     */
    public abstract JComponent getMainComponent();

    /**
     * Initializes all the components on the main component of this widget
     */
    public abstract void initComponents();

}
