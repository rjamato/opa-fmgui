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
 *  File Name: ResourceDetailView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/02/05 19:09:21  jijunwan
 *  Archive Log:    fixed a issue reported by klocwork that is actually not a problem
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/23 16:00:06  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/26 15:15:35  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/18 13:43:33  rjtierne
 *  Archive Log:    Added font style and insets to the Link/Path tabs
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/08 20:14:58  rjtierne
 *  Archive Log:    Renamed from ResourceDetailsView and now the view for the subpages
 *  Archive Log:    on the Topology page
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/24 20:30:30  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Subpage JCardView for the Topology page
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.network.view;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.common.view.IntelTabbedPaneUI;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.network.IResourceNodeSubpageController;

public class ResourceSubpageView extends JSectionView<ISectionListener> {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 5656598693998044183L;

    private JTabbedPane tabbedPane;

    private IntelTabbedPaneUI tabUI;

    private JPanel ctrPanel;

    /**
     * Description:
     * 
     * @param title
     */
    public ResourceSubpageView(String title) {
        super(title);
        // this is unnecessary, but can stop klocwork from complaining
        getMainComponent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.JCardView#getMainComponent()
     */
    @Override
    protected JComponent getMainComponent() {

        if (tabbedPane != null) {
            return tabbedPane;
        }

        // Create the tabbed pane which will be populated when getMainComponent
        // is called from subpages
        tabbedPane = new JTabbedPane();
        tabUI = new IntelTabbedPaneUI();
        ctrPanel = tabUI.getControlPanel();
        ctrPanel.setLayout(new BorderLayout());
        ctrPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 5));
        tabbedPane.setUI(tabUI);
        tabUI.setFont(UIConstants.H4_FONT);
        tabUI.setTabAreaInsets(new Insets(2, 5, 4, 5));

        return tabbedPane;
    }

    public String getCurrentSubpage() {
        int currentTab = tabbedPane.getSelectedIndex();
        if (currentTab < 0) {
            return null;
        } else {
            return tabbedPane.getTitleAt(currentTab);
        }
    }

    public void setTabs(List<IResourceNodeSubpageController> subpages,
            int selection) {
        // remove all old tabs
        // add the view of each subpage to our tabbed pane
        tabbedPane.removeAll();

        for (IResourceNodeSubpageController subpage : subpages) {
            tabbedPane.addTab(subpage.getName(), subpage.getIcon(),
                    subpage.getView(), subpage.getDescription());
        }

        tabbedPane.setSelectedIndex(selection > 0 ? selection : 0);
    }

    public void clearPage(NodeType type) {

    }

}
