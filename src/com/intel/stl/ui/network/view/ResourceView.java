/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 *  Archive Log:    Revision 1.9  2015/08/17 18:54:15  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/08/05 04:09:30  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - applied undo mechanism on Topology Page
 *  Archive Log:
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

import com.intel.stl.ui.network.ResourceScopeType;
import com.intel.stl.ui.network.ResourceSection;

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

    public void initializeViews(
            Map<ResourceScopeType, ResourceSection<?>> cardMap) {
        for (ResourceScopeType type : cardMap.keySet()) {
            add(cardMap.get(type).getView(), type.name());
        }

        layout.show(this, ResourceScopeType.ALL.name());
    }

    public void showLayout(ResourceScopeType type) {
        layout.show(this, type.name());
    }

}
