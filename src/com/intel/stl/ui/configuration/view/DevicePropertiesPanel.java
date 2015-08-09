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
 *  File Name: BasicPropertyPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/10/22 02:15:33  jijunwan
 *  Archive Log:    1) abstracted property related panels to general panels that can be reused at other places
 *  Archive Log:    2) introduced renderer into property panels to allow customizes property render
 *  Archive Log:    3) generalized property style to be able to apply on any ui component
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/09 13:04:36  fernande
 *  Archive Log:    Adding IContextAware interface to generalize setting up Context
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/09/09 13:22:05  jijunwan
 *  Archive Log:    set scroll unit for property scroll pane
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/04 21:14:24  jijunwan
 *  Archive Log:    performance improvement - now we only figure out panel width once
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/04 21:00:02  jijunwan
 *  Archive Log:    fixed an issue on number of columns calculation - it will be slightly slow because it will look through all panels to figure out the right number of columns
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/04 16:54:25  jijunwan
 *  Archive Log:    added code to support changing property viz style through UI
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/12 20:58:03  jijunwan
 *  Archive Log:    1) renamed HexUtils to StringUtils
 *  Archive Log:    2) added a method to StringUtils to get error message for an exception
 *  Archive Log:    3) changed all code to call StringUtils to get error message
 *  Archive Log:    4) some extra ode format change
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/25 20:28:04  fernande
 *  Archive Log:    New property views
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration.view;

import java.util.List;

import javax.swing.JComponent;

import com.intel.stl.api.configuration.PropertyGroup;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.configuration.DevicePropertiesController;
import com.intel.stl.ui.framework.AbstractView;
import com.intel.stl.ui.model.DeviceProperties;

public class DevicePropertiesPanel extends
        AbstractView<DeviceProperties, DevicePropertiesController> {
    private static final long serialVersionUID = 8967309999880078495L;

    private PropertiesPanel<DeviceProperties> mainPanel;

    public void clearPanel() {
        mainPanel.clearPanel();
    }

    public void clear() {
        mainPanel.showMessage(UILabels.STL10109_LOADING_PROPERTY
                .getDescription());
    }

    public void addPropertyGroupPanel(DevicePropertyGroupPanel groupPanel) {
        mainPanel.addPropertyGroupPanel(groupPanel.getMainComponent());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.framework.AbstractView#setController(com.intel.stl.ui
     * .framework.IController)
     */
    @Override
    public void setController(DevicePropertiesController controller) {
        super.setController(controller);
        if (mainPanel != null) {
            mainPanel.setStyleListener(controller);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.framework.AbstractView#modelUpdateFailed(com.intel.stl
     * .ui.framework.AbstractModel, java.lang.Throwable)
     */
    @Override
    public void modelUpdateFailed(DeviceProperties model, Throwable caught) {
        mainPanel.showError(caught);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.framework.AbstractView#modelChanged(com.intel.stl.ui
     * .framework.AbstractModel)
     */
    @Override
    public void modelChanged(DeviceProperties model) {
        mainPanel.setModel(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.framework.AbstractView#getMainComponent()
     */
    @Override
    public JComponent getMainComponent() {
        if (mainPanel == null) {
            mainPanel = new PropertiesPanel<DeviceProperties>();
            if (controller != null) {
                mainPanel.setStyleListener(controller);
            }
        }
        return mainPanel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.framework.AbstractView#initComponents()
     */
    @Override
    public void initComponents() {
    }

    public void initUserSettings(List<PropertyGroup> groups) {
        mainPanel.initUserSettings(groups);
    }
}
