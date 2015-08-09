/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: BlankView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/16 22:08:15  jijunwan
 *  Archive Log:    added device group visualization on UI
 *  Archive Log:    some refactory to make the conf framework to be more general
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:38:18  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intel.stl.ui.admin.view.BlankView.BlankEditorPanel;
import com.intel.stl.ui.common.UIConstants;

public class BlankView extends AbstractConfView<Object, BlankEditorPanel> {
    private static final long serialVersionUID = 1264247778661597631L;

    public BlankView(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.AbstractConfView#createrEditorPanel()
     */
    @Override
    protected BlankEditorPanel createrEditorPanel() {
        return new BlankEditorPanel();
    }

    public static class BlankEditorPanel extends AbstractEditorPanel<Object> {
        private static final long serialVersionUID = 5486825273954504045L;

        private JPanel mainPanel;

        private JLabel label;

        /*
         * (non-Javadoc)
         * 
         * @see com.intel.stl.ui.admin.view.AbstractEditorPanel#getMainPanel()
         */
        @Override
        protected JPanel getMainComponent() {
            if (mainPanel == null) {
                mainPanel = new JPanel();
                mainPanel.setBackground(UIConstants.INTEL_WHITE);
                label = new JLabel();
                mainPanel.add(label);
            }
            return mainPanel;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.intel.stl.ui.admin.view.AbstractEditorPanel#showItemObject(java
         * .lang.Object, java.lang.String[], boolean)
         */
        @Override
        protected void showItemObject(Object app, String[] appNames,
                boolean isEditable) {
            label.setText(app.toString());
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.intel.stl.ui.admin.view.AbstractEditorPanel#updateItemObject(
         * java.lang.Object)
         */
        @Override
        protected void updateItemObject(Object obj) {
        }

    }
}
