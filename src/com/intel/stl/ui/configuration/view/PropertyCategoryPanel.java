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
 *  File Name: PropertyCategoryPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10  2015/02/02 16:26:55  jijunwan
 *  Archive Log:    improved property value label to be single line label with max width
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/10/22 02:15:33  jijunwan
 *  Archive Log:    1) abstracted property related panels to general panels that can be reused at other places
 *  Archive Log:    2) introduced renderer into property panels to allow customizes property render
 *  Archive Log:    3) generalized property style to be able to apply on any ui component
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/13 21:06:47  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/09 13:04:36  fernande
 *  Archive Log:    Adding IContextAware interface to generalize setting up Context
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/09/04 19:56:45  jijunwan
 *  Archive Log:    minor L&F adjustments on property viz
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/04 16:54:25  jijunwan
 *  Archive Log:    added code to support changing property viz style through UI
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/22 16:52:29  fernande
 *  Archive Log:    Closing the gaps between properties and sa_query
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/14 17:37:51  fernande
 *  Archive Log:    Closing the gap on device properties being displayed.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/04 21:17:34  fernande
 *  Archive Log:    Changed to adjust to new DeviceProperties model
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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.model.IPropertyCategory;
import com.intel.stl.ui.model.PropertyItem;

public class PropertyCategoryPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected PropertyVizStyle style;

    protected IPropertyRenderer propertyRenderer;

    public PropertyCategoryPanel() {
        this(new PropertyVizStyle());
    }

    /**
     * Description:
     * 
     * @param style
     */
    public PropertyCategoryPanel(PropertyVizStyle style) {
        super();
        this.style = style;
        initComponent();
    }

    protected void initComponent() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setBackground(UIConstants.INTEL_WHITE);
    }

    /**
     * @return the propertyRenderer
     */
    public IPropertyRenderer getPropertyRenderer() {
        if (propertyRenderer == null) {
            propertyRenderer = new DefaultPropertyRenderer();
        }
        return propertyRenderer;
    }

    /**
     * @param propertyRenderer
     *            the propertyRenderer to set
     */
    public void setPropertyRenderer(IPropertyRenderer propertyRenderer) {
        this.propertyRenderer = propertyRenderer;
    }

    /**
     * @return the style
     */
    public PropertyVizStyle getStyle() {
        return style;
    }

    /**
     * @param style
     *            the style to set
     */
    public void setStyle(PropertyVizStyle style) {
        this.style = style;
    }

    public <C extends IPropertyCategory<? extends PropertyItem<?>>> void setModel(
            C model) {
        int row = 0;
        removeAll();
        if (model.getKeyHeader() != null || model.getValueHeader() != null) {
            addHeaders(model, row);
            row++;
        }

        int itemIndex = 0;
        for (PropertyItem<?> item : model.getItems()) {
            addPropertyItem(item, row, itemIndex++);
            row++;
        }
        repaint();
    }

    private void addHeaders(IPropertyCategory<?> category, int row) {
        GridBagConstraints gc = createConstraints(2, 2, 0, 3);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        Component keyComp =
                getPropertyRenderer().getKeyHeaderComponent(category, row,
                        style);
        String valueHeader = category.getValueHeader();
        if (valueHeader != null && valueHeader.length() > 0) {
            add(keyComp, gc);
            gc = createConstraints(2, 2, 0, 3);
            gc.gridx = 1;
            gc.gridy = 0;
            Component valComp =
                    getPropertyRenderer().getValueHeaderComponent(category,
                            row, style);
            add(valComp, gc);
        } else {
            gc.gridwidth = 2;
            add(keyComp, gc);
        }
        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        add(Box.createGlue(), gc);
    }

    private void addPropertyItem(PropertyItem<?> item, int row, int itemIndex) {
        GridBagConstraints gc = createConstraints(0, 12, 0, 0);
        gc.gridx = 0;
        gc.gridy = row;
        Component keyComp =
                getPropertyRenderer().getKeyComponent(item, itemIndex, row,
                        style);
        add(keyComp, gc);

        gc = createConstraints(0, 0, 0, 3);
        gc.gridx = 1;
        gc.gridy = row;
        Component valComp =
                getPropertyRenderer().getValueComponent(item, itemIndex, row,
                        style);
        add(valComp, gc);
        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.weightx = 1;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        add(Box.createGlue(), gc);

    }

    private GridBagConstraints createConstraints(int yPadTop, int xPadLeft,
            int yPadBtm, int xPadRight) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(yPadTop, xPadLeft, yPadBtm, xPadRight);
        gc.weightx = 1;
        return gc;
    }

}
