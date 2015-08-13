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
 *  File Name: ResourceAllView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7.2.1  2015/08/12 15:27:06  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/05 19:09:21  jijunwan
 *  Archive Log:    fixed a issue reported by klocwork that is actually not a problem
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/11/17 17:14:26  jijunwan
 *  Archive Log:    improved to support initializing property display style
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/22 02:21:27  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/24 19:14:08  rjtierne
 *  Archive Log:    Set visibility in showAll()
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/23 18:02:59  rjtierne
 *  Archive Log:    Removed call to setVisible() in showAll()
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/18 19:24:37  rjtierne
 *  Archive Log:    Removed Link page when node selected from topology page.
 *  Archive Log:    Added Link Statistics panel to the Node page.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/08 20:19:43  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: View for the main overview topology JCard displayed
 *  when no components have been selected on the topology graph   
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.network.view;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.configuration.view.IPropertyListener;
import com.intel.stl.ui.configuration.view.PropertiesPanel;
import com.intel.stl.ui.model.PropertySet;

public class ResourceAllView extends JSectionView<ISectionListener> {
    private static final long serialVersionUID = 5395968067795486597L;

    private PropertiesPanel<PropertySet<?>> mainPanel;

    public ResourceAllView(String title) {
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
        if (mainPanel == null) {
            mainPanel = new PropertiesPanel<PropertySet<?>>(false, true);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
            mainPanel.setMainPanelBackground(UIConstants.INTEL_WHITE);
        }
        return mainPanel;
    }

    public void addPropertyGroupPanel(Component groupPanel) {
        mainPanel.addPropertyGroupPanel(groupPanel);
    }

    public void setModel(PropertySet<?> model) {
        mainPanel.setModel(model);
    }

    public void clearPanel() {
        mainPanel.clearPanel();
    }

    public void setInitialStyle(boolean showBorder, boolean showAlternation) {
        mainPanel.setInitialStyle(showBorder, showAlternation);
    }

    public void setStyleListener(IPropertyListener listener) {
        mainPanel.setStyleListener(listener);
    }
}
