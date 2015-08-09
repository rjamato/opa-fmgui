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
 *  File Name: DevicePropertyGroupPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/04/10 14:23:02  jijunwan
 *  Archive Log:    PR 127495 - Add LED indicator bit to STL_PORT_STATES
 *  Archive Log:    -- display LED Enabled in port properties
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/03/26 17:38:21  jypak
 *  Archive Log:    Online Help updates for additional panels.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/03/10 18:43:13  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/31 17:49:39  jypak
 *  Archive Log:    1. CableInfo updates (Moved the QSFP interpretation logic to backend etc.)
 *  Archive Log:    2. SC2SL updates.
 *  Archive Log:    3. SC2VLt updates.
 *  Archive Log:    4. SC3VLnt updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/19 07:13:28  jypak
 *  Archive Log:    HoQLife, VL Stall Count : property bar chart panel updates
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/11/13 00:36:47  jypak
 *  Archive Log:    MTU by VL bar chart panel updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/22 02:15:33  jijunwan
 *  Archive Log:    1) abstracted property related panels to general panels that can be reused at other places
 *  Archive Log:    2) introduced renderer into property panels to allow customizes property render
 *  Archive Log:    3) generalized property style to be able to apply on any ui component
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration.view;

import java.awt.Component;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JComponent;

import com.intel.stl.api.configuration.ResourceCategory;
import com.intel.stl.ui.configuration.HoQLifeBarChartController;
import com.intel.stl.ui.configuration.LFTHistogramController;
import com.intel.stl.ui.configuration.MTUByVLBarChartController;
import com.intel.stl.ui.configuration.MultiColumnCategoryController;
import com.intel.stl.ui.configuration.PropertyCategoryController;
import com.intel.stl.ui.configuration.PropertyGroupController;
import com.intel.stl.ui.configuration.SC2SLMTBarChartController;
import com.intel.stl.ui.configuration.SC2VLNTMTBarChartController;
import com.intel.stl.ui.configuration.SC2VLTMTBarChartController;
import com.intel.stl.ui.configuration.VLStallCountByVLBarChartController;
import com.intel.stl.ui.framework.AbstractView;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.model.DevicePropertyCategory;
import com.intel.stl.ui.model.DevicePropertyGroup;

public class DevicePropertyGroupPanel extends
        AbstractView<DevicePropertyGroup, PropertyGroupController> {
    private static final long serialVersionUID = 4539967617107147387L;

    private GroupPanel mainPanel;

    protected PropertyVizStyle style;

    /**
     * Description:
     * 
     * @param style
     */
    public DevicePropertyGroupPanel(PropertyVizStyle style) {
        super();
        this.style = style;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.framework.AbstractView#modelUpdateFailed(com.intel.stl
     * .ui.framework.AbstractModel, java.lang.Throwable)
     */
    @Override
    public void modelUpdateFailed(DevicePropertyGroup model, Throwable caught) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.framework.AbstractView#modelChanged(com.intel.stl.ui
     * .framework.AbstractModel)
     */
    @Override
    public void modelChanged(DevicePropertyGroup model) {
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
            mainPanel = new GroupPanel(style);
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

    class GroupPanel extends
            PropertyGroupPanel<DevicePropertyCategory, DevicePropertyGroup> {
        private static final long serialVersionUID = 6635431613502243647L;

        private HelpAction helpAction;

        private HelpBroker helpBroker;

        private HelpSet helpSet;

        /**
         * Description:
         * 
         * @param style
         */
        public GroupPanel(PropertyVizStyle style) {
            super(style);
        }

        @Override
        protected Component createCategoryPanel(
                DevicePropertyCategory category, PropertyVizStyle style) {
            Component categoryPanel;
            // Usually controller should enable help. However, this GroupPanel
            // is supposed to enable help and it is only exposed as JComponent
            // even to DevicePropertyGroupPanel.
            helpAction = HelpAction.getInstance();
            helpBroker = helpAction.getHelpBroker();
            helpSet = helpAction.getHelpSet();
            if (category.size() < 26) {
                if (category.getCategory() == ResourceCategory.LFT_HISTOGRAM) {
                    categoryPanel = new LFTHistogramPanel();
                    new LFTHistogramController(category,
                            (LFTHistogramPanel) categoryPanel, null);

                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getLft(), helpSet);

                } else if (category.getCategory() == ResourceCategory.SC2SLMT_CHART) {
                    categoryPanel = new SC2SLMTBarChartPanel();
                    new SC2SLMTBarChartController(category,
                            (SC2SLMTBarChartPanel) categoryPanel, null);

                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getSC2SL(), helpSet);

                } else if (category.getCategory() == ResourceCategory.SC2VLTMT_CHART) {
                    categoryPanel = new SC2VLTMTBarChartPanel();
                    new SC2VLTMTBarChartController(category,
                            (SC2VLTMTBarChartPanel) categoryPanel, null);
                } else if (category.getCategory() == ResourceCategory.SC2VLNTMT_CHART) {
                    categoryPanel = new SC2VLNTMTBarChartPanel();
                    new SC2VLNTMTBarChartController(category,
                            (SC2VLNTMTBarChartPanel) categoryPanel, null);

                } else if (category.getCategory() == ResourceCategory.MTU_CHART) {
                    categoryPanel = new MTUByVLBarChartPanel();
                    new MTUByVLBarChartController(category,
                            (MTUByVLBarChartPanel) categoryPanel, null);

                } else if (category.getCategory() == ResourceCategory.HOQLIFE_CHART) {
                    categoryPanel = new HoQLifeBarChartPanel();
                    new HoQLifeBarChartController(category,
                            (HoQLifeBarChartPanel) categoryPanel, null);

                } else if (category.getCategory() == ResourceCategory.VL_STALL_CHART) {
                    categoryPanel = new VLStallCountByVLBarChartPanel();
                    new VLStallCountByVLBarChartController(category,
                            (VLStallCountByVLBarChartPanel) categoryPanel, null);

                } else {
                    categoryPanel = new DevicePropertyCategoryPanel(style);
                    new PropertyCategoryController(category,
                            (DevicePropertyCategoryPanel) categoryPanel, null);

                    enableHelpOnCategory(category.getCategory());
                }
            } else {
                if (category.size() < 51) {
                    categoryPanel = new MultiColumnCategoryPanel(2, style);
                    enableHelpOnCategory(category.getCategory());
                } else {
                    categoryPanel = new PagingCategoryPanel(2, 16, 5, style);
                }
                new MultiColumnCategoryController(category,
                        (MultiColumnCategoryPanel) categoryPanel, null);
            }
            return categoryPanel;
        }

        private void enableHelpOnCategory(ResourceCategory category) {
            switch (category) {
                case NODE_INFO:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getNodeGeneral(), helpSet);
                    break;
                case SWITCH_INFO:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getSwitchInformation(), helpSet);
                    break;
                case SWITCH_ROUTING:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getRoutingInformation(), helpSet);
                    break;
                case DEVICE_GROUPS:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getDeviceGroup(), helpSet);
                    break;
                case MFT_TABLE:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getMft(), helpSet);
                    break;
                case PORT_INFO:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getPortDevInfo(), helpSet);
                    break;
                case LINK_WIDTH:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getPortLink(), helpSet);
                    break;
                case LINK_CONNECTED_TO:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getPortLinkConn(), helpSet);
                    break;
                case PORT_CAPABILITIES:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getPortCap(), helpSet);
                    break;
                case VIRTUAL_LANE:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getVL(), helpSet);
                    break;
                case PORT_DIAGNOSTICS:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getDiagnostics(), helpSet);
                    break;
                case PORT_PARTITION_ENFORCEMENT:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getPartition(), helpSet);
                    break;
                case PORT_MANAGEMENT:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getManagement(), helpSet);
                    break;
                case FLIT_CTRL_INTERLEAVE:
                    helpBroker.enableHelpOnButton(getHelpButton(),
                            helpAction.getFlitControl(), helpSet);
                    break;

                default:
                    break;
            }
        };

    }
}
