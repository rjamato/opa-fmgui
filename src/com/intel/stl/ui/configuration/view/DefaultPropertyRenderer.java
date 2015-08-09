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
 *  File Name: DefaultPropertyRenderer.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/02 16:26:55  jijunwan
 *  Archive Log:    improved property value label to be single line label with max width
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
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.model.IPropertyCategory;
import com.intel.stl.ui.model.PropertyItem;

public class DefaultPropertyRenderer implements IPropertyRenderer {
    private final static String NA = STLConstants.K0039_NOT_AVAILABLE
            .getValue();

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.configuration.view.IPropertyRenderer#getKeyHeaderComponent
     * (com.intel.stl.ui.model.IPropertyCategory, int,
     * com.intel.stl.ui.configuration.view.PropertyVizStyle)
     */
    @Override
    public Component getKeyHeaderComponent(IPropertyCategory<?> category,
            int row, PropertyVizStyle style) {
        String key = category.getKeyHeader();
        JLabel res =
                ComponentFactory.getH4Label(key == null ? "" : key, Font.BOLD);
        style.decorateHeaderKey(res, row);
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.configuration.view.IPropertyRenderer#getValueHeaderComponent
     * (com.intel.stl.ui.model.IPropertyCategory, int,
     * com.intel.stl.ui.configuration.view.PropertyVizStyle)
     */
    @Override
    public Component getValueHeaderComponent(IPropertyCategory<?> category,
            int row, PropertyVizStyle style) {
        String value = category.getValueHeader();
        JLabel res =
                ComponentFactory.getH4Label(value == null ? "" : value,
                        Font.BOLD);
        style.decorateHeaderKey(res, row);
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.configuration.view.IPropertyRenderer#getKeyComponent
     * (com.intel.stl.ui.model.PropertyItem, int, int,
     * com.intel.stl.ui.configuration.view.PropertyVizStyle)
     */
    @Override
    public Component getKeyComponent(PropertyItem<?> item, int itemIndex,
            int row, PropertyVizStyle style) {
        String key = item.getLabel();
        JLabel res =
                ComponentFactory.getH4Label(key == null ? "" : key, Font.PLAIN);
        res.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 3));
        style.decorateKey(res, itemIndex);
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.configuration.view.IPropertyRenderer#getValueComponent
     * (com.intel.stl.ui.model.PropertyItem, int, int,
     * com.intel.stl.ui.configuration.view.PropertyVizStyle)
     */
    @Override
    public Component getValueComponent(PropertyItem<?> item, int itemIndex,
            int row, PropertyVizStyle style) {
        String value = item.getValue();
        JLabel res =
                ComponentFactory.getH4Label(value == null ? NA : value,
                        Font.PLAIN);
        res =
                ComponentFactory.deriveLabel(res, false,
                        UIConstants.MAX_LABEL_Width);
        res.setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 2));
        style.decorateValue(res, itemIndex);
        return res;
    }
}
