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
 *  File Name: NavigationPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/04/03 21:06:23  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
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

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.ui.tabbedui.VerticalLayout;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.ComponentFactory;

public class NavigationPanel extends JPanel {
    private static final long serialVersionUID = -7750864974612798171L;

    private final Map<String, IconPanel> itemMap =
            new LinkedHashMap<String, IconPanel>();

    private final List<ChangeListener> listeners =
            new CopyOnWriteArrayList<ChangeListener>();

    public NavigationPanel() {
        super(new VerticalLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 10,
                UIConstants.INTEL_WHITE));
    }

    public void addItem(String name, Icon icon) {
        if (itemMap.containsKey(name)) {
            throw new IllegalArgumentException("Item '" + name
                    + "' alreday existed.");
        }

        IconPanel panel = new IconPanel(name, icon);
        itemMap.put(name, panel);
        add(panel);
    }

    public void selectItem(String name) {
        for (Entry<String, IconPanel> entry : itemMap.entrySet()) {
            IconPanel panel = entry.getValue();
            panel.setSelected(entry.getKey().equals(name));
        }
    }

    protected void fireSelection(String name) {
        IconPanel panel = itemMap.get(name);
        if (panel != null) {
            ChangeEvent event = new ChangeEvent(panel);
            for (ChangeListener listener : listeners) {
                listener.stateChanged(event);
            }
        }
    }

    public void addListener(ChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    public class IconPanel extends JPanel {

        private static final long serialVersionUID = -1275114280998145382L;

        private JLabel[] nameLabels;

        private final String name;

        public IconPanel(String name, Icon image) {
            super();
            this.name = name;
            initializePanel(name, image);
        }

        /**
         * @return the name
         */
        @Override
        public String getName() {
            return name;
        }

        protected void initializePanel(String title, Icon image) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            setBackground(UIConstants.INTEL_BACKGROUND_GRAY);
            setBorder(BorderFactory.createCompoundBorder(BorderFactory
                    .createEmptyBorder(2, 2, 2, 2), BorderFactory
                    .createCompoundBorder(BorderFactory
                            .createLineBorder(UIConstants.INTEL_GRAY),
                            BorderFactory.createEmptyBorder(4, 2, 2, 2))));

            JLabel lblIcon = new JLabel(image);
            lblIcon.setAlignmentX(0.5f);
            add(lblIcon);

            String[] lines = title.split(" ");
            nameLabels = new JLabel[lines.length];
            for (int i = 0; i < lines.length; i++) {
                nameLabels[i] =
                        ComponentFactory.getH4Label(lines[i], Font.BOLD);
                nameLabels[i].setHorizontalAlignment(JLabel.CENTER);
                nameLabels[i].setAlignmentX(0.5f);
                add(nameLabels[i]);
            }

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    fireSelection(name);
                }
            });
        }

        public void setSelected(boolean isSelected) {
            if (isSelected) {
                setBackground(UIConstants.INTEL_WHITE);
                for (JLabel label : nameLabels) {
                    label.setForeground(UIConstants.INTEL_BLUE);
                }
                setBorder(BorderFactory.createCompoundBorder(BorderFactory
                        .createEmptyBorder(2, 2, 2, 2), BorderFactory
                        .createCompoundBorder(BorderFactory.createMatteBorder(
                                1, 5, 1, 0, UIConstants.INTEL_BLUE),
                                BorderFactory.createEmptyBorder(4, 2, 2, 2))));
            } else {
                setBackground(UIConstants.INTEL_BACKGROUND_GRAY);
                for (JLabel label : nameLabels) {
                    label.setForeground(UIConstants.INTEL_GRAY);
                }
                setBorder(BorderFactory.createCompoundBorder(BorderFactory
                        .createEmptyBorder(2, 5, 2, 2), BorderFactory
                        .createCompoundBorder(BorderFactory
                                .createLineBorder(UIConstants.INTEL_GRAY),
                                BorderFactory.createEmptyBorder(4, 2, 2, 2))));
            }
        }
    }
}
