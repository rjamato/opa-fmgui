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
 *  File Name: ResourceCardView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2014/11/05 16:37:15  jijunwan
 *  Archive Log:    renamed ResoureLinkCard to ResourceLinkSection since it a section now
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/23 16:00:06  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/22 02:21:27  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/26 15:15:35  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/18 13:43:43  rjtierne
 *  Archive Log:    Added the Path view to the card map
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/10 14:40:35  rjtierne
 *  Archive Log:    Added new LinkSubpageCard/View when to be displayed when links
 *  Archive Log:    are selected from the topology graph
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/08 20:19:43  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Top level view under the topology page to house the swappable
 *  overview panel and subpage views
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.network.view;

import java.awt.CardLayout;
import java.util.Map;

import javax.swing.JPanel;

import com.intel.stl.ui.network.ResourceAllSection;
import com.intel.stl.ui.network.ResourceLinkSection;
import com.intel.stl.ui.network.ResourceScopeType;
import com.intel.stl.ui.network.ResourceNodeSection;

public class ResourceView extends JPanel {
    
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 2769989356451342557L;
    
    private CardLayout layout;

    public ResourceView() {
        initComponents();
    }
    
    
    private void initComponents() {
        layout = new CardLayout();
        setLayout(layout);
    }
    
    
    public void initializeViews(Map<ResourceScopeType, Object> cardMap) {
        
        Object obj = cardMap.get(ResourceScopeType.ALL);        
        if (obj instanceof ResourceAllSection) {
            add(((ResourceAllSection)obj).getView(), ResourceScopeType.ALL.name());
        }
        
        obj = cardMap.get(ResourceScopeType.NODE);
        if (obj instanceof ResourceNodeSection) {
            add(((ResourceNodeSection)obj).getView(), ResourceScopeType.NODE.name());            
        }
        
        obj = cardMap.get(ResourceScopeType.LINK);
        if (obj instanceof ResourceLinkSection) {
            add(((ResourceLinkSection)obj).getView(), ResourceScopeType.LINK.name());
        }
        
        obj = cardMap.get(ResourceScopeType.PATH);
        if (obj instanceof ResourceLinkSection) {
            add(((ResourceLinkSection)obj).getView(), ResourceScopeType.PATH.name());
        }
        
        layout.show(this, ResourceScopeType.ALL.name());
    }
    
    
    public void showLayout(ResourceScopeType type) {
        layout.show(this, type.name());
    }
    
    

}
