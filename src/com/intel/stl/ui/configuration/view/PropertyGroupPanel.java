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
 *  File Name: PropertyGroupPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.9.2.1  2015/08/12 15:27:09  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/05 19:09:23  jijunwan
 *  Archive Log:    fixed a issue reported by klocwork that is actually not a problem
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/22 02:15:33  jijunwan
 *  Archive Log:    1) abstracted property related panels to general panels that can be reused at other places
 *  Archive Log:    2) introduced renderer into property panels to allow customizes property render
 *  Archive Log:    3) generalized property style to be able to apply on any ui component
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/13 21:06:47  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/09 13:04:36  fernande
 *  Archive Log:    Adding IContextAware interface to generalize setting up Context
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/04 19:56:45  jijunwan
 *  Archive Log:    minor L&F adjustments on property viz
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/04 16:54:25  jijunwan
 *  Archive Log:    added code to support changing property viz style through UI
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/22 16:52:29  fernande
 *  Archive Log:    Closing the gaps between properties and sa_query
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/14 17:37:51  fernande
 *  Archive Log:    Closing the gap on device properties being displayed.
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.common.view.JCardView;
import com.intel.stl.ui.model.IPropertyCategory;
import com.intel.stl.ui.model.PropertyGroup;

public class PropertyGroupPanel<C extends IPropertyCategory<?>, G extends PropertyGroup<C>>
        extends JCardView<ICardListener> {

    private static final long serialVersionUID = 1L;

    protected JPanel totalPanel;

    private JPanel propCardPanel;

    private final PropertyVizStyle style;

    /**
     * Description:
     * 
     * @param style
     */
    public PropertyGroupPanel(PropertyVizStyle style) {
        this(style, "");
    }

    public PropertyGroupPanel(PropertyVizStyle style, String title) {
        super(title);
        this.style = style;
        // this is unnecessary, but can stop klocwork from complaining
        getMainComponent();
    }

    /**
     * @return the style
     */
    public PropertyVizStyle getStyle() {
        return style;
    }

    public void setModel(G model) {
        setTitle(model.getGroupName());
        propCardPanel.removeAll();
        int row = 0;
        for (C category : model.getPropertyCategories()) {
            addCategory(category, row);
            row++;
        }
        propCardPanel.repaint();
        validate();
    }

    private void addCategory(C category, int row) {
        Component categoryPanel = createCategoryPanel(category, style);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0;
        gc.gridy = row;
        gc.weightx = 1;
        int yPadTop = 0, yPadBtm = 2, xPadLeft = 0, xPadRight = 0;
        gc.insets = new Insets(yPadTop, xPadLeft, yPadBtm, xPadRight);
        propCardPanel.add(categoryPanel, gc);
    }

    protected Component createCategoryPanel(C category, PropertyVizStyle style) {
        PropertyCategoryPanel panel = new PropertyCategoryPanel(style);
        panel.setModel(category);
        return panel;
    }

    @Override
    public JComponent getMainComponent() {
        if (totalPanel == null) {
            totalPanel = new JPanel();
            totalPanel.setLayout(new BorderLayout());
            totalPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            propCardPanel = new JPanel();
            propCardPanel.setLayout(new GridBagLayout());
            propCardPanel.setBackground(UIConstants.INTEL_WHITE);
            totalPanel.add(propCardPanel, BorderLayout.NORTH);
        }
        return totalPanel;
    }

}
